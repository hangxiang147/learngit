package com.zhizaolian.staff.dao;

import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffInfoAlterationEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.StaffVO;

/**
 * 员工信息变动记录的dao接口
 * @author wjp
 *
 */
public interface StaffInfoAlterationDao {

	public void save(StaffInfoAlterationEntity staffInfoRecordEntity);

	public ListResult<StaffInfoAlterationEntity> gradeHistory(String userID,int page, int limit);

	public ListResult<StaffInfoAlterationEntity> salaryHistory(String userID, Integer page, Integer limit);

	public ListResult<StaffEntity> findStaffByGradeAndName(StaffVO staffVO, Integer page, Integer limit);
	
}
