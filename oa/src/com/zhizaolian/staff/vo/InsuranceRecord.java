package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 车辆保险记录
 * @author yxl
 *
 */
@Data
public class InsuranceRecord implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String[] time;
	/**
	 * 保额
	 */
	private String[] money;
	/**
	 * 下次保险时间
	 */
	private String[] nextInsuranceTime;

}
