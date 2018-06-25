package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 车辆年检记录
 * @author yxl
 *
 */
@Data
public class YearlyInspectionVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String[] time;
	/**
	 * 下次年检时间
	 */
	private String[] nextYearlyInspectionTime;
}
