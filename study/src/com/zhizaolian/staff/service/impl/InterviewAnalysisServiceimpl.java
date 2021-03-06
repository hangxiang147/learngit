package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.service.InterviewAnalysisService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.vo.InterviewAnalysis;
import com.zhizaolian.staff.vo.InterviewPassInfo;

public class InterviewAnalysisServiceimpl implements InterviewAnalysisService{
	@Autowired
	private BaseDao baseDao;
	@Override
	public InterviewAnalysis getInterViewAnalysisByJob(String job, String beginDate, String endDate) throws Exception{
		String sql = "select SUM(type=2||type=4||type=5) as pass, SUM(type=3) as failed, SUM(type=0) as notCome,"
				+ " RealPostName from(select RealPostName,c.type from OA_Vitae a inner join" 
				+ " OA_VitaeSign b on a.Id = b.vitaeId inner join OA_VitaeResult c on b.id ="
				+ " c.vitaeSignId where a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"' and RealPostName = '"+EscapeUtil.decodeSpecialChars(job)+"')d;";
		List<Object> result = baseDao.findBySql(sql);
		InterviewAnalysis interviewAnalysis = new InterviewAnalysis();
		for(Object object : result){
			Object[] objs = (Object[]) object;
			interviewAnalysis.setPass(objs[0]+"");
			interviewAnalysis.setFailed(objs[1]+"");
			interviewAnalysis.setNotCome(objs[2]+"");
		}
		return interviewAnalysis;
	}

	@Override
	public InterviewAnalysis getInterViewAnalysisByInterviewer(String interviewer, String beginDate, String endDate) throws Exception{
		String sql = "select SUM(type=2||type=4||type=5) as pass, SUM(type=3) as failed, SUM(type=0) as notCome from"
				+ "(select c.type from OA_Vitae a inner join "
				+ "OA_VitaeSign b on a.id = b.vitaeId inner join OA_VitaeResult c on b.id = c.vitaeSignId where "
				+ "a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"' and LOCATE('"+interviewer+"',a.RealSubjectPersonIds)>0)d";
		List<Object> result = baseDao.findBySql(sql);
		InterviewAnalysis interviewAnalysis = new InterviewAnalysis();
		for(Object object : result){
			Object[] objs = (Object[]) object;
			interviewAnalysis.setPass(objs[0]+"");
			interviewAnalysis.setFailed(objs[1]+"");
			interviewAnalysis.setNotCome(objs[2]+"");
		}
		return interviewAnalysis;
	}

	@Override
	public InterviewPassInfo getInterviewPassByJob(boolean first, String beginDate, String endDate) throws Exception{
		String sql = "select SUBSTRING_INDEX(SUM(type=2||type=4||type=5)/SUM(type=0||type=1||type=2||type=3||type=4||type=5)*100,'.',1) "
				+ "as passRate, RealPostName from(select RealPostName,c.type from OA_Vitae a inner join "
				+ "OA_VitaeSign b on a.Id = b.vitaeId inner join OA_VitaeResult c on b.id = c.vitaeSignId "
				+ "where a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"')d group by realPostName";
		List<Object> result = baseDao.findBySql(sql);
		InterviewPassInfo interviewPassInfo = new InterviewPassInfo();
		List<String> jobLst = new ArrayList<String>();
		List<String> passRateLst = new ArrayList<String>();
		for(Object object: result){
			Object[] objs = (Object[]) object;
			passRateLst.add((String)objs[0]);
			if(first){
				//为了页面刚加载，el表达式中的字段显示为字符串
				jobLst.add("'"+(String)objs[1]+"'");
			}else{
				jobLst.add((String)objs[1]);
			}
		}
		interviewPassInfo.setJobLst(jobLst);
		interviewPassInfo.setPassRateLstByJob(passRateLst);
		return interviewPassInfo;
	}

