package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.PermissionDao;
import com.zhizaolian.staff.dao.PermissionMembershipDao;
import com.zhizaolian.staff.entity.PartnerDetailEntity;
import com.zhizaolian.staff.entity.PartnerEntity;
import com.zhizaolian.staff.entity.PartnerOptionEntity;
import com.zhizaolian.staff.entity.PartnerQuitSalaryDetailsEntity;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.entity.PermissionMembershipEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.PartnerService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.PoiUtil;

public class PartnerServiceImpl implements PartnerService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private PermissionMembershipDao permissionMembershipDao;
	@Override
	public PartnerEntity getPartnerApplyByUserId(String userId) {
		String hql = "from PartnerEntity where isDeleted=0 and ifNull(status, 0)!=2 and userId='"+userId+"'";
		return (PartnerEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void startPartner(PartnerEntity partner) {
		partner.setAddTime(new Date());
		partner.setIsDeleted(0);
		baseDao.hqlSave(partner);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PartnerEntity> getPartnerApplysByUserId(String userId) {
		String hql = "from PartnerEntity where isDeleted=0 and userId='"+userId+"'";
		return (List<PartnerEntity>) baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PartnerEntity> getPartnerApplysBy(String userId) {
		String hql = "from PartnerEntity where userId='"+userId+"'";
		return (List<PartnerEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public PartnerEntity getPartnerApplyById(String id) {
		String hql = "from PartnerEntity where id="+id;
		return (PartnerEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<Object> findAllApplyPartners(Integer limit, Integer page, String applyer) {
		String sql = "select partner.id, staffName, applyDate, ifNull(partner.status, 0) status, comment from OA_Partner partner, OA_Staff staff where "
				+ "partner.userId=staff.userId and staff.status!=4 and staff.isDeleted=0 "
				+ "and partner.isDeleted=0 ";
		List<Object> objectList = null;
		if(StringUtils.isNotBlank(applyer)){
			sql += "and staff.staffName like:applyer order by status, partner.addTime desc";
			objectList = sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("applyer", "%"+applyer+"%").setMaxResults(limit).setFirstResult((page-1)*limit).list();
		}else{
			sql += "order by status, partner.addTime desc";
			objectList = sessionFactory.getCurrentSession().createSQLQuery(sql).setMaxResults(limit).setFirstResult((page-1)*limit).list();
		}
		String sqlCount = "select count(partner.id) from OA_Partner partner, OA_Staff staff where "
				+ "partner.userId=staff.userId and staff.status!=4 and staff.isDeleted=0 "
				+ "and partner.isDeleted=0 ";
		int count = 0;
		if(StringUtils.isNotBlank(applyer)){
			sqlCount += "and staff.staffName like:applyer";
			count = Integer.parseInt(sessionFactory.getCurrentSession().createSQLQuery
					(sqlCount).setParameter("applyer", "%"+applyer+"%").uniqueResult()+"");
		}else{
			count = Integer.parseInt(sessionFactory.getCurrentSession().createSQLQuery(sqlCount).uniqueResult()+"");
		}
		return new ListResult<>(objectList, count);
	}
	@Override
	public void auditApply(String applyId, String result, String comment) {
		String sql = "update OA_Partner set status="+result+", comment='"+EscapeUtil.decodeSpecialChars(comment)+"' where id="+applyId;
		baseDao.excuteSql(sql);
	}
	@Override
	public int getToAuditApplyNum() {
		String sql = "select count(*) from OA_Partner where status is null and isDeleted=0";
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	@Override
	public boolean checkHasAuditPartner(String id) {
		return permissionMembershipDao.checkHasPermissionByUserId(Constants.AUDIT_PARTNER, id);
	}
	@Override
	public void savePartnerDetail(PartnerDetailEntity partnerDetail) {
		if(StringUtils.isBlank(partnerDetail.getUserIds())){
			List<Object> allPartners = getAllPartner();
			for(Object obj: allPartners){
				String partner = (String)obj;
				PartnerDetailEntity partnerEntity = new PartnerDetailEntity();
				partnerEntity.setContent(partnerDetail.getContent());
				partnerEntity.setDetailType(partnerDetail.getDetailType());
				partnerEntity.setMoney(partnerDetail.getMoney());
				partnerEntity.setRewardType(partnerDetail.getRewardType());
				partnerEntity.setTheme(partnerDetail.getTheme());
				partnerEntity.setUserId(partner);
				partnerEntity.setAddTime(new Date());
				partnerEntity.setIsDeleted(0);
				int id = baseDao.hqlSave(partnerEntity);
				if(Constants.OPTION.equals(partnerDetail.getRewardType())){
					PartnerOptionEntity staffOption = new PartnerOptionEntity();
					staffOption.setDetailId(id);
					staffOption.setOptionMoney(partnerDetail.getMoney());
					staffOption.setPurchaseDate(new Date());
					staffOption.setPurchaseType(Constants.COMPANY_REWARD);
					staffOption.setStatus("1");//已匹配
					staffOption.setUserId(partner);
					staffOption.setAddTime(new Date());
					staffOption.setIsDeleted(0);
					baseDao.hqlSave(staffOption);
				}
			}
		}else{
			String[] userIds = partnerDetail.getUserIds().split(",");
			for(String partner: userIds){
				PartnerDetailEntity partnerEntity = new PartnerDetailEntity();
				partnerEntity.setContent(partnerDetail.getContent());
				partnerEntity.setDetailType(partnerDetail.getDetailType());
				partnerEntity.setMoney(partnerDetail.getMoney());
				partnerEntity.setRewardType(partnerDetail.getRewardType());
				partnerEntity.setTheme(partnerDetail.getTheme());
				partnerEntity.setUserId(partner);
				partnerEntity.setAddTime(new Date());
				partnerEntity.setIsDeleted(0);
				int id = baseDao.hqlSave(partnerEntity);
				if(Constants.OPTION.equals(partnerDetail.getRewardType())){
					PartnerOptionEntity staffOption = new PartnerOptionEntity();
					staffOption.setDetailId(id);
					staffOption.setOptionMoney(partnerDetail.getMoney());
					staffOption.setPurchaseDate(new Date());
					staffOption.setPurchaseType(Constants.COMPANY_REWARD);
					staffOption.setStatus("1");//已匹配
					staffOption.setUserId(partner);
					staffOption.setIsDeleted(0);
					staffOption.setAddTime(new Date());
					baseDao.hqlSave(staffOption);
				}
			}
		}
	}
	private List<Object> getAllPartner() {
		String sql = "select userId from OA_Partner where isDeleted=0 and status=1";
		return baseDao.findBySql(sql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<Object> findPartnerDetailList(String type, String staffName, Integer limit, Integer page) {
		String sql = "select detail.id, staffName, detailType, rewardType, money, theme, content from OA_PartnerDetail detail, OA_Staff staff where "
				+ "detail.userId=staff.userId and staff.status!=4 and staff.isDeleted=0 "
				+ "and detail.isDeleted=0 ";
		if(StringUtils.isNotBlank(staffName)){
			sql += "and staff.staffName like:staffName ";
		}
		if(StringUtils.isNotBlank(type)){
			sql += "and detailType=:detailType ";
		}
		sql += "order by detail.addTime desc";
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(StringUtils.isNotBlank(staffName)){
			sqlQuery.setParameter("staffName", "%"+staffName+"%");
		}
		if(StringUtils.isNotBlank(type)){
			sqlQuery.setParameter("detailType", type);
		}
		List<Object> objList = sqlQuery.setMaxResults(limit).setFirstResult((page-1)*limit).list();
		String sqlCount = "select count(detail.id) from OA_PartnerDetail detail, OA_Staff staff where "
				+ "detail.userId=staff.userId and staff.status!=4 and staff.isDeleted=0 "
				+ "and detail.isDeleted=0 ";
		if(StringUtils.isNotBlank(staffName)){
			sqlCount += "and staff.staffName like:staffName ";
		}
		if(StringUtils.isNotBlank(type)){
			sqlCount += "and detailType=:detailType";
		}
		sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlCount);
		if(StringUtils.isNotBlank(staffName)){
			sqlQuery.setParameter("staffName", "%"+staffName+"%");
		}
		if(StringUtils.isNotBlank(type)){
			sqlQuery.setParameter("detailType", type);
		}
		int count = Integer.parseInt(sqlQuery.uniqueResult()+"");
		return new ListResult<>(objList, count);
	}
	@Override
	public boolean checkIsPartner(String userId) {
		String sql = "select count(id) from OA_Partner where userId='"+userId+"' and status=1 and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public double getTotalMoney(String id) {
		String sql = "select sum(money) from OA_PartnerOption where isDeleted=0 and userId='"+id+"'";
		Object obj = baseDao.getUniqueResult(sql);
		return Double.parseDouble(null==obj ? "0":String.valueOf(obj));
	}
	@Override
	public double getOptionMonty(String id) {
		String sql = "select sum(optionMoney) from OA_PartnerOption where isDeleted=0 and userId='"+id+"'";
		Object obj = baseDao.getUniqueResult(sql);
		return Double.parseDouble(null==obj ? "0":String.valueOf(obj));
	}
	@Override
	public Map<String, List<String>> getDetailListGroupByType(String userId) {
		String sql = "SELECT\n" +
				"	detailType,\n" +
				"	GROUP_CONCAT(theme ORDER BY addTime separator '###')\n" +
				"FROM\n" +
				"	OA_PartnerDetail\n" +
				"WHERE\n" +
				"	isDeleted=0 and userId = '"+userId+"'\n" +
				"GROUP BY\n" +
				"	detailType";
		List<Object> objList = baseDao.findBySql(sql);
		Map<String, List<String>> typeAndDetailListMap = new HashMap<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			String detailType = (String)objs[0];
			String themeStr = (String)objs[1];
			String[] themes = themeStr.split("###");
			typeAndDetailListMap.put(detailType, Arrays.asList(themes));
		}
		return typeAndDetailListMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<PartnerOptionEntity> findPartnerOptionsByUserId(String id, Integer limit, Integer page) {
		String hql = "from PartnerOptionEntity where userId='"+id+"' and isDeleted=0";
		List<PartnerOptionEntity> partnerOptions = (List<PartnerOptionEntity>) baseDao.hqlPagedFind(hql, page, limit);
		String sqlCount = "select count(id) from OA_PartnerOption where userId='"+id+"' and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(partnerOptions, count);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<Object> findPartnerDetailListByUserId(String type, String userId, Integer limit, Integer page) {
		String sql = "select detailType, rewardType, money, theme, content from OA_PartnerDetail detail where "
				+ "detail.isDeleted=0 and detail.userId=:userId and detailType=:detailType order by detail.addTime desc";
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
		sqlQuery.setParameter("detailType", type);
		sqlQuery.setParameter("userId", userId);
		List<Object> objList = sqlQuery.setMaxResults(limit).setFirstResult((page-1)*limit).list();
		String sqlCount = "select count(detail.id) from OA_PartnerDetail detail where "
				+ "detail.isDeleted=0 and detail.userId=:userId and detailType=:detailType";
		sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlCount);
		sqlQuery.setParameter("detailType", type);
		sqlQuery.setParameter("userId", userId);
		int count = Integer.parseInt(sqlQuery.uniqueResult()+"");
		return new ListResult<>(objList, count);
	}
	@Override
	public void deletePartnerDetail(String id, String rewardType) {
		String sql = "update OA_PartnerDetail set isDeleted=1 where id="+id;
		baseDao.excuteSql(sql);
		sql = "update OA_PartnerOption set isDeleted=1 where detailId="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public ListResult<Object> findPartnerOptions(String[] conditons, Integer limit, Integer page) {
		String sql = "select id, purchaseDate, staffName, money, optionMoney, purchaseType,\n"
				+ "op.status from OA_PartnerOption op, OA_Staff staff where op.isDeleted=0\n"
				+ "and op.userId=staff.userId\n";
		String purchaseBeginDate = conditons[0];
		String purchaseEndDate = conditons[1];
		String purchaserId = conditons[2];
		String purchaseType = conditons[3];
		String status = conditons[4];
		if(StringUtils.isNotBlank(purchaseBeginDate)){
			sql += "and Date(purchaseDate)>='"+purchaseBeginDate+"'\n";
		}
		if(StringUtils.isNotBlank(purchaseEndDate)){
			sql += "and Date(purchaseDate)<='"+purchaseEndDate+"'\n";
		}
		if(StringUtils.isNotBlank(purchaserId)){
			sql += "and op.userId='"+purchaserId+"'\n";
		}
		if(StringUtils.isNotBlank(purchaseType)){
			sql += "and purchaseType='"+purchaseType+"'\n";
		}
		if(StringUtils.isNotBlank(status)){
			sql += "and op.status='"+status+"'\n";
		}
		sql += "order by op.addTime desc";
		List<Object> partnerOptions = baseDao.findPageList(sql, page, limit);
		String sqlCount = "select count(op.id)\n"
				+ "status from OA_PartnerOption op, OA_Staff staff where op.isDeleted=0\n"
				+ "and op.userId=staff.userId\n";
		if(StringUtils.isNotBlank(purchaseBeginDate)){
			sqlCount += "and Date(purchaseDate)>='"+purchaseBeginDate+"'\n";
		}
		if(StringUtils.isNotBlank(purchaseEndDate)){
			sqlCount += "and Date(purchaseDate)<='"+purchaseEndDate+"'\n";
		}
		if(StringUtils.isNotBlank(purchaserId)){
			sqlCount += "and op.userId='"+purchaserId+"'\n";
		}
		if(StringUtils.isNotBlank(purchaseType)){
			sqlCount += "and purchaseType='"+purchaseType+"'\n";
		}
		if(StringUtils.isNotBlank(status)){
			sqlCount += "and op.status='"+status+"'";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(partnerOptions, count);
	}
	@Override
	public boolean checkHasPartnerManage(String id) {
		return permissionMembershipDao.checkHasPermissionByUserId(Constants.PARTNER_MANAGE, id);
	}
	@Override
	public int getToMacthOptionNum() {
		String sql = "select count(*) from OA_PartnerOption where status=0 and isDeleted=0";
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	@Override
	public void matchPartnerOption(String matchPartnerIds, String ratio) {
		String sql = "update OA_PartnerOption set status=1, optionMoney=(money*"+ratio+") where id in("+matchPartnerIds+")";
		baseDao.excuteSql(sql);
	}
	@Override
	public void synData() {
		String hql = "from SalaryDetailEntity order by year, month";
		@SuppressWarnings("unchecked")
		List<SalaryDetailEntity> salarys = (List<SalaryDetailEntity>) baseDao.hqlfind(hql);
		for(SalaryDetailEntity salary: salarys){
			try {
				String userId = salary.getUserId();
				int month = salary.getMonth();
				int year = salary.getYear();
				String time = "";
				if(month<10){
					time = year+"-0"+month+"-"+"01";
				}else{
					time = year+"-"+month+"-"+"01";
				}
				Date addTime = DateUtil.getSimpleDate(time);
				@SuppressWarnings("unchecked")
				List<String> details = (List<String>) ObjectByteArrTransformer.toObject(salary.getDetail());
				if(details.size()!=18){
					continue;
				}
				String quit = details.get(details.size()-1);
				//合伙人
				if(StringUtils.isNotBlank(quit)){
					String reg = "^\\d+(\\.\\d+)?$";
					if(quit.matches(reg)){
						double quitMoney = Double.parseDouble(quit);
						if(quitMoney<=0){
							if(checkIsPartner(userId)){
								String sql = "update OA_Partner set isDeleted=1 where userId='"+userId+"'";
								baseDao.excuteSql(sql);
							}
							continue;
						}
					}else if(checkIsPartner(userId)){
						String sql = "update OA_Partner set isDeleted=1 where userId='"+userId+"'";
						baseDao.excuteSql(sql);
						continue;
					}
					if(!checkIsPartner(userId)){
						PartnerEntity partner = new PartnerEntity();
						partner.setAddTime(addTime);
						//partner.setApplyContent("申请加入合伙人");//申请说明书
						partner.setApplyDate(time);
						partner.setIsDeleted(0);
						partner.setStatus("1");
						partner.setUserId(userId);
						baseDao.hqlSave(partner);
					}
					PartnerOptionEntity partnerOption = new PartnerOptionEntity();
					partnerOption.setAddTime(addTime);
					partnerOption.setIsDeleted(0);
					partnerOption.setMoney(quit);
					partnerOption.setPurchaseDate(addTime);
					//partnerOption.setPurchaseType(Constants.MONEY_PURCHASE);
					partnerOption.setStatus("0");
					partnerOption.setUserId(userId);
					partnerOption.setSalaryId(salary.getId());
					baseDao.hqlSave(partnerOption);
				}else{
					//退出合伙人
					if(checkIsPartner(userId)){
						String sql = "update OA_Partner set isDeleted=1 where userId='"+userId+"'";
						baseDao.excuteSql(sql);
						System.out.println("退出"+month);
					}
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void uploadQuitSalaryDetail(File file) throws Exception {
		// 初始化一个工作簿
		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = WorkbookFactory.create(fis);
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getLastRowNum();
		//从第三行开始
		for(int i=2; i<rows; i++){
			Row row = sheet.getRow(i);
			//姓名
			String name = row.getCell(2).getStringCellValue();
			if(StringUtils.isBlank(name)){
				continue;
			}
			String userId = getUserIdByName(name);
			List<String> quitSalaryDetails =  new ArrayList<>();
			for(int j=13; j<=21; j++){
				String detail = PoiUtil.getCellValue_(row.getCell(j))[1];
				quitSalaryDetails.add(detail);
			}
			PartnerQuitSalaryDetailsEntity entity = new PartnerQuitSalaryDetailsEntity();
			entity.setUserId(userId);
			entity.setQuitSalaryDetails(ObjectByteArrTransformer.toByteArray(quitSalaryDetails));
			baseDao.hqlSave(entity);
		}
	}
	//私有的问题方法，不要用
	private String getUserIdByName(String name){
		String sql = "select UserID from OA_Staff where StaffName='"+name+"' AND IsDeleted=0";
		return (String) baseDao.getUniqueResult(sql);
	}
	@Override
	public PartnerQuitSalaryDetailsEntity getPartnerQuitSalaryDetail(String id) {
		String hql = "from PartnerQuitSalaryDetailsEntity where userId='"+id+"'";
		return (PartnerQuitSalaryDetailsEntity) baseDao.hqlfindUniqueResult(hql);
	}
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private PermissionService permissionService;

	@Override
	public PartnerEntity getPartnerEntityBy(String userId) {
		String hql = "from PartnerEntity where isDeleted = 0 and userId = '"+userId+"'";
		PartnerEntity partnerEntity = (PartnerEntity) baseDao.hqlfindUniqueResult(hql);
		return partnerEntity;
	}
	
	@Override
	public List<PartnerEntity> findExitPartnerTaskVOs(List<Task> exitPartnerTasks) {
		List<PartnerEntity> partnerEntitys = new ArrayList<PartnerEntity>();
		if(null != exitPartnerTasks){
			for(Task exitPartnerTask : exitPartnerTasks){
				List<PartnerEntity> list = getTheListBy(exitPartnerTask.getProcessInstanceId());
				PartnerEntity partnerEntity = list.get(0);
				partnerEntity.setTaskId(exitPartnerTask.getId());
				partnerEntity.setTaskName(exitPartnerTask.getName());
				partnerEntity.setDescription(exitPartnerTask.getDescription());
				partnerEntity.setAssignee(exitPartnerTask.getAssignee());
				partnerEntitys.add(partnerEntity);
			}
		}
		return partnerEntitys;
	}
	
	@Override
	public List<PartnerEntity> getTheListBy(String processInstanceId) {
		String sql =  "SELECT\n" +
				"	a.*, b.id exitParterApplyId,\n" +
				"	b.addTime applyAddTime,\n" +
				"	approvalOpinion,\n" +
				"	auditStatus,\n" +
				"	exitReason,\n" +
				"	b.isDeleted exisIsDeleted,\n" +
				"	partnerId,\n" +
				"	processInstanceId,\n" +
				"	b.updateTime exitUpdateTime\n" +
				"FROM\n" +
				"	oa_partner a\n" +
				"INNER JOIN oa_exitparterapply b ON a.id = b.partnerId\n" +
				"WHERE\n" +
				"	processInstanceId = '"+processInstanceId+"'\n" +
				"AND a.isDeleted = 0\n" +
				"AND b.isDeleted = 0";
		List<Object> objects = baseDao.findBySql(sql);
		List<PartnerEntity> partnerList = new ArrayList<>();
		for(Object obj :objects){
			Object[] objs = (Object[]) obj;
			PartnerEntity partner = new PartnerEntity();
			partner.setId((Integer) objs[0]);
			partner.setAddTime((Date) objs[1]);
			partner.setApplyContent((String) objs[2]);
			partner.setApplyDate((String) objs[3]);
			partner.setComment((String) objs[4]);
			partner.setIsDeleted((Integer) objs[5]);
			partner.setStatus((String) objs[6]);
			partner.setUpdateTime((Date) objs[7]);
			partner.setUserId((String) objs[8]);
			partner.setExitParterApplyId((Integer) objs[9]);
			partner.setApplyAddTime((Date) objs[10]);
			partner.setApprovalOpinion((String) objs[11]);
			partner.setAuditStatus((Integer) objs[12]);
			partner.setExitReason((String) objs[13]);
			partner.setExisIsDeleted((Integer) objs[14]);
			partner.setPartnerId((Integer) objs[15]);
			partner.setProcessInstanceId((String) objs[16]);
			partner.setExitUpdateTime((Date) objs[17]);
			partnerList.add(partner);
		}
		return partnerList;
	}
	@Override
	public void updatePartner(String userId,Integer isDeleted) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = dateFormat.format(date);
		
		String sql = "UPDATE OA_Partner\n" +
				"SET OA_Partner.isDeleted = "+isDeleted+",\n" +
				" updateTime = '"+formatDate+"'\n" +
				"WHERE\n" +
				"	OA_Partner.userId = '"+userId+"'";
		baseDao.excuteSql(sql);
	}

	
	@Override
	public void updateApprovalOpinionAndOthers(String approvalOpinion,Integer auditStatus, String processInstanceId,Integer isDeleted) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = dateFormat.format(date);
		String sql = null;
		if(approvalOpinion == null){
			sql = "UPDATE oa_exitparterapply\n" +
					"SET auditStatus = "+auditStatus+",\n" +
					" updateTime = '"+formatDate+"',\n" +
					" isDeleted = "+isDeleted+"\n" +
					"WHERE\n" +
					"   processInstanceId = '"+processInstanceId+"'\n";
		}else{
			sql = "UPDATE oa_exitparterapply\n" +
					"SET approvalOpinion = '"+approvalOpinion+"',\n" +
					" auditStatus = "+auditStatus+",\n" +
					" updateTime = '"+formatDate+"',\n" +
					" isDeleted = "+isDeleted+"\n" +
					"WHERE\n" +
					"   processInstanceId = '"+processInstanceId+"'\n";
		}
		baseDao.excuteSql(sql);
	}
	@Override
	public void addExitParterApply(String exitReason, String userId) {
		Map<String,Object> vars = new HashMap<String,Object>();
		PermissionEntity permissionEntity = permissionDao.getPermissionByCode(Constants.EXIT_PARTNER);
		PermissionMembershipEntity permissionMembershipEntity = permissionService.findPermissionmembership(permissionEntity.getPermissionID());
		permissionMembershipEntity.getUserGroupID();
		
		vars.put("auditor", permissionMembershipEntity.getUserGroupID());
		vars.put("auditors", new ArrayList<>());
		vars.put("auditGroups", new ArrayList<>());
		vars.put("processType", Constants.EXIT_PARTNER);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EASY_PROCESS,vars);
		
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("easyProcess", vars);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), userId);
		taskService.complete(task.getId());
	
		PartnerEntity partnerEntity = getPartnerEntityBy(userId);
		Integer partnerId = partnerEntity.getId();
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formarDate = dateFormat.format(date);
		String sql= "INSERT INTO oa_exitparterapply (\n" +
				"	exitReason,\n" +
				"	addTime,\n" +
				"	auditStatus,\n" +
				"	partnerId,\n" +
				"	isDeleted,\n" +
				"	processInstanceId\n" +
				")\n" +
				"VALUES\n" +
				"	('"+exitReason+"', '"+formarDate+"', 0, "+partnerId+", 0,'"+processInstance.getId()+"')";
		baseDao.excuteSql(sql);
	}

	@Override
	public List<PartnerEntity> findTheListBy(String userId,Integer exisIsDeleted) {
		String sql =  "SELECT\n" +
				"	a.*, b.id exitParterApplyId,\n" +
				"	b.addTime applyAddTime,\n" +
				"	approvalOpinion,\n" +
				"	auditStatus,\n" +
				"	exitReason,\n" +
				"	b.isDeleted exisIsDeleted,\n" +
				"	partnerId,\n" +
				"	processInstanceId,\n" +
				"	b.updateTime exitUpdateTime\n" +
				"FROM\n" +
				"	oa_partner a\n" +
				"INNER JOIN oa_exitparterapply b ON a.id = b.partnerId\n" +
				"WHERE\n" +
				"	a.userId = '"+userId+"'\n" +
				"AND a.isDeleted = 0\n" +
				"AND b.isDeleted = "+exisIsDeleted;
		List<Object> objects = baseDao.findBySql(sql);
		List<PartnerEntity> partnerList = new ArrayList<>();
		for(Object obj :objects){
			Object[] objs = (Object[]) obj;
			PartnerEntity partner = new PartnerEntity();
			partner.setId((Integer) objs[0]);
			partner.setAddTime((Date) objs[1]);
			partner.setApplyContent((String) objs[2]);
			partner.setApplyDate((String) objs[3]);
			partner.setComment((String) objs[4]);
			partner.setIsDeleted((Integer) objs[5]);
			partner.setStatus((String) objs[6]);
			partner.setUpdateTime((Date) objs[7]);
			partner.setUserId((String) objs[8]);
			partner.setExitParterApplyId((Integer) objs[9]);
			partner.setApplyAddTime((Date) objs[10]);
			partner.setApprovalOpinion((String) objs[11]);
			partner.setAuditStatus((Integer) objs[12]);
			partner.setExitReason((String) objs[13]);
			partner.setExisIsDeleted((Integer) objs[14]);
			partner.setPartnerId((Integer) objs[15]);
			partner.setProcessInstanceId((String) objs[16]);
			partner.setExitUpdateTime((Date) objs[17]);
			partnerList.add(partner);
		}
		return partnerList;
	}
	@Override
	public List<PartnerEntity> findTheListBy(String userId) {
		String sql =  "SELECT\n" +
				"	a.*, b.id exitParterApplyId,\n" +
				"	b.addTime applyAddTime,\n" +
				"	approvalOpinion,\n" +
				"	auditStatus,\n" +
				"	exitReason,\n" +
				"	b.isDeleted exisIsDeleted,\n" +
				"	partnerId,\n" +
				"	processInstanceId,\n" +
				"	b.updateTime exitUpdateTime\n" +
				"FROM\n" +
				"	oa_partner a\n" +
				"INNER JOIN oa_exitparterapply b ON a.id = b.partnerId\n" +
				"WHERE\n" +
				"	a.userId = '"+userId+"'\n"+
				"   ORDER BY b.addTime asc";
		List<Object> objects = baseDao.findBySql(sql);
		List<PartnerEntity> partnerList = new ArrayList<>();
		for(Object obj :objects){
			Object[] objs = (Object[]) obj;
			PartnerEntity partner = new PartnerEntity();
			partner.setId((Integer) objs[0]);
			partner.setAddTime((Date) objs[1]);
			partner.setApplyContent((String) objs[2]);
			partner.setApplyDate((String) objs[3]);
			partner.setComment((String) objs[4]);
			partner.setIsDeleted((Integer) objs[5]);
			partner.setStatus((String) objs[6]);
			partner.setUpdateTime((Date) objs[7]);
			partner.setUserId((String) objs[8]);
			partner.setExitParterApplyId((Integer) objs[9]);
			partner.setApplyAddTime((Date) objs[10]);
			partner.setApprovalOpinion((String) objs[11]);
			partner.setAuditStatus((Integer) objs[12]);
			partner.setExitReason((String) objs[13]);
			partner.setExisIsDeleted((Integer) objs[14]);
			partner.setPartnerId((Integer) objs[15]);
			partner.setProcessInstanceId((String) objs[16]);
			partner.setExitUpdateTime((Date) objs[17]);
			partnerList.add(partner);
		}
		return partnerList;
	}
}
