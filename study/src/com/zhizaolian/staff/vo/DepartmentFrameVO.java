package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
@Data
public class DepartmentFrameVO {
	private CompanyVO companyVO;
	
	private DepartmentVO departmentVO;
	
	private List<DepartmentVO> departmentVOs;
	
	private List<PositionVO> positionVOs;

}
