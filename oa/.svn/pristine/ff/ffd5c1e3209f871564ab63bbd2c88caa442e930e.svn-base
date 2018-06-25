package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.SocialSecurityProcessEntity;

public interface SocialSecurityProcessDao {
	
	void save(SocialSecurityProcessEntity socialSecurityProcessEntity);

	List<SocialSecurityProcessEntity> findSocialSecurityProcessListByPage(int page, int limit);
	
	int countSocialSecurityProcess();
	
	SocialSecurityProcessEntity getSocialSecurityProcessByProcessInstanceID(String processInstanceID);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	void updatePaymentCountByProcessInstanceID(String pInstanceID, double personalCount, double companyCount);
	
	void updateHFCountByProcessInstanceID(String pInstanceID, double totalCount);
	
	SocialSecurityProcessEntity getLastProcessByHFTime(int year, int month, int companyID);
	
	SocialSecurityProcessEntity getLastProcessBySSTime(int year, int month, int companyID);
}
