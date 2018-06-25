package com.zhizaolian.staff.dao;


import java.util.List;


import com.zhizaolian.staff.entity.SpecialEntity;

public interface SpecialDao {
	void save(SpecialEntity specialEntity);
	List<SpecialEntity> findSpecialListByTypeAndID(String userID,Integer type);
	void deleteSpecial(Integer specialID);
	SpecialEntity getSpecialByTypeAndID(String userID,Integer type);
}
