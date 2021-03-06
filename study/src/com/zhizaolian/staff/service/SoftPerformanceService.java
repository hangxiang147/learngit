package com.zhizaolian.staff.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.FunctionEntity;
import com.zhizaolian.staff.entity.ProjectModuleEntity;
import com.zhizaolian.staff.entity.ProjectVersionEntity;
import com.zhizaolian.staff.entity.RequirementEntity;
import com.zhizaolian.staff.entity.SoftGroupEntity;
import com.zhizaolian.staff.enums.SoftPosition;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.FunctionVo;
import com.zhizaolian.staff.vo.ModuleVo;
import com.zhizaolian.staff.vo.PerformanceVo;
import com.zhizaolian.staff.vo.ProblemOrderVo;
import com.zhizaolian.staff.vo.ProjectVO;
import com.zhizaolian.staff.vo.RequirementVo;
import com.zhizaolian.staff.vo.ScoreResultVo;
import com.zhizaolian.staff.vo.SoftPerformanceTaskVO;
import com.zhizaolian.staff.vo.SoftPerformanceVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.SubRequirementVo;
import com.zhizaolian.staff.vo.VersionRequirementVo;
import com.zhizaolian.staff.vo.VersionVo;

public interface SoftPerformanceService {
	//根据 instance
	void checkIsAllComplete(String requirementId);
	
