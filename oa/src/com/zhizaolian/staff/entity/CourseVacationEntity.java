package com.zhizaolian.staff.entity;

import java.io.Serializable;
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
@Table(name="OA_CourseVacation")
public class CourseVacationEntity implements Serializable{
	@Transient
	private static final long serialVersionUID = 2970812974086589687L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer courseId;
	
	private String coursePlanIds;
	
	private String userId;
	
	private String reason;
	
	private String processStatus;
	
	private String processInstanceID;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String userName;
	@Transient
	private String title;
	
	private String requestDate;
	@Transient
	private String courseName;
	@Transient
	private String coursePlanBeginTimes;
	@Transient
	private String taskId;
	@Transient
	private Integer vacationPersonNum;
	@Transient
	private String vacationPersonNames;
	@Transient
	private String joinerNum;
	@Transient
	private String trainClass;
}
