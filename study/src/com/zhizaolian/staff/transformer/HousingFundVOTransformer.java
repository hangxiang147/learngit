package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.HousingFundEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.HousingFundVO;

public class HousingFundVOTransformer extends SafeFunction<HousingFundEntity, HousingFundVO> {
	
	public static final HousingFundVOTransformer INSTANCE = new HousingFundVOTransformer();
	
	@Override
	protected HousingFundVO safeApply(HousingFundEntity input) {
		HousingFundVO output = new HousingFundVO();
		output.setHfID(input.getHfID());
		output.setProcessID(input.getProcessID());
		output.setUserID(input.getUserID());
		output.setPaymentYear(input.getPaymentYear());
		output.setPaymentMonth(input.getPaymentMonth());
		output.setCompanyID(input.getCompanyID());
		output.setEntryDate(input.getEntryDate()==null?"":DateUtil.formateDate(input.getEntryDate()));
		output.setFormalDate(input.getFormalDate()==null?"":DateUtil.formateDate(input.getFormalDate()));
		output.setGender(input.getGender());
		output.setIdType(input.getIdType());
		output.setIdNumber(input.getIdNumber());
		output.setHasPaid(input.getHasPaid());
		output.setCompanyCount(input.getCompanyCount());
		output.setPersonalCount(input.getPersonalCount());
		output.setTotalCount(input.getTotalCount());
		output.setNote(input.getNote());
		return output;
	}
	
	private HousingFundVOTransformer() {
		
	}
}
