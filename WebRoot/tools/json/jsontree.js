(function(){
  function JsonPage(){}
  /**
   * 日志区块
   */
  var log = (function(){
    var logList = [],$logDom = $('ul#log'),timeFormat = 'yyyy-MM-dd HH:mm:ss',
    threshold='warning',levelMap={debug:1,info:2,warning:3,error:4,success:5};
    
    /**
     * 初始化方法
     */
    function init(){
      $('#levelFilter').val(threshold);     //初始时选中默认等级 
      $('#levelFilter').chosen({
        disable_search_threshold : 10,
        no_results_text : "没有数据",
        width:'120px'
      });
      bindEvents();
      //增加示例
      addLog('success', '欢迎使用三日日志器');
    }
    /**
     * 清空日志
     */
    function clean(){
      logList.length = 0;
      $logDom.empty();
    }
    /*
     * 添加日志
     */
    function addLog(level,message){
      var logJson = {},timestamp = new Date().getTime();;
      logJson.level = level;
      logJson.time = formatTime(timestamp,timeFormat);
      logJson.index = logList.length;
      logJson.message = message.toString();
      
      logList.push(logJson);
      domAddLog(logJson);
    }
    /*
     * 渲染指定日志
     * logs : 指定的日志列表
     */
    function render(logs){
      if(logs && logs.length > 0){
        $logDom.empty();
        for(var i=0;i<logs.length;i++){
          domAddLog(logs[i]);
        }
      }
    }
    
    /*
     * 只对 dom 进行操作,添加日志
     */
    function domAddLog(logJson){
      if(levelMap[logJson.level] < levelMap[threshold]){return ;}
      var elements = [];
      elements.push('<span class="linenum">'+logJson.index+'</span>');
      elements.push('<span class="time">'+logJson.time+'</span>');
      elements.push('<span class="level">'+logJson.level+'</span>');
      elements.push('<span class="message" title="'+logJson.message+'">'+logJson.message+'</span>')
      $logDom.append('<li class="'+logJson.level+'" level="'+logJson.level+'" index="'+logJson.index+'">'+elements.join('')+'</li>');
      
      //让消息滚动到最后面
      $logDom[0].scrollTop = $logDom[0].scrollHeight; 
    }
    /**
     * 事件绑定
     */
    function bindEvents(){
      var EVENTS = [{selector:'#logSearch input',types:['keyup','blur'],handler:searchLog},
                    {selector:'#logSearch .search',types:['click'],handler:searchLog},
                    {selector:'#levelFilter',types:['change'],handler:filterLog}];
      
      regPageEvents(EVENTS);
      /**
       * 日志搜索
       */
      function searchLog(e){
        var $target = $(e.target);
        if(!$target.is('input')){
          $target = $target.closest('.search-box').find('input');
        }
        var value = $target.val(),selectLog = [];
        if(logList && logList.length > 0){
          for(var i=0;i<logList.length;i++){
            if(logList[i].message.indexOf(value) != -1){
              selectLog.push(logList[i]);
            }
          }
          render(selectLog);
        }
      }
      
      /**
       * 日志过滤
       */
      function filterLog(evt, params){
        threshold = params.selected;
        if(logList && logList.length > 0){
          var selectLog = [];
          for(var i=0;i<logList.length;i++){
            if(levelMap[logList[i].level] >= levelMap[threshold]){
              selectLog.push(logList[i]);
            }
          }
          render(selectLog);
        }
      }
    }
    
    return {
      clean:clean,
      init:init,
      error:function(message){addLog('error', message)},
      info:function(message){addLog('info', message)},
      success:function(message){addLog('success', message)},
      warning:function(message){addLog('warning', message)},
      debug:function(message){addLog('debug', message)}
    }
  })();
   /**
   * json 处理模块
   */
  var jsonHandle = (function(hlog){
  	/*
  	 * 初始化时为 likeJson 处理模式
  	 */
  	var mode = 'likeJson';		//现在的处理模式
  	
    /*
     * json 构建 
     */
    function buildJson(){
       if(!this.original.jsonString){
         hlog.error('没有传入 json 字符串');
       }
       try{
         if(this.original.type == 'noStandard'){
//        	 logger.debug(this.mode,'现在的处理模式');
           hlog.info('保存原始类 json 串')
           this.original.pre = this.original.jsonString;
           hlog.info('处理成标准 json 串');
           switch(this.mode){
           case 'mapJson':
          	 //TODO 暂时先把 = 替换成 : 后继续
          	 this.original.jsonString = this.original.jsonString.replace(/=/g,':');
           case 'likeJson':
          	 //不标准的先转换成标准的 
             //空数据暂时转换成 $nbsp$ ,后面再转回来 解决空数据 bug at 2016/11/20 by sanri
             this.original.process = this.original.jsonString.replace(/(\'\')|(\"\")/g,'"$nbsp$"');
             this.original.process = this.original.process.replace(/[\r\n\s\'\"]/g,''); 
             //TODO 当数据中有 [] {} : , 等特殊字符时,无法处理
             this.original.jsonString = this.original.process.replace(/([^:\[\]\{\},]+)/g,'"$1"');
             //去掉之前专门为 "" 数据加的标记
             this.original.jsonString = this.original.jsonString.replace('$nbsp$','');
          	 break;
           case 'keyvalueJson':
          	 var jsonObj = serialize2Json(this.original.jsonString);
          	 this.original.jsonString = JSON.stringify(jsonObj);
          	 break;
           }
          
         }
         hlog.info('构建 json 对象');
         this.standardJson = JSON.parse(this.original.jsonString);
         this.original.isHandle = true;
         hlog.success('构建成功');
       }catch(e){
//         logger.error(e);
         hlog.error(e);
         //可能是没有加 {} ,两边加上 {} 再试一次
         hlog.warning('构建失败,将在两边加上 {} 再试一次');
         try{
           this.original.jsonString = '{'+this.original.jsonString+'}';
           this.standardJson = JSON.parse(this.original.jsonString);
           this.original.isHandle = true;
           hlog.success('构建成功');
         }catch(e){
           logger.error(e);
           hlog.error(e);
         }
       }
    }
    
    return {
      standardJson:{},
      original:{},
      mode:mode,
      setNoStandardJson:function(_nostandardJson){
        this.original.jsonString = _nostandardJson;
        this.original.isHandle = false;
        this.original.type='noStandard';
      },
      setStandardJson:function(_standardJson){
        this.original.jsonString = _standardJson;
        this.original.isHandle = false;
        this.original.type='standard';
      },
      /**
       * json 对象和字符串之间的转换方法
       * @param json_ 如果提供 json 对象,则转成字符串;如果提供字符串,则转换成 json 对象
       * @returns
       */
      convert:function(json_){
        if(typeof json_ == 'object'){
          return JSON.stringify(json_);
        }
        return JSON.parse(json_);
      },
      buildJson:buildJson
    }
  })(log);
  
  
  /**
   * jsontree 区块
   */
  var jsonTree = (function(handle,hlog){
  	var $nostandardJson = $('#nostandardjson'),
  			$standardJson = $('#standardjson'),
  			$treeDom = $('#tree');
  	
  	//示例数据
  	var example = {
  			likeJson:"m:[{ab:'大黄',cd:'李遥'},{sanri:{name:'三日',qq:2441719087}}]",
  			mapJson:'{m=[{sanri={name=三日, qq=2441719087}}, {ab=大黄, cd=李遥}]}',
  			keyvalueJson:'m=1&ab=大黄&cd=李遥'
  	}
  	/**
  	 * 初始化例子 json
  	 */
  	function init(){
  		/*
  		 * 初始化 radio  
  		 */
  		$('#inputTypeCmd>.radio-item>input[type=radio]').iCheck({
  			checkboxClass: 'icheckbox_square-green',
  			radioClass: 'iradio_square-green'
  		});
//  	  var exampleJson = "m:[{ab:'大黄',cd:'李遥'},{sanri:{name:'三日',qq:2441719087}}]";
  		$nostandardJson.val(example.likeJson);
  		rebuildNoStandardJson();
  		bindEvents();
  	}
  	/**
  	 * 根据非标准 json 重建 json 树视图
  	 */
  	function rebuildNoStandardJson(){
  	  hlog.info('开始处理 json 串');
  	  var noStandardJsonVal = $nostandardJson.val();
      handle.setNoStandardJson(noStandardJsonVal);
      buildTreeView();
      $standardJson.val(handle.original.jsonString);
  	}
  	/**
  	 * 重建标准 json 串
  	 */
  	function rebuildStandardJson(){
  	  var jsonStr = $standardJson.val();
      handle.setStandardJson(jsonStr);
      buildTreeView();
  	}
  	/**
  	 * 获取  json 
  	 */
  	function getJson(){
  	  return this.json;
  	}
  	/*
  	 * 从树结构获取 json 字符串
  	 * 思路:从原来的 json 结构判断是数组还是对象
  	 */
  	function refreshJson(){
  	  var jsonObj = {};
  	  if($.isArray(this.json)){
  	    jsonObj = [];
  	  }
  	  buildJsonObj(jsonObj,$treeDom);
  	  this.json = jsonObj;
  	  return jsonObj;
  	 /*
  	  * 构建 json 对象
  	  */
  	 function buildJsonObj(parent,$root){
  	   $root.children('li').each(function(i){
  	     var $currentNode = $(this).children('.tree-node'),
  	       select = $currentNode.find('input:checkbox').prop('checked');
  	     if(!select){return true;}//如果没有选中,继续下一次循环
  	     var key = $.trim($currentNode.find('.key').val()),
  	       value = $.trim($currentNode.find('.value').val()),
  	       childJson = undefined,complex = false;
  	     if(value.indexOf('[') != -1){
           var childJson = [];
           complex = true;
         }else if(value.indexOf('{') != -1){
           var childJson = {};
           complex = true;
         }
  	     //如果子结点是复杂类型,需要继续重建子 json 
  	     if(complex){
  	       buildJsonObj(childJson,$(this).children('ul'));
         }
  	     //设置值
         if($.isArray(parent)){
           if(complex){
             parent.push(childJson);
           }else{
             parent.push(value);
           }
         }else {
           if(complex){
             parent[key] = childJson;
           }else{
             parent[key] = value;
           }
         }
  	   });
  	 }
  	}
  	/*
  	 * 树事件,输入框和树进行绑定属于树
  	 */
  	var modeMirro = {likeJson:'类json',mapJson:'map 数据',keyvalueJson:'keyValue 数据'};
  	function bindEvents(){
  	  var _this = this;
  	  var Event = [{selector:$nostandardJson,types:['blur'],handler:nostandardJsonBlur},
                   {selector:$standardJson,types:['blur'],handler:standardJsonBlur},
                   {parent:'#tree',selector:'input:not(:checkbox)',types:['blur','keyup'],handler:jsonRebuild}];
  	  regPageEvents(Event);
  	  
  	  $('#tree').on('ifChecked','input:checkbox',jsonRebuild); 
  	  $('#tree').on('ifUnchecked','input:checkbox',jsonRebuild); 
  	  $('#inputTypeCmd input:radio').on('ifChecked',switchMode);
  	  /*
  	   * 切换 json 输入模式
  	   */
  	  function switchMode(e){
  	  	var $target = $(e.target),
  	  		mode = $target.val();
  	  	//写放默认示例
  	  	switch(mode){
  	  	case 'likeJson':
  	  		$nostandardJson.val(example.likeJson);
  	  		break;
  	  	case 'mapJson':
  	  		$nostandardJson.val(example.mapJson);
  	  		break;
  	  	case 'keyvalueJson':
  	  		$nostandardJson.val(example.keyvalueJson);
  	  		break;
  	  	}
  	  	log.success('模式已成功切换成: '+modeMirro[mode]);
  	  	handle.mode = mode;
  	  	rebuildNoStandardJson();
  	  }
  	  
  	  /**
       * 类 json 失去焦点
       * @param e
       */
      function nostandardJsonBlur(e){
        rebuildNoStandardJson();
      }
      /**
       * 标准 json 失去焦点
       * @param e
       */
      function standardJsonBlur(e){
        rebuildStandardJson();
      }
      
      /**
       * 根据左边树结构更新右边 输入框 json 串,
       * 并重置 自己本身属性 json
       */
      function jsonRebuild(e){
        var $target = $(e.target);
        if($target.is(':checkbox')){
          var select = $target.prop('checked'),$childs = $target.closest('li').find(':checkbox').not($target);
          if(!select){
            $childs.iCheck('uncheck');
          }else{
            $childs.iCheck('check');
          }
        }
        refreshJson();
        $standardJson.val(handle.convert(_this.json));
      }
      
  	}
  	
  	/*
  	 * 构建树视图,前后操作
  	 */
  	function buildTreeView(){
      handle.buildJson();
      this.json = handle.standardJson;
      //构建 json 树视图
      hlog.info('正在构建 json 树视图');
      $treeDom.empty();
      buildTree(this.json,$treeDom,-1);
      $('#tree :checkbox').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green'
      });
      hlog.success('json 树视图构建成功');
  	}
  
  	
  	/*
  	 * 根据 json 数据构建树结构
  	 */
  	var treeNode = '<li><div class="tree-node"><span class="checkbox"><input type="checkbox" name="" checked class="checkbox" value=""></span> <span><input type="text" id="" class="key" value=""> <b>:</b> <input type="text" id="" class="value" value=""></span></div></li>';
  	function buildTree(treeJson,$root,level){
  	  if(!treeJson){
  	    return ;
  	  }
  	  level ++;
  	  var key = undefined,value = undefined;
  	  if($.isArray(treeJson)){
  	    //json 数组 
  	    for(var i=0;i<treeJson.length;i++){
  	      key = i;value = treeJson[i];
  	      buildTreeNode(key,value,level,$root);
  	    }
  	  }else{
  	    //json 对象 
  	    for(var _key in treeJson){
  	      key  = _key;value=treeJson[key];
  	      buildTreeNode(key,value,level,$root);
  	    }
  	  }
  	}
  	/*
  	 * 构建树结点
  	 */
  	function buildTreeNode(key,value,level,$root){
  	  var $currentNode = $(treeNode).appendTo($root);
  	  //加入缩进 
      for(var j=0;j<level;j++){
        $currentNode.find('.tree-node').prepend('<span class="indent"></span>');
      }
      $currentNode.find('.key').val(key);
      var $value = $currentNode.find('.value'),val = undefined;
      if($.isArray(value)){
        val = '['+value.length+']';
      }else if(typeof value == 'object'){
        val = '{'+Object.keys(value).length+'}';
      }
      if(val){
        $ul = $('<ul></ul>');
        $ul.appendTo($currentNode);
        $value.attr('readonly',true);
        buildTree(value,$ul,level);
      }
      $value.val(val || value);
  	}
  	
  	return {
  	  json:{}, 
  		init:init,
  		rebuildNoStandardJson:rebuildNoStandardJson,
  		rebuildStandardJson:rebuildStandardJson
  	}
  })(jsonHandle,log);
 
  
  //扩展页面方法
  $.extend(JsonPage.prototype,{
    init:function(){
    	 log.init();
    	 jsonTree.init();
    },
    relayout:function(clientWidth,clientHeight){
      var pageHeight = $('.page-wrap').height(),  //.height() 方法取的是 innerHeight() 是不包含 padding border 的高度
          pageWidth = $('.page-wrap').width(),
          leftWidth = $('.params-tree').width(),
          rightWidth = pageWidth - leftWidth,
          noStandardJsonHeight = $('.no-standard-json').outerHeight(),
          standardJsonHeight = $('.standard-json').outerHeight(),
          buttonGroupHeight = $('#jsontreeOpts').outerHeight(),
          jsonStatusHeight = pageHeight - noStandardJsonHeight - standardJsonHeight - buttonGroupHeight - $('#inputTypeCmd').outerHeight();
      
      $('.params-tree').height(pageHeight);
      $('.json-input-status').width(rightWidth).height(pageHeight);
      $('.json-status').height(jsonStatusHeight);
      $('.json-status .log').height(jsonStatusHeight - $('.json-status .legend').outerHeight());
    },
    bindEvents:function(){
    	/*
    	 * 通用按扭事件处理
    	 */
    	function eventHandler(e){
    		var $target = $(e.target),
    			event = $target.attr('event');
    		switch(event){
    		case 'cleanNoStandardJson':
    			$('.no-standard-json textarea').val('');
    			break;
    		case 'cleanStandardJson':
    			$('.standard-json textarea').val('');
    			break;
    		case 'cleanLog':
    			log.clean();
    			break;
    		case 'jsonview':
    			openDialog('json视图','/tools/json/jsonview.html',null,'40%');
    		}
    	}
    	var Events = [{selector:'#jsontreeOpts button.btn',types:['click'],handler:eventHandler}];
    	regPageEvents(Events);
    }
  });

  ExtendPage.call(new JsonPage(),'jsonpage',JsonPage);
})(window.jQuery)