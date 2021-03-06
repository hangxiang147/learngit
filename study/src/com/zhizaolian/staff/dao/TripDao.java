package com.zhizaolian.staff.dao;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.TripEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;

public interface TripDao {
	void save(TripEntity tripEntity);

	List<TripEntity> findTripByUserID(String userID, int page, int limit);
	
	int countTripByUserID(String userId);
	
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	List<TripEntity> getTripByKeys(Map<String, String> map,int page,int limit);
	int  getCountOfTripByKeys(Map<String, String > map);

}
