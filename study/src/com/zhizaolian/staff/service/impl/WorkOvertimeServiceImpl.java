package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.entity.AttendanceDetailEntity;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.WorkOvertimeEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.WorkOvertimeTaskVo;
import com.zhizaolian.staff.vo.WorkOvertimeVo;

public class WorkOvertimeServiceImpl implements WorkOvertimeService{
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Override
	public void startWorkOvertime(WorkOvertimeVo workOvertimeVo) {
		workOvertimeVo.setBusinessType(BusinessTypeEnum.WORK_OVERTIME.getName());
		if(Constants.DEP.equals(workOvertimeVo.getType())){
			CompanyEntity company = companyDao.getCompanyByCompanyID(Integer.parseInt(workOvertimeVo.getCompanyId()));
			DepartmentEntity deparment = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(workOvertimeVo.getDepartmentId()));
			workOvertimeVo.setTitle(company.getCompanyName()+"-"+deparment.getDepartmentName()+"的"+BusinessTypeEnum.WORK_OVERTIME.getName());
			workOvertimeVo.setRequestUserName(company.getCompanyName()+"-"+deparment.getDepartmentName());
		}else{
			workOvertimeVo.setTitle(workOvertimeVo.getRequestUserName()+"的"+BusinessTypeEnum.WORK_OVERTIME.getName());
		}
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", workOvertimeVo);
		String requestUserId = "";
		if(Constants.DEP.equals(workOvertimeVo.getType())){
			//同一个部门的加班人员
			String[] userIds = workOvertimeVo.getRequestUserID().split(",");
			//任取一个
			requestUserId = userIds[0];
		}else{
			requestUserId = workOvertimeVo.getRequestUserID();
		}
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(requestUserId);
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor = staffService.queryHeadMan(requestUserId);
		} 
		if (StringUtils.isBlank(supervisor) || requestUserId.equals(supervisor)) {
			supervisor = staffService.querySupervisor(requestUserId);
		}
		String manager = staffService.queryManager(requestUserId);
		List<String> hrGroupList = staffService.queryHRGroupList(requestUserId);
		//无上级主管，总经理审批
		if (StringUtils.isBlank(supervisor) && StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请的审批人（总经理）！");
		}
		if (CollectionUtils.isEmpty(hrGroupList) || !staffService.hasGroupMember(hrGroupList)) {
			throw new RuntimeException("未找到该申请的审批人（人力资源）！");
		}
		vars.put("hrGroup", hrGroupList);
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.WORK_OVERTIME);
		workOvertimeVo.setProcessInstanceID(processInstance.getId());
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), workOvertimeVo.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录加班数据
		saveWorkOvertime(workOvertimeVo);
	}
	@Override
	public void saveWorkOvertime(WorkOvertimeVo workOvertimeVo) {
		WorkOvertimeEntity workOvertimeEntity = null;
		try {
			workOvertimeEntity = (WorkOvertimeEntity) CopyUtil.tryToEntity(workOvertimeVo, WorkOvertimeEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		workOvertimeEntity.setAddTime(new Date());
		workOvertimeEntity.setIsDeleted(0);
		baseDao.hqlSave(workOvertimeEntity);

	}
	@Override
	public List<WorkOvertimeTaskVo> createTaskVOListByTaskList(List<Task> workOvertimeTasks) {
		List<WorkOvertimeTaskVo> workOvertimeTaskVos = new ArrayList<WorkOvertimeTaskVo>();
		if(null == workOvertimeTasks){
			return workOvertimeTaskVos;
		}
		for (Task task : workOvertimeTasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			WorkOvertimeVo arg = (WorkOvertimeVo) runtimeService.getVariable(pInstance.getId(), "arg");
			String department = "";
			String requestUserId = "";
			if(Constants.DEP.equals(arg.getType())){
				String[] overTimeUserIds = arg.getRequestUserID().split(",");
				requestUserId = overTimeUserIds[0];
			}else{
				requestUserId = arg.getRequestUserID();
			}
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(requestUserId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			} 
			WorkOvertimeTaskVo workOvertimeTaskVo = new WorkOvertimeTaskVo();
			workOvertimeTaskVo.setDepartment(department);
			workOvertimeTaskVo.setProcessInstanceID(task.getProcessInstanceId());
			if(Constants.DEP.equals(arg.getType())){
				List<String> overTimeUserNames = new ArrayList<>();
				String[] overTimeUserIds = arg.getRequestUserID().split(",");
				for(String overTimeUserId: overTimeUserIds){
					StaffVO staffVo = staffService.getStaffByUserID(overTimeUserId);
					overTimeUserNames.add(staffVo.getLastName());
				}
				workOvertimeTaskVo.setRequestUserName(StringUtils.join(overTimeUserNames, ","));
			}else{
				workOvertimeTaskVo.setRequestUserName(arg.getRequestUserName());
			}
			workOvertimeTaskVo.setRequestDate(arg.getRequestDate());
			workOvertimeTaskVo.setTaskID(task.getId());
			workOvertimeTaskVo.setTitle(arg.getTitle());
			workOvertimeTaskVo.setTaskName(task.getName());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pInstance.getProcessDefinitionId()).singleResult();
			workOvertimeTaskVo.setBusinessKey(processDefinition.getKey());
			workOvertimeTaskVo.setBeginDate(arg.getBeginDate());
			workOvertimeTaskVo.setEndDate(arg.getEndDate());
			workOvertimeTaskVo.setWorkHours(arg.getWorkHours());
			workOvertimeTaskVo.setReason(arg.getReason());
			workOvertimeTaskVos.add(workOvertimeTaskVo);
		}
		return workOvertimeTaskVos;
	}
	@Override
	public void updateWorkOvertimeProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update WorkOvertimeEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);		
	}
	@Override
	public ListResult<WorkOvertimeTaskVo> findWorkOvertimeTasksByGroups(List<Group> groups, Integer page,
			Integer limit) {
		String arrayString = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.WORK_OVERTIME_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+") order by task.CREATE_TIME_";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<WorkOvertimeTaskVo> taskVOs = createTaskVOList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.WORK_OVERTIME_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<WorkOvertimeTaskVo>(taskVOs, count);
	}
	private List<WorkOvertimeTaskVo> createTaskVOList(List<Object> tasks) {
		List<WorkOvertimeTaskVo> taskVOs = new ArrayList<WorkOvertimeTaskVo>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			WorkOvertimeVo arg = (WorkOvertimeVo) runtimeService.getVariable(pInstance.getId(), "arg");
			String department = "";
			String requestUserId = "";
			if(Constants.DEP.equals(arg.getType())){
				String[] overTimeUserIds = arg.getRequestUserID().split(",");
				requestUserId = overTimeUserIds[0];
			}else{
				requestUserId = arg.getRequestUserID();
			}
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(requestUserId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			} 
			WorkOvertimeTaskVo taskVO = new WorkOvertimeTaskVo();
			taskVO.setProcessInstanceID((String) objs[1]);
			if(Constants.DEP.equals(arg.getType())){
				List<String> overTimeUserNames = new ArrayList<>();
				String[] overTimeUserIds = arg.getRequestUserID().split(",");
				for(String overTimeUserId: overTimeUserIds){
					StaffVO staffVo = staffService.getStaffByUserID(overTimeUserId);
					overTimeUserNames.add(staffVo.getLastName());
				}
				taskVO.setRequestUserName(StringUtils.join(overTimeUserNames, ","));
			}else{
				taskVO.setRequestUserName(arg.getRequestUserName());
			}
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTitle(arg.getTitle());
			taskVO.setDepartment(department);
			taskVO.setBeginDate(arg.getBeginDate());
			taskVO.setEndDate(arg.getEndDate());
			taskVO.setWorkHours(arg.getWorkHours());
			taskVO.setReason(arg.getReason());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	@Override
	public int findWorkOvertimeTasksCountByGroups(List<Group> groups) {
		String arrayString = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.WORK_OVERTIME_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return count;
	}
	@Override
	public ListResult<WorkOvertimeVo> findWorkOvertimeListByUserID(String id, Integer page, Integer limit) {
		List<WorkOvertimeEntity> workOvertimes = getWorkOvertimesByUserId(id,
				page, limit);

		List<WorkOvertimeVo> workOvertimeVos = new ArrayList<WorkOvertimeVo>();
		for (WorkOvertimeEntity workOvertime : workOvertimes) {
			WorkOvertimeVo workOvertimeVo = null;
			try {
				workOvertimeVo = (WorkOvertimeVo) CopyUtil.tryToVo(workOvertime, WorkOvertimeVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(workOvertime.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					WorkOvertimeVo arg = (WorkOvertimeVo) variable.getValue();
					workOvertimeVo.setRequestDate(arg.getRequestDate());
					workOvertimeVo.setTitle(arg.getTitle());
					workOvertimeVo.setUserName(arg.getUserName());
					workOvertimeVo.setBeginDate(arg.getBeginDate());
					workOvertimeVo.setEndDate(arg.getEndDate());
					workOvertimeVo.setWorkHours(arg.getWorkHours());
					workOvertimeVo.setReason(arg.getReason());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(workOvertime.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				workOvertimeVo.setStatus("处理中");
				workOvertimeVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = workOvertimeVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						workOvertimeVo.setStatus(t.getName());
				}
			}
			if(Constants.DEP.equals(workOvertimeVo.getType())){
				List<String> staffVos = new ArrayList<>();
				String[] overTimeUserIds = workOvertime.getRequestUserID().split(",");
				for(String overTimeUserId: overTimeUserIds){
					StaffVO staffVo = staffService.getStaffByUserID(overTimeUserId);
					staffVos.add(staffVo.getLastName());
				}
				workOvertimeVo.setOverTimeUsers(staffVos);
			}
			workOvertimeVos.add(workOvertimeVo);
		}
		int count = getWorkOvertimeCountByUserId(id);
		return new ListResult<WorkOvertimeVo>(workOvertimeVos, count);
	}
	private int getWorkOvertimeCountByUserId(String id) {
		String hql = "select count(id) from WorkOvertimeEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<WorkOvertimeEntity> getWorkOvertimesByUserId(String id, Integer page, Integer limit) {
		String hql="from WorkOvertimeEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<WorkOvertimeEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public ListResult<WorkOvertimeVo> findWorkOvertimeListByCondition(String[] conditions, Integer page,
			Integer limit) {
		//符合查询条件的加班数据
		List<Object> objList = baseDao.findPageList(getQuerySqlByConditions(conditions), page, limit);

		List<WorkOvertimeVo> WorkOvertimeVos = new ArrayList<>();
		for (Object object : objList) {
			Object[] objs = (Object[]) object;
			WorkOvertimeVo workOvertimeVo = new WorkOvertimeVo();
			workOvertimeVo.setRequestUserName(objs[0]+"");
			workOvertimeVo.setBeginDate(objs[1]+"");
			workOvertimeVo.setEndDate(objs[2]+"");
			workOvertimeVo.setWorkHours(objs[3]+"");
			workOvertimeVo.setReason(objs[4]+"");
			workOvertimeVo.setProcessInstanceID(objs[5]+"");
			String requestUserId = objs[6]+"";
			if(Constants.DEP.equals(objs[8]+"")){
				CompanyEntity company = companyDao.getCompanyByCompanyID(Integer.parseInt(String.valueOf(objs[9])));
				DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(String.valueOf(objs[10])));
				workOvertimeVo.setDepartment(company.getCompanyName()+"-"+department.getDepartmentName());
			}else{
				List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(requestUserId);
				if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
					GroupDetailVO group = groupDetails.get(0);
					String department = group.getCompanyName() + "-" + group.getDepartmentName();
					workOvertimeVo.setDepartment(department);
				} 
			}
			/*ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(objs[5]+"")
					.singleResult();
			if (pInstance != null) {
				workOvertimeVo.setStatus("处理中");
			} else {
				Integer value_ = Integer.parseInt((StringUtils.isBlank(objs[7]+"") || null==objs[7]) ? "0":objs[7]+"");
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						workOvertimeVo.setStatus(t.getName());
				}
			}*/
			workOvertimeVo.setStatus((String) objs[11]);
			workOvertimeVo.setThecurrenLink((String) objs[12]);
			workOvertimeVo.setCandidateUsers((String) objs[13]);
			WorkOvertimeVos.add(workOvertimeVo);
		}

		Object countObj = baseDao.getUniqueResult(getQueryCountSqlByConditions(conditions));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<WorkOvertimeVo>(WorkOvertimeVos, count);
	}
	private String getQueryCountSqlByConditions(String[] conditions) {
		StringBuffer hql = new StringBuffer(
				"select count(*) from ( \n");
		hql.append(getQuerySqlByConditions(conditions));
		hql.append(" ) workOvertimeVoCount");
		return hql.toString();
	}
	private String getQuerySqlByConditions(String[] conditions) {
		/*StringBuffer hql = new StringBuffer(
				"select distinct * from (select staff.staffName, beginDate, endDate, workHours, overtime.reason,"
						+ "processInstanceID, overtime.requestUserID, applyResult, type, overtime.companyId, overtime.departmentId from OA_WorkOvertime overtime, OA_Staff staff,ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail "
						+ "where overtime.isDeleted = 0 and locate(staff.UserID, overtime.requestUserID)>0  and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and staff.IsDeleted = 0 and staff.Status != 4 ");
		hql.append(getWhereByConditions(conditions));
		hql.append(" order by overtime.addTime desc) workOvertimeVo");*/
		
		StringBuffer sql =new StringBuffer(
				"SELECT DISTINCT\n" +
						"	workOvertimeVo.*, CASE\n" +
						"WHEN act_ru_task.ID_ IS NULL THEN\n" +
						"	'已完结'\n" +
						"ELSE\n" +
						"	'进行中'\n" +
						"END,\n" +
						" act_ru_task.NAME_,\n" +
						" CASE\n" +
						"WHEN act_ru_task.ASSIGNEE_ IS NULL THEN\n" +
						"	act_ru_task.NAME_\n" +
						"ELSE\n" +
						"	oa_staff.StaffName\n" +
						"END\n" +
						"FROM\n" +
						"	(\n" +
						"		SELECT\n" +
						"			staff.staffName,\n" +
						"			beginDate,\n" +
						"			endDate,\n" +
						"			workHours,\n" +
						"			overtime.reason,\n" +
						"			processInstanceID,\n" +
						"			overtime.requestUserID,\n" +
						"			applyResult,\n" +
						"			type,overtime.companyId, overtime.departmentId\n" +
						"		FROM\n" +
						"			OA_WorkOvertime overtime,\n" +
						"			OA_Staff staff,\n" +
						"			ACT_ID_MEMBERSHIP membership,\n" +
						"			ACT_ID_GROUP idGroup,\n" +
						"			OA_GroupDetail groupDetail\n" +
						"		WHERE\n" +
						"			overtime.isDeleted = 0\n" +
						"		AND locate(\n" +
						"			staff.UserID,\n" +
						"			overtime.requestUserID\n" +
						"		) > 0\n" +
						"		AND staff.UserID = membership.USER_ID_\n" +
						"		AND membership.GROUP_ID_ = idGroup.ID_\n" +
						"		AND idGroup.ID_ = groupDetail.GroupID\n" +
						"		AND staff.IsDeleted = 0\n" +
						"		AND staff. STATUS != 4\n");
		sql.append(getWhereByConditions(conditions));
		sql.append("	) workOvertimeVo\n" +
				"LEFT JOIN act_ru_task ON workOvertimeVo.processInstanceID = act_ru_task.PROC_INST_ID_\n" +
				"LEFT JOIN oa_staff ON act_ru_task.ASSIGNEE_ = oa_staff.UserID\n" +
				"WHERE workOvertimeVo.staffName IS NOT NULL\n");				
		sql.append(getConditions(conditions));				
		sql.append(" ORDER BY\n" +
				"	workOvertimeVo.beginDate DESC");
		return sql.toString();
	}
	private String getWhereByConditions(String[] conditions) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(conditions[3])) {
			whereSql.append(" and beginDate >= '" + conditions[3] + "'");
		}
		if (!StringUtils.isBlank(conditions[4])) {
			whereSql.append(" and beginDate <= '" + conditions[4] + "'");
		}
		if (!StringUtils.isBlank(conditions[0])) {
			whereSql.append(" and staff.UserID='" + conditions[0] + "'");
		}
		if (StringUtils.isNotBlank(conditions[1])) {
			whereSql.append(" and groupDetail.CompanyID = " + conditions[1]);
			if (StringUtils.isNotBlank(conditions[2])) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyIDParentID(Integer.parseInt(conditions[1]), Integer.parseInt(conditions[2]));
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(Integer.parseInt(conditions[2]));
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}

		return whereSql.toString();
	}
	@SuppressWarnings("unchecked")
	@Override
	public String[] getNightWorkTimesAndhours(String userId, String beginDate, String endDate) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		String companyIDString = groups.get(0).getType().split("_")[0];
		String departmentId = groups.get(0).getType().split("_")[1];
		String startWorkOvertime = attendanceService.getWorkOverBeginTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, beginDate);
		//没有加班
		if(StringUtils.isBlank(startWorkOvertime)){
			return new String[]{"0", "0"};
		}
		Date startWorkOvertimeDate = DateUtil.getFullDate("2000-01-01 "+startWorkOvertime+":00");
		String hql = "from WorkOvertimeEntity wt where wt.isDeleted=0 and wt.applyResult=1 and locate('"+userId+"', requestUserID)>0"+
					 " and DATE(beginDate)>='"+beginDate+"' and DATE(endDate)<='"+endDate+"'";
		List<WorkOvertimeEntity> workOvertimeList = (List<WorkOvertimeEntity>) baseDao.hqlfind(hql);
		//加班日期和对应的小时数
		Map<String, Double> workTimeAndHoursMap = new HashMap<>();
		for(WorkOvertimeEntity workOvertime: workOvertimeList){
			String beginTime = workOvertime.getBeginDate();
			String workOvertimeDate = DateUtil.formateDate(DateUtil.getFullDate(beginTime));
			hql = "from AttendanceDetailEntity detail where detail.attendanceDate = '"+workOvertimeDate+"' and detail.userID='"+userId+"'";
			List<AttendanceDetailEntity> attendanceDetailList = (List<AttendanceDetailEntity>) baseDao.hqlfind(hql);
			if(null != attendanceDetailList && attendanceDetailList.size()>0){
				AttendanceDetailEntity attendanceDetail = attendanceDetailList.get(0);
				String attendanceTime = attendanceDetail.getAttendanceTime();
				String[] blockTimes = attendanceTime.split(" ");
				//取最后一次打卡时间
				String lastBlockTime = blockTimes[blockTimes.length-1];
				Date lastBlockDate = DateUtil.getFullDate("2000-01-01 "+lastBlockTime+":00");
				Date limitDate = DateUtil.getFullDate("2000-01-01 04:00:00");
				//只打了一次卡
				if(blockTimes.length==1){
					//打卡时间为加班开始时间后
					if(DateUtil.after(lastBlockDate, startWorkOvertimeDate)){
						//do nothing
					}
					//打卡时间为次日凌晨4点（加班时间限制为凌晨4点）
					else if(DateUtil.before(lastBlockDate, limitDate)){
						Calendar cal = Calendar.getInstance();
						cal.setTime(lastBlockDate);
						cal.add(Calendar.DATE, 1);
						lastBlockDate = cal.getTime();
					}
					else{
						continue;
					}
				}
				Date firstBlockDate = DateUtil.getFullDate("2000-01-01 "+blockTimes[0]+":00");
				//下班打卡可能为第二天（可能是当天早上的打卡，下班未打卡）//避免bug，如08:00~08:10
				if(DateUtil.before(lastBlockDate, firstBlockDate) && DateUtil.before(lastBlockDate, limitDate)){
					Calendar cal = Calendar.getInstance();
					cal.setTime(lastBlockDate);
					cal.add(Calendar.DATE, 1);
					lastBlockDate = cal.getTime();
				}
				double time = lastBlockDate.getTime()
						- startWorkOvertimeDate.getTime();
				//加班时长按半小时起算
				if(time>30*60*1000){
					double workHours = Math.floor(time/(30*60*1000))/2;
					workTimeAndHoursMap.put(workOvertimeDate, workHours);
				}
			}
		}
		Iterator<Entry<String, Double>> ite = workTimeAndHoursMap.entrySet().iterator();
		int index = 0;
		double totalWorkTime = 0;
		while(ite.hasNext()){
			Entry<String, Double> entry = ite.next();
			totalWorkTime += entry.getValue();
			index++;
		}
		return new String[]{index+"", totalWorkTime+""};
	}
	private String  getConditions(String[] conditions) {
		StringBuffer sql = new StringBuffer();
		if (!StringUtils.isBlank(conditions[5])) {
			sql.append(" and (CASE WHEN act_ru_task.ID_ IS NULL THEN\n" +
					"	'已完结'\n" +
					"ELSE\n" +
					"	'进行中'\n" +
					"END) ='"+conditions[5]+"'");
		}
		if (!StringUtils.isBlank(conditions[6])) {
			sql.append(" and act_ru_task.NAME_ ='"+conditions[6]+"'\n");
		}
		if(!StringUtils.isBlank(conditions[7])){
			sql.append(" and (CASE\n" +
					"WHEN act_ru_task.ASSIGNEE_ IS NULL THEN\n" +
					"	act_ru_task.NAME_\n" +
					"ELSE\n" +
					"	oa_staff.StaffName\n" +
					"END) like '%"+conditions[7]+"%'");
		}
		return sql.toString();
	}
}
