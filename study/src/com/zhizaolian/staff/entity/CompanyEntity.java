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
 * 公司表
 * @author zpp
 *
 */
@Data
@Entity
@Table(name = "OA_Company")
public class CompanyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CompanyID", unique = true)
	private Integer companyID;
	
	@Column(name = "CompanyName")
	private String companyName;	 //公司名称
	
	@Column(name = "Code")
	private String code;  //公司代码，用作员工工号前两位
	
	@Column(name = "ParentID")
	private Integer parentID;  //所属上级公司ID，总公司的ParentID为0
	
	@Column(name = "Sort")
	private Float sort;  //排序字段
	
	@Column(name = "Level")
	private Integer level;  //公司级数
	
	@Column(name = "Address")
	private String address;  //公司地址
	
	@Column(name = "Telephone")
	private String telephone;  //联系方式
	
	@Column(name = "Note")
	private String note;  //备注
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
}
