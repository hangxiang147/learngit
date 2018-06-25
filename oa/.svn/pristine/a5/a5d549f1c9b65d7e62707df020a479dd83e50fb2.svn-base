package com.zhizaolian.staff.service;

import java.util.Map;

import com.zhizaolian.staff.entity.CarpoolEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarpoolVo;

public interface CarpoolService {
	
	ListResult<CarpoolVo>  getCarpoolList(Map<String, String> params,int page,int size);
	CarpoolVo getCarpoolById(String id);
	void saveCarpool(CarpoolEntity carpoolEntity);
	void updateCarpool(CarpoolEntity carpoolEntity);
	void delete(String id);
}
