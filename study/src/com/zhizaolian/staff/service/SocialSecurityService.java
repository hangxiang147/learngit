package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;

public interface SocialSecurityService {
	
	int save(SocialSecurityVO socialSecurityVO);
	
	int save(HousingFundVO housingFundVO);
	
	List<SocialSecurityVO> findSocialSecurityListByTime(int year, int month, int companyID);
	
	SocialSecurityVO getSocialSecurityByID(int ssID);
	
	HousingFundVO getHousingFundByID(int hfID);
	
	void deleteSocialSecurityByID(int ssID);
	
	void deleteHousingFundByID(int hfID);
	
	void startSocialSecurity(SocialSecurityProcessVO socialSecurityProcessVO);
	
	ListResult<SocialSecurityProcessVO> findSocialSecurityProcessListByPage(int page, int limit);
	
	SocialSecurityProcessVO getSocialSecurityProcessByProcessInstanceID(String processInstanceID);
	
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	void updateProcessCount(String processInstanceID, double personalCount, double companyCount);
	
	void updateProcessHFCount(String processInstanceID, double totalCount);
	
	SocialSecurityProcessVO getLastProcessVOByHFTime(int year, int month, int companyID);
	
	SocialSecurityProcessVO getLastProcessVOBySSTime(int year, int month, int companyID);
	
	List<SocialSecurityVO> findSocialSecurityListByProcessID(int processID);
	
	List<HousingFundVO> findHousingFundListByProcessID(int processID);
	
	List<HousingFundVO> findHousingFundListByTime(int year, int month, int companyID);
}
