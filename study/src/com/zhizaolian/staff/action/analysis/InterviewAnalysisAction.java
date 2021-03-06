package com.zhizaolian.staff.action.analysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.InterviewAnalysisService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.InterviewAnalysis;
import com.zhizaolian.staff.vo.InterviewPassInfo;

import common.Logger;
import lombok.Getter;
import lombok.Setter;
/**
 * 招聘信息分析
 * @author yxl
 * 
 */
public class InterviewAnalysisAction {
	private Logger logger = Logger.getLogger(InterviewAnalysisAction.class);
	@Getter
	private String selectedPanel;
	//按照岗位的面试通过率
	@Getter
	private List<String> passRateLst = new ArrayList<String>();
	@Getter
	private List<String> jobLst = new ArrayList<String>();
	//默认1，表示按照岗位显示通过率
	@Setter
	private String showPassBy = "1";
	//横坐标的值
	@Setter
	private String xAxisData;
	@Autowired
	private InterviewAnalysisService interviewAnalysisService;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private String beginDate;
	@Getter
	@Setter
	private String endDate;
	//横坐标显示所有数据的百分比
	@Getter
	private String percent;
	public String showPass(){
		try {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(date); 
			calendar.add(Calendar.MONTH, -1);
			//默认开始时间为前一个月
			beginDate = DateUtil.formateDate(calendar.getTime());
			endDate = DateUtil.formateDate(new Date());
			InterviewPassInfo interviewPassInfo = interviewAnalysisService.getInterviewPassByJob(true,
					beginDate, DateUtil.formateFullDate(new Date()));
			jobLst = interviewPassInfo.getJobLst();
			passRateLst = interviewPassInfo.getPassRateLstByJob();
			double percent = Constants.ECHART_SHOW_MAX/jobLst.size();
			this.percent = percent>=1.0 ? "100" : String.valueOf(percent*100).substring(0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取面试通过率信息失败："+e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		return "showPass";
	}
	public String showOffer(){
		try {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(date); 
			calendar.add(Calendar.MONTH, -1);
			//默认开始时间为前一个月
			beginDate = DateUtil.formateDate(calendar.getTime());
			endDate = DateUtil.formateDate(new Date());
			InterviewPassInfo interviewOfferInfo = interviewAnalysisService.getInterViewOfferInfo(true, beginDate,
					DateUtil.formateFullDate(new Date()));
			jobLst = interviewOfferInfo.getJobLst();
			passRateLst = interviewOfferInfo.getPassRateLstByJob();
			double percent = Constants.ECHART_SHOW_MAX/jobLst.size();
			this.percent = percent>=1.0 ? "100" : String.valueOf(percent*100).substring(0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取面试入职率率信息失败："+e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "showOffer";
	}
	public void changeShowPassBy(){
		Map<String, Object> resultMap =new HashMap<>();
		try {
			if("1".equals(showPassBy)){
				InterviewPassInfo interviewPassInfo = interviewAnalysisService.getInterviewPassByJob(false, beginDate,
						interviewAnalysisService.getFullDate(endDate));
				resultMap.put("showPassBy", Constants.JOB);
				resultMap.put("xAxisData", interviewPassInfo.getJobLst());
				resultMap.put("passRateLst", interviewPassInfo.getPassRateLstByJob());
				double percent = Constants.ECHART_SHOW_MAX/interviewPassInfo.getJobLst().size();
				resultMap.put("percent", percent>=1.0 ? "100" : String.valueOf(percent*100).substring(0, 2));
			}else{
				InterviewPassInfo interviewPassInfo = interviewAnalysisService.getInterviewPassByInterviewer(beginDate,
						interviewAnalysisService.getFullDate(endDate));
				resultMap.put("showPassBy", Constants.INTERVIEWER);
				resultMap.put("xAxisData", interviewPassInfo.getInterviewerLst());
				resultMap.put("interviewerIds", interviewPassInfo.getInterviewerIdLst());
				resultMap.put("passRateLst", interviewPassInfo.getPassRateLstByInterviewer());
				double percent = Constants.ECHART_SHOW_MAX/interviewPassInfo.getInterviewerLst().size();
				resultMap.put("percent", percent>=1.0 ? "100" : String.valueOf(percent*100).substring(0, 2));
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("errorMessage", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void changeShowOffer(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			InterviewPassInfo interviewOfferInfo = interviewAnalysisService.getInterViewOfferInfo(false, beginDate,
					interviewAnalysisService.getFullDate(endDate));
			resultMap.put("xAxisData", interviewOfferInfo.getJobLst());
			resultMap.put("passRateLst", interviewOfferInfo.getPassRateLstByJob());
			double percent = Constants.ECHART_SHOW_MAX/interviewOfferInfo.getJobLst().size();
			resultMap.put("percent", percent>=1.0 ? "100" : String.valueOf(percent*100).substring(0, 2));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("errorMessage", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void getPassAndNotPassNums(){
		Map<String, String> resultMap =new HashMap<>();
		try {
			InterviewAnalysis interviewPassInfo = null;
			if("1".equals(showPassBy)){
				interviewPassInfo = interviewAnalysisService.getInterViewAnalysisByJob(xAxisData, beginDate,
						interviewAnalysisService.getFullDate(endDate));
			}else{
				interviewPassInfo = interviewAnalysisService.getInterViewAnalysisByInterviewer(xAxisData, beginDate,
						interviewAnalysisService.getFullDate(endDate));
			}
			resultMap.put("pass", interviewPassInfo.getPass());
			resultMap.put("failed", interviewPassInfo.getFailed());
			resultMap.put("notCome", interviewPassInfo.getNotCome());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void getOffers(){
		Map<String, String> resultMap = new HashMap<>();
		try {
			InterviewAnalysis interviewAnalysis = interviewAnalysisService.getOfferNums(xAxisData, beginDate,
					interviewAnalysisService.getFullDate(endDate));
			resultMap.put("offers", interviewAnalysis.getOffer());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	private void printByJson(Object obj) {
		PrintWriter out = null;
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setCharacterEncoding("utf-8");
		String json = null;
		try {
			out = httpServletResponse.getWriter();
			json = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
		} catch (IOException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		out.print(json);
		out.close();
	}
}
