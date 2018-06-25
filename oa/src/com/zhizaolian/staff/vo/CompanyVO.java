package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class CompanyVO {

	private Integer companyID;
	private String companyName;  //公司名称
	private String code;  //公司代码，用作员工工号前两位
	
}
