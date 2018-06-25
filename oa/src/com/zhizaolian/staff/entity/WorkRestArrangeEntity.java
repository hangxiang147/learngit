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
@Table(name="OA_WorkRestArrange")
public class WorkRestArrangeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 班次id
	 */
	private String workRestId;
	
	private String beginTime;
	
	private String endTime;
	
	private String companyId;
	
	private String departmentId;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private WorkRestTimeEntity workRestTime;
	@Transient
	private String companyName;
	@Transient
	private String departmentName;
}
