package com.zhizaolian.staff.service;

import java.util.Map;

import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarUseVo;


public interface CarUseService {
	void startCarUse(CarUseVo carUseVo);
	void updateCarUseStatus(String instanceId,Integer status);
	ListResult<CarUseVo> getCarUseByKeys(Map<String, String> params,int page,int limit);
}
