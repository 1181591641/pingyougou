package com.pinyougou.search.service;


import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbItem;

public interface ItemSearchService {

	//搜索方法
	public Map search(Map searchMap);
	//导入列表
	public void imporList(List<TbItem> list);
	
	public void deleteByGoodsId(List list);
}
