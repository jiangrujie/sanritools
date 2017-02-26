/**
 * Created by sanri on 2016/9/16.
 */
(function($,undefined){
    /**
     * 组件初始化,只是构建骨架,此时还没有数据
     * @param $this
     * @param opts
     */
    function init(jq,opts){
        var $panel,$title,$dataUl;

        $panel = jq.addClass('select-panel');
        $title = $('<div class="select-head"><h3>'+opts.title+'</h3></div>').appendTo($panel);
        $dataUl = $('<ul class="select-content"></ul>').appendTo($panel);

        //确定位置和大小
        $panel.css({
            width:opts.width,
            height:opts.height,
            top:opts.top,
            left:opts.left
        });

        //title 默认 40 px ,则 dataUl 高度为 height - 40 - 2(border 高度)
        $dataUl.css({
            height:(opts.height - 42)
        });

        return {
            panel:$panel,
            title:$title,
            dataUl:$dataUl
        };
    }

    /**
     * 主数据刷新方法
     * 如果想清空数据,请传 []
     * @param jq
     */
    function refreshData(jq,data){
        var state = jq.data('moveselect'),
            opts = state.opts,
            $dataUl = state.dataUl,
            _this = this;
        //优先 url 请求
        if(opts.url){
            ajaxRequest({url:opts.url,type:'get'},function(_data){
                if(opts.onDataChange && $.isFunction(opts.onDataChange)){
                    opts.onDataChange.call(_this,opts.data,_data);
                }
                opts.data = _data;
                renderUl(jq);
            });
            return ;
        }
        if(data){
            if(opts.onDataChange && $.isFunction(opts.onDataChange)){
                opts.onDataChange.call(_this,opts.data,data);
            }
            opts.data = data;
            renderUl(jq);
        }
        return $dataUl;
    }

    /**
     * 渲染数据区
     * @param jq
     */
    function renderUl(jq){
        var state = jq.data('moveselect'),
            opts = state.opts,
            $dataUl = state.dataUl,
            data = opts.data;
        if(data && $.isArray(data)){
            $dataUl.empty();
            var lis = [];
            for(var i=0;i<data.length;i++){
                //格式化每个结点
                var node = opts.formatter.call(this,data[i]);

                lis.push('<li id="'+node.id+'">');
                if(opts.checkbox){
                    lis.push('<span class="checkbox clearfix"><i></i></span>');
                }
                lis.push('<a href="#" class="clearfix">');
                if(opts.msg && node.msg){
                    lis.push('<span class="msg">'+(node.msg)+'</span>');
                }
                if(opts.rownum){
                    lis.push('<span class="num">'+(i+1)+'</span>');
                }
                if(opts.icon){
                    lis.push('<i class="'+(node.iconCls || opts.iconCls)+'" ></i>');
                }
                lis.push('<span class="text">'+node.text+'</span></a></li>');
            }
            $dataUl.append(lis.join(''));
        }
    }

    function bindEvents(jq){
        var state = jq.data('moveselect'),
            opts = state.opts,
            $dataUl = state.dataUl,
            $title = state.title,
            $panel = state.panel;

        var EVENTS = [
            {selector:$title,types:['mouseover','mouseout'],handler:mouseOverOut},
            {selector:'>li',types:['click'],handler:itemClick,parent:$dataUl},
        ];
        regPageEvents(EVENTS);

        function mouseOverOut(e){
            var type = e.type;
            switch(type){
                case 'mouseover':
                    $title.addClass('hover');
                    break;
                case 'mouseout':
                    $title.removeClass('hover');
                    break;
                default:
                    logger.error('不支持的事件');
            }
        }

        function itemClick(e){
            var $li = $(e.target).closest('li');
            $li.toggleClass('select');
            $li.find('span.checkbox').toggleClass('checked');
            if(opts.onSelect && $.isFunction(opts.onSelect)){
                opts.onSelect.call(this,$li.attr('id')); //TODO
            }
        }

        $panel.draggable({
            handle:'.select-head',
            onStartDrag:function(e){
                //logger.debug(state);
                $title.addClass('down');
            },
            onDrag:function(e){
                //logger.debug(state);
            },
            onStopDrag:function(){
                //logger.debug(state);
                $title.removeClass('down');
            }
        });

    }

    $.fn.moveselect=function(options,params){
        if(typeof options == 'string'){
            var method = $.fn.moveselect.methods[options];
            if(method){
                return method(this,params);
            }
            return ;
        }
        options = options || {};

        return this.each(function(){
           var state = $.data(this,'moveselect'),$this= $(this);
           if(state){
               $.extend(state.opts,options);
           }else{
               state = $.extend({}, $.fn.moveselect.defaults,options);
               state = $.data(this,'moveselect', $.extend({opts:state},init($(this),state)));
           }
           refreshData($(this),options.data);
           bindEvents($this);
//           requireJs(['/js/jquery.draggable.js'],function(){});
        });
    }
    $.fn.moveselect.defaults={
        width:400,
        height:300,
        url:undefined,
        data:[],
        title:'',
        rownum:true,
        checkbox:true,
        msg:true,
        icon:true,
        top:0,
        left:0,
        iconCls:'fa fa-user',
        interval:1000,
        onSelect:function(data){},
        onDataChange:function(preData,newData){},
        formatter:function(node){
            return node;
        }
    }
    $.fn.moveselect.methods={

    }
})(window.jQuery)