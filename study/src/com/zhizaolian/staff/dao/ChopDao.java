package com.zhizaolian.staff.dao;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.entity.ChopBorrow;
import com.zhizaolian.staff.entity.ContractDetailEntity;
import com.zhizaolian.staff.entity.IdBorrowEntity;

public interface ChopDao {
	int getChopListCount(String name,Integer id);
	List<Chop> getChopList(String name ,Integer id,int page,int limit);
	List<ChopBorrow> getChopBorrowByUserId(String userId,int page,int limit);
	int getChopBorrowCountByUserId(String userId);
	ChopBorrow getChopBorrowByInsctnceId(String intanceId);
	void saveChop(Chop chop);
	void updateChop(Chop chop);
	void deleteChop(String chopId);
	void saveChopBorrow(ChopBorrow chopBorrow);
	void updateChopBorrow(ChopBorrow chopBorrow);
	void updateChop(ChopBorrow chopBorrow);
	void updateChopBorrowStatus(String instanceId, Integer status) ;
	List<ChopBorrow> findChopLogListByKeys(Map<String, String>  queryMap, int page, int limit);
	int findChopLogCountByKeys(Map<String, String>  queryMap);
	
	void saveIDBorrow(IdBorrowEntity idBorrowEntity);
	void updateIdBorrow(IdBorrowEntity idBorrowEntity);
	List<IdBorrowEntity> getIdBorrowByUserId(String userId,int page,int limit);
	List<IdBorrowEntity> getIdBorrowByKeys(Map<String, String> queryMap,int page,int limit);
	int getIdBorrowCountByKeys(Map<String, String> queryMap);
	int getIdBorrowCountByUserId(String userId);
	void updateIdBorrowStatus(String instanceId, Integer status);
	IdBorrowEntity	getIdBorrowByInstanceId(String instanceId);
	
	
	void saveContract(ContractDetailEntity contractDetailEntity);
	void updateContract(ContractDetailEntity contractDetailEntity);
	List<ContractDetailEntity> getContractByUserId(String userId,int page,int limit);
	List<ContractDetailEntity> getContractByKeys(Map<String, String> queryMap,int page,int limit);
	int getContractCountByKeys(Map<String, String> queryMap);
	int getContractCountByUserId(String userId);
	void updateContractStatus(String instanceId, Integer status);
	ContractDetailEntity	getContractByInstanceId(String instanceId);

	List<String> getTaskIds(String tableName,String userId,String type,String No_code,String startTime,String endTime,int page ,int limit, String applyerId);
	int getTaskCount(String tableName,String userId,String type,String No_code,String startTime,String endTime, String applyerId);

}
