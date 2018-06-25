package com.zhizaolian.staff.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.ChopDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CarveChopEntity;
import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.entity.ChopBorrow;
import com.zhizaolian.staff.entity.ChopDestroyEntity;
import com.zhizaolian.staff.entity.ContractDetailEntity;
import com.zhizaolian.staff.entity.IdBorrowEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.BrandAuthService;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.vo.CarveChopVo;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.ChopDestroyVo;
import com.zhizaolian.staff.vo.ChopUseLogVo;
import com.zhizaolian.staff.vo.ContractDetailVo;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.IdBorrowVo;

public class ChopServiceImpl implements ChopService {
	@Autowired
	private ChopDao chopDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private BrandAuthService brandAuthService;
	@Override
	public ListResult<Chop> getChopByName(String name, String id, int page,
			int limit) {
		List<Chop> chops = chopDao.getChopList(name,
				id == null ? null : Integer.parseInt(id), page, limit);
		int count = chopDao.getChopListCount(name,
				id == null ? null : Integer.parseInt(id));
		List<Chop> filled_chops = Lists2.transform(chops,
				new Function<Chop, Chop>() {
					@Override
					public Chop apply(Chop arg0) {
						arg0.setStore_personName(
								staffDao.getEmployeeNameByUsrId(
										arg0.getStore_person()));
						arg0.setSubject_personName(
								staffDao.getEmployeeNameByUsrId(
										arg0.getSubject_person()));
						return arg0;
					}
				});
		return new ListResult<Chop>(filled_chops, count);
	}

	@Override
	public void delete(String id) {
		chopDao.deleteChop(id);
	}

	@Override
	public void save(Chop chop) {
		chopDao.saveChop(chop);
	}

	@Override
	public void update(Chop chop) {
		chopDao.updateChop(chop);
	}

	@Override
	public Chop getChopById(String id) {
		List<Chop> chops = chopDao.getChopList(null, Integer.parseInt(id), 1,
				1);
		return CollectionUtils.isEmpty(chops) ? null : chops.get(0);
	}

