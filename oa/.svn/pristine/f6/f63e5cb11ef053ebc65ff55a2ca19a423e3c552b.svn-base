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
@Table(name="OA_ProblemOrder")
public class ProblemOrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer projectId;
	
	private Integer projectVersionId;
	
	private Integer moduleId;
	
	private String orderName;//bug名
	
	private String score;
	
	private String description;
	
	private String attachmentIds;
	
	private String processStatus;
	
	private String processInstanceID;
	
	private String developerId;
	
	private String creatorId;
	
	private String questionerId;
	
	private String dutyPersonId;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
