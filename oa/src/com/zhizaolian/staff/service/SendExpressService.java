package com.zhizaolian.staff.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SendExpressVO;

public interface SendExpressService {
	void saveSendExpress(SendExpressVO sendExpressVO);
    
	ListResult<SendExpressVO> findSendExpressVOsBySendExpressVO(SendExpressVO sendExpressVO,int page,int limit);
	
	HSSFWorkbook exportSendExpressList(SendExpressVO sendExpressVO);
}
