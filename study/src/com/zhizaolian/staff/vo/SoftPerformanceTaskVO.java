package com.zhizaolian.staff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoftPerformanceTaskVO extends TaskVO{

	//截止时间
	private String limitTime;
	
	private String score;
	
	private String projectName;
	private String versionName;
	private String requirementName;
	private String moduleName;
}
