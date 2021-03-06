package com.zhizaolian.staff.service;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.entity.PermissionMembershipEntity;

public interface PermissionService {

	/**
	 * 查询给定用户拥有的所有权限
	 * @param userID
	 * @return
	 */
	List<String> findPermissionsByUserID(String userID);
	
	/**
	 * 查询指定公司拥有指定权限的用户组
	 * @param code
	 * @param companyID
	 * @return
	 */
	List<String> findGroupsByPermissionCodeCompany(String code, int companyID);
	
	/**
	 * 查询拥有指定权限的用户组
	 * @param code
	 * @return
	 */
	List<String> findGroupsByPermissionCode(String code);
	
	/**
	 * 查询指定公司拥有指定权限的用户列表
	 * @param code
	 * @param companyID
	 * @return
	 */
	List<String> findUsersByPermissionCodeCompany(String code, int companyID);
	
	/**
	 * 查询拥有指定权限的用户列表
	 * @param code
	 * @return
	 */
	List<String> findUsersByPermissionCode(String code);

	void deleteRight(String userId);
	/**
	 * 查询定用户拥有的所有工作流权限（组权限除外）
	 * @param userID
	 * @return
	 */
	Map<String, String> findUserPermissionsByUserID(String userID);

	List<PermissionEntity> findPermissionListByUserID(String resignationUserID);
	
	PermissionMembershipEntity findPermissionmembership(Integer permissionID);
}
