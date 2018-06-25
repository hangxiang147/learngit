package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.MeetingActorDao;
import com.zhizaolian.staff.entity.MeetingActorEntity;

public class MeetingActorDaoImpl implements MeetingActorDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(MeetingActorEntity meetingActorEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(meetingActorEntity);
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MeetingActorEntity> findMeetingActorByMeetingID(Integer meetingID,Integer actorType ) {
		Session session=sessionFactory.getCurrentSession();
		String hql = "from MeetingActorEntity meetingActor where meetingActor.meetingID = :meetingID "
				+ "and meetingActor.addType = 2 and meetingActor.actorType = :actorType";
		Query query=session.createQuery(hql);
		query.setParameter("meetingID", meetingID);
		query.setParameter("actorType", actorType);
		return query.list();
	}

}
