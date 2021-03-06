package com.zhizaolian.staff.action.HR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.ContractStatusEnum;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.ContractVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;

import lombok.Getter;
import lombok.Setter;

public class ContractAction extends BaseAction {

	@Setter
	@Getter
	private String selectedPanel;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private String errorMessage;
	@Setter
	@Getter
	private ContractVO contractVO;
	@Autowired
	private ContractService contractService;
	@Setter
	@Getter
	private File contract;
	@Setter
	@Getter 
	private String contractContentType;
	@Setter
	@Getter 
	private String contractFileName;
	@Setter
	@Getter
	private File signat;
	@Setter
	@Getter 
	private String signatContentType;
	@Setter
	@Getter 
	private String signatFileName;
	@Autowired
	private PositionService positionService;
	@Getter
	private InputStream inputStream;
	@Setter
	@Getter
	private String downloadFileFileName;
	@Autowired
	private StaffService staffService;
	
	
	
	private static final long serialVersionUID = 1L;
	
	@Setter
	@Getter
	private Integer type; 
	public String findContractList() {
		if(type==null){
			type=0;
		}
		
		switch(type){
		case 0:
			if(contractVO==null){
				contractVO = new ContractVO();
			}
			
			try{
				ListResult<ContractVO> contractList = contractService.findContractByContractVO(contractVO, page, limit);
				count=contractList.getTotalCount();
				totalPage = count%limit==0 ? count/limit : count/limit+1;
				if(totalPage==0)
				totalPage = 1;
				request.setAttribute("contracts", contractList.getList());
				List<CompanyVO> companyVOs = positionService.findAllCompanys();
				if (contractVO != null && contractVO.getCompanyID() != null) {
					List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(contractVO.getCompanyID());
					if (departmentVOs != null && contractVO.getDepartmentID() != null) {
						List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
						int selectedDepartmentID = contractVO.getDepartmentID();
						while (selectedDepartmentID != 0) {
							selectedDepartmentIDs.add(0, selectedDepartmentID);
							for (DepartmentVO departmentVO : departmentVOs) {
								if (departmentVO.getDepartmentID() == selectedDepartmentID) {
									selectedDepartmentID = departmentVO.getParentID();
								}
							}
						}
						request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
					}
					request.setAttribute("departmentVOs", departmentVOs);
				}
				request.setAttribute("companyVOs", companyVOs);
			}catch(Exception e){
				errorMessage = "查询失败："+e.getMessage();
				e.printStackTrace();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
				return "error";
			}
			break;
		case 1:
			try{
				if(contractVO==null){
					contractVO = new ContractVO();
				}
				
				User user = (User)request.getSession().getAttribute("user");
				List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
				contractVO.setAdmCompanyIDs(Lists2.transform(groups, new SafeFunction<GroupDetailVO, Integer>() {
					@Override
					protected Integer safeApply(GroupDetailVO input) {
						return input.getCompanyID();
					}
				}));
				
				ListResult<ContractVO> contractList = contractService.findContractByContractVO(contractVO, page, limit);
				count=contractList.getTotalCount();
				totalPage = count%limit==0 ? count/limit : count/limit+1;
				if(totalPage==0)
				totalPage = 1;
				request.setAttribute("contracts", contractList.getList());
			}catch(Exception e){
				e.printStackTrace();
				errorMessage = "获取列表失败："+e.getMessage();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
				return "error";
				
			}
			break;
			
		}
		
		
		
		selectedPanel = "contractList";
		return "contractList";
	}
	
