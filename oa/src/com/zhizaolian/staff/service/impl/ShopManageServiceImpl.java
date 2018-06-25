package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.ShopInfoEntity;
import com.zhizaolian.staff.service.ShopManageService;
import com.zhizaolian.staff.utils.ListResult;

public class ShopManageServiceImpl implements ShopManageService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public void saveShopInfo(ShopInfoEntity shopInfo) {
		shopInfo.setIsDeleted(0);
		if(null == shopInfo.getId()){
			shopInfo.setShopStatus(0);
			shopInfo.setAddTime(new Date());
			baseDao.hqlSave(shopInfo);
		}else{
			baseDao.hqlUpdate(shopInfo);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ShopInfoEntity> showShops(Integer limit, Integer page, String reserveTelephone) {
		String hql = "from ShopInfoEntity shop where isDeleted=0";
		if(StringUtils.isNotBlank(reserveTelephone)){
			hql += " and shop.reserveTelephone like :reservePhone";
		}
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		if(StringUtils.isNotBlank(reserveTelephone)){
			query.setParameter("reservePhone", "%"+reserveTelephone+"%");
		}
		List<ShopInfoEntity> shopInfos = query.list();
		String hqlCount = "select count(id) from ShopInfoEntity shop where isDeleted=0";
		if(StringUtils.isNotBlank(reserveTelephone)){
			hqlCount += " and shop.reserveTelephone like :reservePhone";
		}
		query = session.createQuery(hqlCount);
		if(StringUtils.isNotBlank(reserveTelephone)){
			query.setParameter("reservePhone", "%"+reserveTelephone+"%");
		}
		int count = Integer.parseInt(query.uniqueResult()+"");
		return new ListResult<>(shopInfos, count);
	}
	@Override
	public ShopInfoEntity showShopInfoDetail(String shopInfoId) {
		String hql = "from ShopInfoEntity where id="+shopInfoId;
		return (ShopInfoEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void deleteShop(String id) {
		String sql = "update OA_ShopInfo set isDeleted=1 where id="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public void closeShop(String id) {
		String sql = "update OA_ShopInfo set shopStatus=1 where id="+id;
		baseDao.excuteSql(sql);
	}
}
