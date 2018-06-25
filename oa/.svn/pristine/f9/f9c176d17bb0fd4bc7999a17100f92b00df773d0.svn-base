package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.ContractEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.ContractVO;

public class ContractVOTransformer extends SafeFunction<ContractEntity, ContractVO> {
	
	public static final ContractVOTransformer INSTANCE = new ContractVOTransformer();
	
	@Override
	protected ContractVO safeApply(ContractEntity input) {
		ContractVO output = new ContractVO();
		output.setContractID(input.getContractID());
		output.setPartyA(input.getPartyA());
		output.setPartyB(input.getPartyB());
		output.setBeginDate(input.getBeginDate()==null?"":DateUtil.formateDate(input.getBeginDate()));
		output.setEndDate(input.getEndDate()==null?"":DateUtil.formateDate(input.getEndDate()));
		output.setContractBackups(input.getContractBackups());
		output.setSignature(input.getSignature());
		output.setStatus(input.getStatus());
		return output;
	}
	
	private ContractVOTransformer() {
		
	}
}
