package com.zhizaolian.staff.dao;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.CarpoolEntity;

public interface CarpoolDao {

	List<CarpoolEntity> getCarpoolList(Map<String, String> params,int page,int size);
	int getCarpoolCount(Map<String, String> params);
	void save(CarpoolEntity carpoolEntity);
	void updateCarpool(CarpoolEntity carpoolEntity);
	void delete(String id);
}
