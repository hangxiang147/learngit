package com.zhizaolian.staff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoursePlanTaskVo extends TaskVO{
	
	private Integer coursePlanId;
	
	private String beginTime;
	/**
	 * 培训时长
	 */
	private String trainHours;
	/**
	 * 培训地点
	 */
	private String place;
	
	private String courseName;
	
	private String trainClass;
}
