package com.pinyougou.seckill.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.pojo.TbSeckillGoodsExample.Criteria;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillGoodsService;

import entity.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Autowired
	private RedisTemplate redisTemplate;


	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillGoods> findAll() {
		return seckillGoodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSeckillGoods> page = (Page<TbSeckillGoods>) seckillGoodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillGoods seckillGoods) {
		seckillGoodsMapper.insert(seckillGoods);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillGoods seckillGoods) {
		seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillGoods findOne(Long id) {
		return seckillGoodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			seckillGoodsMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbSeckillGoods seckillGoods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		Criteria criteria = example.createCriteria();

		if (seckillGoods != null) {
			if (seckillGoods.getTitle() != null && seckillGoods.getTitle().length() > 0) {
				criteria.andTitleLike("%" + seckillGoods.getTitle() + "%");
			}
			if (seckillGoods.getSmallPic() != null && seckillGoods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + seckillGoods.getSmallPic() + "%");
			}
			if (seckillGoods.getSellerId() != null && seckillGoods.getSellerId().length() > 0) {
				criteria.andSellerIdLike("%" + seckillGoods.getSellerId() + "%");
			}
			if (seckillGoods.getStatus() != null && seckillGoods.getStatus().length() > 0) {
				criteria.andStatusLike("%" + seckillGoods.getStatus() + "%");
			}
			if (seckillGoods.getIntroduction() != null && seckillGoods.getIntroduction().length() > 0) {
				criteria.andIntroductionLike("%" + seckillGoods.getIntroduction() + "%");
			}

		}

		Page<TbSeckillGoods> page = (Page<TbSeckillGoods>) seckillGoodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 返回秒杀的商品列表
	 */
	@Override
	public List<TbSeckillGoods> findList() {
		// 获取值所有返回到list中
		List<TbSeckillGoods> seckillList = redisTemplate.boundHashOps("seckillGoods").values();

		if (seckillList == null || seckillList.size() == 0) {
			TbSeckillGoodsExample example = new TbSeckillGoodsExample();
			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");// 审核通过
			criteria.andStockCountGreaterThan(0);// 剩余库存大于0
			criteria.andStartTimeLessThanOrEqualTo(new Date());// 开始时间小于等于当前时间
			criteria.andEndTimeGreaterThan(new Date());// 结束时间大于当前时间
			seckillList = seckillGoodsMapper.selectByExample(example);
			for (TbSeckillGoods SeckillGoods : seckillList) {
				redisTemplate.boundHashOps("seckillGoods").put(SeckillGoods.getId(), SeckillGoods);
			}
			System.out.println("从缓存库中读取数据");
		} else {
			System.out.println("从缓存中读取数据");
		}
		return seckillList;
	}

	/**
	 * 从缓存中读取商品详细页
	 */
	@Override
	public TbSeckillGoods finOne(Long id) {
		TbSeckillGoods tbSeckillGoods=(TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
	
		return (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
	}

	
}
