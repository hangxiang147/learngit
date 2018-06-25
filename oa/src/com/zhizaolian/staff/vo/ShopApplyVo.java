package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class ShopApplyVo extends BaseVO{
	
	private static final long serialVersionUID = 1802238377659395035L;
	
	private Integer id;
	/**
	 * 申请的业务类型（开店、关店、信息维护。。。）
	 */
	private String applyType;
	/**
	 * 店铺的平台
	 */
	private String platform;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 开店时间
	 */
	private String shopStartTime;
	/**
	 * 操作账号
	 */
	private String operationAccount;
	/**
	 * 办理人
	 */
	private String handlerId;
	
	private String handlerName;
	
	//-----------------开店-----------------
	
	/**
	 * 开店类型
	 */
	private String shopType;
	/**
	 * 一级类目
	 */
	private String firstCategory;
	/**
	 * 二级类目
	 */
	private String secondCategory;
	/**
	 * 认证企业
	 */
	private String certificatedCompany;
	/**
	 * 认证企业（个人开店）
	 */
	private String privateCertificatedCompany;
	/**
	 * 法定代表人（个人开店）
	 */
	private String privateLegalPerson;
	/**
	 * 对公银行账号
	 */
	private String publicBankAccount;
	/**
	 * 法定代表人
	 */
	private String legalPerson;
	/**
	 * 企业认证支付宝
	 */
	private String companyAliPayAccount;
	/**
	 * 企业支付宝验证手机
	 */
	private String companyAliPayPhone;
	/**
	 * 店铺负责人
	 */
	private String shopOwner;
	/**
	 * 个人认证支付宝
	 */
	private String privateAliPayAccount;
	/**
	 * 个人支付宝验证手机
	 */
	private String privateAliPayPhone;
	/**
	 * 个人与企业是否签订协议
	 */
	private String signIn;
	/**
	 * 保证金
	 */
	private String bond;
	/**
	 * 技术年费
	 */
	private String technologyAnnualFee;
	/**
	 * 平台佣金比例
	 */
	private String commissionRate;
	
	//--------------店铺信息维护--------
	
	/**
	 * 原信息
	 */
	private String oldInformation;
	/**
	 * 变更后信息
	 */
	private String changeInformation;
	
	//--------------关店-----------------
	
	/**
	 * 关店原因 
	 */
	private String closeShopReason;
	/**
	 * 关店时间
	 */
	private String closeShopTime;
	/**
	 * 财产接收人
	 */
	private String propertyReceiver;
	
	private String userID;
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
		
	}
}
