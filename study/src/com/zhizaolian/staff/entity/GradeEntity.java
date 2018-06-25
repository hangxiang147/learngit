package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 职级表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Grade")
public class GradeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GradeID", unique = true)
	private Integer gradeID;
	
	@Column(name = "GradeName")
	private String gradeName;  //级别名称
	
	@Column(name = "Sort")
	private Float sort;  //排序字段
	
	@Column(name = "Description")
	private String description;  //级别描述
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
