app.service('uploadService',function($http){
	
	//上传文件
	this.uploadFile=function(){
		//html5新增的类，用于上传
		var formdata=new FormData();
		formdata.append('file',file.files[0]);//file 文件上传框的name
		
		return $http({
			url:'../upload.do',		
			method:'post',
			data:formdata,
			headers:{ 'Content-Type':undefined },//上传文件·必须指定为undefined，否则默认类型为json
			transformRequest: angular.identity//对整个表单进行二进制序列化。两者搭配使用，实现了文件上传			
		});
		
	}
	
	
});