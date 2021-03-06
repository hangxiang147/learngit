package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.HashMultimap;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.WorkReportDao;
import com.zhizaolian.staff.entity.WorkReportEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.CheckDateUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
import com.zhizaolian.staff.vo.WorkReportVO;

public class WorkReportServiceImpl implements WorkReportService {
	@Autowired
	private WorkReportDao workReportDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private BaseDao baseDao; 
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private IdentityService identityService;
	@Override
	public void saveWorkReport(WorkReportVO workReport) {

		for(int i=0;i<workReport.getWorkContent().length;i++){
			WorkReportEntity workReportEntity = WorkReportEntity.builder()
					.userID(workReport.getUserID())
					.reportDate(DateUtil.getSimpleDate(workReport.getReportDate()))
					.weekDay(workReport.getWeekDay())
					.workContent(workReport.getWorkContent()[i])
					.quantity(workReport.getQuantities()[i]==null?1:workReport.getQuantities()[i])
					.assignTaskUserID(workReport.getAssignTaskUserID()[i]==null?"":workReport.getAssignTaskUserID()[i])
					.completeState(workReport.getCompleteState()[i])
					.workHours(workReport.getWorkHours()[i])
					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
					.addTime(new Date())
					.updateTime(new Date())
					.build();
			workReportDao.save(workReportEntity);

		}
	}


