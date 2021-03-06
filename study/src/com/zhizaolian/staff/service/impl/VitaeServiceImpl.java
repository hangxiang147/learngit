package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.JobEntity;
import com.zhizaolian.staff.entity.VitaeEntity;
import com.zhizaolian.staff.entity.VitaeResultEntity;
import com.zhizaolian.staff.entity.VitaeSignEduEntity;
import com.zhizaolian.staff.entity.VitaeSignEntity;
import com.zhizaolian.staff.entity.VitaeSignFamilyEntity;
import com.zhizaolian.staff.entity.VitaeSignJobHistoryEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VitaeService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.VitaeTaskVo;
import com.zhizaolian.staff.vo.VitaeVo;

import net.sf.json.JSONArray;

public class VitaeServiceImpl implements VitaeService {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private StaffService staffService;
	@Autowired 
	private ProcessService processService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private PermissionService permissionService;
	@Override
	public ListResult<JobEntity> getJobEntityListMore(String name, int page,
			int limit) {
		String sqlList = "from JobEntity c where  c.isDelete=0 order by c.addTime desc";
		String sqlCount = "select count(*) from JobEntity c where  c.isDelete=0 ";
		if (StringUtils.isNotBlank(name)) {
			sqlList = "from JobEntity c where c.jobName like'%" + name
					+ "%' and c.isDelete=0 order by c.addTime desc";
			sqlCount = "select count(*) from JobEntity c where c.jobName like'%"
					+ name + "%' and c.isDelete=0 )";
		}
		Object result = baseDao.hqlPagedFind(sqlList, page, limit);
		Integer count = ((Long) baseDao.hqlfindUniqueResult(sqlCount))
				.intValue();
		@SuppressWarnings("unchecked")
		List<JobEntity> list = (List<JobEntity>) result;
		List<JobEntity> resultList=new ArrayList<JobEntity>();
		if(CollectionUtils.isNotEmpty(list)){
			for (JobEntity jobEntity : list) {
				JobEntity jobEntity2=(JobEntity) CopyUtil.tryToEntity(jobEntity, JobEntity.class);
				String tids=jobEntity.getTechnologySubjectPersonId();
				String ids=jobEntity.getSubjectPersonId();
				if(StringUtils.isNotBlank(ids)){
					String[] idsArr=ids.split(",",-1);
					String tName="";
					for (String string : idsArr) {
						if(StringUtils.isNotBlank(string)){
							List<GroupDetailVO> glist=staffService.findGroupDetailsByUserID(string);
							tName+=staffService.getRealNameByUserId(string);
							if(CollectionUtils.isNotEmpty(glist)){
								GroupDetailVO groupDetailVO=glist.get(0);
								tName+="("+groupDetailVO.getCompanyName()+"-"+groupDetailVO.getDepartmentName()+"-"+groupDetailVO.getPositionName()+")";
							}
							tName+="</br>";
						}
					}
					jobEntity2.setSubjectPersonNames(tName);
				}
				if(StringUtils.isNotBlank(tids)){
					String[] idsArr=tids.split(",",-1);
					String tName="";
					for (String string : idsArr) {
						if(StringUtils.isNotBlank(string)){
							List<GroupDetailVO> glist=staffService.findGroupDetailsByUserID(string);
							tName+=staffService.getRealNameByUserId(string);
							if(CollectionUtils.isNotEmpty(glist)){
								GroupDetailVO groupDetailVO=glist.get(0);
								tName+="("+groupDetailVO.getCompanyName()+"-"+groupDetailVO.getDepartmentName()+"-"+groupDetailVO.getPositionName()+")";
							}
							tName+="\n";
						}
					}
					jobEntity2.setTechnologySubjectPersonNames(tName);
				}
				resultList.add(jobEntity2);
			}
		}
		return new ListResult<JobEntity>(resultList, count);
	}

