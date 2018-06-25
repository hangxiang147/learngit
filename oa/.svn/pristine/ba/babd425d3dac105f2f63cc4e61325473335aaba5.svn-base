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

/**
 * 岗位信息表
 * 岗位由公司，部门和职务组成
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_GroupDetail")
public class GroupDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GroupDetailID", unique = true)
	private Integer groupDetailID;
	
	@Column(name = "GroupID")
	private String groupID;  //员工组表主键
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司表主键

	@Column(name = "DepartmentID")
	private Integer departmentID;  //部门表主键
	
	@Column(name = "PositionID")
	private Integer positionID;  //ְ职务表主键
	
	@Column(name = "Responsibility")
	private String responsibility;  //岗位职责描述
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
