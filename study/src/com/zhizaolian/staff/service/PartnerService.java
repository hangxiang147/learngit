package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.PartnerDetailEntity;
import com.zhizaolian.staff.entity.PartnerEntity;
import com.zhizaolian.staff.entity.PartnerOptionEntity;
import com.zhizaolian.staff.entity.PartnerQuitSalaryDetailsEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface PartnerService {

	PartnerEntity getPartnerApplyByUserId(String userId);

	void startPartner(PartnerEntity partner);

	List<PartnerEntity> getPartnerApplysByUserId(String userId);
	
	List<PartnerEntity> getPartnerApplysBy(String userId);

	PartnerEntity getPartnerApplyById(String id);

	ListResult<Object> findAllApplyPartners(Integer limit, Integer page, String applyer);

	void auditApply(String applyId, String result, String comment);

	int getToAuditApplyNum();

	boolean checkHasAuditPartner(String id);

	void savePartnerDetail(PartnerDetailEntity partnerDetail);

	ListResult<Object> findPartnerDetailList(String type, String staffName, Integer limit, Integer page);

	boolean checkIsPartner(String userId);

	double getTotalMoney(String id);

	double getOptionMonty(String id);

	Map<String, List<String>> getDetailListGroupByType(String id);

	ListResult<PartnerOptionEntity> findPartnerOptionsByUserId(String id, Integer limit, Integer page);

	ListResult<Object> findPartnerDetailListByUserId(String type, String id, Integer limit, Integer page);

	void deletePartnerDetail(String id, String rewardType);

	ListResult<Object> findPartnerOptions(String[] conditons, Integer limit, Integer page);

	boolean checkHasPartnerManage(String id);

	int getToMacthOptionNum();

	void matchPartnerOption(String matchPartnerIds, String ratio);

	void synData();

	void uploadQuitSalaryDetail(File file) throws Exception;

	PartnerQuitSalaryDetailsEntity getPartnerQuitSalaryDetail(String id);
	
	//根据用户ID查询合作列表
	PartnerEntity getPartnerEntityBy(String userId);
	
	List<PartnerEntity> findExitPartnerTaskVOs(List<Task> exitPartnerTasks);
	
	List<PartnerEntity> getTheListBy(String processInstanceID);
	
	void updatePartner(String userId,Integer isDeleted);
	
	void updateApprovalOpinionAndOthers(String approvalOpinion,Integer auditStatus, String processInstanceId,Integer isDeleted);
	
	void addExitParterApply(String exitReason,String userId);
	
	List<PartnerEntity> findTheListBy(String userId,Integer exisIsDeleted);
	
	List<PartnerEntity> findTheListBy(String userId);
}
