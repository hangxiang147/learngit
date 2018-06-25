package com.zhizaolian.staff.dao;

import java.util.Date;

import com.zhizaolian.staff.entity.AttendanceDetailEntity;

public interface AttendanceDetailDao {

	void save(AttendanceDetailEntity attendanceDetailEntity);
	
	AttendanceDetailEntity getAttendanceByDateUserID(Date attendanceDate, String userID);
	
	void delete(AttendanceDetailEntity attendanceDetailEntity);
	
}
