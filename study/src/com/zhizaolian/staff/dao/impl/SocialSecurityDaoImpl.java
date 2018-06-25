package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SocialSecurityDao;
import com.zhizaolian.staff.entity.SocialSecurityEntity;

public class SocialSecurityDaoImpl implements SocialSecurityDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(SocialSecurityEntity socialSecurityEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(socialSecurityEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SocialSecurityEntity getSocialSecurityByID(int ssID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityEntity socialSecurity where socialSecurity.ssID = :ssID and socialSecurity.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("ssID", ssID);
		List<SocialSecurityEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public int deleteSocialSecurityByID(int ssID) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_SocialSecurity ss set ss.IsDeleted = 1 "
				+ "where ss.IsDeleted = 0 and ss.SSID = :ssID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("ssID", ssID);
		return query.executeUpdate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SocialSecurityEntity> findSocialSecurityListByProcessID(int processID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityEntity socialSecurity where socialSecurity.processID = :processID and socialSecurity.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("processID", processID);
		return query.list();
	}
	
	@Override
	public void updateProcessIDByTime(int year, int month, int sspID) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_SocialSecurity ss set ss.ProcessID = :sspID "
				+ "where ss.IsDeleted = 0 and ss.PaymentYear = :year and ss.PaymentMonth = :month and ss.ProcessID IS NULL";
		Query query = session.createSQLQuery(sql);
		query.setParameter("sspID", sspID);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.executeUpdate();
	}
	
}
