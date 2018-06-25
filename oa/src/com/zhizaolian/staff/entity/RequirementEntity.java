package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="OA_SoftPerformanceRequirement")
public class RequirementEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private Integer projectId;
	private Integer moduleId;
	private Integer projectVersionId;
	private String name;
	private String description;
	private String attachmentName;
	private String attachmentPath;
	private String source;
	private String ownerId;
	private String ownerName;
	private String creatorId;
	private String priority;
	private String remark;
	private String reviewerId;
	private String reviewerName;
	private String checkStandard;
	private String status;
	private String stage;
	private Integer divide;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private Integer back;
	private String returnReason;
	private String deleteReason;
}
