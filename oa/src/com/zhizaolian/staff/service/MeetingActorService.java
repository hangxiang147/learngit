package com.zhizaolian.staff.service;

import java.util.Set;

import com.zhizaolian.staff.vo.MeetingActorVO;

public interface MeetingActorService {
	public void saveMettingByCompany(Integer companyID,Integer departmentID,Integer meetingID,Integer type);
		
	public void saveMettingByUserID(Integer meetingID,Integer type,String userID);
	
	public Set<MeetingActorVO> findMeetingActorVOByMeetingID(Integer MeetingID,Integer actorType);

}
