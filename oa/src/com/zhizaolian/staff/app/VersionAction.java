package com.zhizaolian.staff.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.enums.APPResultEnum;
import com.zhizaolian.staff.enums.AppTypeEnum;
import com.zhizaolian.staff.service.AppService;

public class VersionAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AppService appService;
	
	public void getLastVersion() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try { 
			int type = Integer.parseInt(request.getParameter("type"));
			String version = appService.getLastVersionByType(AppTypeEnum.valueOf(type));
			resultMap.put("version", version);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
}
