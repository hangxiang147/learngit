package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssetUsageDao;
import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.utils.ListResult;

public class AssetUsageDaoImpl implements AssetUsageDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveAssetUsage(AssetUsageEntity assetUsageEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(assetUsageEntity);
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AssetUsageEntity> getAssetUsageByAssetID(Integer assetID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="FROM AssetUsageEntity usage WHERE usage.isDeleted = 0 and usage.assetID = :assetID";
		Query query=session.createQuery(hql);
		query.setParameter("assetID", assetID);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AssetUsageEntity getAssetUsageByID(Integer usageID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="FROM AssetUsageEntity usage WHERE usage.isDeleted = 0 and usage.usageID = :usageID";
		Query query=session.createQuery(hql);
		query.setParameter("usageID", usageID);
		List<AssetUsageEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<Object> findAssetUsageList(String hql, String hqlCount, int limit, int page) {		Session session=sessionFactory.getCurrentSession();
		Query query=session.createSQLQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<Object> list=query.list();
		int totalCount=((Number)session.createSQLQuery(hqlCount).uniqueResult()).intValue();
		return new ListResult<Object>(list,totalCount);
	}
	
	
	

	@Override
	@SuppressWarnings("unchecked")
	public AssetUsageEntity getAssetUsageByID1(Integer assetID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="FROM AssetUsageEntity usage WHERE usage.isDeleted = 0 and usage.status=0 and usage.assetID = :assetID";
		Query query=session.createQuery(hql);
		query.setParameter("assetID", assetID);
		List<AssetUsageEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	

}
