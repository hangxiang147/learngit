package com.zhizaolian.staff.dao;



import java.util.List;

import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface AssetUsageDao {
	void saveAssetUsage(AssetUsageEntity assetUsageEntity);
	
	public AssetUsageEntity getAssetUsageByID(Integer usageID);
	
    public ListResult<Object> findAssetUsageList(String hql,String hqlCount,int limit,int page);
	
	public List<AssetUsageEntity> getAssetUsageByAssetID(Integer assetID);
	
	public AssetUsageEntity getAssetUsageByID1(Integer assetID);


}
