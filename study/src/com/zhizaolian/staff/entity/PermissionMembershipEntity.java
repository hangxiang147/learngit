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
 * 权限关系表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_PermissionMembership")
public class PermissionMembershipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	@Column(name = "PermissionID")
	private Integer permissionID;  //权限ID
	
	@Column(name = "UserGroupID")
	private String userGroupID;  //拥有权限的用户ID或者员工组ID
	
	@Column(name = "Type")
	private Integer type;  //userGroupID的类型，1：UserID；2：GroupID
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
