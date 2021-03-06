package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.StaffVO;

/**
 * StaffEntity -> StaffVO
 * @author zpp
 *
 */
public class StaffVOTransformer extends SafeFunction<StaffEntity, StaffVO> {

	public static final StaffVOTransformer INSTANCE = new StaffVOTransformer();
	
	@Override
	protected StaffVO safeApply(StaffEntity input) {
		StaffVO output = new StaffVO();
		output.setUserID(input.getUserID());
		output.setLastName(input.getStaffName());
		output.setPositionCategory(input.getPositionCategory());
		output.setGender(input.getGender());
		output.setBirthday(input.getBirthday()==null? null : DateUtil.formateDate(input.getBirthday()));
		output.setTelephone(input.getTelephone());
		output.setIdNumber(input.getIdNumber());
		output.setEducation(input.getEducation());
		output.setMajor(input.getMajor());
		output.setSchool(input.getSchool());
		output.setGraduationDate(input.getGraduationDate()==null ? null : DateUtil.formateDate(input.getGraduationDate()));
		output.setDegreeID(input.getDegreeID());//获取学位证书编号
		output.setEducationID(input.getEducationID());//获取学证书编号
		output.setNativePlace(input.getNativePlace());
		output.setMaritalStatus(input.getMaritalStatus());
		output.setCriminalRecord(input.getCriminalRecord());//获取犯罪记录证明
		output.setAddress(input.getAddress());
		output.setEmergencyContract(input.getEmergencyContract());
		output.setEmergencyPhone(input.getEmergencyPhone());
		output.setEntryDate(input.getEntryDate()==null ? null : DateUtil.formateDate(input.getEntryDate()));
		output.setFormalDate(input.getFormalDate()==null ? null : DateUtil.formateDate(input.getFormalDate()));
		output.setGradeID(input.getGradeID());
		output.setSalary(input.getSalary());
		output.setStatus(input.getStatus());
		output.setAuditStatus(input.getAuditStatus()); //获取审核状态
		output.setStaffID(input.getStaffID()); //获取staffID的值
		output.setAttachementNames(input.getAttachementNames());
		output.setCompanyPhone(input.getCompanyPhone());
		output.setEmail(input.getEmail());
		output.setStarSign(input.getStarSign());
		output.setRegistrationFormId(input.getRegistrationFormId());
		output.setInsurance(input.getInsurance());
		output.setOpenId(input.getOpenId());
		output.setHeadImgId(input.getHeadImgId());
		output.setPerformance(input.getPerformance());
		output.setStandardSalary(input.getStandardSalary());
		output.setBank(input.getBank());
		output.setBankAccount(input.getBankAccount());
		output.setManagePersonNum(input.getManagePersonNum());
		output.setWeixinCodeId(input.getWeixinCodeId());
		output.setCheckItems(input.getCheckItems());
		return output;
	}
	
	private StaffVOTransformer() {
		
	}
}
