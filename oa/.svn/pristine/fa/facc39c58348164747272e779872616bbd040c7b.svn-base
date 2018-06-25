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
@Table(name="OA_ChangeBankAccount")
public class ChangeBankAccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	/**
	 * 账户所属单位名称
	 */
	private String accountCompanyName;
	/**
	 * 账户类别
	 */
	private String accountType;
	/**
	 * 开户行全称
	 */
	private String bankName;
	/**
	 * 账号
	 */
	private String accountNumber;
	/**
	 * 申请类型
	 */
	private String applyType;
	/**
	 * 开户依据
	 */
	private String newAccountReason;
	/**
	 * 账户用途
	 */
	private String accountUse;
	/**
	 * 变更事项
	 */
	private String changeItem;
	/**
	 * 变更后信息
	 */
	private String afterChangeInfo;
	/**
	 * 变更原因
	 */
	private String changeReason;
	/**
	 * 销户原因
	 */
	private String deleteAccountReason;
	/**
	 * 资金去向
	 */
	private String moneyWhere;
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
