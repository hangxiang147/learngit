package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.PositionAlterationDao;
import com.zhizaolian.staff.entity.PositionAlterationEntity;
import com.zhizaolian.staff.utils.ListResult;

public class PositionAlterationDaoImpl implements PositionAlterationDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(PositionAlterationEntity positionAlterationEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(positionAlterationEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ListResult<PositionAlterationEntity> positionHistory(String userID, Integer page, Integer limit) {
		//得到数据集合
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PositionAlterationEntity position where position.userID = :userID and position.isDeleted = 0 ORDER BY position.addTime DESC";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<PositionAlterationEntity> result = query.list();
		//得到符合条件的总的记录数
		String hqlCount = "SELECT COUNT(*) FROM PositionAlterationEntity position WHERE position.userID = :userID and position.isDeleted = 0";
		Query queryCount = session.createQuery(hqlCount);
		queryCount.setParameter("userID", userID);
		Integer count = ((Number)queryCount.uniqueResult()).intValue();
		if (result.size() > 0) {
			return (new ListResult<PositionAlterationEntity>(result,count));
		}
		return null;
	}
}
