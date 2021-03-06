package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.ReimbursementEntity;
import com.zhizaolian.staff.enums.InvoiceTitleEnum;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.ReimbursementVO;

public class ReimbursementVOTransformer extends SafeFunction<ReimbursementEntity, ReimbursementVO> {

	public static final ReimbursementVOTransformer INSTANCE = new ReimbursementVOTransformer();
	
	@Override
	protected ReimbursementVO safeApply(ReimbursementEntity input) {
		ReimbursementVO output = new ReimbursementVO();
		output.setReimbursementID(input.getReimbursementID());
		output.setReimbursementNo(input.getReimbursementNo());
		output.setReternenceId(input.getReternenceId());
		output.setReternenceName(input.getReternenceName());
		output.setReternenceMobile(input.getReternenceMobile());
		output.setFixedAssetNo(input.getFixedAssetNo());
		output.setIsFixedAsset(input.getIsFixedAsset());
		output.setRequestUserID(input.getRequestUserID());
		output.setPayeeID(input.getPayeeID());
		output.setInvoiceTitle(InvoiceTitleEnum.valueOf(input.getInvoiceTitle())==null?"":InvoiceTitleEnum.valueOf(input.getInvoiceTitle()).getName());
		output.setInvoiceNum(input.getInvoiceNum());
		output.setDetailNum(input.getDetailNum());
		output.setTotalAmount(input.getTotalAmount());
		output.setRequestDate(DateUtil.formateFullDate(input.getAddTime()));
		output.setShowPerson1(input.getShowPerson1());
		output.setShowPerson2(input.getShowPerson2());
		output.setShowPerson3(input.getShowPerson3());
		output.setMoneyType(input.getMoneyType());
		return output;
	}
	
	private ReimbursementVOTransformer() {
		
	}
}


