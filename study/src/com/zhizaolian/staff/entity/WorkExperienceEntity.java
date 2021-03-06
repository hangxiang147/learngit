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
 * 员工工作经历记录表
 * @author jndxz
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OA_WorkExperience")
public class WorkExperienceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WEID", unique = true)
	private Integer weID;  // 自增主键
	
	@Column(name = "UserID")
	private String userID;  //员工ID
	
	@Column(name = "Company")
	private String company;  //企业名称
	
	@Column(name = "Years")
	private Double years;  //工作时间
	
	private String beginDate;//开始时间
	
	private String endDate;//结束时间
	
	@Column(name = "Description")
	private String description;  //工作经历描述	
	
	@Column(name = "Referee")
	private String referee;  //证明人
	
	@Column(name = "Telephone")
	private String telephone;  //证明人联系电话

	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime; 
}
