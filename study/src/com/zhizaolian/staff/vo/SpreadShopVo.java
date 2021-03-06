package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class SpreadShopVo {
	private String userID;
	private String userName;
	private String[] shopName;
	private String[] leader;
	private String[] loginAccount;
	private String[] spreadType;
	private Double[] costPerDay;
	private Double[] rechargeAmount;
	private Double[] currentBalance;
}
