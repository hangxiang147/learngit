package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="OA_SocialSecurityClass")
public class SocialSecurityClassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String name;//社保名称
	
	private Double socialSecurityBasic;//社保基数
	
	private Double pension;//养老
	
	private Double medicalInsurance;//医保
	
	private Double unemployment;//失业
	
	private Double seriousIllness;//大病
	
	private Double personalPay = 0.0;//个人缴纳
	
	private Double companyPay;//公司缴纳	
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
}
