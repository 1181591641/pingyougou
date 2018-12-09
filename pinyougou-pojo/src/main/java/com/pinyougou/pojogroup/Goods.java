package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

/**
 * 商品组合实体类

 * @ClassName: Goods

 * @Description: TODO

 * @author: zmz

 * @date: 2018年11月16日 上午8:50:45
 */
public class Goods implements Serializable {
private TbGoods goods;//商品SPU基本信息
private TbGoodsDesc goodsDesc;//商品SUP扩展
private List<TbItem> itemList;//SKU列表

public TbGoods getGoods() {
	return goods;
}
public TbGoodsDesc getGoodsDesc() {
	return goodsDesc;
}

public List<TbItem> getItemList() {
	return itemList;
}
public void setItemList(List<TbItem> itemList) {
	this.itemList = itemList;
}
public void setGoods(TbGoods goods) {
	this.goods = goods;
}
public void setGoodsDesc(TbGoodsDesc goodsDesc) {
	this.goodsDesc = goodsDesc;
}
@Override
public String toString() {
	return "Goods [goods=" + goods + ", goodsDesc=" + goodsDesc + ", itemList=" + itemList + "]";
}




}
