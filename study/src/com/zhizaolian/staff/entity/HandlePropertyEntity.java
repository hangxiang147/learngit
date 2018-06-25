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
@Table(name="OA_HandleProperty")
public class HandlePropertyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 使用部门
	 */
	private String useDepartment;
	/**
	 * 资产名称
	 */
	private String assetName;
	/**
	 * 资产编号
	 */
	private String assetNum;
	/**
	 * 规格型号
	 */
	private String model;
	/**
	 * 处置原因
	 */
	private String handleReason;
	/**
	 * 处置方案
	 */
	private String handleCase;
	
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
