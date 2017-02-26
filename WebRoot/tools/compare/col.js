(function(){
	var CompareColumn = function(){};
	var compareColumn = new CompareColumn('.page-wrap');
	
	$.extend(CompareColumn.prototype,{
		init:function(){
			$('input:checkbox',this.context).iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green'
      });
		},
		relayout:function(clientWidth,clientHeight){
			
		},
		bindEvents:function(){
			var _this = this;
			var EVENTS = [{selector:'#leftColumns',types:['blur'],handler:_this.compare},
			{selector:'#rightColumns',types:['blur'],handler:_this.compare},
			{selector:'#ignoreColumns',types:['blur'],handler:_this.compare},
			{selector:'#ignoreBtns>.btn',types:['click'],handler:onClickBtn}];
			regPageEvents(EVENTS);
			
			function onClickBtn(e){
				var $target = $(e.target);
				if($target.is('.btn')){
					var $checkbox = $target.find(':checkbox');
					if($checkbox.is(':checked')){
						$checkbox.iCheck('uncheck');
					}else{
						$checkbox.iCheck('check');
					}
				}
				_this.compare();
			}
			
		},
		/*
		 * 主比较方法
		 */
		compare:function(){
			var $leftColumns = $('#leftColumns'),
				$rightColumns = $('#rightColumns'),
				$ignoreColumns = $('#ignoreColumns'),
				$leftMuti = $('#leftMuti'),
				$rightMuti = $('#rightMuti'),
				$equalColumns = $('#equalColumns'),
				$ignoreBtns = $('#ignoreBtns');
			
			/*
			 * 处理当前值
			 */
			function handlerVal(val){
				if(isIgnoreBlank){
					val = val.trim();
				}
				if(isIgnoreAa){
					val = val.toLowerCase();
				}
				if(isIgnore_){
					val = val.replace(/_/g,'');
				}
				return val;
			}
			
			/*
			 * 处理输入值
			 */
			function handlerInput(currentColumns){
				if(!currentColumns){
					return [];
				}
				var original = currentColumns.split('\n'),handlerColumns = [];
				if(original && original.length > 0){
					for(var i=0;i< original.length;i++){
						var val = original[i];
						if(!val){continue;}			//val 有可能是空串,或不存在
						handlerColumns.push(handlerVal(val));
					}
				}
				return handlerColumns;
			}
			
			//获取当前输入值 
			var leftColumns = $leftColumns.val(),
				rightColumns = $rightColumns.val(),
				ignoreColumns = $ignoreColumns.val(),
				isIgnoreAa = $ignoreBtns.find('input:checkbox[name=ignoreAa]').is(':checked'),
				isIgnoreBlank = $ignoreBtns.find('input:checkbox[name=ignoreBlank]').is(':checked'),
				isIgnore_ = $ignoreBtns.find('input:checkbox[name=ignore_]').is(':checked');
			//处理
			if(!leftColumns && !rightColumns){
				return ;
			}
			var rightMuti = [],leftMuti = [],eqColumns = [];
			var originalLeft = handlerInput(leftColumns);
			var originalRight = handlerInput(rightColumns);
			var originalIgnore = handlerInput(ignoreColumns);
			
			var needCompare = true;
			
			if(originalIgnore.length == 0){
				//如果没有需要忽略的列
				if(originalRight.length > 0 && originalLeft.length  == 0){
					//右边多出全部列
					rightMuti = originalRight;
					needCompare = false;
				}else if(originalRight.length == 0 && originalLeft.length  > 0){
					//左边多出全部列
					leftMuti = originalLeft;
					needCompare = false;
				}
			}else{
				//去除需要忽略的列
				if(originalRight.length > 0 && originalLeft.length  == 0){
					//右边多出全部列
					for(var i=0;i<originalRight.length;i++){
						if($.inArray(originalRight[i],originalIgnore) == -1){
							rightMuti.push(originalRight[i]);
						}
					}
					needCompare = false;
				}else if(originalRight.length == 0 && originalLeft.length  > 0){
					//左边多出全部列
					for(var i=0;i<originalLeft.length;i++){
						if($.inArray(originalLeft[i],originalIgnore) == -1){
							leftMuti.push(originalLeft[i]);
						}
					}
					needCompare = false;
				}
			}
			//其它情况就都需要比较了
			if(needCompare){
				 //统计相同的列 
	      for(var i=0;i<originalLeft.length;i++){
	        for(var j=0;j<originalRight.length;j++){
	          if(originalLeft[i] == originalRight[j] && $.inArray(originalLeft[i],originalIgnore) == -1){
	            eqColumns.push(originalLeft[i]);
	          }
	        }
	      }
	      //统计旧列比新列多出的列
	      var eqCols = ';'+eqColumns.join(';')+';';
	      for(var i=0;i< originalLeft.length;i++){
	       if(eqCols.indexOf(';'+originalLeft[i]+';') == -1 && $.inArray(originalLeft[i],originalIgnore) == -1){
	         leftMuti.push(originalLeft[i]);
	       }
	      }
	      //统计新列比旧列多出的列
	      for(var i=0;i< originalRight.length;i++){
	        if(eqCols.indexOf(';'+originalRight[i]+';') == -1 && $.inArray(originalRight[i],originalIgnore) == -1){
	          rightMuti.push(originalRight[i]);
	        }
	      }
			}
			
			//写入右边比较结果
			$leftMuti.html(leftMuti.join('<br/>'));
			$rightMuti.html(rightMuti.join('<br/>'));
			$equalColumns.html(eqColumns.join('<br/>'));
		}
	});
	
	ExtendPage.call(compareColumn,'compareColumn',CompareColumn);
})(window.jQuery)