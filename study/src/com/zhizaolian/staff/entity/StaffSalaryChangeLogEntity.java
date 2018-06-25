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
@Table(name="OA_StaffSalaryChangeLog")
public class StaffSalaryChangeLogEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String staffUserId;
	
	private String operatorId;
	
	private String oldSalary;
	
	private String newSalary;
	
	private Date addTime;
	
	private Integer isDeleted;
}
