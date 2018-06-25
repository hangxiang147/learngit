package com.zhizaolian.staff.action.administration;

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
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.activiti.engine.identity.User;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.MeetingActorService;
import com.zhizaolian.staff.service.MeetingMinutesService;
import com.zhizaolian.staff.service.MeetingService;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.MeetingActorVO;
import com.zhizaolian.staff.vo.MeetingMinutesVO;
import com.zhizaolian.staff.vo.MeetingVO;
import com.zhizaolian.staff.vo.NoticeActorVO;
import com.zhizaolian.staff.vo.NoticeVO;
import com.zhizaolian.staff.vo.StaffVO;
import lombok.Getter;
import lombok.Setter;

public class MeetingAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String selectedPanel;
	@Getter 
	@Setter
	private MeetingVO meetingVO;
	@Getter
	@Setter
	private MeetingMinutesVO meetingMinutesVO;
	@Getter
	private String errorMessage;
	@Getter
	@Setter
	private String panel;	
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
	private List<String> groups;
	@Getter
	@Setter
	private List<String> groups1;
	@Autowired
	private PositionService positionService;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter 
	private String[] attachmentContentType;
	@Setter
	@Getter 
	private String[] attachmentFileName;
	@Setter
	@Getter
	private File[] upload;
	@Setter
	@Getter 
	private String[] uploadContentType;
	@Setter
	@Getter 
	private String[] uploadFileName;
	@Getter
	@Setter
	private Set<MeetingActorVO> meetingActorVO1;
	@Getter
	@Setter
	private Set<MeetingActorVO> meetingActorVO2;
	@Getter
	private InputStream inputStream;
	@Setter
	@Getter
	private String downloadFileFileName;
	@Autowired
	private MeetingService meetingService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private MeetingActorService meetingActorService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeActorService noticeActorService;
	@Autowired
	private MeetingMinutesService meetingMinutesService;
	
	
   public String launchMeeting() {
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		selectedPanel = "launchMeeting";
		return "launchMeeting";
	}
	
	
		
		
	public String saveMeeting(){
		InputStream in = null;
		OutputStream out = null;
		try{
			//将参加人公司和部门数组转为字符串
			StringBuffer ppCompanys = new StringBuffer();
			StringBuffer ppDepartments = new StringBuffer();
			if(meetingVO.getPpCompanyIDs()!=null){

				for(int i=0;i<meetingVO.getPpCompanyIDs().length;i++){
					ppCompanys.append(meetingVO.getPpCompanyIDs()[i]+",");
					ppDepartments.append(meetingVO.getPpDepartmentIDs()[i]+",");
				}
				meetingVO.setPpCompanys(ppCompanys.toString());
				meetingVO.setPpDepartments(ppDepartments.toString());
			}
			//将抄送人公司和部门数组转为字符串
			StringBuffer ccCompanys = new StringBuffer();
			StringBuffer ccDepartments = new StringBuffer();
			if(meetingVO.getCcCompanyIDs()!=null){

				for(int i=0;i<meetingVO.getCcCompanyIDs().length;i++){
					ccCompanys.append(meetingVO.getCcCompanyIDs()[i]+",");
					ccDepartments.append(meetingVO.getCcDepartmentIDs()[i]+",");
				}
				meetingVO.setCcCompanys(ccCompanys.toString());
				meetingVO.setCcDepartments(ccDepartments.toString());
			}
			Integer meetingID = meetingService.saveMeeting(meetingVO);

            //保存文件
            if(upload!=null){
            	StringBuffer uploadFileNames=new StringBuffer();
            	for(int i=0;i<upload.length;i++){
            		byte[] buffer = new byte[10*1024*1024];
            		int length = 0;
            		
            		
            		in=new FileInputStream(upload[i]);
            		File f=new File(Constants.MT_FILE_DIRECTORY);
            		if(!f.exists()){
            			f.mkdirs();
            		}
            		out=new FileOutputStream(new File(f,meetingID+"_"+uploadFileName[i]));
            		
            		while((length=in.read(buffer, 0, buffer.length))!=-1){
            			out.write(buffer, 0, length);
            		}
            		uploadFileNames.append(uploadFileName[i]);
            		uploadFileNames.append(",");
            	}
            	meetingVO.setMeetingID(meetingID);
            	meetingVO.setUploadNames(uploadFileNames.toString());
            	meetingService.saveMeeting(meetingVO);
            }
			
			
			
			
			//保存参加人
			if(meetingVO.getPpIDs()!=null){
				for(int i=0;i<meetingVO.getPpIDs().length;i++){
					meetingActorService.saveMettingByUserID(meetingID,1,meetingVO.getPpIDs()[i]);
				}
			}
			//保存抄送人
			if(meetingVO.getCcIDs()!=null){
				for(int i=0;i<meetingVO.getCcIDs().length;i++){
					meetingActorService.saveMettingByUserID(meetingID,2,meetingVO.getCcIDs()[i]);
				}
			}
			//按部门保存参加人
			if(meetingVO.getPpCompanyIDs()!=null){
				for(int i=0;i<meetingVO.getPpCompanyIDs().length;i++){
					meetingActorService.saveMettingByCompany(meetingVO.getPpCompanyIDs()[i],meetingVO.getPpDepartmentIDs()[i], meetingID,1);
				}
			}
			//按部门保存抄送人
			if(meetingVO.getCcCompanyIDs()!=null){
				for(int i=0;i<meetingVO.getCcCompanyIDs().length;i++){
					meetingActorService.saveMettingByCompany(meetingVO.getCcCompanyIDs()[i],meetingVO.getCcDepartmentIDs()[i], meetingID,2);
				}
			}
			//发送通知
			if(meetingVO.getIsNotice()!=null){
				//给参加人发送通知；
				NoticeVO pNoticeVO = new NoticeVO();
				User user = (User)request.getSession().getAttribute("user");
				pNoticeVO.setCreatorID(user.getId());
				pNoticeVO.setNtcTitle("会议通知");
				pNoticeVO.setNtcContent("会议主题："+meetingVO.getTheme()+"\r\n发起人："+meetingVO.getSponsorName()
						+ "\r\n会议开始时间："+meetingVO.getBeginTime()+"\r\n会议结束时间："+meetingVO.getEndTime()+"\r\n会议"
						+ "地点："+meetingVO.getPlace()+"\r\n现邀请您准时参加会议，您也可以在【行政管理】->【会议管理】->【我的会议】中对会议情况进行跟进，请知晓。");
				if(meetingVO.getPpCompanyIDs()!=null){
					pNoticeVO.setCompanys(ppCompanys.toString());
					pNoticeVO.setDepartments(ppDepartments.toString());
				}
				pNoticeVO.setType(2);
				Integer pNtcID =noticeService.saveNoticeVO(pNoticeVO);
				if(meetingVO.getPpIDs()!=null){
					for(int i=0;i<meetingVO.getPpIDs().length;i++){
						NoticeActorVO noticeActorVO = new NoticeActorVO();
						noticeActorVO.setUserID(meetingVO.getPpIDs()[i]);
						noticeActorVO.setNoticeID(pNtcID);
						noticeActorVO.setStatus(0);
						
						noticeActorService.saveNoticeActorVO(noticeActorVO);
					}
				}
				if(meetingVO.getPpCompanyIDs()!=null){
					for(int i=0;i<meetingVO.getPpCompanyIDs().length;i++){
					noticeActorService.saveNoticeListByIDs(meetingVO.getPpCompanyIDs()[i],meetingVO.getPpDepartmentIDs()[i],pNtcID);
					}
				}
				//给抄送人发送通知
				NoticeVO cNoticeVO = new NoticeVO();
				cNoticeVO.setCreatorID(user.getId());
				cNoticeVO.setNtcTitle("会议通知");
				cNoticeVO.setNtcContent("会议主题："+meetingVO.getTheme()+"\r\n发起人："+meetingVO.getSponsorName()
						+ "\r\n会议开始时间："+meetingVO.getBeginTime()+"\r\n会议结束时间："+meetingVO.getEndTime()+"\r\n会议"
						+ "地点："+meetingVO.getPlace()+"\r\n该会议抄送给您，可选择参加。您也可以在【行政管理】->【会议管理】->【我的会议】中对会议情况进行跟进，请知晓。");
				if(meetingVO.getCcCompanyIDs()!=null){
					cNoticeVO.setCompanys(ccCompanys.toString());
					cNoticeVO.setDepartments(ccDepartments.toString());
				}
				cNoticeVO.setType(2);
				Integer cNtcID = noticeService.saveNoticeVO(cNoticeVO);
				if(meetingVO.getCcIDs()!=null){
					for(int i=0;i<meetingVO.getCcIDs().length;i++){
						NoticeActorVO cNoticeActorVO = new NoticeActorVO();
						cNoticeActorVO.setUserID(meetingVO.getCcIDs()[i]);
						cNoticeActorVO.setNoticeID(cNtcID);
						cNoticeActorVO.setStatus(0);
						noticeActorService.saveNoticeActorVO(cNoticeActorVO);
					}
					
				}
				if(meetingVO.getCcCompanyIDs()!=null){
					for(int i=0;i<meetingVO.getCcCompanyIDs().length;i++){
					noticeActorService.saveNoticeListByIDs(meetingVO.getCcCompanyIDs()[i],meetingVO.getCcDepartmentIDs()[i],cNtcID);
					}
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "保存失败："+e.getMessage();
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
		
		return "saveMeeting";
	}	
	
	
	
	public String findMeetingList(){
		if(meetingVO==null){
			meetingVO=new MeetingVO();
		}
		try{
			User user = (User)request.getSession().getAttribute("user");
			String userID=user.getId();
			ListResult<MeetingVO> list= meetingService.findMeetingList(page, limit,userID);
			count=list.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
			totalPage = 1;
			request.setAttribute("meetingList", list.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = "launchMeeting";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		selectedPanel ="meetingList";
		return "findMeetingList";
	}
	
	
	
	
	public String getMeetingByMeetingID(){
		try{
			Integer meetingID=Integer.parseInt(request.getParameter("meetingID"));
			meetingVO= meetingService.getMeetingByMeetingID(meetingID);
		
		
			String[] downloadNames = StringUtils.split(meetingVO.getUploadNames(),",");
			request.setAttribute("downloadNames", downloadNames);
			request.setAttribute("meetingVO",meetingVO);
			String[] ccC=StringUtils.split(meetingVO.getCcCompanys(),",");
			String[] ccD=StringUtils.split(meetingVO.getCcDepartments(),",");
			String[] ppC=StringUtils.split(meetingVO.getPpCompanys(),",");
			String[] ppD=StringUtils.split(meetingVO.getPpDepartments(),",");
			groups = new ArrayList<>();
			if(ccC!=null){
				for(int i=0;i<ccC.length;i++){
					CompanyVO companyVO = positionService.getCompanyByCompanyID(Integer.parseInt(ccC[i]));
					String group="";
					if(!"null".equals(ccD[i])){
					   DepartmentVO departmentVO = positionService.getDepartmentByID(Integer.parseInt(ccD[i]));
					   group = companyVO.getCompanyName()+"-"+departmentVO.getDepartmentName();
					}else {
						group = companyVO.getCompanyName();
					}
					
					groups.add(group);
				}
			}
			request.setAttribute("groups",groups);
			
			groups1 = new ArrayList<>();
			if(ppC!=null){
				
				for(int i=0;i<ppC.length;i++){
					CompanyVO companyVO = positionService.getCompanyByCompanyID(Integer.parseInt(ppC[i]));
					String group="";
					if(!"null".equals(ppD[i])){
					   DepartmentVO departmentVO = positionService.getDepartmentByID(Integer.parseInt(ppD[i]));
					   group = companyVO.getCompanyName()+"-"+departmentVO.getDepartmentName();
					}else {
						group = companyVO.getCompanyName();
					}
					
					groups1.add(group);
				}
			}
			request.setAttribute("groups1",groups1);
			meetingActorVO1=meetingActorService.findMeetingActorVOByMeetingID(meetingID, 1);
			meetingActorVO2=meetingActorService.findMeetingActorVOByMeetingID(meetingID, 2);
			List<MeetingMinutesVO> meetingMinutesVOs = meetingMinutesService.getMeetingMinutesByMeetingID(meetingID);
			request.setAttribute("meetingActorVO1", meetingActorVO1);
			request.setAttribute("meetingActorVO2", meetingActorVO2);
			request.setAttribute("meetingMinutesVOs", meetingMinutesVOs);
			
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";			
		}
		selectedPanel ="meetingList";
		return "getMeetingByMeetingID";
	}
	
	
	
	public String saveMeetingMinutes(){
		InputStream in = null;
		OutputStream out = null;

		try{
			 meetingMinutesService.saveMMVO(meetingMinutesVO);
			 meetingVO = meetingService.getMeetingByMeetingID(meetingMinutesVO.getMeetingID());

			
 
            //保存文件
            if(upload!=null){
            	StringBuffer uploadFileNames=null;
            	if(!StringUtils.isBlank(meetingVO.getUploadNames())){
            		uploadFileNames=new StringBuffer(meetingVO.getUploadNames());
      	        }else{
      	        	uploadFileNames = new StringBuffer();
      	        }
            	
            	for(int i=0;i<upload.length;i++){
            		byte[] buffer = new byte[10*1024*1024];
            		int length = 0;
            		
            		
            		in=new FileInputStream(upload[i]);
            		File f=new File(Constants.MT_FILE_DIRECTORY);
            		if(!f.exists()){
            			f.mkdirs();
            		}
            		out=new FileOutputStream(new File(f,meetingVO.getMeetingID()+"_"+uploadFileName[i]));
            		
            		while((length=in.read(buffer, 0, buffer.length))!=-1){
            			out.write(buffer, 0, length);
            		}
            		uploadFileNames.append(uploadFileName[i]);
            		uploadFileNames.append(",");
            	}
            	
            	meetingVO.setUploadNames(uploadFileNames.toString());
            	meetingService.saveMeeting(meetingVO);
            }
            
				
			 
		}catch(Exception e){
			errorMessage = "保存失败："+e.getMessage();
			e.printStackTrace();
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
		selectedPanel ="meetingList";
		return "saveMeetingMinutes";
	}
	
	
	
	public String newMeetingMinutes(){
		try{
			Integer meetingID=Integer.parseInt(request.getParameter("meetingID"));
			meetingVO= meetingService.getMeetingByMeetingID(meetingID);
			request.setAttribute("meetingVO",meetingVO);
		}catch(Exception e){
			errorMessage = "查询失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";		
			
		}
		selectedPanel ="meetingList";
		return "newMeetingMinutes";
		
	}
	
	
	
	//下载文件
	public String download(){
		try{
			String dLDName = request.getParameter("dLDName");
			inputStream = new FileInputStream(new File(Constants.MT_FILE_DIRECTORY,dLDName));
			downloadFileFileName = dLDName;
		}catch(Exception e){
			errorMessage = "下载失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		
		return "download";
	}
	
	
	
	






}
