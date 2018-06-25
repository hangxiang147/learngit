package com.zhizaolian.staff.service;

import java.io.File;
import java.util.Map;

import com.zhizaolian.staff.entity.CommonSubjectEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommonSubjectVo;

public interface CommonSubjectService {
	
	void startCommonSubject(CommonSubjectVo commonSubjectVo,File[] files,String fileDetail,String requestUserId) throws Exception;
	
	
	ListResult<CommonSubjectVo> getCommonSubjectByKey(CommonSubjectVo commonSubjectVo,Map<String, String> extraMap,int page,int limit);

	CommonSubjectEntity getEntityId(String id);
	
	void updateResult(String result,String processInstanceID);
}
