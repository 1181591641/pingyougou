package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
/**
 * 组合实体类
 */
public class Specification implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private TbSpecification specification;
private List<TbSpecificationOption> specificationOptionList;
public TbSpecification getSpecification() {
	return specification;
}
public void setSpecification(TbSpecification specification) {
	this.specification = specification;
}
public List<TbSpecificationOption> getSpecificationOptionList() {
	return specificationOptionList;
}
public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
	this.specificationOptionList = specificationOptionList;
}
@Override
public String toString() {
	return "Specification [specification=" + specification + ", specificationOptionList=" + specificationOptionList
			+ "]";
}


}
