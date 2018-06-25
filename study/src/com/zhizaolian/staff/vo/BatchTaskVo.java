package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BatchTaskVo {
	private Integer projectId;
	private Integer versionId;
	private Integer[] moduleId;
	private Integer[] requirementId;
	private String[] assignerId;
	private String[] assignerName;
	private String[] name;
	private String[] taskType;
	private String[] priority;
	private String[] estimatedTime;
	private Date[] deadLine;
	private String[] description;
	private String[] score;
	private String projectName;
	private String versionName;
	private String[] moduleName;
	private String[] requirementName;
}
