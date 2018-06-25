package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 期权
 * @author yxl
 *
 */
@Data
@Entity
@Table(name="OA_PartnerOption")
public class PartnerOptionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer detailId;//合伙人内容明细表的id
	
	private Integer salaryId;//薪资表的id
	
	private String userId;//认购人员
	
	private String money;//认购金额
	
	private String optionMoney;//期权配比金额
	
	private String purchaseType;//认购方式
	
	private String status;//匹配状态 0：待匹配 1：已匹配
	
	private Date purchaseDate;//认购时间
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
