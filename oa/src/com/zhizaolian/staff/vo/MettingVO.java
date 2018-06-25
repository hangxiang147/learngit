package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MettingVO {
	private Integer meetingID;
	private String sponsorID;
	private Date beginTime;
	private Date endTime;
	private String place;
	private Integer meetingType;
	private String theme;
	private String content;
	private String companyIDs;
	private String departmentIDs;

}
