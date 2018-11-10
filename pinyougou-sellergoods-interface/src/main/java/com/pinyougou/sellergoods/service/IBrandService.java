package com.pinyougou.sellergoods.service;

import java.util.List;

import com.pinyougou.pojo.TbBrand;

/**
 * 品牌接口
 * @author cy
 *
 */
public interface IBrandService {
	/**
	 * 查询所有的品牌
	 * @return
	 * @throws Exception
	 */
	public List<TbBrand> findBrandAll() throws Exception;
}





