package com.zhizaolian.staff.dao.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;

public class StaffDaoImpl implements StaffDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public int save(StaffEntity staffEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(staffEntity);
		return staffEntity.getStaffID();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public StaffEntity getStaffByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.userID = :userID and staff.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		List<StaffEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public StaffEntity getStaffByStaffID(int staffID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.staffID = :staffID and staff.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("staffID", staffID);
		List<StaffEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public StaffEntity getLatestStaff() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.staffID =(select max(sf.staffID) from StaffEntity sf where sf.isDeleted=0)";
		List<StaffEntity> result = session.createQuery(hql).list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<StaffEntity> findStaffPageListByStatusList(List<Integer> statusList, int page, int limit) {
		if (CollectionUtils.isEmpty(statusList)) {
			return Collections.emptyList();
		}
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.status in (:statusList) and staff.isDeleted = 0 order by entryDate";
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		query.setParameterList("statusList", statusList);
		return query.list();
	}
	
	@Override
	public int countStaffByStatusList(List<Integer> statusList) {
		if (CollectionUtils.isEmpty(statusList)) {
			return 0;
		}
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select count(*) as count from OA_Staff os where os.status in (:statusList) and os.isDeleted = 0";
		Query query = session.createSQLQuery(sql).addScalar("count", StandardBasicTypes.INTEGER);
		query.setParameterList("statusList", statusList);
		return (Integer) query.uniqueResult();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<StaffEntity> findStaffByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.staffName like :name and staff.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("name", "%"+name+"%");
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffEntity> findStaffByName(String name, int limit) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.staffName like :name and staff.isDeleted = 0 ";
		Query query = session.createQuery(hql);
		query.setParameter("name", "%"+name+"%");
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StaffEntity> findStaffByNameAndStatus(String name, Integer positionCategory) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.staffName like :name and staff.positionCategory= :positionCategory and staff.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("positionCategory", positionCategory);
		query.setParameter("name", "%"+name+"%");
		return query.list();
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<Object> findStaffList(String hql, String hqlCount, int limit, int page) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createSQLQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<Object> list=query.list();
		int totalCount=((Number)session.createSQLQuery(hqlCount).uniqueResult()).intValue();
		return new ListResult<Object>(list,totalCount);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public StaffEntity getStaffBytelephone(String telephone) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.telephone = :telephone and staff.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("telephone", telephone);
		List<StaffEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public String getUsefulValidateKeyByUserId(String userId,Boolean isRecent) {
		String sql=null;
		if(isRecent){
			sql="select validateKey from OA_RestMsg where UserId=:UserId  and effectiveTime>NOW() order by effectiveTime desc  ";
		}else{
			sql="select validateKey from OA_RestMsg where UserId=:UserId  order by effectiveTime desc  ";
		}
		Query query =sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("UserId", userId);
		@SuppressWarnings("unchecked")
		List<String> resultList=query.list();
		return resultList.size()>0?resultList.get(0):null;
	}

	@Override
	public String getEmployeeNameByUsrId(String userId) {
		String sql="Select StaffName from OA_Staff where userId=:userId";
		return (String) sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("userId", userId).uniqueResult();
	}

	@Override
	public void insertRestValidateKey(String userId, String validateKey) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTime(new Date());
		//有效时间 1小时
		calendar.add(Calendar.HOUR, 1);
		String nextHourTimeStr=DateUtil.formateFullDate(calendar.getTime());
		String sql=" insert into OA_RestMsg set UserId=:UserId ,ValidateKey=:ValidateKey,EffectiveTime=:EffectiveTime ";
		Query query =sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setParameter("UserId", userId)
				.setParameter("ValidateKey", validateKey)
				.setParameter("EffectiveTime", nextHourTimeStr);
		query.executeUpdate();
	}

	@Override
	public String getStaffNum(String userId) {
		String sql = "select FIRST_ from act_id_user where ID_='"+userId+"'";
		return (String) baseDao.getUniqueResult(sql);
	}

	@Override
	public String getstaffUserIdByStaffNum(String staffNum) {
		String sql = "select id_ from act_id_user where FIRST_='"+staffNum+"'";
		return (String) baseDao.getUniqueResult(sql);
	}
	
}
