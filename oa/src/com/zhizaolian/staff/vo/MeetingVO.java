package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class MeetingVO {
	private Integer meetingID;
	private String sponsorID;
	private String sponsorName;
	private String beginTime;
	private String endTime;
	private String place;
	private Integer meetingType;
	private String theme;
	private String content;
   
	private Integer[] ppCompanyIDs;//参与人公司
	private String ppCompanys;
	private Integer[] ppDepartmentIDs;//参与人部门
	private String ppDepartments;

	private Integer[] ccCompanyIDs;//抄送人公司
	private String ccCompanys;

	private Integer[] ccDepartmentIDs;//抄送人部门

	private String uploadNames;
	
	
	private String ccDepartments;
	private String[] ppIDs;
	private String[] ccIDs;
	private Integer isNotice;//是否发送通知
	
	private String contentMins;
}
