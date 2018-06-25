package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AppVersionDao;
import com.zhizaolian.staff.entity.AppVersionEntity;

public class AppVersionDaoImpl implements AppVersionDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(AppVersionEntity appVersionEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(appVersionEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AppVersionEntity getLastVersionByType(int type) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from AppVersionEntity appVersion where appVersion.type = :type and appVersion.isDeleted = 0 order by appVersion.releaseTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("type", type);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<AppVersionEntity> list = query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
