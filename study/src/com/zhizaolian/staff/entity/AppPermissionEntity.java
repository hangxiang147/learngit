package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="ZZL_AppPermission")
public class AppPermissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer _id;
	
	private Integer parentId;
	
	private String appId;
	
	private String permissionName;
	
	private String permissionCode;
	
	private String type;
	
	private String sort;
	
	private String isUsed;//是否启用，仅限于mes
	
	private String pageUrl;//程式页面地址，仅限于mes
	
	private String requestUrl;
	
	private Date addTime;
	
	private Date updateTime;
	
	private Integer isDeleted = 0;
	@Transient
	private String level;
}
