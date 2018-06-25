package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.GroupDetailEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.GroupDetailVO;
 
public class GroupDetailVOTransformer extends SafeFunction<GroupDetailEntity, GroupDetailVO> {

	public static final GroupDetailVOTransformer INSTANCE = new GroupDetailVOTransformer();
	
	@Override
	protected GroupDetailVO safeApply(GroupDetailEntity input) {
		GroupDetailVO output = new GroupDetailVO();
		output.setGroupDetailID(input.getGroupDetailID());
		output.setGroupID(input.getGroupID());
		output.setCompanyID(input.getCompanyID());
		output.setDepartmentID(input.getDepartmentID());
		output.setPositionID(input.getPositionID());
		output.setResponsibility(input.getResponsibility());
		return output;
	}
	
	private GroupDetailVOTransformer() {
		
	}
}
