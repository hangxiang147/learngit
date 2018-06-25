package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.SocialSecurityEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.SocialSecurityVO;

public class SocialSecurityVOTransformer extends SafeFunction<SocialSecurityEntity, SocialSecurityVO> {
	
	public static final SocialSecurityVOTransformer INSTANCE = new SocialSecurityVOTransformer();
	
	@Override
	protected SocialSecurityVO safeApply(SocialSecurityEntity input) {
		SocialSecurityVO output = new SocialSecurityVO();
		output.setSsID(input.getSsID());
		output.setProcessID(input.getProcessID());
		output.setPaymentYear(input.getPaymentYear());
		output.setPaymentMonth(input.getPaymentMonth());
		output.setUserID(input.getUserID());
		output.setCompanyID(input.getCompanyID());
		output.setIdType(input.getIdType());
		output.setIdNumber(input.getIdNumber());
		output.setBasePay(input.getBasePay());
		output.setSelfPaidRatio(input.getSelfPaidRatio());
		output.setReason(input.getReason());
		output.setPersonalProvidentFund(input.getPersonalProvidentFund());
		output.setCompanyProvidentFund(input.getCompanyProvidentFund());
		output.setTotalProvidentFund(input.getTotalProvidentFund());
		return output;
	}
	
	private SocialSecurityVOTransformer() {
		
	}

}
