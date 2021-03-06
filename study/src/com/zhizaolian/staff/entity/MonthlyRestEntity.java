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
@Table(name="OA_MonthlyRest")
public class MonthlyRestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String year;
	
	private String month;
	
	private String restDays;
	
	private String workDays;
	
	private Integer isDelete;
	
	private Date addTime;
	
	private Date updateTime;
}
