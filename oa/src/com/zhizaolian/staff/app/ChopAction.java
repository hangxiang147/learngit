package com.zhizaolian.staff.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChopBorrrowVo;

import net.sf.json.JSONObject;

public class ChopAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	@Autowired
	private ChopService chopService;
	public void getChops(){
		ListResult<Chop> chops = chopService.getChopByName(null, null, 1, Integer.MAX_VALUE);
		printByJson(chops);
	}
	
	
	public void startChop(){
		String chopVo = this.request.getParameter("chopVo");
		Map<String, String> returnMap=new HashMap<>();
		try{
			ChopBorrrowVo chopBorrrowVo = (ChopBorrrowVo) JSONObject
					.toBean(JSONObject.fromObject(chopVo), ChopBorrrowVo.class);
			try{
				chopBorrrowVo.setStartTime(DateUtil.getFullDate(""+JSONObject.fromObject(chopVo).get("StartTime")));
				chopBorrrowVo.setEndTime(DateUtil.getFullDate(""+JSONObject.fromObject(chopVo).get("EndTime")));
			}catch(Exception ignore){}
			chopService.startChopBorrow(chopBorrrowVo, null);
			returnMap.put("success", "true");
		}catch(Exception e){
			returnMap.put("success", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(returnMap);
		
	}
}
