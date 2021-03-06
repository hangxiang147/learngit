package com.zhizaolian.staff.action.administration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CarpoolEntity;
import com.zhizaolian.staff.service.CarpoolService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarpoolVo;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;

import lombok.Getter;
import lombok.Setter;

public class CarpoolAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Setter
	@Getter
	private CarpoolEntity carpoolEntity;
	@Autowired
	private CarpoolService carpoolService;
	@Autowired
	private PositionService positionService;

	public String saveCarpool() {
		carpoolEntity.setIsDeleted(0);
		carpoolEntity.setAddTime(new Date());
		carpoolService.saveCarpool(carpoolEntity);
		return "carpoolList";
	}

	public String updateCarpool() {
		carpoolService.updateCarpool(carpoolEntity);
		return "carpoolList";
	}

	private final static String[] CARPOOL_KEYS = { "startTime", "endTime", "user_Id", "dept_Id","company_Id","userName" };

	public String carpoolList() {
		try {
			Map<String, String> params = ActionUtil.createMapByRequest(this.request, true, CARPOOL_KEYS);
			
			String companyID_=params.get("company_Id");
			String departmentID_=params.get("dept_Id");
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			Integer companyID = null;
			Integer departmentID = null;
			if(StringUtils.isNotBlank(companyID_)){
				try{
					companyID=Integer.parseInt(companyID_);
					departmentID=Integer.parseInt(departmentID_);
				}catch(Exception ignore){
				}
				if (companyID != null) {
					List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(companyID);
					if (departmentVOs != null && departmentID != null) {
						List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
						int selectedDepartmentID = departmentID;
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
				
				request.setAttribute("companyID", companyID==null?"":companyID);
			}
			request.setAttribute("companyVOs", companyVOs);
			
			if(companyID!=null&&departmentID!=null){
				Set<Integer> deptIds=new HashSet<Integer>();
				deptIds.add(departmentID);
				List<DepartmentVO> departmentEntities =positionService.findDepartmentsByCompanyIDParentID(companyID,departmentID);
				if(CollectionUtils.isNotEmpty(departmentEntities)){
					for (DepartmentVO departmentVO : departmentEntities) {
						deptIds.add(departmentVO.getDepartmentID());
					}
				}
				StringBuffer sb=new StringBuffer();
				for(Integer deptId:deptIds){
					sb.append(""+deptId+",");
				}
				sb.deleteCharAt(sb.length()-1);
				params.put("dept_Id",sb.toString());
			}
			
			ListResult<CarpoolVo> carpoolEntities = carpoolService.getCarpoolList(params, page, limit);
			count = carpoolEntities.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page-1)*limit);
			request.setAttribute("carpoolEntities", carpoolEntities.getList());
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "carpoolList";
		return "carpoolList";
	}
	
	public String carpoolAdd(){
		String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			CarpoolVo carpoolVo=carpoolService.getCarpoolById(id);
			this.request.setAttribute("carpoolEntity", carpoolEntity);
			Integer companyID=carpoolVo.getCompany_Id();
			Integer departmentID=carpoolVo.getDept_Id();
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(companyID);
			if (departmentVOs != null && departmentID != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = departmentID;
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
			request.setAttribute("carpoolVo", carpoolVo);
		}
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		this.request.setAttribute("id", id);
		selectedPanel = "carpoolList";

		return "carpoolAdd";
	}
	
	public void carpoolDelete(){
		String id=request.getParameter("id");
		boolean result=false;
		try{
			carpoolService.delete(id);
			result=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		printByJson("{\"success\":"+result+"}");
	}
}
