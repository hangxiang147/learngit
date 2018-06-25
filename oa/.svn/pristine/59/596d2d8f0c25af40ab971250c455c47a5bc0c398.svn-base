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
@Table(name="OA_TransferProperty")
public class TransferPropertyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String assetId;
	
	private String assetName;
	
	private String assetNum;
	
	private String assetType;
	/**
	 * 规格型号
	 */
	private String model;
	
	private String number;
	
	private String unitPrice;
	
	private String money;
	/**
	 * 调拨原因
	 */
	private String transferReason;
	/**
	 * 调出单位
	 */
	private String oldCompany;
	/**
	 * 调入单位
	 */
	private String newCompany;
	
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
