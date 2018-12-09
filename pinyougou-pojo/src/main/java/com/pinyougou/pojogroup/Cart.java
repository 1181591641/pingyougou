package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbOrderItem;
/**
 * 购物车对象

 * @ClassName: Cart

 * @Description: TODO

 * @author: zmz

 * @date: 2018年12月3日 下午6:21:24
 */
public class Cart  implements Serializable{
private String sellerId;//商家id
private String sellerName;//商家名称	


private List<TbOrderItem> orderItemList;//购物车明细列表
public String getSellerId() {
	return sellerId;
}
public String getSellerName() {
	return sellerName;
}
public List<TbOrderItem> getOrderItemList() {
	return orderItemList;
}
public void setSellerId(String sellerId) {
	this.sellerId = sellerId;
}
public void setSellerName(String sellerName) {
	this.sellerName = sellerName;
}
public void setOrderItemList(List<TbOrderItem> orderItemList) {
	this.orderItemList = orderItemList;
}


}
