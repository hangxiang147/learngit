package com.zhizaolian.staff.timedTask;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zhizaolian.staff.service.KillThreadService;

import common.Logger;

/**
 * 检查长时间等待锁死的线程，并杀掉，
 * 半个小时检测一次，启动时间超过5分钟的线程杀掉
 * @author Administrator
 *
 */
@Lazy(value=false)
public class CheckLongTimeThread {
	@Autowired
	private KillThreadService killThreadService;
	private Logger logger = Logger.getLogger(CheckLongTimeThread.class);
	public void killThread(){
		try {
			killThreadService.killThread();
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			e.printStackTrace();
		}
	}
}
