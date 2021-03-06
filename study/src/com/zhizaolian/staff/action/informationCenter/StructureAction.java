package com.zhizaolian.staff.action.informationCenter;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.StructureNodeEntity;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StructureService;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.StructureNodeVo;

public class StructureAction extends BaseAction {
	
	@Getter
	@Setter
	private String selectedPanel;
	@Autowired
	private PositionService positionService;
	@Autowired
	private StructureService structureService;
	private static final long serialVersionUID = 1L;
	
	public String getStructureByCompanyID() {
		 String companyID = request.getParameter("companyID");
		 List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDDepartmentID(Integer.parseInt(companyID),0);
		 request.setAttribute("departments", departmentVOs);
		 request.setAttribute("companyID", companyID);
		 selectedPanel = companyID;
		 return "getStructureByCompanyID";
	}
	/**
	 * 获取所有上级部门父节点
	 */
	public void getAllParentDepIds(){
		String depId = request.getParameter("deptId");
		List<String> allParentDepIds = positionService.getAllParentDepIds(depId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("allParentDepIds", allParentDepIds);
		printByJson(resultMap);
	}
	public void modifyNode(){
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		positionService.updateName(type, id, name);
		returnSuccess();
	}
	public void deleteNode(){
		Map<String, String> resultMap = new HashMap<>();
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String companyId = request.getParameter("companyId");
		//确保部门或者职位下面没有任何人，否则无法删除
		if(!positionService.checkHasPerson(type, id, companyId)){
			positionService.deleteDepOrPos(type, id);
			resultMap.put("canDelete", "true");
		}else{
			resultMap.put("canDelete", "false");
		}
		printByJson(resultMap);
	}
	public String showNewStructure(){
		selectedPanel = "newStructure";
		StructureNodeVo structureDatas = structureService.getStructureDatas();
		String dataScource = JSONObject.fromObject(structureDatas).toString();
		request.setAttribute("dataScource", dataScource);
		String depth = request.getParameter("depth");
		if(null == depth){
			request.setAttribute("depth", "3");
		}else{
			request.setAttribute("depth", depth);
		}
		return "newStructure";
	}
	@Setter
	@Getter
	private StructureNodeEntity structureNode;
	@Setter
	@Getter
	private int depth;
	public String addStructureNode(){
		structureService.editStructureNode(structureNode);
		//当前节点的深度
		depth = structureService.getCurrentNodeDepth(structureNode);
		depth++;
		return "render_newStructure";
	}
	public String deleteStructureNode(){
		String nodeId = request.getParameter("id");
		structureService.deleteStructureNode(nodeId);
		structureNode = structureService.getStructureNode(Integer.parseInt(nodeId));
		//当前节点的深度
		depth = structureService.getCurrentNodeDepth(structureNode);
		depth++;
		return "render_newStructure";
	}
	public void updateStructureNode(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			structureService.editStructureNode(structureNode);
			resultMap.put("success", true);
			resultMap.put("structureNode", structureNode);
		} catch (Exception e) {
			resultMap.put("success", false);
		}
		printByJson(resultMap);
	}
}
