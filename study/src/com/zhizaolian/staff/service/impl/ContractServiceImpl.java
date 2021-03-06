package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.ContractDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.ChangeContractEntity;
import com.zhizaolian.staff.entity.ContractBorrowEntity;
import com.zhizaolian.staff.entity.ContractEntity;
import com.zhizaolian.staff.entity.ContractManageEntity;
import com.zhizaolian.staff.entity.ContractSignEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.ContractStatusEnum;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.ContractVOTransformer;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.ChangeContractVo;
import com.zhizaolian.staff.vo.ContractBorrowVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.ContractVO;
import com.zhizaolian.staff.vo.DepartmentVO;


public class ContractServiceImpl implements ContractService{
	@Autowired
	private ContractDao contractDao;

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private PositionService positionService;

	@Autowired
	private StaffService staffService;
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
	private PermissionService permissionService;
	@Override
	public Integer saveContract(ContractVO contractVO) {
		Date now = new Date();
		ContractEntity contractEntity = ContractEntity.builder()
				.contractID(contractVO.getContractID())
				.partyA(contractVO.getPartyA())
				.partyB(contractVO.getPartyB())
				.beginDate(DateUtil.getSimpleDate(contractVO.getBeginDate()))
				.endDate(DateUtil.getSimpleDate(contractVO.getEndDate()))
				.contractBackups(contractVO.getContractBackups())
				.signature(contractVO.getSignature())
				.status(ContractStatusEnum.VALID.getValue())
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
				.addTime(now)
				.updateTime(now)
				.build();
		return contractDao.save(contractEntity);
	}

	@Override
	public void updateContract(ContractVO contractVO) {
		ContractEntity contractEntity = contractDao.getContractEntityByContractID(contractVO.getContractID());
		if (contractEntity == null) {
			throw new RuntimeException("合同不存在！");
		}

		if (contractVO.getPartyA() != null) {
			contractEntity.setPartyA(contractVO.getPartyA());
		}
		if (!StringUtils.isEmpty(contractVO.getPartyB())) {
			contractEntity.setPartyB(contractVO.getPartyB());
		}
		if (!StringUtils.isEmpty(contractVO.getBeginDate())) {
			contractEntity.setBeginDate(DateUtil.getSimpleDate(contractVO.getBeginDate()));
		}
		if (!StringUtils.isEmpty(contractVO.getEndDate())) {
			contractEntity.setEndDate(DateUtil.getSimpleDate(contractVO.getEndDate()));
		}
		if (!StringUtils.isEmpty(contractVO.getContractBackups())) {
			contractEntity.setContractBackups(contractVO.getContractBackups());
		}
		if (!StringUtils.isEmpty(contractVO.getSignature())) {
			contractEntity.setSignature(contractVO.getSignature());
		}
		Date now = new Date();
		contractEntity.setUpdateTime(now);
		contractDao.save(contractEntity);
	}

	@Override
	public void deleteContract(int contractID) {
		ContractEntity contractEntity = contractDao.getContractEntityByContractID(contractID);
		if (contractEntity == null) {
			throw new RuntimeException("合同记录不存在！");
		}

		contractEntity.setIsDeleted(IsDeletedEnum.DELETED.getValue());
		contractEntity.setUpdateTime(new Date());
		contractDao.save(contractEntity);
	}

	@Override
	public void expiredContractByContractID(int contractID) {
		ContractEntity contractEntity = contractDao.getContractEntityByContractID(contractID);
		if (contractEntity == null) {
			throw new RuntimeException("合同记录不存在！");
		}

		contractEntity.setStatus(ContractStatusEnum.EXPIRED.getValue());
		contractEntity.setUpdateTime(new Date());
		contractDao.save(contractEntity);
	}