	@Override
	public ListResult<WorkReportDetailVO> findWorkReportListByUserID(WorkReportDetailVO workReportDetailVO,int page,int limit) {
		List<Object> limitedMsg=baseDao.findPageList(getLimitSql(workReportDetailVO),page,limit);
		StringBuffer sb = new StringBuffer();
		if(!CollectionUtils.isEmpty(limitedMsg)){
			sb.append(" and (");
			for(int i=0,length=limitedMsg.size();i<length;i++){
				Object[] currentObject=(Object[]) limitedMsg.get(i);
				sb.append(" (").append(" workReport.userId='").append(""+currentObject[0]).append("' and workReport.reportDate='").append(DateUtil.getDayStr((Date)currentObject[1])).append("') or ");
			}
			sb.delete(sb.lastIndexOf("or"),sb.length());
			sb.append(" ) ");
		}else{
			sb.append(" and 1=0 ");
		}
		List<Object> workReportEntities=workReportDao.findWorkreportListByUserID(getQuerySqlByWorkReportDetailVO(workReportDetailVO,sb.toString()));
		List<WorkReportDetailVO> workReportDetailVOs=new ArrayList<WorkReportDetailVO>();
		for(Object obj:workReportEntities){
			Object[] objs = (Object[]) obj;
			WorkReportDetailVO workReportDetailVO1=new WorkReportDetailVO();
			workReportDetailVO1.setReportDate(DateUtil.formateDate((Date) objs[0]));
			workReportDetailVO1.setName((String) objs[1]);
			String[] contents=StringUtils.split((String) objs[2], "|||");
			String[] assignTasks=StringUtils.splitPreserveAllTokens((String) objs[3], ",");
			String[] completes=StringUtils.split((String) objs[4], ",");
			String[] works=StringUtils.split((String) objs[5], ",");
			workReportDetailVO1.setAddTime(DateUtil.formateFullDate((Date) objs[6]));
			String[] quantity=StringUtils.splitPreserveAllTokens((String) objs[7], ",");
			workReportDetailVO1.setWeekDay((String)objs[8]);
			workReportDetailVO1.setTotalHours((Double) objs[9]);


			List<String> workContent=new ArrayList<>();
			for(int i = 0;i<contents.length;i++){
				String content=String.valueOf(contents[i]);
				workContent.add(content);
			}			
			workReportDetailVO1.setWorkContent(workContent);
			List<Integer> quantities=new ArrayList<>();
			for(int i=0;i<quantity.length;i++){
				Integer number=1;
				if(!StringUtils.isBlank(quantity[i])){
					number=Integer.parseInt(quantity[i]);
				}
				quantities.add(number);
			}
			workReportDetailVO1.setQuantities(quantities);


			List<String> completeState=new ArrayList<>();
			for(int i = 0;i<completes.length;i++){
				String complete=String.valueOf(completes[i]);
				completeState.add(complete);
			}
			workReportDetailVO1.setCompleteState(completeState);


			List<Double> workHours=new ArrayList<>();
			for(int i=0;i<works.length;i++){
				Double work=Double.valueOf(works[i]);
				workHours.add(work);
			}
			workReportDetailVO1.setWorkHours(workHours);


			List<String> assignTaskUserName=new ArrayList<>();
			for(int i=0;i<assignTasks.length;i++){
				String name="";
				if(!StringUtils.isBlank(assignTasks[i])){
					name=String.valueOf(staffService.getStaffByUserID(assignTasks[i]).getLastName());
				}
				assignTaskUserName.add(name);

			}
			workReportDetailVO1.setAssignTaskName(assignTaskUserName);

			workReportDetailVOs.add(workReportDetailVO1);
		}

		Object countObj = baseDao.getUniqueResult(getCountSql(workReportDetailVO));
		int count = countObj==null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<WorkReportDetailVO>(workReportDetailVOs,count);
	}
	private String  getLimitSql(WorkReportDetailVO workReportDetailVO){

		String prevSql="	SELECT\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate\n" +
				"		FROM\n" +
				"			OA_Workreport workReport\n" +
				"		LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID\n" ;
		if(workReportDetailVO.getCompanyID()!=null){
			prevSql+="		LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_\n" +
					"		LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_\n" +
					"		LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID\n" ;
		}
		prevSql+=	"		WHERE\n" +
				"			staff.IsDeleted = 0\n" +
				"		AND staff. STATUS != 4\n" +
				"		AND workReport.IsDeleted = 0\n" 
				+getWhereByWorkReportDetailVO(workReportDetailVO)+
				"		GROUP BY\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate"+
				"	order by workReport.reportDate desc,workReport.ReportId  asc ";

		return prevSql;
	}
	private String  getCountSql(WorkReportDetailVO workReportDetailVO){
		String prevSql="SELECT\n" +
				"	count(*) AS total\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			count(*)\n" +
				"		FROM\n" +
				"			OA_Workreport workReport\n" +
				"		LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID\n" ;
		if(workReportDetailVO.getCompanyID()!=null){
			prevSql+="		LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_\n" +
					"		LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_\n" +
					"		LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID\n" ;
		}
		prevSql+=	"		WHERE\n" +
				"			staff.IsDeleted = 0\n" +
				"		AND staff. STATUS != 4\n" +
				"		AND workReport.IsDeleted = 0\n" 
				+getWhereByWorkReportDetailVO(workReportDetailVO)+
				"		GROUP BY\n" +
				"			workReport.userId,\n" +
				"			workReport.reportDate\n" +
				"	) u";

		return prevSql;
	}

