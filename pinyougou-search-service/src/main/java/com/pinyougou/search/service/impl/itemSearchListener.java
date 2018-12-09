package com.pinyougou.search.service.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
@Component
public class itemSearchListener implements MessageListener {
	@Autowired
	private ItemSearchService ItemSearchService;

	@Override
	public void onMessage(Message message) {
	
		TextMessage textMessage=(TextMessage)message;
		try {
			String text = textMessage.getText();
			System.out.println("监听到了消息");
			List<TbItem> itemList = JSON.parseArray(text,TbItem.class);
			ItemSearchService.imporList(itemList);
			System.out.println("导入到solr索引库");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
