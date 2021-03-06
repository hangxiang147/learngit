package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
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
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.PermissionDao;
import com.zhizaolian.staff.dao.RightDao;
import com.zhizaolian.staff.dao.WorkReportDao;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.entity.ViewReportValidEntity;
import com.zhizaolian.staff.entity.ViewWorkReportEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.ViewWorkReportVo;
import com.zhizaolian.staff.vo.WorkReportDetailVO;

public class ViewReportServiceImpl implements ViewReportService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkReportDao workReportDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Override
	public void startViewReportApply(ViewWorkReportVo viewWorkReportVo) {
		viewWorkReportVo.setBusinessType(BusinessTypeEnum.VIEW_REPORT.getName());
		viewWorkReportVo.setTitle(viewWorkReportVo.getUserName()+"的"+BusinessTypeEnum.VIEW_REPORT.getName());
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", viewWorkReportVo);
		List<String> hrGroupList = staffService.queryHRGroupList(viewWorkReportVo.getUserID());
		if (CollectionUtils.isEmpty(hrGroupList) || !staffService.hasGroupMember(hrGroupList)) {
			throw new RuntimeException("未找到该申请的审批人（人力资源）！");
		}
		vars.put("hrGroup", hrGroupList);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.VIEW_WORK_REPORT);
		viewWorkReportVo.setProcessInstanceID(processInstance.getId());
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), viewWorkReportVo.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		Set<String> allUserIds = new HashSet<>();
		if(StringUtils.isNotBlank(viewWorkReportVo.getUserIds())){
			String[] UserIds = viewWorkReportVo.getUserIds().split(",");
			allUserIds.addAll(Arrays.asList(UserIds));
			viewWorkReportVo.setUserIds(StringUtils.join(UserIds, ","));
		}
		String[] companyIds = viewWorkReportVo.getCompanyId();
		String[] depIds = viewWorkReportVo.getDepartmentId();
		if(null != companyIds){
			if(companyIds.length>0 && StringUtils.isNotBlank(companyIds[0])){
				viewWorkReportVo.setCompanyIds(StringUtils.join(companyIds, ","));
				viewWorkReportVo.setDepIds(StringUtils.join(depIds, ","));
				int index = 0;
				for(String depId: depIds){
					String companyId = companyIds[index];
					Set<String> underlings = new HashSet<>();
					if(StringUtils.isBlank(depId)){
						staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), 0, underlings);
					}else{
						staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(depId), underlings);
					}
					allUserIds.addAll(underlings);
					index++;
				}
			}
		}
		viewWorkReportVo.setAllUserIds(StringUtils.join(allUserIds, ","));
		saveViewWorkReport(viewWorkReportVo);
	}
	private void saveViewWorkReport(ViewWorkReportVo viewWorkReportVo) {
		ViewWorkReportEntity viewWorkReport = (ViewWorkReportEntity) CopyUtil.tryToEntity
				(viewWorkReportVo, ViewWorkReportEntity.class);
		viewWorkReport.setIsDeleted(0);
		viewWorkReport.setAddTime(new Date());
		baseDao.hqlSave(viewWorkReport);
	}
	@Override
	public int findViewReportTaskCountByGroups(List<Group> groups) {
		String arrayString = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.VIEW_WORK_REPORT_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return count;
	}
	@Override
	public ListResult<ViewWorkReportVo> findViewReportTasksByGroups(List<Group> groups, Integer page, Integer limit) throws Exception {
		String arrayString = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.VIEW_WORK_REPORT_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<ViewWorkReportVo> taskVOs = createTaskVOList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.VIEW_WORK_REPORT_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<ViewWorkReportVo>(taskVOs, count);
	}
	private List<ViewWorkReportVo> createTaskVOList(List<Object> tasks) throws Exception{
		List<ViewWorkReportVo> taskVOs = new ArrayList<ViewWorkReportVo>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			ViewWorkReportVo arg = (ViewWorkReportVo) runtimeService.getVariable(pInstance.getId(), "arg");
			List<String> userNames = new ArrayList<>();
			if(StringUtils.isNotBlank(arg.getUserIds())){
				String[] userIds = arg.getUserIds().split(",");
				for(String userId: userIds){
					userNames.add(staffService.getRealNameByUserId(userId));
				}
			}
			ViewWorkReportVo taskVO = new ViewWorkReportVo();
			if(!CollectionUtils.isEmpty(userNames)){
				taskVO.setUserNames(StringUtils.join(userNames, ","));
			}
			String[] companyIds = arg.getCompanyId();
			String[] depIds = arg.getDepartmentId();
			if(null != companyIds && StringUtils.isNotBlank(companyIds[0])){
				List<String> companyAndDepList = new ArrayList<>();
				int index = 0;
				for(String companyId: companyIds){
					CompanyEntity company = companyDao.getCompanyByCompanyID(Integer.parseInt(companyId));
					String CompanyName = "";
					if(null != company){
						CompanyName = company.getCompanyName();
					}
					String depId = depIds[index];
					if(StringUtils.isNotBlank(depId)){
						DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(depId));
						if(null != department){
							CompanyName += "-"+department.getDepartmentName();
						}
					}
					companyAndDepList.add(CompanyName);
					index++;
				}
				taskVO.setCompanyAndDepList(companyAndDepList);
			}
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(arg.getUserID());
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			} 
			taskVO.setDepartment(department);
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setViewType(arg.getViewType());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTitle(arg.getTitle());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	@Override
	public void updateViewReportProcessStatus(String processInstanceID, TaskResultEnum result) {
		String sql = "update OA_ViewWorkReport set applyResult="+result.getValue()+" where processInstanceID="+processInstanceID;
		baseDao.excuteSql(sql);
	}
	@Override
	public void saveViewReportRight(String processInstanceID) {
		ViewWorkReportEntity viewWorkReport = getViewWorkReportByProcessInstanceId(processInstanceID);
		String requestUserId = viewWorkReport.getRequestUserId();
		PermissionEntity permission = permissionDao.getPermissionByCode(Constants.VIEW_WORK_REPORT);
		if(null != permission){
			//检查是否已有权限
			if(!rightDao.checkHasThisRight(requestUserId, permission.getPermissionID())){
				rightDao.createRightMemberShip(requestUserId, "1", String.valueOf(permission.getPermissionID()));
			}
		}
		//保存查看的人员
		String[] allUserId = viewWorkReport.getAllUserIds().split(",");
		for(String userId: allUserId){
			ViewReportValidEntity viewReportValid = new ViewReportValidEntity();
			viewReportValid.setUserId(userId);
			viewReportValid.setViewerId(viewWorkReport.getRequestUserId());
			viewReportValid.setViewType(viewWorkReport.getViewType());
			baseDao.hqlSave(viewReportValid);
		}
		//保存查看的部门
		String companyIdStr = viewWorkReport.getCompanyIds();
		String depIdStr = viewWorkReport.getDepIds();
		if(null != companyIdStr){
			String[] companyIds = companyIdStr.split(",");
			String[] depIds = depIdStr.split(",");
			int index = 0;
			for(String companyId: companyIds){
				String depId = depIds[index];
				ViewReportValidEntity viewReportValid = new ViewReportValidEntity();
				viewReportValid.setCompanyId(companyId);
				viewReportValid.setDepId(depId);
				viewReportValid.setViewerId(viewWorkReport.getRequestUserId());
				viewReportValid.setViewType(viewWorkReport.getViewType());
				baseDao.hqlSave(viewReportValid);
			}
		}
	}
	private ViewWorkReportEntity getViewWorkReportByProcessInstanceId(String processInstanceID) {
		String hql = "from ViewWorkReportEntity where processInstanceID="+processInstanceID;
		return (ViewWorkReportEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void checkPermission(List<String> permissions, String userId) {
		String sql = "select COUNT(id) from OA_ViewReportValid where isDeleted=0 and viewerId='"+userId+"'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count==0){
			permissions.remove(Constants.VIEW_WORK_REPORT);
		}
	}
	@Override
	public boolean checkCanViewDepWorkReport(String userId) {
		String sql = "select COUNT(id) from OA_ViewReportValid where isDeleted=0 and viewerId='"+userId+"' and companyId is not null";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public ListResult<WorkReportDetailVO> getWorkReportsByConditions(ViewWorkReportVo viewReportVo, Integer page,
			Integer limit) {
		List<Object> limitedMsg=baseDao.findPageList(getLimitSql(viewReportVo),page,limit);
		StringBuffer sb = new StringBuffer();
		if(!CollectionUtils.isEmpty(limitedMsg)){
			sb.append(" and (");
			for(int i=0,length=limitedMsg.size();i<length;i++){
				Object[] currentObject=(Object[]) limitedMsg.get(i);
				sb.append(" (").append(" workReport.userId='").append(""+currentObject[0]).append("' and workReport.reportDate='").append(DateUtil.getDayStr((Date)currentObject[1])).append("') or ");
			}
			sb.delete(sb.lastIndexOf("or"),sb.length());
			sb.append(" ) ");
		}else{
			sb.append(" and 1=0 ");
		}
		List<Object> workReportEntities = workReportDao.findWorkreportListByUserID(getQuerySqlByWorkReportDetailVO(viewReportVo,sb.toString()));
		List<WorkReportDetailVO> workReportDetailVOs=new ArrayList<WorkReportDetailVO>();
		for(Object obj:workReportEntities){
			Object[] objs = (Object[]) obj;
			WorkReportDetailVO workReportDetailVO1=new WorkReportDetailVO();
			workReportDetailVO1.setReportDate(DateUtil.formateDate((Date) objs[0]));
			workReportDetailVO1.setName((String) objs[1]);
			String[] contents=StringUtils.split((String) objs[2], "|||");
			String[] assignTasks=StringUtils.splitPreserveAllTokens((String) objs[3], ",");
			String[] completes=StringUtils.split((String) objs[4], ",");
			String[] works=StringUtils.split((String) objs[5], ",");
			workReportDetailVO1.setAddTime(DateUtil.formateFullDate((Date) objs[6]));
			String[] quantity=StringUtils.splitPreserveAllTokens((String) objs[7], ",");
			workReportDetailVO1.setWeekDay((String)objs[8]);
			workReportDetailVO1.setTotalHours((Double) objs[9]);
			workReportDetailVO1.setUserID((String)objs[10]);

			List<String> workContent=new ArrayList<>();
			for(int i = 0;i<contents.length;i++){
				String content=String.valueOf(contents[i]);
				workContent.add(content);
			}			
			workReportDetailVO1.setWorkContent(workContent);
			List<Integer> quantities=new ArrayList<>();
			for(int i=0;i<quantity.length;i++){
				Integer number=1;
				if(!StringUtils.isBlank(quantity[i])){
					number=Integer.parseInt(quantity[i]);
				}
				quantities.add(number);
			}
			workReportDetailVO1.setQuantities(quantities);


			List<String> completeState=new ArrayList<>();
			for(int i = 0;i<completes.length;i++){
				String complete=String.valueOf(completes[i]);
				completeState.add(complete);
			}
			workReportDetailVO1.setCompleteState(completeState);


			List<Double> workHours=new ArrayList<>();
			for(int i=0;i<works.length;i++){
				Double work=Double.valueOf(works[i]);
				workHours.add(work);
			}
			workReportDetailVO1.setWorkHours(workHours);


			List<String> assignTaskUserName=new ArrayList<>();
			for(int i=0;i<assignTasks.length;i++){
				String name="";
				if(!StringUtils.isBlank(assignTasks[i])){
					name=String.valueOf(staffService.getStaffByUserID(assignTasks[i]).getLastName());
				}
				assignTaskUserName.add(name);

			}
			workReportDetailVO1.setAssignTaskName(assignTaskUserName);

			workReportDetailVOs.add(workReportDetailVO1);
		}

		Object countObj = baseDao.getUniqueResult(getCountSql(viewReportVo));
		int count = countObj==null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<WorkReportDetailVO>(workReportDetailVOs,count);
	}
	@Override
	public boolean checkApplyThisDep(Integer companyId, Integer depId, String userId) {
		String sql = "";
		if(null == depId){
			sql = "select count(id) from OA_ViewReportValid where isDeleted=0 and viewerId='"+
				  userId+"' and companyId="+companyId+" and depId=''";
		}else{
			sql = "select count(id) from OA_ViewReportValid where isDeleted=0 and viewerId='"+
		          userId+"' and companyId="+companyId+" and depId="+depId;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public boolean checkApplyThisUser(String userId, String viewerId) {
		String sql = "select count(id) from OA_ViewReportValid where isDeleted=0 and viewerId='"+
				viewerId+"' and userId='"+userId+"'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	private String  getLimitSql(ViewWorkReportVo viewReportVo){

		String prevSql="	SELECT\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate\n" +
				"		FROM\n" +
				"			OA_Workreport workReport\n" +
				"		LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID\n" ;
		if(viewReportVo.get_companyID()!=null){
			prevSql+="		LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_\n" +
					"		LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_\n" +
					"		LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID\n" ;
		}
		prevSql+=	"		WHERE\n" +
				"			staff.IsDeleted = 0\n" +
				"		AND staff. STATUS != 4\n" +
				"		AND workReport.IsDeleted = 0\n" 
				+getWhereByWorkReportDetailVO(viewReportVo)+
				"		GROUP BY\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate"+
				"	order by workReport.reportDate desc,workReport.ReportId  asc ";

		return prevSql;
	}
	private String getWhereByWorkReportDetailVO(ViewWorkReportVo viewReportVo) {
		StringBuffer whereSql=new StringBuffer();
/*		if (!StringUtils.isBlank(viewReportVo.getBeginDate())) {
			whereSql.append(" and workReport.reportDate >= '"+viewReportVo.getBeginDate()+"'");
		}
		if (!StringUtils.isBlank(viewReportVo.getEndDate())) {
			whereSql.append(" and workReport.reportDate <= '"+viewReportVo.getEndDate()+"'");
		}*/
		if(StringUtils.isNotBlank(viewReportVo.getReportDate())){
			whereSql.append(" and workReport.reportDate = '"+viewReportVo.getReportDate()+"'");
		}
		if (!StringUtils.isBlank(viewReportVo.getUserID())) {
			whereSql.append(" and staff.UserID ='"+viewReportVo.getUserID()+"' ");
		}
		if (viewReportVo.get_companyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = "+viewReportVo.get_companyID());
			String departmentIds = "";
			if (viewReportVo.get_departmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(viewReportVo.get_companyID(), viewReportVo.get_departmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(viewReportVo.get_departmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
				departmentIds = arrayString.substring(1, arrayString.length()-1);
			}
			if(viewReportVo.getPage()==1 && StringUtils.isBlank(viewReportVo.getUserID())){
				//若是公司或部门，判断此次查看是不是一次性的，若是，排除下已查看的
				if(checkIsOneTime(viewReportVo, departmentIds)){
					String deletedUserIdStr = getAllDeletedUserIds(viewReportVo.getRequestUserId());
					if(StringUtils.isNotBlank(deletedUserIdStr)){
						whereSql.append(" and workReport.UserID not in("+deletedUserIdStr+")");
					}
					viewReportVo.setOneTime(true);
				}
			}
		}
		
		return whereSql.toString();
	}
	private String getAllDeletedUserIds(String requestUserId) {
		String sql = "select distinct(userId) from OA_ViewReportValid where isDeleted=1 and viewerId='"+requestUserId+"' and viewType='"+Constants.ONE_TIME+"'";
		List<Object> objList = baseDao.findBySql(sql);
		List<String> deletedUserIds = new ArrayList<>();
		for(Object obj: objList){
			deletedUserIds.add("'"+obj+"'");
		}
		return StringUtils.join(deletedUserIds, ",");
	}
	private boolean checkIsOneTime(ViewWorkReportVo viewReportVo, String departmentIds) {
		String sql = "";
		if (viewReportVo.get_departmentID() != null) {
			sql = "select count(id) from OA_ViewReportValid where isDeleted=0 and viewType!='"
					+Constants.ONE_TIME+"' and companyId="+viewReportVo.get_companyID();
		}else{
			sql = "select count(id) from OA_ViewReportValid where isDeleted=0 and viewType!='"
					+Constants.ONE_TIME+"' and companyId="+viewReportVo.get_companyID()+" and depId in("+departmentIds+")";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return false;
		}
		return true;
	}
	private String getQuerySqlByWorkReportDetailVO(ViewWorkReportVo viewReportVo,String tailStr){
		StringBuffer sql=new StringBuffer("SELECT s.ReportDate, s.StaffName, GROUP_CONCAT(s.WorkContent order by s.reportID separator '|||'), "
				+ "GROUP_CONCAT(s.AssignTaskUserID order by s.reportID), GROUP_CONCAT(s.CompleteState order by s.reportID), GROUP_CONCAT(s.WorkHours order by s.reportID), "
				+ "s.AddTime,GROUP_CONCAT(s.Quantity order by s.reportID),s.WeekDay, SUM(s.WorkHours), s.userId from( "
				+ "select DISTINCT * from (SELECT workReport.ReportDate, staff.StaffName, workReport.WorkContent, "
				+ "workReport.AssignTaskUserID, workReport.CompleteState, workReport.WorkHours, workReport.AddTime,workReport.Quantity,workReport.WeekDay,workReport.reportID, workReport.userId "
				+ "FROM OA_Workreport workReport "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_ "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID "				
				+ "WHERE staff.IsDeleted = 0 and staff.Status != 4 and workReport.IsDeleted = 0 ");
		sql.append(tailStr);
		sql.append(getWhereByWorkReportDetailVO(viewReportVo));
		sql.append(" )a) s");
		sql.append(" GROUP BY s.StaffName,s.ReportDate order by s.ReportDate desc,s.ReportId asc ");
		return sql.toString();

	}
	private String  getCountSql(ViewWorkReportVo viewReportVo){
		String prevSql="SELECT\n" +
				"	count(*) AS total\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			count(*)\n" +
				"		FROM\n" +
				"			OA_Workreport workReport\n" +
				"		LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID\n" ;
		if(viewReportVo.get_companyID()!=null){
			prevSql+="		LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_\n" +
					"		LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_\n" +
					"		LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID\n" ;
		}
		prevSql+=	"		WHERE\n" +
				"			staff.IsDeleted = 0\n" +
				"		AND staff. STATUS != 4\n" +
				"		AND workReport.IsDeleted = 0\n" 
				+getWhereByWorkReportDetailVO(viewReportVo)+
				"		GROUP BY\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate\n" +
				"	) u";

		return prevSql;
	}
	@Override
	public void deleteUserIdByOneTime(String userId, String viewerId) {
		String sql = "update OA_ViewReportValid set isDeleted=1 where viewType='"+
						Constants.ONE_TIME+"' and userId='"+userId+"' and viewerId='"+viewerId+"'";
		baseDao.excuteSql(sql);
	}
	@Override
	public ListResult<ViewWorkReportVo> findViewWorkReportListByUserID(String id, Integer page, Integer limit) {
		List<ViewWorkReportEntity> viewWorkReports = getViewWorkReportByUserId(id,
				page, limit);

		List<ViewWorkReportVo> viewWorkReportVos = new ArrayList<ViewWorkReportVo>();
		for (ViewWorkReportEntity viewWorkReport : viewWorkReports) {
			ViewWorkReportVo viewWorkReportVo = null;
			try {
				viewWorkReportVo = (ViewWorkReportVo) CopyUtil.tryToVo(viewWorkReport, ViewWorkReportVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(viewWorkReport.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ViewWorkReportVo arg = (ViewWorkReportVo) variable.getValue();
					viewWorkReportVo.setRequestDate(arg.getRequestDate());
					viewWorkReportVo.setTitle(arg.getTitle());
					viewWorkReportVo.setUserName(arg.getUserName());
					List<String> userNames = new ArrayList<>();
					if(StringUtils.isNotBlank(arg.getUserIds())){
						String[] userIds = arg.getUserIds().split(",");
						for(String userId: userIds){
							userNames.add(staffService.getRealNameByUserId(userId));
						}
					}
					if(!CollectionUtils.isEmpty(userNames)){
						viewWorkReportVo.setUserNames(StringUtils.join(userNames, ","));
					}
					String[] companyIds = arg.getCompanyId();
					String[] depIds = arg.getDepartmentId();
					if(null != companyIds && StringUtils.isNotBlank(companyIds[0])){
						List<String> companyAndDepList = new ArrayList<>();
						int index = 0;
						for(String companyId: companyIds){
							CompanyEntity company = companyDao.getCompanyByCompanyID(Integer.parseInt(companyId));
							String CompanyName = "";
							if(null != company){
								CompanyName = company.getCompanyName();
							}
							String depId = depIds[index];
							if(StringUtils.isNotBlank(depId)){
								DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(depId));
								if(null != department){
									CompanyName += "-"+department.getDepartmentName();
								}
							}
							companyAndDepList.add(CompanyName);
							index++;
						}
						viewWorkReportVo.setCompanyAndDepList(companyAndDepList);
					}
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(viewWorkReport.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				viewWorkReportVo.setStatus("处理中");
				viewWorkReportVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = viewWorkReportVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						viewWorkReportVo.setStatus(t.getName());
				}
			}
			viewWorkReportVos.add(viewWorkReportVo);
		}
		int count = getViewWorkReportCountByUserId(id);
		return new ListResult<ViewWorkReportVo>(viewWorkReportVos, count);
	}
	private int getViewWorkReportCountByUserId(String id) {
		String hql = "select count(id) from ViewWorkReportEntity where IsDeleted=0 and requestUserId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<ViewWorkReportEntity> getViewWorkReportByUserId(String id, Integer page, Integer limit) {
		String hql="from ViewWorkReportEntity where IsDeleted=0 and requestUserId='"+id+"' order by addTime desc";
		return (List<ViewWorkReportEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
}
