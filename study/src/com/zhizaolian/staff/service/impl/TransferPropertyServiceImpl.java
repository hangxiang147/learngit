package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
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

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.TransferPropertyEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TransferPropertyService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.TransferPropertyVo;

public class TransferPropertyServiceImpl implements TransferPropertyService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PermissionService permissionService;
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
	public void startTransferProperty(TransferPropertyVo transferPropertyVo, String recipientId) {
		String enumName = BusinessTypeEnum.TRANSFER_PROPERTY.getName();
		transferPropertyVo.setBusinessType(enumName);
		transferPropertyVo.setTitle(transferPropertyVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(recipientId);
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(recipientId);
		}
		if (StringUtils.isBlank(supervisor) || recipientId.equals(supervisor)) {
			supervisor=staffService.querySupervisor(recipientId);
		}
		String manager = staffService.queryManager(transferPropertyVo.getUserID());
		
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<Group> groups = identityService.createGroupQuery().groupMember(transferPropertyVo.getUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> hrLeaders = permissionService
				.findUsersByPermissionCodeCompany(Constants.HR_LEADER, companyID);
		if(hrLeaders.size()>0){
			vars.put("manageLeader", hrLeaders.get(0));
		}else{
			throw new RuntimeException("未找到行政人事领导，请联系系统管理员配置");
		}
		List<String> financialManagers = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(financialManagers.size()>0){
			vars.put("financialLeader", financialManagers.get(0));
		}else{
			throw new RuntimeException("未找到财务部门负责人，请联系系统管理员配置");
		}
		if(null == supervisor){
			vars.put("useLeader", recipientId);
		}else{
			vars.put("useLeader", supervisor);
		}
		vars.put("manager", manager);
		vars.put("arg", transferPropertyVo);
		vars.put("applyUser", transferPropertyVo.getUserID());
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.TRANSFER_PROPERTY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), transferPropertyVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveTransferProperty(transferPropertyVo, processInstance.getId());
	}
	private void saveTransferProperty(TransferPropertyVo transferPropertyVo, String id) {
		TransferPropertyEntity transferPropertyEntity = null;
		try {
			transferPropertyEntity = (TransferPropertyEntity) CopyUtil.tryToEntity(transferPropertyVo, TransferPropertyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		transferPropertyEntity.setProcessInstanceID(id);
		transferPropertyEntity.setAddTime(new Date());
		transferPropertyEntity.setIsDeleted(0);
		baseDao.hqlSave(transferPropertyEntity);
	}
	@Override
	public void updateTransferPropertyProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update TransferPropertyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}
	@Override
	public ListResult<TransferPropertyVo> findTransferPropertyListByUserID(String id, Integer page, Integer limit) {
		List<TransferPropertyEntity> transferPropertys = getTransferPropertyByUserId(id,
				page, limit);

		List<TransferPropertyVo> transferPropertyVos = new ArrayList<TransferPropertyVo>();
		for (TransferPropertyEntity transferProperty : transferPropertys) {
			TransferPropertyVo transferPropertyVo = null;
			try {
				transferPropertyVo = (TransferPropertyVo) CopyUtil.tryToVo(transferProperty, TransferPropertyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(transferProperty.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					TransferPropertyVo arg = (TransferPropertyVo) variable.getValue();
					transferPropertyVo.setRequestDate(arg.getRequestDate());
					transferPropertyVo.setTitle(arg.getTitle());
					transferPropertyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(transferProperty.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				transferPropertyVo.setStatus("处理中");
				transferPropertyVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = transferPropertyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						transferPropertyVo.setStatus(t.getName());
				}
			}
			transferPropertyVos.add(transferPropertyVo);
		}
		int count = getTransferPropertyCountByUserId(id);
		return new ListResult<TransferPropertyVo>(transferPropertyVos, count);
	}
	private int getTransferPropertyCountByUserId(String id) {
		String hql = "select count(id) from TransferPropertyEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<TransferPropertyEntity> getTransferPropertyByUserId(String id, Integer page, Integer limit) {
		String hql="from TransferPropertyEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<TransferPropertyEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public TransferPropertyVo getTransferPropertyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		TransferPropertyVo transferPropertyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				transferPropertyVo = (TransferPropertyVo) variable.getValue();
			}
		}
		return transferPropertyVo;
	}
}
