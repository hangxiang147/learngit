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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_VitaeSignJobHistory")
public class VitaeSignJobHistoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "vitaeSignId")
	private Integer vitaeSignId;
	@Column(name = "startTime")
	private String startTime;
	@Column(name = "endTime")
	private String endTime;
	@Column(name = "companyName")
	private String companyName;
	@Column(name = "guimo")
	private Integer guimo;
	@Column(name = "position")
	private String position;
	@Column(name = "daiyu")
	private String daiyu;
	@Column(name = "sftgyhls")
	private Integer sftgyhls;
	@Column(name = "zmrdh")
	private String zmrdh;
	@Column(name = "lzyy")
	private String lzyy;
	@Column(name = "sftglzzm")
	private Integer sftglzzm;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
}