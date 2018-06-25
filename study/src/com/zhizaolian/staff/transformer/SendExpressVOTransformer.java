package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.SendExpressEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.SendExpressVO;

public class SendExpressVOTransformer extends SafeFunction<SendExpressEntity, SendExpressVO>{
	public static final SendExpressVOTransformer INSTANCE = new SendExpressVOTransformer();

	@Override
	protected SendExpressVO safeApply(SendExpressEntity input) {
		SendExpressVO output = new SendExpressVO();
		output.setUserID(input.getUserID());
		output.setPostDate(DateUtil.formateDate(input.getPostDate()));
		output.setWeekDay(input.getWeekDay());
		output.setCompanyID(input.getCompanyID());
		output.setDepartmentID(input.getDepartmentID());
		output.setExpressCompany(input.getExpressCompany());
		output.setExpressNumber(input.getExpressNumber());
		output.setType(input.getType());
		output.setReason(input.getReason());
		return output;
	}
	
	private SendExpressVOTransformer(){
		
	}

}
