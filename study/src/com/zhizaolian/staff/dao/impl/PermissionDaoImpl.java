package com.zhizaolian.staff.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.PermissionDao;
import com.zhizaolian.staff.entity.PermissionEntity;

public class PermissionDaoImpl implements PermissionDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	@SuppressWarnings("unchecked")
	public List<PermissionEntity> findPermissionsByIDs(List<Integer> permissionIDs) {
		if (CollectionUtils.isEmpty(permissionIDs)) {
			return Collections.emptyList();
		}
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PermissionEntity permission where permission.isDeleted = 0 and permission.permissionID in (:permissionIDs)";
		Query query = session.createQuery(hql);
		query.setParameterList("permissionIDs", permissionIDs);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public PermissionEntity getPermissionByCode(String code) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PermissionEntity permission where permission.isDeleted = 0 and permission.permissionCode = :code";
		Query query = session.createQuery(hql);
		query.setParameter("code", code);
		List<PermissionEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
