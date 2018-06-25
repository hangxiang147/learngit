package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.PublicRelationEntity;
import com.zhizaolian.staff.entity.PublicRelationEventEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.PublicService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PublicRelationEventVo;
@Service(value="publicService")
public class PublicServiceImpl implements PublicService {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private StaffService staffService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private StaffDao staffDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<PublicRelationEntity> findPublicRelations(Integer page, Integer limit, String category,
			String person) {
		String hql = "from PublicRelationEntity where isDeleted=0\n";
		if(StringUtils.isNotBlank(person)){
			hql += "and otherName like:otherName\n";
		}
		if(StringUtils.isNotBlank(category)){
			hql += "and category like:category\n";
		}
		hql += "order by addTime desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		if(StringUtils.isNotBlank(person)){
			query.setParameter("otherName", "%"+person+"%");
		}
		if(StringUtils.isNotBlank(category)){
			query.setParameter("category", "%"+category+"%");
		}
		query.setMaxResults(limit);
		query.setFirstResult((page-1)*limit);
		List<PublicRelationEntity> publicRelations = query.list();
		for(PublicRelationEntity publicRelation: publicRelations){
			String[] ourPersons = publicRelation.getOurPersonIds().split(",");
			List<String> ourContacts = new ArrayList<>();
			for(String personId: ourPersons){
				StaffEntity staff = staffService.getInJobStaffByUserId(personId);
				if(null != staff){
					ourContacts.add(staff.getStaffName()+"【"+staff.getTelephone()+"】");
				}
			}
			publicRelation.setOurContacts(ourContacts);
		}
		String sqlCount = "select count(id) from OA_PublicRelation where isDeleted=0\n";
		if(StringUtils.isNotBlank(person)){
			sqlCount += "and otherName like:otherName\n";
		}
		if(StringUtils.isNotBlank(category)){
			sqlCount += "and category like:category";
		}
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlCount);
		if(StringUtils.isNotBlank(person)){
			sqlQuery.setParameter("otherName", "%"+person+"%");
		}
		if(StringUtils.isNotBlank(category)){
			sqlQuery.setParameter("category", "%"+category+"%");
		}
		int count = Integer.parseInt(sqlQuery.uniqueResult()+"");
		return new ListResult<>(publicRelations, count);
	}
	@Override
	public void savePublicRelation(PublicRelationEntity publicRelation) {
		if(null == publicRelation.getId()){
			publicRelation.setAddTime(new Date());
			publicRelation.setIsDeleted(0);
			publicRelation.setStatus("0");
			baseDao.hqlSave(publicRelation);
		}else{
			publicRelation.setIsDeleted(0);
			baseDao.hqlUpdate(publicRelation);
		}
	}
	@Override
	public PublicRelationEntity getPublicRelation(String publicRelationId) {
		String hql = "from PublicRelationEntity where id="+publicRelationId;
		return (PublicRelationEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void startApplyPublicEvent(PublicRelationEventVo publicRelationEvent) {
		publicRelationEvent.setBusinessType(BusinessTypeEnum.PUBLIC_EVENT.getName());
		publicRelationEvent.setTitle(publicRelationEvent.getUserName()+"的"+BusinessTypeEnum.PUBLIC_EVENT.getName());
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", publicRelationEvent);
		List<String> publicEventMatchers = permissionService
				.findUsersByPermissionCode(Constants.PUBLIC_EVENT_MACTCHER);
		if(publicEventMatchers.size()>0){
			vars.put("matcher", publicEventMatchers.get(0));
		}else{
			throw new RuntimeException("未找到公关事情的匹配人");
		}
		vars.put("applyer", publicRelationEvent.getUserId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.PUBLIC_EVENT);
		publicRelationEvent.setProcessInstanceID(processInstance.getId());
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), publicRelationEvent.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		savePublicEvent(publicRelationEvent);
	}
	private void savePublicEvent(PublicRelationEventVo publicRelationEvent) {
		PublicRelationEventEntity entity = (PublicRelationEventEntity) CopyUtil.tryToEntity
				(publicRelationEvent, PublicRelationEventEntity.class);
		entity.setIsDeleted(0);
		entity.setAddTime(new Date());
		baseDao.hqlSave(entity);
		
	}
	@Override
	public ListResult<PublicRelationEventVo> findPublicEventListByUserID(String id, Integer page, Integer limit) {
		List<PublicRelationEventEntity> publicRelationEvents = getPublicRelationEventByUserId(id,
				page, limit);

		List<PublicRelationEventVo> publicRelationEventVos = new ArrayList<PublicRelationEventVo>();
		for (PublicRelationEventEntity publicRelationEvent : publicRelationEvents) {
			PublicRelationEventVo publicRelationEventVo = null;
			try {
				publicRelationEventVo = (PublicRelationEventVo) CopyUtil.tryToVo(publicRelationEvent, PublicRelationEventVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(publicRelationEvent.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					PublicRelationEventVo arg = (PublicRelationEventVo) variable.getValue();
					publicRelationEventVo.setRequestDate(arg.getRequestDate());
					publicRelationEventVo.setTitle(arg.getTitle());
					publicRelationEventVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(publicRelationEventVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				publicRelationEventVo.setStatus("进行中");
				publicRelationEventVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = publicRelationEventVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						publicRelationEventVo.setStatus(t.getName());
				}
			}
			publicRelationEventVos.add(publicRelationEventVo);
		}
		int count = getPublicRelationEventCountByUserId(id);
		return new ListResult<PublicRelationEventVo>(publicRelationEventVos, count);
	}
	private int getPublicRelationEventCountByUserId(String id) {
		String hql = "select count(id) from PublicRelationEventEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<PublicRelationEventEntity> getPublicRelationEventByUserId(String id, Integer page, Integer limit) {
		String hql="from PublicRelationEventEntity where IsDeleted=0 and userId='"+id+"' order by deadlineDate desc";
		return (List<PublicRelationEventEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public List<PublicRelationEventVo> getPublicEventTasksByInstanceId(List<Task> publicEventTasks) throws Exception {
		List<PublicRelationEventVo> publicRelationEventVos = new ArrayList<>();
		for(Task publicEventTask: publicEventTasks){
			PublicRelationEventEntity publicEventEntity = getPublicEventTaskByInstanceId(publicEventTask.getProcessInstanceId());
			PublicRelationEventVo publicRelationEventVo = (PublicRelationEventVo) CopyUtil.tryToVo(publicEventEntity, PublicRelationEventVo.class);
			publicRelationEventVo.setTaskName(publicEventTask.getName());
			publicRelationEventVo.setTaskId(publicEventTask.getId());
			publicRelationEventVo.setUserName(staffDao.getStaffByUserID(publicRelationEventVo.getUserId()).getStaffName());
			String deadlineDate = publicRelationEventVo.getDeadlineDate();
			if(DateUtil.getFullDate(deadlineDate).before(new Date())){
				publicRelationEventVo.setCountdown("时间截止");
			}else{
				String countdown = DateUtil.daysAndHoursBetween(new Date(), DateUtil.getFullDate(deadlineDate));
				publicRelationEventVo.setCountdown(countdown);
			}
			publicRelationEventVos.add(publicRelationEventVo);
		}
		Collections.sort(publicRelationEventVos, new Comparator<PublicRelationEventVo>() {
			@Override
			public int compare(PublicRelationEventVo o1, PublicRelationEventVo o2) {
				Date date1 = DateUtil.getFullDate(o1.getDeadlineDate());
				Date date2 = DateUtil.getFullDate(o2.getDeadlineDate());
				if(date1.before(date2)){
					return -1;
				}
				return 0;
			}
		});
		return publicRelationEventVos;
	}
	@Override
	public PublicRelationEventEntity getPublicEventTaskByInstanceId(String processInstanceId) {
		String hql = "from PublicRelationEventEntity where processInstanceID="+processInstanceId;
		return (PublicRelationEventEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void updatePublicEventStatus(TaskResultEnum taskResult, String processInstanceId) {
		String hql = "update PublicRelationEventEntity set applyResult="+taskResult.getValue()+" where processInstanceID="+processInstanceId;
		baseDao.excuteHql(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PublicRelationEntity> findPublicRelations() {
		String hql = "from PublicRelationEntity where isDeleted=0 and status=0 order by otherName";
		return (List<PublicRelationEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void updatePublicRelationId(String publicRelationId, String processInstanceId) {
		String hql = "update PublicRelationEventEntity set publicRelationId="+publicRelationId+" where processInstanceID="+processInstanceId;
		baseDao.excuteHql(hql);
	}
	@Override
	public void closePublicRelation(String publicRelationId) {
		String hql = "update PublicRelationEntity set status=1 where id="+publicRelationId;
		baseDao.excuteHql(hql);
	}
	@Override
	public List<PublicRelationEventVo> showHistoricalPublicEventsByRelationId(String publicRelationId) {
		String hql = "from PublicRelationEventEntity where isDeleted=0 and publicRelationId="+publicRelationId;
		@SuppressWarnings("unchecked")
		List<PublicRelationEventEntity> publicRelationEvents = (List<PublicRelationEventEntity>) baseDao.hqlfind(hql);
		List<PublicRelationEventVo> publicRelationEventVos = new ArrayList<PublicRelationEventVo>();
		for (PublicRelationEventEntity publicRelationEvent : publicRelationEvents) {
			PublicRelationEventVo publicRelationEventVo = null;
			try {
				publicRelationEventVo = (PublicRelationEventVo) CopyUtil.tryToVo(publicRelationEvent, PublicRelationEventVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(publicRelationEvent.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					PublicRelationEventVo arg = (PublicRelationEventVo) variable.getValue();
					publicRelationEventVo.setRequestDate(arg.getRequestDate());
					publicRelationEventVo.setTitle(arg.getTitle());
					publicRelationEventVo.setUserName(arg.getUserName());
				}
			}
			if(String.valueOf(TaskDefKeyEnum.PUBLIC_EVENT_MATCH.getValue())
					.equals(publicRelationEventVo.getStatus())){
				Task task = taskService.createTaskQuery().processInstanceId(publicRelationEventVo.getProcessInstanceID()).singleResult();
				publicRelationEventVo.setHandlerName(staffService.getStaffByUserId(task.getAssignee()).getStaffName());
			}else{
				HistoricTaskInstanceEntity handleTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(publicRelationEventVo.getProcessInstanceID()).
						taskDefinitionKey(TaskDefKeyEnum.PUBLIC_EVENT_HANDLE.getName()).singleResult();
				publicRelationEventVo.setHandlerName(staffService.getStaffByUserId(handleTask.getAssignee()).getStaffName());
			}
			publicRelationEventVos.add(publicRelationEventVo);
		}
		return publicRelationEventVos;
	}
}
