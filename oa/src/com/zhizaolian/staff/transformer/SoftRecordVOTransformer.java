package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.SoftRecordEntity;
import com.zhizaolian.staff.vo.SoftRecordVO;

/**
 * 这是一个上传下载的记录的实体类和VO类之间的转换类
 * @author wjp
 *
 */

public class SoftRecordVOTransformer {
	 
	/*
	 * 实体类向VO类转换
	 */
	public static SoftRecordVO entityToVO (SoftRecordEntity softRecordEntity) {
		
		SoftRecordVO softRecordVO = new SoftRecordVO();
		if(softRecordEntity.getSoftID()!=null) {
			softRecordVO.setSoftID(softRecordEntity.getSoftID());
		}
		if(softRecordEntity.getSoftRecordID()!=null) {
			softRecordVO.setSoftRecordID(softRecordEntity.getSoftRecordID());
		}
		if(softRecordEntity.getType()!=null) {
			softRecordVO.setType(softRecordEntity.getType());
		}
		if(softRecordEntity.getTime()!=null) {
			softRecordVO.setTime(softRecordEntity.getTime());
		}
		softRecordVO.setUserID(softRecordEntity.getUserID());
		return softRecordVO;
	}
	
	/*
	 * VO类向实体类转换
	 */
	public static SoftRecordEntity VOToEntity (SoftRecordVO softRecordVO) {
		
		SoftRecordEntity softRecordEntity = new SoftRecordEntity();
		if(softRecordVO.getSoftID()!=null) {
			softRecordEntity.setSoftID(softRecordVO.getSoftID());
		}
		if(softRecordVO.getSoftRecordID()!=null) {
			softRecordEntity.setSoftRecordID(softRecordVO.getSoftRecordID());
		}
		if(softRecordVO.getType()!=null) {
			softRecordEntity.setType(softRecordVO.getType());
		}
		if(softRecordVO.getTime()!=null) {
			softRecordEntity.setTime(softRecordVO.getTime());
		}
		if(softRecordVO.getUserID()!=null) {
			softRecordEntity.setUserID(softRecordVO.getUserID());
		}
		return softRecordEntity;
	}
}
