package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class VersionRequirementVo {
	private String requirementName;
	private String taskName;
	private Integer taskNum;
	private String estimatedTime;
	private String codeHeader;
	private String testHeader;
	private String actHeader;
	private String owner;
}
