package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.BrandAuthEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.BrandAuthService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.BrandAuthVo;

public class BrandAuthServiceImpl implements BrandAuthService {
	
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ProcessService processService;
	@Override
	public void startBrandAuth(BrandAuthVo brandAuthVo) {
		String enumName = BusinessTypeEnum.BRAND_AUTH.getName();
		brandAuthVo.setBusinessType(enumName);
		brandAuthVo.setTitle(brandAuthVo.getUserName() + "发起的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		List<String> marketManagers = permissionService
				.findUsersByPermissionCode(Constants.MARKET_MANAGER);
		if(marketManagers.size()>0){
			vars.put("marketManager", marketManagers.get(0));
		}else{
			throw new RuntimeException("未找到市场总监，请联系系统管理员配置");
		}
		vars.put("arg", brandAuthVo);
		vars.put("applyer", brandAuthVo.getUserId());
		//启动流程
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.BRAND_AUTH);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), brandAuthVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveBrandAuthInfo(brandAuthVo, processInstance.getId());
		
	}

	private void saveBrandAuthInfo(BrandAuthVo brandAuthVo, String processInstanceId) {
		BrandAuthEntity brandAuthEntity = null;
		try {
			brandAuthEntity = (BrandAuthEntity) CopyUtil.tryToEntity(brandAuthVo, BrandAuthEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		brandAuthEntity.setProcessInstanceID(processInstanceId);
		brandAuthEntity.setAddTime(new Date());
		brandAuthEntity.setIsDeleted(0);
		baseDao.hqlSave(brandAuthEntity);
	}

	@Override
	public List<BrandAuthVo> getBrandAuthTasksByInstanceId(List<Task> brandAuthTasks) {
		List<BrandAuthVo> brandAuthVos = new ArrayList<>();
		for(Task brandAuthTask: brandAuthTasks){
			BrandAuthEntity brandAuthEntity = getBrandAuthByInstanceId(brandAuthTask.getProcessInstanceId());
			BrandAuthVo brandAuthVo = (BrandAuthVo) CopyUtil.tryToVo(brandAuthEntity, BrandAuthVo.class);
			brandAuthVo.setTaskName(brandAuthTask.getName());
			brandAuthVo.setTaskId(brandAuthTask.getId());
			brandAuthVo.setUserName(staffDao.getStaffByUserID(brandAuthVo.getUserId()).getStaffName());
			brandAuthVos.add(brandAuthVo);
		}
		return brandAuthVos;
	}
	@Override
	public BrandAuthEntity getBrandAuthByInstanceId(String processInstanceId) {
		String hql = "from BrandAuthEntity where processInstanceID="+processInstanceId;
		return (BrandAuthEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public BrandAuthVo getBrandAuthVoByInstanceId(String processInstanceID) {
		BrandAuthEntity brandAuth = getBrandAuthByInstanceId(processInstanceID);
		BrandAuthVo brandAuthVo = (BrandAuthVo) CopyUtil.tryToVo(brandAuth, BrandAuthVo.class);
		brandAuthVo.setUserName(staffDao.getStaffByUserID(brandAuthVo.getUserId()).getStaffName());
		return brandAuthVo;
	}

	@Override
	public void updateProcessStatus(String processInstanceID, String result) {
		String sql = "update OA_BrandAuth set processStatus="+result+" where processInstanceID="+processInstanceID;
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateChopProcessInstanceId(String processInstanceId, String chopProcessInstanceId) {
		String sql = "update OA_BrandAuth set chopProcessInstanceID="+chopProcessInstanceId+" where processInstanceId="+processInstanceId;
		baseDao.excuteSql(sql);
	}

	@Override
	public ListResult<BrandAuthVo> findBrandAuthListByUserID(String userId, Integer page, Integer limit) {
		List<BrandAuthEntity> brandAuths = getBrandAuthByUserId(userId,
				page, limit);

		List<BrandAuthVo> brandAuthVos = new ArrayList<BrandAuthVo>();
		for (BrandAuthEntity brandAuth : brandAuths) {
			BrandAuthVo brandAuthVo = (BrandAuthVo) CopyUtil.tryToVo(brandAuth, BrandAuthVo.class);
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(brandAuthVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				brandAuthVo.setStatus("处理中");
				brandAuthVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = brandAuthVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						brandAuthVo.setStatus(t.getName());
				}
			}
			brandAuthVos.add(brandAuthVo);
		}
		int count = getBrandAuthCountByUserId(userId);
		return new ListResult<BrandAuthVo>(brandAuthVos, count);
	}

	private int getBrandAuthCountByUserId(String userId) {
		String hql="select count(*) from BrandAuthEntity where userId=:userId and isDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("userId", userId);
		return ((Long)query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	private List<BrandAuthEntity> getBrandAuthByUserId(String userId, Integer page, Integer limit) {
		String hql="from BrandAuthEntity where userId=:userId and isDeleted=0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("userId", userId);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<BrandAuthVo> findAllBrandAuthList(String companyName, Integer page, Integer limit) {
		String hql = "";
		if(StringUtils.isNotBlank(companyName)){
			hql = "from BrandAuthEntity where companyName like:companyName and isDeleted=0";
		}else{
			hql = "from BrandAuthEntity where isDeleted=0";
		}
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		if(StringUtils.isNotBlank(companyName)){
			query.setParameter("companyName", "%"+companyName+"%");
		}
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<BrandAuthEntity> brandAuths = query.list();
		List<BrandAuthVo> brandAuthVos = new ArrayList<BrandAuthVo>();
		for (BrandAuthEntity brandAuth : brandAuths) {
			BrandAuthVo brandAuthVo = (BrandAuthVo) CopyUtil.tryToVo(brandAuth, BrandAuthVo.class);
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(brandAuthVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				brandAuthVo.setStatus("处理中");
				brandAuthVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = brandAuthVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						brandAuthVo.setStatus(t.getName());
				}
			}
			brandAuthVos.add(brandAuthVo);
		}
		String hqlCount = "";
		if(StringUtils.isNotBlank(companyName)){
			hqlCount = "select count(*) from BrandAuthEntity where companyName like:companyName and isDeleted=0";
		}else{
			hqlCount = "select count(*) from BrandAuthEntity where isDeleted=0";
		}
		query = sessionFactory.getCurrentSession().createQuery(hqlCount);
		if(StringUtils.isNotBlank(companyName)){
			query.setParameter("companyName", "%"+companyName+"%");
		}
		int count = ((Long)query.uniqueResult()).intValue();
		return new ListResult<>(brandAuthVos, count);
	}

	@Override
	public BrandAuthEntity getBrandAuthByChopInstanceId(String processInstanceId) {
		String hql = "from BrandAuthEntity where chopProcessInstanceID="+processInstanceId;
		return (BrandAuthEntity) baseDao.hqlfindUniqueResult(hql);
	}
}
