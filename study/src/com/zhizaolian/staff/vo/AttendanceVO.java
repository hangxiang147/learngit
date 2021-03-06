package com.zhizaolian.staff.vo;

import java.util.Map;

import lombok.Data;

@Data
public class AttendanceVO {
	private String userID;  //员工ID
	private String name;  //员工姓名
	private Integer companyID;  //公司ID
	private String companyName;  //公司名称
	private Integer departmentID;  //部门ID
	private String departmentName;
	private Integer status;  //考勤状态
	private String beginDate;  //开始时间
	private String endDate;  //结束时间
	private String attendanceDate;  //考勤日期
	private String attendanceTime;  //打卡时间
	private String statusString;  //考勤计算结果
	private String note;
	private Integer signin;
	private Integer attendanceDays;//应出勤天数
	private Integer relaxDays;//休息天数
	private Integer notPunchTimes;//未打卡次数
	private Integer lateTime;//迟到时间
	private Integer lateTimes;//迟到次数
	private Integer leaveEarlyTime;//早退时间
	private Integer leaveEarlyTimes;//早退次数
	private int  officeTime;//上班时间
	private String detail;
	private VacationVO vacationVO;
	//未有打卡  天数下标
	private String notPushDateIndex;
	private Integer beginType;
	private Integer attendanceId;
	private String lateStatus;
	//晚上加班次数
	private String nightWorkTimes;
	//晚上加班小时
	private String nightWorkHours;
	//白天加班时长
	private String dayWorkHours;
	//白天加班小时数
	private double dailyWorkOverTimeHours;
	private WorkReportVO workReportVO;
	private SigninVO signinVO;
	private String staffNum;//工号
	private String workDaysAndHoursInWeekend;
	private Map<String, Double> weekendDayAndWorkHoursMap;
	private Integer actualAttendanceDays;
}
