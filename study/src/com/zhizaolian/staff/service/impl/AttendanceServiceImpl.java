package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
//import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.google.common.collect.HashMultimap;
import com.zhizaolian.staff.dao.AttendanceDetailDao;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.SigninDao;
import com.zhizaolian.staff.dao.VacationDao;
import com.zhizaolian.staff.entity.AttendanceDetailEntity;
import com.zhizaolian.staff.entity.MonthlyRestEntity;
import com.zhizaolian.staff.entity.PartnerOptionEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.SigninEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.UserMonthlyRestEntity;
import com.zhizaolian.staff.entity.WorkRestArrangeEntity;
import com.zhizaolian.staff.entity.WorkRestTimeEntity;
import com.zhizaolian.staff.enums.AttendanceBeginType;
import com.zhizaolian.staff.enums.AttendanceEndType;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.CheckDateUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.PoiUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.SigninVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.WorkReportVO;
public class AttendanceServiceImpl implements AttendanceService {
	private Logger logger = Logger.getLogger(AttendanceServiceImpl.class);
	@Autowired
	private AttendanceDetailDao attendanceDetailDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private VacationDao vacationDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private SigninDao signinDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private WorkOvertimeService workOvertimeService;
	@Autowired
	private WorkReportService workReportService;
	//salary表格中有效的行
	private final static int[] EFFCTIVE_LINE={11,13,14,16,17,19,21,24,26,27,28,29,30,31,38,39,40,41};

	private ShortMsgSender shortMsgSender = ShortMsgSender.getInstance();
	@Override
	public void parseSalary(String fileName,int year,int month) throws Exception {
		File file = new File(Constants.SALARY_FILE_DIRECTORY, fileName);
		try(FileInputStream fis = new FileInputStream(file)){
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheetAt(0);
			int rowNum = sheet.getLastRowNum() + 1;

			for (int i =2; i < rowNum; ++i) {
				try{
					Row row = sheet.getRow(i);
					if(null == row){
						continue;
					}
					Cell cell = row.getCell(3);
					if(cell==null)continue;
					String value=PoiUtil.getCellValue(cell)[1];
					String number = String.format("%6d", Integer.parseInt(value))
							.replace(" ", "0");
					User user = identityService.createUserQuery().userFirstName(number).singleResult();
					String department="";
					Cell cellDepartment = row.getCell(1);
					if(cellDepartment!=null){
						department=PoiUtil.getCellValue_(cellDepartment)[1];
					}
					List<String> extraMsg=new ArrayList<>();
					PartnerOptionEntity partnerOption = null;
					for(int j=0;j<=41;j++){
						if(ArrayUtils.contains(EFFCTIVE_LINE, j)){
							cell = row.getCell(j);
							if(cell==null){
								extraMsg.add("");
							}else{
								String money = PoiUtil.getCellValue_(cell)[1];
								extraMsg.add(money);	
								//主动放弃的工资，用于购买公司股份
								if(j==41 && StringUtils.isNotBlank(money)){
									String reg = "^\\d+(\\.\\d+)?$";
									if(money.matches(reg)){
										double $money = Double.parseDouble(money);
										if($money>0){
											partnerOption = new PartnerOptionEntity();
											partnerOption.setAddTime(new Date());
											partnerOption.setIsDeleted(0);
											partnerOption.setMoney(money);
											partnerOption.setPurchaseDate(new Date());
											partnerOption.setPurchaseType(Constants.MONEY_PURCHASE);
											partnerOption.setStatus("0");//待匹配
											partnerOption.setUserId(user.getId());
										}
									}
								}
							}
						}
					}
					byte[] data=ObjectByteArrTransformer.toByteArray(extraMsg);
					if(user==null){
						throw new RuntimeException("第"+(i+1)+"行解析错误：工号不存在");
					}
					SalaryDetailEntity salaryDetailEntity=SalaryDetailEntity.builder().userId(user.getId()).name(staffService.getRealNameByUserId(user.getId())).departmentName(department).month(month).year(year).detail(data).build();
					SalaryDetailEntity salaryDetail = (SalaryDetailEntity) baseDao.hqlfindUniqueResult
							("from SalaryDetailEntity where year="+year +" and month="+month +" and userId='"+user.getId()+"'");
					baseDao.excuteSql("delete from OA_SalaryDetail where year="+year +" and month="+month +" and userId='"+user.getId()+"' ");
					if(null != salaryDetail){
						baseDao.excuteSql("update OA_PartnerOption set isDeleted=1 where salaryId="+salaryDetail.getId());
					}
					int id = baseDao.hqlSave(salaryDetailEntity);
					if(null != partnerOption){
						partnerOption.setSalaryId(id);
						baseDao.hqlSave(partnerOption);
					}
				}catch(Exception e){
					e.printStackTrace();
					throw new RuntimeException("第"+(i+1)+"行解析错误");
				}
			}
		} 
	}


