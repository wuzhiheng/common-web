package com.wonders.commonweb.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 用于返回分页数据，包括一个total和rows
 * 创建日期：2017-12-18下午3:44:43
 * author:wuzhiheng
 */
public class ResultList implements Serializable{

	private long total;
	
	private List<?> rows;
	
	/**
	 * @param total
	 * @param rows
	 */
	public ResultList(long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
	
}
