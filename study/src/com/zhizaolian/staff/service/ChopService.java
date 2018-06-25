package com.zhizaolian.staff.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.entity.ContractDetailEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarveChopVo;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.ChopDestroyVo;
import com.zhizaolian.staff.vo.ChopUseLogVo;
import com.zhizaolian.staff.vo.ContractDetailVo;
import com.zhizaolian.staff.vo.IdBorrowVo;

public interface ChopService {
	void startChopBorrow(ChopBorrrowVo chopBorrrowVo, String processInstanceId);
	ListResult<Chop> getChopByName(String name,String id,int page,int limit);
	ChopBorrrowVo getChopByInstanceId(String instanceId);
	ListResult<ChopBorrrowVo> findChopBorrrowListByUserID(String userId, int page, int limit);
	Chop getChopById(String id);
	void delete(String id);
	void save(Chop chop);
	void update(Chop chop);
	void updateChopBorrowStatus(String  instanceId,Integer status);
	void updateRealBeginTime(String  instanceId);
	void updateRealEnd(String  instanceId);
	
	
	//身份证与公章流程类似  就写到一起了
	void startIdBorrow(IdBorrowVo idBorrowVo);
	void updateIdRealBeginTime(String instanceId);
	void updateIdRealEndTime(String instanceId);
	ListResult<IdBorrowVo> findIdBorrrowListByUserID(String userId, int page, int limit);
	ListResult<ChopBorrrowVo> findChopLogListByKeys(Map<String, String>  queryMap, int page, int limit);
	ListResult<IdBorrowVo> findIdBorrowListByKeys(Map<String, String> queryMap,int page,int limit);
	void updateIdBorrowStatus(String  instanceId,Integer status);
	
	//签合同的
	void startContract(ContractDetailVo contractDetailVo,File file,String fileName) throws Exception;
	void updateContract(ContractDetailEntity contractDetailEntity);
	ContractDetailEntity getContractByInstanceId(String instanceId);
	ListResult<ContractDetailVo> findContractListByUserID(String userId, int page, int limit);
	ListResult<ContractDetailVo> findContractListByKeys(Map<String, String> queryMap);
	void updateContractStatus(String instanceId,Integer status);
	
	List<String> getTaskIds(String tableName,String userId,String type,String No_code,String startTime,String endTime,int page ,int limit, String applyerId);
	int getTaskCount(String tableName,String userId,String type,String No_code,String startTime,String endTime, String applyerId);
	ListResult<ChopBorrrowVo> getChopBorrowVoLst(String name, int page, int limit);
	
	String getPInstanceId(String chopBorrowId);
	
	ListResult<ChopUseLogVo> getChopUseLog(String[] query, int limit, int page);
	
	void startChopDestroy(ChopDestroyVo chopDestroyVo);
	
	String getChopDestroyUserIdByInstanceId(String id);
	
	void updateChopDestroyProcessStatus(TaskResultEnum result, String processInstanceID);
	
	ListResult<ChopDestroyVo> findChopDestroyListByUserID(String id, Integer page, Integer limit);
	
	ChopDestroyVo getChopDestroyVoByProcessInstanceId(String processInstanceID);
	
	void startCarveChop(CarveChopVo carveChopVo);
	
	String getCarveChopUserIdByInstanceId(String id);
	
	void updateCarveChopProcessStatus(TaskResultEnum result, String processInstanceID);
	
	ListResult<CarveChopVo> findCarveChopListByUserID(String id, Integer page, Integer limit);
	
	CarveChopVo getCarveChopVoByProcessInstanceId(String processInstanceID);
	
	ListResult<ChopBorrrowVo> findContractChopList(String beginDate, String endDate, Integer limit, Integer page);
	
	InputStream exportContractChopDatas(String beginDate, String endDate) throws Exception;
	
}
