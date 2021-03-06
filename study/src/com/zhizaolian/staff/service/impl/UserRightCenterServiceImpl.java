package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.entity.AppPermissionEntity;
import com.zhizaolian.staff.entity.AppPermissionShipEntity;
import com.zhizaolian.staff.entity.AppRoleEntity;
import com.zhizaolian.staff.entity.AppRoleShipEntity;
import com.zhizaolian.staff.entity.AppUserEntity;
import com.zhizaolian.staff.entity.ComPermissionEntity;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.UserRightCenterService;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.HttpClientUtil;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Cleanup;
import net.sf.json.JSONObject;
@Service(value="userRightCenterService")
public class UserRightCenterServiceImpl implements UserRightCenterService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private StaffDao staffDao;
	@Override
	public ListResult<Object> findUserAppShipList(String staffName, String appId, Integer limit, Integer page) {
		String sql = "SELECT\n" +
				"	staff.StaffName,\n" +
				"	CONCAT(CompanyName,'-',DepartmentName),\n" +
				"	appInfo.appName,\n" +
				"	appUser.id\n" +
				"FROM\n" +
				"	OA_staff staff,\n" +
				"	ZZL_AppUser appUser,\n" +
				"	ZZL_AppInfo appInfo,\n" +
				"	(select * from ACT_ID_MEMBERSHIP GROUP BY USER_ID_) ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep\n" +
				"WHERE\n" +
				"	staff.UserID = appUser.userId\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND appUser.isDeleted = 0\n" +
				"AND appUser.appId = appInfo.id\n" +
				"AND ship.USER_ID_ = appUser.userId\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND detail.CompanyID = company.CompanyID\n" +
				"AND detail.DepartmentID = dep.DepartmentID\n";
		if(StringUtils.isNotBlank(staffName)){
			sql += "AND StaffName like '%"+staffName+"%'\n";
		}
		if(StringUtils.isNotBlank(appId)){
			sql += "AND appUser.appId ="+appId;
		}
		List<Object> objList = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(appUser.id)\n" +
				"FROM\n" +
				"	OA_Staff staff,\n" +
				"	ZZL_AppUser appUser\n" +
				"WHERE\n" +
				"	staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND appUser.isDeleted = 0\n" +
				"AND staff.UserID = appUser.userId\n";
		if(StringUtils.isNotBlank(staffName)){
			sqlCount += "AND StaffName like '%"+staffName+"%'\n";
		}
		if(StringUtils.isNotBlank(appId)){
			sqlCount += "AND appUser.appId ="+appId;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(objList, count);
	}
	@Override
	public int saveUserAppShip(String appId, String authUserIdStr, String userId) {
		String[] authUserIds = authUserIdStr.split(",");
		if(Constants.MES_APP_ID.equals(appId)){
			String updateUserStatusUrl = Constants.MES_URL+"/updateUserStatus.do";
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("authUserIds", authUserIdStr);
			//A表示激活
			dataMap.put("status", "A");
			String paramStr = authUserIdStr+dataMap.get("status")+Constants.MES_APP_SECRET; 
			String access_token = DigestUtils.md5Hex(paramStr);
			dataMap.put("access_token", access_token);
			String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), updateUserStatusUrl);
			JSONObject resultObj = JSONObject.fromObject(responseResult);
			if("0".equals(resultObj.get("result"))){
				for(String authUserId: authUserIds){
					if(!checkHasRight(authUserId, appId)){
						AppUserEntity appUser = new AppUserEntity();
						appUser.setAppId(appId);
						appUser.setUserId(authUserId);
						appUser.setOperatorId(userId);
						appUser.setAddTime(new Date());
						baseDao.hqlSave(appUser);
					}
				}
				return 0;
			}else{
				return -1;
			}
		}else{
			for(String authUserId: authUserIds){
				if(!checkHasRight(authUserId, appId)){
					AppUserEntity appUser = new AppUserEntity();
					appUser.setAppId(appId);
					appUser.setUserId(authUserId);
					appUser.setOperatorId(userId);
					appUser.setAddTime(new Date());
					baseDao.hqlSave(appUser);
				}
			}
		}
		return 0;
	}
	private boolean checkHasRight(String authUserId, String appId) {
		String sql = "select count(*) from ZZL_AppUser where isDeleted=0 and userId='"
				+authUserId+"' and appId="+appId;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public int deleteUserAppShip(String userAppId) {
		String hql = "from AppUserEntity where id="+userAppId;
		AppUserEntity appUser = (AppUserEntity) baseDao.hqlfindUniqueResult(hql);
		AppInfoEntity appInfo = getAppInfoById(Integer.parseInt(appUser.getAppId()));
		if(Constants.MES_APP_ID.equals(appInfo.getAppId())){
			String updateUserStatusUrl = Constants.MES_URL+"/updateUserStatus.do";
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("authUserIds", appUser.getUserId());
			//I表示关闭
			dataMap.put("status", "I");
			String paramStr = appUser.getUserId()+dataMap.get("status")+Constants.MES_APP_SECRET; 
			String access_token = DigestUtils.md5Hex(paramStr);
			dataMap.put("access_token", access_token);
			String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), updateUserStatusUrl);
			JSONObject resultObj = JSONObject.fromObject(responseResult);
			if("0".equals(resultObj.get("result"))){
				String sql = "update ZZL_AppUser set isDeleted=1 where id="+userAppId;
				baseDao.excuteSql(sql);
				return 0;
			}else{
				return -1;
			}
		}else{
			String sql = "update ZZL_AppUser set isDeleted=1 where id="+userAppId;
			baseDao.excuteSql(sql);
		}
		return 0;
	}
	@Override
	public boolean checkHasAppRight(String appId, String userID) {
		String sql = "SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	ZZL_AppUser appUser,\n" +
				"	ZZL_AppInfo appInfo\n" +
				"WHERE\n" +
				"	appUser.appId = appInfo.id\n" +
				"AND\n" +
				"	userId = '"+userID+"'\n" +
				"AND appInfo.appId = '"+appId+"'\n" +
				"AND appUser.isDeleted = 0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public void saveUserRole(String id, String roleName, String roleDescription, String appId) {
		AppRoleEntity appRole = new AppRoleEntity();
		appRole.set_id(Integer.parseInt(id));
		appRole.setAppId(appId);
		appRole.setRoleName(roleName);
		appRole.setAddTime(new Date());
		appRole.setRoleDescription(roleDescription);
		baseDao.hqlSave(appRole);
	}
	@Override
	public void updateUserRole(String roleName, String roleDescription, String id) {
		String sql = "update ZZL_AppRole set roleName='"+EscapeUtil.decodeSpecialChars(roleName)+"', roleDescription='"+
				EscapeUtil.decodeSpecialChars(roleDescription)+"' where isDeleted=0 and _id="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public void deleteUserRole(String id) {
		String sql = "update ZZL_AppRole set isDeleted=1 where _id="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public int addRoleRight(AppPermissionEntity permission) {
		if(null == permission.getId()){
			permission.setAddTime(new Date());
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(permission.getAppId())){
				String addPermissionUrl = Constants.MES_URL+"/addPermission.do";
				Map<String, String> dataMap = new HashMap<>();
				dataMap.put("name", permission.getPermissionName());
				dataMap.put("type", "目录".equals(permission.getType()) ? "1":"2");
				dataMap.put("sort", permission.getSort()==null ? "":permission.getSort());
				dataMap.put("isUsed", permission.getIsUsed());
				dataMap.put("pageUrl", permission.getPageUrl()==null ? "":permission.getPageUrl());
				dataMap.put("requestUrl", permission.getRequestUrl()==null ? "":permission.getRequestUrl());
				if(null != permission.getParentId()){
					int parent_id = getPermission_id(permission.getParentId());
					dataMap.put("parentId", String.valueOf(parent_id));
					if(StringUtils.isNotBlank(permission.getPermissionCode())){
						dataMap.put("code", permission.getPermissionCode());
						String paramStr = permission.getPermissionName()+permission.getPermissionCode()+parent_id+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
						+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET; 
						String access_token = DigestUtils.md5Hex(paramStr);
						dataMap.put("access_token", access_token);
					}else{
						dataMap.put("code", "");
						String paramStr = permission.getPermissionName()+parent_id+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
						+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET; 
						String access_token = DigestUtils.md5Hex(paramStr);
						dataMap.put("access_token", access_token);
					}
				}else if(StringUtils.isNotBlank(permission.getPermissionCode())){
					dataMap.put("parentId", "");
					dataMap.put("code", permission.getPermissionCode());
					String paramStr = permission.getPermissionName()+permission.getPermissionCode()+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
					+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET;
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}else{
					dataMap.put("parentId", "");
					dataMap.put("code", "");
					String paramStr = permission.getPermissionName()+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
					+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET;
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addPermissionUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				int permissionId = 0;
				int _id = 0;
				if("0".equals(resultObj.get("result"))){
					_id = Integer.parseInt(resultObj.getString("id"));
					permission.set_id(_id);
					permissionId = baseDao.hqlSave(permission);
				}else{
					return 0;
				}
				//mes，新建二级目录时，会自动生成目录名+'查询'的子权限
				if("0".equals(permission.getLevel())){
					String permissionName = permission.getPermissionName()+"查询";
					dataMap = new HashMap<>();
					dataMap.put("name", permissionName);
					dataMap.put("type", "2");
					dataMap.put("sort", "1");
					dataMap.put("isUsed", "Y");
					dataMap.put("code", "Search");
					dataMap.put("parentId", String.valueOf(_id));
					dataMap.put("pageUrl", "");
					dataMap.put("requestUrl", "");
					String appId = permission.getAppId();
					permission = new AppPermissionEntity();
					permission.setPermissionName(permissionName);
					permission.setType("权限");
					permission.setSort("1");
					permission.setIsUsed("Y");
					permission.setPermissionCode("Search");
					permission.setParentId(permissionId);
					permission.setAppId(appId);
					permission.setAddTime(new Date());
					String paramStr = permissionName+dataMap.get("code")+_id+dataMap.get("type")+dataMap.get("sort")+dataMap.get("isUsed")
					        +Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
					responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addPermissionUrl);
					resultObj = JSONObject.fromObject(responseResult);
					if("0".equals(resultObj.get("result"))){
						_id = Integer.parseInt(resultObj.getString("id"));
						permission.set_id(_id);
						baseDao.hqlSave(permission);
					}else{
						return 0;
					}
				}
				return permissionId;
			}else{
				return baseDao.hqlSave(permission);
			}
		}else{
			AppPermissionEntity oldPermission = getPermissionById(permission.getId());
			oldPermission.setType(permission.getType());
			oldPermission.setPermissionName(permission.getPermissionName());
			if(StringUtils.isBlank(permission.getPermissionCode())){
				oldPermission.setPermissionCode(null);
			}else{
				oldPermission.setPermissionCode(permission.getPermissionCode());
			}
			oldPermission.setSort(permission.getSort());
			oldPermission.setIsUsed(permission.getIsUsed());
			oldPermission.setPageUrl(permission.getPageUrl());
			oldPermission.setRequestUrl(permission.getRequestUrl());
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(permission.getAppId())){
				int permission_id = getPermission_id(permission.getId());
				String updatePermissionUrl = Constants.MES_URL+"/updatePermission.do";
				Map<String, String> dataMap = new HashMap<>();
				dataMap.put("id", String.valueOf(permission_id));
				dataMap.put("name", permission.getPermissionName());
				dataMap.put("type", "目录".equals(permission.getType()) ? "1":"2");
				dataMap.put("sort", permission.getSort()==null ? "":permission.getSort());
				dataMap.put("isUsed", permission.getIsUsed());
				dataMap.put("pageUrl", permission.getPageUrl()==null ? "":permission.getPageUrl());
				dataMap.put("requestUrl", permission.getRequestUrl()==null ? "":permission.getRequestUrl());
				if(StringUtils.isNotBlank(permission.getPermissionCode())){
					dataMap.put("code", permission.getPermissionCode());
					String paramStr = permission_id+permission.getPermissionName()+permission.getPermissionCode()+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
					+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}else{
					dataMap.put("code", "");
					String paramStr = permission_id+permission.getPermissionName()+dataMap.get("type")+dataMap.get("sort")+permission.getIsUsed()
					+permission.getPageUrl()+permission.getRequestUrl()+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), updatePermissionUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					baseDao.hqlUpdate(oldPermission);
					return oldPermission.getId();
				}else{
					return 0;
				}
			}else{
				baseDao.hqlUpdate(oldPermission);
				return oldPermission.getId();
			}
		}
	}
	private AppPermissionEntity getPermissionById(int rightId) {
		String hql = "from AppPermissionEntity where id="+rightId;
		return (AppPermissionEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void updateRoleRight(String id, String name, String code) {
		int rightId = Integer.parseInt(id);
		AppPermissionEntity permission = getPermissionBy_id(rightId);
		permission.setPermissionName(name);
		if(StringUtils.isBlank(code)){
			permission.setPermissionCode(null);
		}else{
			permission.setPermissionCode(code);
		}
		baseDao.hqlUpdate(permission);
	}
	private AppPermissionEntity getPermissionBy_id(int rightId) {
		String hql = "from AppPermissionEntity where _id="+rightId;
		return (AppPermissionEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void deleteRoleRight(String appId, String id) {
		String sql = "update ZZL_AppPermission set isDeleted=1 where id="+id;
		//有关mes的信息，需要推送
		if(Constants.MES_APP_ID.equals(appId)){
			int permission_id = getPermission_id(Integer.parseInt(id));
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("id", String.valueOf(permission_id));
			String deletePermissionUrl = Constants.MES_URL+"/deletePermission.do";
			String paramStr = permission_id+Constants.MES_APP_SECRET; 
			String access_token = DigestUtils.md5Hex(paramStr);
			dataMap.put("access_token", access_token);
			String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), deletePermissionUrl);
			JSONObject resultObj = JSONObject.fromObject(responseResult);
			if("0".equals(resultObj.get("result"))){
				baseDao.excuteSql(sql);
				//遍历删除下面所有的子节点
				//deleteNodeForMes(id);
			}
		}else{
			baseDao.excuteSql(sql);
			//遍历删除下面所有的子节点
			deleteNode(id);
		}
	}
	/*	private void deleteNodeForMes(String id) {
		List<AppPermissionEntity> childNodes = getChildNodes(id);
		for(AppPermissionEntity childNode: childNodes){
			String sql = "update ZZL_AppPermission set isDeleted=1 where id="+childNode.getId();
			int permission_id = getPermission_id(childNode.getId());
			String deletePermissionUrl = Constants.MES_URL;
			String paramStr = permission_id+Constants.MES_APP_SECRET; 
			String access_token = DigestUtils.md5Hex(paramStr);
			deletePermissionUrl += "?id="+permission_id+"&access_token="+access_token;
			String responseResult = HttpClientUtil.doGet(deletePermissionUrl);
			JSONObject resultObj = JSONObject.fromObject(responseResult);
			if("0".equals(resultObj.get("result"))){
				baseDao.excuteSql(sql);
			}
			deleteNodeForMes(String.valueOf(childNode.getId()));
		}
	}*/
	private void deleteNode(String id) {
		List<AppPermissionEntity> childNodes = getChildNodes(id);
		for(AppPermissionEntity childNode: childNodes){
			String sql = "update ZZL_AppPermission set isDeleted=1 where id="+childNode.getId();
			baseDao.excuteSql(sql);
			deleteNode(String.valueOf(childNode.getId()));
		}
	}
	@SuppressWarnings("unchecked")
	private List<AppPermissionEntity> getChildNodes(String id) {
		String hql = "from AppPermissionEntity where isDeleted=0 and parentId="+id;
		return (List<AppPermissionEntity>) baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoEntity> findAllApps() {
		String hql = "from AppInfoEntity where isDeleted=0 order by addTime";
		List<AppInfoEntity> appInfos =  (List<AppInfoEntity>) baseDao.hqlfind(hql);
		for(AppInfoEntity appInfo: appInfos){
			StaffEntity staff = staffDao.getStaffByUserID(appInfo.getCreatorId());
			appInfo.setCreatorName(staff.getStaffName());
		}
		return appInfos;
	}
	@Override
	public void saveAppInfo(AppInfoEntity appInfo, File icon, String iconFileName) throws Exception {
		if(null != icon){
			File parent = new File(Constants.APP_ICON);
			parent.mkdirs();
			String saveName = UUID.randomUUID().toString().replaceAll("-", "");
			@Cleanup
			InputStream in = new FileInputStream(icon);
			@Cleanup
			OutputStream out = new FileOutputStream(new File(parent, saveName));
			byte[] buffer = new byte[10 * 1024 * 1024];
			int length = 0;
			while ((length = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
			CommonAttachment commonAttachment = new CommonAttachment();
			commonAttachment.setAddTime(new Date());
			commonAttachment.setIsDeleted(0);
			commonAttachment.setSoftURL(
					Constants.APP_ICON + saveName);
			commonAttachment.setSoftName(iconFileName);
			commonAttachment.setSuffix(iconFileName.substring(iconFileName.lastIndexOf(".")+1, iconFileName.length()));
			Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
			appInfo.setIconId(attachmentId);
		}
		if(null != appInfo.getId()){
			AppInfoEntity oldAppInfo = getAppInfoById(appInfo.getId());
			oldAppInfo.setAppName(appInfo.getAppName());
			oldAppInfo.setGoHomeUrl(appInfo.getGoHomeUrl());
			oldAppInfo.setDescription(appInfo.getDescription());
			if(null != appInfo.getIconId()){
				oldAppInfo.setIconId(appInfo.getIconId());
			}
			baseDao.hqlUpdate(oldAppInfo);
		}else{
			String uuid = UUID.randomUUID().toString();
			appInfo.setAppId(uuid);
			appInfo.setAddTime(new Date());
			appInfo.setAppSecret(DigestUtils.md5Hex(Constants.COM_KEY+appInfo.getAppName()).toUpperCase());
			baseDao.hqlSave(appInfo);
		}
	}
	private AppInfoEntity getAppInfoById(Integer id) {
		String hql = "from AppInfoEntity where id="+id;
		return (AppInfoEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void deleteApp(String appId) {
		String sql = "update ZZL_AppInfo set isDeleted=1 where id="+appId;
		baseDao.excuteSql(sql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<AppRoleEntity> findRolesByAppId(String appId, Integer limit,
			Integer page, String roleName) {
		String hql = "from AppRoleEntity where isDeleted=0 and appId='"+appId+"'\n";
		if(StringUtils.isNotBlank(roleName)){
			hql += "and roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		hql += "order by roleName";
		List<AppRoleEntity> appRoles = (List<AppRoleEntity>) baseDao.hqlPagedFind(hql, page, limit);
		String sql = "select count(id) from ZZL_AppRole where isDeleted=0 and appId='"+appId+"'\n";
		if(StringUtils.isNotBlank(roleName)){
			sql += "and roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		return new ListResult<>(appRoles, count);
	}
	@Override
	public void saveRoleInfo(AppRoleEntity roleInfo) {
		if(null != roleInfo.getId()){
			AppRoleEntity oldRoleInfo = getRoleInfoById(roleInfo.getId());
			roleInfo.setAppId(oldRoleInfo.getAppId());
			oldRoleInfo.setRoleName(roleInfo.getRoleName());
			oldRoleInfo.setRoleDescription(roleInfo.getRoleDescription());
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(roleInfo.getAppId())){
				Map<String, String> dataMap = new HashMap<>();
				int role_id = getRole_id(roleInfo.getId());
				dataMap.put("id", String.valueOf(role_id));
				dataMap.put("roleName", roleInfo.getRoleName());
				String updateRoleUrl = Constants.MES_URL+"/updateRole.do";
				if(StringUtils.isNotBlank(roleInfo.getRoleDescription())){
					dataMap.put("roleDescription", roleInfo.getRoleDescription());
					String paramStr = role_id+roleInfo.getRoleName()+roleInfo.getRoleDescription()+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}else{
					dataMap.put("roleDescription", "");
					String paramStr = role_id+roleInfo.getRoleName()+Constants.MES_APP_SECRET;
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), updateRoleUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					baseDao.hqlUpdate(oldRoleInfo);
				}
			}else{
				baseDao.hqlUpdate(oldRoleInfo);
			}
		}else{
			roleInfo.setAddTime(new Date());
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(roleInfo.getAppId())){
				Map<String, String> dataMap = new HashMap<>();
				String addRoleUrl = Constants.MES_URL+"/addRole.do";
				dataMap.put("roleName", roleInfo.getRoleName());
				if(StringUtils.isNotBlank(roleInfo.getRoleDescription())){
					dataMap.put("roleDescription", roleInfo.getRoleDescription());
					String paramStr = roleInfo.getRoleName()+roleInfo.getRoleDescription()+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}else{
					dataMap.put("roleDescription", "");
					String paramStr = roleInfo.getRoleName()+Constants.MES_APP_SECRET;
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
				}
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addRoleUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					int id = Integer.parseInt(resultObj.getString("id"));
					roleInfo.set_id(id);
					baseDao.hqlSave(roleInfo);
				}
			}else{
				baseDao.hqlSave(roleInfo);
			}
		}
	}
	private int getRole_id(Integer id) {
		String sql = "select _id from ZZL_AppRole where isDeleted=0 and id="+id;
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	private int getPermission_id(Integer id){
		String sql = "select _id from ZZL_AppPermission where isDeleted=0 and id="+id;
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	private AppRoleEntity getRoleInfoById(Integer id) {
		String hql = "from AppRoleEntity where id="+id;
		return (AppRoleEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public boolean checkRoleNameExist(String roleName, String appId, String id) {
		String sql = "select count(*) from ZZL_AppRole where isDeleted=0 and "
				+ "roleName='"+EscapeUtil.decodeSpecialChars(roleName)+"' and appId='"+appId+"'\n";
		if(StringUtils.isNotBlank(id)){
			sql += "and id!="+id;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public void deleteRole(String appId, String roleId) {
		String sql = "update ZZL_AppRole set isDeleted=1 where id="+roleId;
		//有关mes的信息，需要推送
		if(Constants.MES_APP_ID.equals(appId)){
			String deleteUrl = Constants.MES_URL+"/deleteRole.do";
			int role_id = getRole_id(Integer.parseInt(roleId));
			String paramStr = roleId+Constants.MES_APP_SECRET; 
			String access_token = DigestUtils.md5Hex(paramStr);
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("id", String.valueOf(role_id));
			dataMap.put("access_token", access_token);
			String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), deleteUrl);
			JSONObject resultObj = JSONObject.fromObject(responseResult);
			if("0".equals(resultObj.get("result"))){
				baseDao.excuteSql(sql);
			}
		}else{
			baseDao.excuteSql(sql);
		}
	}
	@Override
	public List<Object> getAllFirstLevelNodes(String appId) {
		List<Object> firstNodes = new ArrayList<>();
		String sql = "SELECT\n" +
				"	id,\n" +
				"	CONCAT(\n" +
				"		permissionName,\n" +
				"		CASE\n" +
				"	WHEN permissionCode IS NOT NULL THEN\n" +
				"		CONCAT('【', permissionCode, '】')\n" +
				"	ELSE\n" +
				"		''\n" +
				"	END\n" +
				"	),\n" +
				"	permissionCode, permissionName, type, sort, isUsed, pageUrl, requestUrl\n" +
				"FROM\n" +
				"	ZZL_AppPermission\n" +
				"WHERE\n" +
				"	parentId is null and IsDeleted = 0 and appId='"+appId+"'\n" +
				"ORDER BY\n" +
				"	sort, PermissionName";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] nodeInfo = (Object[])obj;
			Map<String, Object> nodeMap = new HashMap<>(); 
			int id = Integer.parseInt(nodeInfo[0]+"");
			nodeMap.put("id", id);
			nodeMap.put("pId", 0);
			//判断是否有子节点
			if(checkHasChild(id)){
				nodeMap.put("isParent", true);
			}
			if(Constants.RIGHT.equals((String)nodeInfo[4])){
				nodeMap.put("iconSkin", "diy");
				/*				if(!checkHasChild(id)){
					nodeMap.put("iconSkin", "diy");
				}else{
					nodeMap.put("name", (String)nodeInfo[3]);
				}*/
			}
			nodeMap.put("type", (String)nodeInfo[4]);
			nodeMap.put("sort", (String)nodeInfo[5]);
			String isUsed = (String)nodeInfo[6];
			nodeMap.put("isUsed", isUsed);
			String pageUrl = (String)nodeInfo[7];
			nodeMap.put("pageUrl", pageUrl);
			nodeMap.put("requestUrl", (String)nodeInfo[8]);
			String name = (String)nodeInfo[1];
			if(Constants.MES_APP_ID.equals(appId)){
				name += "<span>"+isUsed;
			}
			nodeMap.put("name", name);
			if(null != (String)nodeInfo[2]){
				nodeMap.put("permissionCode", (String)nodeInfo[2]);
			}
			nodeMap.put("permissionName", (String)nodeInfo[3]);
			firstNodes.add(nodeMap);
		}
		return firstNodes;
	}
	@Override
	public boolean checkHasChild(int id) {
		String sql = "select count(*) from ZZL_AppPermission where isDeleted=0 and parentId="+id;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public List<Object> findChildNodesByPId(String parentId) {
		AppPermissionEntity appPermission = getPermissionById(Integer.parseInt(parentId));
		List<Object> nodes = new ArrayList<>();
		String sql = "SELECT\n" +
				"	id,\n" +
				"	IFNULL(parentId, 0),\n" +
				"	CONCAT(\n" +
				"		permissionName,\n" +
				"		CASE\n" +
				"	WHEN permissionCode IS NOT NULL THEN\n" +
				"		CONCAT('【', permissionCode, '】')\n" +
				"	ELSE\n" +
				"		''\n" +
				"	END\n" +
				"	),\n" +
				"	permissionCode, permissionName, type, sort, isUsed, pageUrl, requestUrl\n" +
				"FROM\n" +
				"	ZZL_AppPermission\n" +
				"WHERE\n" +
				"	IsDeleted = 0 and parentId='"+parentId+"'\n" +
				"ORDER BY\n" +
				"	sort, PermissionName";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] nodeInfo = (Object[])obj;
			Map<String, Object> nodeMap = new HashMap<>(); 
			int id = Integer.parseInt(nodeInfo[0]+"");
			nodeMap.put("id", id);
			nodeMap.put("pId", Integer.parseInt(nodeInfo[1]+""));
			//判断是否有子节点
			if(checkHasChild(id)){
				nodeMap.put("isParent", true);
			}
			if(Constants.RIGHT.equals((String)nodeInfo[5])){
				nodeMap.put("iconSkin", "diy");
				/*				if(!checkHasChild(id)){
					nodeMap.put("permissionCode", (String)nodeInfo[3]);
					nodeMap.put("iconSkin", "diy");
				}else{
					nodeMap.put("name", (String)nodeInfo[4]);
				}*/
			}
			nodeMap.put("type", (String)nodeInfo[5]);
			nodeMap.put("sort", (String)nodeInfo[6]);
			String isUsed = (String)nodeInfo[7];
			nodeMap.put("isUsed", isUsed);
			String pageUrl = (String)nodeInfo[8];
			nodeMap.put("pageUrl", pageUrl);
			nodeMap.put("requestUrl", (String)nodeInfo[9]);
			String name = (String)nodeInfo[2];
			if(Constants.MES_APP_ID.equals(appPermission.getAppId())){
				name += "<span>"+isUsed;
			}
			nodeMap.put("name", name);
			if(null != (String)nodeInfo[3]){
				nodeMap.put("permissionCode", (String)nodeInfo[3]);
			}
			nodeMap.put("permissionName", (String)nodeInfo[4]);
			nodes.add(nodeMap);
		}
		return nodes;
	}
	@Override
	public boolean checkRightCodeExist(String permissionCode, String appId, String id) {
		String sql = "select count(*) from ZZL_AppPermission where isDeleted=0 and "
				+ "permissionCode='"+permissionCode+"' and appId='"+appId+"'\n";
		if(StringUtils.isNotBlank(id)){
			sql += "and id!="+id;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public ListResult<Object> findUserRoleShips(String appId, String staffName,
			String roleName, Integer limit, Integer page) {
		String sql = "SELECT\n" +
				"	staff.StaffName,\n" +
				"	staff.dep,\n" +
				"	GROUP_CONCAT(CONCAT('【',roleName,'】') order by roleName),\n" +
				"	staff.UserID\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			StaffName,\n" +
				"			staff.UserID,\n" +
				"			CONCAT(\n" +
				"				CompanyName,\n" +
				"				'-',\n" +
				"				DepartmentName\n" +
				"			) dep\n" +
				"		FROM\n" +
				"			OA_Staff staff,\n" +
				"			ZZL_AppUser appUser,\n" +
				"			ZZL_AppInfo app,\n" +
				"			ACT_ID_MEMBERSHIP\n" +
				"			ship,\n" +
				"			OA_GroupDetail detail,\n" +
				"			OA_Company company,\n" +
				"			OA_Department dep\n" +
				"		WHERE\n" +
				"			staff.IsDeleted = 0\n" +
				"		AND `Status` != 4\n" +
				"		AND appUser.isDeleted = 0\n" +
				"		AND appUser.appId = app.id\n" +
				"		AND app.appId = '"+appId+"'\n" +
				"		AND staff.UserID = appUser.userId\n" +
				"		AND ship.USER_ID_ = staff.UserID\n" +
				"		AND ship.GROUP_ID_ = detail.GroupID\n" +
				"		AND detail.CompanyID = company.CompanyID\n" +
				"		AND detail.DepartmentID = dep.DepartmentID\n" +
				"	) staff\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppRoleShip\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				") rShip ON staff.UserID = rShip.userId\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppRole\n" +
				"	WHERE\n" +
				"		isDeleted = 0 AND appId='"+appId+"'\n" +
				") role ON role.id = rShip.roleId\n";
		if(StringUtils.isNotBlank(staffName)){
			sql += "where StaffName like '%"+staffName+"%'\n";
			if(StringUtils.isNotBlank(roleName)){
				sql += "and role.roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
			}
		}else if(StringUtils.isNotBlank(roleName)){
			sql += "where role.roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		sql += "GROUP BY staff.UserID";
		List<Object> objList = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			staff.UserID\n" +
				"		FROM\n" +
				"			(\n" +
				"				SELECT\n" +
				"					StaffName,\n" +
				"					staff.UserID\n" +
				"				FROM\n" +
				"					OA_Staff staff,\n" +
				"					ZZL_AppUser appUser,\n" +
				"					ZZL_AppInfo app\n" +
				"				WHERE\n" +
				"					staff.IsDeleted = 0\n" +
				"				AND `Status` != 4\n" +
				"				AND appUser.isDeleted = 0\n" +
				"				AND appUser.appId = app.id\n" +
				"		        AND app.appId = '"+appId+"'\n" +
				"				AND staff.UserID = appUser.userId\n" +
				"			) staff\n" +
				"		LEFT JOIN (\n" +
				"			SELECT\n" +
				"				*\n" +
				"			FROM\n" +
				"				ZZL_AppRoleShip\n" +
				"			WHERE\n" +
				"				isDeleted = 0\n" +
				"		) rShip ON staff.UserID = rShip.userId\n" +
				"		LEFT JOIN (\n" +
				"			SELECT\n" +
				"				*\n" +
				"			FROM\n" +
				"				ZZL_AppRole\n" +
				"			WHERE\n" +
				"				isDeleted = 0 AND appId='"+appId+"'\n" +
				"		) role ON role.id = rShip.roleId\n";
		if(StringUtils.isNotBlank(staffName)){
			sqlCount += "where StaffName like '%"+staffName+"%'\n";
			if(StringUtils.isNotBlank(roleName)){
				sqlCount += "and role.roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
			}
		}else if(StringUtils.isNotBlank(roleName)){
			sqlCount += "where role.roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		sqlCount += "GROUP BY staff.UserID)a";
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(objList, count);
	}
	@Override
	public List<Object> findUserRoleShipsByUserId(String userId, String appId) {
		String sql = "SELECT\n" +
				"	roleName,\n" +
				"	role.id,\n" +
				"	rShip.id shipId\n" +
				"FROM\n" +
				"	ZZL_AppRole role\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppRoleShip\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				"	AND userId = '"+userId+"'\n" +
				") rShip ON role.id = rShip.roleId\n" +
				"WHERE\n" +
				"	role.isDeleted=0 and appId = '"+appId+"'\n" +
				"ORDER BY\n" +
				"	roleName";
		return baseDao.findBySql(sql);
	}
	@Override
	public void saveUserRoleShips(String userId, String[] roleIds, String appId) {
		List<String> oldRoleIds = getRoleIdsByUserId(userId, appId);
		List<String> newRoleIds = new ArrayList<>();
		if(null != roleIds){
			newRoleIds.addAll(Arrays.asList(roleIds));
		}
		List<String> addRoleIds = new ArrayList<>();
		List<String> deleteRoleIds = new ArrayList<>();
		for(String oldRoleId: oldRoleIds){
			if(!newRoleIds.contains(oldRoleId)){
				deleteRoleIds.add(oldRoleId);
			}
		}
		for(String newRoleId: newRoleIds){
			if(!oldRoleIds.contains(newRoleId)){
				addRoleIds.add(newRoleId);
			}
		}
		/*		String sql = "UPDATE ZZL_AppRoleShip rShip,\n" +
				" ZZL_AppRole role\n" +
				"SET rShip.isDeleted = 1\n" +
				"WHERE\n" +
				"	rShip.roleId = role.id\n" +
				"AND rShip.userId = '"+userId+"'\n" +
				"AND role.appId = '"+appId+"'";*/
		if(CollectionUtils.isNotEmpty(deleteRoleIds)){
			String sql = "UPDATE ZZL_AppRoleShip set isDeleted=1 where roleId in ("+StringUtils.join(deleteRoleIds, ",")+") "
					+ "and userId='"+userId+"'";
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(appId)){
				String deleteUserRoleShipUrl = Constants.MES_URL+"/deleteUserRoleShip.do";
				List<Integer> role_ids = new ArrayList<>();
				for(String roleId: deleteRoleIds){
					role_ids.add(getRole_id(Integer.parseInt(roleId)));
				}
				Map<String, String> dataMap = new HashMap<>();
				String role_idStr = StringUtils.join(role_ids, ",");
				dataMap.put("roleIds", role_idStr);
				dataMap.put("userId", userId);
				String paramStr = role_idStr+userId+Constants.MES_APP_SECRET; 
				String access_token = DigestUtils.md5Hex(paramStr);
				dataMap.put("access_token", access_token);
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), deleteUserRoleShipUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					baseDao.excuteSql(sql);
				}
			}else{
				baseDao.excuteSql(sql);
			}
		}
		if(CollectionUtils.isNotEmpty(addRoleIds)){
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(appId)){
				String addUserRoleShipUrl = Constants.MES_URL+"/addUserRoleShip.do";
				List<Integer> role_ids = new ArrayList<>();
				for(String roleId: addRoleIds){
					role_ids.add(getRole_id(Integer.parseInt(roleId)));
				}
				Map<String, String> dataMap = new HashMap<>();
				String role_idStr = StringUtils.join(role_ids, ",");
				dataMap.put("roleIds", role_idStr);
				dataMap.put("userId", userId);
				String paramStr = role_idStr+userId+Constants.MES_APP_SECRET; 
				String access_token = DigestUtils.md5Hex(paramStr);
				dataMap.put("access_token", access_token);
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addUserRoleShipUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					for(String roleId: addRoleIds){
						AppRoleShipEntity roleShip = new AppRoleShipEntity();
						roleShip.setIsDeleted(0);
						roleShip.setAddTime(new Date());
						roleShip.setRoleId(Integer.parseInt(roleId));
						roleShip.setUserId(userId);
						baseDao.hqlSave(roleShip);
					}
				}
			}else{
				for(String roleId: addRoleIds){
					AppRoleShipEntity roleShip = new AppRoleShipEntity();
					roleShip.setIsDeleted(0);
					roleShip.setAddTime(new Date());
					roleShip.setRoleId(Integer.parseInt(roleId));
					roleShip.setUserId(userId);
					baseDao.hqlSave(roleShip);
				}
			}
		}
	}
	private List<String> getRoleIdsByUserId(String userId, String appId) {
		List<String> roleIds = new ArrayList<>();
		String sql = "SELECT\n" +
				"	role.id\n" +
				"FROM\n" +
				"	ZZL_AppRole role,\n" +
				"	ZZL_AppRoleShip rShip\n" +
				"WHERE\n" +
				"	role.isDeleted = 0\n" +
				"AND rShip.isDeleted = 0\n" +
				"AND role.id = rShip.roleId\n" +
				"AND rShip.userId = '"+userId+"'\n" +
				"AND role.appId = '"+appId+"'";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			roleIds.add((Integer)obj+"");
		}
		return roleIds;
	}
	private boolean checkHasUserRoleShip(String userId, String roleId) {
		String sql = "select count(*) from ZZL_AppRoleShip where userId='"+userId+"' and roleId="+roleId;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public ListResult<Object> findRoleRightShips(String appId, String rightName, String roleName,
			Integer limit, Integer page) {
		String sql = "SELECT\n" +
				"	role.roleName,\n" +
				"	GROUP_CONCAT(\n" +
				"		CONCAT('【', permissionName, '】')\n" +
				"		ORDER BY\n" +
				"			permissionName\n" +
				"	), role.id\n" +
				"FROM\n" +
				"	ZZL_AppRole role\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppPermissionShip\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				") pShip ON role.id = pShip.roleId\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppPermission\n" +
				"	WHERE\n" +
				"		isDeleted = 0 and type='权限'\n" +
				") p ON pShip.permissionId = p.id\n" +
				"WHERE\n" +
				"	role.isDeleted = 0 and role.appId='"+appId+"'\n";
		if(StringUtils.isNotBlank(roleName)){
			sql += "and roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		if(StringUtils.isNotBlank(rightName)){
			sql += "and permissionName like '%"+EscapeUtil.decodeSpecialChars(rightName)+"%'\n";
		}
		sql += "GROUP BY role.id";
		List<Object> roleRightShips = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(*)\n" +
				"FROM\n" +
				"	(\n" +
				"		SELECT\n" +
				"			role.id\n" +
				"		FROM\n" +
				"			ZZL_AppRole role\n" +
				"		LEFT JOIN (\n" +
				"			SELECT\n" +
				"				*\n" +
				"			FROM\n" +
				"				ZZL_AppPermissionShip\n" +
				"			WHERE\n" +
				"				isDeleted = 0\n" +
				"		) pShip ON role.id = pShip.roleId\n" +
				"		LEFT JOIN (\n" +
				"			SELECT\n" +
				"				*\n" +
				"			FROM\n" +
				"				ZZL_AppPermission\n" +
				"			WHERE\n" +
				"				isDeleted = 0 and type='权限'\n" +
				"		) p ON pShip.permissionId = p.id\n" +
				"		WHERE\n" +
				"			role.isDeleted = 0\n" +
				"		AND role.appId = '"+appId+"'\n";
		if(StringUtils.isNotBlank(roleName)){
			sqlCount += "and roleName like '%"+EscapeUtil.decodeSpecialChars(roleName)+"%'\n";
		}
		if(StringUtils.isNotBlank(rightName)){
			sqlCount += "and permissionName like '%"+EscapeUtil.decodeSpecialChars(rightName)+"%'\n";
		}
		sqlCount += "GROUP BY role.id) a";
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(roleRightShips, count);
	}
	@Override
	public List<Object> getFirstNodeShips(String appId, String roleId) {
		List<Object> firstNodeShips = new ArrayList<>();
		String sql = "SELECT\n" +
				"	p.id,\n" +
				"	CONCAT(\n" +
				"		permissionName,\n" +
				"		CASE\n" +
				"	WHEN permissionCode IS NOT NULL THEN\n" +
				"		CONCAT('【', permissionCode, '】')\n" +
				"	ELSE\n" +
				"		''\n" +
				"	END\n" +
				"	),\n" +
				"	permissionCode,pShip.id shipId, permissionName,type\n" +
				"FROM\n" +
				"	ZZL_AppPermission p\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppPermissionShip\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				"	AND roleId = "+roleId+"\n" +
				") pShip ON p.id = pShip.permissionId\n" +
				"WHERE\n" +
				"	p.isDeleted = 0\n" +
				"AND appId = '"+appId+"'\n" +
				"AND parentId IS NULL\n" +
				"GROUP BY id\n" +
				"ORDER BY\n" +
				"	PermissionName";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] nodeInfo = (Object[])obj;
			Map<String, Object> nodeMap = new HashMap<>(); 
			int id = Integer.parseInt(nodeInfo[0]+"");
			nodeMap.put("id", id);
			nodeMap.put("pId", 0);
			nodeMap.put("name", (String)nodeInfo[1]);
			//判断是否有子节点
			if(checkHasChild(id)){
				nodeMap.put("isParent", true);
			}
			/*			if(null != (String)nodeInfo[2]){
				if(!checkHasChild(id)){
					nodeMap.put("iconSkin", "diy");
				}else{
					nodeMap.put("name", (String)nodeInfo[4]);
				}
			}*/
			if(Constants.RIGHT.equals((String)nodeInfo[5])){
				nodeMap.put("iconSkin", "diy");
			}
			//判断是否勾选
			if(null != (Integer)nodeInfo[3]){
				nodeMap.put("checked", true);
			}
			firstNodeShips.add(nodeMap);
		}
		return firstNodeShips;
	}
	@Override
	public List<Object> findChildNodeShipsByPId(String parentId, String roleId) {
		List<Object> nodes = new ArrayList<>();
		String sql = "SELECT\n" +
				"	p.id,\n" +
				"	IFNULL(parentId, 0),\n" +
				"	CONCAT(\n" +
				"		permissionName,\n" +
				"		CASE\n" +
				"	WHEN permissionCode IS NOT NULL THEN\n" +
				"		CONCAT('【', permissionCode, '】')\n" +
				"	ELSE\n" +
				"		''\n" +
				"	END\n" +
				"	),\n" +
				"	permissionCode,pShip.id shipId,permissionName,type\n" +
				"FROM\n" +
				"	ZZL_AppPermission p\n" +
				"LEFT JOIN (\n" +
				"	SELECT\n" +
				"		*\n" +
				"	FROM\n" +
				"		ZZL_AppPermissionShip\n" +
				"	WHERE\n" +
				"		isDeleted = 0\n" +
				"	AND roleId = "+roleId+"\n" +
				") pShip ON p.id = pShip.permissionId\n" +
				"WHERE\n" +
				"	p.isDeleted = 0\n" +
				"and parentId='"+parentId+"'\n" +
				"GROUP BY id\n" +
				"ORDER BY\n" +
				"	PermissionName";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] nodeInfo = (Object[])obj;
			Map<String, Object> nodeMap = new HashMap<>(); 
			int id = Integer.parseInt(nodeInfo[0]+"");
			nodeMap.put("id", id);
			nodeMap.put("pId", Integer.parseInt(nodeInfo[1]+""));
			nodeMap.put("name", (String)nodeInfo[2]);
			//判断是否有子节点
			if(checkHasChild(id)){
				nodeMap.put("isParent", true);
			}
			/*			if(null != (String)nodeInfo[3]){
				if(!checkHasChild(id)){
					nodeMap.put("permissionCode", (String)nodeInfo[3]);
					nodeMap.put("iconSkin", "diy");
				}else{
					nodeMap.put("name", (String)nodeInfo[5]);
				}
			}*/
			if(Constants.RIGHT.equals((String)nodeInfo[6])){
				nodeMap.put("iconSkin", "diy");
			}
			//判断是否勾选
			if(null != (Integer)nodeInfo[4]){
				nodeMap.put("checked", true);
			}
			nodes.add(nodeMap);
		}
		return nodes;
	}
	@Override
	public boolean saveRoleRightShips(String appId, String parent, String checked,
			String permissionIdStr, String roleId) {
		//父节点（全选）
		if("true".equalsIgnoreCase(parent)){
			/*			if(checkHasRoleRightShip(permissionIdStr, roleId)){
				String sql = "update ZZL_AppPermissionShip set isDeleted=0 where permissionId="
						+permissionIdStr+" and roleId="+roleId;

				baseDao.excuteSql(sql);
			}else{
				AppPermissionShipEntity pShip = new AppPermissionShipEntity();
				pShip.setAddTime(new Date());
				pShip.setPermissionId(Integer.parseInt(permissionIdStr));
				pShip.setRoleId(Integer.parseInt(roleId));
				baseDao.hqlSave(pShip);
			}*/
			AppPermissionShipEntity pShip = new AppPermissionShipEntity();
			pShip.setAddTime(new Date());
			pShip.setPermissionId(Integer.parseInt(permissionIdStr));
			pShip.setRoleId(Integer.parseInt(roleId));
			//有关mes的信息，需要推送
			if(Constants.MES_APP_ID.equals(appId)){
				String addRoleRightShipUrl = Constants.MES_URL+"/addRoleRightShip.do";
				Map<String, String> dataMap = new HashMap<>();
				int permission_id = getPermission_id(Integer.parseInt(permissionIdStr));
				dataMap.put("permissionIds", String.valueOf(permission_id));
				int role_id = getRole_id(Integer.parseInt(roleId));
				dataMap.put("roleId", String.valueOf(role_id));
				String paramStr = String.valueOf(permission_id)+role_id+Constants.MES_APP_SECRET; 
				String access_token = DigestUtils.md5Hex(paramStr);
				dataMap.put("access_token", access_token);
				String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addRoleRightShipUrl);
				JSONObject resultObj = JSONObject.fromObject(responseResult);
				if("0".equals(resultObj.get("result"))){
					baseDao.hqlSave(pShip);
				}else{
					return false;
				}
			}else{
				baseDao.hqlSave(pShip);
			}
			//获取下面所有的子节点
			List<AppPermissionEntity> childNodes = new ArrayList<>();
			getAllChildNodes(permissionIdStr, childNodes);
			//选中
			if("true".equalsIgnoreCase(checked)){
				for(AppPermissionEntity childNode: childNodes){
					/*if(checkHasRoleRightShip(String.valueOf(childNode.getId()), roleId)){
						String sql = "update ZZL_AppPermissionShip set isDeleted=0 where permissionId="
								+childNode.getId()+" and roleId="+roleId;
						baseDao.excuteSql(sql);
					}else{
						AppPermissionShipEntity pShip = new AppPermissionShipEntity();
						pShip.setAddTime(new Date());
						pShip.setPermissionId(childNode.getId());
						pShip.setRoleId(Integer.parseInt(roleId));
						baseDao.hqlSave(pShip);
					}*/
					pShip = new AppPermissionShipEntity();
					pShip.setAddTime(new Date());
					pShip.setPermissionId(childNode.getId());
					pShip.setRoleId(Integer.parseInt(roleId));
					//有关mes的信息，需要推送
					if(Constants.MES_APP_ID.equals(appId)){
						String addRoleRightShipUrl = Constants.MES_URL+"/addRoleRightShip.do";
						Map<String, String> dataMap = new HashMap<>();
						int permission_id = getPermission_id(childNode.getId());
						int role_id = getRole_id(Integer.parseInt(roleId));
						dataMap.put("permissionIds", String.valueOf(permission_id));
						dataMap.put("roleId", String.valueOf(role_id));
						String paramStr = String.valueOf(permission_id)+role_id+Constants.MES_APP_SECRET; 
						String access_token = DigestUtils.md5Hex(paramStr);
						dataMap.put("access_token", access_token);
						String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addRoleRightShipUrl);
						JSONObject resultObj = JSONObject.fromObject(responseResult);
						if("0".equals(resultObj.get("result"))){
							baseDao.hqlSave(pShip);
						}else{
							return false;
						}
					}else{
						baseDao.hqlSave(pShip);
					}
				}
				//取消
			}else{
				List<Integer> ids = new ArrayList<>();
				ids.add(Integer.parseInt(permissionIdStr));
				for(AppPermissionEntity childNode: childNodes){
					ids.add(childNode.getId());
				}
				String sql = "UPDATE ZZL_AppPermissionShip\n" +
						"SET isDeleted = 1\n" +
						"WHERE\n" +
						"roleId = "+roleId+" and permissionId in ("+StringUtils.join(ids, ",")+")";
				//有关mes的信息，需要推送
				if(Constants.MES_APP_ID.equals(appId)){
					List<Integer> permission_ids = new ArrayList<>();
					for(int id: ids){
						permission_ids.add(getPermission_id(id));
					}
					String permission_idsStr = StringUtils.join(permission_ids, ",");
					String deleteRoleRightShipUrl = Constants.MES_URL+"/deleteRoleRightShip.do";
					Map<String, String> dataMap = new HashMap<>();
					dataMap.put("permissionIds", permission_idsStr);
					int role_id = getRole_id(Integer.parseInt(roleId));
					dataMap.put("roleId", String.valueOf(role_id));
					String paramStr = permission_idsStr+role_id+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
					String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), deleteRoleRightShipUrl);
					JSONObject resultObj = JSONObject.fromObject(responseResult);
					if("0".equals(resultObj.get("result"))){
						baseDao.excuteSql(sql);
					}else{
						return false;
					}
				}else{
					baseDao.excuteSql(sql);
				}
			}
			//子节点	
		}else{
			//选中
			if("true".equalsIgnoreCase(checked)){
				String[] permissionIds = permissionIdStr.split(",");
				for(String permissionId: permissionIds){
					/*if(checkHasRoleRightShip(permissionId, roleId)){
						String sql = "update ZZL_AppPermissionShip set isDeleted=0 where permissionId="
								+permissionId+" and roleId="+roleId;
						baseDao.excuteSql(sql);
					}else{
						AppPermissionShipEntity pShip = new AppPermissionShipEntity();
						pShip.setAddTime(new Date());
						pShip.setPermissionId(Integer.parseInt(permissionId));
						pShip.setRoleId(Integer.parseInt(roleId));
						baseDao.hqlSave(pShip);
					}*/
					if(checkHasRoleRightShip(permissionId, roleId)){
						continue;
					}
					AppPermissionShipEntity pShip = new AppPermissionShipEntity();
					pShip.setAddTime(new Date());
					pShip.setPermissionId(Integer.parseInt(permissionId));
					pShip.setRoleId(Integer.parseInt(roleId));
					//有关mes的信息，需要推送
					if(Constants.MES_APP_ID.equals(appId)){
						String addRoleRightShipUrl = Constants.MES_URL+"/addRoleRightShip.do";
						Map<String, String> dataMap = new HashMap<>();
						int permission_id = getPermission_id(Integer.parseInt(permissionId));
						int role_id = getRole_id(Integer.parseInt(roleId));
						dataMap.put("permissionIds", String.valueOf(permission_id));
						dataMap.put("roleId", String.valueOf(role_id));
						String paramStr = String.valueOf(permission_id)+role_id+Constants.MES_APP_SECRET; 
						String access_token = DigestUtils.md5Hex(paramStr);
						dataMap.put("access_token", access_token);
						String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), addRoleRightShipUrl);
						JSONObject resultObj = JSONObject.fromObject(responseResult);
						if("0".equals(resultObj.get("result"))){
							baseDao.hqlSave(pShip);
						}else{
							return false;
						}
					}else{
						baseDao.hqlSave(pShip);
					}
					baseDao.hqlSave(pShip);
				}
				//取消
			}else{
				String sql = "UPDATE ZZL_AppPermissionShip\n" +
						"SET isDeleted = 1\n" +
						"WHERE\n" +
						"roleId = "+roleId+" and permissionId in ("+permissionIdStr+")";
				String[] ids = permissionIdStr.split(",");
				//有关mes的信息，需要推送
				if(Constants.MES_APP_ID.equals(appId)){
					List<Integer> permission_ids = new ArrayList<>();
					for(String id: ids){
						permission_ids.add(getPermission_id(Integer.parseInt(id)));
					}
					Map<String, String> dataMap = new HashMap<>();
					String permission_idsStr = StringUtils.join(permission_ids, ",");
					dataMap.put("permissionIds", permission_idsStr);
					String deleteRoleRightShipUrl = Constants.MES_URL+"/deleteRoleRightShip.do";
					int role_id = getRole_id(Integer.parseInt(roleId));
					dataMap.put("roleId", String.valueOf(role_id));
					String paramStr = permission_idsStr+role_id+Constants.MES_APP_SECRET; 
					String access_token = DigestUtils.md5Hex(paramStr);
					dataMap.put("access_token", access_token);
					String responseResult = HttpClientUtil.doPost(JSONObject.fromObject(dataMap).toString(), deleteRoleRightShipUrl);
					JSONObject resultObj = JSONObject.fromObject(responseResult);
					if("0".equals(resultObj.get("result"))){
						baseDao.excuteSql(sql);
					}else{
						return false;
					}
				}else{
					baseDao.excuteSql(sql);
				}
			}
		}
		return true;
	}
	private void getAllChildNodes(String permissionIdStr,
			List<AppPermissionEntity> childNodes) {
		List<AppPermissionEntity> nodes = getChildNodes(permissionIdStr);
		for(AppPermissionEntity node: nodes){
			getAllChildNodes(String.valueOf(node.getId()), childNodes);
		}
		childNodes.addAll(nodes);
	}
	private boolean checkHasRoleRightShip(String permissionId, String roleId) {
		String sql = "select count(*) from ZZL_AppPermissionShip where isDeleted=0 and permissionId="
				+permissionId+" and roleId="+roleId;
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public String getAppSecret(String appId) {
		String sql = "select appSecret from ZZL_AppInfo where isDeleted=0 and appId='"+appId+"'";
		return (String)baseDao.getUniqueResult(sql);
	}
	@Override
	public List<Object> findAllPermissionCodes(String userID, String appId) {
		List<Object> permissionInfos = new ArrayList<>();
		String sql = "SELECT\n" +
				"	DISTINCT p.permissionName, p.permissionCode\n" +
				"FROM\n" +
				"	ZZL_AppRole role,\n" +
				"	ZZL_AppRoleShip rShip,\n" +
				"	ZZL_AppPermission p,\n" +
				"	ZZL_AppPermissionShip pShip\n" +
				"WHERE\n" +
				"	role.isDeleted = 0\n" +
				"AND rShip.isDeleted = 0\n" +
				"AND p.isDeleted = 0\n" +
				"AND pShip.isDeleted = 0\n" +
				"AND pShip.roleId = rShip.roleId\n" +
				"AND rShip.roleId = role.id\n" +
				"AND userId = '"+userID+"'\n" +
				"AND pShip.permissionId = p.id\n" +
				"AND p.permissionCode is not null\n" +
				"AND role.appId='"+appId+"'";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] permissionInfo = (Object[])obj;
			Map<String, String> infoMap = new HashMap<>();
			infoMap.put("permissionName", (String)permissionInfo[0]);
			infoMap.put("permissionCode", (String)permissionInfo[1]);
			permissionInfos.add(infoMap);
		}
		return permissionInfos;
	}
	@Override
	public void addRolePermission(String id, String appId, String parentId, String name, String code, String type) {
		AppPermissionEntity permission = new AppPermissionEntity();
		permission.set_id(Integer.parseInt(id));
		permission.setAppId(appId);
		permission.setPermissionName(name);
		permission.setType(type);
		permission.setAddTime(new Date());
		if(StringUtils.isNotBlank(parentId)){
			//父节点真实id
			int realParentId = getRealId(parentId);
			permission.setParentId(realParentId);
		}
		if(StringUtils.isNotBlank(code)){
			permission.setPermissionCode(code);
		}
		baseDao.hqlSave(permission);
	}
	private int getRealId(String parentId) {
		String sql = "select id from ZZL_AppPermission where isDeleted=0 and _id="+parentId;
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	@Override
	public void addRoleRightShip(String roleId, String permissionIdStr) {
		String[] permissionIds = permissionIdStr.split(",");
		for(String permissionId: permissionIds){
			int realPermissionId = getRealId(permissionId);
			int realRoleId = getRealRoleId(roleId);
			if(checkHasRoleRightShip(String.valueOf(realPermissionId), String.valueOf(realRoleId))){
				String sql = "update ZZL_AppPermissionShip set isDeleted=0 where permissionId="
						+realPermissionId+" and roleId="+realRoleId;
				baseDao.excuteSql(sql);
			}else{
				AppPermissionShipEntity pShip = new AppPermissionShipEntity();
				pShip.setAddTime(new Date());
				pShip.setPermissionId(realPermissionId);
				pShip.setRoleId(realRoleId);
				baseDao.hqlSave(pShip);
			}
		}
	}
	private int getRealRoleId(String roleId) {
		String sql = "select id from ZZL_AppRole where isDeleted=0 and _id="+roleId;
		return Integer.parseInt(baseDao.getUniqueResult(sql)+"");
	}
	@Override
	public void deleteRoleRightShip(String roleId, String permissionIdStr) {
		int realRoleId = getRealRoleId(roleId);
		String[] permissionIds = permissionIdStr.split(",");
		List<Integer> realPermissionIds = new ArrayList<>();
		for(String permissionId: permissionIds){
			realPermissionIds.add(getRealId(permissionId));
		}
		String sql = "UPDATE ZZL_AppPermissionShip\n" +
				"SET isDeleted = 1\n" +
				"WHERE\n" +
				"roleId = "+realRoleId+" and permissionId in ("+StringUtils.join(realPermissionIds, ",")+")";
		baseDao.excuteSql(sql);

	}
	@Override
	public void addUserRoleShip(String roleIdStr, String userId) {
		String[] roleIds = roleIdStr.split(",");
		for(String roleId: roleIds){
			int realRoleId = getRealRoleId(roleId);
			//检查是否关系记录
			if(checkHasUserRoleShip(userId, String.valueOf(realRoleId))){
				String sql = "update ZZL_AppRoleShip set isDeleted=0 where userId='"+userId+"' and roleId="+realRoleId;
				baseDao.excuteSql(sql);
			}else{
				AppRoleShipEntity roleShip = new AppRoleShipEntity();
				roleShip.setIsDeleted(0);
				roleShip.setAddTime(new Date());
				roleShip.setRoleId(realRoleId);
				roleShip.setUserId(userId);
				baseDao.hqlSave(roleShip);
			}
		}
	}
	@Override
	public void deleteUserRoleShip(String roleIdStr, String userId) {
		List<Integer> realRoleIds = new ArrayList<>();
		String[] roleIds = roleIdStr.split(",");
		for(String roleId: roleIds){
			realRoleIds.add(getRealRoleId(roleId));
		}
		String sql = "UPDATE ZZL_AppRoleShip\n" +
				"SET isDeleted = 1\n" +
				"WHERE\n" +
				"userId = '"+userId+"' and roleId in ("+StringUtils.join(realRoleIds, ",")+")";
		baseDao.excuteSql(sql);
	}
	@Override
	public void deleteRoleRightBy_id(String id) {
		int realId = getRealId(id);
		String sql = "update ZZL_AppPermission set isDeleted=1 where _id="+id;
		baseDao.excuteSql(sql);
		//遍历删除下面所有的子节点
		deleteNode(String.valueOf(realId));
	}
	/**
	 * 固定两层菜单
	 */
	@Override
	public Object findAllPermissionShips(String userID, String appId) {
		List<Object> permissionShips = new ArrayList<>();
		String sql = "SELECT\n" +
				"	DISTINCT p.id, p.permissionName,p.permissionCode\n" +
				"FROM\n" +
				"	ZZL_AppPermission p,\n" +
				"	ZZL_AppPermissionShip pShip,\n" +
				"	ZZL_AppRole r,\n" +
				"	ZZL_AppRoleShip rShip\n" +
				"WHERE\n" +
				"	p.id = pShip.permissionId\n" +
				"AND pShip.roleId = r.id\n" +
				"AND r.id = rShip.roleId\n" +
				"AND p.isDeleted = 0\n" +
				"AND pShip.isDeleted = 0\n" +
				"AND r.isDeleted = 0\n" +
				"AND rShip.isDeleted = 0\n" +
				"AND r.appId = '"+appId+"'\n" +
				"AND rShip.userId = '"+userID+"'\n" +
				"AND p.parentId is null";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Map<Object, Object> permissionShip = new HashMap<>();
			Object[] firstIdAndName = (Object[])obj;
			int permissionId = (Integer)firstIdAndName[0];
			List<Object> childObjs = getChildIds(userID, permissionId);
			List<Object> dataList = new ArrayList<>();
			for(Object childObj: childObjs){
				Object[] idAndName = (Object[])childObj;
				List<String> permissionCodes = getPermissionCodes(userID, (int)idAndName[0]);
				Map<Object, Object> codeMap = new HashMap<>();
				codeMap.put("permissionCode", permissionCodes);
				codeMap.put("id", (int)idAndName[0]);
				codeMap.put("name", (String)idAndName[1]);
				codeMap.put("code", (String)idAndName[2]);
				dataList.add(codeMap);
			}
			permissionShip.put("id", permissionId);
			permissionShip.put("name", (String)firstIdAndName[1]);
			permissionShip.put("code", (String)firstIdAndName[2]);
			permissionShip.put("children", dataList);
			permissionShips.add(permissionShip);
		}
		return permissionShips;
	}
	private List<String> getPermissionCodes(String userID, int permissionId) {
		List<String> permissionCodes = new ArrayList<>();
		String sql = "SELECT DISTINCT\n" +
				"	p.permissionCode\n" +
				"FROM\n" +
				"	ZZL_AppPermission p,\n" +
				"	ZZL_AppRoleShip rShip,\n" +
				"	ZZL_AppRole r,\n" +
				"	ZZL_AppPermissionShip pShip\n" +
				"WHERE\n" +
				"	p.id = pShip.permissionId\n" +
				"AND pShip.roleId = r.id\n" +
				"AND r.id = rShip.roleId\n" +
				"AND p.isDeleted = 0\n" +
				"AND rShip.isDeleted = 0\n" +
				"AND r.isDeleted = 0\n" +
				"AND pShip.isDeleted = 0\n" +
				"AND rShip.userId = '"+userID+"'\n" +
				"AND p.parentId = "+permissionId+"\n" +
				"AND permissionCode IS NOT NULL";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			permissionCodes.add((String)obj);
		}
		return permissionCodes;
	}
	private List<Object> getChildIds(String userId, int permissionId) {
		String sql = "SELECT DISTINCT\n" +
				"	p.id, p.permissionName, p.permissionCode\n" +
				"FROM\n" +
				"	ZZL_AppPermission p,\n" +
				"	ZZL_AppRoleShip rShip,\n" +
				"	ZZL_AppRole r,\n" +
				"	ZZL_AppPermissionShip pShip\n" +
				"WHERE\n" +
				"	p.id = pShip.permissionId\n" +
				"AND pShip.roleId = r.id\n" +
				"AND r.id = rShip.roleId\n" +
				"AND p.isDeleted = 0\n" +
				"AND rShip.isDeleted = 0\n" +
				"AND r.isDeleted = 0\n" +
				"AND pShip.isDeleted = 0\n" +
				"AND rShip.userId = '"+userId+"'\n" +
				"AND p.parentId = "+permissionId;
		return baseDao.findBySql(sql);
	}
	@Override
	public String generatePermissionCode(String id, String name) {
		//共两层菜单
		//第二层菜单
		AppPermissionEntity secondPermission = getPermissionById(Integer.parseInt(id));
		AppPermissionEntity firstPermission = null;
		//第一层菜单
		if(null != secondPermission.getParentId()){
			firstPermission = getPermissionById(secondPermission.getParentId());
		}
		//com系统权限
		ComPermissionEntity permission = getPermissionIdByName(name);
		String permissionCode = "";
		if(null != permission){
			if(null == firstPermission){
				permissionCode = secondPermission.getId()+"_"+permission.getId();
			}else{
				permissionCode = firstPermission.getId()+"_"+secondPermission.getId()+"_"+permission.getId();
			}
		}
		return permissionCode;
	}
	private ComPermissionEntity getPermissionIdByName(String name) {
		String hql = "from ComPermissionEntity where isDeleted=0 and permissionName='"+name+"'";
		return (ComPermissionEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void synData() {
		String hql = "from AppPermissionEntity where isDeleted=0";
		@SuppressWarnings("unchecked")
		List<AppPermissionEntity> permissions = (List<AppPermissionEntity>) baseDao.hqlfind(hql);
		for(AppPermissionEntity permission: permissions){
			if(StringUtils.isNotBlank(permission.getPermissionCode()) && !checkHasChild(permission.getId())){
				permission.setType("权限");
			}else{
				permission.setType("目录");
			}
			baseDao.hqlUpdate(permission);
		}
	}
	@Override
	public void synComUserRoleShips() {
		String sql = "select userId, sortIds from zzl_user";
		List<Object> objs = baseDao.findBySql(sql);
		for(Object obj: objs){
			Object[] obj_ = (Object[])obj;
			String userId = (String)obj_[0];
			String sortIdStr = (String)obj_[1];
			if(StringUtils.isNotBlank(sortIdStr)){
				String[] sortIds = sortIdStr.split(",");
				for(String sortId: sortIds){
					if(StringUtils.isNotBlank(sortId.trim())){
						AppRoleShipEntity roleShip = new AppRoleShipEntity();
						roleShip.setAddTime(new Date());
						roleShip.setIsDeleted(0);
						roleShip.setUserId(userId);
						sql = "select id from ZZL_AppRole where comId="+sortId;
						int roleId = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
						roleShip.setRoleId(roleId);
						baseDao.hqlSave(roleShip);
					}
				}
			}
		}
	}
	@Override
	public List<Object> findAppsByUserId(String id) {
		String sql = "select app.iconId, app.appName, app.appId from ZZL_AppInfo app, ZZL_AppUser"
				+ " appUser where app.isDeleted=0 and app.id=appUser.appId"
				+ " and appUser.isDeleted=0 and userId='"+id+"'";
		return baseDao.findBySql(sql);
	}
	@Override
	public AppInfoEntity getAppInfo(String appId) {
		String hql = "from AppInfoEntity where isDeleted=0 and appId='"+appId+"'";
		return (AppInfoEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public boolean checkIsRight(String id) {
		String hql = "from AppPermissionEntity where id="+id;
		AppPermissionEntity permission = (AppPermissionEntity) baseDao.hqlfindUniqueResult(hql);
		if("权限".equals(permission.getType())){
			return true;
		}
		return false;
	}
	@Override
	public boolean checkIsAllocated(String id) {
		String sql = "SELECT\n" +
				"	count(b.id)\n" +
				"FROM\n" +
				"	zzl_apppermission a,\n" +
				"	zzl_apppermissionship b,\n" +
				"	zzl_approle c\n" +
				"WHERE\n" +
				"	a.id = b.permissionId\n" +
				"AND b.roleId = c.id\n" +
				"AND b.isDeleted = 0\n" +
				"AND c.isDeleted = 0\n" +
				"AND a.id = "+id;
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sql)));
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public boolean checkRoleIsAllocated(String roleId) {
		String sql = "SELECT\n" +
				"	count(role.id)\n" +
				"FROM\n" +
				"	zzl_approle role,\n" +
				"	zzl_apppermissionship pShip\n" +
				"WHERE\n" +
				"	role.id = pShip.roleId\n" +
				"AND pShip.isDeleted = 0\n" +
				"AND role.id = "+roleId;
		int count = Integer.parseInt(String.valueOf(baseDao.getUniqueResult(sql)));
		if(count>0){
			return true;
		}
		return false;
	}
}
