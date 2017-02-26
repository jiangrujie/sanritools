(function(){
  var pkg = regNamespace('business');
  var treeView = {
      dom:$('#menu'),
      init:function(height){
        var _this = this;
        /*
         * 构造 menu 的 children dom 
         * menus 子节点数组,
         * $parentDom,父节点 dom 元素
         */
        function buildMenuChildrenDom(menus,$parentDom){
          if(menus && menus.length > 0){
            for(var i=0;i<menus.length;i++){
              var currMenu = menus[i],
              $currDom = $('<li><a href="'+currMenu.linkUrl+'" aria-expanded="false" ><i class="fa '+currMenu.icon+'"></i>'+currMenu.text+'</a></li>').appendTo($parentDom);
              buildMenuChildrenDom(currMenu.children, $('<ul></ul>').appendTo($currDom));
            }
          }
        }
        
        //当菜单未加载时加载菜单
        if(!this.loaded){
          //请求菜单数据
          ajaxRequest({url:'/pageui/menu.json',type:'get',async:true},function(data){
            _this.dom.empty();
            buildMenuChildrenDom(data.menus,_this.dom);
            _this.loaded = true;
            
            _this.dom.metisMenu();
          });
        }
        //设置树高度
        this.dom.height(height - 1);//多出一像素,不知道为什么
      }
  };
  var pageIndex = {
    /**
     * 当窗口改变时或初始化时进行大小的计算,
     * 由 appbase.js relayout 方法回调
     */
    relayout:function(clientWidth,clientHeight){
      var innerWinHeight = clientHeight - $('.top').outerHeight();
      $('.page-inner-wrap').height(innerWinHeight);
      treeView.init(innerWinHeight);
      
      var mainWidth = clientWidth - treeView.dom.outerWidth();
      $('.page-main').width(mainWidth);
      
      var frameHeight = innerWinHeight - $('#breadcrumb').outerHeight();
      $('#mainFrame').height(frameHeight - 5);  //不知道怎么就多出 5 像素了
    }
  }
  pkg.pageIndex = pageIndex;
})(window.jQuery);