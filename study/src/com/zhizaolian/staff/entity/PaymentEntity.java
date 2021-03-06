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
@Table(name = "OA_Payment")
public class PaymentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ReimbursementID", unique = true)
	private Integer reimbursementID;
	
	@Column(name = "ReimbursementNo")
	private String reimbursementNo;  //报销单号
	
	@Column(name = "UserID")
	private String userID;  //填写申请单的用户ID
	
	@Column(name = "RequestUserID")
	private String requestUserID;  //报销人ID
	
	@Column(name = "PayeeID")
	private String payeeID;  //领款人ID
	
	@Column(name = "InvoiceTitle")
	private Integer invoiceTitle;  //发票抬头
	
	@Column(name = "InvoiceNum")
	private Integer invoiceNum;  //发票张数
	
	@Column(name = "DetailNum")
	private Integer detailNum;  //明细单张数
	
	@Column(name = "TotalAmount")
	private Double totalAmount;  //合计报销金额
	
	
	/**
	 * 证明人Id
	 */
	@Column(name = "ReternenceId")
	private String reternenceId;
	
	/**
	 * 证明人姓名
	 */
	@Column(name = "ReternenceName")
	private String reternenceName;
	
	/**
	 * 证明人手机号
	 */
	@Column(name = "ReternenceMobile")
	private String reternenceMobile;
	/**
	 * 是否固定资产
	 */
	@Column(name = "IsFixedAsset")
	private Integer isFixedAsset;
	
	/**
	 *固定资产编号 
	 */
	@Column(name = "FixedAssetNo")
	private String fixedAssetNo;
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态，1：通过；2：不通过
	
	@Column(name = "MoneyType")
	private String moneyType;
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "ShowPerson1")
	private String showPerson1;
	
	@Column(name = "ShowPerson2")
	private String showPerson2;
	
	@Column(name = "ShowPerson3")
	private String showPerson3;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	@Column(name="isHaveInvoice")
	private Integer isHaveInvoice;
	
	private String bankAccountId;
}
