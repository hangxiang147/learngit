package com.zhizaolian.staff.service;
import java.util.List;

import org.activiti.engine.identity.Group;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ViewWorkReportVo;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
public interface ViewReportService {

	void startViewReportApply(ViewWorkReportVo viewWorkReportVo);

	int findViewReportTaskCountByGroups(List<Group> groups);

	ListResult<ViewWorkReportVo> findViewReportTasksByGroups(List<Group> groups, Integer page, Integer limit) throws Exception;

	void updateViewReportProcessStatus(String processInstanceID, TaskResultEnum result);

	void saveViewReportRight(String processInstanceID);

	void checkPermission(List<String> permissions, String userId);

	boolean checkCanViewDepWorkReport(String id);

	ListResult<WorkReportDetailVO> getWorkReportsByConditions(ViewWorkReportVo viewReportVo, Integer page,
			Integer limit);

	boolean checkApplyThisDep(Integer companyId, Integer depId, String id);

	boolean checkApplyThisUser(String userId, String viewerId);

	void deleteUserIdByOneTime(String userId, String id);

	ListResult<ViewWorkReportVo> findViewWorkReportListByUserID(String id, Integer page, Integer limit);
	
}
