/**
 * Created by sanri on 2016/9/17.
 */
;(function(){
    if(!this.jQuery){
        throw new Error('this reset js require jquery based');
    }
    // 扩展几个方法
    /*
     * 去掉首尾空格
     * 用于兼容 ie
     */
    if (!String.prototype.trim) {
        String.prototype.trim = function() {
//      return this.replace(/(^\s*)|(\s*$)/g, '');// 将字符串前后空格,用空字符串替代。
            return $.trim(this);
        }
    }
    /*
     * 判断是否为空串
     * undefined,NaN 做空的处理
     * null,'',为空串
     * isTrim 是否去除两边空格 boolean ,默认 false
     */
    if(!String.prototype.isEmpty){
        String.prototype.isEmpty = function(isTrim){
//      if(this === undefined || this === NaN){return false;}
            if(this == null || this === undefined || this ===  NaN ){return true;}
            var source = this;
            if(isTrim){
                source = this.trim();
            }
            return source == '' || source.length == 0 ;
        }
    }
    /*
     * 判断字符串是否起始于某个字符串
     * 传入数字会转为字符串比较
     * 用于兼容 ie
     */
    if(!String.prototype.startsWith){
        String.prototype.startsWith = function(str){
            if(str.isEmpty() || str.length > this.length){
                return false;
            }
            var source = String(this),regex = new RegExp('^'+str);
            return regex.test(source);
        }
    }

    /*
     * 判断字符串是否以哪个字符串结尾
     * 用于兼容 ie
     */
    if(!String.prototype.endsWith){
        String.prototype.endsWith = function(str){
            if(str.isEmpty() || str.length > this.length){
                return false;
            }
            var source = String(this),regex = new RegExp(str+'$');
            return regex.test(source);
        }
    }

    /*
     * 计算字符串的长度中文算两个
     * isTrim 是否去除两边空格 boolean ,默认 false
     */
    if (!String.prototype.codeLength) {
        String.prototype.codeLength = function(isTrim) {
            var source = this;
            if(source.isEmpty(isTrim)){
                return 0;
            }
            if(isTrim){
                source = this.trim();
            }
            return source.replace(/[\u4e00-\u9fa5]/g,"**").length;
        }
    }
    /*
     * 格式化字符串
     用法：
     var cls="redclass",text="My name is ",
     str='<div class="{0}">{1} {2}</div>'.diy_format(cls, text,"cssrain");
     */
    String.prototype.diyFormat=function(){
        var args=arguments;
        return this.replace(/\{(\d+)\}/g, function(m, i){
            return args[i];
        });
    };

    /*
     * Array indexOf 方法
     * 用于兼容 ie
     * 比较复杂对象时需实现 comparable(el)
     * var a = [];
     * a.comparable = function(el){return el.index - this.index;}
     * 原来的 indexOf 可能不支持复杂对象比较,所以直接重写
     */
//  if(!Array.prototype.indexOf){
//    Array.prototype.indexOf = function(val){
//        var dec =  -1;
//        if(this.comparable && $.isFunction(this.comparable)){
//            for(var i=0;i<this.length;i++){
//                dec = this.comparable.call(this,el);
//                if(dec == 0){
//                    return i;
//                }
//            }
//        }
////    	if(Array.prototype.indexOf){ //如果不是比较的复杂对象,并且原来的 indexOf 存在的话,则直接使用原来的 indexOf,否则使用 jquery 提供的 $.inArray 方法
////    		return Array.prototype.indexOf.call(this,val);
////    	}
//        return $.inArray(val,this);
//    };
//  }

    /*
     * Array contains $.inArray()
     * 当需要比较复杂对象时,需实现 comparable 接口
     */
//    Array.prototype.contains = function(el){
//        return this.indexOf(el) != -1;
//    };
    /*
     * 移除元素,当传入object 为复杂对象时,需实现  comparable 接口
     * 只有为数字时,才会当成是移除 index ,否则为移除 object
     * 返回移除的元素,则没有移除元素时返回 null
     */
  //remove 因为在使用 fonticonpicker 的时候报错 by sanri at 2016/12/3
  //do not remove because fonticonpicker 在使用数组的时候用了 for in ,这里所有方法都得移除,所以暂时不做移除
    Array.prototype.remove = function(mix){
        var removeEl;
        if(typeof mix == 'number'){
            if(mix >= this.length || mix < 0){
                return null;
            }
            removeEl = this.splice(mix,1); //当移除没有元素时,得到的是空数组
            return removeEl.length > 0 ? removeEl[0] : null;
        }
        var index = this.indexOf(mix);
        return Array.prototype.remove.call(this,index);
    };

    //数组不提供 each 方法 ,使用 $.each

    /*
     * 数组去重复值,
     * 以后有更优的解决方案再替换,这个 indexOf 会很费事
     * 返回本身
     */
    Array.prototype.distinct = function(){
        var self = this,
            _a = this.concat().sort();
        _a.sort(function(a,b){
            if(a == b){
                var n = self.indexOf(a);
                self.splice(n,1);
            }
        });
        return self;
    };
    /*
     * 求与另一数组的交集 注:调用此方法本对象和目标数组都会调用去重方法
     * 复杂对象需实现 comparable
     */
    Array.prototype.intersect = function(other){
        if(!other || !$.isArray(other) || other.length == 0){
            return [];
        }
        other.distinct();this.distinct();
        var arr = [];
        for(var i=0;i<other.length;i++){
            if(this.contains(other[i])){
                arr.push(other[i]);
            }
        }
        return arr;
    };

    /*
     * 把输入值转换为布尔值
     * @method parseBool
     * @param {*}
     *            _input
     * @return bool
     */
    Window.prototype.parseBool = function(_input){
        if( typeof _input == 'string' ){
            _input = _input.replace( /[\s]/g, '' ).toLowerCase();
            if( _input && ( _input == 'false'
                    || _input == '0'
                    || _input == 'null'
                    || _input == 'undefined'
                )) _input = false;
            else if( _input ) _input = true;
        }
        return !!_input;
    }
})(window);