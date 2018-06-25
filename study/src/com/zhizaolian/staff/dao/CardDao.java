package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.CardEntity;

public interface CardDao {

	void save(CardEntity cardEntity);
	
	List<CardEntity> findCardsByUserID(String userID, int page, int limit);
	
	int countCardsByUserID(String userID);
	
	CardEntity getCardByProcessInstanceID(String processInstanceID);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
}
