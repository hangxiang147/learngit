package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.ShopApplyEntity;
import com.zhizaolian.staff.entity.ShopOtherPayEntity;
import com.zhizaolian.staff.entity.ShopPayApplyEntity;
import com.zhizaolian.staff.entity.ShopPayPluginEntity;
import com.zhizaolian.staff.entity.SpreadShopApplyEntity;
import com.zhizaolian.staff.entity.SpreadShopEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ShopApplyService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ShopApplyTaskVo;
import com.zhizaolian.staff.vo.ShopApplyVo;
import com.zhizaolian.staff.vo.ShopPayApplyListVo;
import com.zhizaolian.staff.vo.ShopPayApplyVo;
import com.zhizaolian.staff.vo.ShopPayVo;
import com.zhizaolian.staff.vo.SpreadShopVo;

public class ShopApplyServiceImpl implements ShopApplyService {
	@Autowired
	private StaffService staffService;
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
	public void startShopApply(ShopApplyVo shopApplyVo, File[] attachment, String[] attachmentFileName) throws Exception {
		String enumName = BusinessTypeEnum.SHOP_APPLY.getName();
		shopApplyVo.setBusinessType(enumName);
		shopApplyVo.setTitle(shopApplyVo.getUserName() + "的" + shopApplyVo.getApplyType()+"申请");
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(shopApplyVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(shopApplyVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || shopApplyVo.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(shopApplyVo.getUserID());
		}
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		List<String> financialPersons = permissionService.findUsersByPermissionCode(Constants.FINANCIAL_OPEN_ACCOUNT);
		if(financialPersons.size()>0){
			vars.put("financialOpenAccountUsers", financialPersons);
		}else{
			throw new RuntimeException("未找到开通银行账号的财务人员，请联系系统管理员配置");
		}
		List<String> financialManagers = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(financialManagers.size()>0){
			vars.put("financialManager", financialManagers.get(0));
		}else{
			throw new RuntimeException("未找到财务主管，请联系系统管理员配置");
		}
		vars.put("supervisor", supervisor);
		vars.put("handler", shopApplyVo.getHandlerId());
		vars.put("arg", shopApplyVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.SHOP_APPLY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		int index = 0;
		if(null != attachment){
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				String suffix = fileName.substring(fileName.indexOf(".")+1, fileName.length());
				InputStream is = new FileInputStream(file);
				if(suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpeg")){
					taskService.createAttachment("picture", "", processInstance.getId(), fileName, "", is);
				}else{
					taskService.createAttachment(suffix,"",processInstance.getId(), fileName, "", is);
				}
				index++;
			}
		}
		taskService.setAssignee(task.getId(), shopApplyVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveShopApply(shopApplyVo, processInstance.getId());

	}
	private void saveShopApply(ShopApplyVo shopApplyVo, String id) {
		ShopApplyEntity shopApplyEntity = null;
		try {
			shopApplyEntity = (ShopApplyEntity) CopyUtil.tryToEntity(shopApplyVo, ShopApplyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		shopApplyEntity.setProcessInstanceID(id);
		shopApplyEntity.setAddTime(new Date());
		shopApplyEntity.setIsDeleted(0);
		baseDao.hqlSave(shopApplyEntity);
	}
	@Override
	public List<ShopApplyTaskVo> getShopApplyTaskVOList(List<Task> shopApplyTasks) {
		List<ShopApplyTaskVo> taskVOs = new ArrayList<ShopApplyTaskVo>();
		if(null == shopApplyTasks){
			return taskVOs;
		}
		for (Task task : shopApplyTasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			ShopApplyVo arg = (ShopApplyVo) runtimeService.getVariable(pInstance.getId(), "arg");
			ShopApplyTaskVo taskVO = new ShopApplyTaskVo();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			taskVO.setCreateTime(task.getCreateTime()==null?"":DateUtil.formateFullDate(task.getCreateTime()));
			taskVO.setPlatform(arg.getPlatform());
			taskVO.setShopName(arg.getShopName());
			taskVO.setShopType(arg.getShopType());
			taskVO.setShopStartTime(arg.getShopStartTime());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	@Override
	public ShopApplyVo getShopApplyVo(String taskID) {
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery().
				processInstanceId(task.getProcessInstanceId()).singleResult();
		ShopApplyVo shopApplyVo = (ShopApplyVo) runtimeService.getVariable(pInstance.getId(), "arg");
		return shopApplyVo;
	}
	@Override
	public String getShopApplyUserIdByInstanceId(String id) {
		String sql = "select userID from OA_ShopApply where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}
	@Override
	public void updateShopApplyProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ShopApplyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}
	@Override
	public ListResult<ShopApplyVo> findShopApplyListByUserID(String id, Integer page, Integer limit) {
		List<ShopApplyEntity> shopApplys = getShopApplyByUserId(id,
				page, limit);

		List<ShopApplyVo> shopApplyVos = new ArrayList<ShopApplyVo>();
		for (ShopApplyEntity shopApply : shopApplys) {
			ShopApplyVo shopApplyVo = null;
			try {
				shopApplyVo = (ShopApplyVo) CopyUtil.tryToVo(shopApply, ShopApplyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(shopApply.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ShopApplyVo arg = (ShopApplyVo) variable.getValue();
					shopApplyVo.setRequestDate(arg.getRequestDate());
					shopApplyVo.setTitle(arg.getTitle());
					shopApplyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(shopApply.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				shopApplyVo.setStatus("处理中");
				try{
					Task task = taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
					if(task.getAssignee() == null){
						List<IdentityLink> idList = taskService.getIdentityLinksForTask(task.getId());
						String prevStr = processService
								.getProcessTaskAssignee(pInstance.getId());
						String[] nameArr = new String[idList.size()];
						int i=0;
						for (IdentityLink identityLink : idList) {
							nameArr[i++] = staffService.getRealNameByUserId(identityLink.getUserId());
						}
						shopApplyVo.setAssigneeUserName("【"+prevStr+"】"+StringUtils.join(nameArr,"，"));
					}else{
						shopApplyVo.setAssigneeUserName(processService
								.getProcessTaskAssignee(pInstance.getId()));
					}
				}catch(Exception e){
					shopApplyVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				}
			} else {
				Integer value_ = shopApplyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						shopApplyVo.setStatus(t.getName());
				}
			}
			shopApplyVos.add(shopApplyVo);
		}
		int count = getShopApplyCountByUserId(id);
		return new ListResult<ShopApplyVo>(shopApplyVos, count);
	}
	private int getShopApplyCountByUserId(String id) {
		String hql = "select count(id) from ShopApplyEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<ShopApplyEntity> getShopApplyByUserId(String id, Integer page, Integer limit) {
		String hql="from ShopApplyEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<ShopApplyEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public ShopApplyVo getShopApplyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ShopApplyVo shopApplyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;

			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				shopApplyVo = (ShopApplyVo) variable.getValue();
			}
		}
		return shopApplyVo;
	}
	private void saveShopPayApply(ShopPayApplyVo shopPayApplyVo, String id) {
		ShopPayApplyEntity shopPayApplyEntity = null;
		try {
			shopPayApplyEntity = (ShopPayApplyEntity) CopyUtil.tryToEntity(shopPayApplyVo, ShopPayApplyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		shopPayApplyEntity.setProcessInstanceID(id);
		shopPayApplyEntity.setAddTime(new Date());
		shopPayApplyEntity.setIsDeleted(0);
		baseDao.hqlSave(shopPayApplyEntity);
	}
	@Override
	public List<ShopApplyTaskVo> getShopPayApplyTaskVOList(List<Task> shopPayApplyTasks) {
		List<ShopApplyTaskVo> taskVOs = new ArrayList<ShopApplyTaskVo>();
		if(null == shopPayApplyTasks){
			return taskVOs;
		}
		for (Task task : shopPayApplyTasks) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			ShopPayApplyVo arg = (ShopPayApplyVo) runtimeService.getVariable(pInstance.getId(), "arg");
			ShopApplyTaskVo taskVO = new ShopApplyTaskVo();
			taskVO.setProcessInstanceID(task.getProcessInstanceId());
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID(task.getId());
			taskVO.setTitle(arg.getTitle());
			taskVO.setTaskName(task.getName());
			taskVO.setCreateTime(task.getCreateTime()==null?"":DateUtil.formateFullDate(task.getCreateTime()));
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}
	@Override
	public String getShopPayApplyUserIdByInstanceId(String id) {
		String sql = "select userID from OA_ShopPayApply where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}
	@Override
	public ShopPayApplyVo getShopPayApplyVo(String taskID) {
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery().
				processInstanceId(task.getProcessInstanceId()).singleResult();
		ShopPayApplyVo shopPayApplyVo = (ShopPayApplyVo) runtimeService.getVariable(pInstance.getId(), "arg");
		return shopPayApplyVo;
	}
	@Override
	public SpreadShopEntity getSpreadShop(String spreadId) {
		String hql = "from SpreadShopEntity where id="+spreadId;
		return (SpreadShopEntity)baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void updateShopPayApplyProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ShopPayApplyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);		
	}
	@Override
	public List<ShopPayApplyVo> findShopPayApplyListByUserID(String id) {
		List<ShopPayApplyEntity> shopPayApplys = getShopPayApplyByUserId(id);

		List<ShopPayApplyVo> shopPayApplyVos = new ArrayList<ShopPayApplyVo>();
		for (ShopPayApplyEntity shopPayApply : shopPayApplys) {
			ShopPayApplyVo shopPayApplyVo = null;
			try {
				shopPayApplyVo = (ShopPayApplyVo) CopyUtil.tryToVo(shopPayApply, ShopPayApplyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(shopPayApply.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ShopPayApplyVo arg = (ShopPayApplyVo) variable.getValue();
					shopPayApplyVo.setRequestDate(arg.getRequestDate());
					shopPayApplyVo.setTitle(arg.getTitle());
					shopPayApplyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(shopPayApply.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				shopPayApplyVo.setStatus("处理中");
				try{
					Task task = taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
					if(task.getAssignee() == null){
						List<IdentityLink> idList = taskService.getIdentityLinksForTask(task.getId());
						String prevStr = processService
								.getProcessTaskAssignee(pInstance.getId());
						String[] nameArr = new String[idList.size()];
						int i=0;
						for (IdentityLink identityLink : idList) {
							nameArr[i++] = staffService.getRealNameByUserId(identityLink.getUserId());
						}
						shopPayApplyVo.setAssigneeUserName("【"+prevStr+"】"+StringUtils.join(nameArr,"，"));
					}else{
						shopPayApplyVo.setAssigneeUserName(processService
								.getProcessTaskAssignee(pInstance.getId()));
					}
				}catch(Exception e){
					shopPayApplyVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				}
			} else {
				Integer value_ = shopPayApplyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						shopPayApplyVo.setStatus(t.getName());
				}
			}
			shopPayApplyVos.add(shopPayApplyVo);
		}
		return shopPayApplyVos;
	}
	@SuppressWarnings("unchecked")
	private List<ShopPayApplyEntity> getShopPayApplyByUserId(String id) {
		String hql = "from ShopPayApplyEntity a where IsDeleted=0 and LOCATE('"+id+"',userID)>0";
		return (List<ShopPayApplyEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void updateShopPayApplyExpiredTime(String expiredTime, String processInstanceID) {
		/*		ShopPayApplyVo shopPayApplyVo = (ShopPayApplyVo)runtimeService.getVariable(processInstanceID, "arg");
		shopPayApplyVo.setExpiredTime(expiredTime);
		shopPayApplyVo.setStatus("已开通");
		runtimeService.setVariable(processInstanceID, "arg", shopPayApplyVo);
		String hql = "update ShopPayApplyEntity set expiredTime='"+expiredTime+"', status='已开通' where processInstanceID='"+processInstanceID+"'";
		baseDao.excuteHql(hql);*/
	}
	@Override
	public ShopPayApplyVo getShopPayApplyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ShopPayApplyVo shopPayApplyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;

			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				shopPayApplyVo = (ShopPayApplyVo) variable.getValue();
			}
		}
		return shopPayApplyVo;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<SpreadShopEntity> getSpreadShops(String id) {

		String hql = "from SpreadShopEntity where id in("+id+")";
		return (List<SpreadShopEntity>)baseDao.hqlfind(hql);
	}
	@Override
	public void startShopSpreadPayApply(SpreadShopApplyEntity spreadShopApply, SpreadShopVo spreadShopVo) {
		Map<String, Double> shopNameAndTotalCost = new HashMap<>(); 
		List<SpreadShopEntity> spreadShops =  new ArrayList<>();
		String[] shopNames = spreadShopVo.getShopName();
		for(int i=0; i<shopNames.length; i++){
			if(shopNameAndTotalCost.containsKey(shopNames[i])){
				double totalCost = shopNameAndTotalCost.get(shopNames[i])+spreadShopVo.getCostPerDay()[i];
				shopNameAndTotalCost.put(shopNames[i], totalCost);
			}else{
				shopNameAndTotalCost.put(shopNames[i], spreadShopVo.getCostPerDay()[i]);
			}
			SpreadShopEntity spreadShop = new SpreadShopEntity();
			spreadShop.setShopName(shopNames[i]);
			spreadShop.setLeader(spreadShopVo.getLeader()[i]);
			spreadShop.setLoginAccount(spreadShopVo.getLoginAccount()[i]);
			spreadShop.setSpreadType(spreadShopVo.getSpreadType()[i]);
			spreadShop.setCostPerDay(spreadShopVo.getCostPerDay()[i]+"");
			spreadShop.setRechargeAmount(spreadShopVo.getRechargeAmount()[i]+"");
			spreadShop.setCurrentBalance(spreadShopVo.getCurrentBalance()[i]+"");
			spreadShops.add(spreadShop);
		}
		for(SpreadShopEntity spreadShop: spreadShops){
			spreadShop.setTotalCost(shopNameAndTotalCost.get(spreadShop.getShopName())+"");
			baseDao.hqlSave(spreadShop);
		}
		int index = 0;
		Integer[] spreadIds = new Integer[shopNames.length];
		for(SpreadShopEntity spreadShop: spreadShops){
			spreadShop.setTotalCost(shopNameAndTotalCost.get(spreadShop.getShopName())+"");
			spreadIds[index] =  baseDao.hqlSave(spreadShop);
			index++;
		}
		spreadShopApply.setSpreadIds(StringUtils.join(spreadIds, ','));
		spreadShopApply.setShopName(StringUtils.join(shopNameAndTotalCost.keySet(), ','));
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(spreadShopApply.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(spreadShopApply.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || spreadShopApply.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(spreadShopApply.getUserID());
		}
		//如果没有主管，那么主管是自己
		if(null == supervisor){
			supervisor = spreadShopApply.getUserID();
		}
		spreadShopApply.setSupervisorId(supervisor);
		spreadShopApply.setSupervisorName(staffService.getRealNameByUserId(supervisor));
		spreadShopApply.setAddTime(new Date());
		spreadShopApply.setIsDeleted(0);
		baseDao.hqlSave(spreadShopApply);
	}
	@Override
	public void startShopPayPlugInApply(ShopPayPluginEntity shopPayPlugin) {
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(shopPayPlugin.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(shopPayPlugin.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || shopPayPlugin.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(shopPayPlugin.getUserID());
		}
		//如果没有主管，那么主管是自己
		if(null == supervisor){
			supervisor = shopPayPlugin.getUserID();
		}
		shopPayPlugin.setSupervisorId(supervisor);
		shopPayPlugin.setSupervisorName(staffService.getRealNameByUserId(supervisor));
		shopPayPlugin.setAddTime(new Date());
		shopPayPlugin.setIsDeleted(0);
		baseDao.hqlSave(shopPayPlugin);
	}
	@Override
	public void startShopOtherPayApply(ShopOtherPayEntity otherPay) {
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(otherPay.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(otherPay.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || otherPay.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(otherPay.getUserID());
		}
		//如果没有主管，那么主管是自己
		if(null == supervisor){
			supervisor = otherPay.getUserID();
		}
		otherPay.setSupervisorId(supervisor);
		otherPay.setSupervisorName(staffService.getRealNameByUserId(supervisor));
		otherPay.setAddTime(new Date());
		otherPay.setIsDeleted(0);
		baseDao.hqlSave(otherPay);

	} 
	@Override
	public List<ShopPayVo> getShopPayList(String id) {
		List<ShopPayVo> shopPayVos = new ArrayList<>();
		String sql = "select id, addTime, applyType, userName  from OA_SpreadShopApply where isDeleted=0 and supervisorId='"+id+"' and applyResult is null\n" +
				"UNION \n" +
				"select id, addTime, applyType, userName  from OA_ShopPayPlugin where isDeleted=0 and supervisorId='"+id+"' and applyResult is null\n" +
				"UNION\n" +
				"select id, addTime, applyType, userName  from OA_ShopOtherPay where isDeleted=0 and supervisorId='"+id+"' and applyResult is null";
		List<Object> objList = baseDao.findBySql(sql);
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			ShopPayVo shopPayVo = new ShopPayVo();
			shopPayVo.setId(objs[0]+"");
			shopPayVo.setApplyDate(DateUtil.formateFullDate((Date)objs[1]));
			shopPayVo.setApplyType(objs[2]+"");
			shopPayVo.setUserName(objs[3]+"");
			shopPayVos.add(shopPayVo);
		}
		return shopPayVos;
	}
	@Override
	public String getSpreadId(String id) {
		String sql = "select spreadIds from OA_SpreadShopApply where id="+id;
		return baseDao.getUniqueResult(sql)+"";
	}
	@Override
	public ShopPayPluginEntity getShopPayPlugin(String id) {
		String hql = "from ShopPayPluginEntity where id="+id;
		return (ShopPayPluginEntity)baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public ShopOtherPayEntity getShopOtherPay(String id) {
		String hql = "from ShopOtherPayEntity where id="+id;
		return (ShopOtherPayEntity)baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void startShopPayApply(String[] idAndApplyTypes, String auditResult,
			String userId, String beginDate, String endDate) throws Exception{
		ShopPayApplyVo shopPayApplyVo = new ShopPayApplyVo();
		shopPayApplyVo.setBeginDate(beginDate);
		shopPayApplyVo.setEndDate(endDate);
		//shopPayApplyVo.setUserName(staffService.getRealNameByUserId(userId));
		String enumName = BusinessTypeEnum.SHOP_PAY_APPLY.getName();
		shopPayApplyVo.setBusinessType(enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		List<String> financialPersons = permissionService.findUsersByPermissionCode(Constants.FINANCIAL_CASHIER);
		if(financialPersons.size()>0){
			vars.put("financialAuditUsers", financialPersons);
		}else{
			throw new RuntimeException("未找到财务审批人，请联系系统管理员配置");
		}
		List<String> financialManagers = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(financialManagers.size()>0){
			vars.put("financialManager", financialManagers.get(0));
		}else{
			throw new RuntimeException("未找到财务主管，请联系系统管理员配置");
		}
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));
		List<String> spreadPayApplyIdList = new ArrayList<>();
		List<String> pluginPayApplyIdList = new ArrayList<>();
		List<String> otherPayApplyIdList = new ArrayList<>();
		//所有的申请人
		Set<String> userIds = new HashSet<>();
		for(String idAndApplyType: idAndApplyTypes){
			//前台用符号@@拼凑的
			String[] idAndApplyTypeArr = idAndApplyType.split("@@");
			if(idAndApplyTypeArr.length == 2){
				String id = idAndApplyTypeArr[0];
				String applyType = idAndApplyTypeArr[1];
				String _userId = "";
				if(Constants.PAY_SPREAD.equals(applyType)){
					spreadPayApplyIdList.add(id);
					updateShopPayApplyResult(id, "OA_SpreadShopApply", auditResult);
					_userId = getApplyUserId("OA_SpreadShopApply", id);
				}else if(Constants.PAY_PLUG_IN.equals(applyType)){
					pluginPayApplyIdList.add(id);
					updateShopPayApplyResult(id, "OA_ShopPayPlugin", auditResult);
					_userId = getApplyUserId("OA_ShopPayPlugin", id);
				}else{
					otherPayApplyIdList.add(id);
					updateShopPayApplyResult(id, "OA_ShopOtherPay", auditResult);
					_userId = getApplyUserId("OA_ShopOtherPay", id);
				}
				if(StringUtils.isNotBlank(_userId)){
					userIds.add(_userId);
				}
			}else{
				continue;
			}
		}
		vars.put("users", userIds);
		shopPayApplyVo.setUserID(StringUtils.join(userIds, ","));
		List<String> userNames = new ArrayList<>();
		for(String usrId: userIds){
			String userName = staffService.getRealNameByUserId(usrId);
			if(StringUtils.isNotBlank(userName)){
				userNames.add(userName);
			}
		}
		shopPayApplyVo.setUserName(StringUtils.join(userNames, ","));
		shopPayApplyVo.setTitle(shopPayApplyVo.getUserName() + "的" + enumName);
		shopPayApplyVo.setSpreadPayApplyIds(StringUtils.join(spreadPayApplyIdList, ","));
		shopPayApplyVo.setPluginPayApplyIds(StringUtils.join(pluginPayApplyIdList, ","));
		shopPayApplyVo.setOtherPayApplyIds(StringUtils.join(otherPayApplyIdList, ","));
		vars.put("arg", shopPayApplyVo);
		//审批不同意
		if("2".equals(auditResult)){
			vars.put("supervisorAuditResult", TaskResultEnum.DISAGREE.getValue());
			shopPayApplyVo.setApplyResult(TaskResultEnum.DISAGREE.getValue());
			//审批同意	
		}else{
			vars.put("supervisorAuditResult", TaskResultEnum.AGREE.getValue());
			shopPayApplyVo.setApplyResult(TaskResultEnum.AGREE.getValue());
		}
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.SHOP_PAY_APPLY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), userId);
		taskService.complete(task.getId(), vars);
		
		saveShopPayApply(shopPayApplyVo, processInstance.getId());
	}
	private String getApplyUserId(String table, String id) {
		String sql = "select userID from "+table+" where id="+id;
		return baseDao.getUniqueResult(sql)+"";
	}
	private void updateShopPayApplyResult(String id, String table, String auditResult) {
		String sql = "update "+table+" set applyResult="+auditResult+" where id="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public List<ShopPayApplyListVo> getShopPayApplyListVos(ShopPayApplyVo shopPayApplyVo, Map<String, String> resultMap) {
		List<ShopPayApplyListVo> shopPayApplyListVos = new ArrayList<>();
		String spreadPayApplyIds = shopPayApplyVo.getSpreadPayApplyIds();
		String pluginPayApplyIds = shopPayApplyVo.getPluginPayApplyIds();
		String otherPayApplyIds = shopPayApplyVo.getOtherPayApplyIds();
		//所有类型的推广申请
		List<SpreadShopEntity> allTypeSpreadPayApplys = new ArrayList<>();
		if(StringUtils.isNotBlank(spreadPayApplyIds)){
			String[] spreadPayApplyIdArr = spreadPayApplyIds.split(",");
			for(String spreadPayApplyId: spreadPayApplyIdArr){
				SpreadShopApplyEntity spreadShopApply = getSpreadShopApply(spreadPayApplyId);
				List<SpreadShopEntity> preadPayApplys = getSpreadShops(spreadShopApply.getSpreadIds());
				allTypeSpreadPayApplys.addAll(preadPayApplys);
			}
			Map<String, Double[]> shopNameAndSpreadPayMap = getShopNameAndSpreadPayMap(allTypeSpreadPayApplys);
			int spreadNum = shopNameAndSpreadPayMap.size();
			int pluginNum = StringUtils.isBlank(pluginPayApplyIds) ? 0:pluginPayApplyIds.split(",").length;
			int otherNum = StringUtils.isBlank(otherPayApplyIds) ? 0:otherPayApplyIds.split(",").length;
			//次数最多的申请类型
			int maxNum = 0;
			if(spreadNum>pluginNum){
				maxNum = spreadNum;
			}else{
				maxNum = pluginNum;
			}
			if(otherNum>maxNum){
				maxNum = otherNum;
			}
			for(int i=0; i<maxNum; i++){
				shopPayApplyListVos.add(new ShopPayApplyListVo());
			}
			countByShopNameAndSpreadType(shopNameAndSpreadPayMap, shopPayApplyListVos, resultMap);
		}
		int index = 0;
		//付费插件费用合计
		double pulginPays = 0.0;
		if(StringUtils.isNotBlank(pluginPayApplyIds)){
			String[] pluginPayApplyIdArr = pluginPayApplyIds.split(",");
			if(shopPayApplyListVos.size()<1){
				int pluginNum = StringUtils.isBlank(pluginPayApplyIds) ? 0:pluginPayApplyIds.split(",").length;
				int otherNum = StringUtils.isBlank(otherPayApplyIds) ? 0:otherPayApplyIds.split(",").length;
				int maxNum = 0;
				if(pluginNum>otherNum){
					maxNum = pluginNum;
				}else{
					maxNum = otherNum;
				}
				for(int i=0; i<maxNum; i++){
					shopPayApplyListVos.add(new ShopPayApplyListVo());
				}
			}
			for(String pluginPayApplyId: pluginPayApplyIdArr){
				ShopPayPluginEntity shopPayPlugin = getShopPayPlugin(pluginPayApplyId);
				ShopPayApplyListVo shopPayApplyListVo = shopPayApplyListVos.get(index);
				shopPayApplyListVo.setPluginShopName(shopPayPlugin.getShopName());
				shopPayApplyListVo.setServiceName(shopPayPlugin.getServiceName());
				shopPayApplyListVo.setServiceUse(shopPayPlugin.getServiceUse());
				shopPayApplyListVo.setOpenTime(shopPayPlugin.getOpenTime());
				shopPayApplyListVo.setPayMoney(shopPayPlugin.getPayMoney());
				pulginPays += Double.parseDouble(shopPayPlugin.getPayMoney()==null?"0.0":shopPayPlugin.getPayMoney());
				index++;
			}	
			resultMap.put("付费插件费用合计", pulginPays+"");
		}
		index = 0;
		//其他服务费合计
		double otherPays = 0.0;
		if(StringUtils.isNotBlank(otherPayApplyIds)){
			String[] otherPayApplyIdArr = otherPayApplyIds.split(",");
			if(shopPayApplyListVos.size()<1){
				for(int i=0; i<otherPayApplyIdArr.length; i++){
					shopPayApplyListVos.add(new ShopPayApplyListVo());
				}
			}
			for(String otherPayApplyId: otherPayApplyIdArr){
				ShopOtherPayEntity shopOtherPay = getShopOtherPay(otherPayApplyId);
				ShopPayApplyListVo shopPayApplyListVo = shopPayApplyListVos.get(index);
				shopPayApplyListVo.setProjectName(shopOtherPay.getProjectName());
				shopPayApplyListVo.setProjectUse(shopOtherPay.getProjectUse());
				shopPayApplyListVo.setProjectDescription(shopOtherPay.getDescription());
				shopPayApplyListVo.setProjectPay(shopOtherPay.getProjectPay());
				otherPays += Double.parseDouble(shopOtherPay.getProjectPay()==null?"0.0":shopOtherPay.getProjectPay());
				index++;
			}
			resultMap.put("其他服务费合计", otherPays+"");
		}
		String spreadPayStr = resultMap.get("运营推广费用合计");
		//总计 = 运营推广费用合计 + 付费插件费用合计 + 其他服务费合计
		double allTotalPays = Double.parseDouble(spreadPayStr==null?"0.0":spreadPayStr) + pulginPays + otherPays;
		resultMap.put("总计",allTotalPays+"");
		return shopPayApplyListVos;
	}
	/**
	 * 根据店铺名分组
	 * @param allTypeSpreadPayApplys
	 * @return
	 */
	public Map<String, Double[]> getShopNameAndSpreadPayMap(List<SpreadShopEntity> allTypeSpreadPayApplys){
		//店铺为key，推广费用为value
		Map<String, Double[]> shopNameAndSpreadPayMap = new HashMap<>();
		for(SpreadShopEntity spreadShop: allTypeSpreadPayApplys){
			if(shopNameAndSpreadPayMap.containsKey(spreadShop.getShopName())){
				Double[] spreadPays = shopNameAndSpreadPayMap.get(spreadShop.getShopName());
				if("淘宝直通车".equals(spreadShop.getSpreadType())){
					spreadPays[0] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}else if("钻石展位".equals(spreadShop.getSpreadType())){
					spreadPays[1] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}else{
					spreadPays[2] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}
				shopNameAndSpreadPayMap.put(spreadShop.getShopName(), spreadPays);
			}else{
				Double[] spreadPays = {0.0, 0.0, 0.0};
				if("淘宝直通车".equals(spreadShop.getSpreadType())){
					spreadPays[0] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}else if("钻石展位".equals(spreadShop.getSpreadType())){
					spreadPays[1] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}else{
					spreadPays[2] += Double.parseDouble(spreadShop.getRechargeAmount()==null?"0.0":spreadShop.getRechargeAmount());
				}
				shopNameAndSpreadPayMap.put(spreadShop.getShopName(), spreadPays);
			}
		}
		return shopNameAndSpreadPayMap;
	}
	/**
	 * 根据店铺名和推广类型，计算下费用
	 * @param shopNameAndSpreadPayMap
	 * @param shopPayApplyListVos
	 * @param resultMap
	 */
	private void countByShopNameAndSpreadType(Map<String, Double[]> shopNameAndSpreadPayMap,
			List<ShopPayApplyListVo> shopPayApplyListVos, Map<String, String> resultMap) {
		Iterator<Entry<String, Double[]>> ite = shopNameAndSpreadPayMap.entrySet().iterator();
		//运营推广费用合计
		double totalSpreadPay = 0.0;
		int index = 0;
		while(ite.hasNext()){
			ShopPayApplyListVo shopPayApplyListVo = shopPayApplyListVos.get(index);
			Entry<String, Double[]> entry = ite.next();
			String shopName = entry.getKey();
			Double[] spreadPays = entry.getValue();
			shopPayApplyListVo.setSpreadShopName(shopName);
			double directPassMoney = spreadPays[0];
			double showMoney = spreadPays[1];
			double saleMoney = spreadPays[2];
			double totalMoney = directPassMoney+showMoney+saleMoney;
			totalSpreadPay += totalMoney;
			shopPayApplyListVo.setDirectPassMoney(directPassMoney==0.0?"/":directPassMoney+"");
			shopPayApplyListVo.setShowMoney(showMoney==0.0?"/":showMoney+"");
			shopPayApplyListVo.setSaleMoney(saleMoney==0.0?"/":saleMoney+"");
			shopPayApplyListVo.setTotal(totalMoney+"");
			index++;
		}
		resultMap.put("运营推广费用合计", totalSpreadPay+"");
	}
	private SpreadShopApplyEntity getSpreadShopApply(String spreadPayApplyId) {
		String hql = "from SpreadShopApplyEntity where id="+spreadPayApplyId;
		return (SpreadShopApplyEntity)baseDao.hqlfindUniqueResult(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<SpreadShopApplyEntity> getSpreadShopApplyList(String id) {
		String hql = "from SpreadShopApplyEntity where userID='"+id+"' and applyResult is null";
		return (List<SpreadShopApplyEntity>)baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopPayPluginEntity> getShopPayPluginList(String id) {
		String hql = "from ShopPayPluginEntity where userID='"+id+"' and applyResult is null";
		return  (List<ShopPayPluginEntity>)baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopOtherPayEntity> getShopOtherPayList(String id) {
		String hql = "from ShopOtherPayEntity where userID='"+id+"' and applyResult is null";
		return (List<ShopOtherPayEntity>)baseDao.hqlfind(hql);
	}
	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <T> List<ShopPayApplyVo> changeToShopPayApplyVo(List<T> objList, String paySpread) {
		List<ShopPayApplyVo> shopPayApplyVoList = new ArrayList<>();
		if(Constants.PAY_SPREAD.equals(paySpread)){
			List<SpreadShopApplyEntity> spreadShopApplyList = (List<SpreadShopApplyEntity>)objList;
			for(SpreadShopApplyEntity spreadShopApply: spreadShopApplyList){
				ShopPayApplyVo shopPayApply = new ShopPayApplyVo();
				shopPayApply.setSpreadPayApplyIds(spreadShopApply.getId()+"");
				shopPayApply.setTitle(spreadShopApply.getUserName()+"的店铺付费申请");
				shopPayApply.setRequestDate(DateUtil.formateFullDate(spreadShopApply.getAddTime()));
				shopPayApply.setAssigneeUserName(spreadShopApply.getSupervisorName());
				shopPayApply.setStatus("处理中");
				shopPayApply.setIsProcess("0");
				shopPayApplyVoList.add(shopPayApply);
			}
		}
		else if(Constants.PAY_PLUG_IN.equals(paySpread)){
			List<ShopPayPluginEntity> shopPayPluginList = (List<ShopPayPluginEntity>)objList;
			for(ShopPayPluginEntity shopPayPlugin: shopPayPluginList){
				ShopPayApplyVo shopPayApply = new ShopPayApplyVo();
				shopPayApply.setPluginPayApplyIds(shopPayPlugin.getId()+"");
				shopPayApply.setTitle(shopPayPlugin.getUserName()+"的店铺付费申请");
				shopPayApply.setRequestDate(DateUtil.formateFullDate(shopPayPlugin.getAddTime()));
				shopPayApply.setAssigneeUserName(shopPayPlugin.getSupervisorName());
				shopPayApply.setStatus("处理中");
				shopPayApply.setIsProcess("0");
				shopPayApplyVoList.add(shopPayApply);
			}
		}
		else{
			List<ShopOtherPayEntity> otherPayList = (List<ShopOtherPayEntity>)objList;
			for(ShopOtherPayEntity otherPay: otherPayList){
				ShopPayApplyVo shopPayApply = new ShopPayApplyVo();
				shopPayApply.setOtherPayApplyIds(otherPay.getId()+"");
				shopPayApply.setTitle(otherPay.getUserName()+"的店铺付费申请");
				shopPayApply.setRequestDate(DateUtil.formateFullDate(otherPay.getAddTime()));
				shopPayApply.setAssigneeUserName(otherPay.getSupervisorName());
				shopPayApply.setStatus("处理中");
				shopPayApply.setIsProcess("0");
				shopPayApplyVoList.add(shopPayApply);
			}
		}
		return shopPayApplyVoList;
	}
	@Override
	public void updateShopApplyInfo(String shopType, String aliPayAccount,
			String aliPayPhone, String publicBankAccount, String pInstanceId) {
		String sql = "";
		if("企业开店".equals(shopType)){
			sql = "update OA_ShopApply set companyAliPayAccount='"+aliPayAccount+
					"', companyAliPayPhone='"+aliPayPhone+"', publicBankAccount='"+publicBankAccount+"' where processInstanceID='"+pInstanceId+"'";
		}else{
			sql = "update OA_ShopApply set privateAliPayAccount='"+aliPayAccount+
					"', privateAliPayPhone='"+aliPayPhone+"', publicBankAccount='"+publicBankAccount+"' where processInstanceID='"+pInstanceId+"'";
		}
		baseDao.excuteSql(sql);
	}
	@Override
	public ListResult<Object> getShopPayApplyVosByAssigner(String id, String applyerId, String beginDate,
			String endDate, Integer page, Integer limit) {
		String sqlList = "SELECT\n" +
				"			p.PROC_INST_ID_,\n" +
				"			t.NAME_,\n" +
				"			p.START_TIME_,\n" +
				"			p.END_TIME_,\n" +
				"			group_concat(s.StaffName) as applyer\n" +
				"FROM\n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			OA_ShopPayApply shopPay,\n" +
				"			OA_Staff s\n" +
				"WHERE\n" +
				"		    p.PROC_DEF_ID_ LIKE 'shopPayApply%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND t.ASSIGNEE_ = '"+id+"'\n" +
				"		AND shopPay.ProcessInstanceID = p.PROC_INST_ID_ and t.NAME_ !='办理成功'\n" +
				"		AND LOCATE(s.UserID,shopPay.userID)>0\n";
		if(StringUtils.isNotBlank(applyerId)){
			sqlList += "AND LOCATE('"+applyerId+"',shopPay.userID)>0\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlList += "AND Date(shopPay.addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlList += "AND Date(shopPay.addTime)<='"+endDate+"'\n"; 
		}
		sqlList += "group by PROC_INST_ID_, shopPay.UserID order by START_TIME_ desc ";
		List<Object> list = baseDao.findPageList(sqlList, page, limit);
		String sqlCount = "SELECT count(*) from(SELECT \n" +
				"			p.ID_ \n" +
				"FROM \n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			OA_ShopPayApply shopPay,\n" +
				"			OA_Staff s\n" +
				"WHERE\n" +
				"		    p.PROC_DEF_ID_ LIKE 'shopPayApply%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND t.ASSIGNEE_ = '"+id+"'\n" +
				"		AND shopPay.ProcessInstanceID = p.PROC_INST_ID_ and t.NAME_ !='办理成功'\n" +
				"		AND LOCATE(s.UserID,shopPay.userID)>0\n";
		if(StringUtils.isNotBlank(applyerId)){
			sqlCount += "AND LOCATE('"+applyerId+"',shopPay.userID)>0\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlCount += "AND Date(shopPay.addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlCount += "AND Date(shopPay.addTime)<='"+endDate+"'\n"; 
		}
		sqlCount += "group by p.PROC_INST_ID_, shopPay.UserID)a";
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(list, count);
	}
	
}
