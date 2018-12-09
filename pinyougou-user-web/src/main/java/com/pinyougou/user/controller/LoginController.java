package com.pinyougou.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/showName")
	public Map showName() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("name", name);
		System.out.println("当前用户:"+name);
		return map;
	}

}
