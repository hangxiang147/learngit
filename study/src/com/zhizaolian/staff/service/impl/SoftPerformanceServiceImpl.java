package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.FunctionEntity;
import com.zhizaolian.staff.entity.ProblemOrderEntity;
import com.zhizaolian.staff.entity.ProjectEntity;
import com.zhizaolian.staff.entity.ProjectModuleEntity;
import com.zhizaolian.staff.entity.ProjectVersionEntity;
import com.zhizaolian.staff.entity.RequirementEntity;
import com.zhizaolian.staff.entity.ScoreResultEntity;
import com.zhizaolian.staff.entity.SoftGroupEntity;
import com.zhizaolian.staff.entity.SubReqirementEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.SoftPerformanceScore;
import com.zhizaolian.staff.enums.SoftPosition;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.FunctionVo;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.ModuleVo;
import com.zhizaolian.staff.vo.PerformanceVo;
import com.zhizaolian.staff.vo.ProblemOrderVo;
import com.zhizaolian.staff.vo.ProjectVO;
import com.zhizaolian.staff.vo.RequirementVo;
import com.zhizaolian.staff.vo.ScoreResultVo;
import com.zhizaolian.staff.vo.SoftPerformanceTaskVO;
import com.zhizaolian.staff.vo.SoftPerformanceVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.SubRequirementVo;
import com.zhizaolian.staff.vo.VersionRequirementVo;
import com.zhizaolian.staff.vo.VersionVo;

import lombok.Cleanup;
import net.sf.json.JSONArray;

public class SoftPerformanceServiceImpl implements SoftPerformanceService{
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private IdentityService identityService;
	@Override
	public void commonSave(Object object) {
		baseDao.hqlSave(object);
	}


	@Override
	public String getReuirementPersonId(String requirementId) {
		String hql=" from  RequirementEntity  f where f.id='"+requirementId+"'";
		RequirementEntity re=(RequirementEntity) baseDao.hqlfindUniqueResult(hql);
		return re.getCreatorId();
	}


	@Override
	public void checkIsAllComplete(String requirementId) {
		String sql0="select count(*) from OA_SoftPerformanceFunction f where f.requirementId ='"+requirementId+"' and f.result="+TaskResultEnum.SOFT_CONFIRMSCOREEFFCTIVE.getValue()+" and f.isDelete=0 ";
		String sql1=" select count(*) from OA_SoftPerformanceSubRequirement s where s.requirementId='"+requirementId+"' and isDeleted='0' ";
		int count0=((BigInteger)baseDao.getUniqueResult(sql0)).intValue();
		int count1=((BigInteger)baseDao.getUniqueResult(sql1)).intValue();
		if(count0==count1){
			if(count0!=0){
				String sql=" update   OA_SoftPerformanceRequirement r set r.stage='"+Constants.COMPLETED+"' where r.id='"+requirementId+"' ";
				baseDao.excuteSql(sql);
			}
		}
	}




	@Override
	public void commonUpdate(Object object) {
		baseDao.hqlUpdate(object);
	}

	@Override
	public void commonHql(String hql) {
		baseDao.excuteHql(hql);
	}


