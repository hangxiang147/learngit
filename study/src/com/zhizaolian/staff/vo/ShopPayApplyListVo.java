package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class ShopPayApplyListVo {
	private String spreadShopName;
	//直通车
	private String directPassMoney;
	//钻石展位
	private String showMoney;
	//品销宝
	private String saleMoney;
	private String total;
	private String pluginShopName;
	private String serviceName;
	private String serviceUse;
	private String openTime;
	private String payMoney;
	private String projectName;
	private String projectUse;
	private String projectDescription;
	private String projectPay;
}
