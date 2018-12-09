//控制层 
app.controller('itemCatController', function($scope, $controller,
		itemCatService,typeTemplateService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		itemCatService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		itemCatService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		itemCatService.findOne(id).success(function(response) {
			$scope.entity = response;
			
//			$scope.entity.typeId=$scope.entity.typeId.id
		
			
		});
	}
	
	
	

	// 判断用户名是否重复
	$scope.Names = function() {
	
		if ($scope.entity.id == '') {// 如果有ID
			itemCatService.Names($scope.entity).success(
					function(response) {
						if (response.success) {
							alert(response.message);
							$('#myButton').prop('disabled', true); // 按钮灰掉，且不可点击。
						} else {
							$('#myButton').prop('disabled', false); // 按钮灰掉，且不可点击。
						}
					});
		}
	}
	$scope.parentId=0;//上级ID 
	//根据上级ID显示下级列表  
 	$scope.findByParentId=function(parentId){  	 
 		$scope.parentId=parentId;//记住上级ID 

 	}

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			$scope.entity.typeId=$scope.entity.typeId.id
			serviceObject = itemCatService.update($scope.entity); // 修改
		} else {
			$scope.entity.typeId=$scope.entity.typeId.id
			
			$scope.entity.parentId=$scope.parentId;//赋予上级ID 
			serviceObject = itemCatService.add($scope.entity);// 增加
		}
		console.log($scope.entity);
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
		
				$scope.findByParenId($scope.parentId)
				
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		itemCatService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.findByParenId($scope.parentId)
			
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		itemCatService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	}

	// 商品分类
	$scope.findByParenId = function(parentId) {
		itemCatService.findByParenId(parentId).success(function(response) {
			$scope.list = response;
		});
	}

	$scope.grade = 1;// 当前级别
	// 设置级别
	$scope.setGrade = function(value) {
		$scope.grade = value;
	}
	$scope.selectList = function(p_entity) {

		
		if ($scope.grade == 1) {
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		}
		if ($scope.grade == 2) {
			$scope.entity_1 = p_entity;
			$scope.entity_2 = null;
		}
		if ($scope.grade == 3) {
			$scope.entity_2 = p_entity;
		}
		$scope.findByParenId(p_entity.id);
	}
	
	$scope.typeList = {data : []};
	// 读取商品列表
	$scope.findTypeList = function() {
		typeTemplateService.selectOptionList().success(function(response) {
			$scope.typeList = {
				data : response
			};
		});
	}
	
});
