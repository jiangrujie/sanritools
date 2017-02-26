(function(){
  var Request = function(){};
  var request = new Request();
  var $columnInput = $('#columnInput').find('textarea'),
    $request = $('#request').find('textarea'),
    $response = $('#response'),
    $url = $('#requestUrl');
    /**
   * 类型匹配区单独模块
   */
  var typeMatch = (function(window){
    var columns = [],$typeMatch= $('#typeMatch');
    /**
     * 比较函数,比较设置进来的列和现在的列有哪些相同,哪些不同,相同的不刷新
     */
    function compare(newCol){
      var compareCol = {eq:[],gt:[],lt:[]};   //eq 代表相同的列 ,gt 代表 新列比旧列出来的列 ,lt 相反
      //统计相同的列
      for(var i=0;i<columns.length;i++){
        for(var j=0;j<newCol.length;j++){
          if(columns[i] == newCol[j]){
            compareCol.eq.push(columns[i]);
          }
        }
      }
      //统计旧列比新列多出的列
      var eqCols = ';'+compareCol.eq.join(';')+';';
      for(var i=0;i< columns.length;i++){
       if(eqCols.indexOf(';'+columns[i]+';') == -1){
         compareCol.lt.push(columns[i]);
       }
      }
      //统计新列比旧列多出的列
      for(var i=0;i< newCol.length;i++){
        if(eqCols.indexOf(';'+newCol[i]+';') == -1){
          compareCol.gt.push(newCol[i]);
        }
      }
      return compareCol;
    }
    
    /**
     * 追加列 view
     */
    function appendColumn(cols){
      var lis = [];
      if(cols && cols.length>0){
        for(var i=0;i<cols.length;i++){
          var col = cols[i];
          lis.push('<li id="'+col+'"><strong>'+col+'</strong><div class="button-group"><div class="btn btn-default selected" generate="numeric">数字<input type="text" value="5" style="width: 30px;"></div><div class="btn btn-default" generate="alpha">字母<input type="text" value="5" style="width: 30px;"></div><div class="btn btn-default" generate="mix">混合<input type="text" value="5" style="width: 30px;"></div><button class="btn btn-default" generate="date">日期</button><button class="btn btn-default" generate="idcard">身份证号</button></div></li>')
        }
      }
      $typeMatch.append(lis.join(''));
    }
    /**
     * 移除列 view
     */
    function removeColumn(cols){
      //选中需要去掉的列移除
      var removeCol = '';
      for(var i=0;i<cols.length;i++){
        removeCol += ',[id='+cols[i]+']';
      }
      removeCol = removeCol.substring(1);
      if(removeCol){    //有数据才需要移除,不然全部移除掉了
        $typeMatch.find('li'+removeCol).remove();
      }
    }
    
    /**
     * 传入所有列,刷新列表
     */
    function refreshView(cols){
      if(columns.length == 0){
        $typeMatch.empty();
        columns = cols;
        appendColumn(cols);
        return ;
      }
      var compareColRes = compare(cols);
      
      var eqCols = ';'+compareColRes.eq.join(';')+';';
      //相应的数据中去掉旧列,并保存新列
      var saveColumns = [];
      for(var j=0;j<columns.length;j++){
        if(eqCols.indexOf(';'+columns[j]+';') != -1){
          saveColumns.push(columns[j]);
        }
      }
      columns = saveColumns;
      for(var i=0;i<compareColRes.gt.length;i++){
        columns.push(compareColRes.gt[i]);
      }
      
      //移除旧列
      removeColumn(compareColRes.lt);
      //追加新列
      appendColumn(compareColRes.gt);
    }
    
    /**
     * 初始化的时候进行事件绑定
     */
    function bindEvents(){
      /**
       * 命令组事件处理
       */
      function eventHandler(e){
        var $target = $(e.target),
            event = $target.attr('event');
        switch(event){
        case 'clean':
          $('#response').empty();
          break;
        case 'jsonview':
          openDialog('json视图','/tools/request/jsonview.html',null,'40%');
          break;
        case '_2aB':
          var newCol = [];
          for(var i=0;i<columns.length;i++){
            newCol.push(StringUtil._2aB(columns[i]));
          }
          refreshView(newCol);
          break;
        case 'uppercase':
          var newCol = [];
          for(var i=0;i<columns.length;i++){
            newCol.push(columns[i].toUpperCase());
          }
          refreshView(newCol);
          break;
        case 'lowercase':
          var newCol = [];
          for(var i=0;i<columns.length;i++){
            newCol.push(columns[i].toLowerCase());
          }
          refreshView(newCol);
          break;
        case 'getColumn':
          var split = $target.find('input.split').val(),
              quoat = $target.find('input.quoat').val();
          var alertString = columns.join(split);
          if(quoat){
            var replaceTo = '"$1"';
            if(quoat == "'"){
              replaceTo = "'$1'";
            }
            alertString =  alertString.replace(/([^:\[\]\{\},]+)/g,replaceTo);
          }
          layer.alert(alertString);
          break;
        case 'toJson':
          var json = {},formatter = $('#dateFormat').val();
          for(var i=0;i<columns.length;i++){
            var col = columns[i],
                $li = $('#'+col),
                $selectType = $li.find('.selected'),
                $length = $selectType.find('input'),
                generateType = $selectType.attr('generate'),
                length = $length.val();
            switch(generateType){
            case 'numeric':
              json[col] = generate.num(length);
              break;
            case 'alpha':
              json[col] = generate.letters(length);
              break;
            case 'mix':
              json[col] = generate.words(length);
              break;
            case 'date':
              json[col] = formatTime(generate.date(),formatter);
              break;
            case 'idcard':
              json[col] = generate.idcard();
              break;
            }
          }//end for
          $request.val(JSON.stringify(json));
        }
      }
      /**
       * 选择类型事件处理
       */
      function selectType(e){
        var $target = $(e.target).closest('.btn');
        $target.addClass('selected').siblings().removeClass('selected');
      }
      var EVENTS = [{selector:'#columnCmd button.btn,#responseOpts button.btn',types:['click'],handler:eventHandler},
                    {parent:'#typeMatch',selector:'.btn',types:['click'],handler:selectType}];
      regPageEvents(EVENTS);
    }
    
    return {
      refreshView:refreshView,
      bindEvents:bindEvents
    }
  })(this)
  
  $.extend(Request.prototype,{
    init:function(){
      $(':checkbox[name=isJsonp]').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green'
      });
      var reqs = cacheStorage.getLocal('reqs') || [];
      $('#requestUrl input[name=url]').autocomplete(reqs,{
        minChars: 0,
        matchContains: "word",
        autoFill: false,
        formatItem: function(row, i, max) {
          return i + "/" + max + ": " + row.method +'-'+(row.isJsonp ? 'jsonp':'普通' )+ " [" + row.url + "]";
        },
        formatMatch: function(row, i, max) {
          return row.url;
        },
        formatResult: function(row) {
          return row.url;
        }
        
      }).result(selectResult);
      
      /*
       * 用户选择某一条记录后触发 
       * @param event 事件对象. event.type为result.
       * @param data 选中的数据行.
       * @param formatted formatResult函数返回的值
       */
      function selectResult(event, row, formatted){
        $('#requestUrl').find('input[name=method]').val(row.method);
        $('#request textarea').val(row.params);
        var $jsonp = $('#requestUrl').find('input[name=isJsonp]');
        if(row.isJsonp){
          $jsonp.iCheck('check');
        }else{
          $jsonp.iCheck('uncheck');
        }
      }
    },
    relayout:function(clientWidth,clientHeight){
     var typeMatchHeight = clientHeight - $('#columnInput').outerHeight() - $('#columnCmd').outerHeight(),
       responseHeight = clientHeight - $('#request').outerHeight() - $('#requestUrl').outerHeight() - $('#responseOpts').outerHeight();
     
     $('#typeMatch').height(typeMatchHeight);   // 减去本身的一个 padding-top
     $('#response').height(responseHeight);
    },
    bindEvents:function(){
      typeMatch.bindEvents();//给类型匹配区绑定事件
      var EVENTS = [{selector:$columnInput,types:['keyup','blur'],handler:getColumns},
                    {selector:'#requestUrl input[type=button].submit',types:['click'],handler:sendRequest}];
      
      regPageEvents(EVENTS);
      
      /**
       * 发起请求
       */
      function sendRequest(e){
        var method = $('#requestUrl input[name=method]').val(),
            url = $('#requestUrl input[name=url]').val(),
            params = $('#request textarea').val(),
            options = {type:method,url:url,data:params};
        var isJsonp = $('input[name=isJsonp]').is(':checked');
        options.isJsonp = isJsonp;
        
        ajaxRequest(options,function(data){
          var currentReq = {url:url,method:method,params:params,isJsonp:isJsonp};
          save2Local(currentReq);
          $('#response').text(JSON.stringify(data));
        });
        
        /**
         * 存储本次请求
         */
        function save2Local(currentReq){
          var reqs = cacheStorage.getLocal('reqs') || [];
          if(reqs && reqs.length > 0){
            //比较是否有相同的请求,如果有相同的请求,则过滤不加入数据
            for(var i=0;i<reqs.length;i++){
              var eq = true;
              for(var key in currentReq){
                if(currentReq[key] != reqs[i][key]){
                  eq = false;
                  break;
                }
              }
              if(eq){
                return ;
              }
            }
          }
          reqs.push(currentReq);
          cacheStorage.addLocal('reqs',reqs);
        }
      }
      /**
       * 生成需要匹配的所有列信息
       */
      function getColumns(e){
        var preString = $columnInput.val();
        //按换行,空白,逗号,分号分割,然后去掉两边空格,去掉空行,去掉双引号,单引号
        if(preString){
          var cols = preString.split(/[\n\s,;]/g);
          if(cols && cols.length>0){
            var columns = [];
            for(var i=0;i<cols.length;i++){
              var currentCol = $.trim(cols[i]).replace(/[\'\"]/g,'');
              if(currentCol){
                columns.push(currentCol);
              }
            }
            //如果存在列,则创建列 json 创建工具
//            logger.debug(columns)
            typeMatch.refreshView(columns);
          }
        }
      }
   
    }
  });
  
  ExtendPage.call(request, 'request', Request);
})(window.jQuery)