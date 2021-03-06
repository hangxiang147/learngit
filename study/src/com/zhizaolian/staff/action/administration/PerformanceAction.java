package com.zhizaolian.staff.action.administration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.PerformanceCheckItemEntity;
import com.zhizaolian.staff.entity.PerformanceEntity;
import com.zhizaolian.staff.entity.PerformancePositionTemplateEntity;
import com.zhizaolian.staff.entity.PerformanceProjectEntity;
import com.zhizaolian.staff.entity.PerformanceStaffCheckItemEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.service.PerformanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.TaskVO;

import common.Logger;
import lombok.Getter;
import lombok.Setter;

@Controller
@Scope(value="prototype")
public class PerformanceAction extends BaseAction{
	
	private static final long serialVersionUID = 4665852269778253965L;
	
	private static Logger logger = Logger.getLogger(PerformanceAction.class);
	@Getter
	private String selectedPanel;
	@Autowired
	private PerformanceService performanceService;
	@Autowired
	private PositionService positionService;
	@Setter
	@Getter
	private PerformanceProjectEntity project;
	@Setter
	@Getter
	private List<PerformanceCheckItemEntity> checkItem;
	@Setter
	@Getter
	private List<PerformanceStaffCheckItemEntity> staffCheckItem;
	@Setter
	@Getter
	private String[] templateId;
	@Getter
	private String errorMessage;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Getter
	private Integer type;
	@Autowired
	private ProcessService processService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private StaffService staffService;
	
