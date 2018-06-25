package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.EmailEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.EmailVO;

public class EmailVOTransformer extends SafeFunction<EmailEntity, EmailVO> {

	public static final EmailVOTransformer INSTANCE = new EmailVOTransformer();
	
	@Override
	protected EmailVO safeApply(EmailEntity input) {
		EmailVO output = new EmailVO();
		output.setUserID(input.getUserID());
		output.setRequestUserID(input.getRequestUserID());
		output.setAddress(input.getAddress());
		output.setReason(input.getReason());
		output.setProcessInstanceID(input.getProcessInstanceID());
		output.setConfirmAddress(input.getConfirmAddress());
		output.setOriginalPassword(input.getOriginalPassword());
		output.setLoginUrl(input.getLoginUrl());
		return output;
	}
	
	private EmailVOTransformer() {
		
	}
}
