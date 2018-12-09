package com.pinyougou.cart.service;

import java.util.List;

import com.pinyougou.pojogroup.Cart;

/**
 * 购物车接口
 * 
 * @ClassName: CartService
 * 
 * @Description: TODO
 * 
 * @author: zmz
 * 
 * @date: 2018年12月3日 下午7:40:15
 */
public interface CartService {
	/**
	 * 添加商品到购物车
	 * 
	 * @param cartList
	 *            订单列表
	 * @param itemId
	 *            商家id
	 * @param num
	 *            数量
	 * @return
	 */
	public List<Cart> andGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

	public void status(Long itemId, String status);

	/**
	 * 从redis中取出购物车
	 * 
	 * @param usrname
	 *            用户名名称
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String usrname);

	/**
	 * 将购物车存储到redis中
	 * 
	 * @param username 用户名称
	 * @param cartList 购物车列表
	 */
	public void saveCrateListToRedis(String username, List<Cart> cartList);
	/**
	 * 合并购物车
	 * @param cartCookieList
	 * @param cartRedosList
	 * @return
	 */
	public  List<Cart> mergecartList(List<Cart> cartCookieList,List<Cart> cartRedosList);

}
