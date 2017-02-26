(function(){
  var dicts = {};
  var pkg = top.regNamespace('business');
  $(init);
  function init(){
    render();
    top.dicts = dicts;
    bindEvents();
  }
  
  /**
   * 界面渲染
   */
  function render(){
    var dictNames = {};
    ajaxRequest({url:'/greens/dictNames'},function(names){
      dictNames = names;
    });
    ajaxRequest({url:'/greens/dicts'},function(_dicts){
      var $filter = $('#filter'); 
      dicts = _dicts;
      if(_dicts){
        $filter.empty();
        for(var dictName in _dicts){
          var dictItems = _dicts[dictName],
              $filterItem = $('<div class="filter-element"><label>'+dictNames[dictName]+':</label></div>').appendTo($filter),
              $items = $('<ul class="clearfix"></ul>').appendTo($filterItem);
           if(dictItems){
             for(var dictCode in dictItems){
               $items.append('<li value="'+dictCode+'">'+dictItems[dictCode]+'</li>')
             }
           }
        }
      }
    });
    ajaxRequest({url:'/greens/listAllGreens'},function(greenes){
      var $greenList = $('#greenList');
      $greenList.empty();
      var tastes = dicts['tastes'],
          cuisines = dicts['cuisines'];
      if(greenes && greenes.length>0){
        for(var i =0;i<greenes.length;i++){
          $greenList.append('<li><a href="#"><div class="img-box"><img src="'+greenes[i].image+'"></div><p class="green-name">'+greenes[i].name+'</p><span class="cuisines">菜系:'+cuisines[greenes[i].cuisines]+'</span> <span class="taste">味道:'+tastes[greenes[i].taste]+'</span></a></li>');
        }
      }
    });
  }
  
  /**
   * 事件绑定
   */
  function bindEvents(){
    var dialogDefault = {type:2,maxmin:true,shade: 0.8,btn:['确定','取消'],area: ['600px', '90%'],shadeClose: true,btn2:function(index, layero){}}
    var Events = [{selector:'.addFood',types:['click'],handler:addFood},
                  {selector:'.addGreen',types:['click'],handler:addGreen}];
    regPageEvents(Events);
    
    function addFood(){
      layer.open($.extend({},dialogDefault,{
        title: '增加食物',
        content: root+'/greensclient/foodedit.html',
        yes:function(index, layero){
          var $doc = layero.find('iframe').contents(),
              childFormValidate = pkg.foodedit.validate;
          if(childFormValidate.checkForm()){
            ajaxRequest({url:'/greens/addFood',data:serialize2Json($doc.find('form').serialize())});
            layer.close(index);
            return ;
          }
          childFormValidate.showErrors();
        },
       }));
    }
    
    function addGreen(){
      layer.open($.extend({},dialogDefault,{
        title: '增加菜',
        content: root+'/greensclient/greenedit.html',
        yes:function(index, layero){
          var $doc = layero.find('iframe').contents(),
              childFormValidate = pkg.greenedit.validate;
          if(childFormValidate.checkForm()){
            $doc.find('form')[0].submit();
            layer.close(index);
            return ;
          }
          childFormValidate.showErrors();
        },
       }));
    }
  }
})(window.jQuery);