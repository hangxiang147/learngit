package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_AttendanceDetail")
public class AttendanceDetailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AttendanceDetailID", unique = true)
	private Integer attendanceDetailID;
	
	@Column(name = "UserID")
	private String userID;  //用户ID
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司ID
	
	@Column(name = "AttendanceDate")
	private Date attendanceDate;  //考勤日期
	
	@Column(name = "AttendanceTime")
	private String attendanceTime;  //打卡时间
	
	@Column(name = "BeginType")
	private Integer beginType;  //上班考勤类型，1：正常；2：迟到
	
	@Column(name = "EndType")
	private Integer endType;  //下班考勤类型，1：正常；2：早退；3：加班
	
	@Column(name = "BeginDiff")
	private Long beginDiff;  //上班打卡和作息表的时间差
	
	@Column(name = "EndDiff")
	private Long endDiff;  //下班打卡和作息表的时间差
	
	@Column(name = "Note")
	private String note;
	
	private String lateStatus;
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
