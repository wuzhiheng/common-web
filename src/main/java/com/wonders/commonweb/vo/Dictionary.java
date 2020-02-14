package com.wonders.commonweb.vo;

import java.io.Serializable;

/**
 * 装载select的数据
 * 创建日期：2018-1-7下午10:09:38
 * author:wuzhiheng
 */
public class Dictionary implements Serializable{
	
	private String value;
	
	private String name;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
