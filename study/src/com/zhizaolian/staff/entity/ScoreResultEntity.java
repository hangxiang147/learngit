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
@Table(name = "OA_SoftPerformanceScoreResult")
public class ScoreResultEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "itemDate")
	private Date itemDate;
	@Column(name = "userId")
	private String userId;
	@Column(name = "taskId")
	private Integer taskId;
	@Column(name = "duty")
	private String duty;
	@Column(name = "reason")
	private String reason;
	@Column(name = "addPersonId")
	private String addPersonId;
	private Integer isDeleted;
	@Column(name = "resultScore" ,columnDefinition = "decimal(6,2)")
	private Double resultScore;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
	private String versionId;
	private Integer problemTaskId;
}