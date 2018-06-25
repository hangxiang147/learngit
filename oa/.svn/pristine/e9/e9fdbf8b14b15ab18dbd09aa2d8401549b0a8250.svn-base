package com.zhizaolian.staff.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="OA_SpreadShop")
public class SpreadShopEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 负责人
	 */
	private String leader;
	/**
	 * 登录账号
	 */
	private String loginAccount;
	/**
	 * 推广类型
	 */
	private String spreadType;
	/**
	 * 平均每天的花费
	 */
	private String costPerDay;
	/**
	 * 当前余额
	 */
	private String  currentBalance;
	/**
	 * 预算充值金额
	 */
	private String rechargeAmount;
	/**
	 * 店铺每日总花费
	 */
	private String totalCost;
	
}	
