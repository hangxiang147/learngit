package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.entity.AppPermissionEntity;
import com.zhizaolian.staff.entity.AppRoleEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.AppService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.UserRightCenterService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
@Controller
@Scope(value="prototype")
public class UserRightCenterAction extends BaseAction{

	private static final long serialVersionUID = 3634753138059379501L;
	@Autowired
	private UserRightCenterService userRightCenterService;
	@Autowired
	private AppService appService;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Autowired
	private PositionService positionService;

	public String showUserAppShipList(){
		String staffName = request.getParameter("staffName");
		List<AppInfoEntity> appInfos = appService.findAllAppInfos();
		if(!CollectionUtils.isEmpty(appInfos)){
			String appId = request.getParameter("appId");
			if(StringUtils.isBlank(appId)){
				appId = String.valueOf(appInfos.get(0).getId());
			}
			ListResult<Object> userAppShipList = userRightCenterService.findUserAppShipList(staffName, appId, limit, page);
			count = userAppShipList.getTotalCount();
			totalPage = count%limit==0 ? count/limit:count/limit+1;
			if(totalPage==0) totalPage = 1;
			request.setAttribute("userAppShipList", userAppShipList.getList());
			request.setAttribute("appInfos", appInfos);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("companys", companyVOs);
			request.setAttribute("staffName", staffName);
			request.setAttribute("appId", appId);
			request.setAttribute("result", request.getParameter("result"));
		}
		return "showUserAppShipList";
	}
	@Getter
	private String result;
	public String saveUserAppShip(){
		appId = request.getParameter("appId");
		String authUserIds = request.getParameter("authUserIds");
		User user = (User)request.getSession().getAttribute("user");
		result = String.valueOf(userRightCenterService.saveUserAppShip(appId, authUserIds, user.getId()));
		return "render_showUserAppShipList";
	}
	public String deleteUserAppShip(){
		String userAppId = request.getParameter("userAppId");
		result = String.valueOf(userRightCenterService.deleteUserAppShip(userAppId));
		return "render_showUserAppShipList";
	}
	public String deleteApp(){
		String appId = request.getParameter("appId");
		userRightCenterService.deleteApp(appId);
		return "render_showApps";
	}
	public String deleteRole(){
		String roleId = request.getParameter("roleId");
		String appId = request.getParameter("appId");
		userRightCenterService.deleteRole(appId, roleId);
		return "render_showRoles";
	}
	public String showApps(){
		List<AppInfoEntity> appInfos = userRightCenterService.findAllApps();
		request.setAttribute("appInfos", appInfos);
		return "showApps";
	}
	@Setter
	@Getter
	private AppInfoEntity appInfo;
	@Setter
	private File icon;
	@Setter
	private String iconFileName;

	public String saveApp(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			appInfo.setCreatorId(user.getId());
			userRightCenterService.saveAppInfo(appInfo, icon, iconFileName);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showApps";
	}
	public void checkAppName(){
		String appName = request.getParameter("appName");
		String id = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("exist", appService.checkAppNameExist(appName, id));
		printByJson(resultMap);
	} 
	public String showRoles(){
		List<AppInfoEntity> appInfos = appService.findAllAppInfos();
		if(!CollectionUtils.isEmpty(appInfos)){
			String appId = request.getParameter("appId");
			String roleName = request.getParameter("roleName");
			if(StringUtils.isBlank(appId)){
				appId = String.valueOf(appInfos.get(0).getAppId());
			}
			if(Constants.MES_APP_ID.equals(appId)){
				request.setAttribute("mes", true);
			}else{
				request.setAttribute("mes", false);
			}
			ListResult<AppRoleEntity> roles = userRightCenterService.findRolesByAppId(appId, limit, page, roleName);
			count = roles.getTotalCount();
			totalPage = count%limit==0 ? count/limit:count/limit+1;
			if(totalPage==0) totalPage = 1;
			request.setAttribute("roles", roles.getList());
			request.setAttribute("appInfos", appInfos);
			request.setAttribute("appId", appId);
			request.setAttribute("roleName", roleName);
		}
		return "showRoles";
	}
	@Setter
	@Getter
	public AppRoleEntity roleInfo;
	@Setter
	@Getter
	private String appId;
	
