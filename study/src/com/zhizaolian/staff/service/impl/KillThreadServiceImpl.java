package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.service.KillThreadService;
import com.zhizaolian.staff.utils.DateUtil;
@Service(value="killThreadService")
public class KillThreadServiceImpl implements KillThreadService {
	@Autowired
	private BaseDao baseDao;
	@Override
	public void killThread() throws Exception{
		String sql = "select trx_started,  trx_mysql_thread_id from information_schema.innodb_trx";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] objArray = (Object[])obj;
			String startedTime = (String)objArray[0];
			String threadId = (String)objArray[1];
			//已启动毫秒数
			long differTime = (new Date()).getTime() - DateUtil.getFullDate(startedTime).getTime();
			if(differTime>(30*60*1000)){
				sql = "kill "+threadId;
				baseDao.excuteSql(sql);
			}
		}
	}

}
