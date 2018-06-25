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

/**
 * 社保公积金名单审核记录表
 * @author jndxz
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OA_SocialSecurityProcess")
public class SocialSecurityProcessEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SspID", unique = true)
	private Integer sspID;  
	
	@Column(name = "UserID")
	private String userID;  //提交审核的员工ID
	
	@Column(name = "PaymentYear")
	private Integer paymentYear;  //公积金缴纳的年份
	
	@Column(name = "PaymentMonth")
	private Integer paymentMonth;  //公积金缴纳的月份
	
	@Column(name = "HFPaymentYear")
	private Integer hfPaymentYear;  //社保缴纳的年份
	
	@Column(name = "HFPaymentMonth")
	private Integer hfPaymentMonth;  //社保缴纳的月份
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司ID
	
	@Column(name = "PersonalCount")
	private Double personalCount;  //个人部分
	
	@Column(name = "CompanyCount")
	private Double companyCount;  //单位部分
	
	@Column(name = "TotalCount")
	private Double totalCount;  //合计总额
	
	@Column(name = "HFTotalCount")
	private Double hfTotalCount;  //社保缴纳总额
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
