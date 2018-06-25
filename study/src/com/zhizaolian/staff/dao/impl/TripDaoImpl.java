package com.zhizaolian.staff.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.TripDao;
import com.zhizaolian.staff.entity.TripEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;

public class TripDaoImpl implements TripDao  {
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public void save(TripEntity tripEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(tripEntity);
	}

	@SuppressWarnings("unchecked")
	public List<TripEntity> findTripByUserID(String userID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from TripEntity t where t.isDeleted = 0 and t.userID = :userID order by t.addTime desc";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int countTripByUserID(String userId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from TripEntity t where t.isDeleted = 0 and t.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userId);
		return ((Long)query.uniqueResult()).intValue();
	}

	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		String sql=" update OA_BussinessTrip set applyResult="+taskResult.getValue()+" where processInstanceID='"+processInstanceID+"'";
		sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TripEntity> getTripByKeys(Map<String, String> map,int page,int limit) {
		String startTime=map.get("startTime");
		String endTime=map.get("endTime");
		String userId=map.get("userId");
		String userName=map.get("userName");
		StringBuffer sb =new StringBuffer();
		sb.append(" from TripEntity t where 1=1");
		if(StringUtils.isNotBlank(startTime)){
			sb.append(" and t.startTime >= '").append(startTime).append("' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.startTime <= '").append(endTime).append("' ");
		}
		if(StringUtils.isNotBlank(userId)){
			sb.append(" and t.requestUserID = '").append(userId).append("' ");
		}else{
			if(StringUtils.isNotBlank(userName)){
				sb.append(" and t.requestUserName like '%").append(userName).append("%' ");
			}
		}
		sb.append(" and t.applyResult='1' and t.isDeleted='0' ");
		
		return  sessionFactory.getCurrentSession().createQuery(sb.toString()).setFirstResult((page-1)*limit).setMaxResults(limit).list();
	}

	@Override
	public int getCountOfTripByKeys(Map<String, String> map) {
		String startTime=map.get("startTime");
		String endTime=map.get("endTime");
		String userId=map.get("userId");
		String userName=map.get("userName");
		StringBuffer sb =new StringBuffer();
		sb.append("  select count (bussinessTripID) from TripEntity t where 1=1");
		if(StringUtils.isNotBlank(startTime)){
			sb.append(" and t.startTime >= '").append(startTime).append("' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and t.startTime <= '").append(endTime).append("' ");
		}
		if(StringUtils.isNotBlank(userId)){
			sb.append(" and t.requestUserID = '").append(userId).append("' ");
		}else {
			if(StringUtils.isNotBlank(userName)){
				sb.append(" and t.requestUserName like '%").append(userName).append("%' ");
			}
		}
		sb.append(" and t.applyResult='1' and t.isDeleted='0' ");
		return  ((Long)sessionFactory.getCurrentSession().createQuery(sb.toString()).uniqueResult()).intValue();	
	}
	
}
