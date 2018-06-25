package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;

@Data
public class MeetingActorVO {
	private Integer mtActorID;
	private Integer meetingID;
	private String userID;
	private Integer actorType;
	private Integer addType;
	private String userName;
	private List<String> groupList;
}
