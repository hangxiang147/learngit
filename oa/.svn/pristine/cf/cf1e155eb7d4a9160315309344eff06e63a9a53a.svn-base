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
@Table(name="OA_CourseJoiner")
public class CourseJoinerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 课时表id
	 */
	private Integer courseId; 
	/**
	 * 人员id
	 */
	private String joinUserIds;
	
	private String companyIds;
	
	private String depIds;
	/**
	 * 个人/部门
	 */
	private String type;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
