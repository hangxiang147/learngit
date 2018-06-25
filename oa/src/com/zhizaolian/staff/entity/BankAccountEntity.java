package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 银行卡账号表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_BankAccount")
public class BankAccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AccountID", unique = true)
	private Integer accountID;
	
	@Column(name = "UserID")
	private String userID;  //员工ID
	
	@Column(name = "CardName")
	private String cardName;  //户主姓名
	
	@Column(name = "Bank")
	private String bank;  //开户行
	
	@Column(name = "CardNumber")
	private String cardNumber;  //银行卡卡号
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
