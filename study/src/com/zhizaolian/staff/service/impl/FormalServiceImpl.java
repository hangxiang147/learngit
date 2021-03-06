package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.FormalDao;
import com.zhizaolian.staff.entity.FormalEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.FormalTypeEnum;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.FormalVOTransformer;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.FormalVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

public class FormalServiceImpl implements FormalService{

	@Autowired
	private FormalDao formalDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PermissionService permissionService;
	
	@Override
	public void startFormal(FormalVO formalVO, int type) {
		formalVO.setBusinessType(BusinessTypeEnum.FORMAL.getName());
		formalVO.setTitle(formalVO.getRequestUserName()+"的"+BusinessTypeEnum.FORMAL.getName());
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", formalVO);
		vars.put("type", type);
		if (type == FormalTypeEnum.INVITATION.getValue()) {
			vars.put(TaskDefKeyEnum.FORMAL_INVITATION.getResult(), TaskResultEnum.SEND.getValue());
		}
		String supervisor = staffService.querySupervisor(formalVO.getRequestUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(formalVO.getRequestUserID());
		} 
		if (StringUtils.isBlank(supervisor) || formalVO.getRequestUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(formalVO.getRequestUserID());
		}
		String manager = staffService.queryManager(formalVO.getRequestUserID());
		
		List<Group> groups = identityService.createGroupQuery().groupMember(formalVO.getRequestUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> formalInvitationGroups = permissionService.findGroupsByPermissionCodeCompany(Constants.FORMAL_INVITATION, companyID);
		List<String> formalInvitationUsers = permissionService.findUsersByPermissionCodeCompany(Constants.FORMAL_INVITATION, companyID);
		List<String> formalHRAuditGroups = permissionService.findGroupsByPermissionCode(Constants.FORMAL_HR_AUDIT);
		List<String> formalHRAuditUsers = permissionService.findUsersByPermissionCode(Constants.FORMAL_HR_AUDIT);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请的分公司总经理！");
		}
		if ((!staffService.hasGroupMember(formalInvitationGroups) && CollectionUtils.isEmpty(formalInvitationUsers)) || 
				(!staffService.hasGroupMember(formalHRAuditGroups) && CollectionUtils.isEmpty(formalHRAuditUsers))) {
			throw new RuntimeException("未找到该申请的转正邀请人或转正审批人！");
		}
		
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("formalInvitationGroups", formalInvitationGroups);
		vars.put("formalInvitationUsers", formalInvitationUsers);
		vars.put("formalHRAuditGroups", formalHRAuditGroups);
		vars.put("formalHRAuditUsers", formalHRAuditUsers);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.FORMAL);
		//查询第一个任务
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		if (type == FormalTypeEnum.INVITATION.getValue()) {
			taskService.complete(task.getId(), vars);
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		}
		//设置任务受理人
		taskService.setAssignee(task.getId(), formalVO.getUserID());
		//完成任务
		taskService.complete(task.getId(), vars);
		//记录转正数据
		saveFormal(formalVO, processInstance.getId());
	}
	
	@Override
	public boolean hasSubmitted(String userID) {
		List<FormalEntity> formalEntities = formalDao.findFormalsByRequestUserID(userID);
		for (FormalEntity formalEntity : formalEntities) {
			if (runtimeService.createProcessInstanceQuery().processInstanceId(formalEntity.getProcessInstanceID()).singleResult() != null) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ListResult<FormalVO> findFormalListByUserID(String userID, int page, int limit) {
		//查询OA_Formal表的数据
		List<FormalEntity> formalEntities = formalDao.findFormalsByUserID(userID, page, limit);
		List<FormalVO> formalVOs = new ArrayList<FormalVO>();
		for (FormalEntity formal : formalEntities) {
			FormalVO formalVO = new FormalVO();
			formalVO.setProcessInstanceID(formal.getProcessInstanceID());
			formalVO.setRequestFormalDate(formal.getRequestFormalDate()==null?"":DateUtil.formateDate(formal.getRequestFormalDate()));
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(formal.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					FormalVO arg = (FormalVO) variable.getValue();
					formalVO.setRequestDate(arg.getRequestDate());
					formalVO.setTitle(arg.getTitle());
					formalVO.setRequestFormalDate(arg.getRequestFormalDate());
				}
			}
			
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(formal.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				formalVO.setStatus("处理中");
				formalVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				formalVO.setStatus(TaskResultEnum.valueOf(formal.getProcessStatus()).getName());
			}
			formalVOs.add(formalVO);
		}
		
		int count = formalDao.countFormalsByUserID(userID);
		return new ListResult<FormalVO>(formalVOs, count);
	}
	
	@Override
	public ListResult<TaskVO> findFormalTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit) {
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
		List<TaskVO> taskVOs = createTaskVOList(result);
		
		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("+taskNames.substring(1, taskNames.length()-1)+") "
				+ "and (identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+") "
				+ "or identityLink.USER_ID_ in ("+userIDs.substring(1, userIDs.length()-1)+"))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<TaskVO>(taskVOs, count);
	}
	
	@Override
	public void updateFormal(FormalVO formalVO, String processInstanceID, TaskResultEnum result) {
		FormalEntity formalEntity = formalDao.getFormalByProcessInstanceID(processInstanceID);
		if (result != null) {
			formalEntity.setProcessStatus(result.getValue());
		}
		if (!StringUtils.isBlank(formalVO.getRequestFormalDate())) {
			formalEntity.setRequestFormalDate(DateUtil.getSimpleDate(formalVO.getRequestFormalDate()));
		}
		if (formalVO.getGradeID() != null) {
			formalEntity.setGradeID(formalVO.getGradeID());
		}
		if (!StringUtils.isBlank(formalVO.getSalary())) {
			formalEntity.setSalary(formalVO.getSalary());
		}
		if (!StringUtils.isBlank(formalVO.getSocialSecurity())) {
			formalEntity.setSocialSecurity(formalVO.getSocialSecurity());
		}
		if (!StringUtils.isBlank(formalVO.getSummary())) {
			formalEntity.setSummary(formalVO.getSummary());
		}
		formalDao.save(formalEntity);
	}

	@Override 
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		formalDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	@Override
	public void confirmFormalDate(String processInstanceID, Date formalDate) {
		FormalVO formal = (FormalVO) runtimeService.getVariable(processInstanceID, "arg");
		formal.setActualFormalDate(DateUtil.formateDate(formalDate));
		runtimeService.setVariable(processInstanceID, "arg", formal);
		
		formalDao.updateActualFormalDate(processInstanceID, formalDate);
	}
	
	@Override
	public List<FormalVO> findNotEndFormals() {
		String sql = "SELECT formal.RequestUserID, formal.ProcessInstanceID FROM OA_Formal formal, ACT_HI_PROCINST proc WHERE "
				+ "formal.ProcessInstanceID = proc.PROC_INST_ID_ AND proc.END_ACT_ID_ IS NOT NULL "
				+ "AND formal.IsDeleted = 0";
		List<Object> result = baseDao.findBySql(sql);
		List<FormalVO> formalVOs = new ArrayList<FormalVO>();
		for (Object formal : result) {
			Object[] objs = (Object[]) formal;
			FormalVO formalVO = new FormalVO();
			formalVO.setRequestUserID((String) objs[0]);
			formalVO.setProcessInstanceID((String) objs[1]);
			formalVOs.add(formalVO);
		}
		return formalVOs;
	}
	
	@Override
	public FormalVO getFormalByProcessInstanceID(String processInstanceID) {
		FormalEntity formalEntity = formalDao.getFormalByProcessInstanceID(processInstanceID);
		return FormalVOTransformer.INSTANCE.apply(formalEntity);
	}
	
	@Override
	public List<TaskVO> createTaskVOListByTaskList(List<Task> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Task task : tasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			FormalVO arg = (FormalVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
			if (staffVO != null) {
				taskVO.setEntryDate(staffVO.getEntryDate());
				List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffVO.getUserID());
				taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
					@Override
					protected String safeApply(GroupDetailVO input) {
						return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
					}
				}));
			}
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	
	private List<TaskVO> createTaskVOList(List<Object> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			FormalVO arg = (FormalVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTaskDefKey((String) objs[3]);
			taskVO.setTitle(arg.getTitle());
			StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
			if (staffVO != null) {
				taskVO.setEntryDate(staffVO.getEntryDate());
				List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffVO.getUserID());
				taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
					@Override
					protected String safeApply(GroupDetailVO input) {
						return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
					}
				}));
			}
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	
	private void saveFormal(FormalVO formalVO, String processInstanceID) {
		Date now = new Date();
		FormalEntity formalEntity = FormalEntity.builder()
										.userID(formalVO.getUserID())
										.requestUserID(formalVO.getRequestUserID())
										.processInstanceID(processInstanceID)
										.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
										.addTime(now)
										.updateTime(now)
										.build();
		if (formalVO.getGradeID() != null) {
			formalEntity.setGradeID(formalVO.getGradeID());
		}
		if (!StringUtils.isBlank(formalVO.getSocialSecurity())) {
			formalEntity.setSocialSecurity(formalVO.getSocialSecurity());
		}
		if (!StringUtils.isBlank(formalVO.getSalary())) {
			formalEntity.setSalary(formalVO.getSalary());
		}
		formalDao.save(formalEntity);
	}
	@Override
	public ListResult<FormalVO> findFormalStaffApplicationList(String staffName, int page, int limit, String beginDate, String endDate) {
		//查询OA_Formal表的数据
		List<FormalEntity> formalEntities = formalDao.findFormalsByConditions(staffName, page, limit, beginDate, endDate);
		List<FormalVO> formalVOs = new ArrayList<FormalVO>();
		for (FormalEntity formal : formalEntities) {
			FormalVO formalVO = new FormalVO();
			formalVO.setProcessInstanceID(formal.getProcessInstanceID());
			formalVO.setRequestFormalDate(formal.getRequestFormalDate()==null?"":DateUtil.formateDate(formal.getRequestFormalDate()));
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(formal.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					FormalVO arg = (FormalVO) variable.getValue();
					formalVO.setRequestDate(arg.getRequestDate());
					formalVO.setRequestUserName(arg.getRequestUserName());
					formalVO.setRequestFormalDate(arg.getRequestFormalDate());
				}
			}
			
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(formal.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				formalVO.setStatus("处理中");
				formalVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				formalVO.setStatus(TaskResultEnum.valueOf(formal.getProcessStatus()).getName());
			}
			formalVOs.add(formalVO);
		}
		
		int count = formalDao.countFormalsByConditions(staffName, beginDate, endDate);
		return new ListResult<FormalVO>(formalVOs, count);
	}
}
