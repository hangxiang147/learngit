package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.GroupDetailEntity;

public interface GroupDetailDao {

	void save(GroupDetailEntity groupDetailEntity);
	
	GroupDetailEntity getGroupDetailByID(int groupDetailID);
	
	List<GroupDetailEntity> findGroupDetailPageList(int companyID, int departmentID, int positionID, int page, int limit);

	int countGroupDetails(Integer companyID, List<Integer> departmentIDs, Integer positionID);
	
	List<GroupDetailEntity> findGroupDetailsByGroupIDs(List<String> groupIDs);
	GroupDetailEntity getGroupDetailByGroupID(String groupID);
	
	GroupDetailEntity geDetailEntityByDepartmentIDPositionID(int departmentID, int positionID);}