	@SuppressWarnings("unchecked")
	@Override
	public ListResult<SalaryDetailEntity> getSalarysListByKey(String name,String email,String mobile, int year, int month,String type, int page, int limit) {
		String hqlList=" from SalaryDetailEntity s where s.year="+year+" and s.month="+month;
		if(StringUtils.isNotBlank(name)){
			hqlList+=" and s.name like '%"+name+"%'";
		}
		if(StringUtils.isNotBlank(type)){
			if("-1".equals(type)){
				hqlList+=" and (s.status is null or s.status='')  ";
			}else{
				hqlList+=" and s.status ="+type;				
			}
		}
		if(StringUtils.isNotBlank(email)){
			if("-1".equals(email)){
				hqlList+=" and (s.emailSend is null or s.emailSend =0) ";				
			}else{
				hqlList+=" and s.emailSend =1 ";
			}
		}
		if(StringUtils.isNotBlank(mobile)){
			if("-1".equals(mobile)){
				hqlList+=" and (s.mobileSend is null or s.mobileSend =0) ";				
			}else{
				hqlList+=" and s.mobileSend =1 ";
			}
		}
		String sqlCount="select count(*)  "+hqlList;
		List<SalaryDetailEntity> salaryDetailEntities=(List<SalaryDetailEntity>) baseDao.hqlPagedFind(hqlList, page, limit);
		int count=((Long)baseDao.hqlfindUniqueResult(sqlCount)).intValue();

		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(salaryDetailEntities)){
			for (SalaryDetailEntity salaryDetailEntity : salaryDetailEntities) {
				if(salaryDetailEntity.getDetail()!=null&&salaryDetailEntity.getDetail().length>0)
					try {
						salaryDetailEntity.setDetailList((List<String>) ObjectByteArrTransformer.toObject(salaryDetailEntity.getDetail()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return new ListResult<>(salaryDetailEntities, count);
	}


	@Override
	public void parseExcel(String fileName, int companyID, StringBuffer index) throws Exception {

		File file = new File(Constants.ATTENDANCE_FILE_DIRECTORY, fileName);
		// 初始化一个工作簿
		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = WorkbookFactory.create(fis);
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1; // 获得工作表行数，初始行数为0
		// 上传的Excel带有表头，所以从第二行开始，索引为1

		for (int i = 1; i < rowNum; ++i) {
			index.setLength(0);
			index.append(i+1);
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			//			// 没有工号的员工打卡记录，系统暂不记录
			//			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())
			//					|| !Character.isDigit(row.getCell(0).getStringCellValue().charAt(0))) {
			//				continue;
			//			}
			//
			//			String number = String.format("%6d", Integer.parseInt(row.getCell(0).getStringCellValue().substring(0, 6)))
			//					.replace(" ", "0");
			//			User user = identityService.createUserQuery().userFirstName(number).singleResult();
			//			Date attendanceDate = DateUtil.getSimpleDate(row.getCell(1).getStringCellValue().replace("/", "-"));
			//			String times = row.getCell(2).getStringCellValue();
			//			String note = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
			// 没有工号的员工打卡记录，系统暂不记录
			Cell cell = row.getCell(0);
			if (cell == null || StringUtils.isBlank(PoiUtil.getCellValue(cell)[1])
					|| !Character.isDigit(PoiUtil.getCellValue(cell)[1].charAt(0))) {
				continue;
			}

			String number = String.format("%6d", Integer.parseInt(PoiUtil.getCellValue(cell)[1].substring(0, 6)))
					.replace(" ", "0");
			User user = identityService.createUserQuery().userFirstName(number).singleResult();
			cell = row.getCell(1);
			String[] typeAndVal = PoiUtil.getCellValue(cell);
			Date attendanceDate = null;
			//判断是时间还是字符串
			if("时间".equals(typeAndVal[0])){
				attendanceDate = DateUtil.getSimpleDate(DateUtil.formateDate(DateUtil.getFullDate(typeAndVal[1])));
			}else{
				attendanceDate = DateUtil.getSimpleDate(typeAndVal[1].replace("/", "-"));
			}
			cell = row.getCell(2);
			typeAndVal = PoiUtil.getCellValue(cell);
			String times = "";
			if("时间".equals(typeAndVal[0])){
				times = DateUtil.formateTime(DateUtil.getFullDate(typeAndVal[1]));
			}else{
				times = PoiUtil.getCellValue(cell)[1];
			}
			cell = row.getCell(3);
			String note = cell == null ? "" : PoiUtil.getCellValue(cell)[1];
			//是否出差
			cell = row.getCell(4);
			String trip = cell == null ? "" : PoiUtil.getCellValue(cell)[1];
			boolean isTrip = false;
			//0表示出差，出差人员的考勤按照当地的作息时间来
			if("0".equals(trip)){
				isTrip = true;
			}
			saveAttendanceDetail(user.getId(), attendanceDate, times, companyID, note, isTrip);
		}
	}


	@Override
	public ListResult<AttendanceVO> findAttendancePageListByAttendanceVO(AttendanceVO attendanceVO, int page,
			int limit) {
		if (attendanceVO == null) {
			throw new RuntimeException("获取查询条件失败");
		}
		List<Object> result = baseDao.findPageList(getQuerySqlByAttendanceVO(attendanceVO), page, limit);
		List<AttendanceVO> attendanceVOs = Lists2.transform(result, new SafeFunction<Object, AttendanceVO>() {
			@Override
			protected AttendanceVO safeApply(Object input) {
				AttendanceVO output = new AttendanceVO();
				Object[] objs = (Object[]) input;
				output.setName((String) objs[0]);
				output.setCompanyName(CompanyIDEnum.valueOf((Integer) objs[1]) == null ? ""
						: CompanyIDEnum.valueOf((Integer) objs[1]).getName());
				output.setAttendanceDate(objs[2] == null ? "" : DateUtil.formateDate((Date) objs[2]));
				output.setAttendanceTime((String) objs[3]);
				AttendanceBeginType beginType = AttendanceBeginType.valueOf((Integer) objs[4]);
				output.setNote((String) objs[8]);
				String status = beginType == null ? "" : "上班" + beginType.getName();
				if (AttendanceBeginType.LATE.equals(beginType) && (BigInteger) objs[6] != null) {
					status += (((BigInteger) objs[6]).longValue() / (1000 * 60) + "分钟，");
				}
				AttendanceEndType endType = AttendanceEndType.valueOf((Integer) objs[5]);
				status += endType == null ? "" : "下班" + endType.getName();
				if (AttendanceEndType.EARLY.equals(endType) && (BigInteger) objs[7] != null) {
					status += (((BigInteger) objs[7]).longValue() / (1000 * 60) + "分钟");
				}
				output.setStatusString(status);
				output.setBeginType((Integer) objs[4]);
				output.setAttendanceId((Integer) objs[9]);
				output.setLateStatus(objs[10]+"");
				return output;
			}
		});
		for (AttendanceVO attendance : attendanceVOs) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = null;
			try {
				date1 = sdf.parse(attendance.getAttendanceDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<SigninEntity> signinEntity = (List<SigninEntity>) signinDao.findSigninList(attendanceVO.getUserID(),
					date1);

			if (signinEntity.size() > 0) {
				attendance.setSignin(1);
			} else {
				attendance.setSignin(0);
			}

		}

		Object countObj = baseDao.getUniqueResult(getQueryCountSql(attendanceVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<AttendanceVO>(attendanceVOs, count);
	}


	@Override
	public XSSFWorkbook exportAttendancePageListByAttendanceVO(AttendanceVO attendanceVO) {
		if (attendanceVO == null) {
			throw new RuntimeException("获取查询条件失败");
		}

		List<Object> result = baseDao.findPageList(getQuerySqlByAttendanceVO(attendanceVO), 1, Integer.MAX_VALUE);
		List<AttendanceVO> attendanceVOs = Lists2.transform(result, new SafeFunction<Object, AttendanceVO>() {
			@Override
			protected AttendanceVO safeApply(Object input) {
				AttendanceVO output = new AttendanceVO();
				Object[] objs = (Object[]) input;
				output.setName((String) objs[0]);
				output.setCompanyName(CompanyIDEnum.valueOf((Integer) objs[1]) == null ? ""
						: CompanyIDEnum.valueOf((Integer) objs[1]).getName());
				output.setAttendanceDate(objs[2] == null ? "" : DateUtil.formateDate((Date) objs[2]));
				output.setAttendanceTime((String) objs[3]);
				AttendanceBeginType beginType = AttendanceBeginType.valueOf((Integer) objs[4]);
				output.setNote((String) objs[8]);
				String status = beginType == null ? "" : "上班" + beginType.getName();
				if (AttendanceBeginType.LATE.equals(beginType) && (BigInteger) objs[6] != null) {
					status += (((BigInteger) objs[6]).longValue() / (1000 * 60) + "分钟，");
				}
				AttendanceEndType endType = AttendanceEndType.valueOf((Integer) objs[5]);
				status += endType == null ? "" : "下班" + endType.getName();
				if (AttendanceEndType.EARLY.equals(endType) && (BigInteger) objs[7] != null) {
					status += (((BigInteger) objs[7]).longValue() / (1000 * 60) + "分钟");
				}
				output.setStatusString(status);
				return output;
			}
		});
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("考勤明细");
		XSSFRow row = sheet.createRow((int) 0);
		row.createCell((short) 0).setCellValue("姓名");
		row.createCell((short) 1).setCellValue("地区");
		row.createCell((short) 2).setCellValue("日期");
		row.createCell((short) 3).setCellValue("打卡记录");
		row.createCell((short) 4).setCellValue("考勤状态");
		row.createCell((short) 5).setCellValue("备注");

		for (int i = 0, j = sheet.getLastRowNum() + 1, length = attendanceVOs.size(); i < length; ++i, ++j) {
			XSSFRow row_data = sheet.createRow(j);
			AttendanceVO attendance = attendanceVOs.get(i);
			row_data.createCell((short) 0).setCellValue(attendance.getName());
			row_data.createCell((short) 1).setCellValue(attendance.getCompanyName());
			row_data.createCell((short) 2).setCellValue(attendance.getAttendanceDate());
			row_data.createCell((short) 3).setCellValue(attendance.getAttendanceTime());
			row_data.createCell((short) 4).setCellValue(attendance.getStatusString());
			row_data.createCell((short) 5).setCellValue(attendance.getNote());

		}
		return wb;
	}

	private String getQueryCountSql(AttendanceVO attendanceVO) {
		if(attendanceVO.getCompanyID() != null){
			StringBuffer sql = new StringBuffer("SELECT count(*)"
					+ "FROM (select distinct * from (SELECT staff.StaffName, attendance.CompanyID, attendance.AttendanceDate, "
					+ "attendance.AttendanceTime, attendance.BeginType, attendance.EndType, attendance.BeginDiff, attendance.EndDiff ,attendance.Note "
					+ "FROM OA_AttendanceDetail attendance, OA_Staff staff,ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail WHERE attendance.UserID = staff.UserID "
					+ " and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and staff.IsDeleted = 0 and staff.Status != 4 and attendance.IsDeleted = 0 ");
			sql.append(getWhereByAttendanceVO(attendanceVO));
			sql.append(" ) atte ) atteCount");
			return sql.toString();
		}else{
			StringBuffer sql = new StringBuffer("SELECT count(attendance.userId) "
					+ "FROM OA_AttendanceDetail attendance, OA_Staff staff WHERE attendance.UserID = staff.UserID "
					+ " and staff.IsDeleted = 0 and staff.Status != 4 and attendance.IsDeleted = 0 ");
			sql.append(getWhereByAttendanceVO(attendanceVO));
			return sql.toString();
		}
	}

	private String getQuerySqlByAttendanceVO(AttendanceVO attendanceVO) {
		if(attendanceVO.getCompanyID() != null){
			StringBuffer sql = new StringBuffer(
					"select distinct * from (SELECT staff.StaffName, attendance.CompanyID, attendance.AttendanceDate, "
							+ "attendance.AttendanceTime, attendance.BeginType, attendance.EndType, attendance.BeginDiff, attendance.EndDiff ,attendance.Note, attendance.AttendanceDetailID, attendance.lateStatus "
							+ "FROM OA_AttendanceDetail attendance, OA_Staff staff,ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail WHERE attendance.UserID = staff.UserID "
							+ " and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and staff.IsDeleted = 0 and staff.Status != 4 and attendance.IsDeleted = 0 ");
			sql.append(getWhereByAttendanceVO(attendanceVO));
			sql.append(" order by attendance.AttendanceDate desc) atte");
			return sql.toString();
		}else{
			StringBuffer sql = new StringBuffer(
					"SELECT staff.StaffName, attendance.CompanyID, attendance.AttendanceDate, "
							+ "attendance.AttendanceTime, attendance.BeginType, attendance.EndType, attendance.BeginDiff, attendance.EndDiff ,attendance.Note, attendance.AttendanceDetailID, attendance.lateStatus "
							+ "FROM OA_AttendanceDetail attendance, OA_Staff staff WHERE attendance.UserID = staff.UserID "
							+ " and staff.IsDeleted = 0 and staff.Status != 4 and attendance.IsDeleted = 0 ");
			sql.append(getWhereByAttendanceVO(attendanceVO));
			sql.append(" order by attendance.AttendanceDate desc");
			return sql.toString();
		}
	}

	private String getWhereByAttendanceVO(AttendanceVO attendanceVO) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(attendanceVO.getName())) {
			whereSql.append(" and staff.StaffName like '%" + attendanceVO.getName() + "%' ");
		}
		if (!StringUtils.isBlank(attendanceVO.getBeginDate())) {
			whereSql.append(" and attendance.AttendanceDate >= '" + attendanceVO.getBeginDate() + "'");
		}
		if (!StringUtils.isBlank(attendanceVO.getEndDate())) {
			whereSql.append(" and attendance.AttendanceDate <= '" + attendanceVO.getEndDate() + "'");
		}
		if (attendanceVO.getStatus() != null) {
			if (attendanceVO.getStatus() == AttendanceBeginType.NORMAL.getValue()) {
				whereSql.append(" and attendance.BeginType = " + AttendanceBeginType.NORMAL.getValue());
				whereSql.append(" and attendance.EndType = " + AttendanceEndType.NORMAL.getValue());
			} else {
				whereSql.append(" and (attendance.BeginType = " + attendanceVO.getStatus() + " or "
						+ "attendance.EndType = " + attendanceVO.getStatus() + ")");
			}
		}
		if (attendanceVO.getUserID() != null) {
			whereSql.append(" and attendance.UserID = '" + attendanceVO.getUserID() + "'");
		}
		if (attendanceVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = " + attendanceVO.getCompanyID());
			if (attendanceVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
						attendanceVO.getCompanyID(), attendanceVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(attendanceVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}

		return whereSql.toString();
	}

	@Override
	public void checkAttendanceDetail(Date attendanceDate, String userID) throws Exception {
		AttendanceDetailEntity attendance = attendanceDetailDao.getAttendanceByDateUserID(attendanceDate, userID);
		if (attendance == null) {
			return;
		}

		String[] times = attendance.getAttendanceTime().split(" ");
		if (times.length <= 0) {
			return;
		}
		List<GroupDetailVO> groupDetailVOs=staffService.findGroupDetailsByUserID(attendance.getUserID());
		if(CollectionUtils.isEmpty(groupDetailVOs)){
			return;
		}
		GroupDetailVO groupDetailVO = groupDetailVOs.get(0);
		String departmentId = groupDetailVO.getDepartmentID()+"";
		String companyId = groupDetailVO.getCompanyID()+"";
		String[] workTimeArray = getWorkRestTimeByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(attendanceDate));

		String date = DateUtil.formateDate(attendanceDate);
		//String workTimes = CompanyIDEnum.valueOf(attendance.getCompanyID()).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String beginTime = date + " " + workTimeArray[0] + ":00";
		String endTime = date + " " + workTimeArray[3] + ":00";
		//上午下班时间
		String amEndTime = date + " "+ workTimeArray[1]+":00";
		//下午上班时间
		String pmBeginTime = date + " "+ workTimeArray[2]+":00";
		//		String beginTime = date
		//				+ positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(attendance.getCompanyID()));
		//		String endTime = date + positionService.getEndTimeByCompanyID(CompanyIDEnum.valueOf(attendance.getCompanyID()));
		if (attendance.getBeginType() != AttendanceBeginType.NO_DATA.getValue()) {
			checkBeginTime(date + " " + times[0].trim() + ":00", beginTime, attendance, amEndTime, pmBeginTime);
		}
		if (attendance.getEndType() != AttendanceEndType.NO_DATA.getValue()) {
			checkEndTime(date + " " + times[times.length - 1].trim() + ":00", endTime, attendance, amEndTime, pmBeginTime);
		}

		attendanceDetailDao.save(attendance);
	}

	@Override
	public void checkAttendanceDetailsByVacationVO(VacationVO vacation) throws Exception {
		String beginDate = vacation.getBeginDate().split(" ")[0];
		String endDate = vacation.getEndDate().split(" ")[0];
		while (beginDate.compareTo(endDate) <= 0) {
			Date attendanceDate = DateUtil.getSimpleDate(beginDate);
			// 检查并修改考勤明细
			checkAttendanceDetail(attendanceDate, vacation.getRequestUserID());
			beginDate = DateUtil.getSpecifiedDayAfter(attendanceDate);
		}
	}

	private void saveAttendanceDetail(String userID, Date attendanceDate, String attendanceTime, int companyID,
			String note, boolean isTrip) throws Exception {
		String[] times = attendanceTime.split(" ");
		if (times.length <= 0) {
			return;
		}

		Date now = new Date();
		AttendanceDetailEntity attendanceDetail = AttendanceDetailEntity.builder().userID(userID).companyID(companyID)
				.attendanceDate(attendanceDate).attendanceTime(attendanceTime)
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(now).updateTime(now).note(note).build();
		//异地打卡：1、出差；2、非出差
		//非出差的异地打卡，按照母公司的作息时间考勤
		String departmentId = "";
		if(!isTrip){
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(userID);
			if (!CollectionUtils.isEmpty(groups)) {
				companyID = groups.get(0).getCompanyID();
				departmentId= groups.get(0).getDepartmentID()+"";
			}
		}
		//如果当天已经存在该条记录 删除
		AttendanceDetailEntity attendanceDetailEntity=attendanceDetailDao.getAttendanceByDateUserID(attendanceDate,userID);
		/*if(attendanceDetailEntity!=null){
			attendanceDetailDao.delete(attendanceDetailEntity);
		}*/
		//异地
		boolean differentAttendance = false;
		//修改之前删除的处理：如果查到只是打了一次卡，先删除，再合并
		if(attendanceDetailEntity!=null){
			//先删除
			attendanceDetailDao.delete(attendanceDetailEntity);
			String oldAttendanceTime = attendanceDetailEntity.getAttendanceTime();
			//只打了一次卡，合并
			if(oldAttendanceTime.split(" ").length==1 && attendanceTime.split(" ").length==1){
				Date oldAttendanceDate = DateUtil.getFullDate("2000-01-01 "+oldAttendanceTime+":00");
				Date newAttendanceDate = DateUtil.getFullDate("2000-01-01 "+attendanceTime+":00");
				//判断已上传的打卡时间和需要合并的打卡时间的先后顺序，相等不做处理
				if(oldAttendanceDate.before(newAttendanceDate)){
					attendanceDetail.setAttendanceTime(oldAttendanceTime+" "+attendanceTime);
					times = new String[]{oldAttendanceTime, attendanceTime};
				}else if(oldAttendanceDate.after(newAttendanceDate)){
					attendanceDetail.setAttendanceTime(attendanceTime+" "+oldAttendanceTime);
					times = new String[]{attendanceTime, oldAttendanceTime};
				}
				//异地打卡（老数据和新数据的公司Id不一致），对打卡数据进行排序。由于异地上下班时间不一样，早上上班时间取迟的，晚上下班时间取早的
			}else if(attendanceDetailEntity.getCompanyID() != companyID){
				differentAttendance = true;
				//对打卡时间进行排序
				String[] oldAttendanceTimeArray = oldAttendanceTime.split(" ");
				String[] attendanceTimeArray = attendanceTime.split(" ");
				List<String> attendanceTimeLst = new ArrayList<>();
				attendanceTimeLst.addAll(Arrays.asList(oldAttendanceTimeArray));
				attendanceTimeLst.addAll(Arrays.asList(attendanceTimeArray));
				Collections.sort(attendanceTimeLst);
				attendanceDetail.setAttendanceTime(getAttendanceTimeStr(attendanceTimeLst));
				//取出开始打卡时间和结束的打卡时间
				times = new String[2];
				times[0] = attendanceTimeLst.get(0);
				times[1] = attendanceTimeLst.get(attendanceTimeLst.size()-1);
				//取出老数据所在公司上下班时间
				//String[] oldDutyTime = CompanyIDEnum.valueOf(attendanceDetailEntity.getCompanyID()).getTimeLimitByDate(null).split(" ");
				String[] oldDutyTime = getWorkRestTimeByCompanyIDOrDepartmentId(attendanceDetailEntity.getCompanyID()+"", "", DateUtil.formateDate(attendanceDate));
				String oldBeginTime = oldDutyTime[0];
				String oldEndTime = oldDutyTime[3];
				//上午下班时间
				String oldAmEndTime = oldDutyTime[1];
				//下午上班时间
				String oldPmBeginTime = oldDutyTime[2];
				//取出新数据所在公司上下班时间
				//String[] dutyTime = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null).split(" ");
				String[] dutyTime = getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(attendanceDate));
				String newBeginTime = dutyTime[0];
				String newEndTime = dutyTime[3];
				//上午下班时间
				String newAmEndTime = dutyTime[1];
				//下午上班时间
				String newPmBeginTime = dutyTime[2];
				//早上上班时间取迟的
				List<String> dutyTimeLst = new ArrayList<>();
				dutyTimeLst.add(oldBeginTime);
				dutyTimeLst.add(newBeginTime);
				Collections.sort(dutyTimeLst);
				String beginTime = dutyTimeLst.get(1);
				dutyTimeLst.clear();
				//上午下班取早的
				dutyTimeLst.add(oldAmEndTime);
				dutyTimeLst.add(newAmEndTime);
				Collections.sort(dutyTimeLst);
				String amEndTime = dutyTimeLst.get(0);
				dutyTimeLst.clear();
				//下午上班取晚的
				dutyTimeLst.add(oldPmBeginTime);
				dutyTimeLst.add(newPmBeginTime);
				Collections.sort(dutyTimeLst);
				String pmBeginTime = dutyTimeLst.get(1);
				dutyTimeLst.clear();
				//晚上下班时间取早的
				dutyTimeLst.add(oldEndTime);
				dutyTimeLst.add(newEndTime);
				Collections.sort(dutyTimeLst);
				String endTime = dutyTimeLst.get(0);
				String date = DateUtil.formateDate(attendanceDate);
				beginTime = date+" "+beginTime+":00";
				endTime = date+" "+endTime+":00";
				amEndTime = date+" "+amEndTime+":00";
				pmBeginTime = date+" "+pmBeginTime+":00";
				checkBeginTime(date + " " + times[0].trim() + ":00", beginTime, attendanceDetail, amEndTime, pmBeginTime);
				checkEndTime(date + " " + times[times.length - 1].trim() + ":00", endTime, attendanceDetail, amEndTime, pmBeginTime);
			}
		}
		if(!differentAttendance){
			String date = DateUtil.formateDate(attendanceDate);
			//String[] arr_times=CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null).split(" ");
			String[] arr_times = getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId, DateUtil.formateDate(attendanceDate));
			String beginTime = date + " "+arr_times[0]+":00";
			String endTime = date  + " "+ arr_times[3]+":00";
			//上午下班时间
			String amEndTime = date + " "+arr_times[1]+":00";
			//下午上班时间
			String pmBeginTime = date + " "+arr_times[2]+":00";
			if (times.length == 1) {
				int hour = Integer.parseInt(times[0].split(":")[0]);
				if (hour < 12) {
					attendanceDetail.setEndType(AttendanceEndType.NO_DATA.getValue());
					checkBeginTime(date + " " + times[0].trim() + ":00", beginTime, attendanceDetail, amEndTime, pmBeginTime);
					try {
						sendAttendanceMsg("下班", attendanceDate, userID);
					} catch (Exception e) {
					}
				} else {
					attendanceDetail.setBeginType(AttendanceBeginType.NO_DATA.getValue());
					checkEndTime(date + " " + times[0].trim() + ":00", endTime, attendanceDetail, amEndTime, pmBeginTime);
					try {
						sendAttendanceMsg("上班", attendanceDate, userID);
					} catch (Exception e) {
					}
				}
			} else {
				checkBeginTime(date + " " + times[0].trim() + ":00", beginTime, attendanceDetail, amEndTime, pmBeginTime);
				checkEndTime(date + " " + times[times.length - 1].trim() + ":00", endTime, attendanceDetail, amEndTime, pmBeginTime);
				//检查打卡时间，判断是否未打卡（上班连续打卡，下班连续打卡）
				checkClockTime(attendanceDetail, date + " " + times[0].trim() + ":00", date + " " + times[times.length - 1].trim() + ":00", amEndTime, endTime);
				//加班时间限制为凌晨4点
				Date workOverDate = DateUtil.getFullDate(date + " "+"04:00:00");
				//下班打卡时间
				Date workEndTime = DateUtil.getFullDate(date+" "+times[times.length - 1].trim()+":00");
				//加班至次日上班之前
				if(DateUtil.before(workEndTime, workOverDate)){
					attendanceDetail.setEndType(AttendanceBeginType.NORMAL.getValue());
					attendanceDetail.setEndDiff(0L);
				}
			}
		}
		attendanceDetailDao.save(attendanceDetail);
	}
	private void sendAttendanceMsg(String type, Date attendanceDate, String userID) {
		StaffVO staff = staffService.getStaffByUserID(userID);
		String name = "";
		String telephone = "";
		if(null!=staff){
			telephone = staff.getTelephone();
			if("男".equals(staff.getGender())){
				name = staff.getLastName()+"先生";
			}else{
				name = staff.getLastName()+"女士";
			}
		}
		String content = "【智造链】 尊敬的"+name+"，"+DateUtil.formateDate(attendanceDate)+"当天，您"+type+"未打卡，可登录OA系统查看打卡明细，请知悉。";
		shortMsgSender.send(telephone, content);
	}


	/**
	 * 检查上班或下班是否未打卡（上午第一次打卡和最后一次打卡的间隔在半小时内，由此判断下午未打卡；打卡的时间都在下午下班后，由此判定上午未打卡；复杂情况不予考虑）
	 * @param attendanceDetail 考勤明细
	 * @param clockBeginTime 第一次打卡时间
	 * @param clockEndTime 最后一次打卡时间
	 * @param beginTime 上班时间
	 * @param endTime 下班时间
	 */
	private void checkClockTime(AttendanceDetailEntity attendanceDetail, String clockBeginTime, String clockEndTime,
			String amEndTime, String endTime) {
		Date clockBeginDate = DateUtil.getFullDate(clockBeginTime);
		Date clockEndDate = DateUtil.getFullDate(clockEndTime);
		Date amEndDate = DateUtil.getFullDate(amEndTime);
		Date pmEndDate = DateUtil.getFullDate(endTime);
		//打卡时间都在上午区间，并且打卡间隔小于半小时
		if(DateUtil.before(clockBeginDate, amEndDate) && DateUtil.before(clockEndDate, amEndDate) && 
				(clockEndDate.getTime()-clockBeginDate.getTime())<30*60*1000){
			attendanceDetail.setEndType(AttendanceEndType.NO_DATA.getValue());
			try {
				sendAttendanceMsg("下班", attendanceDetail.getAttendanceDate(), attendanceDetail.getUserID());
			} catch (Exception e) {
			}
			//打卡的时间都在下午下班后
		}else if(DateUtil.after(clockBeginDate, pmEndDate) && DateUtil.after(clockEndDate, pmEndDate)){
			attendanceDetail.setBeginType(AttendanceBeginType.NO_DATA.getValue());
			try {
				sendAttendanceMsg("上班", attendanceDetail.getAttendanceDate(), attendanceDetail.getUserID());
			} catch (Exception e) {
			}
		}
	}


	/**
	 * 
	 * @param mTime
	 * @param beginTime
	 * @param attendanceDetail
	 * @param amEndTime
	 * @param pmBeginTime
	 * @param isProcess 是否是流程
	 */
	private void checkBeginTime(String mTime, String beginTime, AttendanceDetailEntity attendanceDetail,
			String amEndTime, String pmBeginTime) {
		Date mDate = DateUtil.getFullDate(mTime);
		Date beginDate = DateUtil.getFullDate(beginTime);
		attendanceDetail.setBeginType(AttendanceBeginType.NORMAL.getValue());
		attendanceDetail.setBeginDiff(0L);

		// 打卡时间晚于规定上班时间，且请假时间小于晚到时间  
		//		if (mDate.getTime() > beginDate.getTime() && vacationService.calcVacationTime(beginDate, mDate,
		//				attendanceDetail.getUserID()) < (mDate.getTime() - beginDate.getTime())) {
		//			attendanceDetail.setBeginType(AttendanceBeginType.LATE.getValue());
		//			attendanceDetail.setBeginDiff(mDate.getTime() - beginDate.getTime()
		//					- vacationService.calcVacationTime(beginDate, mDate, attendanceDetail.getUserID()));
		//		}
		//之前的 判断 有问题    
		//首先 需要看 当前 mDate是否 大于 beginTime         
		//假如 大于 beginDate 那么  说明迟到了  我需要 判断  真正的上班开始时间  是否包含在  请假中 (表中数据的 结束时间/开始时间     区间包含  真正的上班开始时间 )
		//假如 存在请假 那么    有效数据   按照  请假的开始时间正序    必定是取第一条
		//用  vacation_begin_time -beginTime   假如 小于0 那么 当前 上班正常
		// 假如大于0 那么相差的 就应该是 迟到时间

		//这样修改 后 存在 一个问题      假如   我  10:00 上班  我请了     8:30 -9:30  那么 我应该 只算 晚到 30min
		//所以 我需要 在 情况 1和 2 中剔除 可能存在的有效请假时间    realEndTime-beginTime 区间内存在的请假时间
		if(mDate.after(beginDate)){
			Date vacationBegin = null;
			vacationBegin=vacationService.getBeginAndEndTimeFirst(mTime,attendanceDetail.getUserID());
			//没有请假(查询条件：打卡的开始时间早于请假的结束时间)
			if(vacationBegin==null){
				long time=mDate.getTime() - beginDate.getTime();
				//可能会存在请假的结束时间早于打卡时间，
				//这里存在3种情况：1、请假结束时间与打卡时间的区间是中午休息时间
				//2、迟到（请假结束时间与打卡时间的区间是上班时间）
				//3、请假结束时间与打卡时间的区间是中午休息时间和上班时间
				//获取请假的实际结束时间
				Date realEndTime = vacationService.getRealEndTimeLast(mTime, attendanceDetail.getUserID());
				//请假(查询条件：打卡的开始时间晚于请假的结束时间)
				if(realEndTime!=null){
					realEndTime =DateUtil.getFullDate(DateUtil.formateFullDate(realEndTime));
					//上午下班时间
					Date amEndDate = DateUtil.getFullDate(amEndTime);
					//下午上班时间
					Date pmBeginDate = DateUtil.getFullDate(pmBeginTime);
					//情况2
					if((DateUtil.before(realEndTime, amEndDate)&&DateUtil.before(mDate, amEndDate)) ||
							(DateUtil.after(realEndTime, pmBeginDate)&&DateUtil.after(mDate, pmBeginDate))){
						time -= realEndTime.getTime()-beginDate.getTime();
						attendanceDetail.setBeginDiff(time);
						if(time>0){
							attendanceDetail.setBeginType(AttendanceBeginType.LATE.getValue());					
						}else{
							attendanceDetail.setBeginType(AttendanceBeginType.NORMAL.getValue());					
						}
						//情况1		
					}else if(DateUtil.after(realEndTime, amEndDate) && DateUtil.before(mDate, pmBeginDate)){
						//do nothing
					}
					//情况3
					else if(DateUtil.after(mDate, amEndDate)){
						//上班打卡时间在下午上班之前，并且实际的请假结束时间在上午下班之前
						if(DateUtil.before(realEndTime, amEndDate) && DateUtil.before(mDate, pmBeginDate)){
							time = amEndDate.getTime()-realEndTime.getTime();
							//上班打卡时间在下午上班之后，并且实际的请假结束时间在上午下班之前
						}else if(DateUtil.before(realEndTime, amEndDate) && DateUtil.after(mDate, pmBeginDate)){
							time = mDate.getTime()-realEndTime.getTime() - (pmBeginDate.getTime()-amEndDate.getTime());
							//上班打卡时间在下午上班之后，并且实际的请假结束时间在上午下班之后
						}else if(DateUtil.after(realEndTime, amEndDate) && DateUtil.after(mDate, pmBeginDate)){
							time = mDate.getTime() - pmBeginDate.getTime();
						}
						attendanceDetail.setBeginDiff(time);
						if(time>0){
							attendanceDetail.setBeginType(AttendanceBeginType.LATE.getValue());					
						}else{
							attendanceDetail.setBeginType(AttendanceBeginType.NORMAL.getValue());					
						}
					}
					//没有查到请假
				}else{
					attendanceDetail.setBeginDiff(time);
					if(time>0){
						attendanceDetail.setBeginType(AttendanceBeginType.LATE.getValue());					
					}else{
						attendanceDetail.setBeginType(AttendanceBeginType.NORMAL.getValue());					
					}
				}	

				//假如 请了假 并且 最早有效的开始时间 大于  正常上班开始时间
			}else if(vacationBegin.after(beginDate)){
				long time=vacationBegin.getTime() - beginDate.getTime()-vacationService.getEffectiveVacationTime(beginDate,vacationBegin,attendanceDetail.getUserID());
				attendanceDetail.setBeginDiff(time);
				if(time>0){
					attendanceDetail.setBeginType(AttendanceBeginType.LATE.getValue());					
				}else{
					attendanceDetail.setBeginType(AttendanceBeginType.NORMAL.getValue());					
				}
			}
			//假如 请了假 并且 最早有效的开始时间 小于等于  正常上班开始时间
			else{
				//do nothing
			}
		}

	}
	/**
	 * 
	 * @param mTime
	 * @param endTime
	 * @param attendanceDetail
	 * @param amEndTime
	 * @param pmBeginTime
	 * @param isProcess 是否是流程
	 */
	private void checkEndTime(String mTime, String endTime, AttendanceDetailEntity attendanceDetail,
			String amEndTime, String pmBeginTime) {
		Date mDate = DateUtil.getFullDate(mTime);
		Date endDate = DateUtil.getFullDate(endTime);
		attendanceDetail.setEndType(AttendanceBeginType.NORMAL.getValue());
		attendanceDetail.setEndDiff(0L);
		//		if (mDate.getTime() < endDate.getTime()) {
		//			// 打卡时间早于规定下班时间，判断是否请假
		//			long vacationTime = vacationService.calcVacationTime(mDate, endDate, attendanceDetail.getUserID());
		//			if (vacationTime < endDate.getTime() - mDate.getTime()) {
		//				attendanceDetail.setEndType(AttendanceEndType.EARLY.getValue());
		//				attendanceDetail.setEndDiff(endDate.getTime() - mDate.getTime() - vacationTime);
		//			} else {
		//				attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());
		//				attendanceDetail.setEndDiff(0L);
		//			}
		//		} else {
		//			attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());
		//			attendanceDetail.setEndDiff(mDate.getTime() - endDate.getTime());
		//		}
		//和上面的开始时间 差不多
		//首先 需要看 当前 mDate是否 小于 endDate         
		//假如 小于 beginDate 那么  说明早退了了  我需要 判断  真正的上班开始时间  是否包含在  请假中 (表中数据的 结束时间/开始时间     区间包含  真正的上班开始时间 )
		//假如 存在请假 那么    表中有效数据  按照  请假的结束时间倒序  必定是取第一条
		//用 endTime-vacation_end_time   假如 小于0 那么 当前 上班正常
		// 假如大于0 那么相差的 就应该是 早退时间


		if(mDate.before(endDate)){
			Date vacationEnd = vacationService.getBeginAndEndTimeLast(mTime,attendanceDetail.getUserID());
			//没有请假(查询条件：下班打卡时间晚于请假的开始时间)
			if(vacationEnd==null){
				long time=endDate.getTime()-mDate.getTime();
				//可能会存在请假的开始时间晚于打卡时间，这里存在两种情况：
				//1、请假开始时间与打卡时间的区间是中午休息时间
				//2、早退（请假开始时间与打卡时间的区间是上班时间）
				//3、请假开始时间与打卡时间的区间是中午休息时间和上班时间
				//获取请假的实际开始时间
				Date realBeginDate = vacationService.getRealBeginTimeFirst(mTime, attendanceDetail.getUserID());
				//请假（查询条件：下班打卡时间早于请假的开始时间）
				if(realBeginDate!=null){
					realBeginDate =DateUtil.getFullDate(DateUtil.formateFullDate(realBeginDate));
					//上午下班时间
					Date amEndDate = DateUtil.getFullDate(amEndTime);
					//下午上班时间
					Date pmBeginDate = DateUtil.getFullDate(pmBeginTime);
					//情况2
					if((DateUtil.after(realBeginDate, pmBeginDate) && DateUtil.after(mDate, pmBeginDate)) ||
							(DateUtil.before(realBeginDate, amEndDate) && DateUtil.before(mDate, amEndDate))){
						time -= endDate.getTime()-realBeginDate.getTime();
						attendanceDetail.setEndDiff(time);
						if(time>0){
							attendanceDetail.setEndType(AttendanceEndType.EARLY.getValue());					
						}else{
							attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());					
						}
						//情况3
					}else if(DateUtil.before(mDate, pmBeginDate)){
						//下班打卡时间在上午下班之后，并且实际的请假开始时间在下午上班之后
						if(DateUtil.after(mDate, amEndDate) && DateUtil.after(realBeginDate, pmBeginDate)){
							time = realBeginDate.getTime()-pmBeginDate.getTime();
							//下班打卡时间在上午下班之前，并且实际的请假开始时间在下午上班之后	
						}else if(DateUtil.before(mDate, amEndDate) && DateUtil.after(realBeginDate, pmBeginDate)){
							time = realBeginDate.getTime()-mDate.getTime()-(pmBeginDate.getTime()-amEndDate.getTime());
							//下班打卡时间在上午下班之前，并且实际的请假开始时间在下午上班之前
						}else if(DateUtil.before(mDate, amEndDate) && DateUtil.before(realBeginDate, pmBeginDate)){
							time = amEndDate.getTime()-mDate.getTime();
						}
						attendanceDetail.setEndDiff(time);
						if(time>0){
							attendanceDetail.setEndType(AttendanceEndType.EARLY.getValue());					
						}else{
							attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());					
						}
						//情况1	
					}else if(DateUtil.before(realBeginDate, pmBeginDate) && DateUtil.after(mDate, amEndDate)){
						//do nothing
					}
				}else{
					attendanceDetail.setEndDiff(time);
					if(time>0)
						attendanceDetail.setEndType(AttendanceEndType.EARLY.getValue());
					else
						attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());
				}

				//假如 请了假 并且 正常上班的结束 时间  小于 最晚有效的结束时间
			}else if(endDate.after(vacationEnd)){
				long time=endDate.getTime() - vacationEnd.getTime()-vacationService.getEffectiveVacationTime(vacationEnd,endDate,attendanceDetail.getUserID());
				attendanceDetail.setEndDiff(time);
				if(time>0)
					attendanceDetail.setEndType(AttendanceEndType.EARLY.getValue());
				else
					attendanceDetail.setEndType(AttendanceEndType.NORMAL.getValue());
			}
			//假如 请了假 并且 正常上班的结束 时间 大于  最晚有效的结束时间
			else{
				//do nothing
			}
		}
	}
	
	@Override
	public ListResult<VacationVO> findVacationPageListByVacationVO(VacationVO vacationVO, int page, int limit) {
		List<Object> vacationEntities = vacationDao.findVacationByhql(getQuerySqlByVacationVO(vacationVO), page, limit);

		List<VacationVO> vacationList = new ArrayList<>();
		for (Object object : vacationEntities) {
			Object[] objs = (Object[]) object;
			VacationVO vacation = new VacationVO();
			vacation.setRequestUserName((String) objs[0]);
			vacation.setBeginDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) objs[1]).toString());
			vacation.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) objs[2]).toString());
			vacation.setShowHours((Double) objs[3]);
			vacation.setAgentID((String) objs[4]);
			if (!StringUtils.isBlank(vacation.getAgentID())) {
				vacation.setAgentName(staffService.getStaffByUserID(vacation.getAgentID()).getLastName());
			}
			vacation.setVacationType(Integer.valueOf(objs[5].toString()));
			vacation.setReason((String) objs[6]);
			vacation.setVacationID((Integer) objs[7]);
			vacation.setThecurrenLink((String) objs[9]);
			vacation.setStatus((String) objs[10]);
			vacation.setAssigneeUserName((String) objs[11]);
			/*if((String) objs[8]!=null){
				vacation.setProcessInstanceID((String) objs[8]);
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(vacation.getProcessInstanceID()).singleResult();
				if(processInstance!=null){
					
					vacation.setStatus("进行中");
					vacation.setAssigneeUserName(processService.getProcessTaskAssignee(processInstance.getId()));
					
					Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
					if(task!=null){
						vacation.setTaskName(task.getName());
					}
				}else{
					vacation.setStatus("完结");
				}
			}*/
			vacationList.add(vacation);
		}
		
		
		
		Object countObj = baseDao.getUniqueResult(getQueryCountSqlByVacationVO(vacationVO));
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<VacationVO>(vacationList, count);
	}

	private String getQuerySqlByVacationVO(VacationVO vacationVO) {
		/*StringBuffer hql = new StringBuffer(
				"select distinct * from (select CASE WHEN vacation.departmentId is NULL THEN StaffName ELSE CONCAT(com.CompanyName,'—',dep.DepartmentName) END, vacation.BeginDate ,vacation.EndDate ,vacation.Hours ,vacation.AgentID ,vacation.VacationType ,"
						+ "vacation.Reason, vacation.VacationID from OA_Vacation vacation, OA_Staff staff,ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail,OA_Company com,OA_Department dep "
						+ "where vacation.isDeleted = 0 and (vacation.ProcessStatus is null or vacation.ProcessStatus = 1) and locate(staff.UserID, vacation.RequestUserID)>0 and staff.UserID = membership.USER_ID_ and"
						+ " membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and staff.IsDeleted = 0 and staff.Status != 4 AND IFNULL(vacation.companyId,1) = com.CompanyID AND IFNULL(vacation.departmentId,1) = dep.DepartmentID ");*/
		StringBuffer hql = new StringBuffer(
				"SELECT DISTINCT\n" +
						"	a.*, b.NAME_,\n" +
						"	(\n" +
						"		CASE\n" +
						"		WHEN a.ProcessInstanceID IS NULL THEN\n" +
						"			'补的请假手续'\n" +
						"		WHEN a.ProcessInstanceID IS NOT NULL\n" +
						"		AND b.ID_ IS NULL THEN\n" +
						"			'完结'\n" +
						"		ELSE\n" +
						"			'进行中'\n" +
						"		END\n" +
						"	) AS link,\n" +
						"	(\n" +
						"		CASE\n" +
						"		WHEN b.ASSIGNEE_ IS NULL THEN\n" +
						"			b.NAME_\n" +
						"		ELSE\n" +
						"			c.StaffName\n" +
						"		END\n" +
						"	) AS examine\n" +
						"FROM\n" +
						"	(\n" +
						"		SELECT DISTINCT\n" +
						"			*\n" +
						"		FROM\n" +
						"			(\n" +
						"				SELECT\n" +
						"					CASE\n" +
						"				WHEN vacation.departmentId IS NULL THEN\n" +
						"					StaffName\n" +
						"				ELSE\n" +
						"					CONCAT(\n" +
						"						com.CompanyName,\n" +
						"						'—',\n" +
						"						dep.DepartmentName\n" +
						"					)\n" +
						"				END,\n" +
						"				vacation.BeginDate,\n" +
						"				vacation.EndDate,\n" +
						"				vacation.Hours,\n" +
						"				vacation.AgentID,\n" +
						"				vacation.VacationType,\n" +
						"				vacation.Reason,\n" +
						"				vacation.VacationID,\n" +
						"				vacation.ProcessInstanceID\n" +
						"			FROM\n" +
						"				(\n" +
						"					SELECT\n" +
						"						staff.UserID,\n" +
						"						StaffName,\n" +
						"						vacation.departmentId,\n" +
						"						vacation.companyID,\n" +
						"						vacation.BeginDate,\n" +
						"						vacation.EndDate,\n" +
						"						vacation.Hours,\n" +
						"						vacation.AgentID,\n" +
						"						vacation.VacationType,\n" +
						"						vacation.Reason,\n" +
						"						vacation.VacationID,\n" +
						"						vacation.ProcessInstanceID\n" +
						"					FROM\n" +
						"						OA_Staff staff,\n" +
						"						OA_Vacation vacation\n" +
						"					WHERE\n" +
						"						locate(\n" +
						"							staff.UserID,\n" +
						"							vacation.RequestUserID\n" +
						"						) > 0\n" +
						"					AND staff.IsDeleted = 0\n" +
						"					AND staff. STATUS != 4\n" +
						"					AND vacation.isDeleted = 0\n" +
						"					AND (\n" +
						"						vacation.ProcessStatus IS NULL\n" +
						"						OR vacation.ProcessStatus = 1\n" +
						"					)\n" +
						"				) vacation,\n" +
						"				ACT_ID_MEMBERSHIP membership,\n" +
						"				ACT_ID_GROUP idGroup,\n" +
						"				OA_GroupDetail groupDetail,\n" +
						"				OA_Company com,\n" +
						"				OA_Department dep\n" +
						"			WHERE\n" +
						"				vacation.UserID = membership.USER_ID_\n" +
						"			AND membership.GROUP_ID_ = idGroup.ID_\n" +
						"			AND idGroup.ID_ = groupDetail.GroupID\n" +
						"			AND IFNULL(vacation.companyId, 1) = com.CompanyID\n" +
						"			AND IFNULL(vacation.departmentId, 1) = dep.DepartmentID\n");
		hql.append(getWhereByVacationVO(vacationVO));
		hql.append("			) vacationvo\n" +
						"		ORDER BY\n" +
						"			BeginDate DESC\n" +
						"	) a\n" +
						"LEFT JOIN act_ru_task b ON a.ProcessInstanceID = b.PROC_INST_ID_\n" +
						"LEFT JOIN oa_staff c ON b.ASSIGNEE_ = c.UserID\n" +
						"WHERE\n" +
						"	a.BeginDate IS NOT NULL"
				);
		hql.append(whereCondition(vacationVO));
		return hql.toString();
	}
	private String whereCondition(VacationVO vacationVO){
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isBlank(vacationVO.getStatus())){
			sql.append(" AND (\n" +
					"	CASE\n" +
					"	WHEN a.ProcessInstanceID IS NULL THEN\n" +
					"		'补的请假手续'\n" +
					"	WHEN a.ProcessInstanceID IS NOT NULL\n" +
					"	AND b.ID_ IS NULL THEN\n" +
					"		'完结'\n" +
					"	ELSE\n" +
					"		'进行中'\n" +
					"	END\n" +
					") = '进行中'");
			
		}else if(!vacationVO.getStatus().equals("请选择") && vacationVO.getStatus()!=null){
			sql.append(" AND (\n" +
					"	CASE\n" +
					"	WHEN a.ProcessInstanceID IS NULL THEN\n" +
					"		'补的请假手续'\n" +
					"	WHEN a.ProcessInstanceID IS NOT NULL\n" +
					"	AND b.ID_ IS NULL THEN\n" +
					"		'完结'\n" +
					"	ELSE\n" +
					"		'进行中'\n" +
					"	END\n" +
					") = '"+vacationVO.getStatus()+"'");
		}
		if(StringUtils.isNotBlank(vacationVO.getThecurrenLink())){
			sql.append(" AND b.NAME_ = '"+vacationVO.getThecurrenLink()+"'");
		}
		if(StringUtils.isNotBlank(vacationVO.getAssigneeUserName())){
			sql.append(" AND (\n" +
					"	CASE\n" +
					"	WHEN b.ASSIGNEE_ IS NULL THEN\n" +
					"		b.NAME_\n" +
					"	ELSE\n" +
					"		c.StaffName\n" +
					"	END\n" +
					") LIKE '%"+vacationVO.getAssigneeUserName()+"%'");
		}
		return sql.toString();
	}
	private String getQueryCountSqlByVacationVO(VacationVO vacationVO) {
		/*StringBuffer hql = new StringBuffer(
				" select count(*) from (select distinct * from (select staff.staffName, vacation.BeginDate ,vacation.EndDate ,vacation.Hours ,vacation.AgentID ,vacation.VacationType ,"
						+ "vacation.Reason from OA_Vacation vacation, OA_Staff staff,ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail "
						+ "where vacation.isDeleted = 0 and (vacation.ProcessStatus is null or vacation.ProcessStatus = 1) and locate(staff.UserID, vacation.RequestUserID)>0 and staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and staff.IsDeleted = 0 and staff.Status != 4 ");*/
		StringBuffer hql = new StringBuffer(
				"SELECT COUNT(*)\n" +
						"FROM\n" +
						"	(\n" +
						"		SELECT DISTINCT\n" +
						"			*\n" +
						"		FROM\n" +
						"			(\n" +
						"				SELECT\n" +
						"					CASE\n" +
						"				WHEN vacation.departmentId IS NULL THEN\n" +
						"					StaffName\n" +
						"				ELSE\n" +
						"					CONCAT(\n" +
						"						com.CompanyName,\n" +
						"						'—',\n" +
						"						dep.DepartmentName\n" +
						"					)\n" +
						"				END,\n" +
						"				vacation.BeginDate,\n" +
						"				vacation.EndDate,\n" +
						"				vacation.Hours,\n" +
						"				vacation.AgentID,\n" +
						"				vacation.VacationType,\n" +
						"				vacation.Reason,\n" +
						"				vacation.VacationID,\n" +
						"				vacation.ProcessInstanceID\n" +
						"			FROM\n" +
						"				(\n" +
						"					SELECT\n" +
						"						staff.UserID,\n" +
						"						StaffName,\n" +
						"						vacation.departmentId,\n" +
						"						vacation.companyID,\n" +
						"						vacation.BeginDate,\n" +
						"						vacation.EndDate,\n" +
						"						vacation.Hours,\n" +
						"						vacation.AgentID,\n" +
						"						vacation.VacationType,\n" +
						"						vacation.Reason,\n" +
						"						vacation.VacationID,\n" +
						"						vacation.ProcessInstanceID\n" +
						"					FROM\n" +
						"						OA_Staff staff,\n" +
						"						OA_Vacation vacation\n" +
						"					WHERE\n" +
						"						locate(\n" +
						"							staff.UserID,\n" +
						"							vacation.RequestUserID\n" +
						"						) > 0\n" +
						"					AND staff.IsDeleted = 0\n" +
						"					AND staff. STATUS != 4\n" +
						"					AND vacation.isDeleted = 0\n" +
						"					AND (\n" +
						"						vacation.ProcessStatus IS NULL\n" +
						"						OR vacation.ProcessStatus = 1\n" +
						"					)\n" +
						"				) vacation,\n" +
						"				ACT_ID_MEMBERSHIP membership,\n" +
						"				ACT_ID_GROUP idGroup,\n" +
						"				OA_GroupDetail groupDetail,\n" +
						"				OA_Company com,\n" +
						"				OA_Department dep\n" +
						"			WHERE\n" +
						"				vacation.UserID = membership.USER_ID_\n" +
						"			AND membership.GROUP_ID_ = idGroup.ID_\n" +
						"			AND idGroup.ID_ = groupDetail.GroupID\n" +
						"			AND IFNULL(vacation.companyId, 1) = com.CompanyID\n" +
						"			AND IFNULL(vacation.departmentId, 1) = dep.DepartmentID\n");
		hql.append(getWhereByVacationVO(vacationVO));
		hql.append("			) vacationvo\n" +
						"		ORDER BY\n" +
						"			BeginDate DESC\n" +
						"	) a\n" +
						"LEFT JOIN act_ru_task b ON a.ProcessInstanceID = b.PROC_INST_ID_\n" +
						"LEFT JOIN oa_staff c ON b.ASSIGNEE_ = c.UserID\n" +
						"WHERE\n" +
						"	a.BeginDate IS NOT NULL"
				);
		hql.append(whereCondition(vacationVO));
		return hql.toString();

	}

	@SuppressWarnings("static-access")
	private String getWhereByVacationVO(VacationVO vacationVO) {
		StringBuffer whereSql = new StringBuffer();
		
		if (!StringUtils.isBlank(vacationVO.getBeginDate())) {
			whereSql.append(" and vacation.beginDate >= '" + vacationVO.getBeginDate() + "'");
		}else if(vacationVO.getBeginDate()==null){
			Calendar calendar = Calendar.getInstance();
			calendar.add(calendar.MONTH, -3);
			Date beforeDate = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String defaultStartDate = sdf.format(beforeDate);
			whereSql.append(" and vacation.beginDate >= '" + defaultStartDate + "'");
		}
		if (!StringUtils.isBlank(vacationVO.getEndDate())) {
			whereSql.append(" and vacation.beginDate <= '" + vacationVO.getEndDate() + "'");
		}
		if (!StringUtils.isBlank(vacationVO.getRequestUserName())) {
			whereSql.append(" and StaffName like '%" + vacationVO.getRequestUserName() + "%' ");
		}
		if (vacationVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = " + vacationVO.getCompanyID());
			if (vacationVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyIDParentID(vacationVO.getCompanyID(), vacationVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(vacationVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}

		return whereSql.toString();
	}

	@Override
	public List<SigninVO> findSigninsByDateAndCompanyID(Integer companyID, String date1) throws Exception {
		String date;
		if (StringUtils.isBlank(date1)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = DateUtil.formateDate(cal.getTime());
		} else {
			date = date1;
		}
		String sql1 = "select * from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where staff.positionCategory = 1 and staff.isDeleted=0 and staff.status !=4  and attendance.attendanceDate = '"
				+ date + "' and attendance.companyID='" + companyID + "' "
				+ "and attendance.isDeleted = 0 and signin.userID is null) noSign where not exists(select special.userID from OA_Special special where special.type=2 and special.isDeleted = 0 "
				+ "and special.userID=noSign.userID )";
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select * from (select attendance.userID userID,attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where  staff.positionCategory = 2 and staff.isDeleted=0 and staff.status !=4 and attendance.attendanceDate = '"
				+ date + "' and attendance.companyID='" + companyID + "' "
				+ "and attendance.isDeleted = 0 and signin.userID is null) noSign left join OA_Special special on noSign.userID=special.userID where special.type=2 and special.isDeleted = 0";
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);
		List<SigninVO> signinVOs = new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			SigninVO signinVO = new SigninVO();
			signinVO.setUserID((String) objs[0]);
			signinVO.setSignDates(new String[]{date});
			signinVO.setUserName(staffService.getStaffByUserID((String) objs[0]).getLastName());
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[0]);
			signinVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName() + "—" + input.getDepartmentName() + "—" + input.getPositionName();
				}
			}));
			signinVOs.add(signinVO);
		}

		return signInFilter(signinVOs,date,date,companyID);
	}

	@Override
	public List<SigninVO> findSignByMonthAndCompanyID(Integer companyID, Date date) throws Exception {
		String firstDay = DateUtil.getFirstDayofMonth(date);
		String lastDay = DateUtil.getLastDayofMonth(date);
		String sql1 = "select noSign.userID,count(*),GROUP_CONCAT(noSign.attendanceDate ORDER BY noSign.attendanceDate DESC ) from (select attendance.userID userID,"
				+ "attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where staff.positionCategory = 1 and staff.isDeleted=0 and staff.status !=4 and attendance.attendanceDate >= '"
				+ firstDay + "' and attendance.attendanceDate <= '" + lastDay + "'  and attendance.companyID='"
				+ companyID + "' "
				+ "and attendance.isDeleted = 0 and signin.userID is null) noSign where not exists(select special.userID from OA_Special special where special.type=2 and special.isDeleted = 0 "
				+ "and special.userID=noSign.userID ) group by noSign.userID ";
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select noSign.userID,count(*),GROUP_CONCAT(noSign.attendanceDate ORDER BY noSign.attendanceDate DESC) from (select attendance.userID userID,"
				+ "attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where  staff.positionCategory = 2 and staff.isDeleted=0 and staff.status !=4 and attendance.attendanceDate >= '"
				+ firstDay + "' and attendance.attendanceDate <= '" + lastDay + "' and attendance.companyID='"
				+ companyID + "' "
				+ "and attendance.isDeleted = 0 and signin.userID is null) noSign left join OA_Special special on noSign.userID=special.userID where special.type=2 and special.isDeleted = 0 group by noSign.userID";
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);
		List<SigninVO> signinVOs = new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			SigninVO signinVO = new SigninVO();
			signinVO.setUserID((String) objs[0]);
			if (!StringUtils.isBlank((String) objs[0])) {
				signinVO.setUserName(staffService.getStaffByUserID((String) objs[0]).getLastName());
			}
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[0]);
			for (GroupDetailVO group : groups) {
				if (group.getCompanyID() == companyID) {
					signinVO.setDepartmentName(group.getDepartmentName());
				}
			}

			signinVO.setCount(Integer.parseInt(objs[1].toString()));
			signinVO.setSignDates(StringUtils.split((String) objs[2], ","));
			signinVOs.add(signinVO);
		}

		return signInFilter(signinVOs,firstDay,lastDay, companyID);
	}

	//假如 是beginTime: 2017-01-01 endTime: 2017-01-31
	//那么 时间区间 就是 2017-01-01 00:00:00到2017-02-01 00:00:00(加上一天)
	//我们需要知道 这个时间段 请过假的人     假如 请假 日期 为 2017-01-28 00:00:00到 2017-02-01 18:00:00 也应该算作这段时间内请过假的记录
	//所以 条件 应该是  开始时间 小于等于 2017-02-01 00:00:00 结束时间 大于等于 2017-01-01 00:00:00
	private List<SigninVO> signInFilter(List<SigninVO> signinVOs,String beginTime,String endTime, Integer companyId) throws Exception{
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(DateUtil.parseDay(endTime));
		calendar.add(Calendar.DATE, 1);
		endTime=DateUtil.getDayStr(calendar.getTime());
		if(CollectionUtils.isEmpty(signinVOs))return signinVOs;
		//查询signinVOs 中所有的 用户
		List<String> userIds=new ArrayList<String>();
		for(int i=0,length=signinVOs.size();i<length;i++){
			userIds.add(signinVOs.get(i).getUserID());
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
				"and v.IsDeleted =0  ";
		List<Object> sqlResultList=baseDao.findBySql(sql);
		if(CollectionUtils.isEmpty(sqlResultList))return signinVOs;
		HashMultimap<String, Date[]> vacationMap = HashMultimap.create(); 
		for(int i=0,length=sqlResultList.size();i<length;i++){
			Object[] currentData=(Object[]) sqlResultList.get(i);
			String userId=currentData[0]+"";
			Date startDate=(Date) currentData[1];
			Date endDate=(Date) currentData[2];
			vacationMap.put(userId,new Date[]{startDate,endDate});
		}
		Iterator<SigninVO> it = signinVOs.iterator();
		while(it.hasNext()){
			SigninVO signinVO = it.next();
			String[] times=signinVO.getSignDates();
			if(null == companyId){
				List<Group> groups = identityService.createGroupQuery().groupMember(signinVO.getUserID()).list();
				if(null!=groups && groups.size()>0){
					companyId = Integer.parseInt(groups.get(0).getType().split("_")[0]);
				}
			}
			String[] dates=getEffectiveTime(times,vacationMap.get(signinVO.getUserID()), companyId);
			if(dates==null||dates.length==0)
				it.remove();
			else{
				signinVO.setCount(dates.length);
				signinVO.setSignDates(dates);
			}
		}
		return signinVOs;

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
			//			System.out.println("未签到时间:"+DateUtil.formateDate(time));
			boolean result=isInVacationTimes(time,vacationTimes, companyId);
			//			System.out.print(result+":");
			//			System.out.print(vacationTimes);
			if(!result){
				filterArray.add(timeStr);
			}
		}
		return CollectionUtils.isEmpty(filterArray)?null:filterArray.toArray(new String[]{}) ;
	}
	private boolean isInVacationTimes(Date time,Set<Date[]> vacationTimes, Integer companyId) throws Exception{
		String[] arr_times = getWorkRestTimeByCompanyIDOrDepartmentId(companyId+"", "", DateUtil.formateDate(time));
		return CheckDateUtil.isVacation(time, vacationTimes, companyId, arr_times);

		//		for (Date[] dates: vacationTimes){
		//			boolean  result=isInVacationTime(time,dates[0],dates[1]);
		////				System.out.println("开始时间:"+DateUtil.formateFullDate(dates[0])+",结束时间:"+DateUtil.formateFullDate(dates[1])+",结果:"+result);
		//			if(result)return true;
		//		}
		//		return false;

	}

	/*	private boolean isInVacationTime(Date time,Date startTime,Date endTime){
		Date[] dates=DateUtil.getJobBeginTimeAndEndTime(time);
		if(dates==null){
			return false;
		}
		return dates[0].compareTo(startTime)>=0&&dates[1].compareTo(endTime)<=0;
	}*/


	@Override
	public ListResult<AttendanceVO> findAttendanceStatistics(AttendanceVO attendanceVO, int page, int limit) throws Exception {
		if (attendanceVO == null) {
			throw new RuntimeException("获取查询条件失败");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getSimpleDate(attendanceVO.getBeginDate()));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		List<Object> list = baseDao.findPageList(getAttendanceStatisticsBysql(attendanceVO), 1, Integer.MAX_VALUE);
		// 先处理list 中的打卡字段 进行排序 选出 对应的 limit 条数据
		int startIndex = (page - 1) * limit;
		int endIndex = startIndex + limit;
		int size = list.size();
		startIndex = (startIndex >= size) ? size : startIndex;
		endIndex = (endIndex >= size) ? size : endIndex;
		List<AttendanceVO> attendanceVOs = new ArrayList<>();
		List<Object> resultList = list.subList(startIndex, endIndex);
		Map<String, WorkReportVO> noReportMap = workReportService.findStatisticsByDate(attendanceVO.getCompanyID(), attendanceVO.getBeginDate(), attendanceVO.getEndDate());
		Map<String, SigninVO> noSignInMap = findSignByDateAndCompanyID(attendanceVO.getCompanyID(), attendanceVO.getBeginDate(), attendanceVO.getEndDate());
		String endDate = attendanceVO.getEndDate();
		for (Object obj : resultList) {
			Object[] objs = (Object[]) obj;
			Date leaveDate = (Date)objs[12];
			boolean lastAttendance = true;
			//离职人员
			if(null != leaveDate){
				//离职人员最后一天的打卡日期（全天打卡）
				String lastAttendanceDate = getLeaveStaffLastAttendanceDate((String) objs[5], month);
				if(null == lastAttendanceDate){
					continue;
				}else if(!attendanceVO.getEndDate().equals(lastAttendanceDate)){
					endDate = lastAttendanceDate;
					lastAttendance = false;
				}
			}
			AttendanceVO attendanceVO2 = new AttendanceVO();
			attendanceVO2.setAttendanceDays(Integer.parseInt(objs[0].toString()));
			VacationVO vacationVO = vacationService.getDaysAndHours(attendanceVO.getBeginDate(),
					endDate, (String) objs[5], (Integer) objs[6], (Integer) objs[10]);
			attendanceVO2.setVacationVO(vacationVO);
			String[] beginTypes = StringUtils.split((String) objs[2], ",");
			int beginType = 0;
			String unPublishDateIndex="";
			for (int i = 0; i < beginTypes.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.NO_DATA.getValue()))) {
					beginType += 1;
					unPublishDateIndex+="am"+i+",";
				}
			}
			int endType = 0;
			String[] endTypes = StringUtils.split((String) objs[3], ",");
			for (int i = 0; i < endTypes.length; i++) {
				if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.NO_DATA.getValue()))) {
					endType += 1;
					unPublishDateIndex+="pm"+i+",";
				}
			}
			attendanceVO2.setNotPunchTimes(beginType + endType);
			attendanceVO2.setNotPushDateIndex(unPublishDateIndex);
			String[] beginDiff = StringUtils.split((String) objs[1], ",");
			Integer times = 0;
			//迟到次数
			int lateTimes = 0;
			for (int i = 0; i < beginDiff.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.LATE.getValue()))) {
					times += Integer.parseInt(String.valueOf(Long.parseLong(beginDiff[i]) / (1000 * 60)));
					lateTimes++;
				}
			}

			String[] endDiff = StringUtils.split((String) objs[4], ",");
			Integer time = 0;
			//早退次数
			int leaveEarlyTimes = 0;
			for (int i = 0; i < endDiff.length; i++) {
				if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.EARLY.getValue()))) {
					time += Integer.parseInt(String.valueOf(Long.parseLong(endDiff[i]) / (1000 * 60)));
					leaveEarlyTimes++;
				}
			}
			attendanceVO2.setDetail((objs[8])==null?null:(String)(objs[8]));
			attendanceVO2.setLateTime(times);
			attendanceVO2.setLateTimes(lateTimes);
			attendanceVO2.setLeaveEarlyTime(time);;
			attendanceVO2.setLeaveEarlyTimes(leaveEarlyTimes);
			attendanceVO2.setName(staffService.getStaffByUserID((String) objs[5]).getLastName());
			//获取晚上加班时长
			String[] nightWorkTimesAndhours = workOvertimeService.getNightWorkTimesAndhours((String) objs[5], attendanceVO.getBeginDate(), endDate);
			attendanceVO2.setNightWorkTimes(nightWorkTimesAndhours[0]);
			attendanceVO2.setNightWorkHours(nightWorkTimesAndhours[1]);
			if(noReportMap.containsKey((String) objs[5])){
				attendanceVO2.setWorkReportVO(noReportMap.get((String) objs[5]));
			}else{
				attendanceVO2.setWorkReportVO(new WorkReportVO());
			}
			if(noSignInMap.containsKey((String) objs[5])){
				attendanceVO2.setSigninVO(noSignInMap.get((String) objs[5]));
			}else{
				attendanceVO2.setSigninVO(new SigninVO());
			}
			attendanceVO2.setDepartmentName((String) objs[9]);
			//获取当月公休天数
			UserMonthlyRestEntity monthlyRest = getMonthlyRest((String) objs[5], year, month);
			if(null != monthlyRest){
				//应出勤天数
				attendanceVO2.setAttendanceDays(Integer.parseInt(monthlyRest.getWorkDays()));
			}
			//异常考勤天数（未打卡且未请假）
			List<String> abnormalDates = getAbnormalDays((String) objs[5], DateUtil.getSimpleDate(attendanceVO.getBeginDate()),
					DateUtil.getSimpleDate(endDate));
			int abnormalDays = abnormalDates.size();
			//该月在职天数
			int onJobDays = DateUtil.differentDays(DateUtil.getSimpleDate(attendanceVO.getBeginDate()), DateUtil.getSimpleDate(endDate))+1;
			int vacationDays = 0;
			if(null != vacationVO){
				vacationDays = vacationVO.getDays();
			}
			//实际出勤天数
			int actualAttendanceDays = onJobDays - vacationDays - abnormalDays;
			attendanceVO2.setActualAttendanceDays(actualAttendanceDays);
			boolean whiteJob = staffService.isWhiteJob((String) objs[5]);
			//白领统计周末/公休加班，蓝领无
			if(null != monthlyRest && whiteJob){
				int monthlyRestDays = Integer.parseInt(monthlyRest.getRestDays());
				double dailyWorkOverTimeHours = 0;
				double hours = 0;
				double dailyHours = 0;
				//离职人员
				if(null != leaveDate){
					//当月最后一天打卡上班，反之不计算加班时长
					if(lastAttendance){
						if(null == vacationVO){
							attendanceVO2.setDayWorkHours(monthlyRestDays+"天");
						}else{
							int days = vacationVO.getDays()==null ? 0:vacationVO.getDays();
							hours = vacationVO.getShowHours()==null ? 0:vacationVO.getShowHours();
							dailyHours = getDailyHoursByCompanyIDOrDepartmentId(objs[6]+"",
									objs[10]+"", DateUtil.formateFullDate(new Date()));
							//白天加班时长
							dailyWorkOverTimeHours = monthlyRestDays*dailyHours - (days*dailyHours+hours) - abnormalDays*dailyHours;
						}
					}
				}else if(null == vacationVO){
						attendanceVO2.setDayWorkHours(monthlyRestDays+"天");
					}else{
						int days = vacationVO.getDays()==null ? 0:vacationVO.getDays();
						hours = vacationVO.getShowHours()==null ? 0:vacationVO.getShowHours();
						dailyHours = getDailyHoursByCompanyIDOrDepartmentId(objs[6]+"",
								objs[10]+"", DateUtil.formateFullDate(new Date()));
						//白天加班时长
						dailyWorkOverTimeHours = monthlyRestDays*dailyHours - (days*dailyHours+hours) - abnormalDays*dailyHours;
				}
				if(dailyWorkOverTimeHours>0){
						int day = (int) Math.floor(dailyWorkOverTimeHours/dailyHours);
						hours =  dailyWorkOverTimeHours - day*dailyHours;
						if(day>0){
							if(hours>0){
								attendanceVO2.setDayWorkHours(day+"天"+hours+"小时");
							}else{
								attendanceVO2.setDayWorkHours(day+"天");
							}
						}else if(hours>0){
							attendanceVO2.setDayWorkHours(hours+"小时");
						}
					}
				}
			attendanceVO2.setStaffNum((String) objs[11]);
			Map<String, Double> weekendDayAndWorkHoursMap = new LinkedHashMap<>();
			getWorkHoursInWeekend((String) objs[5], attendanceVO.getBeginDate(),
					endDate, weekendDayAndWorkHoursMap, attendanceVO2);
			attendanceVO2.setWeekendDayAndWorkHoursMap(weekendDayAndWorkHoursMap);
			attendanceVOs.add(attendanceVO2);
		}
		return new ListResult<AttendanceVO>(attendanceVOs, size);
	}

	private void getWorkHoursInWeekend(String userId, String beginDate, String endDate,
			Map<String, Double> weekendDayAndWorkHoursMap, AttendanceVO attendanceVO2) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		String companyIDString = groups.get(0).getType().split("_")[0];
		String departmentId = groups.get(0).getType().split("_")[1];
		String sql = "select attendanceDate, AttendanceTime,beginType,endType from OA_AttendanceDetail where isDeleted=0 and userID='"+userId+
				"' and attendanceDate>='"+beginDate+"' and attendanceDate<='"+endDate+"' and (WEEKDAY(attendanceDate)=5 or WEEKDAY(attendanceDate)=6) order by attendanceDate";
		List<Object> objList = baseDao.findBySql(sql);
		double workHoursInWeekend = 0;
		double workDaysInWeekend = 0;
		double dailyHour = 0;
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			Date attendanceDate = (Date)objs[0];
			String attendanceTime = (String)objs[1];
			int beginType = (Integer)objs[2];
			int endType = (Integer)objs[3];
			dailyHour = getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(attendanceDate));
			String[] workTimeArray = getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(attendanceDate));
			String[] attendanceTimes = attendanceTime.split(" ");
			String beginTime = "";
			String endTime = "";
			//上午忘打卡
			if(AttendanceBeginType.NO_DATA.getValue() == beginType){
				//上班时间取上午的上班时间
				beginTime = DateUtil.formateDate(attendanceDate) + " " + workTimeArray[0] + ":00";
				endTime = DateUtil.formateDate(attendanceDate) + " " + attendanceTimes[attendanceTimes.length-1] + ":00";
			}
			//下午忘打卡
			else if(AttendanceEndType.NO_DATA.getValue() == endType){
				//下班时间取下午的下班时间
				endTime = DateUtil.formateDate(attendanceDate) + " " + workTimeArray[3] + ":00";
				beginTime = DateUtil.formateDate(attendanceDate) + " " + attendanceTimes[0] + ":00";
			}
			else{
				beginTime = DateUtil.formateDate(attendanceDate) + " " + attendanceTimes[0] + ":00";
				endTime = DateUtil.formateDate(attendanceDate) + " " + attendanceTimes[attendanceTimes.length-1] + ":00";
			}
			//判断当天有没有请假//3种情况（早上打卡时间开始的请假，晚上打卡时间结束的请假，中途请假）
			Date currentAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(attendanceDate) + " " + workTimeArray[0] + ":00");
			Date currentPmEndDate = DateUtil.getFullDate(DateUtil.formateDate(attendanceDate) + " " + workTimeArray[3] + ":00");
			//早上打卡时间开始的请假，晚上打卡时间结束的请假
			sql = "SELECT\n" +
					"	date_format(BeginDate, '%Y-%m-%d %H:%i:%s'), date_format(EndDate, '%Y-%m-%d %H:%i:%s')\n" +
					"FROM\n" +
					"	OA_Vacation\n" +
					"WHERE\n" +
					"	IsDeleted = 0\n" +
					"AND (\n" +
					"	ProcessInstanceID IS NULL\n" +
					"	OR ProcessStatus = 1\n" +
					")\n" +
					//"AND RequestUserID = '"+userId+"'\n" +
					"AND locate('"+userId+"', RequestUserID)>0\n" +
					"AND ((BeginDate <= '"+currentAmBeginDate+"' and Date(EndDate)='"+DateUtil.formateDate(attendanceDate)+
					"') OR (EndDate >= '"+currentPmEndDate+"' and Date(BeginDate)='"+DateUtil.formateDate(attendanceDate)+"'))";
			List<Object> vacationList = baseDao.findBySql(sql);
			for(Object vacationObj: vacationList){
				Object[] vacations = (Object[])vacationObj;
				String vacationBeginDate = (String)vacations[0];
				String vacationEndDate = (String)vacations[1];
				//double vacationHours = (double)vacations[2];
				//double _dailyHour = (double)vacations[3];
				if(DateUtil.before(DateUtil.getFullDate(vacationBeginDate), currentAmBeginDate)){
					beginTime = vacationEndDate;
				}
				if(DateUtil.after(DateUtil.getFullDate(vacationEndDate), currentPmEndDate)){
					endTime = vacationBeginDate;
				}
			}
			double hour = vacationService.calcWorkHours(DateUtil.getFullDate(beginTime), DateUtil.getFullDate(endTime), workTimeArray);
			//中途请假
			sql = "SELECT\n" +
					"	Hours\n" +
					"FROM\n" +
					"	OA_Vacation\n" +
					"WHERE\n" +
					"	IsDeleted = 0\n" +
					"AND (\n" +
					"	ProcessInstanceID IS NULL\n" +
					"	OR ProcessStatus = 1\n" +
					")\n" +
					//"AND RequestUserID = '"+userId+"'\n" +
					"AND locate('"+userId+"', RequestUserID)>0\n" +
					"AND BeginDate > '"+currentAmBeginDate+"' AND EndDate < '"+currentPmEndDate+"'";
			vacationList = baseDao.findBySql(sql);
			for(Object hourObj: vacationList){
				hour -= (double)hourObj;
			}
			if(hour>=dailyHour){
				workDaysInWeekend += 1;
			}else{
				workHoursInWeekend += hour;
			}
			if(hour>0){
				weekendDayAndWorkHoursMap.put(DateUtil.formateDate(attendanceDate), hour);
			}
		}
		if(0 != dailyHour){
			double remainHours = workHoursInWeekend % dailyHour;
			double days = (workHoursInWeekend-remainHours)/dailyHour;
			workDaysInWeekend += days;
			workHoursInWeekend = remainHours;
			if(workDaysInWeekend>0){
				if(workHoursInWeekend>0){
					attendanceVO2.setWorkDaysAndHoursInWeekend(removeZero(workDaysInWeekend)+"天"+removeZero(workHoursInWeekend)+"小时");
				}else{
					attendanceVO2.setWorkDaysAndHoursInWeekend(removeZero(workDaysInWeekend)+"天");
				}
			}else{
				attendanceVO2.setWorkDaysAndHoursInWeekend(removeZero(workHoursInWeekend)+"小时");
			}
		}
	}
	private String removeZero(double num){
		String _num = num+"";
		if(_num.endsWith(".0")){
			return _num.split("\\.0")[0];
		}
		return _num;
	}
	private List<AttendanceVO> prepareExportData(AttendanceVO attendanceVO) throws Exception {
		if (attendanceVO == null) {
			throw new RuntimeException("获取查询条件失败");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getSimpleDate(attendanceVO.getBeginDate()));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		List<Object> list = baseDao.findPageList(getAttendanceStatisticsBysql(attendanceVO), 1, Integer.MAX_VALUE);
		List<AttendanceVO> attendanceVOs = new ArrayList<AttendanceVO>(list.size());
		Map<String, WorkReportVO> noReportMap = workReportService.findStatisticsByDate(attendanceVO.getCompanyID(), attendanceVO.getBeginDate(), attendanceVO.getEndDate());
		Map<String, SigninVO> noSignInMap = findSignByDateAndCompanyID(attendanceVO.getCompanyID(), attendanceVO.getBeginDate(), attendanceVO.getEndDate());
		String endDate = attendanceVO.getEndDate();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			Date leaveDate = (Date)objs[12];
			boolean lastAttendance = true;
			//离职人员
			if(null != leaveDate){
				//离职人员最后一天的打卡日期（全天打卡）
				String lastAttendanceDate = getLeaveStaffLastAttendanceDate((String) objs[5], month);
				if(null == lastAttendanceDate){
					continue;
				}else if(!attendanceVO.getEndDate().equals(lastAttendanceDate)){
					endDate = lastAttendanceDate;
					lastAttendance = false;
				}
			}
			AttendanceVO attendanceVO2 = new AttendanceVO();
			attendanceVO2.setAttendanceDays(Integer.parseInt(objs[0].toString()));
			VacationVO vacationVO = vacationService.getDaysAndHours(attendanceVO.getBeginDate(),
					attendanceVO.getEndDate(), (String) objs[5], (Integer) objs[6], (Integer) objs[10]);
			attendanceVO2.setVacationVO(vacationVO);
			String[] beginTypes = StringUtils.split((String) objs[2], ",");
			int beginType = 0;
			for (int i = 0; i < beginTypes.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.NO_DATA.getValue()))) {
					beginType += 1;
				}
			}
			int endType = 0;
			String[] endTypes = StringUtils.split((String) objs[3], ",");
			for (int i = 0; i < endTypes.length; i++) {
				if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.NO_DATA.getValue()))) {
					endType += 1;
				}
			}
			attendanceVO2.setNotPunchTimes(beginType + endType);

			String[] beginDiff = StringUtils.split((String) objs[1], ",");
			Integer times = 0;
			//迟到次数
			int lateTimes = 0;
			for (int i = 0; i < beginDiff.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.LATE.getValue()))) {
					times += Integer.parseInt(String.valueOf(Long.parseLong(beginDiff[i]) / (1000 * 60)));
					lateTimes++;
				}
			}

			String[] endDiff = StringUtils.split((String) objs[4], ",");
			Integer time = 0;
			//早退次数
			int leaveEarlyTimes = 0;
			for (int i = 0; i < endDiff.length; i++) {
				if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.EARLY.getValue()))) {
					time += Integer.parseInt(String.valueOf(Long.parseLong(endDiff[i]) / (1000 * 60)));
					leaveEarlyTimes++;
				}
			}
			attendanceVO2.setLateTime(times);
			attendanceVO2.setLateTimes(lateTimes);
			attendanceVO2.setLeaveEarlyTime(time);;
			attendanceVO2.setLeaveEarlyTimes(leaveEarlyTimes);
			attendanceVO2.setName(staffService.getStaffByUserID((String) objs[5]).getLastName());
			//获取晚上加班时长
			String[] nightWorkTimesAndhours = workOvertimeService.getNightWorkTimesAndhours((String) objs[5], attendanceVO.getBeginDate(), attendanceVO.getEndDate());
			attendanceVO2.setNightWorkTimes(nightWorkTimesAndhours[0]);
			attendanceVO2.setNightWorkHours(nightWorkTimesAndhours[1]);
			if(noReportMap.containsKey((String) objs[5])){
				attendanceVO2.setWorkReportVO(noReportMap.get((String) objs[5]));
			}else{
				attendanceVO2.setWorkReportVO(new WorkReportVO());
			}
			if(noSignInMap.containsKey((String) objs[5])){
				attendanceVO2.setSigninVO(noSignInMap.get((String) objs[5]));
			}else{
				attendanceVO2.setSigninVO(new SigninVO());
			}
			attendanceVO2.setDepartmentName((String) objs[9]);
			//获取当月公休天数
			UserMonthlyRestEntity monthlyRest = getMonthlyRest((String) objs[5], year, month);
			if(null != monthlyRest){
				//应出勤天数
				attendanceVO2.setAttendanceDays(Integer.parseInt(monthlyRest.getWorkDays()));
			}
			//异常考勤天数（未打卡且未请假）
			List<String> abnormalDates = getAbnormalDays((String) objs[5], DateUtil.getSimpleDate(attendanceVO.getBeginDate()),
					DateUtil.getSimpleDate(endDate));
			int abnormalDays = abnormalDates.size();
			//该月在职天数
			int onJobDays = DateUtil.differentDays(DateUtil.getSimpleDate(attendanceVO.getBeginDate()), DateUtil.getSimpleDate(endDate))+1;
			int vacationDays = 0;
			if(null != vacationVO){
				vacationDays = vacationVO.getDays();
			}
			//实际出勤天数
			int actualAttendanceDays = onJobDays - vacationDays - abnormalDays;
			attendanceVO2.setActualAttendanceDays(actualAttendanceDays);
			boolean whiteJob = staffService.isWhiteJob((String) objs[5]);
			//白领统计周末/公休加班，蓝领无
			if(null != monthlyRest && whiteJob){
				int monthlyRestDays = Integer.parseInt(monthlyRest.getRestDays());
				double dailyWorkOverTimeHours = 0;
				double hours = 0;
				double dailyHours = 0;
				//离职人员
				if(null != leaveDate){
					//当月最后一天打卡上班，反之不计算加班时长
					if(lastAttendance){
						if(null == vacationVO){
							attendanceVO2.setDayWorkHours(monthlyRestDays+"天");
						}else{
							int days = vacationVO.getDays()==null ? 0:vacationVO.getDays();
							hours = vacationVO.getShowHours()==null ? 0:vacationVO.getShowHours();
							dailyHours = getDailyHoursByCompanyIDOrDepartmentId(objs[6]+"",
									objs[10]+"", DateUtil.formateFullDate(new Date()));
							//白天加班时长
							dailyWorkOverTimeHours = monthlyRestDays*dailyHours - (days*dailyHours+hours) - abnormalDays*dailyHours;
						}
					}
				}else if(null == vacationVO){
						attendanceVO2.setDayWorkHours(monthlyRestDays+"天");
					}else{
						int days = vacationVO.getDays()==null ? 0:vacationVO.getDays();
						hours = vacationVO.getShowHours()==null ? 0:vacationVO.getShowHours();
						dailyHours = getDailyHoursByCompanyIDOrDepartmentId(objs[6]+"",
								objs[10]+"", DateUtil.formateFullDate(new Date()));
						//白天加班时长
						dailyWorkOverTimeHours = monthlyRestDays*dailyHours - (days*dailyHours+hours) - abnormalDays*dailyHours;
				}
				if(dailyWorkOverTimeHours>0){
						int day = (int) Math.floor(dailyWorkOverTimeHours/dailyHours);
						hours =  dailyWorkOverTimeHours - day*dailyHours;
						if(day>0){
							if(hours>0){
								attendanceVO2.setDayWorkHours(day+"天"+hours+"小时");
							}else{
								attendanceVO2.setDayWorkHours(day+"天");
							}
						}else if(hours>0){
							attendanceVO2.setDayWorkHours(hours+"小时");
						}
					}
				}
			attendanceVO2.setStaffNum((String) objs[11]);
			Map<String, Double> weekendDayAndWorkHoursMap = new LinkedHashMap<>();
			getWorkHoursInWeekend((String) objs[5], attendanceVO.getBeginDate(),
					attendanceVO.getEndDate(), weekendDayAndWorkHoursMap, attendanceVO2);
			attendanceVO2.setWeekendDayAndWorkHoursMap(weekendDayAndWorkHoursMap);
			attendanceVOs.add(attendanceVO2);
		}
		return attendanceVOs;
	}

	@Override
	public XSSFWorkbook exportAttendanceMsg(AttendanceVO attendanceVO) throws Exception {
		List<AttendanceVO> attendanceVOs = prepareExportData(attendanceVO);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("考勤统计明细");
		XSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格
		row.createCell((short) 0).setCellValue("序号");
		row.createCell((short) 1).setCellValue("部门");
		row.createCell((short) 2).setCellValue("工号");
		row.createCell((short) 3).setCellValue("姓名");
		//row.createCell((short) 4).setCellValue("总上班时长");
		row.createCell((short) 4).setCellValue("应出勤天数");
		row.createCell((short) 5).setCellValue("实际出勤天数");
		//row.createCell((short) 5).setCellValue("总休息天数");
		row.createCell((short) 6).setCellValue("白天加班天数");
		row.createCell((short) 7).setCellValue("晚上加班小时");
		row.createCell((short) 8).setCellValue("总休息天数");
		row.createCell((short) 9).setCellValue("非周末请假天数");
		row.createCell((short) 10).setCellValue("小时假总时长");
		row.createCell((short) 11).setCellValue("未刷卡次数");
		row.createCell((short) 12).setCellValue("迟到次数");
		row.createCell((short) 13).setCellValue("迟到时间（分钟）");
		row.createCell((short) 14).setCellValue("早退次数");
		row.createCell((short) 15).setCellValue("早退时间（分钟）");
		row.createCell((short) 16).setCellValue("未写工作日报次数");
		row.createCell((short) 17).setCellValue("未签到次数");

		for (int i = 0, j = sheet.getLastRowNum() + 1, length = attendanceVOs.size(); i < length; ++i, ++j) {
			XSSFRow row_data = sheet.createRow(j);
			AttendanceVO attendance = attendanceVOs.get(i);
			// 第四步，创建单元格，并设置值
			row_data.createCell((short) 0).setCellValue(i+1);
			row_data.createCell((short) 1).setCellValue(attendance.getDepartmentName());
			row_data.createCell((short) 2).setCellValue(attendance.getStaffNum());
			row_data.createCell((short) 3).setCellValue(attendance.getName());
			row_data.createCell((short) 4).setCellValue(attendance.getAttendanceDays());
			row_data.createCell((short) 5).setCellValue(attendance.getActualAttendanceDays());
			row_data.createCell((short) 6).setCellValue(attendance.getDayWorkHours());
			row_data.createCell((short) 7).setCellValue(attendance.getNightWorkHours());
			//row_data.createCell((short) 1).setCellValue(transformMinTime(attendance.getOfficeTime()));
			//Integer attendanceDays = attendance.getAttendanceDays();
			//row_data.createCell((short) 2).setCellValue(attendanceDays == null ? "0天" : attendanceDays + "天");
			VacationVO vacationVO = attendance.getVacationVO();
			if (vacationVO == null) {
				row_data.createCell((short) 8).setCellValue("0天0小时");
				row_data.createCell((short) 9).setCellValue("0");
				row_data.createCell((short) 10).setCellValue("0");
			} else {
				Integer showDays = vacationVO.getDays();
				showDays = showDays == null ? 0 : showDays;
				Double showHours = vacationVO.getShowHours();
				showHours = showHours == null ? 0 : showHours;
				row_data.createCell((short) 8).setCellValue(showDays + "天" + showHours + "时");
				row_data.createCell((short) 9).setCellValue(attendance.getVacationVO().getRestDaysAndHoursInWeekDay());
				row_data.createCell((short) 10).setCellValue(attendance.getVacationVO().getShowHours());
			}
			row_data.createCell((short) 11).setCellValue(attendance.getNotPunchTimes());
			row_data.createCell((short) 12).setCellValue(attendance.getLateTimes());
			row_data.createCell((short) 13).setCellValue(attendance.getLateTime());
			row_data.createCell((short) 14).setCellValue(attendance.getLeaveEarlyTimes());
			row_data.createCell((short) 15).setCellValue(attendance.getLeaveEarlyTime());
			row_data.createCell((short) 16).setCellValue(attendance.getWorkReportVO().getCount());
			row_data.createCell((short) 17).setCellValue(attendance.getSigninVO().getCount());
		}
		return wb;
	}

	/*	private String transformMinTime(int minTime) {
		int hour = (int) Math.floor(minTime / 60);
		int min = minTime % 60;
		return hour + "时" + min + "分";
	}*/
	/**
	 * 计算 上班累积时间  早退时间
	 */
	private int caculateOfficeTime(String timesStr) {
		if (StringUtils.isBlank(timesStr))
			return 0;
		String[] timeArr = timesStr.split(",");
		if (timeArr == null || timeArr.length == 0)
			return 0;
		int sum = 0;
		for (int i = 0, length = timeArr.length; i < length; i++) {
			sum += caculateOfficeTimePerDay(timeArr[i]);
		}
		return sum;
	}

	private int caculateOfficeTimePerDay(String timesStr) {
		try {
			if (StringUtils.isBlank(timesStr))
				return 0;
			String[] timeArr = timesStr.split(" ");
			if (timeArr == null || timeArr.length <= 1)
				return 0;
			List<Integer> times = new ArrayList<Integer>();
			times.clear();
			for (int i = 0, length = timeArr.length; i < length; i++) {
				if (StringUtils.isBlank(timeArr[i])) {
					continue;
				}
				String[] hourAndMin = timeArr[i].split(":");
				int hour = Integer.parseInt(hourAndMin[0]);
				int min = Integer.parseInt(hourAndMin[1]);
				times.add(hour * 60 + min);
			}
			java.util.Collections.sort(times);
			return times.get(times.size() - 1) - times.get(0);
		} catch (Exception e) {
			return 0;
		}
	}

	private String getAttendanceStatisticsBysql(AttendanceVO attendanceVO) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(a.AttendanceDate), GROUP_CONCAT(IFNULL(a.BeginDiff,0) ORDER BY a.AttendanceDate), GROUP_CONCAT(a.BeginType ORDER BY a.AttendanceDate), "
						+ "GROUP_CONCAT(a.EndType ORDER BY a.AttendanceDate), GROUP_CONCAT(IFNULL(a.EndDiff,0) ORDER BY a.AttendanceDate), a.UserID,a.companyID,GROUP_CONCAT"
						+ "(CONCAT(IFNULL(a.AttendanceTime, ''), CASE WHEN a.note!='' THEN CONCAT(' （备注：', a.note,'）') ELSE '' END ) ORDER BY a.AttendanceDate),GROUP_CONCAT"
						+ "(IFNULL(a.AttendanceDate, '') ORDER BY a.AttendanceDate),DepartmentName,DepartmentID,FIRST_,leaveDate FROM ( SELECT DISTINCT attendance.AttendanceDate, attendance.BeginDiff, "
						+ "attendance.BeginType, attendance.EndType, attendance.EndDiff, staff.UserID,attendance.AttendanceTime,attendance.companyID,attendance.UpdateTime,"
						+ "dep.DepartmentName,dep.DepartmentID, act.FIRST_,attendance.note,leaveDate FROM OA_GroupDetail groupDetail, "
						+ "OA_Staff staff,ACT_ID_MEMBERSHIP membership, OA_AttendanceDetail attendance,OA_Department dep,ACT_ID_USER act "
						+ "WHERE staff.UserID = membership.USER_ID_ AND attendance.UserID = staff.UserID AND "
						+ "membership.GROUP_ID_ = groupDetail.GroupID AND attendance.IsDeleted = 0 AND groupDetail.DepartmentID = dep.DepartmentID AND "
						+ "groupDetail.IsDeleted = 0 AND staff.IsDeleted = 0 AND staff.userId=act.ID_");
		sql.append(getWhereAttendanceStatisticsBysql(attendanceVO));
		sql.append(" GROUP BY attendance.AttendanceDate,staff.UserID)a group by a.UserID ORDER BY FIRST_");
		return sql.toString();

	}

	/*	private String getCountAttendanceStatisticsBysql(AttendanceVO attendanceVO) {
		StringBuffer sql = new StringBuffer(
				"select count(*) from(  SELECT count(a.AttendanceDate), GROUP_CONCAT(a.BeginDiff), GROUP_CONCAT(a.BeginType), "
						+ "GROUP_CONCAT(a.EndType), GROUP_CONCAT(a.EndDiff), a.UserID FROM ( SELECT DISTINCT attendance.AttendanceDate, attendance.BeginDiff, "
						+ "attendance.BeginType, attendance.EndType, attendance.EndDiff, staff.UserID FROM OA_GroupDetail groupDetail, "
						+ "OA_Staff staff, ACT_ID_MEMBERSHIP membership, OA_AttendanceDetail attendance "
						+ "WHERE staff.UserID = membership.USER_ID_ AND attendance.UserID = staff.UserID AND "
						+ "membership.GROUP_ID_ = groupDetail.GroupID AND attendance.IsDeleted = 0 AND "
						+ "groupDetail.IsDeleted = 0 AND staff.IsDeleted = 0 ");
		sql.append(getWhereAttendanceStatisticsBysql(attendanceVO));
		sql.append(" )a group by a.UserID )b");
		return sql.toString();
	}*/

	private String getWhereAttendanceStatisticsBysql(AttendanceVO attendanceVO) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(attendanceVO.getName())) {
			whereSql.append(" and staff.StaffName like '%" + attendanceVO.getName() + "%' ");
		}
		if (!StringUtils.isBlank(attendanceVO.getBeginDate())) {
			whereSql.append(" and attendance.AttendanceDate >= '" + attendanceVO.getBeginDate() + "'");
		}
		if (!StringUtils.isBlank(attendanceVO.getEndDate())) {
			whereSql.append(" and attendance.AttendanceDate <= '" + attendanceVO.getEndDate() + "'");
		}
		if (!StringUtils.isBlank(attendanceVO.getStaffNum())) {
			whereSql.append(" and act.FIRST_ = '" + attendanceVO.getStaffNum() + "'");
		}
		if (attendanceVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = " + attendanceVO.getCompanyID());
			if (attendanceVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
						attendanceVO.getCompanyID(), attendanceVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(attendanceVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}
		return whereSql.toString();
	}


	@Override
	public Map<String, String> getWorkHour(String userId) throws Exception {
		/*		String sql = "select SUBSTR(NAME_ FROM 1 FOR 1) from ACT_ID_USER `user` "+
				"inner join "+
				"ACT_ID_MEMBERSHIP ship "+
				"on "+
				"`user`.ID_ = ship.USER_ID_ "+
				"inner join "+
				"ACT_ID_GROUP `group` "+
				"on "+
				"ship.GROUP_ID_ = `group`.ID_ "+
				"where `user`.ID_='"+userId+"'";
		Object obj = baseDao.getUniqueResult(sql);
		String companyId = (String)obj;
		CompanyIDEnum company = CompanyIDEnum.valueOf(Integer.parseInt(companyId));
		String workTimes = company.getTimeLimitByDate(null);
		String[] workTimeArray = workTimes.split(" ");*/
		Map<String, String> workHourMap = new HashMap<>();
		List<GroupDetailVO> groupDetailVOs = staffService.findGroupDetailsByUserID(userId);
		if(CollectionUtils.isEmpty(groupDetailVOs)){
			return workHourMap;
		}
		GroupDetailVO groupDetailVO = groupDetailVOs.get(0);
		String companyId = groupDetailVO.getCompanyID()+"";
		String departmentId = groupDetailVO.getDepartmentID()+"";
		String[] workTimeArray = getWorkRestTimeByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(new Date()));
		workHourMap.put("beginTime", workTimeArray[0]);
		workHourMap.put("endTime", workTimeArray[3]);
		return workHourMap;
	}
	private static String getAttendanceTimeStr (List<String> attendanceTimeLst){
		String attendanceTimeStr = "";
		int index = 0;
		for(String attendanceTime: attendanceTimeLst){
			if(index==0){
				attendanceTimeStr += attendanceTime;
			}else{
				attendanceTimeStr += " "+attendanceTime;
			}
			index++;
		}
		return attendanceTimeStr;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<SalaryDetailEntity> getSalaryByCondition(String userId, String month, String year) {
		String hql = "from SalaryDetailEntity s where s.year="+year+" and s.month="+month+" and s.userId='"+userId+"'";
		List<SalaryDetailEntity> salaryDetailEntities = (List<SalaryDetailEntity>) baseDao.hqlfind(hql);
		if(null != salaryDetailEntities){
			for(SalaryDetailEntity salaryDetailEntity: salaryDetailEntities){
				if(null!=salaryDetailEntity.getDetail() && salaryDetailEntity.getDetail().length>0){
					try {
						salaryDetailEntity.setDetailList((List<String>) ObjectByteArrTransformer.toObject(salaryDetailEntity.getDetail()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return salaryDetailEntities;
	}


	@Override
	public List<Object> getLateOrLeaveNumInfo(String companyId, String lateOrLeave) {

		Calendar calendar = Calendar.getInstance();
		//当前年份
		int year = calendar.get(Calendar.YEAR);
		//当月
		int month = calendar.get(Calendar.MONTH)+1;//由于月份是从0开始的所以加1
		String sql = "";
		if(Constants.LATE.equals(lateOrLeave)){
			sql = "select @rownum\\:=@rownum+1 AS rownum, late, staffName, departmentName from (SELECT @rownum\\:=0) r,(SELECT\n" +
					"	count(AttendanceDetailID) late,\n" +
					"	UserID,\n" +
					"	(\n" +
					"		SELECT\n" +
					"			staff.staffName\n" +
					"		FROM\n" +
					"			OA_Staff staff\n" +
					"		WHERE\n" +
					"			staff.UserID = a.UserID\n" +
					"	) staffName,\n" +
					"		DepartmentName\n" +
					"FROM\n" +
					"	(select DISTINCT attend.UserID, dep.DepartmentName, AttendanceDetailID from OA_AttendanceDetail attend,\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail grDetail,\n" +
					"	OA_Department dep,\n" +
					"	OA_Position pos\n" +
					"WHERE\n" +
					"	(beginType = 2 AND IFNULL(lateStatus, 0) !=1)\n" +
					"AND attend.UserID = staff.UserID\n" +
					"AND staff.`Status`!=4 and staff.IsDeleted=0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = grDetail.GroupID\n" +
					"AND grDetail.DepartmentID = dep.DepartmentID\n" +
					"AND grDetail.PositionID = pos.PositionID\n"+
					"AND YEAR(attend.AttendanceDate) = "+year+"\n" +
					"AND MONTH(attend.AttendanceDate) = "+month+"\n" +
					"AND grDetail.CompanyID = "+companyId+"\n" +
					"AND pos.PositionName!='保洁员' GROUP BY UserID,AttendanceDetailID)a\n"+
					"GROUP BY\n" +
					"	UserID\n" +
					"ORDER BY\n" +
					"	late desc) rank ";
		}else{
			sql = "select @rownum\\:=@rownum+1 AS rownum, `leave`, staffName, departmentName from (SELECT @rownum\\:=0) r,(SELECT\n" +
					"	count(AttendanceDetailID) `leave`,\n" +
					"	UserID,\n" +
					"	(\n" +
					"		SELECT\n" +
					"			staff.staffName\n" +
					"		FROM\n" +
					"			OA_Staff staff\n" +
					"		WHERE\n" +
					"			staff.UserID = a.UserID\n" +
					"	) staffName,\n" +
					"		dep.DepartmentName\n" +
					"FROM\n" +
					"	(select DISTINCT attend.UserID, dep.DepartmentName, AttendanceDetailID from OA_AttendanceDetail attend,\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail grDetail,\n" +
					"	OA_Department dep,\n" +
					"	OA_Position pos\n" +
					"WHERE\n" +
					"	endType = 4\n" +
					"AND attend.UserID = staff.UserID\n" +
					"AND staff.`Status`!=4 and staff.IsDeleted=0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = grDetail.GroupID\n" +
					"AND grDetail.DepartmentID = dep.DepartmentID\n" +
					"AND grDetail.PositionID = pos.PositionID\n"+
					"AND YEAR(attend.AttendanceDate) = "+year+"\n" +
					"AND MONTH(attend.AttendanceDate) = "+month+"\n" +
					"AND grDetail.CompanyID = "+companyId+"\n" +
					"AND pos.PositionName!='保洁员' GROUP BY UserID,AttendanceDetailID)a\n"+
					"GROUP BY\n" +
					"	UserID\n" +
					"ORDER BY\n" +
					"	`leave` desc) rank";
		}
		return baseDao.findBySql(sql);
	}


	@Override
	public void confirmSalary(String year, String month) {
		String sql = "update OA_SalaryDetail set confirm=1 where year='"+year+"' and month='"+month+"'";
		baseDao.excuteSql(sql);
	}


	@Override
	public void updateLateStatus(String attendanceId) {
		String sql = "update OA_AttendanceDetail set lateStatus=1 where AttendanceDetailID="+attendanceId;
		baseDao.excuteSql(sql);
	}


	@Override
	public boolean checkWorkRestName(String workRestName) {
		String sql = "select count(*) from OA_WorkRestTime where workRestName='"+EscapeUtil.decodeSpecialChars(workRestName)+"' and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@Override
	public void saveWorkRestTime(WorkRestTimeEntity workRestTime, String workRestTimeId) {
		workRestTime.setAddTime(new Date());
		workRestTime.setIsDeleted(0);
		if(StringUtils.isNotBlank(workRestTimeId)){
			baseDao.hqlUpdate(workRestTime);
		}else{
			baseDao.hqlSave(workRestTime);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkRestTimeEntity> getWorkRestTimeList() {
		String hql = "from WorkRestTimeEntity where isDeleted=0";
		return (List<WorkRestTimeEntity>) baseDao.hqlfind(hql);
	}


	@Override
	public WorkRestTimeEntity getWorkRestTime(String workRestTimeId) {
		String hql = "from WorkRestTimeEntity where id="+workRestTimeId;
		Object obj = baseDao.hqlfindUniqueResult(hql);
		if(null != obj){
			return (WorkRestTimeEntity)obj;
		}
		return null;
	}


	@Override
	public void deleteWorkRestTime(String workRestTimeId) {
		String sql = "update OA_WorkRestTime set isDeleted=1 where id="+workRestTimeId;
		baseDao.excuteSql(sql);
	}


	@Override
	public void saveWorkArrange(WorkRestArrangeEntity workRestArrange) {
		workRestArrange.setIsDeleted(0);
		workRestArrange.setAddTime(new Date());
		updateLastWorkArrange(workRestArrange.getBeginTime(), workRestArrange.getCompanyId(), workRestArrange.getDepartmentId());
		baseDao.hqlSave(workRestArrange);
	}


	@SuppressWarnings("unchecked")
	@Override
	public ListResult<WorkRestArrangeEntity> getWorkTimeArranges(Integer limit, Integer page) {
		String hql = "from WorkRestArrangeEntity where isDeleted=0 order by beginTime desc";
		List<WorkRestArrangeEntity> workRestArranges = (List<WorkRestArrangeEntity>) baseDao.hqlPagedFind(hql, page, limit);
		for(WorkRestArrangeEntity workRestArrange: workRestArranges){
			workRestArrange.setWorkRestTime(getWorkRestTime(workRestArrange.getWorkRestId()));
			String companyID = workRestArrange.getCompanyId();
			if(StringUtils.isNotBlank(companyID)){
				workRestArrange.setCompanyName(companyDao.getCompanyByCompanyID(
						Integer.parseInt(companyID)).getCompanyName());
			}
			String departmentId = workRestArrange.getDepartmentId();
			if(StringUtils.isNotBlank(departmentId)){
				workRestArrange.setDepartmentName(departmentDao.getDepartmentByDepartmentID(
						Integer.parseInt(departmentId)).getDepartmentName());
			}
		}
		String hqlCount = "select count(w.id) from WorkRestArrangeEntity w where isDeleted=0";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount)+"");
		return new ListResult<WorkRestArrangeEntity>(workRestArranges, count);
	}


	@Override
	public void deleteWorkRestArrange(String workRestArrangeId) {
		String sql = "update OA_WorkRestArrange set isDeleted=1 where id="+workRestArrangeId;
		baseDao.excuteSql(sql);
	}


	@Override
	public boolean checkWorkTimeArrange(String departmentId, String companyId) {
		String sql = "select count(*) from OA_WorkRestArrange where isDeleted=0 and companyId="+companyId;
		if(StringUtils.isNotBlank(departmentId)){
			sql += " and IFNULL(departmentId, 0)="+departmentId;
		}else{
			sql += " and departmentId is null";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public double getDailyHoursByCompanyIDOrDepartmentId(String companyId, String departmentId, String date) throws Exception{
		String hql = "";
		if(StringUtils.isBlank(departmentId)){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}else{
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND IFNULL(departmentId, 0)="+departmentId+"\n"+
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		List<Object> objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			throw new RuntimeException("获取分公司的作息时间失败，分公司代号："+companyId);
		}
		//取最近一次
		WorkRestArrangeEntity workRestArrange = (WorkRestArrangeEntity)objs.get(objs.size()-1);
		WorkRestTimeEntity workRestTime = getWorkRestTime(workRestArrange.getWorkRestId());
		return Double.parseDouble(workRestTime.getWorkHours());
	}


	@SuppressWarnings("unchecked")
	@Override
	public String[] getWorkRestTimeByCompanyIDOrDepartmentId(String companyId, String departmentId, String date) throws Exception{
		String hql = "";
		if(StringUtils.isBlank(departmentId)){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}else{
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND IFNULL(departmentId, 0)="+departmentId+"\n"+
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		List<Object> objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			throw new RuntimeException("获取分公司的作息时间失败，分公司代号："+companyId);
		}
		//取最近一次
		WorkRestArrangeEntity workRestArrange = (WorkRestArrangeEntity)objs.get(objs.size()-1);
		WorkRestTimeEntity workRestTime = getWorkRestTime(workRestArrange.getWorkRestId());
		String[] workTimes = new String[4];
		workTimes[0] = workRestTime.getWorkBeginTime();
		workTimes[1] = workRestTime.getRestBeginTime();
		workTimes[2] = workRestTime.getRestEndTime();
		workTimes[3] = workRestTime.getWorkEndTime();
		return workTimes;
	}


	@Override
	public Map<String, SigninVO> findSignByDateAndCompanyID(Integer companyID, String beginDate, String endDate)
			throws Exception {
		String sql1 = "select noSign.userID,count(*),GROUP_CONCAT(noSign.attendanceDate ORDER BY noSign.attendanceDate DESC ) from (select attendance.userID userID,"
				+ "attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where staff.positionCategory = 1 and staff.isDeleted=0 and attendance.attendanceDate >= '"
				+ beginDate + "' and attendance.attendanceDate <= '" + endDate + "'";  
		if(null != companyID){
			sql1 += " and attendance.companyID='"
					+ companyID + "' "
					+ "and attendance.isDeleted = 0 and signin.userID is null) noSign where not exists(select special.userID from OA_Special special where special.type=2 and special.isDeleted = 0 "
					+ "and special.userID=noSign.userID ) group by noSign.userID ";
		}else{
			sql1 += " and attendance.isDeleted = 0 and signin.userID is null) noSign where not exists(select special.userID from OA_Special special where special.type=2 and special.isDeleted = 0 "
					+ "and special.userID=noSign.userID ) group by noSign.userID ";
		}
		List<Object> result1 = baseDao.findBySql(sql1);
		String sql2 = "select noSign.userID,count(*),GROUP_CONCAT(noSign.attendanceDate ORDER BY noSign.attendanceDate DESC) from (select attendance.userID userID,"
				+ "attendance.attendanceDate attendanceDate from OA_AttendanceDetail attendance left join OA_Signin signin "
				+ "on attendance.userID=signin.userID and attendance.attendanceDate=signin.signinDate "
				+ "left join OA_Staff staff on attendance.userID = staff.userID "
				+ "where  staff.positionCategory = 2 and staff.isDeleted=0 and attendance.attendanceDate >= '"
				+ endDate + "' and attendance.attendanceDate <= '" + beginDate + "'";
		if(null != companyID){
			sql2 +=  " and attendance.companyID='"
					+ companyID + "' "
					+ "and attendance.isDeleted = 0 and signin.userID is null) noSign left join OA_Special special on noSign.userID=special.userID "
					+ "where special.type=2 and special.isDeleted = 0 group by noSign.userID";
		}else{
			sql2 +=  " and attendance.isDeleted = 0 and signin.userID is null) noSign left join OA_Special special "
					+ "on noSign.userID=special.userID where special.type=2 and special.isDeleted = 0 group by noSign.userID";
		}
		List<Object> result = baseDao.findBySql(sql2);
		result.addAll(result1);
		List<SigninVO> signinVOs = new ArrayList<>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			SigninVO signinVO = new SigninVO();
			signinVO.setUserID((String) objs[0]);
			signinVO.setCount(Integer.parseInt(objs[1].toString()));
			signinVO.setSignDates(StringUtils.split((String) objs[2], ","));
			signinVOs.add(signinVO);
		}

		//signInFilter(signinVOs,beginDate, endDate, companyID);
		Map<String, SigninVO> noSignInMap = new HashMap<>();
		for(SigninVO signinVO: signinVOs){
			noSignInMap.put(signinVO.getUserID(), signinVO);
		}
		return noSignInMap;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void updateLastWorkArrange(String beginTime, String companyId, String departmentId) {
		String hql = "";
		if(StringUtils.isNotBlank(departmentId)){
			hql = "from WorkRestArrangeEntity where isDeleted=0 and companyId="+companyId+" and ifNull(departmentId, 0)="+departmentId+" and endTime is null";
		}else{
			hql = "from WorkRestArrangeEntity where isDeleted=0 and companyId="+companyId+" and departmentId is null and (endTime is null or endTime='')";
		}
		List<WorkRestArrangeEntity> workRestArranges = (List<WorkRestArrangeEntity>) baseDao.hqlfind(hql);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getSimpleDate(beginTime));
		cal.add(Calendar.DATE, -1);
		String endTime = DateUtil.formateDate(cal.getTime());
		for(WorkRestArrangeEntity workRestArrange: workRestArranges){
			workRestArrange.setEndTime(endTime);
			baseDao.hqlUpdate(workRestArrange);
		}
	}


	@Override
	public boolean checkWorkRestIsArranged(String workRestId) {
		String sql = "select count(*) from OA_WorkRestArrange where isDeleted=0 and workRestId="+workRestId;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String getWorkOverBeginTimeByCompanyIDOrDepartmentId(String companyId, String departmentId, String date)
			throws Exception {
		String hql = "";
		if(StringUtils.isBlank(departmentId)){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}else{
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND IFNULL(departmentId, 0)="+departmentId+"\n"+
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		List<Object> objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'\n" +
					"AND IFNULL(endTime, '"+date+"') >= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			hql = "FROM WorkRestArrangeEntity\n" +
					"WHERE\n" +
					"	isDeleted = 0\n" +
					"AND companyId="+companyId+"\n" +
					"AND departmentId IS NULL\n" +
					"AND beginTime <= '"+date+"'";
		}
		objs = (List<Object>) baseDao.hqlfind(hql);
		if(null == objs || objs.size()<1){
			throw new RuntimeException("获取分公司的作息时间失败，分公司代号："+companyId);
		}
		//取最近一次
		WorkRestArrangeEntity workRestArrange = (WorkRestArrangeEntity)objs.get(objs.size()-1);
		WorkRestTimeEntity workRestTime = getWorkRestTime(workRestArrange.getWorkRestId());
		return workRestTime.getWorkOverBeginTime();
	}


	@Override
	public String[] getVacationDaysAndHours(String startime, String endtime, String userId) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			//总部优先
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		String companyIDString = group.getType().split("_")[0];
		String departmentId = group.getType().split("_")[1];
		VacationVO vacationVO = vacationService.getDaysAndHours(startime, endtime, userId,
				Integer.parseInt(companyIDString), Integer.parseInt(departmentId));
		if(null == vacationVO){
			return new String[]{"",""};
		}
		return new String[]{vacationVO.getDays()==null?"":vacationVO.getDays()+"",
				vacationVO.getShowHours()==null?"":vacationVO.getShowHours()+""};
	}


	@SuppressWarnings("unchecked")
	@Override
	public void saveMonthlyRestDay(MonthlyRestEntity monthlyRest) {
		if(monthlyRest.getId() != null){
			monthlyRest.setIsDelete(0);
			monthlyRest.setAddTime(new Date());
			baseDao.hqlUpdate(monthlyRest);
		}else{
			monthlyRest.setIsDelete(0);
			monthlyRest.setAddTime(new Date());
			baseDao.hqlSave(monthlyRest);
			//在职或者本月离职的人员
			String hql = "from StaffEntity where isDeleted=0 and (status!=4 or (year(leaveDate)="+
			        monthlyRest.getYear()+" and month(leaveDate)="+monthlyRest.getMonth()+"))";
			List<StaffEntity> staffs = (List<StaffEntity>) baseDao.hqlfind(hql);
			for(StaffEntity staff: staffs){
				UserMonthlyRestEntity userMonthlyRest = new UserMonthlyRestEntity();
				userMonthlyRest.setMonth(monthlyRest.getMonth());
				userMonthlyRest.setYear(monthlyRest.getYear());
				userMonthlyRest.setUserId(staff.getUserID());
				userMonthlyRest.setRestDays(monthlyRest.getRestDays());
				userMonthlyRest.setWorkDays(monthlyRest.getWorkDays());
				userMonthlyRest.setAddTime(new Date());
				baseDao.hqlSave(userMonthlyRest);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MonthlyRestEntity> getMonthlyRests() {
		String hql = "from MonthlyRestEntity where isDelete=0 order by year+0 desc, month+0 desc";
		return (List<MonthlyRestEntity>) baseDao.hqlPagedFind(hql, 1, 10);
	}


	@Override
	public boolean checkMonthExist(String year, String month) {
		String sql = "select count(*) from OA_MonthlyRest where year='"+year+"' and month='"+month+"' and isDelete=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void deleteRest(String restId) {
		String hql = "from MonthlyRestEntity where id="+restId;
		MonthlyRestEntity monthlyRest = (MonthlyRestEntity) baseDao.hqlfindUniqueResult(hql);
		monthlyRest.setIsDelete(1);
		baseDao.hqlUpdate(monthlyRest);
		hql = "from UserMonthlyRestEntity where year="+monthlyRest.getYear()+" and month="+monthlyRest.getMonth();
		List<UserMonthlyRestEntity> userMonthlyRests = (List<UserMonthlyRestEntity>) baseDao.hqlfind(hql);
		for(UserMonthlyRestEntity userMonthlyRest: userMonthlyRests){
			userMonthlyRest.setIsDeleted(1);
			baseDao.hqlUpdate(userMonthlyRest);
		}
	}


	@Override
/*	public MonthlyRestEntity getMonthlyRest(int year, int month) {
		String hql = "from MonthlyRestEntity where year="+year+" and month="+month+" and isDelete=0";
		return (MonthlyRestEntity) baseDao.hqlfindUniqueResult(hql);
	}*/
	
	public UserMonthlyRestEntity getMonthlyRest(String userId, int year, int month) {
		String hql = "from UserMonthlyRestEntity where year="+year+" and month="+month+" and isDeleted=0 and userId='"+userId+"'";
		return (UserMonthlyRestEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public List<Object> findAbnormalAttendanceDatas(String date, String staffName) throws Exception {
		String sql = "SELECT\n" +
				"	staff.UserID\n" +
				"FROM\n" +
				"	OA_Staff staff\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		AttendanceTime,\n" +
				"		UserID\n" +
				"	FROM\n" +
				"		OA_AttendanceDetail\n" +
				"	WHERE\n" +
				"		IsDeleted = 0\n" +
				"	AND AttendanceDate = '"+date+"'\n" +
				") atta ON staff.UserID = atta.UserID\n" +
				"WHERE\n" +
				"	staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND DATE(staff.AddTime) < '"+date+"'\n" +
				"AND AttendanceTime IS NULL\n";
		if(StringUtils.isNotBlank(staffName)){
			sql += "AND staff.staffName like '%"+EscapeUtil.decodeSpecialChars(staffName)+"%'";
		}
		//未打卡人员
		List<Object> objList = baseDao.findBySql(sql);
		List<Object> abnormalAttendanceDatas = new ArrayList<>();
		for(Object obj: objList){
			String userId = String.valueOf(obj);
			List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
			if(groups.size()<1){
				continue;
			}
			String companyIDString = groups.get(0).getType().split("_")[0];
			String departmentId = groups.get(0).getType().split("_")[1];
			String[] workTimeArray = getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, date);
			String beginTime = workTimeArray[0];
			String endTime = workTimeArray[3];
			beginTime = date+" "+beginTime+":00";
			endTime = date+" "+endTime+":00";
			//检查是否当天是否请过假
			sql = "SELECT\n" +
					"	count(VacationID)\n" +
					"FROM\n" +
					"	OA_Vacation\n" +
					"WHERE\n" +
					"	(\n" +
					"		BeginDate < '"+endTime+"'\n" +
					"		AND EndDate > '"+beginTime+"'\n" +
					"	)\n" +
					"AND (\n" +
					"	ProcessInstanceID IS NULL\n" +
					"	OR (ProcessStatus != 2 AND ProcessStatus != 31)\n" +
					")\n" +
					"AND IsDeleted = 0\n" +
					//"AND RequestUserID='"+userId+"'";
					"AND locate('"+userId+"',RequestUserID)>0";
			int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
			if(count==0){
				sql = "select '"+date+"', staff.UserID, StaffName, CompanyName, DepartmentName from \n" +
						"	OA_Staff staff,\n" +
						"	(\n" +
						"		SELECT\n" +
						"			USER_ID_,\n" +
						"			GROUP_ID_\n" +
						"		FROM\n" +
						"			ACT_ID_MEMBERSHIP\n" +
						"		GROUP BY\n" +
						"			USER_ID_\n" +
						"	) ship,\n" +
						"	OA_GroupDetail detail,\n" +
						"	OA_Department dep,\n" +
						"	OA_Company company\n" +
						"WHERE\n" +
						"	staff.userID = ship.USER_ID_\n" +
						"AND ship.GROUP_ID_ = detail.GroupID\n" +
						"AND detail.DepartmentID = dep.DepartmentID\n" +
						"AND detail.CompanyID = company.CompanyID\n" +
						"AND staff.userID='"+userId+"' AND staff.isDeleted=0";
				Object data = baseDao.getUniqueResult(sql);
				abnormalAttendanceDatas.add(data);
			}
		}
		return abnormalAttendanceDatas;
	}


	@Override
	public void addAttendanceTime(AttendanceDetailEntity attend) throws Exception {
		String[] times = attend.getAttendanceTime().split(" ");
		String date = DateUtil.formateDate(attend.getAttendanceDate());
		List<Group> groups = identityService.createGroupQuery().groupMember(attend.getUserID()).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		String companyIDString = group.getType().split("_")[0];
		String departmentId = group.getType().split("_")[1];
		String[] arr_times = getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, date);
		String beginTime = date + " "+arr_times[0]+":00";
		String endTime = date  + " "+ arr_times[3]+":00";
		//上午下班时间
		String amEndTime = date + " "+arr_times[1]+":00";
		//下午上班时间
		String pmBeginTime = date + " "+arr_times[2]+":00";
		checkBeginTime(date + " " + times[0].trim() + ":00", beginTime, attend, amEndTime, pmBeginTime);
		checkEndTime(date + " " + times[times.length - 1].trim() + ":00", endTime, attend, amEndTime, pmBeginTime);
		attend.setCompanyID(Integer.parseInt(companyIDString));
		attend.setIsDeleted(0);
		attend.setAddTime(new Date());
		baseDao.hqlSave(attend);
	}


	@Override
	public XSSFWorkbook exportPartnerAttendDatas() throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT count(a.AttendanceDate), GROUP_CONCAT(IFNULL(a.BeginDiff,0) ORDER BY a.AttendanceDate), GROUP_CONCAT(a.BeginType ORDER BY a.AttendanceDate), "
						+ "GROUP_CONCAT(a.EndType ORDER BY a.AttendanceDate), GROUP_CONCAT(IFNULL(a.EndDiff,0) ORDER BY a.AttendanceDate), a.UserID,a.companyID,GROUP_CONCAT(IFNULL(a.AttendanceTime,'') ORDER BY a.AttendanceDate),GROUP_CONCAT(IFNULL(a.AttendanceDate, '') ORDER BY a.AttendanceDate),DepartmentName,DepartmentID,FIRST_ FROM ( SELECT DISTINCT attendance.AttendanceDate, attendance.BeginDiff, "
						+ "attendance.BeginType, attendance.EndType, attendance.EndDiff, staff.UserID,attendance.AttendanceTime,attendance.companyID,attendance.UpdateTime,dep.DepartmentName,dep.DepartmentID, act.FIRST_ FROM OA_GroupDetail groupDetail, "
						+ "OA_Staff staff,ACT_ID_MEMBERSHIP membership, OA_AttendanceDetail attendance,OA_Department dep,ACT_ID_USER act, OA_Partner partner "
						+ "WHERE staff.userID = partner.userId and partner.isDeleted=0 and staff.UserID = membership.USER_ID_ AND attendance.UserID = staff.UserID AND "
						+ "membership.GROUP_ID_ = groupDetail.GroupID AND attendance.IsDeleted = 0 AND groupDetail.DepartmentID = dep.DepartmentID AND "
						+ "groupDetail.IsDeleted = 0 AND staff.IsDeleted = 0 AND staff.userId=act.ID_");
		sql.append(" and attendance.AttendanceDate >= '2017-01-01'");
		sql.append(" and attendance.AttendanceDate <= '2017-12-31'");
		sql.append(" GROUP BY attendance.AttendanceDate,staff.UserID)a group by a.UserID ORDER BY FIRST_");
		List<Object> list = baseDao.findBySql(sql.toString());
		// 先处理list 中的打卡字段 进行排序 选出 对应的 limit 条数据
		for (int i = 0, size = list.size(); i < size; i++) {
			Object[] objs = (Object[]) list.get(i);
			objs[7] = caculateOfficeTime((String) objs[7]);
		}
		/*		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return (int) (((Object[]) o2)[7]) - (int) (((Object[]) o1)[7]);
			}
		});*/
		List<AttendanceVO> attendanceVOs = new ArrayList<AttendanceVO>(list.size());
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			AttendanceVO attendanceVO2 = new AttendanceVO();
			attendanceVO2.setAttendanceDays(Integer.parseInt(objs[0].toString()));
			try {
				VacationVO vacationVO = vacationService.getDaysAndHours("2017-01-01",
						"2017-12-31", (String) objs[5], (Integer) objs[6], (Integer) objs[10]);
				attendanceVO2.setVacationVO(vacationVO);
			} catch (Exception e) {
				logger.error((String) objs[5]);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			String[] beginTypes = StringUtils.split((String) objs[2], ",");
			int beginType = 0;
			for (int i = 0; i < beginTypes.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.NO_DATA.getValue()))) {
					beginType += 1;
				}
			}
			int endType = 0;
			String[] endTypes = StringUtils.split((String) objs[3], ",");
			for (int i = 0; i < endTypes.length; i++) {
				if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.NO_DATA.getValue()))) {
					endType += 1;
				}
			}
			attendanceVO2.setNotPunchTimes(beginType + endType);

			String[] beginDiff = StringUtils.split((String) objs[1], ",");
			Integer times = 0;
			//迟到次数
			int lateTimes = 0;
			for (int i = 0; i < beginDiff.length; i++) {
				if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.LATE.getValue()))) {
					times += Integer.parseInt(String.valueOf(Long.parseLong(beginDiff[i]) / (1000 * 60)));
					lateTimes++;
				}
			}
			attendanceVO2.setLateTimes(lateTimes);
			attendanceVO2.setName(staffService.getStaffByUserID((String) objs[5]).getLastName());
			attendanceVO2.setOfficeTime((int) (objs[7]));
			attendanceVO2.setDepartmentName((String) objs[9]);
			attendanceVO2.setStaffNum((String) objs[11]);
			attendanceVOs.add(attendanceVO2);
		}
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("考勤统计明细");
		XSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格
		row.createCell((short) 0).setCellValue("序号");
		row.createCell((short) 1).setCellValue("工号");
		row.createCell((short) 2).setCellValue("部门");
		row.createCell((short) 3).setCellValue("姓名");
		row.createCell((short) 4).setCellValue("上班时长");
		row.createCell((short) 5).setCellValue("总休息天数");
		row.createCell((short) 6).setCellValue("迟到次数");

		for (int i = 0, j = sheet.getLastRowNum() + 1, length = attendanceVOs.size(); i < length; ++i, ++j) {
			XSSFRow row_data = sheet.createRow(j);
			AttendanceVO attendance = attendanceVOs.get(i);
			// 第四步，创建单元格，并设置值
			row_data.createCell((short) 0).setCellValue(i+1);
			row_data.createCell((short) 1).setCellValue(attendance.getStaffNum());
			row_data.createCell((short) 2).setCellValue(attendance.getDepartmentName());
			row_data.createCell((short) 3).setCellValue(attendance.getName());
			row_data.createCell((short) 4).setCellValue(transformMinTime(attendance.getOfficeTime()));
			//Integer attendanceDays = attendance.getAttendanceDays();
			//row_data.createCell((short) 2).setCellValue(attendanceDays == null ? "0天" : attendanceDays + "天");
			VacationVO vacationVO = attendance.getVacationVO();
			if (vacationVO == null) {
				row_data.createCell((short) 5).setCellValue("0天0小时");
			} else {
				Integer showDays = vacationVO.getDays();
				showDays = showDays == null ? 0 : showDays;
				Double showHours = vacationVO.getShowHours();
				showHours = showHours == null ? 0 : showHours;
				row_data.createCell((short) 5).setCellValue(showDays + "天" + showHours + "时");
			}
			row_data.createCell((short) 6).setCellValue(attendance.getLateTimes());
		}
		return wb;
	}
	private String transformMinTime(int minTime) {
		int hour = (int) Math.floor(minTime / 60);
		int min = minTime % 60;
		return hour + "时" + min + "分";
	}
	@Override
	public AttendanceVO findAttendanceStatisticsByUserId(Date beginDate, Date endDate, String userId) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		Object obj = getAttendanceStatisticsByUserId(beginDate, endDate, userId);
		Object[] objs = (Object[]) obj;
		//未出勤
		if(Integer.parseInt(objs[0].toString()) == 0){
			return null;
		}
		AttendanceVO attendanceVO = new AttendanceVO();
		attendanceVO.setAttendanceDays(Integer.parseInt(objs[0].toString()));
		attendanceVO.setCompanyID((Integer) objs[6]);
		attendanceVO.setDepartmentID((Integer) objs[9]);
		VacationVO vacationVO = vacationService.getDaysAndHoursForCalSalary(DateUtil.formateDate(beginDate),
				DateUtil.formateDate(endDate), (String) objs[5], (Integer) objs[6], (Integer) objs[9]);
		attendanceVO.setVacationVO(vacationVO);
		String[] beginTypes = StringUtils.split((String) objs[2], ",");
		int beginType = 0;
		String unPublishDateIndex="";
		for (int i = 0; i < beginTypes.length; i++) {
			if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.NO_DATA.getValue()))) {
				beginType += 1;
				unPublishDateIndex+="am"+i+",";
			}
		}
		int endType = 0;
		String[] endTypes = StringUtils.split((String) objs[3], ",");
		for (int i = 0; i < endTypes.length; i++) {
			if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.NO_DATA.getValue()))) {
				endType += 1;
				unPublishDateIndex+="pm"+i+",";
			}
		}
		attendanceVO.setNotPunchTimes(beginType + endType);
		attendanceVO.setNotPushDateIndex(unPublishDateIndex);
		String[] beginDiff = StringUtils.split((String) objs[1], ",");
		Integer times = 0;
		//迟到次数
		int lateTimes = 0;
		for (int i = 0; i < beginDiff.length; i++) {
			if (beginTypes[i].trim().equals(String.valueOf(AttendanceBeginType.LATE.getValue()))) {
				times += Integer.parseInt(String.valueOf(Long.parseLong(beginDiff[i]) / (1000 * 60)));
				lateTimes++;
			}
		}

		String[] endDiff = StringUtils.split((String) objs[4], ",");
		Integer time = 0;
		//早退次数
		int leaveEarlyTimes = 0;
		for (int i = 0; i < endDiff.length; i++) {
			if (endTypes[i].trim().equals(String.valueOf(AttendanceEndType.EARLY.getValue()))) {
				time += Integer.parseInt(String.valueOf(Long.parseLong(endDiff[i]) / (1000 * 60)));
				leaveEarlyTimes++;
			}
		}
		attendanceVO.setDetail((objs[8])==null?null:(String)(objs[8]));
		attendanceVO.setLateTime(times);
		attendanceVO.setLateTimes(lateTimes);
		attendanceVO.setLeaveEarlyTime(time);;
		attendanceVO.setLeaveEarlyTimes(leaveEarlyTimes);
		attendanceVO.setName(staffService.getStaffByUserID((String) objs[5]).getLastName());
		//获取晚上加班时长
		String[] nightWorkTimesAndhours = workOvertimeService.getNightWorkTimesAndhours((String) objs[5], DateUtil.formateDate(beginDate), DateUtil.formateDate(endDate));
		attendanceVO.setNightWorkTimes(nightWorkTimesAndhours[0]);
		attendanceVO.setNightWorkHours(nightWorkTimesAndhours[1]);
		//获取当月公休天数
		UserMonthlyRestEntity monthlyRest = getMonthlyRest((String) objs[5], year, month);
		if(null != monthlyRest){
			int monthlyRestDays = Integer.parseInt(monthlyRest.getRestDays());
			double dailyHours = getDailyHoursByCompanyIDOrDepartmentId(objs[6]+"",
					objs[9]+"", DateUtil.formateFullDate(beginDate));
			if(null == vacationVO){
				attendanceVO.setDayWorkHours(monthlyRestDays+"天");
				attendanceVO.setDailyWorkOverTimeHours(monthlyRestDays*dailyHours);
			}else{
				int days = vacationVO.getDays()==null ? 0:vacationVO.getDays();
				double hours = vacationVO.getShowHours()==null ? 0:vacationVO.getShowHours();
				//白天加班时长
				double dailyWorkOverTimeHours = monthlyRestDays*dailyHours - (days*dailyHours+hours);
				attendanceVO.setDailyWorkOverTimeHours(dailyWorkOverTimeHours);
				if(dailyWorkOverTimeHours>0){
					int day = (int) Math.floor(dailyWorkOverTimeHours/dailyHours);
					hours =  dailyWorkOverTimeHours - day*dailyHours;
					if(day>0){
						if(hours>0){
							attendanceVO.setDayWorkHours(day+"天"+hours+"小时");
						}else{
							attendanceVO.setDayWorkHours(day+"天");
						}
					}else if(hours>0){
						attendanceVO.setDayWorkHours(hours+"小时");
					}
				}
			}
		}
		Map<String, Double> weekendDayAndWorkHoursMap = new LinkedHashMap<>();
		getWorkHoursInWeekend((String) objs[5], DateUtil.formateDate(beginDate),
				DateUtil.formateDate(endDate), weekendDayAndWorkHoursMap, attendanceVO);
		attendanceVO.setWeekendDayAndWorkHoursMap(weekendDayAndWorkHoursMap);
		return attendanceVO;
	}
	private Object getAttendanceStatisticsByUserId(Date beginDate, Date endDate, String userId) {
		String sql = "SELECT\n" +
				"	count(a.AttendanceDate),\n" +
				"	GROUP_CONCAT(\n" +
				"		IFNULL(a.BeginDiff, 0)\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),\n" +
				"	GROUP_CONCAT(\n" +
				"		a.BeginType\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),\n" +
				"	GROUP_CONCAT(\n" +
				"		a.EndType\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),\n" +
				"	GROUP_CONCAT(\n" +
				"		IFNULL(a.EndDiff, 0)\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),\n" +
				"	a.UserID,\n" +
				"	a.companyID,\n" +
				"	GROUP_CONCAT(\n" +
				"		IFNULL(a.AttendanceTime, '')\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),\n" +
				"	GROUP_CONCAT(\n" +
				"		IFNULL(a.AttendanceDate, '')\n" +
				"		ORDER BY\n" +
				"			a.AttendanceDate\n" +
				"	),DepartmentID\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT DISTINCT\n" +
				"			attendance.AttendanceDate,\n" +
				"			attendance.BeginDiff,\n" +
				"			attendance.BeginType,\n" +
				"			attendance.EndType,\n" +
				"			attendance.EndDiff,\n" +
				"			staff.UserID,\n" +
				"			attendance.AttendanceTime,\n" +
				"			attendance.companyID,\n" +
				"			attendance.UpdateTime,\n" +
				"			dep.DepartmentName,\n" +
				"			dep.DepartmentID\n" +
				"		FROM\n" +
				"			OA_GroupDetail groupDetail,\n" +
				"			OA_Staff staff,\n" +
				"			ACT_ID_MEMBERSHIP membership,\n" +
				"			OA_AttendanceDetail attendance,\n" +
				"			OA_Department dep\n" +
				"		WHERE\n" +
				"			staff.UserID = membership.USER_ID_\n" +
				"		AND attendance.UserID = staff.UserID\n" +
				"		AND membership.GROUP_ID_ = groupDetail.GroupID\n" +
				"		AND attendance.IsDeleted = 0\n" +
				"		AND groupDetail.DepartmentID = dep.DepartmentID\n" +
				"		AND groupDetail.IsDeleted = 0\n" +
				"		AND staff.IsDeleted = 0\n" +
				"		AND staff.userId = '"+userId+"'\n" +
				"		AND attendance.AttendanceDate >= '"+DateUtil.formateDate(beginDate)+"'\n" +
				"		AND attendance.AttendanceDate <= '"+DateUtil.formateDate(endDate)+"' \n" +
				" 		GROUP BY attendance.AttendanceDate" +
				"	) a";
		return baseDao.getUniqueResult(sql);
	}


	@Override
	public List<String> getAbnormalDays(String userId, Date beginDate, Date endDate) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		String companyIDString = groups.get(0).getType().split("_")[0];
		String departmentId = groups.get(0).getType().split("_")[1];
		List<String> allDates = getAllDates(beginDate, endDate);
		String sql = "SELECT\n" +
				"	AttendanceDate\n" +
				"FROM\n" +
				"	OA_AttendanceDetail\n" +
				"WHERE\n" +
				"	IsDeleted = 0\n" +
				"AND AttendanceDate IN (\n'" +StringUtils.join(allDates, "','")+"')\n" +
				"AND UserID = '"+userId+"'";
		List<Object> punchDates = baseDao.findBySql(sql);
		//未打卡日期
		List<String> unPunchDates = allDates;
		for(Object punchDateObj: punchDates){
			unPunchDates.remove(DateUtil.formateDate((Date)punchDateObj));
		}
		List<String> abnormalDays = new ArrayList<>();
		for(String unPunchDate: unPunchDates){
			String[] workTimeArray = getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, unPunchDate);
			String beginTime = workTimeArray[0];
			String endTime = workTimeArray[3];
			beginTime = unPunchDate+" "+beginTime+":00";
			endTime = unPunchDate+" "+endTime+":00";
			//检查是否当天是否请过假
			sql = "SELECT\n" +
					"	count(VacationID)\n" +
					"FROM\n" +
					"	OA_Vacation\n" +
					"WHERE\n" +
					"	(\n" +
					"		BeginDate < '"+endTime+"'\n" +
					"		AND EndDate > '"+beginTime+"'\n" +
					"	)\n" +
					"AND (\n" +
					"	ProcessInstanceID IS NULL\n" +
					"	OR (ProcessStatus != 2 AND ProcessStatus != 31)\n" +
					")\n" +
					"AND IsDeleted = 0\n" +
					"AND locate('"+userId+"',RequestUserID)>0";
			int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
			//未请假
			if(count==0){
				abnormalDays.add(unPunchDate);
			}
		}
		return abnormalDays;
	}


	private List<String> getAllDates(Date beginDate, Date endDate) {
		List<String> allDates = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		while(!beginDate.equals(endDate)){
			allDates.add(DateUtil.formateDate(beginDate));
			cal.add(Calendar.DATE, 1);
			beginDate = cal.getTime();
		}
		allDates.add(DateUtil.formateDate(endDate));
		return allDates;
	}


	@Override
	public String getLeaveStaffLastAttendanceDate(String userId, int month) {
		String sql = "SELECT\n" +
				"	AttendanceDate\n" +
				"FROM\n" +
				"	OA_AttendanceDetail\n" +
				"WHERE\n" +
				"	UserID = '"+userId+"'\n" +
				"AND BeginType="+AttendanceBeginType.NORMAL.getValue()+"\n" +
				"AND EndType="+AttendanceEndType.NORMAL.getValue()+"\n" +
				"AND MONTH (AttendanceDate) = "+month+"\n" +
				"ORDER BY\n" +
				"	AttendanceDate DESC limit 0,1";
		Date lastAttendanceDate = (Date) baseDao.getUniqueResult(sql);
		if(null != lastAttendanceDate){
			return DateUtil.formateDate(lastAttendanceDate);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<UserMonthlyRestEntity> getUserMonthlyRests(String staffName, String year, String month,
			Integer limit, Integer page) {
		String hql = "select rest from UserMonthlyRestEntity rest, StaffEntity staff where"
				+ " rest.userId=staff.userID and staff.isDeleted=0 and rest.isDeleted=0 and rest.year="+year+" and rest.month="+month+"\n";
		if(StringUtils.isNotBlank(staffName)){
			hql += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffName)+"%'";
		}
		List<UserMonthlyRestEntity> monthlyRests = (List<UserMonthlyRestEntity>) baseDao.hqlPagedFind(hql, page, limit);
		for(UserMonthlyRestEntity monthlyRest: monthlyRests){
			StaffEntity staff = staffService.getStaffByUserId(monthlyRest.getUserId());
			monthlyRest.setStaffName(staff.getStaffName());
		}
		String hqlCount = "select count(rest.id) from UserMonthlyRestEntity rest, StaffEntity staff where"
				+ " rest.userId=staff.userID and staff.isDeleted=0 and rest.isDeleted=0 and rest.year="+year+" and rest.month="+month+"\n";
		if(StringUtils.isNotBlank(staffName)){
			hqlCount += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffName)+"%'";
		}
		int totalCount = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hqlCount)));
		return new ListResult<>(monthlyRests, totalCount);
	}


	@Override
	public void modifyUserMonthlyRest(String id, String restDays) {
		String hql = "from UserMonthlyRestEntity where id="+id;
		UserMonthlyRestEntity userMonthlyRest = (UserMonthlyRestEntity) baseDao.hqlfindUniqueResult(hql);
		String year = userMonthlyRest.getYear();
		String month = userMonthlyRest.getMonth();
		int monthlyDays = DateUtil.getMonthLastDay(Integer.parseInt(year), Integer.parseInt(month));
		int monthlyWorkDays = monthlyDays - Integer.parseInt(restDays);
		userMonthlyRest.setRestDays(restDays);
		userMonthlyRest.setWorkDays(String.valueOf(monthlyWorkDays));
		baseDao.hqlUpdate(userMonthlyRest);
	}
}
