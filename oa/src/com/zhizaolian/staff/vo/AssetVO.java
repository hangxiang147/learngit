package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
@Data
public class AssetVO {
	
	private Integer assetID;
	private String assetName;
	private String serialNumber;
	private Integer type;
	private String model;
	private Double amount;
	private Integer companyID;
	private String  purchaseTime;
	private String storageLocation;
	private Integer status;	
	private Integer[] assetIDs;
	private List<AssetUsageVO> assetUsageVOs;
	private AssetUsageVO assetUsageVO;
	private String deviceID;       //设备序列号
}
