package com.zhizaolian.staff.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.APPResultEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.UserRightCenterService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Getter;
import lombok.Setter;

public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private StaffService staffService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private IdentityService identityService;
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	private String userName;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private UserRightCenterService userRightCenterService;
	@Autowired
	private NoticeService noticeService;
	@Getter
	private InputStream inputStream;

	public void login() {
		//result, message, userID, userName, List<String> permissionCodes;
		Map<String, Object> result = new HashMap<String, Object>();
		User user;
		try {
			user = staffService.loginValidate(userName, password);
			StaffVO staff = staffService.getStaffByUserID(user.getId());

			if (user.getLastName().equals(staff.getLastName())) {
				result.put("result", APPResultEnum.FIRST_LOGIN.getValue());  //首次登录
				printByJson(result);
				return;
			}

			List<String> permissionCodes = permissionService.findPermissionsByUserID(user.getId());
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			String companyIDString = groups.get(0).getType().split("_")[0];
			//CompanyIDEnum companyID = CompanyIDEnum.valueOf(Integer.parseInt(companyIDString));
			//double dailyHours = positionService.getDailyHoursByCompanyID(companyID);
			/*	String beginTime = positionService.getBeginTimeByCompanyID(companyID);
			String endTime = positionService.getEndTimeByCompanyID(companyID);*/
			String departmentId = groups.get(0).getType().split("_")[1];
			double dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));

			//String workTimes = companyID.getTimeLimitByDate(null);
			//String[] workTimeArray = workTimes.split(" ");
			String beginTime = " " + workTimeArray[0] + ":00";;
			String endTime = " " + workTimeArray[3] + ":00";;
			result.put("result", APPResultEnum.SUCCESS.getValue());
			result.put("message", APPResultEnum.SUCCESS.getName());
			result.put("userID", user.getId());
			result.put("staffNumber", user.getFirstName());
			result.put("userName", staff.getLastName());
			result.put("permissionCodes", permissionCodes);
			result.put("dailyHours", dailyHours);
			result.put("beginTime", beginTime);
			result.put("endTime", endTime);
			printByJson(result);
		} catch (Exception e) {
			result.put("result", APPResultEnum.ERROR.getValue());  //登录失败
			result.put("message", e.getMessage());
			printByJson(result);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

	}
	@Setter
	private String unionid;
	@Setter
	private String appId;
	@Setter
	private String access_token;
	public void checkAccount(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String appSecret = userRightCenterService.getAppSecret(appId);
			String paramStr = unionid+appId+appSecret;
			String access_token = DigestUtils.md5Hex(paramStr);
			//校验密钥
			if(access_token.equals(this.access_token)){
				StaffEntity staff = staffService.getStaffByUnionid(unionid);
				if(null != staff){
					//检查是否授予该用户应用权限
					if(userRightCenterService.checkHasAppRight(appId, staff.getUserID())){
						Map<String, Object> staffInfoMap = new HashMap<>();
						staffInfoMap.put("userID", staff.getUserID());
						staffInfoMap.put("staffName", staff.getStaffName());
						staffInfoMap.put("gender", staff.getGender());
						staffInfoMap.put("birthday", staff.getBirthday());
						staffInfoMap.put("telephone", staff.getTelephone());
						staffInfoMap.put("idNumber", staff.getIdNumber());
						staffInfoMap.put("address", staff.getAddress());
						staffInfoMap.put("headImgUrl", "http://www.zhizaolian.com:9090/app/showImage?id="+staff.getHeadImgId());
						//com需要返回菜单id以及编码、权限编码（父子关系）
						if(Constants.COM_APP_ID.equals(appId)){
							Object permissionInfo = userRightCenterService.findAllPermissionShips(staff.getUserID(), appId);
							result.put("permissionInfo", permissionInfo);
						}else{
							List<Object> permissionInfo = userRightCenterService.findAllPermissionCodes(staff.getUserID(), appId);
							result.put("permissionInfo", permissionInfo);
						}
						result.put("staffInfo", staffInfoMap);
						result.put("result", 0);//0表示成功
					}else{
						result.put("result", 1);
						result.put("msg", "未授予应用权限");
					}
				}else{
					result.put("result", 1);
					result.put("msg", "未注册账号");
				}
			}else{
				result.put("result", 1);
				result.put("msg", "access_token校验不通过");
			}
		} catch (Exception e) {
			result.put("result", 1);
			result.put("msg", "系统异常："+e.getMessage());
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(result);
	}
	public String showImage(){
		String id = request.getParameter("id");
		CommonAttachment commonAttachment = noticeService
				.getCommonAttachmentById(Integer.parseInt(id));
		String attachmentPath = commonAttachment.getSoftURL();
		try {
			inputStream = new FileInputStream(new File(attachmentPath)) ;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showImage";
	}
}
