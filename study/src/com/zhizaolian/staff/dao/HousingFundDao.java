package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.HousingFundEntity;

public interface HousingFundDao {
	
	void save(HousingFundEntity housingFundEntity);

	List<HousingFundEntity> findHousingFundListByProcessID(int processID);
	
	HousingFundEntity getHousingFundByID(int hfID);
	
	int deleteHousingFundByID(int hfID);
	
	void updateProcessIDByTime(int year, int month, int sspID); 
}
