package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssignmentDao;
import com.zhizaolian.staff.entity.AssignmentEntity;
public class AssignmentDaoImpl implements AssignmentDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(AssignmentEntity assignmentEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(assignmentEntity);
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Assignment assignment set assignment.ProcessStatus = :status "
				+ "where assignment.IsDeleted = 0 and assignment.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public void updateBeginDate(String processInstanceID, Date beginDate) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Assignment assignment set assignment.BeginDate = :beginDate "
				+ "where assignment.IsDeleted = 0 and assignment.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("beginDate", beginDate);
		query.executeUpdate();
	}
	
	@Override
	public void updateScore(String processInstanceID, Float score) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Assignment assignment set assignment.score = :score "
				+ "where assignment.IsDeleted = 0 and assignment.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("score", score);
		query.executeUpdate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AssignmentEntity> findAssignmentsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from AssignmentEntity assignment where assignment.isDeleted = 0 and assignment.userID = :userID order by assignment.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countAssignmentsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from AssignmentEntity assignment where assignment.isDeleted = 0 and assignment.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AssignmentEntity getAssignmentByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from AssignmentEntity assignment where assignment.processInstanceID = :processInstanceID and assignment.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<AssignmentEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
