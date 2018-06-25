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
@Table(name = "OA_BrandAuth")
public class BrandAuthEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String companyName;//授权公司
	
	private String platform;
	
	private String shopName;
	
	private String shopAddress;
	
	private String authBeginDate;
	
	private String authEndDate;
	
	private String contact;//联系人
	
	private String telephone;
	
	private String brand;
	
	private String userId;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
	private String chopProcessInstanceID;//公章申请的流程实例ID
	
}
