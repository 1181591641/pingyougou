package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.IBrandService;

import entity.PageResult;

@Service
@Transactional
public class BrandServiceImpl implements IBrandService {

	@Autowired
	private TbBrandMapper brandMapper;

	/**
	 * 返回所有品牌
	 */
	@Override
	public List<TbBrand> findBrandAll() throws Exception {
		return brandMapper.selectByExample(null);
	}

	/**
	 * 返回所有品牌(分页)
	 */
	@Override
	public PageResult findPage(Integer pageNum, Integer pageSize) throws Exception {

		PageHelper.startPage(pageNum, pageSize);// 分页

		com.github.pagehelper.Page<TbBrand> page = (com.github.pagehelper.Page<TbBrand>) brandMapper.selectByExample(null);

		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 添加品牌
	 */
	@Override
	public void add(TbBrand brand) throws Exception {
		brandMapper.insert(brand);

	}

	/**
	 * 根据品牌名称查询
	 */
	@Override
	public TbBrand Names(TbBrand brand) throws Exception {
		TbBrandExample example = new TbBrandExample();

		if (brand != null) {
			if (brand.getName() != "") {
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(brand.getName());
			}
		}
		return brandMapper.selectByExample(example).get(0);

	}

	/**
	 * 根据品牌id查询
	 */
	@Override
	public TbBrand findOne(Long id) throws Exception {

		return brandMapper.selectByPrimaryKey(id);
	}

	/**
	 * 品牌修改
	 */
	@Override
	public void update(TbBrand brand) throws Exception {
		brandMapper.updateByPrimaryKey(brand);

	}

	/**
	 * 删除所选id
	 */
	@Override
	public void deleteByPrimaryKey(Long[] ids) throws Exception {
		for (Long id : ids) {
			brandMapper.deleteByPrimaryKey(id);

		}
	}

	@Override

	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) throws Exception {

		PageHelper.startPage(pageNum, pageSize);// 分页

		TbBrandExample example = new TbBrandExample();

		Criteria criteria = example.createCriteria();
		if (brand != null) {
			if (brand.getName() != null && brand.getName().length() > 0) {
				criteria.andNameLike("%" + brand.getName() + "%");
			}
			if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
				criteria.andFirstCharLike("%" + brand.getFirstChar() + "%");
			}
		}

		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return brandMapper.selectOptionList();
	}

}
