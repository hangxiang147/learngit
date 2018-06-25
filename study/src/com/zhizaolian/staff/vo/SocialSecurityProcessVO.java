package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SocialSecurityProcessVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private Integer sspID; 
	
	private Integer year;  //公积金缴纳年份
	
	private Integer month;  //公积金缴纳月份
	
	private Integer ssYear;  //社保缴纳年份
	
	private Integer ssMonth;  //社保缴纳月份
	
	private Integer companyID;  //公司ID
	
	private Double personalCount;  //个人部分
	
	private Double companyCount;  //公司部分
	
	private Double totalCount;  //合计总额
	
	private Double ssTotalCount;  //社保合计总额
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("totalCount", "合计", totalCount.toString()));
	}
}
