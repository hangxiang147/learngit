package com.zhizaolian.staff.action.performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.FunctionEntity;
import com.zhizaolian.staff.entity.ProjectModuleEntity;
import com.zhizaolian.staff.entity.ProjectVersionEntity;

import com.zhizaolian.staff.entity.SoftGroupEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.SoftPosition;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.entity.RequirementEntity;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.vo.BatchTaskVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.FormField;
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
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.VersionVo;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

public class SoftPerformanceAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private BatchTaskVo batchTaskVo;
	@Setter
	@Getter
	private FunctionVo functionVo;
	@Getter
	private List<VersionVo> versionLst;
	@Getter
	private List<ModuleVo> moduleLst;
	@Getter
	private List<SubRequirementVo> subRequirementLst;
	@Getter
	private boolean isEdit;
	@Getter
	private RequirementVo requirementVo;
	@Getter
	private List<String> attachmentLst;
	@Setter
	@Getter
	private ScoreResultVo scoreResultVo;
	@Getter
	@Setter
	private SubRequirementVo subRequirementVo;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Getter
	@Setter
	private InputStream inputStream;
	@Getter
	@Setter
	private String downloadFileFileName;
	@Setter
	@Getter
	private String requirementId;
	@Getter
	private String selectedPanel;
	@Setter
	@Getter
	private int page = 1;
	@Setter
	@Getter
	private int limit = 20;
	@Getter
	private int totalPage;
	@Getter
	@Setter
	private ProjectVO projectVO;
	@Getter
	private int startIndex;
	@Getter
	private List<ProjectVO> projectLst;
	@Autowired
	private SoftPerformanceService softPerformanceService;
	@Setter
	@Getter
	private String projectId;
	@Getter
	@Setter
	private File[] files;
	@Getter
	@Setter
	private String fileDetail;
	@Setter
	private String loginUserId;
	@Setter
	@Getter
	private ProjectVersionEntity projectVersion;
	@Setter
	@Getter
	private ProjectModuleEntity projectModule;
	@Getter
	private String projectVersions;
	@Getter
	private List<ProjectVO> projects;
	@Getter
	private List<ProjectModuleEntity> modules;
	@Getter
	private List<ProjectVersionEntity> versions;
	@Setter
	@Getter
	private RequirementEntity requirementEntity;

	@Getter
	private List<RequirementEntity> associatedRequires;
	@Setter
	@Getter
	private FunctionEntity functionEntity;
	@Setter
	private String nextStep;
	@Getter
	private List<FunctionVo> taskLst;
	@Setter
	@Getter
	private SoftGroupEntity softGroupEntity;
	@Autowired
	private StaffService staffService;
	@Autowired
	private NoticeService noticeService;

	/**
	 * 我的得分页面 (按月统计)
	 * 
	 * @return
	 */
	public String myScoreList() {
		String year = request.getParameter("year");
		if (StringUtils.isBlank(year)) {
			year = Calendar.getInstance().get(Calendar.YEAR) + "";
		}
		User user = (User) request.getSession().getAttribute("user");
		List<Object> list = softPerformanceService
				.getScoreResultByMouth(user.getId(), year);
		List<PerformanceVo> performanceVos = softPerformanceService.getPerformanceVos(user.getId(), list, year);
		request.setAttribute("year", year);
		request.setAttribute("performanceVos", performanceVos);
		return "myScoreList";
	}

	public String myScoreDetail() {
		User user = (User) request.getSession().getAttribute("user");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		String userId = request.getParameter("userId");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		String beginDateStr = DateUtil.formateDate(cal.getTime());
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String endDateStr = DateUtil.formateDate(cal.getTime());
		List<Object> list = softPerformanceService.getScoreResult(
				StringUtils.isNotBlank(userId) ? userId : user.getId(),
						beginDateStr, endDateStr, year, month);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("list", list);

		request.setAttribute("isFromStatistic",
				request.getParameter("isFromStatistic"));
		return "myScoreDetail";
	}

	public String statisticScoreList() {
		String year = request.getParameter("year");
		if (StringUtils.isBlank(year)) {
			year = Calendar.getInstance().get(Calendar.YEAR) + "";
		}
		String month = request.getParameter("month");
		if (StringUtils.isBlank(month)) {
			month = Calendar.getInstance().get(Calendar.MONTH) + 1 + "";
		}
		ListResult<Object> resultList = softPerformanceService
				.getScoreResult(year, month, page, limit);
		List<PerformanceVo> performanceVos = softPerformanceService.getPerformanceVos(resultList.getList(), year, month);
		count = resultList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("performanceVos", performanceVos);
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		return "statisticScoreList";
	}

	public String softPersonTypeList() {
		String staffName = request.getParameter("staffName");
		String staffId =  request.getParameter("staffId");
		String personType = request.getParameter("personType");
		String projectName = request.getParameter("projectName");
		List<ProjectVO> projects = softPerformanceService.getProjectLst();
		ListResult<SoftGroupEntity> softGroups = softPerformanceService
				.getSoftGroupUsers(page, limit, staffId, personType, projectName);
		count = softGroups.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("list", softGroups.getList());
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("staffName", staffName);
		request.setAttribute("staffId", staffId);
		request.setAttribute("personType", personType);
		request.setAttribute("projectName", projectName);
		request.setAttribute("projects", projects);
		return "softPersonTypeList";
	}
	private static List<TaskDefKeyEnum> checkEnum;
	private static List<TaskDefKeyEnum> ssEnum;
	static {
		checkEnum = new ArrayList<>();
		checkEnum.add(TaskDefKeyEnum.SOFT_TESTCHECK);
		ssEnum = new ArrayList<>();
		ssEnum.add(TaskDefKeyEnum.SOFT_SSTASK);
	}
	public String softPerformanceSubject() {
		User user = (User) request.getSession().getAttribute("user");
		List<String> users = new ArrayList<>();
		users.add(user.getId());
		ListResult<SoftPerformanceTaskVO> softPerformacneVos = softPerformanceService
				.findSoftPerformancesByUserGroupIDs(checkEnum, users, page,
						limit);
		request.setAttribute("resultList", softPerformacneVos.getList());
		count = (int) softPerformacneVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		return "softPerformanceSubject";

	}

	public String softPerformanceSubjectSS() {
		User user = (User) request.getSession().getAttribute("user");
		List<String> users = new ArrayList<>();
		users.add(user.getId());
		ListResult<SoftPerformanceTaskVO> softPerformacneVos = softPerformanceService
				.findSoftPerformancesByUserGroupIDs(ssEnum, users, page, limit);
		request.setAttribute("resultList", softPerformacneVos.getList());
		count = (int) softPerformacneVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		return "softPerformanceSubjectSS";
	}

	public String softPerformanceScoreStatistic() {
		return "softPerformanceScoreStatistic";
	}

	public String softPersonTypeAdd() {
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id)) {
			softGroupEntity = softPerformanceService
					.getSoftGroupById(Integer.parseInt(id));
		}
		return "softPersonTypeAdd";
	}

	public String softGroup() {
		if (softGroupEntity.getId() == null) {
			softGroupEntity.setIsDeleted(0);
			softGroupEntity.setAddTime(new Date());
			softPerformanceService.commonSave(softGroupEntity);
		} else {
			softPerformanceService.commonUpdate(softGroupEntity);
		}
		return "render_softPersonTypeList";
	}

	public void deleteGroup() {
		String hql = "update SoftGroupEntity s set  s.isDeleted=1 where s.id="
				+ request.getParameter("id");
		softPerformanceService.commonHql(hql);
	}

	public String showProject() {
		projectLst = softPerformanceService.getProjectLst();
		return "showProject";
	}
	public String toEditProject() {
		if (null != projectId) {
			projectVO = softPerformanceService.getProject(projectId);
		}
		return "toEditProject";
	}
	public String showProjectDetail() {
		projectVO = softPerformanceService.getProject(projectId);
		List<ProjectVersionEntity> projectVersions = softPerformanceService
				.getProjectVersionLst(projectId);
		String versions = "";
		for (int i = 0; i < projectVersions.size(); i++) {
			if (i == 0) {
				versions += projectVersions.get(i).getVersion();
			} else {
				versions += "; " + projectVersions.get(i).getVersion();
			}
		}
		projectVO.setVersions(versions);
		List<ProjectModuleEntity> projectModules = softPerformanceService
				.getProjectModuleLst(projectId);
		String modules = "";
		for (int i = 0; i < projectModules.size(); i++) {
			if (i == 0) {
				modules += projectModules.get(i).getModule();
			} else {
				modules += "; " + projectModules.get(i).getModule();
			}
		}
		projectVO.setModules(modules);
		return "showProjectDetail";
	}
	public String saveProject() {
		try {
			if (null != projectVO.getId()) {
				softPerformanceService.updateProject(projectVO);
			} else {
				projectVO.setCreatorId(loginUserId);
				softPerformanceService.addProject(projectVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showProject";
	}
	public String deleteProject() {
		try {
			softPerformanceService.deleteProject(projectId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showProject";
	}
	public void saveProjectVersion() {
		Map<String, String> resultMap = new HashMap<>();
		String creatorId = ((User) request.getSession().getAttribute("user"))
				.getId();
		try {
			if (null != projectVersion.getId()) {
				resultMap.put("edit", "true");
				// 判断是否存在
				if (softPerformanceService.isExistProjectVersion(
						projectVersion.getVersion(),
						projectVersion.getProjectId() + "", projectVersion.getId()+"")) {
					resultMap.put("exist", "true");
				} else {
					String[] developers = projectVersion.getDeveloper();
					projectVersion.setDeveloperNum(developers.length);
					String developerStr = Arrays.toString(developers);
					developerStr = developerStr.substring(1, developerStr.length()-1);
					projectVersion.setDevelopers(developerStr);

					String pms = Arrays.toString(projectVersion.getPm());
					projectVersion.setPmNum(projectVersion.getPm().length);
					pms = pms.substring(1, pms.length()-1);
					projectVersion.setPms(pms);

					String fenXis = Arrays.toString(projectVersion.getFenXi());
					projectVersion.setFenXiNum(projectVersion.getFenXi().length);
					fenXis = fenXis.substring(1, fenXis.length()-1);
					projectVersion.setFenXis(fenXis);

					String testers = Arrays.toString(projectVersion.getTester());
					projectVersion.setTesterNum(projectVersion.getTester().length);
					testers = testers.substring(1, testers.length()-1);
					projectVersion.setTesters(testers);

					String shiShis = Arrays.toString(projectVersion.getShiShi());
					projectVersion.setShiShiNum(projectVersion.getShiShi().length);
					shiShis = shiShis.substring(1, shiShis.length()-1);
					projectVersion.setShiShis(shiShis);

					softPerformanceService.updateProjectVersion(projectVersion);
					resultMap.put("exist", "false");
				}
			} else {
				// 判断是否存在
				if (softPerformanceService.isExistProjectVersion(
						projectVersion.getVersion(), projectId, "")) {
					resultMap.put("exist", "true");
				} else {
					projectVersion.setCreatorId(creatorId);
					projectVersion.setProjectId(Integer.parseInt(projectId));

					String[] developers = projectVersion.getDeveloper();
					projectVersion.setDeveloperNum(developers.length);
					String developerStr = Arrays.toString(developers);
					developerStr = developerStr.substring(1, developerStr.length()-1);
					projectVersion.setDevelopers(developerStr);

					String pms = Arrays.toString(projectVersion.getPm());
					projectVersion.setPmNum(projectVersion.getPm().length);
					pms = pms.substring(1, pms.length()-1);
					projectVersion.setPms(pms);

					String fenXis = Arrays.toString(projectVersion.getFenXi());
					projectVersion.setFenXiNum(projectVersion.getFenXi().length);
					fenXis = fenXis.substring(1, fenXis.length()-1);
					projectVersion.setFenXis(fenXis);

					String testers = Arrays.toString(projectVersion.getTester());
					projectVersion.setTesterNum(projectVersion.getTester().length);
					testers = testers.substring(1, testers.length()-1);
					projectVersion.setTesters(testers);

					String shiShis = Arrays.toString(projectVersion.getShiShi());
					projectVersion.setShiShiNum(projectVersion.getShiShi().length);
					shiShis = shiShis.substring(1, shiShis.length()-1);
					projectVersion.setShiShis(shiShis);
					softPerformanceService.addProjectVersion(projectVersion);
					resultMap.put("exist", "false");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void saveProjectModule() {
		String creatorId = ((User) request.getSession().getAttribute("user"))
				.getId();
		Map<String, String> resultMap = new HashMap<>();
		try {
			if (null != projectModule.getId()) {
				resultMap.put("edit", "true");
				// 判断是否存在
				if (softPerformanceService.isExistProjectModule(
						projectModule.getModule(),
						projectModule.getProjectId() + "")) {
					resultMap.put("exist", "true");
				} else {
					softPerformanceService.updateProjectModule(projectModule);
					resultMap.put("exist", "false");
				}
			} else {
				// 判断是否存在
				if (softPerformanceService.isExistProjectModule(
						projectModule.getModule(), projectId)) {
					resultMap.put("exist", "true");
				} else {
					projectModule.setCreatorId(creatorId);
					projectModule.setProjectId(Integer.parseInt(projectId));
					softPerformanceService.addProjectModule(projectModule);
					resultMap.put("exist", "false");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String showRequirement() {
		User user = (User)request.getSession().getAttribute("user");
		Map<String, String> projectVersionsMap = softPerformanceService
				.getProjectVersionsMap(user.getId(), true);
		// 版本管理的权限：产品经理/需求分析/软件测试/实施
		List<String> types = new ArrayList<>();
		types.add("产品经理");
		types.add("需求分析");
		types.add("软件测试");
		types.add("实施");
		getHasRightProject(types, projectVersionsMap);
		JSONObject jsonObject = JSONObject.fromObject(projectVersionsMap);
		projectVersions = jsonObject.toString();
		int problemOrderTaskCount = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
				.taskDefinitionKey(TaskDefKeyEnum.PROBLEM_ORDER_ALLOCATE.getName()).count();
		request.getSession().setAttribute("problemOrderTaskCount", problemOrderTaskCount);
		return "showRequirement";
	}

	public String showRequirementCopy() {
		Map<String, String> projectVersionsMap = softPerformanceService
				.getProjectVersionsMap(null, false);
		// 分配任务的权限：组长
		List<String> types = new ArrayList<>();
		types.add("组长");
		types.add("产品经理");
		getHasRightProject(types, projectVersionsMap);
		JSONObject jsonObject = JSONObject.fromObject(projectVersionsMap);

		projectVersions = jsonObject.toString();
		return "showRequirementCopy";
	}
	@Setter
	@Getter
	private String priority;
	@Setter
	@Getter
	private String requirementName;

	public String showPreparedRequirement() {
		projects = softPerformanceService.getProjectLst();
		String[] query = {priority, requirementName, projectId};
		ListResult<RequirementVo> lstResult = softPerformanceService
				.getPreparedRequirementLst(query, limit, page);
		count = lstResult.getTotalCount(); 
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		startIndex = (page - 1) * limit + 1;
		List<RequirementVo> requirementLst = lstResult.getList();
		request.setAttribute("requirementLst", requirementLst);
		return "showPreparedRequirement";
	}
	public String divideRequireManage() {
		Map<String, String> projectVersionsMap = softPerformanceService
				.getProjectVersionsMap(null, false);
		// 任务分解的权限：产品经理、组长
		List<String> types = new ArrayList<>();
		types.add("产品经理");
		types.add("组长");
		getHasRightProject(types, projectVersionsMap);
		JSONObject jsonObject = JSONObject.fromObject(projectVersionsMap);
		projectVersions = jsonObject.toString();
		return "divideRequireManage";
	}
	public void showRequirementLst() {
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		ListResult<RequirementVo> lstResult;
		try {
			lstResult = softPerformanceService.getRequirementLst(limit, page,
					versionId);
			count = lstResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			startIndex = (page - 1) * limit + 1;
			List<RequirementVo> requirementLst = lstResult.getList();
			resultMap.put("requirementLst", requirementLst);
			resultMap.put("totalPage", totalPage);
			resultMap.put("startIndex", startIndex);
			resultMap.put("limit", limit);
			resultMap.put("page", page);
			resultMap.put("count", count);
			//分解的总工时
			double usedWorkHour = softPerformanceService.getUsedWorkHour(versionId);
			//问题单分配的工时
			double problemOrderWorkHour = softPerformanceService.getProblemOrderWorkHour(versionId);
			resultMap.put("usedWorkHour", (usedWorkHour+problemOrderWorkHour));
			List<Object> hoursByPerson = softPerformanceService.getHoursByPerson(versionId);
			resultMap.put("hoursByPerson", hoursByPerson);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void getDivideRequirementLst() {
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		if(StringUtils.isBlank(versionId)){
			return;
		}
		String status = request.getParameter("status");
		String number = request.getParameter("number");
		String chooseP = request.getParameter("chooseP");
		String requireName = request.getParameter("requireName");
		ListResult<RequirementVo> lstResult;
		try {
			lstResult = softPerformanceService.getDivideRequirementLst(limit,
					page, versionId, status,number,chooseP,requireName);
			count = lstResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			startIndex = (page - 1) * limit + 1;
			List<RequirementVo> requirementLst = lstResult.getList();
			resultMap.put("requirementLst", requirementLst);
			resultMap.put("totalPage", totalPage);
			resultMap.put("startIndex", startIndex);
			resultMap.put("limit", limit);
			resultMap.put("page", page);
			resultMap.put("count", count);
			ProjectVersionEntity version = softPerformanceService.getProjectVersion(versionId);
			int developerNum = version.getDeveloperNum()==null ? 0:version.getDeveloperNum();
			double workHour = Double.parseDouble(version.getWorkHour()==null ? "0":version.getWorkHour());
			String beginDateStr = version.getBeginDate();
			String endDateStr = version.getEndDate();
			int days = 0;
			if(null!=beginDateStr && null!=endDateStr){
				Date beginDate = DateUtil.getSimpleDate(beginDateStr);
				Date endDate = DateUtil.getSimpleDate(endDateStr);
				days = Integer.parseInt((endDate.getTime()-beginDate.getTime())/(60*60*1000*24)+"")+1;
			}
			double totalWorkHour = developerNum*workHour*days;
			resultMap.put("developerNum", developerNum);
			//总工时数
			resultMap.put("totalWorkHour", totalWorkHour);
			//已分配的工时
			double usedWorkHour = softPerformanceService.getUsedWorkHour(versionId);
			//问题单分配的工时
			double problemOrderWorkHour = softPerformanceService.getProblemOrderWorkHour(versionId);
			//剩余工时
			double remainHours = totalWorkHour - (usedWorkHour+problemOrderWorkHour);
			BigDecimal b = new BigDecimal(remainHours);  
			remainHours = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			resultMap.put("remainHours", remainHours);

			List<Object> hoursByPerson = softPerformanceService.getHoursByPerson(versionId);
			resultMap.put("hoursByPerson", hoursByPerson);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void showDivideRequirementLstForPage() {
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		String status = request.getParameter("status");
		String number = request.getParameter("number");
		String chooseP = request.getParameter("chooseP");
		String requireName = request.getParameter("requireName");
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		ListResult<RequirementVo> lstResult;
		try {
			lstResult = softPerformanceService.getDivideRequirementLst(limit,
					page, versionId, status,number,chooseP,requireName);
			int total = lstResult.getTotalCount();
			int totalPage = total % limit == 0
					? total / limit
							: total / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			int startIndex = (page - 1) * limit + 1;
			List<RequirementVo> requirementLst = lstResult.getList();
			resultMap.put("requirementLst", requirementLst);
			resultMap.put("totalPage", totalPage);
			resultMap.put("startIndex", startIndex);
			resultMap.put("limit", limit);
			resultMap.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void showRequirementListForAllocation() {
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		ListResult<RequirementVo> lstResult;
		try {
			lstResult = softPerformanceService.getRequirementLst(limit, page,
					versionId);
			int total = lstResult.getTotalCount();
			totalPage = total % limit == 0 ? total / limit : total / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			startIndex = (page - 1) * limit + 1;
			List<RequirementVo> requirementLst = lstResult.getList();
			resultMap.put("requirementLst", requirementLst);
			resultMap.put("totalPage", totalPage);
			resultMap.put("startIndex", startIndex);
			resultMap.put("limit", limit);
			resultMap.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}

	public void showRequirementLstForPage() {
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		ListResult<RequirementVo> lstResult;
		try {
			lstResult = softPerformanceService.getRequirementLst(limit, page,
					versionId);
			int total = lstResult.getTotalCount();
			int totalPage = total % limit == 0
					? total / limit
							: total / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			int startIndex = (page - 1) * limit + 1;
			List<RequirementVo> requirementLst = lstResult.getList();
			resultMap.put("requirementLst", requirementLst);
			resultMap.put("totalPage", totalPage);
			resultMap.put("startIndex", startIndex);
			resultMap.put("limit", limit);
			resultMap.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String toEditRequirement() {
		if (null != requirementId) {
			isEdit = true;
			requirementEntity = softPerformanceService
					.getRequirement(requirementId);
			projects = softPerformanceService.getProjectLst();
			int projectId = requirementEntity.getProjectId();
			modules = softPerformanceService
					.getProjectModuleLst(projectId + "");
			versions = softPerformanceService
					.getUnCompletedVersionLst(projectId + "");
		} else {
			projects = softPerformanceService.getProjectLst();
			if (projects.size() > 0) {
				ProjectVO project = projects.get(0);
				modules = softPerformanceService
						.getProjectModuleLst(project.getId() + "");
				versions = softPerformanceService
						.getUnCompletedVersionLst(project.getId() + "");
			}
		}
		return "toEditRequirement";
	}
	public String toEditPreparedRequirement() {
		if (null != requirementId) {
			isEdit = true;
			requirementEntity = softPerformanceService
					.getRequirement(requirementId);
			projects = softPerformanceService.getProjectLst();
		} else {
			projects = softPerformanceService.getProjectLst();
		}
		return "toEditPreparedRequirement";
	}
	public void changeProject() {
		String projectId = request.getParameter("project");
		List<ProjectModuleEntity> modules = softPerformanceService
				.getProjectModuleLst(projectId);
		List<ProjectVersionEntity> versions = softPerformanceService
				.getUnCompletedVersionLst(projectId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("modules", modules);
		resultMap.put("versions", versions);
		printByJson(resultMap);
	}
	public void changeProjectForShowTask() {
		String projectId = request.getParameter("project");
		List<ProjectModuleEntity> modules = softPerformanceService
				.getProjectModuleLst(projectId);
		List<ProjectVersionEntity> versions = softPerformanceService
				.getProjectVersionLst(projectId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("modules", modules);
		resultMap.put("versions", versions);
		printByJson(resultMap);
	}
	public void changeProjectForTask() {
		String projectId = request.getParameter("project");
		List<ProjectModuleEntity> modules = softPerformanceService
				.getProjectModuleLst(projectId);
		List<ProjectVersionEntity> versions = softPerformanceService
				.getProjectVersionLst(projectId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("modules", modules);
		resultMap.put("versions", versions);
		if (modules.size() > 0 && versions.size() > 0) {
			List<RequirementEntity> associatedRequires = softPerformanceService
					.getRequirements(versions.get(0).getId(),
							modules.get(0).getId());
			resultMap.put("associatedRequires", associatedRequires);
		}
		printByJson(resultMap);
	}
	public String saveRequirement() {
		try {
			if (null != requirementEntity.getId()) {
				if (null != files) {
					String[] fileNames = fileDetail.split("#@#&");
					softPerformanceService.updateRequirement(requirementEntity,
							files, fileNames);
				} else {
					softPerformanceService.updateRequirement(requirementEntity,
							files, null);
				}
			} else {
				requirementEntity.setCreatorId(loginUserId);
				requirementEntity.setStatus(Constants.NOT_ACTIVE);
				requirementEntity.setStage(Constants.READY_DEVELOP);
				requirementEntity.setDivide(0);
				if (null != files) {
					String[] fileNames = fileDetail.split("#@#&");
					softPerformanceService.addRequirement(requirementEntity,
							files, fileNames);
				} else {
					softPerformanceService.addRequirement(requirementEntity,
							files, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showRequirement";
	}
	@Autowired
	private RuntimeService runtimeService;
	public void endSoftTask() {
		String processInstanceId = request.getParameter("instanceId");
		runtimeService.deleteProcessInstance(processInstanceId, "complete");
		FunctionEntity functionEntity = softPerformanceService
				.geFunctionEntityByInstanceId(processInstanceId);
		functionEntity.setResult(TaskResultEnum.END.getValue());
		softPerformanceService.commonUpdate(functionEntity);
		String requireId = softPerformanceService.getRequireIdByInstanceId(processInstanceId);
		//需求没有分配任务，更新需求的状态
		if(!softPerformanceService.checkRequireIsAllot(requireId)){
			softPerformanceService.updateRequireStatus(requireId, Constants.READY_DEVELOP);
			//需求状态为 未分解
			softPerformanceService.updateRequireDivide(requireId, 0);
			//删除子需求
			softPerformanceService.deleteSubRequirement(requireId);
		}
	}

	public String showTask() {
		projects = softPerformanceService.getProjectLst();
		if (projects.size() > 0) {
			ProjectVO project = projects.get(0);
			modules = softPerformanceService
					.getProjectModuleLst(project.getId() + "");
			versions = softPerformanceService
					.getProjectVersionLst(project.getId() + "");
		}
		User user = (User) request.getSession().getAttribute("user");
		ListResult<FunctionVo> lstResult = softPerformanceService
				.getFuntions(limit, page, user.getId());
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		taskLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showTask";
	}
	public String singleTask() {
		projects = softPerformanceService.getProjectLst();
		if (projects.size() > 0) {
			ProjectVO project = projects.get(0);
			modules = softPerformanceService
					.getProjectModuleLst(project.getId() + "");
			versions = softPerformanceService
					.getProjectVersionLst(project.getId() + "");
			if (modules.size() > 0 && versions.size() > 0) {
				associatedRequires = softPerformanceService.getRequirements(
						versions.get(0).getId(), modules.get(0).getId());
			}
		}
		return "singleTask";
	}

	public String allocateTask() {
		projects = softPerformanceService.getProjectLst();
		String subRequirementId = request.getParameter("subRequireId");
		String requirementId = request.getParameter("requirementId");
		String projectId = softPerformanceService.getProjectId(requirementId);
		modules = softPerformanceService.getProjectModuleLst(projectId);
		versions = softPerformanceService.getProjectVersionLst(projectId);
		softPerformanceVo = softPerformanceService.getTaskVo(subRequirementId);
		request.setAttribute("requirementId", Integer.parseInt(requirementId));
		request.setAttribute("subRequirementId",
				Integer.parseInt(subRequirementId));
		return "allocateTask";
	}

	public void changeProjectVersionOrModule() {
		String versionId = request.getParameter("versionId");
		String moduleId = request.getParameter("moduleId");
		if (null != versionId && null != moduleId) {
			List<RequirementEntity> associatedRequires = softPerformanceService
					.getRequirements(Integer.parseInt(versionId),
							Integer.parseInt(moduleId));
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("associatedRequires", associatedRequires);
			printByJson(resultMap);
		}
	}
	public String saveSingleTask() {
		User user = (User) request.getSession().getAttribute("user");
		softPerformanceVo.setCreatorId(user.getId());
		String userName = staffService.getRealNameByUserId(user.getId());
		softPerformanceVo.setCreatorName(userName);
		softPerformanceVo.setUserID(user.getId());
		softPerformanceVo.setUserName(userName);
		saveTask(softPerformanceVo, files, fileDetail);
		// 更新需求状态
		softPerformanceService
		.updateRequireStatus(softPerformanceVo.getRequirementId());
		/*
		 * if(Constants.SINGLE_TASK.equalsIgnoreCase(nextStep)){ projects =
		 * softPerformanceService.getProjectLst(); int projectId =
		 * softPerformanceVo.getProjectId(); modules =
		 * softPerformanceService.getProjectModuleLst(projectId+""); versions =
		 * softPerformanceService.getProjectVersionLst(projectId+""); int
		 * moduleId = softPerformanceVo.getModuleId(); int versionId =
		 * softPerformanceVo.getProjectVersionId(); if(modules.size()>0 &&
		 * versions.size()>0){ associatedRequires =
		 * softPerformanceService.getRequirements(versionId, moduleId); } }
		 */
		requirementId = softPerformanceVo.getRequirementId() + "";
		softPerformanceService.updateSubRequireDeveloper(softPerformanceVo.getAssignerName(),
				softPerformanceVo.getSubRequirementId()+"");
		return "render_getSubRequirement";
	}
	@Getter
	@Setter
	private SoftPerformanceVo softPerformanceVo;
	private void saveTask(SoftPerformanceVo softPermanceVo, File[] files,
			String fileDetail) {
		softPerformanceService.addFunctionVo(softPermanceVo, files, fileDetail);
	}
	public String showRequirementDetail() {
		String requirementId = request.getParameter("requirementId");
		requirementVo = softPerformanceService
				.getRequirementDetail(requirementId);
		request.setAttribute("fromPreparedRequire",
				request.getParameter("fromPreparedRequire"));
		request.setAttribute("isOther", request.getParameter("isOther"));
		subRequirementLst = softPerformanceService
				.getSubRequirementLst(requirementId);
		return "showRequirementDetail";
	}
	public String downloadAttachment() {
		try {
			String instanceId = request.getParameter("instanceId");
			if (instanceId != null) {
				int index = Integer.parseInt(request.getParameter("index"));
				List<Attachment> attachments = taskService
						.getProcessInstanceAttachments(instanceId);
				if (attachments.size() > 0) {
					String attachmentName = attachments.get(index).getName();
					// 解决中文乱码
					downloadFileFileName = new String(attachmentName.getBytes(),
							"ISO-8859-1");
					inputStream = taskService.getAttachmentContent(
							attachments.get(index).getId());
				}
			} else {
				String attachmentPath = request.getParameter("attachmentPath");
				String attachmentName = request.getParameter("attachmentName");
				inputStream = new FileInputStream(new File(attachmentPath));
				// 解决中文乱码
				downloadFileFileName = new String(attachmentName.getBytes(),
						"ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());

		}
		return "downloadAttachment";
	}
	public void deleteAttachment() {
		String requirementId = request.getParameter("requirementId");
		String attachmentName = request.getParameter("attachmentName");
		String attachmentPath = request.getParameter("attachmentPath");
		Map<String, String> resultMap = new HashMap<>();
		try {
			softPerformanceService.deleteAttachment(requirementId,
					attachmentName, attachmentPath, Constants.REQUIRE_TABLE);
			resultMap.put("flag", "true");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("flag", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String deleteRequirement() {
		String requirementId = request.getParameter("requirementId");
		try {
			softPerformanceService.deleteRequiremnet(requirementId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showRequirement";
	}
	public String deletePreparedRequirement() {
		String requirementId = request.getParameter("requirementId");
		try {
			softPerformanceService.deleteRequiremnet(requirementId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showPreparedRequirement";
	}
	public String batchTask() {
		projects = softPerformanceService.getProjectLst();
		if (projects.size() > 0) {
			ProjectVO project = projects.get(0);
			modules = softPerformanceService
					.getProjectModuleLst(project.getId() + "");
			versions = softPerformanceService
					.getProjectVersionLst(project.getId() + "");
			if (modules.size() > 0 && versions.size() > 0) {
				associatedRequires = softPerformanceService.getRequirements(
						versions.get(0).getId(), modules.get(0).getId());
			}
		}
		return "batchTask";
	}
	public void getModulesAndRequires() {
		String projectId = request.getParameter("projectId");
		String versionId = request.getParameter("versionId");
		List<ProjectModuleEntity> modules = softPerformanceService
				.getProjectModuleLst(projectId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("modules", modules);
		if (modules.size() > 0 && StringUtils.isNotBlank(versionId)) {
			List<RequirementEntity> associatedRequires = softPerformanceService
					.getRequirements(Integer.parseInt(versionId),
							modules.get(0).getId());
			resultMap.put("associatedRequires", associatedRequires);
		}
		printByJson(resultMap);
	}
	public void changeProjectVersion() {
		String versionId = request.getParameter("versionId");
		String moduleId = request.getParameter("moduleId");
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isNotBlank(versionId)
				&& StringUtils.isNotBlank(moduleId)) {
			List<RequirementEntity> associatedRequires = softPerformanceService
					.getRequirements(Integer.parseInt(versionId),
							Integer.parseInt(moduleId));
			resultMap.put("associatedRequires", associatedRequires);
		}
		printByJson(resultMap);
	}
	public String saveBatchTask() {
		User user = (User) request.getSession().getAttribute("user");
		Integer[] requirementId = batchTaskVo.getRequirementId();
		String[] requirementNames = batchTaskVo.getRequirementName();
		String[] moduleNames = batchTaskVo.getModuleName();
		String lastRequirmentName = "";
		for (int i = 0; i < requirementNames.length; i++) {
			if ("同上".equals(requirementNames[i])) {
				requirementNames[i] = lastRequirmentName;
			}
			lastRequirmentName = requirementNames[i];
		}
		String lastModuleName = "";
		for (int i = 0; i < moduleNames.length; i++) {
			if ("同上".equals(moduleNames[i])) {
				moduleNames[i] = lastModuleName;
			}
			lastModuleName = moduleNames[i];
		}
		for (int i = 0; i < requirementId.length; i++) {
			SoftPerformanceVo softPerformanceVo = new SoftPerformanceVo();
			softPerformanceVo.setProjectId(batchTaskVo.getProjectId());
			softPerformanceVo.setProjectVersionId(batchTaskVo.getVersionId());
			softPerformanceVo.setAssignerName(batchTaskVo.getAssignerName()[i]);
			softPerformanceVo.setAssignerId(batchTaskVo.getAssignerId()[i]);
			softPerformanceVo.setDeadline(batchTaskVo.getDeadLine()[i]);
			softPerformanceVo.setDescription(batchTaskVo.getDescription()[i]);
			softPerformanceVo
			.setEstimatedTime(batchTaskVo.getEstimatedTime()[i]);
			softPerformanceVo.setModuleId(batchTaskVo.getModuleId()[i]);
			softPerformanceVo.setName(batchTaskVo.getName()[i]);
			softPerformanceVo.setPriority(batchTaskVo.getPriority()[i]);
			softPerformanceVo
			.setRequirementId(batchTaskVo.getRequirementId()[i]);
			softPerformanceVo.setTaskType(batchTaskVo.getTaskType()[i]);
			softPerformanceVo.setScore(batchTaskVo.getScore()[i]);
			softPerformanceVo.setCreatorId(user.getId());
			softPerformanceVo.setUserID(user.getId());
			String userName = staffService.getRealNameByUserId(user.getId());
			softPerformanceVo.setUserName(userName);
			softPerformanceVo.setCreatorName(userName);
			softPerformanceVo.setProjectName(batchTaskVo.getProjectName());
			softPerformanceVo.setVersionName(batchTaskVo.getVersionName());
			softPerformanceVo.setModuleName(moduleNames[i]);
			softPerformanceVo.setRequirementName(requirementNames[i]);
			saveTask(softPerformanceVo, files, fileDetail);
			// 更新需求状态
			softPerformanceService
			.updateRequireStatus(softPerformanceVo.getRequirementId());
		}

		return "render_showTask";
	}
	public String showTaskDetail() {
		String taskId = request.getParameter("taskId");
		String instanceId = request.getParameter("instanceId");
		functionVo = softPerformanceService.getFuntionDetail(taskId);
		FunctionEntity functionEntity = softPerformanceService
				.geFunctionEntityByInstanceId(instanceId);
		byte[] bytes = functionEntity.getVoDetail();
		if (bytes != null && bytes.length > 0) {
			Object object = null;
			try {
				object = ObjectByteArrTransformer.toObject(bytes);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SoftPerformanceVo functionVo = (SoftPerformanceVo) object;
			List<FormField> formFields = functionVo.getFormFields();
			request.setAttribute("formFields", formFields);
			request.setAttribute("isVo", "1");
		}
		request.setAttribute("isFromScore",
				request.getParameter("isFromScore"));
		request.setAttribute("isFromManage",
				request.getParameter("isFromManage"));
		request.setAttribute("isFromStatistic",
				request.getParameter("isFromStatistic"));
		request.setAttribute("isFromScoreManage",
				request.getParameter("isFromScoreManage"));
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(instanceId);
		List<CommentVO> comments = processService
				.getCommentsByProcessInstanceID(instanceId);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		return "showTaskDetail";
	}
	public String deleteTask() {
		String taskId = request.getParameter("taskId");
		try {
			softPerformanceService.deleteTask(taskId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showTask";
	}
	public String showTaskByConditions() {
		String projectId = functionVo.getProject();
		projects = softPerformanceService.getProjectLst();
		modules = softPerformanceService.getProjectModuleLst(projectId);
		versions = softPerformanceService.getProjectVersionLst(projectId);
		User user = (User) request.getSession().getAttribute("user");
		ListResult<FunctionVo> lstResult = softPerformanceService
				.getFuntions(limit, page, functionVo, user.getId());
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		taskLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showTask";
	}
	public String showVersion() {
		ListResult<VersionVo> lstResult = softPerformanceService
				.getVersions(page, limit, projectId);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		versionLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showVersion";
	}
	public String showModule() {
		ListResult<ModuleVo> lstResult = softPerformanceService.getModules(page,
				limit, projectId);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		moduleLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showModule";
	}
	public String deleteVersion() {
		String versionId = request.getParameter("versionId");
		projectId = request.getParameter("projectId");
		try {
			softPerformanceService.deleteProjectVersion(versionId);
			String updatestVersion = softPerformanceService
					.getUpdatestVersion(projectId);
			// 更新项目的最新版本
			softPerformanceService.updateProjectUpdatestVersion(projectId,
					updatestVersion);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showVersion";
	}
	public String deleteModule() {
		String moduleId = request.getParameter("moduleId");
		projectId = request.getParameter("projectId");
		try {
			softPerformanceService.deleteProjectModule(moduleId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showModule";
	}
	public String toEditTask() {
		String instanceId = request.getParameter("instanceId");
		softPerformanceVo = softPerformanceService.getFuntion(instanceId);
		projects = softPerformanceService.getProjectLst();
		Integer projectId = softPerformanceVo.getProjectId();
		modules = softPerformanceService.getProjectModuleLst(projectId + "");
		versions = softPerformanceService.getProjectVersionLst(projectId + "");
		Integer moduleId = softPerformanceVo.getModuleId();
		Integer versionId = softPerformanceVo.getProjectVersionId();
		associatedRequires = softPerformanceService.getRequirements(versionId,
				moduleId);
		attachmentLst = softPerformanceService.getAttachmentLst(instanceId);
		return "toEditTask";
	}
	public void deleteTaskAttachment() {
		String attachmentName = request.getParameter("attachmentName");
		String instanceId = request.getParameter("instanceId");
		Map<String, String> resultMap = new HashMap<>();
		try {
			softPerformanceService.deleteTaskAttachment(attachmentName,
					instanceId);
			resultMap.put("flag", "true");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("flag", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String saveDeductedScore() {
		double deductedScore = scoreResultVo.getResultScore();
		// 转换为负数
		deductedScore = 0 - deductedScore;
		scoreResultVo.setResultScore(deductedScore);
		ProjectVersionEntity version = softPerformanceService.getProjectVersion(scoreResultVo.getVersionId());
		scoreResultVo.setItemDate(DateUtil.getSimpleDate(version.getEndDate()));
		softPerformanceService.saveDeductedScore(scoreResultVo);
		return "render_showScore";
	}
	public String showScore() {
		ListResult<Object> lstResult = softPerformanceService
				.getScoreResult(limit, page, null, null, null);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		startIndex = (page - 1) * limit + 1;
		request.setAttribute("list", lstResult.getList());
		return "showScore";
	}
	public String deleteScore() {
		String resultId = request.getParameter("id");
		softPerformanceService.deleteResultScore(resultId);
		return "render_showScore";
	}
	/**
	 * 检查扣分项是否为当月，只有当月的才可删除
	 */
	public void checkCanDeleteScore() {
		Map<String, String> resultMap = new HashMap<>();
		String resultId = request.getParameter("id");
		if (softPerformanceService.isInCurrentMonth(resultId)) {
			resultMap.put("canDelete", "true");
		} else {
			resultMap.put("canDelete", "false");
		}
		printByJson(resultMap);
	}
	/*	public void getScores() {
		String userId = request.getParameter("userId");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String scores = softPerformanceService.getScores(userId, beginDate,
				endDate);
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("scores", scores);
		printByJson(resultMap);
	}*/
	public String getScores() {
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String scores = softPerformanceService.getScores(userId, beginDate,
				endDate);
		ListResult<Object> lstResult = softPerformanceService.getScoreResult(limit, page, userId, beginDate, endDate);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		startIndex = (page - 1) * limit + 1;
		request.setAttribute("list", lstResult.getList());
		request.setAttribute("scores", scores);
		request.setAttribute("userId", userId);
		request.setAttribute("userName", userName);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		return "showScore";
	}
	public String showAllTask() {
		projects = softPerformanceService.getProjectLst();
		if (projects.size() > 0) {
			ProjectVO project = projects.get(0);
			modules = softPerformanceService
					.getProjectModuleLst(project.getId() + "");
			versions = softPerformanceService
					.getProjectVersionLst(project.getId() + "");
		}
		ListResult<FunctionVo> lstResult = softPerformanceService
				.getFuntions(limit, page, null);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		taskLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showAllTask";
	}
	public String showAllTaskByConditions() {
		String projectId = functionVo.getProject();
		projects = softPerformanceService.getProjectLst();
		modules = softPerformanceService.getProjectModuleLst(projectId);
		versions = softPerformanceService.getProjectVersionLst(projectId);
		ListResult<FunctionVo> lstResult = softPerformanceService
				.getFuntions(limit, page, functionVo, null);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		taskLst = lstResult.getList();
		startIndex = (page - 1) * limit + 1;
		return "showAllTask";
	}
	public String showSubRequirement() {
		request.setAttribute("requirementId", requirementId);
		subRequirementLst = softPerformanceService
				.getSubRequirementLst(requirementId);
		requirementVo = softPerformanceService
				.getRequirementDetail(requirementId);
		return "showSubRequirement";
	}

	public String getSubRequirement() {
		subRequirementLst = softPerformanceService
				.getSubRequirementLst(requirementId);
		request.setAttribute("requirementId", requirementId);
		return "getSubRequirement";
	}

	public String toEditSubRequire() {
		String subRequireId = request.getParameter("subRequireId");
		if (StringUtils.isNotBlank(subRequireId)) {
			subRequirementVo = softPerformanceService
					.getSubRequirement(subRequireId);
			isEdit = true;
		}
		return "toEditSubRequire";
	}
	public String saveSubRequirement() {
		try {
			if (null != subRequirementVo.getId()) {
				if (null != files) {
					// 前台按#@#&连接的
					String[] fileNames = fileDetail.split("#@#&");
					softPerformanceService.updateSubRequirement(
							subRequirementVo, files, fileNames);
				} else {
					softPerformanceService.updateSubRequirement(
							subRequirementVo, files, null);
				}
			} else {
				subRequirementVo
				.setRequirementId(Integer.parseInt(requirementId));
				if (null != files) {
					String[] fileNames = fileDetail.split("#@#&");
					softPerformanceService.addSubRequirement(subRequirementVo,
							files, fileNames);
				} else {
					softPerformanceService.addSubRequirement(subRequirementVo,
							files, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showSubRequirement";
	}
	public String deleteSubRequire() {
		String subRequireId = request.getParameter("subRequireId");
		String hql = "update SubReqirementEntity set isDeleted=1 where id="
				+ subRequireId;
		softPerformanceService.commonHql(hql);
		return "render_showSubRequirement";
	}
	public void deleteAttachmentForSubRequire() {
		String subRequireId = request.getParameter("subRequireId");
		String attachmentName = request.getParameter("attachmentName");
		String attachmentPath = request.getParameter("attachmentPath");
		Map<String, String> resultMap = new HashMap<>();
		try {
			softPerformanceService.deleteAttachment(subRequireId,
					attachmentName, attachmentPath,
					Constants.SUB_REQUIRE_TABLE);
			resultMap.put("flag", "true");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("flag", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void showSubRequirementLst() {
		String requirementId = request.getParameter("requirementId");
		subRequirementLst = softPerformanceService
				.getSubRequirementLst(requirementId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("subRequirementLst", subRequirementLst);
		printByJson(resultMap);
	}
	public void checkIsCreateTask() {
		Map<String, Object> resultMap = new HashMap<>();
		// 判断是否已创建任务的依据
		String byType = request.getParameter("byType");
		if (Constants.VERSION.equals(byType)) {
			String versionId = request.getParameter("versionId");
			if (softPerformanceService.isCreateTaskByVersion(versionId)) {
				resultMap.put("exist", "true");
			} else {
				resultMap.put("exist", "false");
			}
		} else if (Constants.REQUIRE.equals(byType)) {
			String requireId = request.getParameter("requireId");
			if (softPerformanceService.isCreateTaskByRequire(requireId)) {
				resultMap.put("exist", "true");
			} else {
				resultMap.put("exist", "false");
			}
		}
		printByJson(resultMap);
	}
	public void showTaskByRequire() {
		String requirementId = request.getParameter("requirementId");
		Map<String, Object> resultMap = new HashMap<>();
		List<Object> taskLst = softPerformanceService
				.getTaskLstByRequire(requirementId);
		resultMap.put("taskLst", taskLst);
		printByJson(resultMap);
	}
	public String confirmCompleteRequire() {
		String requirementId = request.getParameter("requireId");
		softPerformanceService.completeRequire(requirementId);
		return "render_showRequirement";
	}
	public void showTaskByVersion() {
		String versionId = request.getParameter("versionId");
		Map<String, List<FunctionVo>> requireAndTaskLstMap = softPerformanceService
				.getTaskLstByVersion(versionId);
		JSONObject jsonObject = JSONObject.fromObject(requireAndTaskLstMap);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("requireAndTaskLstMap", jsonObject.toString());
		printByJson(resultMap);
	}
	public String confirmCompleteVersion() {
		String versionId = request.getParameter("versionId");
		softPerformanceService.completeVersion(versionId);
		return "render_showRequirement";
	}
	public String savePreparedRequirement() {
		try {
			if (null != requirementEntity.getId()) {
				if (null != files) {
					String[] fileNames = fileDetail.split("#@#&");
					//编辑，取消驳回标识
					requirementEntity.setBack(0);
					softPerformanceService.updateRequirement(requirementEntity,
							files, fileNames);
				} else {
					requirementEntity.setBack(0);
					softPerformanceService.updateRequirement(requirementEntity,
							files, null);
				}
			} else {
				requirementEntity.setCreatorId(loginUserId);
				requirementEntity.setStatus(Constants.NOT_ACTIVE);
				requirementEntity.setStage(Constants.READY_DEVELOP);
				requirementEntity.setDivide(0);
				requirementEntity.setBack(0);
				if (null != files) {
					String[] fileNames = fileDetail.split("#@#&");
					softPerformanceService.addRequirement(requirementEntity,
							files, fileNames);
				} else {
					softPerformanceService.addRequirement(requirementEntity,
							files, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showPreparedRequirement";
	}
	public void getVersionList() {
		String projectId = request.getParameter("projectId");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("versionLst",
				softPerformanceService.getUnCompletedVersionLst(projectId));
		printByJson(resultMap);
	}
	public void getModuleList() {
		String projectId = request.getParameter("projectId");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("moduleLst",
				softPerformanceService.getProjectModuleLst(projectId));
		printByJson(resultMap);
	}
	public void saveVersionOrModule() {
		String requireId = request.getParameter("requireId");
		String versionId = request.getParameter("versionId");
		String moduleId = request.getParameter("moduleId");
		softPerformanceService.saveVersionOrModule(requireId, versionId,
				moduleId);
		softPerformanceService.actRequireStatus(requireId);
	}
	/**
	 * 需求作废
	 * 
	 * @return
	 */
	public String deleteRequire() {
		String requireId = request.getParameter("requireId");
		String deleteReason = request.getParameter("deleteReason");
		softPerformanceService.deleteRequire(requireId, deleteReason);
		return "render_divideRequireManage";
	}
	/**
	 * 完成需求细分
	 * 
	 * @return
	 */
	public String completeDivide() {
		String requireId = request.getParameter("requireId");
		softPerformanceService.completeDivide(requireId);
		return "render_divideRequireManage";
	}
	public String goToTaskManage() {
		String subRequirementId = request.getParameter("subRequirementId");
		String requirementId = request.getParameter("requirementId");
		if (StringUtils.isNotBlank(subRequirementId)) {
			// 获取子需求下面的任务列表, 0表示子需求
			taskLst = softPerformanceService.getTaskLst(subRequirementId, 0);
		} else if (StringUtils.isNotBlank(requirementId)) {
			taskLst = softPerformanceService.getTaskLst(requirementId, 1);
		}
		totalPage = 1;
		startIndex = (page - 1) * limit + 1;
		return "showTask";
	}
	public void checkRight() {
		User user = (User) request.getSession().getAttribute("user");
		String projectName = request.getParameter("projectName");
		List<String> types = new ArrayList<>();
		types.add("产品经理");
		types.add("需求分析");
		Map<String, Object> resultMap = new HashMap<>();
		if (softPerformanceService.hasRight(user.getId(), projectName, types)) {
			resultMap.put("hasRight", "true");
		} else {
			resultMap.put("hasRight", "false");
		}
		printByJson(resultMap);
	}
	/**
	 * 筛选当前登录人能够查看的项目信息
	 * 
	 * @param types
	 *            能够查看的人员类型
	 * @param projectVersionsMap
	 * @return
	 */
	public void getHasRightProject(List<String> types,
			Map<String, String> projectVersionsMap) {
		User user = (User) request.getSession().getAttribute("user");
		Iterator<Entry<String, String>> it = projectVersionsMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String project = entry.getKey();
			// 无权限
			if (!softPerformanceService.hasRight(user.getId(), project,
					types)) {
				it.remove();
			}
		}
	}

	@Getter
	@Setter
	private InputStream iStream;
	@Getter
	@Setter
	private String attachmentName;
	public String downloadPic() {
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			inputStream = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(new File(commonAttachment.getSoftURL())
					.getName().getBytes("gbk"), "iso-8859-1");
			attachmentName = attachmentName.substring(32);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadPic";
	}

	@Getter
	@Setter
	private File file;
	public void attachmentSave() {
		String root = "/usr/local/download";
		File parent = new File(root + "/" + "softPerformanceAttachment");
		parent.mkdirs();
		String imageName = request.getParameter("imageName");
		String saveName = UUID.randomUUID().toString().replaceAll("-", "");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(new File(parent, saveName));
			byte[] buffer = new byte[10 * 1024 * 1024];
			int length = 0;
			while ((length = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			CommonAttachment commonAttachment = new CommonAttachment();
			commonAttachment.setAddTime(new Date());
			commonAttachment.setIsDeleted(0);
			commonAttachment.setSoftURL(
					root + "/softPerformanceAttachment/" + saveName);
			commonAttachment.setSize(
					(float) Math.round(file.length() / 1024 / 1024 * 10) / 10
					+ "");
			commonAttachment.setSoftName(imageName);
			commonAttachment.setType(AttachmentType.SOFTPERFORMANCE.getIndex());
			Integer number = noticeService.saveAttachMent(commonAttachment);
			Map<String, Integer> map = new HashMap<>();
			map.put("id", number);
			printByJson(map);
		} catch (IOException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String deductScore(){
		List<ProjectVO> projects = softPerformanceService.getProjectLst();
		request.setAttribute("projects", projects);
		return "deductScore";
	}
	public String exportVersionInfo(){
		try {
			String versionId = request.getParameter("versionId");
			String versionName = softPerformanceService.getProjectVersion(versionId).getVersion();
			String projectName = request.getParameter("projectName");
			inputStream = softPerformanceService.generateVersionInfoFile(versionId, projectName);
			downloadFileFileName = projectName+"-"+versionName+".xls";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "exportVersionInfo";
	}
	public void saveReturnReason(){
		String requirementId = request.getParameter("requirementId");
		String returnReason = request.getParameter("returnReason");
		softPerformanceService.saveReturnReason(requirementId, returnReason);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", "ok");
		printByJson(resultMap);
	}
	/**
	 * 检查需求是否已分解
	 */
	public void checkIsDivide(){
		String reqiureId = request.getParameter("requireId");
		Map<String, Object> resultMap = new HashMap<>();
		if(softPerformanceService.checkIsDivide(reqiureId)){
			resultMap.put("isDivide", "true");
		}else{
			resultMap.put("isDivide", "false");
		}
		printByJson(resultMap);
	}
	public void getVersionsByRequireId(){
		String requireId = request.getParameter("requireId");
		List<ProjectVersionEntity> versions = softPerformanceService.getProjectVersionLstByRequireId(requireId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("versions", versions);
		printByJson(resultMap);
	}
	public void saveVersion(){
		String requireId = request.getParameter("requireId");
		String versionId = request.getParameter("versionId");
		if(StringUtils.isNotBlank(versionId)){
			softPerformanceService.updateRequireVersion(requireId, versionId);
			//更改已下发任务的版本号
			softPerformanceService.updateTaskVersion(requireId, versionId);
			List<String> instanceIdList = softPerformanceService.getInstanceIdListByRequireId(requireId);
			for(String instanceId: instanceIdList){
				if(StringUtils.isNotBlank(instanceId)){
					SoftPerformanceVo softPerformanceVo = (SoftPerformanceVo)runtimeService.getVariable(instanceId, "arg");
					if(null != softPerformanceVo){
						softPerformanceVo.setProjectVersionId(Integer.parseInt(versionId));
						runtimeService.setVariable(instanceId, "arg", softPerformanceVo);
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", "ok");
		printByJson(resultMap);
	}
	public void getPersonsByProjectName(){
		Map<String, Object> resultMap = new HashMap<>();
		String projectName = request.getParameter("projectName");
		List<SoftGroupEntity> pms = softPerformanceService.getSoftPersonsAll(SoftPosition.产品经理, projectName);
		List<SoftGroupEntity> developers = softPerformanceService.getSoftPersonsAll(SoftPosition.编码人员, projectName);
		List<SoftGroupEntity> testers = softPerformanceService.getSoftPersonsAll(SoftPosition.软件测试, projectName);
		List<SoftGroupEntity> shiShis = softPerformanceService.getSoftPersonsAll(SoftPosition.实施, projectName);
		List<SoftGroupEntity> fenXis = softPerformanceService.getSoftPersonsAll(SoftPosition.需求分析, projectName);
		resultMap.put("pms", pms);
		resultMap.put("developers", developers);
		resultMap.put("testers", testers);
		resultMap.put("shiShis", shiShis);
		resultMap.put("fenXis", fenXis);
		printByJson(resultMap);
	}
	public void getPersonsByVersionId(){
		Map<String, Object> resultMap = new HashMap<>();
		String versionId = request.getParameter("versionId");
		List<StaffVO> pms = softPerformanceService.getSoftPersonsByVersion(SoftPosition.产品经理, versionId);
		List<StaffVO> developers = softPerformanceService.getSoftPersonsByVersion(SoftPosition.编码人员, versionId);
		List<StaffVO> testers = softPerformanceService.getSoftPersonsByVersion(SoftPosition.软件测试, versionId);
		List<StaffVO> shiShis = softPerformanceService.getSoftPersonsByVersion(SoftPosition.实施, versionId);
		List<StaffVO> fenXis = softPerformanceService.getSoftPersonsByVersion(SoftPosition.需求分析, versionId);
		resultMap.put("pms", pms);
		resultMap.put("developers", developers);
		resultMap.put("testers", testers);
		resultMap.put("shiShis", shiShis);
		resultMap.put("fenXis", fenXis);
		printByJson(resultMap);
	}
	public void getPartPersons(){
		String versionId = request.getParameter("versionId");
		List<String> pms = softPerformanceService.getSoftPersonUserIdsByVersion(SoftPosition.产品经理, versionId);
		List<String> developers = softPerformanceService.getSoftPersonUserIdsByVersion(SoftPosition.编码人员, versionId);
		List<String> testers = softPerformanceService.getSoftPersonUserIdsByVersion(SoftPosition.软件测试, versionId);
		List<String> shiShis = softPerformanceService.getSoftPersonUserIdsByVersion(SoftPosition.实施, versionId);
		List<String> fenXis = softPerformanceService.getSoftPersonUserIdsByVersion(SoftPosition.需求分析, versionId);
		ProjectVersionEntity version = softPerformanceService.getProjectVersion(versionId);
		ProjectVO project = softPerformanceService.getProject(String.valueOf(version.getProjectId()));
		String projectName = project.getName();
		List<SoftGroupEntity> allPms = softPerformanceService.getSoftPersonsAll(SoftPosition.产品经理, projectName);
		List<SoftGroupEntity> allDevelopers = softPerformanceService.getSoftPersonsAll(SoftPosition.编码人员, projectName);
		List<SoftGroupEntity> allTesters = softPerformanceService.getSoftPersonsAll(SoftPosition.软件测试, projectName);
		List<SoftGroupEntity> allShiShis = softPerformanceService.getSoftPersonsAll(SoftPosition.实施, projectName);
		List<SoftGroupEntity> allFenXis = softPerformanceService.getSoftPersonsAll(SoftPosition.需求分析, projectName);
		for(SoftGroupEntity pm: allPms){
			if(!pms.contains(pm.getUserId())){
				pm.setFlag(false);
			}else{
				pm.setFlag(true);
			}
		}
		for(SoftGroupEntity developer: allDevelopers){
			if(!developers.contains(developer.getUserId())){
				developer.setFlag(false);
			}else{
				developer.setFlag(true);
			}
		}
		for(SoftGroupEntity tester: allTesters){
			if(!testers.contains(tester.getUserId())){
				tester.setFlag(false);
			}else{
				tester.setFlag(true);
			}
		}
		for(SoftGroupEntity shiShi: allShiShis){
			if(!shiShis.contains(shiShi.getUserId())){
				shiShi.setFlag(false);
			}else{
				shiShi.setFlag(true);
			}
		}
		for(SoftGroupEntity fenXi: allFenXis){
			if(!fenXis.contains(fenXi.getUserId())){
				fenXi.setFlag(false);
			}else{
				fenXi.setFlag(true);
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pms", allPms);
		resultMap.put("developers", allDevelopers);
		resultMap.put("testers", allTesters);
		resultMap.put("shiShis", allShiShis);
		resultMap.put("fenXis", allFenXis);
		printByJson(resultMap);
	}
	public void showBaseScoresDetail(){
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String userId = request.getParameter("userId");
		List<PerformanceVo> performanceVos = softPerformanceService.getBaseScoresDetail(year, month, userId);
		//基数总计
		double totalBaseScores = 0;
		for(PerformanceVo performanceVo: performanceVos){
			totalBaseScores += performanceVo.getBaseScores();
		}
		BigDecimal b = new BigDecimal(totalBaseScores);  
		totalBaseScores = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("performanceVos", performanceVos);
		resultMap.put("totalBaseScores", totalBaseScores);
		printByJson(resultMap);
	}
	@Setter
	@Getter
	private ProblemOrderVo problemOrderVo;
	public String problemOrderList(){
		projects = softPerformanceService.getProjectLst();
		String projectId = request.getParameter("projectId");
		String status = request.getParameter("status");
		String problemOrderName = request.getParameter("problemOrderName");
		ListResult<Object> problemOrders = softPerformanceService.findProblemOrderList(projectId, status, problemOrderName, limit, page);
		count = problemOrders.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("problemOrders", problemOrders.getList());
		request.setAttribute("projectId", projectId);
		request.setAttribute("status", status);
		request.setAttribute("problemOrderName", problemOrderName);
		selectedPanel = "problemOrderList";
		User user = (User)request.getSession().getAttribute("user");
		int problemOrderTaskCount = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
				.taskDefinitionKey(TaskDefKeyEnum.PROBLEM_ORDER_ALLOCATE.getName()).count();
		request.getSession().setAttribute("problemOrderTaskCount", problemOrderTaskCount);
		return "problemOrderList";
	}
	@Getter
	private String errorMessage;
	public String error(){
		errorMessage = request.getParameter("errorMessage");
		selectedPanel = request.getParameter("selectedPanel");
		return "error";
	}
	public String startProblemOrder(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			softPerformanceService.startProblemOrder(problemOrderVo, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "problemOrderList";
			errorMessage = e.getMessage();
			return "render_error";
		}
		if("true".equals(request.getParameter("fromPersonal"))){
			return "render_home";
		}else{
			return "render_problemOrderList";
		}
	}
	public String showProblemOrderDetail(){
		String instanceId = request.getParameter("instanceId");
		String problemOrderId = request.getParameter("problemOrderId");
		Object problemOrder = softPerformanceService.getProblemOrderById(problemOrderId);
		request.setAttribute("problemOrder", problemOrder);
		if(null != problemOrder){
			String attachmentIdStr = (String) ((Object[])problemOrder)[7];
			
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
			request.setAttribute("attaList", attaList);
		}
		if(StringUtils.isNotBlank(instanceId)){
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(instanceId);
			List<CommentVO> comments = processService
					.getCommentsByProcessInstanceID(instanceId);
			request.setAttribute("comments", comments);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
			request.setAttribute("showProcess", true);
		}else{
			request.setAttribute("showProcess", false);
		}
		selectedPanel = "problemOrderList";
		return "showProblemOrderDetail";
	}
	public String showImage(){
		String attachmentPath = request.getParameter("attachmentPath");
		//判断路径是不是图片路径
		if(attachmentPath.indexOf("id=")>-1){
			String id = attachmentPath.substring(attachmentPath.indexOf("?id=")+4, attachmentPath.length());
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
	public String downloadAttach() {
		try {
			String attachmentPath = request.getParameter("attachmentPath");
			String attachmentName = request.getParameter("attachmentName");
			inputStream = new FileInputStream(new File(attachmentPath));
			// 解决中文乱码
			downloadFileFileName = new String(attachmentName.getBytes(),
					"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "downloadAttachment";
	}
	public String allotProblemOrder(){
		User user = (User)request.getSession().getAttribute("user");
		List<Task> problemOrderTasks = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.taskDefinitionKey(TaskDefKeyEnum.PROBLEM_ORDER_ALLOCATE.getName()).list();
		List<ProblemOrderVo> problemOrderTaskVos = softPerformanceService.getProblemOrdersByInstanceId(problemOrderTasks);
		int problemOrderTaskCount = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
				.taskDefinitionKey(TaskDefKeyEnum.PROBLEM_ORDER_ALLOCATE.getName()).count();
		request.getSession().setAttribute("problemOrderTaskCount", problemOrderTaskCount);
		request.setAttribute("problemOrderTaskVos", problemOrderTaskVos);
		selectedPanel = "allotProblemOrder";
		return "allotProblemOrder";
	}
	public String startAllocateProblemOrder(){
		String developerId = request.getParameter("developerId");
		String dutyPersonId = request.getParameter("dutyPersonId");
		String score = request.getParameter("score");
		String taskId = request.getParameter("taskId");
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			User user = (User)request.getSession().getAttribute("user");
			softPerformanceService.startAllocateProblemOrder(developerId, score, taskId, processInstanceId, user.getId(), dutyPersonId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "allotProblemOrder";
			errorMessage = e.getMessage();
			return "render_error";
		}
		return "render_allotProblemOrder";
	}
	public String handleProblemOrder(){
		String problemOrderId = request.getParameter("problemOrderId");
		String taskId = request.getParameter("taskId");
		String taskName = request.getParameter("taskName");
		String processInstanceId = request.getParameter("processInstanceId");
		Object problemOrder = softPerformanceService.getProblemOrderById(problemOrderId);
		request.setAttribute("problemOrder", problemOrder);
		if(null != problemOrder){
			String attachmentIdStr = (String) ((Object[])problemOrder)[7];
			
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
			request.setAttribute("attaList", attaList);
		}
		if(StringUtils.isNotBlank(processInstanceId)){
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(processInstanceId);
			List<CommentVO> comments = processService
					.getCommentsByProcessInstanceID(processInstanceId);
			request.setAttribute("comments", comments);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		}
		request.setAttribute("taskId", taskId);
		request.setAttribute("taskName", taskName);
		return "handleProblemOrder";
	}
	@Getter
	private int type;
	public String solveProblemOrder(){
		String taskId = request.getParameter("taskId");
		String comment = request.getParameter("comment");
		try {
			User user = (User)request.getSession().getAttribute("user");
			processService.completeTask(taskId, user.getId(), null, comment);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.PROBLEM_ORDER.getValue();
		return "toTaskList";
	}
	public String confirmProblemOrder(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		try {
			User user = (User)request.getSession().getAttribute("user");
			String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
			processService.completeTask(taskId, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
			if(Integer.parseInt(result) == TaskResultEnum.AGREE.getValue()){
				//更新问题单状态
				softPerformanceService.updateProblemOrderStatus(processInstanceId, result);
				//记录得分
				softPerformanceService.saveProblemOrderScore(processInstanceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.PROBLEM_ORDER.getValue();
		return "toTaskList";
	}
	public String addProblemOrder(){
		User user = (User)request.getSession().getAttribute("user");
		request.setAttribute("questionerId", user.getId());
		StaffVO staffVo = staffService.getStaffByUserID(user.getId());
		request.setAttribute("questionerName", staffVo.getLastName());
		projects = softPerformanceService.getProjectLst();
		return "addProblemOrder";
	}
	public String changeToRequire(){
		String taskId = request.getParameter("taskId");
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			User user = (User)request.getSession().getAttribute("user");
			//问题单转为需求
			softPerformanceService.changeProblemToRequire(taskId, processInstanceId, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "allotProblemOrder";
			errorMessage = e.getMessage();
			return "render_error";
		}
		return "render_allotProblemOrder";
	}
	
	public String forcedToEnd(){
		try{
			User user = (User) request.getSession().getAttribute("user");
			Integer id = Integer.parseInt(request.getParameter("id"));
			String processInstanceID = request.getParameter("processInstanceID");
			softPerformanceService.forcedTerminationTask(user.getId(),id,processInstanceID);
			
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "forcedToEnd";
	}
}
