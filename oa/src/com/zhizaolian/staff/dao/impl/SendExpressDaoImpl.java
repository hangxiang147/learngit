package com.zhizaolian.staff.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SendExpressDao;
import com.zhizaolian.staff.entity.SendExpressEntity;

public class SendExpressDaoImpl implements SendExpressDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(SendExpressEntity sendExpressEntity) {
		Session session = sessionFactory.getCurrentSession();
        session.save(sendExpressEntity);
	}

}
