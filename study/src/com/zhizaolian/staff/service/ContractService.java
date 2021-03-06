package com.zhizaolian.staff.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.ContractBorrowEntity;
import com.zhizaolian.staff.entity.ContractEntity;
import com.zhizaolian.staff.entity.ContractManageEntity;
import com.zhizaolian.staff.enums.ContractStatusEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChangeContractVo;
import com.zhizaolian.staff.vo.ContractBorrowVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.ContractVO;

public interface ContractService {
	
	Integer saveContract(ContractVO contractVO);
	
	ListResult<ContractVO> findContractByContractVO(ContractVO contractVO, int page, int limit);
	
	ContractVO getContractVOBycontractID(Integer contractID);

	void updateContract(ContractVO contractVO);
	
	void deleteContract(int contractID);
	
	void expiredContractByContractID(int contractID);
	
	void saveRenewContract(ContractVO contractVO, File contract, File signat, String contractFileName, String signatFileName);
	
	List<ContractVO> findContractsByPartyBStatus(String userID, ContractStatusEnum status);

	ListResult<ContractManageEntity> getContractLst(String name, int limit, int page);

	ContractManageEntity getContract(String contractId);

	void updateContract(ContractManageEntity contract, File[] attachment, String[] attachmentFileName) throws Exception;

	void saveContract(ContractManageEntity contract, File[] attachment, String[] attachmentFileName) throws Exception;

	List<String> getAttachmentNames(String contractId);

	void deleteContract(String contractId);

	boolean checkIsExist(String contractID, String id);

	void startContractBorrow(ContractBorrowVo contractBorrowVo);

	ListResult<ContractBorrowVo> findContractBorrowLstByUserID(String id, Integer page, Integer limit);

	void updateProcessStatus(TaskResultEnum result, String processInstanceID);

	void updateRealBeginTime(String intanceId);

	void updateRealEndTime(String intanceId);

	ListResult<ContractBorrowEntity> getContractBorrowLst(String[] qurey, int limit, int page);

	void startContractSign(ContractSignVo contractSignVo, File[] attachment, String[] attachmentFileName, File[] attachment2, String[] attachment2FileName) throws Exception;

	String getUserIdByInstanceId(String id);

	void updateContractStatus(TaskResultEnum result, String processInstanceID);

	void updateContractSign(ContractSignVo contractSignVo);

	ListResult<ContractSignVo> findContractSignLstByUserId(String id, Integer page, Integer limit);

	ContractSignVo getContractSignVoByProcessInstanceId(String processInstanceID);

	void startChangeContract(ChangeContractVo changeContractVo, File[] attachment, String[] attachmentFileName) throws Exception;

	ListResult<ChangeContractVo> findChangeContractListByUserID(String id, Integer page, Integer limit);

	String getChangeContractUserIdByInstanceId(String id);

	void updateChangeContractProcessStatus(TaskResultEnum result, String processInstanceID);

	ChangeContractVo getChangeContractVoByProcessInstanceId(String processInstanceID);

	ListResult<Object> getContractSignVoByAssigner(String id, String applyerId, String beginDate,
			String endDate, Integer page, Integer limit);
	
	ContractEntity queryContractEntityBy(String userId);

	ContractEntity getStaffLatestContractByUserId(String userID);
}
