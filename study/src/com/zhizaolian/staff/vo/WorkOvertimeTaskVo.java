package com.zhizaolian.staff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkOvertimeTaskVo extends TaskVO{
	
	private String beginDate;  //开始时间
	
	private String endDate;  //结束时间
	
	private String reason;  //休假原因
	/**
	 * 加班时长
	 */
	private String workHours;
	
	private String department;
}