	@Override
	public ListResult<JobEntity> getJobEntityList(String name, int page,
			int limit) {
		String sqlList = "from JobEntity c where  c.isDelete=0 order by c.addTime desc";
		String sqlCount = "select count(*) from JobEntity c where  c.isDelete=0 ";
		if (StringUtils.isNotBlank(name)) {
			sqlList = "from JobEntity c where c.jobName like'%" + name
					+ "%' and c.isDelete=0 order by c.addTime desc";
			sqlCount = "select count(*) from JobEntity c where c.jobName like'%"
					+ name + "%' and c.isDelete=0 )";
		}
		Object result = baseDao.hqlPagedFind(sqlList, page, limit);
		Integer count = ((Long) baseDao.hqlfindUniqueResult(sqlCount))
				.intValue();
		@SuppressWarnings("unchecked")
		List<JobEntity> list = (List<JobEntity>) result;
		return new ListResult<JobEntity>(list, count);
	}

	@Override
	public JobEntity getJobEntityById(String id) {
		return (JobEntity) baseDao
				.hqlfindUniqueResult("from JobEntity c where c.id=" + id);
	}


	@Override
	public void addJob(JobEntity jobEntity) {
		jobEntity.setAddTime(new Date());
		jobEntity.setIsDelete(0);
		baseDao.hqlSave(jobEntity);
	}

	@Override
	public void updateJob(JobEntity jobEntity) {
		jobEntity.setUpdateTime(new Date());
		baseDao.hqlUpdate(jobEntity);
	}

	@Override
	public void deleteJob(String id) {
		JobEntity jobEntity=getJobEntityById(id);
		jobEntity.setIsDelete(1);
		baseDao.hqlUpdate(jobEntity);
	}

	@Override
	public void startVitae(VitaeVo vitaeVo, File[] files, String fileDetail) {
		vitaeVo.setBusinessType(BusinessTypeEnum.VITAE.getName());
		vitaeVo.setTitle(vitaeVo.getRequestUserName() + "的"
				+ BusinessTypeEnum.VITAE.getName());
		
		//修改requestUser的名称 后缀加上 部门
		List<GroupDetailVO> groups=staffService.findGroupDetailsByUserID(vitaeVo.getRequestUserId());
		if(CollectionUtils.isNotEmpty(groups)){
			GroupDetailVO g0=groups.get(0);
			vitaeVo.setUserName(vitaeVo.getRequestUserName()+"("+g0.getCompanyName()+"-"+g0.getDepartmentName()+"-"+g0.getPositionName()+")");
		}
		Map<String, Object> vars = new HashMap<>();
		vitaeVo.setRequestDate(DateUtil.formateDate(new Date()));
		vars.put("arg", vitaeVo);
		//String manager = staffService.queryManager(vitaeVo.getRequestUserId());
		List<String> generalManagers = permissionService.findUsersByPermissionCode(Constants.GENERAL_MANAGER);
		if(CollectionUtils.isEmpty(generalManagers)){
			throw new RuntimeException("未找到总公司总经理");
		}
        //只寻找一级
		String supervisor = staffService.querySupervisorOneStep(vitaeVo.getRequestUserId());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(vitaeVo.getRequestUserId());
		} 
		if (StringUtils.isBlank(supervisor) || vitaeVo.getRequestUserId().equals(supervisor)) {
			supervisor=staffService.querySupervisor(vitaeVo.getRequestUserId());
		}
		List<String> hrGroupList = staffService
		.queryHRGroupList(Integer.parseInt(vitaeVo.getPostCompanyId()));
		List<String> hrLeaders = permissionService
				.findUsersByPermissionCode(Constants.HR_LEADER);
		if (CollectionUtils.isEmpty(hrGroupList)
				|| CollectionUtils.isEmpty(hrLeaders)) {
			throw new RuntimeException("未找到人事领导！");
		}
		String hrLeader = hrLeaders.get(0);
		vars.put("hrGroup", hrGroupList);
		vars.put("companyLeader", generalManagers.get(0));
		vars.put("hrleader", hrLeader);
		vars.put("supervisor", supervisor);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("Vitae");
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		taskService.setAssignee(task.getId(), vitaeVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveVitae(vitaeVo, processInstance.getId());
	}

	private void saveVitae(VitaeVo vitaeVo, String instanceId) {
		VitaeEntity vitaeEntity = (VitaeEntity) CopyUtil.tryToEntity(vitaeVo,
				VitaeEntity.class);
		vitaeEntity.setInstanceId(instanceId);
		vitaeEntity.setAddTime(new Date());
		vitaeEntity.setIsDeleted(0);
		baseDao.hqlSave(vitaeEntity);
	}

