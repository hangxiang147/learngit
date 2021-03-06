package com.zhizaolian.staff.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.enums.APPResultEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ProblemOrderVo;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

public class ProblemOrderAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private SoftPerformanceService softPerformanceService;
	@Autowired
	private ProcessService processService;
	
	public void startProblemOrder(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String respondBody = getBodyString();
			JSONObject jsonObject = JSONObject.fromObject(respondBody);
			String userId = jsonObject.getString("userId");
			String projectName = jsonObject.getString("project");
			String description = jsonObject.getString("description");
			String orderName = jsonObject.getString("orderName");
			String questionerId = jsonObject.getString("questionerId");
			JSONArray attachmentJSONArray = null;
			try {
				attachmentJSONArray = jsonObject.getJSONArray("attachment");
			} catch (Exception e) {
			}
			List<Integer> attachmentIds = new ArrayList<>();
			if(null != attachmentJSONArray){
				Object[] attachments = attachmentJSONArray.toArray();
				Object[] attachmentFileNames = jsonObject.getJSONArray("attachmentName").toArray();
				File parent = new File(Constants.PRODUCT_FILE_DIRECTORY);
				int index = 0;
				for(Object attachment: attachments){
					String attachmentFileName = (String)attachmentFileNames[index];
					BASE64Decoder decoder = new BASE64Decoder();
					byte[] imageBytes = decoder.decodeBuffer((String)attachment);
					parent.mkdirs();
					String saveName = UUID.randomUUID().toString().replaceAll("-", "");
					@Cleanup
					OutputStream out = new FileOutputStream(new File(parent, saveName));
					out.write(imageBytes);
					out.flush();
					CommonAttachment commonAttachment = new CommonAttachment();
					commonAttachment.setAddTime(new Date());
					commonAttachment.setIsDeleted(0);
					commonAttachment.setSoftURL(
							Constants.PRODUCT_FILE_DIRECTORY + saveName);
					commonAttachment.setSoftName(attachmentFileName);
					commonAttachment.setSuffix(attachmentFileName.substring(attachmentFileName.lastIndexOf(".")+1, attachmentFileName.length()));
					Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
					attachmentIds.add(attachmentId);
					index++;
				}
			}
			ProblemOrderVo problemOrderVo = new ProblemOrderVo();
			problemOrderVo.setDescription(description);
			problemOrderVo.setQuestionerId(questionerId);
			problemOrderVo.setOrderName(orderName);
			problemOrderVo.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			int projectId = softPerformanceService.getProjectIdByName(projectName);
			problemOrderVo.setProjectId(projectId);
			//获取mes最新的版本
			int projectVersionId = softPerformanceService.getProjectLatestVersionId(projectId);
			problemOrderVo.setProjectVersionId(projectVersionId);
			softPerformanceService.startProblemOrder(problemOrderVo, userId);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("result", APPResultEnum.ERROR.getValue());
		}
		printByJson(resultMap);
	}
	@Getter
	private InputStream inputStream;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private TaskService taskService;
	
	public void attachmentSave() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String root = "/usr/local/download";
		File parent = new File(root + "/" + "kindEditor");
		parent.mkdirs();
		String saveName = UUID.randomUUID().toString().replaceAll("-", "");
		try {
			String jsonStr = getBodyString();
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] imageBytes = decoder.decodeBuffer(jsonStr);
			@Cleanup
			OutputStream out = new FileOutputStream(new File(parent, saveName));
			out.write(imageBytes);
			out.flush();
			CommonAttachment commonAttachment = new CommonAttachment();
			commonAttachment.setAddTime(new Date());
			commonAttachment.setIsDeleted(0);
			commonAttachment.setSoftURL(
					root + "/kindEditor/" + saveName);
			Integer number = noticeService.saveAttachMent(commonAttachment);
			String url = "/app/problemOrder/showImage?id="+number;
			int error = 0;
			resultMap.put("url", url);
			resultMap.put("error", error);
		} catch (Exception e) {
			e.printStackTrace();
			int error = 1;
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("error", error);
		} 
		printByJson(resultMap);
	}
	@Getter
	@Setter
	private String attachmentName;
	public String downloadPic() {
		try {
			String attachmentPath = request.getParameter("attachmentPath");
			attachmentName = request.getParameter("attachmentName");
			inputStream = new FileInputStream(new File(attachmentPath));
			// 解决中文乱码
			attachmentName = new String(attachmentName.getBytes(),
					"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "downloadPic";
	}
	public void findProblemOrderList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String respondBody = getBodyString();
			JSONObject jsonObject = JSONObject.fromObject(respondBody);
			String userId = jsonObject.getString("userId");
			String status = jsonObject.getString("zhuantai");
			int page = Integer.parseInt(jsonObject.getString("currentPage"));
			int limit = Integer.parseInt(jsonObject.getString("pageSize"));
			List<ProblemOrderVo> problemOrderTaskVos = null;
			int count = 0;
			//0:处理中；1：已处理；2：待验收；默认：全部
			switch(status){
				case "2":
					List<Task> problemOrderTasks = taskService.createTaskQuery().taskAssignee(userId)
						.taskDefinitionKey(Constants.PROBLEM_ORDER_CONFIRM).listPage((page - 1) * limit, limit);
					problemOrderTaskVos = softPerformanceService.getProblemOrdersByInstanceId(problemOrderTasks);
					count = (int) taskService.createTaskQuery().taskAssignee(userId)
							.taskDefinitionKey(Constants.PROBLEM_ORDER_CONFIRM).count();
				break;
				default:
					ListResult<ProblemOrderVo> problemOrderTaskList = softPerformanceService
						.getProblemOrderListByUserId(page, limit, userId, status);
					problemOrderTaskVos = problemOrderTaskList.getList();
					count = problemOrderTaskList.getTotalCount();
				break;
			}
			int totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			resultMap.put("totalPage", totalPage);
			resultMap.put("count", count);
			resultMap.put("problemOrderTaskVos", problemOrderTaskVos);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	//问题验收
	public void confirmProblem(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String respondBody = getBodyString();
			JSONObject jsonObject = JSONObject.fromObject(respondBody);
			String userId = jsonObject.getString("userId");
			String taskId = jsonObject.getString("taskId");
			String processInstanceId = jsonObject.getString("processInstanceID");
			String result = jsonObject.getString("result");
			String comment = jsonObject.getString("comment");
			processService.completeTask(taskId, userId, TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
			if(Integer.parseInt(result) == TaskResultEnum.AGREE.getValue()){
				//更新问题单状态
				softPerformanceService.updateProblemOrderStatus(processInstanceId, result);
				//记录得分
				softPerformanceService.saveProblemOrderScore(processInstanceId);
			} 
			String status = jsonObject.getString("zhuantai");
			int page = Integer.parseInt(jsonObject.getString("currentPage"));
			int limit = Integer.parseInt(jsonObject.getString("pageSize"));
			List<ProblemOrderVo> problemOrderTaskVos = null;
			int count = 0;
			//0:处理中；1：已处理；2：待验收；默认：全部
			switch(status){
				case "2":
					List<Task> problemOrderTasks = taskService.createTaskQuery().taskAssignee(userId)
						.taskDefinitionKey(Constants.PROBLEM_ORDER_CONFIRM).listPage((page - 1) * limit, limit);
					problemOrderTaskVos = softPerformanceService.getProblemOrdersByInstanceId(problemOrderTasks);
					count = (int) taskService.createTaskQuery().taskAssignee(userId)
							.taskDefinitionKey(Constants.PROBLEM_ORDER_CONFIRM).count();
				break;
				default:
					ListResult<ProblemOrderVo> problemOrderTaskList = softPerformanceService
						.getProblemOrderListByUserId(page, limit, userId, status);
					problemOrderTaskVos = problemOrderTaskList.getList();
					count = problemOrderTaskList.getTotalCount();
				break;
			}
			int totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			resultMap.put("totalPage", totalPage);
			resultMap.put("count", count);
			resultMap.put("problemOrderTaskVos", problemOrderTaskVos);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void showProblemDetail(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> bigMap = new HashMap<String, Object>();
		try {
			String respondBody = getBodyString();
			JSONObject jsonObject = JSONObject.fromObject(respondBody);
			String instanceId = jsonObject.getString("processInstanceID");
			ProblemOrderVo problemOrder = softPerformanceService.getProblemOrderByProcessInstanceId(instanceId);
			bigMap.put("description", StringUtils.isBlank(problemOrder.getDescription()) ? "":problemOrder.getDescription());
			problemOrder.setDescription("");
			if(null != problemOrder){
				String attachmentIdStr = problemOrder.getAttachmentIds();
				List<CommonAttachment> attaList = new ArrayList<>();
				if(StringUtils.isNotBlank(attachmentIdStr)){
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
				}
				resultMap.put("attaList", attaList);
			}
			resultMap.put("problemOrder", problemOrder);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			bigMap.put("resultMap", resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(bigMap);
	}
	public String showImage(){
		String attachmentPath = request.getParameter("attachmentPath");
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(Integer.parseInt(id));
			attachmentPath = commonAttachment.getSoftURL();
		}
		//判断路径是不是图片路径
		if(attachmentPath.indexOf("id=")>-1){
			id = attachmentPath.substring(attachmentPath.indexOf("?id=")+4, attachmentPath.length());
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			attachmentPath = commonAttachment.getSoftURL();
		}
		try {
			inputStream = new FileInputStream(new File(attachmentPath)) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showImage";
	}
}
