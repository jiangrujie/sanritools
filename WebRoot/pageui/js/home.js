(function(){
  $(init);
  
  function init(){
    bindEvents();
  }
  
  function bindEvents(){
    var Events = [{selector:'.panel-more .detail',types:['click'],handler:function(e){
      var $target = $(e.target);
      var href = $target.attr('href');
      top.mainFrame.src=href;
    }}]
    regPageEvents(Events);
  }
})(window.jQuery)