	@Override
	public VitaeEntity getVitaeEntityById(String id) {
		return (VitaeEntity) baseDao
				.hqlfindUniqueResult("from VitaeEntity c where c.id=" + id);
	}

	@Override
	public VitaeEntity getVitaeEntityByIntstanceId(String id) {
		return (VitaeEntity) baseDao
		.hqlfindUniqueResult("from VitaeEntity c where c.instanceId=" + id);
	}

	@Override
	public ListResult<VitaeVo> getVitaeByKeys(String requestUserId, int page,
			int limit) {
		String basic = "from VitaeEntity c where 1=1 ";
		if (StringUtils.isNotBlank(requestUserId)) {
			basic += " and c.requestUserId='" + requestUserId + "'";
		}
		basic += " and c.isDeleted=0 ";
		String listSql = basic + " order by c.addTime desc ";
		String countSql = " select count(*)  " + basic;

		Object result = baseDao.hqlPagedFind(listSql, page, limit);
		Integer count = ((Long) baseDao.hqlfindUniqueResult(countSql))
				.intValue();
		@SuppressWarnings("unchecked")
		List<VitaeEntity> handleList = (List<VitaeEntity>) result;
		List<VitaeVo> resultList = new ArrayList<>();
		for (VitaeEntity entity : handleList) {
			VitaeVo vitaeVo = (VitaeVo) CopyUtil.tryToVo(entity, VitaeVo.class);
			vitaeVo.setUserName(staffService
					.getRealNameByUserId(vitaeVo.getRequestUserId()));
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(vitaeVo.getInstanceId()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					VitaeVo arg = (VitaeVo) variable.getValue();
					vitaeVo.setRequestDate(arg.getRequestDate());
					vitaeVo.setTitle(arg.getTitle());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(vitaeVo.getInstanceId()).singleResult();
			if (pInstance != null) {
				vitaeVo.setStatus("处理中");
				vitaeVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				vitaeVo.setStatus(
						TaskResultEnum.valueOf(vitaeVo.getResult()).getName());
			}
			resultList.add(vitaeVo);
		}
		return new ListResult<VitaeVo>(resultList, count);
	}

	@Override
	public void setVitaeResultByInstanceId(String instanceId, Integer result) {
		String sql = "update VitaeEntity v set v.result=" + result
				+ "where v.instanceId='" + instanceId + "'";
		baseDao.excuteSql(sql);
	}

	@Override
	public ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDs(
			List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			int page, int limit) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
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
		String sql = "SELECT DISTINCT\n" + "	task.ID_,\n"
				+ "	task.PROC_INST_ID_,\n" + "	task.NAME_,\n"
				+ "	task.TASK_DEF_KEY_,\n" + "	vitae.PostName\n" + "FROM\n"
				+ "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<VitaeTaskVo> taskVOs = createVitaeTaskList(result);
		sql = "SELECT \n" + "	count(DISTINCT(task.ID_)) \n" + "FROM\n" + "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<VitaeTaskVo>(taskVOs, count);
	}

