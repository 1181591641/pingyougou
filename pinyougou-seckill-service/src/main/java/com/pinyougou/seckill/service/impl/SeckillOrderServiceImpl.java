package com.pinyougou.seckill.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pay.service.WeixiPayService;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.pojo.TbSeckillOrderExample;
import com.pinyougou.pojo.TbSeckillOrderExample.Criteria;
import com.pinyougou.seckill.service.SeckillOrderService;

import entity.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckillOrderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.insert(seckillOrder);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.updateByPrimaryKey(seckillOrder);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id) {
		return seckillOrderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			seckillOrderMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSeckillOrderExample example = new TbSeckillOrderExample();
		Criteria criteria = example.createCriteria();

		if (seckillOrder != null) {
			if (seckillOrder.getUserId() != null && seckillOrder.getUserId().length() > 0) {
				criteria.andUserIdLike("%" + seckillOrder.getUserId() + "%");
			}
			if (seckillOrder.getSellerId() != null && seckillOrder.getSellerId().length() > 0) {
				criteria.andSellerIdLike("%" + seckillOrder.getSellerId() + "%");
			}
			if (seckillOrder.getStatus() != null && seckillOrder.getStatus().length() > 0) {
				criteria.andStatusLike("%" + seckillOrder.getStatus() + "%");
			}
			if (seckillOrder.getReceiverAddress() != null && seckillOrder.getReceiverAddress().length() > 0) {
				criteria.andReceiverAddressLike("%" + seckillOrder.getReceiverAddress() + "%");
			}
			if (seckillOrder.getReceiverMobile() != null && seckillOrder.getReceiverMobile().length() > 0) {
				criteria.andReceiverMobileLike("%" + seckillOrder.getReceiverMobile() + "%");
			}
			if (seckillOrder.getReceiver() != null && seckillOrder.getReceiver().length() > 0) {
				criteria.andReceiverLike("%" + seckillOrder.getReceiver() + "%");
			}
			if (seckillOrder.getTransactionId() != null && seckillOrder.getTransactionId().length() > 0) {
				criteria.andTransactionIdLike("%" + seckillOrder.getTransactionId() + "%");
			}

		}

		Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 秒杀商品订单
	 */
	@Override
	public void submitOrder(Long seckillId, String userId) {
		// 从缓存中读取数据
		TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
		if (seckillGoods == null) {
			throw new RuntimeException("商品不存在");
		}

		if (seckillGoods.getStockCount() <= 0) {
			throw new RuntimeException("商品已被秒杀完");
		}
		// 库存减1
		seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
		// 更新到缓存中

		redisTemplate.boundHashOps("seckillGoods").put(seckillId, seckillGoods);
		// 如果商品库存等于0
		if (seckillGoods.getStockCount() == 0) {
			// 更新到数据库中
			seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
			// 清空缓存
			redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
			System.out.println("跟新到缓存中");
		}

		// 保存订单列表此时存储到缓存中
		TbSeckillOrder seckillOrder = new TbSeckillOrder();
		seckillOrder.setId(idWorker.nextId());// 主键
		seckillOrder.setSeckillId(seckillId);// 秒杀商品id
		seckillOrder.setMoney(seckillGoods.getCostPrice());// 支付金额
		seckillOrder.setUserId(userId);// 用户id
		seckillOrder.setSellerId(seckillGoods.getSellerId());// 商家id
		seckillOrder.setCreateTime(new Date());// 订单创建时间
		seckillOrder.setStatus("0");// 支付状态
		// 放入缓存中
		redisTemplate.boundHashOps("seckillOrder").put(userId, seckillOrder);

	}

	@Override
	public TbSeckillOrder searchOrderFromRedisByUserId(String userId) {

		return (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
	}

	@Override
	public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {
		// 从缓存中取出订单数据
		TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
		if (seckillOrder == null) {
			throw new RuntimeException("订单不存在");
		}
		if (seckillOrder.getId().longValue() != orderId.longValue()) {
			throw new RuntimeException("订单不正确");
		}
		// 修改订单数据
		seckillOrder.setPayTime(new Date());// 支付时间
		seckillOrder.setStatus("1");// 支付状态
		seckillOrder.setTransactionId(transactionId);// 交易流水
		// 将订单存入到数据库中
		seckillOrderMapper.insert(seckillOrder);
		// 清空缓存
		redisTemplate.boundHashOps("seckillOrder").delete(userId);
	}

	/**
	 * 订单撤销
	 */
	@Override
	public void deleteOrderFromRedis(String userId, Long orderId) {
		// 从缓存中取出订单数据
		TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
		if (seckillOrder != null) {
			// 删除缓存中的订单
			redisTemplate.boundHashOps("seckillOrder").delete(userId);
			// 获取商品列表
			TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods")
					.get(seckillOrder.getSeckillId());
			if (seckillGoods != null) {
				//库存回退
				seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
				// 更新到缓存中
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);
			}else {
			TbSeckillGoods tbSeckillGoods = new 	TbSeckillGoods();
			tbSeckillGoods.setSellerId(seckillOrder.getSellerId());
			tbSeckillGoods.setStockCount(1);//商品数量
			redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), tbSeckillGoods);
			}
		}
	}

}
