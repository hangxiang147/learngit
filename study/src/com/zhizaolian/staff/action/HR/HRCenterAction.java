package com.zhizaolian.staff.action.HR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.VehicleInfoEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.StaticResource;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.StaffAnalysisService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.VehicleService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.service.VitaeService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.ContractVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.ViewWorkReportVo;
import com.zhizaolian.staff.vo.WorkOvertimeTaskVo;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

@Controller(value="hRCenterAction")
@Scope(value="prototype")
public class HRCenterAction extends BaseAction{

	private static final long serialVersionUID = 2177193211462082054L;
	private Logger logger = Logger.getLogger(HRCenterAction.class);
	@Autowired
	private StaffService staffService;
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private FormalService formalService;
	@Autowired
	private WorkOvertimeService workOvertimeService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private ViewReportService viewReportService;
	@Setter
	@Getter
	private Integer type = 1;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Autowired
	private StaffAnalysisService staffAnalysisService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private VitaeService vitaeService;
	@Getter
	@Setter
	private String errorMessage;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ContractService contractService;
	@Setter
	@Getter
	private ContractVO contractVO;
	
	public String goHRCenter(){
		int totalStaffNum = staffService.getTotalStaffNum();
		//试用期的员工数量
		int probationNum = staffService.getProbationNum();
		//正式员工数量
		int formalStaffNum = staffService.getFormalStaffNum();
		//待离职员工
		List<StaffEntity> quitingStaffs = staffService.getQuitingStaffs();
		//待入职人员数量，待开发
		List<String> entryingStaffs = vitaeService.findEntryingStaffs();
		//本月要过生日的员工
		List<StaffEntity> birthStaffs = staffService.findAllBirthStaffsByCurrentMonth();
		//本月入职周年的员工
		List<StaffEntity> anniversaryStaffs = staffService.findAllAnniversaryStaffsByCurrentMonth();
		
		if(contractVO==null){
			contractVO = new ContractVO();
		}
		
		//当前有*名员工合同即将到期
		User user = (User)request.getSession().getAttribute("user");
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
		contractVO.setAdmCompanyIDs(Lists2.transform(groups, new SafeFunction<GroupDetailVO, Integer>() {
			@Override
			protected Integer safeApply(GroupDetailVO input) {
				return input.getCompanyID();
			}
		}));
		ListResult<ContractVO> contractList = contractService.findContractByContractVO(contractVO, page, limit);
		request.setAttribute("contractListCount", contractList.getTotalCount());
		request.setAttribute("contractList", contractList.getList());
		
		
		request.setAttribute("totalStaffNum", totalStaffNum);
		request.setAttribute("probationNum", probationNum);
		request.setAttribute("formalStaffNum", formalStaffNum);
		request.setAttribute("quitingStaffs", quitingStaffs);
		request.setAttribute("entryingStaffs", entryingStaffs);
		request.setAttribute("birthStaffs", birthStaffs);
		request.setAttribute("anniversaryStaffs", anniversaryStaffs);
		
		//人事待办事项
//		User user = (User)request.getSession().getAttribute("user");
		try {
			findTaskList(user);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.toString();
			return "error";
		}
		return "HRCenter";
	}
	public void findTaskList(User user) throws Exception{
		int count = 0;
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		switch (BusinessTypeEnum.valueOf(type)){
		case VACATION:
			if(groups.size()>0){
				ListResult<VacationTaskVO> taskListResult = vacationService.findVacationTasksByGroups(groups, page, limit);
				count = taskListResult.getTotalCount();
				request.setAttribute("taskVOs", taskListResult.getList());
			}
			break;
		case FORMAL_REMIND:
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(user.getId());
			Set<Integer> companyIds = new HashSet<>();
			for(GroupDetailVO groupDetail: groupDetails){
				companyIds.add(groupDetail.getCompanyID());
			}
			List<StaffVO> formalStaffs = new ArrayList<>();
			for(Integer companyId: companyIds){
				List<StaffVO> list = StaticResource.companyIdAndformalStaffVosMap.get(companyId);
				if(null != list){
					formalStaffs.addAll(list);
					count += list.size();
				}
			}
			request.setAttribute("formalStaffs", ActionUtil.page(page, limit, formalStaffs));
			break;
		case VEHICLE_OVERDUE:
			List<VehicleInfoEntity> vehicleInfos = vehicleService.getSoonOverDueVehicles();
			count = vehicleInfos.size();
			request.setAttribute("vehicleInfos", ActionUtil.page(page, limit, vehicleInfos));
			break;
		case RESIGNATION:
			tasks.add(TaskDefKeyEnum.HR_AUDIT);
			tasks.add(TaskDefKeyEnum.RESIGNATION_TRANSFER);
			tasks.add(TaskDefKeyEnum.SALARY_SETTLEMENT);
			ListResult<TaskVO> taskListResult = resignationService.findResignationTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
			request.setAttribute("resignationTasks", taskListResult.getList());
			count = taskListResult.getTotalCount();
			break;
		case FORMAL:
			tasks.add(TaskDefKeyEnum.FORMAL_INVITATION);
			tasks.add(TaskDefKeyEnum.FORMAL_HR_AUDIT);
			ListResult<TaskVO> formalTasks = formalService.findFormalTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
			request.setAttribute("formalTasks", formalTasks.getList());
			count = formalTasks.getTotalCount();
			break;
		case WORK_OVERTIME:
			ListResult<WorkOvertimeTaskVo> overtimeTasks = workOvertimeService.findWorkOvertimeTasksByGroups(groups, page, limit);
			count = overtimeTasks.getTotalCount();
			request.setAttribute("overtimeTasks", overtimeTasks.getList());
			break;
		case AUDIT:
			tasks.add(TaskDefKeyEnum.AUDIT_HR_AUDIT);
			ListResult<TaskVO> auditTaks = staffService.findAuditTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
			count = auditTaks.getTotalCount();
			request.setAttribute("auditTaks", auditTaks.getList());
		case VIEW_REPORT:
			ListResult<ViewWorkReportVo> viewReportTasks = viewReportService.findViewReportTasksByGroups(groups, page, limit);
			count = viewReportTasks.getTotalCount();
			request.setAttribute("viewReportTasks", viewReportTasks.getList());
			break;
		case SOCIAL_SECURITY:
			tasks.add(TaskDefKeyEnum.SS_HR_UPDATE);
			tasks.add(TaskDefKeyEnum.SS_FOLLOW_UP);
			ListResult<TaskVO> socialSecTasks = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
			count = socialSecTasks.getTotalCount();
			request.setAttribute("socialSecTasks", socialSecTasks.getList());
			break;
		default:
			break;
		}
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(user.getId());
		Set<Integer> companyIds = new HashSet<>();
		for(GroupDetailVO groupDetail: groupDetails){
			companyIds.add(groupDetail.getCompanyID());
		}
		int formalRemindCount = 0;
		for(Integer companyId: companyIds){
			List<StaffVO> list = StaticResource.companyIdAndformalStaffVosMap.get(companyId);
			if(null != list){
				formalRemindCount += list.size();
			}
		}
		int soonOverDueVehicleCount = vehicleService.getSoonOverDueVehicleCount();
		request.setAttribute("formalRemindCount", formalRemindCount);
		request.setAttribute("soonOverDueVehicleCount", soonOverDueVehicleCount);
		int vacationCount = vacationService.findVacationTasksByGroups(groups, 0, 1).getTotalCount();
		request.setAttribute("vacationCount", vacationCount);
		tasks.clear();
		tasks.add(TaskDefKeyEnum.HR_AUDIT);
		tasks.add(TaskDefKeyEnum.RESIGNATION_TRANSFER);
		tasks.add(TaskDefKeyEnum.SALARY_SETTLEMENT);
		int resignationHRCount = resignationService.findResignationTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
		request.setAttribute("resignationHRCount", resignationHRCount);
		tasks.clear();
		tasks.add(TaskDefKeyEnum.FORMAL_HR_AUDIT);
		tasks.add(TaskDefKeyEnum.FORMAL_INVITATION);
		int formalHRCount = formalService.findFormalTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
		request.setAttribute("formalHRCount", formalHRCount);
		int workOvertimeCount = workOvertimeService.findWorkOvertimeTasksCountByGroups(groups);
		request.setAttribute("workOvertimeCount", workOvertimeCount);
		tasks.clear();
		tasks.add(TaskDefKeyEnum.AUDIT_HR_AUDIT);
		int auditHRCount = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
		request.setAttribute("auditHRCount", auditHRCount);
		int viewReportCount = viewReportService.findViewReportTaskCountByGroups(groups);
		request.setAttribute("viewReportCount", viewReportCount);
		tasks.clear();
		tasks.add(TaskDefKeyEnum.SS_HR_UPDATE);
		tasks.add(TaskDefKeyEnum.SS_FOLLOW_UP);
		int socialSecurityHRCount = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
		request.setAttribute("socialSecurityHRCount", socialSecurityHRCount);
	}
	/**
	 * 在职人员的分析
	 * @return
	 */
	public String showStaffsInJobAnalysis(){
		String companyId = request.getParameter("companyId");
		DepartmentEntity departments = positionService.findAllBigDepartmentsBycompanyId(companyId);
		//在职人员部门结构
		Map<String, Object> dateMap = staffAnalysisService.getStaffNumByMonthAndDep(departments.getDepartmentNames(), departments.getDepartmentIds());
		request.setAttribute("departments", JSONArray.fromObject(dateMap.get("departmentNames")));
		request.setAttribute("monthList", dateMap.get("data"));
		request.setAttribute("series", JSONArray.fromObject(dateMap.get("series")));
		request.setAttribute("maxStaffNum", dateMap.get("maxStaffNum"));
		//性别部门结构分布
		dateMap = staffAnalysisService.getStaffGenderByDep(departments.getDepartmentNames(), departments.getDepartmentIds(), StaffStatusEnum.JOB);
		request.setAttribute("males", dateMap.get("maleNum"));
		request.setAttribute("females", dateMap.get("femaleNum"));
		request.setAttribute("departmentNamesForGender", JSONArray.fromObject(dateMap.get("departmentNamesForGender")));
		request.setAttribute("genderData", JSONArray.fromObject(dateMap.get("genderData")));
		//各部门关于性别，年龄，学历的结构分布
		dateMap = staffAnalysisService.getStaffInfoByDep(departments.getDepartmentNames(), departments.getDepartmentIds(), StaffStatusEnum.JOB);
		request.setAttribute("departmentsForStaffInfo", JSONArray.fromObject(dateMap.get("departmentsForStaffInfo")));
		request.setAttribute("seriesForStaffInfo", JSONArray.fromObject(dateMap.get("series")));
		//X轴显示数据的比例，最多显示6组
		@SuppressWarnings("unchecked")
		List<String> departmentsForStaffInfo = (List<String>)(dateMap.get("departmentsForStaffInfo"));
		double depNum = departmentsForStaffInfo.size();
		int percent = 0;
		if(depNum<=6){
			percent = 100;
		}else{
			percent = (int) (6/depNum*100);
		}
		request.setAttribute("percent", percent);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		request.setAttribute("companyId", companyId);
		return "showStaffsInJobAnalysis";
	}
	/**
	 * 离职人员的分析
	 * @return
	 */
	public String showLeaveStaffsAnalysis(){
		String companyId = request.getParameter("companyId");
		DepartmentEntity departments = positionService.findAllBigDepartmentsBycompanyId(companyId);
		//离职人员部门结构
		Map<String, Object> dateMap = staffAnalysisService.getLeaveStaffNumByMonthAndDep(departments.getDepartmentNames(), departments.getDepartmentIds());
		request.setAttribute("departments", JSONArray.fromObject(dateMap.get("departmentNames")));
		request.setAttribute("monthList", dateMap.get("data"));
		request.setAttribute("series", JSONArray.fromObject(dateMap.get("series")));
		request.setAttribute("maxStaffNum", dateMap.get("maxStaffNum"));
		//性别部门结构分布
		dateMap = staffAnalysisService.getStaffGenderByDep(departments.getDepartmentNames(), departments.getDepartmentIds(), StaffStatusEnum.LEAVE);
		request.setAttribute("males", dateMap.get("maleNum"));
		request.setAttribute("females", dateMap.get("femaleNum"));
		request.setAttribute("departmentNamesForGender", JSONArray.fromObject(dateMap.get("departmentNamesForGender")));
		request.setAttribute("genderData", JSONArray.fromObject(dateMap.get("genderData")));
		//各部门关于性别，年龄，学历的结构分布
		dateMap = staffAnalysisService.getStaffInfoByDep(departments.getDepartmentNames(), departments.getDepartmentIds(), StaffStatusEnum.LEAVE);
		request.setAttribute("departmentsForStaffInfo", JSONArray.fromObject(dateMap.get("departmentsForStaffInfo")));
		request.setAttribute("seriesForStaffInfo", JSONArray.fromObject(dateMap.get("series")));
		//X轴显示数据的比例，最多显示6组
		@SuppressWarnings("unchecked")
		List<String> departmentsForStaffInfo = (List<String>)(dateMap.get("departmentsForStaffInfo"));
		double depNum = departmentsForStaffInfo.size();
		int percent = 0;
		if(depNum<=6){
			percent = 100;
		}else{
			percent = (int) (6/depNum*100);
		}
		request.setAttribute("percent", percent);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		request.setAttribute("companyId", companyId);
		return "showLeaveStaffsAnalysis";
	}
	@Getter
	private String selectedPanel;
	public String massTexting(){
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		return "massTexting";
	}
	@Setter
	@Getter
	private Integer companyID;
	@Setter
	@Getter
	private Integer departmentID;
	@Setter
	@Getter
	private String noteInfo;
	@Getter
	@Setter
	private List<StaffEntity> useIdList;
	@Setter
	@Getter
	private StaffEntity staffEntity;

