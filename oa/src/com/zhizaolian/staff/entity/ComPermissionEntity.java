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
 * com系统权限数据
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name="ZZL_ComPermission")
public class ComPermissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String permissionName;
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
}