	@Override
	public void startChopBorrow(ChopBorrrowVo chopBorrrowVo, String processInstanceId) {
		String enumName = BusinessTypeEnum.CHOP_BORROW.getName();
		chopBorrrowVo.setBusinessType(enumName);
		chopBorrrowVo.setTitle(chopBorrrowVo.getUser_Name() + "的" + enumName);
		chopBorrrowVo.setUserName(chopBorrrowVo.getUser_Name());
		Map<String, Object> vars = new HashMap<String, Object>();
		List<Chop> chops = chopDao.getChopList(null, chopBorrrowVo.getChop_Id(),
				1, 1);
		Chop chop = chops.get(0);
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(chopBorrrowVo.getUser_Id());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(chopBorrrowVo.getUser_Id());
		}
		if (StringUtils.isBlank(supervisor) || chopBorrrowVo.getUser_Id().equals(supervisor)) {
			supervisor=staffService.querySupervisor(chopBorrrowVo.getUser_Id());
		}
		String manager = staffService.queryManager(chopBorrrowVo.getUser_Id());
		
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("id_storage_user", chop.getStore_person());
		vars.put("id_subject_user", chop.getSubject_person());
		vars.put("chop_isBorrow", chopBorrrowVo.getIsBorrow());
		vars.put("arg", chopBorrrowVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CHOP_BORROW);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), chopBorrrowVo.getUser_Id());
		taskService.complete(task.getId(), vars);
		saveChopBorrow(chopBorrrowVo, processInstance.getId());
		//品牌授权的公章申请，跳转过来的
		if(StringUtils.isNotBlank(processInstanceId)){
			brandAuthService.updateChopProcessInstanceId(processInstanceId, processInstance.getId());
			Task brandAuthTask = taskService.createTaskQuery().processInstanceId(
					processInstanceId).singleResult();
			taskService.setAssignee(brandAuthTask.getId(), chopBorrrowVo.getUser_Id());
			taskService.complete(brandAuthTask.getId());
		}
	}
	private void saveChopBorrow(ChopBorrrowVo chopBorrrowVo,
			String instanceId) {
		ChopBorrow chopBorrow = null;
		try {
			chopBorrow = (ChopBorrow) CopyUtil.DeclaredFieldCopy(
					ChopBorrow.class, chopBorrrowVo, 2, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		chopBorrow.setProcessInstanceID(instanceId);
		chopDao.saveChopBorrow(chopBorrow);;
	}

	@Override
	public ListResult<ChopBorrrowVo> findChopBorrrowListByUserID(String userId,
			int page, int limit) {
		List<ChopBorrow> chopBorrrows = chopDao.getChopBorrowByUserId(userId,
				page, limit);

		List<ChopBorrrowVo> chopBorrrowVos = new ArrayList<ChopBorrrowVo>();
		for (ChopBorrow chopBorrow : chopBorrrows) {
			ChopBorrrowVo chopBorrrowVo = null;
			try {
				chopBorrrowVo = (ChopBorrrowVo) CopyUtil.DeclaredFieldCopy(
						ChopBorrrowVo.class, chopBorrow, 1, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(chopBorrrowVo.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ChopBorrrowVo arg = (ChopBorrrowVo) variable.getValue();
					chopBorrrowVo.setRequestDate(arg.getRequestDate());
					chopBorrrowVo.setTitle(arg.getTitle());
					chopBorrrowVo.setUserName(arg.getUser_Name());
					chopBorrrowVo.setUser_Name(arg.getUserName());
					chopBorrrowVo.setUser_Id(arg.getUserID());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(chopBorrrowVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				chopBorrrowVo.setStatus("处理中");
				chopBorrrowVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = chopBorrrowVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						chopBorrrowVo.setStatus(t.getName());
				}
			}
			Integer chopId = chopBorrrowVo.getChop_Id();
			chopBorrrowVo.setChop_Name(
					chopDao.getChopList(null, chopId, 1, 1).get(0).getName());
			chopBorrrowVos.add(chopBorrrowVo);
		}
		int count = chopDao.getChopBorrowCountByUserId(userId);
		return new ListResult<ChopBorrrowVo>(chopBorrrowVos, count);
	}

	@Override
	public ChopBorrrowVo getChopByInstanceId(String instanceId) {
		ChopBorrow chopBorrow = chopDao.getChopBorrowByInsctnceId(instanceId);
		ChopBorrrowVo chopBorrrowVo = null;
		try {
			chopBorrrowVo = (ChopBorrrowVo) CopyUtil.DeclaredFieldCopy(
					ChopBorrrowVo.class, chopBorrow, 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		Integer chopId = chopBorrrowVo.getChop_Id();
		chopBorrrowVo.setChop_Name(
				chopDao.getChopList(null, chopId, 1, 1).get(0).getName());
		return chopBorrrowVo;
	}

	@Override
	public void updateChopBorrowStatus(String instanceId, Integer status) {
		chopDao.updateChopBorrowStatus(instanceId, status);
	}

	@Override
	public void updateRealBeginTime(String instanceId) {
		ChopBorrow chopBorrow = chopDao.getChopBorrowByInsctnceId(instanceId);
		chopBorrow.setReal_startTime(new Date());;
		chopDao.updateChopBorrow(chopBorrow);
	}

	@Override
	public void updateRealEnd(String instanceId) {
		ChopBorrow chopBorrow = chopDao.getChopBorrowByInsctnceId(instanceId);
		chopBorrow.setReal_endTime(new Date());;
		chopDao.updateChopBorrow(chopBorrow);
	}

	@Override
	public void startIdBorrow(IdBorrowVo idBorrowVo) {
		String enumName = BusinessTypeEnum.ID_BORROW.getName();
		idBorrowVo.setBusinessType(enumName);
		idBorrowVo.setTitle(idBorrowVo.getUser_Name() + "的" + enumName);
		idBorrowVo.setUserName(idBorrowVo.getUser_Name());
		Map<String, Object> vars = new HashMap<String, Object>();

		vars.put("id_item_user", idBorrowVo.getItem_User_Id());
		vars.put("id_is_borrow", idBorrowVo.getIsBorrow());
		vars.put("arg", idBorrowVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.ID_BORROW);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), idBorrowVo.getUser_Id());
		taskService.complete(task.getId(), vars);
		saveIDBorrow(idBorrowVo, processInstance.getId());

	}
	private void saveIDBorrow(IdBorrowVo idBorrowVo, String instanceId) {
		IdBorrowEntity idBorrowEntity = null;
		try {
			idBorrowEntity = (IdBorrowEntity) CopyUtil.DeclaredFieldCopy(
					IdBorrowEntity.class, idBorrowVo, 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		idBorrowEntity.setProcessInstanceID(instanceId);
		chopDao.saveIDBorrow(idBorrowEntity);;
	}
	@Override
	public void updateIdRealBeginTime(String instanceId) {
		IdBorrowEntity idBorrow = chopDao.getIdBorrowByInstanceId(instanceId);
		idBorrow.setReal_startTime(new Date());;
		chopDao.updateIdBorrow(idBorrow);
	}

	@Override
	public void updateIdRealEndTime(String instanceId) {
		IdBorrowEntity idBorrow = chopDao.getIdBorrowByInstanceId(instanceId);
		idBorrow.setReal_endTime(new Date());;
		chopDao.updateIdBorrow(idBorrow);
	}

	@Override
	public ListResult<IdBorrowVo> findIdBorrrowListByUserID(String userId,
			int page, int limit) {
		List<IdBorrowEntity> idBorrows = chopDao.getIdBorrowByUserId(userId,
				page, limit);
		List<IdBorrowVo> idorrrowVos = new ArrayList<IdBorrowVo>();
		for (IdBorrowEntity IdBorrow : idBorrows) {
			IdBorrowVo idBorrowVo = null;
			try {
				idBorrowVo = (IdBorrowVo) CopyUtil
						.DeclaredFieldCopy(IdBorrowVo.class, IdBorrow, 2, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(idBorrowVo.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					IdBorrowVo arg = (IdBorrowVo) variable.getValue();
					idBorrowVo.setRequestDate(arg.getRequestDate());
					idBorrowVo.setTitle(arg.getTitle());
					idBorrowVo.setUserName(arg.getUser_Name());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(idBorrowVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				idBorrowVo.setStatus("处理中");
				idBorrowVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = idBorrowVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						idBorrowVo.setStatus(t.getName());
				}
			}
			idBorrowVo.setItem_User_Name(staffDao
					.getEmployeeNameByUsrId(idBorrowVo.getItem_User_Id()));
			idorrrowVos.add(idBorrowVo);
		}
		int count = chopDao.getIdBorrowCountByUserId(userId);
		return new ListResult<IdBorrowVo>(idorrrowVos, count);
	}

	@Override
	public void updateIdBorrowStatus(String instanceId, Integer status) {
		IdBorrowEntity idBorrowEntity = chopDao
				.getIdBorrowByInstanceId(instanceId);
		idBorrowEntity.setApplyResult(status);
		chopDao.updateIdBorrow(idBorrowEntity);
	}

	@Override
	public void startContract(ContractDetailVo vo,File file,String fileName) throws Exception {
		String enumName = BusinessTypeEnum.CONTRACT.getName();
		vo.setBusinessType(enumName);
		vo.setTitle(vo.getRequestUserName() + "的" + enumName);
		vo.setUserName(vo.getRequestUserName());
		Map<String, Object> vars = new HashMap<String, Object>();

		vars.put("contract_subject_person", vo.getSubjectPersonId());
		vars.put("contract_sign_person", vo.getSignPersoId());
		vars.put("arg", vo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CONTRACT_DETAIL);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		if(file!=null){
			InputStream is = new FileInputStream(file);
			taskService.createAttachment("picture", task.getId(), processInstance.getId(), fileName, "WeChat screenshots", is);	
		}
		taskService.setAssignee(task.getId(), vo.getRequestUserId());
		taskService.complete(task.getId(), vars);
		saveContractDetail(vo, processInstance.getId());

	}
	private void saveContractDetail(ContractDetailVo vo, String instanceId) {
		ContractDetailEntity entity = null;
		try {
			entity = (ContractDetailEntity) CopyUtil
					.DeclaredFieldCopy(ContractDetailEntity.class, vo, 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
		entity.setProcessInstanceID(instanceId);;
		chopDao.saveContract(entity);
	}
	@Override
	public void updateContract(ContractDetailEntity contractDetailEntity) {
		chopDao.updateContract(contractDetailEntity);
	}

	@Override
	public ContractDetailEntity getContractByInstanceId(String instanceId) {
		return chopDao.getContractByInstanceId(instanceId);
	}

	@Override
	public ListResult<ContractDetailVo> findContractListByUserID(String userId,
			int page, int limit) {
		List<ContractDetailEntity> list = chopDao.getContractByUserId(userId,
				page, limit);
		List<ContractDetailVo> voList = new ArrayList<ContractDetailVo>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0, n = list.size(); i < n; i++) {
				ContractDetailVo cDetailVo = null;
				try {
					cDetailVo = (ContractDetailVo) CopyUtil.DeclaredFieldCopy(
							ContractDetailVo.class, list.get(i), 2, null);
				} catch (Exception exception) {
					continue;
				}
				String requestId = cDetailVo.getRequestUserId();
				String subjectId = cDetailVo.getSubjectPersonId();
				String signId = cDetailVo.getSignPersoId();
				String storePersonId = cDetailVo.getStorePersonId();
				String resId=cDetailVo.getResponsiblePersonId();
				if (StringUtils.isNotBlank(requestId)) {
					cDetailVo.setRequestUserName(
							staffDao.getEmployeeNameByUsrId(requestId));
				}
				if (StringUtils.isNotBlank(subjectId)) {
					cDetailVo.setSubjectPersonName(
							staffDao.getEmployeeNameByUsrId(subjectId));
				}
				if (StringUtils.isNotBlank(signId)) {
					cDetailVo.setSignPersonName(
							staffDao.getEmployeeNameByUsrId(signId));
				}
				if (StringUtils.isNotBlank(storePersonId)) {
					cDetailVo.setStorePersonName(
							staffDao.getEmployeeNameByUsrId(storePersonId));
				}
				if(StringUtils.isNotBlank(resId)){
					cDetailVo.setRequestUserName(
							staffDao.getEmployeeNameByUsrId(resId));
				}
				List<HistoricDetail> datas = historyService
						.createHistoricDetailQuery()
						.processInstanceId(cDetailVo.getProcessInstanceID())
						.list();
				for (HistoricDetail historicDetail : datas) {
					HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
					if ("arg".equals(variable.getVariableName())) {
						ContractDetailVo arg = (ContractDetailVo) variable.getValue();
						cDetailVo.setRequestDate(arg.getRequestDate());
						cDetailVo.setTitle(arg.getTitle());
						cDetailVo.setUserName(arg.getRequestUserName());
					}
				}
				ProcessInstance pInstance = runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(cDetailVo.getProcessInstanceID())
						.singleResult();
				if (pInstance != null) {
					cDetailVo.setStatus("处理中");
					cDetailVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				} else {
					Integer value_ = cDetailVo.getProcessStatus();
					if (value_ != null) {
						TaskResultEnum t = TaskResultEnum.valueOf(value_);
						if (t != null)
							cDetailVo.setStatus(t.getName());
					}
				}
				voList.add(cDetailVo);
			}
		}
		int count = chopDao.getContractCountByUserId(userId);
		return new ListResult<ContractDetailVo>(voList, count);
	}

	@Override
	public void updateContractStatus(String instanceId, Integer status) {
		ContractDetailEntity entity = chopDao
				.getContractByInstanceId(instanceId);
		entity.setProcessStatus(status);
		chopDao.updateContract(entity);
	}

	@Override
	public List<String> getTaskIds(String tableName,String userId, String type, String No_code,
			String startTime, String endTime, int page, int limit, String applyerId) {
		return chopDao.getTaskIds(tableName,userId, type, No_code, startTime, endTime, page, limit, applyerId);
	}

	@Override
	public int getTaskCount(String tableName,String userId, String type, String No_code,
			String startTime, String endTime, String applyerId) {
		return chopDao.getTaskCount(tableName,userId, type, No_code, startTime, endTime, applyerId);
	}

	@Override
	public ListResult<ChopBorrrowVo> findChopLogListByKeys(
			Map<String, String> queryMap, int page, int limit) {
		List<ChopBorrow> chopBorrrows = chopDao.findChopLogListByKeys(queryMap,
				page, limit);

		List<ChopBorrrowVo> chopBorrrowVos = new ArrayList<ChopBorrrowVo>();
		for (ChopBorrow chopBorrow : chopBorrrows) {
			ChopBorrrowVo chopBorrrowVo = null;
			try {
				chopBorrrowVo = (ChopBorrrowVo) CopyUtil.DeclaredFieldCopy(
						ChopBorrrowVo.class, chopBorrow, 1, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			chopBorrrowVo.setUser_Name(staffDao.getEmployeeNameByUsrId(chopBorrrowVo.getUser_Id()));
			chopBorrrowVo.setChop_Name(
					chopDao.getChopList(null, chopBorrrowVo.getChop_Id(), 1, 1).get(0).getName());
			chopBorrrowVos.add(chopBorrrowVo);
		}
		int count = chopDao.findChopLogCountByKeys(queryMap);
		return new ListResult<ChopBorrrowVo>(chopBorrrowVos, count);
	}

	@Override
	public ListResult<IdBorrowVo> findIdBorrowListByKeys(
			Map<String, String> queryMap, int page, int limit) {
		List<IdBorrowEntity> idBorrows = chopDao.getIdBorrowByKeys(queryMap,page, limit);
		List<IdBorrowVo> idorrrowVos = new ArrayList<IdBorrowVo>();
		for (IdBorrowEntity IdBorrow : idBorrows) {
			IdBorrowVo idBorrowVo = null;
			try {
				idBorrowVo = (IdBorrowVo) CopyUtil
						.DeclaredFieldCopy(IdBorrowVo.class, IdBorrow, 2, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			idBorrowVo.setItem_User_Name(staffDao
					.getEmployeeNameByUsrId(idBorrowVo.getItem_User_Id()));
			idorrrowVos.add(idBorrowVo);
		}
		int count = chopDao.getIdBorrowCountByKeys(queryMap);
		return new ListResult<IdBorrowVo>(idorrrowVos, count);
	}

	@Override
	public ListResult<ContractDetailVo> findContractListByKeys(
			Map<String, String> queryMap) {
		return null;
	}
	@Autowired
	private BaseDao baseDao;
	@Override
	public ListResult<ChopBorrrowVo> getChopBorrowVoLst(String name, int page, int limit) {
		String sqlLst = "SELECT\n"+
					 "ChopBorrow_Id,\n"+
					 "staff.StaffName,\n"+
					 "chop.`Name`,\n"+
					 "borrow.AddTime\n"+
					 "FROM\n"+
					 "OA_CHopBorrow borrow\n"+
					 "LEFT JOIN OA_Chop chop ON borrow.Chop_Id = chop.Chop_Id\n"+
					 "LEFT JOIN OA_Staff staff ON borrow.user_id = staff.userId\n"+
					 "WHERE\n"+
					 "borrow.IsDeleted = 0\n";
		if(StringUtils.isNotBlank(name)){
			sqlLst += "and name like '%"+name+"%'\n";
		}
		sqlLst +=  "ORDER BY\n"+
				 "borrow.AddTime DESC";
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		String sqlCount = "select count(ChopBorrow_Id) from OA_CHopBorrow where IsDeleted=0";
		List<ChopBorrrowVo> chopBorrowLst = new ArrayList<>();
		for(Object obj: objLst){
			ChopBorrrowVo chopBorrow = new ChopBorrrowVo();
			Object[] objs = (Object[])obj;
			chopBorrow.setChopBorrow_Id(Integer.parseInt(objs[0]+""));
			chopBorrow.setUserName(objs[1]+"");
			chopBorrow.setChop_Name(objs[2]+"");
			chopBorrow.setAddTime(DateUtil.getFullDate(objs[3]+""));
			chopBorrowLst.add(chopBorrow);
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<ChopBorrrowVo>(chopBorrowLst, count);
	}

	@Override
	public String getPInstanceId(String chopBorrowId) {
		String sql = "select ProcessInstanceID from OA_CHopBorrow where ChopBorrow_Id="+chopBorrowId;
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public ListResult<ChopUseLogVo> getChopUseLog(String[] query, int limit, int page) {
		String sqlLst = "SELECT\n"+
						"b.addTime,\n"+
						"staff.StaffName,\n"+
						"b.chopName,\n"+
						"b.chopType,\n"+
						"b.certificateName,\n"+
						"b.certificateType,\n"+
						"useIdReason,\n"+
						"b.userId\n"+
						"FROM\n"+
							"(\n"+
								"SELECT\n"+
									"id.addTime,\n"+
									"id.User_Id userId,\n"+
									"a.chopName,\n"+
									"a.chopType,\n"+
									"a.certificateName,\n"+
									"a.certificateType,\n"+
									"CASE id.Item_User_Id\n"+
								"WHEN '15a9a34e-dcbd-4112-bf42-2a957bc03224' THEN\n"+
									"id.Reason\n"+
								"END useIdReason\n"+
								"FROM\n"+
									"(\n"+
								"SELECT\n"+
									"DATE(chopBorrow.AddTime) addTime,\n"+
									"chopBorrow.`Name` chopName,\n"+
									"chopBorrow.Type chopType,\n"+
									"certificate.`name` certificateName,\n"+
									"certificate.type certificateType,\n"+
									"chopBorrow.User_Id userId\n"+
								"FROM\n"+
									"(\n"+
										"SELECT\n"+
											"`Name`,\n"+
											"chop.Type,\n"+
											"chopBorrow.AddTime,\n"+
											"chopBorrow.User_Id\n"+
										"FROM\n"+
											"OA_ChopBorrow chopBorrow\n"+
										"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
									") chopBorrow\n"+
								"LEFT JOIN (\n"+
									"SELECT\n"+
										"`name`,\n"+
										"certificateBorrow.addTime,\n"+
										"certificateBorrow.userId,\n"+
										"certificate.type\n"+
									"FROM\n"+
										"OA_CertificateBorrow certificateBorrow\n"+
									"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
								") certificate ON chopBorrow.User_Id = certificate.userId\n"+
								"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
								"UNION\n"+
									"SELECT\n"+
										"DATE(certificate.AddTime) addTime,\n"+
										"chopBorrow.`Name` chopName,\n"+
										"chopBorrow.Type chopType,\n"+
										"certificate.`name` certificateName,\n"+
										"certificate.type certificateType,\n"+
										"certificate.userId\n"+
									"FROM\n"+
										"(\n"+
											"SELECT\n"+
												"`Name`,\n"+
												"chop.Type,\n"+
												"chopBorrow.AddTime,\n"+
												"chopBorrow.User_Id\n"+
											"FROM\n"+
												"OA_ChopBorrow chopBorrow\n"+
											"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
										") chopBorrow\n"+
									"RIGHT JOIN (\n"+
										"SELECT\n"+
											"`name`,\n"+
											"certificateBorrow.addTime,\n"+
											"certificateBorrow.userId,\n"+
											"certificate.type\n"+
										"FROM\n"+
											"OA_CertificateBorrow certificateBorrow\n"+
										"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
									") certificate ON chopBorrow.User_Id = certificate.userId\n"+
									"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
							") a\n"+
						"RIGHT JOIN OA_IdBorrow id ON a.userId = id.User_Id\n"+
						"AND DATE(a.addTime) = DATE(id.AddTime)\n"+
						"UNION\n"+
							"SELECT\n"+
								"a.addTime,\n"+
								"a.userId,\n"+
								"a.chopName,\n"+
								"a.chopType,\n"+
								"a.certificateName,\n"+
								"a.certificateType,\n"+
								"CASE id.Item_User_Id\n"+
							"WHEN '15a9a34e-dcbd-4112-bf42-2a957bc03224' THEN\n"+
								"id.Reason\n"+
							"END useIdReason\n"+
							"FROM\n"+
								"(\n"+
									"SELECT\n"+
										"DATE(chopBorrow.AddTime) addTime,\n"+
										"chopBorrow.`Name` chopName,\n"+
										"chopBorrow.Type chopType,\n"+
										"certificate.`name` certificateName,\n"+
										"certificate.type certificateType,\n"+
										"chopBorrow.User_Id userId\n"+
									"FROM\n"+
										"(\n"+
											"SELECT\n"+
												"`Name`,\n"+
												"chop.Type,\n"+
												"chopBorrow.AddTime,\n"+
												"chopBorrow.User_Id\n"+
											"FROM\n"+
												"OA_ChopBorrow chopBorrow\n"+
											"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
										") chopBorrow\n"+
									"LEFT JOIN (\n"+
										"SELECT\n"+
											"`name`,\n"+
											"certificateBorrow.addTime,\n"+
											"certificateBorrow.userId,\n"+
											"certificate.type\n"+
										"FROM\n"+
											"OA_CertificateBorrow certificateBorrow\n"+
										"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
									") certificate ON chopBorrow.User_Id = certificate.userId\n"+
									"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
									"UNION\n"+
										"SELECT\n"+
											"DATE(certificate.AddTime) addTime,\n"+
											"chopBorrow.`Name` chopName,\n"+
											"chopBorrow.Type chopType,\n"+
											"certificate.`name` certificateName,\n"+
											"certificate.type certificateType,\n"+
											"certificate.userId\n"+
										"FROM\n"+
											"(\n"+
												"SELECT\n"+
													"`Name`,\n"+
													"chop.Type,\n"+
													"chopBorrow.AddTime,\n"+
													"chopBorrow.User_Id\n"+
												"FROM\n"+
													"OA_ChopBorrow chopBorrow\n"+
												"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
											") chopBorrow\n"+
										"RIGHT JOIN (\n"+
											"SELECT\n"+
												"`name`,\n"+
												"certificateBorrow.addTime,\n"+
												"certificateBorrow.userId,\n"+
												"certificate.type\n"+
											"FROM\n"+
												"OA_CertificateBorrow certificateBorrow\n"+
											"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
										") certificate ON chopBorrow.User_Id = certificate.userId\n"+
										"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
								") a\n"+
							"LEFT JOIN OA_IdBorrow id ON a.userId = id.User_Id\n"+
							"AND DATE(a.addTime) = DATE(id.AddTime)\n"+
							") b\n"+
							"INNER JOIN OA_Staff staff ON b.userId = staff.UserID\n";
		String startTime = query[0];
		String endTime = query[1];
		String name = query[2];
		
		if(StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime) || StringUtils.isNotBlank(name) ){
			sqlLst += "where\n";
		}
		int index = 0;
		if(StringUtils.isNotBlank(startTime)){
			sqlLst += "b.addTime>='"+startTime+"'\n";
			index++;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(index>0){
				sqlLst += "and ";
			}
			sqlLst += "b.addTime<='"+endTime+" 24:00:00'\n";
			index++;
		}
		if(StringUtils.isNotBlank(name)){
			if(index>0){
				sqlLst += "and ";
			}
			sqlLst += "b.userId='"+name+"'\n";
		}
		sqlLst += "order by b.addTime";
		List<Object> objLst = baseDao.findPageList(sqlLst, page, limit);
		List<ChopUseLogVo> chopUseLogLst = new ArrayList<>();
		for(Object obj: objLst){
			Object[] objs = (Object[])obj;
			ChopUseLogVo chopUseLog = new ChopUseLogVo();
			chopUseLog.setUseDate(objs[0]+"");
			chopUseLog.setUserName(objs[1]+"");
			chopUseLog.setChopName(objs[2]+"");
			chopUseLog.setChopType(objs[3]+"");
			chopUseLog.setCertificateName(objs[4]+"");
			chopUseLog.setCertificateType(objs[5]+"");
			if(null!=objs[6]){
				chopUseLog.setUseIdReason(objs[6]+"");
			}
			String userId = objs[7]+"";
			List<GroupDetailVO> groups=staffService.findGroupDetailsByUserID(userId);
			if(CollectionUtils.isNotEmpty(groups)){
				GroupDetailVO g0=groups.get(0);
				chopUseLog.setDepartment(g0.getCompanyName()+"-"+g0.getDepartmentName());
			}
			chopUseLogLst.add(chopUseLog);
		}
		String sqlCount = "SELECT\n"+
				"count(b.userId)\n"+
				"FROM\n"+
					"(\n"+
						"SELECT\n"+
							"id.addTime,\n"+
							"id.User_Id userId,\n"+
							"a.chopName,\n"+
							"a.chopType,\n"+
							"a.certificateName,\n"+
							"a.certificateType,\n"+
							"CASE id.Item_User_Id\n"+
						"WHEN '15a9a34e-dcbd-4112-bf42-2a957bc03224' THEN\n"+
							"id.Reason\n"+
						"END useIdReason\n"+
						"FROM\n"+
							"(\n"+
						"SELECT\n"+
							"DATE(chopBorrow.AddTime) addTime,\n"+
							"chopBorrow.`Name` chopName,\n"+
							"chopBorrow.Type chopType,\n"+
							"certificate.`name` certificateName,\n"+
							"certificate.type certificateType,\n"+
							"chopBorrow.User_Id userId\n"+
						"FROM\n"+
							"(\n"+
								"SELECT\n"+
									"`Name`,\n"+
									"chop.Type,\n"+
									"chopBorrow.AddTime,\n"+
									"chopBorrow.User_Id\n"+
								"FROM\n"+
									"OA_ChopBorrow chopBorrow\n"+
								"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
							") chopBorrow\n"+
						"LEFT JOIN (\n"+
							"SELECT\n"+
								"`name`,\n"+
								"certificateBorrow.addTime,\n"+
								"certificateBorrow.userId,\n"+
								"certificate.type\n"+
							"FROM\n"+
								"OA_CertificateBorrow certificateBorrow\n"+
							"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
						") certificate ON chopBorrow.User_Id = certificate.userId\n"+
						"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
						"UNION\n"+
							"SELECT\n"+
								"DATE(certificate.AddTime) addTime,\n"+
								"chopBorrow.`Name` chopName,\n"+
								"chopBorrow.Type chopType,\n"+
								"certificate.`name` certificateName,\n"+
								"certificate.type certificateType,\n"+
								"certificate.userId\n"+
							"FROM\n"+
								"(\n"+
									"SELECT\n"+
										"`Name`,\n"+
										"chop.Type,\n"+
										"chopBorrow.AddTime,\n"+
										"chopBorrow.User_Id\n"+
									"FROM\n"+
										"OA_ChopBorrow chopBorrow\n"+
									"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
								") chopBorrow\n"+
							"RIGHT JOIN (\n"+
								"SELECT\n"+
									"`name`,\n"+
									"certificateBorrow.addTime,\n"+
									"certificateBorrow.userId,\n"+
									"certificate.type\n"+
								"FROM\n"+
									"OA_CertificateBorrow certificateBorrow\n"+
								"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
							") certificate ON chopBorrow.User_Id = certificate.userId\n"+
							"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
					") a\n"+
				"RIGHT JOIN OA_IdBorrow id ON a.userId = id.User_Id\n"+
				"AND DATE(a.addTime) = DATE(id.AddTime)\n"+
				"UNION\n"+
					"SELECT\n"+
						"a.addTime,\n"+
						"a.userId,\n"+
						"a.chopName,\n"+
						"a.chopType,\n"+
						"a.certificateName,\n"+
						"a.certificateType,\n"+
						"CASE id.Item_User_Id\n"+
					"WHEN '15a9a34e-dcbd-4112-bf42-2a957bc03224' THEN\n"+
						"id.Reason\n"+
					"END useIdReason\n"+
					"FROM\n"+
						"(\n"+
							"SELECT\n"+
								"DATE(chopBorrow.AddTime) addTime,\n"+
								"chopBorrow.`Name` chopName,\n"+
								"chopBorrow.Type chopType,\n"+
								"certificate.`name` certificateName,\n"+
								"certificate.type certificateType,\n"+
								"chopBorrow.User_Id userId\n"+
							"FROM\n"+
								"(\n"+
									"SELECT\n"+
										"`Name`,\n"+
										"chop.Type,\n"+
										"chopBorrow.AddTime,\n"+
										"chopBorrow.User_Id\n"+
									"FROM\n"+
										"OA_ChopBorrow chopBorrow\n"+
									"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
								") chopBorrow\n"+
							"LEFT JOIN (\n"+
								"SELECT\n"+
									"`name`,\n"+
									"certificateBorrow.addTime,\n"+
									"certificateBorrow.userId,\n"+
									"certificate.type\n"+
								"FROM\n"+
									"OA_CertificateBorrow certificateBorrow\n"+
								"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
							") certificate ON chopBorrow.User_Id = certificate.userId\n"+
							"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
							"UNION\n"+
								"SELECT\n"+
									"DATE(certificate.AddTime) addTime,\n"+
									"chopBorrow.`Name` chopName,\n"+
									"chopBorrow.Type chopType,\n"+
									"certificate.`name` certificateName,\n"+
									"certificate.type certificateType,\n"+
									"certificate.userId\n"+
								"FROM\n"+
									"(\n"+
										"SELECT\n"+
											"`Name`,\n"+
											"chop.Type,\n"+
											"chopBorrow.AddTime,\n"+
											"chopBorrow.User_Id\n"+
										"FROM\n"+
											"OA_ChopBorrow chopBorrow\n"+
										"INNER JOIN OA_Chop chop ON chopBorrow.Chop_Id = chop.Chop_Id\n"+
									") chopBorrow\n"+
								"RIGHT JOIN (\n"+
									"SELECT\n"+
										"`name`,\n"+
										"certificateBorrow.addTime,\n"+
										"certificateBorrow.userId,\n"+
										"certificate.type\n"+
									"FROM\n"+
										"OA_CertificateBorrow certificateBorrow\n"+
									"INNER JOIN OA_Certificate certificate ON certificateBorrow.certificateId = certificate.id\n"+
								") certificate ON chopBorrow.User_Id = certificate.userId\n"+
								"AND DATE(chopBorrow.addTime) = DATE(certificate.addTime)\n"+
						") a\n"+
					"LEFT JOIN OA_IdBorrow id ON a.userId = id.User_Id\n"+
					"AND DATE(a.addTime) = DATE(id.AddTime)\n"+
					") b\n"+
					"INNER JOIN OA_Staff staff ON b.userId = staff.UserID\n";
		if(StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime) || StringUtils.isNotBlank(name) ){
			sqlCount += "where\n";
		}
		index = 0;
		if(StringUtils.isNotBlank(startTime)){
			sqlCount += "b.addTime>='"+startTime+"'\n";
			index++;
		}
		if(StringUtils.isNotBlank(endTime)){
			if(index>0){
				sqlCount += "and ";
			}
			sqlCount += "b.addTime<='"+endTime+" 24:00:00'\n";
			index++;
		}
		if(StringUtils.isNotBlank(name)){
			if(index>0){
				sqlCount += "and ";
			}
			sqlCount += "b.userId='"+name+"'";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<ChopUseLogVo>(chopUseLogLst, count);
	}
	@Autowired
	private PermissionService permissionService;
	@Override
	public void startChopDestroy(ChopDestroyVo chopDestroyVo) {
		String enumName = BusinessTypeEnum.CHOP_DESTROY.getName();
		chopDestroyVo.setBusinessType(enumName);
		chopDestroyVo.setTitle(chopDestroyVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		List<Chop> chops = chopDao.getChopList(null, chopDestroyVo.getChopId(),
				1, 1);
		Chop chop = chops.get(0);
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(chopDestroyVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(chopDestroyVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || chopDestroyVo.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(chopDestroyVo.getUserID());
		}
		String manager = staffService.queryManager(chopDestroyVo.getUserID());
		
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
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("chopStorager", chop.getStore_person());
		vars.put("chopManager", chop.getSubject_person());
		vars.put("applyer", chopDestroyVo.getUserID());
		vars.put("arg", chopDestroyVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CHOP_DESTROY);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), chopDestroyVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveChopDestroy(chopDestroyVo, processInstance.getId());
	}

	private void saveChopDestroy(ChopDestroyVo chopDestroyVo, String id) {
		ChopDestroyEntity chopDestroyEntity = null;
		try {
			chopDestroyEntity = (ChopDestroyEntity) CopyUtil.tryToEntity(chopDestroyVo, ChopDestroyEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		chopDestroyEntity.setProcessInstanceID(id);
		chopDestroyEntity.setAddTime(new Date());
		chopDestroyEntity.setIsDeleted(0);
		baseDao.hqlSave(chopDestroyEntity);
	}

	@Override
	public String getChopDestroyUserIdByInstanceId(String id) {
		String sql = "select userID from OA_ChopDestroy where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public void updateChopDestroyProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ChopDestroyEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}

	@Override
	public ListResult<ChopDestroyVo> findChopDestroyListByUserID(String id, Integer page, Integer limit) {
		List<ChopDestroyEntity> chopDestroys = getChopDestroyByUserId(id,
				page, limit);

		List<ChopDestroyVo> chopDestroyVos = new ArrayList<ChopDestroyVo>();
		for (ChopDestroyEntity chopDestroy : chopDestroys) {
			ChopDestroyVo chopDestroyVo = null;
			try {
				chopDestroyVo = (ChopDestroyVo) CopyUtil.tryToVo(chopDestroy, ChopDestroyVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(chopDestroy.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ChopDestroyVo arg = (ChopDestroyVo) variable.getValue();
					chopDestroyVo.setRequestDate(arg.getRequestDate());
					chopDestroyVo.setTitle(arg.getTitle());
					chopDestroyVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(chopDestroy.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				chopDestroyVo.setStatus("处理中");
				chopDestroyVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = chopDestroyVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						chopDestroyVo.setStatus(t.getName());
				}
			}
			chopDestroyVos.add(chopDestroyVo);
		}
		int count = getChopDestroyCountByUserId(id);
		return new ListResult<ChopDestroyVo>(chopDestroyVos, count);
	}

	private int getChopDestroyCountByUserId(String id) {
		String hql = "select count(id) from ChopDestroyEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}

	@SuppressWarnings("unchecked")
	private List<ChopDestroyEntity> getChopDestroyByUserId(String id, Integer page, Integer limit) {
		String hql="from ChopDestroyEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<ChopDestroyEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}

	@Override
	public ChopDestroyVo getChopDestroyVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ChopDestroyVo chopDestroyVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				chopDestroyVo = (ChopDestroyVo) variable.getValue();
			}
		}
		return chopDestroyVo;
	}

	@Override
	public void startCarveChop(CarveChopVo carveChopVo) {
		String enumName = BusinessTypeEnum.CARVE_CHOP.getName();
		carveChopVo.setBusinessType(enumName);
		carveChopVo.setTitle(carveChopVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(carveChopVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(carveChopVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || carveChopVo.getUserID().equals(supervisor)) {
			supervisor=staffService.querySupervisor(carveChopVo.getUserID());
		}
		String manager = staffService.queryManager(carveChopVo.getUserID());
		
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
		List<Group> groups = identityService.createGroupQuery().groupMember(carveChopVo.getUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> hrLeaders = permissionService
				.findUsersByPermissionCodeCompany(Constants.HR_LEADER, companyID);
		if(hrLeaders.size()>0){
			vars.put("hrLeader", hrLeaders.get(0));
		}else{
			throw new RuntimeException("未找到人事行政领导，请联系系统管理员配置");
		}
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("arg", carveChopVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CARVE_CHOP);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), carveChopVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveCarveChop(carveChopVo, processInstance.getId());
		
	}

	private void saveCarveChop(CarveChopVo carveChopVo, String id) {
		CarveChopEntity carveChopEntity = null;
		try {
			carveChopEntity = (CarveChopEntity) CopyUtil.tryToEntity(carveChopVo, CarveChopEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		carveChopEntity.setProcessInstanceID(id);
		carveChopEntity.setAddTime(new Date());
		carveChopEntity.setIsDeleted(0);
		baseDao.hqlSave(carveChopEntity);
	}

	@Override
	public String getCarveChopUserIdByInstanceId(String id) {
		String sql = "select userID from OA_CarveChop where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public void updateCarveChopProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update CarveChopEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}

	@Override
	public ListResult<CarveChopVo> findCarveChopListByUserID(String id, Integer page, Integer limit) {
		List<CarveChopEntity> carveChops = getCarveChopByUserId(id,
				page, limit);

		List<CarveChopVo> carveChopVos = new ArrayList<CarveChopVo>();
		for (CarveChopEntity carveChop : carveChops) {
			CarveChopVo carveChopVo = null;
			try {
				carveChopVo = (CarveChopVo) CopyUtil.tryToVo(carveChop, CarveChopVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(carveChop.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					CarveChopVo arg = (CarveChopVo) variable.getValue();
					carveChopVo.setRequestDate(arg.getRequestDate());
					carveChopVo.setTitle(arg.getTitle());
					carveChopVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(carveChop.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				carveChopVo.setStatus("处理中");
				carveChopVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = carveChopVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						carveChopVo.setStatus(t.getName());
				}
			}
			carveChopVos.add(carveChopVo);
		}
		int count = getCarveChopCountByUserId(id);
		return new ListResult<CarveChopVo>(carveChopVos, count);
	}

	private int getCarveChopCountByUserId(String id) {
		String hql = "select count(id) from CarveChopEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}

	@SuppressWarnings("unchecked")
	private List<CarveChopEntity> getCarveChopByUserId(String id, Integer page, Integer limit) {
		String hql="from CarveChopEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<CarveChopEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}

	@Override
	public CarveChopVo getCarveChopVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		CarveChopVo carveChopVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				carveChopVo = (CarveChopVo) variable.getValue();
			}
		}
		return carveChopVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ChopBorrrowVo> findContractChopList(String beginDate, String endDate, Integer limit,
			Integer page) {
		String hql = "from ChopBorrow where IsDeleted=0 and fileType='合同'\n";
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and contractApplyDate>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and contractApplyDate<='"+endDate+"'\n";
		}
		hql += "order by contractApplyDate desc";
		List<ChopBorrow> contractChopList = (List<ChopBorrow>) baseDao.hqlPagedFind(hql, page, limit);
		List<ChopBorrrowVo> contractChops = new ArrayList<>();
 		for(ChopBorrow chopBorrow: contractChopList){
			ChopBorrrowVo contractChop = (ChopBorrrowVo) CopyUtil.tryToVo(chopBorrow, ChopBorrrowVo.class);
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(contractChop.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				contractChop.setStatus("处理中");
				contractChop.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = contractChop.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						contractChop.setStatus(t.getName());
				}
			}
			Integer chopId = chopBorrow.getChop_Id();
			contractChop.setChop_Name(
					chopDao.getChopList(null, chopId, 1, 1).get(0).getName());
			contractChop.setUser_Name(staffService.getStaffByUserId(chopBorrow.getUser_Id()).getStaffName());
			contractChops.add(contractChop);
		}
		String hqlCount = "select count(ChopBorrow_Id) from ChopBorrow where IsDeleted=0 and fileType='合同'\n";
		if(StringUtils.isNotBlank(beginDate)){
			hqlCount += "and contractApplyDate>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hqlCount += "and contractApplyDate<='"+endDate+"'\n";
		}
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount)+"");
		return new ListResult<>(contractChops, count);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public InputStream exportContractChopDatas(String beginDate, String endDate) throws Exception {
		String hql = "from ChopBorrow where IsDeleted=0 and fileType='合同'\n";
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and contractApplyDate>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and contractApplyDate<='"+endDate+"'\n";
		}
		hql += "order by contractApplyDate";
		List<ChopBorrow> contractChops = (List<ChopBorrow>) baseDao.hqlfind(hql);
		//创建一个空的excel
		Workbook workbook = new HSSFWorkbook();
		Sheet worksheet = workbook.createSheet("合同类盖章申请");
		// 设置列宽    
		worksheet.setColumnWidth(0, 2000);    
		worksheet.setColumnWidth(1, 7000);    
		worksheet.setColumnWidth(2, 5000);    
		worksheet.setColumnWidth(3, 5000);    
		worksheet.setColumnWidth(4, 5000);    
		worksheet.setColumnWidth(5, 3500);    
		worksheet.setColumnWidth(6, 3500);
		worksheet.setColumnWidth(7, 3500);
		
		Font headfont = workbook.createFont();    
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗    
		//头部样式   
		CellStyle headstyle = workbook.createCellStyle();    
		headstyle.setFont(headfont);    
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		headstyle.setLocked(true); 
		//创建列名行
		Row rowCol = worksheet.createRow(0);
		String[] colNames = {"序号","公章名称","合同名称","甲方","乙方","是否涉及法律","合同申请时间","申请人"};
		int colIndex = 0;
		for(String colName: colNames){
			Cell cell = rowCol.createCell(colIndex);
			cell.setCellStyle(headstyle);
			cell.setCellValue(colName);
			colIndex++;
		}
		//数据从第二行开始填入
		int index = 1;
		int num = 1;
		for(ChopBorrow contractChop: contractChops){
			Row row = worksheet.createRow(index);
			row.createCell(0).setCellValue(num);
			Integer chopId = contractChop.getChop_Id();
			String chopName = chopDao.getChopList(null, chopId, 1, 1).get(0).getName();
			row.createCell(1).setCellValue(chopName);
			row.createCell(2).setCellValue(contractChop.getFileName());
			row.createCell(3).setCellValue(contractChop.getPartyA());
			row.createCell(4).setCellValue(contractChop.getPartyB());
			row.createCell(5).setCellValue(contractChop.getRelateLaw()==0?"否":"是");
			row.createCell(6).setCellValue(contractChop.getContractApplyDate());
			row.createCell(7).setCellValue(staffService.getStaffByUserId(contractChop.getUser_Id()).getStaffName());
			index++;
			num++;
		}
		//将生成的excel写入到输出流里面,然后再通过这个输出流来得到我们所需要的输入流.
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			workbook.write(os);
			ByteArrayInputStream in = new ByteArrayInputStream(os.toByteArray());
			return in;
		} catch (Exception e) {
			throw new Exception(e);
		} finally{
			workbook.close();
		}
	}
}
