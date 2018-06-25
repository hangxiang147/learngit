package com.zhizaolian.staff.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.GroupDetailDao;
import com.zhizaolian.staff.entity.GroupDetailEntity;

public class GroupDetailDaoImpl implements GroupDetailDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(GroupDetailEntity groupDetailEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(groupDetailEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public GroupDetailEntity getGroupDetailByID(int groupDetailID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from GroupDetailEntity groupDetail where groupDetail.groupDetailID = :groupDetailID and groupDetail.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("groupDetailID", groupDetailID);
		List<GroupDetailEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<GroupDetailEntity> findGroupDetailPageList(int companyID, int departmentID, int positionID, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer("from GroupDetailEntity groupDetail where groupDetail.isDeleted = 0");
		if (companyID != 0) {
			hql.append(" and groupDetail.companyID = ").append(companyID);
		}
		if (departmentID != 0) {
			hql.append(" and groupDetail.departmentID = ").append(departmentID);
		}
		if (positionID != 0) {
			hql.append(" and groupDetail.positionID = ").append(positionID);
		}
		Query query = session.createQuery(hql.toString());
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public int countGroupDetails(Integer companyID, List<Integer> departmentIDs, Integer positionID) {
		Session session = sessionFactory.getCurrentSession();
		StringBuffer sql = new StringBuffer("select count(gd.GroupDetailID) as count from OA_GroupDetail gd where gd.IsDeleted = 0");
		if (companyID != null) {
			sql.append(" and gd.CompanyID = ").append(companyID);
		}
		if (departmentIDs != null && departmentIDs.size() != 0) {
			String arrayString = Arrays.toString(departmentIDs.toArray());
			sql.append(" and gd.DepartmentID in (").append(arrayString.substring(1, arrayString.length()-1)).append(")");
		}
		if (positionID != null) {
			sql.append(" and gd.PositionID = ").append(positionID);
		}
		Query query = session.createSQLQuery(sql.toString()).addScalar("count", StandardBasicTypes.INTEGER);
		return (Integer) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<GroupDetailEntity> findGroupDetailsByGroupIDs(List<String> groupIDs) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from GroupDetailEntity groupDetail where groupDetail.groupID in (:groupIDs) and groupDetail.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameterList("groupIDs", groupIDs);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public GroupDetailEntity getGroupDetailByGroupID(String groupID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from GroupDetailEntity groupDetail where groupDetail.groupID = :groupID and groupDetail.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("groupID", groupID);
		List<GroupDetailEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public GroupDetailEntity geDetailEntityByDepartmentIDPositionID(int departmentID,
			int positionID) {
		Session session = sessionFactory.getCurrentSession();
		String hql="from GroupDetailEntity groupDetail where  groupDetail.departmentID = :departmentID and "
				+ "groupDetail.positionID = :positionID and "
				+ "groupDetail.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("departmentID", departmentID);
		query.setParameter("positionID", positionID);
		List<GroupDetailEntity> result=query.list();
		if(result.size()>0){
			return result.get(0);
		}
		
		
		return null;
	}}
