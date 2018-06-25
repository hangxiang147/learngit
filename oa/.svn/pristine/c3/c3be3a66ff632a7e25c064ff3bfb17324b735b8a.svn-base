package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.HousingFundDao;
import com.zhizaolian.staff.dao.SocialSecurityDao;
import com.zhizaolian.staff.dao.SocialSecurityProcessDao;
import com.zhizaolian.staff.entity.HousingFundEntity;
import com.zhizaolian.staff.entity.SocialSecurityEntity;
import com.zhizaolian.staff.entity.SocialSecurityProcessEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.HousingFundVOTransformer;
import com.zhizaolian.staff.transformer.SocialSecurityProcessVOTransformer;
import com.zhizaolian.staff.transformer.SocialSecurityVOTransformer;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;

public class SocialSecurityServiceImpl implements SocialSecurityService {
	
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private SocialSecurityDao socialSecurityDao;
	@Autowired
	private SocialSecurityProcessDao socialSecurityProcessDao;
	@Autowired
	private HousingFundDao housingFundDao;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	
	@Override
	public int save(HousingFundVO housingFundVO) {
		if (housingFundVO == null) {
			throw new RuntimeException("社保缴纳详细信息获取失败！");
		}
		
		Date now = new Date();
		HousingFundEntity housingFundEntity = housingFundVO.getHfID()!=null?housingFundDao.getHousingFundByID(housingFundVO.getHfID()):null;
		if (housingFundEntity == null) {
			housingFundEntity = new HousingFundEntity();
			housingFundEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			housingFundEntity.setAddTime(now);
		}
		
		housingFundEntity.setProcessID(housingFundVO.getProcessID());
		housingFundEntity.setUserID(housingFundVO.getUserID());
		housingFundEntity.setPaymentYear(housingFundVO.getPaymentYear());
		housingFundEntity.setPaymentMonth(housingFundVO.getPaymentMonth());
		housingFundEntity.setCompanyID(housingFundVO.getCompanyID());
		if (!StringUtils.isBlank(housingFundVO.getEntryDate())) {
			housingFundEntity.setEntryDate(DateUtil.getSimpleDate(housingFundVO.getEntryDate()));
		}
		if (!StringUtils.isBlank(housingFundVO.getFormalDate())) {
			housingFundEntity.setFormalDate(DateUtil.getSimpleDate(housingFundVO.getFormalDate()));
		}
		housingFundEntity.setGender(housingFundVO.getGender());
		housingFundEntity.setIdType(housingFundVO.getIdType());
		housingFundEntity.setIdNumber(housingFundVO.getIdNumber());
		housingFundEntity.setHasPaid(housingFundVO.getHasPaid());
		housingFundEntity.setCompanyCount(housingFundVO.getCompanyCount());
		housingFundEntity.setPersonalCount(housingFundVO.getPersonalCount());
		housingFundEntity.setTotalCount(housingFundVO.getTotalCount());
		housingFundEntity.setNote(housingFundVO.getNote());
		housingFundEntity.setUpdateTime(now);
		housingFundDao.save(housingFundEntity);
		return housingFundEntity.getHfID();
	}
	
	@Override
	public int save(SocialSecurityVO socialSecurityVO) {
		if (socialSecurityVO == null) {
			throw new RuntimeException("公积金缴纳详细信息获取失败！");
		}
		
		Date now = new Date();
		SocialSecurityEntity securityEntity = socialSecurityVO.getSsID()!=null?socialSecurityDao.getSocialSecurityByID(socialSecurityVO.getSsID()):null;
		if (securityEntity == null) {
			securityEntity = new SocialSecurityEntity();
			securityEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			securityEntity.setAddTime(now);
		}
		
		securityEntity.setProcessID(socialSecurityVO.getProcessID());
		securityEntity.setUserID(socialSecurityVO.getUserID());
		securityEntity.setPaymentYear(socialSecurityVO.getPaymentYear());
		securityEntity.setPaymentMonth(socialSecurityVO.getPaymentMonth());
		securityEntity.setCompanyID(socialSecurityVO.getCompanyID());
		securityEntity.setIdType(socialSecurityVO.getIdType());
		securityEntity.setIdNumber(socialSecurityVO.getIdNumber());
		securityEntity.setBasePay(socialSecurityVO.getBasePay());
		securityEntity.setSelfPaidRatio(socialSecurityVO.getSelfPaidRatio());
		securityEntity.setReason(socialSecurityVO.getReason());
		securityEntity.setPersonalProvidentFund(socialSecurityVO.getPersonalProvidentFund());
		securityEntity.setCompanyProvidentFund(socialSecurityVO.getCompanyProvidentFund());
		securityEntity.setTotalProvidentFund(socialSecurityVO.getPersonalProvidentFund()+socialSecurityVO.getCompanyProvidentFund());
		securityEntity.setUpdateTime(now);
		
		socialSecurityDao.save(securityEntity);
		return securityEntity.getSsID();
	}
	
