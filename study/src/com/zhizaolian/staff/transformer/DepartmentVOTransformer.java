package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;

/**
 * DepartmentEntity -> DepartmentVO
 * @author zpp
 *
 */
public class DepartmentVOTransformer extends SafeFunction<DepartmentEntity, DepartmentVO> {

	public static final DepartmentVOTransformer INSTANCE = new DepartmentVOTransformer();
	
	@Override
	protected DepartmentVO safeApply(DepartmentEntity input) {
		DepartmentVO output = new DepartmentVO();
		output.setDepartmentID(input.getDepartmentID());
		output.setDepartmentName(input.getDepartmentName());
		output.setParentID(input.getParentID());
		output.setLevel(input.getLevel());
		return output;
	}
	
	private DepartmentVOTransformer() {
		
	}
}
