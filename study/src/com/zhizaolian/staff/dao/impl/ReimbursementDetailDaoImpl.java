package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ReimbursementDetailDao;
import com.zhizaolian.staff.entity.ReimbursementDetailEntity;

public class ReimbursementDetailDaoImpl implements ReimbursementDetailDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(ReimbursementDetailEntity reimbursementDetailEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(reimbursementDetailEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ReimbursementDetailEntity> findReimbursementDetailsByReimbursementID(int reimbursementID,int type) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from ReimbursementDetailEntity reimbursementDetailEntity where reimbursementDetailEntity.isDeleted = 0 "
				+ "and reimbursementDetailEntity.reimbursementID = :reimbursementID";
		if(type==0){
			hql+=" and (reimbursementDetailEntity.type is null or reimbursementDetailEntity.type ='0') ";
		}else if(type==1){
			hql+=" and  reimbursementDetailEntity.type ='1' ";
		}else {
			hql+=" and  reimbursementDetailEntity.type ='2' ";
		}
		Query query=session.createQuery(hql);
		query.setParameter("reimbursementID", reimbursementID);
		return query.list();
	}
}
