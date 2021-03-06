package com.zhizaolian.staff.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BankAccountDao;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.ReimbursementDao;
import com.zhizaolian.staff.dao.ReimbursementDetailDao;
import com.zhizaolian.staff.entity.AdvanceEntity;
import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.entity.PaymentEntity;
import com.zhizaolian.staff.entity.ReimbursementDetailEntity;
import com.zhizaolian.staff.entity.ReimbursementEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.AdvanceVOTransformer;
import com.zhizaolian.staff.transformer.BankAccountVOTransformer;
import com.zhizaolian.staff.transformer.PaymentVOTransformer;
import com.zhizaolian.staff.transformer.ReimbursementVOTransformer;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.AdvanceTaskVO;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.BankAccountVO;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.PaymentTaskVO;
import com.zhizaolian.staff.vo.PaymentVo;
import com.zhizaolian.staff.vo.ReimbursementTaskVO;
import com.zhizaolian.staff.vo.ReimbursementVO;
import com.zhizaolian.staff.vo.StaffVO;

import net.sf.json.JSONArray;

public class ReimbursementServiceImpl implements ReimbursementService {

	@Autowired
	private ReimbursementDao reimbursementDao;
	@Autowired
	private ReimbursementDetailDao reimbursementDetailDao;
	@Autowired
	private BankAccountDao bankAccountDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
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
	@Autowired
	private IdentityService identityService;
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public void startReimbursement(ReimbursementVO reimbursementVO, File[] file,
			String fileDetail) throws FileNotFoundException {
		reimbursementVO
		.setBusinessType(BusinessTypeEnum.REIMBURSEMENT.getName());
		reimbursementVO.setTitle(reimbursementVO.getRequestUserName() + "的"
				+ BusinessTypeEnum.REIMBURSEMENT.getName());
		String[] invoiceTitleList = reimbursementVO.getInvoiceTitle()
				.split("_");
		reimbursementVO.setInvoiceTitleID(Integer.valueOf(invoiceTitleList[0]));
		reimbursementVO.setInvoiceTitle(invoiceTitleList[1]);
		Date now = new Date();
		reimbursementVO.setReimbursementNo("A" + now.getTime());
		// 初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", reimbursementVO);
		String supervisor = staffService
				.querySupervisor(reimbursementVO.getRequestUserID());
		String manager = staffService
				.queryManager(reimbursementVO.getRequestUserID());

		List<Group> groups = identityService.createGroupQuery()
				.groupMember(reimbursementVO.getRequestUserID()).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		int companyID = Integer.parseInt(group.getType().split("_")[0]);
		String firstFinanceCode = Constants.FINANCIAL_FIRST_AUDIT;
		List<String> firstFinance = permissionService
				.findUsersByPermissionCode(firstFinanceCode);
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> finaceManager = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(CollectionUtils.isEmpty(finaceManager)){
			throw new RuntimeException("未找到该申请的财务主管！");
		}
		// 由于 这两个 步骤 反过一次 变量命名 有点问题
		String financial_second_audit = Constants.FINANCIAL_SECOND_AUDIT;
		if (CompanyIDEnum.GUANGZHOU.getValue() == companyID || CompanyIDEnum.FUOSHAN.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_GZ;
		} else if (CompanyIDEnum.QIAN.getValue() == companyID  || CompanyIDEnum.RUDONG.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_QA;
		}
		List<String> financialSecondAuditUsers = permissionService
				.findUsersByPermissionCode(financial_second_audit);
		List<String> financialSecondAuditGroups = permissionService
				.findGroupsByPermissionCode(Constants.FINANCIAL_SECOND_AUDIT);
		List<String> remitMoneyUsers = permissionService
				.findUsersByPermissionCode(Constants.REMIT_MONEY);
		List<String> remitMoneyGroups = permissionService
				.findGroupsByPermissionCode(Constants.REMIT_MONEY);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所在的分公司总经理！");
		}

