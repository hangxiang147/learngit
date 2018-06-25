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
 * 社保缴纳详情表（跟公积金名字取反了）
 * @author jndxz
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OA_HousingFund")
public class HousingFundEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HFID", unique = true)
	private Integer hfID;
	
	@Column(name = "ProcessID")
	private Integer processID;  //对应的审核流程业务表ID
	
	@Column(name = "UserID")
	private String userID;  //用户ID
	
	@Column(name = "PaymentYear")
	private Integer paymentYear;  //缴纳年份
	
	@Column(name = "PaymentMonth")
	private Integer paymentMonth;  //缴纳月份 
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司ID
	
	@Column(name = "EntryDate")
	private Date entryDate;  //入职日期
	
	@Column(name = "FormalDate")
	private Date formalDate;  //转正日期
	
	@Column(name = "Gender")
	private Integer gender;  //性别
	
	@Column(name = "IDType")
	private Integer idType;  //证件类型，1：身份证
	
	@Column(name = "IDNumber")
	private String idNumber;  //证件号码
	
	@Column(name = "HasPaid")
	private Integer hasPaid;  //是否参保过，0：否；1：是
	
	@Column(name = "CompanyCount")
	private Double companyCount;  //单位合计
	
	@Column(name = "PersonalCount")
	private Double personalCount;  //个人合计
	
	@Column(name = "TotalCount")
	private Double totalCount;  //应缴总额
	
	@Column(name = "Note")
	private String note;  //备注
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime; 
}
