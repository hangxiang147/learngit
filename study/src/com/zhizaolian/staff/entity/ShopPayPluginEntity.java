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
@Table(name="OA_ShopPayPlugin")
public class ShopPayPluginEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 店铺申请的类型
	 */
	private String applyType;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 服务/应用名称
	 */
	private String serviceName;
	/**
	 * 插件/服务作用
	 */
	private String serviceUse;
	/**
	 * 开通时长
	 */
	private String openTime;
	/**
	 * 付费金额
	 */
	private String payMoney;
	/**
	 * 付款账号
	 */
	private String payAccount;
	
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
