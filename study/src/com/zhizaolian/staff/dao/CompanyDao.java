package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.CompanyEntity;

public interface CompanyDao {

	List<CompanyEntity> findAllCompanys();
	
	CompanyEntity getCompanyByCompanyID(int companyID);
	
	CompanyEntity getCompanyByCompanyName(String companyName);

	Integer getCompanyIdByUserId(String userId);
	
}
