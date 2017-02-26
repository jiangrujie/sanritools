(function(){
  var pkg = top.regNamespace('business');
  pkg.greenedit = {};
  $(init);
  function init(){
    render();
    $.validator.setDefaults({
      highlight: function(e) {
        $(e).closest(".form-row").removeClass("has-success").addClass("has-error")
      },
      success: function(e) {
        e.closest(".form-row").removeClass("has-error").addClass("has-success")
      },
      errorElement: "span",
      errorPlacement: function(e, r) {
        e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
      },
      errorClass: "help-block",
      validClass: "help-block"
    });
   var validate =  $('#greeneditform').validate({
      rules:{
        name:{
          required:true,
          minlength:2
        },
        taste:'required',
      },
      messages:{
        name:'请输入菜名,至少两个字'
      }
    }); 
   pkg.greenedit.validate = validate;
   bindEvents();
  }
  
  function render(){
    $('#greeneditform').find('select[dict]').each(function(){
      var dictName = $(this).attr('dict');
      initDropDown(top.dicts[dictName],$(this));
      $(this).chosen({
        no_results_text:'没有搜索结果'
      });
    });
    //加载所有的食物
    ajaxRequest({url:'/greens/listAllFood'},function(data){
      if(data && data.length > 0){
        var $allGreen = $('#allgreens').empty();
        for(var i=0;i<data.length;i++){
          $allGreen.append('<li value="'+data[i].id+'"><a href="#">'+data[i].name+'</a></li>')
        }
      }
    });
  }
  
  function bindEvents(){
    var Events = [{selector:'#allgreens>li',types:['click'],handler:function(e){
      $(e.target).closest('li').toggleClass('selected');
    }},
    {parent:'#greeneditform',selector:'.input-group>.addQueue',types:['click'],handler:function(e){
      var $el = $(e.target).closest('.input-group');
      $el.clone().insertAfter($el);
      $el.find('i').removeClass().addClass('fa fa-trash');
    }}];
    
    regPageEvents(Events);
  }
  
  function initDropDown(dict,$select){
    if(dict){
      for(var key in dict){
        $select.append('<option value="'+key+'">'+dict[key]+'</option>');
      }
    }
  }
})(window.jQuery)