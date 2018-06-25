package com.zhizaolian.staff.dao;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.VacationEntity;

public interface VacationDao {
	public VacationEntity findVacationsByvacationID(Integer vacationID);
	
	void deleteVacationsByVacationEntity(VacationEntity vacationEntity);

	void save(VacationEntity vacationEntity);
	
	List<VacationEntity> findVacationsByUserID(String userID, int page, int limit);
	
	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	
	int countVacationsByUserID(String userID);

	List<VacationEntity> findVacationsByRequestUserID(String requestUserID);

	/**
	 * 获取给定时间在请假区间内的所有请假申请
	 * @param time
	 * @param userID
	 * @return
	 */
	List<VacationEntity> findVacationsByDate(Date time, String userID);
	
	/**
	 * 获取所有请假时间区间包含在给定时间区间内的请假申请
	 * @param begin
	 * @param end
	 * @param userID
	 * @return
	 */
	List<VacationEntity> findVacationsByDates(Date begin, Date end, String userID);
	//查询所有请假
	List<Object> findVacationByhql(String hql,int page,int limit);
	
	VacationEntity getVacationByProcessInstanceID(String processInstanceID);
}
