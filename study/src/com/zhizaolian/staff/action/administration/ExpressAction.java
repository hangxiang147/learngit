package com.zhizaolian.staff.action.administration;

import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.identity.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.SendExpressService;
import com.zhizaolian.staff.service.ReceiveExpressService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.NoticeActorVO;
import com.zhizaolian.staff.vo.NoticeVO;
import com.zhizaolian.staff.vo.SendExpressVO;
import com.zhizaolian.staff.vo.SignExpressVO;
import com.zhizaolian.staff.vo.StaffVO;

public class ExpressAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	private String errorMessage;
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
	private SendExpressVO sendExpressVO;
	@Getter
	@Setter
	private SignExpressVO signExpressVO;
	@Getter
	private SignExpressVO signExpress;
	@Getter
	@Setter
	private InputStream excelFile;
	@Getter
	private String excelFileName;
	@Autowired
	private SendExpressService sendExpressService;
	@Autowired
	private ReceiveExpressService signExpressService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeActorService noticeActorService;
	
	
	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}
	
	public String exportSendExpressList() {
		if(sendExpressVO==null){
			sendExpressVO = new SendExpressVO();
		}
		try{
			HSSFWorkbook workbook = sendExpressService.exportSendExpressList(sendExpressVO==null?new SendExpressVO():sendExpressVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("快递账单明细.xlsx".getBytes(), "ISO8859-1");
			output.flush();
			output.close();
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "导出excel失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		return "exportSendExpressList";
	}
	
	public String findSendExpressList() {
		if(sendExpressVO==null){
			sendExpressVO = new SendExpressVO();
		}
		try{
			ListResult<SendExpressVO> sendExpressList = sendExpressService.findSendExpressVOsBySendExpressVO(sendExpressVO, page, limit);
			count=sendExpressList.getTotalCount();
			totalPage=count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0){
				totalPage=1;
			}
			request.setAttribute("sendExpressVOs", sendExpressList.getList());
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (sendExpressVO != null && sendExpressVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(sendExpressVO.getCompanyID());
				if (departmentVOs != null && sendExpressVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = sendExpressVO.getDepartmentID();
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
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		} 

		selectedPanel = "sendExpressList";
		return "sendExpressList";
	}
	public String addSendExpress(){
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		
		selectedPanel = "sendExpressList";
		return "addSendExpress";
	}
	public String saveSendExpress(){

		try{
			sendExpressService.saveSendExpress(sendExpressVO);
		}catch(Exception e){
			errorMessage = "保存失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "saveSendExpress";
	}
	
	public String findSignExpressList(){
		try{
			if(signExpressVO==null){
				signExpressVO = new SignExpressVO();
			}
			ListResult<SignExpressVO> signExpressList = signExpressService.findSignExpressVOsBySignExpressVO(signExpressVO,page, limit);
			count=signExpressList.getTotalCount();
			totalPage=count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0){
				totalPage=1;
			}
			request.setAttribute("signExpressVOs", signExpressList.getList());
		}catch(Exception e){
			errorMessage = "查询失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "signExpressList";
		return "signExpressList";
		
	}
	public String addSignExpress(){
		selectedPanel = "signExpressList";
		return "addSignExpress";
		
	}
	public String saveSignExpress(){
		User user = (User) request.getSession().getAttribute("user");
		signExpressVO.setOperatorID(user.getId());
		signExpressVO.setStatus(1);
		try{
			String ntcContent = "您有一份快递到了，请及时过来领取。 ";
			NoticeVO notice = new NoticeVO();
			notice.setCreatorID(user.getId());
			notice.setNtcTitle("快递领取通知");
			notice.setNtcContent(ntcContent);
			notice.setIsTop(0);
			notice.setType(3);
			Integer noticeID = noticeService.saveNoticeVO(notice);
			NoticeActorVO noticeActor = new NoticeActorVO();
			noticeActor.setUserID(signExpressVO.getRecipientID());
			noticeActor.setNoticeID(noticeID);
			noticeActor.setStatus(0);
			noticeActorService.saveNoticeActorVO(noticeActor);
			signExpressService.saveSignExpress(signExpressVO);
		}catch(Exception e){
			errorMessage = "保存失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "signExpressList";
		return "saveSignExpress";
	}
	
	public String getSignExpressByID(){
		Integer signExpressID = Integer.parseInt(request.getParameter("signExpressID"));
		try{
			signExpress =signExpressService.getSignExpressByID(signExpressID);
			signExpress.setSignExpressID(signExpressID);
		}catch(Exception e){
			errorMessage = "查询失败"+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "getSignExpress";
	}
	
	public String updateSignExpress(){
		try{
			signExpressVO.setStatus(2);
			signExpressService.updateSignExpress(signExpressVO);
		}catch(Exception e){
			errorMessage = "查询失败"+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "updateSignExpress";
	}
	

}
