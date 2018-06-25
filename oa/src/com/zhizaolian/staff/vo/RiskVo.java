package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 风险（周报）
 * @author yxl
 *
 */
@Data
public class RiskVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4622588753255569203L;
	/**
	 * 风险描述
	 */
	private String[] riskDescription;
	/**
	 * 解决方案
	 */
	private String[] solution;
	/**
	 * 责任人
	 */
	private String[] responsiblePerson;
	/**
	 * 计划解决日期
	 */
	private String[] planSolveDate;
}
