package com.zhizaolian.staff.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.AppService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.UserRightCenterService;

import common.Logger;
import lombok.Setter;

@Controller
@Scope(value="prototype")
public class AppAction extends BaseAction{

	private static final long serialVersionUID = -7210593221025196852L;
	private Logger logger = Logger.getLogger(AppAction.class);
	@Setter
	private String appName;
	@Setter
	private String userName;//OA用户账号
	@Setter
	private String password;//OA用户密码
	@Autowired
	private AppService appService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private UserRightCenterService userRightCenterService;

	public void registerApp(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if(staffService.loginValidate(userName, password) != null){
				//检查应用是否已存在
				if(!appService.checkAppNameExist(appName, null)){
					String appId = appService.saveAppInfo(appName);
					resultMap.put("appId", appId);
					resultMap.put("result", 0);
				}else{
					resultMap.put("result", 1);
					resultMap.put("msg", "应用名已存在");
				}
			}else{
				resultMap.put("result", 1);
				resultMap.put("msg", "OA账号或密码不对，没有权限注册");
			}
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 新增用户角色
	 */
	public void addUserRole(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String roleName = request.getParameter("roleName");
		String roleDescription = request.getParameter("roleDescription");
		String appId = request.getParameter("appId");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String access_token = request.getParameter("access_token");
		String paramStr = "";
		if(StringUtils.isNotBlank(roleDescription)){
			paramStr = id+roleName+roleDescription+appId+appSecret;
		}else{
			paramStr = id+roleName+appId+appSecret;
		}
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(roleName)){
				resultMap.put("result", 1);
				resultMap.put("msg", "roleName不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.saveUserRole(id, roleName, roleDescription, appId);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 更新用户角色
	 */
	public void updateUserRole(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String roleName = request.getParameter("roleName");
		String roleDescription = request.getParameter("roleDescription");
		String appId = request.getParameter("appId");
		String access_token = request.getParameter("access_token");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = "";
		if(StringUtils.isNotBlank(roleDescription)){
			paramStr = id+roleName+roleDescription+appId+appSecret;
		}else{
			paramStr = id+roleName+appId+appSecret;
		}
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(roleName)){
				resultMap.put("result", 1);
				resultMap.put("msg", "roleName不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}			
			userRightCenterService.updateUserRole(roleName, roleDescription, id);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 删除用户角色
	 */
	public void deleteUserRole(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		String access_token = request.getParameter("access_token");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = appId+id+appSecret;
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.deleteUserRole(id);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 新增角色权限
	 */
	public void addRoleRight(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		String appId = request.getParameter("appId");
		String parentId = request.getParameter("parentId");
		String access_token = request.getParameter("access_token");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = "";
		if(StringUtils.isNotBlank(code)){
			if(StringUtils.isNotBlank(parentId)){
				paramStr = id+name+type+code+appId+parentId+appSecret;
			}else{
				paramStr = id+name+type+code+appId+appSecret;
			}
		}else if(StringUtils.isNotBlank(parentId)){
			paramStr = id+name+appId+parentId+appSecret;
		}else{
			paramStr = id+name+appId+appSecret;
		}
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(name)){
				resultMap.put("result", 1);
				resultMap.put("msg", "name不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(type)){
				resultMap.put("result", 1);
				resultMap.put("msg", "type不能为空");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.addRolePermission(id, appId, parentId, name, code, type);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 更新角色权限
	 */
	public void updateRoleRight(){
		Map<String, Object> resultMap = new HashMap<>();
		String appId = request.getParameter("appId");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String access_token = request.getParameter("access_token");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = "";
		if(StringUtils.isNotBlank(code)){
			paramStr = name+code+appId+id+appSecret;
		}else{
			paramStr = name+appId+id+appSecret;
		}
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(name)){
				resultMap.put("result", 1);
				resultMap.put("msg", "name不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.updateRoleRight(id, name, code);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 删除角色权限
	 */
	public void deleteRoleRight(){
		Map<String, Object> resultMap = new HashMap<>();
		String id = request.getParameter("id");
		String appId = request.getParameter("appId");
		String access_token = request.getParameter("access_token");
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = appId+id+appSecret;
		if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
			resultMap.put("result", 1);
			resultMap.put("msg", "access_token校验不通过");
			printByJson(resultMap);
			return;
		}
		try {
			if(StringUtils.isBlank(appId)){
				resultMap.put("result", 1);
				resultMap.put("msg", "appId不能为空");
				printByJson(resultMap);
				return;
			}
			if(StringUtils.isBlank(id)){
				resultMap.put("result", 1);
				resultMap.put("msg", "id不能为空");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.deleteRoleRightBy_id(id);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 绑定角色权限
	 */
	public void addRoleRightShip(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String roleId = request.getParameter("roleId");
			String permissionIds = request.getParameter("permissionIds");
			String appId = request.getParameter("appId");
			String access_token = request.getParameter("access_token");
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = appId+roleId+permissionIds+appSecret;
			if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
				resultMap.put("result", 1);
				resultMap.put("msg", "access_token校验不通过");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.addRoleRightShip(roleId, permissionIds);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 取消角色权限
	 */
	public void deleteRoleRightShip(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String roleId = request.getParameter("roleId");
			String permissionIds = request.getParameter("permissionIds");
			String appId = request.getParameter("appId");
			String access_token = request.getParameter("access_token");
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = appId+roleId+permissionIds+appSecret;
			if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
				resultMap.put("result", 1);
				resultMap.put("msg", "access_token校验不通过");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.deleteRoleRightShip(roleId, permissionIds);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 绑定用户角色
	 */
	public void addUserRoleShip(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String roleIds = request.getParameter("roleIds");
			String appId = request.getParameter("appId");
			String userId = request.getParameter("userId");
			String access_token = request.getParameter("access_token");
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = appId+roleIds+userId+appSecret;
			if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
				resultMap.put("result", 1);
				resultMap.put("msg", "access_token校验不通过");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.addUserRoleShip(roleIds, userId);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	/**
	 * 取消用户角色
	 */
	public void deleteUserRoleShip(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String roleIds = request.getParameter("roleIds");
			String appId = request.getParameter("appId");
			String userId = request.getParameter("userId");
			String access_token = request.getParameter("access_token");
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = appId+roleIds+userId+appSecret;
			if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
				resultMap.put("result", 1);
				resultMap.put("msg", "access_token校验不通过");
				printByJson(resultMap);
				return;
			}
			userRightCenterService.deleteUserRoleShip(roleIds, userId);
			resultMap.put("result", 0);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
	public void getUserInfoAndPermissionInfo(){
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String appId = request.getParameter("appId");
			String userId = request.getParameter("userId");
			String access_token = request.getParameter("access_token");
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = appId+userId+appSecret;
			if(!DigestUtils.md5Hex(paramStr).equals(access_token)){
				resultMap.put("result", 1);
				resultMap.put("msg", "access_token校验不通过");
				printByJson(resultMap);
				return;
			}
			StaffEntity staff = staffService.getStaffByUserId(userId);
			Map<String, Object> staffInfoMap = new HashMap<>();
			staffInfoMap.put("userID", staff.getUserID());
			staffInfoMap.put("staffName", staff.getStaffName());
			staffInfoMap.put("gender", staff.getGender());
			staffInfoMap.put("telephone", staff.getTelephone());
			staffInfoMap.put("idNumber", staff.getIdNumber());
			staffInfoMap.put("address", staff.getAddress());
			staffInfoMap.put("headImgUrl", "http://www.zhizaolian.com:9090/app/showImage?id=" + staff.getHeadImgId());
			//com需要返回菜单id以及编码、权限编码（父子关系）
			if(Constants.COM_APP_ID.equals(appId)){
				Object permissionInfo = userRightCenterService.findAllPermissionShips(staff.getUserID(), appId);
				resultMap.put("permissions", permissionInfo);
			}else{
				List<Object> permissionInfo = userRightCenterService.findAllPermissionCodes(staff.getUserID(), appId);
				resultMap.put("permissions", permissionInfo);
			}
			resultMap.put("result", 0);
			resultMap.put("staffInfo", staffInfoMap);
		} catch (Exception e) {
			resultMap.put("result", 1);
			resultMap.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}
}
