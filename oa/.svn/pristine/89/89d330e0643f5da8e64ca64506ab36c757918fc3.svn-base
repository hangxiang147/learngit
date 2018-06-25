package com.zhizaolian.staff.service;

import java.util.List;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.BrandAuthEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.BrandAuthVo;

public interface BrandAuthService {

	void startBrandAuth(BrandAuthVo brandAuthVo);

	List<BrandAuthVo> getBrandAuthTasksByInstanceId(List<Task> brandAuthTasks);

	BrandAuthEntity getBrandAuthByInstanceId(String processInstanceId);

	BrandAuthVo getBrandAuthVoByInstanceId(String processInstanceID);

	void updateProcessStatus(String processInstanceID, String result);

	void updateChopProcessInstanceId(String processInstanceId, String chopProcessInstanceId);

	ListResult<BrandAuthVo> findBrandAuthListByUserID(String id, Integer page, Integer limit);

	ListResult<BrandAuthVo> findAllBrandAuthList(String companyName, Integer page, Integer limit);

	BrandAuthEntity getBrandAuthByChopInstanceId(String id);

}