	@Override
	public List<HousingFundVO> findHousingFundListByTime(int year, int month, int companyID) {
		String sql = "SELECT DISTINCT hf.*, staff.StaffName FROM OA_HousingFund hf, OA_Staff staff "
				+ "WHERE hf.UserID = staff.UserID AND staff.IsDeleted = 0 AND staff.`Status` != 4 "
				+ "AND hf.IsDeleted = 0 AND hf.CompanyID = "+companyID+" AND hf.PaymentYear = "+year
				+ " AND hf.PaymentMonth = "+month+" AND hf.ProcessID IS NULL ORDER BY hf.AddTime DESC";
		List<Object> result = baseDao.findBySql(sql);
		List<HousingFundVO> housingFundVOs = new ArrayList<HousingFundVO>();
		for (Object obj : result) {
			Object[] objs = (Object[]) obj;
			HousingFundVO housingFundVO = new HousingFundVO();
			housingFundVO.setHfID((Integer) objs[0]);
			housingFundVO.setUserID((String) objs[2]);
			housingFundVO.setPaymentYear((Integer) objs[3]);
			housingFundVO.setPaymentMonth((Integer) objs[4]);
			housingFundVO.setCompanyID((Integer) objs[5]);
			housingFundVO.setEntryDate(objs[6]==null?"":DateUtil.formateDate((Date) objs[6]));
			housingFundVO.setFormalDate(objs[7]==null?"":DateUtil.formateDate((Date) objs[7]));
			housingFundVO.setGender(Integer.parseInt(objs[8].toString()));
			housingFundVO.setIdType(Integer.parseInt(objs[9].toString()));
			housingFundVO.setIdNumber((String) objs[10]);
			housingFundVO.setHasPaid(Integer.parseInt(objs[11].toString()));
			housingFundVO.setCompanyCount((Double) objs[12]);
			housingFundVO.setPersonalCount((Double) objs[13]);
			housingFundVO.setTotalCount((Double) objs[14]);
			housingFundVO.setNote((String) objs[15]);
			housingFundVO.setUserName((String) objs[19]);
			housingFundVOs.add(housingFundVO);
		}
		return housingFundVOs;
	}
	
	@Override
	public List<SocialSecurityVO> findSocialSecurityListByTime(int year, int month, int companyID) {
		String sql = "SELECT DISTINCT ss.*, staff.StaffName FROM OA_SocialSecurity ss, OA_Staff staff "
				+ "WHERE ss.UserID = staff.UserID AND staff.IsDeleted = 0 AND staff.`Status` != 4 "
				+ "AND ss.IsDeleted = 0 AND ss.CompanyID = "+companyID+" AND ss.PaymentYear = "+year
				+ " AND ss.PaymentMonth = "+month+" AND ss.ProcessID IS NULL ORDER BY ss.AddTime DESC";
		List<Object> result = baseDao.findBySql(sql);
		List<SocialSecurityVO> socialSecurityVOs = new ArrayList<SocialSecurityVO>();
		for (Object obj : result) {
			Object[] objs = (Object[]) obj;
			SocialSecurityVO socialSecurityVO = new SocialSecurityVO();
			socialSecurityVO.setSsID((Integer)objs[0]);
			socialSecurityVO.setPaymentYear((Integer) objs[2]);
			socialSecurityVO.setPaymentMonth((Integer) objs[3]);
			socialSecurityVO.setUserID((String)objs[4]);
			socialSecurityVO.setCompanyID((Integer) objs[5]);
			socialSecurityVO.setIdType(Integer.parseInt(objs[6].toString()));
			socialSecurityVO.setIdNumber((String)objs[7]);
			socialSecurityVO.setBasePay((Double)objs[8]);
			socialSecurityVO.setSelfPaidRatio((Double)objs[9]);
			socialSecurityVO.setReason((String)objs[10]);
			socialSecurityVO.setPersonalProvidentFund((Double)objs[11]);
			socialSecurityVO.setCompanyProvidentFund((Double)objs[12]);
			socialSecurityVO.setTotalProvidentFund((Double)objs[13]);
			socialSecurityVO.setUserName((String)objs[17]);
			socialSecurityVOs.add(socialSecurityVO);
		}
		return socialSecurityVOs;
	}
	
	@Override
	public SocialSecurityVO getSocialSecurityByID(int ssID) {
		SocialSecurityEntity socialSecurityEntity = socialSecurityDao.getSocialSecurityByID(ssID);
		return SocialSecurityVOTransformer.INSTANCE.apply(socialSecurityEntity);
	}
	
