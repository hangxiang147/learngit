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
 * 花名记录表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Nickname")
public class NicknameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NicknameID", unique = true)
	private Integer nicknameID;
	
	@Column(name = "Name")
	private String name;  //花名
	
	@Column(name = "Type")
	private Integer type;  //花名类型
	
	@Column(name = "Status")
	private Integer status;  //使用状态，0：未使用；1：已使用
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
