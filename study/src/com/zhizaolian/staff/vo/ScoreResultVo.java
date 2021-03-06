package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.Data;
@Data
public class ScoreResultVo {
	private Integer id;
	private String addPersonId;
	private String addPersonName;
	private Date itemDate;
	private String reason;
	private Double resultScore;
	private String userId;
	private String userName;
	private Integer taskId;
	private String duty;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	private String versionId;
	private String problemTaskId;
}
