package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.VacationDao;
import com.zhizaolian.staff.entity.VacationEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;

public class VacationDaoImpl implements VacationDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(VacationEntity vacationEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(vacationEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VacationEntity> findVacationsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from VacationEntity vacation where vacation.isDeleted = 0 and vacation.userID = :userID and vacation.processInstanceID is not null order by vacation.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
		
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Vacation vacation set vacation.ProcessStatus = :status "
				+ "where vacation.IsDeleted = 0 and vacation.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public int countVacationsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from VacationEntity vacation where vacation.isDeleted = 0 and vacation.userID = :userID and vacation.processInstanceID is not null";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VacationEntity> findVacationsByRequestUserID(String requestUserID) {
		
		Session session=sessionFactory.getCurrentSession();
		String hql="from VacationEntity vacation where vacation.isDeleted = 0 and vacation.requestUserID = :requestUserID order by vacation.addTime desc";
		Query query=session.createQuery(hql);
		query.setParameter("requestUserID", requestUserID);
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VacationEntity> findVacationsByDate(Date time, String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from VacationEntity vacation where vacation.isDeleted = 0 and vacation.requestUserID = :userID and "
				+ "vacation.beginDate < :time and vacation.endDate > :time and (vacation.processInstanceID is null or vacation.processStatus = 1)";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setParameter("time", time);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VacationEntity> findVacationsByDates(Date begin, Date end, String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from VacationEntity vacation where vacation.isDeleted = 0 and vacation.requestUserID = :userID and "
				+ "vacation.beginDate >= :begin and vacation.endDate <= :end and (vacation.processInstanceID is null or vacation.processStatus = 1)";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setParameter("begin", begin);
		query.setParameter("end", end);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findVacationByhql(String hql, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public VacationEntity getVacationByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from VacationEntity vacation where vacation.isDeleted = 0 and vacation.processInstanceID = :processInstanceID ";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<VacationEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public VacationEntity findVacationsByvacationID(Integer vacationID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from VacationEntity vacation where vacation.isDeleted = 0 and vacation.vacationID = :vacationID ";
		Query query = session.createQuery(hql);
		query.setParameter("vacationID", vacationID);
		List<VacationEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
		
	}

	@Override
	public void deleteVacationsByVacationEntity(VacationEntity vacationEntity) {
		vacationEntity.setIsDeleted(IsDeletedEnum.DELETED.getValue());
		save(vacationEntity);
		
	}

	
}
