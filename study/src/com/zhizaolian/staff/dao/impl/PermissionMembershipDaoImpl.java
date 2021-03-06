package com.zhizaolian.staff.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.PermissionMembershipDao;
import com.zhizaolian.staff.entity.PermissionMembershipEntity;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;

public class PermissionMembershipDaoImpl implements PermissionMembershipDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findPermissionIDsByUserGroupIDType(String userGroupID, int type) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select pm.PermissionID as permissionID from OA_PermissionMembership pm where pm.IsDeleted = 0 and pm.UserGroupID = :userGroupID and pm.Type = :type";
		Query query = session.createSQLQuery(sql);
		query.setParameter("userGroupID", userGroupID);
		query.setParameter("type", type);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findPermissionIDsByUserGroupIDsType(List<String> userGroupIDs, int type) {
		if (CollectionUtils.isEmpty(userGroupIDs)) {
			return Collections.emptyList();
		}
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select pm.PermissionID as permissionID from OA_PermissionMembership pm where pm.IsDeleted = 0 and pm.UserGroupID in (:userGroupIDs) and pm.Type = :type";
		Query query = session.createSQLQuery(sql);
		query.setParameterList("userGroupIDs", userGroupIDs);
		query.setParameter("type", type);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> findUserGroupIDsByPermissionIDType(int permissionID, int type) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PermissionMembershipEntity membership where membership.isDeleted = 0 and membership.permissionID = :permissionID and membership.type = :type";
		Query query = session.createQuery(hql);
		query.setParameter("permissionID", permissionID);
		query.setParameter("type", type);
		return Lists2.transform(query.list(), new SafeFunction<PermissionMembershipEntity, String>() {
			@Override
			protected String safeApply(PermissionMembershipEntity input) {
				return input.getUserGroupID();
			}
		});
	}

	@Override
	public Map<String, String> findPermissionIDAndCodeMapByUserGroupIDType(String userGroupID, int type) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "SELECT\n" +
				"	ship.id,\n" +
				"	p.PermissionName\n" +
				"FROM\n" +
				"	OA_PermissionMembership ship\n" +
				"INNER JOIN OA_Permission p ON ship.PermissionID = p.PermissionID\n" +
				"WHERE\n" +
				"	ship.UserGroupID = :UserGroupID\n" +
				"AND ship.Type = :type AND ship.isDeleted=0 AND p.isDeleted=0 AND p.process=1";
		Query query = session.createSQLQuery(sql);
		query.setParameter("UserGroupID", userGroupID);
		query.setParameter("type", type);
		@SuppressWarnings("unchecked")
		List<Object> objLst = query.list();
		Map<String, String> permissionIdAndNameMap = new HashMap<>();
		for(Object obj: objLst){
			Object[] objs = (Object[])obj;
			if(null!=objs[0] && null!=objs[1]){
				permissionIdAndNameMap.put(objs[0]+"", objs[1]+"");
			}
		}
		return permissionIdAndNameMap;
	}

	@Override
	public PermissionMembershipEntity findPermissionMembershipById(int rightId) {
		String hql = "from PermissionMembershipEntity where id="+rightId;
		return (PermissionMembershipEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public boolean checkHasPermissionByUserId(String permission, String id) {
		String hql = "select count(p.id) from PermissionMembershipEntity p,  PermissionEntity pe "
				+ "where p.permissionID=pe.permissionID and p.userGroupID='"+id+"' and p.type=1 and pe.permissionCode='"
				+ permission+"' and p.isDeleted=0";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
}
