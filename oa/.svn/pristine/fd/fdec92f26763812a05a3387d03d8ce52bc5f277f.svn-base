package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.CarUseDao;
import com.zhizaolian.staff.entity.CarUseEntity;
import com.zhizaolian.staff.utils.DateUtil;

public class CarUseDaoImpl implements CarUseDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(CarUseEntity carUseEntity) {
		carUseEntity.setAddTime(new Date());
		carUseEntity.setIsDeleted(0);
		sessionFactory.getCurrentSession().save(carUseEntity);
	}

	@Override
	public void update(CarUseEntity carUseEntity) {
		sessionFactory.getCurrentSession().update(carUseEntity);
	}

	@Override
	public void delete(String Id) {
		String hql = "update from CarUseEntity c set c.isDeleted='1' where c.carUse_Id=:carUse_Id";
		sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("carUse_Id", Id).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CarUseEntity> getCarUseByKeys(Map<String, String> params,
			int page, int limit) {
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String id = params.get("id");
		String userId = params.get("userId");
		String status=params.get("status");
		String hql = " from CarUseEntity c where 1=1";
		if (StringUtils.isNotBlank(id)) {
			hql += " and c.carUse_Id ='" + id + "' ";
		}
		if (StringUtils.isNotBlank(userId)) {
			hql += " and c.requestUserID ='" + userId + "' ";
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql += " and c.useTime >='" + startTime + "' ";
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql += " and c.useTime <='" + endTime + "' ";
		}
		if (StringUtils.isNotBlank(status)) {
			hql += " and c.processStatus =" + status + " ";
		}
		hql += " order by c.addTime desc ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getCarUseCountByKeys(Map<String, String> params) {
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		String id = params.get("id");
		String userId = params.get("userId");
		String status=params.get("status");
		String hql = " select count (*) from CarUseEntity c where 1=1";
		if (StringUtils.isNotBlank(id)) {
			hql += " and c.carUse_Id ='" + id + "' ";
		}
		if (StringUtils.isNotBlank(userId)) {
			hql += " and c.requestUserID ='" + userId + "' ";
		}
		if (StringUtils.isNotBlank(startTime)) {
			hql += " and c.useTime >='" + startTime + "' ";
		}
		if (StringUtils.isNotBlank(endTime)) {
			hql += " and c.useTime <='" + endTime + "' ";
		}
		if (StringUtils.isNotBlank(status)) {
			hql += " and c.processStatus =" + status + " ";
		}
		return ((Long) sessionFactory.getCurrentSession().createQuery(hql)
				.uniqueResult()).intValue();
	}

	@Override
	public CarUseEntity getUniqueCarUseEntity(Map<String, String> params) {
		String id = params.get("id");
		String instanceId = params.get("instanceId");
		String hql = " from CarUseEntity c where 1=1";
		if (StringUtils.isNotBlank(id)) {
			hql += " and c.carUse_Id ='" + id + "' ";
		}
		if (StringUtils.isNotBlank(instanceId)) {
			hql += " and c.processInstanceID ='" + instanceId + "' ";
		}
		return  (CarUseEntity) sessionFactory.getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTodayDetail() {
		String str_today=DateUtil.getDayStr(new Date());
		String sql="SELECT DISTINCT\n" +
				"	(a.StaffName)\n" +
				"FROM\n" +
				"	OA_CarUse c\n" +
				"LEFT JOIN OA_Staff a ON c.RequestUserID = a.UserId\n" +
				"WHERE\n" +
				"	c.UseTime = '"+str_today+"' \n" +
				"AND c.ProcessStatus = '1' ";
		
		return sessionFactory.openSession().createSQLQuery(sql).list();
	}
	
}
