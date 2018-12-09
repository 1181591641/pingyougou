package com.pinyougou.mannager.controller;


import java.awt.geom.Ellipse2D;
import java.util.List;




import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.IBrandService;

import entity.PageResult;
import entity.Result;
import java.util.Map;
/**
 * 
 * @ClassName: BrandController
 * 
 * @Description: 运营商
 * 
 * @author: zmz
 * 
 * @date: 2018年11月10日 下午6:57:46
 */
@RestController // 相当于ResponseBody+Controller 不需要再方法上使用@ResponseBody注解了
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private IBrandService brandService;

	/**
	 * 查询所有订单(无分页)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll() {
		try {
			return brandService.findBrandAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查询所有订单(实现了分页)
	 * 
	 * @param page
	 *            当前页面
	 * @param size
	 *            每页记录数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(Integer page, Integer size) throws Exception {

		return brandService.findPage(page, size);

	}

	@RequestMapping("/Names")
	public Result Names(@RequestBody TbBrand brand) {
		try {
			brandService.Names(brand);
			return new Result(true, "商品名称重复");

		} catch (Exception e) {
			return new Result(false, "");
		}
	}

	/**
	 * 产品添加
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand) {
if(brand!=null&&brand.getName()!=null&&!brand.equals(""))
{
		try {
			brandService.add(brand);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	else
	{
		return new Result(false, "添加失败");
	}
	}

	/**
	 * 根据品牌id查询
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id) throws Exception {
	
		return brandService.findOne(id);
	}

	/**
	 * 品牌修改
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand) {
		try {
			brandService.update(brand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	/**
	 * 删除所选
	 * @param brand
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids) {
		try {
			brandService.deleteByPrimaryKey(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}

	
	 @RequestMapping("/search")
	 public PageResult search(Integer page, Integer size,@RequestBody TbBrand brand) throws Exception {

		return brandService.findPage(brand, page, size);
	}
	 @RequestMapping("/selectOptionList")
	 public List<Map> selectOptionList()
	 {
		 return brandService.selectOptionList();
	 }
	 
}
