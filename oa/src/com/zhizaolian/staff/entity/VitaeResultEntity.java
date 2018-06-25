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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_VitaeResult")
public class VitaeResultEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "vitaeSignId")
	private Integer vitaeSignId;
	@Column(name = "nextTaskId")
	private Integer nextTaskId;
	// 0:没有来应聘   1:前来应聘  2:应聘通过 3:应聘不通过4:未入职,5:已入职,6:本次面试通过 但需要 继续复试
	@Column(name = "type")
	private Integer type;
	//预计面试时间
	@Column(name = "guessVitaeTime")
	private Date guessVitaeTime;
	//上次面试结果的Id
	@Column(name = "prevId")
	private Integer prevId;
	//假如是复试的条目   记录本次复试设置的初始面试者
	private String ids;
	private String names;
	private String tIds;
	private String tNames;
	
	@Column(name = "reason")
	private String reason;
	//第一步 由人事 最终通知的面试人员 Ids(有序)
	@Column(name = "subjectPersonsId")
	private String subjectPersonsId;
	//最终技术面试人员id
	@Column(name = "TechnologySubjectPersonId")
	private String technologySubjectPersonId;
	//每个人面试的结果(有序)
	@Column(name = "subjectResult")
	private String subjectResult;
	//每个人面试的打分
	@Column(name = "scoreResult")
	private String scoreResult;
	@Column(name = "step1Time")
	private Date step1Time;
	@Transient
	private String step1Name;
	@Column(name = "step1UserId")
	private String step1UserId;
	@Column(name = "step2Time")
	private Date step2Time;
	//第二步 其实是不需要的 因为 是 业务上 是应聘者录入信息
	@Column(name = "step2UserId")
	private String step2UserId;
	@Column(name = "step3Time")
	private Date step3Time;
	@Column(name = "step3UserId")
	@Transient
	private String step3Name;
	private String step3UserId;
	@Column(name = "step4Time")
	private Date step4Time;
	@Transient
	private String step4Name;
	@Column(name = "step4UserId")
	private String step4UserId;
	//结果的一些详情  列如 面试官的一些意见
	@Column(name = "detail")
	private String detail;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
	@Transient
	private String xm;
}