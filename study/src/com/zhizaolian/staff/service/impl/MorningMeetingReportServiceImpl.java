package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.MorningMeetingEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.MorningMeetingReportService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.MorningMeetingVo;

import lombok.Cleanup;

public class MorningMeetingReportServiceImpl implements MorningMeetingReportService {
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	@Override
	public void startWeekMorningMeetingReport(MorningMeetingVo morningMeetingVo, File[] attachment,
			String[] attachmentFileName) throws Exception {
		if(null != attachment){
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(Constants.PERSONAL_FILE_DIRECTORY);
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
						Constants.PERSONAL_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				morningMeetingVo.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		if(StringUtils.isNotBlank(morningMeetingVo.getTaskId())){
			taskService.complete(morningMeetingVo.getTaskId());
			String processInstanceId = morningMeetingVo.getProcessInstanceID();
			MorningMeetingEntity morningMeeting = getMorningMeetingByInstanceId(processInstanceId);
			morningMeeting.setReportTime(new Date());
			morningMeeting.setHasMeeting("是");
			morningMeeting.setAttachmentIds(morningMeetingVo.getAttachmentIds());
			morningMeeting.setDescription(morningMeetingVo.getDescription());
			morningMeeting.setWeekday(morningMeetingVo.getWeekday());
			baseDao.hqlSave(morningMeeting);
		}else{
			Map<String, Object> vars = new HashMap<>();
			List<String> companyBoss = permissionService.findUsersByPermissionCode(Constants.COMPANY_BOSS);
			if(companyBoss.size()>0){
				vars.put("finalManager", companyBoss.get(0));
			}else{
				throw new RuntimeException("未找到总经理（法定代表人）");
			}
			vars.put("applyer", morningMeetingVo.getUserID());
			vars.put("arg", morningMeetingVo);
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.MORNING_MEETING, vars);
			// 查询第一个任务
			Task task = taskService.createTaskQuery()
					.processInstanceId(processInstance.getId()).singleResult();

			// 完成任务
			taskService.complete(task.getId());
			MorningMeetingEntity morningMeeting = (MorningMeetingEntity) CopyUtil.tryToEntity(morningMeetingVo, MorningMeetingEntity.class);
			morningMeeting.setIsDeleted(0);
			morningMeeting.setAddTime(new Date());
			morningMeeting.setReportTime(new Date());
			morningMeeting.setProcessInstanceID(processInstance.getId());
			baseDao.hqlSave(morningMeeting);
		}
	}
	@Override
	public List<MorningMeetingVo> getMorningMeetingsByInstanceId(List<Task> morningMeetTasks) {
		List<MorningMeetingVo> morningMeetingTaskVos = new ArrayList<>();
		for(Task morningMeetTask: morningMeetTasks){
			MorningMeetingEntity morningMeetingEntity = getMorningMeetingByInstanceId(morningMeetTask.getProcessInstanceId());
			MorningMeetingVo morningMeetingVo = (MorningMeetingVo) CopyUtil.tryToVo(morningMeetingEntity, MorningMeetingVo.class);
			morningMeetingVo.setTaskName(morningMeetTask.getName());
			morningMeetingVo.setTaskId(morningMeetTask.getId());
			morningMeetingVo.setUserName(staffDao.getStaffByUserID(morningMeetingVo.getUserID()).getStaffName());
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(morningMeetingVo.getUserID());
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				String department = group.getCompanyName() + "-" + group.getDepartmentName();
				morningMeetingVo.setDepartment(department);
			}
			morningMeetingTaskVos.add(morningMeetingVo);
		}
		return morningMeetingTaskVos;
	}
	@Override
	public MorningMeetingEntity getMorningMeetingByInstanceId(String processInstanceId) {
		String hql = "from MorningMeetingEntity where isDeleted=0  and processInstanceID="+processInstanceId;
		return (MorningMeetingEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public ListResult<MorningMeetingVo> findMorningMeetingListByUserID(String id, Integer page, Integer limit) {
		List<MorningMeetingEntity> morningMeetings = getMorningMeetingsByUserId(id,
				page, limit);

		List<MorningMeetingVo> morningMeetingVos = new ArrayList<MorningMeetingVo>();
		for (MorningMeetingEntity morningMeeting : morningMeetings) {
			MorningMeetingVo morningMeetingVo = null;
			try {
				morningMeetingVo = (MorningMeetingVo) CopyUtil.tryToVo(morningMeeting, MorningMeetingVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			morningMeetingVo.setUserName(staffService.getStaffByUserID(morningMeetingVo.getUserID()).getLastName());
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(morningMeetingVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				morningMeetingVo.setStatus("处理中");
				morningMeetingVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = morningMeetingVo.getProcessStatus();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						morningMeetingVo.setStatus(t.getName());
				}
			}
			morningMeetingVos.add(morningMeetingVo);
		}
		int count = getMorningMeetingCountByUserId(id);
		return new ListResult<MorningMeetingVo>(morningMeetingVos, count);
	}
	private int getMorningMeetingCountByUserId(String id) {
		String hql = "select count(id) from MorningMeetingEntity where IsDeleted=0 and userId='"+id+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	private List<MorningMeetingEntity> getMorningMeetingsByUserId(String id, Integer page, Integer limit) {
		String hql="from MorningMeetingEntity where IsDeleted=0 and userId='"+id+"' order by addTime desc";
		return (List<MorningMeetingEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public void updateProcessStatus(String processInstanceID, String result) {
		String sql = "update OA_MorningMeeting set processStatus="+result+" where processInstanceID="+processInstanceID;
		baseDao.excuteSql(sql);
	}
}
