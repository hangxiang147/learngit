package com.zhizaolian.staff.entity;

import java.util.Date;

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
@Table(name = "OA_SoftPerformanceSubRequirement")
public class SubReqirementEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer requirementId;
	private Integer taskId;
	private String subRequirementName;
	private String priority;
	private String description;
	private String attachmentName;
	private String attachmentPath;
	private String score;
	/**
	 * 任务的开发人员
	 */
	private String developer;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
}
