//品牌服务
  app.service('brandService', function($http) {

	// 查询品牌列表
	this.findBrandAll = function() {
		return $http.get('../brand/findAll.do');
	}
	// 分页
	this.findPage = function(page, size) {
		return $http.get('../brand/findPage.do?page=' + page + '&size=' + size
				+ '');
	}
	// 根据名称查询
	this.Names = function(entity) {
		return $http.post('../brand/Names.do', entity);
	}
	// 添加
	this.add = function(entity) {
		return $http.post('../brand/add.do', entity);
	}
	// 修改
	this.update = function(entity) {
		return $http.post('../brand/update.do', entity);
	}
	// 根据id查询
	this.findOne = function(id) {
		return $http.get('../brand/findOne.do?id=' + id);
	}
	// 删除
	this.dele = function(selectIds) {
		return $http.post('../brand/delete.do?ids=' + selectIds);
	}
	// 模糊查询
	this.search = function(page, size, searchEntity) {
		return $http.post('../brand/search.do?page=' + page + '&size=' + size,
				searchEntity);
	}
	//品牌下拉列表
	this.selectOptionList=function()
	{
		return  $http.get('../brand/selectOptionList.do');
	}
	

});