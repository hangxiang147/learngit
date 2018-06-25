package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.NoticeActorDao;
import com.zhizaolian.staff.entity.NoticeActorEntity;

public class NoticeActorDaoImpl implements NoticeActorDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveNoticeActor(NoticeActorEntity noticeActorEntity){
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(noticeActorEntity);
	}

	@Override
	public void updateNoticeActor(Integer ntcID,String userID) {
		Session session=sessionFactory.getCurrentSession();
		String hql = "update NoticeActorEntity noticeActor set noticeActor.status =1 "
				+ "where noticeActor.noticeID = :noticeID and noticeActor.userID = :userID";
		Query query=session.createQuery(hql);
		query.setParameter("noticeID", ntcID);
		query.setParameter("userID", userID);
		query.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NoticeActorEntity> findNoticeActorsByNtcID(Integer ntcID) {
		Session session=sessionFactory.getCurrentSession();
		String hql = "from NoticeActorEntity noticeActor where noticeActor.noticeID = :noticeID";
		Query query=session.createQuery(hql);
		query.setParameter("noticeID", ntcID);
		return query.list();
	}
	

}
