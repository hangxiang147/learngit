package com.zhizaolian.staff.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import common.Logger;

public class BaseAction extends ActionSupport implements Action, ServletRequestAware {
	
	public Logger logger = Logger.getLogger(BaseAction.class);
	
	private static final long serialVersionUID = 1L;

	public HttpServletRequest request;
	
	public String execute() throws Exception {
		return ""; 
	}
	
	public void setServletRequest(HttpServletRequest request) {  
		this.request = request;
	}
	
	protected void printByJson(Object object) {
	     printByJson(object, "application/json");
	}
	 
	protected void printByJson(Object obj, String type) {
		PrintWriter out = null;
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType(type);
		httpServletResponse.setCharacterEncoding("utf-8");
		String json = null;
		try {
			out = httpServletResponse.getWriter();
			json = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
		} catch (IOException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		out.print(json);
		out.close();
	}
	protected String getBodyString() throws Exception{
		BufferedReader br = request.getReader();
		String str, jsonStr = "";
		while((str = br.readLine()) != null){
			jsonStr += str;
		}
		return jsonStr;
	}
}
