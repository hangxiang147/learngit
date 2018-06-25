package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.MeetingActorEntity;

public interface MeetingActorDao {
	void save(MeetingActorEntity meetingActorEntity);
	
	public List<MeetingActorEntity> findMeetingActorByMeetingID(Integer meetingID,Integer actorType);
}
