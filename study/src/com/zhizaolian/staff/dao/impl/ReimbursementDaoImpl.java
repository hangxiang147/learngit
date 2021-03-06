package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ReimbursementDao;
import com.zhizaolian.staff.entity.AdvanceEntity;
import com.zhizaolian.staff.entity.PaymentEntity;
import com.zhizaolian.staff.entity.ReimbursementEntity;

public class ReimbursementDaoImpl implements ReimbursementDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int save(ReimbursementEntity reimbursementEntity) {
		Session session = sessionFactory.getCurrentSession();
		return (Integer)session.save(reimbursementEntity);
	}
	
	
	@Override
	public int saveAdvance(AdvanceEntity advanceEntity) {
		Session session = sessionFactory.getCurrentSession();
		return (Integer)session.save(advanceEntity);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ReimbursementEntity> findReimbursementsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ReimbursementEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID order by reimbursement.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AdvanceEntity> findAdvancesByUserID(String userID, int page,
			int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from AdvanceEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID order by reimbursement.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}


	@Override
	public int countAdvancesByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from AdvanceEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}


	@Override
	public int countReimbursementsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from ReimbursementEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Reimbursement reimbursement set reimbursement.ProcessStatus = :status "
				+ "where reimbursement.IsDeleted = 0 and reimbursement.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	



	@Override
	public void updateAdvanceProcessStatusByProcessInstanceID(
			String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Advance reimbursement set reimbursement.ProcessStatus = :status "
				+ "where reimbursement.IsDeleted = 0 and reimbursement.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
		
	}


	@Override
	@SuppressWarnings("unchecked")
	public ReimbursementEntity getReimbursementByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ReimbursementEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<ReimbursementEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public AdvanceEntity getAdvanceByProcessInstanceID(
			String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from AdvanceEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		@SuppressWarnings("unchecked")
		List<AdvanceEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}


	@Override
	public void setfinancialFirstAuditName(String instanceId, String name,int type) {
		if (type==1) {
			String hql=" update ReimbursementEntity e set e.showPerson1='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}else{
			String hql=" update ReimbursementEntity e set e.showPerson3='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}

		
	}


	@Override
	public void setAdvanceFinancialFirstAuditName(String instanceId,
			String name, int type) {
		if (type==1) {
			String hql=" update AdvanceEntity e set e.showPerson1='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}else{
			String hql=" update AdvanceEntity e set e.showPerson3='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}
		
	}


	@Override
	public int savePayment(PaymentEntity paymentEntity) {
		Session session = sessionFactory.getCurrentSession();
		return (Integer)session.save(paymentEntity);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentEntity> findPaymentsByUserID(String userID, Integer page, Integer limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PaymentEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID order by reimbursement.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}


	@Override
	public int countPaymentByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from PaymentEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}


	@Override
	public PaymentEntity getPaymentByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PaymentEntity reimbursement where reimbursement.isDeleted = 0 and reimbursement.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		@SuppressWarnings("unchecked")
		List<PaymentEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}


	@Override
	public void updatePaymentProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Payment reimbursement set reimbursement.ProcessStatus = :status "
				+ "where reimbursement.IsDeleted = 0 and reimbursement.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}


	@Override
	public void setPaymentFinancialFirstAuditName(String instanceId, String name, int type) {
		if (type==1) {
			String hql=" update PaymentEntity e set e.showPerson1='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}else{
			String hql=" update PaymentEntity e set e.showPerson3='"+name+"' where e.processInstanceID='"+instanceId+"' ";
			sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
		}
		
	}
}
