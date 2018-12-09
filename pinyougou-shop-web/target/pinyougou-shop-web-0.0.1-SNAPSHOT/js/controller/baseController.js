//品牌控制层 
app.controller('baseController', function($scope) {

	// 重新加载列表 数据
	$scope.reloadList = function() {
		// 切换页码
		$scope.search($scope.paginationConf.currentPage,
				$scope.paginationConf.itemsPerPage);
	}

	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 10,
		perPageOptions : [ 10, 20, 30, 40, 50 ],
		onChange : function() {
			$scope.reloadList();// 重新加载
		}
	};

	$scope.selectIds = [];// 选中的ID集合

	// 更新复选
	$scope.updateSelection = function($event, id) {
		if ($event.target.checked) {// 如果是被选中,则增加到数组
			$scope.selectIds.push(id);
		} else {
			var idx = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(idx, 1);// 删除
		}
	}

	$scope.jsonToString = function(jsonString, key) {

		var json = JSON.parse(jsonString);
		var value = "";

		for (var i = 0; i < json.length; i++) {
			if (i > 0) {
				value += ",";
			}
			value += json[i][key];
		}

		return value;
	}

	// 查找数组中是否有重复元素
	/*
	 * 参数属性： list：需要传入的集合
	 *  key：传入的健值 
	 *  KeyValue：传入健值对应的属性值
	 */
	$scope.serchObjectByKey = function(list, key, keyValue) {
		// 遍历
		for (var i = 0; i < list.length; i++) {
			//判断该键值对是否有对应的属性值
			if (list[i][key] == keyValue)
				return list[i];
		}
		return null;

	}

});