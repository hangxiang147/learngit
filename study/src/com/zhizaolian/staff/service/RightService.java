package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface RightService {
	List<Object[]> getAllRight();
	void insertRight(String rightName,String code);
	ListResult<Object[]> getRightMemberShip(String userId,int page,int limit);
	ListResult<Object[]> getGroupRightMemberShip(String groupId,int page,int limit);
	void createRightMemberShip(String keyId,String type,String rightId);
	void breakMemberShip(int id);
	String getGroupIdByKeys(String companyId,String departMentId,String positionId);
	void saveRight(PermissionEntity permission);
}
