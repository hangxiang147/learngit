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
@Table(name="OA_VersionFuncionInfo")
public class VersionFuncionInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String versionName;
	
	private byte[] functions;
	
	private String operator;
	
	private String versionDate;
	
	private Date addTime;
	
	private Date updateTime;
	
	private Integer isDeleted;
	@Transient
	private String[] function;
}
