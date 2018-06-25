package com.zhizaolian.staff.dao;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.ResignationEntity;

public interface ResignationDao {

	void save(ResignationEntity resignationEntity);
	
	List<ResignationEntity> findResignationsByUserID(String userID, int page, int limit);
	
	int countResignationsByUserID(String userID);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	void updateSupervisorConfirmDate(String processInstanceID, Date leaveDate);
	
	void updateManagerConfirmDate(String processInstanceID, Date leaveDate);
}
