package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.CarpoolDao;
import com.zhizaolian.staff.entity.CarpoolEntity;

public class CarpoolDaoImpl implements CarpoolDao{
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<CarpoolEntity> getCarpoolList(Map<String, String> params,int page,int size) {
		String dept_Id=params.get("dept_Id");
		String company_Id=params.get("company_Id");
		String startTime=params.get("startTime");
		String endTime=params.get("endTime");
		String user_Id=params.get("user_Id");
		String id=params.get("id");
		StringBuffer sb=new StringBuffer("from CarpoolEntity c where 1=1 ");
		if(StringUtils.isNotBlank(id)){
			sb.append(" and c.Carpool_Id=").append(id);
		}
		if(StringUtils.isNotBlank(company_Id)){
			sb.append(" and c.Company_Id =").append(company_Id);
		}
		if(StringUtils.isNotBlank(dept_Id)){
			sb.append(" and c.Dept_Id in(").append(dept_Id).append(")");
		}
		if(StringUtils.isNotBlank(startTime)){
			sb.append(" and c.StartTime>='").append(startTime).append("'");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and c.EndTime<='").append(endTime).append("'");
		}
		if(StringUtils.isNotBlank(user_Id)){
			sb.append(" and c.PersonDetail like '%").append(user_Id).append("%'");
		}
		sb.append(" and c.IsDeleted=0  order by c.StartTime desc ");
		Query query=sessionFactory.getCurrentSession().createQuery(sb.toString());
		query.setFirstResult((page-1)*size);
		query.setMaxResults(size);
		return query.list();
	}

	@Override
	public void save(CarpoolEntity carpoolEntity) {
		carpoolEntity.setAddTime(new Date());
		sessionFactory.getCurrentSession().save(carpoolEntity);
	}

	@Override
	public void updateCarpool(CarpoolEntity carpoolEntity) {
		sessionFactory.getCurrentSession().update(carpoolEntity);;
	}

	@Override
	public void delete(String id) {
		int id_=Integer.parseInt(id);
		String sql="delete from CarpoolEntity c where c.Carpool_Id=:Carpool_Id";
		sessionFactory.getCurrentSession().createQuery(sql).setParameter("Carpool_Id", id_).executeUpdate();
	}

	@Override
	public int getCarpoolCount(Map<String, String> params) {
		String dept_Id=params.get("dept_Id");
		String startTime=params.get("startTime");
		String endTime=params.get("endTime");
		String user_Id=params.get("user_Id");
		String company_Id=params.get("company_Id");
		String id=params.get("id");
		StringBuffer sb=new StringBuffer("select count(*) from CarpoolEntity c where 1=1 ");
		if(StringUtils.isNotBlank(id)){
			sb.append(" and c.Carpool_Id=").append(id);
		}
		if(StringUtils.isNotBlank(company_Id)){
			sb.append(" and c.Company_Id =").append(company_Id);
		}
		if(StringUtils.isNotBlank(dept_Id)){
			sb.append(" and c.Dept_Id in(").append(dept_Id).append(")");
		}
		if(StringUtils.isNotBlank(startTime)){
			sb.append(" and c.StartTime>='").append(startTime).append("'");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and c.EndTime<='").append(endTime).append("'");
		}
		if(StringUtils.isNotBlank(user_Id)){
			sb.append(" and c.PersonDetail like '%").append(user_Id).append("%'");
		}
		sb.append(" and c.IsDeleted=0 ");
		Query query=sessionFactory.getCurrentSession().createQuery(sb.toString());
	
		return ((Long)query.uniqueResult()).intValue();
	}
}
