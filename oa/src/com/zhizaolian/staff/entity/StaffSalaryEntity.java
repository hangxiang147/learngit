package com.zhizaolian.staff.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="OA_StaffSalary")
public class StaffSalaryEntity implements Serializable{
	private static final long serialVersionUID = -8732645060514032931L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String userId;
	
	private Double standardSalary;//薪资标准
	
	private Double basicSalary;//基本工资
	
	private Double socialSecurityBasic;//社保基数
	
	private Double pension;//养老
	
	private Double medicalInsurance;//医保
	
	private Double unemployment;//失业
	
	private Double seriousIllness;//大病
	
	private Double personalPay;//个人缴纳社保
	
	private Double companyPay;//公司缴纳社保
	
	private Double publicfundBasic;//公积金基数
	
	private Double personalPayFund;//个人缴纳公积金
	
	private Double companyPayFund;//公司缴纳公积金
	
	private Double performanceSalary;//绩效工资
	
	private Double fullAttendance;//满勤
	
	private String otherPartItems;//其它部分的名称
	
	private String otherPartValues;//其它部分的金额
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String[] otherPartItem;
	@Transient
	private Double[] otherPartValue;
	@Transient
	private Map<String, String> itemAndValMap;
}
