package com.pinyougou.page.service.impl;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

@Component
public class pageDeleteListener  implements MessageListener{
	@Autowired
	private ItemPageService itemPageService;

	@Override
	public void onMessage(Message message) {
		
		ObjectMessage objectMessage=(ObjectMessage)message;
		try {
			Long [] ids =(Long []) objectMessage.getObject();
			System.out.println("监听删除");
			itemPageService.deleteGenItemHtml(ids);
			System.out.println("删除成功");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
