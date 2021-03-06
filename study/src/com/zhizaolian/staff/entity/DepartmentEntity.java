package com.zhizaolian.staff.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 部门表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Department")
public class DepartmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DepartmentID", unique = true)
	private Integer departmentID;
	
	@Column(name = "DepartmentName")
	private String departmentName;  //部门名称
	
	@Column(name = "CompanyID")
	private Integer companyID;  //所属公司ID
	
	@Column(name = "ParentID")
	private Integer parentID;  //上级部门ID
	
	@Column(name = "Sort")
	private Float sort;  //排序字段
	
	@Column(name = "Level")
	private Integer level;  //部门级数
	
	@Column(name = "Note")
	private String note;  //备注
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	@Transient
	private List<String> departmentIds;
	@Transient
	private List<String> departmentNames;
}
