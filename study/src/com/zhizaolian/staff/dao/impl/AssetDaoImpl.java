package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssetDao;
import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.utils.ListResult;

public class AssetDaoImpl implements AssetDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(AssetEntity assetEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(assetEntity);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<AssetEntity> findAssetList(String hql, String hqlcount, int page, int limit) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<AssetEntity> list=query.list();
		int totalCount=((Number)session.createQuery(hqlcount).uniqueResult()).intValue();
		return new ListResult<AssetEntity>(list,totalCount);
	}
	@Override
	@SuppressWarnings("unchecked")
	public AssetEntity getAssetByID(Integer assetID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from AssetEntity asset where asset.assetID = :assetID and asset.isDeleted =  0";
		Query query=session.createQuery(hql);
		query.setParameter("assetID", assetID);
		List<AssetEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateAsset(Integer status,Integer assetID, String storageLocation) {
		Session session=sessionFactory.getCurrentSession();
		String hql = "update AssetEntity asset set asset.status =:status,asset.storageLocation = :storageLocation where asset.assetID = :assetID";
		Query query=session.createQuery(hql);
		query.setParameter("storageLocation", storageLocation);
		query.setParameter("status", status);
		query.setParameter("assetID", assetID);
		query.executeUpdate();
		
	}

	@Override
	public void updateAssetEntity(AssetEntity assetEntity) {
		sessionFactory.getCurrentSession().update(assetEntity);
	}

	
	
}