		if (CollectionUtils.isEmpty(firstFinance)
				|| (!staffService.hasGroupMember(financialSecondAuditGroups)
						&& CollectionUtils.isEmpty(financialSecondAuditUsers))
				|| (!staffService.hasGroupMember(remitMoneyGroups)
						&& CollectionUtils.isEmpty(remitMoneyUsers))) {
			throw new RuntimeException("未找到该申请的财务审批人！");
		}
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("financial_manage", finaceManager.get(0));
		if(StringUtils.isEmpty(reimbursementVO.getMoneyType())){
			reimbursementVO.setMoneyType("人民币:1");
		}
		String moneyRate=reimbursementVO.getMoneyType().split(":")[1];
		Double moneyRateDouble=new Double(moneyRate);
		if (reimbursementVO.getTotalAmount()*moneyRateDouble > 1000) {
			vars.put("firstFinance", companyBoss.get(0));
		} else {
			vars.put("firstFinance", firstFinance.get(0));
		}
		reimbursementVO.setShowPerson2(
				staffService.getRealNameByUserId(finaceManager.get(0)));
		vars.put("financialSecondAuditUsers", financialSecondAuditUsers);
		vars.put("financialSecondAuditGroups", financialSecondAuditGroups);
		vars.put("remitMoneyUsers", remitMoneyUsers);
		vars.put("remitMoneyGroups", remitMoneyGroups);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.REIMBURSEMENT);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), reimbursementVO.getUserID());
		int index = -1;
		if (file != null && file.length > 0) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			int i = 0;
			for (Object o : fileDetailList) {
				index++;
				InputStream is = new FileInputStream(file[i]);
				JSONArray jArray = (JSONArray) o;
				String fileName = (String) jArray.get(0);
				if (StringUtils.isBlank(fileName))
					continue;
				String suffix = (String) jArray.get(1);
				if ("jpg".equals(suffix) || "jpeg".equals(suffix)
						|| "png".equals(suffix)) {
					taskService.createAttachment("picture", task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				} else {
					taskService.createAttachment(suffix, task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				}
				i++;
			}
		}
		reimbursementVO.setTotalAmount((Double.valueOf(new DecimalFormat("0.00")
				.format(reimbursementVO.getTotalAmount()))));
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录转正数据
		saveReimbursement(reimbursementVO, processInstance.getId());
	}

	@Override
	public void reStartReimbursement(ReimbursementVO reimbursementVO,
			File[] file, String fileDetail, String instanceId)
					throws IOException {
		reimbursementVO
		.setBusinessType(BusinessTypeEnum.REIMBURSEMENT.getName());
		reimbursementVO.setTitle(reimbursementVO.getRequestUserName() + "的"
				+ BusinessTypeEnum.REIMBURSEMENT.getName());
		String[] invoiceTitleList = reimbursementVO.getInvoiceTitle()
				.split("_");
		reimbursementVO.setInvoiceTitleID(Integer.valueOf(invoiceTitleList[0]));
		reimbursementVO.setInvoiceTitle(invoiceTitleList[1]);
		Date now = new Date();
		reimbursementVO.setReimbursementNo("A" + now.getTime());
		// 初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", reimbursementVO);
		String supervisor = staffService
				.querySupervisor(reimbursementVO.getRequestUserID());
		String manager = staffService
				.queryManager(reimbursementVO.getRequestUserID());

		List<Group> groups = identityService.createGroupQuery()
				.groupMember(reimbursementVO.getRequestUserID()).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		int companyID = Integer.parseInt(group.getType().split("_")[0]);
		String firstFinanceCode = Constants.FINANCIAL_FIRST_AUDIT;
		List<String> firstFinance = permissionService
				.findUsersByPermissionCode(firstFinanceCode);
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> finaceManager = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		// 由于 这两个 步骤 反过一次 变量命名 有点问题
		String financial_second_audit = Constants.FINANCIAL_SECOND_AUDIT;
		if (CompanyIDEnum.GUANGZHOU.getValue() == companyID || CompanyIDEnum.FUOSHAN.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_GZ;
		} else if (CompanyIDEnum.QIAN.getValue() == companyID  || CompanyIDEnum.RUDONG.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_QA;
		}
		List<String> financialSecondAuditUsers = permissionService
				.findUsersByPermissionCode(financial_second_audit);
		List<String> financialSecondAuditGroups = permissionService
				.findGroupsByPermissionCode(Constants.FINANCIAL_SECOND_AUDIT);
		List<String> remitMoneyUsers = permissionService
				.findUsersByPermissionCode(Constants.REMIT_MONEY);
		List<String> remitMoneyGroups = permissionService
				.findGroupsByPermissionCode(Constants.REMIT_MONEY);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所在的分公司总经理！");
		}

		if (CollectionUtils.isEmpty(firstFinance)
				|| (!staffService.hasGroupMember(financialSecondAuditGroups)
						&& CollectionUtils.isEmpty(financialSecondAuditUsers))
				|| (!staffService.hasGroupMember(remitMoneyGroups)
						&& CollectionUtils.isEmpty(remitMoneyUsers))) {
			throw new RuntimeException("未找到该申请的财务审批人！");
		}
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("financial_manage", finaceManager.get(0));
		if (reimbursementVO.getTotalAmount() > 1000) {
			vars.put("firstFinance", companyBoss.get(0));
		} else {
			vars.put("firstFinance", firstFinance.get(0));
		}
		reimbursementVO.setShowPerson2(
				staffService.getRealNameByUserId(finaceManager.get(0)));
		vars.put("financialSecondAuditUsers", financialSecondAuditUsers);
		vars.put("financialSecondAuditGroups", financialSecondAuditGroups);
		vars.put("remitMoneyUsers", remitMoneyUsers);
		vars.put("remitMoneyGroups", remitMoneyGroups);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.REIMBURSEMENT);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), reimbursementVO.getUserID());
		int index = -1;
		Set<Integer> haveInsertAtta=new HashSet<>();
		if (file != null && file.length > 0) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			int i = 0;
			for (Object o : fileDetailList) {
				index++;
				InputStream is = new FileInputStream(file[i]);
				JSONArray jArray = (JSONArray) o;
				try{
					haveInsertAtta.add(Integer.parseInt(jArray.get(2)+""));
				}catch(Exception e){
				}
				String fileName = (String) jArray.get(0);
				if (StringUtils.isBlank(fileName))
					continue;
				String suffix = (String) jArray.get(1);
				if ("jpg".equals(suffix) || "jpeg".equals(suffix)
						|| "png".equals(suffix)) {
					taskService.createAttachment("picture", task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				} else {
					taskService.createAttachment(suffix, task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				}
				i++;
			}
		}

		for(int i=0;i<reimbursementVO.getAmount().length;i++){
			if(!haveInsertAtta.contains(i)){
				List<Attachment> attas = taskService.getProcessInstanceAttachments(instanceId);
				for (Attachment attachment : attas) {
					if(attachment.getDescription().startsWith(i+""))
						taskService.createAttachment(attachment.getType(), task.getId(), processInstance.getId(), attachment.getName(), attachment.getDescription(),taskService.getAttachmentContent(attachment.getId()));
				}
			}
		}


		reimbursementVO.setTotalAmount((Double.valueOf(new DecimalFormat("0.00")
				.format(reimbursementVO.getTotalAmount()))));
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录转正数据
		saveReimbursement(reimbursementVO, processInstance.getId());

	}

	@Override
	public void startAdvance(AdvanceVo advanceVo, File[] file,
			String fileDetail) throws IOException {
		advanceVo.setBusinessType(BusinessTypeEnum.ADVANCE.getName());
		advanceVo.setTitle(advanceVo.getRequestUserName() + "的"
				+ BusinessTypeEnum.ADVANCE.getName());
		if(1==advanceVo.getIsHaveInvoice()){
			String[] invoiceTitleList = advanceVo.getInvoiceTitle().split("_");
			advanceVo.setInvoiceTitleID(Integer.valueOf(invoiceTitleList[0]));
			advanceVo.setInvoiceTitle(invoiceTitleList[1]);
		}
		Date now = new Date();
		advanceVo.setReimbursementNo("A" + now.getTime());
		// 初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		BankAccountEntity  bankAccount = getBankAccountById(Integer.parseInt(advanceVo.getBankAccountId()));
		advanceVo.setCardName(bankAccount.getCardName());
		advanceVo.setBank(bankAccount.getBank());
		advanceVo.setCardNumber(bankAccount.getCardNumber());
		vars.put("arg", advanceVo);
		String supervisor = staffService
				.querySupervisor(advanceVo.getRequestUserID());
		String manager = staffService
				.queryManager(advanceVo.getRequestUserID());

		List<Group> groups = identityService.createGroupQuery()
				.groupMember(advanceVo.getRequestUserID()).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		int companyID = Integer.parseInt(group.getType().split("_")[0]);

		String firstFinanceCode = Constants.FINANCIAL_FIRST_AUDIT;
		List<String> firstFinance = permissionService
				.findUsersByPermissionCode(firstFinanceCode);
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> finaceManager = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(CollectionUtils.isEmpty(finaceManager)){
			throw new RuntimeException("未找到该申请的财务主管！");
		}
		// 由于 这两个 步骤 反过一次 变量命名 有点问题
		String financial_second_audit = Constants.FINANCIAL_SECOND_AUDIT;
		if (CompanyIDEnum.GUANGZHOU.getValue() == companyID ||  CompanyIDEnum.FUOSHAN.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_GZ;
		} else if (CompanyIDEnum.QIAN.getValue() == companyID ||  CompanyIDEnum.RUDONG.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_QA;
		}
		List<String> financialSecondAuditUsers = permissionService
				.findUsersByPermissionCode(financial_second_audit);
		List<String> financialSecondAuditGroups = permissionService
				.findGroupsByPermissionCode(Constants.FINANCIAL_SECOND_AUDIT);
		List<String> remitMoneyUsers = permissionService
				.findUsersByPermissionCode(Constants.REMIT_MONEY);
		List<String> remitMoneyGroups = permissionService
				.findGroupsByPermissionCode(Constants.REMIT_MONEY);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所在的分公司总经理！");
		}

		if (CollectionUtils.isEmpty(firstFinance)
				|| (!staffService.hasGroupMember(financialSecondAuditGroups)
						&& CollectionUtils.isEmpty(financialSecondAuditUsers))
				|| (!staffService.hasGroupMember(remitMoneyGroups)
						&& CollectionUtils.isEmpty(remitMoneyUsers))) {
			throw new RuntimeException("未找到该申请的财务审批人！");
		}
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));

		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("financial_manage", finaceManager.get(0));
		String moneyRate=advanceVo.getMoneyType().split(":")[1];
		Double moneyRateDouble=new Double(moneyRate);
		if (advanceVo.getTotalAmount()*moneyRateDouble > 1000) {
			vars.put("firstFinance", companyBoss.get(0));
		} else {
			vars.put("firstFinance", firstFinance.get(0));
		}
		advanceVo.setShowPerson2(
				staffService.getRealNameByUserId(finaceManager.get(0)));
		vars.put("financialSecondAuditUsers", financialSecondAuditUsers);
		vars.put("financialSecondAuditGroups", financialSecondAuditGroups);
		vars.put("remitMoneyUsers", remitMoneyUsers);
		vars.put("remitMoneyGroups", remitMoneyGroups);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.ADVANCE);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), advanceVo.getUserID());
		int index = -1;
		if (file != null && file.length > 0) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			int i = 0;
			for (Object o : fileDetailList) {
				index++;
				InputStream is = new FileInputStream(file[i]);
				JSONArray jArray = (JSONArray) o;
				String fileName = (String) jArray.get(0);
				if (StringUtils.isBlank(fileName))
					continue;
				String suffix = (String) jArray.get(1);
				if ("jpg".equals(suffix) || "jpeg".equals(suffix)
						|| "png".equals(suffix)) {
					taskService.createAttachment("picture", task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				} else {
					taskService.createAttachment(suffix, task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				}
				i++;
			}
		}

		advanceVo.setTotalAmount((Double.valueOf(
				new DecimalFormat("0.00").format(advanceVo.getTotalAmount()))));
		// 完成任务
		taskService.complete(task.getId(), vars);
		saveAdvance(advanceVo, processInstance.getId());
	}

	@Override
	public ListResult<ReimbursementVO> findReimbursementListByUserID(
			String userID, int page, int limit) {
		// 查询OA_Reimbursement表的数据
		List<ReimbursementEntity> reimbursementEntities = reimbursementDao
				.findReimbursementsByUserID(userID, page, limit);
		List<ReimbursementVO> reimbursementVOs = new ArrayList<ReimbursementVO>();
		for (ReimbursementEntity reimbursement : reimbursementEntities) {
			ReimbursementVO reimbursementVO = new ReimbursementVO();
			reimbursementVO.setReStart(reimbursement.getReStart());
			reimbursementVO
			.setProcessInstanceID(reimbursement.getProcessInstanceID());
			reimbursementVO
			.setReimbursementNo(reimbursement.getReimbursementNo());
			reimbursementVO.setTotalAmount(reimbursement.getTotalAmount());

			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(reimbursement.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					ReimbursementVO arg = (ReimbursementVO) variable.getValue();
					reimbursementVO.setRequestDate(arg.getRequestDate());
					reimbursementVO.setTitle(arg.getTitle());
					reimbursementVO.setPayeeName(arg.getPayeeName());
					reimbursementVO
					.setRequestUserName(arg.getRequestUserName());
					reimbursementVO.setInvoiceTitle(arg.getInvoiceTitle());
					reimbursementVO.setInvoiceNum(arg.getInvoiceNum());
					reimbursementVO.setDetailNum(arg.getDetailNum());
					reimbursementVO.setCardName(arg.getCardName());
					reimbursementVO.setBank(arg.getBank());
					reimbursementVO.setCardNumber(arg.getCardNumber());
					reimbursementVO.setUsage(arg.getUsage());
					reimbursementVO.setReternenceName(arg.getReternenceName());
					reimbursementVO
					.setReternenceMobile(arg.getReternenceMobile());
					reimbursementVO.setIsFixedAsset(arg.getIsFixedAsset());
					reimbursementVO.setFixedAssetNo(arg.getFixedAssetNo());
					reimbursementVO.setAmount(arg.getAmount());
				}
			}

			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(reimbursement.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				reimbursementVO.setStatus("处理中");
				runtimeService.createProcessInstanceQuery().list();
				try{
					Task task=taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
					if(task.getAssignee()==null){
						List<IdentityLink> idList =taskService.getIdentityLinksForTask(task.getId());
						String prevStr=processService
								.getProcessTaskAssignee(pInstance.getId());
						String[] nameArr=new String[idList.size()];
						int i=0;
						for (IdentityLink identityLink : idList) {
							nameArr[i++]=staffService.getRealNameByUserId(identityLink.getUserId());
						}
						reimbursementVO.setAssigneeUserName("【"+prevStr+"】"+StringUtils.join(nameArr,","));
					}else{
						reimbursementVO.setAssigneeUserName(processService
								.getProcessTaskAssignee(pInstance.getId()));
					}
				}catch(Exception e){
					reimbursementVO.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				}


			} else {
				reimbursementVO.setStatus(TaskResultEnum
						.valueOf(reimbursement.getProcessStatus()).getName());
			}
			reimbursementVOs.add(reimbursementVO);
		}

		int count = reimbursementDao.countReimbursementsByUserID(userID);
		return new ListResult<ReimbursementVO>(reimbursementVOs, count);
	}

	@Override
	public ListResult<AdvanceVo> findAdvanceListByUserID(String userID,
			int page, int limit) {
		List<AdvanceEntity> reimbursementEntities = reimbursementDao
				.findAdvancesByUserID(userID, page, limit);
		List<AdvanceVo> advanceVos = new ArrayList<AdvanceVo>();
		for (AdvanceEntity reimbursement : reimbursementEntities) {
			AdvanceVo advanceVo = new AdvanceVo();
			advanceVo
			.setProcessInstanceID(reimbursement.getProcessInstanceID());
			advanceVo.setReimbursementNo(reimbursement.getReimbursementNo());
			advanceVo.setTotalAmount(reimbursement.getTotalAmount());

			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(reimbursement.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					AdvanceVo arg = (AdvanceVo) variable.getValue();
					advanceVo.setRequestDate(arg.getRequestDate());
					advanceVo.setTitle(arg.getTitle());
					//这边把领款人只能为公司内部人的限制取消了，用户id修改为姓名
					if(arg.getPayeeID().length()>25 && arg.getPayeeID().contains("-")){
						StaffVO payeeUser = staffService
								.getStaffByUserID(arg.getPayeeID());
						advanceVo
						.setPayeeName(payeeUser == null ? "" : payeeUser.getLastName());
					}else{
						advanceVo
						.setPayeeName(arg.getPayeeID());
					}
					//advanceVo.setPayeeName(arg.getPayeeName());
					advanceVo.setRequestUserName(arg.getRequestUserName());
					advanceVo.setIsHaveInvoice(arg.getIsHaveInvoice());
					advanceVo.setInvoiceTitle(arg.getInvoiceTitle());
					advanceVo.setInvoiceNum(arg.getInvoiceNum());
					advanceVo.setDetailNum(arg.getDetailNum());
					advanceVo.setCardName(arg.getCardName());
					advanceVo.setBank(arg.getBank());
					advanceVo.setCardNumber(arg.getCardNumber());
					advanceVo.setUsage(arg.getUsage());
					advanceVo.setReternenceName(arg.getReternenceName());
					advanceVo.setReternenceMobile(arg.getReternenceMobile());
					advanceVo.setIsFixedAsset(arg.getIsFixedAsset());
					advanceVo.setFixedAssetNo(arg.getFixedAssetNo());
					advanceVo.setAmount(arg.getAmount());
				}
			}

			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(reimbursement.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				advanceVo.setStatus("处理中");
				runtimeService.createProcessInstanceQuery().list();

				try{
					Task task=taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
					if(task.getAssignee()==null){
						List<IdentityLink> idList =taskService.getIdentityLinksForTask(task.getId());
						String prevStr=processService
								.getProcessTaskAssignee(pInstance.getId());
						String[] nameArr=new String[idList.size()];
						int i=0;
						for (IdentityLink identityLink : idList) {
							nameArr[i++]=staffService.getRealNameByUserId(identityLink.getUserId());
						}
						advanceVo.setAssigneeUserName("【"+prevStr+"】"+StringUtils.join(nameArr,","));
					}else{
						advanceVo.setAssigneeUserName(processService
								.getProcessTaskAssignee(pInstance.getId()));
					}
				}catch(Exception e){
					advanceVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				}


			} else {
				advanceVo.setStatus(TaskResultEnum
						.valueOf(reimbursement.getProcessStatus()).getName());
			}
			advanceVos.add(advanceVo);
		}

		int count = reimbursementDao.countAdvancesByUserID(userID);
		return new ListResult<AdvanceVo>(advanceVos, count);
	}

	@Override
	public void updateProcessStatus(String processInstanceID,
			TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}

		reimbursementDao.updateProcessStatusByProcessInstanceID(
				processInstanceID, taskResult.getValue());
	}

	@Override
	public void updateAdvanceProcessStatus(String processInstanceID,
			TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		reimbursementDao.updateAdvanceProcessStatusByProcessInstanceID(
				processInstanceID, taskResult.getValue());
	}

	@Override
	public BankAccountVO getBankAccountByUserID(String userID) {
		BankAccountEntity bankAccountEntity = bankAccountDao
				.getBankAccountByUserID(userID);
		return BankAccountVOTransformer.INSTANCE.apply(bankAccountEntity);
	}

	@Override
	public void updateBankAccountByUserID(String userID,
			ReimbursementVO reimbursementVO) {
		BankAccountEntity bankAccountEntity = bankAccountDao
				.getBankAccountByUserID(userID);
		if (bankAccountEntity == null) {
			throw new RuntimeException("该用户的打款账号不存在！");
		}

		bankAccountEntity.setCardName(reimbursementVO.getCardName());
		bankAccountEntity.setBank(reimbursementVO.getBank());
		bankAccountEntity.setCardNumber(reimbursementVO.getCardNumber());
		bankAccountEntity.setUpdateTime(new Date());
		bankAccountDao.save(bankAccountEntity);
	}

	@Override
	public void updateAdvanceBankAccountByUserID(String userID,
			AdvanceVo advanceVo) {
		BankAccountEntity bankAccountEntity = bankAccountDao
				.getBankAccountByUserID(userID);
		if (bankAccountEntity == null) {
			throw new RuntimeException("该用户的打款账号不存在！");
		}

		bankAccountEntity.setCardName(advanceVo.getCardName());
		bankAccountEntity.setBank(advanceVo.getBank());
		bankAccountEntity.setCardNumber(advanceVo.getCardNumber());
		bankAccountEntity.setUpdateTime(new Date());
		bankAccountDao.save(bankAccountEntity);
		
	}

	@Override
	public ListResult<ReimbursementTaskVO> findReimbursementsByUserGroupIDs(
			List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			String reimbursementNo, String beginDate, String endDate, int page,
			int limit) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
		String taskNames = Arrays.toString(Lists2
				.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
					@Override
					protected String safeApply(TaskDefKeyEnum input) {
						return "'" + input.getName() + "'";
					}
				}).toArray());
		String userIDs = Arrays.toString(
				Lists2.transform(users, new SafeFunction<String, String>() {
					@Override
					protected String safeApply(String input) {
						return "'" + input + "'";
					}
				}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Reimbursement reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		String whereSQL = generateWhereSQL(reimbursementNo, beginDate, endDate);
		sql += whereSQL;

		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<ReimbursementTaskVO> taskVOs = createReimbursementTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Reimbursement reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<ReimbursementTaskVO>(taskVOs, count);
	}



	@Override
	public ListResult<ReimbursementTaskVO> findReimbursementsAll(String reimbursementNo, String demandName, String beginDate,
			String endDate, int page, int limit) {
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Reimbursement reimbursement, oa_staff staff\n" +
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID AND reimbursement.RequestUserID = staff.UserID";
		String whereSQL = relevanceSQL(reimbursementNo, demandName, beginDate, endDate);
		sql += whereSQL;
		sql += " order by reimbursement.addTime desc";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<ReimbursementTaskVO> taskVOs = createReimbursementTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Reimbursement reimbursement, oa_staff staff "+
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID AND reimbursement.RequestUserID = staff.UserID";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<ReimbursementTaskVO>(taskVOs, count);
	}

	@Override
	public ListResult<AdvanceTaskVO> findAdvancessByUserGroupIDs(
			List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			String reimbursementNo, String beginDate, String endDate, int page,
			int limit) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
		String taskNames = Arrays.toString(Lists2
				.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
					@Override
					protected String safeApply(TaskDefKeyEnum input) {
						return "'" + input.getName() + "'";
					}
				}).toArray());
		String userIDs = Arrays.toString(
				Lists2.transform(users, new SafeFunction<String, String>() {
					@Override
					protected String safeApply(String input) {
						return "'" + input + "'";
					}
				}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Advance reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		String whereSQL = generateWhereSQL(reimbursementNo, beginDate, endDate);
		sql += whereSQL;

		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<AdvanceTaskVO> taskVOs = createAdvanceTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Advance reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<AdvanceTaskVO>(taskVOs, count);
	}



	@Override
	public ListResult<AdvanceTaskVO> findAdvancessAll(String reimbursementNo, String beginDate, String endDate,
			int page, int limit) {
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Advance reimbursement " +
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID";
		String whereSQL = generateWhereSQL(reimbursementNo, beginDate, endDate);
		sql += whereSQL;
		sql += " order by reimbursement.addTime desc";

		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<AdvanceTaskVO> taskVOs = createAdvanceTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Advance reimbursement " +
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<AdvanceTaskVO>(taskVOs, count);
	}

	@Override
	public ReimbursementVO getReimbursementVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (ReimbursementVO) runtimeService.getVariable(pInstance.getId(),
				"arg");
	}

	@Override
	public AdvanceVo getAdvanceVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (AdvanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
	}

	@Override
	public ReimbursementVO getReimbursementVOByProcessInstanceID(
			String processInstanceID) {
		ReimbursementEntity reimbursementEntity = reimbursementDao
				.getReimbursementByProcessInstanceID(processInstanceID);
		ReimbursementVO reimbursementVO = ReimbursementVOTransformer.INSTANCE
				.apply(reimbursementEntity);
		StaffVO requestUser = staffService
				.getStaffByUserID(reimbursementVO.getRequestUserID());
		StaffVO payeeUser = staffService
				.getStaffByUserID(reimbursementVO.getPayeeID());
		reimbursementVO.setRequestUserName(
				requestUser == null ? "" : requestUser.getLastName());
		reimbursementVO
		.setPayeeName(payeeUser == null ? "" : payeeUser.getLastName());
		List<ReimbursementDetailEntity> detailEntities = reimbursementDetailDao
				.findReimbursementDetailsByReimbursementID(
						reimbursementVO.getReimbursementID(), 0);
		int size = detailEntities.size();
		String[] usages = new String[size];
		Double[] amounts = new Double[size];
		for (int i = 0; i < size; ++i) {
			ReimbursementDetailEntity detailEntity = detailEntities.get(i);
			usages[i] = detailEntity.getPurpose();
			amounts[i] = detailEntity.getAmount();
		}
		reimbursementVO.setUsage(usages);
		reimbursementVO.setAmount(amounts);
		return reimbursementVO;
	}

	@Override
	public AdvanceVo geAdvanceTaskVOByProcessInstanceID(
			String processInstanceID) {
		AdvanceEntity reimbursementEntity = reimbursementDao
				.getAdvanceByProcessInstanceID(processInstanceID);
		AdvanceVo reimbursementVO = AdvanceVOTransformer.INSTANCE
				.apply(reimbursementEntity);
		StaffVO requestUser = staffService
				.getStaffByUserID(reimbursementVO.getRequestUserID());
		//这边把领款人只能为公司内部人的限制取消了，用户id修改为姓名
		if(reimbursementVO.getPayeeID().length()>25 && reimbursementVO.getPayeeID().contains("-")){
			StaffVO payeeUser = staffService
					.getStaffByUserID(reimbursementVO.getPayeeID());
			reimbursementVO
			.setPayeeName(payeeUser == null ? "" : payeeUser.getLastName());
		}else{
			reimbursementVO
			.setPayeeName(reimbursementVO.getPayeeID());
		}
		reimbursementVO.setRequestUserName(
				requestUser == null ? "" : requestUser.getLastName());

		List<ReimbursementDetailEntity> detailEntities = reimbursementDetailDao
				.findReimbursementDetailsByReimbursementID(
						reimbursementVO.getReimbursementID(), 1);
		int size = detailEntities.size();
		String[] usages = new String[size];
		Double[] amounts = new Double[size];
		for (int i = 0; i < size; ++i) {
			ReimbursementDetailEntity detailEntity = detailEntities.get(i);
			usages[i] = detailEntity.getPurpose();
			amounts[i] = detailEntity.getAmount();
		}
		reimbursementVO.setUsage(usages);
		reimbursementVO.setAmount(amounts);

		return reimbursementVO;
	}

	private String generateWhereSQL(String reimbursementNo, String beginDate,
			String endDate) {
		StringBuffer sql = new StringBuffer();
		if (!StringUtils.isBlank(reimbursementNo)) {
			sql.append(" and reimbursement.ReimbursementNo like '%"
					+ reimbursementNo + "%' ");
		}
		if (!StringUtils.isBlank(beginDate)) {
			sql.append(" and reimbursement.AddTime >= '" + beginDate
					+ " 00:00:00' ");
		}
		if (!StringUtils.isBlank(endDate)) {
			sql.append(
					" and reimbursement.AddTime <= '" + endDate + " 23:59:59' ");
		}
		return sql.toString();
	}
	private String relevanceSQL(String reimbursementNo, String demandName, String beginDate,
			String endDate){
		StringBuffer sql = new StringBuffer();
		if (!StringUtils.isBlank(reimbursementNo)) {
			sql.append(" and reimbursement.ReimbursementNo like '%"
					+ reimbursementNo + "%' ");
		}
		if(!StringUtils.isBlank(demandName)){
			sql.append(" AND staff.StaffName like '%"
					+ demandName + "%' ");
		}
		if (!StringUtils.isBlank(beginDate)) {
			sql.append(" and reimbursement.AddTime >= '" + beginDate
					+ " 00:00:00' ");
		}
		if (!StringUtils.isBlank(endDate)) {
			sql.append(
					" and reimbursement.AddTime <= '" + endDate + " 23:59:59' ");
		}
		return sql.toString();
	}
	
	@Override
	public List<ReimbursementTaskVO> createTaskVOListByTaskList(
			List<Task> tasks) {
		List<ReimbursementTaskVO> taskVOs = new ArrayList<ReimbursementTaskVO>();
		for (Task task : tasks) {
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId())
					.singleResult();
			// 查询流程参数
			ReimbursementVO arg = (ReimbursementVO) runtimeService
					.getVariable(pInstance.getId(), "arg");
			ReimbursementTaskVO reimbursementTaskVO = new ReimbursementTaskVO();
			reimbursementTaskVO.setProcessInstanceID(pInstance.getId());
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID(task.getId());
			reimbursementTaskVO.setTaskName(task.getName());
			reimbursementTaskVO.setTaskDefKey(task.getTaskDefinitionKey());
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo(arg.getReimbursementNo());
			reimbursementTaskVO.setTotalAmount(arg.getTotalAmount());
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	@Override
	public List<AdvanceTaskVO> createAdvanceTaskVOListByTaskList(
			List<Task> tasks) {
		List<AdvanceTaskVO> taskVOs = new ArrayList<AdvanceTaskVO>();
		for (Task task : tasks) {
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId())
					.singleResult();
			// 查询流程参数
			AdvanceVo arg = (AdvanceVo) runtimeService
					.getVariable(pInstance.getId(), "arg");
			AdvanceTaskVO reimbursementTaskVO = new AdvanceTaskVO();
			reimbursementTaskVO.setProcessInstanceID(pInstance.getId());
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID(task.getId());
			reimbursementTaskVO.setTaskName(task.getName());
			reimbursementTaskVO.setTaskDefKey(task.getTaskDefinitionKey());
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo(arg.getReimbursementNo());
			reimbursementTaskVO.setTotalAmount(arg.getTotalAmount());
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	private List<ReimbursementTaskVO> createReimbursementTaskList(
			List<Object> reimbursements) {
		List<ReimbursementTaskVO> taskVOs = new ArrayList<ReimbursementTaskVO>();
		for (Object task : reimbursements) {
			Object[] objs = (Object[]) task;
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId((String) objs[1]).singleResult();
			// 查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(),
					"arg");
			ReimbursementTaskVO reimbursementTaskVO = new ReimbursementTaskVO();
			reimbursementTaskVO.setProcessInstanceID((String) objs[1]);
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID((String) objs[0]);
			reimbursementTaskVO.setTaskName((String) objs[2]);
			reimbursementTaskVO.setTaskDefKey((String) objs[3]);
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo((String) objs[4]);
			reimbursementTaskVO.setTotalAmount((Double) objs[5]);
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	private List<AdvanceTaskVO> createAdvanceTaskList(
			List<Object> reimbursements) {
		List<AdvanceTaskVO> taskVOs = new ArrayList<>();
		for (Object task : reimbursements) {
			Object[] objs = (Object[]) task;
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId((String) objs[1]).singleResult();
			// 查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(),
					"arg");
			AdvanceTaskVO reimbursementTaskVO = new AdvanceTaskVO();
			reimbursementTaskVO.setProcessInstanceID((String) objs[1]);
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID((String) objs[0]);
			reimbursementTaskVO.setTaskName((String) objs[2]);
			reimbursementTaskVO.setTaskDefKey((String) objs[3]);
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo((String) objs[4]);
			reimbursementTaskVO.setTotalAmount((Double) objs[5]);
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	private void saveReimbursement(ReimbursementVO reimbursementVO,
			String processInstanceID) {
		Date now = new Date();
		ReimbursementEntity reimbursementEntity = ReimbursementEntity.builder()
				.reimbursementNo(reimbursementVO.getReimbursementNo())
				.userID(reimbursementVO.getUserID())
				.requestUserID(reimbursementVO.getRequestUserID())
				.payeeID(reimbursementVO.getPayeeID())
				.invoiceTitle(reimbursementVO.getInvoiceTitleID())
				.invoiceNum(reimbursementVO.getInvoiceNum())
				.detailNum(reimbursementVO.getDetailNum())
				.totalAmount(reimbursementVO.getTotalAmount())
				.processInstanceID(processInstanceID)
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(now)
				.reternenceId(reimbursementVO.getReternenceId())
				.reternenceMobile(reimbursementVO.getReternenceMobile())
				.reternenceName(reimbursementVO.getReternenceName())
				.showPerson2(reimbursementVO.getShowPerson2())
				.isFixedAsset(reimbursementVO.getIsFixedAsset())
				.fixedAssetNo(reimbursementVO.getFixedAssetNo()).updateTime(now)
				.moneyType(reimbursementVO.getMoneyType())
				.build();
		int reimbursementID = reimbursementDao.save(reimbursementEntity);

		int useSize = reimbursementVO.getUsage().length;
		String[] usage = reimbursementVO.getUsage();
		Double[] amount = reimbursementVO.getAmount();
		for (int i = 0; i < useSize; ++i) {
			ReimbursementDetailEntity reimbursementDetailEntity = ReimbursementDetailEntity
					.builder().reimbursementID(reimbursementID)
					.purpose(usage[i]).amount(amount[i])
					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
					.addTime(now).updateTime(now).type("0").build();
			reimbursementDetailDao.save(reimbursementDetailEntity);
		}

		// 保存领款人打款账号
		BankAccountEntity bankAccount = bankAccountDao
				.getBankAccountByUserID(reimbursementVO.getPayeeID());
		if (bankAccount == null) {
			bankAccount = new BankAccountEntity();
			bankAccount.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			bankAccount.setAddTime(new Date());
		}
		bankAccount.setUserID(reimbursementVO.getPayeeID());
		bankAccount.setCardName(reimbursementVO.getCardName());
		bankAccount.setBank(reimbursementVO.getBank());
		bankAccount.setCardNumber(reimbursementVO.getCardNumber());
		bankAccount.setUpdateTime(new Date());
		bankAccountDao.save(bankAccount);

	}
	private void saveAdvance(AdvanceVo advanceVo, String processInstanceID) {
		Date now = new Date();
		AdvanceEntity advanceEntity = AdvanceEntity.builder()
				.reimbursementNo(advanceVo.getReimbursementNo())
				.userID(advanceVo.getUserID())
				.requestUserID(advanceVo.getRequestUserID())
				.payeeID(advanceVo.getPayeeID())
				.invoiceTitle(advanceVo.getInvoiceTitleID())
				.invoiceNum(advanceVo.getInvoiceNum())
				.detailNum(advanceVo.getDetailNum())
				.totalAmount(advanceVo.getTotalAmount())
				.processInstanceID(processInstanceID)
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(now)
				.reternenceId(advanceVo.getReternenceId())
				.isHaveInvoice(advanceVo.getIsHaveInvoice())
				.reternenceMobile(advanceVo.getReternenceMobile())
				.reternenceName(advanceVo.getReternenceName())
				.showPerson2(advanceVo.getShowPerson2())
				.isFixedAsset(advanceVo.getIsFixedAsset())
				.fixedAssetNo(advanceVo.getFixedAssetNo()).updateTime(now)
				.moneyType(advanceVo.getMoneyType())
				.bankAccountId(advanceVo.getBankAccountId())
				.build();
		int reimbursementID = reimbursementDao.saveAdvance(advanceEntity);

		int useSize = advanceVo.getUsage().length;
		String[] usage = advanceVo.getUsage();
		Double[] amount = advanceVo.getAmount();
		for (int i = 0; i < useSize; ++i) {
			ReimbursementDetailEntity reimbursementDetailEntity = ReimbursementDetailEntity
					.builder().reimbursementID(reimbursementID)
					.purpose(usage[i]).amount(amount[i])
					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
					.addTime(now).updateTime(now).type("1").build();
			reimbursementDetailDao.save(reimbursementDetailEntity);
		}
		// 保存领款人打款账号
	/*	BankAccountEntity bankAccount = bankAccountDao
				.getBankAccountByUserID(advanceVo.getPayeeID());
		if (bankAccount == null) {
			bankAccount = new BankAccountEntity();
			bankAccount.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			bankAccount.setAddTime(new Date());
		}
		bankAccount.setUserID(advanceVo.getPayeeID());
		bankAccount.setCardName(advanceVo.getCardName());
		bankAccount.setBank(advanceVo.getBank());
		bankAccount.setCardNumber(advanceVo.getCardNumber());
		bankAccount.setUpdateTime(new Date());
		bankAccountDao.save(bankAccount);*/
	}
	@Override
	public ListResult<ReimbursementVO> findReimbursementList(
			ReimbursementVO reimbursementVO, int page, int limit) {
		List<Object> list = baseDao.findPageList(
				getReimbursementListBySql(reimbursementVO), page, limit);
		List<ReimbursementVO> reimbursementVOs = new ArrayList<>();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			ReimbursementVO reimbursementVO2 = new ReimbursementVO();
			reimbursementVO2.setReimbursementNo((String) objs[0]);
			reimbursementVO2.setRequestUserName(staffService
					.getStaffByUserID((String) objs[1]).getLastName());
			reimbursementVO2.setTotalAmount((Double) objs[2]);
			reimbursementVO2.setRequestDate(objs[3] == null
					? ""
							: DateUtil.formateFullDate((Date) objs[3]));
			reimbursementVO2.setProcessInstanceID((String) objs[4]);
			reimbursementVOs.add(reimbursementVO2);
		}
		Object countObj = baseDao
				.getUniqueResult(getCountReimbursementBySql(reimbursementVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<ReimbursementVO>(reimbursementVOs, count);
	}

	@Override
	public ListResult<AdvanceVo> findAdvanceList(
			ReimbursementVO reimbursementVO, int page, int limit) {
		List<Object> list = baseDao.findPageList(
				getAdvanceListBySql(reimbursementVO), page, limit);
		List<AdvanceVo> reimbursementVOs = new ArrayList<>();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			AdvanceVo reimbursementVO2 = new AdvanceVo();
			reimbursementVO2.setReimbursementNo((String) objs[0]);
			reimbursementVO2.setRequestUserName(staffService
					.getStaffByUserID((String) objs[1]).getLastName());
			reimbursementVO2.setTotalAmount((Double) objs[2]);
			reimbursementVO2.setRequestDate(objs[3] == null
					? ""
							: DateUtil.formateFullDate((Date) objs[3]));
			reimbursementVO2.setProcessInstanceID((String) objs[4]);
			reimbursementVOs.add(reimbursementVO2);
		}
		Object countObj = baseDao
				.getUniqueResult(getCountAdvanceBySql(reimbursementVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<AdvanceVo>(reimbursementVOs, count);
	}

	private String getReimbursementListBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select reimbursement.ReimbursementNo,reimbursement.RequestUserID, "
						+ "reimbursement.TotalAmount,reimbursement.AddTime, reimbursement.ProcessInstanceID from OA_Reimbursement reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();

	}
	private String getAdvanceListBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select reimbursement.ReimbursementNo,reimbursement.RequestUserID, "
						+ "reimbursement.TotalAmount,reimbursement.AddTime, reimbursement.ProcessInstanceID from OA_Advance reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();

	}
	private String getCountReimbursementBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select count(*) from OA_Reimbursement reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();
	}
	private String getCountAdvanceBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select count(*) from OA_Advance reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();
	}
	private String getWhereBySql(ReimbursementVO reimbursementVO) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(reimbursementVO.getRequestUserName())) {
			whereSql.append(" and staff.StaffName like '%"
					+ reimbursementVO.getRequestUserName() + "%' ");
		}
		if (!StringUtils.isBlank(reimbursementVO.getBeginDate())) {
			whereSql.append(" and reimbursement.AddTime >= '"
					+ reimbursementVO.getBeginDate() + "'");
		}
		if (!StringUtils.isBlank(reimbursementVO.getEndDate())) {
			whereSql.append(" and reimbursement.AddTime <= '"
					+ reimbursementVO.getEndDate() + "'");
		}
		if (!StringUtils.isBlank(reimbursementVO.getReimbursementNo())) {
			whereSql.append(" and reimbursement.ReimbursementNo like '%"
					+ reimbursementVO.getReimbursementNo() + "%' ");
		}
		return whereSql.toString();

	}

	@Override
	public void setfinancialFirstAuditName(String instanceId, String name,
			int type) {
		reimbursementDao.setfinancialFirstAuditName(instanceId, name, type);
	}

	@Override
	public void setAdvanceFinancialFirstAuditName(String instanceId,
			String name, int type) {
		reimbursementDao.setAdvanceFinancialFirstAuditName(instanceId, name,
				type);

	}

	@Override
	public void updateRestartStatus(Integer reimbursementID) {
		String sql = "update OA_Reimbursement set reStart=1 where ReimbursementID="+reimbursementID;
		baseDao.excuteSql(sql);
	}

	@Override
	public void startPayment(PaymentVo paymentVo, File[] file, String fileDetail) throws Exception {
		paymentVo.setBusinessType(BusinessTypeEnum.PAYMENT.getName());
		paymentVo.setTitle(paymentVo.getRequestUserName() + "的"
				+ BusinessTypeEnum.PAYMENT.getName());
		if(1==paymentVo.getIsHaveInvoice()){
			String[] invoiceTitleList = paymentVo.getInvoiceTitle().split("_");
			paymentVo.setInvoiceTitleID(Integer.valueOf(invoiceTitleList[0]));
			paymentVo.setInvoiceTitle(invoiceTitleList[1]);
		}
		Date now = new Date();
		paymentVo.setReimbursementNo("A" + now.getTime());
		// 初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		BankAccountEntity  bankAccount = getBankAccountById(Integer.parseInt(paymentVo.getBankAccountId()));
		paymentVo.setCardName(bankAccount.getCardName());
		paymentVo.setBank(bankAccount.getBank());
		paymentVo.setCardNumber(bankAccount.getCardNumber());
		vars.put("arg", paymentVo);
		String supervisor = staffService
				.querySupervisor(paymentVo.getRequestUserID());
		String manager = staffService
				.queryManager(paymentVo.getRequestUserID());

		List<Group> groups = identityService.createGroupQuery()
				.groupMember(paymentVo.getRequestUserID()).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		int companyID = Integer.parseInt(group.getType().split("_")[0]);

		String firstFinanceCode = Constants.FINANCIAL_FIRST_AUDIT;
		List<String> firstFinance = permissionService
				.findUsersByPermissionCode(firstFinanceCode);
		List<String> companyBoss = permissionService
				.findUsersByPermissionCode(Constants.COMPANY_BOSS);
		List<String> finaceManager = permissionService
				.findUsersByPermissionCode(Constants.FINANCIAL_MANAGER);
		if(CollectionUtils.isEmpty(finaceManager)){
			throw new RuntimeException("未找到该申请的财务主管！");
		}
		// 由于 这两个 步骤 反过一次 变量命名 有点问题
		String financial_second_audit = Constants.FINANCIAL_SECOND_AUDIT;
		if (CompanyIDEnum.GUANGZHOU.getValue() == companyID ||  CompanyIDEnum.FUOSHAN.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_GZ;
		} else if (CompanyIDEnum.QIAN.getValue() == companyID ||  CompanyIDEnum.RUDONG.getValue() == companyID) {
			financial_second_audit = Constants.FINANCIAL_FIRST_AUDIT_QA;
		}
		List<String> financialSecondAuditUsers = permissionService
				.findUsersByPermissionCode(financial_second_audit);
		List<String> financialSecondAuditGroups = permissionService
				.findGroupsByPermissionCode(Constants.FINANCIAL_SECOND_AUDIT);
		List<String> remitMoneyUsers = permissionService
				.findUsersByPermissionCode(Constants.REMIT_MONEY);
		List<String> remitMoneyGroups = permissionService
				.findGroupsByPermissionCode(Constants.REMIT_MONEY);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所在的分公司总经理！");
		}

		if (CollectionUtils.isEmpty(firstFinance)
				|| (!staffService.hasGroupMember(financialSecondAuditGroups)
						&& CollectionUtils.isEmpty(financialSecondAuditUsers))
				|| (!staffService.hasGroupMember(remitMoneyGroups)
						&& CollectionUtils.isEmpty(remitMoneyUsers))) {
			throw new RuntimeException("未找到该申请的财务审批人！");
		}
		List<String> fundAllocationUsers = permissionService.findUsersByPermissionCode(Constants.FUND_ALLOCATION);
		if(CollectionUtils.isEmpty(fundAllocationUsers)){
			throw new RuntimeException("未找到该申请的资金分配审批人！");
		}
		vars.put("fundAllocationUser", fundAllocationUsers.get(0));

		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("financial_manage", finaceManager.get(0));
		String moneyRate = paymentVo.getMoneyType().split(":")[1];
		Double moneyRateDouble=new Double(moneyRate);
		if (paymentVo.getTotalAmount()*moneyRateDouble > 1000) {
			vars.put("firstFinance", companyBoss.get(0));
		} else {
			vars.put("firstFinance", firstFinance.get(0));
		}
		paymentVo.setShowPerson2(
				staffService.getRealNameByUserId(finaceManager.get(0)));
		vars.put("financialSecondAuditUsers", financialSecondAuditUsers);
		vars.put("financialSecondAuditGroups", financialSecondAuditGroups);
		vars.put("remitMoneyUsers", remitMoneyUsers);
		vars.put("remitMoneyGroups", remitMoneyGroups);

		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.PAYMENT);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), paymentVo.getUserID());
		int index = -1;
		if (file != null && file.length > 0) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			int i = 0;
			for (Object o : fileDetailList) {
				index++;
				InputStream is = new FileInputStream(file[i]);
				JSONArray jArray = (JSONArray) o;
				String fileName = (String) jArray.get(0);
				if (StringUtils.isBlank(fileName))
					continue;
				String suffix = (String) jArray.get(1);
				if ("jpg".equals(suffix) || "jpeg".equals(suffix)
						|| "png".equals(suffix)) {
					taskService.createAttachment("picture", task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				} else {
					taskService.createAttachment(suffix, task.getId(),
							processInstance.getId(), fileName,
							jArray.get(2) + "_" + index, is);
				}
				i++;
			}
		}

		paymentVo.setTotalAmount((Double.valueOf(
				new DecimalFormat("0.00").format(paymentVo.getTotalAmount()))));
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录数据
		savePayment(paymentVo, processInstance.getId());
	}

	private void savePayment(PaymentVo paymentVo, String processInstanceID) {
		Date now = new Date();
		PaymentEntity paymentEntity = PaymentEntity.builder()
				.reimbursementNo(paymentVo.getReimbursementNo())
				.userID(paymentVo.getUserID())
				.requestUserID(paymentVo.getRequestUserID())
				.payeeID(paymentVo.getPayeeID())
				.invoiceTitle(paymentVo.getInvoiceTitleID())
				.invoiceNum(paymentVo.getInvoiceNum())
				.detailNum(paymentVo.getDetailNum())
				.totalAmount(paymentVo.getTotalAmount())
				.processInstanceID(processInstanceID)
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(now)
				.reternenceId(paymentVo.getReternenceId())
				.isHaveInvoice(paymentVo.getIsHaveInvoice())
				.reternenceMobile(paymentVo.getReternenceMobile())
				.reternenceName(paymentVo.getReternenceName())
				.showPerson2(paymentVo.getShowPerson2())
				.isFixedAsset(paymentVo.getIsFixedAsset())
				.fixedAssetNo(paymentVo.getFixedAssetNo()).updateTime(now)
				.moneyType(paymentVo.getMoneyType())
				.bankAccountId(paymentVo.getBankAccountId())
				.build();
		int reimbursementID = reimbursementDao.savePayment(paymentEntity);
		int useSize = paymentVo.getUsage().length;
		String[] usage = paymentVo.getUsage();
		Double[] amount = paymentVo.getAmount();
		for (int i = 0; i < useSize; ++i) {
			ReimbursementDetailEntity reimbursementDetailEntity = ReimbursementDetailEntity
					.builder().reimbursementID(reimbursementID)
					.purpose(usage[i]).amount(amount[i])
					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
					.addTime(now).updateTime(now).type("2").build();
			reimbursementDetailDao.save(reimbursementDetailEntity);
		}
		// 保存领款人打款账号
/*		BankAccountEntity bankAccount = bankAccountDao
				.getBankAccountByUserID(paymentVo.getPayeeID());
		if (bankAccount == null) {
			bankAccount = new BankAccountEntity();
			bankAccount.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			bankAccount.setAddTime(new Date());
		}
		bankAccount.setUserID(paymentVo.getPayeeID());
		bankAccount.setCardName(paymentVo.getCardName());
		bankAccount.setBank(paymentVo.getBank());
		bankAccount.setCardNumber(paymentVo.getCardNumber());
		bankAccount.setUpdateTime(new Date());
		bankAccountDao.save(bankAccount);*/
	}

	@Override
	public ListResult<PaymentVo> findPaymentListByUserID(String userID, Integer page, Integer limit) {
		List<PaymentEntity> paymentEntities = reimbursementDao
				.findPaymentsByUserID(userID, page, limit);
		List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
		for (PaymentEntity payment : paymentEntities) {
			PaymentVo paymentVo = new PaymentVo();
			paymentVo
			.setProcessInstanceID(payment.getProcessInstanceID());
			paymentVo.setReimbursementNo(payment.getReimbursementNo());
			paymentVo.setTotalAmount(payment.getTotalAmount());

			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(payment.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					PaymentVo arg = (PaymentVo) variable.getValue();
					paymentVo.setRequestDate(arg.getRequestDate());
					paymentVo.setTitle(arg.getTitle());
					//这边把领款人只能为公司内部人的限制取消了，用户id修改为姓名
					if(arg.getPayeeID().length()>25 && arg.getPayeeID().contains("-")){
						StaffVO payeeUser = staffService
								.getStaffByUserID(arg.getPayeeID());
						paymentVo
						.setPayeeName(payeeUser == null ? "" : payeeUser.getLastName());
					}else{
						paymentVo
						.setPayeeName(arg.getPayeeID());
					}
					//advanceVo.setPayeeName(arg.getPayeeName());
					paymentVo.setRequestUserName(arg.getRequestUserName());
					paymentVo.setIsHaveInvoice(arg.getIsHaveInvoice());
					paymentVo.setInvoiceTitle(arg.getInvoiceTitle());
					paymentVo.setInvoiceNum(arg.getInvoiceNum());
					paymentVo.setDetailNum(arg.getDetailNum());
					paymentVo.setCardName(arg.getCardName());
					paymentVo.setBank(arg.getBank());
					paymentVo.setCardNumber(arg.getCardNumber());
					paymentVo.setUsage(arg.getUsage());
					paymentVo.setReternenceName(arg.getReternenceName());
					paymentVo.setReternenceMobile(arg.getReternenceMobile());
					paymentVo.setIsFixedAsset(arg.getIsFixedAsset());
					paymentVo.setFixedAssetNo(arg.getFixedAssetNo());
					paymentVo.setAmount(arg.getAmount());
				}
			}

			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(payment.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				paymentVo.setStatus("处理中");
				runtimeService.createProcessInstanceQuery().list();

				try{
					Task task=taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
					if(task.getAssignee()==null){
						List<IdentityLink> idList =taskService.getIdentityLinksForTask(task.getId());
						String prevStr=processService
								.getProcessTaskAssignee(pInstance.getId());
						String[] nameArr=new String[idList.size()];
						int i=0;
						for (IdentityLink identityLink : idList) {
							nameArr[i++]=staffService.getRealNameByUserId(identityLink.getUserId());
						}
						paymentVo.setAssigneeUserName("【"+prevStr+"】"+StringUtils.join(nameArr,","));
					}else{
						paymentVo.setAssigneeUserName(processService
								.getProcessTaskAssignee(pInstance.getId()));
					}
				}catch(Exception e){
					paymentVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				}


			} else {
				paymentVo.setStatus(TaskResultEnum
						.valueOf(payment.getProcessStatus()).getName());
			}
			paymentVos.add(paymentVo);
		}
		int count = reimbursementDao.countPaymentByUserID(userID);
		return new ListResult<PaymentVo>(paymentVos, count);
	}

	@Override
	public List<PaymentTaskVO> createPaymentTaskVOListByTaskList(List<Task> paymentTask) {
		List<PaymentTaskVO> taskVOs = new ArrayList<PaymentTaskVO>();
		for (Task task : paymentTask) {
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId())
					.singleResult();
			// 查询流程参数
			PaymentVo arg = (PaymentVo) runtimeService
					.getVariable(pInstance.getId(), "arg");
			PaymentTaskVO reimbursementTaskVO = new PaymentTaskVO();
			reimbursementTaskVO.setProcessInstanceID(pInstance.getId());
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID(task.getId());
			reimbursementTaskVO.setTaskName(task.getName());
			reimbursementTaskVO.setTaskDefKey(task.getTaskDefinitionKey());
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo(arg.getReimbursementNo());
			reimbursementTaskVO.setTotalAmount(arg.getTotalAmount());
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	@Override
	public ListResult<PaymentTaskVO> findPaymentsByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups,
			List<String> users, String reimbursementNo, String beginDate, String endDate, int page, int limit) {
		String groupIDs = Arrays.toString(
				Lists2.transform(groups, new SafeFunction<Group, String>() {
					@Override
					protected String safeApply(Group input) {
						return "'" + input.getId() + "'";
					}
				}).toArray());
		String taskNames = Arrays.toString(Lists2
				.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
					@Override
					protected String safeApply(TaskDefKeyEnum input) {
						return "'" + input.getName() + "'";
					}
				}).toArray());
		String userIDs = Arrays.toString(
				Lists2.transform(users, new SafeFunction<String, String>() {
					@Override
					protected String safeApply(String input) {
						return "'" + input + "'";
					}
				}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Payment reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + ")) ";
		String whereSQL = generateWhereSQL(reimbursementNo, beginDate, endDate);
		sql += whereSQL;

		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<PaymentTaskVO> taskVOs = createPaymentTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Payment reimbursement "
				+ "where task.ID_ = identityLink.TASK_ID_ and task.PROC_INST_ID_ = reimbursement.ProcessInstanceID "
				+ "and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in ("
				+ taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in ("
				+ groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in ("
				+ userIDs.substring(1, userIDs.length() - 1) + "))";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<PaymentTaskVO>(taskVOs, count);
	}

	private List<PaymentTaskVO> createPaymentTaskList(List<Object> payments) {
		List<PaymentTaskVO> taskVOs = new ArrayList<>();
		for (Object task : payments) {
			Object[] objs = (Object[]) task;
			// 查询流程实例
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId((String) objs[1]).singleResult();
			// 查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(),
					"arg");
			PaymentTaskVO reimbursementTaskVO = new PaymentTaskVO();
			reimbursementTaskVO.setProcessInstanceID((String) objs[1]);
			reimbursementTaskVO.setRequestUserName(arg.getUserName());
			reimbursementTaskVO.setRequestDate(arg.getRequestDate());
			reimbursementTaskVO.setTaskID((String) objs[0]);
			reimbursementTaskVO.setTaskName((String) objs[2]);
			reimbursementTaskVO.setTaskDefKey((String) objs[3]);
			reimbursementTaskVO.setTitle(arg.getTitle());
			reimbursementTaskVO.setReimbursementNo((String) objs[4]);
			reimbursementTaskVO.setTotalAmount((Double) objs[5]);
			taskVOs.add(reimbursementTaskVO);
		}
		return taskVOs;
	}

	@Override
	public PaymentVo getPaymentVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (PaymentVo) runtimeService.getVariable(pInstance.getId(), "arg");
	}

	@Override
	public PaymentVo gePaymentTaskVOByProcessInstanceID(String processInstanceID) {
		PaymentEntity reimbursementEntity = reimbursementDao
				.getPaymentByProcessInstanceID(processInstanceID);
		PaymentVo reimbursementVO = PaymentVOTransformer.INSTANCE
				.apply(reimbursementEntity);
		StaffVO requestUser = staffService
				.getStaffByUserID(reimbursementVO.getRequestUserID());
		//这边把领款人只能为公司内部人的限制取消了，用户id修改为姓名
		if(reimbursementVO.getPayeeID().length()>25 && reimbursementVO.getPayeeID().contains("-")){
			StaffVO payeeUser = staffService
					.getStaffByUserID(reimbursementVO.getPayeeID());
			reimbursementVO
			.setPayeeName(payeeUser == null ? "" : payeeUser.getLastName());
		}else{
			reimbursementVO
			.setPayeeName(reimbursementVO.getPayeeID());
		}
		reimbursementVO.setRequestUserName(
				requestUser == null ? "" : requestUser.getLastName());

		List<ReimbursementDetailEntity> detailEntities = reimbursementDetailDao
				.findReimbursementDetailsByReimbursementID(
						reimbursementVO.getReimbursementID(), 2);
		int size = detailEntities.size();
		String[] usages = new String[size];
		Double[] amounts = new Double[size];
		for (int i = 0; i < size; ++i) {
			ReimbursementDetailEntity detailEntity = detailEntities.get(i);
			usages[i] = detailEntity.getPurpose();
			amounts[i] = detailEntity.getAmount();
		}
		reimbursementVO.setUsage(usages);
		reimbursementVO.setAmount(amounts);

		return reimbursementVO;
	}

	@Override
	public void updatePaymentBankAccountByUserID(String userID, PaymentVo paymentVo) {
		BankAccountEntity bankAccountEntity = bankAccountDao
				.getBankAccountByUserID(userID);
		if (bankAccountEntity == null) {
			throw new RuntimeException("该用户的打款账号不存在！");
		}

		bankAccountEntity.setCardName(paymentVo.getCardName());
		bankAccountEntity.setBank(paymentVo.getBank());
		bankAccountEntity.setCardNumber(paymentVo.getCardNumber());
		bankAccountEntity.setUpdateTime(new Date());
		bankAccountDao.save(bankAccountEntity);
	}

	@Override
	public void updatePaymentProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		reimbursementDao.updatePaymentProcessStatusByProcessInstanceID(
				processInstanceID, taskResult.getValue());
	}

	@Override
	public void setPaymentFinancialFirstAuditName(String instanceId, String name, int type) {
		reimbursementDao.setPaymentFinancialFirstAuditName(instanceId, name,
				type);
	}

	@Override
	public ListResult<PaymentVo> findPaymentList(ReimbursementVO reimbursementVO, Integer page, Integer limit) {
		List<Object> list = baseDao.findPageList(
				getPaymentListBySql(reimbursementVO), page, limit);
		List<PaymentVo> reimbursementVOs = new ArrayList<>();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			PaymentVo reimbursementVO2 = new PaymentVo();
			reimbursementVO2.setReimbursementNo((String) objs[0]);
			reimbursementVO2.setRequestUserName(staffService
					.getStaffByUserID((String) objs[1]).getLastName());
			reimbursementVO2.setTotalAmount((Double) objs[2]);
			reimbursementVO2.setRequestDate(objs[3] == null
					? ""
							: DateUtil.formateFullDate((Date) objs[3]));
			reimbursementVO2.setProcessInstanceID((String) objs[4]);
			reimbursementVOs.add(reimbursementVO2);
		}
		Object countObj = baseDao
				.getUniqueResult(getCountPaymentBySql(reimbursementVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<PaymentVo>(reimbursementVOs, count);
	}

	private String getCountPaymentBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select count(*) from OA_Payment reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();
	}

	private String getPaymentListBySql(ReimbursementVO reimbursementVO) {
		StringBuffer sql = new StringBuffer(
				"select reimbursement.ReimbursementNo,reimbursement.RequestUserID, "
						+ "reimbursement.TotalAmount,reimbursement.AddTime, reimbursement.ProcessInstanceID from OA_Payment reimbursement,OA_Staff staff "
						+ "where reimbursement.RequestUserID = staff.UserID and reimbursement.IsDeleted = 0 and staff.IsDeleted = 0 and reimbursement.processStatus= 11");
		sql.append(getWhereBySql(reimbursementVO));
		return sql.toString();
	}

	@Override
	public ListResult<PaymentTaskVO> findPaymentsAll(String reimbursementNo, String beginDate, String endDate,
			Integer page, Integer limit) {
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_, reimbursement.ReimbursementNo, reimbursement.TotalAmount "
				+ "from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Payment reimbursement " +
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID";
		String whereSQL = generateWhereSQL(reimbursementNo, beginDate, endDate);
		sql += whereSQL;
		sql += " order by reimbursement.addTime desc";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<PaymentTaskVO> taskVOs = createPaymentTaskList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink, OA_Payment reimbursement " +
				"WHERE\n" +
				"	((task.ID_ = identityLink.TASK_ID_ AND identityLink.TYPE_ = 'candidate') or task.ASSIGNEE_ is not null)\n" +
				"AND task.PROC_INST_ID_ = reimbursement.ProcessInstanceID";
		sql += whereSQL;
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<PaymentTaskVO>(taskVOs, count);
	}

	@Override
	public void updateAdvanceInvoiceIds(String invoiceIds, String pInstanceId) {
		String sql = "update OA_Advance set invoiceAttaIds='"+invoiceIds+"' where processInstanceID="+pInstanceId;
		baseDao.excuteSql(sql);
	}

	@Override
	public AdvanceEntity getAdvanceEntityByPInstanceId(String pInstanceId) {
		return reimbursementDao
				.getAdvanceByProcessInstanceID(pInstanceId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankAccountEntity> getBankAccountByPayeeName(String lastName) {
		String hql = "from BankAccountEntity where userID='"+lastName+"' and isDeleted=0";
		return (List<BankAccountEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public int saveBankAccount(BankAccountEntity bankAccountVo) {
		bankAccountVo.setIsDeleted(0);
		bankAccountVo.setAddTime(new Date());
		return baseDao.hqlSave(bankAccountVo);
	}

	@Override
	public BankAccountEntity getBankAccountById(Integer accountID) {
		String hql = "from BankAccountEntity where accountID="+accountID;
		return (BankAccountEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void updateBankAccount(BankAccountEntity bankAccountVo) {
		bankAccountVo.setIsDeleted(0);
		baseDao.hqlUpdate(bankAccountVo);
	}

	@Override
	public boolean checkBankAccountExist(BankAccountEntity bankAccountVo) {
		String sql = "select count(*) from OA_BankAccount where cardName=:cardName and bank=:bank and cardNumber=:cardNumber and userId=:userId";
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("cardName", bankAccountVo.getCardName());
		query.setParameter("bank", bankAccountVo.getBank());
		query.setParameter("cardNumber", bankAccountVo.getCardNumber());
		query.setParameter("userId", bankAccountVo.getUserID());
		int count = Integer.parseInt(query.uniqueResult()+"");
		if(count>0){
			return true;
		}
		return false;
	}
	
	//报销单管理
	@Override
	public ListResult<ReimbursementVO> getReimbursementVOList(ReimbursementVO reimbursementVO,Integer page,Integer limit) {
		
		List<Object> objects = baseDao.findPageList(sqlGetReimbursementVOList(reimbursementVO),page,limit);
		List<ReimbursementVO> reimbursementVOs = new ArrayList<ReimbursementVO>();
		for(Object object: objects){
			ReimbursementVO reimbursementVo = new ReimbursementVO();
			Object[] objs = (Object[]) object;
			reimbursementVo.setRequestUserName((String) objs[0]);
			reimbursementVo.setWorkingState((String) objs[1]);
			reimbursementVo.setReimbursementNo((String) objs[2]);
			reimbursementVo.setTotalAmount((Double) objs[3]);
			reimbursementVo.setAddTime((Date) objs[4]);
			reimbursementVo.setStatus((String) objs[5]);
			reimbursementVo.setThecurrenLink((String) objs[6]);
			reimbursementVo.setProcessInstanceID((String) objs[7]);
			reimbursementVo.setAssigneeUserName((String) objs[8]);
			reimbursementVo.setCandidateUsers((String) objs[9]);
			reimbursementVOs.add(reimbursementVo);
		}
		Object countObj = baseDao.getUniqueResult(sqlCountReimbursementVOList(reimbursementVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<ReimbursementVO>(reimbursementVOs, count);
	}
	
	private String sqlGetReimbursementVOList(ReimbursementVO reimbursementVO){
		StringBuffer sql = new StringBuffer("SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ IS NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END AS workingState,\n" +
				" OA_Reimbursement.ReimbursementNo,\n" +
				" OA_Reimbursement.TotalAmount,\n" +
				" OA_Reimbursement.AddTime,\n" +
				" CASE\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 1\n" +
				"OR OA_Reimbursement.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 31 THEN\n" +
				"	'流程作废'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" act_ru_task.NAME_,\n" +
				" OA_Reimbursement.ProcessInstanceID,\n" +
				" b.StaffName AS assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	OA_Reimbursement\n" +
				"LEFT JOIN act_ru_task ON OA_Reimbursement.ProcessInstanceID = act_ru_task.PROC_INST_ID_\n" +
				"LEFT JOIN oa_staff a ON OA_Reimbursement.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_id_user ON OA_Reimbursement.RequestUserID = act_id_user.ID_\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_identitylink.TASK_ID_ = act_ru_task.ID_\n" +
				"LEFT JOIN oa_staff c ON act_ru_identitylink.USER_ID_ = c.UserID\n" +
				"WHERE\n" +
				"	OA_Reimbursement.IsDeleted = 0\n");
				
		sql.append(sqlQueryConditionForReimbursementVOList(reimbursementVO));		
		sql.append(" GROUP BY\n" +
				"	OA_Reimbursement.ProcessInstanceID\n");
		if(StringUtils.isNotBlank(reimbursementVO.getAssigneeUserName())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
					reimbursementVO.getAssigneeUserName()+
					"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
					reimbursementVO.getAssigneeUserName()+"%')");
		}
		sql.append(" ORDER BY\n" +
				"	OA_Reimbursement.AddTime DESC");
		return sql.toString();
	}
	
	private String sqlCountReimbursementVOList(ReimbursementVO reimbursementVO){
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM (SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ IS NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END AS workingState,\n" +
				" OA_Reimbursement.ReimbursementNo,\n" +
				" OA_Reimbursement.TotalAmount,\n" +
				" OA_Reimbursement.AddTime,\n" +
				" CASE\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 1\n" +
				"OR OA_Reimbursement.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN OA_Reimbursement.ProcessStatus = 31 THEN\n" +
				"	'流程作废'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" act_ru_task.NAME_,\n" +
				" OA_Reimbursement.ProcessInstanceID,\n" +
				" b.StaffName AS assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	OA_Reimbursement\n" +
				"LEFT JOIN act_ru_task ON OA_Reimbursement.ProcessInstanceID = act_ru_task.PROC_INST_ID_\n" +
				"LEFT JOIN oa_staff a ON OA_Reimbursement.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_id_user ON OA_Reimbursement.RequestUserID = act_id_user.ID_\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_identitylink.TASK_ID_ = act_ru_task.ID_\n" +
				"LEFT JOIN oa_staff c ON act_ru_identitylink.USER_ID_ = c.UserID\n" +
				"WHERE\n" +
				"	OA_Reimbursement.IsDeleted = 0\n");
				
		sql.append(sqlQueryConditionForReimbursementVOList(reimbursementVO));		
		sql.append(" GROUP BY\n" +
				"	OA_Reimbursement.ProcessInstanceID\n");
		if(StringUtils.isNotBlank(reimbursementVO.getAssigneeUserName())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
					reimbursementVO.getAssigneeUserName()+
					"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
					reimbursementVO.getAssigneeUserName()+"%')");
		}
		sql.append(" ORDER BY\n" +
				"	OA_Reimbursement.AddTime DESC )d");
		return sql.toString();
	}
	private String sqlQueryConditionForReimbursementVOList(ReimbursementVO reimbursementVO) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(reimbursementVO.getRequestUserName())) {
			whereSql.append(" and a.StaffName like '%"
					+ reimbursementVO.getRequestUserName() + "%' ");
		}
		if (!StringUtils.isBlank(reimbursementVO.getBeginDate())) {
			whereSql.append(" and OA_Reimbursement.AddTime >= '"
					+ reimbursementVO.getBeginDate() + "'");
		}
		if (!StringUtils.isBlank(reimbursementVO.getEndDate())) {
			whereSql.append(" and OA_Reimbursement.AddTime <= '"
					+ reimbursementVO.getEndDate() + "'");
		}
		if (!StringUtils.isBlank(reimbursementVO.getReimbursementNo())) {
			whereSql.append(" and OA_Reimbursement.ReimbursementNo like '%"
					+ reimbursementVO.getReimbursementNo() + "%' ");
		}
		if(StringUtils.isBlank(reimbursementVO.getStatus())){
			whereSql.append(" and (CASE\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 2 THEN\n" +
					"	'未通过完结'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 1 or OA_Reimbursement.ProcessStatus is null THEN\n" +
					"	'进行中'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 11 THEN\n" +
					"	'打款成功'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 31 THEN\n" +
					"	'流程作废'\n" +
					"ELSE\n" +
					"	'其他情况'\n" +
					"END) ='进行中'\n");
		}else if (!reimbursementVO.getStatus().equals("请选择") && reimbursementVO.getStatus() != null){
			whereSql.append(" and (CASE\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 2 THEN\n" +
					"	'未通过完结'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 1 or OA_Reimbursement.ProcessStatus is null THEN\n" +
					"	'进行中'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 11 THEN\n" +
					"	'打款成功'\n" +
					"WHEN OA_Reimbursement.ProcessStatus = 31 THEN\n" +
					"	'流程作废'\n" +
					"ELSE\n" +
					"	'其他情况'\n" +
					"END) ='"+ reimbursementVO.getStatus()+"'\n");
		}
		if(StringUtils.isNotBlank(reimbursementVO.getThecurrenLink())){
			whereSql.append(" AND act_ru_task.NAME_ = '"+reimbursementVO.getThecurrenLink()+"'");
		}
		return whereSql.toString();
	}
	
	
	
	//预付单管理
	@Override
	public ListResult<AdvanceVo> getAdvanceVoList(AdvanceVo advanceVO, Integer page, Integer limit) {
		List<Object> objects = baseDao.findPageList(sqlGetAdvanceList(advanceVO), page, limit);
		List<AdvanceVo> advanceVoS = new ArrayList<>();
		for(Object object:objects){
			Object[] objs = (Object[]) object;
			AdvanceVo advanceVo = new AdvanceVo();
			advanceVo.setRequestUserName((String) objs[0]);
			advanceVo.setWorkingState((String) objs[1]);
			advanceVo.setReimbursementNo((String) objs[2]);
			advanceVo.setTotalAmount((Double) objs[3]);
			advanceVo.setAddTime((Date) objs[4]);
			advanceVo.setStatus((String) objs[5]);
			advanceVo.setThecurrenLink((String) objs[6]);
			advanceVo.setProcessInstanceID((String) objs[7]);
			advanceVo.setAssigneeUserName((String) objs[8]);
			advanceVo.setCandidateUsers((String) objs[9]);
			advanceVoS.add(advanceVo);
		}
		Object countObj = baseDao.getUniqueResult(sqlCountAdvanceList(advanceVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<AdvanceVo>(advanceVoS,count);
	}
	private String sqlGetAdvanceList(AdvanceVo advanceVo){
		StringBuffer sql=new StringBuffer("SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ is NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END,\n" +
				" oa_advance.ReimbursementNo,\n" +
				" oa_advance.TotalAmount,\n" +
				" oa_advance.AddTime,\n" +
				" CASE\n" +
				"WHEN oa_advance.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN oa_advance.ProcessStatus = 1\n" +
				"OR oa_advance.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN oa_advance.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN oa_advance.ProcessStatus = 31 THEN\n" +
				"	'流程强制中断'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" act_ru_task.NAME_,\n" +
				" oa_advance.ProcessInstanceID,\n" +
				" b.StaffName AS assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	oa_advance\n" +
				"LEFT JOIN act_id_user ON oa_advance.UserID = act_id_user.ID_\n" +
				"LEFT JOIN oa_staff a ON oa_advance.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_ru_task ON oa_advance.ProcessInstanceID = act_ru_task.PROC_INST_ID_\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_identitylink.TASK_ID_ = act_ru_task.ID_\n" +
				"LEFT JOIN oa_staff c ON act_ru_identitylink.USER_ID_ = c.UserID\n" +
				"WHERE\n" +
				"	oa_advance.IsDeleted = 0\n");
				
		sql.append(sqlQueryConditionForGetAdvanceList(advanceVo));
		
		sql.append(" GROUP BY\n" +
				"	oa_advance.ProcessInstanceID\n");
		if(StringUtils.isNotBlank(advanceVo.getAssigneeUserName())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
			advanceVo.getAssigneeUserName()+
			"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
			advanceVo.getAssigneeUserName()+"%')");
		}
		sql.append(" ORDER BY\n" +
				"	oa_advance.AddTime DESC");
		
		return sql.toString();
	}
	private String sqlQueryConditionForGetAdvanceList(AdvanceVo advanceVo){
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(advanceVo.getReimbursementNo())){
			sql.append(" and oa_advance.ReimbursementNo like '%"+advanceVo.getReimbursementNo()+"%'");
		}
		if(StringUtils.isNotBlank(advanceVo.getRequestUserName())){
			sql.append(" and a.StaffName like '%"+advanceVo.getRequestUserName()+"%'");
		}
		if (!StringUtils.isBlank(advanceVo.getBeginDate())) {
			sql.append(" and oa_advance.AddTime >= '"
					+ advanceVo.getBeginDate() + "'");
		}
		if (!StringUtils.isBlank(advanceVo.getEndDate())) {
			sql.append(" and oa_advance.AddTime <= '"
					+ advanceVo.getEndDate() + "'");
		}
		if(StringUtils.isBlank(advanceVo.getStatus())){
			sql.append(" and (CASE\n" +
					"WHEN oa_advance.ProcessStatus = 2 THEN\n" +
					"	'未通过完结'\n" +
					"WHEN oa_advance.ProcessStatus = 1\n" +
					"OR oa_advance.ProcessStatus IS NULL THEN\n" +
					"	'进行中'\n" +
					"WHEN oa_advance.ProcessStatus = 11 THEN\n" +
					"	'打款成功'\n" +
					"WHEN oa_advance.ProcessStatus = 31 THEN\n" +
					"	'流程作废'\n" +
					"ELSE\n" +
					"	'其他情况'\n" +
					"END) = '进行中'\n");
		}else if(!advanceVo.getStatus().equals("请选择") && advanceVo.getStatus()!=null){
			sql.append(" and (CASE\n" +
					"WHEN oa_advance.ProcessStatus = 2 THEN\n" +
					"	'未通过完结'\n" +
					"WHEN oa_advance.ProcessStatus = 1\n" +
					"OR oa_advance.ProcessStatus IS NULL THEN\n" +
					"	'进行中'\n" +
					"WHEN oa_advance.ProcessStatus = 11 THEN\n" +
					"	'打款成功'\n" +
					"WHEN oa_advance.ProcessStatus = 31 THEN\n" +
					"	'流程作废'\n" +
					"ELSE\n" +
					"	'其他情况'\n" +
					"END) = '"+advanceVo.getStatus()+"'\n");
		}
		if(StringUtils.isNotBlank(advanceVo.getThecurrenLink())){
			sql.append(" and act_ru_task.NAME_ = '"+advanceVo.getThecurrenLink()+"'");
		}
		return sql.toString();
	}
	private String sqlCountAdvanceList(AdvanceVo advanceVo) {
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) FROM (SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ is NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END,\n" +
				" oa_advance.ReimbursementNo,\n" +
				" oa_advance.TotalAmount,\n" +
				" oa_advance.AddTime,\n" +
				" CASE\n" +
				"WHEN oa_advance.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN oa_advance.ProcessStatus = 1\n" +
				"OR oa_advance.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN oa_advance.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN oa_advance.ProcessStatus = 31 THEN\n" +
				"	'流程强制中断'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" act_ru_task.NAME_,\n" +
				" oa_advance.ProcessInstanceID,\n" +
				" b.StaffName AS assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	oa_advance\n" +
				"LEFT JOIN act_id_user ON oa_advance.UserID = act_id_user.ID_\n" +
				"LEFT JOIN oa_staff a ON oa_advance.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_ru_task ON oa_advance.ProcessInstanceID = act_ru_task.PROC_INST_ID_\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_identitylink.TASK_ID_ = act_ru_task.ID_\n" +
				"LEFT JOIN oa_staff c ON act_ru_identitylink.USER_ID_ = c.UserID\n" +
				"WHERE\n" +
				"	oa_advance.IsDeleted = 0\n");
				
		sql.append(sqlQueryConditionForGetAdvanceList(advanceVo));
		
		sql.append(" GROUP BY\n" +
				"	oa_advance.ProcessInstanceID\n");
		if(StringUtils.isNotBlank(advanceVo.getCandidateUsers())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
			advanceVo.getCandidateUsers()+
			"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
			advanceVo.getCandidateUsers()+"%')");
		}
		sql.append(" ORDER BY\n" +
				"	oa_advance.AddTime DESC )d");
		
		return sql.toString();
	}
	
	
	
	//付款单管理	
	@Override
	public ListResult<PaymentVo> getPaymentVoList(PaymentVo paymentVo, Integer page, Integer limit) {
		List<Object> objects = baseDao.findPageList(sqlGetPaymentVoList(paymentVo), page, limit);
		List<PaymentVo> paymentVos = new ArrayList<>();
		for(Object object:objects){
			Object[] objs = (Object[]) object;
			PaymentVo paymentVO = new PaymentVo();
			paymentVO.setRequestUserName((String) objs[0]);
			paymentVO.setWorkingState((String) objs[1]);
			paymentVO.setReimbursementNo((String) objs[2]);
			paymentVO.setTotalAmount((Double) objs[3]);
			paymentVO.setAddTime((Date) objs[4]);
			paymentVO.setStatus((String) objs[5]);
			paymentVO.setProcessInstanceID((String) objs[6]);
			paymentVO.setThecurrenLink((String) objs[7]);
			paymentVO.setAssigneeUserName((String) objs[8]);
			paymentVO.setCandidateUsers((String) objs[9]);
			paymentVos.add(paymentVO);
		}
		Object countObj = baseDao.getUniqueResult(sqlCountPaymentVoList(paymentVo));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<PaymentVo>(paymentVos,count);
	}
	
	
	private String sqlGetPaymentVoList(PaymentVo paymentVo){
		StringBuffer sql = new StringBuffer("SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ IS NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END,\n" +
				" oa_payment.ReimbursementNo,\n" +
				" oa_payment.TotalAmount,\n" +
				" oa_payment.AddTime,\n" +
				" CASE\n" +
				"WHEN oa_payment.ProcessStatus = 1\n" +
				"OR oa_payment.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN oa_payment.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN oa_payment.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN oa_payment.ProcessStatus = 11 THEN\n" +
				"	'流程作废'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" oa_payment.ProcessInstanceID,\n" +
				" act_ru_task.NAME_,\n" +
				" b.StaffName assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	oa_payment\n" +
				"LEFT JOIN oa_staff a ON oa_payment.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_id_user ON oa_payment.RequestUserID = act_id_user.ID_\n" +
				"LEFT JOIN act_ru_task ON act_ru_task.PROC_INST_ID_ = oa_payment.ProcessInstanceID\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_task.ID_ = act_ru_identitylink.TASK_ID_\n" +
				"LEFT JOIN oa_staff c ON c.UserID = act_ru_identitylink.USER_ID_\n" +
				"WHERE\n" +
				"	oa_payment.IsDeleted = 0\n");
		sql.append(sqlQueryConditionForGetPaymentVoList(paymentVo));		
				
		sql.append(" GROUP BY\n" +
				"	oa_payment.ProcessInstanceID\n");
		
		if(StringUtils.isNotBlank(paymentVo.getAssigneeUserName())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
					paymentVo.getAssigneeUserName()+
					"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
					paymentVo.getAssigneeUserName()+"%') ");
		}
		
		sql.append("ORDER BY\n" +
				"	oa_payment.AddTime DESC");
		return sql.toString();
	}
	private String sqlQueryConditionForGetPaymentVoList(PaymentVo paymentVo){
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(paymentVo.getReimbursementNo())){
			sql.append(" and oa_payment.ReimbursementNo like '%"+paymentVo.getReimbursementNo()+"%'");
		}
		if(StringUtils.isNotBlank(paymentVo.getRequestUserName())){
			sql.append(" and a.StaffName like '%"+paymentVo.getRequestUserName()+"%'");
		}
		if(StringUtils.isBlank(paymentVo.getStatus())){
			sql.append(" and (CASE\n" +
					"	WHEN oa_payment.ProcessStatus = 1\n" +
					"	OR oa_payment.ProcessStatus IS NULL THEN\n" +
					"		'进行中'\n" +
					"	WHEN oa_payment.ProcessStatus = 2 THEN\n" +
					"		'未通过完结'\n" +
					"	WHEN oa_payment.ProcessStatus = 11 THEN\n" +
					"		'打款成功'\n" +
					"	WHEN oa_payment.ProcessStatus = 11 THEN\n" +
					"		'流程作废'\n" +
					"	ELSE\n" +
					"		'其他情况'\n" +
					"	END) = '进行中'");
		}else if(!paymentVo.getStatus().equals("请选择") && paymentVo.getStatus()!=null){
			sql.append(" and (CASE\n" +
					"	WHEN oa_payment.ProcessStatus = 1\n" +
					"	OR oa_payment.ProcessStatus IS NULL THEN\n" +
					"		'进行中'\n" +
					"	WHEN oa_payment.ProcessStatus = 2 THEN\n" +
					"		'未通过完结'\n" +
					"	WHEN oa_payment.ProcessStatus = 11 THEN\n" +
					"		'打款成功'\n" +
					"	WHEN oa_payment.ProcessStatus = 11 THEN\n" +
					"		'流程作废'\n" +
					"	ELSE\n" +
					"		'其他情况'\n" +
					"	END) = '"+paymentVo.getStatus()+"'");
		}
		
		if (StringUtils.isNotBlank(paymentVo.getBeginDate())) {
			sql.append(" and oa_payment.AddTime >= '"+ paymentVo.getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(paymentVo.getEndDate())) {
			sql.append(" and oa_payment.AddTime <= '"+ paymentVo.getEndDate() + "'");
		}
		if (StringUtils.isNotBlank(paymentVo.getThecurrenLink())){
			sql.append(" and act_ru_task.NAME_ = '"+paymentVo.getThecurrenLink()+"'");
		}
		return sql.toString();
	}
	private String sqlCountPaymentVoList(PaymentVo paymentVo){
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM (SELECT\n" +
				"	a.StaffName requestUserName,\n" +
				"	CASE\n" +
				"WHEN act_id_user.LAST_ IS NULL THEN\n" +
				"	'离职'\n" +
				"ELSE\n" +
				"	'在职'\n" +
				"END,\n" +
				" oa_payment.ReimbursementNo,\n" +
				" oa_payment.TotalAmount,\n" +
				" oa_payment.AddTime,\n" +
				" CASE\n" +
				"WHEN oa_payment.ProcessStatus = 1\n" +
				"OR oa_payment.ProcessStatus IS NULL THEN\n" +
				"	'进行中'\n" +
				"WHEN oa_payment.ProcessStatus = 2 THEN\n" +
				"	'未通过完结'\n" +
				"WHEN oa_payment.ProcessStatus = 11 THEN\n" +
				"	'打款成功'\n" +
				"WHEN oa_payment.ProcessStatus = 11 THEN\n" +
				"	'流程作废'\n" +
				"ELSE\n" +
				"	'其他情况'\n" +
				"END,\n" +
				" oa_payment.ProcessInstanceID,\n" +
				" act_ru_task.NAME_,\n" +
				" b.StaffName assigneeUserName,\n" +
				" GROUP_CONCAT(c.StaffName)\n" +
				"FROM\n" +
				"	oa_payment\n" +
				"LEFT JOIN oa_staff a ON oa_payment.RequestUserID = a.UserID\n" +
				"LEFT JOIN act_id_user ON oa_payment.RequestUserID = act_id_user.ID_\n" +
				"LEFT JOIN act_ru_task ON act_ru_task.PROC_INST_ID_ = oa_payment.ProcessInstanceID\n" +
				"LEFT JOIN oa_staff b ON act_ru_task.ASSIGNEE_ = b.UserID\n" +
				"LEFT JOIN act_ru_identitylink ON act_ru_task.ID_ = act_ru_identitylink.TASK_ID_\n" +
				"LEFT JOIN oa_staff c ON c.UserID = act_ru_identitylink.USER_ID_\n" +
				"WHERE\n" +
				"	oa_payment.IsDeleted = 0\n");
		sql.append(sqlQueryConditionForGetPaymentVoList(paymentVo));		
				
		sql.append(" GROUP BY\n" +
				"	oa_payment.ProcessInstanceID\n");
		
		if(StringUtils.isNotBlank(paymentVo.getAssigneeUserName())){
			sql.append(" HAVING (b.StaffName LIKE '%"+
					paymentVo.getAssigneeUserName()+
					"%' OR GROUP_CONCAT(c.StaffName) LIKE '%"+
					paymentVo.getAssigneeUserName()+"%') ");
		}
		
		sql.append("ORDER BY\n" +
				"	oa_payment.AddTime DESC )d");
		return sql.toString();
	}

	
}
