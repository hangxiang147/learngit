package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.FormalDao;
import com.zhizaolian.staff.entity.FormalEntity;
import com.zhizaolian.staff.utils.DateUtil;

public class FormalDaoImpl implements FormalDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(FormalEntity formalEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(formalEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FormalEntity> findFormalsByRequestUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from FormalEntity formal where formal.isDeleted = 0 and formal.requestUserID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FormalEntity> findFormalsByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from FormalEntity formal where formal.isDeleted = 0 and formal.userID = :userID order by formal.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countFormalsByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from FormalEntity formal where formal.isDeleted = 0 and formal.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public FormalEntity getFormalByProcessInstanceID(String processInstanceID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from FormalEntity formal where formal.processInstanceID = :processInstanceID and formal.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("processInstanceID", processInstanceID);
		List<FormalEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public void updateProcessStatusByProcessInstanceID(String processInstanceID, int status) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Formal formal set formal.ProcessStatus = :status "
				+ "where formal.IsDeleted = 0 and formal.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("status", status);
		query.executeUpdate();
	}
	
	@Override
	public void updateActualFormalDate(String processInstanceID, Date formalDate) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_Formal formal set formal.ActualFormalDate = :formalDate "
				+ "where formal.IsDeleted = 0 and formal.ProcessInstanceID = :processInstanceID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("processInstanceID", processInstanceID);
		query.setParameter("formalDate", formalDate);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormalEntity> findFormalsByConditions(String staffName, int page, int limit, String beginDate,
			String endDate) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "";
		if(StringUtils.isNotBlank(staffName)){
			hql = "select formal from FormalEntity formal, StaffEntity staff where formal.isDeleted=0 and"
					+ " formal.requestUserID=staff.userID and staffName like:staffName\n";
		}else{
			hql = "from FormalEntity formal where formal.isDeleted=0\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and Date(formal.addTime) >=:beginDate\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and Date(formal.addTime) <=:endDate\n";
		}
		hql += "order by formal.addTime desc";
		Query query = session.createQuery(hql);
		if(StringUtils.isNotBlank(staffName)){
			query.setParameter("staffName", "%"+staffName+"%");
		}
		if(StringUtils.isNotBlank(beginDate)){
			query.setParameter("beginDate", DateUtil.getSimpleDate(beginDate));
		}
		if(StringUtils.isNotBlank(endDate)){
			query.setParameter("endDate", DateUtil.getSimpleDate(endDate));
		}
		query.setMaxResults(limit);
		query.setFirstResult((page-1)*limit);
		List<FormalEntity> formalEntitys = query.list();
		return formalEntitys;
	}

	@Override
	public int countFormalsByConditions(String staffName, String beginDate, String endDate) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "";
		if(StringUtils.isNotBlank(staffName)){
			hql = "select count(formalID) from FormalEntity formal, StaffEntity staff where formal.isDeleted=0 and"
					+ " formal.requestUserID=staff.userID and staffName like:staffName\n";
		}else{
			hql = "select count(formalID) from FormalEntity formal where formal.isDeleted=0\n";
		}
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and Date(formal.addTime) >=:beginDate\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and Date(formal.addTime) <=:endDate";
		}
		Query query = session.createQuery(hql);
		if(StringUtils.isNotBlank(staffName)){
			query.setParameter("staffName", "%"+staffName+"%");
		}
		if(StringUtils.isNotBlank(beginDate)){
			query.setParameter("beginDate", DateUtil.getSimpleDate(beginDate));
		}
		if(StringUtils.isNotBlank(endDate)){
			query.setParameter("endDate", DateUtil.getSimpleDate(endDate));
		}
		return Integer.parseInt(query.uniqueResult()+"");
	}
}
