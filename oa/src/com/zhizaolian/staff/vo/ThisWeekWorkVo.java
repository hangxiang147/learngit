package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 本周工作（周报）
 * @author yxl
 *
 */
@Data
public class ThisWeekWorkVo implements Serializable{
	private static final long serialVersionUID = 6727166783046410200L;
	/**
	 * 工作内容
	 */
	private String[] content;
	/**
	 * 计划开始日期
	 */
	private String[] planBeginDate;
	/**
	 * 计划结束日期
	 */
	private String[] planEndDate;
	/**
	 * 实际开始日期
	 */
	private String[] actualBeginDate;
	/**
	 * 实际结束日期
	 */
	private String[] actualEndDate;
	/**
	 * 完成的进度
	 */
	private String[] completeRate;
	/**
	 * 任务下达人
	 */
	private String[] assigner;
	
}
