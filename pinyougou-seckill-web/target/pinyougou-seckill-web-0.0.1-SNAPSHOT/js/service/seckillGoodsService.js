app.service('seckillGoodsService',function($http){
	//读取列表数据绑定到表单中
	this.findList=function(){
		return $http.get('seckillGoods/findList.do');
	}
	// 根据id返回商品详情页
	this.finOne=function(id){
		return $http.get('seckillGoods/finOne.do?id='+id);
	}
	// 秒杀商品订单
	this.submitOrder=function(seckillId){
		return $http.get('seckillOrder/submitOrder.do?seckillId='+seckillId);
	}
});