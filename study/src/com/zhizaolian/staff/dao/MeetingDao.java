package com.zhizaolian.staff.dao;

import com.zhizaolian.staff.entity.MeetingEntity;


public interface MeetingDao {
	Integer saveMetting(MeetingEntity meetingEntity);
	
	
	public MeetingEntity getMeetingByMeetingID(Integer meetingID);
}
