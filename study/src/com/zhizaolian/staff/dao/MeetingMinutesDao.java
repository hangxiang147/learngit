package com.zhizaolian.staff.dao;


import java.util.List;

import com.zhizaolian.staff.entity.MeetingMinutesEntity;

public interface MeetingMinutesDao {
	Integer save(MeetingMinutesEntity meetingMinutesEntity);
	
	public List<MeetingMinutesEntity> getMeetingMinutesByMeetingID(Integer meetingID);
}
