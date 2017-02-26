(function(){
  var pkg = top.regNamespace('business');
  pkg.foodedit = {};
  $(init);
  function init(){
    render();
    $.validator.setDefaults({
      highlight: function(e) {
        $(e).closest(".form-row").removeClass("has-success").addClass("has-error")
      },
      success: function(e) {
        e.closest(".form-row").removeClass("has-error").addClass("has-success")
      },
      errorElement: "span",
      errorPlacement: function(e, r) {
        e.appendTo(r.is(":radio") || r.is(":checkbox") ? r.parent().parent().parent() : r.parent())
      },
      errorClass: "help-block",
      validClass: "help-block"
    });
   var validate =  $('#foodEditForm').validate({
      rules:{
        name:{
          required:true,
          minlength:2
        },
        foodType:'required',
        quality:'required',
        taste:'required',
        money:'required',
        singleScore:'required'
      },
      messages:{
        name:'请输入食物名称,至少两个字'
      }
    }); 
   pkg.foodedit.validate = validate;
  }
  
  function render(){
    $('#foodEditForm').find('select[dict]').each(function(){
      var dictName = $(this).attr('dict');
      initDropDown(top.dicts[dictName],$(this));
      $(this).chosen({
        no_results_text:'没有搜索结果'
      });
    });
   var $monthSelect = $('#month').find('select');
   for(var i=1;i<=12;i++){
     $monthSelect.append('<option value="'+i+'">'+i+'</option>')
   }
   $monthSelect.chosen({
     no_results_text:'没有搜索结果'
   });
  }
  
  function initDropDown(dict,$select){
    if(dict){
      for(var key in dict){
        $select.append('<option value="'+key+'">'+dict[key]+'</option>');
      }
    }
  }
})(window.jQuery)