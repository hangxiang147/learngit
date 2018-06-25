package com.zhizaolian.staff.filter;

import java.io.IOException;




import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;

public class SessionFilter implements Filter  {
	
	/**
	 * 需要排除（不拦截）的URL的正则表达式
	 */
	private Pattern excepUrlPattern;
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) request;  
        String servletPath = httpRequest.getServletPath();
        // 如果请求的路径是排除的URL时，则直接放行
        if (excepUrlPattern.matcher(servletPath).matches()) {
            chain.doFilter(request, response);
            return;
        }
        
        User zuser = (User) httpRequest.getSession().getAttribute("user");  
        if (zuser == null) {  
        		 request.setAttribute("errorMessage", "登录超时，请重新登录！");  
                 RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");  
                 requestDispatcher.forward(request, response);  
        }else{  
            chain.doFilter(request, response);  
        }  
  
    }
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String excepUrlRegex = arg0.getInitParameter("excepUrlRegex");
        if (!StringUtils.isBlank(excepUrlRegex)) {
            excepUrlPattern = Pattern.compile(excepUrlRegex);
        }
 
	}  
}
