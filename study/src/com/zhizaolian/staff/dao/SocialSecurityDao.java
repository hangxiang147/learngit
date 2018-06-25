package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.SocialSecurityEntity;

public interface SocialSecurityDao {

	void save(SocialSecurityEntity socialSecurityEntity);
	
	SocialSecurityEntity getSocialSecurityByID(int ssID);
	
	int deleteSocialSecurityByID(int ssID);
	
	List<SocialSecurityEntity> findSocialSecurityListByProcessID(int processID);
	
	void updateProcessIDByTime(int year, int month, int sspID);
}
