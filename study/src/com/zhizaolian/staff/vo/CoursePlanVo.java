package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class CoursePlanVo extends BaseVO{
	
	private static final long serialVersionUID = -5253485986401937133L;

	private Integer id;
	
	private Integer courseId;
	
	private String beginTime;
	/**
	 * 培训时长
	 */
	private String trainHours;
	/**
	 * 培训地点
	 */
	private String place;
	/**
	 * 讲师
	 */
	private String lecturer;
	/**
	 * 是否发过短信提醒 默认为0，1表示已发
	 */
	private Integer sendMsg;
	
	private String userID;
	
	private String processStatus;
	
	private String processInstanceID;
	
	private String courseName;
	
	private String trainClass;
	
	@Override
	public void createFormFields(List<FormField> fields) {}
}
