package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.AssetUsageVO;

public class AssetUsageVOTransFormer {
	/* 
	 * 实体类向vo
	 */
	public static AssetUsageVO entityToVO(AssetUsageEntity assetUsageEntity){
		AssetUsageVO assetUsageVO=new AssetUsageVO();
		if(assetUsageEntity.getUsageID()!=null){
			assetUsageVO.setUsageID(assetUsageEntity.getUsageID());
		}
		if(assetUsageEntity.getAssetID()!=null){
			assetUsageVO.setAssetID(assetUsageEntity.getAssetID());
		}
		assetUsageVO.setRecipientID(assetUsageEntity.getRecipientID());
		if(assetUsageEntity.getCompanyID()!=null){
			assetUsageVO.setCompanyID(assetUsageEntity.getCompanyID());
		}
		if(assetUsageEntity.getDepartmentID()!=null){
			assetUsageVO.setDepartmentID(assetUsageEntity.getDepartmentID());
		}
		assetUsageVO.setReceiveLocation(assetUsageEntity.getReceiveLocation());
		if(assetUsageEntity.getReceiveTime()!=null){
			assetUsageVO.setReceiveTime(DateUtil.formateDate(assetUsageEntity.getReceiveTime()));
		}
		assetUsageVO.setReceiveOperatorID(assetUsageEntity.getReceiveOperatorID());
		assetUsageVO.setReceiveNote(assetUsageEntity.getReceiveNote());
		assetUsageVO.setReturnLocation(assetUsageEntity.getReturnLocation());
		if(assetUsageEntity.getReturnTime()!=null){
			assetUsageVO.setReturnTime(DateUtil.formateDate(assetUsageEntity.getReturnTime()));
		}
		assetUsageVO.setReturnOperatorID(assetUsageEntity.getReturnOperatorID());
		assetUsageVO.setReturnNote(assetUsageEntity.getReturnNote());
		if(assetUsageEntity.getStatus()!=null){
			assetUsageVO.setStatus(assetUsageEntity.getStatus());
		}
		return assetUsageVO;
		
	}
	/*
	 * vo向实体类
	 */
	
	public static AssetUsageEntity VOToentity(AssetUsageVO assetUsageVO){
		AssetUsageEntity assetUsageEntity=new AssetUsageEntity();
		if(assetUsageVO.getUsageID()!=null){
			assetUsageEntity.setUsageID(assetUsageVO.getUsageID());
		}
		if(assetUsageVO.getAssetID()!=null){
			assetUsageEntity.setAssetID(assetUsageVO.getAssetID());
		}
		assetUsageEntity.setRecipientID(assetUsageVO.getRecipientID());
		if(assetUsageVO.getCompanyID()!=null){
			assetUsageEntity.setCompanyID(assetUsageVO.getCompanyID());
		}
		if(assetUsageVO.getDepartmentID()!=null){
			assetUsageEntity.setDepartmentID(assetUsageVO.getDepartmentID());
		}
		assetUsageEntity.setReceiveLocation(assetUsageVO.getReceiveLocation());
		if(assetUsageVO.getReceiveTime()!=null){
			assetUsageEntity.setReceiveTime(DateUtil.getSimpleDate(assetUsageVO.getReceiveTime()));
		}
		assetUsageEntity.setReceiveOperatorID(assetUsageVO.getReceiveOperatorID());
		assetUsageEntity.setReceiveNote(assetUsageVO.getReceiveNote());
		assetUsageEntity.setReturnLocation(assetUsageVO.getReturnLocation());
		if(assetUsageVO.getReturnTime()!=null){
			assetUsageEntity.setReturnTime(DateUtil.getSimpleDate(assetUsageVO.getReturnTime()));
		}
		assetUsageEntity.setReturnOperatorID(assetUsageVO.getReturnOperatorID());
		assetUsageEntity.setReturnNote(assetUsageVO.getReturnNote());
		if(assetUsageVO.getStatus()!=null){
			assetUsageEntity.setStatus(assetUsageVO.getStatus());
		}
		return assetUsageEntity;
	}

}
