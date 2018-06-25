package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_ReceiveExpress")

public class ReceiveExpressEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ReceiveExpressID", unique = true)
	private Integer receiveExpressID;
	
	@Column(name = "OperatorID")
	private String operatorID; //操作人ID
	
	@Column(name = "RecipientID")
	private String recipientID;  //寄件人ID
	
	@Column(name = "ReceiptDate")
	private Date receiptDate;  //收件日期
	
	@Column(name = "ExpressCompany")
	private Integer expressCompany;  //物流公司
	
	@Column(name = "ExpressNumber")
	private String expressNumber;  //快递单号
	
	@Column(name = "Status")
	private Integer status;  //状态
	
	@Column(name = "ClaimID")
	private String claimID;  //领件人ID
	
	@Column(name = "ClaimDate")
	private Date claimDate;  //领件时间
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
