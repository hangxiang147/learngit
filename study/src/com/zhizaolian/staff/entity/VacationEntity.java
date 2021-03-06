package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请假记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Vacation")
public class VacationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VacationID", unique = true)
	private Integer vacationID;
	
	@Column(name = "UserID")
	private String userID;  //用户ID
	
	@Column(name = "RequestUserID")
	private String requestUserID;  //请假人ID
	
	@Column(name = "Hours")
	private Double hours;  //休假的小时数
	
	private Double dailyHours;//每天的工作时长
	
	@Column(name = "BeginDate")
	private Date beginDate;  //开始日期
	
	@Column(name = "EndDate")
	private Date endDate;  //结束日期
	
	@Column(name = "AgentID")
	private String agentID;  //假期工作代理人ID
	
	@Column(name = "VacationType")
	private Integer vacationType;  //请假类型
	
	@Column(name = "Reason")
	private String reason;  //请假事由
	
	@Column(name = "AttachmentImage")
	private String attachmentImage;  //请假附件
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态，1：同意；2：不同意
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	@Transient
	private String vacationUserName;
	@Transient
	private String workAgentName;
	private String type;//个人、部门
	private Integer companyID;//请假部门所在公司
	private Integer departmentID;//请假的部门
}
