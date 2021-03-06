package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.AdvanceEntity;
import com.zhizaolian.staff.entity.PaymentEntity;
import com.zhizaolian.staff.entity.ReimbursementEntity;

public interface ReimbursementDao {

	int save(ReimbursementEntity reimbursementEntity);
	int saveAdvance(AdvanceEntity advanceEntity);

	List<ReimbursementEntity> findReimbursementsByUserID(String userID, int page, int limit);
	List<AdvanceEntity> findAdvancesByUserID(String userID, int page, int limit);

	int countReimbursementsByUserID(String userID);
	int countAdvancesByUserID(String userID);

	void updateProcessStatusByProcessInstanceID(String processInstanceID, int status);
	void updateAdvanceProcessStatusByProcessInstanceID(String processInstanceID, int status);

	ReimbursementEntity getReimbursementByProcessInstanceID(String processInstanceID);
	AdvanceEntity getAdvanceByProcessInstanceID(String processInstanceID);

	void setfinancialFirstAuditName(String instanceId,String name,int type);
	void setAdvanceFinancialFirstAuditName(String instanceId,String name,int type);
	int savePayment(PaymentEntity paymentEntity);
	List<PaymentEntity> findPaymentsByUserID(String userID, Integer page, Integer limit);
	int countPaymentByUserID(String userID);
	PaymentEntity getPaymentByProcessInstanceID(String processInstanceID);
	void updatePaymentProcessStatusByProcessInstanceID(String processInstanceID, int value);
	void setPaymentFinancialFirstAuditName(String instanceId, String name, int type);
}
