package com.zhizaolian.staff.service;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetVO;
import com.zhizaolian.staff.vo.HandlePropertyVo;

public interface HandlePropertyService {

	ListResult<AssetVO> getAssetList(String assetNum, String assetName, String assetStatus, Integer limit, Integer page);

	void startHandleProperty(HandlePropertyVo handlePropertyVo, String recipientId);

	void updateHandelPropertyProcessStatus(TaskResultEnum result, String processInstanceID);

	ListResult<HandlePropertyVo> findHandlePropertyListByUserID(String id, Integer page, Integer limit);

	HandlePropertyVo getHandlePropertyVoByProcessInstanceId(String processInstanceID);

}
