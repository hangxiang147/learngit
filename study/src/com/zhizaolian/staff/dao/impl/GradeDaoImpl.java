package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.GradeDao;
import com.zhizaolian.staff.entity.GradeEntity;

public class GradeDaoImpl implements GradeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<GradeEntity> findAllGrades() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from GradeEntity grade where grade.isDeleted = 0";
		return session.createQuery(hql).list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public GradeEntity getGradeByGradeID(int gradeID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from GradeEntity grade where grade.gradeID = :gradeID and grade.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("gradeID", gradeID);
		List<GradeEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
