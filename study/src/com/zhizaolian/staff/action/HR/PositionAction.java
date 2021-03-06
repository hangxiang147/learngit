package com.zhizaolian.staff.action.HR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.enums.AlterationTypeEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PositionAlterationVO;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.StaffQueryVO;
import com.zhizaolian.staff.vo.StaffVO;

public class PositionAction extends BaseAction {

	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private String panel;
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
	private String errorMessage;
	@Getter
	@Setter
	private StaffQueryVO staffQueryVO;
	@Getter
	@Setter
	private PositionAlterationVO positionAlterationVO;
	@Getter
	private List<GroupDetailVO> groupDetailVOs;
	@Setter
	private List<String> deleteGroup = new ArrayList<String>();
	@Setter
	private List<String> addGroup = new ArrayList<String>();
	@Getter
	private List<CompanyVO> companyVOs;
	@Setter
	@Getter
	private String group;   //wjp
	
	@Autowired
	private PositionService positionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private IdentityService identityService;
	
	private static final long serialVersionUID = 1L;
	
	public String findPositionList() {
		Integer companyID = StringUtils.isBlank(request.getParameter("companyID")) ? null : Integer.valueOf(request.getParameter("companyID"));
		Integer departmentID = StringUtils.isBlank(request.getParameter("departmentID")) ? null : Integer.valueOf(request.getParameter("departmentID"));
		Integer positionID = StringUtils.isBlank(request.getParameter("positionID")) ? null : Integer.valueOf(request.getParameter("positionID"));
		ListResult<GroupDetailVO> groupListResult = positionService.findGroupDetailPageList(companyID, departmentID, positionID, page, limit);
		count = groupListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		
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
				
				List<PositionVO> positionVOs = positionService.findPositionsByDepartmentID(departmentID);
				request.setAttribute("positionVOs", positionVOs);
			}
			request.setAttribute("departmentVOs", departmentVOs);
		}
		
		request.setAttribute("companyID", companyID==null?"":companyID);
		request.setAttribute("positionID", positionID==null?"":positionID);
		request.setAttribute("groupDetailVOs", groupListResult.getList());
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "findPositionList";
		return "findPositionList";
	}
	
	public String updateResponsibility() {
		try {
			Integer groupDetailID = request.getParameter("groupDetailID")==null ? 0 : Integer.valueOf(request.getParameter("groupDetailID"));
			String responsibility = request.getParameter("responsibility");
			positionService.updateResponsibility(groupDetailID, responsibility);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "编辑失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "updateResponsibility";
	}
	
	public String findStaffList() {
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(StaffStatusEnum.PROBATION.getValue());
		statusList.add(StaffStatusEnum.PRACTICE.getValue());
		statusList.add(StaffStatusEnum.FORMAL.getValue());
		List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
		count = staffService.countStaffByStatusList(statusList);
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("staffVOList", staffVOs);
		selectedPanel = "findStaffList";
		return "staffList";
	}
	
	public String findStaffListByQueryVO() {
		try {
			String trans = new String(staffQueryVO.getName().getBytes("ISO-8859-1"),"UTF-8");
			String name = URLDecoder.decode(trans , "UTF-8");
			staffQueryVO.setName(name);
			
			String tran = new String(staffQueryVO.getPersonalPost().getBytes("ISO-8859-1"), "UTF-8");
			String personalPost = URLDecoder.decode(tran, "UTF-8");
			staffQueryVO.setPersonalPost(personalPost);
			
			ListResult<StaffVO> staffVOListResult = staffService.findStaffPageListByQueryVO(staffQueryVO, page, limit);
			request.setAttribute("staffVOList", staffVOListResult.getList());
			count = staffVOListResult.getTotalCount();
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
			panel = "position";
			return "error";
		}
		
		
		selectedPanel = "findStaffList";
		return "staffList";
	}
	
	public String updateGroups() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "updateGroups";
		}
		
		String userID = request.getParameter("userID");
		try {
			for (String groupType : deleteGroup) {
				Group group = identityService.createGroupQuery().groupType(groupType).singleResult();
				identityService.deleteMembership(userID, group.getId());
				positionService.addPositionAlteration(userID, group.getId(), AlterationTypeEnum.OUT, user.getId());
			}
			
			for (String groupType : addGroup) {
				String[] varList = groupType.split("_");
				Group group = staffService.getGroup(Integer.valueOf(varList[0]), Integer.valueOf(varList[1]), Integer.valueOf(varList[2]));
				identityService.createMembership(userID, group.getId());
				positionService.addPositionAlteration(userID, group.getId(), AlterationTypeEnum.IN, user.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "操作失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		
		return "updateGroups";
	}
	
	public String findGroupDetailsByUserID() {
		String userID = request.getParameter("userID");
		try {
			List<Group> groups = staffService.findGroups(userID);
			groupDetailVOs = positionService.findGroupDetailsByGroups(groups);
			groupDetailVOs.remove(null);
			StaffVO staffVO = staffService.getStaffByUserID(userID);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (CollectionUtils.isEmpty(companyVOs)) {
				request.setAttribute("errorMessage", "获取页面加载信息失败，请联系系统管理员！");
				panel = "position";
				return "error";
			}
			
			request.setAttribute("userID", userID);
			request.setAttribute("staffName", staffVO.getLastName());
			request.setAttribute("companyVOs", companyVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工现有岗位失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			panel = "position";
			return "error";
		}
		
		selectedPanel = "findStaffList";
		return "findGroupDetailsByUserID";
	}
	
	/*当首次点开岗位历史记录时，请求该方法
	 * wjp
	 */
	public String positionHistoryStaffList() {
		  
		try {
			//获取下拉菜单所需要的数据
			List<CompanyVO> companyVOs = positionService.findAllCompanys(); 
			List<PositionVO> positionVOs = positionService.findAllPositions();
			if (CollectionUtils.isEmpty(companyVOs) || CollectionUtils.isEmpty(positionVOs)) {
				request.setAttribute("errorMessage", "获取页面加载信息失败，请联系系统管理员！");
				panel = "position";
				return "error";
			}
			request.setAttribute("staffList", "staffList");
			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("positionVOs", positionVOs);
			
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(StaffStatusEnum.PROBATION.getValue());
			statusList.add(StaffStatusEnum.PRACTICE.getValue());
			statusList.add(StaffStatusEnum.FORMAL.getValue());
			//按照查询条件模糊查询    group=1_44_2&staffQueryVO.name=哈哈哈
			Integer companyID = null;
			Integer departmentID = null;
			Integer positionID = null;
			if(group!=null&&!group.equals("")) { //如果group不为空，那么分拆字段
				String[] groups = group.split("_");
				if(!groups[0].trim().equals(""))
					companyID = Integer.valueOf(groups[0]);
				if(!groups[1].trim().equals(""))
					companyID = Integer.valueOf(groups[1]);
				if(!groups[2].trim().equals(""))
					companyID = Integer.valueOf(groups[2]);
				ListResult<StaffVO> listResult = positionService.getSelectedStaff(companyID,departmentID,positionID, staffQueryVO.getName(),statusList, page, limit);
				//得到结果之后保存，然后返回
//				int count = 0;
				if(listResult!=null) {
					count = listResult.getTotalCount();
					request.setAttribute("staffVOList",listResult.getList());
				}
				totalPage = count%limit==0 ? count/limit : count/limit+1;
				if(totalPage==0)
					totalPage = 1;
				request.setAttribute("staffQueryVO",staffQueryVO);
				request.setAttribute("companyID",companyID);
				request.setAttribute("departmentID",departmentID);
				request.setAttribute("positionID",positionID);
				selectedPanel = "positionHistory";
				return "positionHistory";
			}
			/*
				Group group = staffService.getGroup(Integer.valueOf(varList[0]), Integer.valueOf(varList[1]), Integer.valueOf(varList[2]));
				identityService.createMembership(userID, group.getId());
				positionService.addPositionAlteration(userID, group.getId(), AlterationTypeEnum.IN, user.getId());
			*/
			
			//查询出所有人员的列表 
			
			List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
			count = staffService.countStaffByStatusList(statusList);
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("staffVOList", staffVOs);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工现有岗位失败："+e.getMessage()+e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			panel = "position";
			selectedPanel = "positionHistory";
			return "error";
		}
		
		selectedPanel = "positionHistory";
		return "positionHistory";
	}
	
	/*
	 * 查询岗位历史记录   wjp
	 */
	public String positionHistory() {
		
		//使用客户端的userID参数查询岗位变动记录表
		try{
			List<CompanyVO> companyVOs = positionService.findAllCompanys(); 
			List<PositionVO> positionVOs = positionService.findAllPositions();
			ListResult<PositionAlterationVO> positionAlterationVOList = positionService.positionHistory(positionAlterationVO.getUserID(),page,limit);
//			int count = 0;
			if(positionAlterationVOList!=null) {
				request.setAttribute("positionAlterationVOList", positionAlterationVOList.getList());
				request.setAttribute("staffName",positionAlterationVOList.getList().get(0).getUserName() );
				count = positionAlterationVOList.getTotalCount();
			}else{
				count=0;
			}
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("positionVOs", positionVOs);
			request.setAttribute("history", "history");
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工岗位历史记录失败："+e.getMessage()+e.toString();
			panel = "position";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "position";
		selectedPanel = "findStaffList";
		return "positionHistory";
	}
	public void savePositonType(){
		Map<String, Object> resultMap = new HashMap<>();
		String positionId = request.getParameter("positionId");
		String positionType = request.getParameter("positionType");
		try {
			positionService.savePositonType(positionId, positionType);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", "false");
		}
		resultMap.put("success", "true");
	}
}
