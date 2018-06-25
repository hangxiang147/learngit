package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.ContractEntity;

public interface ContractDao {
	
	Integer save(ContractEntity contractEntity);
	
	ContractEntity getContractEntityByContractID(Integer contractID);
	
	List<ContractEntity> findContractsByPartyBStatus(String userID, int status);

}
