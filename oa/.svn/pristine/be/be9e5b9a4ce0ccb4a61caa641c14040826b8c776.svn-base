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
@Table(name = "OA_AppVersion")
public class AppVersionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VersionID", unique = true)
	private Integer versionID;
	
	@Column(name = "Type")
	private Integer type;  //app类型，1：Android；2：IOS
	
	@Column(name = "Version")
	private String version;  //版本号
	
	@Column(name = "ReleaseTime")
	private Date releaseTime;  //版本发布时间
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
