package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.pojogroup.ItemCat;

import entity.PageResult;
/**
 * 商品服务层接口
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 查询名称是否重复
	 * @param itemCat
	 * @return
	 */
	public TbItemCat Names(TbItemCat itemCat);
	
	/**
	 * 增加
	*/
	public void add(TbItemCat itemCat);
	
	
	/**
	 * 修改
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbItemCat itemCat, int pageNum,int pageSize);
	/**
	 * 显示商品上级
	 * @param id
	 * @return
	 */
	public List<TbItemCat> findByParenId(Long id);
	
	
}
