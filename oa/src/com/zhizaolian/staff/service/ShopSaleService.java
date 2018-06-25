package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.entity.ShopRelatedPersonEntity;

public interface ShopSaleService {

	void generateShopSaleReportTask() throws Exception;

	int getUnCompletedTaskByUserId(String userId);

	List<ShopRelatedPersonEntity> findSaleReportTasksByUserId(String userId);
	
}
