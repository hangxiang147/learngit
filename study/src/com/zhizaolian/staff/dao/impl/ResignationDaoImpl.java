package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ResignationDao;
import com.zhizaolian.staff.entity.ResignationEntity;

public class ResignationDaoImpl implements ResignationDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(ResignationEntity resignationEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(resignationEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ResignationEntity> findResignationsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ResignationEntity resignation where resignation.isDeleted = 0 and resignation.userID = :userID order by resignation.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countResignationsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from ResignationEntity resignation where resignation.isDeleted = 0 and resignation.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Resignation resignation set resignation.ProcessStatus = :status "
				+ "where resignation.IsDeleted = 0 and resignation.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public void updateSupervisorConfirmDate(String processInstanceID, Date leaveDate) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Resignation resignation set resignation.SupervisorConfirmDate = :leaveDate "
				+ "where resignation.IsDeleted = 0 and resignation.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("leaveDate", leaveDate);
		query.executeUpdate();
	}
	
	@Override
	public void updateManagerConfirmDate(String processInstanceID, Date leaveDate) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Resignation resignation set resignation.ManagerConfirmDate = :leaveDate "
				+ "where resignation.IsDeleted = 0 and resignation.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("leaveDate", leaveDate);
		query.executeUpdate();
	}
}
