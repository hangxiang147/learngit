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
@Table(name = "OA_CarUse")
public class CarUseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CarUse_Id", unique = true)
	private Integer carUse_Id;
	@Column(name = "RequestUserID")
	private String requestUserID;
	@Column(name = "HandlePersonId")
	private String handlePersonId;
	@Column(name = "UseTime")
	private String useTime;
	@Column(name = "ProcessStatus")
	private Integer processStatus;
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	@Column(name = "AddTime")
	private Date addTime;
	@Column(name = "UpdateTime")
	private Date updateTime;
}
