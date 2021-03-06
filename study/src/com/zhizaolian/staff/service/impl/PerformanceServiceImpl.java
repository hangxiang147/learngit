﻿package com.zhizaolian.staff.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.PerformanceCheckItemEntity;
import com.zhizaolian.staff.entity.PerformanceEntity;
import com.zhizaolian.staff.entity.PerformancePositionTemplateEntity;
import com.zhizaolian.staff.entity.PerformanceProjectEntity;
import com.zhizaolian.staff.entity.PerformanceStaffCheckItemEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.ToBeDoneTaskEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PerformanceService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PerformanceVo;
import com.zhizaolian.staff.vo.PositionVO;

@Service(value = "performanceService")
public class PerformanceServiceImpl implements PerformanceService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private PositionService positionService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private StaffSalaryService staffSalaryService;

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformancePositionTemplateEntity> findTemplatesByDepId(String departmentId) {
		List<PositionVO> positions = positionService.findPositionsByDepartmentID(Integer.parseInt(departmentId));
		List<Integer> positionIds = new ArrayList<>();
		for (PositionVO position : positions) {
			positionIds.add(position.getPositionID());
		}
		String hql = "from PerformancePositionTemplateEntity where isDeleted=0 and ifNull(status,0)!=2 "
				+ "and positionId in(" + StringUtils.join(positionIds, ",") + ")";
		return (List<PerformancePositionTemplateEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public List<PerformanceCheckItemEntity> saveCheckProject(PerformanceProjectEntity project, List<PerformanceCheckItemEntity> checkItems) {
		project.setAddTime(new Date());
		int projectId = baseDao.hqlSave(project);
		List<PerformanceCheckItemEntity> _checkItems = new ArrayList<>();
		for (PerformanceCheckItemEntity checkItem : checkItems) {
			if(null == checkItem){
				continue;
			}
			checkItem.setAddTime(new Date());
			checkItem.setProjectId(projectId);
			baseDao.hqlSave(checkItem);
			_checkItems.add(checkItem);
		}
		return _checkItems;
	}

	@Override
	public void deleteProject(String projectId) {
		String hql = "update PerformanceProjectEntity set isDeleted=1 where id=" + projectId;
		baseDao.excuteHql(hql);
	}

	@Override
	public PerformanceProjectEntity getProjectInfo(String projectId) {
		String hql = "from PerformanceProjectEntity where id=" + projectId;
		return (PerformanceProjectEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceCheckItemEntity> getProjectCheckInfos(String projectId) {
		String hql = "from PerformanceCheckItemEntity where isDeleted=0 and projectId=" + projectId;
		return (List<PerformanceCheckItemEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public List<PerformanceCheckItemEntity> updateCheckProject(PerformanceProjectEntity project, List<PerformanceCheckItemEntity> checkItems) {
		PerformanceProjectEntity oldProject = getProjectInfo(String.valueOf(project.getId()));
		oldProject.setProject(project.getProject());
		baseDao.hqlUpdate(oldProject);
		List<PerformanceCheckItemEntity> _checkItems = new ArrayList<>();
		for (PerformanceCheckItemEntity checkItem : checkItems) {
			if(null == checkItem){
				continue;
			}
			checkItem.setProjectId(project.getId());
			if (null != checkItem.getId()) {
				baseDao.hqlUpdate(checkItem);
			} else {
				checkItem.setAddTime(new Date());
				baseDao.hqlSave(checkItem);
			}
			_checkItems.add(checkItem);
		}
		return _checkItems;
	}

	@Override
	public int savePositionTemplate(String positionId, String projectIds, String templateName) {
		// 删除该岗位旧的绩效模板
		String hql = "update PerformancePositionTemplateEntity set isDeleted=1 where positionId=" + positionId;
		baseDao.excuteHql(hql);
		PerformancePositionTemplateEntity template = new PerformancePositionTemplateEntity();
		template.setAddTime(new Date());
		template.setPositionId(positionId);
		template.setTemplateName(templateName);
		int templateId = baseDao.hqlSave(template);
		hql = "update PerformanceProjectEntity set templateId=" + templateId + " where id in(" + projectIds + ")";
		baseDao.excuteHql(hql);
		return templateId;
	}

	@Override
	public List<PerformanceProjectEntity> getProjectsByIds(String projectIdStr) {
		List<PerformanceProjectEntity> projects = new ArrayList<>();
		String[] projectIds = projectIdStr.split(",");
		for (String projectId : projectIds) {
			PerformanceProjectEntity project = getProjectInfo(projectId);
			List<PerformanceCheckItemEntity> checkItems = getProjectCheckInfos(projectId);
			project.setCheckItems(checkItems);
			projects.add(project);
		}
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkPositionTemplateExist(String positionId) {
		String hql = "from PerformancePositionTemplateEntity where status is not null and isDeleted=0 and positionId="
				+ positionId;
		List<PerformancePositionTemplateEntity> positionTemplates = (List<PerformancePositionTemplateEntity>) baseDao
				.hqlfind(hql);
		if (positionTemplates.size() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> findPositionTemplateDetailById(String templateId)
			throws Exception {
		String hql = "from PerformanceProjectEntity where isDeleted=0 and templateId=" + templateId;
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		List<PerformanceProjectEntity> cloneProjects = new ArrayList<>();
		for (PerformanceProjectEntity project : projects) {
			PerformanceProjectEntity cloneProject = project.clone();
			cloneProject.setId(null);
			cloneProject.setTemplateId(null);
			int projectId = baseDao.hqlSave(cloneProject);
			List<PerformanceCheckItemEntity> checkItems = getProjectCheckInfos(String.valueOf(project.getId()));
			List<PerformanceCheckItemEntity> cloneCheckItems = new ArrayList<>();
			for (PerformanceCheckItemEntity checkItem : checkItems) {
				PerformanceCheckItemEntity cloneCheckItem = checkItem.clone();
				cloneCheckItem.setProjectId(projectId);
				cloneCheckItem.setId(null);
				baseDao.hqlSave(cloneCheckItem);
				cloneCheckItems.add(cloneCheckItem);
			}
			cloneProject.setCheckItems(cloneCheckItems);
			cloneProject.setCheckItems(checkItems);
			cloneProjects.add(cloneProject);
		}
		return cloneProjects;
	}

	@Override
	public void startPerformanceApply(String userId, String[] templateId) {
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		// PM评审人员
		List<String> pmAuditors = permissionService.findUsersByPermissionCode(Constants.PM_AUDITOR);
		if (pmAuditors.size() > 0) {
			vars.put("auditors", pmAuditors);
		} else {
			throw new RuntimeException("未找到PM评审人");
		}
		vars.put("applyer", userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("performance", vars);
		// 查询第一个任务
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		// 完成任务
		taskService.complete(task.getId());
		// 记录数据
		PerformanceEntity performance = new PerformanceEntity();
		performance.setProcessInstanceID(processInstance.getId());
		performance.setAddTime(new Date());
		performance.setTemplateIds(StringUtils.join(templateId, ","));
		performance.setUserId(userId);
		baseDao.hqlSave(performance);
		// 更新模板状态 0表示已提交审核
		updateTemplateStatus(0, templateId);
	}

	private void updateTemplateStatus(int status, String[] templateId) {
		String hql = "update PerformancePositionTemplateEntity set status=" + status + " where" + " id in("
				+ StringUtils.join(templateId, ",") + ")";
		baseDao.excuteHql(hql);
	}

	@Override
	public void deleteCheckItem(String checkItemId) {
		String hql = "update PerformanceCheckItemEntity set isDeleted=1 where id=" + checkItemId;
		baseDao.excuteHql(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<PerformanceEntity> findPerformanceApplys(Integer limit, Integer page, String userId) {
		String hql = "from PerformanceEntity where templateIds is not null and isDeleted=0 and userId='" + userId + "' order by addTime desc";
		List<PerformanceEntity> performanceTasks = (List<PerformanceEntity>) baseDao.hqlPagedFind(hql, page, limit);
		for (PerformanceEntity performanceTask : performanceTasks) {
			String processInstanceID = performanceTask.getProcessInstanceID();
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceID)
					.singleResult();
			if (pInstance != null) {
				performanceTask.setStatus("进行中");
				performanceTask.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = performanceTask.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						performanceTask.setStatus(t.getName());
				}
			}
			List<PerformancePositionTemplateEntity> positionTemplates = getPositionTemplates(
					performanceTask.getTemplateIds());
			List<String> positionNames = new ArrayList<>();
			for (PerformancePositionTemplateEntity positionTemplate : positionTemplates) {
				positionNames.add(positionTemplate.getTemplateName());
			}
			performanceTask.setPositionNames(StringUtils.join(positionNames, ","));
		}
		String hqlCount = "select count(id) from PerformanceEntity where templateIds is not null and isDeleted=0 and userId='" + userId + "'";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount) + "");
		return new ListResult<>(performanceTasks, count);
	}

	@SuppressWarnings("unchecked")
	private List<PerformancePositionTemplateEntity> getPositionTemplates(String templateIdStr) {
		String hql = "from PerformancePositionTemplateEntity where id in(" + templateIdStr + ")";
		return (List<PerformancePositionTemplateEntity>) baseDao.hqlfind(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformancePositionTemplateEntity> findPositionPerformances(String templateIds) {
		String hql = "from PerformancePositionTemplateEntity where id in(" + templateIds + ")";
		List<PerformancePositionTemplateEntity> positionPerformances = (List<PerformancePositionTemplateEntity>) baseDao
				.hqlfind(hql);
		for (PerformancePositionTemplateEntity positionPerformance : positionPerformances) {
			hql = "from PerformanceProjectEntity where isDeleted=0 and templateId=" + positionPerformance.getId();
			List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
			for (PerformanceProjectEntity project : projects) {
				List<PerformanceCheckItemEntity> checkItems = getProjectCheckInfos(String.valueOf(project.getId()));
				project.setCheckItems(checkItems);
			}
			positionPerformance.setProjects(projects);
		}
		return positionPerformances;
	}

	@Override
	public List<PerformanceEntity> findPerformanceTaskVos(List<Task> performanceTasks) {
		List<PerformanceEntity> performanceTaskVos = new ArrayList<>();
		if(null != performanceTasks){
			for (Task performanceTask : performanceTasks) {
				PerformanceEntity performanceEntity = getPerformanceByProcessInstanceId(
						performanceTask.getProcessInstanceId());
				performanceEntity.setTaskName(performanceTask.getName());
				performanceEntity.setTaskId(performanceTask.getId());
				performanceEntity.setUserName(staffDao.getStaffByUserID(performanceEntity.getUserId()).getStaffName());
				performanceTaskVos.add(performanceEntity);
				List<PerformancePositionTemplateEntity> positionTemplates = getPositionTemplates(
						performanceEntity.getTemplateIds());
				List<String> positionNames = new ArrayList<>();
				for (PerformancePositionTemplateEntity positionTemplate : positionTemplates) {
					positionNames.add(positionTemplate.getTemplateName());
				}
				performanceEntity.setPositionNames(StringUtils.join(positionNames, ","));
			}
		}
		return performanceTaskVos;
	}

	private PerformanceEntity getPerformanceByProcessInstanceId(String processInstanceId) {
		String hql = "from PerformanceEntity where processInstanceID=" + processInstanceId;
		return (PerformanceEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void pmAudit(String taskId, String result, String comment, String userId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		identityService.setAuthenticatedUserId(userId);
		if (!StringUtils.isBlank(comment)) {
			// 添加评论
			taskService.addComment(taskId, pInstance.getId(), comment);
		}
		Map<String, Object> vars = new HashMap<>();
		vars.put("auditResult", Integer.parseInt(result));
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId, vars);
		PerformanceEntity performance = getPerformanceByProcessInstanceId(pInstance.getId());
		if (null != performance) {
			updateTemplateStatus(Integer.parseInt(result), performance.getTemplateIds().split(","));
			performance.setApplyResult(Integer.parseInt(result));
			baseDao.hqlUpdate(performance);
		}
		// 自动生成员工的个人考核项，次月开始实施
		if (TaskResultEnum.AGREE.getValue() == Integer.parseInt(result)) {
			List<PerformancePositionTemplateEntity> positionTemplates = getPositionTemplates(
					performance.getTemplateIds());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			for (PerformancePositionTemplateEntity positionTemplate : positionTemplates) {
				List<PerformanceCheckItemEntity> checkItems = getProjectCheckItems(positionTemplate.getId());
				String positionId = positionTemplate.getPositionId();
				// 获取岗位下面的所有在职员工
				List<Object> staffs = staffService.getStaffsByPositionId(positionId);
				for (Object staffUserId : staffs) {
					for (PerformanceCheckItemEntity checkItem : checkItems) {
						PerformanceStaffCheckItemEntity staffPerformance = new PerformanceStaffCheckItemEntity();
						staffPerformance.setAddMoney(checkItem.getAddMoney());
						staffPerformance.setAddMoneyType(checkItem.getAddMoneyType());
						staffPerformance.setCheckItem(checkItem.getCheckItem());
						staffPerformance.setCoefficient(checkItem.getCoefficient());
						staffPerformance.setMonth(month);
						staffPerformance.setPerAddMoneyValue(checkItem.getPerAddMoneyValue());
						staffPerformance.setPerReduceMoneyValue(checkItem.getPerReduceMoneyValue());
						staffPerformance.setProjectId(checkItem.getProjectId());
						staffPerformance.setReduceMoney(checkItem.getReduceMoney());
						staffPerformance.setReduceMoneyType(checkItem.getReduceMoneyType());
						staffPerformance.setUserId((String) staffUserId);
						staffPerformance.setSupervisor(performance.getUserId());
						staffPerformance.setPositionId(positionId);
						staffPerformance.setYear(year);
						staffPerformance.setAddTime(new Date());
						staffPerformance.setStatus(TaskResultEnum.AGREE.getValue());
						baseDao.hqlSave(staffPerformance);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<PerformanceCheckItemEntity> getProjectCheckItems(Integer templateId) {
		List<PerformanceCheckItemEntity> checkItems = new ArrayList<>();
		String hql = "from PerformanceProjectEntity where isDeleted=0 and templateId=" + templateId;
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		for (PerformanceProjectEntity project : projects) {
			hql = "from PerformanceCheckItemEntity where isDeleted=0 and projectId=" + project.getId();
			checkItems.addAll((List<PerformanceCheckItemEntity>) baseDao.hqlfind(hql));
		}
		return checkItems;
	}

	@Override
	public ListResult<Object> findStaffPerformances(Integer limit, Integer page, String[] conditions) {
		String year = conditions[0];
		String month = conditions[1];
		String userName = conditions[2];
		String companyId = conditions[3];
		String departmentId = conditions[4];
		String sql = "SELECT\n" +
				"	staff.UserID,\n" +
				"	staffName,\n" +
				"	companyName,\n" +
				"	GROUP_CONCAT(DISTINCT\n" +
				"	CONCAT(\n" +
				"		DepartmentName,\n" +
				"		'-',\n" +
				"		PositionName\n" +
				"	))\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_PerformanceStaffCheckItem\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"		AND MONTH = "+month+"\n" +
				"		AND YEAR = "+year+"\n" +
				"	) a, \n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep,\n" +
				"	OA_Position p\n" +
				"WHERE\n" +
				"	a.userId = staff.UserID\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND company.CompanyID = detail.CompanyID\n" +
				"AND detail.DepartmentID = dep.DepartmentID\n" +
				"AND detail.PositionID = p.PositionID\n" +
				"AND a.positionId = p.PositionID\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n";
		if (StringUtils.isNotBlank(userName)) {
			sql += "AND staffName like'%" + EscapeUtil.decodeSpecialChars(userName) + "%'\n";
		}
		if (StringUtils.isNotBlank(companyId)) {
			sql += "AND company.CompanyID=" + companyId + "\n";
		}
		if (StringUtils.isNotBlank(departmentId)) {
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					Integer.parseInt(companyId), Integer.parseInt(departmentId));
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(Integer.parseInt(departmentId));
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sql += "AND detail.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")\n";
		}
		sql += "GROUP BY a.userId";
		List<Object> staffPerformances = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(DISTINCT staff.UserID)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_PerformanceStaffCheckItem\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"		AND MONTH = "+month+"\n" +
				"		AND YEAR = "+year+"\n" +
				"	) a, \n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep,\n" +
				"	OA_Position p\n" +
				"WHERE\n" +
				"	a.userId = staff.UserID\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND company.CompanyID = detail.CompanyID\n" +
				"AND detail.DepartmentID = dep.DepartmentID\n" +
				"AND detail.PositionID = p.PositionID\n" +
				"AND a.positionId = p.PositionID\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n";
		if (StringUtils.isNotBlank(userName)) {
			sqlCount += "AND staffName like'%" + EscapeUtil.decodeSpecialChars(userName) + "%'\n";
		}
		if (StringUtils.isNotBlank(companyId)) {
			sqlCount += "AND company.CompanyID=" + companyId + "\n";
		}
		if (StringUtils.isNotBlank(departmentId)) {
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					Integer.parseInt(companyId), Integer.parseInt(departmentId));
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(Integer.parseInt(departmentId));
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sqlCount += "AND detail.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sqlCount)));
		return new ListResult<>(staffPerformances, count);
	}

	@Override
	public void generateTargetValueTasks(int year, int month) throws Exception {
		String sql = "SELECT DISTINCT\n" +
				"	userId, supervisor\n" +
				"FROM\n" +
				"	OA_PerformanceStaffCheckItem\n" +
				"WHERE\n" +
				"	targetValue IS NULL\n" +
				"AND ifNull(status, 0)=1\n" +
				"AND isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month;
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] userIdAndSupervisor = (Object[])obj;
			Map<String, String> resultMap = new HashMap<>();
			resultMap.put("userId", (String)userIdAndSupervisor[0]);
			resultMap.put("year", String.valueOf(year));
			resultMap.put("month", String.valueOf(month));
			ToBeDoneTaskEntity targetValueTask = new ToBeDoneTaskEntity();
			targetValueTask.setAddTime(new Date());
			targetValueTask.setData(ObjectByteArrTransformer.toByteArray(resultMap));
			targetValueTask.setStatus(0);
			targetValueTask.setType(BusinessTypeEnum.PERFORMANCE_TARGET.getValue());
			targetValueTask.setUserId((String)userIdAndSupervisor[1]);
			baseDao.hqlSave(targetValueTask);
		}
	}

	@SuppressWarnings("unchecked")
	private List<PerformanceStaffCheckItemEntity> getStaffProjectCheckInfos(Integer projectId,
			String userId, int year, int month) {
		String hql = "from PerformanceStaffCheckItemEntity where isDeleted=0 and ifNull(status,0)=1 and projectId=" + projectId+"\n"+
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND userId = '"+(String)userId+"'";
		return (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceVo> findPerformanceTasks(String userId,
			BusinessTypeEnum type) throws Exception {
		List<PerformanceVo> performanceTargetValueTasks = new ArrayList<>();
		String sql = "";
		if(null == userId){
			sql = "SELECT\n" +
					"	id, task.`data`\n" +
					"FROM\n" +
					"	OA_ToBeDoneTask task\n" +
					"WHERE\n" +
					"task.isDeleted = 0\n" +
					"AND task.status = 0\n" +
					"AND task.type = "+type.getValue() +
					" order by addTime desc";
		}else{
			sql = "SELECT\n" +
					"	id, task.`data`\n" +
					"FROM\n" +
					"	OA_ToBeDoneTask task\n" +
					"WHERE\n" +
					"task.userId = '"+userId+"'\n" +
					"AND task.isDeleted = 0\n" +
					"AND task.status = 0\n" +
					"AND task.type = "+type.getValue() +
					" order by addTime desc";
		}
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] tasks = (Object[])obj;
			Map<String, String> resultMap = (Map<String, String>) ObjectByteArrTransformer.toObject((byte[])tasks[1]);
			String staffUserId = resultMap.get("userId");
			String year = resultMap.get("year");
			String month = resultMap.get("month");
			PerformanceVo performanceVo = new PerformanceVo();
			performanceVo.setTaskId((Integer)tasks[0]);
			performanceVo.setStaffName(staffService.getStaffByUserId(staffUserId).getStaffName());
			performanceVo.setYear(year);
			performanceVo.setMonth(month);
			performanceTargetValueTasks.add(performanceVo);
		}
		return performanceTargetValueTasks;
	}

	@Override
	public int getPerformanceTaskCount(String userId, BusinessTypeEnum type) {
		String sql = "";
		if(null == userId){
			sql = "SELECT\n" +
					"	count(id)\n" +
					"FROM\n" +
					"	OA_ToBeDoneTask task\n" +
					"WHERE\n" +
					"task.isDeleted = 0\n" +
					"AND task.status = 0\n" +
					"AND task.type = "+type.getValue();
		}else{
			sql = "SELECT\n" +
					"	count(id)\n" +
					"FROM\n" +
					"	OA_ToBeDoneTask task\n" +
					"WHERE\n" +
					"task.userId = '"+userId+"'\n" +
					"AND task.isDeleted = 0\n" +
					"AND task.status = 0\n" +
					"AND task.type = "+type.getValue();
		}
		return Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sql)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> getTargetProjectsbyTaskId(String taskId) throws Exception {
		String hql = "from ToBeDoneTaskEntity where id="+taskId;
		ToBeDoneTaskEntity task = (ToBeDoneTaskEntity) baseDao.hqlfindUniqueResult(hql);
		Map<String, String> resultMap = (Map<String, String>) ObjectByteArrTransformer.toObject(task.getData());
		String userId = resultMap.get("userId");
		String year = resultMap.get("year");
		String month = resultMap.get("month");
		hql = "SELECT DISTINCT\n" +
				"	project\n" +
				"FROM\n" +
				"	PerformanceStaffCheckItemEntity checkItem,\n" +
				"	PerformanceProjectEntity project\n" +
				"WHERE\n" +
				"	targetValue IS NULL\n" +
				"AND checkItem.projectId = project.id\n" +
				"AND checkItem.isDeleted = 0 and ifNull(checkItem.status,0)=1\n" +
				"AND project.isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND userId = '"+userId+"'";
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		for (PerformanceProjectEntity project : projects) {
			List<PerformanceStaffCheckItemEntity> checkItems = getStaffProjectCheckInfos(
					project.getId(), userId, Integer.parseInt(year), Integer.parseInt(month));
			project.setStaffCheckItems(checkItems);
		}
		return projects;
	}

	@Override
	public void saveStaffCheckItemTargetValue(String[] ids, String[] targetValues, String taskId) {
		if(null != ids){
			for(int i=0; i<ids.length; i++){
				String hql = "update PerformanceStaffCheckItemEntity set targetValue="+targetValues[i]
						+" where id="+ids[i];
				baseDao.excuteHql(hql);
			}
		}
		//完成任务，更新状态
		String hql = "update ToBeDoneTaskEntity set status=1 where id="+taskId;
		baseDao.excuteHql(hql);
	}
	@Override
	public void generateActualValueTasks(int year, int month) throws Exception {
		String sql = "SELECT DISTINCT\n" +
				"	staff.userId, supervisor\n" +
				"FROM\n" +
				"	OA_PerformanceStaffCheckItem checkItem, oa_staff staff\n" +
				"WHERE\n" +
				"	actualValue IS NULL and staff.userId=checkItem.userId and staff.isDeleted=0 \n" +
				"AND ifNull(checkItem.status, 0)=1\n" +
				"AND checkItem.isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month;
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] userIdAndSupervisor = (Object[])obj;
			Map<String, String> resultMap = new HashMap<>();
			resultMap.put("userId", (String)userIdAndSupervisor[0]);
			resultMap.put("year", String.valueOf(year));
			resultMap.put("month", String.valueOf(month));
			ToBeDoneTaskEntity actualValueTask = new ToBeDoneTaskEntity();
			actualValueTask.setAddTime(new Date());
			actualValueTask.setData(ObjectByteArrTransformer.toByteArray(resultMap));
			actualValueTask.setStatus(0);
			actualValueTask.setType(BusinessTypeEnum.PERFORMANCE_ACTUAL.getValue());
			baseDao.hqlSave(actualValueTask);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generateStaffCheckItemPerMonth() throws Exception {
		Integer[] yearAndMonth = getLastMonth();
		int year = yearAndMonth[0];
		int month = yearAndMonth[1];
		String hql = "select checkItem from PerformanceStaffCheckItemEntity checkItem, "
				+ "StaffEntity staff where checkItem.userId=staff.userID and checkItem.isDeleted=0 and ifNull(checkItem.status,0)=1 and staff.isDeleted=0 "
				+ "and staff.status!=4 and year="+year+" and month="+month;
		List<PerformanceStaffCheckItemEntity> staffCheckItems = (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
		for(PerformanceStaffCheckItemEntity staffCheckItem: staffCheckItems){
			PerformanceStaffCheckItemEntity cloneStaffCheckItem = staffCheckItem.clone();
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
			cloneStaffCheckItem.setYear(currentYear);
			cloneStaffCheckItem.setMonth(currentMonth);
			cloneStaffCheckItem.setAddTime(new Date());
			cloneStaffCheckItem.setUpdateTime(null);
			cloneStaffCheckItem.setActualValue(null);
			cloneStaffCheckItem.setTargetValue(null);
			cloneStaffCheckItem.setId(null);
			baseDao.hqlSave(cloneStaffCheckItem);
		}
	}
	private Integer[] getLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date time = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(sdf.format(time));
		sdf = new SimpleDateFormat("MM");
		int month = Integer.parseInt(sdf.format(time));
		return new Integer[]{year, month};
	}

	@Override
	public void saveStaffCheckItemActualValue(String[] ids, String[] actualValues, String taskId) {
		if(null != ids){
			for(int i=0; i<ids.length; i++){
				String hql = "update PerformanceStaffCheckItemEntity set actualValue="+actualValues[i]
						+" where id="+ids[i];
				baseDao.excuteHql(hql);
			}
			//计算工资
			staffSalaryService.calStaffSalary(ids);
		}
		//完成任务，更新状态
		String hql = "update ToBeDoneTaskEntity set status=1 where id="+taskId;
		baseDao.excuteHql(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> getProjectsByUserId(StaffEntity staff, String year,
			String month) {
		//员工的绩效工资
		double performance = staff.getPerformance()==null ? 0:staff.getPerformance();
		String hql = "SELECT DISTINCT\n" +
				"	project\n" +
				"FROM\n" +
				"	PerformanceStaffCheckItemEntity checkItem,\n" +
				"	PerformanceProjectEntity project\n" +
				"WHERE\n" +
				"checkItem.projectId = project.id\n" +
				"AND checkItem.isDeleted = 0 and ifNull(checkItem.status,0)=1\n" +
				"AND project.isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND userId = '"+staff.getUserID()+"'";
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		//所有的绩效金额
		double totalPerformanceMoney = 0;
		for (PerformanceProjectEntity project : projects) {
			List<PerformanceStaffCheckItemEntity> checkItems = getStaffProjectCheckInfos(
					project.getId(), staff.getUserID(), Integer.parseInt(year), Integer.parseInt(month));
			for(PerformanceStaffCheckItemEntity checkItem: checkItems){
				//绩效系数
				double coefficient = checkItem.getCoefficient();
				checkItem.setRateMoney(coefficient*performance);
				Double targetValue = checkItem.getTargetValue();
				Double actualValue = checkItem.getActualValue();
				double performanceMoney = 0;
				//计算绩效金额
				if(null!=targetValue && null!=actualValue){
					//实际完成值大于等于目标值
					if(actualValue>=targetValue){
						//奖励
						if("+".equals(checkItem.getAddMoneyType()) && null!=checkItem.getAddMoney()){
							performanceMoney = (actualValue-targetValue)/checkItem.getPerAddMoneyValue()*checkItem.getAddMoney();
							//少发
						}else if("-".equals(checkItem.getReduceMoneyType()) && null!=checkItem.getReduceMoney()){
							performanceMoney = -(actualValue-targetValue)/checkItem.getPerReduceMoneyValue()*checkItem.getReduceMoney();
						}
						//实际完成值小于目标值	
					}else{
						//少发
						if("-".equals(checkItem.getReduceMoneyType()) && null!=checkItem.getReduceMoney()){
							performanceMoney = (actualValue-targetValue)/checkItem.getPerReduceMoneyValue()*checkItem.getReduceMoney();
							//奖励
						}else if("+".equals(checkItem.getAddMoneyType()) && null!=checkItem.getAddMoney()){
							performanceMoney = -(actualValue-targetValue)/checkItem.getPerAddMoneyValue()*checkItem.getAddMoney();
						}
					}
					BigDecimal b = new BigDecimal(performanceMoney);
					performanceMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					checkItem.setPerformanceMoney(performanceMoney);
					checkItem.setPerformanceSalary(coefficient*performance+performanceMoney);
				}
				totalPerformanceMoney += checkItem.getPerformanceSalary();
			}
			project.setStaffCheckItems(checkItems);
		}
		staff.setTotalPerformanceMoney(totalPerformanceMoney);
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> getProjectsByUserId(String userId, String year, String month) throws Exception {
		String hql = "SELECT DISTINCT\n" +
				"	project\n" +
				"FROM\n" +
				"	PerformanceStaffCheckItemEntity checkItem,\n" +
				"	PerformanceProjectEntity project\n" +
				"WHERE\n" +
				"checkItem.projectId = project.id\n" +
				"AND checkItem.isDeleted = 0 and ifNull(checkItem.status,0)=1\n" +
				"AND project.isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND userId = '"+userId+"'";
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		for (PerformanceProjectEntity project : projects) {
			List<PerformanceStaffCheckItemEntity> checkItems = getStaffProjectCheckInfos(
					project.getId(), userId, Integer.parseInt(year), Integer.parseInt(month));
			List<PerformanceStaffCheckItemEntity> cloneCheckItems = new ArrayList<>();
			//拷贝一份，作修改，评审通过后，再做真正的修改
			for(PerformanceStaffCheckItemEntity checkItem: checkItems){
				PerformanceStaffCheckItemEntity choneStaffCheckItem = checkItem.clone();
				choneStaffCheckItem.setCloneId(choneStaffCheckItem.getId());
				choneStaffCheckItem.setId(null);
				choneStaffCheckItem.setStatus(null);
				int id = baseDao.hqlSave(choneStaffCheckItem);
				choneStaffCheckItem.setId(id);
				cloneCheckItems.add(choneStaffCheckItem);
			}
			project.setStaffCheckItems(cloneCheckItems);
		}
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceStaffCheckItemEntity> getPersonalProjectCheckInfos(String projectId, String checkItemIds) {
		String hql = "from PerformanceStaffCheckItemEntity where isDeleted=0 and projectId=" + projectId
				+" and id in("+checkItemIds+")";
		return (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public List<PerformanceStaffCheckItemEntity> saveStaffCheckProject(PerformanceProjectEntity project,
			List<PerformanceStaffCheckItemEntity> staffCheckItems) {
		project.setAddTime(new Date());
		int projectId = baseDao.hqlSave(project);
		List<PerformanceStaffCheckItemEntity> _staffCheckItems = new ArrayList<>();
		for (PerformanceStaffCheckItemEntity checkItem : staffCheckItems) {
			if(null == checkItem){
				continue;
			}
			checkItem.setAddTime(new Date());
			checkItem.setProjectId(projectId);
			baseDao.hqlSave(checkItem);
			_staffCheckItems.add(checkItem);
		}
		return _staffCheckItems;
	}

	@Override
	public List<PerformanceStaffCheckItemEntity> updateStaffCheckProject(PerformanceProjectEntity project,
			List<PerformanceStaffCheckItemEntity> staffCheckItems, String userId) {
		PerformanceProjectEntity oldProject = getProjectInfo(String.valueOf(project.getId()));
		//项目名不一致，新增
		if(!project.getProject().equals(oldProject.getProject())){
			project.setAddTime(new Date());
			project.setId(null);
			int projectId = baseDao.hqlSave(project);
			project.setId(projectId);
		}
		List<PerformanceStaffCheckItemEntity> _staffCheckItems = new ArrayList<>();
		for (PerformanceStaffCheckItemEntity checkItem : staffCheckItems) {
			if(null == checkItem){
				continue;
			}
			checkItem.setProjectId(project.getId());
			checkItem.setSupervisor(userId);
			if (null != checkItem.getId()) {
				baseDao.hqlUpdate(checkItem);
			} else {
				checkItem.setAddTime(new Date());
				baseDao.hqlSave(checkItem);
			}
			_staffCheckItems.add(checkItem);
		}
		return _staffCheckItems;
	}

	@Override
	public void deleteStaffProject(String projectId, String checkItemIds) {
		String hql = "update PerformanceStaffCheckItemEntity set isDeleted=1 where id in("
				+checkItemIds+") and projectId="+projectId;
		baseDao.excuteHql(hql);
	}

	@Override
	public void deleteStaffCheckItem(String checkItemId) {
		String hql = "update PerformanceStaffCheckItemEntity set isDeleted=1 where id=" + checkItemId;
		baseDao.excuteHql(hql);
	}

	@Override
	public void updatePersonalPerformance(String userId, String checkItemIds, String staffUserId,
			String year, String month) {
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		// PM评审人员
		List<String> pmAuditors = permissionService.findUsersByPermissionCode(Constants.PM_AUDITOR);
		if (pmAuditors.size() > 0) {
			vars.put("auditors", pmAuditors);
		} else {
			throw new RuntimeException("未找到PM评审人");
		}
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("staffPerformance", vars);
		// 查询第一个任务
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		// 完成任务
		taskService.complete(task.getId());
		// 记录数据
		PerformanceEntity performance = new PerformanceEntity();
		performance.setProcessInstanceID(processInstance.getId());
		performance.setAddTime(new Date());
		performance.setUserId(userId);
		performance.setCheckItemIds(checkItemIds);
		performance.setStaffUserId(staffUserId);
		performance.setYear(year);
		performance.setMonth(month);
		baseDao.hqlSave(performance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<PerformanceEntity> findUpdatePerformanceApplys(Integer limit, Integer page, String userId) {
		String hql = "from PerformanceEntity where checkItemIds is not null and isDeleted=0 and userId='" + userId + "' order by addTime desc";
		List<PerformanceEntity> performanceTasks = (List<PerformanceEntity>) baseDao.hqlPagedFind(hql, page, limit);
		for (PerformanceEntity performanceTask : performanceTasks) {
			String processInstanceID = performanceTask.getProcessInstanceID();
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceID)
					.singleResult();
			if (pInstance != null) {
				performanceTask.setStatus("进行中");
				performanceTask.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = performanceTask.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						performanceTask.setStatus(t.getName());
				}
			}
			String staffUserId = performanceTask.getStaffUserId();
			StaffEntity staff = staffService.getStaffByUserId(staffUserId);
			performanceTask.setUserName(staff.getStaffName());
			//岗位
			List<GroupDetailVO> groupDetailVOs = staffService.findGroupDetailsByUserID(staffUserId);
			performanceTask.setGroupDetailVOs(groupDetailVOs);
		}
		String hqlCount = "select count(id) from PerformanceEntity where checkItemIds is not null and isDeleted=0 and userId='" + userId + "'";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount) + "");
		return new ListResult<>(performanceTasks, count);
	}

	@Override
	public ListResult<Object> findStaffPerformances(Integer limit, Integer page, String year,
			String month, String userName, String userId) {
		String sql = "SELECT\n" +
				"	staff.UserID,\n" +
				"	staffName,\n" +
				"	companyName,\n" +
				"	GROUP_CONCAT(DISTINCT\n" +
				"	CONCAT(\n" +
				"		DepartmentName,\n" +
				"		'-',\n" +
				"		PositionName\n" +
				"	))\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_PerformanceStaffCheckItem\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"		AND MONTH = "+month+"\n" +
				"		AND YEAR = "+year+"\n" +
				"	) a, \n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep,\n" +
				"	OA_Position p\n" +
				"WHERE\n" +
				"	a.userId = staff.UserID\n" +
				"AND a.supervisor='"+userId+"'\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND company.CompanyID = detail.CompanyID\n" +
				"AND detail.DepartmentID = dep.DepartmentID\n" +
				"AND detail.PositionID = p.PositionID\n" +
				"AND (a.positionId = p.PositionID OR a.positionId is null)\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n";
		if (StringUtils.isNotBlank(userName)) {
			sql += "AND staffName like'%" + EscapeUtil.decodeSpecialChars(userName) + "%'\n";
		}
		sql += "GROUP BY a.userId";
		List<Object> staffPerformances = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(DISTINCT staff.UserID)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_PerformanceStaffCheckItem\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"		AND MONTH = "+month+"\n" +
				"		AND YEAR = "+year+"\n" +
				"	) a, \n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep,\n" +
				"	OA_Position p\n" +
				"WHERE\n" +
				"	a.userId = staff.UserID\n" +
				"AND a.supervisor='"+userId+"'\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND company.CompanyID = detail.CompanyID\n" +
				"AND detail.DepartmentID = dep.DepartmentID\n" +
				"AND detail.PositionID = p.PositionID\n" +
				"AND (a.positionId = p.PositionID OR a.positionId is null)\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n";
		if (StringUtils.isNotBlank(userName)) {
			sqlCount += "AND staffName like'%" + EscapeUtil.decodeSpecialChars(userName) + "%'\n";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sqlCount)));
		return new ListResult<>(staffPerformances, count);
	}

	@Override
	public boolean checkHasUpdateApply(String userId, String year, String month) {
		String sql = "SELECT\n" +
				"	count(id)\n" +
				"FROM\n" +
				"	OA_Performance\n" +
				"WHERE\n" +
				"	staffUserId = '"+userId+"'\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND isDeleted = 0\n" +
				"AND applyResult IS NULL";
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sql)));
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public List<PerformanceEntity> findPersonalPerformanceTaskVos(List<Task> personalPerformanceTasks) {
		List<PerformanceEntity> performanceTaskVos = new ArrayList<>();
		if(null != personalPerformanceTasks){
			for (Task performanceTask : personalPerformanceTasks) {
				PerformanceEntity performanceEntity = getPerformanceByProcessInstanceId(
						performanceTask.getProcessInstanceId());
				performanceEntity.setTaskName(performanceTask.getName());
				performanceEntity.setTaskId(performanceTask.getId());
				performanceEntity.setUserName(staffDao.getStaffByUserID(performanceEntity.getUserId()).getStaffName());
				String staffUserId = performanceEntity.getStaffUserId();
				StaffEntity staff = staffService.getStaffByUserId(staffUserId);
				performanceEntity.setRequestUserName(staff.getStaffName());
				//岗位
				List<GroupDetailVO> groupDetailVOs = staffService.findGroupDetailsByUserID(staffUserId);
				performanceEntity.setGroupDetailVOs(groupDetailVOs);
				performanceTaskVos.add(performanceEntity);
			}
		}
		return performanceTaskVos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> findPersonalCheckProjects(String checkItemIds) {
		String hql = "SELECT DISTINCT\n" +
				"	project\n" +
				"FROM\n" +
				"	PerformanceStaffCheckItemEntity checkItem,\n" +
				"	PerformanceProjectEntity project\n" +
				"WHERE\n" +
				"checkItem.projectId = project.id\n" +
				"AND checkItem.id in ("+checkItemIds+")";
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		for (PerformanceProjectEntity project : projects) {
			hql = "FROM\n" +
					"	PerformanceStaffCheckItemEntity\n" +
					"WHERE\n" +
					"id in ("+checkItemIds+") and projectId="+project.getId();
			List<PerformanceStaffCheckItemEntity> staffCheckItems = (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
			project.setStaffCheckItems(staffCheckItems);
		}
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pmAuditPersonal(String taskId, String result, String comment, String userId) throws Exception {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		identityService.setAuthenticatedUserId(userId);
		if (!StringUtils.isBlank(comment)) {
			// 添加评论
			taskService.addComment(taskId, pInstance.getId(), comment);
		}
		Map<String, Object> vars = new HashMap<>();
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId, vars);
		PerformanceEntity performance = getPerformanceByProcessInstanceId(pInstance.getId());
		performance.setApplyResult(Integer.parseInt(result));
		baseDao.hqlUpdate(performance);
		// 审批通过，修改个人的绩效考核项
		if (TaskResultEnum.AGREE.getValue() == Integer.parseInt(result)) {
			String hql = "FROM\n" +
					"	PerformanceStaffCheckItemEntity\n" +
					"WHERE\n" +
					"isDeleted = 0 and ifNull(status, 0)=1\n" +
					"AND isDeleted = 0\n" +
					"AND YEAR = "+performance.getYear()+"\n" +
					"AND MONTH = "+performance.getMonth()+"\n" +
					"AND userId = '"+performance.getStaffUserId()+"'";
			//正本
			List<PerformanceStaffCheckItemEntity> realStaffCheckItems = (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
			
			String checkItemIds = performance.getCheckItemIds();
			hql = "select id, cloneId from PerformanceStaffCheckItemEntity where id in("+checkItemIds+")";
			//副本
			List<Object> staffCheckItemObjs = (List<Object>) baseDao.hqlfind(hql);
			List<Integer> realStaffCheckItemIds = new ArrayList<>();
			for(Object staffCheckItemObj: staffCheckItemObjs){
				Object[] staffCheckItem = (Object[])staffCheckItemObj;
				//不是拷贝过来的，是新增的考核项
				if(null == staffCheckItem[1]){
					hql = "update PerformanceStaffCheckItemEntity set status=1 where id="+staffCheckItem[0];
					baseDao.excuteHql(hql);
				}else{
					realStaffCheckItemIds.add((int)staffCheckItem[1]);
				}
			}
			for(PerformanceStaffCheckItemEntity realStaffCheckItem: realStaffCheckItems){
				int staffCheckItemId = realStaffCheckItem.getId();
				//不包含，删除
				if(!realStaffCheckItemIds.contains(staffCheckItemId)){
					realStaffCheckItem.setIsDeleted(1);
					baseDao.hqlUpdate(realStaffCheckItem);
					//更新
				}else{
					hql = "from PerformanceStaffCheckItemEntity where id in("+checkItemIds+") and cloneId="+staffCheckItemId;
					PerformanceStaffCheckItemEntity staffCheckItem = (PerformanceStaffCheckItemEntity) baseDao.hqlfindUniqueResult(hql);
					realStaffCheckItem.setAddMoney(staffCheckItem.getAddMoney());
					realStaffCheckItem.setAddMoneyType(staffCheckItem.getAddMoneyType());
					realStaffCheckItem.setCheckItem(staffCheckItem.getCheckItem());
					realStaffCheckItem.setCoefficient(staffCheckItem.getCoefficient());
					realStaffCheckItem.setMonth(staffCheckItem.getMonth());
					realStaffCheckItem.setPerAddMoneyValue(staffCheckItem.getPerAddMoneyValue());
					realStaffCheckItem.setPerReduceMoneyValue(staffCheckItem.getPerReduceMoneyValue());
					realStaffCheckItem.setReduceMoney(staffCheckItem.getReduceMoney());
					realStaffCheckItem.setReduceMoneyType(staffCheckItem.getReduceMoneyType());
					realStaffCheckItem.setYear(staffCheckItem.getYear());
					realStaffCheckItem.setProjectId(staffCheckItem.getProjectId());
					baseDao.hqlUpdate(realStaffCheckItem);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void synStaffCheckItems(String userID, Integer positionID) throws Exception {
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String sql = "select distinct supervisor from OA_PerformanceStaffCheckItem where isDeleted=0 and status=1 and year="
				+year+" and month="+month+" and positionId ="+positionID;
		String supervisor = (String) baseDao.getUniqueResult(sql);
		String hql = "from PerformancePositionTemplateEntity where isDeleted=0 and ifNull(status,0)=1 "
				+ "and positionId =" + positionID;
		PerformancePositionTemplateEntity positionTemplate = (PerformancePositionTemplateEntity) baseDao.hqlfindUniqueResult(hql);
		if(null != positionTemplate && StringUtils.isNotBlank(supervisor)){
			hql = "from PerformanceProjectEntity where isDeleted=0 and templateId=" + positionTemplate.getId();
			List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
			for (PerformanceProjectEntity project : projects) {
				List<PerformanceCheckItemEntity> checkItems = getProjectCheckInfos(String.valueOf(project.getId()));
				for (PerformanceCheckItemEntity checkItem : checkItems) {
					PerformanceStaffCheckItemEntity staffPerformance = new PerformanceStaffCheckItemEntity();
					staffPerformance.setAddMoney(checkItem.getAddMoney());
					staffPerformance.setAddMoneyType(checkItem.getAddMoneyType());
					staffPerformance.setCheckItem(checkItem.getCheckItem());
					staffPerformance.setCoefficient(checkItem.getCoefficient());
					staffPerformance.setMonth(month);
					staffPerformance.setPerAddMoneyValue(checkItem.getPerAddMoneyValue());
					staffPerformance.setPerReduceMoneyValue(checkItem.getPerReduceMoneyValue());
					staffPerformance.setProjectId(checkItem.getProjectId());
					staffPerformance.setReduceMoney(checkItem.getReduceMoney());
					staffPerformance.setReduceMoneyType(checkItem.getReduceMoneyType());
					staffPerformance.setUserId(userID);
					staffPerformance.setSupervisor(supervisor);
					staffPerformance.setPositionId(String.valueOf(positionID));
					staffPerformance.setYear(year);
					staffPerformance.setAddTime(new Date());
					staffPerformance.setStatus(TaskResultEnum.AGREE.getValue());
					baseDao.hqlSave(staffPerformance);
				}
			}
			Map<String, String> resultMap = new HashMap<>();
			resultMap.put("userId", userID);
			resultMap.put("year", String.valueOf(year));
			resultMap.put("month", String.valueOf(month));
			ToBeDoneTaskEntity actualValueTask = new ToBeDoneTaskEntity();
			actualValueTask.setAddTime(new Date());
			actualValueTask.setData(ObjectByteArrTransformer.toByteArray(resultMap));
			actualValueTask.setStatus(0);
			actualValueTask.setType(BusinessTypeEnum.PERFORMANCE_TARGET.getValue());
			baseDao.hqlSave(actualValueTask);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceProjectEntity> getActualProjectsbyTaskId(String taskId) throws Exception {
		String hql = "from ToBeDoneTaskEntity where id="+taskId;
		ToBeDoneTaskEntity task = (ToBeDoneTaskEntity) baseDao.hqlfindUniqueResult(hql);
		Map<String, String> resultMap = (Map<String, String>) ObjectByteArrTransformer.toObject(task.getData());
		String userId = resultMap.get("userId");
		String year = resultMap.get("year");
		String month = resultMap.get("month");
		hql = "SELECT DISTINCT\n" +
				"	project\n" +
				"FROM\n" +
				"	PerformanceStaffCheckItemEntity checkItem,\n" +
				"	PerformanceProjectEntity project\n" +
				"WHERE\n" +
				"	actualValue IS NULL\n" +
				"AND checkItem.projectId = project.id\n" +
				"AND checkItem.isDeleted = 0 and ifNull(checkItem.status,0)=1\n" +
				"AND project.isDeleted = 0\n" +
				"AND YEAR = "+year+"\n" +
				"AND MONTH = "+month+"\n" +
				"AND userId = '"+(String)userId+"'";
		List<PerformanceProjectEntity> projects = (List<PerformanceProjectEntity>) baseDao.hqlfind(hql);
		for (PerformanceProjectEntity project : projects) {
			List<PerformanceStaffCheckItemEntity> checkItems = getStaffProjectCheckInfos(
					project.getId(), (String)userId, Integer.parseInt(year), Integer.parseInt(month));
			project.setStaffCheckItems(checkItems);
		}
		return projects;
	}
}
