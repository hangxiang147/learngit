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
@Table(name="OA_ExitParterApply")
public class ExitParterApplyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String exitReason;//申请退出合伙人理由
	
	private String processInstanceId;
	
	private Integer auditStatus;//0.申请退出合伙人,1.不同意退出合伙人,2.同意退出合伙人;
	
	private String approvalOpinion;//审批退出意见;
	
	private Integer partnerId;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
