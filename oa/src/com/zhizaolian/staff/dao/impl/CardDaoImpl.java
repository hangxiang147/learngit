package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.CardDao;
import com.zhizaolian.staff.entity.CardEntity;

public class CardDaoImpl implements CardDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(CardEntity cardEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(cardEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CardEntity> findCardsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CardEntity card where card.isDeleted = 0 and card.userID = :userID order by card.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countCardsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from CardEntity card where card.isDeleted = 0 and card.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CardEntity getCardByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CardEntity card where card.isDeleted = 0 and card.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<CardEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_IDCard card set card.ProcessStatus = :status "
				+ "where card.IsDeleted = 0 and card.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
}
