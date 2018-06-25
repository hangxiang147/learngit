package com.zhizaolian.staff.transformer;

import java.text.SimpleDateFormat;

import com.zhizaolian.staff.entity.PositionAlterationEntity;
import com.zhizaolian.staff.vo.PositionAlterationVO;

/**
 * PositionAlterationVO和PositionAlterationEntity类之间的互相转化
 * @author wjp
 *
 */
public class PositionAlterationVOTransformer {
	
	 
	public static PositionAlterationVO entityToVO(PositionAlterationEntity positionAlterationEntity) {
		
		PositionAlterationVO positionAlterationVO  = new PositionAlterationVO();
		positionAlterationVO.setUserID(positionAlterationEntity.getUserID());
		positionAlterationVO.setGroupID(positionAlterationEntity.getGroupID());
		positionAlterationVO.setAlterationType(positionAlterationEntity.getAlterationType()==null?null:positionAlterationEntity.getAlterationType().toString());
		positionAlterationVO.setOperationUserID(positionAlterationEntity.getOperationUserID());
		positionAlterationVO.setAddTime(positionAlterationEntity.getAddTime()==null?null:new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(positionAlterationEntity.getAddTime()));
		return positionAlterationVO;
	}
	
	public  static PositionAlterationEntity VOToEntity(PositionAlterationVO positionAlterationVO) {
		
		//略
		return null;
	}
}
