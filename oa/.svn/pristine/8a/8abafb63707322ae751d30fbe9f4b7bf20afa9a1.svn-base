package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.PermissionDao;
import com.zhizaolian.staff.dao.RightDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.entity.ProjectInfoEntity;
import com.zhizaolian.staff.entity.ProjectReportInfoEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.ProjectStatusEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ProjectService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ProjectInfoVo;

import lombok.Cleanup;

public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private RightDao rightDao;
	@Override
	public void startProject(ProjectInfoVo projectInfoVo, File[] attachment, String[] attachmentFileName) throws Exception {
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.PROJECT_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						Constants.PROJECT_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				projectInfoVo.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		Map<String, Object> vars = new HashMap<>();
		Set<String> projectParticipants = new HashSet<>();
		if(null != projectInfoVo.getProjectParticipants() && projectInfoVo.getProjectParticipants().length()>0){
			projectParticipants.addAll(Arrays.asList(projectInfoVo.getProjectParticipants().split(",")));
		}
		projectParticipants.add(projectInfoVo.getProjectLeaderId());
		vars.put("participants", projectParticipants);
		vars.put("projectLeader", projectInfoVo.getProjectLeaderId());
		if(StringUtils.isBlank(projectInfoVo.getFinalAuditor())){
			vars.put("finalAuditor", null);
		}else{
			vars.put("finalAuditor", projectInfoVo.getFinalAuditor());
		}
		vars.put("arg", projectInfoVo);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.PRPJECT, vars);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), projectInfoVo.getUserID());
		// 完成任务
		taskService.complete(task.getId());
		ProjectInfoEntity projectInfo = (ProjectInfoEntity) CopyUtil.tryToEntity(projectInfoVo, ProjectInfoEntity.class);
		projectInfo.setIsDeleted(0);
		projectInfo.setAddTime(new Date());
		projectInfo.setProcessInstanceID(processInstance.getId());
		projectInfo.setProjectProgress(ProjectStatusEnum.PROGRESS.getValue());
		baseDao.hqlSave(projectInfo);
		//项目发起成功，给所有参与人开通项目管理的权限
		for(String projectParticipant: projectParticipants){
			PermissionEntity permission = permissionDao.getPermissionByCode(Constants.PRPJECT_MANAGEMENT);
			if(null != permission){
				//检查是否已有权限
				if(!rightDao.checkHasThisRight(projectParticipant, permission.getPermissionID())){
					rightDao.createRightMemberShip(projectParticipant, "1", String.valueOf(permission.getPermissionID()));
				}
			}
		}
	}
	@Override
	public ListResult<ProjectInfoVo> findMyaddProjectList(Integer page, Integer limit, String id) {
		List<ProjectInfoEntity> projects = getProjectsByUserId(id,
				page, limit);

		List<ProjectInfoVo> projectInfoVos = new ArrayList<ProjectInfoVo>();
		for (ProjectInfoEntity project : projects) {
			ProjectInfoVo projectInfoVo = null;
			try {
				projectInfoVo = (ProjectInfoVo) CopyUtil.tryToVo(project, ProjectInfoVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			String finalAuditor = projectInfoVo.getFinalAuditor();
			if(StringUtils.isNotBlank(finalAuditor)){
				projectInfoVo.setFinalAuditorName(staffService.getStaffByUserID(finalAuditor).getLastName());
			}
			projectInfoVo.setProjectLeaderName(staffService.getStaffByUserID(projectInfoVo.getProjectLeaderId()).getLastName());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(projectInfoVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				projectInfoVo.setStatus("进行中");
				projectInfoVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = projectInfoVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						projectInfoVo.setStatus(t.getName());
				}
			}
			projectInfoVos.add(projectInfoVo);
		}
		int count = getProjectCountByUserId(id);
		return new ListResult<ProjectInfoVo>(projectInfoVos, count);
	}
	private int getProjectCountByUserId(String id) {
		String hql = "select count(id) from ProjectInfoEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<ProjectInfoEntity> getProjectsByUserId(String id, Integer page, Integer limit) {
		String hql="from ProjectInfoEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<ProjectInfoEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public ProjectInfoVo getProjectInfoByInstanceId(String processInstanceID) {
		String hql = "from ProjectInfoEntity where processInstanceID="+processInstanceID;
		ProjectInfoEntity projectInfo = (ProjectInfoEntity) baseDao.hqlfindUniqueResult(hql);
		ProjectInfoVo projectInfoVo = (ProjectInfoVo) CopyUtil.tryToVo(projectInfo, ProjectInfoVo.class);
		projectInfoVo.setProjectLeaderName(staffService.getStaffByUserID(projectInfoVo.getProjectLeaderId()).getLastName());
		String finalAuditor = projectInfoVo.getFinalAuditor();
		if(StringUtils.isNotBlank(finalAuditor)){
			projectInfoVo.setFinalAuditorName(staffService.getStaffByUserID(finalAuditor).getLastName());
		}
		String projectParticipantStr = projectInfo.getProjectParticipants();
		if(StringUtils.isNotBlank(projectParticipantStr)){
			List<String> projectParticipantNames = new ArrayList<>();
			String[] projectParticipants = projectParticipantStr.split(",");
			for(String projectParticipant: projectParticipants){
				projectParticipantNames.add(staffService.getStaffByUserID(projectParticipant).getLastName());
			}
			projectInfoVo.setProjectParticipantNames(StringUtils.join(projectParticipantNames, ","));
		}
		String attachmentIdStr = projectInfoVo.getAttachmentIds();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			List<CommonAttachment> attaList = new ArrayList<>();
			String[] attachmentIds = attachmentIdStr.split(",");
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				String suffix = attachment.getSuffix();
				//图片
				if(Constants.PIC_SUFFIX.contains(suffix)){
					attachment.setSuffix("png");
				}
				attaList.add(attachment);
			}
			projectInfoVo.setAttaList(attaList);
		}
		return projectInfoVo;
	}
	@Override
	public List<ProjectInfoVo> getProjectInfosByInstanceId(List<Task> projectTasks) {
		List<ProjectInfoVo> projectInfoVos = new ArrayList<>();
		for(Task projectTask: projectTasks){
			ProjectInfoEntity projectInfoEntity = getProjectByInstanceId(projectTask.getProcessInstanceId());
			ProjectInfoVo projectInfoVo = (ProjectInfoVo) CopyUtil.tryToVo(projectInfoEntity, ProjectInfoVo.class);
			projectInfoVo.setTaskName(projectTask.getName());
			projectInfoVo.setTaskId(projectTask.getId());
			projectInfoVo.setUserName(staffDao.getStaffByUserID(projectInfoVo.getUserID()).getStaffName());
			projectInfoVo.setProjectLeaderName(staffDao.getStaffByUserID(projectInfoVo.getProjectLeaderId()).getStaffName());
			projectInfoVos.add(projectInfoVo);
		}
		return projectInfoVos;
	}
	@Override
	public ProjectInfoEntity getProjectByInstanceId(String processInstanceId) {
		String hql = "from ProjectInfoEntity where processInstanceID="+processInstanceId;
		return (ProjectInfoEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void saveProjectReport(String taskId, ProjectReportInfoEntity projectReport, File[] attachment,
			String[] attachmentFileName) throws Exception {
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.PROJECT_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						Constants.PROJECT_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				projectReport.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		projectReport.setIsDeleted(0);
		projectReport.setAddTime(new Date());
		baseDao.hqlSave(projectReport);
		//汇报完成时，任务完结
		if(ProjectStatusEnum.COMPLETE.getValue().equals(projectReport.getProgress())){
			processService.completeTask(taskId, projectReport.getReportUserId(),
					TaskResultEnum.COMPLETE_REPORT, null);
			//检查所有参与人的任务是否都已完成，若完成的话，项目进入待验收状态
			if(checkAllProjectReportStatus(projectReport.getProjectInfoId())){
				updateProjectStatusById(projectReport.getProjectInfoId(), ProjectStatusEnum.CHECK);
			}
		}
	}
	private void updateProjectStatusById(Integer projectInfoId, ProjectStatusEnum check) {
		String sql = "update OA_ProjectInfo set projectProgress='"+check.getValue()+"' where id="+projectInfoId;
		baseDao.excuteSql(sql);
	}
	private boolean checkAllProjectReportStatus(Integer projectInfoId) {
		ProjectInfoEntity projectInfo = getProjectById(projectInfoId);
		String projectLeaderId = projectInfo.getProjectLeaderId();
		String sql = "SELECT\n" +
				"	count(report.id)\n" +
				"FROM\n" +
				"	OA_ProjectReport report\n" +
				"WHERE\n" +
				"	report.isDeleted = 0\n" +
				"AND report.progress = '"+ProjectStatusEnum.COMPLETE.getValue()+"'\n" +
				"AND report.projectInfoId = "+projectInfoId+"\n" +
				"AND report.reportUserId='"+projectLeaderId+"'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count<1){
			return false;
		}
		String projectParticipantStr = projectInfo.getProjectParticipants();
		if(StringUtils.isNotBlank(projectParticipantStr)){
			String[] projectParticipants = projectParticipantStr.split(",");
			for(String projectParticipant: projectParticipants){
				sql = "SELECT\n" +
						"	count(report.id)\n" +
						"FROM\n" +
						"	OA_ProjectReport report\n" +
						"WHERE\n" +
						"	report.isDeleted = 0\n" +
						"AND report.progress = '"+ProjectStatusEnum.COMPLETE.getValue()+"'\n" +
						"AND report.projectInfoId = "+projectInfoId+"\n" +
						"AND report.reportUserId='"+projectParticipant+"'";
				count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
				if(count<1){
					return false;
				}
			}
		}
		return true;
	}
	private ProjectInfoEntity getProjectById(Integer projectInfoId) {
		String hql = "from ProjectInfoEntity where id="+projectInfoId;
		return (ProjectInfoEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectReportInfoEntity> getProjectReportInfos(Integer id, String userId) {
		String hql = "from ProjectReportInfoEntity where isDeleted=0 and projectInfoId="+id+" and reportUserId='"+userId+"'";
		List<ProjectReportInfoEntity> projectReportInfos = (List<ProjectReportInfoEntity>) baseDao.hqlfind(hql);
		for (ProjectReportInfoEntity projectReportInfo : projectReportInfos) {
			String attachmentIdStr = projectReportInfo.getAttachmentIds();
			if(StringUtils.isNotBlank(attachmentIdStr)){
				List<CommonAttachment> attaList = new ArrayList<>();
				String[] attachmentIds = attachmentIdStr.split(",");
				for(String attachmentId: attachmentIds){
					CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
					String suffix = attachment.getSuffix();
					//图片
					if(Constants.PIC_SUFFIX.contains(suffix)){
						attachment.setSuffix("png");
					}
					attaList.add(attachment);
				}
				projectReportInfo.setAttaList(attaList);
			}
		}
		return projectReportInfos;
	}
	@Override
	public Map<String, List<ProjectReportInfoEntity>> getProjectReportInfosMap(Integer id, ProjectInfoVo projectInfo) {
		Map<String, List<ProjectReportInfoEntity>> projectReportInfosMap = new HashMap<>();
		projectReportInfosMap.put(staffDao.getStaffByUserID(projectInfo.getProjectLeaderId()).getStaffName(),
				getProjectReportInfos(id, projectInfo.getProjectLeaderId()));
		String projectParticipantStr = projectInfo.getProjectParticipants();
		if(StringUtils.isNotBlank(projectParticipantStr)){
			String[] projectParticipants = projectParticipantStr.split(",");
			for(String projectParticipant: projectParticipants){
				projectReportInfosMap.put(staffDao.getStaffByUserID(projectParticipant).getStaffName(), getProjectReportInfos(id, projectParticipant));
			}
		}
		return projectReportInfosMap;
	}
	@Override
	public void updateProjectStatus(String processInstanceID, ProjectStatusEnum complete) {
		String sql = "update OA_ProjectInfo set projectProgress='"+complete.getValue()+"' where processInstanceID="+processInstanceID;
		baseDao.excuteSql(sql);
	}
	@Override
	public void modifyProjectReportStatus(Integer id) {
		String sql = "update OA_ProjectReport set progress='"+ProjectStatusEnum.PROGRESS.getValue()+"' where projectInfoId="+id;
		baseDao.excuteSql(sql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ProjectInfoVo> findMyProjectList(Integer page, Integer limit, String userId) {
		String hql = "FROM ProjectInfoEntity\n" +
				"WHERE\n" +
				"	(projectLeaderId = '"+userId+"'\n" +
				"OR LOCATE(\n" +
				"	'"+userId+"',\n" +
				"	projectParticipants\n" +
				") > 0) and isDeleted=0 order by addTime desc";
		List<ProjectInfoEntity> projectInfoEntitys = (List<ProjectInfoEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<ProjectInfoVo> projectInfoVos = new ArrayList<>();
		for(ProjectInfoEntity projectInfo: projectInfoEntitys){
			ProjectInfoVo projectInfoVo = (ProjectInfoVo) CopyUtil.tryToVo(projectInfo, ProjectInfoVo.class);
			String finalAuditor = projectInfoVo.getFinalAuditor();
			if(StringUtils.isNotBlank(finalAuditor)){
				projectInfoVo.setFinalAuditorName(staffService.getStaffByUserID(finalAuditor).getLastName());
			}
			projectInfoVo.setProjectLeaderName(staffService.getStaffByUserID(projectInfoVo.getProjectLeaderId()).getLastName());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(projectInfoVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				projectInfoVo.setStatus("进行中");
				projectInfoVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = projectInfoVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						projectInfoVo.setStatus(t.getName());
				}
			}
			projectInfoVos.add(projectInfoVo);
		}
		String hqlCount = "select count(id) from ProjectInfoEntity\n" +
							"WHERE\n" +
							"	(projectLeaderId = '"+userId+"'\n" +
							"OR LOCATE(\n" +
							"	'"+userId+"',\n" +
							"	projectParticipants\n" +
							") > 0) and isDeleted=0";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount)+"");
		return new ListResult<>(projectInfoVos, count);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<ProjectInfoVo> findAllProjectList(Integer page, Integer limit, String beginDate, String endDate) {
		String hql = "from ProjectInfoEntity where isDeleted=0\n";
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and Date(addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and Date(addTime)<='"+endDate+"'\n";
		}
		hql += "order by addTime desc";
		List<ProjectInfoEntity> projectInfoEntitys = (List<ProjectInfoEntity>) baseDao.hqlPagedFind(hql, page, limit);
		List<ProjectInfoVo> projectInfoVos = new ArrayList<>();
		for(ProjectInfoEntity projectInfo: projectInfoEntitys){
			ProjectInfoVo projectInfoVo = (ProjectInfoVo) CopyUtil.tryToVo(projectInfo, ProjectInfoVo.class);
			String finalAuditor = projectInfoVo.getFinalAuditor();
			if(StringUtils.isNotBlank(finalAuditor)){
				projectInfoVo.setFinalAuditorName(staffService.getStaffByUserID(finalAuditor).getLastName());
			}
			projectInfoVo.setProjectLeaderName(staffService.getStaffByUserID(projectInfoVo.getProjectLeaderId()).getLastName());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(projectInfoVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				projectInfoVo.setStatus("进行中");
				projectInfoVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = projectInfoVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						projectInfoVo.setStatus(t.getName());
				}
			}
			projectInfoVos.add(projectInfoVo);
		}
		String hqlCount = "select count(id) from ProjectInfoEntity where isDeleted=0";
		if(StringUtils.isNotBlank(beginDate)){
			hqlCount += "and Date(addTime)>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hqlCount += "and Date(addTime)<='"+endDate+"'\n";
		}
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(hqlCount)+"");
		return new ListResult<>(projectInfoVos, count);
	}
	@Override
	public boolean checkProjectHasProcess(String processInstanceID) {
		ProjectInfoEntity projectInfo = getProjectByInstanceId(processInstanceID);
		String sql = "select count(id) from OA_ProjectReport where isDeleted=0 and projectInfoId="+projectInfo.getId();
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
}
