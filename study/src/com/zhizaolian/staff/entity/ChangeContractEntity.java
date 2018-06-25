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
@Table(name="OA_ChangeContract")
public class ChangeContractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	/**
	 * 合同编号
	 */
	private String contractId;
	private String contractName;
	/**
	 * 合同内容
	 */
	private String contractContent;
	/**
	 * 合同签订日期
	 */
	private String signDate;
	/**
	 * 合同已履行情况介绍
	 */
	private String contractDescription;
	/**
	 * 是否是变更合同
	 */
	private String isChange;
	/**
	 * 变更合同原因
	 */
	private String changeReason;
	/**
	 * 变更前内容
	 */
	private String beforeChangeContent;
	/**
	 * 变更后内容
	 */
	private String afterChangeContent;
	/**
	 * 解除合同原因
	 */
	private String relieveReason;
	private String userID;
	/**
	 * 审批结果
	 */
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
