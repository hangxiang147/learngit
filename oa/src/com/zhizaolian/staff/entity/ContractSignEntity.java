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
@Table(name="OA_ContractSign")
public class ContractSignEntity {
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
	 * 对方公司名称
	 */
	private String otherCompanyName;
	/**
	 * 合同金额
	 */
	private String money;
	
	private String description;
	/**
	 * 是否是支出类合同
	 */
	private Integer isPay;
	/**
	 * 是否超出本季度预算
	 */
	private Integer exceedSeason;
	/**
	 * 是否超出集团审定预算
	 */
	private Integer exceedGroup;
	/**
	 * 超出本季度预算比例
	 */
	private String exceedSeasonRate;
	/**
	 * 超出集团审定预算比例
	 */
	private String exceedGroupRate;
	
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
