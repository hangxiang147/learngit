package com.zhizaolian.staff.service;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.TransferPropertyVo;

public interface TransferPropertyService {

	void startTransferProperty(TransferPropertyVo transferPropertyVo, String recipientId);

	void updateTransferPropertyProcessStatus(TaskResultEnum result, String processInstanceID);

	ListResult<TransferPropertyVo> findTransferPropertyListByUserID(String id, Integer page, Integer limit);

	TransferPropertyVo getTransferPropertyVoByProcessInstanceId(String processInstanceID);

}
