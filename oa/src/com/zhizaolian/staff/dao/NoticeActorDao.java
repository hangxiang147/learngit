package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.NoticeActorEntity;

public interface NoticeActorDao {
	void saveNoticeActor(NoticeActorEntity noticeActorEntity);
	
	void updateNoticeActor(Integer ntcID,String userID);
	
	List<NoticeActorEntity> findNoticeActorsByNtcID(Integer ntcID);
	

}
