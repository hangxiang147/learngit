package com.zhizaolian.staff.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.dao.VacationDao;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.VacationEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.enums.VacationTypeEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.transformer.VacationVOTransFormer;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.VacationDetailVo;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.VacationVO;

public class VacationServiceImpl implements VacationService{
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private VacationDao vacationDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private CompanyDao companyDao;
	@Override
	public void startVacation(VacationVO vacation, File[] attachment, String[] attachmentFileName) throws Exception {
		//3代表年休假
		if(vacation.getVacationType() == 3){
			//检查是否可以休年假
			String checkResult = checkMeetConditions(vacation.getRequestUserID(), vacation.getBeginDate(), vacation.getEndDate());
			if(!"true".equalsIgnoreCase(checkResult)){
				throw new RuntimeException(checkResult);
			}
		}
		vacation.setBusinessType(BusinessTypeEnum.VACATION.getName());
		if(Constants.DEP.equals(vacation.getType())){
			CompanyEntity company = companyDao.getCompanyByCompanyID(vacation.getCompanyID());
			DepartmentEntity deparment = departmentDao.getDepartmentByDepartmentID(vacation.getDepartmentID());
			vacation.setTitle(company.getCompanyName()+"-"+deparment.getDepartmentName()+"的"+BusinessTypeEnum.VACATION.getName());
			vacation.setRequestUserName(company.getCompanyName()+"-"+deparment.getDepartmentName());
		}else{
			vacation.setTitle(vacation.getRequestUserName()+"的"+BusinessTypeEnum.VACATION.getName());
		}
		//考虑到手机客户端的请假没有考虑午休时间，这边再次计算请假时间
		String[] vacationTextAndHours = getVacationTextAndHoursForHR(vacation.getUserID(), vacation.getBeginDate(), vacation.getEndDate());
		vacation.setShowHours(Double.parseDouble(vacationTextAndHours[1]));
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", vacation);
		String requestUserId = "";
		if(Constants.DEP.equals(vacation.getType())){
			//同一个部门的加班人员
			String[] userIds = vacation.getRequestUserID().split(",");
			//任取一个
			requestUserId = userIds[0];
		}else{
			requestUserId = vacation.getRequestUserID();
		}
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(requestUserId);
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(requestUserId);
		} 
		if (StringUtils.isBlank(supervisor) || requestUserId.equals(supervisor)) {
			supervisor=staffService.querySupervisor(requestUserId);
		}
		String manager = staffService.queryManager(requestUserId);
		List<String> hrGroupList = staffService.queryHRGroupList(requestUserId);
		if (StringUtils.isBlank(manager) || CollectionUtils.isEmpty(hrGroupList)) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		if (!staffService.hasGroupMember(hrGroupList)) {
			throw new RuntimeException("未找到该申请的审批人！");
		}
		vars.put("hrGroup", hrGroupList);
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Vacation");
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), vacation.getUserID());
		if(null != attachment){
			for(File attach: attachment){
				//设置任务附件
				InputStream is = new FileInputStream(attach);
				taskService.createAttachment("picture", task.getId(), processInstance.getId(), "vacation picture", "WeChat screenshots", is);
			}
		}
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录请假数据
		saveVacation(vacation, processInstance.getId(), attachment, attachmentFileName);
	}
	@Override
	public String checkMeetConditions(String requestUserID, String beginDate, String endDate) throws Exception {
		String[] vacationUserIds = requestUserID.split(",");
		for(String vacationUserId: vacationUserIds){
			StaffEntity staff = staffService.getStaffByUserId(vacationUserId);
			String staffName = staff.getStaffName();
			List<Group> groups = identityService.createGroupQuery().groupMember(vacationUserId).list();
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyID = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			double dailyHour = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID, departmentId, beginDate);
			//判断请假是否跨年
			if(DateUtil.getYear(DateUtil.getFullDate(beginDate)) == DateUtil.getYear(DateUtil.getFullDate(beginDate))){
				//计算年假天数
				String annualVacationDayStr = getAnnualVacationDays(beginDate, vacationUserId);
				int annualVacationDays = 0;
				if(Pattern.matches("^[0-9]*$", annualVacationDayStr)){
					annualVacationDays = Integer.parseInt(annualVacationDayStr);
				}else{
					return staffName+annualVacationDayStr;
				}
				Double[] usedAnnualVacationDaysAndHours = getUsedAnnualVacationDaysAndHours(beginDate, vacationUserId);
				//已休年休假天数
				double days = usedAnnualVacationDaysAndHours[0];
				//已休年休假小时数
				double hours = usedAnnualVacationDaysAndHours[1];
				//剩余的年假小时数
				double remainAnnualVacationHours = (annualVacationDays-days)*dailyHour - hours;
				String[] vacationTextAndHours = getVacationTextAndHoursForHR(vacationUserId, beginDate, endDate);
				double vacationHours = Double.parseDouble(vacationTextAndHours[1]);
				if(remainAnnualVacationHours<vacationHours){
					int day = (int)Math.floor(remainAnnualVacationHours/dailyHour);
					double _hour = (remainAnnualVacationHours - day*dailyHour);
					if(day>0){
						if(_hour>0){
							return staffName+"年假只剩余"+day+"天"+_hour+"小时";
						}else{
							return staffName+"年假只剩余"+day+"天";
						}
					}else if(_hour>=0){
						return staffName+"年假只剩余"+remainAnnualVacationHours+"小时";
					}
				}
			}else{
				//今年的年假天数
				String thisYearAnnualVacationDayStr = getAnnualVacationDays(beginDate, vacationUserId);
				int thisYearAnnualVacationDays = 0;
				if(Pattern.matches("^[0-9]*$", thisYearAnnualVacationDayStr)){
					thisYearAnnualVacationDays = Integer.parseInt(thisYearAnnualVacationDayStr);
				}else{
					return thisYearAnnualVacationDayStr;
				}
				Double[] usedAnnualVacationDaysAndHours = getUsedAnnualVacationDaysAndHours(beginDate, vacationUserId);
				//已休年休假天数
				double days = usedAnnualVacationDaysAndHours[0];
				//已休年休假小时数
				double hours = usedAnnualVacationDaysAndHours[1];
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.getFullDate(beginDate));
				Date _endDate = DateUtil.getFullDate(cal.get(Calendar.YEAR)+"-12-31 23:59:59");
				//获取年前需要请年假的小时数
				double beforeYearVacationHours = calcWorkHours(beginDate, DateUtil.getFullDate(beginDate), _endDate, Integer.parseInt(companyID), vacationUserId);
				//今年剩余的年假小时数
				double remainThisYearAnnualVacationHours = (thisYearAnnualVacationDays-days)*dailyHour - hours;
				if(remainThisYearAnnualVacationHours<beforeYearVacationHours){
					int day = (int)Math.floor(remainThisYearAnnualVacationHours/dailyHour);
					double _hour = (remainThisYearAnnualVacationHours - day*dailyHour);
					if(day>0){
						if(_hour>0){
							return staffName+"今年年假只剩余"+day+"天"+_hour+"小时";
						}else{
							return staffName+"今年年假只剩余"+day+"天";
						}
					}else if(_hour>=0){
						return staffName+"今年年假只剩余"+remainThisYearAnnualVacationHours+"小时";
					}
				}
				//明年的年假天数
				String nextYearAnnualVacationDayStr = getAnnualVacationDays(endDate, vacationUserId);
				int nextYearAnnualVacationDays = 0;
				if(Pattern.matches("^[0-9]*$", nextYearAnnualVacationDayStr)){
					nextYearAnnualVacationDays = Integer.parseInt(nextYearAnnualVacationDayStr);
				}else{
					return staffName+nextYearAnnualVacationDayStr;
				}
				usedAnnualVacationDaysAndHours = getUsedAnnualVacationDaysAndHours(beginDate, vacationUserId);
				//已休年休假天数
				days = usedAnnualVacationDaysAndHours[0];
				//已休年休假小时数
				hours = usedAnnualVacationDaysAndHours[1];
				cal.setTime(DateUtil.getFullDate(endDate));
				Date _beginDate = DateUtil.getFullDate(cal.get(Calendar.YEAR)+"-01-01 00:00:00");
				//获取年后需要请年假的小时数
				double afterYearVacationHours = calcWorkHours(beginDate, _beginDate, DateUtil.getFullDate(endDate), Integer.parseInt(companyID), vacationUserId);
				dailyHour = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID, departmentId, endDate);
				//明年剩余的年假小时数
				double remainNextYearAnnualVacationHours = (nextYearAnnualVacationDays-days)*dailyHour - hours;
				if(remainNextYearAnnualVacationHours<afterYearVacationHours){
					int day = (int)Math.floor(remainNextYearAnnualVacationHours/dailyHour);
					double _hour = (remainNextYearAnnualVacationHours - day*dailyHour);
					if(day>0){
						if(_hour>0){
							return staffName+"明年年假只剩余"+day+"天"+_hour+"小时";
						}else{
							return staffName+"明年年假只剩余"+day+"天";
						}
					}else if(_hour>=0){
						return staffName+"明年年假只剩余"+remainNextYearAnnualVacationHours+"小时";
					}
				}
			}
		}
		return "true";
	}
	private Double[] getUsedAnnualVacationDaysAndHours(String beginDate, String requestUserID) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(requestUserID).list();
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		String companyID = group.getType().split("_")[0];
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getFullDate(beginDate));
		//考虑到请假的开始时间和结束时间跨年
		String firstDay = cal.get(Calendar.YEAR) + "-01-01 00:00:00";;
		String lastDay = cal.get(Calendar.YEAR) + "-12-31 23:59:59";;
		String sql = "select beginDate, endDate, hours, dailyHours FROM\n" +
				"	OA_Vacation\n" +
				"WHERE\n" +
				//"	requestUserID = '"+requestUserID+"'\n" +
				"	locate('"+requestUserID+"',requestUserID)>0\n" +
				"AND isDeleted = 0\n" +
				"AND (\n" +
				"	processInstanceID is NULL || (IFNULL(processStatus, 0)!=31 && IFNULL(processStatus, 0)!=2)\n" +
				")\n" +
				"AND beginDate < '"+lastDay+"'\n" +
				"AND endDate > '"+firstDay+"'\n" +
				"AND vacationType=3 order by beginDate";
		List<Object> vacationObjs = baseDao.findBySql(sql);
		//已休年休假天数
		double days = 0;
		//已休年休假小时数
		double hours = 0;
		double dailyHours = 0;
		for(Object vacationObj: vacationObjs){
			Object[] vacation = (Object[])vacationObj;
			Date vacationbeginDate = ((Date)vacation[0]).getTime()<=DateUtil.getFullDate(firstDay).getTime() ? DateUtil.getFullDate(firstDay) : (Date)vacation[0];
			//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(beginDate));
			dailyHours = (double)vacation[3];
			double vacationHours = (double)vacation[2];
			Date vacationEndDate = ((Date)vacation[1]).getTime()>=DateUtil.getFullDate(lastDay).getTime() ? DateUtil.getFullDate(lastDay) : (Date)vacation[1];
			if (!((Date)vacation[0]).equals(vacationbeginDate) || !vacationEndDate.equals((Date)vacation[1])) {
				vacationHours = calcWorkHours(DateUtil.formateFullDate((Date)vacation[0]), vacationbeginDate, vacationEndDate, Integer.parseInt(companyID), requestUserID);
			}
			int day = (int)Math.floor(vacationHours/dailyHours);
			days += day;
			hours += (vacationHours - day*dailyHours);
		}
		int _day = (int)Math.floor(hours/dailyHours);
		days += _day;
		hours = hours-dailyHours*_day;
		BigDecimal b = new BigDecimal(hours);  
		hours = b.setScale(1, BigDecimal.ROUND_UP).doubleValue();  
		return new Double[]{days, hours};
	}
	@Autowired
	private StaffDao staffDao;
	//判断入职年限
	private boolean checkEntryYear(String requestUserID, String beginDate, int index) {
		StaffEntity staff = staffDao.getStaffByUserID(requestUserID);
		Date entryDate = staff.getEntryDate();
		Date _beginDate = DateUtil.getFullDate(beginDate);
		Calendar calEntryDate = Calendar.getInstance();
		calEntryDate.setTime(entryDate);
		Calendar calBeginDate = Calendar.getInstance();
		calBeginDate.setTime(_beginDate);
		//入职所在年份
		int entryYear = calEntryDate.get(Calendar.YEAR);
		//请假所在年份
		int vacationYear = calBeginDate.get(Calendar.YEAR);
		if((vacationYear-entryYear)>index){
			return true;
		}
		return false;
	}
	private String getAnnualVacationDays(String beginDate, String requestUserID) {
		int days = staffService.getComeDays(requestUserID, beginDate);
		//满一年
		if(days>=365){
			//满一年，不满十年，考虑到闰年（大概计算下）
			if(days<(366*2+365*8)){
				//规定满一年的次年才可以休年假，所以这里依据请假时间，来判断下入职是不是前年（大于1）
/*				if(checkEntryYear(requestUserID, beginDate, 1)){
					return "5";
				}else{
					return "入职满一年的次年，才可以休年假";
				}*/
				return "5";
				//满十年，不满二十年	
			}else if(days<(366*5+365*15)){
				//同理，满十年的次年才有10天的年假，所以这里依据请假时间，来判断下入职是不是大于10年
/*				if(checkEntryYear(requestUserID, beginDate, 10)){
					return "10";
				}else{
					return "满十年的次年才有10天的年假";
				}*/
				return "10";
				//20年以上	
			}else{
/*				//同理，满二十年的次年才有20天的年假，所以这里依据请假时间，来判断下入职是不是大于20年
				if(checkEntryYear(requestUserID, beginDate, 20)){
					return "20";
				}else{
					return "满二十年的次年才有20天的年假";
				}*/
				return "20";
			}
			//规定满一年的次年才可以使用年假
		}else{
			return "入职未满一年，没有年假";
		}
	}
	@Override
	public ListResult<VacationTaskVO> findVacationTasksByGroups(List<Group> groups, int page, int limit) {
		String arrayString = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'"+input.getId()+"'";
			}
		}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.VACATION_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<VacationTaskVO> taskVOs = createTaskVOList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and task.TASK_DEF_KEY_ = '"+TaskDefKeyEnum.VACATION_HR_AUDIT.getName()+"' "
				+ "and identityLink.GROUP_ID_ in ("+arrayString.substring(1, arrayString.length()-1)+")";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<VacationTaskVO>(taskVOs, count);
	}

	@Override
	public ListResult<VacationVO> findVacationListByUserID(String userID, int page, int limit) {
		// 查询OA_Vacation表的数据
		List<VacationEntity> vacationEntities = vacationDao.findVacationsByUserID(userID, page, limit);
		List<VacationVO> result = new ArrayList<VacationVO>();
		double dailyHours = 9;
		for (VacationEntity vacation : vacationEntities) {
			try {
				String requestUserId = "";
				if(Constants.DEP.equals(vacation.getType())){
					//同一个部门的请假人员
					String[] userIds = vacation.getRequestUserID().split(",");
					//任取一个
					requestUserId = userIds[0];
				}else{
					requestUserId = vacation.getRequestUserID();
				}
				List<Group> groups = identityService.createGroupQuery().groupMember(requestUserId).list();
				if (groups.size() <= 0) {
					//删除已离职员工的请假记录
					deleteVacationByVacationID(vacation.getVacationID());
					continue;
				}
				//String companyID = groups.get(0).getType().split("_")[0];
				//String departmentId = groups.get(0).getType().split("_")[1];
				//dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(Integer.parseInt(companyID)));
				//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID, departmentId, DateUtil.formateDate(vacation.getBeginDate()));
				dailyHours = vacation.getDailyHours();
			} catch (Exception e) {
				throw new RuntimeException("获取员工所在分部作息时间信息失败！");
			}
			VacationVO vacationVO = new VacationVO();
			vacationVO.setProcessInstanceID(vacation.getProcessInstanceID());
			vacationVO.setBeginDate(vacation.getBeginDate()==null?"":DateUtil.formateFullDate(vacation.getBeginDate()));
			vacationVO.setEndDate(vacation.getEndDate()==null?"":DateUtil.formateFullDate(vacation.getEndDate()));
			double hours = vacation.getHours();
			vacationVO.setDays((int)Math.floor(hours/dailyHours));
			vacationVO.setShowHours(hours - dailyHours*vacationVO.getDays());
			vacationVO.setDailyHours(dailyHours);
			vacationVO.setVacationType(vacation.getVacationType());
			vacationVO.setReason(vacation.getReason());
			vacationVO.setType(vacation.getType());
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(vacation.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					VacationVO arg = (VacationVO) variable.getValue();
					vacationVO.setRequestDate(arg.getRequestDate());
					vacationVO.setRequestUserName(arg.getRequestUserName());
					vacationVO.setTitle(arg.getTitle());
				}
			}
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(vacation.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				vacationVO.setStatus("处理中");
				vacationVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				vacationVO.setStatus(TaskResultEnum.valueOf(vacation.getProcessStatus()).getName());
			}
			/*			try {
				if (!StringUtils.isBlank(vacation.getAttachmentImage())) {
					String[] attachmentImages = vacation.getAttachmentImage().split("#&&#");
					List<byte[]> picList = new ArrayList<byte[]>();
					for(String attachment: attachmentImages){
						if(StringUtils.isNotBlank(attachment)){
							InputStream input = new FileInputStream(Constants.VACATION_FILE_DIRECTORY+attachment);
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							byte[] buffer = new byte[4096];
							int n = 0;
							while (-1 != (n = input.read(buffer))) {
								output.write(buffer, 0, n);
							}
							picList.add( output.toByteArray());
							input.close();
							output.close();
						}
					}
					vacationVO.setPicLst(picList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			if(Constants.DEP.equals(vacation.getType())){
				List<String> staffVos = new ArrayList<>();
				String[] vacationUserIds = vacation.getRequestUserID().split(",");
				for(String vacationUserId: vacationUserIds){
					StaffVO staffVo = staffService.getStaffByUserID(vacationUserId);
					staffVos.add(staffVo.getLastName());
				}
				vacationVO.setVacationUsers(staffVos);
			}
			result.add(vacationVO);
		}

		int count = vacationDao.countVacationsByUserID(userID);
		return new ListResult<VacationVO>(result, count);
	}

	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("审批结果不合法！");
		}

		vacationDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}

	@Override
	public List<VacationVO> findVacationsByCompanyAndDate(Integer companyID, Date date) throws Exception {
		/*		String beginDate = DateUtil.formateDate(date) + positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(companyID));
		String endDate = DateUtil.formateDate(date) + positionService.getEndTimeByCompanyID(CompanyIDEnum.valueOf(companyID));*/
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(date));
		String beginDate = DateUtil.formateDate(date) + " " + workTimeArray[0] + ":00";;
		String endDate = DateUtil.formateDate(date) + " " + workTimeArray[3] + ":00";;
		String sql = "select staff.StaffName, vacation.BeginDate, vacation.EndDate, staff.UserID from OA_Vacation vacation LEFT JOIN ACT_ID_MEMBERSHIP membership ON locate(membership.USER_ID_,vacation.RequestUserID)>0 "
				+ "LEFT JOIN ACT_ID_GROUP gp ON gp.ID_ = membership.GROUP_ID_ "
				+ "LEFT JOIN OA_Staff staff on staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_HI_PROCINST procinst ON procinst.PROC_INST_ID_ = vacation.ProcessInstanceID where "
				+ "vacation.IsDeleted = 0 and staff.IsDeleted = 0 and staff.Status != 4 "
				+ "and gp.TYPE_ like '"+companyID+"_%' and vacation.BeginDate < '"+endDate+"' and vacation.EndDate > '"+beginDate+"' "
				+ "and (vacation.ProcessInstanceID is null or (vacation.ProcessStatus = 1 and procinst.END_ACT_ID_ is not null)) "
				+ "group by UserID, BeginDate";
		List<Object> result = baseDao.findBySql(sql);

		List<VacationVO> vacationVOs = new ArrayList<VacationVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			VacationVO vacationVO = new VacationVO();
			vacationVO.setRequestUserName((String) objs[0]);
			vacationVO.setBeginDate(DateUtil.formateFullDate((Date) objs[1]));
			vacationVO.setEndDate(DateUtil.formateFullDate((Date)objs[2]));
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID((String) objs[3]);
			vacationVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
				}
			}));
			vacationVOs.add(vacationVO);
		}
		return vacationVOs;
	}
	/**
	 * 
	 * @param _beginDate 真正的请假开始时间
	 * @param beginDate 请假开始时间（查询的开始时间或请假时间）
	 * @param endDate
	 * @param companyID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private double calcWorkHours(String _beginDate, Date beginDate, Date endDate, Integer companyID, String userId) throws Exception {
		//CompanyIDEnum company = CompanyIDEnum.valueOf(companyID);
		//String beginTime = positionService.getBeginTimeByCompanyID(company);
		//String endTime = positionService.getEndTimeByCompanyID(company);
		//String workTimes = company.getTimeLimitByDate(null);
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
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, _beginDate);
		//String[] workTimeArray = workTimes.split(" ");
		String beginTime = " " + workTimeArray[0] + ":00";
		String endTime = " " + workTimeArray[3] + ":00";
		//上午下班时间
		String amEndTime = " " + workTimeArray[1] + ":00";
		//下午上班时间
		String pmBeginTime= " " + workTimeArray[2] + ":00";
		//double daily = positionService.getDailyHoursByCompanyID(company);
		//请假开始当天的作息时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+beginTime);
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+amEndTime);
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+pmBeginTime);
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+endTime);
		//请假结束当天的作息时间
		Date _amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(endDate)+beginTime);
		Date _amEndDate = DateUtil.getFullDate(DateUtil.formateDate(endDate)+amEndTime);
		Date _pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(endDate)+pmBeginTime);
		Date _pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(endDate)+endTime);

		if(DateUtil.before(beginDate, amBeginDate)){
			beginDate = amBeginDate;
		}
		//请假的开始时间在请假开始当天的下班后
		if(DateUtil.after(beginDate, pmEndDate)){
			Calendar _calendar = Calendar.getInstance();
			_calendar.setTime(amBeginDate);
			_calendar.add(Calendar.DATE, 1);
			beginDate = _calendar.getTime();
		}
		if(DateUtil.after(endDate, _pmEndDate)){
			endDate = _pmEndDate;
		}
		//请假的结束时间在请假结束当天的上班前
		if(DateUtil.before(endDate, _amBeginDate)){
			Calendar _calendar = Calendar.getInstance();
			_calendar.setTime(_pmEndDate);
			_calendar.add(Calendar.DATE, -1);
			endDate = _calendar.getTime();
		}
		//午休时间
		double breakTime = 0;
		/**1、请假的开始时间和结束时间在同一天
		 * 	1、请假开始时间在上午区间
		 * 	  1.1、结束时间在上午区间//do nothing
		 *    1.2、结束时间在午休区间
		 *    1.3、结束时间在下午区间
		 *  2、请假开始时间在午休区间
		 *    1.1、结束时间在下午区间
		 *  3、请假开始时间在下午区间
		 *    //do nothing
		 * 2、请假的开始时间和结束时间不在同一天
		 * 	1、开始请假时间在上午区间
		 * 	2、开始请假时间在午休区间
		 * 	3、开始请假时间在下午区间
		 * 
		 * 	1、结束请假时间在上午区间
		 * 	2、结束请假时间在午休区间
		 * 	3、结束请假时间在下午区间
		 */
		//请假的开始时间和结束时间在同一天
		if(DateUtil.getDayStr(beginDate).equals(DateUtil.getDayStr(endDate))){
			//请假开始时间在上午区间
			if(DateUtil.before(beginDate, amEndDate)){
				//结束时间在午休区间
				if(DateUtil.after(endDate, amEndDate) && DateUtil.before(endDate, pmBeginDate)){
					breakTime += (endDate.getTime()-amEndDate.getTime());
					//结束时间在下午区间
				}else if(DateUtil.after(endDate, pmBeginDate)){
					breakTime += (pmBeginDate.getTime()-amEndDate.getTime());
				}
				//请假开始时间在午休区间
			}else if(DateUtil.after(beginDate, amEndDate) && DateUtil.before(beginDate, pmBeginDate)){
				//结束时间在下午区间
				if(DateUtil.after(endDate, pmBeginDate)){
					breakTime += (pmBeginDate.getTime()-beginDate.getTime());
				}
				//请假开始时间在下午区间
			}else{
				//do nothing
			}
			//请假的开始时间和结束时间不在同一天
		}else{
			if(DateUtil.before(beginDate, amEndDate)){
				breakTime += (pmBeginDate.getTime()-amEndDate.getTime());
			}else if(DateUtil.after(beginDate, amEndDate) && DateUtil.before(beginDate, pmBeginDate)){
				breakTime += (pmBeginDate.getTime()-beginDate.getTime());
			}else{
				//do nothing
			}

			if(DateUtil.before(endDate, _amEndDate)){
				//do nothing
			}else if(DateUtil.after(endDate, _amEndDate) && DateUtil.before(endDate, _pmBeginDate)){
				breakTime += (endDate.getTime()-_amEndDate.getTime());
			}else{
				breakTime += (_pmBeginDate.getTime()-_amEndDate.getTime());
			}
		}
		//double daily = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId);
		double breakDiff = DateUtil.getFullDate("2000-01-02"+beginTime).getTime()
				- DateUtil.getFullDate("2000-01-01"+endTime).getTime(); 
		/*double dailyTime = DateUtil.getFullDate("2000-01-01"+endTime).getTime()
				- DateUtil.getFullDate("2000-01-01"+beginTime).getTime(); */
		double diff = endDate.getTime() - beginDate.getTime();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		int endDay = calendar.get(Calendar.DATE);
		calendar.setTime(beginDate);
		int beginDay = calendar.get(Calendar.DATE);
		if((endDay-beginDay)>1){
			breakTime += (pmBeginDate.getTime()-amEndDate.getTime())*(endDay-beginDay-1);
		}
		double hours = Math.ceil((diff - breakDiff * (endDay-beginDay) - breakTime)/(60*30*1000))/2;
		//double dailyHours = Math.ceil(dailyTime/(60*30*1000))/2;
		//int day = (int)Math.floor(hours/dailyHours);
		//double hour = hours - dailyHours * day;
		//return daily*day + hour;
		return hours;
	}

	@Override
	public ListResult<VacationVO> findStatisticsPageListByCompanyAndMonth(Integer companyID,String userName, Date date, int page, int limit) throws Exception {		
		/*		String firstDay = DateUtil.getFirstDayofMonth(date) + positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(companyID));
		String lastDay = DateUtil.getLastDayofMonth(date) + positionService.getEndTimeByCompanyID(CompanyIDEnum.valueOf(companyID));*/
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(date));
		//String[] workTimeArray = workTimes.split(" ");
		String firstDay = DateUtil.getFirstDayofMonth(date) + " " + workTimeArray[0] + ":00";;
		String lastDay = DateUtil.getLastDayofMonth(date) + " " + workTimeArray[3] + ":00";;
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(companyID));
		String sql = "SELECT s.UserID, s.StaffName, GROUP_CONCAT(s.Hours), GROUP_CONCAT(s.BeginDate), GROUP_CONCAT(s.EndDate),GROUP_CONCAT(s.VacationID), GROUP_CONCAT(s.dailyHours) from("
				+ "SELECT staff.UserID, staff.StaffName,vacation.VacationID, vacation.Hours, vacation.BeginDate, vacation.EndDate, vacation.dailyHours FROM OA_Vacation vacation "
				//+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON vacation.RequestUserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON locate(membership.USER_ID_,vacation.RequestUserID)>0 "
				+ "LEFT JOIN ACT_ID_GROUP gp ON gp.ID_ = membership.GROUP_ID_ "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_HI_PROCINST procinst ON procinst.PROC_INST_ID_ = vacation.ProcessInstanceID "
				+ "WHERE vacation.IsDeleted = 0 AND staff.IsDeleted = 0 AND staff.Status != 4 "
				+ "AND gp.TYPE_ LIKE '"+companyID+"_%' AND vacation.BeginDate < '"+lastDay+"' ";
		if(StringUtils.isNotBlank(userName)){
			sql+=" AND staff.StaffName like '%"+userName+"%' ";
		}
		sql+= ( "AND vacation.EndDate > '"+firstDay+"' AND (vacation.ProcessInstanceID is null or (vacation.ProcessStatus = 1 "
				+ "AND procinst.END_ACT_ID_ IS NOT NULL)) group by UserID, BeginDate) s GROUP BY s.StaffName");
		List<Object> result = baseDao.findPageList(sql, page, limit);
		double dailyHours = 0;
		List<VacationVO> vacationVOs = new ArrayList<VacationVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			VacationVO vacationVO = new VacationVO();
			vacationVO.setRequestUserID((String) objs[0]);
			vacationVO.setRequestUserName((String) objs[1]);
			vacationVO.setShowHours(0.0);
			String[] hours = StringUtils.split((String) objs[2], ",");
			String[] beginDates = StringUtils.split((String) objs[3], ",");
			String[] endDates = StringUtils.split((String) objs[4], ",");
			String[] _dailyHours = StringUtils.split((String) objs[6], ",");
			List<String> dateDetail = new ArrayList<String>();
			int days = 0;
			//剩余累加的小时数
			double showHours = 0;
			for (int i = 0; i < hours.length; ++i) {
				double hour = Double.valueOf(hours[i]);
				Date beginDate = DateUtil.getFullDate(beginDates[i]).getTime()<=DateUtil.getFullDate(firstDay).getTime() ? DateUtil.getFullDate(firstDay) : DateUtil.getFullDate(beginDates[i]);
				//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(beginDate));
				dailyHours = Double.parseDouble(_dailyHours[i]);
				Date endDate = DateUtil.getFullDate(endDates[i]).getTime()>=DateUtil.getFullDate(lastDay).getTime() ? DateUtil.getFullDate(lastDay) : DateUtil.getFullDate(endDates[i]);
				if (!beginDate.equals(DateUtil.getFullDate(beginDates[i])) || !endDate.equals(DateUtil.getFullDate(endDates[i]))) {
					hour = calcWorkHours(beginDates[i], beginDate, endDate, companyID, (String) objs[0]);
				}
				int day = (int)Math.floor(hour/dailyHours);
				days += day;
				showHours += (hour - day*dailyHours);
				//vacationVO.setShowHours(vacationVO.getShowHours()+hour);
				dateDetail.add(DateUtil.formateFullDate(beginDate)+"至"+DateUtil.formateFullDate(endDate));
			}
			vacationVO.setShowVacationIds( objs[5]==null?null:(String)objs[5]);
			vacationVO.setDateDetail(dateDetail.toArray(new String[dateDetail.size()]));
			int _day = (int)Math.floor(showHours/dailyHours);
			days += _day;
			vacationVO.setDays(days);
			showHours = showHours-dailyHours*_day;
			BigDecimal b = new BigDecimal(showHours);  
			showHours = b.setScale(1, BigDecimal.ROUND_UP).doubleValue();  
			vacationVO.setShowHours(showHours);
			vacationVOs.add(vacationVO);
		}

		sql = "SELECT count(distinct staff.UserID) FROM OA_Vacation vacation "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON locate(membership.USER_ID_,vacation.RequestUserID)>0 "
				+ "LEFT JOIN ACT_ID_GROUP gp ON gp.ID_ = membership.GROUP_ID_ "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_HI_PROCINST procinst ON procinst.PROC_INST_ID_ = vacation.ProcessInstanceID "
				+ "WHERE vacation.IsDeleted = 0 AND staff.IsDeleted = 0 AND staff.Status != 4 "
				+ "AND gp.TYPE_ LIKE '"+companyID+"_%' AND vacation.BeginDate < '"+lastDay+"' ";
		if(StringUtils.isNotBlank(userName)){
			sql+=" AND staff.StaffName like '%"+userName+"%' ";
		}
		sql+= ( "AND vacation.EndDate > '"+firstDay+"' AND (vacation.ProcessInstanceID is null or (vacation.ProcessStatus = 1 "
				+ "AND procinst.END_ACT_ID_ IS NOT NULL)) ");
		Object object = baseDao.getUniqueResult(sql);
		int count = object==null ? 0 : ((BigInteger)object).intValue();
		return new ListResult<VacationVO>(vacationVOs, count);
	}

	@Override
	public long calcVacationTime(Date begin, Date end, String userID) {
		long totalTime = end.getTime() - begin.getTime();
		List<VacationEntity> vacations = vacationDao.findVacationsByDate(begin, userID);
		long time1 = 0L;
		for (VacationEntity vacation : vacations) {
			//人事补录的请假记录，或者审批通过的请假记录
			if (StringUtils.isBlank(vacation.getProcessInstanceID()) || 
					runtimeService.createProcessInstanceQuery().processInstanceId(vacation.getProcessInstanceID()).singleResult() == null) {
				long diff = vacation.getEndDate().getTime() - begin.getTime();
				time1 = diff>time1 ? diff : time1;
			}
		}
		if (time1 >= totalTime) {
			return totalTime;
		}

		long time2 = 0L;
		vacations = vacationDao.findVacationsByDate(end, userID);
		for (VacationEntity vacation : vacations) {
			//人事补录的请假记录，或者审批通过的请假记录
			if (StringUtils.isBlank(vacation.getProcessInstanceID()) || 
					runtimeService.createProcessInstanceQuery().processInstanceId(vacation.getProcessInstanceID()).singleResult() == null) {
				long diff = end.getTime() - vacation.getBeginDate().getTime();
				time2 = diff>time2 ? diff : time2;
			}
		}
		if (time1+time2 >= totalTime) {
			return totalTime;
		}

		long time3 = 0L;
		vacations = vacationDao.findVacationsByDates(begin, end, userID);
		for (VacationEntity vacation : vacations) {
			if (StringUtils.isBlank(vacation.getProcessInstanceID()) || 
					runtimeService.createProcessInstanceQuery().processInstanceId(vacation.getProcessInstanceID()).singleResult() == null) {
				time3 += (vacation.getEndDate().getTime()-vacation.getBeginDate().getTime());
			}
		}
		return time1+time2+time3>totalTime ? totalTime : time1+time2+time3;
	}

	/*private List<TaskVO> createTaskVOList(List<Object> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			BaseVO arg = (BaseVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTitle(arg.getTitle());
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}*/


	private void saveVacation(VacationVO vacation, String processInstanceID, File[] attachment, String[] attachmentFileName) throws Exception {
		Date now = new Date();
		if (attachment != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				byte[] buffer = new byte[10*1024*1024];
				int length = 0;
				int index = 0;
				String fileNames = "";
				for(File attach: attachment){
					in = new FileInputStream(attach);
					File f=new File(Constants.VACATION_FILE_DIRECTORY);
					if(!f.exists()){
						f.mkdirs();
					}
					String fileName = now.getTime()+"_"+attachmentFileName[index];
					out=new FileOutputStream(new File(Constants.VACATION_FILE_DIRECTORY,fileName));
					while((length=in.read(buffer, 0, buffer.length))!=-1){
						out.write(buffer, 0, length);
					}
					if(index==0){
						fileNames += fileName;
					}else{
						fileNames += "#&&#"+fileName;
					}
					index++;
				}

				vacation.setAttachmentImage(fileNames);
			} catch (Exception e) {
				throw new RuntimeException("保存附件至服务器失败！");
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
		VacationEntity vacationEntity = VacationEntity.builder()
				.userID(vacation.getUserID())
				.requestUserID(vacation.getRequestUserID())
				.hours(vacation.getShowHours())
				.beginDate(DateUtil.getFullDate(vacation.getBeginDate()))
				.endDate(DateUtil.getFullDate(vacation.getEndDate()))
				.agentID(vacation.getAgentID())
				.vacationType(vacation.getVacationType())
				.reason(vacation.getReason())
				.processInstanceID(processInstanceID)
				.attachmentImage(vacation.getAttachmentImage())
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
				.dailyHours(vacation.getDailyHours())
				.type(vacation.getType())
				.departmentID(vacation.getDepartmentID())
				.companyID(vacation.getCompanyID())
				.addTime(now)
				.updateTime(now)
				.build();
		vacationDao.save(vacationEntity);
	}



	@Override
	public List<VacationEntity> findVacationsByRequestUserID(String requestUserID) {


		List<VacationEntity> vacationEntities=vacationDao.findVacationsByRequestUserID(requestUserID);

		return vacationEntities;
	}

	//如果请假人id 、开始时间、结束时间、请假类型、工作代理人的id相同则视为重复申请。

	@Override
	public boolean isDuplicateClaim(VacationVO vacationVO) {

		List<VacationEntity> vacationEntities=findVacationsByRequestUserID(vacationVO.getRequestUserID());
		for (VacationEntity vacationEntity : vacationEntities) {
			if(vacationVO.getBeginDate().equals(DateUtil.formateFullDate(vacationEntity.getBeginDate()))&&vacationVO.getEndDate().equals(DateUtil.formateFullDate(vacationEntity.getEndDate()))&&vacationVO.getVacationType().equals(vacationEntity.getVacationType())&&vacationVO.getAgentID().equals(vacationEntity.getAgentID())){
				return true;
			}
		}

		return false;
	}



	@Override
	public void updateVacation(VacationVO vacationVO) throws Exception {
		String[] vacationTextAndHours = getVacationTextAndHoursForHR(vacationVO.getUserID(), vacationVO.getBeginDate(), vacationVO.getEndDate());
		vacationVO.setShowHours(Double.parseDouble(vacationTextAndHours[1]));
		VacationEntity vacationEntity = vacationDao.findVacationsByvacationID(vacationVO.getVacationID());
		vacationEntity.setHours(vacationVO.getShowHours());
		vacationEntity.setBeginDate(DateUtil.getFullDate(vacationVO.getBeginDate()));
		vacationEntity.setEndDate(DateUtil.getFullDate(vacationVO.getEndDate()));
		vacationEntity.setAgentID(vacationVO.getAgentID());
		vacationEntity.setVacationType(vacationVO.getVacationType());
		vacationEntity.setReason(vacationVO.getReason());
		if(vacationVO.getAttachmentImage()!=null){
			vacationEntity.setAttachmentImage(vacationVO.getAttachmentImage());
		}
		Date now = new Date();
		vacationEntity.setUpdateTime(now);
		vacationDao.save(vacationEntity);


	}



	@Override
	public void saveVacation(VacationVO vacation) throws Exception {
		Date now = new Date();
		String[] vacationTextAndHours = getVacationTextAndHoursForHR(vacation.getUserID(), vacation.getBeginDate(), vacation.getEndDate());
		vacation.setShowHours(Double.parseDouble(vacationTextAndHours[1]));
		VacationEntity vacationEntity = VacationEntity.builder()
				.userID(vacation.getUserID())
				.requestUserID(vacation.getRequestUserID())
				.hours(vacation.getShowHours())
				.beginDate(DateUtil.getFullDate(vacation.getBeginDate()))
				.endDate(DateUtil.getFullDate(vacation.getEndDate()))
				.agentID(vacation.getAgentID())
				.vacationType(vacation.getVacationType())
				.reason(vacation.getReason())
				.attachmentImage(vacation.getAttachmentImage())
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
				.addTime(now)
				.updateTime(now)
				.dailyHours(vacation.getDailyHours())
				.type(vacation.getType())
				.companyID(vacation.getCompanyID())
				.departmentID(vacation.getDepartmentID())
				.build();
		vacationDao.save(vacationEntity);


	}

	@Override
	public void deleteVacationByVacationID(int vacationID) {
		VacationEntity vacationEntity = vacationDao.findVacationsByvacationID(vacationID);
		vacationDao.deleteVacationsByVacationEntity(vacationEntity);

	}

	@Override
	public VacationVO findVacationByVacationID(int vacationID) {
		VacationEntity vacationEntity = vacationDao.findVacationsByvacationID(vacationID);
		VacationVO vacationVO = VacationVOTransFormer.INSTANCE.apply(vacationEntity);
		if(Constants.DEP.equals(vacationVO.getType())){
			String[] vacationUserIds = vacationVO.getRequestUserID().split(",");
			List<String> vacationUserNames = new ArrayList<>();
			for(String vacationUserId: vacationUserIds){
				StaffEntity staff = staffService.getStaffByUserId(vacationUserId);
				vacationUserNames.add(staff.getStaffName());
			}
			vacationVO.setRequestUserName(StringUtils.join(vacationUserNames, ","));
		}else{
			vacationVO.setRequestUserName(staffService.getStaffByUserID(vacationEntity.getRequestUserID()).getLastName());
		}
		if(!StringUtils.isBlank(vacationEntity.getAgentID())){
			vacationVO.setAgentName(staffService.getStaffByUserID(vacationEntity.getAgentID()).getLastName());
		}

		return vacationVO;
	}

	@Override
	public VacationVO getVacationByProcessInstanceID(String processInstanceID) {
		VacationEntity vacationEntity = vacationDao.getVacationByProcessInstanceID(processInstanceID);
		return VacationVOTransFormer.INSTANCE.apply(vacationEntity);
	}

	@Override
	public List<VacationTaskVO> createTaskVOListByTaskList(List<Task> tasks) {
		List<VacationTaskVO> taskVOs = new ArrayList<VacationTaskVO>();
		for (Task task : tasks) {
			taskVOs.add(getVacationTaskVOByTask(task));
		}
		Collections.sort(taskVOs, new Comparator<VacationTaskVO>() {
			@Override
			public int compare(VacationTaskVO o, VacationTaskVO oEctype) {
				Date date = null;
				Date dateEctype = null;
				
				String s = o.getBeginDate();
				String sEctype = oEctype.getBeginDate();
				
				String pattern = "yyyy-MM-dd HH:mm:ss";
				
				SimpleDateFormat sdf=new SimpleDateFormat(pattern);
				SimpleDateFormat sdfEctype=new SimpleDateFormat(pattern);
				try {
					date=sdf.parse(s);
					dateEctype=sdfEctype.parse(sEctype);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = date.compareTo(dateEctype);
				return i;
			}
		});
		return taskVOs;
	}

	@Override
	public VacationTaskVO getVacationTaskVOByTask(Task task) {
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		//查询流程参数
		VacationVO arg = (VacationVO) runtimeService.getVariable(pInstance.getId(), "arg");
		VacationTaskVO taskVO = new VacationTaskVO();
		taskVO.setProcessInstanceID(task.getProcessInstanceId());
		taskVO.setRequestUserName(arg.getUserName());
		taskVO.setVacationUserId(arg.getUserID());
		taskVO.setRequestDate(arg.getRequestDate());
		taskVO.setTaskID(task.getId());
		taskVO.setTitle(arg.getTitle());
		taskVO.setDefKey(task.getTaskDefinitionKey());
		taskVO.setTaskName(task.getName());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
		taskVO.setAttachmentSize(attas.size());
		taskVO.setType(arg.getType());
		if(Constants.DEP.equals(arg.getType())){
			CompanyEntity company = companyDao.getCompanyByCompanyID(arg.getCompanyID());
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(arg.getDepartmentID());
			List<String> groupList = new ArrayList<>();
			groupList.add(company.getCompanyName()+"—"+department.getDepartmentName());
			taskVO.setGroupList(groupList);
			taskVO.setVacationUserName(company.getCompanyName()+"—"+department.getDepartmentName());
			List<String> staffVos = new ArrayList<>();
			String[] vacationUserIds = arg.getRequestUserID().split(",");
			for(String vacationUserId: vacationUserIds){
				StaffVO staffVo = staffService.getStaffByUserID(vacationUserId);
				staffVos.add(staffVo.getLastName());
			}
			taskVO.setStaffNames(staffVos);
		}else{
			StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
			if (staffVO != null) {
				List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffVO.getUserID());
				taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
					@Override
					protected String safeApply(GroupDetailVO input) {
						return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
					}
				}));
			}
			taskVO.setVacationUserName(arg.getRequestUserName());
		}
		taskVO.setBeginDate(arg.getBeginDate());
		String showDays = "";
		int day = (int) Math.floor(arg.getShowHours()/arg.getDailyHours());
		if (day != 0) {
			showDays += (day+"天");
		}
		double hour = arg.getShowHours() - day*arg.getDailyHours();
		if (hour != 0) {
			showDays += (hour+"小时");
		}
		taskVO.setVacationTime(showDays);
		taskVO.setEndDate(arg.getEndDate());
		taskVO.setAgentName(arg.getAgentName());
		taskVO.setVacationType(VacationTypeEnum.valueOf(arg.getVacationType()).getName());
		taskVO.setReason(arg.getReason());
		return taskVO;
	}

	private List<VacationTaskVO> createTaskVOList(List<Object> tasks) {
		List<VacationTaskVO> taskVOs = new ArrayList<VacationTaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId((String)objs[1]).singleResult();
			//查询流程参数
			VacationVO arg = (VacationVO) runtimeService.getVariable(pInstance.getId(), "arg");
			VacationTaskVO taskVO = new VacationTaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTitle(arg.getTitle());
			List<Attachment> attas = taskService.getProcessInstanceAttachments(pInstance.getId());
			taskVO.setAttachmentSize(attas.size());
			taskVO.setType(arg.getType());
			//调试bug
			//logger.error(pInstance.getId()+arg.getType()+arg.getCompanyID()+arg.getDepartmentID()+arg.getProcessInstanceID()+"/"+arg.getRequestUserID());
			if(Constants.DEP.equals(arg.getType()) || arg.getRequestUserID().contains(",")){
				VacationEntity vacationEntity = vacationDao.getVacationByProcessInstanceID(pInstance.getId());
				CompanyEntity company = companyDao.getCompanyByCompanyID(vacationEntity.getCompanyID());
				DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(vacationEntity.getDepartmentID());
				List<String> groupList = new ArrayList<>();
				groupList.add(company.getCompanyName()+"—"+department.getDepartmentName());
				taskVO.setGroupList(groupList);
				taskVO.setVacationUserName(company.getCompanyName()+"—"+department.getDepartmentName());
				List<String> staffVos = new ArrayList<>();
				String[] vacationUserIds = arg.getRequestUserID().split(",");
				for(String vacationUserId: vacationUserIds){
					StaffVO staffVo = staffService.getStaffByUserID(vacationUserId);
					staffVos.add(staffVo.getLastName());
				}
				taskVO.setStaffNames(staffVos);
			}else{
				StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
				if (staffVO != null) {
					List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffVO.getUserID());
					taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
						@Override
						protected String safeApply(GroupDetailVO input) {
							return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
						}
					}));
				}
				taskVO.setVacationUserName(arg.getRequestUserName());
			}
			taskVO.setBeginDate(arg.getBeginDate());
			String showDays = "";
			if(arg.getShowHours()==null||arg.getDailyHours()==null){
				arg.setDailyHours(new Double(9));
				runtimeService.setVariable(pInstance.getId(), "arg", arg);
			}
			int day = (int) Math.floor(arg.getShowHours()/arg.getDailyHours());
			if (day != 0) {
				showDays += (day+"天");
			}
			double hour = arg.getShowHours() - day*arg.getDailyHours();
			if (hour != 0) {
				showDays += (hour+"小时");
			}
			taskVO.setVacationTime(showDays);
			taskVO.setEndDate(arg.getEndDate());
			taskVO.setAgentName(arg.getAgentName());
			taskVO.setVacationType(VacationTypeEnum.valueOf(arg.getVacationType()).getName());
			taskVO.setReason(arg.getReason());
			taskVOs.add(taskVO);
		}
		Collections.sort(taskVOs, new Comparator<VacationTaskVO>() {
			@Override
			public int compare(VacationTaskVO o, VacationTaskVO oEctype) {
				Date date = null;
				Date dateEctype = null;
				
				String s = o.getBeginDate();
				String sEctype = oEctype.getBeginDate();
				
				String pattern = "yyyy-MM-dd HH:mm:ss";
				
				SimpleDateFormat sdf=new SimpleDateFormat(pattern);
				SimpleDateFormat sdfEctype=new SimpleDateFormat(pattern);
				try {
					date=sdf.parse(s);
					dateEctype=sdfEctype.parse(sEctype);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = date.compareTo(dateEctype);
				return i;
			}
		});
		return taskVOs;
	}



	@Override
	public VacationVO getDaysAndHours(String beginDate, String endDate, String userID,
			Integer companyID, Integer departmentId) throws Exception {
		/*		String firstDay =beginDate  + positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(companyID));
		String lastDay =endDate + positionService.getEndTimeByCompanyID(CompanyIDEnum.valueOf(companyID));*/
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId+"", beginDate);
		String firstDay = beginDate + " " + workTimeArray[0] + ":00";
		String lastDay = endDate + " " + workTimeArray[3] + ":00";
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(companyID));
		//double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "");
		String sql = "SELECT s.UserID, s.StaffName, GROUP_CONCAT(s.Hours), GROUP_CONCAT(s.BeginDate), GROUP_CONCAT(s.EndDate), GROUP_CONCAT(s.dailyHours) from("
				+ "SELECT staff.UserID, staff.StaffName, vacation.Hours, vacation.BeginDate, vacation.EndDate, vacation.dailyHours FROM OA_Vacation vacation "
				//+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON vacation.RequestUserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON locate(membership.USER_ID_,vacation.RequestUserID)>0 "
				+ "LEFT JOIN ACT_ID_GROUP gp ON gp.ID_ = membership.GROUP_ID_ "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_HI_PROCINST procinst ON procinst.PROC_INST_ID_ = vacation.ProcessInstanceID "
				+ "WHERE vacation.IsDeleted = 0 AND staff.IsDeleted = 0 "
				+ "AND staff.UserID ='"+userID+"' "
				+ "AND vacation.BeginDate < '"+lastDay+"' "
				+ "AND vacation.EndDate > '"+firstDay+"' AND (vacation.ProcessInstanceID is null or (vacation.ProcessStatus = 1 "
				+ "AND procinst.END_ACT_ID_ IS NOT NULL)) GROUP BY UserID, BeginDate) s GROUP BY s.StaffName ";
		List<Object> result = baseDao.findBySql(sql);

		List<VacationVO> vacationVOs = new ArrayList<VacationVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			VacationVO vacationVO = new VacationVO();
			vacationVO.setRequestUserID((String) objs[0]);
			vacationVO.setRequestUserName((String) objs[1]);
			vacationVO.setShowHours(0.0);
			String[] hours = StringUtils.split((String) objs[2], ",");
			String[] beginDates = StringUtils.split((String) objs[3], ",");
			String[] endDates = StringUtils.split((String) objs[4], ",");
			String[] _dailyHours = StringUtils.split((String)objs[5], ",");
			List<String> dateDetail = new ArrayList<String>();
			double dailyHours = 0;
			int days = 0;
			//剩余累加的小时数
			double showHours = 0;
			//每个工作日（周一至周五）休息的时长
			Map<String, Double> weekDayAndRestHoursMap = new TreeMap<>();
			//周一至周五休息的天数
			double restDaysInweekDay = 0;
			//周一至周五休息的小时数（折算成天数剩余的小时数）
			double restHoursInweekDay = 0;
			for (int i = 0; i < hours.length; ++i) {
				double hour = Double.valueOf(hours[i]);
				Date beginDate1 = DateUtil.getFullDate(beginDates[i]).getTime()<=DateUtil.getFullDate(firstDay).getTime() ? DateUtil.getFullDate(firstDay) : DateUtil.getFullDate(beginDates[i]);
				//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(beginDate1));
				dailyHours = Double.parseDouble(_dailyHours[i]);
				Date endDate1 = DateUtil.getFullDate(endDates[i]).getTime()>=DateUtil.getFullDate(lastDay).getTime() ? DateUtil.getFullDate(lastDay) : DateUtil.getFullDate(endDates[i]);
				if (!beginDate1.equals(DateUtil.getFullDate(beginDates[i])) || !endDate1.equals(DateUtil.getFullDate(endDates[i]))) {
					hour = calcWorkHours(beginDates[i], beginDate1, endDate1, companyID, userID);
				}

				Object[] args = {beginDates[i], beginDate1, endDate1, userID, weekDayAndRestHoursMap, hour};
				//判断并获取周一至周五休息的时长
				double restHours = getRestHoursInWeekDay(args);
				double remainHours = restHours % dailyHours;
				restDaysInweekDay += (restHours-remainHours)/dailyHours;
				restHoursInweekDay += remainHours;

				int day = (int)Math.floor(hour/dailyHours);
				days += day;
				showHours += (hour-day*dailyHours);
				//vacationVO.setShowHours(vacationVO.getShowHours()+hour);
				dateDetail.add(DateUtil.formateFullDate(beginDate1)+"至"+DateUtil.formateFullDate(endDate1));
			}
			if(0 != dailyHours){
				double remainHours = restHoursInweekDay % dailyHours;
				restDaysInweekDay += (restHoursInweekDay-remainHours)/dailyHours;
				restHoursInweekDay = remainHours;
				vacationVO.setRestDaysInWeekDay(restDaysInweekDay);
				vacationVO.setRestHoursInWeekDay(restHoursInweekDay);
				if(restDaysInweekDay>0){
					if(restHoursInweekDay>0){
						vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restDaysInweekDay)+"天"+removeZero(restHoursInweekDay)+"小时");
					}else{
						vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restDaysInweekDay)+"天");
					}
				}else{
					vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restHoursInweekDay)+"小时");
				}
				vacationVO.setWeekDayAndRestHoursMap(weekDayAndRestHoursMap);
			}
			vacationVO.setDateDetail(dateDetail.toArray(new String[dateDetail.size()]));
			int _day = (int)Math.floor(showHours/dailyHours);
			days += _day;
			vacationVO.setDays(days);
			showHours = showHours - dailyHours*_day;
			BigDecimal b = new BigDecimal(showHours);  
			showHours = b.setScale(1, BigDecimal.ROUND_UP).doubleValue();  
			vacationVO.setShowHours(showHours);
			vacationVO.setDailyHours(dailyHours);
			vacationVOs.add(vacationVO);
		}
		if(vacationVOs.size()>0){
			return vacationVOs.get(0);
		}
		return null;
	}
	@Override
	public VacationVO getDaysAndHoursForCalSalary(String beginDate, String endDate, String userID,
			Integer companyID, Integer departmentId) throws Exception {
		/*		String firstDay =beginDate  + positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(companyID));
		String lastDay =endDate + positionService.getEndTimeByCompanyID(CompanyIDEnum.valueOf(companyID));*/
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId+"", beginDate);
		String firstDay = beginDate + " " + workTimeArray[0] + ":00";
		String lastDay = endDate + " " + workTimeArray[3] + ":00";
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(companyID));
		//double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "");
		String sql = "SELECT s.UserID, s.StaffName, GROUP_CONCAT(s.Hours), GROUP_CONCAT(s.BeginDate), GROUP_CONCAT(s.EndDate), GROUP_CONCAT(s.dailyHours), GROUP_CONCAT(s.vacationType) from("
				+ "SELECT staff.UserID, staff.StaffName, vacation.Hours, vacation.BeginDate, vacation.EndDate, vacation.dailyHours, vacation.vacationType FROM OA_Vacation vacation "
				//+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON vacation.RequestUserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON locate(membership.USER_ID_,vacation.RequestUserID)>0 "
				+ "LEFT JOIN ACT_ID_GROUP gp ON gp.ID_ = membership.GROUP_ID_ "
				+ "LEFT JOIN OA_Staff staff ON staff.UserID = membership.USER_ID_ "
				+ "LEFT JOIN ACT_HI_PROCINST procinst ON procinst.PROC_INST_ID_ = vacation.ProcessInstanceID "
				+ "WHERE vacation.IsDeleted = 0 AND staff.IsDeleted = 0 "
				+ "AND staff.UserID ='"+userID+"' "
				+ "AND vacation.BeginDate < '"+lastDay+"' "
				+ "AND vacation.EndDate > '"+firstDay+"' AND (vacation.ProcessInstanceID is null or (vacation.ProcessStatus = 1 "
				+ "AND procinst.END_ACT_ID_ IS NOT NULL)) GROUP BY UserID, BeginDate) s GROUP BY s.StaffName ";
		List<Object> result = baseDao.findBySql(sql);

		List<VacationVO> vacationVOs = new ArrayList<VacationVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			VacationVO vacationVO = new VacationVO();
			vacationVO.setRequestUserID((String) objs[0]);
			vacationVO.setRequestUserName((String) objs[1]);
			vacationVO.setShowHours(0.0);
			String[] hours = StringUtils.split((String) objs[2], ",");
			String[] beginDates = StringUtils.split((String) objs[3], ",");
			String[] endDates = StringUtils.split((String) objs[4], ",");
			String[] _dailyHours = StringUtils.split((String)objs[5], ",");
			String[] vacationTypes = StringUtils.split((String)objs[6], ",");
			List<String> dateDetail = new ArrayList<String>();
			double dailyHours = 0;
			int ordinaryVacationdays = 0;//普通请假天数
			int specialVacationDays = 0;//特殊请假天数
			//剩余累加的小时数
			double ordinaryVacationHours = 0;//普通请假小时数
			double specialVacationHours = 0;//特殊请假小时数
			//每个工作日（周一至周五）休息的时长
			Map<String, Double> weekDayAndRestHoursMap = new TreeMap<>();
			//周一至周五休息的天数
			double restDaysInweekDay = 0;
			//周一至周五休息的小时数（折算成天数剩余的小时数）
			double restHoursInweekDay = 0;
			for (int i = 0; i < hours.length; ++i) {
				int vacationType = Integer.parseInt(vacationTypes[i]);
				//公假、事假
				if(vacationType==VacationTypeEnum.PAID.getValue() || vacationType==VacationTypeEnum.MATTER.getValue()){
					double hour = Double.valueOf(hours[i]);
					Date beginDate1 = DateUtil.getFullDate(beginDates[i]).getTime()<=DateUtil.getFullDate(firstDay).getTime() ? DateUtil.getFullDate(firstDay) : DateUtil.getFullDate(beginDates[i]);
					//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(beginDate1));
					dailyHours = Double.parseDouble(_dailyHours[i]);
					Date endDate1 = DateUtil.getFullDate(endDates[i]).getTime()>=DateUtil.getFullDate(lastDay).getTime() ? DateUtil.getFullDate(lastDay) : DateUtil.getFullDate(endDates[i]);
					if (!beginDate1.equals(DateUtil.getFullDate(beginDates[i])) || !endDate1.equals(DateUtil.getFullDate(endDates[i]))) {
						hour = calcWorkHours(beginDates[i], beginDate1, endDate1, companyID, userID);
					}

					Object[] args = {beginDates[i], beginDate1, endDate1, userID, weekDayAndRestHoursMap, hour};
					//判断并获取周一至周五休息的时长
					double restHours = getRestHoursInWeekDay(args);
					double remainHours = restHours % dailyHours;
					restDaysInweekDay += (restHours-remainHours)/dailyHours;
					restHoursInweekDay += remainHours;

					int day = (int)Math.floor(hour/dailyHours);
					ordinaryVacationdays += day;
					ordinaryVacationHours += (hour-day*dailyHours);
					//vacationVO.setShowHours(vacationVO.getShowHours()+hour);
					dateDetail.add(BusinessTypeEnum.valueOf(vacationType).getName()+":"+DateUtil.formateFullDate(beginDate1)+"至"+DateUtil.formateFullDate(endDate1));
				//年假、婚假等	
				}else{
					double hour = Double.valueOf(hours[i]);
					Date beginDate1 = DateUtil.getFullDate(beginDates[i]).getTime()<=DateUtil.getFullDate(firstDay).getTime() ? DateUtil.getFullDate(firstDay) : DateUtil.getFullDate(beginDates[i]);
					//dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID+"", "", DateUtil.formateDate(beginDate1));
					dailyHours = Double.parseDouble(_dailyHours[i]);
					Date endDate1 = DateUtil.getFullDate(endDates[i]).getTime()>=DateUtil.getFullDate(lastDay).getTime() ? DateUtil.getFullDate(lastDay) : DateUtil.getFullDate(endDates[i]);
					if (!beginDate1.equals(DateUtil.getFullDate(beginDates[i])) || !endDate1.equals(DateUtil.getFullDate(endDates[i]))) {
						hour = calcWorkHours(beginDates[i], beginDate1, endDate1, companyID, userID);
					}

					int day = (int)Math.floor(hour/dailyHours);
					specialVacationDays += day;
					specialVacationHours += (hour-day*dailyHours);
					//vacationVO.setShowHours(vacationVO.getShowHours()+hour);
					dateDetail.add(DateUtil.formateFullDate(beginDate1)+"至"+DateUtil.formateFullDate(endDate1));
				}
			}
			if(0 != dailyHours){
				double remainHours = restHoursInweekDay % dailyHours;
				restDaysInweekDay += (restHoursInweekDay-remainHours)/dailyHours;
				restHoursInweekDay = remainHours;
				vacationVO.setRestDaysInWeekDay(restDaysInweekDay);
				vacationVO.setRestHoursInWeekDay(restHoursInweekDay);
				if(restDaysInweekDay>0){
					if(restHoursInweekDay>0){
						vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restDaysInweekDay)+"天"+removeZero(restHoursInweekDay)+"小时");
					}else{
						vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restDaysInweekDay)+"天");
					}
				}else{
					vacationVO.setRestDaysAndHoursInWeekDay(removeZero(restHoursInweekDay)+"小时");
				}
				vacationVO.setWeekDayAndRestHoursMap(weekDayAndRestHoursMap);
			}
			vacationVO.setDateDetail(dateDetail.toArray(new String[dateDetail.size()]));
			//普通请假
			int _day = (int)Math.floor(ordinaryVacationHours/dailyHours);
			ordinaryVacationdays += _day;
			vacationVO.setOrdinaryVacationdays(ordinaryVacationdays);
			ordinaryVacationHours = ordinaryVacationHours - dailyHours*_day;
			BigDecimal b = new BigDecimal(ordinaryVacationHours);  
			ordinaryVacationHours = b.setScale(1, BigDecimal.ROUND_UP).doubleValue();  
			vacationVO.setOrdinaryVacationHours(ordinaryVacationHours);
			vacationVO.setDailyHours(dailyHours);
			//特殊请假
			if(specialVacationDays!=0 || specialVacationHours!=0){
				_day = (int)Math.floor(specialVacationHours/dailyHours);
				specialVacationDays += _day;
				vacationVO.setSpecialVacationDays(specialVacationDays);
				specialVacationHours = specialVacationHours - dailyHours*_day;
				b = new BigDecimal(specialVacationHours);  
				specialVacationHours = b.setScale(1, BigDecimal.ROUND_UP).doubleValue();  
				vacationVO.setSpecialVacationHours(specialVacationHours);
			}
			//总的请假天数
			int days = ordinaryVacationdays + specialVacationDays;
			double vacationHours = ordinaryVacationHours + specialVacationHours;
			//折算成天数
			double remainHours = vacationHours%vacationVO.getDailyHours();
			double vacationDays = (vacationHours-remainHours)/vacationVO.getDailyHours();
			days += vacationDays;
			vacationVO.setDays(days);
			vacationVO.setShowHours(remainHours);
			vacationVOs.add(vacationVO);
			
		}
		if(vacationVOs.size()>0){
			return vacationVOs.get(0);
		}
		return null;
	}
	@SuppressWarnings("serial")
	private List<String> weekDayList = new ArrayList<String>(){
		{add("一");add("二");add("三");add("四");add("五");}
	};
	/**
	 * 获取请假期间，属于周一至周五的上班时长
	 * 1、请假开始时间和结束时间在同一天
	 * 2、请假开始时间和结束时间不在同一天
	 * 
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private double getRestHoursInWeekDay(Object[] args) throws Exception {
		//真正的请假开始时间
		String _beginDate = (String) args[0];
		//查询的起始时间或请假开始时间
		Date beginDate = (Date) args[1]; 
		Date endDate = (Date) args[2];
		String userID = (String) args[3];
		Map<String, Double> weekDayAndRestHoursMap = (Map<String, Double>) args[4];
		double hours = (double) args[5];
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
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
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, _beginDate);
		//请假开始时间和结束时间在同一天
		String beginDay = DateUtil.formateDate(beginDate);
		String endDay = DateUtil.formateDate(endDate);
		if(beginDay.equals(endDay)){
			String weekDay = DateUtil.getWeekDay(beginDate);
			//判断是否是周一至周五
			if(weekDayList.contains(weekDay)){
				weekDayAndRestHoursMap.put(beginDay, hours);
				return hours;
			}
			return 0;
		}else{
			Calendar cal = Calendar.getInstance();
			cal.setTime(beginDate);
			Date day = cal.getTime();
			beginDay = DateUtil.formateDate(day);
			double restHours = 0;
			while(!beginDay.equals(endDay)){
				String weekDay = DateUtil.getWeekDay(day);
				Date _endDate = DateUtil.getFullDate(beginDay+" "+workTimeArray[3]+":00");
				if(weekDayList.contains(weekDay)){
					double hour = calcRestHours(day, _endDate, workTimeArray);
					if(hour>0){
						weekDayAndRestHoursMap.put(beginDay, hour);
					}
					restHours += hour;
				}
				cal.add(Calendar.DATE, 1);
				day = DateUtil.getFullDate(DateUtil.formateDate(cal.getTime())+" "+workTimeArray[0]+":00");
				beginDay = DateUtil.formateDate(day);
			}
			//开始时间和结束时间在同一天
			String weekDay = DateUtil.getWeekDay(day);
			//判断是否是周一至周五
			if(weekDayList.contains(weekDay)){
				double hour = calcWorkHours(day, endDate, workTimeArray);
				if(hour>0){
					weekDayAndRestHoursMap.put(beginDay, hour);
				}
				restHours += hour;
			}
			return restHours;
		}
	}
	@Override
	public double calcWorkHours(Date beginDate, Date endDate, String[] workTimeArray) {
		String beginTime = " " + workTimeArray[0] + ":00";
		String endTime = " " + workTimeArray[3] + ":00";
		//上午下班时间
		String amEndTime = " " + workTimeArray[1] + ":00";
		//下午上班时间
		String pmBeginTime= " " + workTimeArray[2] + ":00";
		//作息时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+beginTime);
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+amEndTime);
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+pmBeginTime);
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+endTime);
		
		if(DateUtil.before(beginDate, amBeginDate)){
			beginDate = amBeginDate;
		}
		if(DateUtil.after(endDate, pmEndDate)){
			endDate = pmEndDate;
		}
		//请假的开始时间在请假开始当天的下班后
		if(DateUtil.after(beginDate, pmEndDate)){
			return 0;
		}
		//请假的结束时间在请假结束当天的上班前
		if(DateUtil.before(endDate, amBeginDate)){
			return 0;
		}
		//午休时间
		double breakTime = 0;
		/** 1、请假开始时间在上午区间
		 * 	  1.1、结束时间在上午区间//do nothing
		 *    1.2、结束时间在午休区间
		 *    1.3、结束时间在下午区间
		 *  2、请假开始时间在午休区间
		 *    1.1、结束时间在下午区间
		 *  3、请假开始时间在下午区间
		 *    //do nothing
		 */
		//请假开始时间在上午区间
		if(DateUtil.before(beginDate, amEndDate)){
			//结束时间在午休区间
			if(DateUtil.after(endDate, amEndDate) && DateUtil.before(endDate, pmBeginDate)){
				breakTime += (endDate.getTime()-amEndDate.getTime());
				//结束时间在下午区间
			}else if(DateUtil.after(endDate, pmBeginDate)){
				breakTime += (pmBeginDate.getTime()-amEndDate.getTime());
			}
			//请假开始时间在午休区间
		}else if(DateUtil.after(beginDate, amEndDate) && DateUtil.before(beginDate, pmBeginDate)){
			//结束时间在下午区间
			if(DateUtil.after(endDate, pmBeginDate)){
				breakTime += (pmBeginDate.getTime()-beginDate.getTime());
			}
			//请假开始时间在下午区间
		}else{
			//do nothing
		}
		double diff = endDate.getTime() - beginDate.getTime();
		return Math.floor((diff - breakTime)/(60*30*1000))/2;
	}
	@Override
	public double calcRestHours(Date beginDate, Date endDate, String[] workTimeArray) {
		String beginTime = " " + workTimeArray[0] + ":00";
		String endTime = " " + workTimeArray[3] + ":00";
		//上午下班时间
		String amEndTime = " " + workTimeArray[1] + ":00";
		//下午上班时间
		String pmBeginTime= " " + workTimeArray[2] + ":00";
		//作息时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+beginTime);
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+amEndTime);
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+pmBeginTime);
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(beginDate)+endTime);
		
		if(DateUtil.before(beginDate, amBeginDate)){
			beginDate = amBeginDate;
		}
		if(DateUtil.after(endDate, pmEndDate)){
			endDate = pmEndDate;
		}
		//请假的开始时间在请假开始当天的下班后
		if(DateUtil.after(beginDate, pmEndDate)){
			return 0;
		}
		//请假的结束时间在请假结束当天的上班前
		if(DateUtil.before(endDate, amBeginDate)){
			return 0;
		}
		//午休时间
		double breakTime = 0;
		/** 1、请假开始时间在上午区间
		 * 	  1.1、结束时间在上午区间//do nothing
		 *    1.2、结束时间在午休区间
		 *    1.3、结束时间在下午区间
		 *  2、请假开始时间在午休区间
		 *    1.1、结束时间在下午区间
		 *  3、请假开始时间在下午区间
		 *    //do nothing
		 */
		//请假开始时间在上午区间
		if(DateUtil.before(beginDate, amEndDate)){
			//结束时间在午休区间
			if(DateUtil.after(endDate, amEndDate) && DateUtil.before(endDate, pmBeginDate)){
				breakTime += (endDate.getTime()-amEndDate.getTime());
				//结束时间在下午区间
			}else if(DateUtil.after(endDate, pmBeginDate)){
				breakTime += (pmBeginDate.getTime()-amEndDate.getTime());
			}
			//请假开始时间在午休区间
		}else if(DateUtil.after(beginDate, amEndDate) && DateUtil.before(beginDate, pmBeginDate)){
			//结束时间在下午区间
			if(DateUtil.after(endDate, pmBeginDate)){
				breakTime += (pmBeginDate.getTime()-beginDate.getTime());
			}
			//请假开始时间在下午区间
		}else{
			//do nothing
		}
		double diff = endDate.getTime() - beginDate.getTime();
		return Math.ceil((diff - breakTime)/(60*30*1000))/2;
	}
	@Override
	public Date getBeginAndEndTimeFirst(String time,String userId) {
		//String sql = " select v.BeginDate from OA_Vacation  v where v.BeginDate<='"+time+"' and v.EndDate >='"+time+"' AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0' and v.RequestUserID='"+userId+"' order by v.BeginDate asc ";
		String sql = " select v.BeginDate from OA_Vacation  v where v.BeginDate<='"+time+"' and v.EndDate >='"+time+"' AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0' and locate('"+userId+"' ,v.RequestUserID)>0 order by v.BeginDate asc ";
		List<Object> resultList=baseDao.findBySql(sql);
		if (resultList!=null&&resultList.size()>0) {
			return (Date)resultList.get(0);
		}
		return null;
	}

	@Override
	public Date getBeginAndEndTimeLast(String time,String userId) {
		//String sql = " select v.EndDate from OA_Vacation  v where v.BeginDate<='"+time+"' and v.EndDate >='"+time+"' AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0'  and v.RequestUserID='"+userId+"' order by v.EndDate desc ";
		String sql = " select v.EndDate from OA_Vacation  v where v.BeginDate<='"+time+"' and v.EndDate >='"+time+"' AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0'  and locate('"+userId+"' ,v.RequestUserID)>0 order by v.EndDate desc ";
		List<Object> resultList=baseDao.findBySql(sql);
		if (resultList!=null&&resultList.size()>0) {
			return (Date)resultList.get(0);
		}
		return null;
	}

	@Override
	public long getEffectiveVacationTime(Date startTime, Date endTime,String userID) {
		//String sql="select  v.BeginDate,v.EndDate from OA_Vacation v where ( v.EndDate>='"+startTime+"' or v.BeginDate<='"+endTime+"' )  AND v.ProcessStatus='1' and v.IsDeleted='0'  and v.RequestUserID='"+userID+"' order by v.BeginDate  ";
		String sql="select  v.BeginDate,v.EndDate from OA_Vacation v where ( v.EndDate>='"+startTime+"' or v.BeginDate<='"+endTime+"' )  AND v.ProcessStatus='1' and v.IsDeleted='0'  and locate('"+userID+"' ,v.RequestUserID)>0 order by v.BeginDate  ";
		List<Object> resultList=baseDao.findBySql(sql);
		if (resultList!=null&&resultList.size()>0) {
			//因为只用于 迟到早退的判断 这里有效数据 正常只有一条  这里无论有几条 只取一条
			Object[] times=(Object[]) resultList.get(0);
			Date begin=((Date)times[0]).after(startTime)?(Date)times[0]:startTime;
			Date end=((Date)times[1]).after(endTime)?endTime:(Date)times[1];
			long time=end.getTime()-begin.getTime();
			return time>0?time:0;
		}
		return 0;
	}

	@Override
	public String getInstanceIdByVacationId(String id) {
		String sql="select v.ProcessInstanceID from OA_Vacation v where v.VacationID="+id;
		List<Object> result=baseDao.findBySql(sql);
		if(result==null||result.size()==0){
			return null;
		}
		return result.get(0)+"";
	}

	@Override
	public Date getRealEndTimeLast(String time, String userId) {
		String sql = "select v.EndDate as realEndTime from OA_Vacation v where v.BeginDate<='"+time+"' "+
				"and SUBSTRING_INDEX(v.EndDate,' ',1) = SUBSTRING_INDEX('"+time+"',' ',1) "+
				"AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0' and locate('"+userId+"',v.RequestUserID)>0 order by v.EndDate desc";
		List<Object> resultLst=baseDao.findBySql(sql);
		if (null!=resultLst && resultLst.size()>0) {
			return (Date)resultLst.get(0);
		}
		return null;
	}

	@Override
	public Date getRealBeginTimeFirst(String time, String userId) {
		String sql = "select v.BeginDate as realBeginTime from OA_Vacation  v where v.EndDate >='"+time+"' "+ 
				"and SUBSTRING_INDEX(v.BeginDate,' ',1) = SUBSTRING_INDEX('"+time+"',' ',1) "+
				"AND (ProcessInstanceID is null or v.ProcessStatus='1') and v.IsDeleted='0'  and locate('"+userId+"',v.RequestUserID)>0 order by v.BeginDate";
		List<Object> resultLst=baseDao.findBySql(sql);
		if (null!=resultLst && resultLst.size()>0) {
			return (Date)resultLst.get(0);
		}
		return null;
	}

	@Override
	public String getProcessInstanceId(Integer vacationId) {
		String sql = "select ProcessInstanceID from OA_Vacation where VacationID="+vacationId;
		Object obj = baseDao.getUniqueResult(sql);
		if( null==obj){
			return "";
		}
		return obj+"";
	}
	public VacationTaskVO getVacationTaskVOByTask(String processInstanceId) {
		ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		//查询流程参数
		VacationVO arg = (VacationVO) runtimeService.getVariable(pInstance.getId(), "arg");
		VacationTaskVO taskVO = new VacationTaskVO();
		taskVO.setProcessInstanceID(processInstanceId);
		taskVO.setRequestUserName(arg.getUserName());
		taskVO.setVacationUserId(arg.getUserID());
		taskVO.setRequestDate(arg.getRequestDate());
		taskVO.setTitle(arg.getTitle());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceId);
		taskVO.setAttachmentSize(attas.size());
		StaffVO staffVO = staffService.getStaffByUserID(arg.getRequestUserID());
		if (staffVO != null) {
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffVO.getUserID());
			taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName()+"—"+input.getDepartmentName()+"—"+input.getPositionName();
				}
			}));
		}
		taskVO.setVacationUserName(arg.getRequestUserName());
		taskVO.setBeginDate(arg.getBeginDate());
		String showDays = "";
		int day = (int) Math.floor(arg.getShowHours()/arg.getDailyHours());
		if (day != 0) {
			showDays += (day+"天");
		}
		double hour = arg.getShowHours() - day*arg.getDailyHours();
		if (hour != 0) {
			showDays += (hour+"小时");
		}
		taskVO.setVacationTime(showDays);
		taskVO.setEndDate(arg.getEndDate());
		taskVO.setAgentName(arg.getAgentName());
		taskVO.setVacationType(VacationTypeEnum.valueOf(arg.getVacationType()).getName());
		taskVO.setReason(arg.getReason());
		return taskVO;
	}

	@Override
	public List<VacationDetailVo> getVacationDetailObjs(String companyId) throws Exception {
		List<VacationDetailVo> vacationDetailVos = new ArrayList<>();
		String sql = "";
		if(StringUtils.isNotBlank(companyId)){
			sql = "SELECT\n" +
					"	DepartmentName,\n" +
					"	dep.DepartmentID,\n" +
					"	StaffName,\n" +
					"	staff.UserID,\n" +
					"	vacation.beginDate,\n" +
					"	vacation.endDate,\n" +
					"	vacation.reason,\n" +
					"	vacation.VacationID\n" +
					"FROM\n" +
					"	(\n" +
					"		SELECT\n" +
					"			*\n" +
					"		FROM\n" +
					"			OA_Vacation\n" +
					"		WHERE\n" +
					"			IFNULL(ProcessStatus, 0) != 31\n" +
					"		AND	IFNULL(ProcessStatus, 0) != 2\n" +
					"		AND IsDeleted = 0\n" +
					"		AND CURRENT_DATE () >= DATE(BeginDate)\n" +
					"		AND CURRENT_DATE () <= DATE(EndDate)\n" +
					"	) vacation,\n" +
					"	OA_Staff staff,\n" +
					" ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail grDetail,\n" +
					"	OA_Department dep,\n" +
					"	OA_Company company\n" +
					"WHERE\n" +
					"	locate(staff.UserID, vacation.RequestUserID)>0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.IsDeleted = 0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = grDetail.GroupID\n" +
					"AND grDetail.DepartmentID = dep.DepartmentID\n" +
					"AND grDetail.CompanyID = company.CompanyID\n" +
					"AND company.CompanyID = "+companyId+"\n" +
					//"GROUP BY VacationID\n" +
					"GROUP BY UserID, beginDate\n" +
					"order by DepartmentName";
		}else{
			sql = "SELECT\n" +
					"	DepartmentName,\n" +
					"	dep.DepartmentID,\n" +
					"	StaffName,\n" +
					"	staff.UserID,\n" +
					"	vacation.beginDate,\n" +
					"	vacation.endDate,\n" +
					"	vacation.reason\n," +
					"	vacation.VacationID\n" +
					"FROM\n" +
					"	(\n" +
					"		SELECT\n" +
					"			*\n" +
					"		FROM\n" +
					"			OA_Vacation\n" +
					"		WHERE\n" +
					"			IFNULL(ProcessStatus, 0) != 31\n" +
					"		AND	IFNULL(ProcessStatus, 0) != 2\n" +
					"		AND IsDeleted = 0\n" +
					"		AND CURRENT_DATE () >= DATE(BeginDate)\n" +
					"		AND CURRENT_DATE () <= DATE(EndDate)\n" +
					"	) vacation,\n" +
					"	OA_Staff staff,\n" +
					" ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail grDetail,\n" +
					"	OA_Department dep\n" +
					"WHERE\n" +
					"	locate(staff.UserID, vacation.RequestUserID)>0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.IsDeleted = 0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = grDetail.GroupID\n" +
					"AND grDetail.DepartmentID = dep.DepartmentID\n"+
					//"GROUP BY VacationID\n" +
					"GROUP BY UserID, beginDate\n" +
					"order by DepartmentName";
		}
		//请假信息
		List<Object> vacationDetailObjs = baseDao.findBySql(sql);
		for(Object vacationDetailObj: vacationDetailObjs){
			Object[] objs = (Object[])vacationDetailObj;
			VacationDetailVo vacationDetailVo = new VacationDetailVo();
			vacationDetailVo.setDepartmentName(objs[0]+"");
			vacationDetailVo.setDepartmentId(objs[1]+"");
			vacationDetailVo.setStaffName(objs[2]+"");
			vacationDetailVo.setReason(null==objs[6] ? "":objs[6]+"");
			String vacationId = objs[7]+"";
			vacationDetailVo.setVacationID(vacationId);
			//请假人员id
			String userId = objs[3]+"";
			//请假开始时间
			String beginTime = objs[4]+"";
			//请假结束时间
			String endTime = objs[5]+"";
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
			companyId = group.getType().split("_")[0];
			//如果请假结束时间是今天上班时间之前，不算今日请假
			if(checkVacationEndTime(endTime, userId)){
				String[] values = getVacationDateStr(userId, beginTime, endTime, companyId, vacationId);
				vacationDetailVo.setVacationDate(values[0]);
				vacationDetailVo.setVacationDays(values[1]);
				vacationDetailVo.setContinuousRestDays(values[2]);
				vacationDetailVo.setCurrentMonthRestDays(values[3]);
				//OA审批人
				String auditor = getVacationAuditor(userId);
				vacationDetailVo.setAuditor(auditor);
				vacationDetailVos.add(vacationDetailVo);
			}
		}
		return vacationDetailVos;
	}
	private boolean checkVacationEndTime(String endTime, String userId) throws Exception {
		String companyId = "";
		String departmentId = "";
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		if(null!=groups && groups.size()>0){
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				//总部优先
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			companyId = group.getType().split("_")[0];
			departmentId = group.getType().split("_")[1];
		}
		//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyId));
		//String workTimes = companyID.getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		Date vacationEndDate = DateUtil.getFullDate(endTime);
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(vacationEndDate));
		String amBeginTime = workTimeArray[0];
		String today = DateUtil.formateDate(new Date());
		Date amBeginDate = DateUtil.getFullDate(today+" "+amBeginTime+":00");
		if(DateUtil.before(vacationEndDate, amBeginDate)){
			return false;
		}
		return true;
	}

	/**
	 * 获取请假的审批人
	 * @param userId
	 * @return
	 */
	private String getVacationAuditor(String userId) {
		String auditor = "";
		//只寻找一级
		auditor = staffService.querySupervisorOneStep(userId);
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(auditor)){
			auditor = staffService.queryHeadMan(userId);
		}
		if(StringUtils.isBlank(auditor) || userId.equals(auditor)) {
			auditor = staffService.querySupervisor(userId);
		}
		//无上级主管，总经理审批
		if(StringUtils.isBlank(auditor)){
			auditor = staffService.queryManager(userId);	
		}
		if(StringUtils.isNotBlank(auditor)){
			auditor = staffService.getRealNameByUserId(auditor);
		}
		return auditor;
	}

	/**
	 * 首先判断请假时间是否在一天内（结束时间小于等于第二天的上班时间）
	 * 1、同一天（只考虑一般的请假情况（上午请假，下午请假，一天假），请假时间横跨上午和下午的上班时间， 按一天算）
	 *   1.1、上午请假，请假结束时间-请假开始时间，不足半天（小于上午作息时间），按小时计（半小时起算）
	 *   1.2 、下午请假，请假结束时间-请假开始时间，不足半天（小于下午作息时间），按小时计（半小时起算）
	 *   1.3 、一天假
	 * 2、不在同一天（请假时间横跨上午和下午的上班时间， 按一天算）
	 *   2。1、开始时间在下午工作区间内
	 *   	2.1.1、结束时间在上午工作区间内
	 *   	2.1.2、结束时间为整天
	 *   2.2、开始时间为整天
	 *   	2.2。1、结束时间在上午工作区间内
	 *   	2.2.2、结束时间为整天

	 * @param userId
	 * @param beginTime 请假开始时间
	 * @param endTime 请假结束时间
	 * @param companyId
	 * @param vacationId
	 * @return [请假日期，请假天数，已连休天数，月累计休息天数]
	 * @throws Exception 
	 */
	@SuppressWarnings("static-access")
	public String[] getVacationDateStr(String userId, String beginTime, String endTime, String companyId, String vacationId) throws Exception{
		//请假日期
		String vacationDateStr = "";
		//请假天数
		String vacationDays = "";
		//已连休天数
		String continuousRestDays = "";
		//月累计休息天数
		String currentMonthRestDays = "";
		String departmentId = "";
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
		departmentId = group.getType().split("_")[1];
		//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyId));
		//String workTimes = companyID.getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		Date vacationBeginDate = DateUtil.getFullDate(beginTime);
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId,
				departmentId, DateUtil.formateDate(vacationBeginDate));
		//作息时间
		String amBeginTime = workTimeArray[0];
		String amEndTime = workTimeArray[1];
		String pmBeginTime = workTimeArray[2];
		String pmEndTime = workTimeArray[3];
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(vacationBeginDate); 
		calendar.add(calendar.DATE,1);
		//第二天上班的开始时间
		Date secondDayBeginTime = DateUtil.getFullDate(DateUtil.formateDate(calendar.getTime())+" "+amBeginTime+":00");
		Date vacationEndDate = DateUtil.getFullDate(endTime);
		//请假当天的上午上班时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amBeginTime+":00");
		//请假当天的上午下班时间
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amEndTime+":00");
		//请假当天的下午上班时间
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmBeginTime+":00");
		//请假当天的下午下班时间
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
		//请假结束的下午上班时间
		Date _pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String vacationBeginDateStr = sdf.format(vacationBeginDate).replace("-", ".");
		//把日期前面的0去掉，如08.25
		if(vacationBeginDateStr.startsWith("0")){
			vacationBeginDateStr = vacationBeginDateStr.substring(1, vacationBeginDateStr.length());
		}
		String vacationEndDateStr = sdf.format(vacationEndDate).replace("-", ".");
		//把日期前面的0去掉，如08.25
		if(vacationEndDateStr.startsWith("0")){
			vacationEndDateStr = vacationEndDateStr.substring(1, vacationEndDateStr.length());
		}
		//上午的工作时间
		double amWorkTime = amEndDate.getTime() - amBeginDate.getTime();
		double amWorkHours = Math.ceil(amWorkTime/(60*30*1000))/2;
		//下午的工作时间
		double pmWorkTime = pmEndDate.getTime() - pmBeginDate.getTime();
		double pmWorkHours = Math.ceil(pmWorkTime/(60*30*1000))/2;
		//中间的休息时间
		double breakTime = pmBeginDate.getTime() - amEndDate.getTime();
		//之前的请假时长（不包括今天所在的请假）
		double[] beforeVacationTimes = getBeforeVacationDays(vacationId, userId);
		double beforeVacationDays = beforeVacationTimes[0];
		double beforeVacationHours =  beforeVacationTimes[1];
		//每天的工作时长
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(Integer.parseInt(companyId)));
		double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(vacationBeginDate));
		/* * 1、同一天（只考虑一般的请假情况（上午请假，下午请假，一天假），请假时间横跨上午和下午的上班时间， 按一天算）
		 *   1.1、上午请假，请假结束时间-请假开始时间，不足半天（小于上午作息时间），按小时计（半小时起算）
		 *   1.2 、下午请假，请假结束时间-请假开始时间，不足半天（小于下午作息时间），按小时计（半小时起算）
		 *   1.3 、一天假
		 */
		//结束时间小于等于第二天的上班时间，由此判断请假时间在同一天内
		if(DateUtil.before(vacationEndDate, secondDayBeginTime)){
			//请假的时间
			double time = vacationEndDate.getTime() - vacationBeginDate.getTime();
			//按半小时起算，向上取值
			double vacationHours = Math.ceil(time/(60*30*1000))/2;
			//上午请假
			if(DateUtil.before(vacationEndDate, pmBeginDate)){
				vacationDateStr = vacationBeginDateStr+"上午";
				//请假时间为上午半天
				if(vacationHours>=amWorkHours){
					vacationDays = "0.5";
					continuousRestDays = "0.5";
					beforeVacationHours += amWorkHours;
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(hours>0){
						//保留一位小数，四舍五入
						hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
						currentMonthRestDays = removeZero(beforeDays)+"天"+removeZero(hours)+"小时";
					}else{
						currentMonthRestDays = removeZero(beforeDays);
					}

				}else{
					vacationDays = removeZero(vacationHours)+"小时";
					beforeVacationHours += vacationHours;
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(beforeDays>0){
						if(hours>0){
							//保留一位小数，四舍五入
							hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
							currentMonthRestDays = removeZero(beforeDays)+"天"+removeZero(hours)+"小时";
						}else{
							currentMonthRestDays = removeZero(beforeDays);
						}
					}else{
						currentMonthRestDays = removeZero(beforeVacationHours)+"小时";
					}
				}

				//下午请假	
			}else if(DateUtil.after(vacationBeginDate, amEndDate)){
				vacationDateStr = vacationBeginDateStr+"下午";
				//请假时间为下午半天
				if(vacationHours>=pmWorkHours){
					vacationDays = "0.5";
					continuousRestDays = "0.5";
					beforeVacationHours += pmWorkHours;
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(hours>0){
						//保留一位小数，四舍五入
						hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
						currentMonthRestDays = removeZero(beforeDays)+"天"+removeZero(hours)+"小时";
					}else{
						currentMonthRestDays = removeZero(beforeDays);
					}
				}else{
					vacationDays = vacationHours+"小时";
					beforeVacationHours += vacationHours;
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(beforeDays>0){
						if(hours>0){
							//保留一位小数，四舍五入
							hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
							currentMonthRestDays = removeZero(beforeDays)+"天"+removeZero(hours)+"小时";
						}else{
							currentMonthRestDays = removeZero(beforeDays);
						}
					}else{
						currentMonthRestDays = removeZero(beforeVacationHours)+"小时";
					}
				}
				//一天假
				//此处做个修改，请假时间横跨上午和下午的上班时间， 按一天算的，现在按照小时来计算（除去休息时间）
				//1、请假开始时间在上午工作区间内
				//	1.1、请假结束时间在下午下班之后
				//  1.2、请假结束时间在下午下班之前
				//2、请假开始时间在上午上班之前
				//	2.1、请假结束时间在下午下班之后
				//  2.2、请假结束时间在下午下班之前
			}else{
				vacationDateStr = vacationBeginDateStr;
				//vacationDays = "1";
				//continuousRestDays = "1";
				vacationHours = 0;
				if(vacationBeginDate.before(amEndDate) && vacationBeginDate.after(amBeginDate)){
					if(DateUtil.after(vacationEndDate, pmEndDate)){
						time = pmEndDate.getTime() - vacationBeginDate.getTime() - breakTime;
						vacationHours = Math.ceil(time/(60*30*1000))/2;
					}else{
						time = vacationEndDate.getTime() - vacationBeginDate.getTime() - breakTime;
						vacationHours = Math.ceil(time/(60*30*1000))/2;
					}
					vacationDays = vacationHours+"小时";
					continuousRestDays = vacationHours+"小时";
				}else{
					if(DateUtil.after(vacationEndDate, pmEndDate)){
						vacationDays = "1";
						continuousRestDays = "1";
						vacationHours = dailyHours;
					}else{
						time = vacationEndDate.getTime() - amBeginDate.getTime() - breakTime;
						vacationHours = Math.ceil(time/(60*30*1000))/2;
						vacationDays = vacationHours+"小时";
						continuousRestDays = vacationHours+"小时";
					}
				}
				beforeVacationHours += vacationHours;
				double beforeDays = Math.floor(beforeVacationHours/dailyHours);
				double hours = beforeVacationHours - beforeDays*dailyHours;
				beforeDays += beforeVacationDays;
				if(hours>0){
					//保留一位小数，四舍五入
					hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
					if(beforeDays>0){
						currentMonthRestDays = removeZero(beforeDays)+"天"+removeZero(hours)+"小时";
					}else{
						currentMonthRestDays = removeZero(hours)+"小时";
					}
				}else{
					currentMonthRestDays = removeZero(beforeDays);
				}
			}
			//不在同一天
		}else{
			/* * 2、不在同一天（请假时间横跨上午和下午的上班时间， 按一天算）
			 *   2。1、开始时间在下午工作区间内
			 *   	2.1.1、结束时间在上午工作区间内
			 *   	2.1.2、结束时间为整天
			 *   2.2、开始时间为整天
			 *   	2.2。1、结束时间在上午工作区间内
			 *   	2.2.2、结束时间为整天
			 */
			//请假开始当天的下午下班时间
			Date firstDayPmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
			//请假开始当天的请假时间
			double firstDayVacationTime = firstDayPmEndDate.getTime() - vacationBeginDate.getTime();
			double firstDayVacationHours = Math.ceil(firstDayVacationTime/(60*30*1000))/2;
			//请假结束当天的上午上班时间
			Date lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
			//请假结束当天的请假时间
			double lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
			//请假结束时间在上午上班之前，往前推一天
			if(lastDayVacationTime<=0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(vacationEndDate);
				cal.add(Calendar.DATE, -1);
				vacationEndDate = DateUtil.getFullDate(DateUtil.formateDate(cal.getTime())+" "+pmEndTime+":00");
				//重新计算
				lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
				lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
				_pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
				vacationEndDateStr = sdf.format(vacationEndDate).replace("-", ".");
				//把日期前面的0去掉，如08.25
				if(vacationEndDateStr.startsWith("0")){
					vacationEndDateStr = vacationEndDateStr.substring(1, vacationEndDateStr.length());
				}
			}
			double lastDayVacationHours = Math.ceil(lastDayVacationTime/(60*30*1000))/2;
			//今天的日期
			Date currentDate = new Date();
			//当月第一天
			Date monthFirstDate = DateUtil.getSimpleDate(DateUtil.formateMonth(currentDate)+"-01");
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthFirstDate);
			cal.add(Calendar.DATE, -1);
			//上个月的最后一天
			Date lastMonthLastDate = cal.getTime();
			//上个月最后一天的下班时间
			Date lastMonthLastDayEndTime = DateUtil.getFullDate(DateUtil.formateDate(lastMonthLastDate)+" "+pmEndTime+":00");
			//当月开始的请假时间，用于计算月累计休息时间
			Date currentMonthVacationBeginDate = null;
			//请假开始时间从上个月开始
			if(DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
				currentMonthVacationBeginDate = DateUtil.getFullDate(DateUtil.formateDate(monthFirstDate)+" "+amBeginTime+":00");
			}else{
				currentMonthVacationBeginDate = vacationBeginDate;
			}
			//开始时间在下午工作区间内
			if(DateUtil.after(vacationBeginDate, amEndDate)){
				//请假距离今天的天数，请假的开始当天不计
				double _days = DateUtil.daysBetween(vacationBeginDate, currentDate);
				//请假距离今天的天数（请假若从上月开始，就从当月初开始算起）
				double days_ = DateUtil.daysBetween(currentMonthVacationBeginDate, currentDate);
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					vacationDateStr = vacationBeginDateStr+"下午-"+vacationEndDateStr+"上午";
					//请假的天数，请假的开始和结束的当天不计
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//开始下午半天
					if(firstDayVacationHours>=pmWorkHours){
						days += 0.5;
						//结束上午半天
						if(lastDayVacationHours>=amWorkHours){
							days += 0.5;
							vacationDays = removeZero(days);
							//请假的结束时间就是今天，请假开始的半天加上请假结束的半天，正好为一天
							if(DateUtils.isSameDay(currentDate, vacationEndDate)){
								continuousRestDays = removeZero(_days);
								if(DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours += amWorkHours;
								}
							}else{
								//加上请假开始的半天时间
								//_days += 0.5;//注释，修改为小时，精算累计请假天数
								continuousRestDays = removeZero((_days+0.5));//连休的天数，粗算，上午或下午即为半天
								if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours += pmWorkHours;
								}else{
									days_ += 1;
								}
							}
							double beforeDays = Math.floor(beforeVacationHours/dailyHours);
							double hours = beforeVacationHours - beforeDays*dailyHours;
							beforeDays += beforeVacationDays;
							if(hours>0){
								//保留一位小数，四舍五入
								hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
								currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
							}else{
								currentMonthRestDays = removeZero((beforeDays+days_));
							}

							//结束上午几个小时
						}else{
							vacationDays = removeZero(days)+"天"+removeZero(lastDayVacationHours)+"小时";
							//请假的结束时间就是今天
							if(DateUtils.isSameDay(currentDate, vacationEndDate)){
								//请假开始半天，请假结束当天（今天）只有几个小时，应减去半天，再加上小时数
								//_days -= 0.5;
								if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours = (beforeVacationHours - amWorkHours + lastDayVacationHours);
								}else{
									beforeVacationHours = (beforeVacationHours + lastDayVacationHours);
								}
								continuousRestDays = removeZero((_days-0.5))+"天"+removeZero(lastDayVacationHours)+"小时";
								double beforeDays = Math.floor(beforeVacationHours/dailyHours);
								double hours = beforeVacationHours - beforeDays*dailyHours;
								beforeDays += beforeVacationDays;
								if(hours>0){
									//保留一位小数，四舍五入
									hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
									currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
								}else{
									currentMonthRestDays = removeZero((beforeDays+days_));
								}
							}else{
								//加上请假开始的半天时间
								//_days += 0.5;
								if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours += pmWorkHours;
								}else{
									days_ += 1;
								}
								continuousRestDays = removeZero((_days+0.5));
								double beforeDays = Math.floor(beforeVacationHours/dailyHours);
								double hours = beforeVacationHours - beforeDays*dailyHours;
								beforeDays += beforeVacationDays;
								if(hours>0){
									//保留一位小数，四舍五入
									hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
									currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
								}else{
									currentMonthRestDays = removeZero((beforeDays+days_));
								}
							}
						}
						//开始下午几个小时
					}else{
						//结束上午半天
						if(lastDayVacationHours>=amWorkHours){
							days += 0.5;
							vacationDays = removeZero(days)+"天"+removeZero(firstDayVacationHours)+"小时";
							//请假的结束时间就是今天
							if(DateUtils.isSameDay(currentDate, vacationEndDate)){
								//请假结束半天，请假开始当天只有几个小时，应减去半天，再加上小时数
								//_days -= 0.5;
								if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours -= pmWorkHours;
								}else{
									beforeVacationHours += amWorkHours;
								}
								continuousRestDays = removeZero((_days-0.5))+"天"+removeZero(firstDayVacationHours)+"小时";
							}else{
								//加上请假开始当天的小时数
								continuousRestDays = removeZero(_days)+"天"+removeZero(firstDayVacationHours)+"小时";
								if(DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									days_ += 1;
								}
							}
							if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
								beforeVacationHours += firstDayVacationHours;
							}
							double beforeDays = Math.floor(beforeVacationHours/dailyHours);
							double hours = beforeVacationHours - beforeDays*dailyHours;
							beforeDays += beforeVacationDays;
							if(hours>0){
								//保留一位小数，四舍五入
								hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
								currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
							}else{
								currentMonthRestDays = removeZero((beforeDays+days_));
							}
							//结束上午几个小时
						}else{
							vacationDays = removeZero(days)+"天"+removeZero((firstDayVacationHours+lastDayVacationHours))+"小时";
							//请假的结束时间就是今天
							if(DateUtils.isSameDay(currentDate, vacationEndDate)){
								//请假结束几个小时，请假开始几个小时，应减去一天，再加上小时数
								_days -= 1;
								continuousRestDays = removeZero(_days)+"天"+removeZero((firstDayVacationHours+lastDayVacationHours))+"小时";
								if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									beforeVacationHours += (firstDayVacationHours+lastDayVacationHours);
								}else{
									beforeVacationHours += lastDayVacationHours;
								}
								double beforeDays = Math.floor(beforeVacationHours/dailyHours);
								double hours = beforeVacationHours - beforeDays*dailyHours;
								beforeDays += beforeVacationDays;
								currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
								if((beforeDays+days_)>0){
									if(hours>0){
										//保留一位小数，四舍五入
										hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
										currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
									}else{
										currentMonthRestDays = removeZero((beforeDays+days_));
									}
								}else{
									currentMonthRestDays = removeZero(beforeVacationHours)+"小时";
								}
							}else{
								if(DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
									days_ += 1;
								}else{
									beforeVacationHours += firstDayVacationHours;
								}
								//加上请假开始当天的小时数
								continuousRestDays = removeZero(_days)+"天"+removeZero(firstDayVacationHours)+"小时";
								double beforeDays = Math.floor(beforeVacationHours/dailyHours);
								double hours = beforeVacationHours - beforeDays*dailyHours;
								beforeDays += beforeVacationDays;
								if(hours>0){
									//保留一位小数，四舍五入
									hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
									currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
								}else{
									currentMonthRestDays = removeZero((beforeDays+days_));
								}
							}
						}
					}
					//结束时间为整天	
				}else{
					vacationDateStr = vacationBeginDateStr+"下午-"+vacationEndDateStr;
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate);
					//开始下午半天
					if(firstDayVacationHours>=pmWorkHours){
						days += 0.5;
						vacationDays = removeZero(days);
						//_days += 0.5;
						if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
							beforeVacationHours += pmWorkHours;
						}else{
							days_ += 1;
						}
						continuousRestDays = removeZero((_days+0.5));
						//开始下午几个小时
					}else{
						vacationDays = removeZero(days)+"天"+removeZero(firstDayVacationHours)+"小时";
						continuousRestDays = removeZero(_days)+"天"+removeZero(firstDayVacationHours)+"小时";
						if(!DateUtil.before(vacationBeginDate, lastMonthLastDayEndTime)){
							beforeVacationHours += firstDayVacationHours;
						}else{
							days_ += 1;
						}
					}
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(hours>0){
						//保留一位小数，四舍五入
						hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
						currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
					}else{
						currentMonthRestDays = removeZero((beforeDays+days_));
					}
				}
				//开始时间为整天
			}else{
				//请假距离今天的天数，今天不计
				double _days = DateUtil.daysBetween(vacationBeginDate, currentDate);
				//考虑到请假可能从上月开始
				double days_ = DateUtil.daysBetween(currentMonthVacationBeginDate, currentDate);
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					vacationDateStr = vacationBeginDateStr+"-"+vacationEndDateStr+"上午";
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate);
					//结束上午半天
					if(lastDayVacationHours>=amWorkHours){
						days += 0.5;
						vacationDays = removeZero(days);
						//请假的结束时间就是今天
						if(DateUtils.isSameDay(currentDate, vacationEndDate)){
							//加上请假结束的半天
							//_days += 0.5;
							beforeVacationHours += amWorkHours;
							continuousRestDays = removeZero((_days+0.5));
						}else{
							//加上今天一天
							_days += 1;
							days_ += 1;
							continuousRestDays = removeZero(_days);
						}
						double beforeDays = Math.floor(beforeVacationHours/dailyHours);
						double hours = beforeVacationHours - beforeDays*dailyHours;
						beforeDays += beforeVacationDays;
						if(hours>0){
							//保留一位小数，四舍五入
							hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
							currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
						}else{
							currentMonthRestDays = removeZero((beforeDays+days_));
						}
						//结束上午几个小时	
					}else{
						vacationDays = removeZero(days)+"天"+removeZero(lastDayVacationHours)+"小时";
						//请假的结束时间就是今天
						if(DateUtils.isSameDay(currentDate, vacationEndDate)){
							//加上请假结束当天的几个小时
							continuousRestDays = removeZero(_days)+"天"+removeZero(lastDayVacationHours)+"小时";
							beforeVacationHours += lastDayVacationHours;
						}else{
							//加上今天一天
							_days += 1;
							days_ += 1;
							continuousRestDays = removeZero(_days);
						}
						double beforeDays = Math.floor(beforeVacationHours/dailyHours);
						double hours = beforeVacationHours - beforeDays*dailyHours;
						beforeDays += beforeVacationDays;
						if(hours>0){
							//保留一位小数，四舍五入
							hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
							currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
						}else{
							currentMonthRestDays = removeZero((beforeDays+days_));
						}
					}
					//结束时间为整天	
				}else{
					vacationDateStr = vacationBeginDateStr+"-"+vacationEndDateStr;
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)+1;
					vacationDays = removeZero(days);
					//加上今天一天
					_days += 1;
					days_ += 1;
					continuousRestDays = removeZero(_days);
					double beforeDays = Math.floor(beforeVacationHours/dailyHours);
					double hours = beforeVacationHours - beforeDays*dailyHours;
					beforeDays += beforeVacationDays;
					if(hours>0){
						//保留一位小数，四舍五入
						hours =  new BigDecimal(hours).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();  
						currentMonthRestDays = removeZero((beforeDays+days_))+"天"+removeZero(hours)+"小时";
					}else{
						currentMonthRestDays = removeZero((beforeDays+days_));
					}
				}
			}
		}
		String[] values = {vacationDateStr, vacationDays, continuousRestDays, currentMonthRestDays};
		return values;
	}
	/**
	 * 获取人员的当月请假小时数（除了当日所在的请假以及提前的请假）
	 * @param vacationId
	 * @return
	 * @throws Exception 
	 */
	public double[]  getBeforeVacationDays(String vacationId, String userId) throws Exception{
		String sql = "SELECT\n" +
				"	Hours,BeginDate, dailyHours\n" +
				"FROM\n" +
				"	OA_Vacation\n" +
				"WHERE\n" +
				"	IFNULL(ProcessStatus, 0) != 31 AND IFNULL(ProcessStatus, 0) != 2\n" +
				"AND IsDeleted = 0\n" +
				//"AND RequestUserID = '"+userId+"'\n" +
				"AND locate('"+userId+"', RequestUserID)>0\n" +
				"AND MONTH (CURRENT_DATE()) = MONTH (EndDate) AND YEAR(CURRENT_DATE()) = YEAR(EndDate) AND VacationID!="+vacationId+
				" AND DATE(BeginDate)<=DATE(CURRENT_DATE())";
		List<Object> objList = baseDao.findBySql(sql);
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		if(null==groups || groups.size()<1){
			return new double[]{0,0};
		}
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			//总部优先
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		String companyId = group.getType().split("_")[0];
		String departmentId = group.getType().split("_")[1];
		//上个月的请假时间
		//double lastMonthVacationHours = 0;
		//折算成天数结余的请假小时数
		double vacationHours = 0;
		//请假天数
		double days = 0;
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			double dailyHour = (double)objs[2];
			double hour = null==objs[0] ? 0.0:(double)objs[0];
			Date beginDate = (Date)objs[1];
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId,
					departmentId, DateUtil.formateDate(beginDate));
			if(null == workTimeArray){
				continue;
			}
			Date now = new Date();
			//当月第一天
			Date monthFirstDate = DateUtil.getSimpleDate(DateUtil.formateMonth(now)+"-01");
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthFirstDate);
			cal.add(Calendar.DATE, -1);
			//上个月的最后一天
			Date lastMonthLastDate = cal.getTime();
			//上个月最后一天的下班时间
			Date lastMonthLastDayEndTime = DateUtil.getFullDate(DateUtil.formateDate(lastMonthLastDate)+" "+workTimeArray[3]+":00");
			//请假开始时间从上个月开始
			if(DateUtil.before(beginDate, lastMonthLastDayEndTime)){
				String[] vacationTextAndHours = getVacationTextAndHoursForHR(userId,
						DateUtil.formateFullDate(beginDate), DateUtil.formateFullDate(lastMonthLastDayEndTime));
				if(null!=vacationTextAndHours && vacationTextAndHours.length==2){
					//lastMonthVacationHours += Double.parseDouble(vacationTextAndHours[1]);
					hour -= Double.parseDouble(vacationTextAndHours[1]);
				}
			}
			//加上上一个请假折算天数剩余的小时数
			hour += vacationHours;
			vacationHours = 0;
			double remainHours = hour%dailyHour;
			double vacationDays = (hour-remainHours)/dailyHour;
			days += vacationDays;
			vacationHours += remainHours;
		}
		
		return new double[]{days, vacationHours};
	}
	private String removeZero(double num){
		String _num = num+"";
		if(_num.endsWith(".0")){
			return _num.split("\\.0")[0];
		}
		return _num;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream exportVacationDetail(List<VacationDetailVo> vacationDetailVos) throws Exception {
		String[] colTitles = {"序号","部门","姓名","请假日期","请假天数","已连休天数","当月休天数","是否正常请假","OA审批人","备注"};
		//创建一个空excel文件
		Workbook workbook = new HSSFWorkbook();
		Sheet worksheet = workbook.createSheet("请假明细"); 
		// 设置列宽    
		worksheet.setColumnWidth(0, 2000);    
		worksheet.setColumnWidth(1, 6500);    
		worksheet.setColumnWidth(2, 3500);    
		worksheet.setColumnWidth(3, 6500);    
		worksheet.setColumnWidth(4, 3500);    
		worksheet.setColumnWidth(5, 3500);    
		worksheet.setColumnWidth(6, 3500);    
		worksheet.setColumnWidth(7, 3500);
		worksheet.setColumnWidth(8, 3500);
		worksheet.setColumnWidth(9, 10000);
		//列头字体
		Font columnHeadFont = workbook.createFont();    
		columnHeadFont.setFontName("宋体");    
		columnHeadFont.setFontHeightInPoints((short) 10);    
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		//列表样式
		CellStyle columnHeadStyle = workbook.createCellStyle(); 
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index); 
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中   
		columnHeadStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		columnHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		columnHeadStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
		//普通单元格样式
		CellStyle gridStyle = workbook.createCellStyle();
		gridStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		gridStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		gridStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		gridStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
		gridStyle.setWrapText(true);// 自动换行 
		//写列名
		Row rowColTiTle = worksheet.createRow(0);
		int index = 0;
		//写入列名
		for(String colTitle: colTitles){
			Cell cell = rowColTiTle.createCell(index);
			cell.setCellValue(colTitle);
			cell.setCellStyle(columnHeadStyle);
			index++;
		}
		Map<String, List<VacationDetailVo>> vacationDetailVoMap = filterVacationByDep(vacationDetailVos);
		//写入请假明细
		Iterator<Entry<String, List<VacationDetailVo>>> ite = vacationDetailVoMap.entrySet().iterator();
		//内容从第二行开始
		int rowNum = 1;
		//请假序号
		int vacationIndex = 1;
		while(ite.hasNext()){
			Entry<String, List<VacationDetailVo>> entry = ite.next();
			List<VacationDetailVo> vacationDetailVoList = entry.getValue();
			for(VacationDetailVo vacationDetailVo: vacationDetailVoList){
				Row row = worksheet.createRow(rowNum);
				//序号
				Cell cellNO = row.createCell(0);
				cellNO.setCellValue(vacationIndex);
				cellNO.setCellStyle(gridStyle);
				//部门
				Cell cellDep = row.createCell(1);
				cellDep.setCellValue(vacationDetailVo.getDepartmentName());
				cellDep.setCellStyle(gridStyle);
				//姓名
				Cell cellName = row.createCell(2);
				cellName.setCellValue(vacationDetailVo.getStaffName());
				cellName.setCellStyle(gridStyle);
				//请假日期
				Cell cellDate = row.createCell(3);
				cellDate.setCellValue(vacationDetailVo.getVacationDate());
				cellDate.setCellStyle(gridStyle);
				//请假天数
				Cell cellDay = row.createCell(4);
				cellDay.setCellValue(vacationDetailVo.getVacationDays());
				cellDay.setCellStyle(gridStyle);
				//已连休天数
				Cell cellContinue = row.createCell(5);
				cellContinue.setCellValue(vacationDetailVo.getContinuousRestDays());
				cellContinue.setCellStyle(gridStyle);
				//空的情况，就合并单元格
				if(StringUtils.isBlank(vacationDetailVo.getContinuousRestDays())){
					worksheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));
				}
				//当月休天数
				Cell cellMonth = row.createCell(6);
				cellMonth.setCellValue(vacationDetailVo.getCurrentMonthRestDays());
				cellMonth.setCellStyle(gridStyle);
				//是否正常请假
				Cell cellNormal = row.createCell(7);
				cellNormal.setCellValue("是");
				cellNormal.setCellStyle(gridStyle);
				//OA审批人
				Cell cellAudit = row.createCell(8);
				cellAudit.setCellValue(vacationDetailVo.getAuditor());
				cellAudit.setCellStyle(gridStyle);
				//备注
				Cell cellMark = row.createCell(9);
				cellMark.setCellValue(vacationDetailVo.getReason());
				cellMark.setCellStyle(gridStyle);
				rowNum++;
				vacationIndex++;
			}
			//同一个部门的请假人数
			int sameDepVacationNums = vacationDetailVoList.size();
			//相同部门，合并单元格
			if(sameDepVacationNums>1){
				worksheet.addMergedRegion(new CellRangeAddress((rowNum-sameDepVacationNums), (rowNum-1), 1, 1));
			}
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
	/**
	 * 按部门筛选请假信息
	 * @param vacationDetailVos
	 * @return
	 */
	public Map<String, List<VacationDetailVo>> filterVacationByDep(List<VacationDetailVo> vacationDetailVos){
		Map<String, List<VacationDetailVo>> vacationDetailVoMap = new HashMap<>();
		for(VacationDetailVo vacationDetailVo: vacationDetailVos){
			String depId = vacationDetailVo.getDepartmentId();
			if(vacationDetailVoMap.containsKey(depId)){
				vacationDetailVoMap.get(depId).add(vacationDetailVo);
			}else{
				List<VacationDetailVo> vacationDetailVoList = new ArrayList<>();
				vacationDetailVoList.add(vacationDetailVo);
				vacationDetailVoMap.put(depId, vacationDetailVoList);
			}
		}
		return vacationDetailVoMap;
	}

	@SuppressWarnings("static-access")
	@Override
	public String[] getVacationTextAndHours(String userId, String beginTime, String endTime) throws Exception {
		//请假天数
		String vacationDayStr = "";
		//请假的小时数
		double showHours = 0;
		String departmentId = "";
		String companyId = "";
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		if(null!=groups && groups.size()>0){
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				//总部优先
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			companyId = group.getType().split("_")[0];
			departmentId = group.getType().split("_")[1];
		}
		//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyId));
		//String workTimes = companyID.getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		Date vacationBeginDate = DateUtil.getFullDate(beginTime);
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(new Date()));
		//作息时间
		String amBeginTime = workTimeArray[0];
		String amEndTime = workTimeArray[1];
		String pmBeginTime = workTimeArray[2];
		String pmEndTime = workTimeArray[3];
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(vacationBeginDate); 
		calendar.add(calendar.DATE,1);
		//第二天上班的开始时间
		Date secondDayBeginTime = DateUtil.getFullDate(DateUtil.formateDate(calendar.getTime())+" "+amBeginTime+":00");
		Date vacationEndDate = DateUtil.getFullDate(endTime);
		//请假当天的上午上班时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amBeginTime+":00");
		//请假当天的上午下班时间
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amEndTime+":00");
		//请假当天的下午上班时间
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmBeginTime+":00");
		//请假当天的下午下班时间
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
		//请假结束的上午上班时间
		Date _amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
		//请假结束的下午上班时间
		Date _pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
		Date _pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmEndTime+":00");
		//上午的工作时间
		double amWorkTime = amEndDate.getTime() - amBeginDate.getTime();
		double amWorkHours = Math.ceil(amWorkTime/(60*30*1000))/2;
		//下午的工作时间
		double pmWorkTime = pmEndDate.getTime() - pmBeginDate.getTime();
		double pmWorkHours = Math.ceil(pmWorkTime/(60*30*1000))/2;
		//每天的工作时长
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(Integer.parseInt(companyId)));
		double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(new Date()));
		/* * 1、同一天（只考虑一般的请假情况（上午请假，下午请假，一天假），请假时间横跨上午和下午的上班时间， 按一天算）
		 *   1.1、上午请假，请假结束时间-请假开始时间，不足半天（小于上午作息时间），按小时计（半小时起算）
		 *   1.2 、下午请假，请假结束时间-请假开始时间，不足半天（小于下午作息时间），按小时计（半小时起算）
		 *   1.3 、一天假
		 */
		int companyID = Integer.parseInt(companyId);
		//结束时间小于等于第二天的上班时间，由此判断请假时间在同一天内
		if(DateUtil.before(vacationEndDate, secondDayBeginTime)){
			//请假的时间
			double time = vacationEndDate.getTime() - vacationBeginDate.getTime();
			//按半小时起算，向上取值
			double vacationHours = Math.ceil(time/(60*30*1000))/2;
			//上午请假
			if(DateUtil.before(vacationEndDate, pmBeginDate)){
				//只有总部允许请小时假，其他地方小时假按半天算
				if(!"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
					showHours = amWorkHours;
				}else if(vacationHours>=amWorkHours){
					showHours = amWorkHours;
				}
				else{
					showHours = vacationHours;
				}

				//下午请假	
			}else if(DateUtil.after(vacationBeginDate, amEndDate)){
				//请假时间为下午半天
				if(!"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
					showHours = pmWorkHours;
				}else if(vacationHours>=pmWorkHours){
					showHours = pmWorkHours;
				}
				else{
					showHours = vacationHours;
				}
				//总部
			}else if("骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
				//上午请假时间在上班之前
				if(DateUtil.before(vacationBeginDate, amBeginDate)){
					vacationBeginDate = amBeginDate;
				}
				//下午请假时间在下班之后
				if(DateUtil.after(vacationEndDate, pmEndDate)){
					vacationEndDate = pmEndDate;
				}
				time = vacationEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime());
				//按半小时起算，向上取值
				vacationHours = Math.ceil(time/(60*30*1000))/2;
				showHours = vacationHours;
			}
			//一天假	
			else{
				showHours = dailyHours;
			}
			//不在同一天
		}else{
			/* * 2、不在同一天
			 *   2。1、开始时间在下午工作区间内
			 *   	2.1.1、结束时间在上午工作区间内
			 *   	2.1.2、结束时间为整天
			 *   2.2、开始时间在上午工作区间内
			 *   	2.2.1、总部
			 *   	2.2.1.1、结束时间在上午工作区间内
			 *   	2.2.1.2、结束时间为下午工作区间内
			 *   
			 *   	2.2.2、非总部 -> 开始时间视为整天
			 *   	2.2.2.1、结束时间在上午工作区间内
			 *   	2.2.2.2、结束时间为整天
			 */
			//请假开始当天的下午下班时间
			Date firstDayPmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
			//开始的请假时间在当天下班后
			if(DateUtil.after(vacationBeginDate, firstDayPmEndDate)){
				vacationBeginDate = firstDayPmEndDate;
			}
			//请假开始当天的请假时间
			double firstDayVacationTime = firstDayPmEndDate.getTime() - vacationBeginDate.getTime();
			double firstDayVacationHours = Math.ceil(firstDayVacationTime/(60*30*1000))/2;
			//请假结束当天的上午上班时间
			Date lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
			//请假结束当天的请假时间
			double lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
			//请假结束时间在上午上班之前，往前推一天
			if(lastDayVacationTime<=0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(vacationEndDate);
				cal.add(Calendar.DATE, -1);
				vacationEndDate = DateUtil.getFullDate(DateUtil.formateDate(cal.getTime())+" "+pmEndTime+":00");
				//重新计算
				lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
				lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
				_amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
				_pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
				_pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmEndTime+":00");
			}
			double lastDayVacationHours = Math.ceil(lastDayVacationTime/(60*30*1000))/2;
			//开始时间在下午工作区间内
			if(DateUtil.after(vacationBeginDate, amEndDate)){
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					//请假的天数，请假的开始和结束的当天不计
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//开始下午半天/非总部
					if(firstDayVacationHours>=pmWorkHours || !"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
						showHours += pmWorkHours;
						//总部结束上午半天/非总部
						if(lastDayVacationHours>=amWorkHours || !"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
							showHours += amWorkHours;
							showHours += days*dailyHours;
							//结束上午几个小时
						}else if("骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){

							showHours += days*dailyHours+lastDayVacationHours;
						}
						//开始下午几个小时
					}else{
						//结束上午半天
						if(lastDayVacationHours>=amWorkHours){
							showHours += amWorkHours;
							showHours += days*dailyHours+firstDayVacationHours;
							//结束上午几个小时
						}else{
							showHours = days*dailyHours+firstDayVacationHours+lastDayVacationHours;
						}
					}
					//结束时间为整天（非总部）
				}else if(!"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate);
					showHours += days*dailyHours+pmWorkHours;
				}else{
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假结束时间在下午下班之后
					if(DateUtil.after(vacationEndDate, _pmEndDate)){
						vacationEndDate = _pmEndDate;
					}
					//结束当天的请假小时数
					double endDayHours = 			
							Math.ceil(((double)vacationEndDate.getTime() - _amBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					showHours += endDayHours;
					//开始下午半天
					if(firstDayVacationHours>=pmWorkHours){
						showHours += days*dailyHours+pmWorkHours;
						//开始下午几个小时
					}else{
						showHours += days*dailyHours+firstDayVacationHours;
					}
				}
				//开始时间为整天（非总部，不可请小时假）
			}else if(!"骑岸".equals(CompanyIDEnum.valueOf(companyID).getName())){
				//结束时间在上午工作区间内，按半天处理
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate);
					showHours += days*dailyHours+amWorkHours;
					//结束时间为整天	
				}else{
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)+1;
					showHours += days*dailyHours;
				}
			}
			//开始时间在上午工作区间内（总部）
			else{
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假开始时间在上班之前
					if(DateUtil.before(vacationBeginDate, amBeginDate)){
						vacationBeginDate = amBeginDate;
					}
					//请假当天的请假小时数
					showHours +=  Math.ceil(((double)pmEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					//结束上午半天
					if(lastDayVacationHours>=amWorkHours){
						showHours += days*dailyHours + amWorkHours;
						//结束上午几个小时	
					}else{
						showHours +=  days*dailyHours + lastDayVacationHours;
					}
					//结束时间在下午工作区间内
				}else{
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假开始时间在上午上班之前
					if(DateUtil.before(vacationBeginDate, amBeginDate)){
						vacationBeginDate = amBeginDate;
					}
					//请假结束时间在下午下班之后
					if(DateUtil.after(vacationEndDate, _pmEndDate)){
						vacationEndDate = _pmEndDate;
					}
					//请假当天的请假小时数
					showHours += Math.ceil(((double)pmEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					//结束当天的请假小时数
					double endDayHours = Math.ceil(((double)vacationEndDate.getTime() - _amBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					showHours += days*dailyHours+endDayHours;
				}
			}
		}
		double remainHours = showHours%dailyHours;
		double vacationDays = (showHours-remainHours)/dailyHours;
		if(vacationDays>0){
			if(remainHours>0){
				vacationDayStr += (removeZero(vacationDays)+"天"+removeZero(remainHours)+"小时");
			}else{
				vacationDayStr += removeZero(vacationDays)+"天";
			}
		}else{
			vacationDayStr += removeZero(remainHours)+"小时";
		}
		return new String[]{vacationDayStr, showHours+""};
	}

	@SuppressWarnings("static-access")
	@Override
	public String[] getVacationTextAndHoursForHR(String userId, String beginTime, String endTime) throws Exception {
		//请假天数
		String vacationDayStr = "";
		//请假的小时数
		double showHours = 0;
		String departmentId = "";
		String companyId = "";
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		if(null!=groups && groups.size()>0){
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				//总部优先
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			companyId = group.getType().split("_")[0];
			departmentId = group.getType().split("_")[1];
		}
		//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyId));
		//String workTimes = companyID.getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		Date vacationBeginDate = DateUtil.getFullDate(beginTime);
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(vacationBeginDate));
		//作息时间
		String amBeginTime = workTimeArray[0];
		String amEndTime = workTimeArray[1];
		String pmBeginTime = workTimeArray[2];
		String pmEndTime = workTimeArray[3];
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(vacationBeginDate); 
		calendar.add(calendar.DATE,1);
		//第二天上班的开始时间
		Date secondDayBeginTime = DateUtil.getFullDate(DateUtil.formateDate(calendar.getTime())+" "+amBeginTime+":00");
		Date vacationEndDate = DateUtil.getFullDate(endTime);
		//请假当天的上午上班时间
		Date amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amBeginTime+":00");
		//请假当天的上午下班时间
		Date amEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+amEndTime+":00");
		//请假当天的下午上班时间
		Date pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmBeginTime+":00");
		//请假当天的下午下班时间
		Date pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
		//请假结束的上午上班时间
		Date _amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
		//请假结束的下午上班时间
		Date _pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
		Date _pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmEndTime+":00");
		//上午的工作时间
		double amWorkTime = amEndDate.getTime() - amBeginDate.getTime();
		double amWorkHours = Math.ceil(amWorkTime/(60*30*1000))/2;
		//下午的工作时间
		double pmWorkTime = pmEndDate.getTime() - pmBeginDate.getTime();
		double pmWorkHours = Math.ceil(pmWorkTime/(60*30*1000))/2;
		//每天的工作时长
		//double dailyHours = positionService.getDailyHoursByCompanyID(CompanyIDEnum.valueOf(Integer.parseInt(companyId)));
		double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyId, departmentId, DateUtil.formateDate(vacationBeginDate));
		/* * 1、同一天（只考虑一般的请假情况（上午请假，下午请假，一天假），请假时间横跨上午和下午的上班时间， 按一天算）
		 *   1.1、上午请假，请假结束时间-请假开始时间，不足半天（小于上午作息时间），按小时计（半小时起算）
		 *   1.2 、下午请假，请假结束时间-请假开始时间，不足半天（小于下午作息时间），按小时计（半小时起算）
		 *   1.3 、一天假
		 */
		//结束时间小于等于第二天的上班时间，由此判断请假时间在同一天内
		if(DateUtil.before(vacationEndDate, secondDayBeginTime)){
			//请假的时间
			double time = vacationEndDate.getTime() - vacationBeginDate.getTime();
			//按半小时起算，向上取值
			double vacationHours = Math.ceil(time/(60*30*1000))/2;
			//上午请假
			if(DateUtil.before(vacationEndDate, pmBeginDate)){
				if(vacationHours>=amWorkHours){
					showHours = amWorkHours;
				}
				else{
					showHours = vacationHours;
				}

				//下午请假	
			}else if(DateUtil.after(vacationBeginDate, amEndDate)){
				//请假时间为下午半天
				if(vacationHours>=pmWorkHours){
					showHours = pmWorkHours;
				}
				else{
					showHours = vacationHours;
				}
			}else{
				//上午请假时间在上班之前
				if(DateUtil.before(vacationBeginDate, amBeginDate)){
					vacationBeginDate = amBeginDate;
				}
				//下午请假时间在下班之后
				if(DateUtil.after(vacationEndDate, pmEndDate)){
					vacationEndDate = pmEndDate;
				}
				time = vacationEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime());
				//按半小时起算，向上取值
				vacationHours = Math.ceil(time/(60*30*1000))/2;
				showHours = vacationHours;
			}
			//不在同一天
		}else{
			/* * 2、不在同一天
			 *   2。1、开始时间在下午工作区间内
			 *   	2.1.1、结束时间在上午工作区间内
			 *   	2.1.2、结束时间为整天
			 *   2.2、开始时间在上午工作区间内
			 *   	2.2.1、总部
			 *   	2.2.1.1、结束时间在上午工作区间内
			 *   	2.2.1.2、结束时间为下午工作区间内
			 *   
			 *   	2.2.2、非总部 -> 开始时间视为整天
			 *   	2.2.2.1、结束时间在上午工作区间内
			 *   	2.2.2.2、结束时间为整天
			 */
			//请假开始当天的下午下班时间
			Date firstDayPmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+pmEndTime+":00");
			//开始的请假时间在当天下班后
			if(DateUtil.after(vacationBeginDate, firstDayPmEndDate)){
				vacationBeginDate = firstDayPmEndDate;
			}
			//请假开始当天的请假时间
			double firstDayVacationTime = firstDayPmEndDate.getTime() - vacationBeginDate.getTime();
			double firstDayVacationHours = Math.ceil(firstDayVacationTime/(60*30*1000))/2;
			//请假结束当天的上午上班时间
			Date lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
			//请假结束当天的请假时间
			double lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
			//请假结束时间在上午上班之前，往前推一天
			if(lastDayVacationTime<=0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(vacationEndDate);
				cal.add(Calendar.DATE, -1);
				vacationEndDate = DateUtil.getFullDate(DateUtil.formateDate(cal.getTime())+" "+pmEndTime+":00");
				//重新计算
				lastDayAmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
				lastDayVacationTime = vacationEndDate.getTime() - lastDayAmBeginDate.getTime();
				_amBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+amBeginTime+":00");
				_pmBeginDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmBeginTime+":00");
				_pmEndDate = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+pmEndTime+":00");
			}
			double lastDayVacationHours = Math.ceil(lastDayVacationTime/(60*30*1000))/2;
			//开始时间在下午工作区间内
			if(DateUtil.after(vacationBeginDate, amEndDate)){
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					//请假的天数，请假的开始和结束的当天不计
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//开始下午半天
					if(firstDayVacationHours>=pmWorkHours){
						showHours += pmWorkHours;
						//总部结束上午半天
						if(lastDayVacationHours>=amWorkHours){
							showHours += amWorkHours;
							showHours += days*dailyHours;
							//结束上午几个小时
						}else{
							showHours += days*dailyHours+lastDayVacationHours;
						}
						//开始下午几个小时
					}else{
						//结束上午半天
						if(lastDayVacationHours>=amWorkHours){
							showHours += amWorkHours;
							showHours += days*dailyHours+firstDayVacationHours;
							//结束上午几个小时
						}else{
							showHours = days*dailyHours+firstDayVacationHours+lastDayVacationHours;
						}
					}
					//结束时间在下午工作区间内
				}else{
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假结束时间在下午下班之后
					if(DateUtil.after(vacationEndDate, _pmEndDate)){
						vacationEndDate = _pmEndDate;
					}
					//结束当天的请假小时数
					double endDayHours = 			
							Math.ceil(((double)vacationEndDate.getTime() - _amBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					showHours += endDayHours;
					//开始下午半天
					if(firstDayVacationHours>=pmWorkHours){
						showHours += days*dailyHours+pmWorkHours;
						//开始下午几个小时
					}else{
						showHours += days*dailyHours+firstDayVacationHours;
					}
				}
			}
			//开始时间在上午工作区间内
			else{
				//结束时间在上午工作区间内
				if(DateUtil.before(vacationEndDate, _pmBeginDate)){
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假开始时间在上班之前
					if(DateUtil.before(vacationBeginDate, amBeginDate)){
						vacationBeginDate = amBeginDate;
					}
					//请假当天的请假小时数
					showHours +=  Math.ceil(((double)pmEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					//结束上午半天
					if(lastDayVacationHours>=amWorkHours){
						showHours += days*dailyHours + amWorkHours;
						//结束上午几个小时	
					}else{
						showHours +=  days*dailyHours + lastDayVacationHours;
					}
					//结束时间在下午工作区间内
				}else{
					double days = DateUtil.daysBetween(vacationBeginDate, vacationEndDate)-1;
					//请假开始时间在上午上班之前
					if(DateUtil.before(vacationBeginDate, amBeginDate)){
						vacationBeginDate = amBeginDate;
					}
					//请假结束时间在下午下班之后
					if(DateUtil.after(vacationEndDate, _pmEndDate)){
						vacationEndDate = _pmEndDate;
					}
					//请假当天的请假小时数
					showHours += Math.ceil(((double)pmEndDate.getTime() - vacationBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					//结束当天的请假小时数
					double endDayHours = Math.ceil(((double)vacationEndDate.getTime() - _amBeginDate.getTime() - (pmBeginDate.getTime()-amEndDate.getTime()))/(60*30*1000))/2;
					showHours += days*dailyHours+endDayHours;
				}
			}
		}
		double remainHours = showHours%dailyHours;
		double vacationDays = (showHours-remainHours)/dailyHours;
		if(vacationDays>0){
			if(remainHours>0){
				vacationDayStr += (removeZero(vacationDays)+"天"+removeZero(remainHours)+"小时");
			}else{
				vacationDayStr += removeZero(vacationDays)+"天";
			}
		}else{
			vacationDayStr += removeZero(remainHours)+"小时";
		}
		return new String[]{vacationDayStr, showHours+""};
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VacationEntity> getUnderlingVacationVos(String vacationDate,
			List<Integer> departmentIds, String userId) throws Exception {
		//所有下属
		Set<String> allUnderlings = new HashSet<>();
		for(int departmentId: departmentIds){
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(departmentId);
			if(null==department){
				continue;
			}
			staffService.getDepartmentAllStaffs(department.getCompanyID(), departmentId, allUnderlings);
		}
		String hql = "";
		if(StringUtils.isNotBlank(vacationDate)){
			hql = "SELECT vacation FROM\n" +
					"	VacationEntity vacation, StaffEntity staff\n" +
					"WHERE\n" +
					"	IFNULL(processStatus, 0) != 31\n" +
					"AND IFNULL(processStatus, 0) != 2\n" +
					"AND vacation.isDeleted = 0\n" +
					"AND staff.isDeleted=0 \n" +
					"AND staff.status!=4\n" +
					"AND '"+vacationDate+"' >= DATE(beginDate)\n" +
					"AND '"+vacationDate+"' <= DATE(endDate)\n" +
					"AND requestUserID in("+listToSqlInStr(allUnderlings)+")\n" +
					"AND vacation.requestUserID = staff.userID\n";
		}else{
			hql = "SELECT vacation FROM\n" +
					"	VacationEntity vacation, StaffEntity staff\n" +
					"WHERE\n" +
					"	IFNULL(processStatus, 0) != 31\n" +
					"AND IFNULL(processStatus, 0) != 2\n" +
					"AND vacation.isDeleted = 0\n" +
					"AND staff.isDeleted=0 \n" +
					"AND staff.status!=4\n" +
					"AND CURRENT_DATE () >= DATE(beginDate)\n" +
					"AND CURRENT_DATE () <= DATE(endDate)\n" +
					"AND requestUserID in("+listToSqlInStr(allUnderlings)+")\n" +
					"AND vacation.requestUserID = staff.userID\n";
		}
		/*if(StringUtils.isNotBlank(staffName)){
			hql += "AND staff.staffName LIKE '%"+staffName+"%'";
		}*/
		Object obj = baseDao.hqlfind(hql);
		if(null==obj){
			return new ArrayList<VacationEntity>();
		}
		List<VacationEntity> vacations = (List<VacationEntity>) obj;
		Iterator<VacationEntity> ite = vacations.iterator();
		//根据请假时间过滤掉不符合的请假
		while(ite.hasNext()){
			VacationEntity vacation = ite.next();
			Date beginVacationTime = vacation.getBeginDate();
			Date endVacationTime = vacation.getEndDate();
			String vacationUserId = vacation.getRequestUserID();
			List<Group> groups = identityService.createGroupQuery().groupMember(vacationUserId).list();
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
			String[] workTimeArr = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateFullDate(beginVacationTime));
			String beginTime = workTimeArr[0];
			String endTime = workTimeArr[3];
			if(null == vacationDate){
				vacationDate = DateUtil.formateDate(new Date());
			}
			Date beginDate = DateUtil.getFullDate(vacationDate+" "+beginTime+":00");
			Date endDate = DateUtil.getFullDate(vacationDate+" "+endTime+":00");
			//过滤掉自己的请假
			if(userId.equals(vacation.getRequestUserID())){
				ite.remove();
				continue;
			}
			//请假开始时间在当日下班后，不算当日请假
			if(DateUtil.after(beginVacationTime, endDate)){
				ite.remove();
				continue;
			}
			////请假结束时间在当日上班前，不算当日请假
			if(DateUtil.before(endVacationTime, beginDate)){
				ite.remove();
				continue;
			}
			vacation.setVacationUserName(staffService.getRealNameByUserId(vacation.getRequestUserID()));
			vacation.setWorkAgentName(staffService.getRealNameByUserId(vacation.getAgentID()));
		}
		return vacations;
	}
	private String listToSqlInStr(Set<String> strs) { 
		StringBuffer sb = new StringBuffer(); 
		for(String str: strs){
			sb.append("'").append(str).append("'").append(","); 
		}
		if(sb.length()>0){
			return sb.toString().substring(0, sb.length() - 1); 
		}else{
			return "''";
		}
	}
	/**
	 * 检查是否包含小时假
	 * @throws Exception 
	 */
	@Override
	public boolean checkVacation(VacationVO vacationVO) throws Exception {
		List<Group> groups = identityService.createGroupQuery().groupMember(vacationVO.getRequestUserID()).list();
		String companyID = groups.get(0).getType().split("_")[0];
		//总部可以请小时假
		if(CompanyIDEnum.QIAN.getValue() == Integer.parseInt(companyID)){
			return false;
		}
		String departmentId = groups.get(0).getType().split("_")[1];
		String beginDate = vacationVO.getBeginDate();
		String endDate = vacationVO.getEndDate();
		String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID, departmentId, beginDate);

		Date vacationBeginDate = DateUtil.getFullDate(beginDate);
		Date vacationAmBeginTime = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+workTimeArray[0]+":00");
		Date vacationAmEndTime = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+workTimeArray[1]+":00");
		Date vacationPmBeginTime = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+workTimeArray[2]+":00");
		Date vacationPmEndTime = DateUtil.getFullDate(DateUtil.formateDate(vacationBeginDate)+" "+workTimeArray[3]+":00");

		Date vacationEndDate = DateUtil.getFullDate(endDate);
		Date _vacationAmENTime = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+workTimeArray[0]+":00");
		Date _vacationAmEndTime = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+workTimeArray[1]+":00");
		Date _vacationPmBeginTime = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+workTimeArray[2]+":00");
		Date _vacationPmEndTime = DateUtil.getFullDate(DateUtil.formateDate(vacationEndDate)+" "+workTimeArray[3]+":00");
		//检查请假开始时间或者请假结束时间是否在上班时间内
		if((vacationBeginDate.after(vacationAmBeginTime)&&vacationBeginDate.before(vacationAmEndTime))
				|| (vacationEndDate.after(_vacationAmENTime)&&vacationEndDate.before(_vacationAmEndTime))
				|| (vacationBeginDate.after(vacationPmBeginTime)&&vacationBeginDate.before(vacationPmEndTime))
				|| (vacationEndDate.after(_vacationPmBeginTime)&&vacationEndDate.before(_vacationPmEndTime))){
			return true;
		}
		return false;
	}
	@Override
	public String getStaffAnnualVacationInfo(String userID) throws Exception {
		Date now = new Date();
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		Group group = null;
		
		if(groups.size()>0){
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyID = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			double dailyHour = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyID,
					departmentId, DateUtil.formateFullDate(now));
			//计算年假天数
			String annualVacationDayStr = getAnnualVacationDays(DateUtil.formateFullDate(now), userID);
			int annualVacationDays = 0;
			if(Pattern.matches("^[0-9]*$", annualVacationDayStr)){
				annualVacationDays = Integer.parseInt(annualVacationDayStr);
			}else{
				return "无";
			}
			Double[] usedAnnualVacationDaysAndHours = getUsedAnnualVacationDaysAndHours(DateUtil.formateFullDate(now), userID);
			//已休年休假天数
			double days = usedAnnualVacationDaysAndHours[0];
			//已休年休假小时数
			double hours = usedAnnualVacationDaysAndHours[1];
			//剩余的年假小时数
			double remainAnnualVacationHours = (annualVacationDays-days)*dailyHour - hours;
			int day = (int)Math.floor(remainAnnualVacationHours/dailyHour);
			double _hour = (remainAnnualVacationHours - day*dailyHour);
			if(day>0){
				if(_hour>0){
					return day+"天"+_hour+"小时";
				}else{
					return day+"天";
				}
			}else{
				return remainAnnualVacationHours+"小时";
			}
		}else{
			return "已离职";
		}
			
		
	}
	@Override
	public List<String> findVacationUsers(String vacationId) {
		String hql = "from VacationEntity where vacationID="+vacationId;
		VacationEntity vacation = (VacationEntity) baseDao.hqlfindUniqueResult(hql);
		String requestUserId = vacation.getRequestUserID();
		String[] vacationUserIds = requestUserId.split(",");
		List<String> vacationUserNames = new ArrayList<>();
		for(String vacationUserId: vacationUserIds){
			StaffEntity staff = staffService.getStaffByUserId(vacationUserId);
			vacationUserNames.add(staff.getStaffName());
		}
		return vacationUserNames;
	}
	@Override
	public boolean hasMarriageHoliday(String requestUserId) {
		//判断是否已婚
		StaffEntity staff = staffService.getStaffByUserId(requestUserId);
		//已婚
		if(staff.getMaritalStatus() == 1){
			return false;
		}
		//未婚的情况下，是否已请过婚假
		String sql = "select count(vacationID) from OA_Vacation where requestUserID='"+requestUserId+"'" +
				     " and vacationType=4 and ifNull(processStatus, 0)!=31 and ifNull(processStatus, 0)!=2 and isDeleted=0";
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sql)));
		if(count>0){
			return false;
		}
		return true;
	} 
}