	String getQuerySqlByWorkReportDetailVO(WorkReportDetailVO workReportDetailVO,String tailStr){
		StringBuffer sql=new StringBuffer("SELECT s.ReportDate, s.StaffName, GROUP_CONCAT(s.WorkContent order by s.reportID separator '|||'), "
				+ "GROUP_CONCAT(s.AssignTaskUserID order by s.reportID), GROUP_CONCAT(s.CompleteState order by s.reportID), GROUP_CONCAT(s.WorkHours order by s.reportID), "
				+ "s.AddTime,GROUP_CONCAT(s.Quantity order by s.reportID),s.WeekDay, SUM(s.WorkHours) from( "
				+ "select DISTINCT * from (SELECT workReport.ReportDate, staff.StaffName, workReport.WorkContent, "
				+ "workReport.AssignTaskUserID, workReport.CompleteState, workReport.WorkHours, workReport.AddTime,workReport.Quantity,workReport.WeekDay,workReport.reportID "
				+ "FROM OA_Workreport workReport "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_ "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID "				
				+ "WHERE staff.IsDeleted = 0 and staff.Status != 4 and workReport.IsDeleted = 0 ");
		sql.append(tailStr);
		sql.append(getWhereByWorkReportDetailVO(workReportDetailVO));
		sql.append(" )a) s");

		sql.append(" GROUP BY s.StaffName,s.ReportDate order by s.ReportDate desc,s.ReportId asc ");
		return sql.toString();

	}

/*	private String getQueryCountSql(WorkReportDetailVO workReportDetailVO){

		StringBuffer sql=new StringBuffer("select count(*) from (SELECT staff.StaffName, workReport.ReportDate FROM OA_Workreport workReport "			
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = workReport.UserID "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_GROUP idGroup ON membership.GROUP_ID_ = idGroup.ID_ "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON idGroup.ID_ = groupDetail.GroupID "		
				+ "WHERE workReport.UserID = staff.UserID "
				+ " and staff.IsDeleted = 0 and staff.Status != 4 and workReport.IsDeleted = 0 ");

		sql.append(getWhereByWorkReportDetailVO(workReportDetailVO));
		sql.append("  GROUP BY workReport.ReportDate, staff.StaffName ) s");

		return sql.toString();

	} */  
	private String getWhereByWorkReportDetailVO(WorkReportDetailVO workReportDetailVO){
		StringBuffer whereSql=new StringBuffer();
		if (!StringUtils.isBlank(workReportDetailVO.getBeginDate())) {
			whereSql.append(" and workReport.reportDate >= '"+workReportDetailVO.getBeginDate()+"'");
		}
		if (!StringUtils.isBlank(workReportDetailVO.getEndDate())) {
			whereSql.append(" and workReport.reportDate <= '"+workReportDetailVO.getEndDate()+"'");
		}
		if(workReportDetailVO.getUserID()!=null){
			whereSql.append(" and workReport.UserID = '"+workReportDetailVO.getUserID()+"'");
		}
		if (!StringUtils.isBlank(workReportDetailVO.getName())) {
			whereSql.append(" and staff.StaffName like '%"+workReportDetailVO.getName()+"%' ");
		}
		if (!StringUtils.isBlank(workReportDetailVO.getReportDate())) {
			whereSql.append(" and workReport.reportDate = '"+workReportDetailVO.getReportDate()+"'");
		}
		if (workReportDetailVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = "+workReportDetailVO.getCompanyID());
			if (workReportDetailVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(workReportDetailVO.getCompanyID(), workReportDetailVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(workReportDetailVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}

		return whereSql.toString();
	}


	@Override
	public List<WorkReportVO> findWorkReportsByDate(Integer companyID,String date1) throws Exception {
		String date;
		if(StringUtils.isBlank(date1)){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = DateUtil.formateDate(cal.getTime());
		}else{
			date = date1;
		}
		String sql1 = "select * from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate = '"+date+"' and attendance.companyID='"+companyID+"' and "
				+ "attendance.isDeleted = 0 and  staff.positionCategory = 1 and staff.isDeleted=0  and staff.status !=4 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
				+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport where not exists(select special.userID from OA_Special special where special.type=1 and special.isDeleted = 0 "
				+ "and special.userID=noWorkReport.userID )";		
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select * from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate = '"+date+"' and attendance.companyID='"+companyID+"' and "
				+ "attendance.isDeleted = 0 and  staff.positionCategory = 2 and staff.isDeleted=0 and staff.status !=4 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
				+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport left join OA_Special special on noWorkReport.userID=special.userID where special.type = 1 and special.isDeleted = 0";
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);


		List<WorkReportVO> workReportVOs=new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			WorkReportVO workReportVO=new WorkReportVO();
			workReportVO.setUserID((String)objs[0]);
			workReportVO.setUserName(staffService.getStaffByUserID((String)objs[0]).getLastName());
			workReportVO.setReportDates(new String[]{date});
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[0]);
			workReportVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
				}
			}));
			workReportVOs.add(workReportVO);
		}
		return workReportVOs;
		//只有一天 结束时间和开始时间都是同一天
		//return workReportFilter(workReportVOs,date,date,companyID);
	}

	//假如 是beginTime: 2017-01-01 endTime: 2017-01-31
	//那么 时间区间 就是 2017-01-01 00:00:00到2017-02-01 00:00:00(加上一天)
	//我们需要知道 这个时间段 请过假的人     假如 请假 日期 为 2017-01-28 00:00:00到 2017-02-01 18:00:00 也应该算作这段时间内请过假的记录
	//所以 条件 应该是  开始时间 小于等于 2017-02-01 00:00:00 结束时间 大于等于 2017-01-01 00:00:00
	private List<WorkReportVO> workReportFilter(List<WorkReportVO> workReportVOs,String beginTime,String endTime, Integer companyId) throws Exception{
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(DateUtil.parseDay(endTime));
		calendar.add(Calendar.DATE, 1);
		endTime=DateUtil.getDayStr(calendar.getTime());
		if(CollectionUtils.isEmpty(workReportVOs))return workReportVOs;
		//查询signinVOs 中所有的 用户
		List<String> userIds=new ArrayList<String>();
		for(int i=0,length=workReportVOs.size();i<length;i++){
			userIds.add(workReportVOs.get(i).getUserID());
		}
		String sql="SELECT\n" +
				"	UserID,\n" +
				"	BeginDate,\n" +
				"	EndDate\n" +
				"FROM\n" +
				"	OA_Vacation v,\n" +
				"	ACT_HI_PROCINST p\n" +
				"WHERE\n (v.ProcessInstanceID is null or (" +
				"	v.ProcessInstanceID = p.PROC_INST_ID_\n" +
				"AND p.END_TIME_ IS NOT NULL\n" +
				"AND v.ProcessStatus = '"+TaskResultEnum.AGREE.getValue()+"' )) \n" +
				"AND v.UserID IN (\n" ;
		StringBuffer sb = new StringBuffer();
		for(int i=0,length=userIds.size();i<length;i++){
			sb.append("'").append(userIds.get(i)).append("',");
		}
		sb.deleteCharAt(sb.length()-1);
		sql+=sb.toString();
		sql+=")\n" +
				"and v.EndDate>='"+beginTime+"'\n" +
				"and v.BeginDate<='"+endTime+"' "+
				"and v.IsDeleted=0 ";
		List<Object> sqlResultList=baseDao.findBySql(sql);
		if(CollectionUtils.isEmpty(sqlResultList))return workReportVOs;
		HashMultimap<String, Date[]> vacationMap = HashMultimap.create(); 
		for(int i=0,length=sqlResultList.size();i<length;i++){
			Object[] currentData=(Object[]) sqlResultList.get(i);
			String userId=currentData[0]+"";
			Date startDate=(Date) currentData[1];
			Date endDate=(Date) currentData[2];
			vacationMap.put(userId,new Date[]{startDate,endDate});
		}
		Iterator<WorkReportVO> it = workReportVOs.iterator();
		while(it.hasNext()){
			WorkReportVO workReportVO = it.next();
			//			    System.out.print(workReportVO.getUserID()+":"+workReportVO.getUserName());
			String[] times=workReportVO.getReportDates();
			if(null == companyId){
				List<Group> groups = identityService.createGroupQuery().groupMember(workReportVO.getUserID()).list();
				if(null!=groups && groups.size()>0){
					companyId = Integer.parseInt(groups.get(0).getType().split("_")[0]);
				}
			}
			String[] dates=getEffectiveTime(times,vacationMap.get(workReportVO.getUserID()), companyId);
			if(dates==null||dates.length==0)
				it.remove(); 
			else{
				workReportVO.setCount(dates.length);
				workReportVO.setReportDates(dates);
			}
		}
		return workReportVOs;

	}
	/**
	 * 过滤掉VacationTimes 中包含的日期(8:30-11:20 12:10-18:00)
	 * @param times
	 * @param VacationTimes
	 * @return
	 * @throws Exception 
	 */
	private String[] getEffectiveTime(String[] times,Set<Date[]> vacationTimes, Integer companyId) throws Exception{
		List<String> filterArray=new ArrayList<String>(times.length);
		for (String timeStr : times) {
			Date time=DateUtil.parseDay(timeStr);
			if(time==null)continue;
			//				System.out.println("未签到时间:"+DateUtil.formateDate(time));
			if(!isInVacationTimes(time,vacationTimes, companyId)){
				filterArray.add(timeStr);
			}
		}
		return CollectionUtils.isEmpty(filterArray)?null:filterArray.toArray(new String[]{}) ;
	}
	private boolean isInVacationTimes(Date time,Set<Date[]> vacationTimes, Integer companyId) throws Exception{
		String[] arr_times = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId+"", "", DateUtil.formateDate(time));
		return CheckDateUtil.isVacation(time, vacationTimes, companyId, arr_times);
		//			for (Date[] dates: vacationTimes){ 
		//				boolean  result=isInVacationTime(time,dates[0],dates[1]);
		////					System.out.println("开始时间:"+DateUtil.formateFullDate(dates[0])+",结束时间:"+DateUtil.formateFullDate(dates[1])+",结果:"+result);
		//				if(result)return true;
		//			}
		//			return false;

	}
