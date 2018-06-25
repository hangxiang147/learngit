package com.zhizaolian.staff.entity;

import java.util.Date;

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
@Table(name="OA_Partner")
public class PartnerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String userId;
	
	private String applyDate;//申请加入合伙人时间
	
	private String applyContent;//申请内容
	
	private String status;//审批状态  1：同意；2：不同意  3:同意退出
	
	private String comment;//审批意见
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
	@Transient
	private String taskId;
	
	@Transient
	private String taskName;
	
	@Transient
	private String description;
	
	@Transient
	private String assignee;
	
	@Transient
	private Integer exitParterApplyId;//申请退出合伙人id;
	
	@Transient
	private String exitReason;//申请退出合伙人理由;
	
	@Transient
	private Date applyAddTime;//申请退出合伙人的时间;
	
	@Transient
	private String processInstanceId;
	
	@Transient
	private Integer auditStatus;//0.申请退出合伙人,1.不同意退出合伙人,2.同意退出合伙人;
	
	@Transient
	private String approvalOpinion;//申请退出意见;
	
	@Transient
	private Integer exisIsDeleted;
	
	@Transient
	private Date exitUpdateTime;
	
	@Transient
	private Integer partnerId;
}
