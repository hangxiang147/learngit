package com.zhizaolian.staff.service;

import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.enums.StaffStatusEnum;

public interface StaffAnalysisService {

	Map<String, Object> getStaffNumByMonthAndDep(List<String> depNames, List<String> depIds);

	Map<String, Object> getStaffGenderByDep(List<String> departmentNames, List<String> departmentIds, StaffStatusEnum status);

	Map<String, Object> getStaffInfoByDep(List<String> departmentNames, List<String> departmentIds, StaffStatusEnum status);

	Map<String, Object> getLeaveStaffNumByMonthAndDep(List<String> departmentNamesForStaffNum,
			List<String> departmentIdsForStaffNum);

}
