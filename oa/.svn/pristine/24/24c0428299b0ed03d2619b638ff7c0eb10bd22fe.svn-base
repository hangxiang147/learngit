package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SkillDao;
import com.zhizaolian.staff.entity.SkillEntity;



public class SkillDaoImpl implements SkillDao {
	@Autowired
	private SessionFactory sessionFactory;
		

	@Override
	public void save(SkillEntity skillEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(skillEntity);
		
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<SkillEntity> findSkillsByUserID(String userID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from SkillEntity skill where skill.userID = :userID and skill.isDeleted = 0 ";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);
		List<SkillEntity> list=query.list();
		return list;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findSkillListByUserID(String sql, int page, int limit) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createSQLQuery(sql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}


	@Override
	public Object getUniqueResult(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.uniqueResult();
		
	}


	@Override
	public void delete(String userID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="delete SkillEntity skill where skill.userID = :userID and skill.isDeleted = 0  ";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);
		query.executeUpdate();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<SkillEntity> getSkillBySkillID(String userID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from SkillEntity skill where skill.isDeleted = 0 and skill.userID = :userID ";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);				
		return query.list();
	}



}
