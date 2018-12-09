package com.pinyougou.cart.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.ws.wssecurity.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *  购物车控制类

 * @ClassName: CartController

 * @Description: TODO

 * @author: zmz

 * @date: 2018年12月3日 下午11:39:28
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pay.service.WeixiPayService;
import com.pinyougou.pojogroup.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Reference
	private CartService cartService;

	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {

		// 从cookie中取出购物车列表
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if (cartListString == null || cartListString.equals("")) {
			cartListString = "[]";
		}
		// 将String类型的购物车列表转换为json形式的list集合
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		// 用户没有登录
		// 获取当前登陆的用户
		String usrname = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前用户" + usrname);
		if (usrname.equals("anonymousUser")) {

			return cartList_cookie;
		} else {
			// 如果用户登录了
			// 从redis中取出购物车列表
			List<Cart> cartList_redis = cartService.findCartListFromRedis(usrname);
			//
			if (cartList_cookie.size() > 0) {
				// 得到合并后的购物车
				List<Cart> cartList = cartService.mergecartList(cartList_cookie, cartList_redis);
				// 将合并后的购物车添加到redis中
				cartService.saveCrateListToRedis(usrname, cartList);
				// 清空cookie中的值
				util.CookieUtil.deleteCookie(request, response, "cartList");
				return cartList;
			}
			return cartList_redis;
		}

	}

	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins = "http://localhost:9105", allowCredentials = "true") // 支持跨域和携带cookie
	public Result addGoodsToCartList(Long itemId, Integer num) {
		// 获取当前登陆的用户
		String usrname = SecurityContextHolder.getContext().getAuthentication().getName();
		// response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");//
		// 允许此域访问
		// response.setHeader("Access-Control-Allow-Origin", "*");// 允许所有域访问
		// response.setHeader("Access-Control-Allow-Credentials", "true");//
		// 允许发送cookie，涉及到cookie操作时加上此属性
		try {
			// 从cookie中取出购物车
			List<Cart> cartList = findCartList();
			// 调用服务方法操作购物车
			cartList = cartService.andGoodsToCartList(cartList, itemId, num);

			// 获取当前登陆的用户

			// 如果没有登录
			if (usrname.equals("anonymousUser")) {
				// 将新的购物车存入cookie
				String cartListString = JSON.toJSONString(cartList);
				util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
				System.out.println("向cookie存储购物车");
			} else {
				// 如果登陆了
				// 将新的购物车存入redis中
				cartService.saveCrateListToRedis(usrname, cartList);
				System.out.println("向redis存储购物车");
			}
			return new Result(true, "存入购物车成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "存入购物车失败");
		}
	}

	@RequestMapping("/status")
	private void status(Long itemId, String status) {
		cartService.status(itemId, status);
	}

	

	

}
