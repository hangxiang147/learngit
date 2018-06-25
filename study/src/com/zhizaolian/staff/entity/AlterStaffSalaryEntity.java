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
@Data
@Entity
@Table(name="OA_AlterStaffSalary")
public class AlterStaffSalaryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private byte[] beforeSalaryData;
	
	private byte[] afterSalaryData;
	
	private String effectDate; //薪资变更的生效时间
	
	private String attachmentIds;
	
	private String userId;
	
	private String requestUserId;
	
	private Integer applyResult;
	
	private String processInstanceID;
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private StaffSalaryEntity beforeSalary;
	@Transient
	private StaffSalaryEntity afterSalary;
	@Transient
	private String staffName;
	@Transient
	private String userName;
	@Transient
	private String status;
	@Transient
	private String assigneeUserName;
	@Transient
	private List<String> attaIds;
	@Transient
	private String department;
	@Transient
	private String taskId;
}
