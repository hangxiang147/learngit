package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.CompanyVO;

/**
 * CompanyEntity -> CompanyVO
 * @author zpp
 *
 */
public class CompanyVOTransformer extends SafeFunction<CompanyEntity, CompanyVO> {

	public static final CompanyVOTransformer INSTANCE = new CompanyVOTransformer();
	
	@Override
	protected CompanyVO safeApply(CompanyEntity input) {
		CompanyVO output = new CompanyVO();
		output.setCompanyID(input.getCompanyID());
		output.setCompanyName(input.getCompanyName());
		output.setCode(input.getCode());
		return output;
	}
	
	private CompanyVOTransformer() {
		
	}
}
