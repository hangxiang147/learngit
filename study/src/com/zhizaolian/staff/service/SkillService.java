package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SkillVO;
import com.zhizaolian.staff.vo.StaffVO;


public interface SkillService {
	public void addSkill(StaffVO staffVO); 
	
	public List<StaffVO> getSkillByUserID(String userID); 
	
	public ListResult<SkillVO> findSkillListByUserID(SkillVO skillVO,int page,int limit);	
	
	public SkillVO getSkillBySkillID(String userID);
	
	public void updateSkill(StaffVO staffVO,String userID);
	
	

}
