package com.pinyougou.pojogroup;

import java.io.Serializable;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbTypeTemplate;

public class ItemCat implements Serializable{
	private static final long serialVersionUID = 1L;
    private TbItemCat itemCat;
    private TbTypeTemplate tbTypeTemplate;
    
	public TbItemCat getItemCat() {
		return itemCat;
	}
	public TbTypeTemplate getTbTypeTemplate() {
		return tbTypeTemplate;
	}
	public void setItemCat(TbItemCat itemCat) {
		this.itemCat = itemCat;
	}
	public void setTbTypeTemplate(TbTypeTemplate tbTypeTemplate) {
		this.tbTypeTemplate = tbTypeTemplate;
	}

}
