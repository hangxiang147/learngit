package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.MorningMeetingEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.MorningMeetingVo;

public interface MorningMeetingReportService {

	void startWeekMorningMeetingReport(MorningMeetingVo morningMeetingVo, File[] attachment,
			String[] attachmentFileName) throws Exception;

	List<MorningMeetingVo> getMorningMeetingsByInstanceId(List<Task> morningMeetTasks);

	MorningMeetingEntity getMorningMeetingByInstanceId(String processInstanceId);

	ListResult<MorningMeetingVo> findMorningMeetingListByUserID(String id, Integer page, Integer limit);

	void updateProcessStatus(String processInstanceID, String result);

}