	@Override
	public InterviewPassInfo getInterviewPassByInterviewer(String beginDate, String endDate) throws Exception{
		String sql = "select SUBSTRING_INDEX(SUM(type=2||type=4||type=5)/SUM(type=0||type=1||type=2||type=3||type=4||type=5)*100,'.',1)"
				+ " as passRate, (select StaffName from OA_Staff where UserID = interviewerId)as interviewer, interviewerId"
				+ " from(select interviewerId,c.type from (select b.id_ as interviewerId, result, a.id as vitaeId,updateTime"
				+ " from OA_Vitae a INNER JOIN ACT_ID_USER b on LOCATE(b.id_,a.RealSubjectPersonIds)>0) a inner join" 
				+ " OA_VitaeSign b on a.vitaeId = b.vitaeId inner join OA_VitaeResult c on b.id = c.vitaeSignId"
				+ " where a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"')d group by interviewerId;";
		List<Object> result = baseDao.findBySql(sql);
		InterviewPassInfo interviewPassInfo = new InterviewPassInfo();
		List<String> interviewerLst = new ArrayList<>();
		List<String> interviewerIdLst = new ArrayList<>();
		List<String> passRateLst = new ArrayList<>();
		for(Object object: result){
			Object[] objs = (Object[]) object;
			passRateLst.add(objs[0]+"");
			interviewerLst.add(objs[1]+"");
			interviewerIdLst.add(objs[2]+"");
		}
		interviewPassInfo.setInterviewerLst(interviewerLst);
		interviewPassInfo.setInterviewerIdLst(interviewerIdLst);
		interviewPassInfo.setPassRateLstByInterviewer(passRateLst);
		return interviewPassInfo;
	}

	@Override
	public InterviewAnalysis getOfferNums(String job, String beginDate, String endDate) throws Exception {
		String sql = "select SUM(type=5), RealPostName from(select RealPostName,c.type from OA_Vitae a inner join" 
				+ " OA_VitaeSign b on a.Id = b.vitaeId inner join OA_VitaeResult c on b.id ="
				+ " c.vitaeSignId where a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"' and RealPostName = '"+EscapeUtil.decodeSpecialChars(job)+"')d;";
		List<Object> result = baseDao.findBySql(sql);
		InterviewAnalysis interviewAnalysis = new InterviewAnalysis();
		for(Object object : result){
			Object[] objs = (Object[]) object;
			interviewAnalysis.setOffer(objs[0]+"");
		}
		return interviewAnalysis;
	}

	@Override
	public InterviewPassInfo getInterViewOfferInfo(boolean first, String beginDate, String endDate) throws Exception {
		String sql = "select SUBSTRING_INDEX(SUM(type=5)/SUM(type=0||type=1||type=2||type=3||type=4||type=5)*100,'.',1) "
				+ "as passRate, RealPostName from(select RealPostName,c.type from OA_Vitae a inner join "
				+ "OA_VitaeSign b on a.Id = b.vitaeId inner join OA_VitaeResult c on b.id = c.vitaeSignId "
				+ "where a.Result = '19' and a.updateTime BETWEEN '"+EscapeUtil.decodeSpecialChars(beginDate)
				+"' and '"+EscapeUtil.decodeSpecialChars(endDate)+"')d group by realPostName";
		List<Object> result = baseDao.findBySql(sql);
		InterviewPassInfo interviewPassInfo = new InterviewPassInfo();
		List<String> jobLst = new ArrayList<String>();
		List<String> passRateLst = new ArrayList<String>();
		for(Object object: result){
			Object[] objs = (Object[]) object;
			passRateLst.add((String)objs[0]);
			if(first){
				//为了页面刚加载，el表达式中的字段显示为字符串
				jobLst.add("'"+(String)objs[1]+"'");
			}else{
				jobLst.add((String)objs[1]);
			}
		}
		interviewPassInfo.setJobLst(jobLst);
		interviewPassInfo.setPassRateLstByJob(passRateLst);
		return interviewPassInfo;
	}

	@Override
	public String getFullDate(String date) {
		Date currentDate = new Date();//当前时间
		Date compareDate = DateUtil.parseDay(date);
		if(DateUtils.isSameDay(currentDate, compareDate)){
			return DateUtil.formateFullDate(currentDate);
		}else{
			return date+" 23:59:59";
		}
	}
}
