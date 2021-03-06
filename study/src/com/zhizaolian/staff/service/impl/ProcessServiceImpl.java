package com.zhizaolian.staff.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CredentialEntity;
import com.zhizaolian.staff.entity.CredentialUploadEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssignmentService;
import com.zhizaolian.staff.service.BankAccountService;
import com.zhizaolian.staff.service.CarUseService;
import com.zhizaolian.staff.service.CardService;
import com.zhizaolian.staff.service.CertificateService;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.CommonSubjectService;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.EmailService;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.HandlePropertyService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.PurchasePropertyService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.ShopApplyService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TransferPropertyService;
import com.zhizaolian.staff.service.TripService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CommonSubjectTaskVo;
import com.zhizaolian.staff.vo.CommonSubjectVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.CoursePlanTaskVo;
import com.zhizaolian.staff.vo.CoursePlanVo;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.SoftPerformanceTaskVO;
import com.zhizaolian.staff.vo.SoftPerformanceVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;



public class ProcessServiceImpl implements ProcessService{

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private FormalService formalService;
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private CardService cardService;
	@Autowired
	private TripService tripService;
	@Autowired
	private ChopService chopService;
	@Autowired
	private SocialSecurityService socialSecurityService;	
	@Autowired
	private CarUseService carUseService;
	@Autowired
	private SoftPerformanceService softPerformanceService;
	@Autowired
	private CommonSubjectService commonSubjectService;
	
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private PurchasePropertyService purchasePropertyService;
	@Autowired
	private HandlePropertyService handlePropertyService;
	@Autowired
	private TransferPropertyService transferPropertyService;
	@Autowired
	private ShopApplyService shopApplyService;
	@Autowired
	private WorkOvertimeService workOvertimeService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private ViewReportService viewReportService;
	@Override
	public List<TaskVO> createTaskVOList(List<Task> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		if(null == tasks){
			return taskVOs;
		}
		for (Task task : tasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(), "arg");
			if(null == arg){
				continue;
			}
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pInstance.getProcessDefinitionId()).singleResult();
			taskVO.setBusinessKey(processDefinition.getKey());
			taskVO.setCreateTime(task.getCreateTime()==null?"":DateUtil.formateFullDate(task.getCreateTime()));
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	
	



	@Override
	public List<CommonSubjectTaskVo> createCommonSubjectVoList(List<Task> tasks) {
		List<CommonSubjectTaskVo> taskVOs = new ArrayList<CommonSubjectTaskVo>();
		for (Task task : tasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			CommonSubjectVo arg = (CommonSubjectVo) runtimeService.getVariable(pInstance.getId(), "arg");
			CommonSubjectTaskVo taskVO = new CommonSubjectTaskVo();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pInstance.getProcessDefinitionId()).singleResult();
			taskVO.setBusinessKey(processDefinition.getKey());
			taskVO.setCreateTime(task.getCreateTime()==null?"":DateUtil.formateFullDate(task.getCreateTime()));
			taskVO.setTitle_(arg.getTitle_());
			taskVO.setRoute(arg.getRoute());
			taskVO.setType(arg.getType());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}





	@Override
	public List<SoftPerformanceTaskVO> createSoftPerformanceTaskVoList(
			List<Task> tasks) {
		List<SoftPerformanceTaskVO> taskVOs = new ArrayList<SoftPerformanceTaskVO>();
		for (Task task : tasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
			SoftPerformanceTaskVO taskVO = new SoftPerformanceTaskVO();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setScore(arg.getScore());
			taskVO.setTaskName(task.getName());
			taskVO.setLimitTime(arg.getDeadline()==null?"":DateUtil.formateDate(arg.getDeadline()));
			taskVO.setRequirementName(arg.getRequirementName());
			taskVO.setVersionName(arg.getVersionName());
			taskVO.setModuleName(arg.getModuleName());
			taskVO.setProjectName(arg.getProjectName());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pInstance.getProcessDefinitionId()).singleResult();
			taskVO.setBusinessKey(processDefinition.getKey());
			taskVO.setCreateTime(task.getCreateTime()==null?"":DateUtil.formateFullDate(task.getCreateTime()));
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}





	@Override
	public List<FormField> getFormFields(String taskID) {
		ProcessInstance pInstance = getProcessInstance(taskID);
		BaseVO baseVO = (BaseVO) runtimeService.getVariable(pInstance.getId(), "arg");
		return baseVO.getFormFields();
	}
	
