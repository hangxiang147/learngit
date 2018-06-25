package com.zhizaolian.staff.dao;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.AssignmentEntity;

public interface AssignmentDao {

	void save(AssignmentEntity assignmentEntity);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	void updateBeginDate(String processInstanceID, Date beginDate);
	
	void updateScore(String processInstanceID, Float score);
	
	List<AssignmentEntity> findAssignmentsByUserID(String userID, int page, int limit);
	
	int countAssignmentsByUserID(String userID);
	
	AssignmentEntity getAssignmentByProcessInstanceID(String processInstanceID);
}
