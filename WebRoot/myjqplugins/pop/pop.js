;(function($,window,undefined){
  var Pop = (function(){
    var instance = undefined,
        DEFAULT = {
          width:260,
          delay:10000,
          apearTime:1000,
          hideTime:500,
          title:'',
          subTitle:'',
          url:'',
          content:'',
          onOpened:function(option,$panel){
          },
          onClosed:function(option,$panel){
          }
          
        };
    function PopWin(options){
      var opts = $.extend({},DEFAULT,options),htmlCodes = [];
      
      htmlCodes.push('<div class="pop-container">');
      htmlCodes.push('<div class="pop-header clearfix">');
      htmlCodes.push('<a href="#" class="close"><span >关闭</span><i class="fa fa-remove"></i></a>');
      htmlCodes.push('<h2 class="title">'+opts.title+'</h2></div>');
      htmlCodes.push('<div class="pop-content">');
      htmlCodes.push('<h3 class="sub-title">'+opts.subTitle+'</h3>');
      htmlCodes.push('<div class="content">'+opts.content+'</div></div>');
      htmlCodes.push('<div class="pop-footer clearfix"><a href="#" class="see-more">查看 »</a></div></div>');
      
      var $popPanel = $(htmlCodes.join('')).appendTo($('body'));
      state = $('body').data('pop',{opts:opts,popPanel:$popPanel});
      $popPanel.hide();//构造后隐藏
      
      $popPanel.append('<iframe  src="about:blank" frameborder=0 style="height: 100%; width: 100%; position: absolute; left: 0px; z-index: -1; top: 0px; scrolling: no"></iframe>');
      
      //如果有副标题,则内容首行缩进,否则没有
      if(opts.subTitle != ''){
        $popPanel.find('.content').css({
          textIndent:24
        });
      }else{
        $popPanel.find('h3.sub-title').hide();
      }
    
    }

    $.extend(PopWin.prototype,{
      options:function(){
        var state = $('body').data('pop');
        if(state){
          return state.opts;
        }
        return null;
      },
      modify:function(options){
        var state = $('body').data('pop');
        if(state == null){
          return ;
        }
        var opts = $.extend(state.opts,options),
            $popPanel = state.popPanel;
        //只有title ,subTitle ,content,url 界面元素需要修改
        $popPanel.find('h2.title').html(opts.title);
        if(opts.subTitle != ''){
          $popPanel.find('h3.sub-title').html(opts.subTitle);
          $popPanel.find('.content').css({
            textIndent:24
          });
          $popPanel.find('h3.sub-title').show();
        }else{
          $popPanel.find('.content').css({
            textIndent:0
          });
          $popPanel.find('h3.sub-title').hide();
        }
        $popPanel.find('.content').html(opts.content);
        if(opts.url == ''){
          return ;
        }
        if(typeof opts.url == 'string'){
          $popPanel.find('a.see-more').attr('href',opts.url).attr('target','_blank');
          return ;
        }
        if(typeof opts.url == 'function'){
          $popPanel.find('a.see-more').unbind('click').bind('click',opts.url);
        }
      },
      open:function(){
        var state = $('body').data('pop');
        if(state == null){
          return ;
        }
        var opts = state.opts,
            $popPanel = state.popPanel;
        openPopPanel();
        if(opts.onOpened && $.isFunction(opts.onOpened)){
          opts.onOpened.call(instance,opts,$popPanel);
        }
        
      },
      close:function(){
        var state = $('body').data('pop');
        if(state == null){
          return ;
        }
        var opts = state.opts,
            $popPanel = state.popPanel;
        $popPanel.hide();
        if(opts.onClosed && $.isFunction(opts.onClosed)){
          opts.onClosed.call(instance,opts,$popPanel);
        }
      }
    });
    
    /*
     * 绑定事件
     */
    function bindEvents(){
      var state = $('body').data('pop'),
          opts = state.opts,
          $popPanel = state.popPanel;
      $('a.close',$popPanel).bind('click',instance.close);
      if(opts.url == ''){
        return ;
      }
      if(typeof opts.url == 'string'){
        $popPanel.find('a.see-more').attr('href',opts.url).attr('target','_blank');
        return ;
      }
      if(typeof opts.url == 'function'){
        $popPanel.find('a.see-more').bind('click',opts.url);
        return ;
      }
      throw new Error('不支持的 url 或事件:'+opts.url);
    }

    /*
     * 打开面板
     */
    function openPopPanel(){
      var state = $('body').data('pop');
      if(state == null){
        return ;
      }
      var opts = state.opts,
          $popPanel = state.popPanel;
      $popPanel.slideDown(opts.apearTime).delay(opts.delay).fadeOut(opts.hideTime);
    }

    return {
      getInstance:function(options){
        if(!instance){
          instance = new PopWin(options);
          bindEvents();
        }else{
          instance.modify(options);
        }
        return instance;
      }
    }
    
  })();
  window.Pop = Pop;
})(window.jQuery,window)