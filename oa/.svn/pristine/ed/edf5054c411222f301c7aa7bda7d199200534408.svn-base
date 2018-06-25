package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SocialSecurityProcessDao;
import com.zhizaolian.staff.entity.SocialSecurityProcessEntity;

public class SocialSecurityProcessDaoImpl implements SocialSecurityProcessDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(SocialSecurityProcessEntity socialSecurityProcessEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(socialSecurityProcessEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SocialSecurityProcessEntity> findSocialSecurityProcessListByPage(int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityProcessEntity ssp where ssp.isDeleted = 0 order by ssp.addTime desc";
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countSocialSecurityProcess() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from SocialSecurityProcessEntity ssp where ssp.isDeleted = 0";
		Query query = session.createQuery(hql);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SocialSecurityProcessEntity getSocialSecurityProcessByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityProcessEntity ssp where ssp.isDeleted = 0 and ssp.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<SocialSecurityProcessEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_SocialSecurityProcess ssp set ssp.ProcessStatus = :status "
				+ "where ssp.IsDeleted = 0 and ssp.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public void updatePaymentCountByProcessInstanceID(String pInstanceID, double personalCount, double companyCount) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_SocialSecurityProcess ssp set ssp.personalCount = :personalCount, "
				+ "ssp.companyCount = :companyCount, ssp.totalCount = :totalCount "
				+ "where ssp.IsDeleted = 0 and ssp.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", pInstanceID);
		query.setParameter("personalCount", personalCount);
		query.setParameter("companyCount", companyCount);
		query.setParameter("totalCount", personalCount+companyCount);
		query.executeUpdate();
	}
	
	@Override
	public void updateHFCountByProcessInstanceID(String pInstanceID, double totalCount) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_SocialSecurityProcess ssp set ssp.HFTotalCount = :totalCount "
				+ "where ssp.IsDeleted = 0 and ssp.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", pInstanceID);
		query.setParameter("totalCount", totalCount);
		query.executeUpdate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SocialSecurityProcessEntity getLastProcessByHFTime(int year, int month, int companyID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityProcessEntity ssp where ssp.isDeleted = 0 and ssp.paymentYear = :paymentYear "
				+ "and ssp.paymentMonth = :paymentMonth and ssp.companyID = :companyID order by ssp.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("paymentYear", year);
		query.setParameter("paymentMonth", month);
		query.setParameter("companyID", companyID);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<SocialSecurityProcessEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SocialSecurityProcessEntity getLastProcessBySSTime(int year, int month, int companyID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SocialSecurityProcessEntity ssp where ssp.isDeleted = 0 and ssp.hfPaymentYear = :paymentYear "
				+ "and ssp.hfPaymentMonth = :paymentMonth and ssp.companyID = :companyID order by ssp.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("paymentYear", year);
		query.setParameter("paymentMonth", month);
		query.setParameter("companyID", companyID);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<SocialSecurityProcessEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

}
