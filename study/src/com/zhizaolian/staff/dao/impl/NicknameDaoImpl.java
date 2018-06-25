package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.NicknameDao;
import com.zhizaolian.staff.entity.NicknameEntity;

public class NicknameDaoImpl implements NicknameDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NicknameEntity> findNicknamesByType(int type) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from NicknameEntity nickname where nickname.isDeleted = 0 and nickname.type = :type";
		Query query = session.createQuery(hql);
		query.setParameter("type", type);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public NicknameEntity getNicknameByID(int nicknameID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from NicknameEntity nickname where nickname.nicknameID = :nicknameID and nickname.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("nicknameID", nicknameID);
		List<NicknameEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public void save(NicknameEntity nicknameEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(nicknameEntity);
	}
	
}
