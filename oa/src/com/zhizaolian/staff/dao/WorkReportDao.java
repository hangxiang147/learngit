package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.WorkReportEntity;

public interface WorkReportDao {
	void save(WorkReportEntity workReportEntity);
	List<Object> findWorkreportListByUserID(String sql);
	Object getUniqueResult(String sql);
	List<WorkReportEntity> findWorkReportByDateAndUserID(String date,String userID);

}
