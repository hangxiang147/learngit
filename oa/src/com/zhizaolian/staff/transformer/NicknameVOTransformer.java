package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.NicknameEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.NicknameVO;

public class NicknameVOTransformer extends SafeFunction<NicknameEntity, NicknameVO> {

	public static final NicknameVOTransformer INSTANCE = new NicknameVOTransformer();
	
	@Override
	protected NicknameVO safeApply(NicknameEntity input) {
		NicknameVO nicknameVO = new NicknameVO();
		nicknameVO.setNicknameID(input.getNicknameID());
		nicknameVO.setName(input.getName());
		nicknameVO.setType(input.getType());
		nicknameVO.setStatus(input.getStatus());
		return nicknameVO;
	}
	
	private NicknameVOTransformer() {
		
	}
}
