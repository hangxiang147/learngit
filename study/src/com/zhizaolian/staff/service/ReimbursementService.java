package com.zhizaolian.staff.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.AdvanceEntity;
import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AdvanceTaskVO;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.BankAccountVO;
import com.zhizaolian.staff.vo.PaymentTaskVO;
import com.zhizaolian.staff.vo.PaymentVo;
import com.zhizaolian.staff.vo.ReimbursementTaskVO;
import com.zhizaolian.staff.vo.ReimbursementVO;

public interface ReimbursementService {

	/**
	 * 启动一个报销流程
	 * @param reimbursementVO
	 */
	void startReimbursement(ReimbursementVO reimbursementVO,File[] file,String fileName) throws IOException;
	void reStartReimbursement(ReimbursementVO reimbursementVO,File[] file,String fileName,String instanceId) throws IOException;
	void startAdvance(AdvanceVo advanceVo,File[] file,String fileName) throws IOException;


	/**
	 * 分页查询指定用户的报销申请
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<ReimbursementVO> findReimbursementListByUserID(String userID, int page, int limit);

	ListResult<AdvanceVo> findAdvanceListByUserID(String userID, int page, int limit);

	/**
	 * 更新报销申请流程节点的处理结果
	 * @param processInstanceID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	void updateAdvanceProcessStatus(String processInstanceID, TaskResultEnum taskResult);

	/**
	 * 根据用户ID查找该员工预留的打款账号
	 * @param userID
	 * @return
	 */
	BankAccountVO getBankAccountByUserID(String userID);

	/**
	 * 修改指定用户的打款账号
	 * @param userID
	 */
	void updateBankAccountByUserID(String userID, ReimbursementVO reimbursementVO);
	void updateAdvanceBankAccountByUserID(String userID, AdvanceVo advanceVo);
	/**
	 * 根据用户组和用户列表，分页查找有权限处理的报销申请列表
	 * @param tasks
	 * @param groups
	 * @param users
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<ReimbursementTaskVO> findReimbursementsByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, String reimbursementNo, String beginDate, String endDate, int page, int limit);

	ListResult<ReimbursementTaskVO> findReimbursementsAll(String reimbursementNo, String demandName, String beginDate, String endDate, int page, int limit);


	ListResult<AdvanceTaskVO> findAdvancessByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, String reimbursementNo, String beginDate, String endDate, int page, int limit);

	ListResult<AdvanceTaskVO> findAdvancessAll(String reimbursementNo, String beginDate, String endDate, int page, int limit);

	/**
	 * 根据taskID，查询对应报销申请流程的报销申请参数
	 * @param taskID
	 * @return
	 */
	ReimbursementVO getReimbursementVOByTaskID(String taskID);
	AdvanceVo getAdvanceVOByTaskID(String taskID);
	/**
	 * 根据流程实例ID，查询对应报销流程的详细参数
	 * @param processInstanceID
	 * @return
	 */
	ReimbursementVO getReimbursementVOByProcessInstanceID(String processInstanceID);
	AdvanceVo geAdvanceTaskVOByProcessInstanceID(String processInstanceID);

	List<ReimbursementTaskVO> createTaskVOListByTaskList(List<Task> tasks);
	List<AdvanceTaskVO> createAdvanceTaskVOListByTaskList(List<Task> tasks);

	ListResult<ReimbursementVO> findReimbursementList(ReimbursementVO reimbursementVO,int page,int limit);
	ListResult<AdvanceVo> findAdvanceList(ReimbursementVO reimbursementVO,int page,int limit);
	/**
	 * 由于票据上的需要  需要将 一级财务 是哪个会计 进行审批的进行记录   现在 记录到 Reimbursement实体上
	 * @param instanceId
	 * @param name
	 */
	void setfinancialFirstAuditName(String instanceId,String name,int type);
	void setAdvanceFinancialFirstAuditName(String instanceId,String name,int type);
	void updateRestartStatus(Integer reimbursementID);
	void startPayment(PaymentVo paymentVo, File[] file, String fileDetail) throws Exception;
	ListResult<PaymentVo> findPaymentListByUserID(String id, Integer page, Integer limit);
	List<PaymentTaskVO> createPaymentTaskVOListByTaskList(List<Task> paymentTask);
	ListResult<PaymentTaskVO> findPaymentsByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,
			String reimbursementNo, String beginDate, String endDate, int page, int limit);
	PaymentVo getPaymentVOByTaskID(String taskID);
	PaymentVo gePaymentTaskVOByProcessInstanceID(String id);
	void updatePaymentBankAccountByUserID(String payeeID, PaymentVo arg);
	void updatePaymentProcessStatus(String processInstanceID, TaskResultEnum result);
	void setPaymentFinancialFirstAuditName(String id, String realNameByUserId, int i);
	ListResult<PaymentVo> findPaymentList(ReimbursementVO reimbursementVO, Integer page, Integer limit);
	ListResult<PaymentTaskVO> findPaymentsAll(String reimbursementNo, String beginDate, String endDate, Integer page,
			Integer limit);
	void updateAdvanceInvoiceIds(String invoiceIds, String pInstanceId);
	AdvanceEntity getAdvanceEntityByPInstanceId(String pInstanceId);
	List<BankAccountEntity> getBankAccountByPayeeName(String lastName);
	int saveBankAccount(BankAccountEntity bankAccountVo);
	BankAccountEntity getBankAccountById(Integer accountID);
	void updateBankAccount(BankAccountEntity bankAccountVo);
	boolean checkBankAccountExist(BankAccountEntity bankAccountVo);
	ListResult<ReimbursementVO> getReimbursementVOList(ReimbursementVO reimbursementVO,Integer page,Integer limit);
	ListResult<AdvanceVo> getAdvanceVoList(AdvanceVo advanceVo,Integer page,Integer limit);
	ListResult<PaymentVo> getPaymentVoList(PaymentVo paymentVo,Integer page,Integer limit);
}
