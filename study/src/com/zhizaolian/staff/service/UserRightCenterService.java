package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;

import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.entity.AppPermissionEntity;
import com.zhizaolian.staff.entity.AppRoleEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface UserRightCenterService {

	ListResult<Object> findUserAppShipList(String staffName, String appId, Integer limit, Integer page);

	int saveUserAppShip(String appId, String authUserIds, String userId);

	int deleteUserAppShip(String userAppId);

	boolean checkHasAppRight(String appId, String userID);

	void saveUserRole(String id, String roleName, String roleDescription, String appId);

	void updateUserRole(String roleName, String roleDescription, String id);

	void deleteUserRole(String id);

	int addRoleRight(AppPermissionEntity permission);

	void updateRoleRight(String id, String name, String code);

	void deleteRoleRight(String appId, String id);

	List<AppInfoEntity> findAllApps();

	void saveAppInfo(AppInfoEntity appInfo, File icon, String iconFileName) throws Exception;

	void deleteApp(String appId);

	ListResult<AppRoleEntity> findRolesByAppId(String appId, Integer limit, Integer page, String roleName);

	void saveRoleInfo(AppRoleEntity roleInfo);

	boolean checkRoleNameExist(String roleName, String appId, String id);

	void deleteRole(String appId, String roleId);

	List<Object> getAllFirstLevelNodes(String appId);

	List<Object> findChildNodesByPId(String id);

	boolean checkRightCodeExist(String permissionCode, String appId, String id);

	boolean checkHasChild(int id);

	ListResult<Object> findUserRoleShips(String appId, String staffName, String roleName, Integer limit,
			Integer page);

	List<Object> findUserRoleShipsByUserId(String userId, String appId);

	void saveUserRoleShips(String userId, String[] roleId, String appId);

	ListResult<Object> findRoleRightShips(String appId, String rightName, String roleName,
			Integer limit, Integer page);

	List<Object> getFirstNodeShips(String appId, String roleId);

	List<Object> findChildNodeShipsByPId(String id, String roleId);

	boolean saveRoleRightShips(String appId, String parent, String checked, String permissionIds, String roleId);

	String getAppSecret(String appId);

	List<Object> findAllPermissionCodes(String userID, String appId);

	void addRolePermission(String id, String appId, String parentId, String name, String code, String type);

	void addRoleRightShip(String roleId, String permissionIds);

	void deleteRoleRightShip(String roleId, String permissionIds);

	void addUserRoleShip(String roleIds, String userId);

	void deleteUserRoleShip(String roleIds, String userId);

	void deleteRoleRightBy_id(String id);

	Object findAllPermissionShips(String userID, String appId);

	String generatePermissionCode(String id, String name);

	void synData();

	void synComUserRoleShips();

	List<Object> findAppsByUserId(String id);

	AppInfoEntity getAppInfo(String appId);

	boolean checkIsRight(String id);

	boolean checkIsAllocated(String id);

	boolean checkRoleIsAllocated(String roleId);
	
}
