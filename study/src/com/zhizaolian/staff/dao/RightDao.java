package com.zhizaolian.staff.dao;

import java.util.List;

public interface RightDao {
	List<Object[]> getAllRight();
	void insertRight(String rightName,String code);
	List<Object[]> getRightMemberShip(String userId,int page,int limit);
	int getRightMemberShipCount(String userID);
	List<Object[]> getGroupRightMemberShip(String groupId,int page,int limit);
	int getGroupRightMemberShip(String groupId);
	void createRightMemberShip(String keyId,String type,String rightId);
	void breakMemberShip(int id);
	String getGroupIdByKeys(String companyId,String departMentId,String positionId);
	boolean checkHasThisRight(String requestUserId, Integer permissionID);

}
