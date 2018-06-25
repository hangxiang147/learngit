package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.DepartmentEntity;

public interface DepartmentDao {

	List<DepartmentEntity> findDepartmentsByCompanyIDParentID(int companyID, int parentID);
	
	List<DepartmentEntity> findDepartmentsByCompanyID(int companyID);
	
	DepartmentEntity getDepartmentByDepartmentID(int departmentID);
	
	DepartmentEntity getDepartmentByCompanyIDAndName(int companyID, String name);
	
	List<DepartmentEntity> findDepartmentByParentID(int parentID);

	DepartmentEntity getDepartmentByDepartmentID_(int departmentID);
	/**
	 * 获取人员所在部门Id（若存在多个分公司的部门，以总部优先）
	 * @param userId
	 * @return
	 */
	Integer getDeparmentIdByUserId(String userId);
}
