package com.zhizaolian.staff.service;

import com.zhizaolian.staff.entity.PurchasePropertyEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PurchasePropertyVo;

public interface PurchasePropertyService {

	void startPurchaseProperty(PurchasePropertyVo purchasePropertyVo);

	void updateProcessStatus(TaskResultEnum result, String processInstanceID);

	PurchasePropertyEntity getPurchasePropertyUserIdByInstanceId(String id);

	void updatePurchaseProperty(PurchasePropertyVo purchasePropertyVo);

	void updatePurchasePropertyForBudget(PurchasePropertyVo purchasePropertyVo);

	ListResult<PurchasePropertyVo> findPurchasePropertyListByUserID(String id, Integer page, Integer limit);

	PurchasePropertyVo getPurchasePropertyVoByProcessInstanceId(String processInstanceID);
	
}
