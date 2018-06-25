package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.SocialSecurityProcessEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;

public class SocialSecurityProcessVOTransformer extends SafeFunction<SocialSecurityProcessEntity, SocialSecurityProcessVO> {
	
	public static final SocialSecurityProcessVOTransformer INSTANCE = new SocialSecurityProcessVOTransformer();
	
	@Override
	protected SocialSecurityProcessVO safeApply(SocialSecurityProcessEntity input) {
		SocialSecurityProcessVO output = new SocialSecurityProcessVO();
		output.setSspID(input.getSspID());
		output.setUserID(input.getUserID());
		output.setYear(input.getPaymentYear());
		output.setMonth(input.getPaymentMonth());
		output.setSsYear(input.getHfPaymentYear());
		output.setSsMonth(input.getHfPaymentMonth());
		output.setCompanyID(input.getCompanyID());
		output.setPersonalCount(input.getPersonalCount());
		output.setCompanyCount(input.getCompanyCount());
		output.setTotalCount(input.getTotalCount());
		output.setSsTotalCount(input.getHfTotalCount());
		return output;
	}
	
	private SocialSecurityProcessVOTransformer() {
		
	}

}
