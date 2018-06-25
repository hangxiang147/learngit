package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.StaffInfoAlterationDao;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffInfoAlterationEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.StaffVO;
/**
 * dao接口实现类
 * @author wjp
 *
 */
public class StaffInfoAlterationDaoImpl implements StaffInfoAlterationDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void save(StaffInfoAlterationEntity staffInfoRecordEntity) {
		
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(staffInfoRecordEntity);
		
	}
	
	@SuppressWarnings("unchecked")
	public ListResult<StaffInfoAlterationEntity> gradeHistory(String userID,int page, int limit) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffInfoAlterationEntity staff where staff.userID = :userID and staff.gradeBefore != staff.gradeAfter and staff.isDeleted = 0 ORDER BY staff.addTime DESC";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<StaffInfoAlterationEntity> result = query.list();
		String hqlCount = "SELECT COUNT(*) FROM StaffInfoAlterationEntity staff WHERE staff.userID = :userID and staff.gradeBefore != staff.gradeAfter and staff.isDeleted = 0";
		Query queryCount = session.createQuery(hqlCount);
		queryCount.setParameter("userID", userID);
		Integer count = ((Number)queryCount.uniqueResult()).intValue();
		if (result.size() > 0) {
			return (new ListResult<StaffInfoAlterationEntity>(result,count));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ListResult<StaffInfoAlterationEntity> salaryHistory(String userID,Integer page, Integer limit) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffInfoAlterationEntity staff where staff.userID = :userID and staff.salaryBefore != staff.salaryAfter and staff.isDeleted = 0 ORDER BY staff.addTime DESC";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<StaffInfoAlterationEntity> result = query.list();
		String hqlCount = "SELECT COUNT(*) FROM StaffInfoAlterationEntity staff WHERE staff.userID = :userID and staff.salaryBefore != staff.salaryAfter and staff.isDeleted = 0";
		Query queryCount = session.createQuery(hqlCount);
		queryCount.setParameter("userID", userID);
		Integer count = ((Number)queryCount.uniqueResult()).intValue();
		if (result.size() > 0) {
			return (new ListResult<StaffInfoAlterationEntity>(result,count));
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<StaffEntity> findStaffByGradeAndName(StaffVO staffVO, Integer page, Integer limit) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.isDeleted = 0";
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(staffVO.getLastName())) {
			whereSql.append(" and staff.staffName like '%"+staffVO.getLastName()+"%'");
		}
		if (staffVO.getGradeID()!= null) {
			whereSql.append(" and staff.gradeID = "+staffVO.getGradeID());
		}
		hql = hql+whereSql.toString();
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<StaffEntity> result = query.list();
		String hqlCount = "SELECT COUNT(*) FROM StaffEntity staff WHERE staff.isDeleted = 0";
		hqlCount = hqlCount+whereSql.toString();
		Query queryCount = session.createQuery(hqlCount);
		Integer count = ((Number)queryCount.uniqueResult()).intValue();
		if (result.size() > 0) {
			return (new ListResult<StaffEntity>(result,count));
		}
		return null;
	}

	
}
