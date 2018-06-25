package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;

import com.zhizaolian.staff.entity.CertificateBorrowEntity;
import com.zhizaolian.staff.entity.CertificateEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CertificateBorrowVo;

public interface CertificateService {
	
	void saveCertificate(CertificateEntity certificate, File[] files, String[] fileNames) throws Exception;

	ListResult<CertificateEntity> getCertificateLst(String name, int limit, int page);

	CertificateEntity getCertificate(String certificateId);

	List<String> getAttachmentNames(String certificateId);

	void deleteCertificate(String certificateId);

	void updateCertificate(CertificateEntity certificate, File[] attachment, String[] attachmentFileName) throws Exception;

	void startCertificateBorrow(CertificateBorrowVo certificateBorrowVo);

	ListResult<CertificateBorrowVo> findCertificateBorrowLstByUserID(String id, Integer page, Integer limit);

	ListResult<CertificateBorrowEntity> getCertificateBorrowLst(String[] qurey, int limit, int page);

	void updateIdRealBeginTime(String intanceId);

	void updateIdRealEndTime(String intanceId);

	void updateProcessStatus(TaskResultEnum result, String processInstanceID);

	boolean checkIsExist(String certificateName, String certificateType,String id);

}
