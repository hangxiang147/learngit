package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.EmailDao;
import com.zhizaolian.staff.entity.EmailEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.EmailService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.EmailVOTransformer;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.EmailVO;

public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private StaffService staffService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private EmailDao emailDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;

	@Override
	public void startEmail(EmailVO emailVO) {
		emailVO.setBusinessType(BusinessTypeEnum.EMAIL.getName());
		emailVO.setTitle(emailVO.getRequestUserName()+"的"+BusinessTypeEnum.EMAIL.getName());
		//初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", emailVO);
		String supervisor = staffService.querySupervisor(emailVO.getRequestUserID());
		
		List<String> emailAuditUsers = permissionService.findUsersByPermissionCode(Constants.EMAIL_AUDIT);
		List<String> emailAuditGroups = permissionService.findGroupsByPermissionCode(Constants.EMAIL_AUDIT);
		List<String> openMailBoxUsers = permissionService.findUsersByPermissionCode(Constants.OPEN_MAILBOX);
		List<String> openMailBoxGroups = permissionService.findGroupsByPermissionCode(Constants.OPEN_MAILBOX);
		if ((!staffService.hasGroupMember(emailAuditGroups) && CollectionUtils.isEmpty(emailAuditUsers)) ||
				(!staffService.hasGroupMember(openMailBoxGroups) && CollectionUtils.isEmpty(openMailBoxUsers))) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		
		vars.put("supervisor", supervisor);
		vars.put("emailAuditUsers", emailAuditUsers);
		vars.put("emailAuditGroups", emailAuditGroups);
		vars.put("openMailBoxUsers", openMailBoxUsers);
		vars.put("openMailBoxGroups", openMailBoxGroups);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EMAIL);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), emailVO.getUserID());
		taskService.complete(task.getId(), vars);
		
		saveEmail(emailVO, processInstance.getId());
	}
	
	@Override
	public ListResult<EmailVO> findEmailListByUserID(String userID, int page, int limit) {
		//查询OA_Email表的数据
		List<EmailEntity> emailEntities = emailDao.findEmailsByUserID(userID, page, limit);
		List<EmailVO> emailVOs = new ArrayList<EmailVO>();
		for (EmailEntity email : emailEntities) {
			EmailVO emailVO = new EmailVO();
			emailVO.setProcessInstanceID(email.getProcessInstanceID());
			emailVO.setAddress(email.getAddress());
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery()
					.processInstanceId(email.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					EmailVO arg = (EmailVO) variable.getValue();
					emailVO.setRequestDate(arg.getRequestDate());
					emailVO.setTitle(arg.getTitle());
					emailVO.setRequestUserName(arg.getRequestUserName());
				}
			}
			
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(email.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				emailVO.setStatus("处理中");
				emailVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				emailVO.setStatus(TaskResultEnum.valueOf(email.getProcessStatus()).getName());
			}
			emailVOs.add(emailVO);
		}
		
		int count = emailDao.countEmailsByUserID(userID);
		return new ListResult<EmailVO>(emailVOs, count);
	}
	
	@Override 
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		emailDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	@Override
	public EmailVO getEmailVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (EmailVO) runtimeService.getVariable(pInstance.getId(), "arg");
	}
	
	@Override
	public EmailVO getEmailVOByProcessInstanceID(String processInstanceID) {
		EmailEntity emailEntity = emailDao.getEmailByProcessInstanceID(processInstanceID);
		return EmailVOTransformer.INSTANCE.apply(emailEntity);
	}
	
	@Override
	public void confirmEmailAccount(String processInstanceID, String address, String password, String loginUrl) {
		emailDao.updateEmailAccountByProcessInstanceID(processInstanceID, address, password, loginUrl);
	}
	
	private void saveEmail(EmailVO emailVO, String processInstanceID) {
		Date now = new Date();
		EmailEntity emailEntity = EmailEntity.builder()
										.userID(emailVO.getUserID())
										.requestUserID(emailVO.getRequestUserID())
										.address(emailVO.getAddress())
										.reason(emailVO.getReason())
										.processInstanceID(processInstanceID)
										.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
										.addTime(now)
										.updateTime(now)
										.build();
		emailDao.save(emailEntity);
	}
}
