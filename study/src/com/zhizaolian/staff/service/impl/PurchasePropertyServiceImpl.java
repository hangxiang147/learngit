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
import com.zhizaolian.staff.entity.PurchasePropertyEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.PurchasePropertyService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PurchasePropertyVo;

public class PurchasePropertyServiceImpl implements PurchasePropertyService{
	@Autowired
	private StaffService staffService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Override
	public void startPurchaseProperty(PurchasePropertyVo purchasePropertyVo) {
		String enumName = BusinessTypeEnum.PURCHASE_PROPERTY.getName();
		purchasePropertyVo.setBusinessType(enumName);
		purchasePropertyVo.setTitle(purchasePropertyVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(purchasePropertyVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(purchasePropertyVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || purchasePropertyVo.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(purchasePropertyVo.getUserID());
		}
		String manager = staffService.queryManager(purchasePropertyVo.getUserID());
		
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		List<Group> groups = identityService.createGroupQuery().groupMember(purchasePropertyVo.getUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> officeLeaders = permissionService
				.findUsersByPermissionCode(Constants.OFFICE_LRADER);
		if(officeLeaders.size()>0){
			vars.put("officeLeader", officeLeaders.get(0));
		}else{
			throw new RuntimeException("未找到办公室领导，请联系系统管理员配置");
		}
		List<String> purchasers = permissionService
				.findUsersByPermissionCodeCompany(Constants.PROPERTY_PURCHASER, companyID);
		if(purchasers.size()>0){
			vars.put("purchasers", purchasers);
		}else{
			throw new RuntimeException("未找到采购人，请联系系统管理员配置");
		}
		List<String> budgetPersons = permissionService
				.findUsersByPermissionCode(Constants.PROPERTY_BUDGET);
		if(budgetPersons.size()>0){
			vars.put("budgetPersons", budgetPersons);
		}else{
			throw new RuntimeException("未找到预算小组，请联系系统管理员配置");
		}
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("applyUser", purchasePropertyVo.getUserID());
		vars.put("arg", purchasePropertyVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.PURCHASE_PROPERTY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), purchasePropertyVo.getUserID());
		taskService.complete(task.getId(), vars);
		savePurchaseProperty(purchasePropertyVo, processInstance.getId());
	}
	private void savePurchaseProperty(PurchasePropertyVo purchasePropertyVo, String id) {
		PurchasePropertyEntity purchasePropertyEntity = null;
		try {
			purchasePropertyEntity = (PurchasePropertyEntity) CopyUtil.tryToEntity(purchasePropertyVo, PurchasePropertyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		purchasePropertyEntity.setProcessInstanceID(id);
		purchasePropertyEntity.setAddTime(new Date());
		purchasePropertyEntity.setIsDeleted(0);
		baseDao.hqlSave(purchasePropertyEntity);
		
	}
	@Override
	public void updateProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update PurchasePropertyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}
	@Override
	public PurchasePropertyEntity getPurchasePropertyUserIdByInstanceId(String id) {
		String hql = "from PurchasePropertyEntity where processInstanceID='"+id+"'";
		return (PurchasePropertyEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void updatePurchaseProperty(PurchasePropertyVo purchasePropertyVo) {
		String sql = "update OA_PurchaseProperty set productName='"+purchasePropertyVo.getProductName()+"', _model='"+purchasePropertyVo.get_model()
		+"', _number='"+purchasePropertyVo.get_number()+"', unitPrice='"+purchasePropertyVo.getUnitPrice()
		+"',purchaserCheckResult='"+purchasePropertyVo.getPurchaserCheckResult()+"' where processInstanceID='"+purchasePropertyVo.getProcessInstanceID()+"'";
		baseDao.excuteSql(sql);
	}
	@Override
	public void updatePurchasePropertyForBudget(PurchasePropertyVo purchasePropertyVo) {
		String sql = "update OA_PurchaseProperty set propertyType='"+purchasePropertyVo.getPropertyType()+"', propertyNum='"+purchasePropertyVo.getPropertyNum()
		+"', useTime='"+purchasePropertyVo.getUseTime()+"', netSalvageRate='"+purchasePropertyVo.getNetSalvageRate()
		+"' where processInstanceID='"+purchasePropertyVo.getProcessInstanceID()+"'";
		baseDao.excuteSql(sql);
		
	}
	@Override
	public ListResult<PurchasePropertyVo> findPurchasePropertyListByUserID(String id, Integer page, Integer limit) {
		List<PurchasePropertyEntity> purchasePropertys = getPurchasePropertyByUserId(id,
				page, limit);

		List<PurchasePropertyVo> purchasePropertyVos = new ArrayList<PurchasePropertyVo>();
		for (PurchasePropertyEntity purchaseProperty : purchasePropertys) {
			PurchasePropertyVo purchasePropertyVo = null;
			try {
				purchasePropertyVo = (PurchasePropertyVo) CopyUtil.tryToVo(purchaseProperty, PurchasePropertyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(purchaseProperty.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					PurchasePropertyVo arg = (PurchasePropertyVo) variable.getValue();
					purchasePropertyVo.setRequestDate(arg.getRequestDate());
					purchasePropertyVo.setTitle(arg.getTitle());
					purchasePropertyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(purchaseProperty.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				purchasePropertyVo.setStatus("处理中");
				purchasePropertyVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = purchasePropertyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						purchasePropertyVo.setStatus(t.getName());
				}
			}
			purchasePropertyVos.add(purchasePropertyVo);
		}
		int count = getPurchasePropertyCountByUserId(id);
		return new ListResult<PurchasePropertyVo>(purchasePropertyVos, count);
	}
	private int getPurchasePropertyCountByUserId(String id) {
		String hql = "select count(id) from PurchasePropertyEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<PurchasePropertyEntity> getPurchasePropertyByUserId(String id, Integer page, Integer limit) {
		String hql="from PurchasePropertyEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<PurchasePropertyEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public PurchasePropertyVo getPurchasePropertyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		PurchasePropertyVo purchasePropertyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				purchasePropertyVo = (PurchasePropertyVo) variable.getValue();
			}
		}
		return purchasePropertyVo;
	}
}
