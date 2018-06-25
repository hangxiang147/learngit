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
@Table(name = "OA_ContractDetail")
public class ContractDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Contract_Id", unique = true)
	private Integer contract_Id;
	@Column(name = "PicId")
	private String picId;
	@Column(name = "No")
	private String no;
	@Column(name = "RequestUserId")
	private String requestUserId;
	@Column(name = "ResponsiblePersonId")
	private String responsiblePersonId;
	@Column(name = "Detail")
	private String detail;
	@Column(name = "Purpose")
	private String purpose;
	@Column(name = "SubjectPersonId")
	private String subjectPersonId;
	@Column(name = "SignPersoId")
	private String signPersoId;
	@Column(name = "StorePersonId")
	private String storePersonId;
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;
	@Column(name = "SubjectTime")
	private Date subjectTime;
	@Column(name = "SignTime")
	private Date signTime;
	@Column(name = "StorePlace")
	private String storePlace;
	@Column(name = "ProcessStatus")
	private Integer processStatus;
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	@Column(name = "AddTime")
	private Date addTime;
	@Column(name = "UpdateTime")
	private Date updateTime;
}
