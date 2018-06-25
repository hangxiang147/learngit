package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ContractDao;
import com.zhizaolian.staff.entity.ContractEntity;

public class ContractDaoImpl implements ContractDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer save(ContractEntity contractEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(contractEntity);
		return contractEntity.getContractID();
	
	}

	@Override
	@SuppressWarnings("unchecked")
	public ContractEntity getContractEntityByContractID(Integer contractID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from ContractEntity contract where contract.contractID = :contractID and contract.isDeleted = 0";
		Query query=session.createQuery(hql);
		query.setParameter("contractID", contractID);
		List<ContractEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ContractEntity> findContractsByPartyBStatus(String userID, int status) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from ContractEntity contract where contract.partyB = :partyB and contract.status = :status and contract.isDeleted = 0";
		Query query=session.createQuery(hql);
		query.setParameter("partyB", userID);
		query.setParameter("status", status);
		return query.list();
	}

}
