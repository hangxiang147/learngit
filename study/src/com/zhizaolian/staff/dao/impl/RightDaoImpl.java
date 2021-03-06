package com.zhizaolian.staff.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.RightDao;
import com.zhizaolian.staff.utils.DateUtil;

public class RightDaoImpl implements RightDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllRight() {
		String sql = "select PermissionName,PermissionCode,PermissionID,process,processKeys,nodeKey,type,mapKey from OA_Permission where IsDeleted='0' ";
		return sessionFactory.getCurrentSession().createSQLQuery(sql).list();
	}

	@Override
	public void insertRight(String rightName, String code) {
		String sql = "insert into OA_Permission (PermissionName,PermissionCode,IsDeleted,AddTime,UpdateTime)\n"
				+ "values(:rightName,:code,0,:addTime,:updateTime)";
		String now = DateUtil.getNowString();
		sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("rightName", rightName).setParameter("code", code)
				.setParameter("addTime", now).setParameter("updateTime", now)
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getRightMemberShip(String userId,
			int page, int limit) {
		String sql="SELECT\n" +
				"	s.StaffName,p.PermissionName,pe.ID\n" +
				"FROM\n" +
				"	OA_Permission p,\n" +
				"	OA_PermissionMembership pe,\n" +
				"	OA_Staff s\n" +
				"WHERE\n" +
				"	p.PermissionID = pe.PermissionID\n" +
				"	and s.UserID=pe.UserGroupID\n" +
				"	and s.IsDeleted=0\n" +
				"	and p.IsDeleted=0\n" +
				"  and pe.IsDeleted=0\n" +
				"	and pe.Type=1\n" ;
		if(StringUtils.isNotBlank(userId)){
			sql+="  and pe.UserGroupID='"+userId+"'";
		}
		sql+=	"order by pe.PermissionID";
		return sessionFactory.getCurrentSession().createSQLQuery(sql).setFirstResult((page-1)*limit).setMaxResults(limit).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getGroupRightMemberShip(String groupId, int page,
			int limit) {
		String sql="SELECT\n" +
				"	p.PermissionName,p.PermissionID,s.Type\n" +
				"	,c.CompanyName,d.DepartmentName,po.PositionName,s.ID\n" +
				"FROM\n" +
				"	OA_PermissionMembership s,\n" +
				"	OA_Permission p,\n" +
				"	OA_GroupDetail g,\n" +
				"	OA_Company c,\n" +
				"	OA_Department d,\n" +
				"	OA_Position po\n" +
				"WHERE\n" +
				"	s.PermissionID = p.PermissionID\n" +
				"and g.GroupID=s.UserGroupID\n" +
				"and d.DepartmentID=g.DepartmentID\n" +
				"and po.PositionID=g.PositionID\n" +
				"and c.CompanyID=g.CompanyID\n" +
				"and s.type=2\n" +
				"and g.IsDeleted=0\n" +
				"and s.IsDeleted=0\n" +
				"and p.IsDeleted=0\n" +
				"and c.IsDeleted=0\n" +
				"and po.IsDeleted=0\n" +
				"and d.IsDeleted=0\n" ;
		if(StringUtils.isNotBlank(groupId)){
			sql+="  and s.UserGroupID='"+groupId+"'";
		}
		sql+="order by PermissionID";
		return sessionFactory.getCurrentSession().createSQLQuery(sql).setFirstResult((page-1)*limit).setMaxResults(limit).list();
	}

	@Override
	public void createRightMemberShip(String keyId, String type,
			String rightId) {
		String sql = "insert into OA_PermissionMembership (PermissionID,UserGroupID,Type,IsDeleted,AddTime,UpdateTime)\n"
				+ "values(:permissionID,:userGroupID,:type,0,:addTime,:updateTime)";
		String now = DateUtil.getNowString();
		sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("permissionID",rightId).setParameter("userGroupID",keyId)
				.setParameter("addTime", now).setParameter("updateTime", now).setParameter("type", Integer.parseInt(type))
				.executeUpdate();
	}

	@Override
	public int getRightMemberShipCount(String userID) {
		String sql="SELECT\n" +
				"	count(*) \n" +
				"FROM\n" +
				"	OA_Permission p,\n" +
				"	OA_PermissionMembership pe,\n" +
				"	OA_Staff s\n" +
				"WHERE\n" +
				"	p.PermissionID = pe.PermissionID\n" +
				"	and s.UserID=pe.UserGroupID\n" +
				"	and s.IsDeleted=0\n" +
				"	and p.IsDeleted=0\n" +
				"  and pe.IsDeleted=0\n" +
				"	and pe.Type=1\n" ;
		if(StringUtils.isNotBlank(userID)){
			sql+="  and pe.UserGroupID='"+userID+"'";
		}
		return ((BigInteger)sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult()).intValue();
	}

	@Override
	public int getGroupRightMemberShip(String groupId) {
		String sql="SELECT\n" +
				"	count(*)  " +
				"FROM\n" +
				"	OA_PermissionMembership s,\n" +
				"	OA_Permission p,\n" +
				"	OA_GroupDetail g,\n" +
				"	OA_Company c,\n" +
				"	OA_Department d,\n" +
				"	OA_Position po\n" +
				"WHERE\n" +
				"	s.PermissionID = p.PermissionID\n" +
				"and g.GroupID=s.UserGroupID\n" +
				"and d.DepartmentID=g.DepartmentID\n" +
				"and po.PositionID=g.PositionID\n" +
				"and c.CompanyID=g.CompanyID\n" +
				"and s.type=2\n" +
				"and g.IsDeleted=0\n" +
				"and s.IsDeleted=0\n" +
				"and p.IsDeleted=0\n" +
				"and c.IsDeleted=0\n" +
				"and po.IsDeleted=0\n"+
				"and d.IsDeleted=0\n" ;
		if(StringUtils.isNotBlank(groupId)){
			sql+="  and s.UserGroupID='"+groupId+"'";
		}
		return ((BigInteger)sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult()).intValue();
	}

	@Override
	public void breakMemberShip(int id) {
		String sql="update OA_PermissionMembership s set s.IsDeleted=1 where s.ID=:ID ";
		sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("ID", id).executeUpdate();
	}

	@Override
	public String getGroupIdByKeys(String companyId, String departMentId,
			String positionId) {
		String sql="SELECT\n" +
				"	g.GroupID\n" +
				"FROM\n" +
				"	OA_GroupDetail g\n" +
				"WHERE\n" +
				"	g.CompanyID = :CompanyID\n" +
				"AND g.DepartmentID = :DepartmentID\n" +
				"AND g.PositionID = :PositionID\n" +
				"AND g.IsDeleted = 0";
		@SuppressWarnings("unchecked")
		List<String> result=sessionFactory.getCurrentSession().createSQLQuery(sql)
		.setParameter("CompanyID", companyId)
		.setParameter("DepartmentID", departMentId)
		.setParameter("PositionID", positionId)
		.list();
		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0)+"";
		}
		return null;
	}

	@Override
	public boolean checkHasThisRight(String requestUserId, Integer permissionID) {
		String sql = "select count(PermissionID) from OA_PermissionMembership ship "+
					 "where ship.PermissionID = "+permissionID+" and ship.IsDeleted=0 and ship.UserGroupID = '"+requestUserId+"'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	
}
