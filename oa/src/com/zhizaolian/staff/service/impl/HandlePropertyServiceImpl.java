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
import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.entity.HandlePropertyEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssetUsageService;
import com.zhizaolian.staff.service.HandlePropertyService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.AssetVOTransFormer;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetUsageVO;
import com.zhizaolian.staff.vo.AssetVO;
import com.zhizaolian.staff.vo.HandlePropertyVo;

public class HandlePropertyServiceImpl implements HandlePropertyService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private AssetUsageService assetUsageService;
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
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Override
	public ListResult<AssetVO> getAssetList(String assetNum, String assetName, String assetStatus, Integer limit, Integer page) {
		String hqlList = "from AssetEntity where isDeleted=0";
		if(StringUtils.isNotBlank(assetNum)){
			hqlList += " and SerialNumber like '%" + EscapeUtil.decodeSpecialChars(assetNum) + "%'";
		}
		if(StringUtils.isNotBlank(assetName)){
			hqlList += " and AssetName like '%" + EscapeUtil.decodeSpecialChars(assetName) + "%'";
		}
		if(StringUtils.isNotBlank(assetStatus)){
			hqlList +=  " and Status="+assetStatus;
		}
		hqlList += " order by addTime";
		Object objLst = baseDao.hqlPagedFind(hqlList,page,limit);
		@SuppressWarnings("unchecked")
		List<AssetEntity> assetList = (List<AssetEntity>)objLst;
		String hqlCount="select count(*) from AssetEntity where isDeleted=0";
		if(StringUtils.isNotBlank(assetNum)){
			hqlCount += " and SerialNumber like '%" + EscapeUtil.decodeSpecialChars(assetNum) + "%'";
		}
		if(StringUtils.isNotBlank(assetName)){
			hqlCount += " and AssetName like '%" + EscapeUtil.decodeSpecialChars(assetName) + "%'";
		}
		if(StringUtils.isNotBlank(assetStatus)){
			hqlCount +=  " and Status="+assetStatus;
		}
		Integer count = ((Long) baseDao.hqlfindUniqueResult(hqlCount))
				.intValue();
		List<AssetVO> list=new ArrayList<AssetVO>();
		for(AssetEntity assetEntity: assetList){ 
			AssetVO assetVO = AssetVOTransFormer.entityToVO(assetEntity);
			AssetUsageVO assetUsageVO = assetUsageService.getAssstUsageByAssetID1(assetVO.getAssetID());
			assetVO.setAssetUsageVO(assetUsageVO);
			
			list.add(assetVO);
			
		}
		return new ListResult<AssetVO>(list, count);
	}
	@Override
	public void startHandleProperty(HandlePropertyVo handlePropertyVo, String recipientId) {
		String enumName = BusinessTypeEnum.HANDLE_PROPERTY.getName();
		handlePropertyVo.setBusinessType(enumName);
		handlePropertyVo.setTitle(handlePropertyVo.getUserName() + "的" + enumName);
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
		String manager = staffService.queryManager(handlePropertyVo.getUserID());
		
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<Group> groups = identityService.createGroupQuery().groupMember(handlePropertyVo.getUserID()).list();
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
		vars.put("arg", handlePropertyVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.HANDLE_PROPERTY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), handlePropertyVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveHandleProperty(handlePropertyVo, processInstance.getId());
		
	}
	private void saveHandleProperty(HandlePropertyVo handlePropertyVo, String id) {
		HandlePropertyEntity handlePropertyEntity = null;
		try {
			handlePropertyEntity = (HandlePropertyEntity) CopyUtil.tryToEntity(handlePropertyVo, HandlePropertyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		handlePropertyEntity.setProcessInstanceID(id);
		handlePropertyEntity.setAddTime(new Date());
		handlePropertyEntity.setIsDeleted(0);
		baseDao.hqlSave(handlePropertyEntity);
	}
	@Override
	public void updateHandelPropertyProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update HandlePropertyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}
	@Override
	public ListResult<HandlePropertyVo> findHandlePropertyListByUserID(String id, Integer page, Integer limit) {
		List<HandlePropertyEntity> handlePropertys = getHandlePropertyByUserId(id,
				page, limit);

		List<HandlePropertyVo> handlePropertyVos = new ArrayList<HandlePropertyVo>();
		for (HandlePropertyEntity handleProperty : handlePropertys) {
			HandlePropertyVo handlePropertyVo = null;
			try {
				handlePropertyVo = (HandlePropertyVo) CopyUtil.tryToVo(handleProperty, HandlePropertyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(handleProperty.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					HandlePropertyVo arg = (HandlePropertyVo) variable.getValue();
					handlePropertyVo.setRequestDate(arg.getRequestDate());
					handlePropertyVo.setTitle(arg.getTitle());
					handlePropertyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(handleProperty.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				handlePropertyVo.setStatus("处理中");
				handlePropertyVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = handlePropertyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						handlePropertyVo.setStatus(t.getName());
				}
			}
			handlePropertyVos.add(handlePropertyVo);
		}
		int count = getHandlePropertyCountByUserId(id);
		return new ListResult<HandlePropertyVo>(handlePropertyVos, count);
	}
	private int getHandlePropertyCountByUserId(String id) {
		String hql = "select count(id) from HandlePropertyEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<HandlePropertyEntity> getHandlePropertyByUserId(String id, Integer page, Integer limit) {
		String hql="from HandlePropertyEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<HandlePropertyEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public HandlePropertyVo getHandlePropertyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		HandlePropertyVo handlePropertyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				handlePropertyVo = (HandlePropertyVo) variable.getValue();
			}
		}
		return handlePropertyVo;
	}
}
