package com.zhizaolian.staff.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.entity.AttendanceDetailEntity;
import com.zhizaolian.staff.entity.MonthlyRestEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.UserMonthlyRestEntity;
import com.zhizaolian.staff.entity.WorkRestArrangeEntity;
import com.zhizaolian.staff.entity.WorkRestTimeEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EmailSender;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.SigninVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.VacationDetailVo;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.WorkOvertimeVo;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

public class AttendanceAction extends BaseAction {

	@Getter
	private double dailyHours;
	@Getter
	private String beginTime;
	@Getter
	private String endTime;
	@Autowired
	private StaffService staffService;

	@Getter
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
	@Setter
	private Integer result; // 任务处理结果
	@Getter
	private Integer totalCount = 0;
	@Getter
	@Setter
	private Integer flag;
	@Setter
	private File uploadFile;
	@Setter
	@Getter
	private AttendanceVO attendanceVO = new AttendanceVO();
	@Setter
	@Getter
	private List<SigninVO> noSignStatistics;
	@Setter
	@Getter
	private VacationVO vacationVO = new VacationVO();
	@Setter
	@Getter
	private String searchDate;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String attachmentContentType;
	@Setter
	@Getter
	private String[] attachmentFileName;
	@Setter
	@Getter
	private ByteArrayInputStream excelFile;
	@Setter
	@Getter
	private String excelFileName;

	@Autowired
	private IdentityService identityService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private WorkOvertimeService workOvertimeService;

