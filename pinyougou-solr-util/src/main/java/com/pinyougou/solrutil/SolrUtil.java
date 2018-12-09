package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void imporItemData(){
		
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		List<TbItem> itemlist = itemMapper.selectByExample(example);
		for(TbItem item:itemlist) {
			Map map = JSON.parseObject(item.getSpec(), Map.class);//将规格转换为map
			item.setSpecMap(map);
		}
		solrTemplate.saveBeans(itemlist);
		solrTemplate.commit();
	}
	public static void main(String[] args) {
		ApplicationContext aContext=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = aContext.getBean(SolrUtil.class);
		solrUtil.imporItemData();
	}
}
