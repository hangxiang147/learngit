package com.zhizaolian.staff.dao;

import com.zhizaolian.staff.entity.AppVersionEntity;

public interface AppVersionDao {

	void save(AppVersionEntity appVersionEntity);
	
	AppVersionEntity getLastVersionByType(int type);
}