	private static final long serialVersionUID = 1L;

	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}

	public String vacationStatistics() {
		try {
			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			String userName = request.getParameter("userName");
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			List<VacationVO> vacationVOs = vacationService.findVacationsByCompanyAndDate(companyID, new Date());
			ListResult<VacationVO> statistics = vacationService.findStatisticsPageListByCompanyAndMonth(companyID,
					userName, new Date(), page, limit);
			count = statistics.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			request.setAttribute("userName", userName);
			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("vacationVOs", vacationVOs);
			request.setAttribute("statisticList", statistics.getList());
			request.setAttribute("count", vacationVOs.size());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "统计失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "vacationManagement";
		return "vacationManagement";
	}

	public String attendanceDetail() {
		try {
			if (!StringUtils.isBlank(attendanceVO.getName())) {
				// 中文姓名解码
				String trans = new String(attendanceVO.getName().getBytes("ISO-8859-1"), "UTF-8");
				String name = URLDecoder.decode(trans, "UTF-8");
				attendanceVO.setName(name);
			}

			ListResult<AttendanceVO> attendanceVOListResult = attendanceService
					.findAttendancePageListByAttendanceVO(attendanceVO, page, limit);
			request.setAttribute("attendanceVOList", attendanceVOListResult.getList());
			count = attendanceVOListResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "考勤明细查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		if (attendanceVO != null && attendanceVO.getCompanyID() != null) {
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(attendanceVO.getCompanyID());
			if (departmentVOs != null && attendanceVO.getDepartmentID() != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = attendanceVO.getDepartmentID();
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
		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}

	public String exportAttendanceDetail() {
		try {
			if (!StringUtils.isBlank(attendanceVO.getName())) {
				String trans = new String(attendanceVO.getName().getBytes("ISO-8859-1"), "UTF-8");
				String name = URLDecoder.decode(trans, "UTF-8");
				attendanceVO.setName(name);
			}
			XSSFWorkbook workbook = attendanceService.exportAttendancePageListByAttendanceVO(attendanceVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("考勤明细.xlsx".getBytes(), "ISO8859-1");
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出excel失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportAttendanceDetail";
	}

	public String upload() {
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}

	public String uploadDetail() {
		int companyID = Integer.parseInt(request.getParameter("companyID"));
		String fileName = CompanyIDEnum.valueOf(companyID).getName() + "_" + DateUtil.formateDate(new Date()) + "_"
				+ UUID.randomUUID().toString();
		File target = new File(Constants.ATTENDANCE_FILE_DIRECTORY, fileName);
		StringBuffer index = new StringBuffer();
		try {
			FileUtils.copyFile(uploadFile, target);
			attendanceService.parseExcel(fileName, companyID, index);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "文件上传解析失败：" + "第"+index+"行数据异常，请检查或者联系系统管理员，异常原因："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "uploadDetail";
	}


	public String salaryContentUpload(){

		String fileName =  DateUtil.formateDate(new Date()) + "_"
				+ UUID.randomUUID().toString();
		File file=new File(Constants.SALARY_FILE_DIRECTORY);
		if(!file.exists()){
			file.mkdirs();
		}
		File target = new File(Constants.SALARY_FILE_DIRECTORY, fileName);
		try {
			FileUtils.copyFile(uploadFile, target);
			attendanceService.parseSalary(fileName, Integer.parseInt(request.getParameter("year")),Integer.parseInt(request.getParameter("month")));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "uploadDetail";

	}

	public String salaryUpload(){
		selectedPanel = "salaryUpload";
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		//上个月
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		request.setAttribute("year", calendar.get(Calendar.YEAR));
		request.setAttribute("month", calendar.get(Calendar.MONTH)+1);
		return "salaryUpload";
	}



	public String salarySend(){
		selectedPanel = "salarySend";
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		request.setAttribute("year", calendar.get(Calendar.YEAR));
		request.setAttribute("month", calendar.get(Calendar.MONTH)+1);
		return "salarySend";
	}

	/**
	 * 查询当年当月份工资条录入情况
	 * 返回 总共多少员工 共录入了多少条 有多少人已经录入了工资条信息
	 * @return
	 */
	public void salaryDetail(){
		String year=request.getParameter("year");
		String month=request.getParameter("month");
		if(month.startsWith("0")){
			month=month.substring(1,2);
		}
		String sendType=request.getParameter("sendType");
		if("1".equals(sendType)){
			printByJson(staffService.getSalaryMobileDetail(year, month));
		}else{
			printByJson(staffService.getSalaryEmailDetail(year, month));
		}
	}

	public void toSendSalary(){
		String year=request.getParameter("year");
		String month=request.getParameter("month");
		if(month.startsWith("0")){
			month=month.substring(1,2);
		}
		String sendType=request.getParameter("sendType");
		if("1".equals(sendType)){
			sendMobile(year,month);
		}else{
			sendEmail(year,month);
		}
		//确认
		attendanceService.confirmSalary(year, month);
	}


	private final static String[] TITLES={"姓名","基础工资","绩效奖金",
			"绩效提成","补贴","满勤","应抵扣金额",
			"其他扣除","迟到扣除","扣养老","扣医保"
			,"扣失业","大病","扣住房公积金","扣个税","实发工资"
	};
	private void sendEmail(String year,String month){
		List<Object> list=staffService.getUnPostEmails(year, month);
		String title="工资条发放提醒";
		EmailSender emailSender=EmailSender.getInstance();
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			if(StringUtils.isBlank(objs[1]+"")){
				continue;
			}else{
				try{
					String content="<h3>"+year+"年"+month+"月份工资条信息已发送至您的邮箱，请及时查看！如果疑问，请联系人事部胡经理或登录oa系统提出异议 ！</h3>";
					String userName=staffService.getRealNameByUserId(objs[0]+"");
					List<String> salaryDetail=staffService.getItemMonthSalaryResult(objs[0]+"",year,month);
					StringBuilder sb = new StringBuilder("<table style=\"border:1px solid #000\">");
					sb.append("<tr><td style=\"border:1px solid #000\">");
					sb.append(StringUtils.join(TITLES,"</td><td style=\"border:1px solid #000\">"));
					sb.append("</td></tr>");
					sb.append("<tr>");
					sb.append("<td style=\"border:1px solid #000\" >");
					sb.append(userName);
					sb.append("</td><td style=\"border:1px solid #000\">");
					sb.append(StringUtils.join(salaryDetail,"</td ><td style=\"border:1px solid #000\">"));
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</table>");
					emailSender.sendEmail(objs[1]+"",title,content+sb.toString());
					staffService.completeEmail(year, month, objs[0]+"");
				}catch(Exception e){
					e.printStackTrace();
					StringWriter sw = new StringWriter(); 
					e.printStackTrace(new PrintWriter(sw, true)); 
					logger.error(sw.toString());
				}
			}
		}
	}


	private void sendMobile(String year,String month){
		List<Object> list=staffService.getUnPostMobiles(year, month);
		ShortMsgSender shortMsgSender=ShortMsgSender.getInstance();
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			if(StringUtils.isBlank(objs[1]+"")||objs[1]==null){
				continue;
			}else{
				try{
					StaffVO staffVO = staffService.getStaffByUserID(objs[0]+"");
					String name = "";
					if("男".equals(staffVO.getGender())){
						name = staffVO.getLastName()+"先生";
					}else{
						name = staffVO.getLastName()+"女士";
					}
					String content="【智造链】 尊敬的"+name+"，您的"+year+"年"+month+"月的工资条，已发送至oa系统，请您及时登录确认。";
					shortMsgSender.send(objs[1]+"",content);
					staffService.completeMobile(year, month, objs[0]+"");
				}catch(Exception e){
					e.printStackTrace();
					StringWriter sw = new StringWriter(); 
					e.printStackTrace(new PrintWriter(sw, true)); 
					logger.error(sw.toString());
				}
			}
		}
	}
	public String salaryShow(){
		selectedPanel = "salaryShow";
		String year=request.getParameter("year");
		String month=request.getParameter("month");
		String type=request.getParameter("type");
		String name=request.getParameter("name");

		request.setAttribute("name", name);
		request.setAttribute("type", type);
		String emailSend= request.getParameter("emailSend");
		String mobileSend=request.getParameter("mobileSend");
		request.setAttribute("emailSend", emailSend);
		request.setAttribute("mobileSend", mobileSend);

		Calendar calendar=Calendar.getInstance();
		Integer yearInteger=null;
		Integer monthInteger=null;
		if(StringUtils.isBlank(year)){
			yearInteger=calendar.get(Calendar.YEAR);
		}else{
			yearInteger=Integer.parseInt(year);
		}

		if(StringUtils.isBlank(month)){
			monthInteger=calendar.get(Calendar.MONTH)+1;
		}else{
			monthInteger=Integer.parseInt(month);
		}
		request.setAttribute("year", yearInteger);
		request.setAttribute("month", monthInteger);
		ListResult<SalaryDetailEntity> lists=attendanceService.getSalarysListByKey(name, emailSend,mobileSend,yearInteger, monthInteger,type, page, limit);
		count=lists.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page-1)*limit);
		request.setAttribute("lists", lists.getList());
		return "salaryShow";
	}

	public String vacationManagement() {
		try {
			ListResult<VacationVO> vacationVOPageList = attendanceService.findVacationPageListByVacationVO(vacationVO,
					page, limit);
			request.setAttribute("vacations", vacationVOPageList.getList());
			count = vacationVOPageList.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if(vacationVO.getBeginDate()==null){
				Calendar calendar = Calendar.getInstance();
				calendar.add(calendar.MONTH, -3);
				Date beforeDate = calendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String defaultStartDate = sdf.format(beforeDate);
				vacationVO.setBeginDate(defaultStartDate);
			}
			if (vacationVO != null && vacationVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyID(vacationVO.getCompanyID());
				if (departmentVOs != null && vacationVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = vacationVO.getDepartmentID();
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

		} catch (Exception e) {
			errorMessage = "查询请假列表失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "vacationManagement";
		return "vacationManagement";
	}

	public String deleteVacation() {
		try {
			int vacationID = Integer.parseInt(request.getParameter("vacationID"));
			VacationVO vacationVO = vacationService.findVacationByVacationID(vacationID);
			vacationService.deleteVacationByVacationID(vacationID);
			// 根据请假记录检查相关考勤统计数据
			attendanceService.checkAttendanceDetailsByVacationVO(vacationVO);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());

		}

		return "deleteVacation";

	}

	public String loadVacation() {

		int vacationID = Integer.parseInt(request.getParameter("vacationID"));

		try {
			VacationVO vacationVO = vacationService.findVacationByVacationID(vacationID);
			request.setAttribute("vacation", vacationVO);
			String requestUserId = "";
			if(Constants.DEP.equals(vacationVO.getType())){
				String[] vacationUserIds = vacationVO.getRequestUserID().split(",");
				requestUserId = vacationUserIds[0];
			}else{
				requestUserId = vacationVO.getRequestUserID();
			}
			List<Group> groups = identityService.createGroupQuery().groupMember(requestUserId).list();
			String companyIDString = groups.get(0).getType().split("_")[0];
			String departmentId = groups.get(0).getType().split("_")[1];

			//	CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyIDString));
			//	dailyHours = positionService.getDailyHoursByCompanyID(companyID);
			dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, 
					DateUtil.formateDate(DateUtil.getFullDate(vacationVO.getBeginDate())));
			/*			beginTime = positionService.getBeginTimeByCompanyID(companyID);
			endTime = positionService.getEndTimeByCompanyID(companyID);*/
			//String workTimes = companyID.getTimeLimitByDate(null);
			//String[] workTimeArray = workTimes.split(" ");
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, 
					DateUtil.formateDate(DateUtil.getFullDate(vacationVO.getBeginDate())));
			beginTime = " " + workTimeArray[0] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
			String attachmentImageStr = vacationVO.getAttachmentImage();
			if(StringUtils.isNotBlank(attachmentImageStr)){
				String[] attachmentImage = attachmentImageStr.split("#&&#");
				request.setAttribute("attachmentImage", attachmentImage);
			}
		} catch (Exception e) {
			errorMessage = "获取员工所在分公司作息时间失败！";
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "vacationManagement";
		return "updateVacation";
	}
	@Autowired
	private TaskService taskService;
	public String updateVacation() {
		InputStream in = null;
		OutputStream out = null;
		try {
			String uploadMode = request.getParameter("uploadMode");
			VacationVO vacation = vacationService.findVacationByVacationID(vacationVO.getVacationID());
			String[] vacationTextAndHours = vacationService.getVacationTextAndHoursForHR(vacationVO.getUserID(), vacationVO.getBeginDate(), vacationVO.getEndDate());
			vacationVO.setShowHours(Double.parseDouble(vacationTextAndHours[1]));
			int index = 0;
			String fileNames = "";
			//判断是否是流程
			String processInstanceId = vacationService.getProcessInstanceId(vacationVO.getVacationID());
			if(StringUtils.isNotBlank(processInstanceId)){
				List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				for(Task task: tasks){
					VacationVO vacationVo = (VacationVO) taskService.getVariable(task.getId(), "arg");
					vacationVO.setTitle(vacationVo.getTitle());
					vacationVO.setBusinessType(vacation.getBusinessType());
					taskService.setVariable(task.getId(), "arg", vacationVO);
				}
			}
			if(null != attachment){
				//判断是否是流程
				if(StringUtils.isNotBlank(processInstanceId)){
					if(Constants.REPLACE.equals(uploadMode)){
						List<Attachment> attas = taskService
								.getProcessInstanceAttachments(processInstanceId);
						//删除附件，以便覆盖
						for(Attachment atta: attas){
							taskService.deleteAttachment(atta.getId());
						}
					}
				}
				for(File file: attachment){
					byte[] buffer = new byte[10 * 1024 * 1024];
					int length = 0;
					String attachmentFileName = UUID.randomUUID().toString().replaceAll("-", "")
							+ this.attachmentFileName[index].substring(this.attachmentFileName[index].indexOf("."));
					if(index==0){
						fileNames += attachmentFileName;
					}else{
						fileNames += "#&&#"+attachmentFileName;
					}
					in = new FileInputStream(file);
					File f = new File(Constants.VACATION_FILE_DIRECTORY);
					if (!f.exists()) {
						f.mkdirs();
					}
					out = new FileOutputStream(new File(Constants.VACATION_FILE_DIRECTORY, attachmentFileName));

					while ((length = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, length);
					}
					index++;
					//判断是否是流程
					if(StringUtils.isNotBlank(processInstanceId)){
						InputStream is = new FileInputStream(file);
						taskService.createAttachment("picture", "", processInstanceId, "vacation picture", "WeChat screenshots", is);
					}
				}
			}
			if(StringUtils.isNotBlank(fileNames)){
				if(Constants.ADD.equals(uploadMode)){
					String attachmentImage = vacation.getAttachmentImage();
					if(StringUtils.isNotBlank(attachmentImage)){
						fileNames += "#&&#"+attachmentImage;
					}
				}
				vacationVO.setAttachmentImage(fileNames);
			}
			vacationService.updateVacation(vacationVO);
			// 根据请假记录检查相关考勤统计数据
			attendanceService.checkAttendanceDetailsByVacationVO(vacation);
			attendanceService.checkAttendanceDetailsByVacationVO(vacationVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "修改请假失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}

		return "vacationList";
	}

	public String newVacation() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyIDString));
			//dailyHours = positionService.getDailyHoursByCompanyID(companyID);
			/*	beginTime = positionService.getBeginTimeByCompanyID(companyID);
			endTime = positionService.getEndTimeByCompanyID(companyID);*/
			//String workTimes = companyID.getTimeLimitByDate(null);
			//String[] workTimeArray = workTimes.split(" ");
			//存在多个职位，以总部的职位优先
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyIDString = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			beginTime = " " + workTimeArray[0] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		request.setAttribute("objectType", request.getParameter("objectType"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		selectedPanel = "vacationManagement";
		return "newVacation";
	}

	public String saveVacation() {
		InputStream in = null;
		OutputStream out = null;

		try {
			int index = 0;
			String fileNames = "";
			if(null != attachment){
				for(File file: attachment){
					byte[] buffer = new byte[10 * 1024 * 1024];
					int length = 0;
					String attachmentFileName = UUID.randomUUID().toString().replaceAll("-", "") + this.attachmentFileName[index];
					if(index==0){
						fileNames += attachmentFileName;
					}else{
						fileNames += "#&&#"+attachmentFileName;
					}
					in = new FileInputStream(file);
					File f = new File(Constants.VACATION_FILE_DIRECTORY);
					if (!f.exists()) {
						f.mkdirs();
					}
					out = new FileOutputStream(new File(Constants.VACATION_FILE_DIRECTORY, attachmentFileName));

					while ((length = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, length);
					}
					index++;
				}
			}
			if(StringUtils.isNotBlank(fileNames)){
				vacationVO.setAttachmentImage(fileNames);
			}
			vacationService.saveVacation(vacationVO);
			// 根据请假记录检查相关考勤统计数据
			attendanceService.checkAttendanceDetailsByVacationVO(vacationVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "新增请假失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}

		return "vacationList";
	}
	public void checkAnnualVacation(){
		String requestUserID = request.getParameter("requestUserID");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		Map<String, String> resultMap = new HashMap<>();
		try {
			//检查是否可以休年假
			String checkResult = vacationService.checkMeetConditions(requestUserID, beginDate, endDate);
			resultMap.put("result", checkResult);
		} catch (Exception e) {
			resultMap.put("result", e.getMessage());
			e.printStackTrace();
		}
		printByJson(resultMap);
	}
	public void checkMarriageHoliday(){
		Map<String, Object> resultMap = new HashMap<>();
		String requestUserId = request.getParameter("userId");
		resultMap.put("hasMarriageHoliday", vacationService.hasMarriageHoliday(requestUserId));
		printByJson(resultMap);
	}
	public String signinStatistics() {
		try {

			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			List<SigninVO> signinVOs = attendanceService.findSigninsByDateAndCompanyID(companyID, searchDate);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("signinVOs", signinVOs);
			request.setAttribute("count", signinVOs.size());
			request.setAttribute("companyVOs", companyVOs);
		} catch (Exception e) {
			errorMessage = "统计签到情况失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "signinStatistics";
		return "signinStatistics";
	}

	public String findSignByMonth() {
		try {
			Integer companyID = Integer.parseInt(request.getParameter("companyID"));
			String searchMonth = request.getParameter("searchMonth");
			noSignStatistics = attendanceService.findSignByMonthAndCompanyID(companyID,
					DateUtil.getSimpleDate(searchMonth + "-01"));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "findSignByMonth";
		}
		return "findSignByMonth";
	}

	public String sendCompanyAttendanceStatistics() {
		try {
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			request.setAttribute("companyVOs", companyVOs);
			if(null == totalPage) totalPage=1;
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}

	public String findAttendanceStatisticsList() {
		try {
			List<CompanyVO> companyVOs = positionService.findAllCompanys();

			if (attendanceVO != null && attendanceVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyID(attendanceVO.getCompanyID());
				if (departmentVOs != null && attendanceVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = attendanceVO.getDepartmentID();
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
			String name = attendanceVO.getName();
			if (name != null) {
				String trans = new String(name.getBytes("ISO-8859-1"), "UTF-8");
				name = URLDecoder.decode(trans, "UTF-8");
				attendanceVO.setName(name);
			}
			ListResult<AttendanceVO> attendanceVOs = attendanceService.findAttendanceStatistics(attendanceVO, page,
					limit);
			count = attendanceVOs.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("attendanceVOs", attendanceVOs.getList());
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}

	public String exportAttendanceStatisticsMsg() {
		try {
			attendanceVO.setName(URLDecoder.decode(attendanceVO.getName(),"utf-8"));
			XSSFWorkbook workbook = attendanceService.exportAttendanceMsg(attendanceVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("考勤统计明细.xlsx".getBytes(), "ISO8859-1");
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出excel失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportAttendanceStatisticsMsg";
	}
	@Getter
	private InputStream inputStream;
	public String showImage(){
		String vacationImage = request.getParameter("vacationImage");
		try {
			inputStream = new FileInputStream(new File(Constants.VACATION_FILE_DIRECTORY, vacationImage)) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showImage";
	}
	@Setter
	@Getter
	private String userId;
	@Setter
	@Getter
	private String userName;
	@Setter
	@Getter
	private String companyId;
	@Setter
	@Getter
	private String departmentId;
	@Setter
	@Getter
	private String beginDate;
	@Setter
	@Getter
	private String endDate;
	@Setter
	@Getter
	private String status;
	@Setter
	@Getter
	private String name_;
	@Setter
	@Getter
	private String assignee;

	public String workOvertimeList(){
		try {
			//查询条件
			String[] conditions = {userId, companyId, departmentId, beginDate, endDate, status , name_, assignee};
			ListResult<WorkOvertimeVo> workOvertimeListResult = workOvertimeService.findWorkOvertimeListByCondition(conditions,
					page, limit);
			request.setAttribute("workOvertimeVos", workOvertimeListResult.getList());
			count = workOvertimeListResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (StringUtils.isNotBlank(companyId)) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyID(Integer.parseInt(companyId));
				if (departmentVOs != null && StringUtils.isNotBlank(departmentId)) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int _departmentId = Integer.parseInt(departmentId);
					while (_departmentId != 0) {
						selectedDepartmentIDs.add(0, _departmentId);
						for (DepartmentVO departmentVO : departmentVOs) {
							if (departmentVO.getDepartmentID() == _departmentId) {
								departmentId = departmentVO.getParentID()+"";
								_departmentId = Integer.parseInt(departmentId);
							}
						}
					}
					request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
				}
				request.setAttribute("departmentVOs", departmentVOs);
			}
			request.setAttribute("companyVOs", companyVOs);

			request.setAttribute("userId", userId);
			request.setAttribute("userName", userName);
			request.setAttribute("companyId", companyId);
			request.setAttribute("departmentId", departmentId);
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
		} catch (Exception e) {
			errorMessage = "查询加班列表失败：" + e.toString();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "workOvertimeList";
		return "workOvertimeList";
	}
	public String lateAndLeaveList(){
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		String companyId = request.getParameter("companyId");
		if(StringUtils.isNotBlank(companyId)){
			request.setAttribute("lateObjs", attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LATE));
			//request.setAttribute("leaveObjs", attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LEAVE));
			//默认显示登录人的所属公司
		}else{
			User user = (User) request.getSession().getAttribute("user");
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
			if (!CollectionUtils.isEmpty(groups)) {
				//存在多个职位，以总部的职位优先
				GroupDetailVO group = null;
				for(GroupDetailVO _group: groups){
					group = _group;
					if(CompanyIDEnum.QIAN.getValue() == group.getCompanyID()){
						break;
					}
				}
				companyId = String.valueOf(group.getCompanyID());
				request.setAttribute("lateObjs", attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LATE));
				//request.setAttribute("leaveObjs", attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LEAVE));
			}
		}
		request.setAttribute("companyId", companyId);
		selectedPanel = "lateAndLeaveList";
		return "lateAndLeaveList";
	}
	public void changeLateStatus(){
		String attendanceId = request.getParameter("attendanceId");
		attendanceService.updateLateStatus(attendanceId);
		Map<String, String> result = new HashMap<>();
		result.put("result", "true");
		printByJson(result);
	}
	/**
	 * 获取每日的请假明细
	 * @return
	 */
	public String vacationDetail(){
		try {
			String companyId = request.getParameter("companyId");
			List<VacationDetailVo> vacationDetailVos = vacationService.getVacationDetailObjs(companyId);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			count = vacationDetailVos.size();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page-1)*limit);
			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("vacationDetailVos", ActionUtil.page(page, limit, vacationDetailVos));
			request.setAttribute("allVacationDetailVos", JSONArray.fromObject(vacationDetailVos).toString());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("companyId", companyId);
		selectedPanel = "vacationManagement";
		return "vacationManagement";
	}
	@Setter
	@Getter
	private String allVacationDetailVos;
	public String exportVacationDetail(){
		try {
			JSONArray json = JSONArray.fromObject(allVacationDetailVos);
			@SuppressWarnings("unchecked")
			List<VacationDetailVo> vacationDetailVos = (List<VacationDetailVo>)JSONArray.toCollection(json, VacationDetailVo.class);
			inputStream = vacationService.exportVacationDetail(vacationDetailVos);
			excelFileName = new String(("请假明细-"+DateUtil.getTodayString()+".xls").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "exportVacationDetail";
	}
	@Setter
	@Getter
	private WorkRestTimeEntity workRestTime;

	public String workTimeSet(){
		String workRestTimeId = request.getParameter("workRestTimeId");
		if(StringUtils.isNotBlank(workRestTimeId)){
			WorkRestTimeEntity workRestTime = attendanceService.getWorkRestTime(workRestTimeId);
			if(null != workRestTime){
				request.setAttribute("workRestTime", workRestTime);
			}
			request.setAttribute("workRestTimeId", workRestTimeId);
		}
		List<WorkRestTimeEntity> workRestTimeList = attendanceService.getWorkRestTimeList();
		request.setAttribute("workRestTimeList", workRestTimeList);
		selectedPanel = "workTimeArrange";
		return "workTimeArrange";
	}
	public void checkWorkRestName(){
		String workRestName = request.getParameter("workRestName");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("exist", attendanceService.checkWorkRestName(workRestName));
		printByJson(resultMap);
	}
	public String saveWorkRestTime(){
		attendanceService.saveWorkRestTime(workRestTime, request.getParameter("workRestTimeId"));
		return "render_workTimeSet";
	}
	public String deleteWorkRestTime(){
		String workRestTimeId = request.getParameter("workRestTimeId");
		attendanceService.deleteWorkRestTime(workRestTimeId);
		return "render_workTimeSet";
	}
	public String deleteWorkRestArrange(){
		String workRestArrangeId = request.getParameter("workRestArrangeId");
		attendanceService.deleteWorkRestArrange(workRestArrangeId);
		return "render_workTimeArrange";
	}
	public String workTimeArrange(){
		ListResult<WorkRestArrangeEntity> workRestArranges = attendanceService.getWorkTimeArranges(limit, page);
		request.setAttribute("workRestArranges", workRestArranges.getList());
		count = workRestArranges.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		if (StringUtils.isNotBlank(companyId)) {
			List<DepartmentVO> departmentVOs = positionService
					.findDepartmentsByCompanyID(Integer.parseInt(companyId));
			if (departmentVOs != null && StringUtils.isNotBlank(departmentId)) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int _departmentId = Integer.parseInt(departmentId);
				while (_departmentId != 0) {
					selectedDepartmentIDs.add(0, _departmentId);
					for (DepartmentVO departmentVO : departmentVOs) {
						if (departmentVO.getDepartmentID() == _departmentId) {
							departmentId = departmentVO.getParentID()+"";
							_departmentId = Integer.parseInt(departmentId);
						}
					}
				}
				request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
			}
			request.setAttribute("departmentVOs", departmentVOs);
		}
		List<WorkRestTimeEntity> workRestTimeList = attendanceService.getWorkRestTimeList();
		request.setAttribute("workRestTimeList", workRestTimeList);
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("companyId", companyId);
		request.setAttribute("departmentId", departmentId);
		selectedPanel = "workTimeArrange";
		return "workTimeArrange";
	}
	public String saveWorkArrange(){
		String workRestId = request.getParameter("workRestId");
		String companyId = request.getParameter("companyId");
		String departmentId = request.getParameter("departmentId");
		String beginTime = request.getParameter("beginTime");
		WorkRestArrangeEntity workRestArrange = new WorkRestArrangeEntity();
		workRestArrange.setWorkRestId(workRestId);
		workRestArrange.setCompanyId(companyId);
		workRestArrange.setBeginTime(beginTime);
		if(StringUtils.isNotBlank(departmentId)){
			workRestArrange.setDepartmentId(departmentId);
		}
		attendanceService.saveWorkArrange(workRestArrange);
		return "render_workTimeArrange";
	}
	//	public void checkWorkTimeArrange(){
	//		String departmentId = request.getParameter("departmentId");
	//		String companyId = request.getParameter("companyId");
	//		Map<String, Object> resultMap = new HashMap<>();
	//		resultMap.put("exist", attendanceService.checkWorkTimeArrange(departmentId, companyId));
	//		printByJson(resultMap);		
	//	}
	public void calVacationTime(){
		String requestUserId = request.getParameter("userID");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		try {
			requestUserId = requestUserId.split(",")[0];
			String[] vacationTextAndHours = vacationService.getVacationTextAndHoursForHR(requestUserId, beginTime, endTime);
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("vacationTextAndHours", vacationTextAndHours);
			printByJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	public void checkWorkRestIsArranged(){
		String workRestId = request.getParameter("workRestId");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("arranged", attendanceService.checkWorkRestIsArranged(workRestId));
		printByJson(resultMap);		
	}
	public void getAttachmentNames(){
		Map<String, Object> resultMap = new HashMap<>();
		String vacationId = request.getParameter("vacationId");
		VacationVO vacationVO = vacationService.findVacationByVacationID(Integer.parseInt(vacationId));
		String attachmentImageStr = vacationVO.getAttachmentImage();
		if(StringUtils.isNotBlank(attachmentImageStr)){
			String[] attachmentImage = attachmentImageStr.split("#&&#");
			resultMap.put("attachments", attachmentImage);
			resultMap.put("exist", true);
		}else{
			resultMap.put("exist", false);
		}
		printByJson(resultMap);	
	}
	@Setter
	@Getter
	private MonthlyRestEntity monthlyRest;
	public String monthlyRestDaySet(){
		List<MonthlyRestEntity> monthlyRests = attendanceService.getMonthlyRests();
		String staffName = request.getParameter("staffName");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isBlank(year)){
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		if(StringUtils.isBlank(month)){
			month = String.valueOf(cal.get(Calendar.MONTH)+1);
		}
		ListResult<UserMonthlyRestEntity> userMonthlyRests = attendanceService.getUserMonthlyRests(staffName, year, month, limit, page);
		int count = userMonthlyRests.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage == 0) totalPage=1;
		totalCount = userMonthlyRests.getTotalCount();
		request.setAttribute("monthlyRests", monthlyRests);
		request.setAttribute("userMonthlyRests", userMonthlyRests.getList());
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("staffName", staffName);
		selectedPanel = "workTimeArrange";
		return "workTimeArrange";
	}
	public String saveMonthlyRestDay(){
		String month = monthlyRest.getMonth();
		if(month.startsWith("0")){
			month = month.substring(1, 2);
			monthlyRest.setMonth(month);
		}
		attendanceService.saveMonthlyRestDay(monthlyRest);
		return "render_monthlyRestDaySet";
	}
	public void checkMonthExist(){
		Map<String, Object> resultMap = new HashMap<>();
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		if(month.startsWith("0")){
			month = month.substring(1, 2);
		}
		resultMap.put("exist", attendanceService.checkMonthExist(year, month));
		printByJson(resultMap);
	}
	public String deleteRest(){
		String restId = request.getParameter("restId");
		attendanceService.deleteRest(restId);
		return "render_monthlyRestDaySet";
	}
	public String findAbnormalAttendanceDatas(){
		String date = request.getParameter("date");
		String staffName = request.getParameter("staffName");
		if(StringUtils.isBlank(date)){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Date lastDate = cal.getTime();
			date = DateUtil.formateDate(lastDate);
		}
		try {
			List<Object> abnormalAttendanceDatas = attendanceService.findAbnormalAttendanceDatas(date, staffName);
			count = abnormalAttendanceDatas.size();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("abnormalAttendanceDatas", ActionUtil.page(page, limit, abnormalAttendanceDatas));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		request.setAttribute("date", date);
		request.setAttribute("staffName", staffName);
		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}
	public String newVacationForAbnormal(){
		String userId = request.getParameter("userId");
		String date = request.getParameter("date");
		request.setAttribute("date", date);
		try {
			List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
			//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyIDString));
			//dailyHours = positionService.getDailyHoursByCompanyID(companyID);
			/*	beginTime = positionService.getBeginTimeByCompanyID(companyID);
			endTime = positionService.getEndTimeByCompanyID(companyID);*/
			//String workTimes = companyID.getTimeLimitByDate(null);
			//String[] workTimeArray = workTimes.split(" ");
			//存在多个职位，以总部的职位优先
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyIDString = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			beginTime = " " + workTimeArray[0] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(userId);
		request.setAttribute("staff", staff);
		User user = (User) request.getSession().getAttribute("user");
		StaffEntity loginUser = staffService.getStaffByUserId(user.getId());
		request.setAttribute("userName", loginUser.getStaffName());
		selectedPanel = "attendanceAbnormal";
		return "newVacationForAbnormal";
	}
	public void getWorkTime(){
		Map<String, String> resultMap = new HashMap<>();
		String userId = request.getParameter("userId");
		String date = request.getParameter("date");
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		//存在多个职位，以总部的职位优先
		Group group = null;
		for(Group _group: groups){
			group = _group;
			String[] posList = group.getType().split("_");
			if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
				break;
			}
		}
		String companyIDString = group.getType().split("_")[0];
		String departmentId = group.getType().split("_")[1];
		try {
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, date);
			beginTime = workTimeArray[0];
			endTime = workTimeArray[3];
			resultMap.put("beginTime", beginTime);
			resultMap.put("endTime", endTime);
			printByJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	@Getter
	private String attendDate;
	public String addAttendanceTime(){
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String attendDate = request.getParameter("attendDate");
		String attendUserId = request.getParameter("attendUserId");
		String note = request.getParameter("note");
		AttendanceDetailEntity attend = new AttendanceDetailEntity();
		attend.setAttendanceDate(DateUtil.getSimpleDate(attendDate));
		attend.setAttendanceTime(beginTime+" "+endTime);
		attend.setNote(note);
		attend.setUserID(attendUserId);
		try {
			attendanceService.addAttendanceTime(attend);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		this.attendDate = attendDate;
		return "render_abnormalAttendanceDatas";
	}
	public String exportPartnerAttendDatas(){
		try {
			XSSFWorkbook workbook = attendanceService.exportPartnerAttendDatas();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("合伙人2017年考勤统计.xlsx".getBytes(), "ISO8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "exportPartnerAttendDatas";
	}
	public void findVacationUsers(){
		String vacationId = request.getParameter("vacationId");
		List<String> vacationUsers = vacationService.findVacationUsers(vacationId);
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("vacationUsers", StringUtils.join(vacationUsers, ","));
		printByJson(resultMap);
	}
	/**
	 * 检查设置的月份是否有周末跨月（月初是否是周日，月末是否是周六）
	 */
	public void checkMonthlyWeekend(){
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Date date = sdf.parse(year+"-"+month);
			int monthInt = DateUtil.getMonth(date);
			//该月天数
			int days = DateUtil.getMonthLastDay(Integer.parseInt(year), monthInt);
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date firstDayDate = sdf.parse(year+"-"+month+"-01");
			Date lastDayDate = sdf.parse(year+"-"+month+"-"+days);
			String firstDayWeek = DateUtil.getWeekDay(firstDayDate);
			String lastDayWeek = DateUtil.getWeekDay(lastDayDate);
			if("日".equals(firstDayWeek) || "六".equals(lastDayWeek)){
				resultMap.put("check", false);
			}else{
				resultMap.put("check", true);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("error", true);
		}
		printByJson(resultMap);
	}
	public void modifyUserMonthlyRest(){
		String id = request.getParameter("id");
		String restDays = request.getParameter("restDays");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			attendanceService.modifyUserMonthlyRest(id, restDays);
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			resultMap.put("success", false);
		}
		printByJson(resultMap);
	}
}
