package com.zhizaolian.staff.action;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.entity.CommonSubjectEntity;
import com.zhizaolian.staff.service.CommonSubjectService;
import com.zhizaolian.staff.vo.CommonSubjectVo;

import lombok.Getter;
import lombok.Setter;

public class CommonSubjectAction extends BaseAction {

	@Getter
	@Setter
	private CommonSubjectVo commonSubjectVo;
	@Autowired
	private CommonSubjectService commonSubjectService;
	@Getter
	@Setter
	private File[] files;
	@Getter
	@Setter
	private String fileDetail;
	private static final long serialVersionUID = 1L;

	public void startCommonSubject() {
		User user = (User) request.getSession().getAttribute("user");
		try {
			commonSubjectService.startCommonSubject(commonSubjectVo, files, fileDetail,user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			returnFail(e.getMessage());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		returnSuccess();
	}
	
	public void getContent(){
		CommonSubjectEntity  cSubjectEntity=commonSubjectService.getEntityId(request.getParameter("id"));
		printByJson(cSubjectEntity);
	}

}
