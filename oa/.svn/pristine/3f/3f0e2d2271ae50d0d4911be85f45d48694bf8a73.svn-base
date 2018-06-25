package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.FormalEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.FormalVO;

public class FormalVOTransformer extends SafeFunction<FormalEntity, FormalVO>{

	public static final FormalVOTransformer INSTANCE =  new FormalVOTransformer();
	
	@Override
	protected FormalVO safeApply(FormalEntity input) {
		FormalVO output = new FormalVO();
		output.setRequestUserID(input.getRequestUserID());
		return output;
	}
	
	private FormalVOTransformer() {
		
	}
}
