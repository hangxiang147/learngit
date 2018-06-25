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
@Table(name="OA_SpreadShopApply")
public class SpreadShopApplyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	private String shopName;
	/**
	 * 店铺申请的类型
	 */
	private String applyType;
	/**
	 * 付费推广表的id
	 */
	private String spreadIds;
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
