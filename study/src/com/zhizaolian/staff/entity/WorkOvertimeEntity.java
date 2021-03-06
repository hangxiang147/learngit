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
@Table(name="OA_WorkOvertime")
public class WorkOvertimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 加班开始时间
	 */
	private String beginDate;
	/**
	 * 加班结束时间
	 */
	private String endDate;
	/**
	 * 加班时长
	 */
	private String workHours;
	/**
	 * 加班原因
	 */
	private String reason;
	
	private String userID;
	private String requestUserName;
	private String requestUserID;//所有加班人员的id
	private String type;//个人、部门
	private String companyId;//加班部门所在公司
	private String departmentId;//加班的部门
	/**
	 * 审批结果
	 */
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
