package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixiPayService {
	/**
	 * 生成二维码
	 * 
	 * @param out_trade_no
	 *            订单号
	 * @param total_fee
	 *            金额
	 * @return
	 */
	public Map createNative(String out_trade_no, String total_fee);

	/**
	 * 查询支付订单状态
	 * 
	 * @param out_trade_no 订单编号
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);
	/**
	 * 关闭订单
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no); 
}
