package com.zhizaolian.staff.service;


import java.util.List;

import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetUsageVO;

public interface AssetUsageService {
	public void SaveAssetUsage(AssetUsageVO assetUsageVO);
	
	public void updateAssetUsage(AssetUsageVO assetUsageVO);
	
	public ListResult<AssetUsageVO> findAssetUsageList(AssetUsageVO assetUsageVO,int limit,int page);
	
	AssetUsageVO getAssetUsageByUsageID(Integer usageID);
	
	public List<AssetUsageVO> getAssstUsageByAssetID(Integer assetID);

	AssetUsageVO getAssstUsageByAssetID1(Integer assetID);

	public void saveAssetUsage(AssetUsageEntity assetUsage);
	
	
}
