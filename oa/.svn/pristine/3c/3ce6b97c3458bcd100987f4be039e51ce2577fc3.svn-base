package com.zhizaolian.staff.dao;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.CarUseEntity;


public interface CarUseDao {
	void save(CarUseEntity carUseEntity);
	void update(CarUseEntity carUseEntity);
	void delete(String Id);
	List<CarUseEntity> getCarUseByKeys(Map<String, String> params, int page,int limit);
	CarUseEntity getUniqueCarUseEntity(Map<String, String> params);
	int getCarUseCountByKeys(Map<String, String> params);
	List<String> getTodayDetail(); 
	
}
