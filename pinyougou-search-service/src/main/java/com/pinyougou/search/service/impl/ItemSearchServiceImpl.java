package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.Size2DSyntax;

import org.omg.CosNaming.IstringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;


@Service(timeout = 100000)
public class ItemSearchServiceImpl implements ItemSearchService {

	private static final String Integer = null;
	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	public Map search(Map searchMap) {
		Map map = new HashMap<>();
		Map serarcgList = serarcgList(searchMap);
		List<String> searchCtegoryList = searchCtegoryList(searchMap);
		String str = (String) searchMap.get("keywords");
		searchMap.put("keywords", str.replace(" ", ""));
		// 查询列表
		map.putAll(serarcgList);
		// 分组数据
		map.put("ctegoryList", searchCtegoryList);
		// 获取规格和品牌列表
		String category = (String) searchMap.get("category");
		if (!category.equals("")) {
			map.putAll(searchBrandAndSpecList(category));
		} else {
			if (searchCtegoryList.size() > 0) {
				map.putAll(searchBrandAndSpecList(searchCtegoryList.get(0)));
			}
		}

		return map;
	}

	// 查询列表
	private Map serarcgList(Map searchMap) {
		Map map = new HashMap<>();
		// 获取查询条件对象
		HighlightQuery query = new SimpleHighlightQuery();
		/**
		 * 为语句设置高亮的步骤 1.需要获取需要设置高亮的列 <em style='color:red'>高亮字段</em> 需要为字段设置如上方式的标签包裹着
		 * 2.需要获取该字段的前缀位置 3.需要获取该字段的后缀位置
		 */
		// 高亮显示
		// 构建高亮选项对象对象
		// 设置高亮域(多个高亮域)
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
		// 设置前缀
		highlightOptions.setSimplePrefix("<em style='color:red'>");
		// 设置后缀
		highlightOptions.setSimplePostfix("</em>");
		// 为查询设置高亮选项
		query.setHighlightOptions(highlightOptions);

		// 根据关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 根据商品分类查询
		if (!"".equals(searchMap.get("category"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria fileterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(fileterCriteria);
			query.addFilterQuery(filterQuery);
		}
		// 根据品牌查询
		if (!"".equals(searchMap.get("brand"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria fileterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(fileterCriteria);
			query.addFilterQuery(filterQuery);
		}
		// 按照规格查询
		if (searchMap.get("spec") != null) {
			Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
			for (String key : specMap.keySet()) {

				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}

		}

		// 按照价格查询
		if (!"".equals(searchMap.get("price"))) {
			String[] price = ((String) searchMap.get("price")).split("-");
			if (!price[0].equals("0")) { // 如果最低价格不等于0
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if (!price[1].equals("*")) { // 如果最高价格不等于*
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		// 分页查询

		Integer pageNo = (Integer) searchMap.get("pageNo");
		if (pageNo == null) {

			pageNo = 1;
		}

		Integer pageSize = (Integer) searchMap.get("pageSize");
		if (pageSize == null) {

			pageSize = 40;
		}
		// 设置页起始位置
		query.setOffset((pageNo - 1) * pageSize);
		// 设置每页显示数量
		query.setRows(pageSize);

		// 排序
		String sortValue = (String) searchMap.get("sort");
		String sortField = (String) searchMap.get("sortField");
		if (sortValue != null && !sortValue.equals("")) {
			if(sortValue.equals("ASC")) {
			Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
			query.addSort(sort);
			} 
				if(sortValue.equals("DESC")) {
					Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
					query.addSort(sort);
			}
		}

		// 获取高亮分页
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

		// 获取高亮集合入口
		List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();
		// 遍历集合
		for (HighlightEntry<TbItem> entry : highlighted) {
			// 获取高亮列表(可以设置多个域，每个域对应一个高亮入口)
			List<Highlight> highlights = entry.getHighlights();

			if (highlights.size() > 0 && highlights.get(0).getSnipplets().size() > 0) {

				TbItem tbItem = entry.getEntity();
				// 替换高亮数据
				tbItem.setTitle(highlights.get(0).getSnipplets().get(0));
			}
		}

		// 获取高亮分页全部数据添加到map集合中
		map.put("rows", page.getContent());
		// 总页数
		map.put("totalPage", page.getTotalPages());
		// 总记录数
		map.put("total", page.getTotalElements());
		return map;
	}

	// 分组查询
	private List searchCtegoryList(Map searchMap) {
		List list = new ArrayList<>();
		Query query = new SimpleQuery("*:*");
		// 根据关键字搜索
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 设置分组
		// 添加分组字段
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		// 获取分组页
		GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);
		// 获取分组对象
		GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
		// 获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// 获取分组集合入口
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			// 获取分组数据
			String groupValue = entry.getGroupValue();
			list.add(groupValue);
		}
		return list;
	}

	// 获取品牌和规格列表
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap<>();
		// 根据商品分类名称获取模板Id
		Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		if (templateId != null) {
			// 根据模板id获取品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
			// 根据模板id获取规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("brandList", brandList);
			map.put("specList", specList);
		}

		return map;

	}
	//添加SKU到solr
	@Override
	public void imporList(List<TbItem> list) {

		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		
	}
   //根据goodsId删除SKU
	@Override
	public void deleteByGoodsId(List list) {
		
		Query query=new SimpleQuery();
		Criteria criteria=new Criteria("item_goodsid").in(list);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		// TODO Auto-generated method stub
		
	}
}
