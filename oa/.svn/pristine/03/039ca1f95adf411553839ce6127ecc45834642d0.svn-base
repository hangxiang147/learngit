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
 * 职位表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Position")
public class PositionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PositionID", unique = true)
	private Integer positionID; 
	
	@Column(name = "PositionName")
	private String positionName;  //职位名称
	
	@Column(name = "DepartmentID")
	private Integer departmentID;  //部门ID
	
	@Column(name = "ParentID")
	private Integer parentID;  //所属上级职位ID
	
	@Column(name = "Sort")
	private Float sort;  //排序字段
	
	@Column(name = "Level")
	private Integer level;  //职位级数
	
	@Column(name = "Description")
	private String description;  //ְ职位描述
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;  
	
	@Column(name = "AddTime")
	private Date addTime;  
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	private Integer positionType;//1：白领、2：蓝领
	
}
