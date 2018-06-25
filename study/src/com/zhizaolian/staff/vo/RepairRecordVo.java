package com.zhizaolian.staff.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 车辆维修记录
 * @author yxl
 *
 */
@Data
public class RepairRecordVo implements Serializable{
	
	private static final long serialVersionUID = -6577109843048679415L;
	private String[] time;
	/**
	 * 维修项目
	 */
	private String[] repairItems;
	/**
	 * 维修金额
	 */
	private String[] repairMoney;
}
