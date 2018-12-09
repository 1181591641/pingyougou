package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 查询姓名
	 */
	@Override
	public TbSpecification Names(TbSpecification tbSpecification) {

		TbSpecificationExample exampl = new TbSpecificationExample();
		Criteria criteria = exampl.createCriteria();
		if (tbSpecification != null) {
	
				criteria.andSpecNameEqualTo(tbSpecification.getSpecName());
		
		}
		return specificationMapper.selectByExample(exampl).get(0);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		// 获取规格实体
		TbSpecification tbSpecification = specification.getSpecification();
		specificationMapper.insert(tbSpecification);
		// 获取规格选项集合
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option : specificationOptionList) {
			option.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(option);
		}

	}

	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification) {

		TbSpecification tbSpecification = specification.getSpecification();
		specificationMapper.updateByPrimaryKey(tbSpecification);

		// 先删除所有规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andSpecIdEqualTo(tbSpecification.getId());
		specificationOptionMapper.deleteByExample(example);
		// 添加规格选项
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption option : specificationOptionList) {
			option.setSpecId(tbSpecification.getId());
			specificationOptionMapper.insert(option);
		}
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id) {
		// 创建Specification对象
		Specification specification = new Specification();
		// 通过id获取对象
		TbSpecification selectByPrimaryKey = specificationMapper.selectByPrimaryKey(id);
		// 放入方法中
		specification.setSpecification(selectByPrimaryKey);
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> selectByExample = specificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(selectByExample);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
	
		for (Long id : ids) {
			specificationMapper.deleteByPrimaryKey(id);
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.pinyougou.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
			createCriteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);
		}
	}

	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example = new TbSpecificationExample();
		Criteria criteria = example.createCriteria();

		if (specification != null) {
			if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
				criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
			}

		}

		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}
	@Override
	public List<Map> selectOptionList() {
	return specificationMapper.selectOptionList();
	}

}
