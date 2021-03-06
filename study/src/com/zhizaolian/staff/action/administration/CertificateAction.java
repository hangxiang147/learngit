package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CertificateBorrowEntity;
import com.zhizaolian.staff.entity.CertificateEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.CertificateService;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Getter;
import lombok.Setter;

public class CertificateAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private CertificateEntity certificate;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String[] attachmentFileName;
	@Autowired
	private CertificateService certificateService;
	@Setter
	@Getter
	private String certificateId;
	@Setter
	@Getter
	private int page = 1;
	@Setter
	@Getter
	private int limit = 20;
	@Getter
	private int totalPage;
	public String toEditCertificate(){
		if (null != certificateId) {
			certificate = certificateService.getCertificate(certificateId);
		}
		return "editCertificate";
	}
	public String  saveCertificate(){
		try {
			if (null != certificate.getId()) {
				certificateService.updateCertificate(certificate,
						attachment, attachmentFileName);
			} else {
				certificate.setAddTime(new Date());
				certificate.setIsDeleted(0);
				certificateService.saveCertificate(certificate,
						attachment, attachmentFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "toShowCertificates";
	}
	public String showCertificates(){
		String name = request.getParameter("name");
		request.setAttribute("name", name);
		ListResult<CertificateEntity> lstResult = certificateService.getCertificateLst(name, limit, page);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		int startIndex = (page - 1) * limit + 1;
		request.setAttribute("startIndex", startIndex);
		request.setAttribute("certificateLst", lstResult.getList());
		return "showCertificates";
	}
	public String showAttachment(){
		String certificateId = request.getParameter("certificateId");
		List<String> attachmentNames = certificateService.getAttachmentNames(certificateId);
		request.setAttribute("attachmentNames", attachmentNames);
		return "showAttachment";
	}
	@Getter
	private InputStream inputStream;
	public String showImage(){
		String certificateImage = request.getParameter("certificateImage");
		try {
			inputStream = new FileInputStream(new File(Constants.CERTIFICATE_FILE_DIRECTORY, certificateImage)) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "imgStream";
	}
	public String  deleteCertificate(){
		String certificateId = request.getParameter("certificateId");
		certificateService.deleteCertificate(certificateId);
		return "render_showCertificates";
	}
	@Setter
	@Getter
	private String startTime;
	@Setter
	@Getter
	private String endTime;
	public String toCertificateUseLog(){
		String[] qurey = {certificateId, startTime, endTime};
		ListResult<CertificateBorrowEntity> lstResult = certificateService.getCertificateBorrowLst(qurey, limit, page);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		int startIndex = (page - 1) * limit + 1;
		request.setAttribute("startIndex", startIndex);
		request.setAttribute("certificateBorrowLst", lstResult.getList());
		return "toCertificateUseLog";
	}
	public void checkIsExist(){
		String certificateName = request.getParameter("name");
		String certificateType = request.getParameter("type");
		String id=request.getParameter("id");
		Map<String, String> resultMap = new HashMap<>();
		if(certificateService.checkIsExist(certificateName, certificateType,id)){
			resultMap.put("isExist", "true");
		}else{
			resultMap.put("isExist", "false");
		}
		printByJson(resultMap);
	}
}
