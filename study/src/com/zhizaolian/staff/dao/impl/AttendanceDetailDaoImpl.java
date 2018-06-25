package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AttendanceDetailDao;
import com.zhizaolian.staff.entity.AttendanceDetailEntity;

public class AttendanceDetailDaoImpl implements AttendanceDetailDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(AttendanceDetailEntity attendanceDetailEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(attendanceDetailEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AttendanceDetailEntity getAttendanceByDateUserID(Date attendanceDate, String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from AttendanceDetailEntity attendance where attendance.userID = :userID and attendance.attendanceDate = :attendanceDate and attendance.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("attendanceDate", attendanceDate);
		query.setParameter("userID", userID);
		List<AttendanceDetailEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void delete(AttendanceDetailEntity attendanceDetailEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(attendanceDetailEntity);
	}
	
	
}
