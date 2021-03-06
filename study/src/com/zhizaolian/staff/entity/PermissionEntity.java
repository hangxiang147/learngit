package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 权限表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Permission")
public class PermissionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PermissionID", unique = true)
	private Integer permissionID;  
	
	@Column(name = "PermissionName")
	private String permissionName;  //权限名称
	
	@Column(name = "PermissionCode")
	private String permissionCode;  //权限代码
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	private Integer process;
	
	private String processKeys;//这里的流程key值可以是多个
	
	private String nodeKey;
	
	private Integer type;//值：1,2  //1：指定个人；2：候选人
	
	private String mapKey;
}
