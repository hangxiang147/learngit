package com.zhizaolian.staff.vo;



import lombok.Data;

@Data
public class MeetingMinutesVO {
     
	private Integer mMID;

	private Integer meetingID;//会议ID

	private String[] contents;//纪要内容
	
	private String[] ownerIDs;//负责人
	
	private String attachementNames;  //上传附件文件名
	
	private String ownerName;
	
	private String content;
	
	private String ownerID;
}