	public String saveRole(){
		try {
			userRightCenterService.saveRoleInfo(roleInfo);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		appId = roleInfo.getAppId();
		return "render_showRoles";
	}
	public void saveRight(){

	}
	public void checkRoleName(){
		String roleName = request.getParameter("roleName");
		String appId = request.getParameter("appId");
		String id = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("exist", userRightCenterService.checkRoleNameExist(roleName, appId, id));
		printByJson(resultMap);
	} 
	public void checkRightCode(){
		String permissionCode = request.getParameter("permissionCode");
		String appId = request.getParameter("appId");
		String id = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("exist", userRightCenterService.checkRightCodeExist(permissionCode, appId, id));
		printByJson(resultMap);
	}
	public String showRights(){
		List<AppInfoEntity> appInfos = appService.findAllAppInfos();
		if(!CollectionUtils.isEmpty(appInfos)){
			String appId = request.getParameter("appId");
			if(StringUtils.isBlank(appId)){
				appId = appInfos.get(0).getAppId();
			}
			List<Object> firstNodes = userRightCenterService.getAllFirstLevelNodes(appId);
			request.setAttribute("firstNodes", JSONArray.fromObject(firstNodes));
			request.setAttribute("appInfos", appInfos);
			request.setAttribute("appId", appId);
			if(Constants.COM_APP_ID.equals(appId)){
				request.setAttribute("com", true);
			}else if(Constants.MES_APP_ID.equals(appId)){
				request.setAttribute("mes", true);
			}
		}
		return "showRights";
	}
	public void getFirstLevelNodes(){
		String appId = request.getParameter("appId");
		List<Object> firstNodes = userRightCenterService.getAllFirstLevelNodes(appId);
		printByJson(firstNodes);
	}
	public void showChildNodes(){
		String id = request.getParameter("id");
		List<Object> childNodes = userRightCenterService.findChildNodesByPId(id);
		printByJson(childNodes);
	}
	public void addFirstRightContent(){
		Map<String, Object> resultMap = new HashMap<>();
		String appId = request.getParameter("appId");
		String rightName = request.getParameter("name");
		String code = request.getParameter("code");
		String type = request.getParameter("type");
		String sort = request.getParameter("sort");
		String isUsed = request.getParameter("isUsed");
		String pageUrl = request.getParameter("pageUrl");
		String requestUrl = request.getParameter("requestUrl");
		AppPermissionEntity permission = new AppPermissionEntity();
		permission.setAppId(appId);
		permission.setPermissionName(rightName);
		permission.setType(type);
		if(StringUtils.isNotBlank(code)){
			permission.setPermissionCode(code);
		}
		permission.setSort(sort);
		permission.setIsUsed(isUsed);
		permission.setPageUrl(pageUrl);
		permission.setRequestUrl(requestUrl);
		try {
			Integer rightId = userRightCenterService.addRoleRight(permission);
			if(0 == rightId){
				resultMap.put("success", false);
			}else{
				Map<String, Object> nodeMap = new HashMap<>();
				nodeMap.put("id", rightId);
				nodeMap.put("pid", 0);
				String name = "";
				if(StringUtils.isNotBlank(code)){
					name = rightName+"【"+code+"】";
				}else{
					name = rightName;
				}
				if(Constants.MES_APP_ID.equals(appId)){
					name += "<span>"+isUsed;
				}
				nodeMap.put("name", name);
				if(Constants.RIGHT.equals(type)){
					nodeMap.put("iconSkin", "diy");
				}
				nodeMap.put("type", type);
				nodeMap.put("sort", sort);
				nodeMap.put("isUsed", isUsed);
				nodeMap.put("pageUrl", pageUrl);
				nodeMap.put("requestUrl", requestUrl);
				if(null != code){
					nodeMap.put("permissionCode", code);
				}
				nodeMap.put("permissionName", rightName);
				resultMap.put("nodeMap", nodeMap);
				resultMap.put("success", true);
			}
		} catch (Exception e) {
			resultMap.put("success", false);
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void addRight(){
		Map<String, Object> resultMap = new HashMap<>();
		String parentId = request.getParameter("parentId");
		String rightName = request.getParameter("rightName");
		String rightCode = request.getParameter("rightCode");
		String appId = request.getParameter("appId");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String sort = request.getParameter("sort");
		String isUsed = request.getParameter("isUsed");
		String pageUrl = request.getParameter("pageUrl");
		String requestUrl = request.getParameter("requestUrl");
		String level = request.getParameter("level");
		try {
			//编辑状态
			if(StringUtils.isNotBlank(id)){
				//若目录下有节点，则该目录不可修改为权限
				if(Constants.RIGHT.equals(type) &&
						userRightCenterService.checkHasChild(Integer.parseInt(id))){
					resultMap.put("success", false);
					resultMap.put("msg", "父节点不可为权限");
					printByJson(resultMap);
					return;
				}
			}
			AppPermissionEntity permission = new AppPermissionEntity();
			if(StringUtils.isNotBlank(id)){
				permission.setId(Integer.parseInt(id));
			}
			permission.setAppId(appId);
			if(StringUtils.isNotBlank(parentId)){
				permission.setParentId(Integer.parseInt(parentId));
			}
			permission.setPermissionName(rightName);
			if(StringUtils.isNotBlank(rightCode)){
				permission.setPermissionCode(rightCode);
			}else{
				permission.setPermissionCode(null);
			}
			permission.setType(type);
			permission.setSort(sort);
			permission.setIsUsed(isUsed);
			permission.setPageUrl(pageUrl);
			permission.setRequestUrl(requestUrl);
			permission.setLevel(level);
			int permissionId = userRightCenterService.addRoleRight(permission);
			if(permissionId==0){
				resultMap.put("success", false);
			}else{
				resultMap.put("success", true);
			}
		} catch (Exception e) {
			resultMap.put("success", false);
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void deleteRightNode(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		try {
			//mes无法删除子节点，分配角色的权限也无法删除
			if(Constants.MES_APP_ID.equals(appId)){
				if(userRightCenterService.checkHasChild(Integer.parseInt(id))){
					resultMap.put("hasChild", true);
					printByJson(resultMap);
					return;
				}
				//若是权限，判断是否已分配角色
				if(userRightCenterService.checkIsRight(id) && userRightCenterService.checkIsAllocated(id)){
					resultMap.put("isAllocated", true);
					printByJson(resultMap);
					return;
				}
			}
			userRightCenterService.deleteRoleRight(appId, id);
			resultMap.put("success", true);
		} catch (Exception e) {
			resultMap.put("success", false);
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public String showUserRoleShipList(){
		List<AppInfoEntity> appInfos = appService.findAllAppInfos();
		if(!CollectionUtils.isEmpty(appInfos)){
			String appId = request.getParameter("appId");
			if(StringUtils.isBlank(appId)){
				appId = appInfos.get(0).getAppId();
			}
			String staffName = request.getParameter("staffName");
			String roleName = request.getParameter("roleName");
			ListResult<Object> userRoleShips = userRightCenterService.findUserRoleShips(
					appId, staffName, roleName, limit, page);
			request.setAttribute("userRoleShips", userRoleShips.getList());
			count = userRoleShips.getTotalCount();
			totalPage = count%limit==0 ? count/limit:count/limit+1;
			if(totalPage==0) totalPage = 1;
			request.setAttribute("staffName", staffName);
			request.setAttribute("roleName", roleName);
			request.setAttribute("appId", appId);
			request.setAttribute("appInfos", appInfos);
		}
		return "showUserRoleShips";
	}
	public String modifyUserRoleShip(){
		String userId = request.getParameter("userId");
		String appId = request.getParameter("appId");
		List<Object> userRoleShips = userRightCenterService.findUserRoleShipsByUserId(userId, appId);
		request.setAttribute("userRoleShips", userRoleShips);
		request.setAttribute("userId", userId);
		request.setAttribute("appId", appId);
		return "modifyUserRoleShip";
	}
	public String modifyRoleRightShip(){
		String roleId = request.getParameter("roleId");
		String appId = request.getParameter("appId");
		List<Object> firstNodeShips = userRightCenterService.getFirstNodeShips(appId, roleId);
		request.setAttribute("firstNodeShips", JSONArray.fromObject(firstNodeShips));
		request.setAttribute("roleId", roleId);
		request.setAttribute("appId", appId);
		return "modifyRoleRightShip";
	}
	@Setter
	private String[] roleId;
	
	public String saveUserRoleShips(){
		String userId = request.getParameter("userId");
		appId = request.getParameter("appId");
		try {
			userRightCenterService.saveUserRoleShips(userId, roleId, appId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "render_showUserRoleShips";
	}
	public String showRoleRightShipList(){
		List<AppInfoEntity> appInfos = appService.findAllAppInfos();
		if(!CollectionUtils.isEmpty(appInfos)){
			String appId = request.getParameter("appId");
			if(StringUtils.isBlank(appId)){
				appId = appInfos.get(0).getAppId();
			}
			String roleName = request.getParameter("roleName");
			String rightName = request.getParameter("rightName");
			ListResult<Object> roleRightShips = userRightCenterService.findRoleRightShips(
					appId, rightName, roleName, limit, page);
			request.setAttribute("roleRightShips", roleRightShips.getList());
			count = roleRightShips.getTotalCount();
			totalPage = count%limit==0 ? count/limit:count/limit+1;
			if(totalPage==0) totalPage = 1;
			request.setAttribute("rightName", rightName);
			request.setAttribute("roleName", roleName);
			request.setAttribute("appId", appId);
			request.setAttribute("appInfos", appInfos);
		}
		return "showRoleRightShips";
	}
	public void showChildNodeShips(){
		String id = request.getParameter("id");
		String roleId = request.getParameter("roleId");
		List<Object> childNodeShips = userRightCenterService.findChildNodeShipsByPId(id, roleId);
		printByJson(childNodeShips);
	}
	public void saveRoleRightShips(){
		Map<String, Object> resultMap = new HashMap<>();
		String permissionIds = request.getParameter("permissionIds");
		String roleId = request.getParameter("roleId");
		String checked= request.getParameter("checked");
		String parent = request.getParameter("parent");
		String appId = request.getParameter("appId");
		try {
			boolean success = userRightCenterService.saveRoleRightShips(appId, parent, checked, permissionIds, roleId);
			resultMap.put("success", success);
		} catch (Exception e) {
			resultMap.put("success", false);
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void generatePermissionCode(){
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String permissionCode = userRightCenterService.generatePermissionCode(id, name);
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("permissionCode", permissionCode);
		printByJson(resultMap);
	}
	public String showUserApps(){
		request.setAttribute("message", request.getParameter("message"));
		request.setAttribute("bind", request.getParameter("bind"));
		return "showUserApps";
	}
	public void checkRoleIsAllocated(){
		String roleId = request.getParameter("roleId");
		Map<String, Object> resultMap = new HashMap<>();
		if(userRightCenterService.checkRoleIsAllocated(roleId)){
			resultMap.put("allocated", true);
		}else{
			resultMap.put("allocated", false);
		}
		printByJson(resultMap);
	}
	//刷数据++++++++++++++++++++
	public void synPermissionData(){
		userRightCenterService.synData();
	}
	public void synComUserRoleShips(){
		userRightCenterService.synComUserRoleShips();
	}
	//++++++++++++++++++++++++
}
