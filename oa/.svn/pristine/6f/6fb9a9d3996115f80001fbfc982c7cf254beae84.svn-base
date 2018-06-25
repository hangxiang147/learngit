package com.zhizaolian.staff.timedTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.zhizaolian.staff.service.PerformanceService;

@Lazy(value=false)
public class SynPerformanceTask {
	private static Logger logger = Logger.getLogger(SynPerformanceTask.class);
	@Autowired
	private PerformanceService performanceService;
	/**
	 * 生成部门主管的待办
	 * （当月的指标值的填写任务以及上月的实际完成值的填写任务）
	 */
	public void generatePerformanceTask(){
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		try {
			performanceService.generateTargetValueTasks(year, month);
			Integer[] yearAndMonth = getLastMonth();
			performanceService.generateActualValueTasks(yearAndMonth[0], yearAndMonth[1]);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			e.printStackTrace();
		}
	}
	/**
	 * 每月自动生成个人考核内容的数据
	 */
	public void generateStaffCheckItemPerMonth(){
		try {
			performanceService.generateStaffCheckItemPerMonth();
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			e.printStackTrace();
		}
	}
	private Integer[] getLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date time = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(sdf.format(time));
		sdf = new SimpleDateFormat("MM");
		int month = Integer.parseInt(sdf.format(time));
		return new Integer[]{year, month};
	}

}
