package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AppVersionDao;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.entity.AppVersionEntity;
import com.zhizaolian.staff.enums.AppTypeEnum;
import com.zhizaolian.staff.service.AppService;
import com.zhizaolian.staff.utils.EscapeUtil;

public class AppServiceImpl implements AppService {
	
	@Autowired
	private AppVersionDao appVersionDao;
	@Autowired
	private BaseDao baseDao;
	@Override
	public String getLastVersionByType(AppTypeEnum type) {
		if (type == null) {
			throw new RuntimeException("APP类型不合法！");
		}
		
		AppVersionEntity versionEntity = appVersionDao.getLastVersionByType(type.getValue());
		if (versionEntity == null) {
			throw new RuntimeException("版本号不存在！");
		}
		return versionEntity.getVersion();
	}

	@Override
	public String saveAppInfo(String appName) {
		AppInfoEntity appInfo = new AppInfoEntity();
		appInfo.setAddTime(new Date());
		appInfo.setAppName(appName);
		String uuid = UUID.randomUUID().toString();
		appInfo.setAppId(uuid);
		baseDao.hqlSave(appInfo);
		return uuid;
	}

	@Override
	public boolean checkAppNameExist(String appName, String id) {
		String sql = "select count(id) from ZZL_AppInfo where isDeleted=0 and appName='"+
							  EscapeUtil.decodeSpecialChars(appName)+"'\n";
		if(StringUtils.isNotBlank(id)){
			sql += "and id!="+id;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoEntity> findAllAppInfos() {
		String hql = "from AppInfoEntity where isDeleted=0";
		return (List<AppInfoEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public boolean checkAppExistByAppId(String appId) {
		String hql = "from AppInfoEntity where isDeleted=0 and appId='"+appId+"'";
		AppInfoEntity appInfo = (AppInfoEntity) baseDao.hqlfindUniqueResult(hql);
		if(null != appInfo){
			return true;
		}
		return false;
	}
}
