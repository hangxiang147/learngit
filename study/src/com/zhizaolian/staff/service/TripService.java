package com.zhizaolian.staff.service;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zhizaolian.staff.entity.TripEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.TripVo;

/**
 * @author Zhouk
 * 出差预约申请service
 */
public interface TripService {
	/**
	 * 启动一个出差预约流程
	 * @param tripVo
	 */
	void StartTrip    (TripVo tripVo);

	/**
	 *根据userId 查看 历史出差预约
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TripVo> findTripListByUserID(String userID, int page, int limit);
	
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	ListResult<TripEntity> getTripByKeys(Map<String, String> map,int page,int limit);
	HSSFWorkbook exportTrips(Map<String, String> map);
	
	
}
