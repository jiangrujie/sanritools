(function(){
	var pkg = top.regNamespace('business'), 
			app = pkg.app,uploadphoto;
	var UploadPhoto = function(){};
	uploadphoto = new UploadPhoto('.upload-content');
	
	$.extend(UploadPhoto.prototype,{
		init:function(){
			
		},
		relayout:function(clientWidth,clientHeight){
			
		},
		bindEvents:function(){
			
		}
	});
	
	ExtendPage.call(uploadphoto,'uploadphoto',UploadPhoto);
})(window.jQuery)