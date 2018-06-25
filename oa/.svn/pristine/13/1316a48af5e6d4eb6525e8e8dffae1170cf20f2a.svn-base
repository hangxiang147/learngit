package com.zhizaolian.staff.utils;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.enums.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 生成新的个人待办时，给APP发送推送消息
 * @author jndxz
 *
 */
@Slf4j
public class JPushTaskCreateListener implements TaskListener {
	
	@Autowired
	private IdentityService identityService;

	public void notify(DelegateTask delegateTask) {
		try {
			User usr = identityService.createUserQuery().userId(delegateTask.getAssignee()).singleResult();
			JPushClientUtil.sendToRegistrationID(usr.getFirstName(), Constants.NOTIFICATION_TASK);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		 
	}
	
}
