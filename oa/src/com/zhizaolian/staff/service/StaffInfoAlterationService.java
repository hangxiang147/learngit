package com.zhizaolian.staff.service;


import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;
import com.zhizaolian.staff.vo.StaffVO;

/**
 * 员工信息表变动（目前是薪资和职级变动）的service接口
 * @author wjp
 *
 */
public interface StaffInfoAlterationService {
	
	public void update(StaffInfoAlterationVO staffInfoAlterationVO);

	public ListResult<StaffInfoAlterationVO> gradeHistory(String userID,Integer page, Integer limit);

	public ListResult<StaffInfoAlterationVO> salaryHistory(String userID, Integer page, Integer limit);

	public ListResult<StaffVO> findStaffByGradeAndName(StaffVO staffVO,Integer page, Integer limit);

	
}
