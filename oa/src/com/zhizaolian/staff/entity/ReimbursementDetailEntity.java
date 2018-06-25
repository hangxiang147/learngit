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
 * 报销明细表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_ReimbursementDetail")
public class ReimbursementDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DetailID", unique = true)
	private Integer detailID;  
	
	@Column(name = "ReimbursementID")
	private Integer reimbursementID;  //报销记录ID
	
	@Column(name = "Purpose")
	private String purpose;  //用途
	
	@Column(name = "Amount")
	private Double amount;  //金额
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	@Column(name = "Type")
	private String type;
}
