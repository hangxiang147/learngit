package com.zhizaolian.staff.dao;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.FormalEntity;

public interface FormalDao {

	void save(FormalEntity formalEntity);
	
	List<FormalEntity> findFormalsByRequestUserID(String userID);
	
	List<FormalEntity> findFormalsByUserID(String userID, int page, int limit);
	
	int countFormalsByUserID(String userID);
	
	FormalEntity getFormalByProcessInstanceID(String processInstanceID);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	void updateActualFormalDate(String processInstanceID, Date formalDate);

	List<FormalEntity> findFormalsByConditions(String staffName, int page, int limit, String beginDate, String endDate);

	int countFormalsByConditions(String staffName, String beginDate, String endDate);
}
