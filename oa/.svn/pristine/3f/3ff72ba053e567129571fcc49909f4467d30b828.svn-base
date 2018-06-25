package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.PaymentEntity;
import com.zhizaolian.staff.enums.InvoiceTitleEnum;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.PaymentVo;
public class PaymentVOTransformer extends SafeFunction<PaymentEntity, PaymentVo> {
	public static final PaymentVOTransformer INSTANCE = new PaymentVOTransformer();
	@Override
	protected PaymentVo safeApply(PaymentEntity input) {
		PaymentVo output = new PaymentVo();
		output.setReimbursementID(input.getReimbursementID());
		output.setReimbursementNo(input.getReimbursementNo());
		output.setRequestUserID(input.getRequestUserID());
		output.setPayeeID(input.getPayeeID());
		output.setIsHaveInvoice(input.getIsHaveInvoice());
		if(input.getIsHaveInvoice()==null||input.getIsHaveInvoice()==1){
			output.setInvoiceTitle(InvoiceTitleEnum.valueOf(input.getInvoiceTitle())==null?"":InvoiceTitleEnum.valueOf(input.getInvoiceTitle()).getName());
			output.setInvoiceNum(input.getInvoiceNum());
			output.setDetailNum(input.getDetailNum());
		}
		output.setTotalAmount(input.getTotalAmount());
		output.setRequestDate(DateUtil.formateFullDate(input.getAddTime()));
		output.setShowPerson1(input.getShowPerson1());
		output.setShowPerson2(input.getShowPerson2());
		output.setShowPerson3(input.getShowPerson3());
		output.setMoneyType(input.getMoneyType());
		output.setReternenceName(input.getReternenceName());
		output.setReternenceMobile(input.getReternenceMobile());
		return output;
		
	}
}