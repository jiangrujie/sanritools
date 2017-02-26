(function(){
	var treeselect = (function(){
		var treeData = [],$treeDom = $('#tree');
		init();
		function init(){
			loadData();
			bindEvents();
		}
		
		/**
		 * 请求树数据
		 */
		function loadData(){
			ajaxRequest({url:'/pageui2/pages/treeselect/data.json',type:'get'},function(treedata){
				treeData = treedata.content;
				render(treeData);
			});
		}
		
		/**
		 * 事件绑定
		 */
		function bindEvents(){
			var EVENTS = [{parent:$treeDom,selector:'.tree-node',types:['click'],handler:treeItemClick}];
			regPageEvents(EVENTS);
			/**
			 * 树结点点击
			 */
			function treeItemClick(e){
				var $target = $(e.target),$li = $target.closest('li'),$currentNodeCheckbox = $target;
				
				if(!$currentNodeCheckbox.is(':checkbox')){
				  $currentNodeCheckbox = $li.find('>.tree-node :checkbox');
  				$currentNodeCheckbox.prop('checked',!$currentNodeCheckbox.prop('checked'));
				}
				
				var currentState = $currentNodeCheckbox.prop('indeterminate',false).prop('checked');
				selectChild($li,currentState);
				selectParent($li,currentState);
			}
			/*
			 * 选择孩子节点
			 */
			function selectChild($li,state){
			  $li.find(':checkbox').prop('checked',state);
			}
			
			/*
			 * 选择父结点 
			 */
			function selectParent($li,state){
			  var $parent = $li.closest('ul').siblings('a.tree-node'),
			    $siblings = $li.siblings('li').not($li);
			  
			  var count = 0;
			  $siblings.each(function(){
			    var $currCheckbox = $(this).find('>.tree-node :checkbox'),
			      currState = $currCheckbox.prop('checked');
			    if((state && !currState) || (!state && currState)){
			      //半选中状态
			      $parent.find(':checkbox').prop('indeterminate',true);
			      return false;
			    }
			    count ++;
			  });
			  
			  if(count == $siblings.size()){
			    //移除半选中,全选中
			    $parent.find(':checkbox').prop('indeterminate',false).prop('checked',state);
			  }
			  
			  //递归查找所有父结点x
			  selectParent($parent.closest('li'), state);
			}
			
		}
		
		/*
		 * 渲染一颗树
		 */
		function render(treeData_){
			if(treeData_ && treeData_.length > 0){
				$treeDom.empty();
				buildTree(treeData_, $treeDom,0);
			}
			/*
			 * 构建树结点 
			 */
			function buildTree(childJson,$parent,level){
				if(childJson && childJson.length> 0){
					for(var i = 0;i<childJson.length;i++){
						var $currentNode = $('<li><a href="#" class="tree-node"><span><input type="checkbox"></span> <span class="node_name">'+childJson[i].gridName+'</span> <span class="grid-users"></span></a></li>').appendTo($parent);
						$currentNode.data('original',childJson[i]);	//保存原始数据
						//加入缩进
						for(var k = 0;k<level;k++){
							$currentNode.children('.tree-node').prepend('<span class="indent"></span>');
						}
						//加入用户
						var $gridUsers = $currentNode.find('.grid-users');
						if(childJson[i].gridUsers && childJson[i].gridUsers.length > 0){
							for(var j=0;j<childJson[i].gridUsers.length;j++){
								var $users = $('<b>'+childJson[i].gridUsers[j].userName+'</b>').appendTo($gridUsers);
								$users.data('original',childJson[i].gridUsers[j]);
							}
						}
						level ++;
						var $ul = $('<ul></ul>').appendTo($currentNode);
						buildTree(childJson[i].infos, $ul,level);
						level --;
					}
				}
			}
		}
		
		return {
			
		}
	})()
})()