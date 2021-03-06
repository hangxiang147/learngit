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

/**
 * 注册应用信息
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name="ZZL_AppInfo")
public class AppInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String appId;//应用唯一性id
	
	private String appSecret;//应用密钥
	
	private String appName;//应用名
	
	private String goHomeUrl;//跳转链接
	
	private String saveUserInfoUrl;//app获取用户信息保存Session接口
	
	private String description;//应用简介
	
	private Integer iconId;//应用图标
	
	private String creatorId;
	
	private Date addTime;
	
	private Date updateTime;
	
	private Integer isDeleted = 0;
	@Transient
	private String creatorName;
}
