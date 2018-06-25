package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.MeetingDao;
import com.zhizaolian.staff.entity.MeetingEntity;

public class MeetingDaoImpl implements MeetingDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer saveMetting(MeetingEntity meetingEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(meetingEntity);
		return meetingEntity.getMeetingID();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public MeetingEntity getMeetingByMeetingID(Integer meetingID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from MeetingEntity meeting where meeting.meetingID = :meetingID and meeting.isDeleted = 0";
		Query query=session.createQuery(hql);
		query.setParameter("meetingID", meetingID);
		List<MeetingEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
