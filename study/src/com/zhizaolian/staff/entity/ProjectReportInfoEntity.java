package com.zhizaolian.staff.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
@Data
@Entity
@Table(name="OA_ProjectReport")
public class ProjectReportInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer projectInfoId;
	
	private String reportUserId;
	
	private String reportUserName;
	
	private String progress;//进行中；已完结
	
	private String attachmentIds;
	
	private String reportContent;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime; 
	@Transient
	private List<CommonAttachment> attaList;
}
