(function() {
	var SqlClient = function() {},	//主模块
		TableTree = function() {},		//表格列表模块
		CodeMirroInput = function(){},// sql 输入和解析模板
		QuicklySql = function(){},	//快速 sql 模块
		ResultSet = function(){};		//结果集模块
	var sqlclient = new SqlClient('.page-wrap');
	sqlclient.tabletree = new TableTree('#tablesTree');
	sqlclient.codeMirro = new CodeMirroInput('#sqlInput');
	sqlclient.quicklySql = new QuicklySql('#quicklySql');
	sqlclient.resultSet = new ResultSet('.result-set-wrap');
	
	/**
	 * sql 客户端方法
	 */
	$.extend(SqlClient.prototype, {
		init : function() {
			this.quicklySql.init();
			this.tabletree.init();
			this.codeMirro.init();
			this.resultSet.init();
		},
		relayout : function(clientWidth, clientHeight) {
			this.tabletree.relayout(clientWidth, clientHeight);
			this.resultSet.relayout(clientWidth, clientHeight);
		},
		bindEvents : function() {
			this.quicklySql.bindEvents();
			this.codeMirro.bindEvents();
			this.tabletree.bindEvents();
			this.resultSet.bindEvents();
		}
	});

	/**
	 * tables 方法
	 * 表格插件
	 */
	(function() {
		
		/*
		 * 根据传入的表格渲染一颗树
		 * params 
		 * 	tables: Array 
		 * 	_this : 当前TableTree 对象
		 */
		function renderZtree(tables,_this){

      var treeNodes = [];
      //树结点渲染
      for ( var i = 0; i < tables.length; i++) {
        var table = tables[i], columns = table.columns, 
          //add by sanri at 2016/12/8 解决因表名过滤引发的 id 带有 em 标记 ,过滤时增加一个属性 realName
            tableId = table.realName || table.tableName, tableName = formatTableName(i,table);
        treeNodes.push({id : tableId,name : tableName,parentId : -1});
        //加入列
        if (columns && columns.length > 0) {
          for ( var j = 0; j < columns.length; j++) {
            var column = columns[j], columnId = column.realName || column.columnName, 
                columnName = formatColumnName(j, column);
            treeNodes.push({id : columnId,name : columnName,parentId : tableId});
          }
        }
      }
      _this.zTree = $.fn.zTree.init(_this.$zTree, _this.config, treeNodes);
      
      //private methods
      /*
       * 格式化 表名和字段名
       */
      function formatTableName(index,table){
        var columns = table.columns || [];
        return (index + 1)+'.'+table.tableName+'('+(table.comments || '未说明')+')['+columns.length+']';
      }
      function formatColumnName(index,column){
        return (index + 1) + '.'+column.columnName+'('+(column.comments || '未说明')+')';
      }
		}
		
		/*
		 * 根据搜索条件过滤出表格或字段
		 * 只要表名或表注释匹配,那么整张表都显示,不管字段是什么,否则再去匹配字段
		 * 如果有列匹配的,那么到最后把树展开
		 */
		function filter(text,_this){
		  /*
		   * 标记表名或表注释 
		   */
		  function _markTable(markTable,field,lowerText){
		    markTable[field] = markTable[field].replace(new RegExp('('+lowerText+')',"gi"),'<em class="table-mark">'+'$1'+'</em>');
		  }
		  /*
		   * 标记字段名
		   */
		  function _markColumn(markColumn,field,lowerText){
		    markColumn[field] = markColumn[field].replace(new RegExp('('+lowerText+')',"gi"),'<em class="table-mark">'+'$1'+'</em>');
		  }
		  
		  if(!text){
		    renderZtree(_this.allTables, _this);
		    return ;
		  }
		  var lowerText = text.toLowerCase(),renderTables = [],columnMatch = false;
		  if(_this.allTables && _this.allTables.length>0){
		    //比较所有表格
  		  table : for(var i=0;i<_this.allTables.length;i++){
  		    var tableName = _this.allTables[i].tableName,
  		      tableComments = _this.allTables[i].comments,
  		      columns = _this.allTables[i].columns;
  		    if(tableName.toLowerCase().indexOf(lowerText) != -1 || tableComments.toLowerCase().indexOf(lowerText) != -1){
  		      //标红显示 
  		      var markTable = $.extend({},_this.allTables[i]);    //保存原来的表格对象
  		      if(tableName.toLowerCase().indexOf(lowerText) != -1){ //标记表名
  		        _markTable(markTable,'tableName',lowerText);
  		      }else{  //标记注释 
  		        _markTable(markTable,'comments',lowerText);
  		      }
  		      markTable.realName = tableName;
  		      renderTables.push(markTable);
  		      continue;
          }
  		    //比较列,如果有匹配的,则加入当前表格
  		    if(columns && columns.length > 0){
    		    for(var j=0;j<columns.length;j++){
    		      var columnName = columns[j].columnName,
    		          columnComments = columns[j].comments;
    		      if(columnName.toLowerCase().indexOf(lowerText) != -1 || columnComments.toLowerCase().indexOf(lowerText) != -1){
    		        columnMatch = true;
    		        var markTable = $.extend(true,{},_this.allTables[i]);    //保存原来的表格对象,深度复制
    		        if(columnName.toLowerCase().indexOf(lowerText) != -1){
    		          _markColumn(markTable.columns[j], 'columnName', lowerText);
    		        }else{
    		          _markColumn(markTable.columns[j], 'comments', lowerText);
    		        }
    		        markTable.columns[j].realName = columnName;
                renderTables.push(markTable);
                continue table;
              }
    		    }
  		    }
  		  }
		  } //end if 
		  renderZtree(renderTables, _this);
		  //如果其中有列匹配的表,那么渲染完成后展开树
		  if(columnMatch){
//		    _this.zTree.expandAll(true);  效果并不是太好
		  }
		}
		
		/*
     * 树结点点击之前,判断是表还是列,做出相应操作
     */
    function beforeClick(treeId, treeNode){ //treeId 指的是整颗树的 id
      if (treeNode.isParent) {
        //写入查表 sql ,并执行
        var line = 'select * from '+treeNode.id+';';
        sqlclient.codeMirro.appendSql(line);
        sqlclient.codeMirro.executorSql(line);
      }
    }
		
		$.extend(TableTree.prototype, {
			tablesMap:{},
			allTables:[],
			zTree:undefined,
			$zTree:$('#tablesTree'),
			config:{
				view : {
					dblClickExpand : false,
					showLine : true,
					selectedMulti : true,
					nameIsHTML:true  //name 节点支持 html
				},
				data : {
					simpleData : {
						enable : true,
						idKey : 'id',
						pIdKey : 'parentId',
						rootPId : -1
					}
				},
				callback : {
					beforeClick :beforeClick ,
					onClick :undefined,
					onRightClick : undefined
				}
			
			},
			init : function() {
				 //tablesTree 初始化数据 
	      var _this = this;
	      addLoadingMask(this.$zTree);
	      ajaxRequest({url : '/jdbc/tables',async:true},function(tables) {
	      	// treeview 移除加载动画 ,不用了,已经被树动态加载给覆盖了
	      	removeLoadingMask(_this.$zTree);
	      	
	        if(tables && tables.length){
	        	_this.allTables = tables;
	        	//快速 sql 加载所有的表 
	        	sqlclient.quicklySql.loadTables(tables);
	          for(var i=0;i<tables.length;i++){
	            var table = tables[i];
	            _this.tablesMap[table.tableName] = table; //保存最初始的 所有表
	          }
	          renderZtree(tables,_this);
	          return ;
	        }
	        logger.debug('没有表');
	      }); // tablesTree 初始化数据结束
	      
	    //初始化右键菜单
	      $.contextMenu({
	        selector:'#tablesTree>li',
	        items:{
	          'insert':{name:'添加数据',icon:'add',callback:function(key,opts){
	            var treeId = opts.$trigger.attr('id'),
	                treeNode = _this.zTree.getNodeByTId(treeId),
	                tableName = treeNode.id,
	                table = _this.tablesMap[tableName];
	            _this.zTree.selectNode(treeNode);   //选中节点,总要知道是操作的哪张表
	            layer.open({
	              type:1,
	              area:['50%','50%'],
	              content:'内容',
	              btn:['提交数据','取消'],
	              yes:function(index,layero){
	                //TODO 需要按照 table 生成随机数据,然后可以提交了
	              },
	              calcel:function(index,layero){
	                layer.close(index);
	              }
	            });
//	            logger.debug(table);
	          }},
	          'edit':{name:'编辑数据',icon:'edit',callback:function(key,opts){
	            
	          }},
	          'desc':{name:'表结构描述',icon:'edit',callback:function(key,opts){
	            
	          }}
	        }
	      });
			},
			relayout : function(clientWidth, clientHeight) {
				// 计算表格列表最大能达到的高度
				var tablesTreeHeight = clientHeight - $('#ztreeCmd').outerHeight() - $('#tableSearch').outerHeight();
				$('#tablesTree').height(tablesTreeHeight);
			},
			bindEvents : function() {
			  var _this = this;
			  var EVENTS = [{selector:'#tableSearch',types:['keyup'],handler:filterTables},
			                {selector:'#ztreeCmd>.btn',types:['click'],handler:eventHandler}];
			  regPageEvents(EVENTS);
			  
			  /*
			   * 过滤表格
			   */
			  function filterTables(e){
			    var $target = $(e.target),
            text = $target.val().trim();
          filter(text, _this);
			  }
			  /*
	       * 展开,关闭方法
	       */
	      function eventHandler(e){
	        var $target = $(e.target).closest('button'),
	            event = $target.attr('event');
	        switch(event){
	        case 'expand':
	          _this.zTree.expandAll(true);
	          break;
	        case 'compress':
	          _this.zTree.expandAll(false);
	        default:
	        }
	        
	      }
			}
		});
	})();
	
	/**
	 * codeMirro 控件
	 */
	;(function(){
		
		$.extend(CodeMirroInput.prototype,{
			codeMirro:undefined,
			$codeMirro:$('#sqlInput>textarea'),
			config:{
        value: '--在些处输入你的脚本\n',
        mode:'text/x-plsql',
        tabSize:2,
        tabMode:'indent',
        lineNumbers:true,
        autoMatchParens: true,
        textWrapping: true,
        autofocus: true,
        matchBrackets : true,
        styleActiveLine:true,
        extraKeys: {"Alt-/": "autocomplete"},
			},
			init:function(){
				//初始化 codeMirro 
				this.codeMirro = CodeMirror.fromTextArea(this.$codeMirro[0],this.config);
				this.codeMirro.setSize('100%','100%');
			},
			 /**
	     * 向编辑器中加一行 sql 
	     */
			appendSql:function(sql){
	      var nowValue = this.codeMirro.getValue();
	      if(nowValue.indexOf(sql) == -1){ //没有这句话才写,不然会写一堆
	      	this.codeMirro.setValue(this.codeMirro.getValue()+sql+'\n');
	      }
			},
			/**
	     * sql 执行方法,
	     * 思路:向后台传送 sql 串,并返回结果;然后调用 tabs 模块新增 tab
	     * 可以发多句 sql ,以 ; 分隔 
	     */
			executorSql:function(sqls){
	      ajaxRequest({url:'/jdbc/executeSql',data:{executeSqls:sqls}},function(resultSets){
	        if(resultSets){
	          for(var sqlStatemt in resultSets){
	            sqlclient.resultSet.addTab(sqlStatemt,resultSets[sqlStatemt]);
	          }
	        }
	      });
			},
			bindEvents:function(){
			  var _this = this;
				var EVENTS = [{selector:'#sqlCmd>.btn',types:['click'],handler:eventHandler}];
				regPageEvents(EVENTS);
				
				/*
				 * 所有按扭的事件
				 */
				function eventHandler(e){
	        var $target = $(e.target),
	            event = $target.attr('event');
	        switch(event){
	        case 'executorSql':
	          var selection = _this.codeMirro.getSelection();
	          if(!selection){       //没有选中的话,默认执行全部
	            selection = _this.codeMirro.getValue();
	          }
	          if(!selection)return ;
	          _this.executorSql(selection);
	          break;
	        default:;
	        }
	      }
			}
		});
	})();
	
	/**
	 * 快速 sql 区
	 */
	(function(){
		var chosenDefault = {
				disable_search_threshold : 10,
        no_results_text : "没有数据",
        placeholder_text_multiple:'选择表名来操作',
        placeholder_text_single:'选择一个选项',
        search_contains:true		//设置可以从任意位置搜索字符串
		}
		$.extend(QuicklySql.prototype,{
			init:function(){
				$('#sqlHead').chosen($.extend(chosenDefault,{
	        width:'120px'
				}));
				this.bindEvents();
			},
			/**
			 * 选择表格区,加载所有的表
			 * @param tables
			 */
			loadTables:function(tables){
				if(tables && tables.length > 0){
					for(var i=0;i<tables.length;i++){
						$('#multipleTable').append('<option value="'+tables[i].tableName+'">'+(tables[i].comments || tables[i].tableName)+'</option>')
					}
				}
				$('#multipleTable').chosen($.extend(chosenDefault,{
	        width:'220px',
				}));
			},
			bindEvents:function(){
				$('#sqlHead').on('change',createSql);
				var $sqlContent = $('#sqlContent');
				/*
				 * 创建 sql 语句
				 */
				function createSql(event,params){
					switch(params.selected){
					case 'select':
						$sqlContent.find('.content.code0').text('from');
						$('#multipleTable').val('').prop('multiple',true).trigger('chosen:updated');
						break;
					case 'update':
						$sqlContent.find('.content.code0').text('');
						break;
					case 'delete':
						$sqlContent.find('.content.code0').text('from');
						break;
					case 'insert':
						$sqlContent.find('.content.code0').text('into');
						//表选择框变成单选
//						$('#multipleTable').val('').prop('multiple',false).trigger('chosen:updated');
						$('#multipleTable').removeAttr('multiple').trigger('chosen:updated');
						
						break;
					}
				}
			}
		});
	})();
	
	/**
	 * 查询结果集区
	 * 包含滚动标签页和结果集
	 */
	;(function(){
		 /*
     * 结果集中追加一个表格
     * 写一张表格追加进 tables-result-set,并美化 ;并隐藏其它表格
     */
		function appendTable(newTabId,resultSet,_this){
      if(resultSet.head && resultSet.head.length > 0){
        var head = [],body = [];
        //拼接 head
        for(var i=0;i<resultSet.head.length;i++){
          var columnName = resultSet.head[i];
          head.push('<th>'+columnName+'</th>');
        }
        //拼接 body
        for(var j=0;j<resultSet.body.length;j++){
          var mapObj = resultSet.body[j];
          body.push('<tr>');
          for(var colName in mapObj){
            body.push('<td>'+mapObj[colName]+'</td>');
          }
          body.push('</tr>');
        }
        var tabHtml = '<table id="table_'+newTabId+'"><thead><tr>'+head.join('')+'</tr></thead><tbody>'+body.join('')+'</tbody></table>';
//        logger.debug(tabHtml);
        _this.$tables.find('table').parent().hide();             //隐藏其它表格
        $(tabHtml).appendTo(_this.$tables).dataTable();         //显示当前追加的表格
      }
		}
		
		$.extend(ResultSet.prototype,{
			$scrollTabs:$('#tableTabs'),
			$tables:$('#tables'),
			tabsMap:{},
			init:function(){
				var _this = this;
				this.$scrollTabs.scrolltabs({
	        onSelect:function(tab){
	          var tabId = tab.id,
	              id = tabId.split('_')[1];
	          _this.$tables.find('table[id=table_'+id+']').parent().show().siblings().hide();   //显示当前选中的表格
	        },
	        onClose:function(tab){
	          var tabId = tab.id,
	              id = tabId.split('_')[1];
	          _this.$tables.find('table[id=table_'+id+']').parent().remove();
	          delete _this.tabsMap[tab.title];   //删除 map 中数据 
	        }
	      });
			},
			relayout:function(clientWidth,clientHeight){
			  var wrapHeight = clientHeight - $('#quicklySql').outerHeight() - $('#sqlInput').outerHeight() - $('#sqlCmd').outerHeight();
			  $('#tables').parent().height(wrapHeight);
			},
			/**
			 * 增加一个标签页,
			 * params:
			 * 	sqlId : 提供一个 sql 语句
			 * 	resultSet : json 类型的结果集
			 */
			addTab:function(sqlId,resultSet){
	      if(!sqlId){return ;}
	      if(sqlId in this.tabsMap){
	        var tabId = this.tabsMap[sqlId].id,
	            id = tabId.split('_')[1];
	        this.$scrollTabs.scrolltabs('select',tabId);
	        this.$scrollTabs.scrolltabs('showActive');
	        //datatable 重新加载  TODO 
	        return ;
	      }
	      //如果不存在,则新加 tab
	      var newTabId = Math.round((Math.random() * 1000)),
	          tab = {id:'tab_'+newTabId,title:sqlId,};
	      this.$scrollTabs.scrolltabs('add',tab);
	      this.tabsMap[sqlId] = tab;
	      appendTable(newTabId,resultSet,this);
			},
			bindEvents:function(){
				
			}
		});
	})();
	
	ExtendPage.call(sqlclient, 'sqlclient', SqlClient);
})(window.jQuery)