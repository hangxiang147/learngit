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
 * 公积金缴纳详情表（名字取反了）
 * @author jndxz
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OA_SocialSecurity")
public class SocialSecurityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SSID", unique = true)
	private Integer ssID;
	
	@Column(name = "ProcessID")
	private Integer processID;  //对应的审核流程业务表ID
	
	@Column(name = "PaymentYear")
	private Integer paymentYear;  //公积金缴纳年份
	
	@Column(name = "PaymentMonth")
	private Integer paymentMonth;  //公积金缴纳月份 
	
	@Column(name = "UserID")
	private String userID;  //员工ID
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司ID
	
	@Column(name = "IDType")
	private Integer idType;  //证件类型，1：身份证
	
	@Column(name = "IDNumber")
	private String idNumber;  //证件号码
	
	@Column(name = "BasePay")
	private Double basePay;  //公积金缴纳基数
	
	@Column(name = "SelfPaidRatio")
	private Double selfPaidRatio;  //个人缴存比例（%）
	
	@Column(name = "Reason")
	private String reason;  //增加原因
	
	@Column(name = "PersonalProvidentFund")
	private Double personalProvidentFund;  //公积金个人部分
	
	@Column(name = "CompanyProvidentFund")
	private Double companyProvidentFund;  //公积金单位部分
	
	@Column(name = "TotalProvidentFund")
	private Double totalProvidentFund;  //合计公积金缴存额（元）
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime; 
}
