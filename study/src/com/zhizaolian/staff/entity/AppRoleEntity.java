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
@Table(name="ZZL_AppRole")
public class AppRoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer _id;
	
	private String appId;
	
	private String roleName;//角色名称
	
	private String roleDescription;//角色描述
	
	private Date addTime;
	
	private Date updateTime;
	
	private Integer isDeleted = 0;
}
