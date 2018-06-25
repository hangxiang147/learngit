package com.zhizaolian.staff.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Setter;

import org.apache.struts2.ServletActionContext;

public class AuthenticationTag extends SimpleTagSupport {
	
	@Setter
	private String name;

	@Override
	@SuppressWarnings("unchecked") 
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<String> permissions = (List<String>) request.getSession().getAttribute("permissions");
		if(null != permissions){
			for (String permission : permissions) {
				if (name.equals(permission)) {
					getJspBody().invoke(null);
					return;
				}
			}
		}
	}
}
