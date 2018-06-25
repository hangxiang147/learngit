package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SpecialDao;

import com.zhizaolian.staff.entity.SpecialEntity;

public class SpecialDaoImpl implements SpecialDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	
	public void save(SpecialEntity specialEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(specialEntity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecialEntity> findSpecialListByTypeAndID(String userID, Integer type) {
		
		Session session=sessionFactory.getCurrentSession();
		String hql="from SpecialEntity special where special.isDeleted = 0 and special.userID = :userID and special.type = :type";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setParameter("type", type);
		List<SpecialEntity> list=query.list();
		
		return list;
	}

	@Override
	public void deleteSpecial(Integer specialID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="update SpecialEntity special set special.isDeleted = 1 where special.specialID = :specialID ";
		Query query=session.createQuery(hql);
		query.setParameter("specialID", specialID);
		
		query.executeUpdate();
		
			
		
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public SpecialEntity getSpecialByTypeAndID(String userID, Integer type) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from SpecialEntity special where special.isDeleted = 1 and special.userID = :userID and special.type = :type";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setParameter("type", type);
		List<SpecialEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
