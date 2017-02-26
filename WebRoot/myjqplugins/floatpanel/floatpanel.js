(function($,undefined){
	//扩展 trim 方法
  if (!String.prototype.trim) {
    String.prototype.trim = function() {
    	return $.trim(this);
    }
  }
	
	/*
	 * 初始化方法
	 */
	function init($this,opts){
		var offset = $this.offset(),
				targetWidth = $this.outerWidth(true),
				targetHeight = $this.outerHeight(true),
				$parent = $this.parent(),
				$toolbar = $('<div class="toolbar clearfix"><div class="toolbar-item toolbar-right" eventId="search"><input type="text" placeholder="请输入要搜索的人员"> <i class="fa fa-search"></i></div><div class="toolbar-item" eventId="selectAll"><input type="checkbox"><label>全选/取消</label></div><div class="toolbar-item" eventId="retainSelect"><label>反选</label></div></div>'),
				$dataUl = $('<ul class="list-data"></ul>'),
				$panel  = $('<div class="dropdown-panel"></div>').append($toolbar).append($dataUl).appendTo($parent);
		//设置位置信息
		$panel.css({
			top:(offset.top + targetHeight),
			left:(offset.left - (opts.width - targetWidth)), /*右对齐 */
			width:opts.width,
			height:opts.height
		});
//		//解决在 3d 被遮盖的问题 放这里不管用
		$panel.append('<iframe  src="about:blank" frameborder=0 style="height: 100%; width: 100%; position: absolute; left: 0px; z-index: -1; top: 0px; scrolling: no"></iframe>');
		//设置数据区大小
		$dataUl.css({
			height:(opts.height - $toolbar.outerHeight(true))
		});
		$panel.hide(); //加载后先隐藏
		return {
			panel:$panel,
			toolbar:$toolbar,
			dataUl:$dataUl
		}
	}
	
	/*
	 * 排序,默认选中的会排在前面
	 */
	function sort(jq){
		var opts = $.data(jq[0],'floatpanel').opts,
				data = opts.data;
		if(!data || data.length <= 1){
			return ;
		}
		var dataSelected = [],dataNotSelected = [],selectedStr = '$'+opts.selected.join('$')+'$';
		for(var i=0;i<data.length;i++){
			var row = data[i];
			if(selectedStr.indexOf(row.id) != -1){
				dataSelected.push(row);
			}else{
				dataNotSelected.push(row);
			}
		}
		opts.data = dataSelected.concat(dataNotSelected);
	}
	/*
	 * 刷新视图,此方法仅加载数据,赋值给全局数据
	 */
	function loadData(jq){
		var opts = $.data(jq[0],'floatpanel').opts;
		if(opts.url){
			//优先数据请求
			$.log('通过地址请求数据,暂未实现');
			return ;
		}
	}
	/*
	 * 渲染视图
	 * 可加入过滤的数据来渲染
	 */
	function render(jq,filterData){
		var opts = $.data(jq[0],'floatpanel').opts,
				$dataUl = $.data(jq[0],'floatpanel').dataUl,
				data = opts.data;
		if(filterData){
			data = filterData;
		}
		
		$dataUl.empty();
		if(!data || data.length == 0){
			return ;
		}
		
		var uldata = [];
		for(var i=0;i<data.length;i++){
			var row = data[i];
			if(row['selected']){
				opts.selected.push(data[i].id);
			}
			
			uldata.push('<li id="'+row.id+'" >');
			uldata.push('<a href="#">');
			if(opts.rownum){
				uldata.push('<span class="rownum">'+(i+1)+'</span>');
			}
			uldata.push('<input type="checkbox" />');
			uldata.push('<i class="fa '+row.iconCls+'"></i>');
			uldata.push('<span class="text">'+row.text+'</span>');
			uldata.push('</a></li>');
		}
		$dataUl.append(uldata.join(''));
		//选中已经选中的元素
		for(var i =0 ;i<opts.selected.length;i++){
			$dataUl.find('li[id='+opts.selected[i]+']').addClass('selected');
			$dataUl.find('li[id='+opts.selected[i]+'] input[type=checkbox]').prop('checked',true);
		}
		//回调选中事件 
		opts.onSelected.call(this,opts.selected);
	}
	
	/*
	 * 事件绑定
	 */
	function bindEvents(jq){
		var state = $.data(jq[0],'floatpanel'),
				opts = state.opts,
				$toolbar = state.toolbar,
				$dataUl = state.dataUl,
				$panel = state.panel,
				events = [{selector:'.toolbar-item[eventId=selectAll]',types:['click'],handler:toggleSelect},
				          {selector:'.toolbar-item[eventId=retainSelect]',types:['click'],handler:retainSelect},
				          {selector:'.toolbar-item[eventId=search]',types:['blur','keyup'],handler:search},
				          {selector:'.toolbar-item[eventId=search] i',types:['click'],handler:search}]
		
//		for(var i=0;i<events.length;i++){
//			for(var j=0;j<events[i]['events'].length;j++){
//				$(events[i]['selector'],$toolbar).bind(events[i]['events'][j],function(e){
//					alert(events[i].handler)
//					events[i].handler.call(this,jq,e);
//				});
//			}
//		}
		while(events[0]){
  		(function(cfg){
				for(var i = 0, l = cfg.types.length; i < l; i++) {
					$(cfg.selector).bind(cfg.types[i], function(e) {
						if($.isFunction(cfg.handler)) {
							cfg.handler.call(this, jq,e);
							e.stopPropagation();
						}
					});
				}
  		})(events.shift());
  	}
		
		
		//监听 上,下,左,右,回车键,esc
//		$(document).on('keyup',$panel,panelOnKeyDown);
		//元素点击时打开/关闭面板
		jq.bind('keydown',function(e){
			panelOnKeyDown.call(this,jq,e);
		}).bind('click',function(e){
			if(opts.closed){
				openPanel(jq, false);
				return ;
			}
			closePanel(jq, false);
		});
		//数据结点点击事件
		$dataUl.on('click','li',function(e){
			dataLiClick.call(this,jq,e);
		});
		
		
	}
	/*
	 * 监听 上,下,左,右,回车键,esc 键
	 */
	function panelOnKeyDown(jq,e){
		var state = $.data(jq[0],'floatpanel'),
				opts = state.opts,
				$dataUl = state.dataUl,
				$panel = state.panel,
				$hoverA = $dataUl.find('li>a.hover'),$hoverLi = $hoverA.parent();
		switch(e.keyCode){
		case 13: //enter
			search(jq,e);
			break;
		case 27: //esc 
			closePanel(jq,false);
			break;
		case 32: //space 
			toggleSelectLi($hoverLi,opts);
			break;
		case 37://arrow left
			//暂时未实现 TODO 
			break;
		case 38: //arrow up
			if($hoverLi.size() == 0){
				//一个都没选中的时候,选中第一个
				$dataUl.find('li:first>a').addClass('hover');
				return ;
			}
			$hoverA.removeClass('hover');
			var $nowLi = $hoverLi.prev();
			$nowLi.find('a').addClass('hover');
			if($nowLi.size() > 0 && $nowLi.offset().top - $panel.offset().top < $nowLi.outerHeight(true) ){
				//scroll
				var preScrollTop = $dataUl.scrollTop();
				$dataUl.scrollTop(preScrollTop - $nowLi.outerHeight(true));
			}
			break;
		case 39://arrow right
			//暂时未实现 TODO 
			break;
		case 40: //arrow down
			if($hoverLi.size() == 0){
				//一个都没选中的时候,选中第一个
				$dataUl.find('li:first>a').addClass('hover');
				return ;
			}
			$hoverA.removeClass('hover');
			var $nowLi = $hoverLi.next();
			$nowLi.find('a').addClass('hover');
			if($nowLi.size() > 0 && $nowLi.offset().top - $panel.offset().top > opts.height - $nowLi.outerHeight(true) ){
				//scroll
				//console.log('scroll')
				var preScrollTop = $dataUl.scrollTop();
				$dataUl.scrollTop(preScrollTop + $nowLi.outerHeight(true));
			}
			break;
		default:
			//其它键不做处理
		}
	}
	
	
	/*
	 * 数据节点击
	 */
	function dataLiClick(jq,e){
		var $li = $(e.target).closest('li'),
				opts = $.data(jq[0], "floatpanel").opts;
		toggleSelectLi($li,opts);
	}
	
	/*
	 * 取消,选中单个元素
	 */
	function toggleSelectLi($li,opts){
		var $checkbox = $li.find('input[type=checkbox]'),
				status = $checkbox.prop('checked'),
				id = $li.attr('id');
		
		$checkbox.prop('checked',!!!status);
		if(!!!status){
			$li.addClass('selected');
			opts.selected.push(id);
		}else{
			$li.removeClass('selected');
			for(var i=0;i<opts.selected.length;i++){
				if(opts.selected[i] == id){
					opts.selected.splice(i,1);
					break;
				}
			}
		}
		opts.onSelected.call(this,opts.selected);
	}
	
	/*
	 * 搜索人员
	 */
	function search(jq,e){
		var state = $.data(jq[0], "floatpanel"),
				opts = state.opts,
				$toolbar = state.toolbar,
				$input = $toolbar.find('input[type=text]'),
				$dataUl = state.dataUl;
		
		if(e.keyCode == 38 || e.keyCode == 40 || (e.keyCode == 32 && e.ctrlKey) || e.keyCode == 27 || e.keyCode == 13){
			panelOnKeyDown.call(this,jq,e);
			return ;
		}
		
		if(!opts.data || opts.data.length == 0){
			return ;
		}
		var filterParam = $input.val().trim();
		var filterData = [];
		//当为空时,查询所有
		if(filterParam == ''){
			render(jq, opts.data);
			return ;
		}
		for(var i=0;i<opts.data.length;i++){
			var row = opts.data[i];
			if(row['text'].toUpperCase().indexOf(filterParam.toUpperCase()) != -1){
				filterData.push(row);
			}
		}
		render(jq, filterData);
	}
	
	/*
	 * 全选/取消 
	 */
	function toggleSelect(jq,e){
		var state = $.data(jq[0], "floatpanel"),
				opts = state.opts,
				$dataUl = state.dataUl,
				$selectFlag = $(e.target).closest('.toolbar-item').find('input[type=checkbox]');
		
		var selected = $selectFlag.prop('checked');
		$selectFlag.prop('checked',!!!selected);
		$dataUl.find('li input[type=checkbox]').prop('checked',!!!selected);
		//设置选中
		if(!opts.data || opts.data.length == 0){return ;}
		if(!!!selected){
			//防止重复选中
			var selectedStr = '$'+opts.selected.join('$')+'$';
			for(var i=0;i<opts.data.length;i++){
				if(selectedStr.indexOf(opts.data[i].id) == -1){
					opts.selected.push(opts.data[i].id);
				}
			}
			opts.onSelected.call(this,opts.selected);
			$dataUl.find('li').addClass('selected');
			return ;
		}
		opts.selected = [];
		opts.onSelected.call(this,opts.selected);
		$dataUl.find('li').removeClass('selected');
	}
	/*
	 * 反选
	 */
	function retainSelect(jq,e){
		var state = $.data(jq[0], "floatpanel"),
				opts = state.opts,
				selected = opts.selected,
				data = opts.data,
				$dataUl = state.dataUl,
				newSelect = [];
		if(!data || data.length  == 0){
			return ;
		}
		var selectedStr = '$'+selected.join('$')+'$';
		for(var i=0;i<data.length;i++){
			if(selectedStr.indexOf('$'+data[i].id+'$') == -1){
				newSelect.push(data[i].id);
				$dataUl.find('li[id='+data[i].id+']').addClass('selected');
				$dataUl.find('li[id='+data[i].id+'] input[type=checkbox]').prop('checked',true);
				continue;
			}
			$dataUl.find('li[id='+data[i].id+']').removeClass('selected');
			$dataUl.find('li[id='+data[i].id+'] input[type=checkbox]').prop('checked',false);
		}
		opts.selected = newSelect;
		opts.onSelected.call(this,opts.selected);
	}
	/*
	 * 关闭面板
	 */
	function closePanel(jq,forceClose){
		var state = $.data(jq[0], "floatpanel"),
				opts = state.opts;
	  if (forceClose != true && !opts.onBeforeClose.call(jq)) {
	  	return;
	  }
	  state.panel.hide();
	  opts.closed = true;
	}
	/*
	 * 打开面板
	 */
	function openPanel(jq,forceOpen){
		var state = $.data(jq[0], "floatpanel"),
				opts = state.opts,
				$panel = state.panel;
		 if (forceOpen != true && !opts.onBeforeOpen.call(jq)) {
       return;
		 }
		 sort(jq); //对选择的数据进行排序
		 render(jq);
		 state.panel.show();
		 opts.closed = false; //TODO 有可能刷新数据在排序之前,导致数据没有排序
		 //搜索聚焦
		 state.toolbar.find('.toolbar-item>input[type=text]').focus();
	}
	
	$.fn.floatpanel = function(options,params){
		if(typeof options == 'string'){
			var method = $.fn.floatpanel.methods[options];
			if (method) {
				return method(this, params);
			}
			throw 'no such method ' + options;
			return;
		}
		options = options || {};
		
		return this.each(function(){
			var _this = $(this);
			var state = $.data(this, 'floatpanel');
			if (state) {
				$.extend(state.options, options);
			}else{
				state = $.extend({}, $.fn.floatpanel.defaults, options);
        state = $.data(this, "floatpanel", $.extend({opts: state}, init($(this), state)));
        $(this).addClass('dropdown-close');
        loadData($(this));
    		render($(this));
        bindEvents($(this));
			}
		});
	}
	$.fn.floatpanel.defaults = {
			width:400,
			height:300,
			data:null,
			selected:[],
			url:undefined,
			interval:-1,
			rownum:true,
			singleSelect:false,
			pageSize:10,
			onBeforeClose:function(jq,param){return true}, //返回 false 阻止关闭
			onBeforeOpen:function(jq,param){return true},
			onSelected:function(selecteds){}, //在选择后调用,返回选择的数据 [id] 数组
			onOpen:function(jq,param){},
			closed:true
	}
	$.fn.floatpanel.methods={
			options:function(jq){
				return $.data(jq[0], "floatpanel").opts;
			},
			/**
			 * 刷新数据,只会刷新第一个的数据
			 */
			refresh:function(jq,_data){
				var opts = $.data(jq[0],'floatpanel').opts;
				//如果面板已经打开,则不需要刷新数据了
				if(opts.closed){
					if(!_data || _data.length == 0){
						return ;
					}
					if(opts.selected.length == 0){
						opts.data = _data;
					}else{
						var selectedStr = opts.selected.join('$');
						//已经选中的数据加上选择标记
						for(var i=0;i<_data.length;i++){
							var row = _data[i];
							if('$'+selectedStr+'$'.indexOf('$'+row['id']+'$') != -1){
								row['selected'] = true;
							}
						}
					}
					//render(jq);
				}
			},
			select:function(jq,param){
				var opts = $.data(jq[0],'floatpanel').opts;
				opts.selected = param;
				render(jq);
			},
			close:function(jq,param){
				return jq.each(function(){
					closePanel($(this),param);
				});
			},
			open:function(jq,param){
				return jq.each(function(){
					openPanel($(this),param)
				})
			}
			
	}
	
})(window.jQuery)