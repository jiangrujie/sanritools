/**
 * Created by sanri on 2016/9/17.
 * based app method
 */
;(function($,global){
    /**
     * 获取项目根路径
     */
    function getRootPath(){
        var curWwwPath=top.document.location.href,
            pathName=top.document.location.pathname;

        var pos=curWwwPath.indexOf(pathName),
            localhostPaht=curWwwPath.substring(0,pos),
            projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);

        return(localhostPaht+projectName);
    }
    
    /**
     * 基础属性
     */
    var root = getRootPath();

    /**
     * 封装的 ajax 方法
     * 如果需跨域请求,可以设置这三个属性
     *  dataType:'jsonp',
     *  jsonpCallback:'sendRequestCallback',//这个默认是 jquery 随机字符串,可以写死,但写死不安全因为可仿后台了
     *  jsonp:'thisCallback',        //这个是参数名,后台要返回 thisCallback(jsonString) 形式,默认是 callback
     */
    function ajaxRequest(options,success,error){
        var options = $.extend({type:'post',
            async:false,
            contentType:'application/json',
            dataType:'json',
            error:error,
            success:success},options);
        if(options.url.startsWith('http')){  //如果以 http 打头,则为跨域请求
          if(options.isJsonp){                //使用 jsonp 跨域 
            options.dataType='jsonp';
          }
          //如果不使用 jsonp 跨域 ,则 chrome 浏览器 和后台都需要设置
          /*
           * 后台设置
           *   response.addHeader("Access-Control-Allow-Origin", "*");
           * chrome 设置
           *  目标加入 --disable-web-security
           */
        }else{
          options.url = root+options.url;   //本地请求
        }
        if(typeof options.data == 'object'){
          options.data = JSON.stringify(options.data);
        }
        try{
        	$.ajax(options);
        }catch(e){
        	logger.error(e,'json 请求出错');
        	if(e.stack){
        	  logger.error(e.stack, 'json 请求出错详细信息');
        	}
        }
    }

    /**
     * 日志打印
     * @type {{level: *, debug: logger.debug, info: logger.info, error: logger.error}}
     */
    var logger = {
        level:'debug',
        version:20160917,
        levelMap:{
            debug:1,
            info:2,
            error:3
        },
        debug:function(message,mark){
            myConsole(message,'debug',mark);
        },
        info:function(message,mark){
            myConsole(message,'debug',mark);
        },
        error:function(message,mark,callback){
            myConsole(message,'debug',mark);
            if(callback && $.isFunction(callback)){
                callback.call(this);
            }
        }
    };

    /**
     * 日志打印方法,
     * 当前级别小于设定级别,则不会打印
     */
    function myConsole(message,level,mark){
        if(logger.levelMap[level] < logger.levelMap[logger.level]){
            return ;
        }
        mark = mark || '消息';
        if(this.console){
            switch(level){
                case 'debug':
                case 'info':
                    console.log(mark+':');
                    console.log(message);
                    break;
                case 'error':
                    console.error(mark+':');
                    console.error(message);
                    break;
                default:
                    console.error('不支持的级别');
            }
            if(message && message.stack){
                console.log(message.stack);
            }
        }else if(JSON){
          //如果 JSON 存在,则可以使用 JSON.stringify(json 对象) 方法 TODO
          
        }else{
          //自定义 日志打印 TODO 
        }
    }

    /**
     * 事件注册方法
     * 入参格式
     * selector 和 parent 支持字符串(jquery 选择器)和对象(jquery 对象)
     * [{selector:jquery选择器,types:[事件1,事件2],handler:function,parent:jquery 选择器}] parent 如果存在的话,会注册动态事件
     */
    function regPageEvents(cfgArr){
        if(!cfgArr){
            return ;
        }
        while(cfgArr[0]){
            (function(cfg){
                if(typeof cfg.parent  == 'string'){
                  cfg.parent = $(cfg.parent);
                }
                if(typeof cfg.selector == 'string'){
                  //如果是动态事件,这里不能再取为对象了 fix bug at 2016/10/04
                  if(!cfg.parent){
                    cfg.selector = $(cfg.selector);
                  }
                }
                for(var i = 0, l = cfg.types.length; i < l; i++) {
                    if(cfg.parent){
                        //注册动态事件
                        cfg.parent.on(cfg.types[i],cfg.selector, function(e) {
                            if($.isFunction(cfg.handler)) {
                                cfg.handler.apply(this, arguments);
                                e.stopPropagation();    //停止冒泡
                            }
                        });
                        continue;
                    }
                    cfg.selector.bind(cfg.types[i], function(e) {
                        if($.isFunction(cfg.handler)) {
                            cfg.handler.apply(this, arguments);
                            e.stopPropagation();
                        }
                    });
                }
            })(cfgArr.shift());
        }
    }
    
    //解决严重 bug by sanri at 2016/11/27 ,因为命名空间是公共的每个页面如果一进来就调用这个方法的话,
    //那么当一个页面初始始的时候,注册到这个命名空间的其它页面也同时初始化了,其它页面重新初始可能导致问题
//    $(init);
//    $(window).resize(relayout);
//    
//    function init(){
//      var pkg = regNamespace('business');
//      $('input[type=text]').eq(0).focus();//初始化时第一个输入框聚集
//      for(page in pkg){
//        if(pkg[page] && pkg[page]['init'] && $.isFunction(pkg[page]['init']) ){
//          pkg[page]['init'].call(pkg[page]);
//        }
//      }
//      relayout();
//    }
//    /*
//     * 初始化和窗口大小改变时的自适应
//     * 对特殊标记的扫描 full-window,fit
//     */
//    function relayout(){
//    	clientWidth = window.document.documentElement.clientWidth;
//    	clientHeight = window.document.documentElement.clientHeight;
//
//    	$('body.full-window').css({
//    		width:clientWidth,
//    		height:clientHeight
//    	});
//    	$('body.full-window').find('.fit').each(function(){
//    		var $parent = $(this).parent();
//    		$(this).css({
//    			width:$parent.width(),
//    			height:$parent.height()
//    		});
//    	});
//    	//布局回调,子页面重用方法 relayout
//    	var pkg = regNamespace('business');
//    	for(page in pkg){
//    	  if(pkg[page] && pkg[page]['relayout'] && $.isFunction(pkg[page]['relayout']) ){
//    	    pkg[page]['relayout'].call(pkg[page],clientWidth,clientHeight);
//    	  }
//    	}
//    }
    
    /**
     * string 工具类
     * 作者:sanri
     * 时间:2016-8-30下午1:49:44
     * 问题:可能 ie 不兼容一些方法以后再说
     *  注:可能不兼容的方法 String.indexOf() String.lastIndexOf()  
     */
    var StringUtil = (function(){
      /*
       * 拆分点
       * elArray:Array 可能的拆分元素
       * director:boolean 方向
       *  true:
       *  false:从后往前找 default
       * 返回 -1 说明没有找到
       */
      function splitPoint(name,elArray,director){
        if(!elArray || !name){
          return -1;
        }
        for(var i=0;i<elArray.length;i++){
          var el = elArray[i];
          if(!director){
            var pos = name.lastIndexOf(el);
            if(pos == -1){
              continue;
            }
            return pos;
          }
          pos = name.indexOf(el);
          if(pos == -1){
            continue;
          }
          return pos;
        }
        return -1;
      }
      /*
       * 功能:basename 用于获取路径中的文件名称,不存在返回 null
       */
      function basename(path){
        if(path){
          var pos = splitPoint(path,['/','\\']);
          if(pos != -1){
            return path.substring(pos+1);
          }
        }
        return null;
      }
      /*
       * 功能:获取路径中的路径 
       */
      function dirname(path){
        if(path){
          var pos = splitPoint(path, ['/','\\']);
          if(pos != -1){
            return path.substring(0,pos)+'/';
          }
        }
        return null;
      }
      /*
       * 功能:获取前缀
       */
      function prefix(name){
        if(name){
          var pos = splitPoint(name, ['.']);
          if(pos != -1){
            return name.substring(0,pos);
          }
        }
        return null;
      }
      /*
       * 获取后缀
       */
      function suffix(name){
        if(name){
          var pos = splitPoint(name, ['.']);
          if(pos != -1){
            return name.substring(pos);
          }
        }
        return null;
      }
      /*
       * 把EXT_ORG_TYPE这种下划线格式的数据转换成extOrgType驼峰式
       * */
      function _2aB(name) {
        var result = '';
        // 快速检查
        if (name == '') {
          // 没必要转换
          return "";
        } else if (name.indexOf("_") == '-1') {
          // 不含下划线，全部转小写
          return name.toLowerCase();
        }
        // 用下划线将原始字符串分割
        var camels = name.split("_");
        for (var i = 0, len = camels.length; i < len; i++) {
          var camel = camels[i];
          // 跳过原始字符串中开头、结尾的下换线或双重下划线
          if (camel == '') {
            continue;
          }
          // 处理真正的驼峰片段
          if (result.length == 0) {
            // 第一个驼峰片段，全部字母都小写
            result += camel.toLowerCase();
          } else {
            // 其他的驼峰片段，首字母大写
            result += (camel.substring(0, 1).toUpperCase());
            result += (camel.substring(1).toLowerCase());
          }
        }
        return result.toString();
      }
      /*
       * 把extOrgType驼峰式的数据转换成EXT_ORG_TYPE下划线格式
       * */
      function aB2_(name) {
        //  var name="extOrgType";
        var result = '';
        if (name != '' && name.length > 0) {
          // 循环处理其余字符
          for (var i = 0; i < name.length; i++) {
            var s = name.substring(i, i + 1);
            // 在大写字母前添加下划线
            if (s == s.toUpperCase()) {
              result += ("_");
            }
            // 其他字符直接转成大写
            result += (s.toUpperCase());
          }
        }
        return result.toString();
      }
      
      return {
        basename:basename,
        dirname:dirname,
        suffix:suffix,
        prefix:prefix,
        _2aB:_2aB,
        aB2_:aB2_
      }
    })(this);
    
    /**
     * @author liuqing 2014-10-14修改，修复多个相同属性结果覆盖的问题
     * 
     * 将url请求参数序列化成json对象，如果有相同名称的参数将使用seperator分隔存放于一个属性中
     * @param param 需要处理的url请求字符串，形如：a=1&b=2&c=3
     * @param seperator 多个属性的分隔符，默认','
     * @param vFilter 每一组键值对的过滤器，用于值的预处理
     * @return 序列化后的对象
     */
    var serialize2Json = function(param, seperator, vFilter) {
      seperator = seperator || ",";
      param = (function() {
            try {
                return decodeURIComponent(param);
            } catch(ex) {
                return param;
            }
        })();
      vFilter = vFilter || $.noop;
      
      var ret = {},
        pairsArr = param.split('&'), pair, idx, key, value;
      $.each(pairsArr, function() {
        idx = this.indexOf("=");
        if(-1 === idx) {
          return true;
        }
        key = this.substring(0, idx);
        value = this.substring(idx + 1, this.length);
        
        value = vFilter.call(param, key, value) || value;
        
        if(ret[key]) {
          if(-1 === ret[key].indexOf(value)) {
            ret[key] = ret[key] + seperator + value;
          }
        } else {
          ret[key] = value;
        }
      });
      
      return ret;
    }
    
    /**
     * 模板生成字符串,暂时未挂载
     */
    function diyFormat(map,tmpl) {
      if(!tmpl || typeof tmpl != 'string' || !map ){return tmpl;}
      return tmpl.replace(/\{(\d+)\}/g, function(m, i) {
        return map[i];
      });
    }
    
    /**
     * 根据毫秒数格式化日期 
     */
    function formatTime(time, format){
        var t = new Date(time);
        var tf = function(i){return (i < 10 ? '0' : '') + i ;};
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
            switch(a){
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            }
        });
    };
    
    /**
     * 函数调用队列管理器
     * @author sanri
     * @param {[type]} arrFunc [description]
     */
    global.FuncQue = function (arrFunc) {
      this.arrFunc = arrFunc || [];
      this.index = 0;
    };
    global.FuncQue.prototype = {
      "addFunc": function (factory) {
        this.arrFunc.push(factory);
      },
      "doNext": function (target) {
        if (this.arrFunc.length > 0) {
          var factory = $.isFunction(this.arrFunc[0]) ? this.arrFunc[0] : this.arrFunc[0].func;
          var data = $.isFunction(this.arrFunc[0]) ? {} : this.arrFunc[0].data;
          this.arrFunc.splice(0, 1);
          var objQue = { "target": this, "callback": arguments.callee, "data": data, "index": this.index };
          factory.call(target, objQue);
        }
      },
      "exec": function (target) {
        this.doNext(target);
      }
    };
    
    /**
     * 命名空间方法 
     */
    function regNamespace(nameSpaceName){
      var nsArray = nameSpaceName.split('.'),
        sEval = "",
        sNS = "";
      for(var i = 0;i < nsArray.length; i++) {
        if(i != 0) {
          sNS += ".";
        }
        sNS += nsArray[i];            
        sEval += "if (typeof(" + sNS + ")=='undefined') {"
        + sNS+"=new Object(); "
        + "}";
      }
      if(sEval.length > 0) {     
        eval(sEval);
        return eval(sNS);
      }   
    }
    /**
     * url 工具类
     */
    function URLtool() {
      return {
        "parsUrl": function (url) {
          url = url || global.location.href;
          var a = document.createElement('a');
          //创建一个链接
          a.href = url ;
          return {
            source: url,
            protocol: a.protocol.replace(':', ''),
            host: a.hostname,
            port: a.port,
            query: a.search,
            params: (function () {
              var ret = {},
              seg = a.search.replace(/^\?/, '').split('&'),
              len = seg.length, i = 0, s;
              for (; i < len; i++) {
                if (!seg[i]) { continue; }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
              }
              return ret;
            })(),
            file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
            hash: a.hash.replace('#', ''),
            path: a.pathname.replace(/^([^\/])/, '/$1'),
            relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
            segments: a.pathname.replace(/^\//, '').split('/')
          };
        },
        /**
         * 获取url后面的参数
         * @param  {[type]} url [可以为空]
         */
        "getUrlParam": function (url) {
          var objUrl = url ? this.parsUrl(url) : this.parsUrl(window.location.href);
          return objUrl.params || "";
        },
        /**
         * 根据key获取url后面参数的value
         * @param  {[type]} key [description]
         * @param  {[type]} url [可以为空]
         */
        "getValueByUrlKey": function (key, url) {
          var objUrlParam = this.getUrlParam(url);
          return objUrlParam[key] || "";
        }
      };
    }
    
    /**
     * 抽象页面,所有的页面都需要继承此页面,
     * 然后只给出一个实例放入命名空间
     */
    function AbstractPage(context){
      this.context = context;
      this.regObj = {};
      this.requestParam = URLtool().parsUrl().params;
      this.root = root;
    }
    /**
     * 页面的公共方法 
     */
    $.extend(AbstractPage.prototype,{
      
    });
    /**
     * 页面继承公共方法
     * 使用方法
     * modulName: 模块名
     * 	Child: 子类
     * 	extend.call(child,modulName,Child)
     */
    function ExtendPage(modulName,Child) {
  　　　　var F = function(){},_this = this;
  　　　　F.prototype = AbstractPage.prototype;
  　　　　Child.prototype = new F();
  　　　　Child.prototype.constructor = Child;
  　　　　Child.uber = AbstractPage.prototype;
  		
  		AbstractPage.call(this);
  		//加上 top. 解决没有注册到同一 window ,导致组件找不到
  		var pkg = top.regNamespace('business');
  		pkg[modulName] = this;
  		
  		$(init);$(window).resize(relayout);
  		
  		/*
  		 * 页面初始化
  		 */
  		function init(){
  			if(_this.init && $.isFunction(_this.init)){
  				_this.init.call(_this);
  			}
  			relayout.call(_this);
  		}
  		
  		/*
  		 * 页面重置大小时调用
  		 */
  		function relayout(){
  			var clientWidth = window.document.documentElement.clientWidth,
  					clientHeight = window.document.documentElement.clientHeight;
  			/*
         * 初始化和窗口大小改变时的自适应
         * 对特殊标记的扫描 full-window,fit
         */
        $('body.full-window').css({
          width:clientWidth,
          height:clientHeight
        });
        $('body.full-window').find('.fit').each(function(){
          var $parent = $(this).parent();
          $(this).css({
            width:$parent.width(),
            height:$parent.height()
          });
        });
  			if(_this.relayout && $.isFunction(_this.relayout)){
  				_this.relayout.call(_this,clientWidth,clientHeight);
  			}
  			//事件绑定
  			if(_this.bindEvents && $.isFunction(_this.bindEvents)){
  				_this.bindEvents.call(_this);
  			}
  		}
  　　}
    /**
     * 外宽度默认包括元素的内边距(padding)、边框(border)，
     * 但不包括外边距(margin)部分的宽度。你也可以指定参数为true，以包括外边距(margin)部分的宽度
     * 现在默认是设置为 box-sizing为 border box ,设置的宽度默认就是包含了 padding 和 border 的
     */
    
    /**
     * 打开对话框 依赖 layer
     * 通用:iframe 形式
     * 如果 title 是个对象,则后面参数无效, title 将做为 layer 对话框选项
     */
    function openDialog(title,url,params,width,height,ok,close){
      var dialogDefault = {type:2,maxmin:true,shade: 0.8,btn:['确定','取消'],shadeClose: true};
      
      title = title || '标题';
      url = url ? root + url : location.href;
      width = width || '90%';
      height = height || '90%';
      var options = {};
      
      if(title && typeof title=='object'){
        options = $.extend({},dialogDefault,title);
      }else{
        options = $.extend({},dialogDefault,{
          title:title,
          content:url,
          yes:ok,
          area:[width,height],
          btn2:function(index, layero){
            if(close && $.isFunction(close)){
              close.call(this,index,layero);
            }
          }
        });
      }
      if(layer){
        return layer.open(options);
      }else{
        logger.error('请加入 layer 依赖','错误');
      }
    }
    
    /**
     * 页面存储处理
     * 需后续优化
     */
    var cacheStorage = (function(){
    	if(!sessionStorage){
    		logger.info('浏览器不支持 sessionStorage');
    		return ;
    	}
    	/**
    	 * key:String
    	 * value:对象或数组 
    	 */
    	function addLocal(key,value){
    		localStorage.setItem(key,JSON.stringify(value));
    	}
    	function getLocal(key){
    		return JSON.parse(localStorage.getItem(key));
    	}
    	
    	return {
    		addLocal:addLocal,
    		getLocal:getLocal
    	}
    })()
    $.extend(this,{
        //属性
        root:root,
        logger:logger,
        //方法
        ajaxRequest:ajaxRequest,
        regPageEvents:regPageEvents,
        formatTime:formatTime,
        diyFormat:diyFormat,
        regNamespace:regNamespace,
        ExtendPage:ExtendPage,
        serialize2Json:serialize2Json,
        openDialog:openDialog,
        //类
        StringUtil:StringUtil,
        FuncQue:FuncQue,
        URLtool:URLtool,
        AbstractPage:AbstractPage,
        cacheStorage:cacheStorage
    });
    
    //加载进度条配置
    var spinOpts = {
    		lines: 15 // The number of lines to draw
    		, length: 30 // The length of each line
    		, width: 6 // The line thickness
    		, radius: 20 // The radius of the inner circle
    		, scale: 1 // Scales overall size of the spinner
    		, corners: 1 // Corner roundness (0..1)
    		, color: '#222' // #rgb or #rrggbb or array of colors
    		, opacity: 0.45 // Opacity of the lines
    		, rotate: 0 // The rotation offset
    		, direction: 1 // 1: clockwise, -1: counterclockwise
    		, speed: 0.7 // Rounds per second
    		, trail: 37 // Afterglow percentage
    		, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
    		, zIndex: 2e9 // The z-index (defaults to 2000000000)
    		, className: 'spinner' // The CSS class to assign to the spinner
    		, top: '50%' // Top position relative to parent
    		, left: '50%' // Left position relative to parent
    		, shadow: false // Whether to render a shadow
    		, hwaccel: true // Whether to use hardware acceleration
    		, position: 'absolute' // Element positioning
    }
    
    /*
     * mask 依赖于 spin 是一个插件来实现加载动画
     * 而 addLoadingMask 是自已写的一个加载动画 依赖于图片 loding.gif
     */
    $.extend(this,{
    	//给目标元素加入加载中的遮罩
    	mask:function($target){
    		$target = $target || $('body');
    		$target.spin(spinOpts);
    	},
    	//移除加载中的遮罩
    	unmask:function($target){
    		$target = $target || $('body');
    		$target.spin(false);
    	},
    	addLoadingMask:function($target){
  	    var $mask = $("<div class=\"body-mask\"></div>");
  	    //先记住原来的定位结构
  	    $mask.data('position',$mask.css('position'));
  	    $target.css('position','relative');
  	    
  	    $mask.appendTo($target);
  	    var msg = $("<div class=\"body-mask-msg\" style=\"display:block\"></div>").html("正在加载，请稍等。。。").appendTo($target);
  	    msg.css("left", ($target.outerWidth() - 182) / 2);		//182 是图片加文本宽度			
    	},
    	removeLoadingMask:function($target){
    		var $mask = $target.children("div.body-mask");
    		//还原定位结构
    		$target.css('position',$mask.data('position'));
    		
    		$target.children("div.body-mask-msg").remove();
    		$mask.remove();
    	}
    });
})(window.jQuery,this);