	private void saveFile(File mfile, String dir, String fName) {
		InputStream in = null;
		OutputStream out = null;
		try {
			byte[] buffer = new byte[10*1024*1024];
			int length = 0;
			in = new FileInputStream(mfile);
			File f = new File(dir);
			if(!f.exists()){
				f.mkdirs();
			}
			out = new FileOutputStream(new File(f, fName));
			while((length=in.read(buffer, 0, buffer.length))!=-1){
				out.write(buffer, 0, length);
			}
		} catch (Exception e) {
			throw new RuntimeException("文件保存失败："+e.getMessage());
		} finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}

	}

	@Override
	public void saveRenewContract(ContractVO contractVO, File contract, File signat, String contractFileName, String signatFileName) {
		ContractEntity oldContract = contractDao.getContractEntityByContractID(contractVO.getContractID());

		//修改现有合同状态为已过期
		expiredContractByContractID(contractVO.getContractID());

		contractVO.setContractID(null);
		int contractID = saveContract(contractVO);

		if (contract != null) {
			saveFile(contract, Constants.CONTRACT_FILE_DIRECTORY, contractID+"_"+contractFileName);
			contractVO.setContractBackups(contractFileName);
		}

		if (signat != null) {
			saveFile(signat, Constants.CONTRACT_FILE_DIRECTORY, contractID+"_"+signatFileName);
			contractVO.setSignature(signatFileName);
		} else if (!StringUtils.isBlank(oldContract.getSignature())) {
			//保存员工签名
			contractVO.setSignature(oldContract.getSignature());
			File oldSignat = new File(Constants.CONTRACT_FILE_DIRECTORY+oldContract.getContractID()+"_"+oldContract.getSignature());
			oldSignat.renameTo(new File(Constants.CONTRACT_FILE_DIRECTORY+contractID+"_"+oldContract.getSignature()));
		}

		contractVO.setContractID(contractID);
		updateContract(contractVO);
	}

	@Override
	public List<ContractVO> findContractsByPartyBStatus(String userID, ContractStatusEnum status) {
		if (status == null) {
			throw new RuntimeException("合同状态不合法！");
		}

		List<ContractEntity> contractEntities = contractDao.findContractsByPartyBStatus(userID, status.getValue());
		return Lists2.transform(contractEntities, ContractVOTransformer.INSTANCE);
	}

	@Override
	public ListResult<ContractVO> findContractByContractVO(ContractVO contractVO, int page, int limit){
		List<Object> list = baseDao.findPageList(getSqlByContractVO(contractVO), page, limit);
		List<ContractVO> contractVOs = new ArrayList<>();
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			ContractVO contractVO2= new ContractVO();
			contractVO2.setContractID((Integer) objs[0]);
			contractVO2.setPartyA(Integer.parseInt(objs[1].toString()));
			contractVO2.setPartyB((String) objs[2]);
			contractVO2.setBeginDate(DateUtil.formateDate((Date) objs[3]));
			contractVO2.setEndDate(DateUtil.formateDate((Date) objs[4]));
			contractVO2.setAddTime(DateUtil.formateDate((Date) objs[5]));
			contractVOs.add(contractVO2);
		}
		Object countObj=baseDao.getUniqueResult(getCountSqlByContractVO(contractVO));
		int count=countObj == null? 0:((BigInteger)countObj).intValue();
		return new ListResult<ContractVO>(contractVOs,count);

	}

	private String getSqlByContractVO(ContractVO contractVO){
		StringBuffer sql=new StringBuffer("select distinct * from(select contract.contractID,contract.partyA,staff.StaffName,contract.beginDate,contract.endDate, contract.addTime "
				+ "from OA_Contract contract,OA_Staff staff,ACT_ID_MEMBERSHIP membership,OA_GroupDetail groupDetail "
				+ "where contract.partyB = staff.UserID and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = groupDetail.GroupID  "
				+ "and contract.IsDeleted = 0 and contract.Status = 0 and staff.IsDeleted = 0 and staff.Status != 4");
		sql.append(getWhereBySql(contractVO));
		sql.append(" order by contract.beginDate");
		sql.append(")a");

		return sql.toString();

	}

	private String getCountSqlByContractVO(ContractVO contractVO){
		StringBuffer sql=new StringBuffer("select count(*) from (select distinct * from(select contract.contractID,contract.partyA,staff.StaffName,contract.beginDate,contract.endDate "
				+ "from OA_Contract contract,OA_Staff staff,ACT_ID_MEMBERSHIP membership,OA_GroupDetail groupDetail "
				+ "where contract.partyB = staff.UserID and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = groupDetail.GroupID  "
				+ "and contract.IsDeleted = 0 and contract.Status = 0 and staff.IsDeleted = 0 and staff.Status != 4 ");
		sql.append(getWhereBySql(contractVO));
		sql.append(")a)s");
		return sql.toString();
	}

	private String getWhereBySql(ContractVO contractVO){
		StringBuffer whereSql=new StringBuffer();
		if (!StringUtils.isBlank(contractVO.getPartyBName())) {
			whereSql.append(" and staff.StaffName like '%"+contractVO.getPartyBName()+"%' ");
		}
		if (contractVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.companyID = "+contractVO.getCompanyID());
			if (contractVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(contractVO.getCompanyID(), contractVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(contractVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.departmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}

		if (!CollectionUtils.isEmpty(contractVO.getAdmCompanyIDs())) {
			String companyIDs = Arrays.toString(contractVO.getAdmCompanyIDs().toArray());
			whereSql.append(" and groupDetail.companyID in ("+companyIDs.substring(1, companyIDs.length()-1)+")");
			Date now = new Date();
			whereSql.append(" and datediff (contract.endDate,'"+DateUtil.formateDate(now)+"') <=30 ");
		}

		return whereSql.toString();

	}

	@Override
	public ContractVO getContractVOBycontractID(Integer contractID) {
		ContractEntity contractEntity = contractDao.getContractEntityByContractID(contractID);
		ContractVO contractVO = new ContractVO();
		contractVO.setContractID(contractEntity.getContractID());
		contractVO.setPartyA(contractEntity.getPartyA());
		contractVO.setPartyB(contractEntity.getPartyB());
		contractVO.setPartyBName(staffService.getStaffByUserID(contractEntity.getPartyB()).getLastName());
		contractVO.setBeginDate(DateUtil.formateDate(contractEntity.getBeginDate()));
		contractVO.setEndDate(DateUtil.formateDate(contractEntity.getEndDate()));
		contractVO.setContractBackups(contractEntity.getContractBackups());
		contractVO.setSignature(contractEntity.getSignature());

		return contractVO;
	}

	@Override
	public ListResult<ContractManageEntity> getContractLst(String name, int limit, int page) {
		String sql = "from ContractManageEntity where isDeleted=0";
		if(StringUtils.isNotBlank(name)){
			sql += " and name like '%" + name + "%'";
		}
		@SuppressWarnings("unchecked")
		List<ContractManageEntity> contractLst = (List<ContractManageEntity>)baseDao.hqlPagedFind(sql, page, limit);
		List<ContractManageEntity> _contractLst = Lists2.transform(contractLst,
				new Function<ContractManageEntity, ContractManageEntity>() {
			@Override
			public ContractManageEntity apply(ContractManageEntity arg0) {
				arg0.setStore_personName(
						staffDao.getEmployeeNameByUsrId(
								arg0.getStore_person()));
				arg0.setSubject_personName(
						staffDao.getEmployeeNameByUsrId(
								arg0.getSubject_person()));
				return arg0;
			}
		});

		String sqlCount = "select count(id) from ContractManageEntity where isDeleted=0";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(sqlCount)+"");
		return new ListResult<ContractManageEntity>(_contractLst, count);
	}

	@Override
	public ContractManageEntity getContract(String contractId) {
		String hql = "from ContractManageEntity where id="+contractId;
		ContractManageEntity contract = (ContractManageEntity)baseDao.hqlfindUniqueResult(hql);
		String attachmentNameStr = contract.getAttachmentName();
		List<String> attachmentNames = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentNameStr)){
			String[] attachNames = attachmentNameStr.split("#@#&");
			for(String attachmentName: attachNames){
				if(StringUtils.isNotBlank(attachmentName)){
					attachmentNames.add(attachmentName);
				}
			}
		}
		contract.setAttachmentNames(attachmentNames);
		contract.setStore_personName(staffDao.getEmployeeNameByUsrId(contract.getStore_person()));
		contract.setSubject_personName(staffDao.getEmployeeNameByUsrId(contract.getSubject_person()));
		return contract;
	}

	@Override
	public void updateContract(ContractManageEntity contract, File[] attachment, String[] attachmentFileName) throws Exception{
		if(null != attachment && attachment.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<attachment.length; i++){
				String fileName = attachmentFileName[i];
				fileName = UUID.randomUUID()+"_"+fileName;
				File destFile = new File(Constants.CERTIFICATE_FILE_DIRECTORY, fileName);
				FileUtils.copyFile(attachment[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			contract.setAttachmentName(attachmentNames);
			contract.setAttachmentPath(attachmentPaths);
		}
		baseDao.hqlUpdate(contract);
	}

	@Override
	public void saveContract(ContractManageEntity contract, File[] attachment, String[] attachmentFileName) throws Exception {
		if(null != attachment && attachment.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<attachment.length; i++){
				String fileName = attachmentFileName[i];
				fileName = UUID.randomUUID()+"_"+fileName;
				File destFile = new File(Constants.CERTIFICATE_FILE_DIRECTORY, fileName);
				FileUtils.copyFile(attachment[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			contract.setAttachmentName(attachmentNames);
			contract.setAttachmentPath(attachmentPaths);
		}
		baseDao.hqlSave(contract);

	}

	@Override
	public List<String> getAttachmentNames(String contractId) {
		String sql = "select attachmentName from OA_ContractManage where id="+contractId;
		String attachmentNameStr = baseDao.getUniqueResult(sql)+"";
		List<String> attachmentNames = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentNameStr)){
			String[] attachNames= attachmentNameStr.split("#@#&");
			for(String attachmentName: attachNames){
				if(StringUtils.isNotBlank(attachmentName)){
					attachmentNames.add(attachmentName);
				}
			}
		}
		return attachmentNames;
	}

	@Override
	public void deleteContract(String contractId) {
		String sql = "update OA_ContractManage set isDeleted=1 where id="+contractId;
		baseDao.excuteSql(sql);
	}

	@Override
	public boolean checkIsExist(String contractID, String id) {
		String sql = "select count(*) from OA_ContractManage where contractID='"+contractID+"' and isDeleted=0";
		if(StringUtils.isNotBlank(id)){
			sql += " and id!="+id;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public void startContractBorrow(ContractBorrowVo contractBorrowVo) {
		String enumName = BusinessTypeEnum.CONTRACT_BORROW.getName();
		contractBorrowVo.setBusinessType(enumName);
		contractBorrowVo.setTitle(contractBorrowVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		ContractManageEntity contract = getContract(contractBorrowVo.getContractId()+"");
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(contractBorrowVo.getUserId());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(contractBorrowVo.getUserId());
		}
		if (StringUtils.isBlank(supervisor) || contractBorrowVo.getUserId().equals(supervisor)) {
			supervisor=staffService.querySupervisor(contractBorrowVo.getUserId());
		}
		String manager = staffService.queryManager(contractBorrowVo.getUserId());

		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("id_storage_user", contract.getStore_person());
		vars.put("id_subject_user", contract.getSubject_person());
		vars.put("contract_isBorrow", contractBorrowVo.getIsBorrow());
		vars.put("arg", contractBorrowVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CONTRACT_BORROW);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), contractBorrowVo.getUserId());
		taskService.complete(task.getId(), vars);
		saveContractBorrow(contractBorrowVo, processInstance.getId());
	}

	private void saveContractBorrow(ContractBorrowVo contractBorrowVo, String instanceId) {
		ContractBorrowEntity contractBorrowEntity = null;
		try {
			contractBorrowEntity = (ContractBorrowEntity) CopyUtil.tryToEntity(contractBorrowVo, ContractBorrowEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		contractBorrowEntity.setProcessInstanceID(instanceId);
		contractBorrowEntity.setAddTime(new Date());
		contractBorrowEntity.setIsDeleted(0);
		baseDao.hqlSave(contractBorrowEntity);
	}

	@Override
	public ListResult<ContractBorrowVo> findContractBorrowLstByUserID(String userId, Integer page, Integer limit) {
		List<ContractBorrowEntity> contractBorrows = getContractBorrowByUserId(userId,
				page, limit);

		List<ContractBorrowVo> contractBorrowVos = new ArrayList<ContractBorrowVo>();
		for (ContractBorrowEntity contractBorrow : contractBorrows) {
			ContractBorrowVo contractBorrowVo = null;
			try {
				contractBorrowVo = (ContractBorrowVo) CopyUtil.tryToVo(contractBorrow, ContractBorrowVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(contractBorrow.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ContractBorrowVo arg = (ContractBorrowVo) variable.getValue();
					contractBorrowVo.setRequestDate(arg.getRequestDate());
					contractBorrowVo.setTitle(arg.getTitle());
					contractBorrowVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(contractBorrow.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				contractBorrowVo.setStatus("处理中");
				contractBorrowVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = contractBorrowVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						contractBorrowVo.setStatus(t.getName());
				}
			}
			Integer certificateId = contractBorrowVo.getContractId();
			contractBorrowVo.setContractName(
					getContract(certificateId+"").getName());
			contractBorrowVos.add(contractBorrowVo);
		}
		int count = getContractBorrowCountByUserId(userId);
		return new ListResult<ContractBorrowVo>(contractBorrowVos, count);
	}

	private int getContractBorrowCountByUserId(String userId) {
		String hql = "select count(id) from ContractBorrowEntity where IsDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}

	@SuppressWarnings("unchecked")
	private List<ContractBorrowEntity> getContractBorrowByUserId(String userId, Integer page, Integer limit) {
		String hql="from ContractBorrowEntity where IsDeleted=0 and userId='"+userId+"'";
		return (List<ContractBorrowEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}

	@Override
	public void updateProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ContractBorrowEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}

	@Override
	public void updateRealBeginTime(String intanceId) {
		String sql = "update OA_ContractBorrow set realStartTime='"+DateUtil.formateFullDate(new Date())+"' where processInstanceID='"+intanceId+"'";
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateRealEndTime(String intanceId) {
		String sql = "update OA_ContractBorrow set realEndTime='"+DateUtil.formateFullDate(new Date())+"' where processInstanceID='"+intanceId+"'";
		baseDao.excuteSql(sql);
	}

	@Override
	public ListResult<ContractBorrowEntity> getContractBorrowLst(String[] query, int limit, int page) {
		String hql = "from ContractBorrowEntity where isDeleted=0 and contractId="+query[0];
		if(StringUtils.isNotBlank(query[1])){
			hql += " and addTime>='"+query[1]+"'";
		}
		if(StringUtils.isNotBlank(query[2])){
			hql += " and addTime<='"+query[2]+" 24:00:00'";
		}
		Object objLst = baseDao.hqlPagedFind(hql, page, limit);
		@SuppressWarnings("unchecked")
		List<ContractBorrowEntity> contractBorrowLst = (List<ContractBorrowEntity>)objLst;
		List<ContractBorrowEntity> _contractBorrowLst = Lists2.transform(contractBorrowLst,
				new Function<ContractBorrowEntity, ContractBorrowEntity>() {

			@Override
			public ContractBorrowEntity apply(ContractBorrowEntity arg0) {
				arg0.setUserName(staffDao.getEmployeeNameByUsrId(
						arg0.getUserId()));
				return arg0;
			}

		});
		String countSql = "select count(*) from OA_ContractBorrow where isDeleted=0 and contractId="+query[0];
		if(StringUtils.isNotBlank(query[1])){
			countSql += " and addTime>="+query[1];
		}
		if(StringUtils.isNotBlank(query[2])){
			countSql += " and addTime<="+query[2];
		}
		Object obj = baseDao.getUniqueResult(countSql);
		int count = Integer.parseInt(obj+"");
		return new ListResult<ContractBorrowEntity>(_contractBorrowLst, count);
	}

	@Override
	public void startContractSign(ContractSignVo contractSignVo, File[] attachment,
			String[] attachmentFileName, File[] attachment2, String[] attachment2FileName) throws Exception {

		String enumName = BusinessTypeEnum.CONTRACT.getName();
		contractSignVo.setBusinessType(enumName);
		contractSignVo.setTitle(contractSignVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(contractSignVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor = staffService.queryHeadMan(contractSignVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || contractSignVo.getUserID().equals(supervisor)) {
			supervisor = staffService.querySupervisor(contractSignVo.getUserID());
		}
		String manager = staffService.queryManager(contractSignVo.getUserID());

		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> finaceManager = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		if(finaceManager.size()>0){
			vars.put("financial_manager", finaceManager.get(0));
		}else{
			throw new RuntimeException("未找到财务负责人");
		}
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		vars.put("arg", contractSignVo);
		//启动流程
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CONTRACT_SIGN);
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
		index = 0;
		if(null != attachment2){
			for(File file: attachment2){
				String fileName = attachment2FileName[index];
				String suffix = fileName.substring(fileName.indexOf(".")+1, fileName.length());
				InputStream is = new FileInputStream(file);
				if(suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpeg")){
					taskService.createAttachment("picture", "", processInstance.getId(), fileName, Constants.LAW_WORK_AUDIT, is);
				}else{
					taskService.createAttachment(suffix,"",processInstance.getId(), fileName, Constants.LAW_WORK_AUDIT, is);
				}
				index++;
			}
		}
		taskService.setAssignee(task.getId(), contractSignVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveContractSign(contractSignVo, processInstance.getId());
	}

	private void saveContractSign(ContractSignVo contractSignVo, String processInstanceId) {
		ContractSignEntity contractSignEntity = null;
		try {
			contractSignEntity = (ContractSignEntity) CopyUtil.tryToEntity(contractSignVo, ContractSignEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		contractSignEntity.setProcessInstanceID(processInstanceId);
		contractSignEntity.setAddTime(new Date());
		contractSignEntity.setIsDeleted(0);
		baseDao.hqlSave(contractSignEntity);
	}

	@Override
	public String getUserIdByInstanceId(String id) {
		String sql = "select userId from OA_ContractSign where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public void updateContractStatus(TaskResultEnum result, String processInstanceID) {
		String sql="update  OA_ContractSign s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"'";
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateContractSign(ContractSignVo contractSignVo) {
		String sql = "update OA_ContractSign set exceedGroup="+contractSignVo.getExceedGroup()+", exceedGroupRate='"+contractSignVo.getExceedGroupRate()
					+"', exceedSeason="+contractSignVo.getExceedSeason()+", exceedSeasonRate='"+contractSignVo.getExceedSeasonRate()
					+"' where processInstanceID='"+contractSignVo.getProcessInstanceID()+"'";
		baseDao.excuteSql(sql);
	}

	@Override
	public ListResult<ContractSignVo> findContractSignLstByUserId(String userId, Integer page, Integer limit) {
		List<ContractSignEntity> contractSigns = getContractSignByUserId(userId,
				page, limit);

		List<ContractSignVo> contractSignVos = new ArrayList<ContractSignVo>();
		for (ContractSignEntity contractSign : contractSigns) {
			ContractSignVo contractSignVo = null;
			try {
				contractSignVo = (ContractSignVo) CopyUtil.tryToVo(contractSign, ContractSignVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(contractSign.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ContractSignVo arg = (ContractSignVo) variable.getValue();
					contractSignVo.setRequestDate(arg.getRequestDate());
					contractSignVo.setTitle(arg.getTitle());
					contractSignVo.setUserName(arg.getUserName());
					contractSignVo.setContractName(arg.getContractName());
					contractSignVo.setOtherCompanyName(arg.getOtherCompanyName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(contractSign.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				contractSignVo.setStatus("处理中");
				contractSignVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = contractSignVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						contractSignVo.setStatus(t.getName());
				}
			}
			contractSignVos.add(contractSignVo);
		}
		int count = getContractSignCountByUserId(userId);
		return new ListResult<ContractSignVo>(contractSignVos, count);
	}

	private int getContractSignCountByUserId(String userId) {
		String hql = "select count(id) from ContractSignEntity where IsDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}

	@SuppressWarnings("unchecked")
	private List<ContractSignEntity> getContractSignByUserId(String userId, Integer page, Integer limit) {
		String hql="from ContractSignEntity where IsDeleted=0 and userId='"+userId+"'";
		return (List<ContractSignEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}

	@Override
	public ContractSignVo getContractSignVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ContractSignVo contractSignVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				contractSignVo = (ContractSignVo) variable.getValue();
			}
		}
		return contractSignVo;
	}

	@Override
	public void startChangeContract(ChangeContractVo changeContractVo, File[] attachment, String[] attachmentFileName) throws Exception {
		String enumName = BusinessTypeEnum.CONTRACT_CHANGE.getName();
		changeContractVo.setBusinessType(enumName);
		changeContractVo.setTitle(changeContractVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(changeContractVo.getUserID());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor = staffService.queryHeadMan(changeContractVo.getUserID());
		}
		if (StringUtils.isBlank(supervisor) || changeContractVo.getUserID().equals(supervisor)) {
			supervisor = staffService.querySupervisor(changeContractVo.getUserID());
		}
		String manager = staffService.queryManager(changeContractVo.getUserID());

		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		if(companyBoss.size()>0){
			vars.put("finalManager", companyBoss.get(0));
		}else{
			throw new RuntimeException("未找到总经理（法定代表人）");
		}
		vars.put("arg", changeContractVo);
		//启动流程
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CONTRACT_CHANGE);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), changeContractVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveContractChange(changeContractVo, processInstance.getId());
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
	}

	private void saveContractChange(ChangeContractVo changeContractVo, String processInstanceId) {
		ChangeContractEntity changeContractEntity = null;
		try {
			changeContractEntity = (ChangeContractEntity) CopyUtil.tryToEntity(changeContractVo, ChangeContractEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		changeContractEntity.setProcessInstanceID(processInstanceId);
		changeContractEntity.setAddTime(new Date());
		changeContractEntity.setIsDeleted(0);
		baseDao.hqlSave(changeContractEntity);
	}

	@Override
	public ListResult<ChangeContractVo> findChangeContractListByUserID(String userId, Integer page, Integer limit) {
		List<ChangeContractEntity> contractChanges = getContractChangeByUserId(userId,
				page, limit);

		List<ChangeContractVo> contractChangeVos = new ArrayList<ChangeContractVo>();
		for (ChangeContractEntity contractChange : contractChanges) {
			ChangeContractVo changeContractVo = null;
			try {
				changeContractVo = (ChangeContractVo) CopyUtil.tryToVo(contractChange, ChangeContractVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(contractChange.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					ChangeContractVo arg = (ChangeContractVo) variable.getValue();
					changeContractVo.setRequestDate(arg.getRequestDate());
					changeContractVo.setTitle(arg.getTitle());
					changeContractVo.setUserName(arg.getUserName());
					changeContractVo.setContractName(arg.getContractName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(contractChange.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				changeContractVo.setStatus("处理中");
				changeContractVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = changeContractVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						changeContractVo.setStatus(t.getName());
				}
			}
			contractChangeVos.add(changeContractVo);
		}
		int count = getContractChangeCountByUserId(userId);
		return new ListResult<ChangeContractVo>(contractChangeVos, count);
	}

	@SuppressWarnings("unchecked")
	private List<ChangeContractEntity> getContractChangeByUserId(String userId, Integer page, Integer limit) {
		String hql="from ChangeContractEntity where IsDeleted=0 and userId='"+userId+"'";
		return (List<ChangeContractEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	private int getContractChangeCountByUserId(String userId) {
		String hql = "select count(id) from ChangeContractEntity where IsDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}

	@Override
	public String getChangeContractUserIdByInstanceId(String id) {
		String sql = "select userId from OA_ChangeContract where processInstanceID='"+id+"'";
		return baseDao.getUniqueResult(sql)+"";
	}

	@Override
	public void updateChangeContractProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update ChangeContractEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}

	@Override
	public ChangeContractVo getChangeContractVoByProcessInstanceId(String processInstanceID) {
		List<HistoricDetail> datas = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceID)
				.list();
		ChangeContractVo changeContractVo = null;
		for (HistoricDetail historicDetail : datas) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
			
			if ("arg".equals(variable.getVariableName())) {
				//取最新的赋值
				changeContractVo = (ChangeContractVo) variable.getValue();
			}
		}
		return changeContractVo;
	}

	@Override
	public ListResult<Object> getContractSignVoByAssigner(String id, String applyerId, String beginDate,
			String endDate, Integer page, Integer limit) {
		String sqlList = "SELECT\n" +
				"			p.PROC_INST_ID_,\n" +
				"			t.NAME_,\n" +
				"			p.START_TIME_,\n" +
				"			p.END_TIME_,\n" +
				"			t.START_TIME_ AS START_TIME_1,\n" +
				"			t.END_TIME_ AS END_TIME_1,\n" +
				" 			contract.contractName,\n" +
				"			s.StaffName as applyer\n" +
				"FROM\n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			OA_ContractSign contract,\n" +
				"			OA_Staff s\n" +
				"WHERE\n" +
				"		    p.PROC_DEF_ID_ LIKE 'ContractSign%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND t.ASSIGNEE_ = '"+id+"'\n" +
				"		AND contract.ProcessInstanceID = p.PROC_INST_ID_ and t.NAME_ !='提出申请'\n" +
				"		AND contract.userID = s.UserID\n";
		if(StringUtils.isNotBlank(applyerId)){
			sqlList += "AND contract.userID = '"+applyerId+"'\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlList += "AND contract.addTime>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			endDate += " 23:59:59";
			sqlList += "AND contract.addTime<='"+endDate+"'\n"; 
		}
		sqlList += " order by START_TIME_ desc ";
		List<Object> list = baseDao.findPageList(sqlList, page, limit);
		String sqlCount = "SELECT \n" +
				"			count(p.ID_) \n" +
				"FROM \n" +
				"			ACT_HI_PROCINST p,\n" +
				"			ACT_HI_TASKINST t,\n" +
				"			OA_ContractSign contract,\n" +
				"			OA_Staff s\n" +
				"WHERE\n" +
				"		    p.PROC_DEF_ID_ LIKE 'ContractSign%'\n" +
				"		AND p.PROC_INST_ID_ = t.PROC_INST_ID_\n" +
				"		AND t.ASSIGNEE_ = '"+id+"'\n" +
				"		AND contract.ProcessInstanceID = p.PROC_INST_ID_ and t.NAME_ !='提出申请'\n" +
				"		AND contract.userID = s.UserID\n";
		if(StringUtils.isNotBlank(applyerId)){
			sqlCount += "AND contract.userID = '"+applyerId+"'\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlCount += "AND contract.addTime>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlCount += "AND contract.addTime<='"+endDate+"'\n"; 
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(list, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContractEntity queryContractEntityBy(String userId) {
		String hql = "from ContractEntity where partyB = '"+userId+"' and isDeleted = 0";
		List<ContractEntity> list2 = (List<ContractEntity>) baseDao.hqlfind(hql);
		ContractEntity contractEntity;
		if(list2.size() > 0){
			contractEntity = list2.get(list2.size()-1);
			return contractEntity;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContractEntity getStaffLatestContractByUserId(String userID) {
		String hql = "from ContractEntity where PartyB='"+userID+"' and isDeleted=0 order by beginDate desc";
		List<ContractEntity> contracts = (List<ContractEntity>) baseDao.hqlfind(hql);
		if(contracts.size() > 0){
			return contracts.get(0);
		}
		return null;
	}
}
