package com.zhizaolian.staff.service;


import java.util.Set;
import com.zhizaolian.staff.vo.NoticeActorVO;


public interface NoticeActorService {
	void saveNoticeActorVO(NoticeActorVO noticeActorVO);

	void updateNoticeActor(Integer ntcID,String userID);
	
	void saveNoticeListByIDs(Integer companyID,Integer departmentID,Integer ntcActorID);
	/**
	 * 接受通知人ID
	 * @param userID
	 * @return
	 */
	Set<NoticeActorVO> findNtcActorVOByNtcID(Integer ntcID);
	}
