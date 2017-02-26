(function() {
  var pkg = regNamespace('business');
  function App() {}
  extend(App, AbstractPage);

  function initTimer(){
    //初始化日期时间
    var now = new Date(),
        time = formatTime(now.getTime(),'yyyy年MM月dd日 HH:mm:ss'),
        weekMirro = {1:'星期一',2:'星期二',3:'星期三',4:'星期四',5:'星期五',6:'星期六',7:'星期日'},
        week = weekMirro[now.getDay()];
    $('#nowtime').text(time);
    $('#week').text(week);
  }
  
  $.extend(App.prototype, {
    init : function() {
      initTimer();
      setInterval(initTimer, 1000);
    },
    relayout:function(clientWidth,clientHeight){
      var topHeight = $('.top').outerHeight(),
          treeviewWidth = $('#treeview').outerWidth(true),
          pathHeight = $('.page-main>.path').outerHeight(),
          treeviewHeight = clientHeight - topHeight,
          mainWidth = clientWidth - treeviewWidth,
          frameHeight = treeviewHeight - pathHeight;
      
      $('#treeview').height(treeviewHeight);
      $('.page-main').width(mainWidth);
      $('#mainframe').height(frameHeight - 5).width(mainWidth);
    }
  });
  pkg.App = new App();
})(window.jQuery)