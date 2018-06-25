package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class SubRequirementVo {
	private Integer id;
	private Integer taskId;
	private Integer requirementId;
	private String subRequirementName;
	private Integer count;
	private String priority;
	private String description;
	private String attachmentName;
	private String attachmentPath;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	private List<String> attachmentNames;
	private List<String> attachmentPaths;
	private String score;
	private boolean canEdit;
	private String developer;
}
