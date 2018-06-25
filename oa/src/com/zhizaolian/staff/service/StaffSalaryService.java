package com.zhizaolian.staff.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.AlterStaffSalaryEntity;
import com.zhizaolian.staff.entity.SocialSecurityClassEntity;
import com.zhizaolian.staff.entity.StaffSalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChangeSalaryDetailVo;
import com.zhizaolian.staff.vo.PaySalaryTaskVo;
import com.zhizaolian.staff.vo.RewardAndPunishmentVo;

public interface StaffSalaryService {

	List<SocialSecurityClassEntity> findSocialSecurityClasss();

	void saveSocialSecurity(SocialSecurityClassEntity socialSecurity);

	SocialSecurityClassEntity findSocialSecurityById(String id);

	int saveStaffSalary(StaffSalaryEntity staffSalary);

	void updateStaffSalaryUserId(String id, Integer staffSalaryId);

	StaffSalaryEntity getStaffSalary(String userID);

	List<AlterStaffSalaryEntity> findStaffSalaryAlterations(String userId) throws Exception;

	void applyAlterStaffSalary(StaffSalaryEntity staffSalary, AlterStaffSalaryEntity alterStaffSalary,
			File[] attachment, String[] attachmentFileName) throws Exception;

	boolean checkHasApplyAlterSalary(String userId);

	AlterStaffSalaryEntity showSalaryAlterationDetail(String id) throws Exception;

	List<AlterStaffSalaryEntity> findAlterSalaryTaskVos(List<Task> alterSalaryTasks);

	void updateAlterSalaryStatus(String result, String taskId);

	void refreshStaffSalary() throws Exception;

	void generateStaffMonthlySalary() throws Exception;

	void calStaffSalary(String[] ids);

	ListResult<Object> findStaffSalarys(StaffSalaryDetailEntity staffDetail, Integer limit,
			Integer page);

	StaffSalaryDetailEntity getStaffSalaryDetailById(String id) throws Exception;

	void startApplyChangeStaffSalary(String staffSalaryId, String userId, Map<String, Double> otherDeductionItemMap,
			Map<String, Double> otherSubsidyItemMap) throws Exception;

	double calTax(double preTaxSalary);

	List<ChangeSalaryDetailVo> findChangeSalaryDetailTaskVos(List<Task> changeSalaryDetailTasks) throws Exception;

	StaffSalaryDetailEntity getStaffSalaryDetailByInstanceId(String processInstanceId) throws Exception;

	void updateChangeSalaryDetailStatus(String result, String processInstanceID);

	boolean checkHasApplyChangeSalaryDetail(String staffSalaryId);

	void updateChangeSalaryDetail(String processInstanceID) throws Exception;

	ListResult<ChangeSalaryDetailVo> findChangeStaffSalaryList(Integer limit, Integer page,
			ChangeSalaryDetailVo changeSalaryDetailVo);

	void startSendSalary(String sendType, String staffSalaryIds, String userId) throws Exception;

	InputStream exportStaffSalarys(String rootPath, StaffSalaryDetailEntity staffDetail) throws Exception;

	ListResult<Object> findStaffSalaryPayInfos(StaffSalaryDetailEntity staffDetail, Integer limit, Integer page);

	void allApplyPaySalary(StaffSalaryDetailEntity staffDetail, String userId) throws Exception;

	void startApplyPaySalary(String staffSalaryIds, String userId, StaffSalaryDetailEntity staffDetail)
			throws Exception;

	List<PaySalaryTaskVo> findPaySalaryTaskVos(List<Task> paySalaryTasks);

	List<PaySalaryTaskVo> getPaySalaryInfosByInstanceId(String processInstanceId) throws Exception;

	void updateStaffSalaryPayStatus(String staffSalaryIds, int value);

	InputStream exportPaySalarys(String processInstanceId) throws Exception;

	void updatePaySalaryApplyStatus(String processInstanceId, int value);

	ListResult<PaySalaryTaskVo> findPaySalaryApplyList(Integer limit, Integer page, PaySalaryTaskVo paySalaryVo);

	ListResult<RewardAndPunishmentVo> findRewardAndPunishmentList(Integer limit, Integer page,
			RewardAndPunishmentVo rewardAndPunishmentVo) throws Exception;

	void startRewardAndPunishment(File[] attachment, String[] attachmentFileName,
			RewardAndPunishmentVo rewardAndPunishmentVo) throws Exception;

	List<RewardAndPunishmentVo> findRewardAndPunishmentTaskVos(List<Task> rewardAndPunishmentTasks) throws Exception;

	RewardAndPunishmentVo getRewardAndPunishmentByProcessInstanceId(String processInstanceId) throws Exception;

	void updateRewardAndPunishmentStatus(String result, String taskId);

	StaffSalaryDetailEntity getStaffSalaryDetailByUserId(int year, int month, String userId);
	/**
	 * 提薪申请时，若是管理人员，需检查下属人数是否满足设定的最少管理人数
	 * @param userId
	 * @return
	 */
	boolean CheckMeetManageCondition(String userId);

	void batchImportSalary(File salaryFile) throws Exception;

	void synOldSalary();

	List<StaffSalaryDetailEntity> getStaffSalarysByUserId(String userId);

}
