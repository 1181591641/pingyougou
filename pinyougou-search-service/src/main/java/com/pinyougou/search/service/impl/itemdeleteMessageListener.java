package com.pinyougou.search.service.impl;

import java.io.Serializable;
import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;


@Component
public class itemdeleteMessageListener implements MessageListener {
	@Autowired
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		ObjectMessage textMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) textMessage.getObject();
			System.out.println("监听");
			itemSearchService.deleteByGoodsId(Arrays.asList(ids));
			System.out.println("执行索引库删除");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
