package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class SendExpressVO {
	private String userID;
	private String userName;
	private String postDate;
	private Integer companyID;
	private Integer departmentID;
	private String departmentName;
	private Integer expressCompany;
	private String expressNumber;
	private String reason;
    private String beginDate;//开始时间
	
	private String endDate;  //结束时间
	
	private String weekDay;//星期几
	private Integer type;  //类型，1：寄付；2：到付
}