	public String showPerformanceDiagram(){
		selectedPanel = "createPerformanceCase";
		return "showPerformanceDiagram";
	}
	public String newPerformance(){
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "createPerformanceCase";
		return "newPerformance";
	}
	/**
	 * 获取部门下面所有岗位的绩效模板
	 */
	public void findTemplatesByDepId(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String departmentId = request.getParameter("departmentId");
			List<PerformancePositionTemplateEntity> templates = performanceService.findTemplatesByDepId(departmentId);
			resultMap.put("templates", templates);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void saveCheckProject(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			checkItem = performanceService.saveCheckProject(project, checkItem);
			resultMap.put("project", project);
			resultMap.put("checkItem", checkItem);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void saveStaffCheckProject(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			staffCheckItem = performanceService.saveStaffCheckProject(project, staffCheckItem);
			resultMap.put("project", project);
			resultMap.put("checkItem", staffCheckItem);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void deleteProject(){
		String projectId = request.getParameter("projectId");
		performanceService.deleteProject(projectId);
	}
	public void deleteStaffProject(){
		String projectId = request.getParameter("projectId");
		String checkItemIds = request.getParameter("checkItemIds");
		performanceService.deleteStaffProject(projectId, checkItemIds);
	}
	public void findProjectCheckContent(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String projectId = request.getParameter("projectId");
			PerformanceProjectEntity project = performanceService.getProjectInfo(projectId);
			List<PerformanceCheckItemEntity> projectChecks = performanceService.getProjectCheckInfos(projectId);
			resultMap.put("project", project);
			resultMap.put("projectChecks", projectChecks);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void findPersonalProjectCheckContent(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String projectId = request.getParameter("projectId");
			String checkItemIds = request.getParameter("checkItemIds");
			PerformanceProjectEntity project = performanceService.getProjectInfo(projectId);
			List<PerformanceStaffCheckItemEntity> projectChecks = performanceService.getPersonalProjectCheckInfos(projectId, checkItemIds);
			resultMap.put("project", project);
			resultMap.put("projectChecks", projectChecks);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void updateCheckProject(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			checkItem = performanceService.updateCheckProject(project, checkItem);
			resultMap.put("project", project);
			resultMap.put("checkItem", checkItem);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void updateStaffCheckProject(){
		Map<String, Object> resultMap = new HashMap<>();
		User user = (User)request.getSession().getAttribute("user");
		try {
			staffCheckItem = performanceService.updateStaffCheckProject(project, staffCheckItem, user.getId());
			resultMap.put("project", project);
			resultMap.put("checkItem", staffCheckItem);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void savePositionTemplate(){
		Map<String, Object> resultMap = new HashMap<>();
		String positionId = request.getParameter("positionId");
		String projectIds = request.getParameter("projectIds");
		String templateName = request.getParameter("templateName");
		try {
			int templateId = performanceService.savePositionTemplate(positionId, projectIds, templateName);
			List<PerformanceProjectEntity> projects = performanceService.getProjectsByIds(projectIds);
			resultMap.put("projects", projects);
			resultMap.put("position", templateName);
			resultMap.put("templateId", templateId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void checkPositionTemplateExist(){
		Map<String, Object> resultMap = new HashMap<>();
		String positionId = request.getParameter("positionId");
		resultMap.put("exist", performanceService.checkPositionTemplateExist(positionId));
		printByJson(resultMap);
	}
	public void findPositionTemplateDetail(){
		Map<String, Object> resultMap = new HashMap<>();
		String templateId = request.getParameter("templateId");
		try {
			List<PerformanceProjectEntity> projects  = performanceService.findPositionTemplateDetailById(templateId);
			resultMap.put("projects", projects);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String startPerformanceApply(){
		User user = (User)request.getSession().getAttribute("user");
		try {
			performanceService.startPerformanceApply(user.getId(), templateId);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "岗位绩效方案提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("岗位绩效方案提交失败："+sw.toString());
			return "error";
		}
		return "render_findPerformanceApplys";
	}
	public String updatePersonalPerformance(){
		String checkItemIds = request.getParameter("checkItemIds");
		String staffUserId = request.getParameter("userId");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		User user = (User)request.getSession().getAttribute("user");
		try {
			performanceService.updatePersonalPerformance(user.getId(), checkItemIds, staffUserId, year, month);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "个人绩效方案的修改提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("个人绩效方案的修改提交失败："+sw.toString());
			return "error";
		}
		return "render_findUpdatePerformanceApplys";
	}
	public String findPerformanceApplys(){
		User user = (User)request.getSession().getAttribute("user");
		ListResult<PerformanceEntity> performances = performanceService.findPerformanceApplys(limit, page, user.getId());
		count = performances.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("performances", performances.getList());
		selectedPanel = "performanceApplys";
		return "performanceApplys";
	}
	public String findUpdatePerformanceApplys(){
		User user = (User)request.getSession().getAttribute("user");
		ListResult<PerformanceEntity> performances = performanceService.findUpdatePerformanceApplys(limit, page, user.getId());
		count = performances.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("performances", performances.getList());
		selectedPanel = "updatePerformanceApplys";
		return "updatePerformanceApplys";
	}
	public void deleteCheckItem(){
		String checkItemId = request.getParameter("checkItemId");
		performanceService.deleteCheckItem(checkItemId);
	}
	public void deleteStaffCheckItem(){
		String checkItemId = request.getParameter("checkItemId");
		performanceService.deleteStaffCheckItem(checkItemId);
	}
	public String showPerformanceDetail(){
		String templateIds = request.getParameter("templateIds");
		try {
			List<PerformancePositionTemplateEntity> postionPerformances = performanceService.findPositionPerformances(templateIds);
			request.setAttribute("postionPerformances", postionPerformances);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "performanceApplys";
		return "showPerformanceDetail";
	}
	public String showPersonalPerformanceDetail(){
		String checkItemIds = request.getParameter("checkItemIds");
		try {
			List<PerformanceProjectEntity> projects = performanceService.findPersonalCheckProjects(checkItemIds);
			request.setAttribute("projects", projects);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "updatePerformanceApplys";
		return "showPersonalPerformanceDetail";
	}
	public String toPMAudit(){
		String templateIds = request.getParameter("templateIds");
		try {
			List<PerformancePositionTemplateEntity> postionPerformances = performanceService.findPositionPerformances(templateIds);
			request.setAttribute("postionPerformances", postionPerformances);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		selectedPanel = "findTaskList";
		return "pmAudit";
	}
	public String toPMAuditPersonal(){
		String checkItemIds = request.getParameter("checkItemIds");
		try {
			List<PerformanceProjectEntity> projects = performanceService.findPersonalCheckProjects(checkItemIds);
			request.setAttribute("projects", projects);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("staffName", request.getParameter("staffName"));
		selectedPanel = "findTaskList";
		return "pmAuditPersonal";
	}
	public String pmAudit(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		User user = (User) request.getSession().getAttribute("user");
		performanceService.pmAudit(taskId, result, comment, user.getId());
		type = BusinessTypeEnum.PERFORMANCE.getValue();
		return "toTaskList";
	}
	public String pmAuditPersonal(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		User user = (User) request.getSession().getAttribute("user");
		try {
			performanceService.pmAuditPersonal(taskId, result, comment, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.PERSONAL_PERFORMANCE.getValue();
		return "toTaskList";
	}
	public String modifyPositionPerformance(){
		String templateIds = request.getParameter("templateIds");
		try {
			List<PerformancePositionTemplateEntity> postionPerformances = performanceService.findPositionPerformances(templateIds);
			request.setAttribute("postionPerformances", postionPerformances);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		selectedPanel = "findTaskList";
		return "modifyPositionPerformance";
	}
	public String updatePerformance(){
		String taskId = request.getParameter("taskId");
		User user = (User)request.getSession().getAttribute("user");
		taskService.setAssignee(taskId, user.getId());
		taskService.complete(taskId);
		type = BusinessTypeEnum.PERFORMANCE.getValue();
		return "toTaskList";
	}
	public String showProcessHistory(){
		String processInstanceId = request.getParameter("processInstanceId");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceId);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceId);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = request.getParameter("selectedPanel");
		return "processHistory";
	}
	public String findStaffPerformances(){
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		if(StringUtils.isBlank(year)){
			year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
		}
		String userName = request.getParameter("userName");
		String companyId = request.getParameter("companyId");
		String departmentId = request.getParameter("departmentId");
		String[] conditions = {year, month, userName, companyId, departmentId};
		ListResult<Object> staffPerformances = performanceService.
				findStaffPerformances(limit, page, conditions);
		count = staffPerformances.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("performances", staffPerformances.getList());
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("userName", userName);
		request.setAttribute("companyId", companyId);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		if (StringUtils.isNotBlank(companyId)) {
			List<DepartmentVO> departmentVOs = positionService
					.findDepartmentsByCompanyID(Integer.parseInt(companyId));
			if (null!=departmentVOs && StringUtils.isNotBlank(departmentId)) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = Integer.parseInt(departmentId);
				while (selectedDepartmentID != 0) {
					selectedDepartmentIDs.add(0, selectedDepartmentID);
					for (DepartmentVO departmentVO : departmentVOs) {
						if (departmentVO.getDepartmentID() == selectedDepartmentID) {
							selectedDepartmentID = departmentVO.getParentID();
						}
					}
				}
				request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
			}
			request.setAttribute("departmentVOs", departmentVOs);
		}
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "staffPerformances";
		return "staffPerformances";
	}
	public String findUnderlingPerformances(){
		User user = (User)request.getSession().getAttribute("user");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		if(StringUtils.isBlank(year)){
			year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
		}
		String userName = request.getParameter("userName");
		ListResult<Object> staffPerformances = performanceService.
				findStaffPerformances(limit, page, year, month, userName, user.getId());
		count = staffPerformances.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("performances", staffPerformances.getList());
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("userName", userName);
		selectedPanel = "underlingPerformances";
		return "underlingPerformances";
	}
	public String writeTargetValue(){
		String taskId = request.getParameter("taskId");
		try {
			List<PerformanceProjectEntity> projects = performanceService.getTargetProjectsbyTaskId(taskId);
			request.setAttribute("projects", projects);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "findTaskList";
		request.setAttribute("taskId", taskId);
		return "writeTargetValue";
	}
	public String writeActualValue(){
		String taskId = request.getParameter("taskId");
		try {
			List<PerformanceProjectEntity> projects = performanceService.getActualProjectsbyTaskId(taskId);
			request.setAttribute("projects", projects);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "findTaskList";
		request.setAttribute("taskId", taskId);
		return "writeActualValue";
	}
	@Setter
	@Getter
	private String[] id;
	@Setter
	@Getter
	private String[] targetValue;
	public String saveTargetValue(){
		String taskId = request.getParameter("taskId");
		performanceService.saveStaffCheckItemTargetValue(id, targetValue, taskId);
		type = BusinessTypeEnum.PERFORMANCE_TARGET.getValue();
		return "toTaskList";
	}
	@Setter
	@Getter
	private String[] actualValue;
	public String saveActualValue(){
		String taskId = request.getParameter("taskId");
		try {
			performanceService.saveStaffCheckItemActualValue(id, actualValue, taskId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.PERFORMANCE_ACTUAL.getValue();
		return "toTaskList";
	}
	public String showPersonalPerformance(){
		String userId = request.getParameter("userId");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		try {
			StaffEntity staff = staffService.getStaffByUserId(userId);
			List<PerformanceProjectEntity> projects = performanceService.getProjectsByUserId(staff, year, month);
			request.setAttribute("projects", projects);
			request.setAttribute("staff", staff);
			request.setAttribute("month", year+"-"+month);
/*			String salary = staff.getSalary();
			String regex = "^\\d+(\\.\\d+)?$";
			if(Pattern.matches(regex, salary)){
				//基本工资 + 绩效工资
				double finalSalary = Double.parseDouble(staff.getSalary()) - (staff.getPerformance()==null ? 0:staff.getPerformance()) + staff.getTotalPerformanceMoney();
				request.setAttribute("finalSalary", finalSalary);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = request.getParameter("selectedPanel");
		return "showPersonalPerformance";
	}
	public String findMyStaffPerformances(){
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		if(StringUtils.isBlank(year)){
			year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
		}
		User user = (User)request.getSession().getAttribute("user");
		try {
			StaffEntity staff = staffService.getStaffByUserId(user.getId());
			List<PerformanceProjectEntity> projects = performanceService.getProjectsByUserId(staff, year, month);
			request.setAttribute("projects", projects);
			request.setAttribute("staff", staff);
			request.setAttribute("month", year+"-"+month);
/*			String salary = staff.getSalary();
			String regex = "^\\d+(\\.\\d+)?$";
			if(Pattern.matches(regex, salary)){
				//基本工资 + 绩效工资
				double finalSalary = Double.parseDouble(staff.getSalary()) - staff.getPerformance() + staff.getTotalPerformanceMoney();
				request.setAttribute("finalSalary", finalSalary);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		selectedPanel = "myStaffPerformances";
		return "myStaffPerformances";
	}
	public String toModifyPersonalPerformance(){
		String userId = request.getParameter("userId");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		request.setAttribute("userId", userId);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		selectedPanel = "underlingPerformances";
		return "toModifyPersonalPerformance";
	}
	public String modifyPersonalPerformance(){
		String userId = request.getParameter("userId");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		try {
			List<PerformanceProjectEntity> projects = performanceService.getProjectsByUserId(userId, year, month);
			request.setAttribute("projects", projects);
			StaffEntity staff = staffService.getStaffByUserId(userId);
			request.setAttribute("staff", staff);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("userId", userId);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		selectedPanel = "underlingPerformances";
		return "modifyPersonalPerformance";
	}
	public void checkHasUpdateApply(){
		String userId = request.getParameter("userId");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		Map<String, Object> resultMap = new HashMap<>();
		boolean hasUpdateApply = performanceService.checkHasUpdateApply(userId, year, month);
		resultMap.put("hasUpdateApply", hasUpdateApply);
		printByJson(resultMap);
	}
}