	public String saveRenewContract() {
		try {
			contractService.saveRenewContract(contractVO, contract, signat, contractFileName, signatFileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "合同续签失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		return "saveRenewContract";
	}
	
	public String saveContract(){
		InputStream in = null;
		OutputStream out = null;
		try{
			Integer contractID = contractVO.getContractID()==null?contractService.saveContract(contractVO):contractVO.getContractID();
			
			if(contract!=null){
				if (!StringUtils.isEmpty(contractVO.getContractBackups())) {
					File backups = new File(Constants.CONTRACT_FILE_DIRECTORY, contractID+"_"+contractVO.getContractBackups());
					if (backups.exists()) {
						backups.delete();
					}
				}
	    		byte[] buffer = new byte[10*1024*1024];
	    		int length = 0;
	    		in=new FileInputStream(contract);
	    		File f=new File(Constants.CONTRACT_FILE_DIRECTORY);
	    		if(!f.exists()){
	    			f.mkdirs();
	    		}
	    		out=new FileOutputStream(new File(f,contractID+"_"+contractFileName));
	    		while((length=in.read(buffer, 0, buffer.length))!=-1){
	    			out.write(buffer, 0, length);
	    		}
	    		contractVO.setContractBackups(contractFileName);
    		}
			if(signat!=null){
				if (!StringUtils.isEmpty(contractVO.getSignature())) {
					File signature = new File(Constants.CONTRACT_FILE_DIRECTORY, contractID+"_"+contractVO.getSignature());
					if (signature.exists()) {
						signature.delete();
					}
				}
				byte[] buffer = new byte[10*1024*1024];
				int length = 0;
				in=new FileInputStream(signat);
				File f=new File(Constants.CONTRACT_FILE_DIRECTORY);
				if(!f.exists()){
					f.mkdirs();
				}
				out=new FileOutputStream(new File(f,contractID+"_"+signatFileName));
				while((length=in.read(buffer, 0, buffer.length))!=-1){
					out.write(buffer, 0, length);
				}
				contractVO.setSignature(signatFileName);
			}
			contractVO.setContractID(contractID);
			contractService.updateContract(contractVO);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
				}
			
		}
		return "saveContract";
	}
	
	public String findContractsByPartyB() {
		try {
			int contractID = Integer.parseInt(request.getParameter("contractID"));
			ContractVO contractVO = contractService.getContractVOBycontractID(contractID);
			List<ContractVO> expiredContractVOs = contractService.findContractsByPartyBStatus(contractVO.getPartyB(), ContractStatusEnum.EXPIRED);
			request.setAttribute("contractVO", contractVO);
			request.setAttribute("expiredContractVOs", expiredContractVOs);
		} catch(Exception e){
			errorMessage = "查询失败："+e.getMessage();
			return "error";
		}
		
		return "findContracts";
	}
	
	public String updateContractByContractID() {
		try{
			Integer contractID=Integer.parseInt(request.getParameter("contractID"));
			ContractVO contractVO = contractService.getContractVOBycontractID(contractID);
			request.setAttribute("contractVO", contractVO);

		}catch(Exception e){
			errorMessage = "查询失败："+e.getMessage();
			return "error";
		}
		selectedPanel = "contractList";
		return "updateContract";
	}
	
	public String deleteContractByContractID() {
		try {
			Integer contractID = Integer.parseInt(request.getParameter("contractID"));
			contractService.deleteContract(contractID);
		} catch (Exception e) {
			errorMessage = "合同删除失败：" + e.getMessage();
		}
		
		return "deleteContract";
	}
	
	//下载文件
	public String download(){
		try{
			String dLDName = request.getParameter("dLDName");
			inputStream = new FileInputStream(new File(Constants.CONTRACT_FILE_DIRECTORY,dLDName));
			downloadFileFileName = new String(dLDName.getBytes(), "ISO8859-1");
		}catch(Exception e){
			errorMessage = "下载失败："+e.getMessage();
			e.printStackTrace();
			return "error";
			
		}
		
		return "download";
	}
	
	public String findRemindCtrList(){
		try{
			if(contractVO==null){
				contractVO = new ContractVO();
			}
			
			User user = (User)request.getSession().getAttribute("user");
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
			contractVO.setAdmCompanyIDs(Lists2.transform(groups, new SafeFunction<GroupDetailVO, Integer>() {
				@Override
				protected Integer safeApply(GroupDetailVO input) {
					return input.getCompanyID();
				}
			}));
			
			ListResult<ContractVO> contractList = contractService.findContractByContractVO(contractVO, page, limit);
			count=contractList.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
			totalPage = 1;
			request.setAttribute("contracts", contractList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取列表失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}

		selectedPanel = "remindCtrList";
		return "remindCtrList";
	}
	
	public String renewContract() {
		try{
			Integer contractID=Integer.parseInt(request.getParameter("contractID"));
			ContractVO contractVO = contractService.getContractVOBycontractID(contractID);
			request.setAttribute("contractVO", contractVO);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		selectedPanel = "remindCtrList";
		return "renewContract";
	}
	
	public String getContractByContractID(){
		try{
			Integer contractID=Integer.parseInt(request.getParameter("contractID"));
			ContractVO contractVO = contractService.getContractVOBycontractID(contractID);
			request.setAttribute("contractVO", contractVO);

		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		return "getContract";
	}
	
}
