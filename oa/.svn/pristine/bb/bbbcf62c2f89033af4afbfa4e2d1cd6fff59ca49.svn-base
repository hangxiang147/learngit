package com.zhizaolian.staff.service;

import com.zhizaolian.staff.entity.VersionFuncionInfo;
import com.zhizaolian.staff.utils.ListResult;

public interface VersionInfoService {

	void saveVersionInfo(VersionFuncionInfo versionFuncionInfo, String id) throws Exception;

	ListResult<VersionFuncionInfo> findVersionInfoList(Integer limit, Integer page, String beginDate, String endDate) throws Exception;

	VersionFuncionInfo getVersionFuncionInfo(String id) throws Exception;

	void deleteVersion(String id);

	boolean checkVersionNoticeShow(String id);

	VersionFuncionInfo getLatestVersionFunctionInfo() throws Exception;

	void addVersionNoticeActor(String id);

}
