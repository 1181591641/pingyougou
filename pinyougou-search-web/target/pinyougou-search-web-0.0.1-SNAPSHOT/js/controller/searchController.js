app
		.controller(
				'searchController',
				function($scope,$location, searchService) {
					// 定义搜索对象的结果股
					$scope.searchMap = {
						'keywords' : '',
						'category' : '',
						'brand' : '',
						'spec' : {},
						'price' : '',
						'pageNo' : 1,
						'pageSize' : 40,
						'sort':'',
						'sortField':''
					};
					// 搜索
					$scope.search = function() {
						$scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);// 转换为数字
						searchService.search($scope.searchMap).success(
										function(response) {
											$scope.resultMap = response;
											buildPageLabel();

											for (var i = 0; i < response.brandList.length; i++) {
												if ($scope.searchMap.keywords.indexOf(response.brandList[i].text )>=0) {
													$scope.resultMap.brandList = '';
												}
											}
										});
					}

					// 分页栏
					// 构建分页栏
					buildPageLabel = function() {
						// 构建分页栏
						$scope.pageLabel = [];
						var firstPage = 1;// 开始页码
						var lastPage = $scope.resultMap.totalPage;// 截止页码
						$scope.firstDot = false;// 前面有点
						$scope.lastDot = true;// 后边有点

						if ($scope.resultMap.totalPage > 7) { // 如果页码数量大于5

							if ($scope.searchMap.pageNo <=5) {// 如果当前页码小于等于3
																// ，显示前5页
								lastPage = 7;
								$scope.firstDot = false;// 前面没点
							} else if ($scope.searchMap.pageNo >= $scope.resultMap.totalPage - 2) {// 显示后5页
								firstPage = $scope.resultMap.totalPage - 4;
								$scope.lastDot = false;// 后边没点
							} else { // 显示以当前页为中心的5页
								if($scope.searchMap.pageNo-2>2){
		
									$scope.firstDot = true;// 前面有点
									
								}
								firstPage = $scope.searchMap.pageNo - 2;
								lastPage = $scope.searchMap.pageNo + 2;
							}
						} else {
							$scope.firstDot = false;// 前面无点
							$scope.lastDot = false;// 后边无点
						}

						// 构建页码
						for (var i = firstPage; i <= lastPage; i++) {
							$scope.pageLabel.push(i);
						}
					}
					
					

					// //判断当前页是否为第一页
					 $scope.isTopPage=function(){
						 
					 if($scope.searchMap.pageNo==1){
					 return true;
					 }else{
					 return false;
					 }
					 }

					// 判断当前页是否为最后一页
					$scope.isEndPage = function() {
						
					if($scope.resultMap!=undefined){
							if ($scope.searchMap.pageNo == $scope.resultMap.totalPage) {
								return true;
							} else {
								return false;
							}
					}
						
					}

					// 添加搜索项 改变searchMap的值
					$scope.addSearchItem = function(key, value) {

						if (key == 'category' || key == 'brand'
								|| key == 'price') {// 如果用户点击的是分类或品牌
							$scope.searchMap[key] = value;

						} else {// 用户点击的是规格
							$scope.searchMap.spec[key] = value;
						}
						$scope.search();// 查询
					}

					// 撤销搜索项
					$scope.removeSearchItem = function(key) {
						if (key == 'category' || key == 'brand'
								|| key == 'price') {// 如果用户点击的是分类或品牌
							$scope.searchMap[key] = "";
						} else {// 用户点击的是规格
							delete $scope.searchMap.spec[key];
						}
						$scope.search();// 查询
					}

					$scope.EmptyList = function(keywords) {
						$scope.searchMap = {
							'keywords' : keywords,
							'category' : '',
							'brand' : '',
							'spec' : {},
							'price' : '',
							'pageNo' : 1,
							'pageSize' : 40,
							'sort':'',
							'sortField':''
						};
						$scope.search();
					}

					// 分页查询

					$scope.queryByPage = function(pageNo) {
						if (pageNo < 1 || pageNo > $scope.resultMap.totalPage) {
							return;
						}
						$scope.searchMap.pageNo = pageNo;
						$scope.search();// 查询
					}

					//价格排序
					$scope.sortSerach=function(sort,sortField){
						$scope.searchMap.sort=sort;	
						$scope.searchMap.sortField=sortField;	
						$scope.search();// 查询
					}
					//加载关键字
					$scope.loadKeywords=function(){
						$scope.searchMap.keywords=$location.search()['keywords'];
						$scope.search();// 查询
					}
					
				});