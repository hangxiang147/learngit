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
 * 查看员工工作日报
 * @author yxl
 */
@Data
@Entity
@Table(name="OA_ViewWorkReport")
public class ViewWorkReportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String requestUserId;//申请人id
	
	private String companyIds;
	
	private String depIds;
	
	private String userIds;//页面选择的人员Id
	
	private String allUserIds;//被查看所有人员Id
	
	private String viewType;
	
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
}
