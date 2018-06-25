package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.EmailDao;
import com.zhizaolian.staff.entity.EmailEntity;

public class EmailDaoImpl implements EmailDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(EmailEntity emailEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(emailEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EmailEntity> findEmailsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from EmailEntity email where email.isDeleted = 0 and email.userID = :userID order by email.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countEmailsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from EmailEntity email where email.isDeleted = 0 and email.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();	
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Email email set email.ProcessStatus = :status "
				+ "where email.IsDeleted = 0 and email.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public void updateEmailAccountByProcessInstanceID(String processInstanceID, String address, String password, String loginUrl) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Email email set email.ConfirmAddress = :address, email.OriginalPassword = :password, email.LoginUrl = :loginUrl "
				+ "where email.IsDeleted = 0 and email.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("address", address);
		query.setParameter("password", password);
		query.setParameter("loginUrl", loginUrl);
		query.executeUpdate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public EmailEntity getEmailByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from EmailEntity email where email.isDeleted = 0 and email.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<EmailEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
