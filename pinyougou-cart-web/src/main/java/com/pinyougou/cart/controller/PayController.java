package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixiPayService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {
	@Reference
	private WeixiPayService weixiPayService;
	@Reference
	private OrderService orderService;

	@RequestMapping("/createNative")
	public Map createNative() {
		// 获取当前登陆用户
		String userid = SecurityContextHolder.getContext().getAuthentication().getName();
		// 提取支付日志(从缓存)
		TbPayLog payLog = orderService.searchPayLogFromRedis(userid);

		// 调用微信支付接口
		if (payLog != null) {
		
			return weixiPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
			
		} else {
			return new HashMap<>();
		}

	}

	/**
	 * 查询订单完成状态
	 * 
	 * @param out_trade_no
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		Result result = null;
		int x = 0;
		while (true) {
	
			// 查询订单状态
			Map<String, String> map = weixiPayService.queryPayStatus(out_trade_no);
			// 订单为空
			if (map == null) {
				result = new Result(false, "支付发生错误");
				System.out.println("支付发出错误！！！");
				break;
			} else {
				/*
				 * 判断订单支付状态 SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVOKED—已撤销（付款码支付）
				 * USERPAYING--用户支付中（付款码支付） PAYERROR--支付失败(其他原因，如银行返回失败)
				 */
				if (map.get("trade_state").equals("SUCCESS")) {
					result = new Result(true, "支付成功");
					System.out.println(out_trade_no);
					System.out.println(map.get("transaction_id"));
					orderService.updateOrderStatus(out_trade_no, map.get("transaction_id")+"");
					System.out.println("支付成功");
					break;
				}
			}
			try {
				// 没五秒调用一次此方法进行查询
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x++;
			if (x >= 3) {
				result = new Result(false, "二维码超时");

				break;
			}
			System.out.println("等待支付"+x+"..." );
		}
		return result;
	}

}
