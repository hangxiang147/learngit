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
@Table(name="OA_ShopPayApply")
public class ShopPayApplyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 表OA_SpreadShopApply的id
	 */
	private String spreadPayApplyIds;
	/**
	 * 表OA_ShopPayPlugin的id
	 */
	private String pluginPayApplyIds;
	/**
	 * 表OA_ShopOtherPay的id
	 */
	private String otherPayApplyIds;
	private String beginDate;
	private String endDate;
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
