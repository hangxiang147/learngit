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
@Table(name = "OA_ToBeDoneTask")
public class ToBeDoneTaskEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer type;//任务类型    BusinessTypeEnum
	
	private byte[] data;
	
	private String userId;
	
	private Integer status = 0;//0 未完成；1 完成
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
}
