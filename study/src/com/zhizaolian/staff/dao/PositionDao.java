package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.PositionEntity;

public interface PositionDao {

	List<PositionEntity> findAllPositions();
	
	List<PositionEntity> findPositionsByDepartmentID(int departmentID);
	
	PositionEntity getPositionByPositionID(int positionID);
	
	PositionEntity getPositionByPositionName(String name);
	PositionEntity getPositionByDepartmentIDName(int departmentID, String name);
	int getPositionIdByUserId(String userId);
}