	@Override
	public ListResult<SoftPerformanceTaskVO> findSoftPerformancesByUserGroupIDs(
			List<TaskDefKeyEnum> tasks, List<String> users, int page,
			int limit) {

		String taskNames = Arrays.toString(Lists2
				.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
					@Override
					protected String safeApply(TaskDefKeyEnum input) {
						return "'" + input.getName() + "'";
					}
				}).toArray());
		String userIDs = Arrays.toString(
				Lists2.transform(users, new SafeFunction<String, String>() {
					@Override
					protected String safeApply(String input) {
						return "'" + input + "'";
					}
				}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_,spf.addTime,spf.assignerName,spf.priority,spf.score,spf.estimatedTime,spf.deadline "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_SoftPerformanceFunction spf "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = spf.instanceId "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ") ";
		String sqlCount = "select COUNT(DISTINCT task.ID_) "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_SoftPerformanceFunction spf "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = spf.instanceId "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ") ";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		BigInteger value=(BigInteger)baseDao.getUniqueResult(sqlCount);
		List<SoftPerformanceTaskVO> resultList= new ArrayList<SoftPerformanceTaskVO>();
		if(CollectionUtils.isNotEmpty(result)){
			for (Object object : result) {
				Object[] objects=(Object[]) object;
				ProcessInstance pInstance = runtimeService
						.createProcessInstanceQuery()
						.processInstanceId((String) objects[1]).singleResult();
				SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(pInstance.getId(),
						"arg");
				SoftPerformanceTaskVO taskVO = new SoftPerformanceTaskVO();
				taskVO.setProcessInstanceID(pInstance.getId());
				taskVO.setRequestUserName(arg.getUserName());
				taskVO.setRequestDate(arg.getRequestDate());
				taskVO.setTaskID(objects[0]+"");
				taskVO.setTitle(arg.getTitle());
				taskVO.setScore(arg.getScore());
				taskVO.setLimitTime(arg.getDeadline()==null?"":DateUtil.formateDate(arg.getDeadline()));
				taskVO.setRequirementName(arg.getRequirementName());
				taskVO.setVersionName(arg.getVersionName());
				taskVO.setModuleName(arg.getModuleName());
				taskVO.setProjectName(arg.getProjectName());
				taskVO.setTaskName(objects[2]+"");
				taskVO.setScore(arg.getScore());
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pInstance.getProcessDefinitionId()).singleResult();
				taskVO.setBusinessKey(processDefinition.getKey());
				taskVO.setCreateTime(objects[4]==null?"":objects[4]+"");
				resultList.add(taskVO);
			}
		}
		return new ListResult<SoftPerformanceTaskVO>(resultList,value.intValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SoftGroupEntity> getSoftPersons(SoftPosition type, boolean isLeader) {
		String hqlList="from SoftGroupEntity s where s.type='"+type.name()+"' and s.isGroupLeader="+(isLeader?1:0)+" and s.isDeleted=0  order by s.sortIndex ";
		return (List<SoftGroupEntity>)baseDao.hqlfind(hqlList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SoftGroupEntity> getSoftPersonsAll(SoftPosition type) {
		String hqlList="from SoftGroupEntity s where s.type='"+type.name()+"'  and s.isDeleted=0  order by s.sortIndex ";
		return (List<SoftGroupEntity>)baseDao.hqlfind(hqlList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SoftGroupEntity> getSoftPersonsAll(SoftPosition type,
			String Project) {
		String hqlList="from SoftGroupEntity s where s.type='"+type.name()+"'  and s.project='"+Project+"' and s.isDeleted=0  order by s.sortIndex ";
		return (List<SoftGroupEntity>)baseDao.hqlfind(hqlList);
	}

	@Override
	public FunctionEntity geFunctionEntityByInstanceId(String instanceId) {
		String hqlQuery="from FunctionEntity f where f.instanceId='"+instanceId+"'";
		return (FunctionEntity) baseDao.hqlfindUniqueResult(hqlQuery);
	}



	@Override
	public List<Object> getScoreResult(String userId, String startTime,
			String endTime, String year, String month) {
		/*		String querySql = "SELECT\n" +
				"	(\n" +
				"		CASE\n" +
				"		WHEN s.taskId THEN\n" +
				"			'是'\n" +
				"		ELSE\n" +
				"			'否'\n" +
				"		END\n" +
				"	),\n" +
				"	f.`name`,\n" +
				"	p.`name` AS pname, s.duty,\n" +
				"	m.module,\n" +
				"	v.version,\n" +
				"	f.addTime,\n" +
				"	f.score,\n" +
				"	s.resultScore,\n" +
				"	s.itemDate,\n" +
				"	f.id,\n" +
				"	s.reason,\n" +
				"	s.taskId,\n" +
				"	f.instanceId\n" +
				"FROM\n" +
				"	OA_SoftPerformanceScoreResult s\n" +
				"LEFT JOIN OA_SoftPerformanceFunction f ON s.taskId = f.id\n" +
				"LEFT JOIN OA_SoftPerformanceModule m ON f.moduleId = m.id\n" +
				"LEFT JOIN OA_SoftPerformanceProject p ON f.projectId = p.id\n" +
				"LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"WHERE\n" +
				"	s.isDeleted = 0\n" +
				"AND (\n" +
				"  f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	OR (\n" +
				"		YEAR (s.itemDate) = "+year+"\n" +
				"		AND MONTH (s.itemDate) = "+month+"\n" +
				"		AND s.versionId IS NOT NULL\n" +
				"	)\n" +
				")\n"+
				"And s.userId='"+userId+"' "+
				"ORDER BY\n" +
				"	s.itemDate";*/

		String querySql = "SELECT\n" +
				"	*\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			(\n" +
				"				CASE\n" +
				"				WHEN s.taskId THEN\n" +
				"					'需求'\n" +
				"				ELSE\n" +
				"					'扣分'\n" +
				"				END\n" +
				"			),\n" +
				"			f.`name`,\n" +
				"			p.`name` AS pname,\n" +
				"			s.duty,\n" +
				"			m.module,\n" +
				"			v.version,\n" +
				"			f.addTime,\n" +
				"			f.score,\n" +
				"			s.resultScore,\n" +
				"			s.itemDate,\n" +
				"			f.id,\n" +
				"			s.reason,\n" +
				"			s.taskId,\n" +
				"			f.instanceId\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult s\n" +
				"		LEFT JOIN OA_SoftPerformanceFunction f ON s.taskId = f.id\n" +
				"		LEFT JOIN OA_SoftPerformanceModule m ON f.moduleId = m.id\n" +
				"		LEFT JOIN OA_SoftPerformanceProject p ON f.projectId = p.id\n" +
				"		LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"		WHERE\n" +
				"			s.isDeleted = 0\n" +
				"		AND s.problemTaskId IS NULL\n" +
				"		AND (\n" +
				"			f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	OR (\n" +
				"		YEAR (s.itemDate) = "+year+"\n" +
				"		AND MONTH (s.itemDate) = "+month+"\n" +
				"				AND s.versionId IS NOT NULL\n" +
				"			)\n" +
				"		)\n" +
				"And s.userId='"+userId+"' "+
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				'问题',\n" +
				"				problem.orderName,\n" +
				"				p.`name`,\n" +
				"				s.duty,\n" +
				"				'',\n" +
				"				v.version,\n" +
				"				problem.addTime,\n" +
				"				problem.score,\n" +
				"				s.resultScore,\n" +
				"				s.itemDate,\n" +
				"				problem.id,\n" +
				"				'',\n" +
				"				'',\n" +
				"				problem.processInstanceID\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult s,\n" +
				"				OA_ProblemOrder problem,\n" +
				"				OA_SoftPerformanceProject p,\n" +
				"				OA_SoftPerformanceVersion v\n" +
				"			WHERE\n" +
				"				s.isDeleted = 0\n" +
				"			AND s.problemTaskId = problem.id\n" +
				"			AND problem.projectId = p.id\n" +
				"			AND problem.projectVersionId = v.id\n" +
				"And s.userId='"+userId+"' "+
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	) allScore\n" +
				"ORDER BY\n" +
				"	allScore.itemDate";
		return baseDao.findBySql(querySql);
	}
	@Override
	public ListResult<Object> getScoreResult(String year, String month,
			int page, int limit) {
		/*		String sql = "SELECT\n" +
				"	s.StaffName,\n" +
				"	s.UserID,\n" +
				"	sum(sc.resultScore)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceScoreResult sc\n" +
				"LEFT JOIN OA_SoftPerformanceFunction f ON sc.taskId = f.id\n" +
				"LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"LEFT JOIN OA_Staff s ON sc.userId = s.UserID\n" +
				"WHERE\n" +
				"sc.isDeleted=0 " +
				"AND (\n" +
				"  f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	OR (\n" +
				"		YEAR (sc.itemDate) = "+year+"\n" +
				"		AND MONTH (sc.itemDate) = "+month+"\n" +
				"		AND sc.versionId IS NOT NULL\n" +
				"	)\n" +
				")\n"+
				"GROUP BY\n" +
				"	s.StaffName,\n" +
				"	s.UserID";*/
		String sql = "SELECT\n" +
				"	StaffName,\n" +
				"	score.UserID,\n" +
				"	sum(resultScore)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			sc.UserID,\n" +
				"			sc.resultScore\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult sc\n" +
				"		LEFT JOIN OA_SoftPerformanceFunction f ON sc.taskId = f.id\n" +
				"		LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"		WHERE\n" +
				"			sc.isDeleted = 0\n" +
				"		AND (\n" +
				"			f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"			OR (\n" +
				"		YEAR (sc.itemDate) = "+year+"\n" +
				"		AND MONTH (sc.itemDate) = "+month+"\n" +
				"				AND sc.versionId IS NOT NULL\n" +
				"			)\n" +
				"		)\n" +
				"		AND sc.problemTaskId IS NULL\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				sc.userId,\n" +
				"				sc.resultScore\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult sc,\n" +
				"				OA_ProblemOrder p,\n" +
				"				OA_SoftPerformanceVersion v\n" +
				"			WHERE\n" +
				"				sc.isDeleted = 0\n" +
				"			AND sc.problemTaskId = p.id\n" +
				"			AND p.projectVersionId = v.id\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	) score,\n" +
				"	OA_Staff staff\n" +
				"WHERE\n" +
				"	score.UserID = staff.UserID\n" +
				"GROUP BY\n" +
				"	staff.StaffName,\n" +
				"	staff.UserID";
		List<Object> result=baseDao.findPageList(sql, page, limit);
		/*		String countSql="select count(*) from(SELECT\n" +
				"	s.StaffName,\n" +
				"	s.UserID,\n" +
				"	sum(sc.resultScore)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceScoreResult sc\n" +
				"LEFT JOIN OA_SoftPerformanceFunction f ON sc.taskId = f.id\n" +
				"LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"LEFT JOIN OA_Staff s ON sc.userId = s.UserID\n" +
				"WHERE\n" +
				"sc.isDeleted=0 " +
				"AND (\n" +
				"  f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	OR (\n" +
				"		YEAR (sc.itemDate) = "+year+"\n" +
				"		AND MONTH (sc.itemDate) = "+month+"\n" +
				"		AND sc.versionId IS NOT NULL\n" +
				"	)\n" +
				")\n"+
				"GROUP BY\n" +
				"	s.StaffName,\n" +
				"	s.UserID)a";*/
		String countSql = "select count(*) from(SELECT\n" +
				"	StaffName,\n" +
				"	score.UserID,\n" +
				"	sum(resultScore)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			sc.UserID,\n" +
				"			sc.resultScore\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult sc\n" +
				"		LEFT JOIN OA_SoftPerformanceFunction f ON sc.taskId = f.id\n" +
				"		LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"		WHERE\n" +
				"			sc.isDeleted = 0\n" +
				"		AND (\n" +
				"			f.isDelete = 0\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"			OR (\n" +
				"		YEAR (sc.itemDate) = "+year+"\n" +
				"		AND MONTH (sc.itemDate) = "+month+"\n" +
				"				AND sc.versionId IS NOT NULL\n" +
				"			)\n" +
				"		)\n" +
				"		AND sc.problemTaskId IS NULL\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				sc.userId,\n" +
				"				sc.resultScore\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult sc,\n" +
				"				OA_ProblemOrder p,\n" +
				"				OA_SoftPerformanceVersion v\n" +
				"			WHERE\n" +
				"				sc.isDeleted = 0\n" +
				"			AND sc.problemTaskId = p.id\n" +
				"			AND p.projectVersionId = v.id\n" +
				"	AND YEAR (v.endDate) = "+year+"\n" +
				"	AND MONTH (v.endDate) = "+month+"\n" +
				"	) score,\n" +
				"	OA_Staff staff\n" +
				"WHERE\n" +
				"	score.UserID = staff.UserID\n" +
				"GROUP BY\n" +
				"	staff.StaffName,\n" +
				"	staff.UserID)a";
		int number=((BigInteger)baseDao.getUniqueResult(countSql)).intValue();
		return new ListResult<>(result, number);
	}

	@Override
	public List<Object> getScoreResultByMouth(String userId,
			String year) {
		if (StringUtils.isBlank(year)||StringUtils.isBlank(userId)) {
			throw new IllegalArgumentException();
		}
		/*String sql = "SELECT\n" +
				"	st.staffName,\n" +
				"	sum(resultScore),\n" +
				"   IFNULL(MONTH(v.endDate),MONTH(s.itemDate))  month_ \n" +
				"   FROM\n" +
				"	OA_SoftPerformanceScoreResult s left join \n" +
				"	OA_Staff st \n" +
				"on\n" +
				"	s.userId=st.UserID\n" +
				"LEFT JOIN\n" +
				"	OA_SoftPerformanceFunction f\n" +
				"on \n" +
				"	s.taskId =f.id\n" +
				"LEFT JOIN\n" +
				"	OA_SoftPerformanceVersion v\n" +
				"ON \n" +
				"	f.projectVersionId = v.id\n" +
				"where\n" +
				" s.userId='"+userId+"' \n" +
				" and  YEAR(s.itemDate)='"+year+"'\n" +
				"and s.isDeleted=0\n" +
				"GROUP BY\n" +
				"	month_\n" +
				"order by month_ ";*/
		String sql = "SELECT\n" +
				"	StaffName,\n" +
				"	sum(resultScore),\n" +
				"	month_\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			s.userId,\n" +
				"			resultScore,\n" +
				"			IFNULL(\n" +
				"				MONTH (v.endDate),\n" +
				"				MONTH (s.itemDate)\n" +
				"			) month_\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult s\n" +
				"		LEFT JOIN OA_SoftPerformanceFunction f ON s.taskId = f.id\n" +
				"		LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"		WHERE\n" +
				" s.userId='"+userId+"' \n" +
				" and  YEAR(IFNULL(v.endDate,s.itemDate))='"+year+"'\n" +
				"		AND s.isDeleted = 0 AND s.problemTaskId is NULL\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				s.userId,\n" +
				"				s.resultScore,\n" +
				"				MONTH (v.endDate) month_\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult s,\n" +
				"				OA_ProblemOrder p,\n" +
				"				OA_SoftPerformanceVersion v\n" +
				"			WHERE\n" +
				"				s.problemTaskId = p.id\n" +
				"			AND p.projectVersionId = v.id\n" +
				"			AND s.isDeleted = 0\n" +
				" and  YEAR(IFNULL(v.endDate,s.itemDate))='"+year+"'\n" +
				" and s.userId='"+userId+"' \n" +
				"	) score, OA_Staff staff\n" +
				"where score.userId = staff.UserID\n" +
				"GROUP BY\n" +
				"	month_\n" +
				"ORDER BY\n" +
				"	month_";
		return baseDao.findBySql(sql);
	}

	@Override
	public void updateProcessStatus(TaskResultEnum taskResultEnum,
			String instanceId) {
		String hql="update FunctionEntity s set s.result="+taskResultEnum.getValue()+" where s.instanceId='"+instanceId+"' ";
		baseDao.excuteHql(hql);
	}

	@Override
	public ListResult<SoftGroupEntity> getSoftGroupUsers(int page, int limit, String staffId, String personType, String projectName) {
		String hqlList = "from SoftGroupEntity s where s.isDeleted=0";
		if(StringUtils.isNotBlank(staffId)){
			hqlList += " and userId='"+staffId+"'";
		}
		if(StringUtils.isNotBlank(personType)){
			hqlList += " and type='"+personType+"'";
		}
		if(StringUtils.isNotBlank(projectName)){
			hqlList += " and project='"+projectName+"'";
		}
		hqlList += "order by s.type,s.sortIndex";
		Object objLst = baseDao.hqlPagedFind(hqlList,page,limit);
		@SuppressWarnings("unchecked")
		List<SoftGroupEntity> softGroups = (List<SoftGroupEntity>)objLst;
		String hqlCount ="select count(*) from SoftGroupEntity s where s.isDeleted=0 ";
		if(StringUtils.isNotBlank(staffId)){
			hqlCount += " and userId='"+staffId+"'";
		}
		if(StringUtils.isNotBlank(personType)){
			hqlCount += " and type='"+personType+"'";
		}
		if(StringUtils.isNotBlank(projectName)){
			hqlCount += " and project='"+projectName+"'";
		}
		Integer count = ((Long) baseDao.hqlfindUniqueResult(hqlCount))
				.intValue();
		return new ListResult<SoftGroupEntity>(softGroups, count);
	}

	@Override
	public SoftGroupEntity getSoftGroupById(Integer id) {
		String hql="from SoftGroupEntity s where s.id="+id;
		return (SoftGroupEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public List<ProjectVO> getProjectLst() {
		List<ProjectVO> projectVOLst = new ArrayList<>();
		String hqlLst = "from ProjectEntity where isDelete=0 order by addTime desc";
		Object objLst = baseDao.hqlfind(hqlLst);
		@SuppressWarnings("unchecked")
		List<ProjectEntity> projectLst = (List<ProjectEntity>)objLst;
		for(ProjectEntity projectEntity: projectLst){
			String sql = "select StaffName from OA_Staff where UserID = '"+projectEntity.getProjectHeader()+"'";
			Object obj = baseDao.getUniqueResult(sql);
			String projectHeaderName = (String)obj;
			sql = "select staffName from OA_Staff where UserID = '"+projectEntity.getTestHeader()+"'";
			obj = baseDao.getUniqueResult(sql);
			String testHeaderName = (String)obj;
			projectVOLst.add(ProjectVO.builder().addTime(projectEntity.getAddTime()).code(projectEntity.getCode())
					.creatorId(projectEntity.getCreatorId()).description(projectEntity.getDescription())
					.id(projectEntity.getId()).isDelete(projectEntity.getIsDelete()).name(projectEntity.getName())
					.projectHeaderId(projectEntity.getProjectHeader()).projectHeaderName(projectHeaderName)
					.testHeaderId(projectEntity.getTestHeader()).testHeaderName(testHeaderName).updatestVersion(projectEntity.getUpdatestVersion())
					.updateTime(projectEntity.getUpdateTime())
					.ss(projectEntity.getSs())
					.zz(projectEntity.getZz())
					.kf(projectEntity.getKf())
					.jl(projectEntity.getJl())
					.xq(projectEntity.getXq())
					.cs(projectEntity.getCs())
					.build());
		}
		return projectVOLst;
	}

	@Override
	public void addProject(ProjectVO project) throws Exception {
		project.setAddTime(new Date());
		project.setUpdateTime(new Date());
		project.setIsDelete(0);
		ProjectEntity projectEntity = ProjectEntity.builder().addTime(project.getAddTime()).code(project.getCode())
				.creatorId(project.getCreatorId()).description(project.getDescription())
				.id(project.getId()).isDelete(project.getIsDelete()).name(project.getName())
				.projectHeader(project.getProjectHeaderId()).testHeader(project.getTestHeaderId())
				.updatestVersion(project.getUpdatestVersion())
				.updateTime(project.getUpdateTime())
				.ss(project.getSs())
				.zz(project.getZz())
				.kf(project.getKf())
				.jl(project.getJl())
				.xq(project.getXq())
				.cs(project.getCs())
				.build();
		baseDao.hqlSave(projectEntity);
	}

	@Override
	public void deleteProject(String id) throws Exception {
		ProjectEntity projectEntity = (ProjectEntity) baseDao
				.hqlfindUniqueResult("from ProjectEntity where id=" + id);
		projectEntity.setIsDelete(1);
		baseDao.hqlUpdate(projectEntity);
	}

	public ProjectVO getProject(String id) {
		ProjectEntity projectEntity = (ProjectEntity) baseDao
				.hqlfindUniqueResult("from ProjectEntity where id=" + id);
		String sql = "select StaffName from OA_Staff where UserID = '"+projectEntity.getProjectHeader()+"'";
		Object obj = baseDao.getUniqueResult(sql);
		String projectHeaderName = (String)obj;
		sql = "select staffName from OA_Staff where UserID = '"+projectEntity.getTestHeader()+"'";
		obj = baseDao.getUniqueResult(sql);
		String testHeaderName = (String)obj;
		return ProjectVO.builder().addTime(projectEntity.getAddTime()).code(projectEntity.getCode())
				.creatorId(projectEntity.getCreatorId()).description(projectEntity.getDescription())
				.id(projectEntity.getId()).isDelete(projectEntity.getIsDelete()).name(projectEntity.getName())
				.projectHeaderId(projectEntity.getProjectHeader()).projectHeaderName(projectHeaderName)
				.testHeaderId(projectEntity.getTestHeader()).testHeaderName(testHeaderName).updatestVersion(projectEntity.getUpdatestVersion())
				.updateTime(projectEntity.getUpdateTime())
				.ss(projectEntity.getSs())
				.zz(projectEntity.getZz())
				.kf(projectEntity.getKf())
				.jl(projectEntity.getJl())
				.xq(projectEntity.getXq())
				.cs(projectEntity.getCs())
				.build();
	}

	public void updateProject(ProjectVO project) throws Exception {
		project.setUpdateTime(new Date());
		ProjectEntity projectEntity = ProjectEntity.builder().addTime(project.getAddTime()).code(project.getCode())
				.creatorId(project.getCreatorId()).description(project.getDescription())
				.id(project.getId()).isDelete(project.getIsDelete()).name(project.getName())
				.projectHeader(project.getProjectHeaderId()).testHeader(project.getTestHeaderId())
				.updatestVersion(project.getUpdatestVersion())
				.updateTime(project.getUpdateTime())
				.ss(project.getSs())
				.zz(project.getZz())
				.kf(project.getKf())
				.jl(project.getJl())
				.xq(project.getXq())
				.cs(project.getCs())
				.build();
		baseDao.hqlUpdate(projectEntity);
	}
	@Override
	public void addProjectVersion(ProjectVersionEntity version) throws Exception {
		version.setAddTime(new Date());
		version.setUpdateTime(new Date());
		version.setIsDelete(0);
		baseDao.hqlSave(version);
		//更新项目的最新版本
		updateProjectUpdatestVersion(version.getProjectId()+"", version.getVersion());
	}

	@Override
	public void updateProjectVersion(ProjectVersionEntity version) throws Exception {
		version.setUpdateTime(new Date());
		String sql = "update OA_SoftPerformanceVersion version set version.version='"
				+EscapeUtil.decodeSpecialChars(version.getVersion())+"', version.beginDate='"+version.getBeginDate()+"',"
				+ "version.developerNum="+version.getDeveloperNum()+",version.endDate='"+version.getEndDate()+"',"
				+ "version.developers='"+version.getDevelopers()+"',version.fenXis='"+version.getFenXis()+"',"
				+ "version.pms='"+version.getPms()+"',version.shiShis='"+version.getShiShis()+"',"
				+ "version.testers='"+version.getTesters()+"',"
				+ "version.workHour='"+version.getWorkHour()+"' where version.id="+version.getId();
		baseDao.excuteSql(sql);
		//更新项目的最新版本
		updateProjectUpdatestVersion(version.getProjectId()+"", version.getVersion());
	}

	@Override
	public boolean isExistProjectVersion(String version, String projectId, String id) throws Exception {
		String sql = "select id from OA_SoftPerformanceVersion where projectId="+projectId
				+" and version='"+EscapeUtil.decodeSpecialChars(version)+"' and isDelete=0";
		if(StringUtils.isNotBlank(id)){
			sql += " and id!="+id;
		}
		Object obj = baseDao.getUniqueResult(sql);
		if(null != obj){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectVersionEntity> getProjectVersionLst(String projectId) {
		String hql = "from ProjectVersionEntity version where isDelete=0 and projectId="+projectId
				+" order by addTime desc";
		return (List<ProjectVersionEntity>)baseDao.hqlfind(hql);
	}

	@Override
	public ProjectVersionEntity getProjectVersion(String id) {
		String hql = "from ProjectVersionEntity where id="+id;
		return (ProjectVersionEntity)baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void deleteProjectVersion(String id) throws Exception {
		String hql = "from ProjectVersionEntity where id="+id;
		ProjectVersionEntity versionEntity = (ProjectVersionEntity)baseDao.hqlfindUniqueResult(hql);
		versionEntity.setIsDelete(1);
		baseDao.hqlUpdate(versionEntity);
	}

	@Override
	public void updateProjectUpdatestVersion(String id, String version) throws Exception {
		String sql = "update OA_SoftPerformanceProject set updatestVersion='"+EscapeUtil.decodeSpecialChars(version)+"' where id="+id;
		baseDao.excuteSql(sql);
	}

	@Override
	public void addProjectModule(ProjectModuleEntity module) throws Exception {
		module.setAddTime(new Date());
		module.setUpdateTime(new Date());
		module.setIsDelete(0);
		baseDao.hqlSave(module);

	}

	@Override
	public void updateProjectModule(ProjectModuleEntity module) throws Exception {
		module.setUpdateTime(new Date());
		String sql = "update OA_SoftPerformanceModule module set module.module='"+
				EscapeUtil.decodeSpecialChars(module.getModule())+"' where module.id="+module.getId();
		baseDao.excuteSql(sql);

	}

	@Override
	public boolean isExistProjectModule(String module, String projectId) throws Exception {
		String sql = "select id from OA_SoftPerformanceModule where projectId="+
				projectId+" and module='"+EscapeUtil.decodeSpecialChars(module)+"' and isDelete=0";
		Object obj = baseDao.getUniqueResult(sql);
		if(null != obj){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectModuleEntity> getProjectModuleLst(String projectId) {
		String hql = "from ProjectModuleEntity where isDelete=0 and projectId="+projectId+" order by addTime desc";
		return (List<ProjectModuleEntity>)baseDao.hqlfind(hql);
	}

	@Override
	public ProjectModuleEntity getProjectModule(String id) {
		String hql = "from ProjectModuleEntity where id="+id;
		return (ProjectModuleEntity)baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void deleteProjectModule(String id) throws Exception {
		String hql = "from ProjectModuleEntity where id="+id;
		ProjectModuleEntity moduleEntity = (ProjectModuleEntity)baseDao.hqlfindUniqueResult(hql);
		moduleEntity.setIsDelete(1);
		baseDao.hqlUpdate(moduleEntity);

	}

	@Override
	public Map<String, String> getProjectVersionsMap(String userId, boolean requireManage) {
		String sql = "SELECT\n"+
				"project.`name`,\n"+
				"GROUP_CONCAT(version order by version.addTime desc),\n"+
				"GROUP_CONCAT(version.id order by version.addTime desc),\n"+
				"GROUP_CONCAT(IFNULL(STATUS, '0') order by version.addTime desc)\n"+
				"FROM\n"+
				"OA_SoftPerformanceVersion version\n"+
				"INNER JOIN OA_SoftPerformanceProject project ON version.projectId = project.id\n"+
				"WHERE\n"+
				"project.isDelete = 0\n"+
				"AND version.isDelete = 0\n";
		if(!requireManage){
			sql += "and version.status is null\n";
		}
		sql +=  "GROUP BY\n"+
				"version.projectId\n"+
				"order by\n"+
				"version.addTime desc";
		List<Object> objLst = baseDao.findBySql(sql);
		Map<String, String> projectAndVersionsMap = new HashMap<String, String>();
		for(Object obj: objLst){
			Object[] objs = (Object[]) obj;
			if(null != userId){
				sql = "select sum(type='产品经理') from OA_SoftGroup where project='"+(String)objs[0]+"' and userId='"+userId+"' and isDeleted=0";
				Object countObj = baseDao.getUniqueResult(sql);
				int count = Integer.parseInt(countObj==null ? "0":countObj+"");
				if(count>0){
					projectAndVersionsMap.put((String)objs[0], (String)objs[1]+"#@@#"+(String)objs[2]+"#@@#"+(String)objs[3]+"#@@#"+"产品经理");
				}else{
					sql = "select sum(type='需求分析') from OA_SoftGroup where project='"+(String)objs[0]+"' and userId='"+userId+"' and isDeleted=0";
					countObj = baseDao.getUniqueResult(sql);
					count = Integer.parseInt(countObj==null ? "0":countObj+"");
					if(count>0){
						projectAndVersionsMap.put((String)objs[0], (String)objs[1]+"#@@#"+(String)objs[2]+"#@@#"+(String)objs[3]+"#@@#"+"需求分析");
					}else{
						projectAndVersionsMap.put((String)objs[0], (String)objs[1]+"#@@#"+(String)objs[2]+"#@@#"+(String)objs[3]+"#@@#"+"其他");
					}
				}
			}else{
				projectAndVersionsMap.put((String)objs[0], (String)objs[1]+"#@@#"+(String)objs[2]+"#@@#"+(String)objs[3]);
			}
		}
		return projectAndVersionsMap;
	}

	public void addRequirement(RequirementEntity requirement, 
			File[] files, String[] fileNames) throws Exception {
		requirement.setAddTime(new Date());
		requirement.setUpdateTime(new Date());
		requirement.setIsDelete(0);

		if(null != files && files.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<files.length; i++){
				String fileName = fileNames[i];
				File destFile = new File(Constants.PRODUCT_FILE_DIRECTORY, UUID.randomUUID()+"_"+fileName);
				FileUtils.copyFile(files[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			requirement.setAttachmentName(attachmentNames);
			requirement.setAttachmentPath(attachmentPaths);
		}
		baseDao.hqlSave(requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RequirementEntity> getRequirements(Integer versionId, Integer moduleId) {
		String hql = "from RequirementEntity where isDelete=0 and (stage!='"+Constants.COMPLETED+"' or stage is null) and moduleId="+moduleId+" and projectVersionId="+versionId;
		return (List<RequirementEntity>)baseDao.hqlfind(hql);
	}

	@Override
	public void addFunction(FunctionEntity function) throws Exception {
		function.setAddTime(new Date());
		function.setIsDelete(0);
		baseDao.hqlSave(function);

	}

	@Override
	public ListResult<FunctionVo> getFuntions(int limit, int page, String userId) {
		String sqlLst = "select a.id,a.`name`,a.priority,a.taskType,a.assignerName,a.estimatedTime,"
				+ "a.score,b.`name` associatedRequirement,a.result,a.instanceId,a.addTime,v.version "+ 
				"from OA_SoftPerformanceFunction a inner join OA_SoftPerformanceRequirement b on a.requirementId=b.id "
				+ "inner join OA_SoftPerformanceVersion v ON b.projectVersionId = v.id where a.isDelete=0";
		if(null!=userId){
			sqlLst += " and a.creatorId='"+userId+"'";
		}
		sqlLst += " order by a.addTime desc";
		String sqlCount = "select count(id) from FunctionEntity where isDelete=0";
		if(null!=userId){
			sqlCount += " and creatorId='"+userId+"'";
		}
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = ((Long)baseDao.hqlfindUniqueResult(sqlCount)).intValue();
		List<FunctionVo> taskLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			FunctionVo task = new FunctionVo();
			task.setId(objs[0]+"");
			task.setName(objs[1]+"");
			task.setPriority(objs[2]+"");
			task.setTaskType(objs[3]+"");
			task.setAssigner(objs[4]+"");
			task.setEstimatedTime(objs[5]+"");
			task.setScore(objs[6]+"");
			task.setAssociatedRequirement(objs[7]+"");
			task.setResult(objs[8]+"");
			task.setInstanceId(objs[9]+"");
			task.setAddTime(objs[10]+"");
			task.setVersion(objs[11]+"");
			taskLst.add(task);
		}
		return new ListResult<FunctionVo>(taskLst, count);
	}

	@Override
	public void addFunctionVo(SoftPerformanceVo softPerformanceVo, File[] files,
			String fileDetail) {
		softPerformanceVo.setBusinessType(BusinessTypeEnum.SOFTPERFORMANCE.getName());
		softPerformanceVo.setTitle(softPerformanceVo.getName() );


		//获取 分值配比

		String hql=" from  ProjectEntity  f where f.id='"+softPerformanceVo.getProjectId()+"'";
		ProjectEntity projectEntity=(com.zhizaolian.staff.entity.ProjectEntity) baseDao.hqlfindUniqueResult(hql);
		softPerformanceVo.setXQpercent(projectEntity.getXq());
		softPerformanceVo.setJLPercent(projectEntity.getJl());
		softPerformanceVo.setKFPercent(projectEntity.getKf());
		softPerformanceVo.setSSpercent(projectEntity.getSs());
		softPerformanceVo.setZZpercent(projectEntity.getZz());
		softPerformanceVo.setCSpercent(projectEntity.getCs());		
		List<GroupDetailVO> groups=staffService.findGroupDetailsByUserID(softPerformanceVo.getUserID());
		if(CollectionUtils.isNotEmpty(groups)){
			GroupDetailVO g0=groups.get(0);
			softPerformanceVo.setUserName(softPerformanceVo.getUserName()+"("+g0.getCompanyName()+"-"+g0.getDepartmentName()+"-"+g0.getPositionName()+")");
		}
		Map<String, Object> vars = new HashMap<>();
		softPerformanceVo.setRequestDate(DateUtil.formateFullDate(new Date()));
		vars.put("arg", softPerformanceVo);
		List<SoftGroupEntity>  cs=getSoftPersonsAll((SoftPosition.软件测试));
		List<SoftGroupEntity>  ss=getSoftPersonsAll((SoftPosition.实施));

		//项目经理是根据项目名称设置的
		List<SoftGroupEntity> jl=getSoftPersonsAll(SoftPosition.产品经理,softPerformanceVo.getProjectName());
		if(CollectionUtils.isEmpty(jl)){
			jl=getSoftPersons(SoftPosition.产品经理,true);
		}
		Function<List<SoftGroupEntity>, List<String>> getUserId=new Function<List<SoftGroupEntity>, List<String>>() {
			@Override
			public List<String> apply(List<SoftGroupEntity> arg0) {
				List<String> result=new ArrayList<String>();
				if(CollectionUtils.isNotEmpty(arg0)){
					for (SoftGroupEntity softGroupEntity : arg0) {
						result.add(softGroupEntity.getUserId());
					}
				}
				return result;
			}
		};
		if(CollectionUtils.isEmpty(cs)){
			throw new RuntimeException("测试人员缺失,请对相应人员进行维护");
		}
		if(CollectionUtils.isEmpty(ss)){
			throw new RuntimeException("实施人员缺失,请对相应人员进行维护");
		}
		if(CollectionUtils.isEmpty(jl)){
			throw new RuntimeException("产品经理缺失,请对相应人员进行维护");
		}
		vars.put("checkPersons", getUserId.apply(cs));
		vars.put("ssPersons", getUserId.apply(ss));
		vars.put("groupLeader", softPerformanceVo.getCreatorId());
		vars.put("assignee", softPerformanceVo.getAssignerId());
		//最终由产品经理审核
		vars.put("confirmLeader",jl.get(0).getUserId());
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("SoftPerformance");
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		int index = -1;
		try {
			if (files != null && files.length > 0) {
				@SuppressWarnings("unchecked")
				List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
				int i = 0;
				for (Object o : fileDetailList) {
					index++;
					InputStream is = new FileInputStream(files[i]);
					JSONArray jArray = (JSONArray) o;
					String fileName = (String) jArray.get(0);
					if (StringUtils.isBlank(fileName))
						continue;
					String suffix = (String) jArray.get(1);
					if ("jpg".equals(suffix) || "jpeg".equals(suffix)
							|| "png".equals(suffix)) {
						taskService.createAttachment("picture", task.getId(),
								processInstance.getId(), fileName, "" + index,
								is);
					} else {
						taskService.createAttachment(suffix, task.getId(),
								processInstance.getId(), fileName, "" + index,
								is);
					}
					i++;
				}
			}
			//继承需求细分的附件
			Integer subRequireId = softPerformanceVo.getSubRequirementId();
			Map<String, File> fileMap = getSubRequireAttachments(subRequireId);
			Iterator<Entry<String, File>> ite = fileMap.entrySet().iterator();
			index = -1;
			while(ite.hasNext()){
				index++;
				Entry<String, File> entry = ite.next();
				String fileName = entry.getKey();
				String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
				File file = entry.getValue();
				InputStream is = new FileInputStream(file);
				if ("jpg".equals(suffix) || "jpeg".equals(suffix)
						|| "png".equals(suffix)) {
					taskService.createAttachment("picture", task.getId(),
							processInstance.getId(), fileName, "" + index,
							is);
				} else {
					taskService.createAttachment(suffix, task.getId(),
							processInstance.getId(), fileName, "" + index,
							is);
				}
			}


		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		taskService.setAssignee(task.getId(), softPerformanceVo.getCreatorId());
		taskService.complete(task.getId(), vars);
		FunctionEntity functionEntity=(FunctionEntity) CopyUtil.tryToEntity(softPerformanceVo, FunctionEntity.class); 
		try {
			functionEntity.setInstanceId(processInstance.getId());
			functionEntity.setIsDelete(0);
			functionEntity.setAddTime(new Date());
			baseDao.hqlSave(functionEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public ListResult<RequirementVo> getDivideRequirementLst(int limit, int page, String versionId, String status,String number,String chooseP,String requireName)
			throws Exception{
		/*		String sqlLst = "select requirement.id, requirement.priority, requirement.`name`, module.module,\n"+
				"staff.StaffName creator, requirement.`status`, requirement.stage,\n"+
				"COUNT(`function`.id) taskNum,requirement.divide \n"+
				"from OA_SoftPerformanceRequirement requirement \n"+
				"left join (select * from OA_SoftPerformanceFunction where isDelete=0) `function` \n"+
				"on requirement.id = `function`.requirementId \n"+
				"inner join OA_SoftPerformanceModule module \n"+
				"on requirement.moduleId = module.id \n"+
				"inner join OA_Staff staff \n"+
				"on requirement.creatorId = staff.UserID \n"+
				"where requirement.projectVersionId="+versionId+" and requirement.isDelete=0\n";*/
		String sqlLst = "SELECT\n" +
				"	requirement.id,\n" +
				"	requirement.priority,\n" +
				"	requirement.`name`,\n" +
				"	module.module,\n" +
				"	staff.StaffName creator,\n" +
				"	requirement.`status`,\n" +
				"	requirement.stage,\n" +
				" COUNT(DISTINCT(`function`.id)) taskNum,"+
				"  	COUNT(DISTINCT(subrequirement.id)) sumNum,"+
				" requirement.divide "+
				"FROM\n" +
				"	OA_SoftPerformanceRequirement requirement\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		OA_SoftPerformanceFunction\n" +
				"	WHERE\n" +
				"				isDelete = 0 and ((result!='6' and result!='31' and result !='29')  or (result is null )) \n" +
				") `function` ON requirement.id = `function`.requirementId\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		OA_SoftPerformanceSubRequirement\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				") `subrequirement` ON subrequirement.requirementId = requirement.id\n" +
				"INNER JOIN OA_SoftPerformanceModule module ON requirement.moduleId = module.id\n" +
				"INNER JOIN OA_Staff staff ON requirement.creatorId = staff.UserID\n" +
				"WHERE\n" +
				"	requirement.projectVersionId = "+versionId+"\n" +
				"AND requirement.isDelete = 0\n";
		if(StringUtils.isNotBlank(status)){
			if("3".equals(status)){
				sqlLst += "and requirement.status='"+Constants.DELETE+"'\n";
			}else if("1".equals(status)){
				sqlLst += "and requirement.divide=1 and requirement.status!='"+Constants.DELETE+"'\n";
			}else if("2".equals(status)){
				sqlLst += "and requirement.divide=0 and requirement.status!='"+Constants.DELETE+"'\n";
			}
		}
//		number =  chooseP = requireName
		if(StringUtils.isNotBlank(number)){
			sqlLst +=" AND requirement.id = "+number+"\n";
		}
		if(StringUtils.isNotBlank(requireName)){
			sqlLst += " AND requirement.`name` LIKE '%"+requireName+"%'\n";
		}
		if(StringUtils.isNotBlank(chooseP)){
			sqlLst += " AND requirement.priority = '"+chooseP+"'\n";
		}
		
		sqlLst += "group by requirement.id";
		
		String sqlCount = "select COUNT(id) from ( "+sqlLst+" ) a";
		/*String sqlCount = "select COUNT(id) from OA_SoftPerformanceRequirement requirement where requirement.isDelete=0 and requirement.projectVersionId="+versionId;
		if(StringUtils.isNotBlank(status)){
			if("3".equals(status)){
				sqlCount += " and requirement.status='"+Constants.DELETE+"'";
			}else if("1".equals(status)){
				sqlCount += " and requirement.divide=1";
			}else if("2".equals(status)){
				sqlCount += " and requirement.divide=0";
			}
		}*/
		
		if(StringUtils.isNotBlank(number)){
			sqlCount +=" AND requirement.id = "+number+"\n";
		}
		if(StringUtils.isNotBlank(requireName)){
			sqlCount += " AND requirement.`name` LIKE '%"+requireName+"%'\n";
		}
		if(StringUtils.isNotBlank(chooseP)){
			sqlCount += " AND requirement.priority = '"+chooseP+"'\n";
		}
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		List<RequirementVo> requirementLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			RequirementVo requirement = new RequirementVo();
			requirement.setId(objs[0]+"");
			requirement.setPriority(objs[1]+"");
			requirement.setName(objs[2]+"");
			requirement.setModule(objs[3]+"");
			requirement.setCreator(objs[4]+"");
			requirement.setStatus(objs[5]+"");
			requirement.setStage(objs[6]+"");
			requirement.setTaskNum(objs[7]+"");
			requirement.setSumNum(objs[8]+"");
			requirement.setDivide(objs[9]+"");
			requirementLst.add(requirement);
		}
		return new ListResult<RequirementVo>(requirementLst, count);
	}
	@Override
	public ListResult<RequirementVo> getRequirementLst(int limit, int page, String versionId) throws Exception{
		String sqlLst = "SELECT\n" +
				"	requirement.id,\n" +
				"	requirement.priority,\n" +
				"	requirement.`name`,\n" +
				"	module.module,\n" +
				"	staff.StaffName creator,\n" +
				"	requirement.`status`,\n" +
				"	requirement.stage,\n" +
				" COUNT(DISTINCT(`function`.id)) taskNum,"+
				"  	COUNT(DISTINCT(subrequirement.id)) sumNum,"+
				" requirement.divide "+
				"FROM\n" +
				"	OA_SoftPerformanceRequirement requirement\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		OA_SoftPerformanceFunction\n" +
				"	WHERE\n" +
				"				isDelete = 0 and ((result!='6' and result!='31' and result !='29')  or (result is null )) \n" +
				") `function` ON requirement.id = `function`.requirementId\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		OA_SoftPerformanceSubRequirement\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				") `subrequirement` ON subrequirement.requirementId = requirement.id\n" +
				"INNER JOIN OA_SoftPerformanceModule module ON requirement.moduleId = module.id\n" +
				"INNER JOIN OA_Staff staff ON requirement.creatorId = staff.UserID\n" +
				"WHERE\n" +
				"	requirement.projectVersionId = "+versionId+"\n" +
				"AND requirement.isDelete = 0\n" +
				"AND requirement.`status`!='"+Constants.DELETE+"'\n"+
				"GROUP BY\n" +
				"	requirement.id";
		String sqlCount = "select COUNT(id) from OA_SoftPerformanceRequirement requirement where requirement.isDelete=0 and requirement.projectVersionId="+versionId+" AND requirement.`status`!='"+Constants.DELETE+"'";
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		List<RequirementVo> requirementLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			RequirementVo requirement = new RequirementVo();
			requirement.setId(objs[0]+"");
			requirement.setPriority(objs[1]+"");
			requirement.setName(objs[2]+"");
			requirement.setModule(objs[3]+"");
			requirement.setCreator(objs[4]+"");
			requirement.setStatus(objs[5]+"");
			requirement.setStage(objs[6]+"");
			requirement.setTaskNum(objs[7]+"");
			requirement.setSumNum(objs[8]+"");;
			requirement.setState(objs[9]==null?null:(objs[9]+""));
			requirementLst.add(requirement);
		}
		return new ListResult<RequirementVo>(requirementLst, count);
	}
	@Override
	public RequirementVo getRequirementDetail(String requirementId) {
		String sql = "SELECT\n" +
				"	requirement.priority,\n" +
				"	requirement.`name`,\n" +
				"	module.module,\n" +
				"	staff.StaffName creator,\n" +
				"	source,\n" +
				"	ownerName,\n" +
				"	remark,\n" +
				"	reviewerName,\n" +
				"	requirement.description,\n" +
				"	checkStandard,\n" +
				"	attachmentName,\n" +
				"	attachmentPath,\n" +
				"	stage,\n" +
				"	project.`name` projectName, requirement.deleteReason\n" +
				"FROM\n" +
				"	OA_SoftPerformanceRequirement requirement\n" +
				"INNER JOIN OA_Staff staff ON requirement.creatorId = staff.UserID\n" +
				"LEFT JOIN OA_SoftPerformanceModule module ON requirement.moduleId = module.id\n" +
				"inner join OA_SoftPerformanceProject project on requirement.projectId = project.id\n" +
				"WHERE\n" +
				"requirement.id = "+requirementId;
		Object object = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])object;
		RequirementVo requirementVo = new RequirementVo();
		requirementVo.setPriority(objs[0]+"");
		requirementVo.setName(objs[1]+"");
		requirementVo.setModule(objs[2]+"");
		requirementVo.setCreator(objs[3]+"");
		requirementVo.setSource(objs[4]+"");
		requirementVo.setOwner(objs[5]+"");
		requirementVo.setRemark(objs[6]+"");
		requirementVo.setReviewer(objs[7]+"");
		requirementVo.setDescription(objs[8]+"");
		requirementVo.setCheckStandard(objs[9]+"");
		String attachmentNameStr = objs[10]+"";
		String attachmentPathStr = objs[11]+"";
		if(!"null".equals(attachmentNameStr) && !"null".equals(attachmentPathStr)
				&& StringUtils.isNotBlank(attachmentPathStr) && StringUtils.isNotBlank(attachmentNameStr)){
			String[] attachmentNames = attachmentNameStr.split("#@#&");
			List<String> attachmentNameLst = new ArrayList<>();
			for(String attachmentName: attachmentNames){
				if(StringUtils.isNotBlank(attachmentName)){
					attachmentNameLst.add(attachmentName);
				}
			}
			requirementVo.setAttachmentNames(attachmentNameLst);

			String[] attachmentPaths = attachmentPathStr.split("#@#&");
			List<String> attachmentPathLst = new ArrayList<>();
			for(String attachmentPath: attachmentPaths){
				if(StringUtils.isNotBlank(attachmentPath)){
					attachmentPathLst.add(attachmentPath);
				}
			}
			requirementVo.setAttachmentPaths(attachmentPathLst);
		}
		requirementVo.setStage(objs[12]+"");
		requirementVo.setProjectName(objs[13]+"");
		requirementVo.setDeleteReason(objs[14]==null?"":objs[14]+"");
		return requirementVo;
	}

	@Override
	public RequirementEntity getRequirement(String requirementId) {
		String hql = "from RequirementEntity where id="+requirementId;
		return (RequirementEntity)baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void deleteAttachment(String requirementId, String attachmentName, String attachmentPath, String tableName) throws Exception{
		String sql = "select attachmentName, attachmentPath from "+tableName+" where id="+requirementId;
		Object object = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])object;
		String attachmentNameStr = "";
		if(null != objs[0]){
			attachmentNameStr = objs[0]+"";
		}
		String attachmentPathStr = "";
		if(null != objs[1]){
			attachmentPathStr = objs[1]+"";
		}
		String[] attachmentNames = attachmentNameStr.split("#@#&");
		String[] attachmentPaths = attachmentPathStr.split("#@#&");
		String newAttachmentNameStr="";
		String newAttachmentPathStr="";
		//去除删除的附件
		for(int i=0; i<attachmentNames.length; i++){
			if(attachmentName.trim().equals(attachmentNames[i].trim())){
				continue;
			}
			if(i==0){
				newAttachmentNameStr += attachmentNames[i];
			}else{
				newAttachmentNameStr += "#@#&"+attachmentNames[i];
			}
		}
		for(int i=0; i<attachmentPaths.length; i++){
			if(attachmentPath.trim().equals(attachmentPaths[i].trim())){
				continue;
			}
			if(i==0){
				newAttachmentPathStr += attachmentPaths[i];
			}else{
				newAttachmentPathStr += "#@#&"+attachmentPaths[i];
			}
		}
		String updateSql = "update "+tableName
				+ " set attachmentName='"+EscapeUtil.decodeSpecialChars(newAttachmentNameStr)+"', attachmentPath='"
				+EscapeUtil.decodeSpecialChars(newAttachmentPathStr)+"' where id="+requirementId;
		baseDao.excuteSql(updateSql);
		//若删除的是需求的附件，则删除分解任务继承的附件
		if(Constants.REQUIRE_TABLE.equals(tableName)){
			deleteSubRequireAttachment(requirementId, attachmentName, attachmentPath);
		}
	}

	private void deleteSubRequireAttachment(String requirementId, String attachmentName, String attachmentPath) {
		String hql = "from SubReqirementEntity where requirementId="+requirementId;
		@SuppressWarnings("unchecked")
		List<SubReqirementEntity> subRequires = (List<SubReqirementEntity>)baseDao.hqlfind(hql);
		for(SubReqirementEntity subRequire: subRequires){
			String attachmentNameStr = subRequire.getAttachmentName();
			String attachmentPathStr = subRequire.getAttachmentPath();
			if(StringUtils.isNotBlank(attachmentNameStr)){
				String[] attachmentNames = attachmentNameStr.split("#@#&");
				String[] attachmentPaths = attachmentPathStr.split("#@#&");
				String newAttachmentNameStr="";
				String newAttachmentPathStr="";
				//去除删除的附件
				for(int i=0; i<attachmentNames.length; i++){
					if(attachmentName.trim().equals(attachmentNames[i].trim())){
						continue;
					}
					if(i==0){
						newAttachmentNameStr += attachmentNames[i];
					}else{
						newAttachmentNameStr += "#@#&"+attachmentNames[i];
					}
				}
				for(int i=0; i<attachmentPaths.length; i++){
					if(attachmentPath.trim().equals(attachmentPaths[i].trim())){
						continue;
					}
					if(i==0){
						newAttachmentPathStr += attachmentPaths[i];
					}else{
						newAttachmentPathStr += "#@#&"+attachmentPaths[i];
					}
				}
				subRequire.setAttachmentName(newAttachmentNameStr);
				subRequire.setAttachmentPath(newAttachmentPathStr);
				baseDao.hqlUpdate(subRequire);
			}
		}
	}

	@Override
	public void updateRequirement(RequirementEntity requirement, File[] files, String[] fileNames) throws Exception {
		String sql = "select attachmentName, attachmentPath from OA_SoftPerformanceRequirement where id="+requirement.getId();
		Object object = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])object;
		String attachmentNameStr = "";
		if(null != objs[0]){
			attachmentNameStr = objs[0]+"";
		}
		String attachmentPathStr = "";
		if(null != objs[1]){
			attachmentPathStr = objs[1]+"";
		}
		requirement.setUpdateTime(new Date());
		String attachmentNames = "";
		String attachmentPaths = "";
		if(null != files && files.length>0){
			for(int i=0; i<files.length; i++){
				String fileName = fileNames[i];
				File destFile = new File(Constants.PRODUCT_FILE_DIRECTORY, UUID.randomUUID()+"_"+fileName);
				FileUtils.copyFile(files[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			if(StringUtils.isNotBlank(attachmentNameStr) && StringUtils.isNotBlank(attachmentPathStr)){
				attachmentNameStr += "#@#&"+attachmentNames;
				attachmentPathStr += "#@#&"+attachmentPaths;
			}else{
				attachmentNameStr = attachmentNames;
				attachmentPathStr = attachmentPaths;
			}
			String hql = "from SubReqirementEntity where requirementId="+requirement.getId();
			@SuppressWarnings("unchecked")
			List<SubReqirementEntity> subRequires = (List<SubReqirementEntity>)baseDao.hqlfind(hql);
			for(SubReqirementEntity subRequire: subRequires){
				String subAttachNameStr = subRequire.getAttachmentName();
				String subAttachPathStr = subRequire.getAttachmentPath();
				if(StringUtils.isNotBlank(subAttachNameStr)){
					subAttachNameStr += "#@#&"+attachmentNames;
				}else{
					subAttachNameStr += attachmentNames;
				}
				if(StringUtils.isNotBlank(subAttachPathStr)){
					subAttachPathStr += "#@#&"+attachmentPaths;
				}else{
					subAttachPathStr += attachmentPaths;
				}
				subRequire.setAttachmentName(subAttachNameStr);
				subRequire.setAttachmentPath(subAttachPathStr);
				baseDao.hqlSave(subRequire);
			}
		}
		requirement.setAttachmentName(attachmentNameStr);
		requirement.setAttachmentPath(attachmentPathStr);
		baseDao.hqlUpdate(requirement);

	}

	@Override
	public void deleteRequiremnet(String id) throws Exception {
		RequirementEntity requirementEntity = (RequirementEntity) baseDao
				.hqlfindUniqueResult("from RequirementEntity where id=" + id);
		requirementEntity.setIsDelete(1);
		baseDao.hqlUpdate(requirementEntity);

	}

	@Override
	public FunctionVo getFuntionDetail(String taskId) {
		String sql = "select `function`.id,`function`.`name`,`function`.priority,"+
				"`function`.taskType,`function`.assignerName,`function`.estimatedTime,"+
				"`function`.score,requirement.`name` associatedRequirement,"+
				"project.`name` project, module.module, version.version,"+
				"`function`.description, `function`.instanceId, `function`.deadline "+
				"from OA_SoftPerformanceFunction `function` "+
				"inner join OA_SoftPerformanceRequirement requirement "+
				"on `function`.requirementId=requirement.id "+
				"inner join OA_SoftPerformanceProject project "+
				"on `function`.projectId = project.id "+
				"inner join OA_SoftPerformanceModule module "+
				"on module.id = `function`.moduleId "+
				"inner join OA_SoftPerformanceVersion version "+
				"on `function`.projectVersionId = version.id "+
				"where `function`.id="+taskId;
		Object object = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])object;
		FunctionVo functionVo = new FunctionVo();
		functionVo.setId(objs[0]+"");
		functionVo.setName(objs[1]+"");
		functionVo.setPriority(objs[2]+"");
		functionVo.setTaskType(objs[3]+"");
		functionVo.setAssigner(objs[4]+"");
		functionVo.setEstimatedTime(objs[5]+"");
		functionVo.setScore(objs[6]+"");
		functionVo.setAssociatedRequirement(objs[7]+"");
		functionVo.setProject(objs[8]+"");
		functionVo.setModule(objs[9]+"");
		functionVo.setVersion(objs[10]+"");
		functionVo.setDescription(objs[11]+"");
		String instanceId = objs[12]+"";
		functionVo.setInstanceId(instanceId);
		functionVo.setDeadline(objs[13]+"");
		sql = "select name_ from ACT_HI_ATTACHMENT where PROC_INST_ID_='"+instanceId+"'";
		List<Object> objLst = baseDao.findBySql(sql);
		List<String> attachmentNames = new ArrayList<>();
		for(Object obj: objLst){
			attachmentNames.add(obj+"");
		}
		functionVo.setAttachmentNames(attachmentNames);
		return functionVo;
	}

	@Override
	public void deleteTask(String id) throws Exception {
		FunctionEntity functionEntity = (FunctionEntity) baseDao
				.hqlfindUniqueResult("from FunctionEntity where id=" + id);
		functionEntity.setIsDelete(1);
		baseDao.hqlUpdate(functionEntity);

	}

	@Override
	public ListResult<FunctionVo> getFuntions(int limit, int page, FunctionVo functionVo, String userId) {
		/*		String sqlLst = "select a.id,a.`name`,a.priority,a.taskType,a.assignerName,"
				+ "a.estimatedTime,a.score,b.`name` associatedRequirement,a.result,a.instanceId,a.addTime "+ 
				"from OA_SoftPerformanceFunction a inner join OA_SoftPerformanceRequirement b on a.requirementId=b.id "
				+ "where a.isDelete=0 and a.projectId="+functionVo.getProject();
		String versionId = functionVo.getVersion();
		String moduleId = functionVo.getModule();
		String isComplete = functionVo.getIsComplete();
		String beginDate = functionVo.getBeginDate();
		String endDate = functionVo.getEndDate();
		if(StringUtils.isNotBlank(versionId)){
			sqlLst += " and a.projectVersionId="+versionId;
		}
		if(StringUtils.isNotBlank(moduleId)){
			sqlLst += " and a.moduleId="+moduleId;
		}
		if(null!=userId){
			sqlLst += " and a.creatorId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlLst += " and DATE(a.addTime)>='"+beginDate+"'";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlLst += " and DATE(a.addTime)<='"+endDate+"'";
		}
		if("true".equalsIgnoreCase(isComplete)){
			sqlLst += " and (a.result=6 or a.result=29 or a.result=30 or a.result=31)";
		}else{
			sqlLst += " and (a.result!=6 and a.result!=29 and a.result!=30 and a.result!=31 or a.result is null)";
		}
		sqlLst += " order by a.addTime desc";
		String sqlCount = "select count(id) from FunctionEntity where isDelete=0 and projectId="+functionVo.getProject();
		if(StringUtils.isNotBlank(versionId)){
			sqlCount += " and projectVersionId="+versionId;
		}
		if(StringUtils.isNotBlank(moduleId)){
			sqlCount += " and moduleId="+moduleId;
		}
		if(null!=userId){
			sqlCount += " and creatorId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlCount += " and DATE(addTime)>='"+beginDate+"'";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlCount += " and DATE(addTime)<='"+endDate+"'";
		}
		if("true".equalsIgnoreCase(isComplete)){
			sqlCount += " and (result=6 or result=29 or result=30 or result=31)";
		}else{
			sqlCount += " and (result!=6 and result!=29 and result!=30 and result!=31 or result is null)";
		}
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = ((Long)baseDao.hqlfindUniqueResult(sqlCount)).intValue();
		List<FunctionVo> taskLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			FunctionVo task = new FunctionVo();
			task.setId(objs[0]+"");
			task.setName(objs[1]+"");
			task.setPriority(objs[2]+"");
			task.setTaskType(objs[3]+"");
			task.setAssigner(objs[4]+"");
			task.setEstimatedTime(objs[5]+"");
			task.setScore(objs[6]+"");
			task.setAssociatedRequirement(objs[7]+"");
			task.setResult(objs[8]+"");
			task.setInstanceId(objs[9]+"");
			task.setAddTime(objs[10]+"");
			taskLst.add(task);
		}
		return new ListResult<FunctionVo>(taskLst, count);*/
		String sqlLst = "select a.id,IFNULL(a.`name`,s.subRequirementName),a.priority,a.taskType,a.assignerName,"
				+ "a.estimatedTime,IFNULL(a.score,s.score),b.`name` associatedRequirement,a.result,a.instanceId,a.addTime,v.version "+ 
				"from OA_SoftPerformanceFunction a RIGHT JOIN OA_SoftPerformanceSubRequirement s"
				+ " ON a.subRequirementId = s.id LEFT JOIN OA_SoftPerformanceRequirement b ON"
				+ " s.requirementId = b.id LEFT JOIN OA_SoftPerformanceVersion v ON b.projectVersionId = v.id "
				+ "where (a.isDelete=0 and a.projectId="+functionVo.getProject();
		String versionId = functionVo.getVersion();
		String moduleId = functionVo.getModule();
		String isComplete = functionVo.getIsComplete();
		String beginDate = functionVo.getBeginDate();
		String endDate = functionVo.getEndDate();
		String name = functionVo.getName();
		String conditions = "";
		if(StringUtils.isNotBlank(versionId)){
			sqlLst += " and a.projectVersionId="+versionId;
			conditions += "b.projectVersionId="+versionId;
		}
		if(StringUtils.isNotBlank(name)){
			sqlLst += " and a.name like '%"+EscapeUtil.decodeSpecialChars(name)+"%'";
			if(StringUtils.isNotBlank(conditions)){
				conditions += " and s.subRequirementName like '%"+EscapeUtil.decodeSpecialChars(name)+"%'";
			}else{
				conditions += " s.subRequirementName like '%"+EscapeUtil.decodeSpecialChars(name)+"%'";
			}
		}
		if(StringUtils.isNotBlank(moduleId)){
			sqlLst += " and a.moduleId="+moduleId;
			if(StringUtils.isNotBlank(conditions)){
				conditions += " and b.moduleId="+moduleId;
			}else{
				conditions += "b.moduleId="+moduleId;
			}
		}
		if(null!=userId){
			sqlLst += " and a.creatorId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlLst += " and DATE(a.addTime)>='"+beginDate+"'";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlLst += " and DATE(a.addTime)<='"+endDate+"'";
		}
		if("true".equalsIgnoreCase(isComplete)){
			sqlLst += " and (a.result=6 or a.result=29 or a.result=30 or a.result=31))";
		}else{
			sqlLst += " and (a.result!=6 and a.result!=29 and a.result!=30"
					+ " and a.result!=31 or a.result is null)) ";
			String _conditions = "";
			if(StringUtils.isNotBlank(conditions)){
				_conditions += " (a.id is null and b.projectId="+functionVo.getProject()
				+" and s.subRequirementName is not null and "+conditions+") ";
			}else{
				_conditions += " (a.id is null and b.projectId="+functionVo.getProject()
				+" and s.subRequirementName is not null) ";
			}
			sqlLst += " or "+_conditions;
		}
		sqlLst += " order by a.addTime desc";
		String sqlCount = "SELECT\n" +
				"	count(s.id)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceFunction a\n" +
				"RIGHT JOIN OA_SoftPerformanceSubRequirement s ON a.subRequirementId = s.id\n" +
				"LEFT JOIN OA_SoftPerformanceRequirement b ON s.requirementId = b.id\n" +
				"WHERE\n" +
				"	(\n" +
				"		a.isDelete = 0\n" +
				"		AND a.projectId="+functionVo.getProject();
		if(StringUtils.isNotBlank(versionId)){
			sqlCount += " and a.projectVersionId="+versionId;
		}
		if(StringUtils.isNotBlank(name)){
			sqlCount += " and a.name like '%"+EscapeUtil.decodeSpecialChars(name)+"%'";
		}
		if(StringUtils.isNotBlank(moduleId)){
			sqlCount += " and a.moduleId="+moduleId;
		}
		if(null!=userId){
			sqlCount += " and a.creatorId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlCount += " and DATE(a.addTime)>='"+beginDate+"'";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlCount += " and DATE(a.addTime)<='"+endDate+"'";
		}
		if("true".equalsIgnoreCase(isComplete)){
			sqlCount += " and (a.result=6 or a.result=29 or a.result=30 or a.result=31))";
		}else{
			sqlCount += " and (a.result!=6 and a.result!=29 and a.result!=30 and a.result!=31 or a.result is null))";
			if(StringUtils.isNotBlank(conditions)){
				conditions = " (a.id is null and b.projectId="+functionVo.getProject()
				+" and s.subRequirementName is not null and "+conditions+") ";
			}else{
				conditions += " (a.id is null and b.projectId="+functionVo.getProject()
				+" and s.subRequirementName is not null) ";
			}
			sqlCount += " or "+conditions;
		}
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		
		List<FunctionVo> taskLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			FunctionVo task = new FunctionVo();
			task.setId(objs[0]+"");
			task.setName(objs[1]+"");
			task.setPriority(objs[2]+"");
			task.setTaskType(objs[3]+"");
			task.setAssigner(objs[4]+"");
			task.setEstimatedTime(objs[5]+"");
			task.setScore(objs[6]+"");
			task.setAssociatedRequirement(objs[7]+"");
			task.setResult(objs[8]+"");
			task.setInstanceId(objs[9]+"");
			task.setAddTime(objs[10]+"");
			task.setVersion(objs[11]+"");
			taskLst.add(task);
		}
		return new ListResult<FunctionVo>(taskLst, count);
	}

	@Override
	public ListResult<VersionVo> getVersions(int page, int limit, String projectId) {
		String sql = "SELECT\n" +
				"	version.id,\n" +
				"	version.version,\n" +
				"	staff.StaffName,\n" +
				"	version.addTime,\n" +
				"	version.projectId,\n" +
				"	version.developerNum,\n" +
				"	version.beginDate,\n" +
				"	version.endDate,\n" +
				"	version.workHour,\n" +
				"	version.status\n" +
				"FROM\n" +
				"	OA_SoftPerformanceVersion version\n" +
				"INNER JOIN OA_Staff staff ON version.creatorId = staff.UserID\n" +
				"WHERE\n" +
				"	version.projectId = "+projectId+"\n" +
				"AND version.isDelete = 0 order by version.addTime desc";
		String sqlCount = "select count(id) from OA_SoftPerformanceVersion where projectId="+projectId+" and isDelete=0";
		List<Object> objLst = baseDao.findPageList(sql, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		List<VersionVo> versionVos = new ArrayList<>();
		for(Object object: objLst){
			Object[] obj = (Object[])object;
			VersionVo version = new VersionVo();
			version.setId(obj[0]+"");
			version.setVersion(obj[1]+"");
			version.setCreator(obj[2]+"");
			version.setCreateTime(obj[3]+"");
			version.setProjectId(obj[4]+"");
			version.setDeveloperNum(Integer.parseInt(obj[5]==null ? "0":obj[5]+""));
			version.setBeginDate(obj[6]==null ? "":obj[6]+"");
			version.setEndDate(obj[7]==null ? "":obj[7]+"");
			version.setWorkHour(obj[8]==null ? "":obj[8]+"");
			version.setStatus(obj[9]==null ? "":obj[9]+"");
			versionVos.add(version);
		}
		return new ListResult<VersionVo>(versionVos, count);
	}

	@Override
	public ListResult<ModuleVo> getModules(int page, int limit, String projectId) {
		String sql = "select module.id, module.module, staff.StaffName, module.addTime, module.projectId "+
				"from OA_SoftPerformanceModule module "+
				"inner join OA_Staff staff on module.creatorId=staff.UserID "+
				"where module.projectId="+projectId+" and module.isDelete=0";
		String sqlCount = "select count(id) from OA_SoftPerformanceModule where projectId="+projectId+" and isDelete=0";
		List<Object> objLst = baseDao.findPageList(sql, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		List<ModuleVo> moduleVos = new ArrayList<>();
		for(Object object: objLst){
			Object[] obj = (Object[])object;
			ModuleVo module = new ModuleVo();
			module.setId(obj[0]+"");
			module.setModule(obj[1]+"");
			module.setCreator(obj[2]+"");
			module.setCreateTime(obj[3]+"");
			module.setProjectId(obj[4]+"");
			moduleVos.add(module);
		}
		return new ListResult<ModuleVo>(moduleVos, count);
	}

	@Override
	public String getUpdatestVersion(String projectId) {
		String sql = "select version.version from OA_SoftPerformanceVersion version where version.projectId = "+projectId
				+ " and version.isDelete=0 order by version.updateTime desc limit 0,1";
		Object obj = baseDao.getUniqueResult(sql);
		return obj+"";
	}

	@Override
	public SoftPerformanceVo getFuntion(String instanceId) {
		String sql = "from FunctionEntity where instanceId='"+instanceId+"' and isDelete=0";
		FunctionEntity functionEntity = (FunctionEntity)baseDao.hqlfindUniqueResult(sql);
		SoftPerformanceVo softPerformanceVo = (SoftPerformanceVo)CopyUtil.toVo(functionEntity, SoftPerformanceVo.class);
		return softPerformanceVo;
	}

	@Override
	public List<String> getAttachmentLst(String instanceId) {
		String sql = "select name_ from ACT_HI_ATTACHMENT where PROC_INST_ID_='"+instanceId+"'";
		List<Object> objLst = baseDao.findBySql(sql);
		List<String> attachmentNames = new ArrayList<>();
		for(Object obj: objLst){
			attachmentNames.add(obj+"");
		}
		return attachmentNames;
	}

	@Override
	public void deleteTaskAttachment(String attachmentName, String instanceId) {
		String sql = "delete from ACT_HI_ATTACHMENT where PROC_INST_ID_='"+instanceId+"' and NAME_='"+attachmentName+"'"; 
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateRequireStatus(Integer requirementId) {
		String sql = "update OA_SoftPerformanceRequirement set status='"+
				Constants.ACTIVATE+"',stage='"+Constants.DEVELOPING+"' where id="+requirementId;
		baseDao.excuteSql(sql);

	}

	@Override
	public void saveDeductedScore(ScoreResultVo scoreResultVo) {
		ScoreResultEntity scoreResultEntity = (ScoreResultEntity) CopyUtil.toEntity(scoreResultVo, ScoreResultEntity.class);
		scoreResultEntity.setAddTime(new Date());
		scoreResultEntity.setIsDeleted(0);
		baseDao.hqlSave(scoreResultEntity);
	}

	@Override
	public ListResult<Object> getScoreResult(int limit, int page, String userId, String beginDate, String endDate) {
		String querySql="SELECT\n" +
				"	*\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			(\n" +
				"				CASE\n" +
				"				WHEN s.taskId THEN\n" +
				"					'需求'\n" +
				"				ELSE\n" +
				"					'扣分'\n" +
				"				END\n" +
				"			),\n" +
				"			f. NAME,\n" +
				"			p. NAME AS pname,\n" +
				"			staff.StaffName,\n" +
				"			s.duty,\n" +
				"			m.module,\n" +
				"			v.version,\n" +
				"			f.addTime,\n" +
				"			f.score,\n" +
				"			s.resultScore,\n" +
				"			s.itemDate,\n" +
				"			f.id,\n" +
				"			s.reason,\n" +
				"			s.taskId,\n" +
				"			s.id resultId,\n" +
				"			f.instanceId,\n" +
				"			s.userId\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult s\n" +
				"		LEFT JOIN OA_SoftPerformanceFunction f ON s.taskId = f.id\n" +
				"		LEFT JOIN OA_SoftPerformanceProject p ON f.projectId = p.id\n" +
				"		LEFT JOIN OA_SoftPerformanceVersion v ON f.projectVersionId = v.id\n" +
				"		LEFT JOIN OA_SoftPerformanceModule m ON f.moduleId = m.id\n" +
				"		INNER JOIN OA_Staff staff ON s.userId = staff.UserID\n" +
				"		WHERE\n" +
				"			s.isDeleted = 0\n" +
				"		AND s.problemTaskId IS NULL\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				'问题',\n" +
				"				problem.orderName,\n" +
				"				p.`name`,\n" +
				"				staff.StaffName,\n" +
				"				s.duty,\n" +
				"				'',\n" +
				"				v.version,\n" +
				"				problem.addTime,\n" +
				"				problem.score,\n" +
				"				s.resultScore,\n" +
				"				s.itemDate,\n" +
				"				problem.id,\n" +
				"				'',\n" +
				"				'',\n" +
				"				s.id resultId,\n" +
				"				problem.processInstanceID,\n" +
				"				s.userId\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult s,\n" +
				"				OA_ProblemOrder problem,\n" +
				"				OA_SoftPerformanceProject p,\n" +
				"				OA_SoftPerformanceVersion v,\n" +
				"				OA_Staff staff\n" +
				"			WHERE\n" +
				"				s.userId = staff.UserID\n" +
				"			AND s.isDeleted = 0\n" +
				"			AND s.problemTaskId = problem.id\n" +
				"			AND problem.projectId = p.id\n" +
				"			AND problem.projectVersionId = v.id\n" +
				"	) allScore\n";
		if(StringUtils.isNotBlank(userId)){
			querySql += "WHERE allScore.itemDate <= '"+endDate+"'\n" +
					"AND allScore.itemDate >= '"+beginDate+"'\n" +
					"And allScore.userId='"+userId+"'\n";
		}
		querySql += 	"ORDER BY\n" +
				"	allScore.itemDate desc";
		String sqlCount = "select count(id) from OA_SoftPerformanceScoreResult where isDeleted=0\n";
		if(StringUtils.isNotBlank(userId)){
			sqlCount +=  "AND itemDate <= '"+endDate+"'\n" +
					"AND itemDate >= '"+beginDate+"'\n" +
					"And userId='"+userId+"'\n";
		}
		List<Object> objLst = baseDao.findPageList(querySql, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<Object>(objLst, count);
	}

	@Override
	public boolean isInCurrentMonth(String resultId) {
		String sql = "select itemDate from OA_SoftPerformanceScoreResult where id="+resultId;
		Object obj = baseDao.getUniqueResult(sql);
		String resultDay = obj+"";
		String resultMonth = resultDay.substring(0, resultDay.lastIndexOf("-"));
		Date currentDate = new Date();
		String currentDay = DateUtil.getDayStr(currentDate);
		String currentMonth = currentDay.substring(0, resultDay.lastIndexOf("-"));
		if(resultMonth.equals(currentMonth)){
			return true;
		}
		return false;
	}

	@Override
	public void deleteResultScore(String resultId) {
		String sql = "update OA_SoftPerformanceScoreResult set isDeleted='1' where taskId is null and id="+resultId+" ";
		baseDao.excuteSql(sql);
	}

	@Override
	public String getScores(String userId, String beginDate, String endDate) {
		String sql = "select sum(resultScore) scores "+
				"from OA_SoftPerformanceScoreResult "+
				"where isDeleted=0 and userId='"+userId+"' and itemDate>='"+beginDate+"' and itemDate<='"+endDate+"'";
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public void addSubRequirement(SubRequirementVo subRequirementVo, File[] files, String[] fileNames) throws Exception{
		subRequirementVo.setAddTime(new Date());
		subRequirementVo.setIsDeleted(0);
		String attachmentNames = "";
		String attachmentPaths = "";
		if(null != fileNames){
			for(int i=0; i<files.length; i++){
				String fileName = fileNames[i];
				File destFile = new File(Constants.PRODUCT_FILE_DIRECTORY, UUID.randomUUID()+"_"+fileName);
				FileUtils.copyFile(files[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
		}
		RequirementEntity requirement = getRequirementByRequireId(subRequirementVo.getRequirementId());
		String attachNames = requirement.getAttachmentName();
		String attachPaths = requirement.getAttachmentPath();
		if(StringUtils.isNotBlank(attachNames)){
			String[] attachs = attachNames.split("#@#&");
			String[] paths = attachPaths.split("#@#&");
			int index = 0;
			for(String attach: attachs){
				if(StringUtils.isNotBlank(attach)){
					if(StringUtils.isNotBlank(attachmentNames)){
						attachmentNames += "#@#&"+attach;
						attachmentPaths += "#@#&"+paths[index];
					}else{
						attachmentNames += attach;
						attachmentPaths += paths[index];
					}
				}
				index++;
			}
		}
		subRequirementVo.setAttachmentName(attachmentNames);
		subRequirementVo.setAttachmentPath(attachmentPaths);
		SubReqirementEntity subRequire = (SubReqirementEntity) CopyUtil.toEntity(subRequirementVo, SubReqirementEntity.class);
		baseDao.hqlSave(subRequire);

	}

	private RequirementEntity getRequirementByRequireId(Integer requirementId) {
		String hql = "from RequirementEntity where id="+requirementId;
		return (RequirementEntity) baseDao.hqlfindUniqueResult(hql);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<SubRequirementVo> getSubRequirementLst(String requirementId) {
		String hql = "from SubReqirementEntity where isDeleted=0 and requirementId="+requirementId+" order by addTime";
		List<SubReqirementEntity> subRequires = (List<SubReqirementEntity>) baseDao.hqlfind(hql);
		List<SubRequirementVo> subRequireVos = new ArrayList<>();
		for(SubReqirementEntity subRequire: subRequires){
			SubRequirementVo subRequireVo = new SubRequirementVo();
			subRequireVo.setId(subRequire.getId());
			subRequireVo.setAddTime(subRequire.getAddTime());
			subRequireVo.setDescription(subRequire.getDescription());
			subRequireVo.setIsDeleted(subRequire.getIsDeleted());
			String sql="SELECT\n" +
					"	count(*)\n" +
					"FROM\n" +
					"	OA_SoftPerformanceFunction f\n" +
					"WHERE\n" +
					"	f.subRequirementId = "+subRequire.getId()+"\n" +
					"AND f.isDelete = 0\n" +
					"AND (\n" +
					"	(\n" +
					"		result != '6'\n" +
					"		AND result != '31'\n" +
					"		AND result != '29'\n" +
					"	)\n" +
					"	OR (result IS NULL)\n" +
					")";
			subRequireVo.setCount(((BigInteger)baseDao.getUniqueResult(sql)).intValue());
			subRequireVo.setPriority(subRequire.getPriority());
			subRequireVo.setRequirementId(subRequire.getRequirementId());
			subRequireVo.setSubRequirementName(subRequire.getSubRequirementName());
			subRequireVo.setScore(subRequire.getScore());
			subRequireVo.setDeveloper(subRequire.getDeveloper());
			String attachmentNameStr = subRequire.getAttachmentName();
			String attachmentPathStr = subRequire.getAttachmentPath();
			if(!"null".equals(attachmentNameStr) && !"null".equals(attachmentPathStr)
					&& StringUtils.isNotBlank(attachmentPathStr) && StringUtils.isNotBlank(attachmentNameStr)){
				String[] attachmentNames = attachmentNameStr.split("#@#&");
				List<String> attachmentNameLst = new ArrayList<>();
				for(String attachmentName: attachmentNames){
					if(StringUtils.isNotBlank(attachmentName)){
						attachmentNameLst.add(attachmentName);
					}
				}
				subRequireVo.setAttachmentNames(attachmentNameLst);

				String[] attachmentPaths = attachmentPathStr.split("#@#&");
				List<String> attachmentPathLst = new ArrayList<>();
				for(String attachmentPath: attachmentPaths){
					if(StringUtils.isNotBlank(attachmentPath)){
						attachmentPathLst.add(attachmentPath);
					}
				}
				subRequireVo.setAttachmentPaths(attachmentPathLst);
			}
			sql = "SELECT\n"+
					"result\n"+
					"FROM\n"+
					"OA_SoftPerformanceSubRequirement requirement\n"+
					"LEFT JOIN OA_SoftPerformanceFunction funtion \n"+
					"ON requirement.id = funtion.subRequirementId\n"+
					"WHERE\n"+
					"isDeleted = 0\n"+
					"AND requirement.id ="+subRequire.getId()+"\n"+
					"ORDER BY\n"+
					"requirement.addTime DESC";
			List<Object> objLst = baseDao.findBySql(sql);
			for(Object obj: objLst){
				if("6".equals(obj) || "29".equals(obj) || "31".equals(obj) || null==obj || StringUtils.isBlank(obj+"")){
					subRequireVo.setCanEdit(true);
				}else{
					subRequireVo.setCanEdit(false);
				}
			}
			subRequireVos.add(subRequireVo);
		}
		return subRequireVos;
	}

	@Override
	public SubRequirementVo getSubRequirement(String subRequireId) {
		String hql = "from SubReqirementEntity where id="+subRequireId;
		return (SubRequirementVo) CopyUtil.toVo((SubReqirementEntity)baseDao.hqlfindUniqueResult(hql), SubRequirementVo.class);
	}

	@Override
	public void updateSubRequirement(SubRequirementVo subRequirementVo, File[] files, String[] fileNames) throws Exception {
		String sql = "select attachmentName, attachmentPath from OA_SoftPerformanceSubRequirement where id="+subRequirementVo.getId();
		Object object = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])object;
		String attachmentNameStr = "";
		if(null != objs[0]){
			attachmentNameStr = objs[0]+"";
		}
		String attachmentPathStr = "";
		if(null != objs[1]){
			attachmentPathStr = objs[1]+"";
		}
		subRequirementVo.setUpdateTime(new Date());
		if(null != files && files.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<files.length; i++){
				String fileName = fileNames[i];
				File destFile = new File(Constants.PRODUCT_FILE_DIRECTORY, UUID.randomUUID()+"_"+fileName);
				FileUtils.copyFile(files[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			if(StringUtils.isNotBlank(attachmentNameStr) && StringUtils.isNotBlank(attachmentPathStr)){
				attachmentNameStr += "#@#&"+attachmentNames;
				attachmentPathStr += "#@#&"+attachmentPaths;
			}else{
				attachmentNameStr = attachmentNames;
				attachmentPathStr = attachmentPaths;
			}
		}
		subRequirementVo.setAttachmentName(attachmentNameStr);
		subRequirementVo.setAttachmentPath(attachmentPathStr);
		SubReqirementEntity subRequirementEntity = (SubReqirementEntity) CopyUtil.toEntity(subRequirementVo, SubReqirementEntity.class);
		baseDao.hqlUpdate(subRequirementEntity);

	}

	@Override
	public boolean isCreateTaskByVersion(String versionId) {
		String sql = "select COUNT(id) from OA_SoftPerformanceFunction"
				+ " `function` where `function`.projectVersionId="+versionId+" and `function`.isDelete=0";
		Object obj = baseDao.getUniqueResult(sql);
		int count = Integer.parseInt(obj+"");
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isCreateTaskByRequire(String requireId) {
		String sql = "select COUNT(id) from OA_SoftPerformanceFunction"
				+ " `function` where `function`.requirementId="+requireId+" and `function`.isDelete=0 and IFNULL(`function`.result,0)!=31";
		Object obj = baseDao.getUniqueResult(sql);
		int count = Integer.parseInt(obj+"");
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public List<Object> getTaskLstByRequire(String requirementId) {
		String sql = "select name,priority,taskType,assignerName,result from OA_SoftPerformanceFunction "+
				"`function` where `function`.requirementId="+requirementId+" and `function`.isDelete=0 and IFNULL(`function`.result,0)!=31";
		List<Object> objLst = baseDao.findBySql(sql);
		return objLst;
	}

	@Override
	public void completeRequire(String requirementId) {
		String sql = "update OA_SoftPerformanceRequirement set stage='"+Constants.COMPLETED+"' where id="+requirementId;
		baseDao.excuteSql(sql);
	}
	public void deleteRequire(String requirementId, String deleteReason){
		String sql = "update OA_SoftPerformanceRequirement set status='"+Constants.DELETE+"', deleteReason='"+EscapeUtil.decodeSpecialChars(deleteReason)+
				"' where id="+requirementId;
		baseDao.excuteSql(sql);
	}
	@Override
	public Map<String, List<FunctionVo>> getTaskLstByVersion(String versionId) {
		Map<String, List<FunctionVo>> requireAndTaskLstMap = new HashMap<>();
		String sql = "select requirement.`name` requirementName, staff.StaffName, requirement.stage,\n"+
				"`function`.`name`,`function`.priority,taskType,assignerName,result\n"+
				"from (select * from OA_SoftPerformanceFunction where isDelete=0) `function`\n"+
				"right join OA_SoftPerformanceRequirement requirement ON\n"+
				"`function`.requirementId = requirement.id\n"+
				"inner join OA_Staff staff on requirement.creatorId=staff.UserID\n"+
				"where requirement.projectVersionId="+versionId+" and requirement.isDelete=0 and requirement.`status`!='"+Constants.DELETE+"'";
		List<Object> objLst = baseDao.findBySql(sql);
		for(Object obj: objLst){
			Object[] objs = (Object[])obj;
			String requirementName = objs[0]+"";
			String staffName = objs[1]+"";
			String stage = objs[2]+"";
			String key = requirementName+"#&&#"+staffName+"#&&#"+stage;
			FunctionVo functionVo = new FunctionVo();
			functionVo.setName(objs[3]+"");
			functionVo.setPriority(objs[4]+"");
			functionVo.setTaskType(objs[5]+"");
			functionVo.setAssigner(objs[6]+"");
			functionVo.setResult(objs[7]+"");
			if(requireAndTaskLstMap.containsKey(key)){
				requireAndTaskLstMap.get(key).add(functionVo);
			}else{
				List<FunctionVo> taskLst = new ArrayList<>();
				taskLst.add(functionVo);
				requireAndTaskLstMap.put(key, taskLst);
			}
		}
		return requireAndTaskLstMap;
	}

	@Override
	public void completeVersion(String versionId) {
		String sql = "update OA_SoftPerformanceVersion set status='"+Constants.COMPLETED+"' where id="+versionId;
		baseDao.excuteSql(sql);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectVersionEntity> getUnCompletedVersionLst(String projectId) {
		String hql = "from ProjectVersionEntity version where isDelete=0 and projectId="+projectId
				+" and (version.status!='"+Constants.COMPLETED+"' or version.status is null) order by addTime desc";
		return (List<ProjectVersionEntity>)baseDao.hqlfind(hql);
	}

	@Override
	public ListResult<RequirementVo> getPreparedRequirementLst(String[] query, int limit, int page) {
		String sqlLst = "select requirement.id, requirement.priority, requirement.`name`, module.module,\n"+
				"staff.StaffName creator, project.`name` project, version.version, requirement.projectId, project.name projectName,\n"+
				"requirement.back,requirement.returnReason\n"+
				"from OA_SoftPerformanceRequirement requirement\n"+
				"inner join OA_SoftPerformanceProject project\n"+
				"on (requirement.projectId = project.id)\n"+
				"left join OA_SoftPerformanceModule module \n"+
				"on (requirement.moduleId = module.id)\n"+
				"left join OA_SoftPerformanceVersion version\n"+
				"on (requirement.projectVersionId=version.id)\n"+
				"inner join OA_Staff staff \n"+
				"on requirement.creatorId = staff.UserID \n"+
				"where requirement.isDelete=0 \n"+
				"and (requirement.moduleId is null or requirement.projectVersionId  is null)\n";

		String priority = query[0];
		String requirementName = query[1];
		String projectId = query[2];
		if(StringUtils.isNotBlank(priority)){
			sqlLst += "and requirement.priority='"+priority+"'\n";
		}
		if(StringUtils.isNotBlank(requirementName)){
			sqlLst += "and requirement.`name` like '%"+requirementName+"%'\n";
		}
		if(StringUtils.isNotBlank(projectId)){
			sqlLst += "and requirement.projectId="+projectId+"\n";
		}
		sqlLst += "ORDER BY (case requirement.priority when '低' then 0 when '中' then 1 when '高' then 2 when '加急' then 3 end) desc";

		String sqlCount = "select COUNT(id) from OA_SoftPerformanceRequirement where isDelete=0\n"+
				"and (moduleId is null or projectVersionId  is null)";
		if(StringUtils.isNotBlank(priority)){
			sqlCount += "and priority='"+priority+"'\n";
		}
		if(StringUtils.isNotBlank(requirementName)){
			sqlCount += "and `name` like '%"+requirementName+"%'\n";
		}
		if(StringUtils.isNotBlank(projectId)){
			sqlCount += "and projectId="+projectId;
		}
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		List<RequirementVo> requirementLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			RequirementVo requirement = new RequirementVo();
			requirement.setId(objs[0]+"");
			requirement.setPriority(objs[1]+"");
			requirement.setName(objs[2]+"");
			requirement.setModule(objs[3]+"");
			requirement.setCreator(objs[4]+"");
			requirement.setProject(objs[5]+"");
			requirement.setVersion(objs[6]+"");
			requirement.setProject(objs[7]+"");
			requirement.setProjectName(objs[8]+"");
			requirement.setBack(objs[9]==null ? 0:Integer.parseInt(objs[9]+""));
			requirement.setReturnReason(objs[10]+"");
			requirementLst.add(requirement);
		}
		return new ListResult<RequirementVo>(requirementLst, count);
	}

	@Override
	public void saveVersionOrModule(String requireId, String versionId, String moduleId) {

		String sql = "update OA_SoftPerformanceRequirement set ";

		int index = 0;
		if(StringUtils.isNotBlank(versionId)){
			index++;
			sql += "projectVersionId="+versionId;
		}
		if(StringUtils.isNotBlank(moduleId)){
			if(index==1){
				sql += ",moduleId="+moduleId;
			}else{
				sql += "moduleId="+moduleId;
			}
		}
		sql += " where id="+requireId;
		baseDao.excuteSql(sql);

	}

	@Override
	public void completeDivide(String requireId) {
		String sql = "update OA_SoftPerformanceRequirement set divide=1 where id="+requireId;
		baseDao.excuteSql(sql);

	}

	@Override
	public SoftPerformanceVo getTaskVo(String subReqireId) {
		String sql = "select req.projectId,req.projectVersionId,req.moduleId,sub.subRequirementName,\n"+
				"sub.score,sub.description,sub.attachmentName, sub.attachmentPath, req.name requirementName\n"+
				"from\n"+ 
				"OA_SoftPerformanceSubRequirement sub\n"+
				"inner join OA_SoftPerformanceRequirement req\n"+
				"on \n"+
				"sub.requirementId=req.id\n"+
				"where sub.id="+subReqireId;
		Object obj = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])obj;
		SoftPerformanceVo taskVo = new SoftPerformanceVo();
		taskVo.setProjectId(Integer.parseInt(objs[0]+""));
		taskVo.setProjectVersionId(Integer.parseInt(objs[1]+""));
		taskVo.setModuleId(Integer.parseInt(objs[2]+""));
		taskVo.setName(objs[3]+"");
		taskVo.setScore(objs[4]+"");
		taskVo.setDescription(objs[5]+"");
		taskVo.setAttachmentName(objs[6]+"");
		taskVo.setAttachmentPath(objs[7]+"");
		taskVo.setRequirementName(objs[8]+"");
		return taskVo;
	}

	@Override
	public String getProjectId(String requirementId) {
		String sql = "select projectId from OA_SoftPerformanceRequirement where id="+requirementId;
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public List<FunctionVo> getTaskLst(String subRequirementId, int type) {
		String sqlLst = "select a.id,a.`name`,a.priority,a.taskType,a.assignerName,a.estimatedTime,\n"+
				"a.score,b.`name` associatedRequirement,a.result,a.instanceId,a.addTime \n"+
				"from OA_SoftPerformanceFunction a \n"+
				"inner join OA_SoftPerformanceRequirement b on a.requirementId=b.id \n"+
				"where a.isDelete=0 ";
		if(0==type){
			sqlLst += "and a.subRequirementId="+subRequirementId;
		}else if(1==type){
			sqlLst += "and a.requirementId="+subRequirementId;
		}
		List<Object> objLst = baseDao.findBySql(sqlLst);
		List<FunctionVo> taskLst = new ArrayList<>();
		for(Object object: objLst){
			Object[] objs = (Object[]) object;
			FunctionVo task = new FunctionVo();
			task.setId(objs[0]+"");
			task.setName(objs[1]+"");
			task.setPriority(objs[2]+"");
			task.setTaskType(objs[3]+"");
			task.setAssigner(objs[4]+"");
			task.setEstimatedTime(objs[5]+"");
			task.setScore(objs[6]+"");
			task.setAssociatedRequirement(objs[7]+"");
			task.setResult(objs[8]+"");
			task.setInstanceId(objs[9]+"");
			task.setAddTime(objs[10]+"");
			taskLst.add(task);
		}
		return taskLst;
	}
	@Override
	public void actRequireStatus(String requirementId) {
		String sql = "update OA_SoftPerformanceRequirement set status='"+
				Constants.ACTIVATE+"' where id="+requirementId;
		baseDao.excuteSql(sql);

	}
	public Map<String, File> getSubRequireAttachments(Integer subRequireId){
		String sql = "select attachmentName, attachmentPath from OA_SoftPerformanceSubRequirement where id="+subRequireId;
		Object obj = baseDao.getUniqueResult(sql);
		Object[] objs = (Object[])obj;
		Map<String, File> fileMap = new HashMap<>();
		if(objs[0] == null || objs[1] == null || StringUtils.isBlank(objs[0]+"") || StringUtils.isBlank(objs[1]+"")){
			return fileMap;
		}else{
			String attachmentNameStr = objs[0]+"";
			String attachmentPathStr = objs[1]+"";
			String[] attachmentNames = attachmentNameStr.split("#@#&");
			String[] attachmentPaths = attachmentPathStr.split("#@#&");
			for(int i=0; i<attachmentNames.length; i++){

				String attachmentName = attachmentNames[i];
				if(StringUtils.isBlank(attachmentName)){
					continue;
				}
				String attachmentPath = attachmentPaths[i];
				File file = new File(attachmentPath);
				fileMap.put(attachmentName, file);
			}
		}
		return fileMap;
	}


	@Override
	public boolean hasRight(String userId, String projectName, List<String> type) {
		String sql = "select COUNT(id) from OA_SoftGroup where isDeleted=0 and userId = '"+userId+"' and project='"+projectName+"' ";
		String typeConditions = "type in (";
		String isLeader = "";
		int index = 0;
		//组长
		boolean hasLeader = false;
		//人员类型
		boolean hasType = false;
		for(String _type: type){
			if("组长".equals(_type)){
				isLeader += " (isGroupLeader=1 and type='编码人员') ";
				hasLeader = true;
				continue;
			}
			hasType = true;
			if(index==0){
				typeConditions += "'"+_type+"'";
			}else{
				typeConditions += ",'"+_type+"'";
			}
			index++;
		}
		typeConditions += ")";
		if(hasLeader&&!hasType){
			sql += " and "+isLeader;
		}else if(!hasLeader&&hasType){
			sql += " and "+typeConditions;
		}else if(hasLeader&&hasType){
			sql += " and ("+isLeader+" or "+typeConditions+")";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@Override
	public double getUsedWorkHour(String versionId) {
		String sql = "SELECT\n" +
				"	sum(score)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceSubRequirement sub\n" +
				"WHERE\n" +
				"	sub.requirementId IN (\n" +
				"		SELECT\n" +
				"			id\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceRequirement\n" +
				"		WHERE\n" +
				"			projectVersionId = "+versionId+"\n" +
				"		AND isDelete = 0 and `status`!='"+Constants.DELETE+"'\n" +
				"	)\n" +
				"AND sub.isDeleted = 0";
		Object obj = baseDao.getUniqueResult(sql);

		return Double.parseDouble(obj==null ? "0":obj+"");
	}


	@Override
	public InputStream generateVersionInfoFile(String versionId, String projectName) throws Exception {
		ProjectVersionEntity version = getProjectVersion(versionId);
		String title = "软件开发部"+projectName+"本周开发需求    版本号"+version.getVersion();
		String beginDateStr = version.getBeginDate();
		String endDateStr = version.getEndDate();
		int days = 0;
		if(null!=beginDateStr && null!=endDateStr){
			Date beginDate = DateUtil.getSimpleDate(beginDateStr);
			Date endDate = DateUtil.getSimpleDate(endDateStr);
			days = Integer.parseInt((endDate.getTime()-beginDate.getTime())/(60*60*1000*24)+1+"");
		}
		int developerNum = version.getDeveloperNum()==null ? 0:version.getDeveloperNum();
		double workHour = Double.parseDouble(version.getWorkHour()==null ? "0":version.getWorkHour());
		double totalWorkHour = developerNum*workHour*days;
		String versionInfo = "版本开发时间："+(beginDateStr==null?"":beginDateStr)+"到"+(endDateStr==null?"":endDateStr)+"   【每日"+workHour+"时，共"+days+"日，共"+developerNum+"人，总共"+totalWorkHour+"工时】";
		String[] colTitles = {"NO","需求项任务项","预计工时","编码负责","测试负责","实施负责","属主","签名"};
		//版本对应已分解完成的需求
		List<VersionRequirementVo> versionRequirementLst = getVersionRequirementInfo(versionId, projectName);
		String fileName = UUID.randomUUID()+"-"+projectName+"-"+version.getVersion()+".xls";
		writeExcel(fileName, title, versionInfo, colTitles, versionRequirementLst);
		File file = new File(Constants.PRODUCT_FILE_DIRECTORY, fileName);
		InputStream inputStream = new FileInputStream(file);
		return inputStream;
	}
	@SuppressWarnings("deprecation")
	private void writeExcel(String fileName, String title,
			String versionInfo, String[] colTitles, List<VersionRequirementVo> versionRequirementLst){
		//创建一个空excel文件
		Workbook workbook=new HSSFWorkbook();
		Sheet worksheet = workbook.createSheet("版本信息"); 
		// 设置列宽    
		worksheet.setColumnWidth(0, 2000);    
		worksheet.setColumnWidth(1, 6500);    
		worksheet.setColumnWidth(2, 10000);    
		worksheet.setColumnWidth(3, 3500);    
		worksheet.setColumnWidth(4, 3500);    
		worksheet.setColumnWidth(5, 3500);    
		worksheet.setColumnWidth(6, 3500);    
		worksheet.setColumnWidth(7, 3500);
		worksheet.setColumnWidth(8, 3500);
		//设置字体    
		Font headfont = workbook.createFont();    
		headfont.setFontName("宋体");    
		headfont.setFontHeightInPoints((short) 11);// 字体大小    
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗    
		//头部样式   
		CellStyle headstyle = workbook.createCellStyle();    
		headstyle.setFont(headfont);    
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		headstyle.setLocked(true);    
		headstyle.setWrapText(true);// 自动换行 
		//副头部样式   
		CellStyle copyHeadstyle = workbook.createCellStyle();    
		copyHeadstyle.setFont(headfont);    
		copyHeadstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左右居中    
		copyHeadstyle.setLocked(true);    
		copyHeadstyle.setWrapText(true);// 自动换行
		//列头字体
		Font columnHeadFont = workbook.createFont();    
		columnHeadFont.setFontName("宋体");    
		columnHeadFont.setFontHeightInPoints((short) 10);    
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		//列表样式
		CellStyle columnHeadStyle = workbook.createCellStyle(); 
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中   
		columnHeadStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		columnHeadStyle.setFillForegroundColor(HSSFColor.YELLOW.index); 
		columnHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
		//普通单元格样式
		CellStyle gridStyle = workbook.createCellStyle();
		gridStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		gridStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		gridStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
		gridStyle.setWrapText(true);// 自动换行 
		//创建标题行
		Row rowTitle = worksheet.createRow(0);
		Cell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellValue(title);
		cellTitle.setCellStyle(headstyle);
		worksheet.addMergedRegion(new CellRangeAddress(0,0,0,8));// 设置单元格合并
		//版本信息
		Row rowVersionInfo = worksheet.createRow(1);
		Cell cellVersionInfo = rowVersionInfo.createCell(0);
		cellVersionInfo.setCellValue(versionInfo);
		cellVersionInfo.setCellStyle(copyHeadstyle);
		worksheet.addMergedRegion(new CellRangeAddress(1,1,0,8));

		Row rowColTiTle = worksheet.createRow(2);
		int index = 0;
		//写入列名
		for(String colTitle: colTitles){
			//第2列和第3列合并，列是空白的
			if(index==2){
				Cell cell = rowColTiTle.createCell(index);
				cell.setCellStyle(columnHeadStyle);
				index=3;
			}
			Cell cell = rowColTiTle.createCell(index);
			cell.setCellValue(colTitle);
			cell.setCellStyle(columnHeadStyle);
			index++;
		}
		worksheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
		//表格内容从第4行开始
		int rowNum = 3;
		//任务序号
		int taskIndex = 1;
		//每个需求对应的任务数
		int taskNum = 0;
		index = 1;
		for(VersionRequirementVo requirementVo: versionRequirementLst){
			taskNum = requirementVo.getTaskNum();
			Row row = worksheet.createRow(rowNum);
			//NO
			Cell cellNO = row.createCell(0);
			cellNO.setCellValue(taskIndex);
			cellNO.setCellStyle(gridStyle);
			//需求
			Cell cellRequire = row.createCell(1);
			cellRequire.setCellValue(requirementVo.getRequirementName());
			cellRequire.setCellStyle(gridStyle);
			//任务
			Cell cellTask = row.createCell(2);
			cellTask.setCellValue(requirementVo.getTaskName());
			cellTask.setCellStyle(gridStyle);
			//预计工时
			Cell cellTime = row.createCell(3);
			cellTime.setCellValue(requirementVo.getEstimatedTime());
			cellTime.setCellStyle(gridStyle);
			//编码负责
			Cell cellCode = row.createCell(4);
			cellCode.setCellValue(requirementVo.getCodeHeader());
			cellCode.setCellStyle(gridStyle);
			//测试负责
			Cell cellTest = row.createCell(5);
			cellTest.setCellValue(requirementVo.getTestHeader());
			cellTest.setCellStyle(gridStyle);
			//实施负责
			Cell cellAct = row.createCell(6);
			cellAct.setCellValue(requirementVo.getActHeader());
			cellAct.setCellStyle(gridStyle);
			//属主
			Cell cellOwner = row.createCell(7);
			cellOwner.setCellValue(requirementVo.getOwner());
			cellOwner.setCellStyle(gridStyle);
			//签名
			Cell cellAsign = row.createCell(8);
			cellAsign.setCellStyle(gridStyle);
			//若相同需求的任务已录入完成，则把相同的需求行合并单元格
			if(taskNum == index){
				if(taskNum>1){
					worksheet.addMergedRegion(new CellRangeAddress(rowNum-taskNum+1, rowNum, 1, 1));
				}
				index = 0;
			}
			taskIndex++;
			rowNum++;
			index++;
		}
		File file = new File(Constants.PRODUCT_FILE_DIRECTORY, fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				workbook.write(fos);
				fos.close();  
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
	}


	@Override
	public List<VersionRequirementVo> getVersionRequirementInfo(String versionId, String projectName) {
		String headerSql = "select userName, type from OA_SoftGroup where project='"+projectName+"' "
				+ "and isGroupLeader='1' and isDeleted=0";
		List<Object> headerObj = baseDao.findBySql(headerSql);
		Map<String, String> headerMap = new HashMap<>();
		for(Object obj: headerObj){
			Object[] objs = (Object[]) obj;
			String userName = objs[0]+"";
			String type = objs[1]+"";
			headerMap.put(type, userName);
		}
		String sql = "SELECT\n" +
				"	requirement.`name` requirementName,\n" +
				"	count(requirement.`name`)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceSubRequirement\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"	) sub\n" +
				"INNER JOIN OA_SoftPerformanceRequirement requirement ON sub.requirementId = requirement.id\n" +
				"WHERE\n" +
				"	requirement.projectVersionId = "+versionId+"\n" +
				"AND requirement.isDelete = 0\n" +
				"AND requirement.`status` != '"+Constants.DELETE+"'\n" +
				"AND requirement.divide = '1'\n"+
				"group by requirementName";
		List<Object> countObjLst = baseDao.findBySql(sql);
		Map<String, Integer> requireAndTaskNumMap = new HashMap<>();
		for(Object countObj: countObjLst){
			Object[] objs = (Object[])countObj;
			String requireName = objs[0]+"";
			Integer taskNum = objs[1]==null ? 0:Integer.parseInt(objs[1]+"");
			requireAndTaskNumMap.put(requireName, taskNum);
		}
		sql = "SELECT\n" +
				"	requirement.`name` requirementName,\n" +
				"	sub.subRequirementName,\n" +
				"	sub.score,\n" +
				"	requirement.ownerName\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceSubRequirement\n" +
				"		WHERE\n" +
				"			isDeleted = 0\n" +
				"	) sub\n" +
				"INNER JOIN OA_SoftPerformanceRequirement requirement ON sub.requirementId = requirement.id\n" +
				"WHERE\n" +
				"	requirement.projectVersionId = "+versionId+"\n" +
				"AND requirement.isDelete = 0\n" +
				"AND requirement.`status` != '"+Constants.DELETE+"'\n" +
				"AND requirement.divide = '1'\n"+
				"order by requirementName;";
		List<Object> objLst = baseDao.findBySql(sql);
		List<VersionRequirementVo> versionRequirementLst = new ArrayList<>();
		for(Object obj: objLst){
			VersionRequirementVo versionVo = new VersionRequirementVo();
			Object[] objs = (Object[])obj;
			String requirementName = objs[0]+"";
			versionVo.setRequirementName(requirementName);
			Integer taskNum = requireAndTaskNumMap.get(requirementName);
			versionVo.setTaskNum(taskNum);
			versionVo.setTaskName(objs[1]+"");
			versionVo.setEstimatedTime(objs[2]+"");
			versionVo.setOwner(objs[3]+"");
			versionVo.setActHeader(headerMap.get("实施"));
			versionVo.setCodeHeader(headerMap.get("编码人员"));	
			versionVo.setTestHeader(headerMap.get("软件测试"));
			versionRequirementLst.add(versionVo);
		}
		return versionRequirementLst;
	}


	@Override
	public void saveReturnReason(String requirementId, String returnReason) {
		String sql = "update OA_SoftPerformanceRequirement set back = 1, returnReason = '"+returnReason+"' where id="+requirementId;
		baseDao.excuteSql(sql);

	}


	@Override
	public boolean checkIsDivide(String reqiureId) {
		String sql = "SELECT\n" +
				"	count(sub.id)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceRequirement requirement\n" +
				"INNER JOIN OA_SoftPerformanceSubRequirement sub ON requirement.id = sub.requirementId\n" +
				"WHERE\n" +
				"	requirement.id = "+reqiureId;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectVersionEntity> getProjectVersionLstByRequireId(String projectId) {
		String hql = 
				"FROM\n" +
						"	ProjectVersionEntity version\n" +
						"WHERE\n" +
						"	version.projectId = (\n" +
						"		SELECT\n" +
						"			projectId\n" +
						"		FROM\n" +
						"			RequirementEntity\n" +
						"		WHERE\n" +
						"			id = "+projectId+")\n" +
						"			and isDelete = 0\n" +
						"			and status is null";
		return (List<ProjectVersionEntity>)baseDao.hqlfind(hql);
	}


	@Override
	public void updateRequireVersion(String requireId, String versionId) {
		String sql = "update OA_SoftPerformanceRequirement set projectVersionId="+versionId+" where id="+requireId;
		baseDao.excuteSql(sql);
	}


	@Override
	public List<Object> getHoursByPerson(String versionId) {
		String sql = "SELECT\n" +
				"	SUM(score),\n" +
				"	staff.StaffName\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			sub.score,\n" +
				"			task.assignerId userId\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceFunction task\n" +
				"		INNER JOIN OA_SoftPerformanceSubRequirement sub ON task.subRequirementId = sub.id\n" +
				"		WHERE\n" +
				"			task.projectVersionId = "+versionId+"\n" +
				"		AND task.isDelete = 0\n" +
				"		AND sub.isDeleted = 0\n" +
				"		AND (\n" +
				"			task.result != 31 || task.result IS NULL\n" +
				"		)\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				problem.score,\n" +
				"				developerId userId\n" +
				"			FROM\n" +
				"				OA_ProblemOrder problem\n" +
				"			WHERE\n" +
				"				problem.isDeleted = 0 and problem.projectVersionId="+versionId+"\n" +
				"			AND score IS NOT NULL\n" +
				"	) a,\n" +
				"	OA_Staff staff\n" +
				"WHERE\n" +
				"	a.userId = staff.UserID\n" +
				"GROUP BY\n" +
				"	a.userId";
		return baseDao.findBySql(sql);
	}


	@Override
	public boolean checkRequireIsAllot(String requireId) {
		String sql = "SELECT\n" +
				"	count(id)\n" +
				"FROM\n" +
				"	OA_SoftPerformanceFunction `function`\n" +
				"WHERE\n" +
				"	requirementId = "+requireId+"\n" +
				"AND IFNULL(result, 0) != 31\n" +
				"AND isDelete = 0";
		if(Integer.parseInt(baseDao.getUniqueResult(sql)+"")>0){
			return true;
		}
		return false;
	}


	@Override
	public void updateRequireStatus(String requireId, String status) {
		String sql = "update OA_SoftPerformanceRequirement set stage='"+status+"' where id="+requireId;
		baseDao.excuteSql(sql);
	}


	@Override
	public String getRequireIdByInstanceId(String instanceId) {
		String sql = "select requirementId from OA_SoftPerformanceFunction where instanceId='"+instanceId+"'";
		return baseDao.getUniqueResult(sql)+"";
	}


	@Override
	public void deleteSubRequirement(String requireId) {
		String sql = "update OA_SoftPerformanceSubRequirement set isDeleted=1 where requirementId="+requireId;
		baseDao.excuteSql(sql);
	}


	@Override
	public void updateRequireDivide(String requireId, int i) {
		String sql = "update OA_SoftPerformanceRequirement set divide='"+i+"' where id="+requireId;
		baseDao.excuteSql(sql);
	}


	@Override
	public void updateTaskVersion(String requireId, String versionId) {
		String sql = "update OA_SoftPerformanceFunction set projectVersionId="+versionId+" where requirementId="+requireId;
		baseDao.excuteSql(sql);
	}


	@Override
	public List<String> getInstanceIdListByRequireId(String requireId) {
		List<String> instanceIdList = new ArrayList<>();
		String sql = "select instanceId from OA_SoftPerformanceFunction where isDelete=0 and (IFNULL(result,0)!=31&&IFNULL(result,0)!=29&&IFNULL(result,0)!=30&&IFNULL(result,0)!=6) and requirementId="+requireId;
		List<Object> objLst = baseDao.findBySql(sql);
		for(Object obj: objLst){
			instanceIdList.add(obj+"");
		}
		return instanceIdList;
	}


	@Override
	public void updateSubRequireDeveloper(String developer, String subRequirementId) {
		String sql = "update OA_SoftPerformanceSubRequirement set developer='"+developer+"' where id="+subRequirementId;
		baseDao.excuteSql(sql);
	}


	@Override
	public List<StaffVO> getSoftPersonsByVersion(SoftPosition type, String versionId) {
		List<StaffVO> staffs = new ArrayList<>();
		String sql = "";
		switch (type.name()) {
		case "产品经理":
			sql = "select pms from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "需求分析":
			sql = "select fenXis from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "编码人员":
			sql = "select developers from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "软件测试":
			sql = "select testers from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "实施":
			sql = "select shiShis from OA_SoftPerformanceVersion where id="+versionId;
			break;
		default:
			break;
		}
		Object obj = baseDao.getUniqueResult(sql);
		if(null != obj){
			String[] staffIds = (obj+"").split(",");
			for(String staffId: staffIds){
				if(StringUtils.isNotBlank(staffId)){
					StaffVO staff = staffService.getStaffByUserID(staffId.trim());
					if(null != staff){
						staffs.add(staff);
					}
				}
			}
		}
		return staffs;
	}


	@Override
	public List<PerformanceVo> getPerformanceVos(List<Object> list, String year, String month) {
		List<PerformanceVo> performances = new ArrayList<>();
		for(Object obj: list){
			Object[] objs = (Object[])obj;
			PerformanceVo performance = new PerformanceVo();
			performance.setStaffName(objs[0]+"");
			String userId = objs[1]+"";
			performance.setUserId(userId);
			String totalScores = objs[2]==null ? "" : objs[2]+"";
			performance.setTotalScores(totalScores);
			//月度得分
			double scoresByMonth = Double.parseDouble(totalScores);
			//基数分（满分）
			double baseScores = 0;
			//按版本统计分数
			Map<String, Double> totalScoresByVersion = getTotalScoresByVersion(userId, year, month);

			Iterator<Entry<String, Double>> ite = totalScoresByVersion.entrySet().iterator();

			while(ite.hasNext()){
				Entry<String, Double> entry = ite.next();

				String versionIdAndDuty = entry.getKey();
				String[] versionIdAndDutyArray = versionIdAndDuty.split(",");
				//版本id
				String versionId = versionIdAndDutyArray[0];
				//职责
				String duty = versionIdAndDutyArray[1];
				ProjectVersionEntity version = getProjectVersion(versionId);
				Integer projectId = version.getProjectId();
				int developerNum = version.getDeveloperNum()==null ? 0:version.getDeveloperNum();
				double workHour = Double.parseDouble(version.getWorkHour()==null ? "0":version.getWorkHour());
				String beginDateStr = version.getBeginDate();
				String endDateStr = version.getEndDate();
				int days = 0;
				if(null!=beginDateStr && null!=endDateStr){
					Date beginDate = DateUtil.getSimpleDate(beginDateStr);
					Date endDate = DateUtil.getSimpleDate(endDateStr);
					days = Integer.parseInt((endDate.getTime()-beginDate.getTime())/(60*60*1000*24)+"")+1;
				}
				//版本分配的总分
				double totalWorkHour = developerNum*workHour*days;
				ProjectVO project = getProject(projectId+"");
				double percent = 0;
				//当前版本分配的该职责的人数
				int personNum = 1;
				//double problemScore = 0;
				switch(duty){
				case "项目经理":
					percent = project.getJl();
					personNum = getSoftPersonNumByVersion(SoftPosition.产品经理, versionId);
					break;
				case "需求":
					percent= project.getXq();
					personNum = getSoftPersonNumByVersion(SoftPosition.需求分析, versionId);
					break;
				case "组长":
					percent = project.getZz();
					break;
				case "开发人员":
					percent = project.getKf();
					personNum = getSoftPersonNumByVersion(SoftPosition.编码人员, versionId);
					break;
				case "测试":
					percent = project.getCs();
					personNum = getSoftPersonNumByVersion(SoftPosition.软件测试, versionId);
					break;
				case "实施":
					percent = project.getSs();
					personNum = getSoftPersonNumByVersion(SoftPosition.实施, versionId);
					break;
					/*				case "问题单":
					problemScore = getProblemOrderWorkHourByUserId(versionId, userId);
					break;*/
				default:break;
				}
				//这边作了修改，问题单的解决人员加分，问题单的责任人减分，基数分数不变
				//baseScores += (totalWorkHour*(percent/100))/personNum+problemScore;
				//检查是否有未完成的bug单
				//List<ProblemOrderEntity> problemOrders = getUnCompleteProblemOrdersByUserId(userId, versionId);
				//for(ProblemOrderEntity problemOrder: problemOrders){
				//	baseScores += Double.parseDouble(problemOrder.getScore());
				//}
				baseScores += (totalWorkHour*(percent/100))/personNum;
			}
			double kValue = 0;
			if(baseScores != 0){
				kValue = scoresByMonth/baseScores;
			}else{
				kValue = 1;
			}
			BigDecimal b = new BigDecimal(kValue);  
			kValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
			performance.setKValue(kValue+"");
			performances.add(performance);
		}
		return performances;
	}

	private Integer getSoftPersonNumByVersion(SoftPosition type, String versionId) {
		String sql = "";
		switch (type.name()) {
		case "产品经理":
			sql = "select pmNum from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "需求分析":
			sql = "select fenXiNum from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "编码人员":
			sql = "select developerNum from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "软件测试":
			sql = "select testerNum from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "实施":
			sql = "select shiShiNum from OA_SoftPerformanceVersion where id="+versionId;
			break;
		default:
			break;
		}
		Object obj = baseDao.getUniqueResult(sql);
		return (Integer)obj;
	}


	private Map<String, Double> getTotalScoresByVersion(String userId, String year, String month) {
		/*		String sql = "SELECT\n" +
				"	sum(sc.resultScore),v.id,sc.duty,v.projectId\n" +
				"FROM\n" +
				"	OA_SoftPerformanceScoreResult sc,\n" +
				"	OA_SoftPerformanceFunction f,\n" +
				"	OA_SoftPerformanceVersion v\n" +
				"WHERE\n" +
				"	sc.taskId = f.id\n" +
				"	AND f.projectVersionId = v.id\n" +
				"and  YEAR (v.endDate) = "+year+"\n" +
				"and sc.isDeleted=0 and f.isDelete = 0\n" +
				"AND MONTH(v.endDate) = "+month+"\n" +
				"AND sc.userId='"+userId+"'\n" +
				"GROUP BY\n" +
				"	v.id,sc.duty\n"+
				"ORDER BY v.projectId,v.id";*/
		String sql = "SELECT\n" +
				"	sum(resultScore),\n" +
				"	versionId,\n" +
				"	duty,\n" +
				"	projectId\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			sc.resultScore,\n" +
				"			v.id versionId,\n" +
				"			sc.duty,\n" +
				"			v.projectId\n" +
				"		FROM\n" +
				"			OA_SoftPerformanceScoreResult sc,\n" +
				"			OA_SoftPerformanceFunction f,\n" +
				"			OA_SoftPerformanceVersion v\n" +
				"		WHERE\n" +
				"			sc.taskId = f.id\n" +
				"		AND f.projectVersionId = v.id\n" +
				"and  YEAR (v.endDate) = "+year+"\n" +
				"		AND sc.isDeleted = 0\n" +
				"		AND f.isDelete = 0\n" +
				"AND MONTH(v.endDate) = "+month+"\n" +
				"		AND sc.userId = '"+userId+"'\n" +
				"		UNION ALL\n" +
				"			SELECT\n" +
				"				sc.resultScore,\n" +
				"				v.id,\n" +
				"				sc.duty,\n" +
				"				v.projectId\n" +
				"			FROM\n" +
				"				OA_SoftPerformanceScoreResult sc,\n" +
				"				OA_ProblemOrder p,\n" +
				"				OA_SoftPerformanceVersion v\n" +
				"			WHERE\n" +
				"				sc.problemTaskId = p.id\n" +
				"			AND p.projectVersionId = v.id\n" +
				"			AND sc.isDeleted = 0\n" +
				"and  YEAR (v.endDate) = "+year+"\n" +
				"AND MONTH(v.endDate) = "+month+"\n" +
				"AND sc.userId='"+userId+"'\n" +
				"	) score\n" +
				"GROUP BY\n" +
				"	score.versionId,\n" +
				"	score.duty\n" +
				"ORDER BY\n" +
				"	score.projectId,\n" +
				"	score.versionId";
		List<Object> objList = baseDao.findBySql(sql);
		Map<String, Double> totalScoresByVersion = new LinkedHashMap<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			String totalScores = objs[0]==null ? "" : objs[0]+"";
			double scoresByMonth = Double.parseDouble(totalScores);
			String versionId = objs[1]+"";
			String duty = objs[2]+"";
			totalScoresByVersion.put(versionId+","+duty, scoresByMonth);
		}
		return totalScoresByVersion;
	}


	@Override
	public List<PerformanceVo> getBaseScoresDetail(String year, String month, String userId) {
		List<PerformanceVo> performanceVos = new ArrayList<>();
		//按版本统计分数
		Map<String, Double> totalScoresByVersion = getTotalScoresByVersion(userId, year, month);
		Iterator<Entry<String, Double>> ite = totalScoresByVersion.entrySet().iterator();
		while(ite.hasNext()){
			PerformanceVo performanceVo = new PerformanceVo();
			Entry<String, Double> entry = ite.next();
			String versionIdAndDuty = entry.getKey();
			performanceVo.setGetScores(entry.getValue());
			String[] versionIdAndDutyArray = versionIdAndDuty.split(",");
			//版本id
			String versionId = versionIdAndDutyArray[0];
			//职责
			String duty = versionIdAndDutyArray[1];
			performanceVo.setDuty(duty);
			ProjectVersionEntity version = getProjectVersion(versionId);
			performanceVo.setVersionName(version.getVersion());
			Integer projectId = version.getProjectId();
			performanceVo.setProjectName(getProject(projectId+"").getName());
			int developerNum = version.getDeveloperNum()==null ? 0:version.getDeveloperNum();
			double workHour = Double.parseDouble(version.getWorkHour()==null ? "0":version.getWorkHour());
			String beginDateStr = version.getBeginDate();
			String endDateStr = version.getEndDate();
			int days = 0;
			if(null!=beginDateStr && null!=endDateStr){
				Date beginDate = DateUtil.getSimpleDate(beginDateStr);
				Date endDate = DateUtil.getSimpleDate(endDateStr);
				days = Integer.parseInt((endDate.getTime()-beginDate.getTime())/(60*60*1000*24)+"")+1;
			}
			//版本分配的总分
			double totalWorkHour = developerNum*workHour*days;
			performanceVo.setVersionScores(totalWorkHour+"");
			ProjectVO project = getProject(projectId+"");
			double percent = 0;
			//当前版本分配的该职责的人数
			int personNum = 1;
			double problemScore = 0;
			switch(duty){
			case "项目经理":
				percent = project.getJl();
				personNum = getSoftPersonNumByVersion(SoftPosition.产品经理, versionId);
				List<StaffVO> pms = getSoftPersonsByVersion(SoftPosition.产品经理, versionId);
				String pmNames = "";
				for(int i=0;i<pms.size();i++){
					if(i==0){
						pmNames += pms.get(i).getLastName();
					}else{
						pmNames += ", "+pms.get(i).getLastName();
					}
				}
				performanceVo.setStaffNames(pmNames);
				break;
			case "需求":
				percent= project.getXq();
				personNum = getSoftPersonNumByVersion(SoftPosition.需求分析, versionId);
				List<StaffVO> fenXis = getSoftPersonsByVersion(SoftPosition.需求分析, versionId);
				String fenXiNames = "";
				for(int i=0;i<fenXis.size();i++){
					if(i==0){
						fenXiNames += fenXis.get(i).getLastName();
					}else{
						fenXiNames += ", "+fenXis.get(i).getLastName();
					}
				}
				performanceVo.setStaffNames(fenXiNames);
				break;
			case "组长":
				percent = project.getZz();
				performanceVo.setStaffNames(staffService.getRealNameByUserId(userId));
				break;
			case "开发人员":
				percent = project.getKf();
				personNum = getSoftPersonNumByVersion(SoftPosition.编码人员, versionId);
				List<StaffVO> developers = getSoftPersonsByVersion(SoftPosition.编码人员, versionId);
				String developerNames = "";
				for(int i=0;i<developers.size();i++){
					if(i==0){
						developerNames += developers.get(i).getLastName();
					}else{
						developerNames += ", "+developers.get(i).getLastName();
					}
				}
				performanceVo.setStaffNames(developerNames);
				break;
			case "测试":
				percent = project.getCs();
				personNum = getSoftPersonNumByVersion(SoftPosition.软件测试, versionId);
				List<StaffVO> testers = getSoftPersonsByVersion(SoftPosition.软件测试, versionId);
				String testerNames = "";
				for(int i=0;i<testers.size();i++){
					if(i==0){
						testerNames += testers.get(i).getLastName();
					}else{
						testerNames += ", "+testers.get(i).getLastName();
					}
				}
				performanceVo.setStaffNames(testerNames);
				break;
			case "实施":
				percent = project.getSs();
				personNum = getSoftPersonNumByVersion(SoftPosition.实施, versionId);
				List<StaffVO> shiShis = getSoftPersonsByVersion(SoftPosition.实施, versionId);
				String shiShiNames = "";
				for(int i=0;i<shiShis.size();i++){
					if(i==0){
						shiShiNames += shiShis.get(i).getLastName();
					}else{
						shiShiNames += ", "+shiShis.get(i).getLastName();
					}
				}
				performanceVo.setStaffNames(shiShiNames);
				break;
			case "问题单":
				problemScore = getProblemOrderWorkHourByUserId(versionId, userId);
				break;
			default:break;
			}
			performanceVo.setPercent(percent+"%");
			if(problemScore!=0){
				//问题单的得分，不算在基数里面
				performanceVo.setBaseScores(0);
			}else{
				double baseScores = (totalWorkHour*(percent/100))/personNum;
				BigDecimal b = new BigDecimal(baseScores);  
				baseScores = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
				performanceVo.setBaseScores(baseScores);
			}
			performanceVos.add(performanceVo);
			//检查是否有未完成的bug单
			/*			List<ProblemOrderEntity> problemOrders = getUnCompleteProblemOrdersByUserId(userId, versionId);
			for(ProblemOrderEntity problemOrder: problemOrders){
				PerformanceVo _PerformanceVo = new PerformanceVo();
				_PerformanceVo.setDuty(SoftPerformanceScore.问题单.name());
				_PerformanceVo.setVersionName(version.getVersion());
				_PerformanceVo.setProjectName(getProject(projectId+"").getName());
				_PerformanceVo.setBaseScores(Double.parseDouble(problemOrder.getScore()));
				_PerformanceVo.setGetScores(0);
				performanceVos.add(_PerformanceVo);
			}*/
		}
		return performanceVos;
	}


	@Override
	public List<PerformanceVo> getPerformanceVos(String userId, List<Object> list, String year) {
		List<PerformanceVo> performances = new ArrayList<>();
		for(Object obj: list){
			Object[] objs = (Object[])obj;
			PerformanceVo performance = new PerformanceVo();
			performance.setUserId(userId);
			String totalScores = objs[1]==null ? "" : objs[1]+"";
			performance.setTotalScores(totalScores);
			String month = objs[2]==null ? "" : objs[2]+"";
			performance.setMonth(month);
			//月度得分
			double scoresByMonth = Double.parseDouble(totalScores);
			//基数分（满分）
			double baseScores = 0;
			//按版本统计分数
			Map<String, Double> totalScoresByVersion = getTotalScoresByVersion(userId, year, month);

			Iterator<Entry<String, Double>> ite = totalScoresByVersion.entrySet().iterator();
			while(ite.hasNext()){
				Entry<String, Double> entry = ite.next();
				String versionIdAndDuty = entry.getKey();
				String[] versionIdAndDutyArray = versionIdAndDuty.split(",");
				//版本id
				String versionId = versionIdAndDutyArray[0];

				//职责
				String duty = versionIdAndDutyArray[1];
				ProjectVersionEntity version = getProjectVersion(versionId);
				Integer projectId = version.getProjectId();
				int developerNum = version.getDeveloperNum()==null ? 0:version.getDeveloperNum();
				double workHour = Double.parseDouble(version.getWorkHour()==null ? "0":version.getWorkHour());
				String beginDateStr = version.getBeginDate();
				String endDateStr = version.getEndDate();
				int days = 0;
				if(null!=beginDateStr && null!=endDateStr){
					Date beginDate = DateUtil.getSimpleDate(beginDateStr);
					Date endDate = DateUtil.getSimpleDate(endDateStr);
					days = Integer.parseInt((endDate.getTime()-beginDate.getTime())/(60*60*1000*24)+"")+1;
				}
				//版本分配的总分
				double totalWorkHour = developerNum*workHour*days;
				ProjectVO project = getProject(projectId+"");
				double percent = 0;
				//当前版本分配的该职责的人数
				int personNum = 1;
				//double problemScore = 0;
				switch(duty){
				case "项目经理":
					percent = project.getJl();
					personNum = getSoftPersonNumByVersion(SoftPosition.产品经理, versionId);
					break;
				case "需求":
					percent= project.getXq();
					personNum = getSoftPersonNumByVersion(SoftPosition.需求分析, versionId);
					break;
				case "组长":
					percent = project.getZz();
					break;
				case "开发人员":
					percent = project.getKf();
					personNum = getSoftPersonNumByVersion(SoftPosition.编码人员, versionId);
					break;
				case "测试":
					percent = project.getCs();
					personNum = getSoftPersonNumByVersion(SoftPosition.软件测试, versionId);
					break;
				case "实施":
					percent = project.getSs();
					personNum = getSoftPersonNumByVersion(SoftPosition.实施, versionId);
					break;
					/*				case "问题单":
					problemScore = getProblemOrderWorkHourByUserId(versionId, userId);
					break;*/
				default:break;
				}
				baseScores += (totalWorkHour*(percent/100))/personNum;
				/*				//检查是否有未完成的bug单
				List<ProblemOrderEntity> problemOrders = getUnCompleteProblemOrdersByUserId(userId, versionId);
				for(ProblemOrderEntity problemOrder: problemOrders){
					baseScores += Double.parseDouble(problemOrder.getScore());
				}*/
			}
			double kValue = 0;
			if(baseScores != 0){
				kValue = scoresByMonth/baseScores;
			}else{
				kValue = 1;
			}
			BigDecimal b = new BigDecimal(kValue);  
			kValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
			performance.setKValue(kValue+"");
			performances.add(performance);
		}
		return performances;
	}


	/*	@SuppressWarnings("unchecked")
	private List<ProblemOrderEntity> getUnCompleteProblemOrdersByUserId(String userId, String versionId) {
		String hql = "from ProblemOrderEntity where isDeleted=0 and processStatus is null and developerId='"+
	                 userId+"' and projectVersionId="+versionId;
		return (List<ProblemOrderEntity>) baseDao.hqlfind(hql);
	}*/


	@Override
	public void startProblemOrder(ProblemOrderVo problemOrderVo, String userId) throws Exception {
		problemOrderVo.setCreatorId(userId);
		File[] attachment = problemOrderVo.getAttachment();
		String[] attachmentFileName = problemOrderVo.getAttachmentFileName();
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.PRODUCT_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						Constants.PRODUCT_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				problemOrderVo.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		List<String> projectManagers = permissionService.findUsersByPermissionCode(Constants.PROJECT_MANAGER);
		if(projectManagers.size()<1){
			throw new RuntimeException("找不到负责分配问题单的人员，请联系系统管理员配置");
		}
		//根据项目的最新版本
		int latestVersionId = getProjectLatestVersionId(problemOrderVo.getProjectId());
		problemOrderVo.setProjectVersionId(latestVersionId);
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", problemOrderVo);
		vars.put("projectManager", projectManagers);
		vars.put("applyer", userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.PROBLEM_ORDER);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		// 完成任务
		taskService.complete(task.getId(), vars);
		ProblemOrderEntity problemOrder = (ProblemOrderEntity) CopyUtil.tryToEntity(problemOrderVo, ProblemOrderEntity.class);
		problemOrder.setIsDeleted(0);
		problemOrder.setAddTime(new Date());
		problemOrder.setProcessInstanceID(processInstance.getId());
		baseDao.hqlSave(problemOrder);
	}
	@Override
	public Integer getProjectLatestVersionId(Integer projectId) {
		String sql = "select id from OA_SoftPerformanceVersion where isDelete=0 and projectId="+projectId+" ORDER BY addTime desc limit 0,1";
		return (Integer)baseDao.getUniqueResult(sql);
	}


	@Override
	public ListResult<Object> findProblemOrderList(String projectId, String status, String problemOrderName, int limit,
			int page) {
		String sql = "SELECT\n" +
				"	project.`name`,\n" +
				"	version.version,\n" +
				"	problem.orderName,\n" +
				"	staff.StaffName,\n" +
				"	_staff.StaffName _StaffName,\n" +
				"	problem.addTime,\n" +
				"	problem.id,\n" +
				"	problem.processInstanceID,\n" +
				"	problem.processStatus\n" +
				"FROM\n" +
				"	OA_ProblemOrder problem,\n" +
				"	OA_Staff staff,\n" +
				"	OA_Staff _staff,\n" +
				"	OA_SoftPerformanceProject project,\n" +
				"	OA_SoftPerformanceVersion `version`\n" +
				"WHERE\n" +
				"	problem.creatorId = staff.UserID\n" +
				"AND problem.questionerId = _staff.UserID\n" +
				"AND problem.isDeleted = 0\n" +
				"AND problem.projectId = project.id\n" +
				"AND problem.projectVersionId = `version`.id\n";
		if(StringUtils.isNotBlank(projectId)){
			sql += "AND problem.projectId="+projectId+"\n";
		}
		if(StringUtils.isNotBlank(status)){
			sql += "AND IFNULL(problem.processStatus, 0)="+status+"\n";
		}
		if(StringUtils.isNotBlank(problemOrderName)){
			sql += "AND problem.orderName like '%"+problemOrderName+"%'\n";
		}
		sql += "order by problem.addTime desc";
		List<Object> objList = baseDao.findPageList(sql, page, limit);
		String sqlCount = "select count(id) from OA_ProblemOrder where isDeleted=0\n";
		if(StringUtils.isNotBlank(projectId)){
			sqlCount += "AND projectId="+projectId+"\n";
		}
		if(StringUtils.isNotBlank(status)){
			sqlCount += "AND IFNULL(processStatus, 0)="+status+"\n";
		}
		if(StringUtils.isNotBlank(problemOrderName)){
			sqlCount += "AND orderName like '%"+problemOrderName+"%'";
		}
		int totalCount = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(objList, totalCount);
	}


	@Override
	public Object getProblemOrderById(String problemOrderId) {
		String sql = "SELECT\n" +
				"	project.`name`,\n" +
				"	version.version,\n" +
				"	problem.orderName,\n" +
				"	staff.StaffName,\n" +
				"	problem.addTime,\n" +
				"	problem.score,\n" +
				"	problem.description,\n" +
				"	problem.attachmentIds,\n" +
				"	(select s.staffName from OA_Staff s where problem.developerId=s.userId) developer,\n" +
				"	(select s.staffName from OA_Staff s where problem.questionerId=s.userId) questioner,\n" +
				"	(select s.staffName from OA_Staff s where problem.dutyPersonId=s.userId) dutyPerson\n" +
				"FROM\n" +
				"	OA_ProblemOrder problem,\n" +
				"	OA_Staff staff,\n" +
				"	OA_SoftPerformanceProject project,\n" +
				"	OA_SoftPerformanceVersion `version`\n" +
				"WHERE\n" +
				"	problem.creatorId = staff.UserID\n" +
				"AND problem.isDeleted = 0\n" +
				"AND problem.projectId = project.id\n" +
				"AND problem.projectVersionId = `version`.id\n" +
				"AND problem.id="+problemOrderId;

		return baseDao.getUniqueResult(sql);
	}


	@Override
	public List<ProblemOrderVo> getProblemOrdersByInstanceId(List<Task> problemOrderTasks) {
		List<ProblemOrderVo> problemOrderTaskVos = new ArrayList<>();
		for(Task problemOrderTask: problemOrderTasks){
			ProblemOrderEntity problemOrderEntity = getProblemOrderByInstanceId(problemOrderTask.getProcessInstanceId());
			ProblemOrderVo problemOrderVo = (ProblemOrderVo) CopyUtil.tryToVo(problemOrderEntity, ProblemOrderVo.class);
			problemOrderVo.setTaskId(problemOrderTask.getId());
			problemOrderVo.setCreatorName(staffDao.getStaffByUserID(problemOrderVo.getCreatorId()).getStaffName());
			problemOrderVo.setTaskName(problemOrderTask.getName());
			problemOrderVo.setQuestionerName(staffDao.getStaffByUserID(problemOrderVo.getQuestionerId()).getStaffName());
			problemOrderTaskVos.add(problemOrderVo);
		}
		return problemOrderTaskVos;
	}


	private ProblemOrderEntity getProblemOrderByInstanceId(String processInstanceId) {
		String hql = "from ProblemOrderEntity where isDeleted=0  and processInstanceID="+processInstanceId;
		return (ProblemOrderEntity) baseDao.hqlfindUniqueResult(hql);
	}


	@Override
	public void startAllocateProblemOrder(String developerId, String score, String taskId,
			String processInstanceId, String userId, String dutyPersonId) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("problem", "0");
		vars.put("developer", developerId);
		identityService.setAuthenticatedUserId(userId);
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId, vars);
		String sql = "update OA_ProblemOrder set score="+score+",developerId='"+developerId+"',dutyPersonId='"+dutyPersonId+"' where processInstanceID="+processInstanceId;
		baseDao.excuteSql(sql);
		ProblemOrderEntity problemOrder = getProblemOrderByInstanceId(processInstanceId);
		//责任人扣分
		ScoreResultEntity scoreResult = new ScoreResultEntity();
		scoreResult.setVersionId(String.valueOf(problemOrder.getProjectVersionId()));
		ProjectVersionEntity version = getProjectVersion(String.valueOf(problemOrder.getProjectVersionId()));
		scoreResult.setItemDate(DateUtil.getSimpleDate(version.getEndDate()));
		scoreResult.setResultScore(0-Double.parseDouble(problemOrder.getScore()));
		scoreResult.setAddTime(new Date());
		scoreResult.setReason(problemOrder.getOrderName());
		scoreResult.setUserId(problemOrder.getDutyPersonId());
		scoreResult.setIsDeleted(0);
		baseDao.hqlSave(scoreResult);
	}


	@Override
	public void saveProblemOrderScore(String processInstanceId) {
		ProblemOrderEntity problemOrder = getProblemOrderByInstanceId(processInstanceId);
		ScoreResultEntity score = new ScoreResultEntity();
		score.setAddTime(new Date());
		score.setItemDate(new Date());
		score.setResultScore(Double.parseDouble(problemOrder.getScore()));
		score.setProblemTaskId(problemOrder.getId());
		score.setDuty(SoftPerformanceScore.问题单.name());
		score.setUserId(problemOrder.getDeveloperId());
		score.setIsDeleted(0);
		baseDao.hqlSave(score);
	}


	@Override
	public void updateProblemOrderStatus(String processInstanceId, String result) {
		String sql = "update OA_ProblemOrder set processStatus="+result+" where processInstanceID="+processInstanceId;
		baseDao.excuteSql(sql);
	}


	@Override
	public double getProblemOrderWorkHour(String versionId) {
		String sql = "SELECT\n" +
				"	IFNULL(sum(score),0)\n" +
				"FROM\n" +
				"	OA_ProblemOrder problem\n" +
				"WHERE problem.isDeleted=0\n" +
				"AND problem.projectVersionId = "+versionId;
		return Double.parseDouble(baseDao.getUniqueResult(sql)+"");
	}
	@Override
	public double getProblemOrderWorkHourByUserId(String versionId, String userId){
		String sql = "SELECT\n" +
				"	IFNULL(sum(score),0)\n" +
				"FROM\n" +
				"	OA_ProblemOrder problem\n" +
				"WHERE problem.isDeleted=0\n" +
				"AND problem.projectVersionId = "+versionId+"\n" +
				"AND problem.developerId='"+userId+"'";
		return Double.parseDouble(baseDao.getUniqueResult(sql)+"");
	}


	@Override
	public Integer getProjectIdByName(String projectName) {
		String sql = "select id from OA_SoftPerformanceProject where name='"+projectName+"' and isDelete=0 limit 0,1";
		return (Integer)baseDao.getUniqueResult(sql);
	}


	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ProblemOrderVo> getProblemOrderListByUserId(int page,
			int limit, String userId, String status) {
		String hql = "from ProblemOrderEntity where isDeleted=0 and creatorId='"+userId+"' ";
		String sqlCount = "select count(id) from OA_ProblemOrder where isDeleted=0 and creatorId='"+userId+"' ";
		//0:处理中；1：已处理；2：待验收；默认：全部
		switch(status){
		case "0":
			hql += " and processStatus is null";
			sqlCount += " and processStatus is null";
			break;
		case "1":
			hql += " and processStatus=1";
			sqlCount += " and processStatus=1";
			break;
		default:break;
		}
		hql += " order by addTime";
		List<ProblemOrderEntity> problemOrders = (List<ProblemOrderEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<ProblemOrderVo> problemOrderTaskVos = new ArrayList<>();
		for(ProblemOrderEntity problemOrder: problemOrders){
			ProblemOrderVo problemOrderVo = (ProblemOrderVo) CopyUtil.tryToVo(problemOrder, ProblemOrderVo.class);
			problemOrderVo.setCreatorName(staffDao.getStaffByUserID(problemOrderVo.getCreatorId()).getStaffName());
			problemOrderVo.setQuestionerName(staffDao.getStaffByUserID(problemOrderVo.getQuestionerId()).getStaffName());
			problemOrderTaskVos.add(problemOrderVo);
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(problemOrderTaskVos, count);
	}

	@Override
	public ProblemOrderVo getProblemOrderByProcessInstanceId(String instanceId) {
		String sql = "SELECT\n" +
				"	problem.orderName,\n" +
				"	staff.StaffName,\n" +
				"	problem.addTime,\n" +
				"	problem.description,\n" +
				"	problem.attachmentIds,\n" +
				"	(select s.staffName from OA_Staff s where problem.questionerId=s.userId) questioner\n" +
				"FROM\n" +
				"	OA_ProblemOrder problem,\n" +
				"	OA_Staff staff,\n" +
				"	OA_SoftPerformanceProject project\n" +
				"WHERE\n" +
				"	problem.creatorId = staff.UserID\n" +
				"AND problem.isDeleted = 0\n" +
				"AND problem.projectId = project.id\n" +
				"AND problem.processInstanceID="+instanceId;
		Object[] obj = (Object[]) baseDao.getUniqueResult(sql);
		ProblemOrderVo problemOrderVo = new ProblemOrderVo();
		problemOrderVo.setOrderName((String)obj[0]);
		problemOrderVo.setCreatorName((String)obj[1]);
		problemOrderVo.setAddTime((Date)obj[2]);
		problemOrderVo.setDescription((String)obj[3]);
		problemOrderVo.setAttachmentIds((String)obj[4]);
		problemOrderVo.setQuestionerName((String)obj[5]);
		return problemOrderVo;
	}
	@Override
	public void changeProblemToRequire(String taskId, String processInstanceId, String userId) {
		ProblemOrderEntity problemOrder = getProblemOrderByInstanceId(processInstanceId);
		RequirementEntity require = new RequirementEntity();
		require.setAddTime(new Date());
		String attachmentIdStr = problemOrder.getAttachmentIds();
		List<String> attachmentNames = new ArrayList<>();
		List<String> attachmentPaths = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			String[] attachmentIds = attachmentIdStr.split(",");
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				attachmentNames.add(attachment.getSoftName());
				attachmentPaths.add(attachment.getSoftURL());
				require.setAttachmentName(StringUtils.join(attachmentNames, "#@#&"));
				require.setAttachmentPath(StringUtils.join(attachmentPaths, "#@#&"));
			}
		}
		//require.setCreatorId(problemOrder.getCreatorId());
		require.setCreatorId(userId);
		require.setDescription(problemOrder.getDescription());
		require.setIsDelete(0);
		require.setName(problemOrder.getOrderName());
		require.setOwnerId(problemOrder.getQuestionerId());
		require.setOwnerName(staffService.getStaffByUserId(problemOrder.getQuestionerId()).getStaffName());
		require.setPriority("高");
		require.setStatus(Constants.NOT_ACTIVE);
		require.setStage(Constants.READY_DEVELOP);
		require.setDivide(0);
		require.setProjectId(problemOrder.getProjectId());
		baseDao.hqlSave(require);
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("problem", "1");
		identityService.setAuthenticatedUserId(userId);
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId, vars);
		updateProblemOrderStatus(processInstanceId, String.valueOf(TaskResultEnum.AGREE.getValue()));
	}


	@Override
	public List<String> getSoftPersonUserIdsByVersion(SoftPosition type, String versionId) {
		List<String> userIds = new ArrayList<>();
		String sql = "";
		switch (type.name()) {
		case "产品经理":
			sql = "select pms from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "需求分析":
			sql = "select fenXis from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "编码人员":
			sql = "select developers from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "软件测试":
			sql = "select testers from OA_SoftPerformanceVersion where id="+versionId;
			break;
		case "实施":
			sql = "select shiShis from OA_SoftPerformanceVersion where id="+versionId;
			break;
		default:
			break;
		}
		Object obj = baseDao.getUniqueResult(sql);
		if(null != obj){
			String[] staffIds = (obj+"").split(",");
			for(String staffId: staffIds){
				if(StringUtils.isNotBlank(staffId)){
					userIds.add(staffId.trim());
				}
			}
		}
		return userIds;
	}
	
	@Override
	public void endProblemOrderEntityById(Integer id) {
		String sql ="UPDATE oa_problemorder\n" +
				"SET oa_problemorder.processStatus = '31'\n" +
				"WHERE\n" +
				"	oa_problemorder.id = "+id;
		baseDao.excuteSql(sql);
	}
	
	@Override
	public void forcedTerminationTask(String userId,Integer id,String processInstanceID) {
		try {
			this.endProblemOrderEntityById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceID).singleResult();
		taskService.setAssignee(task.getId(), userId);
		runtimeService.deleteProcessInstance(processInstanceID, "强制结束");
	}


	
}
