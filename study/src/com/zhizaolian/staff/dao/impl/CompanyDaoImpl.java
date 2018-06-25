package com.zhizaolian.staff.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;

public class CompanyDaoImpl implements CompanyDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	@Override
	@SuppressWarnings("unchecked")
	public List<CompanyEntity> findAllCompanys() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CompanyEntity company where company.isDeleted = 0";
		return session.createQuery(hql).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CompanyEntity getCompanyByCompanyID(int companyID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CompanyEntity company where company.companyID = :companyID and company.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("companyID", companyID);
		List<CompanyEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompanyEntity getCompanyByCompanyName(String companyName) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CompanyEntity company where company.companyName = :companyName and company.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("companyName", companyName);
		List<CompanyEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Integer getCompanyIdByUserId(String userId) {
		String sql = "SELECT\n" +
				"	CompanyID\n" +
				"FROM\n" +
				"	oa_groupdetail groupDetail,\n" +
				"	act_id_membership ship\n" +
				"WHERE\n" +
				"	groupDetail.isDeleted=0 and groupDetail.GroupID = ship.GROUP_ID_\n" +
				"AND ship.USER_ID_ = '"+userId+"'";
		List<Object> objs = baseDao.findBySql(sql);
		Integer companyId = null;
		for(Object obj: objs){
			companyId = (int)obj;
			//总部优先
			if(companyId == CompanyIDEnum.QIAN.getValue()){
				break;
			}
		}
		return companyId;
	}
}
