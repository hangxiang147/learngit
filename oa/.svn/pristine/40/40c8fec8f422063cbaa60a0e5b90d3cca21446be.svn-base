package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.ProjectInfoEntity;
import com.zhizaolian.staff.entity.ProjectReportInfoEntity;
import com.zhizaolian.staff.enums.ProjectStatusEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ProjectInfoVo;

public interface ProjectService {

	void startProject(ProjectInfoVo projectInfoVo, File[] attachment, String[] attachmentFileName) throws Exception;

	ListResult<ProjectInfoVo> findMyaddProjectList(Integer page, Integer limit, String id);

	ProjectInfoVo getProjectInfoByInstanceId(String processInstanceID);

	List<ProjectInfoVo> getProjectInfosByInstanceId(List<Task> projectTasks);

	void saveProjectReport(String taskId, ProjectReportInfoEntity projectReport, File[] attachment, String[] attachmentFileName) throws Exception;

	List<ProjectReportInfoEntity> getProjectReportInfos(Integer id, String userId);

	Map<String, List<ProjectReportInfoEntity>> getProjectReportInfosMap(Integer id, ProjectInfoVo projectInfo);

	ProjectInfoEntity getProjectByInstanceId(String processInstanceId);

	void updateProjectStatus(String processInstanceID, ProjectStatusEnum complete);

	void modifyProjectReportStatus(Integer id);

	ListResult<ProjectInfoVo> findMyProjectList(Integer page, Integer limit, String id);

	ListResult<ProjectInfoVo> findAllProjectList(Integer page, Integer limit, String beginDate, String endDate);

	boolean checkProjectHasProcess(String processInstanceID);

}
