package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.WorkReportDao;
import com.zhizaolian.staff.entity.WorkReportEntity;
import com.zhizaolian.staff.utils.DateUtil;

public class WorkReportDaoImpl implements WorkReportDao {
	@Autowired
	private SessionFactory sessionFactory;
    @Override
	public void save(WorkReportEntity workReportEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(workReportEntity);

	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findWorkreportListByUserID(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.list(); 
	}
	@Override
	public Object getUniqueResult(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<WorkReportEntity> findWorkReportByDateAndUserID(String date, String userID) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from WorkReportEntity workReport where workReport.userID =:userID "
				+ "and workReport.reportDate =:reportDate and workReport.isDeleted=0");
		query.setParameter("userID", userID);
		query.setParameter("reportDate",DateUtil.getSimpleDate(date));
		return query.list();
		
	}
   
}
