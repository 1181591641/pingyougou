package com.pinyougou.page.service.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;
@Component
public class pageListener implements MessageListener {
	@Autowired
	private ItemPageService itemPageService;

	@Override
	public void onMessage(Message message) {
	TextMessage textMessage=(TextMessage)message;
	try {
		String ids =textMessage.getText();
		System.out.println("接收到消息");
		itemPageService.genItemHtml(Long.parseLong(ids));
		System.out.println("网页生成功");
	} catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	}

}
