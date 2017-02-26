(function($){
  var TreeView = function(){}
  $.extend(TreeView.prototype,{
    url:'/pageui2/menu.json',
    dom:$('#treeview'),
    init:function(){
      var menuData = this.loadMenu();
      this.render(menuData);
      this.bindEvents();
    },
    loadMenu:function(){
      var menus = {};
      ajaxRequest({url:this.url},function(data){
        menus =  data.menus;
      });
      return menus;
    },
    render:function(menuData){
      buildMenu(menuData,this.dom);
      
      /*
       * 菜单构建
       */
      function buildMenu(menus,parent){
        if(menus && menus.length>0){
          for(var i=0;i<menus.length;i++){
            var $currMenu = $('<li treeId="'+menus[i].id+'" class="level'+i+'"><a href="'+menus[i].linkUrl+'"><span iconCls="'+menus[i].iconCls+'" class="node-icon '+menus[i].iconCls+'"></span><span class="node-name">'+menus[i].text+'</span></a></li>').appendTo(parent);
            buildMenu(menus[i].children, $('<ul class="submenu" status="unexpand" ></ul>').appendTo($currMenu));
          }
        }
      }
    },
    /**
     * 
     * @param expandFlag 
     *  true: 表示展开
     *  flase: 折叠 
     */
    expandAll:function(expandFlag){
      
    },
    /**
     * 
     * @param id 结点标记
     * @param expandFlag 
     *  true: 表示展开
     *  flase: 折叠 
     */
    expandNode:function(id,expandFlag){
      var $currMenu = this.dom.find('li[treeid='+id+']');
      if(expandFlag){
        $currMenu.children('ul.submenu').show().attr('status','expand');
      }else{
        $currMenu.children('ul.submenu').hide().attr('status','unexpand');
      }
    },
    toggleNode:function(id){
      var $currMenu = this.dom.find('li[treeid='+id+']'),
          status = $currMenu.children('ul.submenu').attr('status');
      if(status == 'unexpand'){
        this.expandNode(id, true);
      }else{
        this.expandNode(id, false);
      }
    },
    treeNodeClick:function(e){
      var $a = $(e.target).closest('a'),
          href = $a.attr('href');
      $('#mainframe')[0].src=root+href;
    },
    bindEvents:function(){
      var _this = this;
      var Events = [{parent:'.treeview',selctor:'li>a',types:['click'],handler:treeNodeClick}]
      regPageEvents(Events);
      /**
       * 树结点点击 
       * @param e
       */
      function treeNodeClick(e){
        e.preventDefault();
        var $currMenu = $(e.target).closest('li'),
            treeId = $currMenu.attr('treeid'),
            childMenu = $currMenu.children('ul.submenu');
        $currMenu.siblings().removeClass('selected');
        if(childMenu.find('li').size() > 0){
          _this.toggleNode(treeId);
          childMenu.find('li:first').addClass('selected');
          return ;
        }
        $currMenu.addClass('selected');
        var class_ = $currMenu.attr('iconcls'),prefix = '';
        if(class_ && class_ != ''){
          prefix = class_.split('_')[0];
          $currMenu.removeClass(class_).addClass(prefix+'_hover').attr('iconcls',prefix+'_hover');
        }
        if(_this.treeNodeClick && $.isFunction(_this.treeNodeClick)){
          _this.treeNodeClick.call(_this,e);
        }
      }
    }
  });
  
  var treeview = new TreeView();
  treeview.init();
})(window.jQuery)