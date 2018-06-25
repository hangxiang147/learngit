package com.zhizaolian.staff.action.HR;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.AlterStaffSalaryEntity;
import com.zhizaolian.staff.entity.SocialSecurityClassEntity;
import com.zhizaolian.staff.entity.StaffSalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.SalaryPayEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.NumberUtil;
import com.zhizaolian.staff.vo.ChangeSalaryDetailVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.PaySalaryTaskVo;
import com.zhizaolian.staff.vo.RewardAndPunishmentVo;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;
@Controller
@Scope(value="prototype")
public class StaffSalaryAction extends BaseAction{

	private static final long serialVersionUID = -1824344045774356285L;

	private Logger logger = Logger.getLogger(HRCenterAction.class);
	@Setter
	@Getter
	private String selectedPanel;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	private Integer totalCount = 0;
	@Autowired
	private StaffSalaryService staffSalaryService;
	@Setter
	@Getter
	private SocialSecurityClassEntity socialSecurity;
	@Setter
	@Getter
	private StaffSalaryEntity staffSalary;
	@Setter
	@Getter
	private AlterStaffSalaryEntity alterStaffSalary;
	@Autowired
	private ProcessService processService;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String[] attachmentFileName;
	@Setter
	@Getter
	private InputStream inputStream;
	@Getter
	private int type;
	@Setter
	@Getter
	private StaffSalaryDetailEntity staffDetail;
	@Setter
	private String[] deductionItem;
	@Setter
	private Double[] deductionMoney;
	@Setter
	private String[] subsidyItem;
	@Setter
	private Double[] subsidyMoney;
	@Autowired
	private TaskService taskService;
	@Setter
	@Getter
	private ChangeSalaryDetailVo changeSalaryDetailVo;
	@Setter
	@Getter
	private PaySalaryTaskVo paySalaryVo;
	@Autowired
	private PositionService positionService;
	@Getter
	private String errorMessage;
	/**
	 * 获取社保信息
	 * @return
	 */
	public String findSocialSecurityInfos(){
		try {
			List<SocialSecurityClassEntity> socialSecurityClasss = staffSalaryService.findSocialSecurityClasss();
			request.setAttribute("socialSecurityClasss", socialSecurityClasss);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "socialSecurityInfo";
		return "socialSecurityInfos";
	}
	public String saveSocialSecurity(){
		try {
			staffSalaryService.saveSocialSecurity(socialSecurity);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "socialSecurityInfo";
		return "render_socialSecurityInfos";
	}
	public void findSocialSecurityById(){
		String id = request.getParameter("id");
		SocialSecurityClassEntity socialSecurity = staffSalaryService.findSocialSecurityById(id);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("socialSecurity", socialSecurity);
		printByJson(resultMap);
	}
	public String staffSalaryAlterations(){
		String userId = request.getParameter("userId");
		try {
			List<AlterStaffSalaryEntity> staffSalaryAlterations = staffSalaryService.findStaffSalaryAlterations(userId);
			request.setAttribute("staffSalaryAlterations", staffSalaryAlterations);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "salaryAlteration";
		return "staffSalaryAlterations";
	}
	public String toAlterStaffSalary(){
		request.setAttribute("userId", request.getParameter("userId"));
		selectedPanel = "salaryAlteration";
		return "toAlterStaffSalary";
	}
	public String alterStaffSalary(){
		String userId = request.getParameter("userId");
		StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(userId);
		request.setAttribute("staffSalary", staffSalary);
		request.setAttribute("userId", userId); 
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		request.setAttribute("minDate", sdf.format(cal.getTime())+"-01");
		selectedPanel = "salaryAlteration";
		return "alterStaffSalary";
	}
	public String applyAlterStaffSalary(){
		User user = (User)request.getSession().getAttribute("user");
		alterStaffSalary.setUserId(user.getId());
		try {
			staffSalaryService.applyAlterStaffSalary(staffSalary, alterStaffSalary, attachment, attachmentFileName);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_findAllInJobStaffs";
	}
	public void checkApplyAlterSalary(){
		String userId = request.getParameter("userId");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(userId);
			if(null == staffSalary){
				resultMap.put("maintainSalary", false);
				printByJson(resultMap);
				return;
			}else{
				resultMap.put("maintainSalary", true);
			}
			boolean hasApplyAlterSalary = staffSalaryService.checkHasApplyAlterSalary(userId);
			resultMap.put("hasApplyAlterSalary", hasApplyAlterSalary);
			boolean meetManageCondition = staffSalaryService.CheckMeetManageCondition(userId);
			resultMap.put("meetManageCondition", meetManageCondition);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String showSalaryAlterationDetail(){
		String id = request.getParameter("id");
		try {
			AlterStaffSalaryEntity staffSalaryALteration = staffSalaryService.showSalaryAlterationDetail(id);
			request.setAttribute("staffSalaryALteration", staffSalaryALteration);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "salaryAlteration";
		return "showSalaryAlterationDetail";
	}
	public String auditSalaryAlteration(){
		String id = request.getParameter("id");
		try {
			AlterStaffSalaryEntity staffSalaryALteration = staffSalaryService.showSalaryAlterationDetail(id);
			request.setAttribute("staffSalaryALteration", staffSalaryALteration);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		selectedPanel = "findTaskList";
		return "auditSalaryAlteration";
	}
	public String completeTask(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		User user = (User)request.getSession().getAttribute("user");
		try {
			staffSalaryService.updateAlterSalaryStatus(result, taskId);
			processService.completeTask(taskId, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.ALTER_SALARY.getValue();
		return "toTaskList";
	}
	public String completeTaskForChangeSalary(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		User user = (User)request.getSession().getAttribute("user");
		try {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceID = task.getProcessInstanceId();
			staffSalaryService.updateChangeSalaryDetailStatus(result, processInstanceID);
			//审批通过，更改工资条
			if(String.valueOf(TaskResultEnum.AGREE.getValue()).equals(result)){
				staffSalaryService.updateChangeSalaryDetail(processInstanceID);
			}
			processService.completeTask(taskId, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.CHANGE_SALARY_DETAIL.getValue();
		return "toTaskList";
	}
	public String processHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = request.getParameter("selectedPanel");
		return "processHistory";
	}
	public String staffSalarys(){
		Calendar calendar = Calendar.getInstance();
		//默认上个月
		if(null == staffDetail){
			staffDetail = new StaffSalaryDetailEntity();
		}
		if(null == staffDetail.getYear()){
			calendar.add(Calendar.MONTH, -1);
			staffDetail.setYear(calendar.get(Calendar.YEAR));
		}
		if(null == staffDetail.getMonth()){
			staffDetail.setMonth(calendar.get(Calendar.MONTH)+1);
		}
		ListResult<Object> staffSalarys = staffSalaryService.findStaffSalarys(staffDetail, limit, page);
		int total = staffSalarys.getTotalCount();
		totalCount = total;
		totalPage = total % limit == 0 ? total / limit : total / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("staffSalarys", staffSalarys.getList());
		if (null != staffDetail.getCompanyId()) {
			List<DepartmentVO> departmentVOs = positionService
					.findDepartmentsByCompanyID(staffDetail.getCompanyId());
			if (departmentVOs != null && staffDetail.getDepartmentId() != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = staffDetail.getDepartmentId();
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
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "staffSalarys";
		return "staffSalarys";
	}
	public String showStaffSalaryDetail(){
		String id = request.getParameter("id");
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailById(id);
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("depName", request.getParameter("depName"));
		request.setAttribute("staffName", request.getParameter("staffName"));
		selectedPanel = "staffSalarys";
		return "showStaffSalaryDetail";
	}
	public String toApplyChangeStaffSalary(){
		request.setAttribute("staffSalaryId", request.getParameter("id"));
		request.setAttribute("depName", request.getParameter("depName"));
		request.setAttribute("staffName", request.getParameter("staffName"));
		selectedPanel = "staffSalarys";
		return "toApplyChangeStaffSalary";
	}
	public String applyChangeStaffSalary(){
		String staffSalaryId = request.getParameter("staffSalaryId");
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailById(staffSalaryId);
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("depName", request.getParameter("depName"));
		request.setAttribute("staffName", request.getParameter("staffName"));
		request.setAttribute("staffSalaryId", request.getParameter("staffSalaryId"));
		selectedPanel = "staffSalarys";
		return "applyChangeStaffSalary";
	}
	public String startApplyChangeStaffSalary(){
		String staffSalaryId = request.getParameter("staffSalaryId");
		Map<String, Double> otherDeductionItemMap = new HashMap<>();
		Map<String, Double> otherSubsidyItemMap = new HashMap<>();
		if(null != deductionItem){
			for(int i=0; i<deductionItem.length; i++){
				otherDeductionItemMap.put(deductionItem[i], deductionMoney[i]);
			}
		}
		if(null != subsidyItem){
			for(int i=0; i<subsidyItem.length; i++){
				otherSubsidyItemMap.put(subsidyItem[i], subsidyMoney[i]);
			}
		}
		User user = (User)request.getSession().getAttribute("user");
		try {
			staffSalaryService.startApplyChangeStaffSalary(staffSalaryId, user.getId(), otherDeductionItemMap, otherSubsidyItemMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.getMessage();
			return "error";
		}
		return "render_changeStaffSalaryApplys";
	}
	public void calAfterTaxSalary(){
		String staffSalaryId = request.getParameter("staffSalaryId");
		String otherSubsidyMoney = request.getParameter("otherSubsidyMoney");
		String totalDeductionMoney = request.getParameter("totalDeductionMoney");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailById(staffSalaryId);
			double preTaxSalary = staffSalary.getPreTaxSalary();
			double totalMoney = staffSalary.getTotalMoney();
			Double otherDedution = staffSalary.getOtherDeduction();
			if(null == otherDedution){
				otherDedution = 0.0;
			}
			Double otherSubsidy = staffSalary.getOtherSubsidy();
			if(null == otherSubsidy){
				otherSubsidy = 0.0;
			}
			//重新计算税前工资
			preTaxSalary = (preTaxSalary+otherDedution-otherSubsidy) - (StringUtils.isNotBlank(totalDeductionMoney) ? Double.parseDouble(totalDeductionMoney):0)
					+ (StringUtils.isNotBlank(otherSubsidyMoney) ? Double.parseDouble(otherSubsidyMoney):0);

			totalMoney = (totalMoney+otherDedution-otherSubsidy) - (StringUtils.isNotBlank(totalDeductionMoney) ? Double.parseDouble(totalDeductionMoney):0)
					+ (StringUtils.isNotBlank(otherSubsidyMoney) ? Double.parseDouble(otherSubsidyMoney):0);
			double tax = staffSalaryService.calTax(preTaxSalary);
			double afterTaxSalary = preTaxSalary - tax;
			afterTaxSalary = NumberUtil.Rounding(afterTaxSalary, 2);
			resultMap.put("tax", tax);
			resultMap.put("afterTaxSalary", afterTaxSalary);
			resultMap.put("totalMoney", NumberUtil.Rounding(totalMoney, 2));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String auditSalaryDetailChange(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailByInstanceId(processInstanceId);
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		selectedPanel = "findTaskList";
		return "auditSalaryDetailChange";
	}
	public void checkHasApplyChangeSalaryDetail(){
		String staffSalaryId = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>();
		boolean hasApplyChangeSalaryDetail = staffSalaryService.checkHasApplyChangeSalaryDetail(staffSalaryId);
		resultMap.put("hasApplyChangeSalaryDetail", hasApplyChangeSalaryDetail);
		printByJson(resultMap);
	}
	
	public String findChangeStaffSalaryList(){
		//默认在职
		if(null == changeSalaryDetailVo){
			changeSalaryDetailVo = new ChangeSalaryDetailVo();
			changeSalaryDetailVo.setStaffStatus(StaffStatusEnum.JOB.getValue());
		}
		ListResult<ChangeSalaryDetailVo> changeSalaryDetailVos = staffSalaryService.findChangeStaffSalaryList(limit, page, changeSalaryDetailVo);
		int total = changeSalaryDetailVos.getTotalCount();
		totalCount = total;
		totalPage = total % limit == 0 ? total / limit : total / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("changeStaffSalaryList", changeSalaryDetailVos.getList());
		selectedPanel = "changeStaffSalaryList";
		return "changeStaffSalaryApplys";
	}
	public String showSalaryChangeDetail(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailByInstanceId(processInstanceId);
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "changeStaffSalaryList";
		return "showSalaryChangeDetail";
	}
	public void startSendSalary(){
		Map<String, String> resultMap = new HashMap<>();
		String sendType = request.getParameter("sendType");
		String staffSalaryIds = request.getParameter("staffSalaryIds");
		User user = (User)request.getSession().getAttribute("user");
		try {
			staffSalaryService.startSendSalary(sendType, staffSalaryIds, user.getId());
			resultMap.put("success", "true");
		} catch (Exception e) {
			resultMap.put("success", "false");
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	@Getter
	private String excelFileName;
	@Getter
	private InputStream salaryExcelFile;
	public String exportStaffSalarys(){
		try {
			String rootPath = request.getSession().getServletContext().getRealPath("/");//获取工程的根路径
			salaryExcelFile = staffSalaryService.exportStaffSalarys(rootPath, staffDetail);
			// 解决中文乱码
			excelFileName = new String((staffDetail.getYear()+"年"+staffDetail.getMonth()+"月"+"工资单.xlsx").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "exportStaffSalarys";
	}
	public String toApplyPaySalary(){
		selectedPanel = "staffSalaryPayInfos";
		return "toApplyPaySalary";
	}
	public String applyPaySalary(){
		Calendar calendar = Calendar.getInstance();
		//默认上个月
		if(null == staffDetail){
			staffDetail = new StaffSalaryDetailEntity();
		}
		if(null == staffDetail.getYear()){
			calendar.add(Calendar.MONTH, -1);
			staffDetail.setYear(calendar.get(Calendar.YEAR));
		}
		if(null == staffDetail.getMonth()){
			staffDetail.setMonth(calendar.get(Calendar.MONTH)+1);
		}
		ListResult<Object> staffSalaryPayInfos = staffSalaryService.findStaffSalaryPayInfos(staffDetail, limit, page);
		int total = staffSalaryPayInfos.getTotalCount();
		totalCount = total;
		totalPage = total % limit == 0 ? total / limit : total / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("staffSalarys", staffSalaryPayInfos.getList());
		if (null != staffDetail.getCompanyId()) {
			List<DepartmentVO> departmentVOs = positionService
					.findDepartmentsByCompanyID(staffDetail.getCompanyId());
			if (departmentVOs != null && staffDetail.getDepartmentId() != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = staffDetail.getDepartmentId();
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
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "staffSalaryPayInfos";
		return "applyPaySalary";
	}
	public String allApplyPaySalary(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			staffSalaryService.allApplyPaySalary(staffDetail, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.getMessage();
			return "error";
		}
		return "render_paySalaryApplys";
	}
	public String partApplyPaySalary(){
		try {
			String staffSalaryIds = request.getParameter("staffSalaryIds");
			User user = (User)request.getSession().getAttribute("user");
			staffSalaryService.startApplyPaySalary(staffSalaryIds, user.getId(), staffDetail);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.getMessage();
			return "error";
		}
		return "render_paySalaryApplys";
	}
	public String findPaySalaryInfos(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			List<PaySalaryTaskVo> paySalaryInfos = staffSalaryService.getPaySalaryInfosByInstanceId(processInstanceId);
			request.setAttribute("paySalaryInfos", paySalaryInfos);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "findTaskList";
			return "error";
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("processInstanceId", processInstanceId);
		selectedPanel = "findTaskList";
		return "paySalaryInfos";
	}
	public String exportPaySalarys(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			salaryExcelFile = staffSalaryService.exportPaySalarys(processInstanceId);
			// 解决中文乱码
			excelFileName = new String(("工资单.xlsx").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "exportStaffSalarys";
	}
	public String confirmSalaryPayStatus(){
		String taskId = request.getParameter("taskId");
		String comment = request.getParameter("comment");
		String successSalaryIds = request.getParameter("successSalaryIds");
		String failedSalaryIds = request.getParameter("failedSalaryIds");
		User user = (User)request.getSession().getAttribute("user");
		try {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			processService.completeTask(taskId, user.getId(), TaskResultEnum.COMPLETE_HANDLE, comment);
			if(StringUtils.isNotBlank(successSalaryIds)){
				staffSalaryService.updateStaffSalaryPayStatus(successSalaryIds, SalaryPayEnum.SUCCESS.getValue());
			}
			if(StringUtils.isNotBlank(failedSalaryIds)){
				staffSalaryService.updateStaffSalaryPayStatus(failedSalaryIds, SalaryPayEnum.FAILED.getValue());
			}
			staffSalaryService.updatePaySalaryApplyStatus(processInstanceId, TaskResultEnum.AGREE.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.PAY_SALARY.getValue();
		return "toTaskList";
	}
	public void changeSalaryPayStatus(){
		String salaryId = request.getParameter("salaryId");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			staffSalaryService.updateStaffSalaryPayStatus(salaryId, SalaryPayEnum.SUCCESS.getValue());
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("success", false);
		}
		printByJson(resultMap);
	}
	public String findPaySalaryApplyList(){
		//默认处理中
		if(null == paySalaryVo){
			paySalaryVo = new PaySalaryTaskVo();
			paySalaryVo.setStatus(Constants.PROGRESS);
		}
		ListResult<PaySalaryTaskVo> paySalaryApplyVos = staffSalaryService.findPaySalaryApplyList(limit, page, paySalaryVo);
		int total = paySalaryApplyVos.getTotalCount();
		totalCount = total;
		totalPage = total % limit == 0 ? total / limit : total / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("paySalaryApplyVos", paySalaryApplyVos.getList());
		selectedPanel = "applyPaySalaryList";
		return "paySalaryApplyList";
	}
	public String showApplyPaySalaryInfos(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			List<PaySalaryTaskVo> paySalaryInfos = staffSalaryService.getPaySalaryInfosByInstanceId(processInstanceId);
			request.setAttribute("paySalaryInfos", paySalaryInfos);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "applyPaySalaryList";
			return "error";
		}
		selectedPanel = "applyPaySalaryList";
		return "showApplyPaySalaryInfos";
	}
	public String toAuditRewardAndPunishment(){
		String processInstanceId = request.getParameter("processInstanceId");
		try {
			RewardAndPunishmentVo rewardAndPunishmentVo = staffSalaryService.getRewardAndPunishmentByProcessInstanceId(processInstanceId);
			request.setAttribute("rewardAndPunishmentVo", rewardAndPunishmentVo);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		request.setAttribute("taskId", request.getParameter("taskId"));
		selectedPanel = "findTaskList";
		return "toAuditRewardAndPunishment";
	}
	public String auditRewardAndPunishment(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		User user = (User)request.getSession().getAttribute("user");
		try {
			staffSalaryService.updateRewardAndPunishmentStatus(result, taskId);
			processService.completeTask(taskId, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.REWARD_PUNISHMENT.getValue();
		return "toTaskList";
	}
	public String toBatchImportSalary(){
		selectedPanel = "batchImportSalary";
		return "toBatchImportSalary";
	}
	@Setter
	private File salaryFile;
	public void batchImportSalary(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			staffSalaryService.batchImportSalary(salaryFile);
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("success", false);
		}
		printByJson(resultMap);
	}
	/**
	 * 刷新旧的工资数据
	 */
	public void synOldSalary(){
		staffSalaryService.synOldSalary();
	}
	public void showSalaryDetail(){
		String id = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			StaffSalaryDetailEntity staffSalary = staffSalaryService.getStaffSalaryDetailById(id);
			resultMap.put("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
}
