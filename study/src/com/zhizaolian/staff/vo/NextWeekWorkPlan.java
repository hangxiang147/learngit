package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 下周工计划（周报）
 * @author yxl
 *
 */
@Data
public class NextWeekWorkPlan implements Serializable{
	private static final long serialVersionUID = -4863523581759518810L;
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
}
