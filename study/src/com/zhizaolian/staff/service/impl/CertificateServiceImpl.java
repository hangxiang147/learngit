package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CertificateBorrowEntity;
import com.zhizaolian.staff.entity.CertificateEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.CertificateService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.vo.CertificateBorrowVo;

public class CertificateServiceImpl implements CertificateService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Override
	public void saveCertificate(CertificateEntity certificate, File[] files, String[] fileNames) 
			throws Exception{
		if(null != files && files.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<files.length; i++){
				String fileName = fileNames[i];
				fileName = UUID.randomUUID()+"_"+fileName;
				File destFile = new File(Constants.CERTIFICATE_FILE_DIRECTORY, fileName);
				FileUtils.copyFile(files[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			certificate.setAttachmentName(attachmentNames);
			certificate.setAttachmentPath(attachmentPaths);
		}
		baseDao.hqlSave(certificate);
	}
	@Override
	public ListResult<CertificateEntity> getCertificateLst(String name, int limit, int page) {
		String sql = "from CertificateEntity where isDeleted=0";
		if(StringUtils.isNotBlank(name)){
			sql += " and name like '%" + name + "%'";
		}
		@SuppressWarnings("unchecked")
		List<CertificateEntity> certificateLst = (List<CertificateEntity>)baseDao.hqlPagedFind(sql, page, limit);
		List<CertificateEntity> _certificateLst = Lists2.transform(certificateLst,
				new Function<CertificateEntity, CertificateEntity>() {
					@Override
					public CertificateEntity apply(CertificateEntity arg0) {
						arg0.setStore_personName(
								staffDao.getEmployeeNameByUsrId(
										arg0.getStore_person()));
						arg0.setSubject_personName(
								staffDao.getEmployeeNameByUsrId(
										arg0.getSubject_person()));
						return arg0;
					}
				});
		
		String sqlCount = "select count(id) from CertificateEntity where isDeleted=0";
		int count = Integer.parseInt(baseDao.hqlfindUniqueResult(sqlCount)+"");
		return new ListResult<CertificateEntity>(_certificateLst, count);
	}
	@Override
	public CertificateEntity getCertificate(String certificateId) {
		String hql = "from CertificateEntity where id="+certificateId;
		CertificateEntity certificate = (CertificateEntity)baseDao.hqlfindUniqueResult(hql);
		String attachmentNameStr = certificate.getAttachmentName();
		List<String> attachmentNames = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentNameStr)){
			String[] attachNames = attachmentNameStr.split("#@#&");
			for(String attachmentName: attachNames){
				if(StringUtils.isNotBlank(attachmentName)){
					attachmentNames.add(attachmentName);
				}
			}
		}
		certificate.setAttachmentNames(attachmentNames);
		certificate.setStore_personName(staffDao.getEmployeeNameByUsrId(certificate.getStore_person()));
		certificate.setSubject_personName(staffDao.getEmployeeNameByUsrId(certificate.getSubject_person()));
		return certificate;
	}
	@Override
	public List<String> getAttachmentNames(String certificateId) {
		String sql = "select attachmentName from OA_Certificate where id="+certificateId;
		String attachmentNameStr = baseDao.getUniqueResult(sql)+"";
		List<String> attachmentNames = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentNameStr)){
			String[] attachNames= attachmentNameStr.split("#@#&");
			for(String attachmentName: attachNames){
				if(StringUtils.isNotBlank(attachmentName)){
					attachmentNames.add(attachmentName);
				}
			}
		}
		return attachmentNames;
	}
	@Override
	public void deleteCertificate(String certificateId) {
		String sql = "update OA_Certificate set isDeleted=1 where id="+certificateId;
		baseDao.excuteSql(sql);
		
	}
	@Override
	public void updateCertificate(CertificateEntity certificate, File[] attachment, String[] attachmentFileName) throws Exception {
		if(null != attachment && attachment.length>0){
			String attachmentNames = "";
			String attachmentPaths = "";
			for(int i=0; i<attachment.length; i++){
				String fileName = attachmentFileName[i];
				fileName = UUID.randomUUID()+"_"+fileName;
				File destFile = new File(Constants.CERTIFICATE_FILE_DIRECTORY, fileName);
				FileUtils.copyFile(attachment[i], destFile);
				if(i==0){
					attachmentNames += fileName;
					attachmentPaths += destFile;
				}else{
					attachmentNames += "#@#&"+fileName;
					attachmentPaths += "#@#&"+destFile;
				}
			}
			certificate.setAttachmentName(attachmentNames);
			certificate.setAttachmentPath(attachmentPaths);
		}
		baseDao.hqlUpdate(certificate);
		
	}
	@Override
	public void startCertificateBorrow(CertificateBorrowVo certificateBorrowVo) {
		String enumName = BusinessTypeEnum.CERTIFICATE_BORROW.getName();
		certificateBorrowVo.setBusinessType(enumName);
		certificateBorrowVo.setTitle(certificateBorrowVo.getUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		CertificateEntity certificate = getCertificate(certificateBorrowVo.getCertificateId()+"");
		//只寻找一级
		String supervisor = staffService.querySupervisorOneStep(certificateBorrowVo.getUserId());
		//如果没有主管 那么 组长审批
		if(StringUtils.isBlank(supervisor)){
			supervisor=staffService.queryHeadMan(certificateBorrowVo.getUserId());
		}
		if (StringUtils.isBlank(supervisor) || certificateBorrowVo.getUserId().equals(supervisor)) {
			supervisor=staffService.querySupervisor(certificateBorrowVo.getUserId());
		}
		String manager = staffService.queryManager(certificateBorrowVo.getUserId());
		
		if (StringUtils.isBlank(manager)) {
			throw new RuntimeException("未找到该申请人所属部门分管领导");
		}
		vars.put("supervisor", supervisor);
		vars.put("manager", manager);
		vars.put("id_storage_user", certificate.getStore_person());
		vars.put("id_subject_user", certificate.getSubject_person());
		vars.put("certificate_isBorrow", certificateBorrowVo.getIsBorrow());
		vars.put("arg", certificateBorrowVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CERTIFICATE_BORROW);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), certificateBorrowVo.getUserId());
		taskService.complete(task.getId(), vars);
		saveCertificateBorrow(certificateBorrowVo, processInstance.getId());
	}
	private void saveCertificateBorrow(CertificateBorrowVo certificateBorrowVo,
			String instanceId) {
		CertificateBorrowEntity certificateBorrowEntity = null;
		try {
			certificateBorrowEntity = (CertificateBorrowEntity) CopyUtil.tryToEntity(certificateBorrowVo, CertificateBorrowEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		certificateBorrowEntity.setProcessInstanceID(instanceId);
		certificateBorrowEntity.setAddTime(new Date());
		certificateBorrowEntity.setIsDeleted(0);
		baseDao.hqlSave(certificateBorrowEntity);
	}
	@Override
	public ListResult<CertificateBorrowVo> findCertificateBorrowLstByUserID(String userId, Integer page, Integer limit) {
		List<CertificateBorrowEntity> certificateBorrows = getCertificateBorrowByUserId(userId,
				page, limit);

		List<CertificateBorrowVo> certificateBorrowVos = new ArrayList<CertificateBorrowVo>();
		for (CertificateBorrowEntity certificateBorrow : certificateBorrows) {
			CertificateBorrowVo certificateBorrowVo = null;
			try {
				certificateBorrowVo = (CertificateBorrowVo) CopyUtil.tryToVo(certificateBorrow, CertificateBorrowVo.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService
					.createHistoricDetailQuery()
					.processInstanceId(certificateBorrowVo.getProcessInstanceID())
					.list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					CertificateBorrowVo arg = (CertificateBorrowVo) variable.getValue();
					certificateBorrowVo.setRequestDate(arg.getRequestDate());
					certificateBorrowVo.setTitle(arg.getTitle());
					certificateBorrowVo.setUserName(arg.getUserName());
				}
			}
			ProcessInstance pInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(certificateBorrowVo.getProcessInstanceID())
					.singleResult();
			if (pInstance != null) {
				certificateBorrowVo.setStatus("处理中");
				certificateBorrowVo.setAssigneeUserName(processService
						.getProcessTaskAssignee(pInstance.getId()));
			} else {
				Integer value_ = certificateBorrowVo.getApplyResult();
				if (value_ != null) {
					TaskResultEnum t = TaskResultEnum.valueOf(value_);
					if (t != null)
						certificateBorrowVo.setStatus(t.getName());
				}
			}
			Integer certificateId = certificateBorrowVo.getCertificateId();
			certificateBorrowVo.setCertificateName(
					getCertificate(certificateId+"").getName());
			certificateBorrowVos.add(certificateBorrowVo);
		}
		int count = getChopBorrowCountByUserId(userId);
		return new ListResult<CertificateBorrowVo>(certificateBorrowVos, count);
	}
	public int getChopBorrowCountByUserId(String userId) {
		String hql = "select count(id) from CertificateBorrowEntity where IsDeleted=0 and userId='"+userId+"'";
		return Integer.parseInt(baseDao.hqlfindUniqueResult(hql)+"");
	}
	@SuppressWarnings("unchecked")
	public List<CertificateBorrowEntity> getCertificateBorrowByUserId(String userId, int page,
			int limit) {
		String hql="from CertificateBorrowEntity where IsDeleted=0 and userId='"+userId+"'";
		return (List<CertificateBorrowEntity>) baseDao.hqlPagedFind(hql, page, limit);
	}
	@Override
	public ListResult<CertificateBorrowEntity> getCertificateBorrowLst(String[] query, int limit, int page) {
		String hql = "from CertificateBorrowEntity where isDeleted=0 and certificateId="+query[0];
		if(StringUtils.isNotBlank(query[1])){
			hql += " and addTime>='"+query[1]+"'";
		}
		if(StringUtils.isNotBlank(query[2])){
			hql += " and addTime<='"+query[2]+" 24:00:00'";
		}
		Object objLst = baseDao.hqlPagedFind(hql, page, limit);
		@SuppressWarnings("unchecked")
		List<CertificateBorrowEntity> certificateBorrowLst = (List<CertificateBorrowEntity>)objLst;
		List<CertificateBorrowEntity> _certificateBorrowLst = Lists2.transform(certificateBorrowLst,
				new Function<CertificateBorrowEntity, CertificateBorrowEntity>() {

			@Override
			public CertificateBorrowEntity apply(CertificateBorrowEntity arg0) {
				arg0.setUserName(staffDao.getEmployeeNameByUsrId(
										arg0.getUserId()));
				return arg0;
			}
			
		});
		String countSql = "select count(*) from OA_CertificateBorrow where isDeleted=0 and certificateId="+query[0];
		if(StringUtils.isNotBlank(query[1])){
			countSql += " and addTime>="+query[1];
		}
		if(StringUtils.isNotBlank(query[2])){
			countSql += " and addTime<="+query[2];
		}
		Object obj = baseDao.getUniqueResult(countSql);
		int count = Integer.parseInt(obj+"");
		return new ListResult<CertificateBorrowEntity>(_certificateBorrowLst, count);
	}
	@Override
	public void updateIdRealBeginTime(String intanceId) {
		String sql = "update OA_CertificateBorrow set realStartTime='"+DateUtil.formateFullDate(new Date())+"' where processInstanceID='"+intanceId+"'";
		baseDao.excuteSql(sql);
	}
	@Override
	public void updateIdRealEndTime(String intanceId) {
		String sql = "update OA_CertificateBorrow set realEndTime='"+DateUtil.formateFullDate(new Date())+"' where processInstanceID='"+intanceId+"'";
		baseDao.excuteSql(sql);
	}
	@Override
	public void updateProcessStatus(TaskResultEnum result, String processInstanceID) {
		String hql="update CertificateBorrowEntity s set s.applyResult="+result.getValue()+" where s.processInstanceID='"+processInstanceID+"' ";
		baseDao.excuteHql(hql);
	}
	@Override
	public boolean checkIsExist(String certificateName, String certificateType,String id) {
		String sql = "select count(*) from OA_Certificate certificate where certificate.`name`='"
					 +certificateName+"' and certificate.type='"+certificateType+"' and certificate.isDeleted=0 ";
		if(StringUtils.isNotBlank(id)){
			sql+=" and certificate.id !="+id;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
}