	String getReuirementPersonId(String requirementId);
	void commonSave(Object object);
	void commonUpdate(Object object);
	void commonHql(String hql);
	ListResult<SoftPerformanceTaskVO> findSoftPerformancesByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<String> users, int page, int limit);

	/**
	 * @param type 人员类型
	 * @param isLeader 是否是组长
	 * @return
	 */
	List<SoftGroupEntity> getSoftPersons(SoftPosition type,boolean isLeader);
	List<SoftGroupEntity> getSoftPersonsAll(SoftPosition type);
	List<SoftGroupEntity> getSoftPersonsAll(SoftPosition type,String Project);

	FunctionEntity geFunctionEntityByInstanceId(String instanceId);

	ListResult<Object> getScoreResult(String year,String month,int page,int limit);
	List<Object> getScoreResult(String userId,String startTime,String endTime,String year,String month);
	//某年内 某人的得分
	List<Object> getScoreResultByMouth(String userId,String year);
	
	void updateProcessStatus(TaskResultEnum taskResultEnum,String instanceId);
	
	ListResult<SoftGroupEntity> getSoftGroupUsers(int page,int limit, String staffId, String personType, String projectName);
	SoftGroupEntity getSoftGroupById(Integer id);
	
	List<ProjectVO> getProjectLst();

	ProjectVO getProject(String id);

	void addProject(ProjectVO project) throws Exception;

	void updateProject(ProjectVO project) throws Exception;

	void deleteProject(String id) throws Exception;

	void addProjectVersion(ProjectVersionEntity version) throws Exception;

	void updateProjectVersion(ProjectVersionEntity version) throws Exception;

	boolean isExistProjectVersion(String version, String projectId, String id) throws Exception;

	List<ProjectVersionEntity> getProjectVersionLst(String projectId);

	ProjectVersionEntity getProjectVersion(String id);

	void deleteProjectVersion(String id) throws Exception;

	void updateProjectUpdatestVersion(String id, String version) throws Exception;

	void addProjectModule(ProjectModuleEntity module) throws Exception;

	void updateProjectModule(ProjectModuleEntity module) throws Exception;

	boolean isExistProjectModule(String module, String projectId) throws Exception;

	List<ProjectModuleEntity> getProjectModuleLst(String projectId);

	ProjectModuleEntity getProjectModule(String id);

	void deleteProjectModule(String id) throws Exception;
	
	Map<String, String> getProjectVersionsMap(String userId, boolean requireManage);
	
	void addRequirement(RequirementEntity requirement, File[] files, String[] fileNames) throws Exception;
	
	List<RequirementEntity> getRequirements(Integer versionId, Integer moduleId);
	
	void addFunction(FunctionEntity function) throws Exception;
	
	void addFunctionVo(SoftPerformanceVo function,File[] files,String fileDetail);

	ListResult<FunctionVo> getFuntions(int limit, int page, String userId);
	
	ListResult<RequirementVo> getRequirementLst(int limit, int page, String versionId) throws Exception;
	
	RequirementVo getRequirementDetail(String requirementId);
	
	RequirementEntity getRequirement(String requirementId);
	
	void deleteAttachment(String requirementId, String attachmentName, String attachmentPath, String tableName) throws Exception;
	
	void updateRequirement(RequirementEntity requirement, File[] files, String[] fileNames) throws Exception;
	
	void deleteRequiremnet(String id) throws Exception;
	
	FunctionVo getFuntionDetail(String taskId);;
	
	void deleteTask(String id) throws Exception;
	
	ListResult<FunctionVo> getFuntions(int limit, int page, FunctionVo functionVo, String userId);
	
	ListResult<VersionVo> getVersions(int page, int limit, String projectId);
	
	ListResult<ModuleVo> getModules(int page, int limit, String projectId);
	
	String getUpdatestVersion(String projectId);
	
	SoftPerformanceVo getFuntion(String instanceId);
	
	List<String> getAttachmentLst(String instanceId);
	
	void deleteTaskAttachment(String attachmentName, String instanceId);
	
	void updateRequireStatus(Integer requirementId);
	
	void saveDeductedScore(ScoreResultVo scoreResultVo);
	
	ListResult<Object> getScoreResult(int limit, int page, String userId, String beginDate, String endDate);
	
	boolean isInCurrentMonth(String resultId);
	
	void deleteResultScore(String resultId);
	
	String getScores(String userId, String beginDate, String endDate);
	
	void addSubRequirement(SubRequirementVo subRequirementVo, File[] files, String[] fileNames) throws Exception;
	
	List<SubRequirementVo> getSubRequirementLst(String requirementId);
	
	SubRequirementVo getSubRequirement(String subRequireId);
	
	void updateSubRequirement(SubRequirementVo subRequirementVo, File[] files, String[] fileNames) throws Exception;
	
	boolean isCreateTaskByVersion(String versionId);
	
	boolean isCreateTaskByRequire(String requireId);
	
	List<Object> getTaskLstByRequire(String requirementId);
	
	void completeRequire(String requirementId);
	
	Map<String, List<FunctionVo>> getTaskLstByVersion(String versionId);
	
	void completeVersion(String versionId);
	
	List<ProjectVersionEntity> getUnCompletedVersionLst(String projectId);
	
	ListResult<RequirementVo> getPreparedRequirementLst(String[] query, int limit, int page);
	
	void saveVersionOrModule(String requireId, String versionId, String moduleId);
	
	void deleteRequire(String requirementId, String deleteReason);
	
	void completeDivide(String requireId);
	
	ListResult<RequirementVo> getDivideRequirementLst(int limit, int page, String versionId, String status,String number,String chooseP,String requireName) throws Exception;
	
	SoftPerformanceVo getTaskVo(String subReqireId);
	
	String getProjectId(String requirementId);
	
	List<FunctionVo> getTaskLst(String subRequirementId, int type);
	
	void actRequireStatus(String requirementId);
	
	boolean hasRight(String userId, String projectName, List<String> type);

	double getUsedWorkHour(String versionId);

	InputStream generateVersionInfoFile(String versionId, String projectName) throws Exception;
	
	List<VersionRequirementVo> getVersionRequirementInfo(String versionId, String projectName);

	void saveReturnReason(String requirementId, String returnReason);

	boolean checkIsDivide(String reqiureId);

	List<ProjectVersionEntity> getProjectVersionLstByRequireId(String projectId);

	void updateRequireVersion(String requireId, String versionId);

	List<Object> getHoursByPerson(String versionId);
	
	boolean checkRequireIsAllot(String requireId);
	
	void updateRequireStatus(String requireId, String status);
	
	String getRequireIdByInstanceId(String instanceId);

	void deleteSubRequirement(String requireId);

	void updateRequireDivide(String requireId, int i);

	void updateTaskVersion(String requireId, String versionId);

	List<String> getInstanceIdListByRequireId(String requireId);

	void updateSubRequireDeveloper(String developer, String subRequirementId);

	List<StaffVO> getSoftPersonsByVersion(SoftPosition type, String versionId);

	List<PerformanceVo> getPerformanceVos(List<Object> list, String year, String month);

	List<PerformanceVo> getBaseScoresDetail(String year, String month, String userId);

	List<PerformanceVo> getPerformanceVos(String userId, List<Object> list, String year);

	void startProblemOrder(ProblemOrderVo problemOrderVo, String id) throws Exception;

	ListResult<Object> findProblemOrderList(String projectId, String status, String problemOrderName, int limit, int page);

	Object getProblemOrderById(String problemOrderId);

	List<ProblemOrderVo> getProblemOrdersByInstanceId(List<Task> problemOrderTasks);

	void startAllocateProblemOrder(String developerId, String score, String taskId, String processInstanceId, String userId, String dutyPersonId);

	void saveProblemOrderScore(String processInstanceId);

	void updateProblemOrderStatus(String processInstanceId, String result);

	double getProblemOrderWorkHour(String versionId);

	double getProblemOrderWorkHourByUserId(String versionId, String userId);

	Integer getProjectIdByName(String projectName);

	Integer getProjectLatestVersionId(Integer projectId);

	ListResult<ProblemOrderVo> getProblemOrderListByUserId(int page, int limit, String userId, String status);

	ProblemOrderVo getProblemOrderByProcessInstanceId(String instanceId);

	void changeProblemToRequire(String taskId, String processInstanceId, String id);

	List<String> getSoftPersonUserIdsByVersion(SoftPosition 产品经理, String versionId);
	
	void endProblemOrderEntityById(Integer id);
	
	void forcedTerminationTask(String userId,Integer id,String processInstanceID);
	
}
