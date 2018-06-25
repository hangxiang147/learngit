package com.zhizaolian.staff.service;

import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.MeetingVO;

public interface MeetingService {
	Integer saveMeeting(MeetingVO meetingVO);
	
	ListResult<MeetingVO> findMeetingList(int page,int limit,String userID);
	
	public MeetingVO getMeetingByMeetingID(Integer meetingID);
}
