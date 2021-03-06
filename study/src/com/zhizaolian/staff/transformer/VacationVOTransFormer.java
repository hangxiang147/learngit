package com.zhizaolian.staff.transformer;




import java.text.SimpleDateFormat;

import com.zhizaolian.staff.entity.VacationEntity;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.VacationVO;

public class VacationVOTransFormer extends SafeFunction<VacationEntity, VacationVO> {
	public static final VacationVOTransFormer INSTANCE = new VacationVOTransFormer();


	@Override
	protected VacationVO safeApply(VacationEntity input) {
		
		VacationVO output=new VacationVO();
		output.setVacationID(input.getVacationID());
		output.setUserID(input.getUserID());
		//output.setUserName(staffService.getStaffByUserID(input.getUserID()).getLastName());
		output.setRequestUserID(input.getRequestUserID());
		//output.setRequestUserName(staffService.getStaffByUserID(input.getRequestUserID()).getLastName());
		output.setShowHours(input.getHours());
		output.setBeginDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getBeginDate()).toString());
		output.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(input.getEndDate()).toString());
		output.setAgentID(input.getAgentID());
		//output.setAgentName(staffService.getStaffByUserID(input.getAgentID()).getLastName());
		output.setVacationType(input.getVacationType());
		output.setReason(input.getReason());
		output.setAttachmentImage(input.getAttachmentImage());
		output.setType(input.getType());
		output.setCompanyID(input.getCompanyID());
		output.setDepartmentID(input.getDepartmentID());
		return output;
	}
	private VacationVOTransFormer(){
		
	}
	

}
