package com.zhizaolian.staff.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssignmentService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.vo.AssignmentVO;

import net.sf.json.JSONObject;

public class AssignmentAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired 
	private ProcessService processService;
	public void startAssignment() {
		Map<String, String> resultMap = new HashMap<String, String>();
		String voStr=request.getParameter("assignmentVO");
		try {
			AssignmentVO assignmentVO = (AssignmentVO) JSONObject
					.toBean(JSONObject.fromObject(voStr), AssignmentVO.class);
			assignmentService.startAssignment(assignmentVO);
			resultMap.put("success", "true");
		} catch (Exception e) {
			resultMap.put("success", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	
	public void editAssignment() {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String taskID = request
					.getParameter("taskID");
			ProcessInstance processInstance = processService.getProcessInstance(taskID);

			String voStr=request.getParameter("assignmentVO");
			AssignmentVO assignmentVO = (AssignmentVO) JSONObject
					.toBean(JSONObject.fromObject(voStr), AssignmentVO.class);
			if (!StringUtils.isBlank(processInstance.getId())) {
				// 执行人确认任务前，分配人修改已分配任务
				assignmentService.updateAssignment(processInstance.getId(),
						assignmentVO);
			}
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			// 修改任务参数
			assignmentService.updateAssignment(pInstance.getId(), assignmentVO);
			// 完成任务
			processService.completeTask(taskID, assignmentVO.getUserID(),
					TaskResultEnum.COMPLETE, "");
			// 更新任务分配业务表流程节点状态
			assignmentService.updateProcessStatus(pInstance.getId(),
					TaskResultEnum.COMPLETE);
			resultMap.put("success", "true");

		} catch (Exception e) {
			resultMap.put("success", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);

	}
}
