package com.zhizaolian.staff.dao;

import com.zhizaolian.staff.entity.BankAccountEntity;

public interface BankAccountDao {

	void save(BankAccountEntity bankAccountEntity);
	
	BankAccountEntity getBankAccountByUserID(String userID);

}
