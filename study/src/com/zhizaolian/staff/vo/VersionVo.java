package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class VersionVo {
	private String id;
	private String projectId;
	private String version;
	private String creator;
	private String createTime;
	private Integer developerNum;
	private String beginDate;
	private String endDate;
	private String workHour;
	private String status;
}
