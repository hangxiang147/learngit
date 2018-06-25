package com.zhizaolian.staff.action;



import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.service.SigninService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.SigninVO;

import lombok.Getter;
import lombok.Setter;

public class SigninAction extends BaseAction {
	
	@Autowired
	private SigninService signinService;
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private String panel;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private SigninVO signinVO;
	@Setter
	@Getter
	private AttendanceVO attendanceVO;
	
	private static final long serialVersionUID = 1L;
	/*
	 * 点击签到按钮保存数据
	 */
	public String signin() {
		try{
			User user=(User)request.getSession().getAttribute("user");
			String userID=user.getId();
			SigninVO signin=new SigninVO();
			signin.setUserID(userID);
			Date date=new Date();
			signin.setSigninDate(DateUtil.formateDate(date));
			signinService.saveSignin(signin);
		}catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		
		return "signin";
	}

}
