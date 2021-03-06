package com.zhizaolian.staff.action.administration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.app.BaseAction;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.service.RightService;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Getter;
import lombok.Setter;

/** 
 * @author Zhouk
 * @date 2017年4月18日 下午12:52:10
 * @describtion 权限 维护 主要是维护 oa_permission oa_permissionmembership 两个表
 *
 *              主要表现在展示和新增 oa_permission 2 名称 和code oa_permissionmembership 人员/组
 *              选择人员组 选择 一项权限 (当前权限是混合的 菜单权限和流程权限在一起) 选择组类似 人员新增那里 然后 根据
 *              companyId-deptId-positionId 查询 groupId 塞入
 *              oa_permissionmembership
 **/
public class RightAction extends BaseAction {

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
	private Integer type;
	@Autowired
	private RightService rightService;
	@Setter
	@Getter
	private PermissionEntity permission;
	
	public String listRightPage() {
		request.setAttribute("result", rightService.getAllRight());
		return "listRightPage";
	}

	public void getRights() {
		printByJson(rightService.getAllRight());
	}

/*	public String saveRight() {
		String rightName = request.getParameter("rightName");
		String rightCode = request.getParameter("rightCode");
		rightService.insertRight(rightName, rightCode);;
		return "redirectRightPage";
	}*/
	public String saveRight(){
		rightService.saveRight(permission);
		return "redirectRightPage";
	}
	@Getter
	public int count = 0;
	public String listRightMemberShipPersonal() {
		String keyId = request.getParameter("keyId");
		ListResult<Object[]> resultList = rightService.getRightMemberShip(keyId,
				page, limit);
		count = resultList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("results", resultList.getList());
		request.setAttribute("keyId", keyId);
		return "listRightMemberShipPersonal";
	}
	public String listRightMemberShipGroup() {
		ListResult<Object[]> resultList = rightService
				.getGroupRightMemberShip(null, page, limit);
		count = resultList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("results", resultList.getList());
		return "listRightMemberShipGroup";
	}
	public void createMemberShip() {
		String rightId = request.getParameter("rightId");
		String keyId = request.getParameter("keyId");
		String type = request.getParameter("type");
		rightService.createRightMemberShip(keyId, type, rightId);
		request.setAttribute("type", type);
	}
	public void breakMemberShip(){
		String idStr=request.getParameter("id");
		Map<String, String> resultMap =new HashMap<>();
		try{
			Integer id=Integer.parseInt(idStr);	
			rightService.breakMemberShip(id);
			resultMap.put("success", "true");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("success", "false");
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	
	public void getGroupIdByKeys(){
		Map<String, String> resultMap =new HashMap<>();
		resultMap.put("id", rightService.getGroupIdByKeys(request.getParameter("c_id"), request.getParameter("d_id"), request.getParameter("p_id")));
		printByJson(resultMap);
	}
}
