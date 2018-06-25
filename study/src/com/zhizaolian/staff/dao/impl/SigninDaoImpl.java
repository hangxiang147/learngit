package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SigninDao;
import com.zhizaolian.staff.entity.SigninEntity;

public class SigninDaoImpl implements SigninDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(SigninEntity signinEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(signinEntity);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SigninEntity> findSigninList(String userID, Date signinDate) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from SigninEntity signin where signin.isDeleted = 0 and signin.userID = :userID and signin.signinDate = :signinDate";
		Query query=session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setParameter("signinDate", signinDate);
		List<SigninEntity> list=query.list();
		
		return list;
	}

	

}
