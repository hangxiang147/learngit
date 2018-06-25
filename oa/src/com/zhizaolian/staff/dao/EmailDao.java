package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.EmailEntity;

public interface EmailDao {

	void save(EmailEntity emailEntity);
	
	List<EmailEntity> findEmailsByUserID(String userID, int page, int limit);
	
	int countEmailsByUserID(String userID);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	void updateEmailAccountByProcessInstanceID(String processInstanceID, String address, String password, String loginUrl);

	EmailEntity getEmailByProcessInstanceID(String processInstanceID);

}
