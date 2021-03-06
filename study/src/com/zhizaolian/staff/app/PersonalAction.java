package com.zhizaolian.staff.app;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.enums.APPResultEnum;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssignmentService;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.EmailService;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.SigninService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.AssignmentVO;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.EmailVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.NoticeVO;
import com.zhizaolian.staff.vo.ReimbursementVO;
import com.zhizaolian.staff.vo.ResignationVO;
import com.zhizaolian.staff.vo.SigninVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
import com.zhizaolian.staff.vo.WorkReportVO;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

public class PersonalAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	@Setter
	private String workReportVO;

	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private SigninService signinService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeActorService noticeActorService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private  ChopService chopService;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private WorkReportService workReportService;

	public void modifyPassword() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userID = request.getParameter("userID");
		User user = identityService.createUserQuery().userId(userID)
				.singleResult();
		if (user == null || user.getPassword() == null) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", "账号异常，修改失败！");
			printByJson(resultMap);
			return;
		}

		if (!user.getPassword().equals(request.getParameter("oldPassword"))) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", "原密码错误，修改失败！");
			printByJson(resultMap);
			return;
		}

		user.setPassword(request.getParameter("newPassword"));
		identityService.saveUser(user);
		resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		resultMap.put("message", APPResultEnum.SUCCESS.getName());
		printByJson(resultMap);
	}

	public void findTaskList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			List<Task> taskList = taskService.createTaskQuery()
					.taskAssignee(userID).orderByTaskCreateTime().desc()
					.listPage((page - 1) * limit, limit);
			List<TaskVO> taskVOs = processService.createTaskVOList(taskList);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("taskVOs", taskVOs);
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void auditVacation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			Task task = taskService.createTaskQuery().taskId(taskID)
					.singleResult();
			VacationTaskVO vacationVO = vacationService
					.getVacationTaskVOByTask(task);
			VacationVO vacation = vacationService
					.getVacationByProcessInstanceID(
							vacationVO.getProcessInstanceID());
			if (!StringUtils.isBlank(vacation.getAttachmentImage())) {
				/*vacationVO.setAttachmentUrl(Constants.APACHE_SERVER_URL_VACATION
						+ vacation.getAttachmentImage());*/
				String[] attachmentImages = vacation.getAttachmentImage().split("#&&#");
				List<byte[]> picList = new ArrayList<byte[]>();
				for(String attachment: attachmentImages){
					if(StringUtils.isNotBlank(attachment)){
						InputStream input = new FileInputStream(Constants.VACATION_FILE_DIRECTORY+attachment);
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						byte[] buffer = new byte[4096];
						int n = 0;
						while (-1 != (n = input.read(buffer))) {
							output.write(buffer, 0, n);
						}
						picList.add( output.toByteArray());
						input.close();
						output.close();
					}
				}
				vacationVO.setPicLst(picList);
			}

			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			List<CommentVO> comments = processService
					.getCommentsByProcessInstanceID(pInstance.getId());
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(pInstance.getId());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("vacationVO", vacationVO);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("comments", comments);
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void taskComplete() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			String userID = request.getParameter("userID");
			int result = Integer.parseInt(request.getParameter("result"));
			String comment = request.getParameter("comment");
			String businessType = request.getParameter("businessType");
			Task task = taskService.createTaskQuery().taskId(taskID)
					.singleResult();
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			// 完成任务
			if ("vacation_supervisor_audit"
					.equals(task.getTaskDefinitionKey())) {
				// 如果需要上级主管审批 那么 判断是否有上级主管
				Map<String, Object> keys = new HashMap<String, Object>();
				keys.put("needSuperSubject", 2);
				if (result == 3) {
					// 再上级 主管 就是 当前 人的上级主管
					String supervisor = staffService.querySupervisor(userID);
					if (StringUtils.isNotBlank(supervisor)) {
						keys.put("vacation_super_person", supervisor);
						keys.put("needSuperSubject", 1);
					}
				}
				// 1 代表 通过 有上级主管 3代表 通过无上级主管
				if (result == 3) {
					result = 1;
					keys.put("needSuperSubject", 2);
				}
				processService.completeTask(taskID, userID,
						TaskResultEnum.valueOf(result), comment, keys);
			} else {
				processService.completeTask(taskID, userID,
						TaskResultEnum.valueOf(result), comment);
			}
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(),
					TaskResultEnum.valueOf(result), businessType);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

	}
	public void getPicByUserId() {
		String userId = request.getParameter("userId");
		Picture pic = identityService.getUserPicture(userId);
		printByJson(pic);
	}
	public void auditReimbursement() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			Task task = taskService.createTaskQuery().taskId(taskID)
					.singleResult();
			ReimbursementVO reimbursementVO = reimbursementService
					.getReimbursementVOByTaskID(taskID);
			List<CommentVO> comments = processService.getComments(taskID);
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(pInstance.getId());
			List<Attachment> attas = taskService
					.getProcessInstanceAttachments(pInstance.getId());
			List<byte[]> picList = new ArrayList<byte[]>();
			List<Integer> indexList= new ArrayList<Integer>();
			int index=0;
			if (CollectionUtils.isNotEmpty(attas)) {
				for (Attachment attachment : attas) {
					InputStream input = taskService
							.getAttachmentContent(attachment.getId());
					String description=attachment.getDescription();
					int number=index;

					try{
						if(StringUtils.isNotBlank(description)){
							if (description.indexOf("_")!=-1) {
								number=Integer.parseInt(description.substring(0,1));
							}else{
								number=Integer.parseInt(description);
							}
						}
					}catch(Exception ignore){}
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					byte[] buffer = new byte[4096];
					int n = 0;
					while (-1 != (n = input.read(buffer))) {
						output.write(buffer, 0, n);
					}
					//					byte[] indexByte=intToByteArray(number);
					indexList.add(number);
					picList.add( output.toByteArray());
					input.close();
					output.close();
					index++;
				}
			} ;

			List<Group> groups = identityService.createGroupQuery()
					.groupMember(reimbursementVO.getRequestUserID()).list();
			if (groups.size() > 0) {
				String[] positionIDs = groups.get(0).getType().split("_");
				String companyName = CompanyIDEnum
						.valueOf(Integer.parseInt(positionIDs[0])).getName();
				String departmentName = positionService
						.getDepartmentByID(Integer.parseInt(positionIDs[1]))
						.getDepartmentName();
				resultMap.put("companyName", companyName);
				resultMap.put("departmentName", departmentName);
			}
			resultMap.put("reimbursementVO", reimbursementVO);
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("taskDefKey", task.getTaskDefinitionKey());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("picList", createReturnList(indexList,picList));
			//			resultMap.put("picList", picList);
			//			resultMap.put("indexList", indexList);
			printByJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

	}

	private List<List<byte[]>> createReturnList(List<Integer> indexList,List<byte[]> picContent){
		if(indexList==null||indexList.size()==0)return null;
		Map<Integer,List<byte[]>> result = new LinkedHashMap<>();	

		for(int i=0,n=indexList.size();i<n;i++){
			int xiabiao=indexList.get(i);
			byte[] content=picContent.get(i);
			List<byte[]> list = result.get(xiabiao);
			if(list==null){
				list=new ArrayList<>();
				list.add(content);
				result.put(xiabiao, list);
			}else{
				list.add(content);
			}
		}
		List<List<byte[]>> listnew= new ArrayList<>();
		for (Entry<Integer, List<byte[]>> entry : result.entrySet()) {
			listnew.add(entry.getValue());
		}
		return listnew;
	}
	//	private byte[] intToByteArray(final int integer) {
	//		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
	//		byte[] byteArray = new byte[4];
	//
	//		for (int n = 0; n < byteNum; n++)
	//		byteArray[3 - n] = (byte) (integer>>> (n * 8));
	//
	//		return (byteArray);
	//	}
	public void updateBankAccount() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			ReimbursementVO arg = (ReimbursementVO) runtimeService
					.getVariable(pInstance.getId(), "arg");
			arg.setCardName(request.getParameter("cardName"));
			arg.setBank(request.getParameter("bank"));
			arg.setCardNumber(request.getParameter("cardNumber"));
			runtimeService.setVariable(pInstance.getId(), "arg", arg);

			// 完成任务
			String userID = request.getParameter("userID");
			String comment = request.getParameter("comment");
			processService.completeTask(taskID, userID, null, comment);
			// 更新打款账号
			reimbursementService.updateBankAccountByUserID(arg.getPayeeID(),
					arg);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

	}

	public void findAttendanceDetail() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			AttendanceVO attendanceVO = new AttendanceVO();
			attendanceVO.setUserID(userID);
			ListResult<AttendanceVO> attendanceListResult = attendanceService
					.findAttendancePageListByAttendanceVO(attendanceVO, page,
							limit);
			Date now = new Date();
			int signin = signinService.findSingleByDateUserID(
					DateUtil.getSimpleDate(DateUtil.formateDate(now)), userID);
			resultMap.put("attendanceVOList", attendanceListResult.getList());
			resultMap.put("signin", signin);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void signin() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			SigninVO signin = new SigninVO();
			signin.setUserID(userID);
			Date now = new Date();
			signin.setSigninDate(DateUtil.formateDate(now));
			signinService.saveSignin(signin);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

	}

	public void showInformation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			StaffVO staffVO = staffService.getStaffByUserID(userID);
			User usr = identityService.createUserQuery().userId(userID)
					.singleResult();
			staffVO.setStaffNumber(usr.getFirstName());
			Picture pic = identityService.getUserPicture(userID);
			if (pic != null) {
				staffVO.setPicArray(pic.getBytes());
			}
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("staffVO", staffVO);
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void findGroupDetails() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			List<GroupDetailVO> groupDetailVOs = staffService
					.findGroupDetailsByUserID(userID);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("groupDetailVOs", groupDetailVOs);
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void updateInformation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			/*String userID = request.getParameter("userID");
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID(userID);
			staffVO.setTelephone(request.getParameter("telephone"));
			staffVO.setEmergencyContract(
					request.getParameter("emergencyContract"));
			staffVO.setEmergencyPhone(request.getParameter("emergencyPhone"));
			staffVO.setAddress(request.getParameter("address"));
			staffService.updateStaffInformation(staffVO);*/
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void findMessageList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			ListResult<NoticeVO> messageListResult = noticeService
					.findNoticesByUserID(userID, limit, page);
			resultMap.put("messageList", messageListResult.getList());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void readMessage() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			int ntcID = Integer.parseInt(request.getParameter("ntcID"));
			NoticeVO noticeVO = noticeService.getNoticeByNtcID(ntcID);
			noticeActorService.updateNoticeActor(ntcID, userID);
			resultMap.put("noticeVO", noticeVO);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void findNoticeList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			NoticeVO noticeVO = new NoticeVO();
			noticeVO.setType(1); // 不要硬编码，使用枚举
			ListResult<NoticeVO> noticeListResult = noticeService
					.findNoticeList1(noticeVO, limit, page);
			resultMap.put("noticeList", noticeListResult.getList());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	@Getter
	@Setter
	private InputStream attachment;
	@Getter
	@Setter
	private String attachmentName;
	public void downloadVideo() {
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			attachment = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(new File(commonAttachment.getSoftURL())
					.getName().getBytes("gbk"), "iso-8859-1");
			attachmentName = attachmentName.substring(32);
			processRequest(attachmentName, commonAttachment.getSoftURL(),
					request, ServletActionContext.getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public String downloadPic(){
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			attachment = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(new File(commonAttachment.getSoftURL())
					.getName().getBytes("gbk"), "iso-8859-1");
			attachmentName = attachmentName.substring(32);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadPic";
	}
	public String downloadCommon() {
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			attachment = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(new File(commonAttachment.getSoftURL())
					.getName().getBytes("gbk"), "iso-8859-1");
			attachmentName = attachmentName.substring(32);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadAttchment";
	}
	private static final Pattern RANGE_PATTERN = Pattern
			.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");

	private void processRequest(String fileName, String filePath,
			final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		Path video = Paths.get(filePath);

		int length = (int) Files.size(video);
		int start = 0;
		int end = length - 1;

		String range = request.getHeader("Range");
		range = range == null ? "" : range;
		Matcher matcher = RANGE_PATTERN.matcher(range);

		if (matcher.matches()) {
			String startGroup = matcher.group("start");
			start = startGroup.isEmpty() ? start : Integer.valueOf(startGroup);
			start = start < 0 ? 0 : start;

			String endGroup = matcher.group("end");
			end = endGroup.isEmpty() ? end : Integer.valueOf(endGroup);
			end = end > length - 1 ? length - 1 : end;
		}

		int contentLength = end - start + 1;

		response.reset();
		response.setBufferSize(1024 * 16);
		response.setHeader("Content-Disposition",
				String.format("inline;filename=\"%s\"", fileName));
		response.setHeader("Accept-Ranges", "bytes");
		response.setDateHeader("Last-Modified",
				Files.getLastModifiedTime(video).toMillis());
		response.setDateHeader("Expires",
				System.currentTimeMillis() + 1000 * 60 * 60 * 24);
		response.setContentType(Files.probeContentType(video));
		response.setHeader("Content-Range",
				String.format("bytes %s-%s/%s", start, end, length));
		response.setHeader("Content-Length",
				String.format("%s", contentLength));
		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

		int bytesRead;
		int bytesLeft = contentLength;
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 16);

		try (SeekableByteChannel input = Files.newByteChannel(video,
				java.nio.file.StandardOpenOption.READ);
				OutputStream output = response.getOutputStream()) {
			input.position(start);
			while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
				buffer.clear();
				output.write(buffer.array(), 0,
						bytesLeft < bytesRead ? bytesLeft : bytesRead);
				bytesLeft -= bytesRead;
			}
		}catch (Exception ignore) {
			StringWriter sw = new StringWriter(); 
			ignore.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	public void readNotice() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int ntcID = Integer.parseInt(request.getParameter("ntcID"));
			NoticeVO noticeVO = noticeService.getNoticeByNtcID(ntcID);
			resultMap.put("noticeVO", noticeVO);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			resultMap.put("attach", noticeService
					.getCommonAttachmentByFID(noticeVO.getNtcID(), AttachmentType.NOTICE));
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void messageHomePage() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			int taskTotalCount = (int) taskService.createTaskQuery()
					.taskAssignee(userID). 	count();
			int formalCount = (int)taskService.createTaskQuery().taskAssignee(userID).processDefinitionKey("Formal").count();
			int certificateCount = (int)taskService.createTaskQuery().taskAssignee(userID).processDefinitionKey("CertificateBorrow").count();
			int changeContractCount = (int)taskService.createTaskQuery().taskAssignee(userID).processDefinitionKey("ChangeContract").count();
			int bankAccountCount = (int)taskService.createTaskQuery().taskAssignee(userID).processDefinitionKey("bankAccount").count();
			int taskCount = taskTotalCount - formalCount - certificateCount - changeContractCount - bankAccountCount;
			int unReadMessageCount = noticeService
					.countUnReadNoticeByUserID(userID);
			Date now = new Date();
			int signin = signinService.findSingleByDateUserID(
					DateUtil.getSimpleDate(DateUtil.formateDate(now)), userID);
			NoticeVO noticeVO = noticeService.getLatestNotice();
			resultMap.put("taskCount", taskCount);
			resultMap.put("unReadMessageCount", unReadMessageCount);
			resultMap.put("signin", signin);
			resultMap.put("latestNotice",
					noticeVO == null ? "" : noticeVO.getNtcTitle());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void auditEmail() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			EmailVO emailVO = emailService.getEmailVOByTaskID(taskID);
			List<CommentVO> comments = processService.getComments(taskID);
			ProcessInstance processInstance = processService
					.getProcessInstance(taskID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(
							processInstance.getId());
			resultMap.put("emailVO", emailVO);
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void auditAssignment() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			Task task = taskService.createTaskQuery().taskId(taskID)
					.singleResult();
			AssignmentVO assignmentVO = assignmentService
					.getAssignmentVOByTaskID(taskID);
			List<CommentVO> comments = processService.getComments(taskID);
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(pInstance.getId());
			resultMap.put("taskDefKey", task.getTaskDefinitionKey());
			resultMap.put("assignmentVO", assignmentVO);
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void confirmAssignment() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			String userID = request.getParameter("userID");
			int result = Integer.parseInt(request.getParameter("result"));
			String comment = request.getParameter("comment");
			String beginDate = request.getParameter("beginDate");

			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			if (result == TaskResultEnum.RECEIVE.getValue()
					&& !StringUtils.isBlank(beginDate)) {
				// 设置任务的开始时间
				assignmentService.updateBeginDate(pInstance.getId(),
						DateUtil.getFullDate(beginDate));
			}
			// 完成确认
			processService.completeTask(taskID, userID,
					TaskResultEnum.valueOf(result), comment);
			// 更新OA_Assignment表的流程节点状态processStatus
			assignmentService.updateProcessStatus(pInstance.getId(),
					TaskResultEnum.valueOf(result));
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void auditResignation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			Task task = taskService.createTaskQuery().taskId(taskID)
					.singleResult();
			ResignationVO resignationVO = resignationService
					.getResignationVOByTaskID(taskID);
			StaffVO staffVO = staffService
					.getStaffByUserID(resignationVO.getRequestUserID());
			if (staffVO != null) {
				List<GroupDetailVO> groups = staffService
						.findGroupDetailsByUserID(staffVO.getUserID());
				List<String> groupList = Lists2.transform(groups,
						new SafeFunction<GroupDetailVO, String>() {
					@Override
					protected String safeApply(GroupDetailVO input) {
						return input.getCompanyName() + "—"
								+ input.getDepartmentName() + "—"
								+ input.getPositionName();
					}
				});
				resultMap.put("groupList", groupList);
			}
			List<CommentVO> comments = processService.getComments(taskID);
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(pInstance.getId());
			resultMap.put("resignationVO", resignationVO);
			resultMap.put("taskDefKey", task.getTaskDefinitionKey());
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void resignationConfirm() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String taskID = request.getParameter("taskID");
			String userID = request.getParameter("userID");
			int result = Integer.parseInt(request.getParameter("result"));
			String comment = request.getParameter("comment");
			String confirmLeaveDate = request.getParameter("confirmLeaveDate");
			ProcessInstance pInstance = processService
					.getProcessInstance(taskID);
			if (result == TaskResultEnum.AGREE.getValue()
					&& !StringUtils.isBlank(confirmLeaveDate)) {
				// 确认员工的离职日期
				Task task = taskService.createTaskQuery().taskId(taskID)
						.singleResult();
				resignationService.confirmLeaveDate(pInstance.getId(),
						DateUtil.getSimpleDate(confirmLeaveDate),
						task.getTaskDefinitionKey());
			}
			// 完成任务
			processService.completeTask(taskID, userID,
					TaskResultEnum.valueOf(result), comment);
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(),
					TaskResultEnum.valueOf(result),
					BusinessTypeEnum.RESIGNATION.getName());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void findProcessList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			int type = Integer.parseInt(request.getParameter("type"));
			switch (BusinessTypeEnum.valueOf(type)) {
			case VACATION :
				ListResult<VacationVO> vaListResult = vacationService
				.findVacationListByUserID(userID, page, limit);
				resultMap.put("vacationVOs", vaListResult.getList());
				break;
			case ASSIGNMENT :
				ListResult<AssignmentVO> asListResult = assignmentService
				.findAssignmentListByUserID(userID, page, limit);
				resultMap.put("assignmentVOs", asListResult.getList());
				break;
			case RESIGNATION :
				ListResult<ResignationVO> reListResult = resignationService
				.findResignationListByUserID(userID, page, limit);
				resultMap.put("resignationVOs", reListResult.getList());
				break;
			case REIMBURSEMENT :
				ListResult<ReimbursementVO> rbListResult = reimbursementService
				.findReimbursementListByUserID(userID, page, limit);
				resultMap.put("reimbursementVOs", rbListResult.getList());
				break;
			case CHOP_BORROW:
				ListResult<ChopBorrrowVo> chopList =chopService.findChopBorrrowListByUserID(userID, page, limit);
				resultMap.put("chopVos", chopList.getList());
				break;
			case ADVANCE:
				ListResult<AdvanceVo> advanceList = reimbursementService
				.findAdvanceListByUserID(userID, page, limit);
				resultMap.put("reimbursementVOs", advanceList.getList());
				break;
			default :
				break;
			}

			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	public void getReimnursementPic(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String instanceId=request.getParameter("instanceId");
		Task task = taskService.createTaskQuery()
				.processInstanceId(instanceId).singleResult();
		if (task != null) {
			try{
				List<Attachment> attas = taskService
						.getProcessInstanceAttachments(instanceId);
				List<byte[]> picList = new ArrayList<byte[]>();
				List<Integer> indexList= new ArrayList<Integer>();
				int index=0;
				if (CollectionUtils.isNotEmpty(attas)) {
					for (Attachment attachment : attas) {
						InputStream input = taskService
								.getAttachmentContent(attachment.getId());
						String description=attachment.getDescription();
						int number=index;

						try{
							if(StringUtils.isNotBlank(description)){
								if (description.indexOf("_")!=-1) {
									number=Integer.parseInt(description.substring(0,1));
								}else{
									number=Integer.parseInt(description);
								}
							}
						}catch(Exception ignore){}
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						byte[] buffer = new byte[4096];
						int n = 0;
						while (-1 != (n = input.read(buffer))) {
							output.write(buffer, 0, n);
						}
						indexList.add(number);
						picList.add( output.toByteArray());
						input.close();
						output.close();
						index++;
					}
				} ;
				resultMap.put("picList", createReturnList(indexList,picList));
			}catch(IOException e){
				e.printStackTrace();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
			}
		}
		printByJson(resultMap);
	}
	public void processHistory() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String processInstanceID = request
					.getParameter("processInstanceID");
			List<CommentVO> comments = processService
					.getCommentsByProcessInstanceID(processInstanceID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(processInstanceID);
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	public void processHistoryForVacation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String processInstanceID = request
					.getParameter("processInstanceID");
			VacationTaskVO vacationVO = vacationService
					.getVacationTaskVOByTask(processInstanceID);
			VacationVO vacation = vacationService
					.getVacationByProcessInstanceID(
							vacationVO.getProcessInstanceID());
			if (!StringUtils.isBlank(vacation.getAttachmentImage())) {
				String[] attachmentImages = vacation.getAttachmentImage().split("#&&#");
				List<byte[]> picList = new ArrayList<byte[]>();
				for(String attachment: attachmentImages){
					if(StringUtils.isNotBlank(attachment)){
						InputStream input = new FileInputStream(Constants.VACATION_FILE_DIRECTORY+attachment);
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						byte[] buffer = new byte[4096];
						int n = 0;
						while (-1 != (n = input.read(buffer))) {
							output.write(buffer, 0, n);
						}
						picList.add( output.toByteArray());
						input.close();
						output.close();
					}
				}
				vacationVO.setPicLst(picList);
			}
			List<CommentVO> comments = processService
					.getCommentsByProcessInstanceID(processInstanceID);
			List<TaskVO> finishedTaskVOs = processService
					.findFinishedTasksByProcessInstanceID(processInstanceID);
			resultMap.put("comments", comments);
			resultMap.put("finishedTaskVOs", finishedTaskVOs);
			resultMap.put("vacationVO", vacationVO);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	public void findWorkReportList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			WorkReportDetailVO workReportDetailVO = new WorkReportDetailVO();
			workReportDetailVO.setUserID(userID);
			ListResult<WorkReportDetailVO> listResult = workReportService
					.findWorkReportListByUserID(workReportDetailVO, page,
							limit);
			resultMap.put("workReportDetailVOs", listResult.getList());
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void getWorkReportDetail() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userID = request.getParameter("userID");
			String reportDate = request.getParameter("reportDate");
			List<WorkReportVO> workReportVOs = workReportService
					.findWorkReportByDateAndUserID(reportDate, userID);
			List<Group> groups = identityService.createGroupQuery()
					.groupMember(userID).list();
			List<String> groupList = Lists2.transform(groups,
					new SafeFunction<Group, String>() {
				@Override
				protected String safeApply(Group input) {
					String[] positionIDs = input.getType().split("_");
					String companyName = CompanyIDEnum
							.valueOf(Integer.parseInt(positionIDs[0]))
							.getName();
					String departmentName = positionService
							.getDepartmentByID(
									Integer.parseInt(positionIDs[1]))
							.getDepartmentName();
					String positionName = positionService
							.getPositionByPositionID(
									Integer.parseInt(positionIDs[2]))
							.getPositionName();
					return companyName + " — " + departmentName + " — "
					+ positionName;
				}
			});
			resultMap.put("workReportVOs", workReportVOs);
			resultMap.put("groupList", groupList);
			resultMap.put("reportDate", reportDate);
			String userName = staffService.getStaffByUserID(userID).getLastName();
			if(staffService.isPartner(userID)){
				userName =  "【合】"+userName;
			}
			resultMap.put("userName",userName);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void findStaffByName() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try { 
			String name = request.getParameter("name");
			List<StaffVO> staffVOs = staffService.findStaffByName(name);
			resultMap.put("staffVOs", staffVOs);
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	private final static 	String[] WEEK_OF_DAYS = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	public void saveWorkReport() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			@SuppressWarnings("static-access")
			JSONObject obj = new JSONObject().fromObject(workReportVO);
			WorkReportVO workReportVO = (WorkReportVO) JSONObject.toBean(obj,
					WorkReportVO.class);
			WorkReportDetailVO workReportDetailVO = new WorkReportDetailVO();
			workReportDetailVO.setUserID(workReportVO.getUserID());
			workReportDetailVO.setReportDate(workReportVO.getReportDate());

			String userId=workReportVO.getUserID();
			String reportDate=null;
			String weekDay=null;
			List<GroupDetailVO> groupDetailVOs=staffService.findGroupDetailsByUserID(userId);
			Calendar calendar=Calendar.getInstance();
			if(CollectionUtils.isNotEmpty(groupDetailVOs)){
				try{
					GroupDetailVO groupDetailVO = groupDetailVOs.get(0);
					String companyIDString = groupDetailVO.getCompanyID()+"";
					//String times=CompanyIDEnum.valueOf(groupDetailVO.getCompanyID()).getTimeLimitByDate(null);
					//String sb_time=times.split(" ")[0];
					String departmentId = groupDetailVO.getDepartmentID()+"";
					Date now = new Date();
					String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(now));
					String nowDay=DateUtil.formateDate(now);
					Date sbDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(nowDay+" "+workTimeArray[0]);
					if(now.compareTo(sbDate)<0){
						calendar.add(Calendar.DATE, -1);
						int week=calendar.get(Calendar.DAY_OF_WEEK);
						weekDay=WEEK_OF_DAYS[week-1];
						reportDate=DateUtil.getDayStr(calendar.getTime());
					}else{
						reportDate=nowDay;
						int week=calendar.get(Calendar.DAY_OF_WEEK);
						weekDay=WEEK_OF_DAYS[week-1];
					}
					workReportVO.setReportDate(reportDate);
					workReportVO.setWeekDay(weekDay);
					workReportDetailVO.setReportDate(reportDate);
				}catch(Exception ignore){
					ignore.printStackTrace();
					StringWriter sw = new StringWriter(); 
					ignore.printStackTrace(new PrintWriter(sw, true)); 
					logger.error(sw.toString());
				};

			}


			ListResult<WorkReportDetailVO> listResult = workReportService
					.findWorkReportListByUserID(workReportDetailVO, 0, 1);
			workReportDetailVO.getWeekDay();

			if (listResult.getTotalCount() != 0) {
				resultMap.put("result", APPResultEnum.RESUBMIT.getValue());
				resultMap.put("message", APPResultEnum.RESUBMIT.getName());
				printByJson(resultMap);
			} else {
				workReportService.saveWorkReport(workReportVO);
				resultMap.put("result", APPResultEnum.SUCCESS.getValue());
				resultMap.put("message", APPResultEnum.SUCCESS.getName());
				printByJson(resultMap);
			}
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}

	public void startVacation() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BufferedOutputStream stream = null;
		try {
			List<File> fileLst = new ArrayList<>();
			List<String> fileNameLst = new ArrayList<>();
			String vacation = request.getParameter("vacationVO");
			@SuppressWarnings("static-access")
			JSONObject obj = new JSONObject().fromObject(vacation);
			VacationVO vacationVO = (VacationVO) JSONObject.toBean(obj,
					VacationVO.class);
			//检查是否请了小时假，若是，不通过
			if(vacationService.checkVacation(vacationVO)){
				resultMap.put("result", APPResultEnum.ERROR.getValue());
				resultMap.put("message", "无法请小时假，请假以半天结算");
				printByJson(resultMap);
				return;
			}
			String type = request.getParameter("type");
			//如果type不为空，说明是安卓
			if(StringUtils.isNotBlank(type)){
				int index = Integer.parseInt(type);
				for(int i=0; i<index; i++){
					File file = ((MultiPartRequestWrapper)request).getFiles("pic"+i)[0];
					fileLst.add(file);
					fileNameLst.add(i+"v.jpg");
				}
			}else{
				String[] attachments = request.getParameterMap().get("attachment[]");
				int index = 0;
				if(null != attachments){
					for(String attach: attachments){
						if (!StringUtils.isEmpty(attach)) {
							BASE64Decoder decoder = new BASE64Decoder();
							byte[] attachmentBytes = decoder.decodeBuffer(attach);
							String fileName = index+"v.jpg";
							File file = new File(fileName);
							FileOutputStream fstream = new FileOutputStream(file);
							stream = new BufferedOutputStream(fstream);
							stream.write(attachmentBytes);
							fileLst.add(file);
							fileNameLst.add(fileName);
							index++;
							//vacationVO.setAttachment(file);
							//vacationVO.setAttachmentFileName("v.jpg");
						}
					}
				}
			}
			if(fileLst.size()>0 && fileNameLst.size()>0){
				vacationService.startVacation(vacationVO, fileLst.toArray(new File[fileLst.size()]), fileNameLst.toArray(new String[fileNameLst.size()]));
			}else{
				vacationService.startVacation(vacationVO, null, null);
			}
			resultMap.put("result", APPResultEnum.SUCCESS.getValue());
			resultMap.put("message", APPResultEnum.SUCCESS.getName());
			printByJson(resultMap);
		} catch (Exception e) {
			resultMap.put("result", APPResultEnum.ERROR.getValue());
			resultMap.put("message", e.getMessage());
			printByJson(resultMap);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	@Getter
	@Setter
	private InputStream vacationPicture;
	@Getter
	@Setter
	private Integer index;
	@Setter
	@Getter
	private String downloadFileFileName;
	public String getVacationAttachmentAll() {
		String taskID = request.getParameter("taskID");
		ProcessInstance processInstance = processService
				.getProcessInstance(taskID);
		if (processInstance != null) {
			List<Attachment> attachments = taskService
					.getProcessInstanceAttachments(processInstance.getId());
			if (index == null)
				index = 0;
			if (attachments.size() > 0) {
				vacationPicture = taskService
						.getAttachmentContent(attachments.get(index).getId());
				downloadFileFileName = attachments.get(index).getName();
			}
		}
		return "vacationAttachmentAll";
	}

	public void getSalaryList(){
		//String userId=request.getParameter("userId");
		//List<SalaryDetailEntity> list=staffService.getSalarys(userId);
		//app端开发暂停，工资显示有问题，先传空数据
		List<SalaryDetailEntity> list = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		printByJson(resultMap);
	}
	public void getRealDate(){
		String realDate = DateUtil.formateDate(new Date());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("realDate", realDate);
		resultMap.put("result", APPResultEnum.SUCCESS.getValue());
		printByJson(resultMap);
	}
}
