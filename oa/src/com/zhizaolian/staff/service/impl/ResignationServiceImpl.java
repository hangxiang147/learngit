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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.ResignationDao;
import com.zhizaolian.staff.entity.ResignationEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.ResignationVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

public class ResignationServiceImpl implements ResignationService {
	
	@Autowired
	private ResignationDao resignationDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionService positionService;

	@Override
	public void startResignation(ResignationVO resignationVO) {
		resignationVO.setBusinessType(BusinessTypeEnum.RESIGNATION.getName());
		resignationVO.setTitle(resignationVO.getRequestUserName()+"的"+BusinessTypeEnum.RESIGNATION.getName());
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", resignationVO);
		String supervisor = staffService.querySupervisor(resignationVO.getRequestUserID());
		String manager = staffService.queryManager(resignationVO.getRequestUserID());
		
		List<Group> groups = identityService.createGroupQuery().groupMember(resignationVO.getRequestUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> resignationHRAuditUsers = permissionService.findUsersByPermissionCodeCompany(Constants.RESIGNATION_HR_AUDIT, companyID);
		List<String> resignationHRAuditGroups = permissionService.findGroupsByPermissionCodeCompany(Constants.RESIGNATION_HR_AUDIT, companyID);
		List<String> resignationTransferGroups = permissionService.findGroupsByPermissionCodeCompany(Constants.RESIGNATION_TRANSFER, companyID);
		List<String> salarySettlementGroups = permissionService.findGroupsByPermissionCode(Constants.SALARY_SETTLEMENT);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		if ((!staffService.hasGroupMember(resignationHRAuditGroups) && CollectionUtils.isEmpty(resignationHRAuditUsers)) 
				||!staffService.hasGroupMember(resignationTransferGroups) 
				||!staffService.hasGroupMember(salarySettlementGroups)) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("resignationHRAuditUsers", resignationHRAuditUsers);
		vars.put("resignationHRAuditGroups", resignationHRAuditGroups);
		vars.put("resignationTransferGroups", resignationTransferGroups);
		vars.put("salarySettlementGroups", salarySettlementGroups);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.RESIGNATION);
		// 查询任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), resignationVO.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录离职数据
		saveResignation(resignationVO, processInstance.getId());
	}
	
	@Override
	public ListResult<ResignationVO> findResignationListByUserID(String userID, int page, int limit) {
		//查询OA_Resignation表的数据
		List<ResignationEntity> resignationEntities = resignationDao.findResignationsByUserID(userID, page, limit);
		List<ResignationVO> resignationVOs = new ArrayList<ResignationVO>();
		for (ResignationEntity resignation : resignationEntities) {
			ResignationVO resignationVO = new ResignationVO();
			resignationVO.setProcessInstanceID(resignation.getProcessInstanceID());
			resignationVO.setLeaveDate(resignation.getLeaveDate()==null?"":DateUtil.formateDate(resignation.getLeaveDate()));
			resignationVO.setReasons(resignation.getReasons());
			resignationVO.setNote(resignation.getNote());
			resignationVO.setSupervisorConfirmDate(resignation.getSupervisorConfirmDate()==null ? "" : DateUtil.formateDate(resignation.getSupervisorConfirmDate()));
			resignationVO.setManagerConfirmDate(resignation.getManagerConfirmDate()==null ? "" : DateUtil.formateDate(resignation.getManagerConfirmDate()));
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(resignation.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					ResignationVO arg = (ResignationVO) variable.getValue();
					resignationVO.setRequestDate(arg.getRequestDate());
					resignationVO.setRequestUserName(arg.getRequestUserName());
					resignationVO.setTitle(arg.getTitle());
				}
			}
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(resignation.getProcessInstanceID()).singleResult();
			
			if (pInstance != null) {
				resignationVO.setStatus("处理中");
				resignationVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				resignationVO.setStatus(TaskResultEnum.valueOf(resignation.getProcessStatus()).getName());
			}
			resignationVOs.add(resignationVO);
		}
		