	private List<VitaeTaskVo> createVitaeTaskList(List<Object> vitaes) {
		List<VitaeTaskVo> taskVOs = new ArrayList<VitaeTaskVo>();
		for (Object task : vitaes) {
			Object[] objs = (Object[]) task;
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId((String) objs[1]).singleResult();
			// 查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(),
					"arg");
			VitaeTaskVo vitaeTaskVo = new VitaeTaskVo();
			vitaeTaskVo.setProcessInstanceID((String) objs[1]);
			vitaeTaskVo.setRequestUserName(arg.getUserName());
			vitaeTaskVo.setRequestDate(arg.getRequestDate());
			vitaeTaskVo.setTaskID((String) objs[0]);
			vitaeTaskVo.setTaskName((String) objs[2]);
			vitaeTaskVo.setTaskDefKey((String) objs[3]);
			vitaeTaskVo.setTitle(arg.getTitle());
			vitaeTaskVo.setPostName((String) objs[4]);
			if(objs.length>5)
				vitaeTaskVo.setGuessTime(objs[5]==null?"":objs[5]+"" );
			taskVOs.add(vitaeTaskVo);
		}
		return taskVOs;
	}

	@Override
	public void completeVitae(String instanceId, String name, String postKey,
			String ids, String names,String tids, String tnames) {
		VitaeEntity vitaeEntity=(VitaeEntity) baseDao.hqlfindUniqueResult("from VitaeEntity c where c.instanceId='"+instanceId+"' ");
		vitaeEntity.setRealNeedPersonDescription(postKey);
		vitaeEntity.setRealPostName(name);
		vitaeEntity.setRealSubjectPersonIds(ids);
		vitaeEntity.setRealTechnologySubjectPersonIds(tids);
		baseDao.hqlSave(vitaeEntity);
	}

	@Override
	public void updateVitaeStatusByInstanceId(String instanceId,
			Integer result) {
		VitaeEntity vitaeEntity=(VitaeEntity) baseDao.hqlfindUniqueResult("from VitaeEntity c where c.instanceId='"+instanceId+"' ");
		vitaeEntity.setResult(result);
		baseDao.hqlSave(vitaeEntity);
	}

	@Override
	public VitaeSignEntity VitaeSignEntity(String id) {
		String hql="from VitaeSignEntity v where v.id="+id+" and v.isDeleted=0";
		return (VitaeSignEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VitaeSignEntity> VitaeSignEntityByVitaeId(String id) {
		String hql="from VitaeSignEntity v where v.vitaeId="+id+" and v.isDeleted=0";
		return (List<VitaeSignEntity>) baseDao.hqlfind(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VitaeResultEntity> getVitaeResultListBySignId(String signId) {
		return (List<VitaeResultEntity>) baseDao.hqlfind("from VitaeResultEntity v where v.vitaeSignId="+signId+" and v.isDeleted=0 order by v.addTime");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VitaeSignEduEntity> getVitaeSignEduEntityByVitaeId(String id) {
		return (List<VitaeSignEduEntity>) baseDao.hqlfind("from VitaeSignEduEntity v where v.vitaeSignId="+id+" and v.isDeleted=0 order by v.sortIndex");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VitaeSignFamilyEntity> getVitaeSignFamilyEntityByVitaeId(
			String id) {
		return (List<VitaeSignFamilyEntity>) baseDao.hqlfind("from VitaeSignFamilyEntity v where v.vitaeSignId="+id+" and v.isDeleted=0 order by v.sort");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VitaeSignJobHistoryEntity> getVitaeSignJobHistoryEntityByVitaeId(
			String id) {
		return (List<VitaeSignJobHistoryEntity>) baseDao.hqlfind("from VitaeSignJobHistoryEntity v where v.vitaeSignId="+id+" and v.isDeleted=0 order by v.endTime desc");
	}

	@Override
	public int commonSave(Object obj) {
		return baseDao.hqlSave(obj);
	}
	
	@Override
	public ListResult<VitaeSignEntity> getPagedVitaeByName(String name,
			int page, int limit) {
		String sqlList = "from VitaeSignEntity c where 1=1";
		if(StringUtils.isNotBlank(name)){
			sqlList+=" and c.xm like '%"+name +"%'";
		}
		sqlList+=" and c.isDeleted=0 order by c.addTime desc";
		String sqlCount = "select count(*) from VitaeSignEntity c where 1=1  ";
		if(StringUtils.isNotBlank(name)){
		sqlCount+=" and c.xm like '%"+name +"%'";
		}
		sqlCount+=" and c.isDeleted=0 ";
		Object result = baseDao.hqlPagedFind(sqlList, page, limit);
		Integer count = ((Long) baseDao.hqlfindUniqueResult(sqlCount))
				.intValue();
		@SuppressWarnings("unchecked")
		List<VitaeSignEntity> resultList = (List<VitaeSignEntity>) result;
		return new ListResult<VitaeSignEntity>(resultList, count);
	}

	@Override
	public void clearSignLinkTable(String id){
		String hql0="update VitaeSignEduEntity v set v.isDeleted=0 where v.vitaeSignId="+id ;
		String hql1="update VitaeSignFamilyEntity v set v.isDeleted=0 where v.vitaeSignId="+id ;
		String hql2="update VitaeSignJobHistoryEntity v set v.isDeleted=0 where v.vitaeSignId="+id ;
		baseDao.excuteHql(hql0);
		baseDao.excuteHql(hql1);
		baseDao.excuteHql(hql2);
		
	}

	@Override
	public synchronized int getLastBh() {
		String sql=" select max(bh) from OA_vitaeSign ";
		try{
			return (Integer) baseDao.getUniqueResult(sql);
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public void commonUpdate(Object obj) {
		 baseDao.hqlUpdate(obj);	
	}

	@Override
	public int getEffectiveTaskId(List<TaskDefKeyEnum> tasks,
			List<Group> groups, List<String> users,int stepIndex) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
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
		String sql = "SELECT DISTINCT\n" + "task.ID_\n"
				+ "FROM\n"
				+ "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) "
				+"and not EXISTS(select id from OA_VitaeResult v where   v.nextTaskId=task.ID_)";
		
		List<Object> result = baseDao.findBySql(sql);
		return Integer.parseInt(result.get(result.size()-1)+"");
	}

	@Override
	public int findVitaeVoByUserGroupIDsCount(List<TaskDefKeyEnum> tasks,
			List<Group> groups, List<String> users, String intsanceId) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
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
		String sql = "SELECT \n" + "	count(*)\n" + "FROM\n" + "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))"
				+" and task.PROC_INST_ID_="+intsanceId;
		Object countObj = baseDao.getUniqueResult(sql);		
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return count;
	}

	@Override
	public ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDsByStep(
			List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			int page, int limit, int step) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
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
		String sql = "SELECT DISTINCT\n" + "	task.ID_,\n"
				+ "	task.PROC_INST_ID_,\n" + "	task.NAME_,\n"
				+ "	task.TASK_DEF_KEY_,\n" + "	vitae.PostName\n" + "FROM\n"
				+ "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<VitaeTaskVo> taskVOs = createVitaeTaskList(result);
		for(VitaeTaskVo vitaeTaskVo:taskVOs){
			@SuppressWarnings("unchecked")
			List<VitaeResultEntity> vitaeResultEntities=(List<VitaeResultEntity>)baseDao.hqlfind("from VitaeResultEntity v where v.nextTaskId="+vitaeTaskVo.getTaskID());
			if(vitaeResultEntities!=null&&vitaeResultEntities.size()>0){
				vitaeTaskVo.setVitaeResultId(vitaeResultEntities.get(0).getId());
				vitaeTaskVo.setVitaeSignId(vitaeResultEntities.get(0).getVitaeSignId());
				VitaeSignEntity vitaeSignEntity=(VitaeSignEntity) baseDao.hqlfindUniqueResult(" from VitaeSignEntity e where e.id="+vitaeResultEntities.get(0).getVitaeSignId());
				if(vitaeSignEntity!=null){
					vitaeTaskVo.setItemPersonName(vitaeSignEntity.getXm());
					vitaeTaskVo.setItemPersonTelephone(vitaeSignEntity.getLxfs());
				}
			}
		}
		sql = "SELECT \n" + "	count(DISTINCT(task.ID_)) \n" + "FROM\n" + "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<VitaeTaskVo>(taskVOs, count);
	}

	@Override
	public ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDsByStepForNew(
			List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			int page, int limit, int step) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
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
		String sql = "SELECT DISTINCT\n" + "	task.ID_,\n"
				+ "	task.PROC_INST_ID_,\n" + "	task.NAME_,\n"
				+ "	task.TASK_DEF_KEY_,\n" + "	vitae.PostName\n ,v.guessVitaeTime " + "FROM\n"
				+ "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n,OA_VitaeResult v\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n and task.ID_=v.nextTaskId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		List<Object> result = baseDao.findPageList(sql+"order by v.guessVitaeTime  ", page, limit);
		List<VitaeTaskVo> taskVOs = createVitaeTaskList(result);
		for(VitaeTaskVo vitaeTaskVo:taskVOs){
			@SuppressWarnings("unchecked")
			List<VitaeResultEntity> vitaeResultEntities=(List<VitaeResultEntity>)baseDao.hqlfind("from VitaeResultEntity v where v.nextTaskId="+vitaeTaskVo.getTaskID());
			if(vitaeResultEntities!=null&&vitaeResultEntities.size()>0){
				vitaeTaskVo.setVitaeResultId(vitaeResultEntities.get(0).getId());
				vitaeTaskVo.setVitaeSignId(vitaeResultEntities.get(0).getVitaeSignId());
				VitaeSignEntity vitaeSignEntity=(VitaeSignEntity) baseDao.hqlfindUniqueResult(" from VitaeSignEntity e where e.id="+vitaeResultEntities.get(0).getVitaeSignId());
				if(vitaeSignEntity!=null){
					vitaeTaskVo.setItemPersonName(vitaeSignEntity.getXm());
					vitaeTaskVo.setItemPersonTelephone(vitaeSignEntity.getLxfs());
				}
								
			}
		}
		sql = "SELECT \n" + "	count(DISTINCT(task.ID_)) \n" + "FROM\n" + "	ACT_RU_TASK task,\n"
				+ "	ACT_RU_IDENTITYLINK identityLink,\n"
				+ "	OA_Vitae vitae\n" + "WHERE\n"
				+ "	task.ID_ = identityLink.TASK_ID_\n"
				+ "AND task.PROC_INST_ID_ = vitae.InstanceId\n"
				+ "AND identityLink.TYPE_ = 'candidate'"
				+ " and task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<VitaeTaskVo>(taskVOs, count);
	}

	@Override
	public void outOfTime(String resultId,String userID) {
		VitaeResultEntity vitaeResultEntity=(VitaeResultEntity) baseDao.hqlfindUniqueResult("from VitaeResultEntity v where v.id="+resultId);
		vitaeResultEntity.setType(0);
		vitaeResultEntity.setStep1Time(new Date());
		vitaeResultEntity.setStep1UserId(userID);
		commonSave(vitaeResultEntity);
	}

	@Override
	public VitaeResultEntity VitaeResultEntityByTaskId(String taskId) {
		return (VitaeResultEntity) baseDao.hqlfindUniqueResult("from VitaeResultEntity v where v.nextTaskId="+taskId);

	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<Object> getVitaeResultEntityByName(String name,
			String userId, int page, int limit) {
		String sqlList = " select s.xm,v.scoreResult,v.subjectResult,v.type,v.subjectPersonsId,v.technologySubjectPersonId from OA_VitaeResult v , OA_VitaeSign s where v.vitaeSignId=s.id ";
		if(StringUtils.isNotBlank(name)){
			sqlList+=" and s.xm  like '"+name+"' " ;
		}
		sqlList+="and ( v.subjectPersonsId like '%"+userId+"%' or v.technologySubjectPersonId like '%"+userId+"%') and v.type !='0' and v.type !='1'  order by v.addTime desc ";
		String sqlCount = " select count(*) from OA_VitaeResult v , OA_VitaeSign s where v.vitaeSignId=s.id ";
		if(StringUtils.isNotBlank(name)){
			sqlCount+=" and s.xm  like '"+name+"' " ;
		}
		sqlCount+="and ( v.subjectPersonsId like '%"+userId+"%' or v.technologySubjectPersonId like '%"+userId+"%') and v.type !='0' and v.type !='1'   ";
		Object result = baseDao.findPageList(sqlList, page, limit);
		Integer count = ((BigInteger) baseDao.getUniqueResult(sqlCount))
				.intValue();
		return new ListResult<Object>((List<Object>)result, count);
	}

	@Override
	public List<String> findEntryingStaffs() {
		String sql = "SELECT\n" +
				"	b.xm\n" +
				"FROM\n" +
				"	OA_VitaeResult a,\n" +
				"	OA_VitaeSign b\n" +
				"WHERE\n" +
				"	a.type = 2\n" +
				"AND a.vitaeSignId = b.id\n" +
				"AND b.isDeleted = 0\n" +
				"AND a.isDeleted = 0";
		List<Object> entryingStaffObjs = baseDao.findBySql(sql);
		List<String> entryingStaffs = new ArrayList<>();
		for(Object obj: entryingStaffObjs){
			entryingStaffs.add((String)obj);
		}
		return entryingStaffs;
	}

	
}
