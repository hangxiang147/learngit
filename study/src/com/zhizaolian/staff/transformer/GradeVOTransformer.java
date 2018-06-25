package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.GradeEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.GradeVO;

/**
 * GradeEntity -> GradeVO
 * @author zpp
 *
 */
public class GradeVOTransformer extends SafeFunction<GradeEntity, GradeVO> {

	public static final GradeVOTransformer INSTANCE = new GradeVOTransformer();
	
	@Override
	protected GradeVO safeApply(GradeEntity input) {
		GradeVO output = new GradeVO();
		output.setGradeID(input.getGradeID());
		output.setGradeName(input.getGradeName());
		return output;
	}
	
	private GradeVOTransformer() {
		
	}

}
