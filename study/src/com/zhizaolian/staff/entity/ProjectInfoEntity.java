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
@Table(name="OA_ProjectInfo")
public class ProjectInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String projectName;
	
	private String projectDescription;//项目描述
	
	private String attachmentIds;
	
	private String projectLeaderId;//项目负责人
	
	private String projectParticipants;//项目参与人
	
	private String finalAuditor;//项目的最终审核人
	
	private String projectProgress;//项目的进度
	
	private String userID;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
