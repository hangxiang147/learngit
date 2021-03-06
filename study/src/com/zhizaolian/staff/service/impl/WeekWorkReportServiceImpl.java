package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.WeekReportEntity;
import com.zhizaolian.staff.entity.WeekReporterEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.WeekWorkReportService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.NextWeekWorkPlan;
import com.zhizaolian.staff.vo.RiskVo;
import com.zhizaolian.staff.vo.ThisWeekWorkVo;

public class WeekWorkReportServiceImpl implements WeekWorkReportService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public boolean checkCanWriteReport(String id) throws Exception {
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(id);
		Integer companyID = groups.get(0).getCompanyID();
		String departmentId = groups.get(0).getDepartmentID()+"";
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId, DateUtil.formateDate(new Date()));
		String beginTime = workTimeArray[0];
		String endTime = workTimeArray[3];
		Date now = new Date();
		Date beginDate = DateUtil.getFullDate(DateUtil.formateDate(now)+" "+beginTime+":00");
		Date endDate = DateUtil.getFullDate(DateUtil.formateDate(now)+" "+endTime+":00");
		@SuppressWarnings("deprecation")
		int week = now.getDay();
		//周五、周六、周日或者周一
		if(week==5 || week==6 || week==0 || week==1){
			//若是周五，须检查有么有到下班时间
			if(week==5 && DateUtil.before(now, endDate)){
				return true;
			//若是周一，须检查有么有到上班时间
			}else if(week==1 && DateUtil.after(now, beginDate)){
				return true;
			}
			return false;
		}
		return true;
	}
	@Override
	public int saveWeekReport(WeekReportEntity weekReport, ThisWeekWorkVo thisWeekWorkVo, RiskVo riskVo,
			NextWeekWorkPlan nextWeekWorkPlan, String weekWorkSummary) throws Exception {
		weekReport.setThisWeekWorks(ObjectByteArrTransformer.toByteArray(thisWeekWorkVo));
		if(null != riskVo){
			weekReport.setRisks(ObjectByteArrTransformer.toByteArray(riskVo));
		}
		if(null != nextWeekWorkPlan){
			weekReport.setNextWorkPlans(ObjectByteArrTransformer.toByteArray(nextWeekWorkPlan));
		}
		weekReport.setWeekWorkSummary(weekWorkSummary);
		weekReport.setIsDeleted(0);
		weekReport.setAddTime(new Date());
		return baseDao.hqlSave(weekReport);
	}
	@Override
	public WeekReportEntity getWeekReportDetail(String weekReportId) {
		String hql = "from WeekReportEntity where id="+weekReportId;
		return (WeekReportEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public List<WeekReportEntity> getWeekReportList(String id, String beginDate, String endDate) throws Exception {
		String hql = "from WeekReportEntity where userId='"+id+"' and isDeleted=0";
		if(StringUtils.isNotBlank(beginDate)){
			hql += " and Date(addTime)>='"+beginDate+"'";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += " and Date(addTime)<='"+endDate+"'";
		}
		hql += " order by addTime desc";
		@SuppressWarnings("unchecked")
		List<WeekReportEntity> weekReportList = (List<WeekReportEntity>) baseDao.hqlfind(hql);
		for(WeekReportEntity weekReport: weekReportList){
			weekReport.setThisWeekWorkVo((ThisWeekWorkVo) ObjectByteArrTransformer.toObject(weekReport.getThisWeekWorks()));
			int thisWeekWorkNum = weekReport.getThisWeekWorkVo().getContent().length;
			int riskNum = 0;
			int nextWeekWorkNum = 0;
			byte[] risks = weekReport.getRisks();
			if(null != risks){
				weekReport.setRiskVo((RiskVo)ObjectByteArrTransformer.toObject(risks));
				riskNum = weekReport.getRiskVo().getResponsiblePerson().length;
			}
			byte[] nextWeekWorks = weekReport.getNextWorkPlans();
			if(null != nextWeekWorks){
				weekReport.setNextWeekWork((NextWeekWorkPlan) ObjectByteArrTransformer.toObject(nextWeekWorks));
				nextWeekWorkNum = weekReport.getNextWeekWork().getContent().length;
			}
			int maxNum = thisWeekWorkNum;
			if(riskNum>maxNum){
				maxNum = riskNum;
			}
			if(nextWeekWorkNum>maxNum){
				maxNum = nextWeekWorkNum;
			}
			weekReport.setMaxRow(maxNum);
		}
		return weekReportList;
	}
	@Override
	public boolean checkRepeatedReport(String userId) throws Exception {
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(userId);
		Integer companyID = groups.get(0).getCompanyID();
		String departmentId = groups.get(0).getDepartmentID()+"";
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId, DateUtil.formateDate(new Date()));
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String endTime = workTimeArray[3];
		Calendar cal = Calendar.getInstance();
		//本周日报开始汇报的时间
		String reportBeginTime = "";
		Date now = cal.getTime();
		@SuppressWarnings("deprecation")
		int week = now.getDay();
		
		//周五
		if(week==5){
			reportBeginTime = DateUtil.formateDate(now)+" "+endTime+":00";
		//周六	
		}else if(week==6){
			cal.add(Calendar.DATE, -1);
			reportBeginTime = DateUtil.formateDate(cal.getTime())+" "+endTime+":00";
		//周天	
		}else if(week==7){
			cal.add(Calendar.DATE, -2);
			reportBeginTime = DateUtil.formateDate(cal.getTime())+" "+endTime+":00";
		//周一	
		}else{
			cal.add(Calendar.DATE, -3);
			reportBeginTime = DateUtil.formateDate(cal.getTime())+" "+endTime+":00";
		}
		String sql = "select count(*) from OA_WeekReport where addTime>='"+reportBeginTime
				+"' and addTime<='"+DateUtil.formateFullDate(now)+"' and isDeleted=0 and userId='"+userId+"'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return false;
		}
		return true;
	}
	@Override
	public ListResult<WeekReportEntity> findWeekReportListByConditions(String[] conditions, Integer page,
			Integer limit) throws  Exception{
		String staffId = conditions[0];
		String companyId = conditions[1];
		String beginDate = conditions[2];
		String endDate = conditions[3];
		String sql = "SELECT\n" +
				"	wr.*\n" +
				"FROM\n" +
				"	OA_WeekReport wr,\n" +
				"	OA_GroupDetail gr,\n" +
				"	OA_Staff staff,\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			ACT_ID_MEMBERSHIP\n" +
				"		GROUP BY\n" +
				"			USER_ID_\n" +
				"	) ship\n" +
				"WHERE\n" +
				"	wr.userId = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = gr.GroupID\n" +
				"AND wr.isDeleted = 0 and staff.userId = wr.userId and staff.isDeleted=0 and staff.status!=4\n";
		if(StringUtils.isNotBlank(staffId)){
			sql += "AND staff.userId='"+staffId+"'\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sql += "AND Date(wr.addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			sql += "AND Date(wr.addTime)<='"+endDate+"'\n";
		}
		if(StringUtils.isNotBlank(companyId)){
			sql += "AND gr.CompanyID = "+companyId+"\n";
		}
		sql += "order by addTime desc";
		List<Object> objects = baseDao.findPageList(sql, page, limit);
		List<WeekReportEntity> weekReportList = new ArrayList<>();
		for(Object obj: objects){
			WeekReportEntity weekReport = new WeekReportEntity();
			Object[] objs = (Object[])obj;
			weekReport.setThisWeekWorkVo((ThisWeekWorkVo) ObjectByteArrTransformer.toObject((byte[])objs[5]));
			int thisWeekWorkNum = weekReport.getThisWeekWorkVo().getContent().length;
			int riskNum = 0;
			int nextWeekWorkNum = 0;
			byte[] risks = (byte[])objs[4];
			if(null != risks){
				weekReport.setRiskVo((RiskVo)ObjectByteArrTransformer.toObject(risks));
				riskNum = weekReport.getRiskVo().getResponsiblePerson().length;
			}
			byte[] nextWeekWorks = (byte[])objs[3];;
			if(null != nextWeekWorks){
				weekReport.setNextWeekWork((NextWeekWorkPlan) ObjectByteArrTransformer.toObject(nextWeekWorks));
				nextWeekWorkNum = weekReport.getNextWeekWork().getContent().length;
			}
			int maxNum = thisWeekWorkNum;
			if(riskNum>maxNum){
				maxNum = riskNum;
			}
			if(nextWeekWorkNum>maxNum){
				maxNum = nextWeekWorkNum;
			}
			weekReport.setMaxRow(maxNum);
			weekReport.setWeekWorkSummary(objs[8]+"");
			weekReport.setId(Integer.parseInt(objs[0]+""));
			weekReport.setUserName(staffService.getRealNameByUserId(objs[7]+""));
			weekReport.setUserId(objs[7]+"");
			weekReport.setAddTime((Date)objs[1]);
			List<Group> groups = identityService.createGroupQuery().groupMember(objs[7]+"").list();
			List<String> groupList = Lists2.transform(groups, new SafeFunction<Group, String>() {
				@Override
				protected String safeApply(Group input) {
					String[] positionIDs = input.getType().split("_");
					String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
					String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
							.getDepartmentName();
					String positionName = positionService.getPositionByPositionID(Integer.parseInt(positionIDs[2]))
							.getPositionName();
					return companyName + " — " + departmentName + " — " + positionName;
				}
			});
			weekReport.setGroupList(groupList);
			weekReportList.add(weekReport);
		}
		String sqlCount = "SELECT\n" +
				"	count(wr.id)\n" +
				"FROM\n" +
				"	OA_WeekReport wr,\n" +
				"	OA_GroupDetail gr,\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			ACT_ID_MEMBERSHIP\n" +
				"		GROUP BY\n" +
				"			USER_ID_\n" +
				"	) ship\n" +
				"WHERE\n" +
				"	wr.userId = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = gr.GroupID\n" +
				"AND wr.isDeleted = 0\n";
		if(StringUtils.isNotBlank(staffId)){
			sqlCount += "AND userId='"+staffId+"'\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			sqlCount += "AND Date(wr.addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlCount += "AND Date(wr.addTime)<='"+endDate+"'\n";
		}
		if(StringUtils.isNotBlank(companyId)){
			sqlCount += "AND gr.CompanyID = "+companyId+"\n";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(weekReportList, count);
	}
	@Override
	public List<Object> findNeedWeekReportPersons(String userName, String companyId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT\n" +
				"	reporter.userName, company.CompanyName, dep.DepartmentName, reporter.id, reporter.partner\n" +
				"FROM\n" +
				"	OA_WeekReporter reporter,\n" +
				"	(\n" +
				"		SELECT\n" +
				"			*\n" +
				"		FROM\n" +
				"			ACT_ID_MEMBERSHIP\n" +
				"		GROUP BY\n" +
				"			USER_ID_\n" +
				"	) ship,\n" +
				"	OA_GroupDetail groupDetail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep, oa_staff staff\n" +
				"WHERE reporter.isDeleted=0\n" +
				"and staff.userId=reporter.userId and staff.status!=4\n" +
				"AND reporter.userId = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = groupDetail.GroupID\n" +
				"AND groupDetail.CompanyID = company.CompanyID\n" +
				"AND groupDetail.DepartmentID = dep.DepartmentID\n";
		if(StringUtils.isNotBlank(companyId)){
			sql += "AND groupDetail.CompanyID=:companyId\n";
		}
		if(StringUtils.isNotBlank(userName)){
			sql += "AND reporter.userName like :userName";
		}
		SQLQuery  query = session.createSQLQuery(sql);
		if(StringUtils.isNotBlank(companyId)){
			query.setParameter("companyId", companyId);
		}
		if(StringUtils.isNotBlank(userName)){
			query.setParameter("userName", "%"+userName+"%");
		}
		@SuppressWarnings("unchecked")
		List<Object> weekReporter = query.list();  
		return weekReporter;
	}
	@Override
	public void addWeekReporter(WeekReporterEntity weekReporter) {
		weekReporter.setIsDeleted(0);
		weekReporter.setAddTime(new Date());
		baseDao.hqlSave(weekReporter);
	}
	@Override
	public void deleteWeekReporter(String reporterId) {
		String sql = "update OA_WeekReporter set isDeleted=1 where id="+reporterId;
		baseDao.excuteSql(sql);
	}
	@Override
	public List<WeekReportEntity> findWeekReportsByDate(Integer companyID, String beginDate, String endDate) throws Exception {
		String sql = "";
		if(StringUtils.isNotBlank(beginDate)){
			sql = "SELECT\n" +
					"	reporter.userId\n" +
					"FROM\n" +
					"	OA_WeekReporter reporter,\n" +
					"	(\n" +
					"		SELECT\n" +
					"			*\n" +
					"		FROM\n" +
					"			ACT_ID_MEMBERSHIP\n" +
					"		GROUP BY\n" +
					"			USER_ID_\n" +
					"	) ship,\n" +
					"	OA_GroupDetail groupDetail, OA_Staff staff\n" +
					"WHERE\n" +
					"	reporter.userId NOT IN (\n" +
					"		SELECT\n" +
					"			report.userId\n" +
					"		FROM\n" +
					"			OA_WeekReport report\n" +
					"		WHERE\n" +
					"			DATE(report.addTime) >= '"+beginDate+"'\n" +
					"		AND DATE(report.addTime) <= '"+endDate+"'\n" +
					"	)\n" +
					"AND staff.isDeleted=0 and staff.status!=4 and staff.userId=reporter.userId\n" +
					"AND reporter.isDeleted = 0\n" +
					"AND groupDetail.IsDeleted = 0\n" +
					"AND reporter.userId = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = groupDetail.GroupID\n" +
					"AND groupDetail.CompanyID = "+companyID;
		}else{
			//获取上周的日期区间
			String[] lastWeekTime = DateUtil.getLastTimeInterval();
			//上周一
			String lastMonday = lastWeekTime[0];
			//获取本周的日期区间
			String[] thisWeekTime = DateUtil.getTimeInterval();
			//本周一
			String thisMonday = thisWeekTime[0];
			sql = "SELECT\n" +
					"	reporter.userId\n" +
					"FROM\n" +
					"	OA_WeekReporter reporter,\n" +
					"	(\n" +
					"		SELECT\n" +
					"			*\n" +
					"		FROM\n" +
					"			ACT_ID_MEMBERSHIP\n" +
					"		GROUP BY\n" +
					"			USER_ID_\n" +
					"	) ship,\n" +
					"	OA_GroupDetail groupDetail, OA_Staff staff\n" +
					"WHERE\n" +
					"	reporter.userId NOT IN (\n" +
					"		SELECT\n" +
					"			report.userId\n" +
					"		FROM\n" +
					"			OA_WeekReport report\n" +
					"		WHERE\n" +
					"			DATE(report.addTime) >= '"+lastMonday+"'\n" +
					"		AND DATE(report.addTime) <= '"+thisMonday+"'\n" +
					"	)\n" +
					"AND staff.isDeleted=0 and staff.status!=4 and staff.userId=reporter.userId\n" +
					"AND reporter.isDeleted = 0\n" +
					"AND groupDetail.IsDeleted = 0\n" +
					"AND reporter.userId = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = groupDetail.GroupID\n" +
					"AND groupDetail.CompanyID = "+companyID;
		}
		List<Object> objs = baseDao.findBySql(sql);
		List<WeekReportEntity> weeReportVos = new ArrayList<>();
		for(Object obj: objs){
			WeekReportEntity weekReport = new WeekReportEntity();
			String reporter = staffService.getRealNameByUserId(obj+"");
			weekReport.setUserName(reporter);
			List<Group> groups = identityService.createGroupQuery().groupMember(obj+"").list();
			List<String> groupList = Lists2.transform(groups, new SafeFunction<Group, String>() {
				@Override
				protected String safeApply(Group input) {
					String[] positionIDs = input.getType().split("_");
					String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
							.getDepartmentName();
					String positionName = positionService.getPositionByPositionID(Integer.parseInt(positionIDs[2]))
							.getPositionName();
					return departmentName + " — " + positionName;
				}
			});
			weekReport.setGroupList(groupList);
			weeReportVos.add(weekReport);
		}
		return weeReportVos;
	}
}