	public String sendShortMessages(){
		try {
			if(useIdList == null || useIdList.size()==0){
				List<StaffVO> list;
				if(departmentID==null){
					list = staffService.findStaffsByCompanyID(companyID);
				}else{
					list = staffService.findStaffsByCompanyIDDepartmentID(companyID, departmentID);
				}
				for(StaffVO staffVO:list){
//					ShortMsgSender.getInstance().send(staffVO.getTelephone(), "【智造链】：" +noteInfo);
					System.out.println(staffVO.getLastName()+staffVO.getTelephone()+"：智造链："+noteInfo);
				}
			}else{
				for(int i=0;i<useIdList.size();i++){
					String userId = useIdList.get(i).getUserID();
					StaffEntity staff = staffService.getStaffByUserId(userId);
					if(staff!=null){
//						ShortMsgSender.getInstance().send(staff.getTelephone(), "【智造链】：" +noteInfo);
						System.out.println(staff.getStaffName()+staff.getTelephone()+"：智造链："+noteInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "sendShortMessages";
	}
	public String refresh(){
		User user = (User)request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if(groups.size()>0){
			ListResult<VacationTaskVO> taskListResult = vacationService.findVacationTasksByGroups(groups, page, 1000);
			List<VacationTaskVO> vacationList = taskListResult.getList();
			for(VacationTaskVO vacation: vacationList){
				String processInstanceId = vacation.getProcessInstanceID();
				runtimeService.deleteProcessInstance(processInstanceId, "complete");
				vacationService.updateProcessStatus(processInstanceId, TaskResultEnum.AGREE);
			}
			ListResult<WorkOvertimeTaskVo> overtimeTasks = workOvertimeService.findWorkOvertimeTasksByGroups(groups, page, 1000);
			List<WorkOvertimeTaskVo> overtimeList = overtimeTasks.getList();
			for(WorkOvertimeTaskVo overtime: overtimeList){
				String processInstanceId = overtime.getProcessInstanceID();
				runtimeService.deleteProcessInstance(processInstanceId, "complete");
				workOvertimeService.updateWorkOvertimeProcessStatus(TaskResultEnum.AGREE, processInstanceId);
			}
		} 
		return "HRCenter";
	}
	
	public void sendMessages(){
		try {
			if(useIdList == null || useIdList.size()==0){
				List<StaffVO> list;
				if(departmentID==null){
					list = staffService.findStaffsByCompanyID(companyID);
				}else{
					list = staffService.findStaffsByCompanyIDDepartmentID(companyID, departmentID);
				}
				for(StaffVO staffVO:list){
					ShortMsgSender.getInstance().send(staffVO.getTelephone(), "【智造链】：" +noteInfo);
//					System.out.println(staffVO.getLastName()+staffVO.getTelephone()+"：智造链："+noteInfo);
				}
			}else{
				for(int i=0;i<useIdList.size();i++){
					String userId = useIdList.get(i).getUserID();
					StaffEntity staff = staffService.getStaffByUserId(userId);
					if(staff!=null){
						ShortMsgSender.getInstance().send(staff.getTelephone(), "【智造链】：" +noteInfo);
//						System.out.println(staff.getStaffName()+staff.getTelephone()+"：智造链："+noteInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		printByJson(1);
	}
}
