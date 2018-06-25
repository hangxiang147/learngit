package com.zhizaolian.staff.service;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetVO;

public interface AssetService {
	Integer addAsset(AssetVO assetVO);
	
	public ListResult<AssetVO> findAssetList(AssetVO assetVO,int limit,int page);
	
	AssetVO getAssetByID(Integer assetID);

	void updateAssetCompany(String assetId, String companyName);
	
	List<Object> getStaffAssets(String userId) throws Exception;

	void updateAssetUsageStatus(String usageID);
	
	XSSFWorkbook exportAssetVO(AssetVO assetVO);
	
	void updateAssetEntity(AssetEntity assetEntity);
}