		int count = resignationDao.countResignationsByUserID(userID);
		return new ListResult<ResignationVO>(resignationVOs, count);
	}
	
	@Override 
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		resignationDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	@Override
	public ListResult<TaskVO> findResignationTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit) {
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
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
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
	public ListResult<TaskVO> findResignationTasksByGroups(List<Group> groups, List<TaskDefKeyEnum> tasks, int page, int limit) {
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
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("+taskNames.substring(1, taskNames.length()-1)+") "
				+ "and identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+")";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<TaskVO> taskVOs = createTaskVOList(result);
		
		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("+taskNames.substring(1, taskNames.length()-1)+") "
				+ "and identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<TaskVO>(taskVOs, count);
	}
	
	@Override
	public List<TaskVO> createTaskVOListByTaskList(List<Task> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Task task : tasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			ResignationVO arg = (ResignationVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
			if (staffVO != null) {
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
	
	@Override
	public void confirmLeaveDate(String processInstanceID, Date leaveDate, String taskDefKey) {
		ResignationVO resignationVO = (ResignationVO) runtimeService.getVariable(processInstanceID, "arg");
		if (TaskDefKeyEnum.SUPERVISOR_AUDIT.getName().equals(taskDefKey)) {
			//主管确认离职日期
			resignationVO.setSupervisorConfirmDate(DateUtil.formateDate(leaveDate));
			resignationDao.updateSupervisorConfirmDate(processInstanceID, leaveDate);
		} else if (TaskDefKeyEnum.MANAGER_AUDIT.getName().equals(taskDefKey)) {
			//总经理确认离职日期
			resignationVO.setManagerConfirmDate(DateUtil.formateDate(leaveDate));
			resignationDao.updateManagerConfirmDate(processInstanceID, leaveDate);
		}
		runtimeService.setVariable(processInstanceID, "arg", resignationVO);
	}
	
	@Override
	public ResignationVO getResignationVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (ResignationVO) runtimeService.getVariable(pInstance.getId(), "arg");
	}
	
	private List<TaskVO> createTaskVOList(List<Object> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			ResignationVO arg = (ResignationVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTitle(arg.getTitle());
			StaffVO staffVO=null;
			try{
				 staffVO = staffService.getStaffByUserID(arg.getRequestUserID());				
			}catch(Exception e){
				e.printStackTrace();
			}
			if (staffVO != null) {
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
	
	private void saveResignation(ResignationVO resignation, String processInstanceID) {
		Date now = new Date();
		ResignationEntity resignationEntity = ResignationEntity.builder()
													.userID(resignation.getUserID())
													.requestUserID(resignation.getRequestUserID())
													.leaveDate(DateUtil.getSimpleDate(resignation.getLeaveDate()))
													.reasons(StringUtils.join(resignation.getReason(), "；"))
													.note(resignation.getNote())
													.processInstanceID(processInstanceID)
													.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
													.addTime(now)
													.updateTime(now)
													.build();
		resignationDao.save(resignationEntity);
	}

	@Override
	public ListResult<ResignationVO> findResignationByResignationVO(ResignationVO resignationVO, int page, int limit) {
		List<Object> list=baseDao.findPageList(getResignationListBySql(resignationVO), page, limit);
		List<ResignationVO> resignationVOs = new ArrayList<>();
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			ResignationVO resignationVo=new ResignationVO();
			resignationVo.setRequestUserName((String) objs[0]);
			resignationVo.setRequestUserID((String) objs[1]);
			resignationVo.setLeaveDate(objs[2]==null?"":DateUtil.formateDate((Date) objs[2]));
			resignationVo.setReasons((String) objs[3]);
			resignationVo.setNote((String) objs[4]);
			resignationVo.setRequestLeaveDate(objs[5]==null?"":DateUtil.formateDate((Date) objs[5]));
			resignationVo.setManagerConfirmDate(objs[6]==null?"":DateUtil.formateDate((Date) objs[6]));
			resignationVo.setRequestDate(objs[7]==null?"":DateUtil.formateFullDate((Date) objs[7]));
			resignationVo.setTelephone((String) objs[8]);
			resignationVo.setEntryDate(objs[9]==null?"":DateUtil.formateDate((Date) objs[9]));
			resignationVo.setStaffEntityStatus((Byte) objs[10]);
			resignationVo.setProcessStatus((Byte) objs[11]);
			
			resignationVo.setStatus((String) objs[13]);
			resignationVo.setAssigneeUserName((String) objs[14]);
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[1]);
			if(resignationVO.getCompanyID()!=null){
				for(GroupDetailVO group:groups){
					if(group.getCompanyID()==resignationVO.getCompanyID()){
						resignationVo.setDepartmentName(group.getDepartmentName());
				   }
				}
				
			}else{
				resignationVo.setDepartmentName(CollectionUtils.isEmpty(groups)?"":groups.get(0).getDepartmentName());
			}

			resignationVOs.add(resignationVo);
		}
		Object countObj=baseDao.getUniqueResult(getCountResignationBySql(resignationVO));
		int count=countObj == null? 0:((BigInteger)countObj).intValue();
		return new ListResult<ResignationVO>(resignationVOs,count);
	}
	
	
	private String getResignationListBySql(ResignationVO resignationVO){
		StringBuffer sql = new StringBuffer("SELECT * FROM ("
				+ " SELECT DISTINCT staff.StaffName,staff.UserID,staff.leaveDate,resignation.reasons,resignation.note,resignation.LeaveDate AS requestLeaveDate,"
				+ " resignation.managerConfirmDate,resignation.AddTime,staff.Telephone,staff.EntryDate,staff.`Status`,resignation.ProcessStatus,"
				+ " IFNULL(staff.leaveDate,resignation.managerConfirmDate) AS _leaveDate,"
				+ " task.NAME_,"
				+ " staff_sec.StaffName assigneeUserName"
				+ " FROM OA_Staff staff"
				+ " LEFT JOIN OA_Resignation resignation ON resignation.RequestUserID = staff.UserID"
				+ " LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_"
				+ " LEFT JOIN OA_GroupDetail groupDetail ON membership.GROUP_ID_ = groupDetail.GroupID"
				+ " LEFT JOIN act_ru_task task ON resignation.ProcessInstanceID = task.PROC_INST_ID_"
				+ " LEFT JOIN oa_staff staff_sec ON task.ASSIGNEE_ = staff_sec.UserID"
				+ " WHERE staff.IsDeleted = 0");
		sql.append(getWhereBySql(resignationVO));
		sql.append(" ORDER BY resignation.AddTime desc )a"
				+ " GROUP BY a.UserID ORDER BY a.leaveDate DESC,a.EntryDate ASC");
		return sql.toString();
	}
	
	private String getCountResignationBySql(ResignationVO resignationVO){
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM( ");
		sql.append(getResignationListBySql(resignationVO));
		sql.append(" ) b");
		return sql.toString();
	}
	
	private String getWhereBySql(ResignationVO resignationVO){
		StringBuffer whereSql=new StringBuffer();
		if(resignationVO.getStaffEntityStatus()==null || resignationVO.getStaffEntityStatus()==1){//默认进入页面选择：待离职
			whereSql.append(" AND staff.`Status` != 4 AND resignation.IsDeleted = 0 AND  (resignation.ProcessStatus IS NULL OR resignation.ProcessStatus = 1) ");
		}else if(resignationVO.getStaffEntityStatus()==2){//请选择
			whereSql.append(" AND (staff.`Status` = 4 OR (staff.`Status` != 4 AND resignation.IsDeleted = 0 AND  (resignation.ProcessStatus IS NULL OR resignation.ProcessStatus = 1))) ");
		}else if(resignationVO.getStaffEntityStatus()==4){//已离职
			whereSql.append(" AND staff.`Status` = 4 ");
		}
		
		if (!StringUtils.isBlank(resignationVO.getRequestUserName())) {
			whereSql.append(" and staff.StaffName like '%"+resignationVO.getRequestUserName()+"%' ");
		}
		if (!StringUtils.isBlank(resignationVO.getBeginDate())) {
			whereSql.append(" and staff.leaveDate >= '"+resignationVO.getBeginDate()+"'");
		}
		if (!StringUtils.isBlank(resignationVO.getEndDate())) {
			whereSql.append(" and staff.leaveDate <= '"+resignationVO.getEndDate()+"'");
		}
		if (resignationVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.companyID = "+resignationVO.getCompanyID());
			if (resignationVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(resignationVO.getCompanyID(), resignationVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(resignationVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.departmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}
		if (!StringUtils.isBlank(resignationVO.getStatus())){
			whereSql.append(" and task.NAME_ = '"+resignationVO.getStatus()+"'");
		}
		if(!StringUtils.isBlank(resignationVO.getAssigneeUserName())){
			whereSql.append(" and staff_sec.StaffName like '%"+resignationVO.getAssigneeUserName()+"%'");
		}
		return whereSql.toString();
		
	}

	@Override
	public XSSFWorkbook exportResignationVO(ResignationVO resignationVO) {
		List<Object> list=baseDao.findBySql(getResignationListBySql(resignationVO));
		List<ResignationVO> resignationVOs = new ArrayList<>();
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			ResignationVO resignation=new ResignationVO();
			resignation.setRequestUserName((String) objs[0]);
			resignation.setRequestUserID((String) objs[1]);
			resignation.setLeaveDate(objs[2]==null?"":DateUtil.formateDate((Date) objs[2]));
			resignation.setReasons((String) objs[3]);
			resignation.setNote((String) objs[4]);
			resignation.setRequestLeaveDate(objs[5]==null?"":DateUtil.formateDate((Date) objs[5]));
			resignation.setManagerConfirmDate(objs[6]==null?"":DateUtil.formateDate((Date) objs[6]));
			resignation.setRequestDate(objs[7]==null?"":DateUtil.formateFullDate((Date) objs[7]));
			resignation.setTelephone((String) objs[8]);
			resignation.setEntryDate(objs[9]==null?"":DateUtil.formateDate((Date) objs[9]));
			resignation.setStaffEntityStatus((Byte) objs[10]);
			resignation.setProcessStatus((Byte) objs[11]);
			
			resignation.setStatus((String) objs[13]);
			resignation.setAssigneeUserName((String) objs[14]);
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[1]);
			if(resignationVO.getCompanyID()!=null){
				for(GroupDetailVO group:groups){
					if(group.getCompanyID()==resignationVO.getCompanyID()){
						resignation.setDepartmentName(group.getDepartmentName());
				   }
				}
				
			}else{
				resignation.setDepartmentName(CollectionUtils.isEmpty(groups)?"":groups.get(0).getDepartmentName());
			}

			resignationVOs.add(resignation);
		}
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		XSSFRow row = sheet.createRow((int) 0);
		
		row.createCell((short) 0).setCellValue("部门");
		row.createCell((short) 1).setCellValue("离职人");
		row.createCell((short) 2).setCellValue("手机号");
		row.createCell((short) 3).setCellValue("状态");
		row.createCell((short) 4).setCellValue("入职日期");
		row.createCell((short) 5).setCellValue("申请时间");
		row.createCell((short) 6).setCellValue("申请离职日期");
		row.createCell((short) 7).setCellValue("总经理确认离职日期");
		row.createCell((short) 8).setCellValue("离职日期");
		row.createCell((short) 9).setCellValue("当前环节");
		row.createCell((short) 10).setCellValue("待处理人");
		
		for(int i=0,j=sheet.getLastRowNum()+1;i<resignationVOs.size();++i,++j){
			XSSFRow row_date = sheet.createRow(j);
			ResignationVO resignation = resignationVOs.get(i);
			
			row_date.createCell((short) 0).setCellValue(resignation.getDepartmentName());
			row_date.createCell(1).setCellValue(resignation.getRequestUserName());
			row_date.createCell(2).setCellValue(resignation.getTelephone());
			
			/*<c:choose>
  			<c:when test="${res.staffEntityStatus==4 }">
  				<td>已离职</td>
  			</c:when>
  			<c:when test="${res.staffEntityStatus!=4 && res.processStatus==1 }">
  				<td>待离职<br>(已批准)</td>
  			</c:when>
  			<c:otherwise>
  				<td>待离职</td>
  			</c:otherwise>
  			</c:choose>*/
			if(resignation.getStaffEntityStatus()==4){
				row_date.createCell(3).setCellValue("已离职");
			}else if(resignation.getStaffEntityStatus()!=4 && resignation.getProcessStatus() ==null){
				row_date.createCell(3).setCellValue("待离职");
			}else if(resignation.getStaffEntityStatus()!=4 && resignation.getProcessStatus() ==1){
				row_date.createCell(3).setCellValue("待离职(已批准)");
			}
			row_date.createCell(4).setCellValue(resignation.getEntryDate());
			row_date.createCell(5).setCellValue(resignation.getRequestDate());
			row_date.createCell(6).setCellValue(resignation.getRequestLeaveDate());
			row_date.createCell(7).setCellValue(resignation.getManagerConfirmDate());
			row_date.createCell(8).setCellValue(resignation.getLeaveDate());
			row_date.createCell(9).setCellValue(resignation.getStatus());
			row_date.createCell(10).setCellValue(resignation.getAssigneeUserName());
		}
		return wb;
	}
}
