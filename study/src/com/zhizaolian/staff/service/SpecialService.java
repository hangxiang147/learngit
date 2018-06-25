package com.zhizaolian.staff.service;



import java.util.List;

import com.zhizaolian.staff.vo.SpecialVO;

public interface SpecialService {
	void saveSpecial(SpecialVO specialVO);
	public Integer getSpecialByTypeUserID(Integer type,String userID);
	List<SpecialVO> findBySql(Integer positionCategory,Integer type);
	public void deleteSpecial(Integer specialID);
}
