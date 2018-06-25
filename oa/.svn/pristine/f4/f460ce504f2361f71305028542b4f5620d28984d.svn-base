package com.zhizaolian.staff.entity;

import java.io.Serializable;
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
@Table(name="OA_EasyProcess")
public class EasyProcessEntity implements Serializable{
	private static final long serialVersionUID = -3107691241220184036L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private byte[] data;//业务数据
	
	private Integer businessType;//业务类型
	
	private Integer year;
	
	private Integer month;
	
	private String userId;
	
	private String requestUserId;
	
	private Integer applyResult;
	
	private String processInstanceID;
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
}
