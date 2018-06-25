package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.PositionEntity;
import com.zhizaolian.staff.enums.PositionEnum;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.PositionVO;

/**
 * PositionEntity -> PositionVO
 * @author zpp
 *
 */
public class PositionVOTransformer extends SafeFunction<PositionEntity, PositionVO> {
	
	public static final PositionVOTransformer INSTANCE = new PositionVOTransformer();
	
	@Override
	protected PositionVO safeApply(PositionEntity input) {
		PositionVO output = new PositionVO();
		output.setPositionID(input.getPositionID());
		output.setPositionName(input.getPositionName());
		PositionEnum position = PositionEnum.valueOf(null==input.getPositionType() ? 0:input.getPositionType());
		if(null != position){
			output.setPositionType(position.getName());
		}
		return output;
	}
	
	private PositionVOTransformer() {
		
	}
}
