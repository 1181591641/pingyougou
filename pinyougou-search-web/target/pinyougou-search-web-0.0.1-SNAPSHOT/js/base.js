var app=angular.module('pinyougou',[]);
//定义过滤器
app.filter('trustHtml',function($sce){
return function(data){//传入的参数时被过滤的内容
	return $sce.trustAsHtml(data);//返回的内容为过滤之后的内容(html安全认证)
}
	
});