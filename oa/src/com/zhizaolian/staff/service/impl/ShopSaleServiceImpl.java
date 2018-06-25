package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.ShopRelatedPersonEntity;
import com.zhizaolian.staff.entity.ToBeDoneTaskEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.service.ShopSaleService;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;

@Service(value="shopSaleService")
public class ShopSaleServiceImpl implements ShopSaleService{
	@Autowired
	private BaseDao baseDao;

	@SuppressWarnings("unchecked")
	@Override
	public void generateShopSaleReportTask() throws Exception {
		String hql = "from ShopRelatedPersonEntity where isDeleted=0";
		List<ShopRelatedPersonEntity> shops = (List<ShopRelatedPersonEntity>) baseDao.hqlfind(hql);
		for(ShopRelatedPersonEntity shop: shops){
			String shopOwner = shop.getShopOwner();
			if(StringUtils.isBlank(shopOwner)){
				continue;
			}
			ToBeDoneTaskEntity task = new ToBeDoneTaskEntity();
			task.setAddTime(new Date());
			task.setData(ObjectByteArrTransformer.toByteArray(shop));
			task.setType(BusinessTypeEnum.SHOP_DAY_SALE_REPORT.getValue());
			task.setUserId(shopOwner);
			baseDao.hqlSave(task);
		}
	}

	@Override
	public int getUnCompletedTaskByUserId(String userId) {
		String hql = "select count(id) from ToBeDoneTaskEntity where status=0 and "
				+ "isDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hql)));
	}

	@Override
	public List<ShopRelatedPersonEntity> findSaleReportTasksByUserId(String userId) {
		String sql = "SELECT\n" +
				"	task.`data`,\n" +
				"	StaffName\n" +
				"FROM\n" +
				"	OA_Staff staff,\n" +
				"	OA_ToBeDoneTask task\n" +
				"WHERE\n" +
				"	staff.UserID = task.userId\n" +
				"AND task.userId = ''\n" +
				"AND staff.`Status` != 4\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND task.isDeleted = 0\n" +
				"AND task.type = "+BusinessTypeEnum.SHOP_DAY_SALE_REPORT.getValue();
		List<Object> objs = baseDao.findBySql(sql);
		//此功能停止开发
		return null;
	}
}
