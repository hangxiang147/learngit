package com.zhizaolian.staff.utils;

import java.util.List;

/**
 * 分页查询结果
 * @author zpp
 *
 * @param <T>
 */
public class ListResult<T> {

	private List<T> list;
	private int totalCount;
	
	public ListResult() {
		super();
	}
	
	public ListResult(List<T> list, int totalCount) {
		super();
		this.list = list;
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
