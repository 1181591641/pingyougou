package com.pinyougou.mannager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.IBrandService;

@RestController  //相当于ResponseBody+Controller 不需要再方法上使用@ResponseBody注解了
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private IBrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll() throws Exception {
		return brandService.findBrandAll();
	}
}




