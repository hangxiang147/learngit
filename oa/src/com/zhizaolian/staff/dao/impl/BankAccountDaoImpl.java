package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BankAccountDao;
import com.zhizaolian.staff.entity.BankAccountEntity;

public class BankAccountDaoImpl implements BankAccountDao{

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public void save(BankAccountEntity bankAccountEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(bankAccountEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public BankAccountEntity getBankAccountByUserID(String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from BankAccountEntity bankAccount where bankAccount.userID = :userID and bankAccount.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userID);
		List<BankAccountEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
