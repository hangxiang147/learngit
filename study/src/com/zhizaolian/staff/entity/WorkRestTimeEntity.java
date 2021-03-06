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
@Table(name="OA_WorkRestTime")
public class WorkRestTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 班次名称
	 */
	private String workRestName;
	/**
	 * 出勤工时
	 */
	private String workHours;
	
	private String workBeginTime;
	
	private String workEndTime;
	
	private String restBeginTime;
	
	private String restEndTime;
	
	private String workOverBeginTime;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
