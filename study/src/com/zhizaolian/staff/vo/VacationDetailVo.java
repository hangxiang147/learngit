package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class VacationDetailVo {
	/**
	 * 部门名字
	 */
	private String departmentName;
	/**
	 * 部门id
	 */
	private String departmentId;
	
	private String staffName;
	/**
	 * 请假日期
	 */
	private String vacationDate;
	/**
	 * 请假天数
	 */
	private String vacationDays;
	/**
	 * 已连休天数
	 */
	private String continuousRestDays;
	/**
	 * 当月休天数
	 */
	private String currentMonthRestDays;
	/**
	 * OA审批人
	 */
	private String auditor;
	/**
	 * 请假原因
	 */
	private String reason;
	
	private String vacationID;
}
