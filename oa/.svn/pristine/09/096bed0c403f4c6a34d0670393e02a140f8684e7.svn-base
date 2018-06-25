 package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.ChangeBankAccountEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.BankAccountService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChangeBankAccountVo;

public class BankAccountServiceImpl implements BankAccountService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private ProcessService processService;
	@Override
	public void startBankAccount(ChangeBankAccountVo changeBankAccountVo) {
		String enumName = BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getName();
		changeBankAccountVo.setBusinessType(enumName);
		changeBankAccountVo.setTitle(changeBankAccountVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(changeBankAccountVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor = staffService.queryHeadMan(changeBankAccountVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || changeBankAccountVo.getUserID().equals(supervisor)) {
			supervisor = staffService.querySupervisor(changeBankAccountVo.getUserID());
		}
		String manager = staffService.queryManager(changeBankAccountVo.getUserID());

		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		List<String> financialManagers = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(financialManagers.size()>0){
			vars.put("financialManager", financialManagers.get(0));
		}else{
			throw new RuntimeException("未找到财务主管，请联系系统管理员配置");
		}
		List<String> cashiers = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_CASHIER);
		if(cashiers.size()>0){
			vars.put("financialAuditUsers", cashiers);
		}else{
			throw new RuntimeException("未找到执行的出纳，请联系系统管理员配置");
		}
		vars.put("arg", changeBankAccountVo);
		//启动流程
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.BANK_ACCOUNT);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), changeBankAccountVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveChangeBankAccount(changeBankAccountVo, processInstance.getId());

	}
	private void saveChangeBankAccount(ChangeBankAccountVo changeBankAccountVo, String processInstanceId) {
		ChangeBankAccountEntity changeBankAccountEntity = null;
		try {
			changeBankAccountEntity = (ChangeBankAccountEntity) CopyUtil.tryToEntity(changeBankAccountVo, ChangeBankAccountEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		changeBankAccountEntity.setProcessInstanceID(processInstanceId);
		changeBankAccountEntity.setAddTime(new Date());
		changeBankAccountEntity.setIsDeleted(0);
		baseDao.hqlSave(changeBankAccountEntity);
		
	}
	@Override
	public String getUserIdByInstanceId(String id) {
		String sql = "select userID from OA_ChangeBankAccount where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}
	@Override
	public void updateBankAccountProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ChangeBankAccountEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
		
	}
	@Override
	public ListResult<ChangeBankAccountVo> findChangeBankAccountListByUserID(String userId, Integer page, Integer limit) {
		List<ChangeBankAccountEntity> changeBankAccounts = getChangeBankAccountByUserId(userId,
				page, limit);

		List<ChangeBankAccountVo> changeBankAccountVos = new ArrayList<ChangeBankAccountVo>();
		for (ChangeBankAccountEntity changeBankAccount : changeBankAccounts) {
			ChangeBankAccountVo changeBankAccountVo = null;
			try {
				changeBankAccountVo = (ChangeBankAccountVo) CopyUtil.tryToVo(changeBankAccount, ChangeBankAccountVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(changeBankAccount.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ChangeBankAccountVo arg = (ChangeBankAccountVo) variable.getValue();
					changeBankAccountVo.setRequestDate(arg.getRequestDate());
					changeBankAccountVo.setTitle(arg.getTitle());
					changeBankAccountVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(changeBankAccount.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				changeBankAccountVo.setStatus("处理中");
				changeBankAccountVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = changeBankAccount.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						changeBankAccountVo.setStatus(t.getName());
				}
			}
			changeBankAccountVos.add(changeBankAccountVo);
		}
		int count = getChangeBankAccountCountByUserId(userId);
		return new ListResult<ChangeBankAccountVo>(changeBankAccountVos, count);
	}
	private int getChangeBankAccountCountByUserId(String userId) {
		String hql = "select count(id) from ChangeBankAccountEntity where IsDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<ChangeBankAccountEntity> getChangeBankAccountByUserId(String userId, Integer page, Integer limit) {
		String hql="from ChangeBankAccountEntity where IsDeleted=0 and userId='"+userId+"'";
		return (List<ChangeBankAccountEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public ChangeBankAccountVo getChangeBankAccountVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ChangeBankAccountVo changeBankAccountVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				changeBankAccountVo = (ChangeBankAccountVo) variable.getValue();
			}
		}
		return changeBankAccountVo;
	}
}
