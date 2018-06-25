package com.zhizaolian.staff.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.WorkExperienceDao;
import com.zhizaolian.staff.entity.WorkExperienceEntity;

public class WorkExperienceDaoImpl implements WorkExperienceDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(WorkExperienceEntity workExperienceEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(workExperienceEntity);
	}
}
