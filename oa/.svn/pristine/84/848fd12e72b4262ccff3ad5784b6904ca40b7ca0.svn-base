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
@Table(name = "OA_VitaeSignEdu")
public class VitaeSignEduEntity {
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
	@Column(name = "sortIndex")
	private Integer sortIndex;
	@Column(name = "schoolName")
	private String schoolName;
	@Column(name = "zhuanye")
	private String zhuanye;
	@Column(name = "xueli")
	private String xueli;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
}