package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.ContractBorrowEntity;
import com.zhizaolian.staff.entity.ContractManageEntity;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Getter;
import lombok.Setter;

public class ContractManageAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private int page = 1;
	@Setter
	@Getter
	private int limit = 20;
	@Getter
	private int totalPage;
	@Autowired
	private ContractService contractService;
	@Setter
	@Getter
	private String contractId;
	@Setter
	@Getter
	private ContractManageEntity contract;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String[] attachmentFileName;
	
	public String showContracts(){
		String name = request.getParameter("name");
		request.setAttribute("name", name);
		ListResult<ContractManageEntity> lstResult = contractService.getContractLst(name, limit, page);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		int startIndex = (page - 1) * limit + 1;
		request.setAttribute("startIndex", startIndex);
		request.setAttribute("contractLst", lstResult.getList());
		return "showContracts";
	}
	public String toEditContract(){
		if (null != contractId) {
			contract = contractService.getContract(contractId);
		}
		return "editContract";
	}
	public String saveContract(){
		try {
			if (null != contract.getId()) {
				contractService.updateContract(contract,
						attachment, attachmentFileName);
			} else {
				contract.setAddTime(new Date());
				contract.setIsDeleted(0);
				contractService.saveContract(contract,
						attachment, attachmentFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "toShowContracts";
	}
	public String showAttachment(){
		String contractId = request.getParameter("contractId");
		List<String> attachmentNames = contractService.getAttachmentNames(contractId);
		request.setAttribute("attachmentNames", attachmentNames);
		return "showAttachment";
	}
	public String deleteContract(){
		String contractId = request.getParameter("contractId");
		contractService.deleteContract(contractId);
		return "render_showContracts";
	}
	public void checkIsExist(){
		String contractID = request.getParameter("contractID");
		String id=request.getParameter("id");
		Map<String, String> resultMap = new HashMap<>();
		if(contractService.checkIsExist(contractID, id)){
			resultMap.put("isExist", "true");
		}else{
			resultMap.put("isExist", "false");
		}
		printByJson(resultMap);
	}
	@Setter
	@Getter
	private String startTime;
	@Setter
	@Getter
	private String endTime;
	public String toContractUseLog(){
		String[] qurey = {contractId, startTime, endTime};
		ListResult<ContractBorrowEntity> lstResult = contractService.getContractBorrowLst(qurey, limit, page);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		int startIndex = (page - 1) * limit + 1;
		request.setAttribute("startIndex", startIndex);
		request.setAttribute("contractBorrowLst", lstResult.getList());
		return "toContractUseLog";
	}
}
