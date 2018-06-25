package com.zhizaolian.staff.service;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChangeBankAccountVo;

public interface BankAccountService {

	void startBankAccount(ChangeBankAccountVo changeBankAccountVo);

	String getUserIdByInstanceId(String id);

	void updateBankAccountProcessStatus(TaskResultEnum result, String processInstanceID);

	ListResult<ChangeBankAccountVo> findChangeBankAccountListByUserID(String id, Integer page, Integer limit);

	ChangeBankAccountVo getChangeBankAccountVoByProcessInstanceId(String processInstanceID);
	
}
