package com.zhizaolian.staff.vo;

import lombok.Data;
@Data
public class AssetUsageVO {
	private Integer usageID;
	private Integer assetID;
	private String assetName;
	private String recipientID;
	private String recipientName;
	private Integer companyID;
	private String  companyName;
	private Integer departmentID;
	private String departmentName;
	private String receiveLocation;
	private String receiveTime;	private String receiveOperatorID;
	private String receiveOperatorName;
	private String receiveNote;
	private String returnLocation;
	private String returnTime;
	private String returnOperatorID;
	private String returnOperatorName;
	private String returnNote;
	private Integer status;
	private Integer[] assetIDs;
	private Integer[] usageIDs;
	private AssetVO assetVO;
}
