package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;

public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DepartmentEntity> findDepartmentsByCompanyIDParentID(int companyID, int parentID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.isDeleted = 0 and department.companyID = :companyID and department.parentID = :parentID";
		Query query = session.createQuery(hql);
		query.setParameter("companyID", companyID);
		query.setParameter("parentID", parentID);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DepartmentEntity> findDepartmentsByCompanyID(int companyID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.isDeleted = 0 and department.companyID = :companyID";
		Query query = session.createQuery(hql);
		query.setParameter("companyID", companyID);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public DepartmentEntity getDepartmentByDepartmentID(int departmentID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.departmentID = :departmentID and department.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("departmentID", departmentID);
		List<DepartmentEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public DepartmentEntity getDepartmentByDepartmentID_(int departmentID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.departmentID = :departmentID";
		Query query = session.createQuery(hql);
		query.setParameter("departmentID", departmentID);
		List<DepartmentEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public DepartmentEntity getDepartmentByCompanyIDAndName(int companyID, String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.departmentName like :departmentName and department.companyID = :companyID and department.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("departmentName", "%"+name+"%");
		query.setParameter("companyID", companyID);
		List<DepartmentEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DepartmentEntity> findDepartmentByParentID(int parentID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DepartmentEntity department where department.isDeleted = 0 and department.parentID = :parentID";
		Query query = session.createQuery(hql);		
		query.setParameter("parentID", parentID);
		return query.list();
	}

	@Override
	public Integer getDeparmentIdByUserId(String userId) {
		String sql = "SELECT\n" +
				"	groupDetail.DepartmentID,\n" +
				"	CompanyID\n" +
				"FROM\n" +
				"	oa_groupdetail groupDetail,\n" +
				"	act_id_membership ship\n" +
				"WHERE\n" +
				"	groupDetail.isDeleted=0 and groupDetail.GroupID = ship.GROUP_ID_\n" +
				"AND ship.USER_ID_ = '"+userId+"'";
		List<Object> objs = baseDao.findBySql(sql);
		Integer departmentId = null;
		for(Object obj: objs){
			Object[] info = (Object[])obj;
			departmentId = (int)info[0];
			//总部优先
			if((int)info[1] == CompanyIDEnum.QIAN.getValue()){
				break;
			}
		}
		return departmentId;
	}
}
