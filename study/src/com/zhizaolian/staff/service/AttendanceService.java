package com.zhizaolian.staff.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhizaolian.staff.entity.AttendanceDetailEntity;
import com.zhizaolian.staff.entity.MonthlyRestEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.UserMonthlyRestEntity;
import com.zhizaolian.staff.entity.WorkRestArrangeEntity;
import com.zhizaolian.staff.entity.WorkRestTimeEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.SigninVO;
import com.zhizaolian.staff.vo.VacationVO;

public interface AttendanceService {

	/**
	 * 解析员工考勤数据excel文件
	 * @param fileName
	 * @param companyID
	 */
	void parseExcel(String fileName, int companyID, StringBuffer index) throws Exception;
	
	
	ListResult<SalaryDetailEntity> getSalarysListByKey(String name,String mobile,String email,int year,int month,String type,int pape,int limit);
	
	void parseSalary(String fileName,int year,int month) throws Exception;
	/**
	 * 分页查询满足给定查询条件的考勤信息
	 * @param attendanceVO
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<AttendanceVO> findAttendancePageListByAttendanceVO(AttendanceVO attendanceVO, int page, int limit);
	XSSFWorkbook exportAttendancePageListByAttendanceVO(AttendanceVO attendanceVO);

	ListResult<VacationVO> findVacationPageListByVacationVO(VacationVO vacationVO,int page,int limit);
	
	/**
	 * 检查指定用户指定日期的考勤统计，并修改
	 * @param attendanceDate
	 * @param userID
	 * @throws Exception 
	 */
	void checkAttendanceDetail(Date attendanceDate, String userID) throws Exception;
	
	/**
	 * 根据请假记录，检查相关的考勤统计数据
	 * @param vacation
	 * @throws Exception 
	 */
	void checkAttendanceDetailsByVacationVO(VacationVO vacation) throws Exception;
	
	//查询未签到的人
	public List<SigninVO> findSigninsByDateAndCompanyID(Integer companyID,String date) throws Exception;
	
	//按月统计未签到的人
	public List<SigninVO> findSignByMonthAndCompanyID(Integer companyID,Date date) throws Exception;
	
	public Map<String, SigninVO> findSignByDateAndCompanyID(Integer companyID, String beginDate, String endDate) throws Exception;
	
	//考勤统计
	public ListResult<AttendanceVO> findAttendanceStatistics(AttendanceVO attendanceVO, int page, int limit) throws Exception;
	
	XSSFWorkbook exportAttendanceMsg(AttendanceVO attendanceVO) throws Exception;

	Map<String, String> getWorkHour(String userId) throws Exception;


	List<SalaryDetailEntity> getSalaryByCondition(String userId, String month, String year);


	List<Object> getLateOrLeaveNumInfo(String companyId, String lateOrLeave);


	void confirmSalary(String year, String month);


	void updateLateStatus(String attendanceId);


	boolean checkWorkRestName(String workRestName);


	void saveWorkRestTime(WorkRestTimeEntity workRestTime, String workRestTimeId);


	List<WorkRestTimeEntity> getWorkRestTimeList();


	WorkRestTimeEntity getWorkRestTime(String workRestTimeId);


	void deleteWorkRestTime(String workRestTimeId);


	void saveWorkArrange(WorkRestArrangeEntity workRestArrange);


	ListResult<WorkRestArrangeEntity> getWorkTimeArranges(Integer limit, Integer page);


	void deleteWorkRestArrange(String workRestArrangeId);


	boolean checkWorkTimeArrange(String departmentId, String companyId);
	
	double getDailyHoursByCompanyIDOrDepartmentId(String companyId, String departmentId, String date) throws Exception;
	
	String getWorkOverBeginTimeByCompanyIDOrDepartmentId(String companyId, String departmentId, String date) throws Exception;
	
	String[] getWorkRestTimeByCompanyIDOrDepartmentId(String companyId, String departmentId, String date) throws Exception;


	void updateLastWorkArrange(String beginTime, String companyId, String departmentId);


	boolean checkWorkRestIsArranged(String workRestId);

	String[] getVacationDaysAndHours(String startime, String endtime, String userId) throws Exception;


	void saveMonthlyRestDay(MonthlyRestEntity monthlyRest);


	List<MonthlyRestEntity> getMonthlyRests();


	boolean checkMonthExist(String year, String month);


	void deleteRest(String restId);


	//MonthlyRestEntity getMonthlyRest(int year, int month);


	List<Object> findAbnormalAttendanceDatas(String date, String staffName) throws Exception;


	void addAttendanceTime(AttendanceDetailEntity attend) throws Exception;


	XSSFWorkbook exportPartnerAttendDatas() throws Exception;


	AttendanceVO findAttendanceStatisticsByUserId(Date beginDate, Date endDate, String userId) throws Exception;


	List<String>  getAbnormalDays(String userId, Date beginDate, Date endDate) throws Exception;

	/**
	 * 获取离职人员最后一次打卡的日期（全天打卡）
	 * @param userId
	 * @return
	 */
	String getLeaveStaffLastAttendanceDate(String userId, int month);

	ListResult<UserMonthlyRestEntity> getUserMonthlyRests(String staffName, String year, String month, Integer limit,
			Integer page);


	void modifyUserMonthlyRest(String id, String restDays);


	UserMonthlyRestEntity getMonthlyRest(String userId, int year, int month);

	
}
