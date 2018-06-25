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
@Table(name = "OA_Asset")
public class AssetEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AssetID", unique = true)
	private Integer assetID;
	
	@Column(name="AssetName")
	private String assetName;		//资产名称
	
	@Column(name="SerialNumber")
	private String serialNumber;	//资产条码
	
	@Column(name="Type")			
	private Integer type;			//资产类别，1：行政办公设备；2：电子产品；3：通信设备；4：交通工具	
	
	@Column(name="Model")
	private String model;			//规格型号
	
	@Column(name="Amount")
	private Double amount;			//金额
	
	@Column(name="CompanyID")
	private Integer companyID;		//所属公司
	
	@Column(name="PurchaseTime")
	private Date purchaseTime;		//购入时间
	
	@Column(name="StorageLocation")
	private String storageLocation;	//存放地点
	
	@Column(name="Status")
	private Integer status;			//状态，0：闲置；1：在用
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	private String deviceID;       //设备序列号
}
