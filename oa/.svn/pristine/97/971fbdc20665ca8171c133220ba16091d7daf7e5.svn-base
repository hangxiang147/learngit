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
 * 离职记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Resignation")
public class ResignationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ResignationID", unique = true)
	private Integer resignationID;
	
	@Column(name = "UserID")
	private String userID;  //填写离职申请的用户ID
	
	@Column(name = "RequestUserID")
	private String requestUserID;  //离职人ID
	
	@Column(name = "LeaveDate")
	private Date leaveDate;  //离职日期
	
	@Column(name = "Reasons")
	private String reasons;  //离职原因
	
	@Column(name = "Note")
	private String note;  //备注
	
	@Column(name = "SupervisorConfirmDate")
	private Date supervisorConfirmDate;  //主管确认离职日期
	
	@Column(name = "ManagerConfirmDate")
	private Date managerConfirmDate;  //总经理确认离职日期	
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态，1：通过；2：不通过
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
