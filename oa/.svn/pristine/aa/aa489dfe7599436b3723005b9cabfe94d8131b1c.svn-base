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
@Table(name = "OA_Job")
public class JobEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true)
	private Integer id;
	@Column(name = "JobName")
	private String jobName;
	@Column(name = "JobEName")
	private String jobEName;
	@Column(name = "JobDescription")
	private String jobDescription;
	@Column(name = "SubjectPersonId")
	private String subjectPersonId;
	@Column(name = "SubjectPersonNames")
	private String subjectPersonNames;
	@Column(name = "TechnologySubjectPersonNames")
	private String technologySubjectPersonNames;
	@Column(name = "TechnologySubjectPersonId")
	private String technologySubjectPersonId;
	@Column(name = "IsDelete")
	private Integer isDelete;
	@Column(name = "AddTime")
	private Date addTime;
	@Column(name = "UpdateTime")
	private Date updateTime;
}
