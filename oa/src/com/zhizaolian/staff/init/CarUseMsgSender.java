package com.zhizaolian.staff.init;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zhizaolian.staff.dao.CarUseDao;
import com.zhizaolian.staff.utils.ShortMsgSender;

public class CarUseMsgSender implements Job {
//	private final static String DEFAULT_DRIVER_NAME="黄平";
	private final static String DEFAULT_DRIVER_MOBILE="18252519111";
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CarUseDao  chopDao=(CarUseDao) context.getJobDetail().getJobDataMap().get("dao");
		List<String> list=chopDao.getTodayDetail();
		if(CollectionUtils.isNotEmpty(list)){
			String path=(String)context.getJobDetail().getJobDataMap().get("basic_path_")+"WEB-INF"+File.separator+"init.properties";
//			String dirverName=DEFAULT_DRIVER_NAME;
			String dirverMobile=DEFAULT_DRIVER_MOBILE;
			try{
				Properties properties = new Properties();
				properties.load(new FileInputStream(path));
//				dirverName=(String) properties.get("caruse.driver_name");
				dirverMobile=(String) properties.get("caruse.driver_mobile");
			}catch(Exception ignore){}
			StringBuilder sb=new StringBuilder();
			for(String str:list){
				sb.append(str+"，");
			}
			sb.deleteCharAt(sb.length()-1);			
			ShortMsgSender.getInstance().send(dirverMobile, "【智造链】今日车辆预约人员："+sb.toString());
		}
	}

}
