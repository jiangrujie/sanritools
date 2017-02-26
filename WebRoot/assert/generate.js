(function($,global){
  var ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  var LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  var NUMBERCHAR = "0123456789";
  //随机数生成 如 length=10 生成包括从 0 到 10 的任何数
  function random(length){
    return Math.round(Math.random() * length);
  }
  //随机生成 不包括头或者尾
  function randomNo(length,head,foot){
    head = head || true;
    foot = foot || true;
    if(head && foot){return random(length);}
    if(head){return random(length - 1);}
    if(foot){return random(length -1) + 1;}
  }
  //由时间戳格式化日期
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
  //由日期字符串形式生成时间戳
  function unFormatTime(time, format){
    format = format || 'yyyyMMdd';
    time = time || formatTime(new Date().getTime(),'yyyyMMdd');
    format = format.replace(/yyyy|MM|dd/g,function(a){
      switch(a){
      case 'yyyy':
        return '(\\d{4})';
      case 'MM':
        return '(\\d{2})';
      case 'dd':
        return '(\\d{2})';
      }
    });
    var regex = new RegExp(format,'g');
    var formatTimeStr = time.replace(regex,'$1/$2/$3');
    return new Date(formatTimeStr).getTime();
  }
  var Generate = function(){}
  Generate.prototype={
    words:function(length,src){
      length = length || 0;
      src = src || ALLCHAR;
      var res = '';
      while(length-- != 0){
        res += src.charAt(random(src.length - 1));
      }
      return res;
    },
    chinese:function(length){
      length = length || 0;
      var res = ''
      while(length-- != 0){
        eval( "var word=" +  '"\\u' + (randomNo(20901,false,true) + 19968).toString(16)+'"');
        res += word;
      }
      return res;
    },
    letters:function(length){
      return this.words(length,LETTERCHAR);
      //return this.words.call(this,length,LETTERCHAR);
    },
    //生成定长的数字字符串,前面可能有 0 
    numStr:function(length){
      return this.words(length,NUMBERCHAR);
    },
    string:function(length){
      return this.words(length,ALLCHAR);
    },
    //生成定长的数字,返回字符串类型,前面不带 0
    num:function(length){
      /*var preNum = this.numStr(length);
      var numStr = parseInt(preNum) + '';
      while(numStr.length != length){
        this.num(length);
      }
      return numStr;*/
      var beginNumStr = NUMBERCHAR.substring(1);
      var beginNum = beginNumStr.charAt(random(beginNumStr.length - 1));
      var afterNum = this.numStr(length - 1);
      return beginNum + afterNum;
    },
    //由开始日期和结束日期生成一个日期;传入的格式由 format 控制;返回时间戳
    date:function(format,begin,end){
      format = format || 'yyyyMMdd';
      begin = begin || formatTime(new Date(0).getTime(),format);
      end = end || formatTime(new Date().getTime(),format);
      var length = unFormatTime(end,format) - unFormatTime(begin,format);
      return parseInt(unFormatTime(begin,format)) + parseInt(random(length));
    },
    idcard:function(area,yyyyMMdd,sex){
      sex = (sex == 0 ? 2:sex); // 0 也认为是假了
      area = area || '430124';
      yyyyMMdd = yyyyMMdd || formatTime(this.date(),'yyyyMMdd');
      sex = sex || 1;
      //sex 男为奇数,女为偶数
      var sno = this.numStr(3);
      while(parseInt(sno) % 2 != sex % 2 ){
        sno = this.num(3);
      }
      var id17 = area + yyyyMMdd + sno;
      return id17 + getVerify(id17);
      function getVerify(id17){
        var power = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
        for (i = 0,sum=0; i < id17.length; i++) {
          sum += id17[i] * power[i];
        }
        var t = sum % 11;
        var r = (12 - t) % 11;
        return r == 10?'x':r;
      }
      
    }
  }
  
  global.Generate = Generate;
  global.generate = new Generate();
  
  //扩展 logger 用于测试多数据输出
  logger.debugMulti = function(){
    for(var i=0;i<arguments.length;i++){
      logger.debug(arguments[i],'multiDebug');
    }
  }
})(window.jQuery,window)