package com.zhizaolian.staff.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.WeekReportEntity;
import com.zhizaolian.staff.entity.WeekReporterEntity;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.WeekWorkReportService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
import com.zhizaolian.staff.vo.WorkReportVO;

import lombok.Getter;
import lombok.Setter;

public class WorkReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter 
	private String selectedPanel;
	@Setter
	@Getter
	private WorkReportDetailVO workReportDetailVO;
	@Setter
	@Getter
	private String searchDate;
	@Setter
	@Getter
	private List<WorkReportVO> reportStatistics;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer flag;
	@Getter
	private Integer totalPage;
	@Autowired
	private WorkReportService workReportService;
	@Getter
	private String errorMessage;
	@Autowired
	private PositionService positionService;
	@Autowired
	private WeekWorkReportService weekWorkReportService;
	
	public String findWorkReportList() {
		if(workReportDetailVO==null){
			workReportDetailVO=new WorkReportDetailVO();	
		}
		if(StringUtils.isBlank(workReportDetailVO.getBeginDate())){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			workReportDetailVO.setBeginDate(DateUtil.formateDate(cal.getTime()));
		}
		try{
			ListResult<WorkReportDetailVO> workReportDetailVOListResult=workReportService.findWorkReportListByUserID(workReportDetailVO,page,limit);
			request.setAttribute("workReportDetailVOs", workReportDetailVOListResult.getList());
			count=workReportDetailVOListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(0 == totalPage) totalPage=1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (workReportDetailVO != null && workReportDetailVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(workReportDetailVO.getCompanyID());
				if (departmentVOs != null && workReportDetailVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = workReportDetailVO.getDepartmentID();
					while (selectedDepartmentID != 0) {
						selectedDepartmentIDs.add(0, selectedDepartmentID);
						for (DepartmentVO departmentVO : departmentVOs) {
							if (departmentVO.getDepartmentID() == selectedDepartmentID) {
								selectedDepartmentID = departmentVO.getParentID();
							}						}
					}
					request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
				}
				request.setAttribute("departmentVOs", departmentVOs);
			}
			request.setAttribute("companyVOs", companyVOs);
		}catch(Exception e){
			errorMessage = "查询工作列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
			
		selectedPanel = "workReport";
		return "workReportList";
	}
	public String findWorkReportStatistics(){
		try{
			
			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			List<WorkReportVO> workReportVOs=workReportService.findWorkReportsByDate(companyID,searchDate);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("workReportVOs", workReportVOs);
			request.setAttribute("count", workReportVOs.size());
			request.setAttribute("companyVOs", companyVOs);
		}catch(Exception e){
			errorMessage = "查询未汇报失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		selectedPanel = "workReport";
		return "workReportList";
	}
	public String findReportStatistics(){
		try{
			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			String searchMonth =request.getParameter("searchMonth");
			reportStatistics=workReportService.findStatisticsByMonth(companyID, DateUtil.getSimpleDate(searchMonth+"-01"));
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "reportStatistics";
	}
	public String findWeekReportList(){
		String staffName = request.getParameter("staffName");
		String staffId = request.getParameter("staffId");
		String companyId = request.getParameter("companyId");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		try{
			String[] conditions = {staffId, companyId, beginDate, endDate};
			ListResult<WeekReportEntity> weekReportListResult = weekWorkReportService.findWeekReportListByConditions(conditions,page,limit);
			request.setAttribute("weekReports", weekReportListResult.getList());
			count = weekReportListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0){
				totalPage = 1;
			}
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("companyVOs", companyVOs);
		}catch(Exception e){
			errorMessage = "查询工作列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		request.setAttribute("staffName", staffName);	
		request.setAttribute("staffId", staffId);
		request.setAttribute("companyId", companyId);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		selectedPanel = "workReport";
		return "workReportList";
	}
	public String weekReporterManage(){
		try {
			String userName = request.getParameter("userName");
			String companyId = request.getParameter("companyId");
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			List<Object> needWeekReportPersons = weekWorkReportService.findNeedWeekReportPersons(userName, companyId);
			count = needWeekReportPersons.size();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0){
				totalPage = 1;
			}
			request.setAttribute("needWeekReportPersons", ActionUtil.page(page, limit, needWeekReportPersons));
			request.setAttribute("userName", userName);
			request.setAttribute("companyId", companyId);
			request.setAttribute("companyVOs", companyVOs);
		} catch (Exception e) {
			errorMessage = "查询周报汇报人员列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "workReport";
		return "workReportList";
	}
	public String addWeekReporter(){
		//String partner = request.getParameter("partner");
		String weekReporterName = request.getParameter("weekReporterName");
		String weekReporterId = request.getParameter("weekReporterUserId");
		WeekReporterEntity weekReporter = new WeekReporterEntity();
		//weekReporter.setPartner(partner);
		weekReporter.setUserName(weekReporterName);
		weekReporter.setUserId(weekReporterId);
		weekWorkReportService.addWeekReporter(weekReporter);
		return "render_weekReporterManage";
	}
	public String deleteWeekReporter(){
		String reporterId = request.getParameter("reporterId");
		weekWorkReportService.deleteWeekReporter(reporterId);
		return "render_weekReporterManage";
	}
	public String findWeekReportStatistics(){
		try{
			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			List<WeekReportEntity> weekReportVos = weekWorkReportService.findWeekReportsByDate(companyID, beginDate, endDate);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("weekReportVos", weekReportVos);
			request.setAttribute("count", weekReportVos.size());
			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
		}catch(Exception e){
			errorMessage = "查询未汇报失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "workReport";
		return "workReportList";
	}
}

