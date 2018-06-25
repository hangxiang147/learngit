package com.zhizaolian.staff.entity;

import java.util.Date;

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
@Table(name = "OA_CertificateBorrow")
public class CertificateBorrowEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certificateBorrowId", unique = true)
	private Integer certificateBorrowId;
	private Integer certificateId;
	@Transient
	private String certificateName;
	private String userId;
	@Transient
	private String userName;
	private Integer isBorrow;
	private String reason;
	private Date startTime;
	private Date endTime;
	private Date realStartTime;
	private Date realEndTime;
	private String processInstanceID;
	private Integer processStatus;
	private Integer applyResult;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
}
