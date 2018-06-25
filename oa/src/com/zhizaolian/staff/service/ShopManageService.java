package com.zhizaolian.staff.service;

import com.zhizaolian.staff.entity.ShopInfoEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface ShopManageService {

	void saveShopInfo(ShopInfoEntity shopInfo);

	ListResult<ShopInfoEntity> showShops(Integer limit, Integer page, String reserveTelephone);

	ShopInfoEntity showShopInfoDetail(String shopInfoId);

	void deleteShop(String id);

	void closeShop(String id);

}
