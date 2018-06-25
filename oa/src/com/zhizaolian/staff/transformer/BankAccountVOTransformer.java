package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.BankAccountVO;

/**
 * BankAccountEntity -> BankAccountVO
 * @author zpp
 *
 */
public class BankAccountVOTransformer extends SafeFunction<BankAccountEntity, BankAccountVO> {

	public static final BankAccountVOTransformer INSTANCE = new BankAccountVOTransformer();
	
	@Override
	protected BankAccountVO safeApply(BankAccountEntity input) {
		BankAccountVO output = new BankAccountVO();
		output.setUserID(input.getUserID());
		output.setCardName(input.getCardName());
		output.setBank(input.getBank());
		output.setCardNumber(input.getCardNumber());
		return output;
	}
	
	private BankAccountVOTransformer() {
		
	}
}
