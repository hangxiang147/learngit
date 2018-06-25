package com.zhizaolian.staff.entity;

import java.io.Serializable;
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
@Table(name="OA_SoftPerformanceFunction")
public class FunctionEntity  implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private Integer projectId;
	private Integer projectVersionId;
	private Integer moduleId;
	private Integer requirementId;
	private Integer subRequirementId;
	private String taskType;
	private String assignerId;
	private String assignerName;
	private String priority;
	private String estimatedTime;
	private String name;
	private String description;
	private String attachmentName;
	private String attachmentPath;
	private String score;
	private String creatorId;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private Integer result;
	private Date deadline;
	private String instanceId;
	private String testerId;
	private String ssId;
	private String isSatisfy;
	private Integer evaluate;
	private String dutyType;
	private String reason;
	
	private byte[] voDetail;
}
