package com.zhizaolian.staff.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ActionUtil {
	private final static int DEFAULT_PAGE_SIZE=20;
	
	public static Map<String, String> createMapByRequest(HttpServletRequest request, Boolean isReturn,
			String... keys) {
		if (request == null) 
			throw new IllegalArgumentException();
		if (keys == null || keys.length == 0)
			return null;
		Map<String, String> returnMap = new HashMap<String, String>();
		for (int i = 0, length = keys.length; i < length; i++) {
			String name = keys[i];
			String value = request.getParameter(keys[i]);
			returnMap.put(name, value);
			if (isReturn)
				request.setAttribute(name, value);
		}
		return returnMap;
	}
	public  static<T> void setListResult(HttpServletRequest request,ListResult<T> resultList,int page){
		int totalCount=resultList.getTotalCount();
		int totalPage=totalCount%DEFAULT_PAGE_SIZE==0 ? totalCount/DEFAULT_PAGE_SIZE : totalCount/DEFAULT_PAGE_SIZE+1;
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("limit", DEFAULT_PAGE_SIZE);	
		request.setAttribute("page", page);
	}
	public  static<T> void setListResult(HttpServletRequest request,ListResult<T> resultList,int page,int size){
		if(size==0)
			throw new IllegalArgumentException("每页至少显示一条");
		int totalCount=resultList.getTotalCount();
		int totalPage=totalCount%size==0 ? totalCount/size : totalCount/size+1;
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("limit", size);
		request.setAttribute("page", page);
	}
	/**
	 * @param pageNo 当前页码
	 * @param pageSize 页数
	 * @param list  所有集合
	 * @return
	 * @throws Exception
	 */
	public static<T> List<T> page(int pageNo,int pageSize,List<T> list) throws Exception{
		List<T> result = new ArrayList<T>();
		if(list != null && list.size() > 0){
			int allCount = list.size();
			int pageCount = allCount % pageSize == 0 ? allCount / pageSize : allCount / pageSize + 1;
			if(pageNo >= pageCount){
				pageNo = pageCount;
			}
			int start = (pageNo-1) * pageSize;
			int end = pageNo * pageSize;
			if(end >= allCount){
				end = allCount;
			}
			for(int i = start; i < end; i ++){
				result.add(list.get(i));
			}
		}
		return (result != null && result.size() > 0) ? result : null;
	}
}
