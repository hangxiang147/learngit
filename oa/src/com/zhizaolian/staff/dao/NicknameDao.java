package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.NicknameEntity;

public interface NicknameDao {

	List<NicknameEntity> findNicknamesByType(int type);
	
	NicknameEntity getNicknameByID(int nicknameID);
	
	void save(NicknameEntity nicknameEntity);
}
