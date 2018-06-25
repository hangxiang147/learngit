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
@Table(name="OA_ShopOtherPay")
public class ShopOtherPayEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 店铺申请的类型
	 */
	private String applyType;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 项目作用
	 */
	private String projectUse;
	/**
	 * 项目明细
	 */
	private String description;
	/**
	 * 项目花费
	 */
	private String projectPay;
	/**
	 * 申请人
	 */
	private String userID;
	private String userName;
	/**
	 * 主管id
	 */
	private String supervisorId;
	/**
	 * 主管名字
	 */
	private String supervisorName;
	/**
	 * 审批结果
	 */
	private Integer applyResult;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
}
