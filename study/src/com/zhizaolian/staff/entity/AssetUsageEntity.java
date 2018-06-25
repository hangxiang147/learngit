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
@Table(name="OA_AssetUsage")
public class AssetUsageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UsageID", unique = true)
	private Integer usageID;
	
	@Column(name="AssetID")
	private Integer assetID;             		//资产id
	
	@Column(name="RecipientID")
	private String recipientID;					//领用人id
	
	@Column(name="CompanyID")
	private Integer companyID;					//领用公司id
	
	@Column(name="DepartmentID")
	private Integer departmentID;				//使用部门id
	
	@Column(name="ReceiveLocation")
	private String receiveLocation;				//领用后存放地点
	
	@Column(name="ReceiveTime")
	private Date receiveTime;					//领用时间				
	
	@Column(name="ReceiveOperatorID")
	private String receiveOperatorID;				//领用处理人
	
	@Column(name="ReceiveNote")
	private String receiveNote;					//领用备注
	
	@Column(name="ReturnLocation")
	private String returnLocation;				//退库后存放地点
	
	@Column(name="ReturnTime")
	private Date returnTime;					//退库时间
	
	@Column(name="ReturnOperatorID")
	private String returnOperatorID;			//退库处理人
	
	@Column(name="ReturnNote")
	private String returnNote;					//退库备注
	
	@Column(name="Status")
	private Integer status;						//状态
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
