package com.zhizaolian.staff.action.informationCenter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.StaffQueryVO;
import com.zhizaolian.staff.vo.StaffVO;

public class QueryAction extends BaseAction {

	@Getter 
	private String selectedPanel;
	@Getter
	@Setter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private StaffQueryVO staffQueryVO;
	
	@Autowired
	private StaffService staffService;
	@Autowired
	private PositionService positionService;
	
	private static final long serialVersionUID = 1L;
	
	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}
	
	public String queryStaff() {
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(StaffStatusEnum.PROBATION.getValue());
		statusList.add(StaffStatusEnum.PRACTICE.getValue());
		statusList.add(StaffStatusEnum.FORMAL.getValue());
		List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
		int count = staffService.countStaffByStatusList(statusList);
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("staffVOList", staffVOs);
		
		selectedPanel = "queryStaff";
		return "queryStaff";
	}
	
	public String findStaffListByQueryVO() {
		try {
			String trans = new String(staffQueryVO.getName().getBytes("ISO-8859-1"),"UTF-8");
			String name = URLDecoder.decode(trans , "UTF-8");
			staffQueryVO.setName(name);
			
			//不允许按姓名模糊查询
			staffQueryVO.setFuzzyQuery_Name(false);
			ListResult<StaffVO> staffVOListResult = staffService.findStaffPageListByQueryVO(staffQueryVO, page, limit);
			request.setAttribute("staffVOList", staffVOListResult.getList());
			int count = staffVOListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (staffQueryVO != null && staffQueryVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(staffQueryVO.getCompanyID());
				if (departmentVOs != null && staffQueryVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = staffQueryVO.getDepartmentID();
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
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		
		selectedPanel = "queryStaff";
		return "queryStaff";
	}
}
