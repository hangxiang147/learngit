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
@Table(name="OA_PurchaseProperty")
public class PurchasePropertyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 资产名称
	 */
	private String propertyName;
	/**
	 * 资产数量
	 */
	private String number;
	/**
	 * 预算总价
	 */
	private String budgetPrice;
	/**
	 * 是否超出预算
	 */
	private String exceedBudget;
	/**
	 * 规格型号
	 */
	private String model;
	/**
	 * 使用地点
	 */
	private String place;
	/**
	 * 保管人
	 */
	private String storageUserName;
	private String storageUserId;
	/**
	 * 购置原因
	 */
	private String reason;
	/**
	 * 品名
	 */
	private String productName;
	/**
	 * 采购人员填写的规格型号
	 */
	private String _model;
	/**
	 * 采购人员填写的数量
	 */
	private String _number;
	/**
	 * 单价
	 */
	private String unitPrice;
	/**
	 * 使用或采购部门验收结果
	 */
	private String purchaserCheckResult;
	/**
	 * 固定资产和低值易耗品分类
	 */
	private String propertyType;
	/**
	 * 固定资产和低值易耗品编号
	 */
	private String propertyNum;
	/**
	 * 折旧年限
	 */
	private String useTime;
	/**
	 * 净残值率
	 */
	private String netSalvageRate;
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
