//控制层 
app.controller('goodsController', function($scope, $controller, goodsService,
		uploadService, itemCatService, typeTemplateService, $location) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		goodsService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		goodsService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}
	var id = $location.search()['id'];
	// 查询实体
	$scope.findOne = function(id) {
		var id = $location.search()['id'];

		if (id == null) {
			return;
		}
		goodsService.findOne(id).success(function(response) {
			$scope.entity = response;
			//商品介绍
			editor.html($scope.entity.goodsDesc.introduction);
			//商品图片
			$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
			//扩展属性
			$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
			//规格选项
			$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems); 
			//转换sku列表中的规格对象
			for(var i=0;i< $scope.entity.itemList.length;i++ ){
				$scope.entity.itemList[i].spec=  JSON.parse($scope.entity.itemList[i].spec);					
			}
			
			});
	}



	//保存 
	$scope.save=function(){	
		$scope.entity.goodsDesc.introduction=editor.html();
		
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert("保存成功");
					location.href='goods.html';
					
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		goodsService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		goodsService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	}

	// 上传图片
	$scope.uploadFile = function() {
		uploadService.uploadFile().success(function(response) {
			if (response.success) {
				$scope.image_entity.url = response.message;
			} else {
				alert(response.message);
			}
		});
	}
	// 创建上传图片和规格选项初始化参数
	$scope.entity = {
		goodsDesc : {
			itemImages : [],
			specificationItems : []
		}
	};

	// 将当前上传的图片实体存入图片列表
	$scope.add_image_entity = function() {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
	}

	// 移除图片
	$scope.remove_image_entity = function(index) {
		$scope.entity.goodsDesc.itemImages.splice(index, 1);
	}

	// 查询一级商品分类
	$scope.selecItemCat1List = function() {
		itemCatService.findByParenId(0).success(function(response) {
			$scope.ItemCat1List = response;
		});
		console.log($scope.ItemCat1List);
	}

	// 查询二级商品分类
	// $scope.$watch为监控的意思，第一个参数为要监控的值，然后function里的参数为，第一个为更新后的值，第二个为更新前的值
	$scope.$watch('entity.goods.category1Id', function(newValue, oldValue) {
		if ($scope.ItemCat1List == undefined) {
			return;
		}

		itemCatService.findByParenId(newValue).success(function(response) {
			$scope.ItemCat2List = response;

		});



	});

	// 查询三级商品分类
	$scope.$watch('entity.goods.category2Id', function(newValue, oldValue) {
		console.log($scope.ItemCat2List);
		if ($scope.ItemCat1List == undefined) {
			return;
		}

		itemCatService.findByParenId(newValue).success(function(response) {
			$scope.ItemCat3List = response;
		});

	});
	if (id == null) {
		// 一级目录内容变化，清空三级目录和对应模版id
		$scope.clearCate1 = function() {
			if ($scope.ItemCat3List != null) {
				// 清空模版id
				$scope.entity.goods.typeTemplateId = '';
				$scope.ItemCat2List = '';
				// 清空3级目录
				$scope.ItemCat3List = '';
				// 清空扩展属性
				$scope.entity.goodsDesc.customAttributeItems = null;
				// 清空规格
				$scope.specList = '';

			}

		};

		// 二级目录内容变化，清空三级目录和对应模版id
		$scope.clearCate2 = function() {
			if ($scope.ItemCat3List != null) {
				// 清空模版id
				$scope.entity.goods.typeTemplateId = '';
				// 清空扩展属性
				$scope.entity.goodsDesc.customAttributeItems = null;
				// 清空3级目录
				$scope.ItemCat3List = '';
				// 清空扩展属性
				$scope.specList = '';
			}
		};
	}
	// 读取模板id
	$scope.$watch('entity.goods.category3Id', function(newValue, oldValue) {
		if ($scope.ItemCat3List != null) {
			itemCatService.findOne(newValue).success(function(response) {
				$scope.entity.goods.typeTemplateId = response.typeId;

			});
		}
	});

	$scope.$watch('entity.goods.typeTemplateId', function(newValue, oldValue) {

		if ($scope.ItemCat1List == undefined) {
			return;
		}

		// 品牌下拉列表
		typeTemplateService.findOne(newValue).success(
				function(response) {
					$scope.typeTemplate = response;
					$scope.typeTemplate.brandIds = JSON
							.parse($scope.typeTemplate.brandIds);
				});

		// 扩展属性
		typeTemplateService.findOne(newValue).success(
				function(response) {
					$scope.typeTempeCus = response;
					if(id==null){
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTempeCus.customAttributeItems);
					}
				});

		// 读取规格
		typeTemplateService.findSpecList(newValue).success(function(response) {
			$scope.specList = response;
		});

	});

	// 规格选项
	$scope.updateSpecAttribute = function($event, name, value) {
		// 调用集合判断方法传递参数
		var object = $scope.serchObjectByKey(
				$scope.entity.goodsDesc.specificationItems, 'attributeName',
				name);
		// 判断返回值
		if (object != null) {
			// 被点击
			if ($event.target.checked) {
				// 如果不为空。则说明该集合存在
				// 直接追加数据即可
				object.attributeValue.push(value);
			} else {
				// 取消点击
				object.attributeValue.splice(object.attributeValue
						.indexOf(value), 1);
				// 如果该集合的attributeValue的value的长度等于0，则表示没有被勾选的，需要删除该集合
				if (object.attributeValue.length == 0) {
					$scope.entity.goodsDesc.specificationItems.splice(
							$scope.entity.goodsDesc.specificationItems
									.indexOf(object), 1)

				}
			}
		} else {

			// 如果返回值为空，则说明没有该数据，需要创建一个新的集合
			$scope.entity.goodsDesc.specificationItems.push({
				"attributeName" : name,
				"attributeValue" : [ value ]
			});
		}

	}

	/*
	 * 选择移动3g时： [{"attributeName":"网络","attributeValue":["移动3G"]}] 选择移动3g和移动4g时
	 * [{"attributeName":"网络","attributeValue":["移动3G"]}]
	 * [{"attributeName":"网络","attributeValue":["移动4G"]}] 选择移动3g和移动4g和内存16g时：
	 * [{"attributeName":"网络","attributeValue":["移动3G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
	 * [{"attributeName":"网络","attributeValue":["移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
	 * 选择移动3g和移动4g和内存16g和内存32g
	 * [{"attributeName":"网络","attributeValue":["移动3G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
	 * [{"attributeName":"网络","attributeValue":["移动3G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
	 * [{"attributeName":"网络","attributeValue":["移动3G"]},{"attributeName":"机身内存","attributeValue":["32G"]}]
	 * [{"attributeName":"网络","attributeValue":["移动3G"]},{"attributeName":"机身内存","attributeValue":["32G"]}]
	 */
	// 创建SKU列表
	$scope.createItemList = function() {
		// 列表初始化
		$scope.entity.itemList = [ {
			spec : {},
			price : 0,
			num : 99999,
			status : '0',
			isDefault : '0'
		} ];
		// 获取规格选项
		var items = $scope.entity.goodsDesc.specificationItems;
		// 遍历规格选项，进行循环添加
		for (var i = 0; i < items.length; i++) {
			$scope.entity.itemList = addColumn($scope.entity.itemList,
					items[i].attributeName, items[i].attributeValue);

		}

	}

	addColumn = function(list, columnName, columnValues) {

		var newList = [];
		// 初始化定义值
		for (var i = 0; i < list.length; i++) {
			// 接收循环初始化值
			var oldRow = list[i];
			// 循环遍历规则选项value
			for (var j = 0; j < columnValues.length; j++) {
				// 深克隆
				var newRow = JSON.parse(JSON.stringify(oldRow));
				// 将value值赋值给对应的规格选择名称
				newRow.spec[columnName] = columnValues[j];
				// 添加
				newList.push(newRow);
			}
		}
		return newList;
	}
	/**
	 * 选择规格为一个时： [{drice":0,"num":99999,"status":"0","isDefault":"0"}]
	 * 选择两个规格一样时：
	 * 
	 * [{"spec":{"网络":"移动3G"},"price":0,"num":99999,"status":"0","isDefault":"0"},{"spec":{"网络":"移动4G"},"price":0,"num":99999,"status":"0","isDefault":"0"}]
	 * 选择两个不一样的规则时
	 * [{"spec":{"网络":"移动3G","机身内存":"16G"},"price":0,"num":99999,"status":"0","isDefault":"0"}]
	 * 
	 * [{"spec":{"网络":"移动3G","机身内存":"16G"},"price":0,"num":99999,"status":"0","isDefault":"0"},
	 * {"spec":{"网络":"移动4G","机身内存":"16G"},"price":0,"num":99999,"status":"0","isDefault":"0"}]
	 * 
	 */
	// 商品状态显示
	$scope.status = [ '未审核', '已审核', '审核已通过', '已关闭' ];
	$scope.itemCatList = [];// 商品分类列表
	// 商品分类显示
	$scope.findItemCatList = function() {
		// 获取全部商品分类
		itemCatService.findAll().success(function(response) {
			// 遍历商品分类
			for (var i = 0; i < response.length; i++) {
				$scope.itemCatList[response[i].id] = response[i].name;
			}

		});

	}
	//判断规格与规格选项是否应该被勾选
	$scope.checkAttributeValue=function(specName,optionName){
		var items= $scope.entity.goodsDesc.specificationItems;
		var object =$scope.serchObjectByKey( items,'attributeName', specName);
		
		if(object!=null){
			if(object.attributeValue.indexOf(optionName)>=0){//如果能够查询到规格选项
				return true;
			}else{
				return false;
			}			
		}else{
			return false;
		}		
	}
});
