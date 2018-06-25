package com.zhizaolian.staff.service;

import com.zhizaolian.staff.vo.InterviewAnalysis;
import com.zhizaolian.staff.vo.InterviewPassInfo;

public interface InterviewAnalysisService {
	/**
	 * 按照岗位获取面试信息
	 * @return
	 */
	InterviewAnalysis getInterViewAnalysisByJob(String job, String beginDate, String endDate) throws Exception;
	/**
	 * 按照面试官获取面试信息
	 * @return
	 */
	InterviewAnalysis getInterViewAnalysisByInterviewer(String interviewer, String beginDate, String endDate) throws Exception;
	/**
	 * 获取录用的人数
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	InterviewAnalysis getOfferNums(String job, String beginDate, String endDate) throws Exception;
	
	InterviewPassInfo getInterviewPassByJob(boolean first, String beginDate, String endDate) throws Exception;
	
	InterviewPassInfo getInterViewOfferInfo(boolean first, String beginDate, String endDate) throws Exception;
	
	InterviewPassInfo getInterviewPassByInterviewer(String beginDate, String endDate) throws Exception;
	/**
	 * 判断date是不是当天，若是当天，时间就是当前时间，否则是23:59:59
	 * @param date
	 * @return
	 */
	String getFullDate(String date);
}
