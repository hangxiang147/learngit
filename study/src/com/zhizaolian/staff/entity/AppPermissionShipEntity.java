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
@Table(name="ZZL_AppPermissionShip")
public class AppPermissionShipEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer roleId;
	
	private Integer permissionId;
	
	private Date addTime;
	
	private Date updateTime;
	
	private Integer isDeleted = 0;
	
}	