/*	private boolean isInVacationTime(Date time,Date startTime,Date endTime){
		Date[] dates=DateUtil.getJobBeginTimeAndEndTime(time);
		if(dates==null){
			return false;
		}
		return dates[0].compareTo(startTime)>=0&&dates[1].compareTo(endTime)<=0;
	}*/
	@Override
	public List<WorkReportVO> findStatisticsByMonth(Integer companyID, Date date) throws Exception {
		String firstDay = DateUtil.getFirstDayofMonth(date);
		String lastDay = DateUtil.getLastDayofMonth(date);
		String sql1 = "select noWorkReport.userID,count(*),GROUP_CONCAT(noWorkReport.attendanceDate order by noWorkReport.attendanceDate desc) from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate >= '"+firstDay+"' and attendance.attendanceDate <= '"+lastDay+"' and attendance.companyID="+companyID+" and "
				+ "attendance.isDeleted = 0 and  staff.positionCategory = 1 and staff.isDeleted=0  and staff.status !=4 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
				+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport where not exists(select special.userID from OA_Special special where special.type=1 and special.isDeleted = 0 "
				+ "and special.userID=noWorkReport.userID ) group by noWorkReport.userID";		
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select noWorkReport.userID,count(*),GROUP_CONCAT(noWorkReport.attendanceDate order by noWorkReport.attendanceDate desc ) from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate >= '"+firstDay+"' and attendance.attendanceDate <= '"+lastDay+"' and attendance.companyID="+companyID+" and "
				+ "attendance.isDeleted = 0 and  staff.positionCategory = 2 and staff.isDeleted=0 and staff.status !=4 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
				+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport left join OA_Special special on noWorkReport.userID=special.userID where special.type = 1 and special.isDeleted = 0 "
				+ "group by noWorkReport.userID";		
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);
		List<WorkReportVO> workReportVOs=new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			WorkReportVO workReportVO=new WorkReportVO();
			workReportVO.setUserID((String)objs[0]);
			if(!StringUtils.isBlank((String)objs[0])){
				workReportVO.setUserName(staffService.getStaffByUserID((String)objs[0]).getLastName());
			}
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[0]);
			for(GroupDetailVO group:groups){
				if(group.getCompanyID()==companyID){
					workReportVO.setDepartmentName(group.getDepartmentName());
				}
			}
			workReportVO.setCount(Integer.parseInt(objs[1].toString()));
			workReportVO.setReportDates(StringUtils.split((String) objs[2], ",") );
			workReportVOs.add(workReportVO);
		}

		return workReportFilter(workReportVOs,firstDay,lastDay,companyID);
	}


	@Override
	public List<WorkReportVO> findWorkReportByDateAndUserID(String date, String userID) {
		List<WorkReportEntity> workReportEntities = workReportDao.findWorkReportByDateAndUserID(date, userID);
		List<WorkReportVO> workReportVOs=new ArrayList<>();
		for(WorkReportEntity workReportEntity:workReportEntities){
			WorkReportVO workReportVO=new WorkReportVO();
			workReportVO.setWorkContents(workReportEntity.getWorkContent());
			workReportVO.setWeekDay(workReportEntity.getWeekDay());
			if(workReportEntity.getQuantity()!=null){
				workReportVO.setQuantity(workReportEntity.getQuantity());
			}
			if(!StringUtils.isBlank(workReportEntity.getAssignTaskUserID())){
				workReportVO.setAssignTaskUserNames(staffService.getStaffByUserID(workReportEntity.getAssignTaskUserID()).getLastName());
			}
			workReportVO.setCompleteStates(workReportEntity.getCompleteState());
			workReportVO.setWorkHour(workReportEntity.getWorkHours());
			workReportVO.setAddTime(DateUtil.formateFullDate(workReportEntity.getAddTime()));
			workReportVOs.add(workReportVO);
		}
		return workReportVOs;
	}


	@Override
	public Map<String, WorkReportVO> findStatisticsByDate(Integer companyID, String beginDate, String endDate)
			throws Exception {
		String sql1 = "select noWorkReport.userID,count(*),GROUP_CONCAT(noWorkReport.attendanceDate order by noWorkReport.attendanceDate desc) from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate >= '"+beginDate+"' and attendance.attendanceDate <= '"+endDate+"'";
		if(null != companyID){
			sql1 += " and attendance.companyID="+companyID+" and "
					+ "attendance.isDeleted = 0 and  staff.positionCategory = 1 and staff.isDeleted=0 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
					+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport where not exists(select special.userID from OA_Special special where special.type=1 and special.isDeleted = 0 "
					+ "and special.userID=noWorkReport.userID ) group by noWorkReport.userID";
		}else{
			sql1 += " and attendance.isDeleted = 0 and  staff.positionCategory = 1 and staff.isDeleted=0 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
					+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport where not exists(select special.userID from OA_Special special where special.type=1 and special.isDeleted = 0 "
					+ "and special.userID=noWorkReport.userID ) group by noWorkReport.userID";
		}
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select noWorkReport.userID,count(*),GROUP_CONCAT(noWorkReport.attendanceDate order by noWorkReport.attendanceDate desc ) from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where attendance.attendanceDate >= '"+beginDate+"' and attendance.attendanceDate <= '"+endDate+"'";
		if(null != companyID){
			sql2 += " and attendance.companyID="+companyID+" and "
					+ "attendance.isDeleted = 0 and  staff.positionCategory = 2 and staff.isDeleted=0 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
					+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport left join OA_Special special on noWorkReport.userID=special.userID where special.type = 1 and special.isDeleted = 0 "
					+ "group by noWorkReport.userID";	
		}else{
			sql2 += " and attendance.isDeleted = 0 and  staff.positionCategory = 2 and staff.isDeleted=0 and not exists(select workReport.userID,workReport.reportDate from OA_Workreport workReport "
					+ "where workReport.userID=attendance.userID and attendance.attendanceDate=workReport.reportDate )) noWorkReport left join OA_Special special on noWorkReport.userID=special.userID where special.type = 1 and special.isDeleted = 0 "
					+ "group by noWorkReport.userID";	
		}
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);
		List<WorkReportVO> workReportVOs=new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			WorkReportVO workReportVO=new WorkReportVO();
			workReportVO.setUserID((String)objs[0]);
			if(!StringUtils.isBlank((String)objs[0])){
				workReportVO.setUserName(staffService.getStaffByUserID((String)objs[0]).getLastName());
			}
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[0]);
			for(GroupDetailVO group:groups){
				if(group.getCompanyID()==companyID){
					workReportVO.setDepartmentName(group.getDepartmentName());
				}
			}
			workReportVO.setCount(Integer.parseInt(objs[1].toString()));
			workReportVO.setReportDates(StringUtils.split((String) objs[2], ",") );
			workReportVOs.add(workReportVO);
		}

		Map<String, WorkReportVO> noReportMap = new HashMap<>();
		//workReportFilter(workReportVOs,beginDate,endDate,companyID);
		for(WorkReportVO workReportVO: workReportVOs){
			noReportMap.put(workReportVO.getUserID(), workReportVO);
		}
		return noReportMap;
	}
}
