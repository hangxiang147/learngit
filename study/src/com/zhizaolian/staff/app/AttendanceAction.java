package com.zhizaolian.staff.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.enums.APPResultEnum;
import com.zhizaolian.staff.service.AttendanceService;

import net.sf.json.JSONObject;

public class AttendanceAction extends BaseAction{
	@Autowired
	private AttendanceService attendanceService;
	private static final long serialVersionUID = 1L;
	/**
	 * 获取上下班时间
	 */ 
	public void getWorkHour(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userId = request.getParameter("userId");
			Map<String, String> workHour = attendanceService.getWorkHour(userId);
			resultMap.put("workHour", workHour);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void getVacationDaysAndHours(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String jsonStr = getBodyString();
			JSONObject jsonOject = JSONObject.fromObject(jsonStr);
			String[] dayAndHours = attendanceService.getVacationDaysAndHours(jsonOject.getString("startime"),
					jsonOject.getString("endtime"), jsonOject.getString("uuid"));
			resultMap.put("day", StringUtils.isBlank(dayAndHours[0])?0:dayAndHours[0]);
			resultMap.put("hour", StringUtils.isBlank(dayAndHours[1])?0:dayAndHours[1]);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
}
