package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="OA_WeekReporter")
public class WeekReporterEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String userId;
	
	private String userName;
	
	private String partner;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
	@Transient
	private String companyName;
	@Transient
	private String departmentName;
}
