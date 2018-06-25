package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.enums.AppTypeEnum;

public interface AppService {

	/**
	 * 获取指定类型app最新版本的版本号
	 * @param type 
	 * @return
	 */
	String getLastVersionByType(AppTypeEnum type);

	String saveAppInfo(String appName);

	boolean checkAppNameExist(String appName, String id);

	List<AppInfoEntity> findAllAppInfos();

	boolean checkAppExistByAppId(String appId);
}
