package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ReceiveExpressDao;
import com.zhizaolian.staff.entity.ReceiveExpressEntity;
import com.zhizaolian.staff.utils.ListResult;

public class ReceiveExpressDaoImpl implements ReceiveExpressDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ReceiveExpressEntity signExpressEntity) {
		Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(signExpressEntity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<ReceiveExpressEntity> findSignExpressList(String hql, String hqlcount, int page, int limit) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<ReceiveExpressEntity> list=query.list();
		int totalCount=((Number)session.createQuery(hqlcount).uniqueResult()).intValue();
		return new ListResult<ReceiveExpressEntity>(list,totalCount);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReceiveExpressEntity getSignExpressEntityByID(Integer receiveExpressID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from ReceiveExpressEntity receiveExpress where receiveExpress.isDeleted = 0 "
				+ "and receiveExpress.receiveExpressID = :receiveExpressID";
		Query query=session.createQuery(hql);
		query.setParameter("receiveExpressID", receiveExpressID);
		List<ReceiveExpressEntity> list=query.list();
		return list.get(0);
	}
}
