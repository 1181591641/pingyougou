package com.pinyougou.sellergoods.service;

import java.util.List;



import com.pinyougou.pojo.TbBrand;

import entity.PageResult;
import java.util.Map;


/**
 * 品牌接口
 * @author zmz
 *
 */
public interface IBrandService {
	/**
	 * 查询所有的品牌
	 * @return
	 * @throws Exception
	 */
	public List<TbBrand> findBrandAll() throws Exception;
	/**
	 * 品牌分页
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(Integer pageNum,Integer pageSize)throws Exception;
	
	/**
	 * 添加品牌
	 * @param brand
	 * @throws Exception
	 */
	public void add(TbBrand brand) throws Exception;
	
	/**
	 * 判断品牌名称是否重复
	 * @param brand
	 * @throws Exception
	 */
	public TbBrand Names(TbBrand brand) throws Exception;
	/**
	 * 根据id查询品牌
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TbBrand findOne(Long id)throws Exception;
	
	/**
	 * 品牌修改
	 * @param brand
	 * @throws Exception
	 */
	public void update(TbBrand brand) throws Exception;
	
	/**
	 * 删除所选
	 * @param id
	 * @throws Exception
	 */
	public void deleteByPrimaryKey(Long [] ids) throws Exception;

	
	/**
	 * 模糊查询
	 * @param brand
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	
	PageResult findPage(TbBrand brand, int pageNum, int pageSize)throws Exception;
	/**
	 * 返回下拉列表数据
	 * @return
	 */
	public List<Map> selectOptionList();
}





