package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.entity.WeekReportEntity;
import com.zhizaolian.staff.entity.WeekReporterEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.NextWeekWorkPlan;
import com.zhizaolian.staff.vo.RiskVo;
import com.zhizaolian.staff.vo.ThisWeekWorkVo;

public interface WeekWorkReportService {

	boolean checkCanWriteReport(String id) throws Exception;

	int saveWeekReport(WeekReportEntity weekReport, ThisWeekWorkVo thisWeekWorkVo, RiskVo riskVo,
			NextWeekWorkPlan nextWeekWorkPlan, String weekWorkSummary) throws Exception;

	WeekReportEntity getWeekReportDetail(String weekReportId);

	List<WeekReportEntity> getWeekReportList(String id, String beginDate, String endDate) throws Exception;

	boolean checkRepeatedReport(String userId) throws Exception;

	ListResult<WeekReportEntity> findWeekReportListByConditions(String[] conditions, Integer page, Integer limit) throws Exception;

	List<Object> findNeedWeekReportPersons(String userName, String companyId);

	void addWeekReporter(WeekReporterEntity weekReporter);

	void deleteWeekReporter(String reporterId);

	List<WeekReportEntity> findWeekReportsByDate(Integer companyID, String beginDate, String endDate) throws Exception;

}
