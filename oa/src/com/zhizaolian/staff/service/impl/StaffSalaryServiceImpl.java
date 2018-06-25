package com.zhizaolian.staff.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.PositionDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.AlterStaffSalaryEntity;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.EasyProcessEntity;
import com.zhizaolian.staff.entity.PerformanceStaffCheckItemEntity;
import com.zhizaolian.staff.entity.PositionEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.SocialSecurityClassEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffSalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.entity.UserMonthlyRestEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.SalaryPayEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EmailSender;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.NumberUtil;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.PoiUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.ChangeSalaryDetailVo;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PaySalaryTaskVo;
import com.zhizaolian.staff.vo.RewardAndPunishmentVo;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.WorkReportVO;

import lombok.Cleanup;

@Service(value="staffSalaryService")
public class StaffSalaryServiceImpl implements StaffSalaryService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private WorkReportService workReportService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private PositionService positionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionDao positionDao;

	private final static String[] TITLES={"姓名","基础工资","绩效奖金",
			"绩效提成","加班补贴","满勤","应抵扣金额",
			"其他扣除","迟到扣除","扣养老","扣医保"
			,"扣失业","大病","扣住房公积金","扣个税","实发工资"
	};
	@SuppressWarnings("unchecked")
	@Override
	public List<SocialSecurityClassEntity> findSocialSecurityClasss() {
		String hql = "from SocialSecurityClassEntity where isDeleted=0 order by name";
		return (List<SocialSecurityClassEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public void saveSocialSecurity(SocialSecurityClassEntity socialSecurity) {
		if(null != socialSecurity.getId()){
			baseDao.hqlUpdate(socialSecurity);
		}else{
			socialSecurity.setAddTime(new Date());
			baseDao.hqlSave(socialSecurity);
		}
	}

	@Override
	public SocialSecurityClassEntity findSocialSecurityById(String id) {
		String hql = "from SocialSecurityClassEntity where id="+id;
		return (SocialSecurityClassEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public int saveStaffSalary(StaffSalaryEntity staffSalary) {
		staffSalary.setAddTime(new Date());
		staffSalary.setOtherPartItems(StringUtils.join(staffSalary.getOtherPartItem(), ","));
		staffSalary.setOtherPartValues(StringUtils.join(staffSalary.getOtherPartValue(), ","));
		return baseDao.hqlSave(staffSalary);
	}

	@Override
	public void updateStaffSalaryUserId(String userId, Integer staffSalaryId) {
		String hql = "update StaffSalaryEntity set userId='"+userId+"' where id="+staffSalaryId;
		baseDao.excuteHql(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StaffSalaryEntity getStaffSalary(String userID) {
		String hql = "from StaffSalaryEntity where isDeleted=0 and userId='"+userID+"'";
		List<StaffSalaryEntity> staffSalarys = (List<StaffSalaryEntity>) baseDao.hqlfind(hql);
		if(staffSalarys.size()>0){
			StaffSalaryEntity  staffSalary = staffSalarys.get(0);
			String otherPartItemStr = staffSalary.getOtherPartItems();
			String otherPartValueStr = staffSalary.getOtherPartValues();
			if(null != otherPartItemStr){
				String[] otherPartItems = otherPartItemStr.split(",");
				String[] otherPartValues = otherPartValueStr.split(",");
				Map<String, String> itemAndValMap = new HashMap<>();
				int index = 0;
				for(String otherPartItem: otherPartItems){
					itemAndValMap.put(otherPartItem, otherPartValues[index]);
					index++;
				}
				staffSalary.setItemAndValMap(itemAndValMap);
			}
			return staffSalary;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlterStaffSalaryEntity> findStaffSalaryAlterations(String userId) throws Exception {
		String hql = "from AlterStaffSalaryEntity where requestUserId='"+userId+"' order by addTime desc";
		List<AlterStaffSalaryEntity> staffSalaryAlterations = (List<AlterStaffSalaryEntity>) baseDao.hqlfind(hql);
		for(AlterStaffSalaryEntity staffSalaryAlteration: staffSalaryAlterations){
			staffSalaryAlteration.setBeforeSalary((StaffSalaryEntity) ObjectByteArrTransformer.toObject(staffSalaryAlteration.getBeforeSalaryData()));
			staffSalaryAlteration.setAfterSalary((StaffSalaryEntity) ObjectByteArrTransformer.toObject(staffSalaryAlteration.getAfterSalaryData()));
			StaffEntity staff = staffService.getStaffByUserId(userId);
			staffSalaryAlteration.setStaffName(staff.getStaffName());
			staffSalaryAlteration.setUserName(staffService.getStaffByUserId(staffSalaryAlteration.getUserId()).getStaffName());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(staffSalaryAlteration.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				staffSalaryAlteration.setStatus("进行中");
				staffSalaryAlteration.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = staffSalaryAlteration.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						staffSalaryAlteration.setStatus(t.getName());
				}
			}
		}
		return staffSalaryAlterations;
	}

	@Override
	public void applyAlterStaffSalary(StaffSalaryEntity staffSalary, AlterStaffSalaryEntity alterStaffSalary,
			File[] attachment, String[] attachmentFileName) throws Exception {
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> generalManagers = permissionService.findUsersByPermissionCode(Constants.GENERAL_MANAGER);
		if(generalManagers.size()<0){
			throw new RuntimeException("未找到总经理，请联系系统管理员配置！");
		}
		vars.put("auditor", generalManagers.get(0));
		vars.put("auditors", new ArrayList<>());
		vars.put("auditGroups", new ArrayList<>());
		vars.put("processType", Constants.ALTER_SALARY);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EASY_PROCESS);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), alterStaffSalary.getUserId());
		taskService.complete(task.getId(), vars);
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.SALARY_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						Constants.SALARY_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				alterStaffSalary.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		if(null != staffSalary.getOtherPartItem()){
			Map<String, String> itemAndValMap = new HashMap<>();
			int index = 0;
			for(String otherPartItem: staffSalary.getOtherPartItem()){
				itemAndValMap.put(otherPartItem, String.valueOf(staffSalary.getOtherPartValue()[index]));
				index++;
			}
			staffSalary.setItemAndValMap(itemAndValMap);
		}
		staffSalary.setOtherPartItems(StringUtils.join(staffSalary.getOtherPartItem(), ","));
		staffSalary.setOtherPartValues(StringUtils.join(staffSalary.getOtherPartValue(), ","));
		alterStaffSalary.setAfterSalaryData(ObjectByteArrTransformer.toByteArray(staffSalary));
		StaffSalaryEntity beforeStaffSalary = getStaffSalary(alterStaffSalary.getRequestUserId());
		alterStaffSalary.setBeforeSalaryData(ObjectByteArrTransformer.toByteArray(beforeStaffSalary));
		alterStaffSalary.setAddTime(new Date());
		alterStaffSalary.setProcessInstanceID(processInstance.getId());
		baseDao.hqlSave(alterStaffSalary);
	}

	@Override
	public boolean checkHasApplyAlterSalary(String userId) {
		String hql = "select count(id) from AlterStaffSalaryEntity where "
				+ "requestUserId='"+userId+"' and isDeleted=0 and applyResult is null";
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hql)));
		if(count > 0){
			return true;
		}
		return false;
	}

	@Override
	public AlterStaffSalaryEntity showSalaryAlterationDetail(String id) throws Exception {
		String hql = "from AlterStaffSalaryEntity where id="+id;
		AlterStaffSalaryEntity staffSalaryAlteration = (AlterStaffSalaryEntity) baseDao.hqlfindUniqueResult(hql);
		staffSalaryAlteration.setBeforeSalary((StaffSalaryEntity) ObjectByteArrTransformer.toObject(staffSalaryAlteration.getBeforeSalaryData()));
		staffSalaryAlteration.setAfterSalary((StaffSalaryEntity) ObjectByteArrTransformer.toObject(staffSalaryAlteration.getAfterSalaryData()));
		String attachmentIdStr = staffSalaryAlteration.getAttachmentIds();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			String[] attachmentIds = attachmentIdStr.split(",");
			staffSalaryAlteration.setAttaIds(Arrays.asList(attachmentIds));
		}
		return staffSalaryAlteration;
	}

	@Override
	public List<AlterStaffSalaryEntity> findAlterSalaryTaskVos(List<Task> alterSalaryTasks) {
		List<AlterStaffSalaryEntity> alterStaffSalaryTasks = new ArrayList<>();
		for(Task alterSalaryTask: alterSalaryTasks){
			String hql = "from AlterStaffSalaryEntity where processInstanceID="+alterSalaryTask.getProcessInstanceId();
			AlterStaffSalaryEntity alterStaffSalary = (AlterStaffSalaryEntity) baseDao.hqlfindUniqueResult(hql);
			StaffEntity staff = staffService.getStaffByUserId(alterStaffSalary.getRequestUserId());
			alterStaffSalary.setStaffName(staff.getStaffName());
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(staff.getUserID());
			if(!CollectionUtils.isEmpty(groupDetails)){
				GroupDetailVO g0 = groupDetails.get(0);
				alterStaffSalary.setDepartment(g0.getCompanyName()+"-"+g0.getDepartmentName());
			}
			alterStaffSalary.setUserName(staffService.getStaffByUserId(alterStaffSalary.getUserId()).getStaffName());
			alterStaffSalary.setTaskId(alterSalaryTask.getId());
			alterStaffSalaryTasks.add(alterStaffSalary);
		}
		return alterStaffSalaryTasks;
	}

	@Override
	public void updateAlterSalaryStatus(String result, String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String hql = "update AlterStaffSalaryEntity set applyResult="+result+" where processInstanceID="+task.getProcessInstanceId();
		baseDao.excuteHql(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refreshStaffSalary() throws Exception {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		String hql = "from AlterStaffSalaryEntity where isDeleted=0 and ifNull(applyResult, 0)=1 and month(effectDate)="+month
				+ " and year(effectDate)="+year;
		List<AlterStaffSalaryEntity> alterStaffSalarys = (List<AlterStaffSalaryEntity>) baseDao.hqlfind(hql);
		for(AlterStaffSalaryEntity alterStaffSalary: alterStaffSalarys){
			String userId = alterStaffSalary.getRequestUserId();
			hql = "from StaffSalaryEntity where isDeleted=0 and userId='"+userId+"'";
			List<StaffSalaryEntity> staffSalarys = (List<StaffSalaryEntity>) baseDao.hqlfind(hql);
			if(staffSalarys.size()>0){
				StaffSalaryEntity  staffSalary = staffSalarys.get(0);
				StaffSalaryEntity afterStaffSalary = (StaffSalaryEntity) ObjectByteArrTransformer.toObject(
						alterStaffSalary.getAfterSalaryData());
				staffSalary.setBasicSalary(afterStaffSalary.getBasicSalary());
				staffSalary.setCompanyPay(afterStaffSalary.getCompanyPay());
				staffSalary.setCompanyPayFund(afterStaffSalary.getCompanyPayFund());
				staffSalary.setFullAttendance(afterStaffSalary.getFullAttendance());
				staffSalary.setMedicalInsurance(afterStaffSalary.getMedicalInsurance());
				staffSalary.setOtherPartItems(afterStaffSalary.getOtherPartItems());
				staffSalary.setOtherPartValues(afterStaffSalary.getOtherPartValues());
				staffSalary.setPension(afterStaffSalary.getPension());
				staffSalary.setPerformanceSalary(afterStaffSalary.getPerformanceSalary());
				staffSalary.setPersonalPay(afterStaffSalary.getPersonalPay());
				staffSalary.setPersonalPayFund(afterStaffSalary.getPersonalPayFund());
				staffSalary.setPublicfundBasic(afterStaffSalary.getPublicfundBasic());
				staffSalary.setSeriousIllness(afterStaffSalary.getSeriousIllness());
				staffSalary.setSocialSecurityBasic(afterStaffSalary.getSocialSecurityBasic());
				staffSalary.setStandardSalary(afterStaffSalary.getStandardSalary());
				staffSalary.setUnemployment(afterStaffSalary.getUnemployment());
				baseDao.hqlUpdate(staffSalary);
				hql = "from StaffEntity where isDeleted=0 and userID='"+userId+"'";
				StaffEntity staff = (StaffEntity) baseDao.hqlfindUniqueResult(hql);
				staff.setStandardSalary(afterStaffSalary.getStandardSalary());
				staff.setPerformance(afterStaffSalary.getPerformanceSalary());
				baseDao.hqlUpdate(staff);
			}
		}
	}
	/**
	 * 生成人员基本薪资
	 * @throws Exception
	 */
	@Override
	public void generateStaffMonthlySalary() throws Exception {
		//获取上月在职的所有员工
		List<Object> lastMonthInJobStaffs = getLastMonthInJobStaffs();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		//上个月的最后一天
		Date endDate = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		//上个月的第一天
		Date beginDate = cal.getTime();
		//未发日报人员集合
		Map<String, WorkReportVO> noReportMap = workReportService.findStatisticsByDate(null, DateUtil.formateDate(beginDate),
				DateUtil.formateDate(endDate));
		for(Object lastMonthInJobStaff: lastMonthInJobStaffs){
			List<String> remarks = new ArrayList<>();
			Object[] objs = (Object[])lastMonthInJobStaff;
			String userId = (String)objs[0];
			Date leaveDate = (Date)objs[1];
			boolean lastAttendance = true;
			Date lastAttendanceDate = endDate;
			//离职人员
			if(null != leaveDate){
				//离职人员最后一天的打卡日期（全天打卡）
				String lastAttendanceTime = attendanceService.getLeaveStaffLastAttendanceDate(userId, month);
				if(null == lastAttendanceTime){
					continue;
				}else if(!DateUtil.formateDate(endDate).equals(lastAttendanceTime)){
					lastAttendanceDate = DateUtil.getSimpleDate(lastAttendanceTime);
					lastAttendance = false;
				}
			}
			StaffSalaryDetailEntity staffSalaryDetail = new StaffSalaryDetailEntity();
			boolean isPartner = staffService.isPartner(userId);
			//员工薪资标准
			StaffSalaryEntity staffSalary = getStaffSalary(userId);
			if(null == staffSalary){
				continue;
			}
			//赋值
			setStaffSalaryValue(staffSalaryDetail, staffSalary);
			//未发工作日报次数（普通员工20元/次，合伙人60元/次）
			WorkReportVO workReortVo = noReportMap.get(userId);
			if(null != workReortVo){
				int noSendTimes = workReortVo.getCount();
				staffSalaryDetail.setNoSendReportTimes(noSendTimes);
				if(isPartner){
					double money = noSendTimes*Constants.NO_WORKREPORT*3;
					staffSalaryDetail.setNoSendReportMoney(money);
				}else{
					double money = noSendTimes*Constants.NO_WORKREPORT;
					staffSalaryDetail.setNoSendReportMoney(money);
				}
			}
			//考勤数据
			AttendanceVO attendance = attendanceService.findAttendanceStatisticsByUserId(beginDate, lastAttendanceDate, userId);
			if(null == attendance){
				continue;
			}
			//迟到次数以及对应的分钟数（普通员工2元/分钟，合伙人6元/分钟）
			staffSalaryDetail.setLateTimes(attendance.getLateTimes());
			int lateMinutes = attendance.getLateTime();
			staffSalaryDetail.setLateMinutes(lateMinutes);
			if(lateMinutes>0){
				if(isPartner){
					staffSalaryDetail.setLateMoney(lateMinutes*Constants.LATE_MONEY*3);
				}else{
					staffSalaryDetail.setLateMoney(lateMinutes*Constants.LATE_MONEY);
				}
			}
			//未刷卡次数（1次忘打卡不扣钱）
			int noPunchTimes = attendance.getNotPunchTimes();
			staffSalaryDetail.setNoPunchTimes(noPunchTimes);
			staffSalaryDetail.setNoPunchMoney(noPunchTimes*Constants.NO_PUNCH_MONEY);
			//异常考勤天数（未打卡且未请假）
			List<String> abnormalDates = attendanceService.getAbnormalDays(userId, beginDate, lastAttendanceDate);
			int abnormalDays = abnormalDates.size();
			if(abnormalDays>0){
				remarks.add(StringUtils.join(abnormalDates, ",")+",考勤异常");
			}
			UserMonthlyRestEntity monthlyRest = attendanceService.getMonthlyRest(userId, year, month);
			//当月在职天数
			int days = DateUtil.differentDays(beginDate, lastAttendanceDate)+1;
			//应出勤天数
			int attendanceDays = days - Integer.parseInt(monthlyRest.getRestDays());
			staffSalaryDetail.setAttendanceDays(attendanceDays);
			//请假数据
			VacationVO vacation = attendance.getVacationVO();
			//普通请假（公假，事假）天数
			int ordinaryVacationDays = 0;
			//普通请假时长折算成天数，剩余的小时数
			double ordinaryVacationHours = 0;
			//特殊请假（婚假，年休假等），不纳入满勤考核，没有绩效，只有基本工资
			int specialVacationDays = 0;
			//每日工作时长
			double dailyHour = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(String.valueOf(attendance.getCompanyID()),
					String.valueOf(attendance.getDepartmentID()), DateUtil.formateDate(beginDate));
			if(null != vacation){
				ordinaryVacationDays = vacation.getOrdinaryVacationdays()==null ? 0:vacation.getOrdinaryVacationdays();
				specialVacationDays = vacation.getSpecialVacationDays()==null ? 0:vacation.getSpecialVacationDays();
				ordinaryVacationHours = vacation.getOrdinaryVacationHours();
				//特殊请假时长折算成天数，剩余的小时数，忽略
				//double specialVacationHours = vacation.getSpecialVacationHours();
			}
			//实际出勤天数（算上了特殊请假），用来判断是否达到满勤
			int actualAttendanceDays = days - ordinaryVacationDays - abnormalDays;
			staffSalaryDetail.setDailyHour(dailyHour);
			//晚上加班小时
			double nightOvertimeHours = Double.parseDouble(attendance.getNightWorkHours());
			//加班可抵扣请假时长折算成天数剩余的小时数（注意，只可抵扣小时）
			if(nightOvertimeHours>0 && ordinaryVacationHours>0){
				if(nightOvertimeHours>ordinaryVacationHours){
					nightOvertimeHours -= ordinaryVacationHours;
					ordinaryVacationHours = 0;
				}else{
					ordinaryVacationHours -= nightOvertimeHours;
					nightOvertimeHours = 0;
				}
			}
			if(nightOvertimeHours>0){
				staffSalaryDetail.setNightOvertimeHours(nightOvertimeHours);
			}
			//加班补贴    晚上加班补贴（基本工资/30/每日工作小时数*加班小时数）；周末/公休加班：白领一百一天，蓝领无
			double overtimeMoney = staffSalaryDetail.getBasicSalary()/30/dailyHour*nightOvertimeHours;
			//白天加班时长
			double dailyWorkOverTimeHours = 0;
			boolean whiteJob = staffService.isWhiteJob(userId);
			//白领
			if(whiteJob){
				//离职人员
				if(null != leaveDate){
					//当月最后一天打卡上班，反之不计算加班时长
					if(lastAttendance){
						dailyWorkOverTimeHours = attendance.getDailyWorkOverTimeHours();
					}
				}else{
					dailyWorkOverTimeHours = attendance.getDailyWorkOverTimeHours();
				}
				//减去异常时长
				dailyWorkOverTimeHours -= abnormalDays*dailyHour;
				if(dailyWorkOverTimeHours>0){
					staffSalaryDetail.setDayOvertimeHours(attendance.getDayWorkHours());
					overtimeMoney += 100/dailyHour*dailyWorkOverTimeHours;
				}
			}
			if(overtimeMoney>0){
				staffSalaryDetail.setOvertimeSubsidy(NumberUtil.Rounding(overtimeMoney, 2));
			}
			//满勤  1、400满勤：达到应出勤天数；工作日每休息一天，扣一百
			//   2、120满勤：达到应出勤天数
			double fullAttendance = 0;
			//达到了应出勤天数
			if((actualAttendanceDays>=attendanceDays && ordinaryVacationHours==0) ||
					((actualAttendanceDays-1)>=attendanceDays)){
				if(whiteJob){
					fullAttendance = Constants.WHITE_FULL_ATTENDANCE;
				}else{
					fullAttendance = Constants.BLUE_FULL_ATTENDANCE;
				}
				if(null != vacation){
					double restDaysInWeekDay = vacation.getRestDaysInWeekDay();
					double restHoursInWeekDay = vacation.getRestHoursInWeekDay();
					fullAttendance -= restDaysInWeekDay*100;
					//折算成小时扣除
					fullAttendance -= 100/dailyHour*restHoursInWeekDay;
				}
			}
			if(fullAttendance>=0){
				staffSalaryDetail.setFullAttendance(NumberUtil.Rounding(fullAttendance, 2));
			}

			//抵减小时
			if(ordinaryVacationHours>0){
				staffSalaryDetail.setDeductibleHours(ordinaryVacationHours);
				staffSalaryDetail.setDeductibleMoney(NumberUtil.Rounding(staffSalaryDetail.getBasicSalary()/30/dailyHour*ordinaryVacationHours, 2));
			}
			//基本工资 （ 基本工资/应出勤天数*实际出勤天数）
			staffSalaryDetail.setBasicSalary(NumberUtil.Rounding(staffSalaryDetail.getBasicSalary()/staffSalaryDetail.getAttendanceDays()*
					(actualAttendanceDays>staffSalaryDetail.getAttendanceDays() ? staffSalaryDetail.getAttendanceDays():actualAttendanceDays), 2));
			//减去特殊请假的实际出勤天数
			actualAttendanceDays -= specialVacationDays;
			staffSalaryDetail.setActualAttendanceDays(actualAttendanceDays);
			//奖惩
			List<RewardAndPunishmentVo> rewardAndPunishmentVos = getRewardAndPunishmentsByUserIdAndDate(year, month, userId);
			double reward = 0;
			double punishment = 0;
			for(RewardAndPunishmentVo rewardAndPunishmentVo: rewardAndPunishmentVos){
				//0：奖励；1：惩罚
				if("0".equals(rewardAndPunishmentVo.getType())){
					reward += rewardAndPunishmentVo.getMoney();
				}else{
					punishment += rewardAndPunishmentVo.getMoney();
				}
			}
			if(reward > 0){
				staffSalaryDetail.setReward(reward);
			}
			if(punishment > 0){
				staffSalaryDetail.setPenalty(punishment);
			}
			staffSalaryDetail.setRemarks(ObjectByteArrTransformer.toByteArray(remarks));
			staffSalaryDetail.setUserId(userId);
			Integer companyId = companyDao.getCompanyIdByUserId(userId);
			staffSalaryDetail.setCompanyId(companyId);
			Integer departmentId = departmentDao.getDeparmentIdByUserId(userId);
			staffSalaryDetail.setDepartmentId(departmentId);
			staffSalaryDetail.setYear(year);
			staffSalaryDetail.setMonth(month);
			staffSalaryDetail.setAddTime(new Date());
			baseDao.hqlSave(staffSalaryDetail);
		}
	}

	@SuppressWarnings("unchecked")
	private List<RewardAndPunishmentVo> getRewardAndPunishmentsByUserIdAndDate(int year, int month, String userId) throws Exception {
		String hql = "from EasyProcessEntity where year="+year+" and month="+month+" and LOCATE('"+userId+"',requestUserId)>0 AND applyResult=1" +
				" and businessType="+BusinessTypeEnum.REWARD_PUNISHMENT.getValue();
		List<EasyProcessEntity> processList = (List<EasyProcessEntity>) baseDao.hqlfind(hql);
		List<RewardAndPunishmentVo> rewardAndPunishmentVos = new ArrayList<>();
		for(EasyProcessEntity process: processList){
			RewardAndPunishmentVo rewardAndPunishmentVo = (RewardAndPunishmentVo) ObjectByteArrTransformer.toObject(process.getData());
			rewardAndPunishmentVos.add(rewardAndPunishmentVo);
		}
		return rewardAndPunishmentVos;
	}

	private void setStaffSalaryValue(StaffSalaryDetailEntity staffSalaryDetail, StaffSalaryEntity staffSalary) {
		staffSalaryDetail.setBasicSalary(staffSalary.getBasicSalary());
		staffSalaryDetail.setCompanyPay(staffSalary.getCompanyPay());
		staffSalaryDetail.setCompanyPayFund(staffSalary.getCompanyPayFund());
		staffSalaryDetail.setFullAttendance(staffSalary.getFullAttendance());
		staffSalaryDetail.setMedicalInsurance(staffSalary.getMedicalInsurance());
		staffSalaryDetail.setPension(staffSalary.getPension());
		staffSalaryDetail.setPerformanceSalary(staffSalary.getPerformanceSalary());
		staffSalaryDetail.setPersonalpay(staffSalary.getPersonalPay());
		staffSalaryDetail.setPersonalPayFund(staffSalary.getPersonalPayFund());
		staffSalaryDetail.setPublicFund(staffSalary.getPersonalPayFund());
		staffSalaryDetail.setSeriousIllness(staffSalary.getSeriousIllness());
		staffSalaryDetail.setStandardSalary(staffSalary.getStandardSalary());
		staffSalaryDetail.setUnemployment(staffSalary.getUnemployment());
	}

	private List<Object> getLastMonthInJobStaffs() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		String sql = "SELECT\n" +
				"	UserID, LeaveDate\n" +
				"FROM\n" +
				"	OA_Staff\n" +
				"WHERE\n" +
				"	IsDeleted = 0\n" +
				"AND `Status` != 4\n" +
				"UNION\n" +
				"	(\n" +
				"		SELECT\n" +
				"			userId, LeaveDate\n" +
				"		FROM\n" +
				"			OA_Staff\n" +
				"		WHERE\n" +
				"			IsDeleted = 0\n" +
				"		AND `Status` = 4\n" +
				"		AND MONTH (LeaveDate) = "+month+"\n" +
				"		AND YEAR (LeaveDate) = "+year+"\n" +
				"	)";
		return baseDao.findBySql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void calStaffSalary(String[] ids) {
		String hql = "from PerformanceStaffCheckItemEntity where id in ("+StringUtils.join(ids, ",")+")";
		List<PerformanceStaffCheckItemEntity> performanceStaffCheckItems = (List<PerformanceStaffCheckItemEntity>) baseDao.hqlfind(hql);
		if(performanceStaffCheckItems.size()<1){
			throw new RuntimeException("绩效工资计算失败，请联系系统管理员");
		}
		PerformanceStaffCheckItemEntity performanceStaffCheckItem = performanceStaffCheckItems.get(0);
		int year = performanceStaffCheckItem.getYear();
		int month = performanceStaffCheckItem.getMonth();
		String userId = performanceStaffCheckItem.getUserId();
		StaffSalaryDetailEntity staffSalaryDetail = getStaffSalaryDetailByUserId(year, month, userId);
		if(null == staffSalaryDetail){
			throw new RuntimeException("绩效工资计算失败，请联系系统管理员");
		}
		double performance = staffSalaryDetail.getPerformanceSalary();
		//所有的绩效金额
		double totalPerformanceMoney = 0;
		for(PerformanceStaffCheckItemEntity checkItem: performanceStaffCheckItems){
			//绩效系数
			double coefficient = checkItem.getCoefficient();
			checkItem.setRateMoney(coefficient*performance);
			Double targetValue = checkItem.getTargetValue();
			Double actualValue = checkItem.getActualValue();
			double performanceMoney = 0;
			//计算绩效金额
			if(null!=targetValue && null!=actualValue){
				//实际完成值大于等于目标值
				if(actualValue>=targetValue){
					//奖励
					if("+".equals(checkItem.getAddMoneyType()) && null!=checkItem.getAddMoney()){
						performanceMoney = (actualValue-targetValue)/checkItem.getPerAddMoneyValue()*checkItem.getAddMoney();
						//少发
					}else if("-".equals(checkItem.getReduceMoneyType()) && null!=checkItem.getReduceMoney()){
						performanceMoney = -(actualValue-targetValue)/checkItem.getPerReduceMoneyValue()*checkItem.getReduceMoney();
					}
					//实际完成值小于目标值	
				}else{
					//少发
					if("-".equals(checkItem.getReduceMoneyType()) && null!=checkItem.getReduceMoney()){
						performanceMoney = (actualValue-targetValue)/checkItem.getPerReduceMoneyValue()*checkItem.getReduceMoney();
						//奖励
					}else if("+".equals(checkItem.getAddMoneyType()) && null!=checkItem.getAddMoney()){
						performanceMoney = -(actualValue-targetValue)/checkItem.getPerAddMoneyValue()*checkItem.getAddMoney();
					}
				}
				BigDecimal b = new BigDecimal(performanceMoney);
				performanceMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				checkItem.setPerformanceMoney(performanceMoney);
				double performanceSalary = NumberUtil.Rounding(coefficient*performance+performanceMoney, 2);
				if(performanceSalary<0){
					performanceSalary = 0;
				}
				checkItem.setPerformanceSalary(performanceSalary);
			}
			totalPerformanceMoney += checkItem.getPerformanceSalary();
		}
		//绩效工资（绩效工资/应出勤天数*实际出勤天数）
		staffSalaryDetail.setPerformanceSalary(NumberUtil.Rounding(totalPerformanceMoney/staffSalaryDetail.getAttendanceDays()*
				(staffSalaryDetail.getActualAttendanceDays()>staffSalaryDetail.getAttendanceDays() ? staffSalaryDetail.getAttendanceDays():
					staffSalaryDetail.getActualAttendanceDays()), 2));
		//未扣除应扣项目的工资 = 基础工资+绩效工资+满勤+加班补贴+行政奖励-抵减金额-迟到扣除-未汇报日报罚款-未刷卡扣除-行政处罚
		double totalMoney = staffSalaryDetail.getBasicSalary()+staffSalaryDetail.getPerformanceSalary()+staffSalaryDetail.getFullAttendance()
		+(staffSalaryDetail.getOvertimeSubsidy()==null ? 0:staffSalaryDetail.getOvertimeSubsidy())+
		(staffSalaryDetail.getReward()==null ? 0:staffSalaryDetail.getReward())-
		(staffSalaryDetail.getDeductibleMoney()==null ? 0:staffSalaryDetail.getDeductibleMoney())
		-(staffSalaryDetail.getLateMoney()==null ? 0:staffSalaryDetail.getLateMoney())
		-(staffSalaryDetail.getNoSendReportMoney()==null ? 0:staffSalaryDetail.getNoSendReportMoney())
		-(staffSalaryDetail.getNoPunchMoney()==null ? 0:staffSalaryDetail.getNoPunchMoney())
		-(staffSalaryDetail.getPenalty()==null ? 0:staffSalaryDetail.getPenalty());
		totalMoney = NumberUtil.Rounding(totalMoney, 2);
		staffSalaryDetail.setTotalMoney(totalMoney);
		//税前工资=未扣除应扣项目的工资-个人缴纳保险-个人缴纳公积金
		double preTaxSalary = totalMoney
				-(staffSalaryDetail.getPersonalpay()==null ? 0:staffSalaryDetail.getPersonalpay())
				-(staffSalaryDetail.getPersonalPayFund()==null ? 0:staffSalaryDetail.getPersonalPayFund());
		preTaxSalary = NumberUtil.Rounding(preTaxSalary, 2);
		staffSalaryDetail.setPreTaxSalary(preTaxSalary);
		//税收工资部份，现在工资起征点为3500
		double taxSalary = preTaxSalary - 3500;
		//个人所得税
		double tax = taxSalary<0?0.0:
			taxSalary<=1500?0.03*taxSalary:
				taxSalary<=4500?taxSalary*0.1-105:
					taxSalary<=9000?taxSalary*0.2-555:
						taxSalary<=35000?taxSalary*0.25-1005:
							taxSalary<=55000?taxSalary*0.3-2755:
								taxSalary<=80000?taxSalary*0.35-5505:
									taxSalary*0.45-13505;
		tax = NumberUtil.Rounding(tax, 2);
		staffSalaryDetail.setPersonalIncomeTax(tax);
		baseDao.hqlUpdate(staffSalaryDetail);
	}
	@Override
	@SuppressWarnings("unchecked")
	public StaffSalaryDetailEntity getStaffSalaryDetailByUserId(int year, int month, String userId) {
		String hql = "from StaffSalaryDetailEntity where isDeleted=0 and year="+year+" and month="+month+" and userId='"+userId+"'";
		List<StaffSalaryDetailEntity> staffSalaryDetails = (List<StaffSalaryDetailEntity>) baseDao.hqlfind(hql);
		if(staffSalaryDetails.size()>0){
			return staffSalaryDetails.get(0);
		}
		return null;
	}

	@Override
	public ListResult<Object> findStaffSalarys(StaffSalaryDetailEntity staffDetail, Integer limit,
			Integer page) {
		String sql = "SELECT\n" +
				"	StaffName, concat(companyName,'-',DepartmentName), emailSend,\n"
				+ " mobileSend, preTaxSalary, personalIncomeTax,basicSalary,performanceSalary, salary.id, payStatus\n" +
				"FROM\n" +
				"	oa_staffsalarydetail salary,\n" +
				"	oa_department dep,\n" +
				"   oa_company com,\n" +
				"	oa_staff staff\n" +
				"WHERE\n" +
				"	salary.preTaxSalary is not null and salary.isDeleted = 0\n" +
				"AND staff.IsDeleted = 0\n" +
				"and salary.departmentId = dep.DepartmentID\n" +
				"and salary.companyId = com.companyId\n" +
				"and salary.userId = staff.UserID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.`month` = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			sql += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getMobileSend()){
			sql += "and ifNull(mobileSend, 0)="+staffDetail.getMobileSend()+"\n";
		}
		if(null != staffDetail.getEmailSend()){
			sql += "and ifNull(emailSend, 0)="+staffDetail.getEmailSend()+"\n";
		}
		if(null != staffDetail.getCompanyId()){
			sql += "and com.companyId="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				sql += "and staff.status!=4\n";
			}else{
				sql += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			sql += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sql+=" and dep.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		sql += " order by salary.companyId, salary.departmentId";
		List<Object> staffSalarys = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	oa_staffsalarydetail salary,\n" +
				"	oa_staff staff,\n" +
				"	oa_department dep,\n" +
				"   oa_company com\n" +
				"WHERE\n" +
				"	salary.preTaxSalary is not null and salary.isDeleted = 0\n" +
				"and salary.departmentId = dep.DepartmentID\n" +
				"and salary.companyId = com.companyId\n" +
				"and salary.userId = staff.UserID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.`month` = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			sqlCount += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getMobileSend()){
			sqlCount += "and ifNull(mobileSend, 0)="+staffDetail.getMobileSend()+"\n";
		}
		if(null != staffDetail.getEmailSend()){
			sqlCount += " and ifNull(emailSend, 0)="+staffDetail.getEmailSend()+"\n";
		}
		if(null != staffDetail.getCompanyId()){
			sqlCount += "and com.companyId="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				sqlCount += "and staff.status!=4\n";
			}else{
				sqlCount += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			sqlCount += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sqlCount+=" and dep.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sqlCount)));
		return new ListResult<>(staffSalarys, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StaffSalaryDetailEntity getStaffSalaryDetailById(String id) throws Exception {
		String hql = "from StaffSalaryDetailEntity where id="+id;
		StaffSalaryDetailEntity staffSalary = (StaffSalaryDetailEntity) baseDao.hqlfindUniqueResult(hql);
		StaffEntity staff = staffDao.getStaffByUserID(staffSalary.getUserId());
		StaffStatusEnum staffStatus = StaffStatusEnum.valueOf(staff.getStatus());
		staffSalary.setStaffStatus(staffStatus.getName());
		staffSalary.setStaffName(staff.getStaffName());
		staffSalary.setStaffNum(staffDao.getStaffNum(staffSalary.getUserId()));
		DepartmentEntity department = departmentDao.getDepartmentByDepartmentID_(staffSalary.getDepartmentId());
		if(null != department){
			staffSalary.setDepName(department.getDepartmentName());
		}
		CompanyEntity company = companyDao.getCompanyByCompanyID(staffSalary.getCompanyId());
		if(null != company){
			staffSalary.setCompanyName(company.getCompanyName());
		}
		staffSalary.setAfterTaxSalary(NumberUtil.Rounding(staffSalary.getPreTaxSalary()-staffSalary.getPersonalIncomeTax(), 2));
		//应扣项目的合计
		double totalDeduction = (staffSalary.getPersonalpay()==null ? 0:staffSalary.getPersonalpay())
				+(staffSalary.getPersonalPayFund()==null ? 0:staffSalary.getPersonalPayFund()) +
				(staffSalary.getPersonalIncomeTax()==null ? 0:staffSalary.getPersonalIncomeTax());
		staffSalary.setTotalDeduction(NumberUtil.Rounding(totalDeduction, 2));
		if(null != staffSalary.getOtherDeductionItems()){
			Map<String, Double> otherDeductionItems = (Map<String, Double>) ObjectByteArrTransformer.toObject(staffSalary.getOtherDeductionItems());
			if(otherDeductionItems.size()>0){
				Collection<Double> deductionMoneys = otherDeductionItems.values();
				double totalDeductionMoney = 0;
				for(Double deductionMoney: deductionMoneys){
					totalDeductionMoney += deductionMoney;
				}
				if(totalDeductionMoney>0){
					staffSalary.setOtherDeduction(NumberUtil.Rounding(totalDeductionMoney, 2));
				}
			}
			staffSalary.setDeductionItemMap(otherDeductionItems);
		}
		if(null != staffSalary.getOtherSubsidyItems()){
			Map<String, Double> otherSubsidyItems = (Map<String, Double>) ObjectByteArrTransformer.toObject(staffSalary.getOtherSubsidyItems());
			if(otherSubsidyItems.size()>0){
				Collection<Double> subsidyMoneys = otherSubsidyItems.values();
				double totalSubsidyMoney = 0;
				for(Double subsidyMoney: subsidyMoneys){
					totalSubsidyMoney += subsidyMoney;
				}
				if(totalSubsidyMoney>0){
					staffSalary.setOtherSubsidy(NumberUtil.Rounding(totalSubsidyMoney, 2));
				}
			}
			staffSalary.setSubsidyItemMap(otherSubsidyItems);
		}
		byte[] remarkData = staffSalary.getRemarks();
		if(null != remarkData){
			List<String> remarks = (List<String>) ObjectByteArrTransformer.toObject(remarkData);
			staffSalary.setRemarkList(remarks);
		}else{
			staffSalary.setRemarkList(new ArrayList<String>());
		}
		return staffSalary;
	}

	@Override
	public void startApplyChangeStaffSalary(String staffSalaryId, String userId, Map<String, Double> otherDeductionItemMap,
			Map<String, Double> otherSubsidyItemMap) throws Exception {
		double totalDeductionMoney = 0;
		if(otherDeductionItemMap.size()>0){
			Collection<Double> deductionMoneys = otherDeductionItemMap.values();
			for(Double deductionMoney: deductionMoneys){
				totalDeductionMoney += deductionMoney;
			}
		}
		double otherSubsidyMoney = 0;
		if(otherSubsidyItemMap.size()>0){
			Collection<Double> subsidyMoneys = otherSubsidyItemMap.values();
			for(Double subsidyMoney: subsidyMoneys){
				otherSubsidyMoney += subsidyMoney;
			}
		}
		StaffSalaryDetailEntity staffSalaryEntity = getStaffSalaryDetailById(staffSalaryId);
		StaffSalaryDetailEntity staffSalary = (StaffSalaryDetailEntity) CopyUtil.clone(staffSalaryEntity, StaffSalaryDetailEntity.class);
		double preTaxSalary = staffSalary.getPreTaxSalary();
		double totalMoney = staffSalary.getTotalMoney();
		Double otherDedution = staffSalary.getOtherDeduction();
		if(null == otherDedution){
			otherDedution = 0.0;
		}
		Double otherSubsidy = staffSalary.getOtherSubsidy();
		if(null == otherSubsidy){
			otherSubsidy = 0.0;
		}
		//重新计算税前工资
		preTaxSalary = (preTaxSalary+otherDedution-otherSubsidy) - totalDeductionMoney + otherSubsidyMoney;
		totalMoney = (totalMoney+otherDedution-otherSubsidy) - totalDeductionMoney + otherSubsidyMoney;
		double tax = calTax(preTaxSalary);
		double afterTaxSalary = preTaxSalary - tax;
		afterTaxSalary = NumberUtil.Rounding(afterTaxSalary, 2);
		staffSalary.setAfterTaxSalary(afterTaxSalary);
		staffSalary.setPersonalIncomeTax(tax);
		staffSalary.setTotalMoney(NumberUtil.Rounding(totalMoney, 2));
		staffSalary.setPreTaxSalary(NumberUtil.Rounding(preTaxSalary, 2));
		staffSalary.setSubsidyItemMap(otherSubsidyItemMap);
		staffSalary.setDeductionItemMap(otherDeductionItemMap);
		staffSalary.setOtherDeduction(NumberUtil.Rounding(totalDeductionMoney, 2));
		staffSalary.setOtherSubsidy(NumberUtil.Rounding(otherSubsidyMoney, 2));
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> generalManagers = permissionService.findUsersByPermissionCode(Constants.GENERAL_MANAGER);
		if(generalManagers.size()<0){
			throw new RuntimeException("未找到总经理，请联系系统管理员配置！");
		}
		vars.put("auditor", generalManagers.get(0));
		vars.put("auditors", new ArrayList<>());
		vars.put("auditGroups", new ArrayList<>());
		vars.put("processType", Constants.CHANGE_SALARY_DETAIL);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EASY_PROCESS);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		taskService.complete(task.getId(), vars);
		EasyProcessEntity easyProcess = new EasyProcessEntity();
		easyProcess.setData(ObjectByteArrTransformer.toByteArray(staffSalary));
		easyProcess.setUserId(userId);
		easyProcess.setRequestUserId(staffSalary.getUserId());
		easyProcess.setAddTime(new Date());
		easyProcess.setProcessInstanceID(processInstance.getId());
		easyProcess.setBusinessType(BusinessTypeEnum.CHANGE_SALARY_DETAIL.getValue());
		easyProcess.setYear(staffSalary.getYear());
		easyProcess.setMonth(staffSalary.getMonth());
		baseDao.hqlSave(easyProcess);
	}

	@Override
	public double calTax(double preTaxSalary) {
		//税收工资部份，现在工资起征点为3500
		double taxSalary = preTaxSalary - 3500;
		//个人所得税
		double tax = taxSalary<0?0.0:
			taxSalary<=1500?0.03*taxSalary:
				taxSalary<=4500?taxSalary*0.1-105:
					taxSalary<=9000?taxSalary*0.2-555:
						taxSalary<=35000?taxSalary*0.25-1005:
							taxSalary<=55000?taxSalary*0.3-2755:
								taxSalary<=80000?taxSalary*0.35-5505:
									taxSalary*0.45-13505;
		tax = NumberUtil.Rounding(tax, 2);
		return tax;
	}

	@Override
	public List<ChangeSalaryDetailVo> findChangeSalaryDetailTaskVos(List<Task> changeSalaryDetailTasks) throws Exception {
		List<ChangeSalaryDetailVo> changeStaffDetailSalaryTasks = new ArrayList<>();
		for(Task changeSalaryDetailTask: changeSalaryDetailTasks){
			ChangeSalaryDetailVo changeSalaryDetailVo = new ChangeSalaryDetailVo();
			String hql = "from EasyProcessEntity where processInstanceID="+changeSalaryDetailTask.getProcessInstanceId();
			EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
			StaffSalaryDetailEntity newStaffSalaryDetail = (StaffSalaryDetailEntity) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
			changeSalaryDetailVo.setProcessInstanceID(easyProcessEntity.getProcessInstanceID());
			StaffEntity staff = staffService.getStaffByUserId(easyProcessEntity.getRequestUserId());
			changeSalaryDetailVo.setRequestUserName(staff.getStaffName());
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(staff.getUserID());
			if(!CollectionUtils.isEmpty(groupDetails)){
				GroupDetailVO g0 = groupDetails.get(0);
				changeSalaryDetailVo.setDepartment(g0.getCompanyName()+"-"+g0.getDepartmentName());
			}
			changeSalaryDetailVo.setUserName(staffService.getStaffByUserId(easyProcessEntity.getUserId()).getStaffName());
			changeSalaryDetailVo.setTaskId(changeSalaryDetailTask.getId());
			changeSalaryDetailVo.setAddTime(easyProcessEntity.getAddTime());
			changeSalaryDetailVo.setYear(newStaffSalaryDetail.getYear());
			changeSalaryDetailVo.setMonth(newStaffSalaryDetail.getMonth());
			changeStaffDetailSalaryTasks.add(changeSalaryDetailVo);
		}
		return changeStaffDetailSalaryTasks;
	}

	@Override
	public StaffSalaryDetailEntity getStaffSalaryDetailByInstanceId(String processInstanceId) throws Exception {
		String hql = "from EasyProcessEntity where processInstanceID="+processInstanceId;
		EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
		StaffSalaryDetailEntity newStaffSalaryDetail = (StaffSalaryDetailEntity) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
		StaffSalaryDetailEntity oldStaffSalaryDetail = getStaffSalaryDetailById(String.valueOf(newStaffSalaryDetail.getId()));
		newStaffSalaryDetail.setOldDeductionItemMap(oldStaffSalaryDetail.getDeductionItemMap());
		newStaffSalaryDetail.setOldSubsidyItemMap(oldStaffSalaryDetail.getSubsidyItemMap());
		DepartmentEntity department = departmentDao.getDepartmentByDepartmentID_(newStaffSalaryDetail.getDepartmentId());
		if(null != department){
			newStaffSalaryDetail.setDepName(department.getDepartmentName());
		}
		newStaffSalaryDetail.setStaffName(staffService.getStaffByUserId(newStaffSalaryDetail.getUserId()).getStaffName());
		return newStaffSalaryDetail;
	}

	@Override
	public void updateChangeSalaryDetailStatus(String result, String processInstanceID) {
		String hql = "update EasyProcessEntity set applyResult="+result+" where processInstanceID="+processInstanceID;
		baseDao.excuteHql(hql);
	}

	@Override
	public boolean checkHasApplyChangeSalaryDetail(String staffSalaryId) {
		String hql = "from StaffSalaryDetailEntity where id="+staffSalaryId;
		StaffSalaryDetailEntity staffSalary = (StaffSalaryDetailEntity) baseDao.hqlfindUniqueResult(hql);
		hql = "select count(id) from EasyProcessEntity where "
				+ "requestUserId='"+staffSalary.getUserId()+"' and isDeleted=0 and applyResult is null"
				+ " and businessType="+BusinessTypeEnum.CHANGE_SALARY_DETAIL.getValue()+" and year="+staffSalary.getYear()
				+" and month="+staffSalary.getMonth();
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hql)));
		if(count > 0){
			return true;
		}
		return false;
	}

	@Override
	public void updateChangeSalaryDetail(String processInstanceID) throws Exception {
		String hql = "from EasyProcessEntity where processInstanceID="+processInstanceID;	
		EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
		StaffSalaryDetailEntity newStaffSalaryDetail = (StaffSalaryDetailEntity) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
		hql = "from StaffSalaryDetailEntity where id="+newStaffSalaryDetail.getId();
		StaffSalaryDetailEntity oldStaffSalaryDetail = (StaffSalaryDetailEntity) baseDao.hqlfindUniqueResult(hql);
		oldStaffSalaryDetail.setOtherDeductionItems(ObjectByteArrTransformer.toByteArray(newStaffSalaryDetail.getDeductionItemMap()));
		oldStaffSalaryDetail.setOtherSubsidyItems(ObjectByteArrTransformer.toByteArray(newStaffSalaryDetail.getSubsidyItemMap()));
		oldStaffSalaryDetail.setPreTaxSalary(newStaffSalaryDetail.getPreTaxSalary());
		oldStaffSalaryDetail.setTotalMoney(newStaffSalaryDetail.getTotalMoney());
		oldStaffSalaryDetail.setPersonalIncomeTax(newStaffSalaryDetail.getPersonalIncomeTax());
		baseDao.hqlUpdate(oldStaffSalaryDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ChangeSalaryDetailVo> findChangeStaffSalaryList(Integer limit, Integer page,
			ChangeSalaryDetailVo changeSalaryDetailVo) {
		String hql = "select process from EasyProcessEntity process, StaffEntity staff where process.requestUserId=staff.userID and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.CHANGE_SALARY_DETAIL.getValue()+"\n";
		//在职
		if(StaffStatusEnum.JOB.getValue() == changeSalaryDetailVo.getStaffStatus()){
			hql += " and staff.status!=4\n";
		}else{
			hql += " and staff.status=4\n";
		}
		if(null != changeSalaryDetailVo.getYear()){
			hql += " and process.year="+changeSalaryDetailVo.getYear()+"\n";
		}
		if(null != changeSalaryDetailVo.getMonth()){
			hql += " and process.month="+changeSalaryDetailVo.getMonth()+"\n";
		}
		if(StringUtils.isNotBlank(changeSalaryDetailVo.getStatus())){
			if(Constants.PROGRESS.equals(changeSalaryDetailVo.getStatus())){
				hql += "and process.applyResult is null\n";
			}else{
				hql += "and process.applyResult="+changeSalaryDetailVo.getStatus()+"\n";
			}
		}
		if(StringUtils.isNotBlank(changeSalaryDetailVo.getRequestUserName())){
			hql += " and staffName like '%"+EscapeUtil.decodeSpecialChars(changeSalaryDetailVo.getRequestUserName())+"%'\n";
		}
		hql += "order by process.addTime desc";
		List<EasyProcessEntity> processList = (List<EasyProcessEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<ChangeSalaryDetailVo> changeSalaryDetailList = new ArrayList<>();
		for(EasyProcessEntity process: processList){
			ChangeSalaryDetailVo changeSalaryDetail = new ChangeSalaryDetailVo();
			changeSalaryDetail.setYear(process.getYear());
			changeSalaryDetail.setMonth(process.getMonth());
			String requestUserId = process.getRequestUserId();
			int departmentId = departmentDao.getDeparmentIdByUserId(requestUserId);
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID_(departmentId);
			changeSalaryDetail.setDepartment(department.getDepartmentName());
			changeSalaryDetail.setRequestUserName(staffService.getStaffByUserId(process.getRequestUserId()).getStaffName());
			changeSalaryDetail.setUserName(staffService.getStaffByUserId(process.getUserId()).getStaffName());
			changeSalaryDetail.setProcessInstanceID(process.getProcessInstanceID());
			changeSalaryDetail.setAddTime(process.getAddTime());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(changeSalaryDetail.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				changeSalaryDetail.setStatus("进行中");
				changeSalaryDetail.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = process.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						changeSalaryDetail.setStatus(t.getName());
				}
			}
			changeSalaryDetailList.add(changeSalaryDetail);
		}
		String hqlCount =  "select count(process.id) from EasyProcessEntity process, StaffEntity staff where process.requestUserId=staff.userID and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.CHANGE_SALARY_DETAIL.getValue()+"\n";
		//在职
		if(StaffStatusEnum.JOB.getValue() == changeSalaryDetailVo.getStaffStatus()){
			hqlCount += " and staff.status!=4\n";
		}else{
			hqlCount += " and staff.status=4\n";
		}
		if(null != changeSalaryDetailVo.getYear()){
			hqlCount += " and process.year="+changeSalaryDetailVo.getYear()+"\n";
		}
		if(null != changeSalaryDetailVo.getMonth()){
			hqlCount += " and process.month="+changeSalaryDetailVo.getMonth()+"\n";
		}
		if(StringUtils.isNotBlank(changeSalaryDetailVo.getStatus())){
			if(Constants.PROGRESS.equals(changeSalaryDetailVo.getStatus())){
				hqlCount += "and process.applyResult is null\n";
			}else{
				hqlCount += "and process.applyResult="+changeSalaryDetailVo.getStatus()+"\n";
			}
		}
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hqlCount)));
		return new ListResult<>(changeSalaryDetailList, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startSendSalary(String sendType, String staffSalaryIds, String userId) throws Exception {
		String hql = "from StaffSalaryDetailEntity where isDeleted=0 and id in("+staffSalaryIds+")\n";
		//1表示推送方式是短信；2表示推送方式是邮箱
		if("1".equals(sendType)){
			hql += "and mobileSend=-1";
		}else{
			hql += "and emailSend=-1";
		}
		List<StaffSalaryDetailEntity> staffSalarys = (List<StaffSalaryDetailEntity>) baseDao.hqlfind(hql);
		if("1".equals(sendType)){
			sendSalaryByMobile(staffSalarys, userId);
		}else{
			sendSalaryByEmail(staffSalarys, userId);
		}
	}

	private void sendSalaryByEmail(List<StaffSalaryDetailEntity> staffSalarys, String userId) throws Exception{
		String title="工资条推送提醒";
		EmailSender emailSender=EmailSender.getInstance();
		for(StaffSalaryDetailEntity staffSalary: staffSalarys){
			String staffUserId = staffSalary.getUserId();
			StaffEntity staff = staffService.getStaffByUserId(staffUserId);
			staffSalary.setStaffName(staff.getStaffName());
			String email = staff.getEmail();
			if(StringUtils.isNotBlank(email)){
				int year = staffSalary.getYear();
				int month = staffSalary.getMonth();
				String content="<h3>"+year+"年"+month+"月份工资条基础信息，具体明细请登录OA系统查看！</h3>";
				List<String> salaryDetail = getSalaryDetail(staffSalary);
				StringBuilder sb = new StringBuilder("<table style=\"border:1px solid #000\">");
				sb.append("<tr><td style=\"border:1px solid #000\">");
				sb.append(StringUtils.join(TITLES,"</td><td style=\"border:1px solid #000\">"));
				sb.append("</td></tr>");
				sb.append("<tr>");
				sb.append("<td style=\"border:1px solid #000\" >");
				sb.append(staff.getStaffName());
				sb.append("</td><td style=\"border:1px solid #000\">");
				sb.append(StringUtils.join(salaryDetail,"</td ><td style=\"border:1px solid #000\">"));
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</table>");
				emailSender.sendEmail(email,title,content+sb.toString());
				//更新状态
				String hql = "update StaffSalaryDetailEntity set emailSend=1, emailSendOperator='"+userId+"' where id="+staffSalary.getId();
				baseDao.excuteHql(hql);
			}
		}
	}

	private List<String> getSalaryDetail(StaffSalaryDetailEntity staffSalary) {
		List<String> salaryDetails = new ArrayList<>();
		salaryDetails.add(String.valueOf(staffSalary.getBasicSalary()));
		salaryDetails.add(String.valueOf(staffSalary.getPerformanceSalary()));
		salaryDetails.add("");
		salaryDetails.add(String.valueOf(staffSalary.getOvertimeSubsidy()==null ? "":staffSalary.getOvertimeSubsidy()));
		salaryDetails.add(String.valueOf(staffSalary.getFullAttendance()==null ? "":staffSalary.getFullAttendance()));
		salaryDetails.add(String.valueOf(staffSalary.getDeductibleMoney()==null ? "":staffSalary.getDeductibleMoney()));
		salaryDetails.add(String.valueOf(staffSalary.getOtherDeduction()==null ? "":staffSalary.getOtherDeduction()));
		salaryDetails.add(String.valueOf(staffSalary.getLateMoney()==null ? "":staffSalary.getLateMoney()));
		salaryDetails.add(String.valueOf(staffSalary.getPension()==null ? "":staffSalary.getPension()));
		salaryDetails.add(String.valueOf(staffSalary.getMedicalInsurance()==null ? "":staffSalary.getMedicalInsurance()));
		salaryDetails.add(String.valueOf(staffSalary.getUnemployment()==null ? "":staffSalary.getUnemployment()));
		salaryDetails.add(String.valueOf(staffSalary.getSeriousIllness()==null ? "":staffSalary.getSeriousIllness()));
		salaryDetails.add(String.valueOf(staffSalary.getPublicFund()==null ? "":staffSalary.getPublicFund()));
		double personalIncomeTax = staffSalary.getPersonalIncomeTax();
		double preTaxSalary = staffSalary.getPreTaxSalary();
		double afterTaxSalary = NumberUtil.Rounding(preTaxSalary - personalIncomeTax, 2);
		salaryDetails.add(String.valueOf(personalIncomeTax));
		salaryDetails.add(String.valueOf(afterTaxSalary));
		return salaryDetails;
	}

	private void sendSalaryByMobile(List<StaffSalaryDetailEntity> staffSalarys, String userId) throws Exception{
		ShortMsgSender shortMsgSender=ShortMsgSender.getInstance();
		for(StaffSalaryDetailEntity staffSalary: staffSalarys){
			//被推送员工
			String staffUserId = staffSalary.getUserId();
			StaffEntity staff = staffService.getStaffByUserId(staffUserId);
			if(StringUtils.isNotBlank(staff.getTelephone())){
				String name = "";
				if("男".equals(staff.getGender())){
					name = staff.getStaffName()+"先生";
				}else{
					name = staff.getStaffName()+"女士";
				}
				String content="【智造链】 尊敬的"+name+"，您的"+staffSalary.getYear()+"年"+
						staffSalary.getMonth()+"月的工资条，已发送至oa系统，请您及时登录确认。";
				shortMsgSender.send(staff.getTelephone(),content);
				//更新状态
				String hql = "update StaffSalaryDetailEntity set mobileSend=1,  mobileSendOperator='"+userId+"' where id="+staffSalary.getId();
				baseDao.excuteHql(hql);
			}
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public InputStream exportStaffSalarys(String rootPath, StaffSalaryDetailEntity staffDetail) throws Exception {
		FileInputStream fis = new FileInputStream(rootPath+Constants.SALARY_TEMPLATE);
		Workbook workbook = WorkbookFactory.create(fis);
		Font font = workbook.createFont();    
		font.setFontName("宋体");    
		font.setFontHeightInPoints((short) 10);// 字体大小    
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);// 自动换行 
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		String title = "工    资   结   算    单   "+staffDetail.getYear()+"年"+staffDetail.getMonth()+"月份";
		//写入标题
		Row titleRow = sheet.getRow(0);
		Cell titleCell = titleRow.getCell(0);
		titleCell.setCellValue(title);
		List<StaffSalaryDetailEntity> staffSalaryDetails = getStaffSalaryDetails(staffDetail);
		int index = 1;
		//从第5行开始
		int rowNum = 4;
		for(StaffSalaryDetailEntity staffSalaryDetail: staffSalaryDetails){
			String userId = staffSalaryDetail.getUserId();
			StaffEntity staff = staffService.getStaffByUserId(userId);
			//序号
			Row row = sheet.createRow(rowNum);
			Cell indexCell = row.createCell(0);
			indexCell.setCellValue(index);
			indexCell.setCellStyle(cellStyle);
			//部门
			Cell depCell = row.createCell(1);
			CompanyEntity  company = companyDao.getCompanyByCompanyID(staffSalaryDetail.getCompanyId());
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID_(staffSalaryDetail.getDepartmentId());
			if(null!=company && null!=department){
				depCell.setCellValue(company.getCompanyName()+"-"+department.getDepartmentName());
			}
			depCell.setCellStyle(cellStyle);
			//姓名
			Cell staffNameCell = row.createCell(2);
			staffNameCell.setCellValue(staff.getStaffName());
			staffNameCell.setCellStyle(cellStyle);
			//工号
			Cell staffNumCell = row.createCell(3);
			String staffNum = staffDao.getStaffNum(userId);
			staffNumCell.setCellValue(staffNum);
			staffNumCell.setCellStyle(cellStyle);
			//银行卡号
			Cell bankAccountCell = row.createCell(4);
			bankAccountCell.setCellValue(handleIfNull(staff.getBankAccount()));
			bankAccountCell.setCellStyle(cellStyle);
			//开户行
			Cell bankCell = row.createCell(5);
			bankCell.setCellValue(handleIfNull(staff.getBank()));
			bankCell.setCellStyle(cellStyle);
			//试用/正式/离职
			Cell staffStatusCell = row.createCell(6);
			StaffStatusEnum staffStatus = StaffStatusEnum.valueOf(staff.getStatus());
			staffStatusCell.setCellValue(staffStatus.getName());
			staffStatusCell.setCellStyle(cellStyle);
			//薪资标准
			Cell standardSalaryCell = row.createCell(7);
			standardSalaryCell.setCellValue(handleIfNull(staffSalaryDetail.getStandardSalary()));
			standardSalaryCell.setCellStyle(cellStyle);
			//应出勤
			Cell attendanceDaysCell = row.createCell(8);
			attendanceDaysCell.setCellValue(handleIfNull(staffSalaryDetail.getAttendanceDays()));
			attendanceDaysCell.setCellStyle(cellStyle);
			//实际出勤
			Cell actualAttendanceDaysCell = row.createCell(9);
			actualAttendanceDaysCell.setCellValue(handleIfNull(staffSalaryDetail.getActualAttendanceDays()));
			actualAttendanceDaysCell.setCellStyle(cellStyle);
			//基础工资
			Cell basicSalary = row.createCell(10);
			basicSalary.setCellValue(handleIfNull(staffSalaryDetail.getBasicSalary()));
			basicSalary.setCellStyle(cellStyle);
			//绩效奖金
			Cell performanceSalary = row.createCell(11);
			performanceSalary.setCellValue(handleIfNull(staffSalaryDetail.getPerformanceSalary()));
			performanceSalary.setCellStyle(cellStyle);
			//白天加班时长
			Cell dayOvertimeHours = row.createCell(12);
			dayOvertimeHours.setCellValue(handleIfNull(staffSalaryDetail.getDayOvertimeHours()));
			dayOvertimeHours.setCellStyle(cellStyle);
			//晚上加班小时
			Cell nightOvertimeHoursCell = row.createCell(13);
			nightOvertimeHoursCell.setCellValue(handleIfNull(staffSalaryDetail.getNightOvertimeHours()));
			nightOvertimeHoursCell.setCellStyle(cellStyle);
			//加班工资
			Cell overtimeSubsidyCell = row.createCell(14);
			overtimeSubsidyCell.setCellValue(handleIfNull(staffSalaryDetail.getOvertimeSubsidy()));
			overtimeSubsidyCell.setCellStyle(cellStyle);
			//满勤
			Cell fullAttendaceCell = row.createCell(15);
			fullAttendaceCell.setCellValue(handleIfNull(staffSalaryDetail.getFullAttendance()));
			fullAttendaceCell.setCellStyle(cellStyle);
			//抵减小时
			Cell deductibleHoursCell = row.createCell(16);
			deductibleHoursCell.setCellValue(handleIfNull(staffSalaryDetail.getDeductibleHours()));
			deductibleHoursCell.setCellStyle(cellStyle);
			//应抵减金额
			Cell deductibleMoneyCell = row.createCell(17);
			deductibleMoneyCell.setCellValue(handleIfNull(staffSalaryDetail.getDeductibleMoney()));
			deductibleMoneyCell.setCellStyle(cellStyle);
			//未刷卡次数
			Cell noPunchTimesCell = row.createCell(18);
			noPunchTimesCell.setCellValue(handleIfNull(staffSalaryDetail.getNoPunchTimes()));
			noPunchTimesCell.setCellStyle(cellStyle);
			//迟到次数	
			Cell lateTimesCell = row.createCell(19);
			lateTimesCell.setCellValue(handleIfNull(staffSalaryDetail.getLateTimes()));
			lateTimesCell.setCellStyle(cellStyle);
			//迟到分钟
			Cell lateMinutesCell = row.createCell(20);
			lateMinutesCell.setCellValue(handleIfNull(staffSalaryDetail.getLateMinutes()));
			lateMinutesCell.setCellStyle(cellStyle);
			//迟到扣款	
			Cell lateMoneyCell = row.createCell(21);
			lateMoneyCell.setCellValue(handleIfNull(staffSalaryDetail.getLateMoney()));
			lateMoneyCell.setCellStyle(cellStyle);
			//奖励
			Cell rewardCell = row.createCell(22);
			rewardCell.setCellValue(handleIfNull(staffSalaryDetail.getReward()));
			rewardCell.setCellStyle(cellStyle);
			//行政处罚
			Cell penaltyCell = row.createCell(23);
			penaltyCell.setCellValue(handleIfNull(staffSalaryDetail.getPenalty()));
			penaltyCell.setCellStyle(cellStyle);
			//其它扣除	
			Cell otherDeductionCell = row.createCell(24);
			if(null != staffSalaryDetail.getOtherDeductionItems()){
				Map<String, Double> otherDeductionItems = (Map<String, Double>) ObjectByteArrTransformer.toObject(staffSalaryDetail.getOtherDeductionItems());
				if(otherDeductionItems.size()>0){
					Collection<Double> deductionMoneys = otherDeductionItems.values();
					double totalDeductionMoney = 0;
					for(Double deductionMoney: deductionMoneys){
						totalDeductionMoney += deductionMoney;
					}
					if(totalDeductionMoney>0){
						otherDeductionCell.setCellValue(NumberUtil.Rounding(totalDeductionMoney, 2));
					}
				}
			}
			otherDeductionCell.setCellStyle(cellStyle);
			//其它补贴
			Cell otherSubsidyCell = row.createCell(25);
			if(null != staffSalaryDetail.getOtherSubsidyItems()){
				Map<String, Double> otherSubsidyItems = (Map<String, Double>) ObjectByteArrTransformer.toObject(staffSalaryDetail.getOtherSubsidyItems());
				if(otherSubsidyItems.size()>0){
					Collection<Double> subsidyMoneys = otherSubsidyItems.values();
					double totalSubsidyMoney = 0;
					for(Double subsidyMoney: subsidyMoneys){
						totalSubsidyMoney += subsidyMoney;
					}
					if(totalSubsidyMoney>0){
						otherSubsidyCell.setCellValue(NumberUtil.Rounding(totalSubsidyMoney, 2));
					}
				}
			}
			
			otherSubsidyCell.setCellStyle(cellStyle);
			//应发工资合计
			Cell totalMoneyCell = row.createCell(26);
			totalMoneyCell.setCellValue(staffSalaryDetail.getTotalMoney());
			totalMoneyCell.setCellStyle(cellStyle);
			//扣养老	
			Cell pensionCell = row.createCell(27);
			pensionCell.setCellValue(handleIfNull(staffSalaryDetail.getPension()));
			pensionCell.setCellStyle(cellStyle);
			//扣医保	
			Cell medicalInsuranceCell = row.createCell(28);
			medicalInsuranceCell.setCellValue(handleIfNull(staffSalaryDetail.getMedicalInsurance()));
			medicalInsuranceCell.setCellStyle(cellStyle);
			//扣失业	
			Cell unemploymentCell = row.createCell(29);
			unemploymentCell.setCellValue(handleIfNull(staffSalaryDetail.getUnemployment()));
			unemploymentCell.setCellStyle(cellStyle);
			//大病	
			Cell seriousIllnessCell = row.createCell(30);
			seriousIllnessCell.setCellValue(handleIfNull(staffSalaryDetail.getSeriousIllness()));
			seriousIllnessCell.setCellStyle(cellStyle);
			//扣住房公积金	
			Cell publicFundCell = row.createCell(31);
			publicFundCell.setCellValue(handleIfNull(staffSalaryDetail.getPublicFund()));
			publicFundCell.setCellStyle(cellStyle);
			//扣个税	
			Cell personalIncomeTaxCell = row.createCell(32);
			personalIncomeTaxCell.setCellValue(handleIfNull(staffSalaryDetail.getPersonalIncomeTax()));
			personalIncomeTaxCell.setCellStyle(cellStyle);
			//应扣项目合计	
			Cell totalDeductionCell = row.createCell(33);
			//应扣项目的合计
			double totalDeduction = (staffSalaryDetail.getPersonalpay()==null ? 0:staffSalaryDetail.getPersonalpay())
					+(staffSalaryDetail.getPersonalPayFund()==null ? 0:staffSalaryDetail.getPersonalPayFund()) +
					(staffSalaryDetail.getPersonalIncomeTax()==null ? 0:staffSalaryDetail.getPersonalIncomeTax());
			totalDeductionCell.setCellValue(handleIfNull(NumberUtil.Rounding(totalDeduction, 2)));
			totalDeductionCell.setCellStyle(cellStyle);
			//个人社保	
			Cell personalPayCell = row.createCell(34);
			personalPayCell.setCellValue(handleIfNull(staffSalaryDetail.getPersonalpay()));
			personalPayCell.setCellStyle(cellStyle);
			//公司社保	
			Cell companyPayCell = row.createCell(35);
			companyPayCell.setCellValue(handleIfNull(staffSalaryDetail.getCompanyPay()));
			companyPayCell.setCellStyle(cellStyle);
			//个人公积金
			Cell personalPayFundCell = row.createCell(36);
			personalPayFundCell.setCellValue(handleIfNull(staffSalaryDetail.getPersonalPayFund()));
			personalPayFundCell.setCellStyle(cellStyle);
			//公司公积金
			Cell companyPayFundCell = row.createCell(37);
			companyPayFundCell.setCellValue(handleIfNull(staffSalaryDetail.getCompanyPayFund()));
			companyPayFundCell.setCellStyle(cellStyle);
			//税前工资
			double preTaxSalary = staffSalaryDetail.getPreTaxSalary();
			Cell preTaxSalaryCell = row.createCell(38);
			preTaxSalaryCell.setCellValue(preTaxSalary);
			preTaxSalaryCell.setCellStyle(cellStyle);
			double personalIncomeTax = staffSalaryDetail.getPersonalIncomeTax();
			//税后工资
			double afterTaxSalary = NumberUtil.Rounding(preTaxSalary-personalIncomeTax, 2);
			Cell afterTaxSalaryCell = row.createCell(39);
			afterTaxSalaryCell.setCellValue(afterTaxSalary);
			afterTaxSalaryCell.setCellStyle(cellStyle);
			//备注
			Cell remarkCell = row.createCell(40);
			byte[] object = staffSalaryDetail.getRemarks();
			if(null != object){
				List<String> remarkList = (List<String>) ObjectByteArrTransformer.toObject(object);
				String remarks = StringUtils.join(remarkList, "/r/n");
				remarkCell.setCellValue(remarks);
			}
			remarkCell.setCellStyle(cellStyle);
			index++;
			rowNum++;
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

	@SuppressWarnings("unchecked")
	private List<StaffSalaryDetailEntity> getStaffSalaryDetails(StaffSalaryDetailEntity staffDetail) {
		String hql = "SELECT\n" +
				"	salary\n" +
				"FROM\n" +
				"	StaffSalaryDetailEntity salary,\n" +
				"	DepartmentEntity dep,\n" +
				"   CompanyEntity com,\n" +
				"	StaffEntity staff\n" +
				"WHERE\n" +
				"	salary.preTaxSalary is not null and salary.isDeleted = 0\n" +
				"AND staff.isDeleted = 0\n" +
				"and salary.departmentId = dep.departmentID\n" +
				"and salary.companyId = com.companyID\n" +
				"and salary.userId = staff.userID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.month = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			hql += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getMobileSend()){
			hql += "and ifNull(mobileSend, 0)="+staffDetail.getMobileSend()+"\n";
		}
		if(null != staffDetail.getEmailSend()){
			hql += "and ifNull(emailSend, 0)="+staffDetail.getEmailSend()+"\n";
		}
		if(null != staffDetail.getCompanyId()){
			hql += "and com.companyID="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				hql += "and staff.status!=4\n";
			}else{
				hql += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			hql += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			hql+=" and dep.departmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		hql += " order by salary.companyId, salary.departmentId";
		return (List<StaffSalaryDetailEntity>) baseDao.hqlfind(hql);
	}
	private String handleIfNull(Object value){
		if(null == value){
			return "";
		}else{
			return String.valueOf(value);
		}
	}

	@Override
	public ListResult<Object> findStaffSalaryPayInfos(StaffSalaryDetailEntity staffDetail, Integer limit,
			Integer page) {
		String sql = "SELECT\n" +
				"	StaffName, concat(companyName,'-',DepartmentName), payStatus," +
				" staff.status, staff.positionCategory,salary.id\n" +
				"FROM\n" +
				"	oa_staffsalarydetail salary,\n" +
				"	oa_department dep,\n" +
				"   oa_company com,\n" +
				"	oa_staff staff\n" +
				"WHERE\n" +
				"	salary.mobileSend = 1 and salary.isDeleted = 0\n" +
				"AND staff.IsDeleted = 0\n" +
				"and salary.departmentId = dep.DepartmentID\n" +
				"and salary.companyId = com.companyId\n" +
				"and salary.userId = staff.UserID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.`month` = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			sql += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getPayStatus()){
			sql += "and payStatus="+staffDetail.getPayStatus()+"\n";
		}
		if(null != staffDetail.getCompanyId()){
			sql += "and com.companyId="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				sql += "and staff.status!=4\n";
			}else{
				sql += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			sql += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sql+=" and dep.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		sql += " order by salary.companyId, salary.departmentId";
		List<Object> staffSalarys = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	oa_staffsalarydetail salary,\n" +
				"	oa_staff staff,\n" +
				"	oa_department dep,\n" +
				"   oa_company com\n" +
				"WHERE\n" +
				"	salary.mobileSend = 1 and salary.isDeleted = 0\n" +
				"and salary.departmentId = dep.DepartmentID\n" +
				"and salary.companyId = com.companyId\n" +
				"and salary.userId = staff.UserID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.`month` = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			sqlCount += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getPayStatus()){
			sqlCount += "and payStatus="+staffDetail.getPayStatus()+"\n";
		}
		if(null != staffDetail.getCompanyId()){
			sqlCount += "and com.companyId="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				sqlCount += "and staff.status!=4\n";
			}else{
				sqlCount += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			sqlCount += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sqlCount+=" and dep.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sqlCount)));
		return new ListResult<>(staffSalarys, count);
	}

	@Override
	public void allApplyPaySalary(StaffSalaryDetailEntity staffDetail, String userId) throws Exception {
		String sql = "SELECT\n" +
				"salary.id\n" +
				"FROM\n" +
				"	oa_staffsalarydetail salary,\n" +
				"	oa_department dep,\n" +
				"   oa_company com,\n" +
				"	oa_staff staff\n" +
				"WHERE\n" +
				"	salary.mobileSend = 1 and salary.isDeleted = 0 and payStatus="+SalaryPayEnum.NOT_APPLY.getValue()+"\n" +
				"AND staff.IsDeleted = 0\n" +
				"and salary.departmentId = dep.DepartmentID\n" +
				"and salary.companyId = com.companyId\n" +
				"and salary.userId = staff.UserID\n" +
				"and salary.year = "+staffDetail.getYear()+"\n" +
				"and salary.`month` = "+staffDetail.getMonth()+"\n";
		if(StringUtils.isNotBlank(staffDetail.getStaffName())){
			sql += "and staffName like '%"+EscapeUtil.decodeSpecialChars(staffDetail.getStaffName())+"%'\n";
		}
		if(null != staffDetail.getCompanyId()){
			sql += "and com.companyId="+staffDetail.getCompanyId()+"\n";
		}
		if(StringUtils.isNotBlank(staffDetail.getStaffStatus())){
			//在职
			if(StaffStatusEnum.JOB.getValue() == Integer.parseInt(staffDetail.getStaffStatus())){
				sql += "and staff.status!=4\n";
			}else{
				sql += "and staff.status=4\n";
			}
		}
		if(StringUtils.isNotBlank(staffDetail.getJobType())){
			sql += "and staff.positionCategory="+staffDetail.getJobType()+"\n";
		}
		if(null != staffDetail.getDepartmentId()){
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
					staffDetail.getCompanyId(), staffDetail.getDepartmentId());
			List<Integer> departmentIDs = Lists2.transform(departmentVOs,
					new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(staffDetail.getDepartmentId());
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sql+=" and dep.DepartmentID in ("
					+ arrayString.substring(1, arrayString.length() - 1) + ")";
		}
		List<Object> staffSalaryIdObjs = baseDao.findBySql(sql);
		String staffSalaryIds = StringUtils.join(staffSalaryIdObjs, ",");
		startApplyPaySalary(staffSalaryIds, userId, staffDetail);
	}
	@Override
	public void startApplyPaySalary(String staffSalaryIds, String userId, StaffSalaryDetailEntity staffDetail) throws Exception{
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> remitMoneyUsers = permissionService
				.findUsersByPermissionCode(Constants.REMIT_MONEY);
		if (CollectionUtils.isEmpty(remitMoneyUsers)) {
			throw new RuntimeException("未找到该申请的财务打款人！");
		}
		vars.put("auditor", null);
		vars.put("auditors", remitMoneyUsers);
		vars.put("auditGroups", new ArrayList<>());
		vars.put("processType", Constants.PAY_SALARY);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EASY_PROCESS);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		taskService.complete(task.getId(), vars);
		EasyProcessEntity easyProcess = new EasyProcessEntity();
		easyProcess.setData(ObjectByteArrTransformer.toByteArray(staffSalaryIds));
		easyProcess.setUserId(userId);
		easyProcess.setAddTime(new Date());
		easyProcess.setProcessInstanceID(processInstance.getId());
		easyProcess.setBusinessType(BusinessTypeEnum.PAY_SALARY.getValue());
		easyProcess.setYear(staffDetail.getYear());
		easyProcess.setMonth(staffDetail.getMonth());
		baseDao.hqlSave(easyProcess);
		//更新工资发放的状态
		updateStaffSalaryPayStatus(staffSalaryIds, SalaryPayEnum.APPLY.getValue());
	}
	@Override
	public void updateStaffSalaryPayStatus(String staffSalaryIds, int value) {
		String hql = "update StaffSalaryDetailEntity set payStatus="+value+" where id in("+staffSalaryIds+")";
		baseDao.excuteHql(hql);
	}

	@Override
	public List<PaySalaryTaskVo> findPaySalaryTaskVos(List<Task> paySalaryTasks) {
		List<PaySalaryTaskVo> paySalaryTaskVos = new ArrayList<>();
		for(Task paySalaryTask: paySalaryTasks){
			PaySalaryTaskVo paySalaryTaskVo = new PaySalaryTaskVo();
			String hql = "from EasyProcessEntity where processInstanceID="+paySalaryTask.getProcessInstanceId();
			EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
			paySalaryTaskVo.setProcessInstanceID(easyProcessEntity.getProcessInstanceID());
			StaffEntity staff = staffService.getStaffByUserId(easyProcessEntity.getUserId());
			paySalaryTaskVo.setUserName(staff.getStaffName());
			paySalaryTaskVo.setTaskId(paySalaryTask.getId());
			paySalaryTaskVo.setAddTime(easyProcessEntity.getAddTime());
			paySalaryTaskVo.setYear(easyProcessEntity.getYear());
			paySalaryTaskVo.setMonth(easyProcessEntity.getMonth());
			paySalaryTaskVos.add(paySalaryTaskVo);
		}
		return paySalaryTaskVos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaySalaryTaskVo> getPaySalaryInfosByInstanceId(String processInstanceId) throws Exception {
		String hql = "from EasyProcessEntity where processInstanceID="+processInstanceId;
		EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
		String staffSalaryIds = (String) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
		hql = "from StaffSalaryDetailEntity where id in("+staffSalaryIds+") order by companyId, departmentId";
		List<StaffSalaryDetailEntity> staffSalarys = (List<StaffSalaryDetailEntity>) baseDao.hqlfind(hql);
		List<PaySalaryTaskVo> paySalaryInfos = new ArrayList<>();
		for(StaffSalaryDetailEntity staffSalary: staffSalarys){
			PaySalaryTaskVo paySalaryTaskVo = new PaySalaryTaskVo();
			String userId = staffSalary.getUserId();
			StaffEntity staff = staffService.getStaffByUserId(userId);
			paySalaryTaskVo.setUserName(staff.getStaffName());
			paySalaryTaskVo.setStaffNum(staffDao.getStaffNum(userId));
			CompanyEntity  company = companyDao.getCompanyByCompanyID(staffSalary.getCompanyId());
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID_(staffSalary.getDepartmentId());
			if(null != company ){
				paySalaryTaskVo.setCompany(company.getCompanyName());
			}
			if(null != department){
				paySalaryTaskVo.setDepartment(department.getDepartmentName());
			}
			double preTaxSalary = staffSalary.getPreTaxSalary();
			double personalIncomeTax = staffSalary.getPersonalIncomeTax();
			double afterTaxSalary = NumberUtil.Rounding(preTaxSalary - personalIncomeTax, 2);
			paySalaryTaskVo.setPreTaxSalary(preTaxSalary);
			paySalaryTaskVo.setPersonalIncomeTax(personalIncomeTax);
			paySalaryTaskVo.setAfterTaxSalary(afterTaxSalary);
			paySalaryTaskVo.setSalaryId(staffSalary.getId());
			paySalaryInfos.add(paySalaryTaskVo);
		}
		return paySalaryInfos;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream exportPaySalarys(String processInstanceId) throws Exception {
		List<PaySalaryTaskVo> paySalaryInfos = getPaySalaryInfosByInstanceId(processInstanceId);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("工资单");
		Font font = workbook.createFont();    
		font.setFontName("宋体");    
		font.setFontHeightInPoints((short) 10);// 字体大小    
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);// 自动换行 
		//写入标题
		Row titleRow = sheet.createRow(0);
		String[] titles = {"序号","姓名","工号","公司","部门","税前工资","个税","税后工资"};
		for(int i=0; i<titles.length; i++){
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(cellStyle);
		}
		int index = 1;
		for(PaySalaryTaskVo paySalaryInfo: paySalaryInfos){
			Row row = sheet.createRow(index);
			Cell indexCell = row.createCell(0);
			indexCell.setCellValue(index);
			indexCell.setCellStyle(cellStyle);
			Cell nameCell = row.createCell(1);
			nameCell.setCellValue(paySalaryInfo.getUserName());
			nameCell.setCellStyle(cellStyle);
			Cell numCell = row.createCell(2);
			numCell.setCellValue(paySalaryInfo.getStaffNum());
			numCell.setCellStyle(cellStyle);
			Cell companyCell = row.createCell(3);
			companyCell.setCellValue(paySalaryInfo.getCompany());
			companyCell.setCellStyle(cellStyle);
			Cell depCell = row.createCell(4);
			depCell.setCellValue(handleIfNull(paySalaryInfo.getDepartment()));
			depCell.setCellStyle(cellStyle);
			Cell preTaxCell = row.createCell(5);
			preTaxCell.setCellValue(paySalaryInfo.getPreTaxSalary());
			preTaxCell.setCellStyle(cellStyle);
			Cell personalIncomeTaxCell = row.createCell(6);
			personalIncomeTaxCell.setCellValue(paySalaryInfo.getPersonalIncomeTax());
			personalIncomeTaxCell.setCellStyle(cellStyle);
			Cell afterTaxCell = row.createCell(7);
			afterTaxCell.setCellValue(paySalaryInfo.getAfterTaxSalary());
			afterTaxCell.setCellStyle(cellStyle);
			index++;
		}
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

	@Override
	public void updatePaySalaryApplyStatus(String processInstanceId, int value) {
		String hql = "update EasyProcessEntity set applyResult="+value+" where processInstanceID="+processInstanceId;
		baseDao.excuteHql(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<PaySalaryTaskVo> findPaySalaryApplyList(Integer limit, Integer page,
			PaySalaryTaskVo paySalaryVo) {
		String hql = "select process from EasyProcessEntity process, StaffEntity staff where process.userId=staff.userID and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.PAY_SALARY.getValue()+"\n";
		if(null != paySalaryVo.getYear()){
			hql += " and process.year="+paySalaryVo.getYear()+"\n";
		}
		if(null != paySalaryVo.getMonth()){
			hql += " and process.month="+paySalaryVo.getMonth()+"\n";
		}
		if(StringUtils.isNotBlank(paySalaryVo.getStatus())){
			if(Constants.PROGRESS.equals(paySalaryVo.getStatus())){
				hql += "and process.applyResult is null\n";
			}else{
				hql += "and process.applyResult="+paySalaryVo.getStatus()+"\n";
			}
		}
		hql += "order by process.addTime desc";
		List<EasyProcessEntity> processList = (List<EasyProcessEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<PaySalaryTaskVo> paySalaryApplyList = new ArrayList<>();
		for(EasyProcessEntity process: processList){
			PaySalaryTaskVo paySalaryTaskVo = new PaySalaryTaskVo();
			paySalaryTaskVo.setYear(process.getYear());
			paySalaryTaskVo.setMonth(process.getMonth());
			paySalaryTaskVo.setUserName(staffService.getStaffByUserId(process.getUserId()).getStaffName());
			paySalaryTaskVo.setProcessInstanceID(process.getProcessInstanceID());
			paySalaryTaskVo.setAddTime(process.getAddTime());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(paySalaryTaskVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				Task task=taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
				paySalaryTaskVo.setStatus("进行中");
				List<IdentityLink> idList =taskService.getIdentityLinksForTask(task.getId());
				List<String> nameArr = new ArrayList<>();
				for (IdentityLink identityLink : idList) {
					StaffEntity staff = staffDao.getStaffByUserID(identityLink.getUserId());
					if(null != staff){
						nameArr.add(staff.getStaffName());
					}
				}
				paySalaryTaskVo.setAssigneeUserName("【财务打款确认】"+StringUtils.join(nameArr,","));
			} else {
				Integer value_ = process.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						paySalaryTaskVo.setStatus(t.getName());
				}
			}
			paySalaryApplyList.add(paySalaryTaskVo);
		}
		String hqlCount =  "select count(process.id) from EasyProcessEntity process, StaffEntity staff where process.userId=staff.userID and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.PAY_SALARY.getValue()+"\n";
		if(null != paySalaryVo.getYear()){
			hqlCount += " and process.year="+paySalaryVo.getYear()+"\n";
		}
		if(null != paySalaryVo.getMonth()){
			hqlCount += " and process.month="+paySalaryVo.getMonth()+"\n";
		}
		if(StringUtils.isNotBlank(paySalaryVo.getStatus())){
			if(Constants.PROGRESS.equals(paySalaryVo.getStatus())){
				hqlCount += "and process.applyResult is null\n";
			}else{
				hqlCount += "and process.applyResult="+paySalaryVo.getStatus()+"\n";
			}
		}
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hqlCount)));
		return new ListResult<>(paySalaryApplyList, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<RewardAndPunishmentVo> findRewardAndPunishmentList(Integer limit, Integer page,
			RewardAndPunishmentVo rewardAndPunishmentVo) throws Exception {
		String hql = "select distinct process from EasyProcessEntity process, StaffEntity staff where locate(staff.userID, process.requestUserId)>0 and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.REWARD_PUNISHMENT.getValue()+"\n";
		if(StringUtils.isNotBlank(rewardAndPunishmentVo.getStatus())){
			if(Constants.PROGRESS.equals(rewardAndPunishmentVo.getStatus())){
				hql += "and process.applyResult is null\n";
			}else{
				hql += "and process.applyResult="+rewardAndPunishmentVo.getStatus()+"\n";
			}
		}
		if(StringUtils.isNotBlank(rewardAndPunishmentVo.getRequestUserNames())){
			hql += " and staffName like '%"+EscapeUtil.decodeSpecialChars(rewardAndPunishmentVo.getRequestUserNames())+"%'\n";
		}
		hql += "order by process.addTime desc";
		List<EasyProcessEntity> processList = (List<EasyProcessEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<RewardAndPunishmentVo> rewardAndPunishmentList = new ArrayList<>();
		for(EasyProcessEntity process: processList){
			RewardAndPunishmentVo rewardAndPunishment = new RewardAndPunishmentVo();
			String requestUserIdStr = process.getRequestUserId();
			String[] requestUserIds = requestUserIdStr.split(",");
			List<String> requestUserNames = new ArrayList<>(requestUserIds.length);
			for(String requestUserId: requestUserIds){
				StaffEntity staff = staffDao.getStaffByUserID(requestUserId);
				requestUserNames.add(staff.getStaffName());
			}
			rewardAndPunishment.setRequestUserNames(StringUtils.join(requestUserNames, ","));
			rewardAndPunishment.setUserName(staffDao.getStaffByUserID(process.getUserId()).getStaffName());
			RewardAndPunishmentVo rewardAndPunishmentData = (RewardAndPunishmentVo) ObjectByteArrTransformer.toObject(process.getData());
			rewardAndPunishment.setType(rewardAndPunishmentData.getType());
			rewardAndPunishment.setMoney(rewardAndPunishmentData.getMoney());
			rewardAndPunishment.setReason(rewardAndPunishmentData.getReason());
			rewardAndPunishment.setEffectiveDate(rewardAndPunishmentData.getEffectiveDate());
			String attachmentIdStr = rewardAndPunishmentData.getAttachmentIds();
			if(StringUtils.isNotBlank(attachmentIdStr)){
				rewardAndPunishment.setAttachmentIds(attachmentIdStr);
				String[] attachmentIds = attachmentIdStr.split(",");
				List<String> attachmentNames = new ArrayList<>(attachmentIds.length);
				for(String attachmentId: attachmentIds){
					CommonAttachment attach = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
					attachmentNames.add(attach.getSoftName());
				}
				rewardAndPunishment.setAttachmentNames(StringUtils.join(attachmentNames, ","));
			}
			rewardAndPunishment.setProcessInstanceID(process.getProcessInstanceID());
			rewardAndPunishment.setAddTime(process.getAddTime());

			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(rewardAndPunishment.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				rewardAndPunishment.setStatus("进行中");
				rewardAndPunishment.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = process.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						rewardAndPunishment.setStatus(t.getName());
				}
			}
			rewardAndPunishmentList.add(rewardAndPunishment);
		}
		String hqlCount =  "select count(distinct process.id) from EasyProcessEntity process, StaffEntity staff where locate(staff.userID, process.requestUserId)>0 and process.isDeleted=0 and staff.isDeleted=0 and\n"
				+ " businessType="+BusinessTypeEnum.REWARD_PUNISHMENT.getValue()+"\n";
		if(StringUtils.isNotBlank(rewardAndPunishmentVo.getStatus())){
			if(Constants.PROGRESS.equals(rewardAndPunishmentVo.getStatus())){
				hqlCount += "and process.applyResult is null\n";
			}else{
				hqlCount += "and process.applyResult="+rewardAndPunishmentVo.getStatus()+"\n";
			}
		}
		if(StringUtils.isNotBlank(rewardAndPunishmentVo.getRequestUserNames())){
			hqlCount += " and staffName like '%"+EscapeUtil.decodeSpecialChars(rewardAndPunishmentVo.getRequestUserNames())+"%'\n";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hqlCount)));
		return new ListResult<>(rewardAndPunishmentList, count);
	}

	@Override
	public void startRewardAndPunishment(File[] attachment, String[] attachmentFileName,
			RewardAndPunishmentVo rewardAndPunishmentVo) throws Exception {
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.PM_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						Constants.PM_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				rewardAndPunishmentVo.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> generalManagers = permissionService.findUsersByPermissionCode(Constants.GENERAL_MANAGER);
		if(generalManagers.size()<0){
			throw new RuntimeException("未找到总经理，请联系系统管理员配置！");
		}
		vars.put("auditor", generalManagers.get(0));
		vars.put("auditors", new ArrayList<>());
		vars.put("auditGroups", new ArrayList<>());
		vars.put("processType", Constants.REWARD_PUNISHMENT);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.EASY_PROCESS);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), rewardAndPunishmentVo.getUserId());
		taskService.complete(task.getId(), vars);
		EasyProcessEntity easyProcess = new EasyProcessEntity();
		easyProcess.setData(ObjectByteArrTransformer.toByteArray(rewardAndPunishmentVo));
		easyProcess.setUserId(rewardAndPunishmentVo.getUserId());
		easyProcess.setRequestUserId(rewardAndPunishmentVo.getRequestUserIds());
		easyProcess.setAddTime(new Date());
		easyProcess.setProcessInstanceID(processInstance.getId());
		easyProcess.setBusinessType(BusinessTypeEnum.REWARD_PUNISHMENT.getValue());
		String effectiveTime = rewardAndPunishmentVo.getEffectiveDate();
		Date effectiveDate = DateUtil.getSimpleDate(effectiveTime+"-01");
		easyProcess.setYear(DateUtil.getYear(effectiveDate));
		easyProcess.setMonth(DateUtil.getMonth(effectiveDate));
		baseDao.hqlSave(easyProcess);
	}

	@Override
	public List<RewardAndPunishmentVo> findRewardAndPunishmentTaskVos(List<Task> rewardAndPunishmentTasks) throws Exception {
		List<RewardAndPunishmentVo> rewardAndPunishmentVos = new ArrayList<>();
		for(Task rewardAndPunishmentTask: rewardAndPunishmentTasks){
			RewardAndPunishmentVo rewardAndPunishmentVo = new RewardAndPunishmentVo();
			String hql = "from EasyProcessEntity where processInstanceID="+rewardAndPunishmentTask.getProcessInstanceId();
			EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
			rewardAndPunishmentVo.setProcessInstanceID(easyProcessEntity.getProcessInstanceID());
			StaffEntity staff = staffService.getStaffByUserId(easyProcessEntity.getUserId());
			rewardAndPunishmentVo.setUserName(staff.getStaffName());
			RewardAndPunishmentVo rewardAndPunishmentData = (RewardAndPunishmentVo) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
			rewardAndPunishmentVo.setType(rewardAndPunishmentData.getType());
			rewardAndPunishmentVo.setMoney(rewardAndPunishmentData.getMoney());
			rewardAndPunishmentVo.setEffectiveDate(rewardAndPunishmentData.getEffectiveDate());
			rewardAndPunishmentVo.setReason(rewardAndPunishmentData.getReason());
			String[] requestUserIds = easyProcessEntity.getRequestUserId().split(",");
			List<String> requestUserNames = new ArrayList<>(requestUserIds.length);
			for(String requestUserId: requestUserIds){
				requestUserNames.add(staffDao.getStaffByUserID(requestUserId).getStaffName());
			}
			rewardAndPunishmentVo.setRequestUserNames(StringUtils.join(requestUserNames, ","));
			rewardAndPunishmentVo.setTaskId(rewardAndPunishmentTask.getId());
			rewardAndPunishmentVo.setAddTime(easyProcessEntity.getAddTime());
			rewardAndPunishmentVos.add(rewardAndPunishmentVo);
		}
		return rewardAndPunishmentVos;
	}

	@Override
	public RewardAndPunishmentVo getRewardAndPunishmentByProcessInstanceId(String processInstanceId) throws Exception {
		RewardAndPunishmentVo rewardAndPunishmentVo = new RewardAndPunishmentVo();
		String hql = "from EasyProcessEntity where processInstanceID="+processInstanceId;
		EasyProcessEntity easyProcessEntity = (EasyProcessEntity) baseDao.hqlfindUniqueResult(hql);
		rewardAndPunishmentVo.setProcessInstanceID(easyProcessEntity.getProcessInstanceID());
		RewardAndPunishmentVo rewardAndPunishmentData = (RewardAndPunishmentVo) ObjectByteArrTransformer.toObject(easyProcessEntity.getData());
		rewardAndPunishmentVo.setType(rewardAndPunishmentData.getType());
		rewardAndPunishmentVo.setMoney(rewardAndPunishmentData.getMoney());
		rewardAndPunishmentVo.setEffectiveDate(rewardAndPunishmentData.getEffectiveDate());
		rewardAndPunishmentVo.setReason(rewardAndPunishmentData.getReason());
		String attachmentIdStr = rewardAndPunishmentData.getAttachmentIds();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			rewardAndPunishmentVo.setAttachmentIds(attachmentIdStr);
			String[] attachmentIds = attachmentIdStr.split(",");
			List<CommonAttachment> attachments = new ArrayList<>(attachmentIds.length);
			for(String attachmentId: attachmentIds){
				CommonAttachment attach = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				if(Constants.PIC_SUFFIX.contains(attach.getSuffix())){
					attach.setImageType("png");
				}
				attachments.add(attach);
			}
			rewardAndPunishmentVo.setAttachments(attachments);
		}
		String[] requestUserIds = easyProcessEntity.getRequestUserId().split(",");
		List<String> requestUserNames = new ArrayList<>(requestUserIds.length);
		for(String requestUserId: requestUserIds){
			requestUserNames.add(staffDao.getStaffByUserID(requestUserId).getStaffName());
		}
		rewardAndPunishmentVo.setRequestUserNames(StringUtils.join(requestUserNames, ","));
		rewardAndPunishmentVo.setAddTime(easyProcessEntity.getAddTime());
		return rewardAndPunishmentVo;
	}

	@Override
	public void updateRewardAndPunishmentStatus(String result, String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String hql = "update EasyProcessEntity set applyResult="+result+" where processInstanceID="+task.getProcessInstanceId();
		baseDao.excuteHql(hql);
	}

	@Override
	public boolean CheckMeetManageCondition(String userId) {
		//主管级别的职位
		String[] positions = {"总经理","主管","经理","总监","组长"};
		Set<String> groupTypes = new HashSet<>();
		StaffEntity staff = staffDao.getStaffByUserID(userId);
		//设定的最少管理人数
		Integer managePersonNum = staff.getManagePersonNum();
		if(null != managePersonNum){
			List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
			for(Group group: groups){
				String[] groupType = group.getType().split("_");
				String positionId = groupType[2];
				PositionEntity position = positionDao.getPositionByPositionID(Integer.parseInt(positionId));
				if(null != position){
					String postionName = position.getPositionName();
					for(String _position: positions){
						//判断是否是主管级别的职位
						if(postionName.contains(_position)){
							groupTypes.add(group.getType());
						}
					}
				}
			}
			//下属员工数量
			int underlingPersonNum = 0;
			for(String groupTypeStr: groupTypes){
				String[] groupType = groupTypeStr.split("_");
				String companyId = groupType[0];
				String departmentId = groupType[1];
				Set<String> underlings = new HashSet<>();
				Set<String> staffs = staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(departmentId), underlings);
				underlingPersonNum += staffs.size();
			}
			if(underlingPersonNum>=managePersonNum){
				return true;
			}
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void batchImportSalary(File salaryFile) throws Exception {
		FileInputStream fis = new FileInputStream(salaryFile);
		Workbook workbook = WorkbookFactory.create(fis);
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1; // 获得工作表行数，初始行数为0
		//从第二行开始
		for(int i=1; i<rowNum; i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			Cell staffNumCell = row.getCell(0);
			if(null == staffNumCell){
				continue;
			}
			String[] typeAndValue = PoiUtil.getCellValue_(staffNumCell);
			if(!NumberUtil.isNum((typeAndValue[1]))){
				continue;
			}
			String staffNum = typeAndValue[1];
			String userId = staffDao.getstaffUserIdByStaffNum(staffNum);
			if(null == userId){
				continue;
			}
			StaffSalaryEntity staffSalary = new StaffSalaryEntity();
			staffSalary.setUserId(userId);
			Cell standardSalaryCell = row.getCell(1);
			if(null == standardSalaryCell){
				continue;
			}
			typeAndValue = PoiUtil.getCellValue_(standardSalaryCell);
			if(!NumberUtil.isNum((typeAndValue[1]))){
				continue;
			}
			staffSalary.setStandardSalary(Double.parseDouble(typeAndValue[1]));
			Cell basicSalaryCell = row.getCell(2);
			if(null == basicSalaryCell){
				continue;
			}
			typeAndValue = PoiUtil.getCellValue_(basicSalaryCell);
			if(!NumberUtil.isNum((typeAndValue[1]))){
				continue;
			}
			staffSalary.setBasicSalary(Double.parseDouble(typeAndValue[1]));
			Cell performanceSalaryCell = row.getCell(3);
			if(null == performanceSalaryCell){
				continue;
			}
			typeAndValue = PoiUtil.getCellValue_(performanceSalaryCell);
			if(!NumberUtil.isNum((typeAndValue[1]))){
				continue;
			}
			staffSalary.setPerformanceSalary(Double.parseDouble(typeAndValue[1]));
			Cell fullSalaryCell = row.getCell(4);
			if(null != fullSalaryCell){
				typeAndValue = PoiUtil.getCellValue_(fullSalaryCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setFullAttendance(Double.parseDouble(typeAndValue[1]));
			}
			Cell socialSecurityCell = row.getCell(5);
			if(null != socialSecurityCell){
				typeAndValue = PoiUtil.getCellValue_(socialSecurityCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setSocialSecurityBasic(Double.parseDouble(typeAndValue[1]));
			}
			Cell pensionCell = row.getCell(6);
			if(null != pensionCell){
				typeAndValue = PoiUtil.getCellValue_(pensionCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setPension(Double.parseDouble(typeAndValue[1]));
			}
			Cell medicalInsuranceCell= row.getCell(7);
			if(null != medicalInsuranceCell){
				typeAndValue = PoiUtil.getCellValue_(medicalInsuranceCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setMedicalInsurance(Double.parseDouble(typeAndValue[1]));
			}
			Cell unCell= row.getCell(8);
			if(null != unCell){
				typeAndValue = PoiUtil.getCellValue_(unCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setUnemployment(Double.parseDouble(typeAndValue[1]));
			}
			Cell seriousIllCell= row.getCell(9);
			if(null != seriousIllCell){
				typeAndValue = PoiUtil.getCellValue_(seriousIllCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setSeriousIllness(Double.parseDouble(typeAndValue[1]));
			}
			Cell personalPayCell= row.getCell(10);
			if(null != personalPayCell){
				typeAndValue = PoiUtil.getCellValue_(personalPayCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setPersonalPay(Double.parseDouble(typeAndValue[1]));
			}
			Cell companyPayCell= row.getCell(11);
			if(null != companyPayCell){
				typeAndValue = PoiUtil.getCellValue_(companyPayCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setCompanyPay(Double.parseDouble(typeAndValue[1]));
			}
			Cell publicFundBasicCell= row.getCell(12);
			if(null != publicFundBasicCell){
				typeAndValue = PoiUtil.getCellValue_(publicFundBasicCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setPublicfundBasic(Double.parseDouble(typeAndValue[1]));
			}
			Cell personalPayFundCell= row.getCell(13);
			if(null != personalPayFundCell){
				typeAndValue = PoiUtil.getCellValue_(personalPayFundCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setPersonalPayFund(Double.parseDouble(typeAndValue[1]));
			}
			Cell companyPayFundCell= row.getCell(14);
			if(null != companyPayFundCell){
				typeAndValue = PoiUtil.getCellValue_(companyPayFundCell);
				if(!NumberUtil.isNum((typeAndValue[1]))){
					continue;
				}
				staffSalary.setCompanyPayFund(Double.parseDouble(typeAndValue[1]));
			}
			staffSalary.setIsDeleted(0);
			staffSalary.setAddTime(new Date());
			baseDao.hqlSave(staffSalary);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void synOldSalary() {
		String hql = "from SalaryDetailEntity s where confirm=1 order by s.year desc,s.month desc";
		List<SalaryDetailEntity> salaryDetailEntities = (List<SalaryDetailEntity>) baseDao.hqlfind(hql);
		for (SalaryDetailEntity salaryDetailEntity : salaryDetailEntities) {
			if (salaryDetailEntity.getDetail() != null && salaryDetailEntity.getDetail().length > 0)
				try {
					List<String> detailList = (List<String>) ObjectByteArrTransformer.toObject(salaryDetailEntity.getDetail());
					StaffSalaryDetailEntity staffSalaryDetail = new StaffSalaryDetailEntity();
					String userId = salaryDetailEntity.getUserId();
					staffSalaryDetail.setUserId(userId);
					Integer companyId = companyDao.getCompanyIdByUserId(userId);
					Integer departmentId = departmentDao.getDeparmentIdByUserId(userId);
					if(null!=companyId && null!=departmentId){
						staffSalaryDetail.setCompanyId(companyId);
						staffSalaryDetail.setDepartmentId(departmentId);
					}
					double basicSalary = 0;
					if(!NumberUtil.isNum(detailList.get(0))){
						continue;
					}
					basicSalary = Double.parseDouble(detailList.get(0));
					staffSalaryDetail.setBasicSalary(basicSalary);
					double performanceSalary = 0;
					if(NumberUtil.isNum(detailList.get(1))){
						performanceSalary = Double.parseDouble(detailList.get(1));
						staffSalaryDetail.setPerformanceSalary(performanceSalary);
					}
					double overtimeSubsidy = 0;
					if(NumberUtil.isNum(detailList.get(3))){
						overtimeSubsidy = Double.parseDouble(detailList.get(3));
						staffSalaryDetail.setOvertimeSubsidy(overtimeSubsidy);
					}
					double fullAttendance = 0;
					if(NumberUtil.isNum(detailList.get(4))){
						fullAttendance = Double.parseDouble(detailList.get(4));
						staffSalaryDetail.setFullAttendance(fullAttendance);
					}
					double deductibleMoney = 0;
					if(NumberUtil.isNum(detailList.get(5))){
						deductibleMoney = Double.parseDouble(detailList.get(5));
						staffSalaryDetail.setDeductibleMoney(deductibleMoney);
					}
					double otherDeduction = 0;
					if(NumberUtil.isNum(detailList.get(6))){
						otherDeduction = Double.parseDouble(detailList.get(6));
						Map<String, Double> otherDeductionItems = new HashMap<>();
						otherDeductionItems.put("其它扣除", otherDeduction);
						staffSalaryDetail.setOtherDeductionItems(ObjectByteArrTransformer.toByteArray(otherDeductionItems));
					}
					double lateMoney = 0;
					if(NumberUtil.isNum(detailList.get(7))){
						lateMoney = Double.parseDouble(detailList.get(7));
						staffSalaryDetail.setLateMoney(lateMoney);
					}
					double pension = 0;
					if(NumberUtil.isNum(detailList.get(8))){
						pension = Double.parseDouble(detailList.get(8));
						staffSalaryDetail.setPension(pension);
					}
					double medicalInsurance = 0;
					if(NumberUtil.isNum(detailList.get(9))){
						medicalInsurance = Double.parseDouble(detailList.get(9));
						staffSalaryDetail.setMedicalInsurance(medicalInsurance);
					}
					double unEmployment = 0;
					if(NumberUtil.isNum(detailList.get(10))){
						unEmployment = Double.parseDouble(detailList.get(10));
						staffSalaryDetail.setUnemployment(unEmployment);
					}
					double seriousIllness = 0;
					if(NumberUtil.isNum(detailList.get(11))){
						seriousIllness = Double.parseDouble(detailList.get(11));
						staffSalaryDetail.setSeriousIllness(seriousIllness);
					}
					double publicFund = 0;
					if(NumberUtil.isNum(detailList.get(12))){
						publicFund = Double.parseDouble(detailList.get(12));
						staffSalaryDetail.setPublicFund(publicFund);
					}
					double personalIncomeTax = 0;
					if(NumberUtil.isNum(detailList.get(13))){
						personalIncomeTax = Double.parseDouble(detailList.get(13));
						staffSalaryDetail.setPersonalIncomeTax(personalIncomeTax);
					}
					String afterTaxSalary = detailList.get(14);
					if(!NumberUtil.isNum(afterTaxSalary)){
						continue;
					}
					double _afterTaxSalary = Double.parseDouble(afterTaxSalary);
					double preTaxSalary = NumberUtil.Rounding(_afterTaxSalary+personalIncomeTax, 2);
					double totalMoney = NumberUtil.Rounding(basicSalary + performanceSalary + overtimeSubsidy + fullAttendance
							- lateMoney - deductibleMoney - otherDeduction, 2);
					staffSalaryDetail.setTotalMoney(totalMoney);
					staffSalaryDetail.setPersonalpay(NumberUtil.Rounding(pension + medicalInsurance + unEmployment + seriousIllness, 2));
					staffSalaryDetail.setAfterTaxSalary(_afterTaxSalary);
					staffSalaryDetail.setPreTaxSalary(preTaxSalary);
					staffSalaryDetail.setIsDeleted(0);
					staffSalaryDetail.setAddTime(new Date());
					staffSalaryDetail.setYear(salaryDetailEntity.getYear());
					staffSalaryDetail.setMonth(salaryDetailEntity.getMonth());
					staffSalaryDetail.setEmailSend(-1);
					staffSalaryDetail.setMobileSend(1);
					staffSalaryDetail.setPayStatus(1);
					baseDao.hqlSave(staffSalaryDetail);
				} catch (Exception e) {
					e.printStackTrace();
				} 
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffSalaryDetailEntity> getStaffSalarysByUserId(String userId) {
		String hql = "from StaffSalaryDetailEntity where isDeleted=0 and userId='"+userId+"' order by year desc, month desc";
		return (List<StaffSalaryDetailEntity>) baseDao.hqlPagedFind(hql, 1, 5);
	}
}
