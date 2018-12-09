app.controller('cartController', function($scope, cartService) {

	$scope.findCartList = function() {
		cartService.findCartList().success(function(response) {
			$scope.cartList = response;
			$scope.totalValue = cartService.sum($scope.cartList);
		});

	}

	$scope.addGoodsToCartList = function(itemId, num) {
		cartService.addGoodsToCartList(itemId, num).success(function(response) {
			if (response.success) {
				$scope.findCartList();
			} else {
				alert(response.message);

			}
		});

	}
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
	
	$scope.checkedAll = function() {

		var check = $scope.check;
	
	if(check==undefined)
		{
		check=false;
		}
		if (check == false) {
			for (var i = 0; i < $scope.cartList.length; i++) {
				$scope.cartList[i].state = true;
				$scope.sellercheck=true;
			
		
				for(var j=0;j<$scope.cartList[i].orderItemList.length;j++){
					$scope.selectIds.push($scope.cartList[i].orderItemList[j].itemId);
				}
			}
			$scope.sellercheck=true;
		} else {
			for (var i = 0; i < $scope.cartList.length; i++) {
				$scope.cartList[i].state = false;
			}

			$scope.selectIds=[];
		}
	}

	$scope.status = function(itemId, num) {
		cartService.status(itemId, num).success(function(response) {

			$scope.findCartList();

		});

	}
	$scope.cartRemove = function(itemId) {
		cartService.cartRemove(itemId).success(function(response) {
			if (response.success) {
				$scope.findCartList();
			} else {
				alert(response.message);

			}
		});

	}
	
	
	

	//获取当前用户的地址列表
	$scope.findAddressList=function(){
		cartService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				console.log($scope.addressList);
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}					
				}
				
			}
		);		
	}
	
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;		
	}
	
	//判断某地址对象是不是当前选择的地址
	$scope.isSeletedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}		
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=cartService.update( $scope.entity ); //修改  
		}else{
			serviceObject=cartService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findAddressList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	
	//选择支付类型
	//订单对象
	$scope.order={paymentType:'1'};
	$scope.seletPayType=function(type){
		$scope.oder.paymentType=type;
	}
	
	//保存订单
	$scope.submitOrder=function(){
		console.log($scope.address);
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		
		cartService.submitOrder( $scope.order ).success(
				function(response){
					if(response.success){
						//页面跳转
						if(	$scope.order.paymentType=='1'){//如果是微信支付，跳转到支付页面
							location.href="pay.html";
						}else{//如果货到付款，跳转到提示页面
							location.href="paysuccess.html";
						}
						
					}else{
						alert(response.message);	//也可以跳转到提示页面				
					}
		});
	}
});