package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface StaffDao {

	int save(StaffEntity staffEntity);
	
	StaffEntity getStaffByUserID(String userID);
	
	StaffEntity getStaffByStaffID(int staffID);
	
	StaffEntity getLatestStaff();
	
	List<StaffEntity> findStaffPageListByStatusList(List<Integer> statusList, int page, int limit);
	
	int countStaffByStatusList(List<Integer> statusList);
	
	List<StaffEntity> findStaffByName(String name);
	List<StaffEntity> findStaffByName(String name,int limit);
	
	List<StaffEntity> findStaffByNameAndStatus(String name,Integer positionCategory);
	
	public ListResult<Object> findStaffList(String hql,String hqlCount,int limit,int page);
	
	StaffEntity getStaffBytelephone(String telephone);
	
	String getUsefulValidateKeyByUserId(String userId,Boolean isRecent);
	
	String getEmployeeNameByUsrId(String userId);
	
	void  insertRestValidateKey(String userId,String validateKey);

	String getStaffNum(String userId);

	String getstaffUserIdByStaffNum(String staffNum);

}
