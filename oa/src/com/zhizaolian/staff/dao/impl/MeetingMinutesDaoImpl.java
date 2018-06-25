package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.MeetingMinutesDao;
import com.zhizaolian.staff.entity.MeetingMinutesEntity;

public class MeetingMinutesDaoImpl implements MeetingMinutesDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer save(MeetingMinutesEntity meetingMinutesEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(meetingMinutesEntity);
		return meetingMinutesEntity.getMMID();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MeetingMinutesEntity> getMeetingMinutesByMeetingID(Integer meetingID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from MeetingMinutesEntity meeting where meeting.meetingID = :meetingID and meeting.isDeleted = 0";
		Query query=session.createQuery(hql);
		query.setParameter("meetingID", meetingID);
		List<MeetingMinutesEntity> list=query.list();
		
		return list;
	}
	

}