	@Override
	public List<FormField> getFormFieldsByProcessInstanceID(String processInstanceID) {
		List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(processInstanceID).list();
		BaseVO baseVO = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			if ("arg".equals(variable.getVariableName())) {
				baseVO = (BaseVO) variable.getValue();
			}
		}
		return baseVO==null ? Collections.<FormField>emptyList() : baseVO.getFormFields();
	}
	
	@Override
	public List<CommentVO> getComments(String taskID) {
		ProcessInstance pi = getProcessInstance(taskID);
		return getCommentsByProcessInstanceID(pi.getId());
	}
	
	@Override
	public List<CommentVO> getCommentsByProcessInstanceID(String processInstanceID) {
		List<CommentVO> result = new ArrayList<CommentVO>();
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceID);
		for (Comment c : comments) {
			if (StringUtils.isBlank(c.getUserId())) {
				continue;
			}
			StaffVO staff = staffService.getStaffByUserID(c.getUserId());
			CommentVO vo = new CommentVO();
			vo.setContent(c.getFullMessage());
			vo.setTime(DateUtil.formateFullDate(c.getTime()));
			vo.setUserName(staff.getLastName());
			vo.setTaskID(c.getTaskId());
			result.add(vo);
		}
		return result;
	}
	
	@Override
	public void completeTask(String taskID, String userID, TaskResultEnum result, String comment){
		ProcessInstance pInstance = getProcessInstance(taskID);
		identityService.setAuthenticatedUserId(userID);
		if (!StringUtils.isBlank(comment)) {
			//添加评论
			taskService.addComment(taskID, pInstance.getId(), comment);
		}
		//完成任务
		Map<String, Object> vars = new HashMap<String, Object>();
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		String name = getTaskResultName(task.getTaskDefinitionKey());
		if (!StringUtils.isBlank(name)) {
			if (result == null) {
				throw new RuntimeException("处理结果不合法");
			}
			//这里塞入的值比较特殊  commonSubject需要有额外的跳出循环的结果
			if(TaskDefKeyEnum.COMMONSUBJECT_CX.getName().equals(name)){
				//弱国结果是未通过
				if(TaskResultEnum.DISAGREE.getValue()==result.getValue()){
					vars.put(name, result.getValue());		
				}
			}else{
				vars.put(name, result.getValue());				
			}
		}
		//判断是否是预付申请的打款成功
		if(task.getProcessDefinitionId().contains(Constants.ADVANCE)
				&& null!=result && result.getValue()==TaskResultEnum.REMIT_SUCCESS.getValue()){
			AdvanceVo advance = reimbursementService.geAdvanceTaskVOByProcessInstanceID(pInstance.getId());
			vars.put("applyer", advance.getRequestUserID());
			List<String> invoiceAuditors = permissionService.findUsersByPermissionCode(Constants.AUDIT_INVOICE);
			if(invoiceAuditors.size()<1){
				throw new RuntimeException("未找到发票审核人员，请联系系统管理员配置");
			}
			vars.put("invoiceAuditor", invoiceAuditors);
		}
		taskService.setAssignee(taskID, userID);
		taskService.complete(taskID, vars);
	}
	
	@Override
	public void completeTask(String taskID, String userID,
			TaskResultEnum result, String comment,
			Map<String,Object> keys) {
		ProcessInstance pInstance = getProcessInstance(taskID);
		identityService.setAuthenticatedUserId(userID);
		if (!StringUtils.isBlank(comment)) {
			//添加评论
			taskService.addComment(taskID, pInstance.getId(), comment);
		}
		//完成任务
		Map<String, Object> vars = new HashMap<String, Object>();
		String name = getTaskResultName(taskService.createTaskQuery().taskId(taskID).singleResult().getTaskDefinitionKey());
		if (!StringUtils.isBlank(name)) {
			if (result == null) {
				throw new RuntimeException("处理结果不合法");
			}
			vars.put(name, result.getValue());
			for(Entry<String, Object> entry:keys.entrySet()){
				vars.put(entry.getKey(), entry.getValue());
			}
		}
		taskService.setAssignee(taskID, userID);
		taskService.complete(taskID, vars);
	}


	private String getTaskResultName(String taskDefKey) {
		if (TaskDefKeyEnum.VACATION_SUPERVISOR_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.VACATION_SUPERVISOR_AUDIT.getResult();
		} else if (TaskDefKeyEnum.VACATION_MANAGER_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.VACATION_MANAGER_AUDIT.getResult();
		} else if (TaskDefKeyEnum.VACATION_HR_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.VACATION_HR_AUDIT.getResult();
		} else if (TaskDefKeyEnum.ASSIGNMENT_CONFIRM.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.ASSIGNMENT_CONFIRM.getResult();
		} else if (TaskDefKeyEnum.ASSIGNMENT_MODIFY.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.ASSIGNMENT_MODIFY.getResult();
		} else if (TaskDefKeyEnum.SUPERVISOR_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.SUPERVISOR_AUDIT.getResult();
		} else if (TaskDefKeyEnum.MANAGER_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.MANAGER_AUDIT.getResult();
		} else if (TaskDefKeyEnum.HR_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.HR_AUDIT.getResult();
		} else if (TaskDefKeyEnum.FORMAL_HR_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.FORMAL_HR_AUDIT.getResult();
		} else if (TaskDefKeyEnum.FORMAL_INVITATION.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.FORMAL_INVITATION.getResult();
		} else if (TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT.getResult();
		} else if (TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT.getResult();
		} else if (TaskDefKeyEnum.REMIT_MONEY.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.REMIT_MONEY.getResult();
		} else if (TaskDefKeyEnum.EMAIL_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.EMAIL_AUDIT.getResult();
		} else if (TaskDefKeyEnum.OPEN_MAILBOX.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.OPEN_MAILBOX.getResult();
		} else if (TaskDefKeyEnum.CARD_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.CARD_AUDIT.getResult();
		}else if (TaskDefKeyEnum.BUSSINESSTRIP_BUYTICKET.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.BUSSINESSTRIP_BUYTICKET.getResult();
		} else if (TaskDefKeyEnum.BUSSINESSTRIP_CONFIRM.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.BUSSINESSTRIP_CONFIRM.getResult();
		} else if (TaskDefKeyEnum.SS_MANAGER_AUDIT.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.SS_MANAGER_AUDIT.getResult();
		} else if (TaskDefKeyEnum.SS_FINANCIAL_PROCESSING.getName().equals(taskDefKey)) {
			return TaskDefKeyEnum.SS_FINANCIAL_PROCESSING.getResult();
		}else if(TaskDefKeyEnum.CHOP_BORROW_SUBJECT.getName().equals(taskDefKey))
			return TaskDefKeyEnum.CHOP_BORROW_SUBJECT.getResult();
		else if(TaskDefKeyEnum.ID_BORROW_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.ID_BORROW_SUBJECT.getResult();
		}else if(TaskDefKeyEnum.CONTRACT_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_SUBJECT.getResult();
		}else if(TaskDefKeyEnum.VACATION_SUPER_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VACATION_SUPER_SUBJECT.getResult();
		}else if(TaskDefKeyEnum.FINANCIAL_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.FINANCIAL_MANAGER_AUDIT.getResult();
		}else if(TaskDefKeyEnum.RESIGNATION_TRANSFER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.RESIGNATION_TRANSFER.getResult();
		}else if(TaskDefKeyEnum.VITAE_COMPANYLEADER_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VITAE_COMPANYLEADER_SUBJECT.getResult();
		}else if(TaskDefKeyEnum.VITAE_HRLEADER_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VITAE_HRLEADER_SUBJECT.getResult();
		}else if(TaskDefKeyEnum.VITAE_START_INVITE.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VITAE_START_INVITE.getResult();
		}else if(TaskDefKeyEnum.VITAE_STEP1.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VITAE_STEP1.getResult();
		}else if(TaskDefKeyEnum.VITAE_STEP3.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.VITAE_STEP3.getResult();
		}else if(TaskDefKeyEnum.SOFT_CONFIRMTASK.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SOFT_CONFIRMTASK.getResult();
		}else if(TaskDefKeyEnum.SOFT_GROUPLEADERCHECK.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SOFT_GROUPLEADERCHECK.getResult();
		}else if(TaskDefKeyEnum.SOFT_TESTCHECK.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SOFT_TESTCHECK.getResult();
		}else if(TaskDefKeyEnum.SOFT_EDITTASK.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SOFT_EDITTASK.getResult();
		}else if(TaskDefKeyEnum.CHOP_BORROW_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_BORROW_SUPERVISOR_AUDIT.getResult();
		}else if(TaskDefKeyEnum.CHOP_BORROW_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_BORROW_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CERTIFICATE_BORROW_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CERTIFICATE_BORROW_SUBJECT.getResult();
		}
		else if(TaskDefKeyEnum.CERTIFICATE_BORROW_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CERTIFICATE_BORROW_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CERTIFICATE_BORROW_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CERTIFICATE_BORROW_SUPERVISOR_AUDIT.getResult();
		}else if(TaskDefKeyEnum.COMMONSUBJECT_CX.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.COMMONSUBJECT_CX.getResult();
		}
		else if(TaskDefKeyEnum.CERTIFICATE_BORROW_BORROW.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CERTIFICATE_BORROW_BORROW.getResult();
		}
		else if(TaskDefKeyEnum.CERTIFICATE_BORROW_RETURN.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CERTIFICATE_BORROW_RETURN.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_BORROW_SUBJECT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_BORROW_SUBJECT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_BORROW_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_BORROW_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_BORROW_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_BORROW_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_BORROW_BORROW.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_BORROW_BORROW.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_BORROW_RETURN.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_BORROW_RETURN.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_SIGN_FINANCIAL.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_SIGN_FINANCIAL.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_SIGN_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_SIGN_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_SIGN_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_SIGN_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_SIGN_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_SIGN_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_CHANGE_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_CHANGE_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_CHANGE_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_CHANGE_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CONTRACT_CHANGE_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CONTRACT_CHANGE_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BANK_ACCOUNT_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BANK_ACCOUNT_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.BANK_ACCOUNT_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BANK_ACCOUNT_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BANK_ACCOUNT_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BANK_ACCOUNT_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BANK_ACCOUNT_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BANK_ACCOUNT_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BANK_ACCOUNT_FINAL_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BANK_ACCOUNT_FINAL_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_CHOPMANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_CHOPMANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_HANDOVER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_HANDOVER.getResult();
		}
		else if(TaskDefKeyEnum.CHOP_DESTROY_COMPLETE.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CHOP_DESTROY_COMPLETE.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_PURCHASER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_PURCHASER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_BUDGET_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_BUDGET_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_OFFICE_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_OFFICE_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_PURCHASER_CONFIRM.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_PURCHASER_CONFIRM.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_BUDGET_CONFIRM.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_BUDGET_CONFIRM.getResult();
		}
		else if(TaskDefKeyEnum.PURCHASE_PROPERTY_SIGN.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PURCHASE_PROPERTY_SIGN.getResult();
		}
		else if(TaskDefKeyEnum.CARVE_CHOP_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CARVE_CHOP_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CARVE_CHOP_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CARVE_CHOP_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.CARVE_CHOP_FINAL_MANAGER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CARVE_CHOP_FINAL_MANAGER.getResult();
		}
		else if(TaskDefKeyEnum.CARVE_CHOP_ADMINISTRATION_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.CARVE_CHOP_ADMINISTRATION_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.HANDLE_PROPERTY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.HANDLE_PROPERTY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.HANDLE_PROPERTY_MANAGE_DEPARTMENT_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.HANDLE_PROPERTY_MANAGE_DEPARTMENT_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.HANDLE_PROPERTY_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.HANDLE_PROPERTY_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.HANDLE_PROPERTY_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.HANDLE_PROPERTY_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.TRANSFER_PROPERTY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.TRANSFER_PROPERTY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.TRANSFER_PROPERTY_MANAGE_DEPARTMENT_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.TRANSFER_PROPERTY_MANAGE_DEPARTMENT_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.TRANSFER_PROPERTY_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.TRANSFER_PROPERTY_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.TRANSFER_PROPERTY_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.TRANSFER_PROPERTY_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.TRANSFER_PROPERTY_COMPLETE_TRANSFER.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.TRANSFER_PROPERTY_COMPLETE_TRANSFER.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_APPLY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_APPLY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_APPLY_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_APPLY_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_APPLY_FINAL_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_APPLY_FINAL_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_APPLY_FINAL_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_APPLY_FINAL_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_APPLY_HANDLE_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_APPLY_HANDLE_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_PAY_APPLY_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_PAY_APPLY_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_PAY_APPLY_FINANCIAL_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_PAY_APPLY_FINANCIAL_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_PAY_APPLY_FINAL_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_PAY_APPLY_FINAL_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_PAY_APPLY_FINANCIAL_HANDLE.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_PAY_APPLY_FINANCIAL_HANDLE.getResult();
		}
		else if(TaskDefKeyEnum.SHOP_PAY_APPLY_HANDLE_SUCCESS.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.SHOP_PAY_APPLY_HANDLE_SUCCESS.getResult();
		}
		else if(TaskDefKeyEnum.WORK_OVERTIME_SUPERVISOR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.WORK_OVERTIME_SUPERVISOR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.WORK_OVERTIME_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.WORK_OVERTIME_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.WORK_OVERTIME_HR_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.WORK_OVERTIME_HR_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.FUND_ALLOCATION_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.FUND_ALLOCATION_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PROBLEM_ORDER_CONFIRM.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PROBLEM_ORDER_CONFIRM.getResult();
		}
		else if(TaskDefKeyEnum.ADVANCE_UPLOAD_INVOICE.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.ADVANCE_UPLOAD_INVOICE.getResult();
		}
		else if(TaskDefKeyEnum.MRONING_MEETING_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.MRONING_MEETING_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PROJECT_CHECK.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PROJECT_CHECK.getResult();
		}
		else if(TaskDefKeyEnum.PROJECT_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PROJECT_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.PROJECT_REPORT_PROGRESS.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PROJECT_REPORT_PROGRESS.getResult();
		}
		else if(TaskDefKeyEnum.BRAND_AUTH_MARKET_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BRAND_AUTH_MARKET_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BRAND_AUTH_FINAL_MANAGER_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BRAND_AUTH_FINAL_MANAGER_AUDIT.getResult();
		}
		else if(TaskDefKeyEnum.BRAND_AUTH_APPLY_STAMP.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BRAND_AUTH_APPLY_STAMP.getResult();
		}
		else if(TaskDefKeyEnum.BRAND_AUTH_COMPLETE_STAMP.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.BRAND_AUTH_COMPLETE_STAMP.getResult();
		}
		else if(TaskDefKeyEnum.PUBLIC_EVENT_MATCH.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PUBLIC_EVENT_MATCH.getResult();
		}
		else if(TaskDefKeyEnum.PUBLIC_EVENT_HANDLE.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PUBLIC_EVENT_HANDLE.getResult();
		}
		else if(TaskDefKeyEnum.PERFORMANCE_PM_AUDIT.getName().equals(taskDefKey)){
			return TaskDefKeyEnum.PERFORMANCE_PM_AUDIT.getResult();
		}
		return null;
		
	}
	
	@Override
	public ProcessInstance getProcessInstance(String taskID) {
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		return runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
	}
	
	@Override
	public String getProcessTaskAssignee(String processInstanceID) {
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceID).list();
		String userName = Arrays.toString(Lists2.transform(tasks, new SafeFunction<Task, String>() {
			@Override
			protected String safeApply(Task input) {
				if (!StringUtils.isEmpty(input.getAssignee())) {
					return staffService.getStaffByUserID(input.getAssignee()).getLastName();
				}
				return input.getName();
			}
		}).toArray());
		return userName.substring(1, userName.length()-1);
	}
	
	@Override
	public List<TaskVO> findFinishedTasksByProcessInstanceID(String processInstanceID) {
		List<HistoricTaskInstance> finishedTasks = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceID)
				.finished()
				.orderByHistoricTaskInstanceEndTime()
				.asc().list();
		return Lists2.transform(finishedTasks, new SafeFunction<HistoricTaskInstance, TaskVO>() {
			//需要一个map保存重复节点的结果该取第几个
			Map<String, Integer> indexMap=new HashMap<String,Integer>();
			@Override
			protected TaskVO safeApply(HistoricTaskInstance input) {
				TaskVO taskVO = new TaskVO();
				taskVO.setTaskID(input.getId());
				taskVO.setTaskName(input.getName());
				if (StringUtils.isBlank(input.getAssignee())) {
					return null;
				}
				StaffVO assignee = staffService.getStaffByUserID(input.getAssignee());
				taskVO.setAssigneeName(assignee.getLastName());
				taskVO.setAssigneeId(assignee.getUserID());
				Picture pic = null;
				try {
					pic=identityService.getUserPicture(assignee.getUserID());
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				taskVO.setAssigneePic(pic==null?null:pic.getBytes());
				taskVO.setEndTime(DateUtil.formateFullDate(input.getEndTime()));
				taskVO.setTaskDefKey(input.getTaskDefinitionKey());
				String resultName = getTaskResultName(input.getTaskDefinitionKey());
				
				if (!StringUtils.isBlank(resultName)) {
					List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(input.getProcessInstanceId()).list();
					int index=0;
					for (HistoricDetail historicDetail : datas) {			
						HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
						if (resultName.equals(variable.getVariableName())) {
							index++;
							Integer needIndex=indexMap.get(resultName);
							if(needIndex==null)needIndex=1;
							if(needIndex==index){
								taskVO.setResult(TaskResultEnum.valueOf((Integer)variable.getValue()).getName());
								indexMap.put(resultName,++needIndex);
								break;
							}
						}
					}
				}
				return taskVO;
			}
		});
	}
	
	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum result, String businessType) {
		if (BusinessTypeEnum.VACATION.getName().equals(businessType)) {
			//更新OA_Vacation表的流程节点状态processStatus
			vacationService.updateProcessStatus(processInstanceID, result);
			
		} else if (BusinessTypeEnum.ASSIGNMENT.getName().equals(businessType)) {
			//更新OA_Assignment表的流程节点状态 
			assignmentService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.RESIGNATION.getName().equals(businessType)) {
			//更新OA_Resignation表的流程节点状态
			resignationService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.FORMAL.getName().equals(businessType)) {
			//更新OA_Formal表的流程节点状态
			formalService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.REIMBURSEMENT.getName().equals(businessType)) {
			//更新OA_Reimbursement表的流程节点状态
			reimbursementService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.EMAIL.getName().equals(businessType)) {
			//更新OA_Email表的流程节点状态
			emailService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.CARD.getName().equals(businessType)) {
			//更新OA_IDCard表的流程节点状态
			cardService.updateProcessStatus(processInstanceID, result);
		} else if(BusinessTypeEnum.BUSSNIESSTRIP.getName().equals(businessType)){
			tripService.updateProcessStatus(processInstanceID, result);
		} else if (BusinessTypeEnum.SOCIAL_SECURITY.getName().equals(businessType)) {
			//更新OA_SocialSecurityProcess表的流程节点状态
			socialSecurityService.updateProcessStatus(processInstanceID, result);		
		}else if(BusinessTypeEnum.CHOP_BORROW.getName().equals(businessType)){
			chopService.updateChopBorrowStatus(processInstanceID, result.getValue());
		}else if(BusinessTypeEnum.ID_BORROW.getName().equals(businessType)){
			chopService.updateIdBorrowStatus(processInstanceID, result.getValue());
		}else if(BusinessTypeEnum.CONTRACT.getName().equals(businessType)){
			//chopService.updateContractStatus(processInstanceID, result.getValue());
			contractService.updateContractStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.CAR_USE.getName().equals(businessType)){
			carUseService.updateCarUseStatus(processInstanceID, result.getValue());
		}else if(BusinessTypeEnum.ADVANCE.getName().equals(businessType)){
			reimbursementService.updateAdvanceProcessStatus(processInstanceID, result);
		}else if(BusinessTypeEnum.PAYMENT.getName().equals(businessType)){
			reimbursementService.updatePaymentProcessStatus(processInstanceID, result);
		}
		else if(BusinessTypeEnum.SOFTPERFORMANCE.getName().equals(businessType)){
			softPerformanceService.updateProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.COMMONSUBJECT.getName().equals(businessType)){
			commonSubjectService.updateResult(result.getValue()+"",processInstanceID);
		}else if(BusinessTypeEnum.CERTIFICATE_BORROW.getName().equals(businessType)){
			certificateService.updateProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.CONTRACT_BORROW.getName().equals(businessType)){
			contractService.updateProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.CONTRACT_CHANGE.getName().equals(businessType)){
			contractService.updateChangeContractProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getName().equals(businessType)){
			bankAccountService.updateBankAccountProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.CHOP_DESTROY.getName().equals(businessType)){
			chopService.updateChopDestroyProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.PURCHASE_PROPERTY.getName().equals(businessType)){
			purchasePropertyService.updateProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.CARVE_CHOP.getName().equals(businessType)){
			chopService.updateCarveChopProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.HANDLE_PROPERTY.getName().equals(businessType)){
			handlePropertyService.updateHandelPropertyProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.TRANSFER_PROPERTY.getName().equals(businessType)){
			transferPropertyService.updateTransferPropertyProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.SHOP_APPLY.getName().equals(businessType)){
			shopApplyService.updateShopApplyProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.SHOP_PAY_APPLY.getName().equals(businessType)){
			shopApplyService.updateShopPayApplyProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.WORK_OVERTIME.getName().equals(businessType)){
			workOvertimeService.updateWorkOvertimeProcessStatus(result, processInstanceID);
		}else if(BusinessTypeEnum.PAYMENT.getName().equals(businessType)){
			reimbursementService.updatePaymentProcessStatus(processInstanceID, result);
		}else if(BusinessTypeEnum.VIEW_REPORT.getName().equals(businessType)){
			viewReportService.updateViewReportProcessStatus(processInstanceID, result);
		}
	}
	
	@Override
	public ListResult<TaskVO> findTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit) {
		String groupIDs = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String taskNames = Arrays.toString(Lists2.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
			@Override
			protected String safeApply(TaskDefKeyEnum input) {
				return "'"+input.getName()+"'";
			}
		}).toArray());
		String userIDs = Arrays.toString(Lists2.transform(users, new SafeFunction<String, String>() {
			@Override
			protected String safeApply(String input) {
				return "'"+input+"'";
			}
		}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("+taskNames.substring(1, taskNames.length()-1)+") "
				+ "and (identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+") "
				+ "or identityLink.USER_ID_ in ("+userIDs.substring(1, userIDs.length()-1)+"))";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<TaskVO> taskVOs = createTaskList(result);
		
		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("+taskNames.substring(1, taskNames.length()-1)+") "
				+ "and (identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+") "
				+ "or identityLink.USER_ID_ in ("+userIDs.substring(1, userIDs.length()-1)+"))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<TaskVO>(taskVOs, count);
	}
	
	private List<TaskVO> createTaskList(List<Object> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(), "arg");
			if(arg==null)continue;
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTaskDefKey((String) objs[3]);
			taskVO.setTitle(arg.getTitle());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}


	@Override
	public ListResult<Object> getAllInstanceIdByUserAndTypePrefix(String userId,
			String typePrefix,int page,int limit,String filter) {
		String tableName = "OA_Reimbursement";
		if("Advance".equals(typePrefix)){
			tableName = "OA_Advance";
		}else if("Payment".equals(typePrefix)){
			tableName = "OA_Payment";
		}
		String sql="SELECT\n" +
				"	*\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			p.PROC_INST_ID_,\n" +
				"			t.NAME_,\n" +
				"			p.START_TIME_,\n" +
				"			p.END_TIME_,\n" +
				"			t.START_TIME_ AS START_TIME_1,\n" +
				"			t.END_TIME_ AS END_TIME_1,\n" +
				" 			r.ReimbursementNo,r.TotalAmount,GROUP_CONCAT(detail.Purpose)"+
				"		FROM\n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			"+tableName+" r,OA_ReimbursementDetail detail\n"+
				"		WHERE\n" +
				"			p.PROC_DEF_ID_ LIKE '"+typePrefix+"%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND t.ASSIGNEE_ = '"+userId+"'\n" +
				"		AND r.ProcessInstanceID=p.PROC_INST_ID_ and r.ReimbursementID=detail.ReimbursementID\n";
				if(StringUtils.isNotBlank(filter)){
					sql+=filter;
				}
				if("Reimbursement".equals(typePrefix)){
					sql += " and (detail.Type='0' or detail.type is null) ";
				}else if("Advance".equals(typePrefix)){
					sql += " and detail.type='1'";
				}else{
					sql += " and detail.type='2'";
				}
				sql += "GROUP BY r.ReimbursementID ";
				sql+="	) a,\n" +
				"	(\n" +
				"		SELECT\n" +
				"			p.PROC_INST_ID_ as pi,\n" +
				"			s.StaffName\n" +
				"		FROM\n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			OA_Staff s\n" +
				"		WHERE\n" +
				"			p.PROC_DEF_ID_ LIKE '"+typePrefix+"%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND (t.NAME_ = '报销申请' || t.NAME_ = '预付申请' || t.NAME_ = '付款申请')\n" +
				"		and s.UserID=t.ASSIGNEE_\n" +
				"	) b\n" +
				"WHERE\n" +
				"	a.PROC_INST_ID_ = b.pi";
		
		sql+=" order by START_TIME_ desc ";
		List<Object> list=baseDao.findPageList(sql,page,limit);
		
		String countSql="SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	ACT_HI_PROCINST p,\n" +
				"	ACT_HI_TASKINST t,\n" +
				"  	"+tableName+" r "+
				"WHERE\n" +
				"	p.PROC_DEF_ID_ LIKE '"+typePrefix+"%'\n" +
				"AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"AND r.ProcessInstanceID=p.PROC_INST_ID_\n"+
				"AND t.ASSIGNEE_='"+userId+"'";
		if(StringUtils.isNotBlank(filter)){
			countSql+=filter;
		}
		int totalCount=((BigInteger)baseDao.getUniqueResult(countSql)).intValue();
		return new ListResult<>(list, totalCount);
	}





	@Override
	public void contractSignFinancialConfirm(String taskID, String userId, TaskResultEnum result, String comment,
			ContractSignVo contractSignVo) {
		ProcessInstance pInstance = getProcessInstance(taskID);
		identityService.setAuthenticatedUserId(userId);
		if (!StringUtils.isBlank(comment)) {
			//添加评论
			taskService.addComment(taskID, pInstance.getId(), comment);
		}
		//完成任务
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", contractSignVo);
		String name = getTaskResultName(taskService.createTaskQuery().taskId(taskID).singleResult().getTaskDefinitionKey());
		if (!StringUtils.isBlank(name)) {
			if (result == null) {
				throw new RuntimeException("处理结果不合法");
			}
			vars.put(name, result.getValue());
		}
		taskService.setAssignee(taskID, userId);
		taskService.complete(taskID, vars);
		
	}

	@Override
	public List<CoursePlanTaskVo> createCoursePlanTaskVoList(List<Task> classHours) {
		List<CoursePlanTaskVo> coursePlanTaskVos = new ArrayList<>();
		for(Task classHour: classHours){
			//查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(classHour.getProcessInstanceId()).singleResult();
			CoursePlanVo coursePlanVo = (CoursePlanVo) runtimeService.getVariable(processInstance.getId(), "arg");
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(classHour.getProcessDefinitionId()).singleResult();
			CoursePlanTaskVo coursePlanTaskVo = new CoursePlanTaskVo();
			coursePlanTaskVo.setBusinessKey(processDefinition.getKey());
			coursePlanTaskVo.setCourseName(coursePlanVo.getCourseName());
			coursePlanTaskVo.setTrainClass(coursePlanVo.getTrainClass());
			coursePlanTaskVo.setRequestUserName(coursePlanVo.getUserName());
			coursePlanTaskVo.setTaskName(classHour.getName());
			coursePlanTaskVo.setTaskID(classHour.getId());
			coursePlanTaskVo.setBeginTime(coursePlanVo.getBeginTime());
			coursePlanTaskVo.setPlace(coursePlanVo.getPlace());
			coursePlanTaskVo.setTrainHours(coursePlanVo.getTrainHours());
			coursePlanTaskVo.setCoursePlanId(coursePlanVo.getId());
			coursePlanTaskVos.add(coursePlanTaskVo);
		}
		return coursePlanTaskVos;
	}
	// 添加岗位资格证书--------------------------------
		@Override
		public void addCredentialEntity(String applyUserId, String applyExplain, String offerUserId) {
			// 初始化任务参数
			Map<String, Object> vars = new HashMap<String, Object>();
			// 申请人id
			vars.put("applyId", applyUserId);
			// 提供者id
			vars.put("offerId", offerUserId);
			// 审核人id
			vars.put("checkId", applyUserId);

			
			// 组
//			List<String> hrGroupList = queryHRGroupList(applyUserId);
//			List<Group> groups = identityService.createGroupQuery().groupMember(applyUserId).list();
//			List<String> groupIDs = Lists2.transform(groups, new SafeFunction<Group, String>() {
//				@Override
//				protected String safeApply(Group input) {
//					return input.getId();
//				}
//			});
			
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("postQualificationCertificate",
					vars);

			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

			taskService.setAssignee(task.getId(), offerUserId);
			taskService.complete(task.getId());

			Date date = new Date();
			String formatDate = null;
			DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatDate = dFormat.format(date);

			String sql = "INSERT INTO oa_credential (applyUserId,applyExplain,offerUserId,processInstanceID,status,addTime,isDeleted) VALUES ('"
					+ applyUserId + "','" + applyExplain + "','" + offerUserId + "','" + processInstance.getId() + "',1,'"
					+ formatDate + "',0)";
			baseDao.excuteSql(sql);
		}

		@Override
		public ListResult<CredentialEntity> findCredentialBy(Integer page, Integer limit) {
			String sql = "SELECT * FROM oa_credential WHERE isDeleted = 0";
			List<Object> objects = baseDao.findPageList(sql, page, limit);
			List<CredentialEntity> credentials = new ArrayList<CredentialEntity>();
			for (Object object : objects) {
				Object[] objs = (Object[]) object;
				CredentialEntity credentialEntity = new CredentialEntity();
				credentialEntity.setId((Integer) objs[0]);
				credentialEntity.setAddTime((Date) objs[1]);
				credentialEntity.setApplyExplain((String) objs[2]);
				credentialEntity.setApplyResult((String) objs[3]);
				credentialEntity.setApplyUserId((String) objs[4]);
				
				credentialEntity.setOfferUserId((String) objs[6]);
				credentialEntity.setProcessInstanceID((String) objs[7]);
				credentialEntity.setStatus((Integer) objs[8]);
				credentialEntity.setUpdateTime((Date) objs[9]);
				credentials.add(credentialEntity);
			}
			String countSql = "SELECT COUNT(id) FROM oa_credential WHERE isDeleted = 0";
			Object resultObjs = baseDao.getUniqueResult(countSql);
			int count = resultObjs == null ? 0 : ((BigInteger) resultObjs).intValue();

			ListResult<CredentialEntity> list = new ListResult<CredentialEntity>(credentials, count);

			return list;
		}
		
		
		@Autowired
		private StaffDao staffDao;
		@Override
		public List<CredentialEntity> findCredentialVOTasks(List<Task> credentialVOTasks) {
			List<CredentialEntity> credentialVOs = new ArrayList<>();
			if (null != credentialVOTasks) {
				for (Task credentialVOTask : credentialVOTasks) {
					CredentialEntity credentialEntity = getCredentialByProcessInstanceID(
							credentialVOTask.getProcessInstanceId());
					credentialEntity.setTaskId(credentialVOTask.getId());
					credentialEntity.setTaskName(credentialVOTask.getName());
					String applyUserName = staffDao.getStaffByUserID(credentialEntity.getApplyUserId()).getStaffName();
					credentialEntity.setApplyUserName(applyUserName);
					String applyPositionNames = staffService.getStaffByUserID(credentialEntity.getApplyUserId()).getPositionName();
					credentialEntity.setApplyPositionNames(applyPositionNames);
					String cfferUserName = staffDao.getStaffByUserID(credentialEntity.getOfferUserId()).getStaffName();
					credentialEntity.setOfferUserName(cfferUserName);
					credentialVOs.add(credentialEntity);
				}
			}
			return credentialVOs;
		}

		private CredentialEntity getCredentialByProcessInstanceID(String processInstanceID) {
			String hql = "from CredentialEntity where processInstanceID = " + processInstanceID;
			return (CredentialEntity) baseDao.hqlfindUniqueResult(hql);
		}

		@Override
		public void addCredentialUploadEntity(CredentialUploadEntity credentialUploadEntity) throws Exception {
			credentialUploadEntity.setAddTime(new Date());
			credentialUploadEntity.setCredentialPictureData(ObjectByteArrTransformer.inputStreamToByte(new FileInputStream(credentialUploadEntity.getCredentialPicture())));
			baseDao.hqlSave(credentialUploadEntity);
		}
		
		@Override
		public void updateCredentialEntity(Integer id) {
			Date date = new Date();
			DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formatDate = dFormat.format(date);
			String sql = "UPDATE oa_credential SET status = 2,updateTime = '"+formatDate+"' WHERE id = "+id;
			baseDao.excuteSql(sql);
		}

		@Override
		public List<CredentialEntity> getPersonalCredentialById(Integer id) throws Exception{
			String sql = "SELECT\n" +
					"	a.*, b.id credentialUploadId,\n" +
					"	b.addTime credentialUploadAddTime,\n" +
					"	credentialEntityId,\n" +
					"	credentialName,\n" +
					"	credentialPictureData,\n" +
					"	credentialUrl,\n" +
					"	b.updateTime credentialupdateTime\n" +
					"FROM\n" +
					"	oa_credential a\n" +
					"INNER JOIN oa_credentialupload b ON a.id = b.credentialEntityId\n" +
					"WHERE\n" +
					"	a.id = "+id+"\n" +
					"AND a.isDeleted = 0\n" +
					"AND b.isDeleted = 0";
			List<Object> objects = baseDao.findBySql(sql);
			List<CredentialEntity> credentialEntitys = new ArrayList<CredentialEntity>();
			for(Object object : objects){
				CredentialEntity credentialEntity = new CredentialEntity();
				Object[] objs = (Object[]) object;
				credentialEntity.setId((Integer) objs[0]);
				credentialEntity.setAddTime((Date) objs[1]);
				credentialEntity.setApplyExplain((String) objs[2]);
				credentialEntity.setApplyResult((String) objs[3]);
				credentialEntity.setApplyUserId((String) objs[4]);
				credentialEntity.setIsDeleted((Integer) objs[5]);
				credentialEntity.setOfferUserId((String) objs[6]);
				credentialEntity.setProcessInstanceID((String) objs[7]);
				credentialEntity.setStatus((Integer) objs[8]);
				credentialEntity.setUpdateTime((Date) objs[9]);
				credentialEntity.setCredentialUploadId((Integer) objs[10]);
				credentialEntity.setCredentialUploadAddTime((Date) objs[11]);
				credentialEntity.setCredentialEntityId((Integer) objs[12]);
				credentialEntity.setCredentialName((String) objs[13]);
//				credentialEntity.setCredentialPicture((File) ObjectByteArrTransformer.toObject((byte[]) objs[14]));
				credentialEntity.setCredentialPicture((byte[]) objs[14]);
				credentialEntity.setCredentialUrl((String) objs[15]);
				credentialEntity.setCredentialUpdateTime((Date) objs[16]);
				credentialEntitys.add(credentialEntity);
			}
			return credentialEntitys;
		}
		
		@Override
		public CredentialEntity getCredentialEntityBy(Integer Id) {
			String hql = "from CredentialEntity where id = "+Id+" and isDeleted = 0";
			return (CredentialEntity) baseDao.hqlfindUniqueResult(hql);
		}
		
		@Override
		public void checkCredential(String taskId, String result, String applyResult, Integer id) {
//			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
//					.processInstanceId(task.getProcessInstanceId()).singleResult();
//			identityService.setAuthenticatedUserId(userId);
			Map<String, Object> vars = new HashMap<>();
			vars.put("checkResult", Integer.parseInt(result));
			if(applyResult !=null){
				if(Integer.parseInt(result)==1){
					updateCredential(id,applyResult,3);
				}else if(Integer.parseInt(result)==2){
					updateCredential(id,applyResult,4);
				}
			}else{
				if(Integer.parseInt(result)==1){
					updateCredential(id,3);
				}else if(Integer.parseInt(result)==2){
					updateCredential(id,4);
				}
			}
			
			taskService.complete(taskId, vars);
		}

		@Override
		public void updateCredential(Integer id, String applyResult,Integer status) {
			Date date = new Date();
			DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formatDate = dFormat.format(date);
			String sql = "UPDATE oa_credential SET applyResult = '"+applyResult+"',status = "+status+",updateTime = '"+formatDate+"' WHERE id = "+id;
			baseDao.excuteSql(sql);
		}
		
		@Override
		public void updateCredential(Integer id, Integer status) {
			Date date = new Date();
			DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formatDate = dFormat.format(date);
			String sql = "UPDATE oa_credential SET applyResult = "+null+",status = "+status+",updateTime = '"+formatDate+"' WHERE id = "+id;
			baseDao.excuteSql(sql);
			
		}
		
		@Override
		public CredentialUploadEntity getCredentialUploadEntityById(Integer id) {
			String hql = "FROM CredentialUploadEntity WHERE id = "+id;
			return (CredentialUploadEntity) baseDao.hqlfindUniqueResult(hql);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<CredentialUploadEntity> getCredentialUploadEntityBy(Integer credentialEntityId) {
			String hql = "FROM CredentialUploadEntity WHERE credentialEntityId = "+
							credentialEntityId+" and isDeleted = 0";
			return (List<CredentialUploadEntity>) baseDao.hqlfind(hql);
		}
		
//		@Override
//		public void addCredentialUploadEntity(CredentialUploadEntity credentialUploadEntity) throws Exception {
//			credentialUploadEntity.setAddTime(new Date());
//			credentialUploadEntity.setCredentialPictureData(ObjectByteArrTransformer.inputStreamToByte(new FileInputStream(credentialUploadEntity.getCredentialPicture())));
//			baseDao.hqlSave(credentialUploadEntity);
//		}
		
		@Override
		public void updateCredentialUploadEntity(CredentialUploadEntity credentialUploadEntity,Integer id) {
//			Date date = new Date();
//			DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String formatDate = dFormat.format(date);
//			try {
//				credentialUploadEntity.setCredentialPictureData(ObjectByteArrTransformer.inputStreamToByte(new FileInputStream(credentialUploadEntity.getCredentialPicture())));
//				String sql = "UPDATE oa_credentialupload\n" +
//						"SET oa_credentialupload.credentialName = '"+credentialUploadEntity.getCredentialName()+"',\n" +
//						" credentialPictureData = "+ObjectByteArrTransformer.inputStreamToByte(new FileInputStream(credentialUploadEntity.getCredentialPicture()))+",\n" +
//						" credentialPictureExt = '"+credentialUploadEntity.getCredentialPictureExt()+"',\n" +
//						" oa_credentialupload.credentialUrl = '"+credentialUploadEntity.getCredentialUrl()+"',\n" +
//						" oa_credentialupload.updateTime = '"+formatDate+"'\n" +
//						"WHERE\n" +
//						"	oa_credentialupload.id ="+id;
//				baseDao.excuteSql(sql);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			System.out.println(credentialUploadEntity.getCredentialPicture());
			try {
				if(credentialUploadEntity.getCredentialPicture()!=null){
					credentialUploadEntity.setUpdateTime(new Date());
					credentialUploadEntity.setCredentialPictureData(ObjectByteArrTransformer.inputStreamToByte(new FileInputStream(credentialUploadEntity.getCredentialPicture())));
					baseDao.hqlUpdate(credentialUploadEntity);
				}else{
					Date date = new Date();
					DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String formatDate = dFormat.format(date);
					String sql = "UPDATE oa_credentialupload\n" +
							"SET credentialName = '"+credentialUploadEntity.getCredentialName()+"',\n" +
							" credentialUrl = '"+credentialUploadEntity.getCredentialUrl()+"',\n" +
							" updateTime = '"+formatDate+"'\n" +
							"WHERE\n" +
							"	id = "+credentialUploadEntity.getId();
					baseDao.excuteSql(sql);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void deleteCredentialUpload(Integer id) {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formarDate = dateFormat.format(date);
			String sql = "UPDATE oa_credentialupload\n" +
					"SET isDeleted = 1,\n" +
					" updateTime = '"+formarDate+"'\n" +
					"WHERE\n" +
					"	id = "+id;
			baseDao.excuteSql(sql);
		}


		
}
