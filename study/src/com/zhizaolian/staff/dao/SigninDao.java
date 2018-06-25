package com.zhizaolian.staff.dao;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.SigninEntity;

public interface SigninDao {
	void save(SigninEntity signinEntity);
	List<SigninEntity> findSigninList(String userID,Date signinDate);

}
