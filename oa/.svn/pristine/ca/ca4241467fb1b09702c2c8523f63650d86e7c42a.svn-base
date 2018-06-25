package com.zhizaolian.staff.transformer;



import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.AssetVO;

public class AssetVOTransFormer {
	/* 
	 * 实体类向vo
	 */
	public static AssetVO entityToVO(AssetEntity assetEntity){
		AssetVO assetVO=new AssetVO();
		if(assetEntity.getAssetID()!=null){
			assetVO.setAssetID(assetEntity.getAssetID());
		}
		assetVO.setAssetName(assetEntity.getAssetName());
		assetVO.setSerialNumber(assetEntity.getSerialNumber());
		if(assetEntity.getType()!=null){
			assetVO.setType(assetEntity.getType());
		}
		assetVO.setModel(assetEntity.getModel());
		if(assetEntity.getAmount()!=null){
			assetVO.setAmount(assetEntity.getAmount());
		}
		if(assetEntity.getCompanyID()!=null){
			assetVO.setCompanyID(assetEntity.getCompanyID());
		}
		if(assetEntity.getPurchaseTime()!=null){
			assetVO.setPurchaseTime(DateUtil.formateDate(assetEntity.getPurchaseTime()));
		}
		assetVO.setStorageLocation(assetEntity.getStorageLocation());
		if(assetEntity.getStatus()!=null){
			assetVO.setStatus(assetEntity.getStatus());
		}
		if(assetEntity.getStatus()!=null){
			assetVO.setDeviceID(assetEntity.getDeviceID());
		}
		return assetVO;
		
	}
	
	/*
	 * vo向实体类
	 */
	
	public static AssetEntity VOToEntity(AssetVO assetVO){
		AssetEntity assetEntity=new AssetEntity();
		
		if(assetVO.getAssetID()!=null){
			assetEntity.setAssetID(assetVO.getAssetID());
		}
		assetEntity.setAssetName(assetVO.getAssetName());
		assetEntity.setSerialNumber(assetVO.getSerialNumber());
		if(assetVO.getType()!=null){
			assetEntity.setType(assetVO.getType());
		}
		assetEntity.setModel(assetVO.getModel());
		if(assetVO.getAmount()!=null){
			assetEntity.setAmount(assetVO.getAmount());
		}
		if(assetVO.getCompanyID()!=null){
			assetEntity.setCompanyID(assetVO.getCompanyID());
		}
		if(assetVO.getPurchaseTime()!=null){
			assetEntity.setPurchaseTime(DateUtil.getSimpleDate(assetVO.getPurchaseTime()));
		}
		assetEntity.setStorageLocation(assetVO.getStorageLocation());
		if(assetVO.getStatus()!=null){
			assetEntity.setStatus(assetVO.getStatus());
		}
		
		
		return assetEntity;
		
	}

}