	@Override
	public HousingFundVO getHousingFundByID(int hfID) {
		HousingFundEntity housingFundEntity = housingFundDao.getHousingFundByID(hfID);
		return HousingFundVOTransformer.INSTANCE.apply(housingFundEntity);
	}
	
	@Override
	public void deleteSocialSecurityByID(int ssID) {
		int count = socialSecurityDao.deleteSocialSecurityByID(ssID);
		if (count <= 0) {
			throw new RuntimeException("该记录不存在！");
		}
	}
	
	@Override
	public void deleteHousingFundByID(int hfID) {
		int count = housingFundDao.deleteHousingFundByID(hfID);
		if (count <= 0) {
			throw new RuntimeException("该记录不存在！");
		}
	}
	
	@Override
	public void startSocialSecurity(SocialSecurityProcessVO socialSecurityProcessVO) {
		List<Group> groups = identityService.createGroupQuery().groupMember(socialSecurityProcessVO.getUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		socialSecurityProcessVO.setCompanyID(companyID);
		socialSecurityProcessVO.setBusinessType(BusinessTypeEnum.SOCIAL_SECURITY.getName());
		socialSecurityProcessVO.setTitle("五险一金申报表审核");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", socialSecurityProcessVO);
		
		String manager = staffService.queryManager(socialSecurityProcessVO.getUserID());
		List<String> ssHRUpdateUsers = permissionService.findUsersByPermissionCodeCompany(Constants.SS_HR_UPDATE, companyID);
		List<String> ssHRUpdateGroups = permissionService.findGroupsByPermissionCodeCompany(Constants.SS_HR_UPDATE, companyID);
		List<String> ssFinancialProcessingUsers = permissionService.findUsersByPermissionCode(Constants.SS_FINANCIAL_PROCESSING);
		List<String> ssFinancialProcessingGroups = permissionService.findGroupsByPermissionCode(Constants.SS_FINANCIAL_PROCESSING);
		List<String> ssFollowUpUsers = permissionService.findUsersByPermissionCodeCompany(Constants.SS_FOLLOW_UP, companyID);
		List<String> ssFollowUpGroups = permissionService.findGroupsByPermissionCodeCompany(Constants.SS_FOLLOW_UP, companyID);
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		if ((!staffService.hasGroupMember(ssHRUpdateGroups) && CollectionUtils.isEmpty(ssHRUpdateUsers)) ||
				(!staffService.hasGroupMember(ssFinancialProcessingGroups) && CollectionUtils.isEmpty(ssFinancialProcessingUsers)) ||
				(!staffService.hasGroupMember(ssFollowUpGroups) && CollectionUtils.isEmpty(ssFollowUpUsers))) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		
		vars.put("manager", manager);
		vars.put("ssHRUpdateUsers", ssHRUpdateUsers);
		vars.put("ssHRUpdateGroups", ssHRUpdateGroups);
		vars.put("ssFinancialProcessingUsers", ssFinancialProcessingUsers);
		vars.put("ssFinancialProcessingGroups", ssFinancialProcessingGroups);
		vars.put("ssFollowUpUsers", ssFollowUpUsers);
		vars.put("ssFollowUpGroups", ssFollowUpGroups);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.SOCIAL_SECURITY);
		// 查询任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), socialSecurityProcessVO.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录社保审核数据
		int sspID = saveSocialSecurityProcess(socialSecurityProcessVO, processInstance.getId());
		//修改社保公积金明细记录对应的流程ID
		socialSecurityDao.updateProcessIDByTime(socialSecurityProcessVO.getYear(), socialSecurityProcessVO.getMonth(), sspID);
		housingFundDao.updateProcessIDByTime(socialSecurityProcessVO.getSsYear(), socialSecurityProcessVO.getSsMonth(), sspID);
	}
	
	private int saveSocialSecurityProcess(SocialSecurityProcessVO socialSecurityProcessVO, String processInstanceID) {
		Date now = new Date();
		SocialSecurityProcessEntity socialSecurityProcessEntity = SocialSecurityProcessEntity.builder()
																			.userID(socialSecurityProcessVO.getUserID())
																			.paymentYear(socialSecurityProcessVO.getYear())
																			.paymentMonth(socialSecurityProcessVO.getMonth())
																			.hfPaymentYear(socialSecurityProcessVO.getSsYear())
																			.hfPaymentMonth(socialSecurityProcessVO.getSsMonth())
																			.companyID(socialSecurityProcessVO.getCompanyID())
																			.personalCount(socialSecurityProcessVO.getPersonalCount())
																			.companyCount(socialSecurityProcessVO.getCompanyCount())
																			.totalCount(socialSecurityProcessVO.getTotalCount())
																			.hfTotalCount(socialSecurityProcessVO.getSsTotalCount())
																			.processInstanceID(processInstanceID)
																			.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
																			.addTime(now)
																			.updateTime(now)
																			.build();
		socialSecurityProcessDao.save(socialSecurityProcessEntity);
		return socialSecurityProcessEntity.getSspID();
	}
	
