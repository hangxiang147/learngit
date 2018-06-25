package com.zhizaolian.staff.dao;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.PermissionMembershipEntity;

public interface PermissionMembershipDao {

	List<Integer> findPermissionIDsByUserGroupIDType(String userGroupID, int type);
	
	List<Integer> findPermissionIDsByUserGroupIDsType(List<String> userGroupIDs, int type);
	
	List<String> findUserGroupIDsByPermissionIDType(int permissionID, int type);
	
	Map<String, String> findPermissionIDAndCodeMapByUserGroupIDType(String userGroupID, int type);

	PermissionMembershipEntity findPermissionMembershipById(int rightId);

	boolean checkHasPermissionByUserId(String permission, String id);
}
