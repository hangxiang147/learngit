package com.zhizaolian.staff.transformer;

import java.text.SimpleDateFormat;

import com.zhizaolian.staff.entity.StaffInfoAlterationEntity;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;

/**
 * StaffInfoAlterationVO和StaffInfoAlterationEntity类之间的互相转化
 * @author wjp
 *
 */
public class StaffInfoAlterationVOTransformer {
	
	 
	public static StaffInfoAlterationVO entityToVO(StaffInfoAlterationEntity staffInfoAlterationEntity) {
		
		StaffInfoAlterationVO staffInfoAlterationVO = new StaffInfoAlterationVO();
		staffInfoAlterationVO.setStaffInfoAlterationID(staffInfoAlterationEntity.getStaffInfoAlterationID()==null?null:staffInfoAlterationEntity.getStaffInfoAlterationID());
		staffInfoAlterationVO.setOperatorID(staffInfoAlterationEntity.getOperatorID());
		staffInfoAlterationVO.setUserID(staffInfoAlterationEntity.getUserID());
		staffInfoAlterationVO.setGradeIDBefore(staffInfoAlterationEntity.getGradeBefore()==null?null:staffInfoAlterationEntity.getGradeBefore());
		staffInfoAlterationVO.setGradeIDAfter(staffInfoAlterationEntity.getGradeAfter()==null?null:staffInfoAlterationEntity.getGradeAfter());
		staffInfoAlterationVO.setSalaryBefore(staffInfoAlterationEntity.getSalaryBefore()==null?null:staffInfoAlterationEntity.getSalaryBefore().toString());
		staffInfoAlterationVO.setSalaryAfter(staffInfoAlterationEntity.getSalaryAfter()==null?null:staffInfoAlterationEntity.getSalaryAfter().toString());
		staffInfoAlterationVO.setType(staffInfoAlterationEntity.getType()==null?null:staffInfoAlterationEntity.getType());
		staffInfoAlterationVO.setOperateTime(staffInfoAlterationEntity.getAddTime()==null?null:new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(staffInfoAlterationEntity.getAddTime())); //得到操作时间
		staffInfoAlterationVO.setEffectDate(staffInfoAlterationEntity.getEffectDate());
		staffInfoAlterationVO.setAttachmentIds(staffInfoAlterationEntity.getAttachmentIds());
		return staffInfoAlterationVO;
	}
	
	public  static StaffInfoAlterationEntity VOToEntity(StaffInfoAlterationVO staffInfoAlterationVO) {
		
		StaffInfoAlterationEntity staffInfoAlterationEntity = new StaffInfoAlterationEntity();
		staffInfoAlterationEntity.setStaffInfoAlterationID(staffInfoAlterationVO.getStaffInfoAlterationID()==null?null:staffInfoAlterationVO.getStaffInfoAlterationID());
		staffInfoAlterationEntity.setOperatorID(staffInfoAlterationVO.getOperatorID());
		staffInfoAlterationEntity.setUserID(staffInfoAlterationVO.getUserID());
		staffInfoAlterationEntity.setGradeBefore(staffInfoAlterationVO.getGradeIDBefore()==null?null:staffInfoAlterationVO.getGradeIDBefore());
		staffInfoAlterationEntity.setGradeAfter(staffInfoAlterationVO.getGradeIDAfter()==null?null:staffInfoAlterationVO.getGradeIDAfter());
		if(staffInfoAlterationVO.getSalaryBefore()!=null)
			staffInfoAlterationEntity.setSalaryBefore(staffInfoAlterationVO.getSalaryBefore());
		if(staffInfoAlterationVO.getSalaryAfter()!=null)
			staffInfoAlterationEntity.setSalaryAfter(staffInfoAlterationVO.getSalaryAfter());
		staffInfoAlterationEntity.setType(staffInfoAlterationVO.getType()==null?null:staffInfoAlterationVO.getType());
		staffInfoAlterationEntity.setEffectDate(staffInfoAlterationVO.getEffectDate());
		if(staffInfoAlterationVO.getAttachmentIds()!=null){
			staffInfoAlterationEntity.setAttachmentIds(staffInfoAlterationVO.getAttachmentIds());
		}
		return staffInfoAlterationEntity;
	}
}
