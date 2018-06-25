package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.PositionDao;
import com.zhizaolian.staff.entity.PositionEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;

public class PositionDaoImpl implements PositionDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	@Override
	@SuppressWarnings("unchecked")
	public List<PositionEntity> findAllPositions() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionEntity position where position.isDeleted = 0";
		return session.createQuery(hql).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByDepartmentID(int departmentID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionEntity position where position.departmentID = :departmentID and position.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("departmentID", departmentID);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public PositionEntity getPositionByPositionID(int positionID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionEntity position where position.positionID = :positionID and position.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("positionID", positionID);
		List<PositionEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PositionEntity getPositionByPositionName(String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionEntity position where position.positionName = :positionName and position.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("positionName", name);
		List<PositionEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked") 
	public PositionEntity getPositionByDepartmentIDName(int departmentID, String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionEntity position where position.departmentID = :departmentID and position.positionName = :positionName and position.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("departmentID", departmentID);
		query.setParameter("positionName", name);
		List<PositionEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public int getPositionIdByUserId(String userId) {
		String sql = "SELECT\n" +
				"	positionID, CompanyID\n" +
				"FROM\n" +
				"	oa_groupdetail groupDetail,\n" +
				"	act_id_membership ship\n" +
				"WHERE\n" +
				"	groupDetail.isDeleted=0 and groupDetail.GroupID = ship.GROUP_ID_\n" +
				"AND ship.USER_ID_ = '"+userId+"'";
		List<Object> objs = baseDao.findBySql(sql);
		Integer positionId = null;
		for(Object obj: objs){
			Object[] info = (Object[])obj;
			positionId = (int)info[0];
			//总部优先
			if((int)info[1] == CompanyIDEnum.QIAN.getValue()){
				break;
			}
		}
		return positionId;
	}
}
