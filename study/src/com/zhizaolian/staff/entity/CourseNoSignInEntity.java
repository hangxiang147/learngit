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
@Table(name="OA_CourseSignIn")
public class CourseNoSignInEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 课时表id
	 */
	private Integer coursePlanId; 
	
	private String userId;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
