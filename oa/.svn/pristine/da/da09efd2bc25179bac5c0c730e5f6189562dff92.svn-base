package com.zhizaolian.staff.dao;



import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface AssetDao {
	void save(AssetEntity assetEntity);
	
	public ListResult<AssetEntity> findAssetList(String hql ,String hqlcount ,int page ,int limit);
	
	AssetEntity getAssetByID(Integer assetID);
	
	void updateAsset(Integer status,Integer assetID,String storageLocation);
	
	void updateAssetEntity(AssetEntity assetEntity);
	
}
