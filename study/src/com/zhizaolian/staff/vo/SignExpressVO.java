package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class SignExpressVO {

	private String recipientID;  //寄件人ID
	
	private String recipientName;
	
	private String operatorID; //操作人ID

	private String receiptDate;  //收件日期
	
	private Integer expressCompany;  //物流公司
	
	private String expressNumber;  //快递单号
	
	private Integer status;  //快递单号

	private String claimID;  //领件人ID
	
	private String claimName;

	private String claimDate;  //领件时间
	
	private String beginDate;
	
	private String endDate;
	
	private Integer signExpressID;//快递签收ID

}
