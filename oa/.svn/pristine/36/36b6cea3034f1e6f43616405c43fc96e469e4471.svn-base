package com.zhizaolian.staff.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
import com.zhizaolian.staff.vo.WorkReportVO;

public interface WorkReportService {
	void saveWorkReport(WorkReportVO workReportVO);
	
	ListResult<WorkReportDetailVO> findWorkReportListByUserID(WorkReportDetailVO workReportDetailVO,int page,int limit);
	
	List<WorkReportVO> findWorkReportsByDate(Integer companyID,String date) throws Exception;
	
	List<WorkReportVO> findStatisticsByMonth(Integer companyID,Date date) throws Exception;
	
	List<WorkReportVO> findWorkReportByDateAndUserID(String date,String userID);
	
	Map<String, WorkReportVO> findStatisticsByDate(Integer companyID, String beginDate, String endDate) throws Exception;

}
