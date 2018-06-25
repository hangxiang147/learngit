package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.CommonSubjectEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.RouteType;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.CommonSubjectService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.produceUtil;
import com.zhizaolian.staff.vo.CommonSubjectVo;

public class CommonSubjectServiceImpl implements CommonSubjectService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	

	@Override
	public void startCommonSubject(CommonSubjectVo commonSubjectVo, File[] files, String fileDetail,
			String requestUserId) throws Exception {

		// 初始化baseVo
		produceUtil.initBaseVo(commonSubjectVo, requestUserId, staffService, BusinessTypeEnum.COMMONSUBJECT);

		// 初始化流程必要数据
		String userIds = commonSubjectVo.getUserIds();
		if (StringUtils.isEmpty(userIds)) {
			throw new RuntimeException("人员参数不能为空！");
		}
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("route", RouteType.getIndex(commonSubjectVo.getRoute()));
		vars.put("users", Arrays.asList(userIds.split(",", -1)));
		vars.put("arg", commonSubjectVo);
		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("CommonSubject");
		// 塞入附件
		baseDao.saveActivitiAttchment(files, fileDetail, processInstance.getId());

		// 走完申请所在的第一步
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), commonSubjectVo.getUserID());
		taskService.complete(task.getId(), vars);

		// 保存实体表数据
		CommonSubjectEntity commonSubjectEntity = (CommonSubjectEntity) CopyUtil.tryToEntity(commonSubjectVo,
				CommonSubjectEntity.class);
		//父类里的属性并不会被拷贝
		commonSubjectEntity.setUserID(commonSubjectVo.getUserID());
		commonSubjectEntity.setUserName(commonSubjectVo.getUserName());
		produceUtil.initEntity(commonSubjectEntity, processInstance.getId());
		baseDao.hqlSave(commonSubjectEntity);
	}

	
	@Override
	public ListResult<CommonSubjectVo> getCommonSubjectByKey(CommonSubjectVo commonSubjectVo,
			Map<String, String> extraMap, int page, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(" from CommonSubjectEntity c  where 1=1");
		if (StringUtils.isNotBlank(commonSubjectVo.getType())) {
			sb.append(" and c.type='").append(commonSubjectVo.getType()).append("' ");
		}
		if (StringUtils.isNotBlank(commonSubjectVo.getUserName())) {
			sb.append(" and c.userName like '%").append(commonSubjectVo.getUserName()).append("%' ");
		}
		if (StringUtils.isNotBlank(commonSubjectVo.getUserID())) {
			sb.append(" and c.userID='").append(commonSubjectVo.getUserID()).append("' ");
		}
		if (extraMap != null && !extraMap.isEmpty()) {
			String startTime = extraMap.get("startTime");
			String endTime = extraMap.get("endTime");
			if (StringUtils.isNotBlank(startTime)) {
				sb.append(" and c.addTime >= '").append(startTime).append("' ");
			}
			if (StringUtils.isNotBlank(endTime)) {
				sb.append(" and c.addTime <= '").append(endTime).append("' ");
			}
		}
		@SuppressWarnings("unchecked")
		List<CommonSubjectEntity> handleList = (List<CommonSubjectEntity>) baseDao.hqlPagedFind(sb.toString(), page,
				limit);
		int count = ((Long) baseDao.hqlfindUniqueResult("select count(*) " + sb.toString())).intValue();
				
		List<CommonSubjectVo> resultList = new ArrayList<>();
		for (CommonSubjectEntity entity : handleList) {
			CommonSubjectVo cSubjectVo = (CommonSubjectVo) CopyUtil.tryToVo(entity, CommonSubjectVo.class);
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(entity.getInstanceId()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					CommonSubjectVo arg = (CommonSubjectVo) variable.getValue();
					cSubjectVo.setRequestDate(arg.getRequestDate());
					cSubjectVo.setTitle(arg.getTitle());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(entity.getInstanceId()).singleResult();
			if (pInstance != null) {
				cSubjectVo.setStatus("处理中");
				cSubjectVo.setProcessInstanceID(entity.getInstanceId());
				cSubjectVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer result=entity.getResult();
				cSubjectVo.setProcessInstanceID(entity.getInstanceId());
				cSubjectVo.setStatus(
						TaskResultEnum.valueOf(result==null?TaskResultEnum.COMPLETEAll.getValue():result).getName());
			}
			resultList.add(cSubjectVo);
		}		
		return new ListResult<>(resultList, count);
	}


	@Override
	public CommonSubjectEntity getEntityId(String id) {
		String hql="from  CommonSubjectEntity c  where c.id="+id;
		return 	(CommonSubjectEntity) baseDao.hqlfindUniqueResult(hql);
	}


	@Override
	public void updateResult(String result,String processInstanceID) {
		ProcessInstance pInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceID).singleResult();
		if(pInstance==null){
			String hql=" update CommonSubjectEntity c set c.result="+result+" where c.instanceId='"+processInstanceID+"'";
			baseDao.excuteHql(hql);
		}
	}
	
	
}
