package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;

	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {

		try {
			// 获取文件名
			String originalFilename = file.getOriginalFilename();
			// 得到文件扩展名
			String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
			util.FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			String fileId = client.uploadFile(file.getBytes(), extName);
			// 获取图片完整地址
			String url = FILE_SERVER_URL + fileId;
			return new Result(true, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上传失败");
		}

	}
}
