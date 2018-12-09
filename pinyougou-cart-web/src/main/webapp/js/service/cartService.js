//购物车服务层
app.service('cartService',function($http){
	//购物车列表
	this.findCartList=function(){
		return $http.get("cart/findCartList.do");
		
	}
	//添加商品到购物车
	this.addGoodsToCartList=function(itemId, num){
		return $http.get("cart/addGoodsToCartList.do?itemId="+itemId+"&num="+num);
		
	}
	//删除
	this.status=function(itemId,status){
		return $http.get("cart/status.do?itemId="+itemId+"&status="+status);
		
	}
	//求和
	this.sum=function(cartList)
	{
	var totalValue={totalNum:0,totalMoney:0};
	
		for(var i=0;i<cartList.length;i++){
			for(var j=0;j<cartList[i].orderItemList.length;j++){
			var orderItem=cartList[i].orderItemList[j];
			
			/*if(cartList[i].state=="1"){*/
			totalValue.totalNum+=orderItem.num;
			totalValue.totalMoney+=orderItem.totalFee;
			}
			
			/*}*/
		}
		return totalValue;
	}
	  //查询地址
	this.findAddressList = function() {
		return $http.get("address/findListByLoginUser.do");
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../address/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../address/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../address/delete.do?ids='+ids);
	}
	this.submitOrder=function(order){
		return $http.post("/order/add.do",order)
	}
	
});