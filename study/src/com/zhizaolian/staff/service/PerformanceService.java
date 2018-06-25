package com.zhizaolian.staff.service;

import java.util.List;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.PerformanceCheckItemEntity;
import com.zhizaolian.staff.entity.PerformanceEntity;
import com.zhizaolian.staff.entity.PerformancePositionTemplateEntity;
import com.zhizaolian.staff.entity.PerformanceProjectEntity;
import com.zhizaolian.staff.entity.PerformanceStaffCheckItemEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PerformanceVo;

public interface PerformanceService {

	List<PerformancePositionTemplateEntity> findTemplatesByDepId(String departmentId);

	List<PerformanceCheckItemEntity> saveCheckProject(PerformanceProjectEntity project, List<PerformanceCheckItemEntity> checkItem);

	void deleteProject(String projectId);

	PerformanceProjectEntity getProjectInfo(String projectId);

	List<PerformanceCheckItemEntity> getProjectCheckInfos(String projectId);

	List<PerformanceCheckItemEntity> updateCheckProject(PerformanceProjectEntity project, List<PerformanceCheckItemEntity> checkItem);

	int savePositionTemplate(String positionId, String projectIds, String templateName);

	List<PerformanceProjectEntity> getProjectsByIds(String projectIds);

	boolean checkPositionTemplateExist(String positionId);

	List<PerformanceProjectEntity> findPositionTemplateDetailById(String templateId) throws Exception;

	void startPerformanceApply(String userId, String[] templateId);

	void deleteCheckItem(String checkItemId);

	ListResult<PerformanceEntity> findPerformanceApplys(Integer limit, Integer page, String userId);

	List<PerformancePositionTemplateEntity> findPositionPerformances(String templateIds);

	List<PerformanceEntity> findPerformanceTaskVos(List<Task> performanceTasks);

	void pmAudit(String taskId, String result, String comment, String userId);

	ListResult<Object> findStaffPerformances(Integer limit, Integer page, String[] conditions);

	void generateTargetValueTasks(int year, int month) throws Exception;

	List<PerformanceVo> findPerformanceTasks(String userId, BusinessTypeEnum type) throws Exception;

	int getPerformanceTaskCount(String userId, BusinessTypeEnum type);

	List<PerformanceProjectEntity> getTargetProjectsbyTaskId(String taskId) throws Exception;

	void generateActualValueTasks(int year, int month) throws Exception;

	void generateStaffCheckItemPerMonth() throws Exception;

	List<PerformanceProjectEntity> getProjectsByUserId(StaffEntity staff, String year,
			String month);

	List<PerformanceProjectEntity> getProjectsByUserId(String userId, String year, String month) throws Exception;

	List<PerformanceStaffCheckItemEntity> getPersonalProjectCheckInfos(String projectId, String checkItemIds);

	List<PerformanceStaffCheckItemEntity> saveStaffCheckProject(PerformanceProjectEntity project, List<PerformanceStaffCheckItemEntity> staffCheckItem);

	List<PerformanceStaffCheckItemEntity> updateStaffCheckProject(PerformanceProjectEntity project,
			List<PerformanceStaffCheckItemEntity> staffCheckItem, String userId);

	void deleteStaffProject(String projectId, String checkItemIds);

	void deleteStaffCheckItem(String checkItemId);

	void updatePersonalPerformance(String id, String checkItemIds, String staffUserId, String year, String month);

	ListResult<PerformanceEntity> findUpdatePerformanceApplys(Integer limit, Integer page, String id);

	ListResult<Object> findStaffPerformances(Integer limit, Integer page, String year, String month,
			String userName, String userId);

	boolean checkHasUpdateApply(String userId, String year, String month);

	List<PerformanceEntity> findPersonalPerformanceTaskVos(List<Task> personalPerformanceTasks);

	List<PerformanceProjectEntity> findPersonalCheckProjects(String checkItemIds);

	void pmAuditPersonal(String taskId, String result, String comment, String id) throws Exception;

	void saveStaffCheckItemTargetValue(String[] id, String[] targetValue, String taskId);

	void saveStaffCheckItemActualValue(String[] id, String[] actualValue, String taskId);

	void synStaffCheckItems(String userID, Integer positionID) throws Exception;

	List<PerformanceProjectEntity> getActualProjectsbyTaskId(String taskId) throws Exception;

}