	@Override
	public ListResult<SocialSecurityProcessVO> findSocialSecurityProcessListByPage(int page, int limit) {
		//查询OA_SocialSecurityProcess表的数据
		List<SocialSecurityProcessEntity> socialSecurityProcessEntities = socialSecurityProcessDao.findSocialSecurityProcessListByPage(page, limit);
		List<SocialSecurityProcessVO> socialSecurityProcessVOs = new ArrayList<SocialSecurityProcessVO>();
		for (SocialSecurityProcessEntity socialSecurityProcessEntity : socialSecurityProcessEntities) {
			SocialSecurityProcessVO socialSecurityProcessVO = new SocialSecurityProcessVO();
			socialSecurityProcessVO.setYear(socialSecurityProcessEntity.getPaymentYear());
			socialSecurityProcessVO.setMonth(socialSecurityProcessEntity.getPaymentMonth());
			socialSecurityProcessVO.setPersonalCount(socialSecurityProcessEntity.getPersonalCount());
			socialSecurityProcessVO.setCompanyCount(socialSecurityProcessEntity.getCompanyCount());
			socialSecurityProcessVO.setTotalCount(socialSecurityProcessEntity.getTotalCount());
			socialSecurityProcessVO.setProcessInstanceID(socialSecurityProcessEntity.getProcessInstanceID());
			socialSecurityProcessVO.setUserName(staffService.getStaffByUserID(socialSecurityProcessEntity.getUserID()).getLastName());
			socialSecurityProcessVO.setRequestDate(DateUtil.formateFullDate(socialSecurityProcessEntity.getAddTime()));
			
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(socialSecurityProcessEntity.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				socialSecurityProcessVO.setStatus("处理中");
				socialSecurityProcessVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				socialSecurityProcessVO.setStatus(TaskResultEnum.valueOf(socialSecurityProcessEntity.getProcessStatus()).getName());
			}
			socialSecurityProcessVOs.add(socialSecurityProcessVO);
		}
		
		int count = socialSecurityProcessDao.countSocialSecurityProcess();
		return new ListResult<SocialSecurityProcessVO>(socialSecurityProcessVOs, count);
	}
	
	@Override
	public SocialSecurityProcessVO getSocialSecurityProcessByProcessInstanceID(String processInstanceID) {
		SocialSecurityProcessEntity socialSecurityProcessEntity = socialSecurityProcessDao.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		return SocialSecurityProcessVOTransformer.INSTANCE.apply(socialSecurityProcessEntity);
	}
	
	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		socialSecurityProcessDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	@Override
	public void updateProcessCount(String processInstanceID, double personalCount, double companyCount) {
		socialSecurityProcessDao.updatePaymentCountByProcessInstanceID(processInstanceID, personalCount, companyCount);
	}
	
	@Override
	public void updateProcessHFCount(String processInstanceID, double totalCount) {
		socialSecurityProcessDao.updateHFCountByProcessInstanceID(processInstanceID, totalCount);
	}
	
	@Override
	public SocialSecurityProcessVO getLastProcessVOByHFTime(int year, int month, int companyID) {
		SocialSecurityProcessEntity socialSecurityProcessEntity = socialSecurityProcessDao.getLastProcessByHFTime(year, month, companyID);
		return SocialSecurityProcessVOTransformer.INSTANCE.apply(socialSecurityProcessEntity);
	}
	
	@Override
	public SocialSecurityProcessVO getLastProcessVOBySSTime(int year, int month, int companyID) {
		SocialSecurityProcessEntity socialSecurityProcessEntity = socialSecurityProcessDao.getLastProcessBySSTime(year, month, companyID);
		return SocialSecurityProcessVOTransformer.INSTANCE.apply(socialSecurityProcessEntity);
				
	}
	
	@Override
	public List<SocialSecurityVO> findSocialSecurityListByProcessID(int processID) {
		List<SocialSecurityEntity> socialSecurityEntities = socialSecurityDao.findSocialSecurityListByProcessID(processID);
		return Lists2.transform(socialSecurityEntities, SocialSecurityVOTransformer.INSTANCE);
	}
	
	@Override
	public List<HousingFundVO> findHousingFundListByProcessID(int processID) {
		List<HousingFundEntity> housingFundEntities = housingFundDao.findHousingFundListByProcessID(processID);
		return Lists2.transform(housingFundEntities, HousingFundVOTransformer.INSTANCE);
	}
}
