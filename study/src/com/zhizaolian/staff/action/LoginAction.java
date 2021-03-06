package com.zhizaolian.staff.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.zhizaolian.staff.entity.AppInfoEntity;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.NicknameStatusEnum;
import com.zhizaolian.staff.enums.NicknameTypeEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.UserRightCenterService;
import com.zhizaolian.staff.service.VersionInfoService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.utils.DesUtil;
import com.zhizaolian.staff.utils.HttpClientUtil;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.NicknameVO;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

public class LoginAction extends BaseAction {

	@Getter
	@Setter
	private StaffVO staffVO;
	@Getter
	@Setter
	private int status;

	@Getter
	@Setter
	private String errorMessage;
	@Getter
	private List<NicknameVO> nicknameVOs;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ViewReportService viewReportService;
	@Autowired
	private VersionInfoService versionInfoService;
	@Getter
	@Setter
	private String showVersionNotice;
	@Getter
	@Setter
	private String firstLogin;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private UserRightCenterService userRightCenterService;

	private static final long serialVersionUID = 1L;

	public String index() {
		if (!StringUtils.isEmpty(errorMessage)) {
			try {
				errorMessage = URLDecoder.decode(errorMessage, "utf-8");
			} catch (Exception e) {
				errorMessage = e.getMessage();
				return "exception-error";
			}
		}
		try {
			String code = request.getParameter("code");
			// 微信扫码登录
			if (StringUtils.isNotBlank(code)) {
				// 通过code获取access_token
				String accessTokenUrl = Constants.WX_INTERFACE_URL + "oauth2/access_token?appid=" + Constants.APP_ID
						+ "&secret=" + Constants.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
				String responseResult = HttpClientUtil.doGet(accessTokenUrl);
				if (StringUtils.isNotBlank(responseResult)) {
					JSONObject resultObj = JSONObject.fromObject(responseResult);
					if (resultObj.has("errmsg")) {
						errorMessage = "获取access_token失败";
					} else {
						String access_token = resultObj.getString("access_token");
						String openid = resultObj.getString("openid");
						// 判断是否是微信扫码绑定账号跳转而来
						User user_ = (User) request.getSession().getAttribute("user");
						if (null != user_) {
							// 绑定openId
							staffService.updateStaffOpenId(user_.getId(), openid);
							// 获取微信头像并保存到数据库
							String userInfoUrl = Constants.WX_INTERFACE_URL + "userinfo?access_token=" + access_token
									+ "&openid=" + openid;
							responseResult = HttpClientUtil.doGet(userInfoUrl);
							if (StringUtils.isNotBlank(responseResult)) {
								resultObj = JSONObject.fromObject(responseResult);
								if (resultObj.has("headimgurl")) {
									// 微信头像url
									String headImgUrl = resultObj.getString("headimgurl");
									if (StringUtils.isNotBlank(headImgUrl)) {
										int headImgId = savePicFromURL(headImgUrl, Constants.STAFF_WX_HEAD_IMG);
										// 检查是否已保存微信头像，若有，则更新
										String headImgID = staffService.getStaffHeadImg(user_.getId());
										if (null != headImgID) {
											CommonAttachment atta = noticeService
													.getCommonAttachmentById(Integer.parseInt(headImgID));
											// 删除服务器微信头像
											FileUtils.forceDelete(new File(atta.getSoftURL()));
										}
										staffService.updateStaffHeadImgId(user_.getId(), headImgId);
										ServletActionContext.getContext().getSession().put("headImgId", headImgId);
									}
									//用户unionId
									String unionid = resultObj.getString("unionid");
									staffService.updateStaffUnionId(user_.getId(), unionid);
								}
							}
							firstLogin = "true";
							return "loginSuccess";
						}
						// 根据opendid查找对应的oa人员
						StaffVO staff = staffService.getStaffByOpenId(openid);
						if (null == staff) {
							request.setAttribute("bind", false);
						} else {
							// 获取微信头像并保存到数据库
							String userInfoUrl = Constants.WX_INTERFACE_URL + "userinfo?access_token=" + access_token
									+ "&openid=" + openid;
							responseResult = HttpClientUtil.doGet(userInfoUrl);
							if (StringUtils.isNotBlank(responseResult)) {
								resultObj = JSONObject.fromObject(responseResult);
								if (resultObj.has("headimgurl")) {
									// 微信头像url
									String headImgUrl = resultObj.getString("headimgurl");
									if (StringUtils.isNotBlank(headImgUrl)) {
										int headImgId = savePicFromURL(headImgUrl, Constants.STAFF_WX_HEAD_IMG);
										// 检查是否已保存微信头像，若有，则更新
										String headImgID = staffService.getStaffHeadImg(staff.getUserID());
										if (null != headImgID) {
											CommonAttachment atta = noticeService
													.getCommonAttachmentById(Integer.parseInt(headImgID));
											// 删除服务器微信头像
											FileUtils.forceDelete(new File(atta.getSoftURL()));
										}
										staffService.updateStaffHeadImgId(staff.getUserID(), headImgId);
										ServletActionContext.getContext().getSession().put("headImgId", headImgId);
									}
								}
							}
							User user;
							StaffVO staffVo;
							try {
								user = staffService.loginValidate(staff.getNickName(), staff.getPassword());
								staffVo = staffService.getStaffByUserID(user.getId());
							} catch (Exception e) {
								errorMessage = e.getMessage();
								StringWriter sw = new StringWriter();
								e.printStackTrace(new PrintWriter(sw, true));
								logger.error(sw.toString());
								return "loginFail";
							}

							ServletActionContext.getContext().getSession().put("user", user);
							List<String> permissions = permissionService.findPermissionsByUserID(user.getId());
							if (permissions.contains(Constants.VIEW_WORK_REPORT)) {
								viewReportService.checkPermission(permissions, user.getId());
							}
							ServletActionContext.getContext().getSession().put("permissions", permissions);

							if (user.getLastName().equals(staffVo.getLastName())) {
								return "firstLogin";
							}
							// 判断版本发布的通告是否显示
							if (versionInfoService.checkVersionNoticeShow(user.getId())) {
								showVersionNotice = "true";
							} else {
								showVersionNotice = "false";
							}
							firstLogin = "true";
							return "loginSuccess";
						}
					}
				} else {
					errorMessage = "获取access_token失败";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "index";
	}

	/*
	 * public String login() { User user; StaffVO staff; try { user =
	 * staffService.loginValidate(staffVO.getUserName(), staffVO.getPassword());
	 * staff = staffService.getStaffByUserID(user.getId()); } catch (Exception
	 * e) { errorMessage = e.getMessage(); StringWriter sw = new StringWriter();
	 * e.printStackTrace(new PrintWriter(sw, true));
	 * logger.error(sw.toString()); return "loginFail"; }
	 * 
	 * ServletActionContext.getContext().getSession().put("user", user);
	 * List<String> permissions =
	 * permissionService.findPermissionsByUserID(user.getId());
	 * if(permissions.contains(Constants.VIEW_WORK_REPORT)){
	 * viewReportService.checkPermission(permissions, user.getId()); }
	 * ServletActionContext.getContext().getSession().put("permissions",
	 * permissions);
	 * 
	 * if (user.getLastName().equals(staff.getLastName())) { return
	 * "firstLogin"; } //判断版本发布的通告是否显示
	 * if(versionInfoService.checkVersionNoticeShow(user.getId())){
	 * showVersionNotice = "true"; }else{ showVersionNotice = "false"; }
	 * firstLogin = "true";
	 * ServletActionContext.getContext().getSession().put("headImgId",
	 * staff.getHeadImgId()); return "loginSuccess"; }
	 */
	@Getter
	private boolean bind = true;

	public String login() {
		User user;
		StaffVO staff;
		try {
			user = staffService.loginValidate(staffVO.getUserName(), staffVO.getPassword());
			staff = staffService.getStaffByUserID(user.getId());
		} catch (Exception e) {
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "loginFail";
		}
		ServletActionContext.getContext().getSession().put("user", user);
		if (user.getLastName().equals(staff.getLastName())) {
			return "firstLogin";
		}
		String headImgId = staff.getHeadImgId();
		ServletActionContext.getContext().getSession().put("headImgId", headImgId);
		List<Object> appInfos = userRightCenterService.findAppsByUserId(user.getId());
		ServletActionContext.getContext().getSession().put("appInfos", appInfos);
		// 判断有没有绑定微信（openId）
		if (StringUtils.isBlank(staff.getOpenId())) {
			bind = false;
		}
		return "loginSuccess";
	}

	public String goHome() {
		User user = (User) request.getSession().getAttribute("user");
		List<String> permissions = permissionService.findPermissionsByUserID(user.getId());
		if (permissions.contains(Constants.VIEW_WORK_REPORT)) {
			viewReportService.checkPermission(permissions, user.getId());
		}
		ServletActionContext.getContext().getSession().put("permissions", permissions);
		// 判断版本发布的通告是否显示
		if (versionInfoService.checkVersionNoticeShow(user.getId())) {
			showVersionNotice = "true";
		} else {
			showVersionNotice = "false";
		}
		firstLogin = "true";
		return "goHome";
	}

	@Getter
	private String goHomeUrl;
	@Getter
	private String message;

	// 跳转到对应的app主页面
	@SuppressWarnings("deprecation")
	public String goAppHome() {
		User user = (User) request.getSession().getAttribute("user");
		String appId = request.getParameter("appId");
		AppInfoEntity appInfo = userRightCenterService.getAppInfo(appId);
		String appSecret = userRightCenterService.getAppSecret(appId);
		String paramStr = user.getId()+appSecret;
		goHomeUrl = appInfo.getGoHomeUrl();
		try {
			if(goHomeUrl.contains("?")){
				goHomeUrl = appInfo.getGoHomeUrl()+"&userID="+java.net.URLEncoder.encode(Base64.encode(DesUtil.encrypt(user.getId().getBytes(),
						Constants.DES_KEY)))+"&access_token="+DigestUtils.md5Hex(paramStr);
			}else{
				goHomeUrl = appInfo.getGoHomeUrl()+"?userID="+java.net.URLEncoder.encode(Base64.encode(DesUtil.encrypt(user.getId().getBytes(),
						Constants.DES_KEY)))+"&access_token="+DigestUtils.md5Hex(paramStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goAppHome";
	}

	public String logout() {
		ServletActionContext.getContext().getSession().remove("user");
		ServletActionContext.getContext().getSession().remove("permissions");
		return "index";
	}

	public String initUserAccount() {
		if (!StringUtils.isEmpty(errorMessage)) {
			try {
				errorMessage = URLDecoder.decode(errorMessage, "utf-8");
			} catch (Exception e) {
				errorMessage = e.getMessage();
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				logger.error(sw.toString());
				return "exception-error";
			}
		}

		return "initUserAccount";
	}

	public String findNicknamesByType() {
		int type = request.getParameter("type") == null ? 0 : Integer.parseInt(request.getParameter("type"));
		nicknameVOs = staffService.findNicknamesByType(NicknameTypeEnum.valueOf(type));

		return "findNicknamesByType";
	}

	public String saveUserAccount() throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "initUserAccountFailed";
		}

		String nicknameID = request.getParameter("nicknameID");
		String password = request.getParameter("password");
		NicknameVO nicknameVO = staffService.getNicknameByID(nicknameID == null ? 0 : Integer.parseInt(nicknameID));
		if (nicknameVO == null) {
			errorMessage = "花名不存在，请重新选择！";
			return "initUserAccountFailed";
		}
		if (nicknameVO.getStatus() == NicknameStatusEnum.USED.getValue()) {
			errorMessage = "花名已使用，请重新选择！";
			return "initUserAccountFailed";
		}

		user.setLastName(nicknameVO.getName());

		// 给password加密
		user.setPassword(DesUtil.encrypt(password));

		identityService.saveUser(user);
		staffService.updateNicknameStatus(nicknameVO.getNicknameID(), NicknameStatusEnum.USED);
		// 判断版本发布的通告是否显示
		if (versionInfoService.checkVersionNoticeShow(user.getId())) {
			showVersionNotice = "true";
		} else {
			showVersionNotice = "false";
		}
		return "loginSuccess";
	}

	public String toResetStep1() {
		return "toResetStep1";
	}

	public String toResetStep2() {
		String userId = request.getParameter("userId");
		String code = request.getParameter("code");
		String savedKey = staffService.getUsefulValidateKeyByUserId(userId, false);
		if (savedKey == null || !savedKey.equals(code)) {
			return "error";
		}
		User user = identityService.createUserQuery().userId(userId).singleResult();
		request.setAttribute("userId", userId);
		request.setAttribute("code", code);
		request.setAttribute("userName", user.getLastName());
		return "toResetStep2";
	}

	public String toResetStep3() throws Exception {
		String userId = request.getParameter("userId");
		String code = request.getParameter("code");
		String password = request.getParameter("inputPassword");
		String savedKey = staffService.getUsefulValidateKeyByUserId(userId, false);
		if (savedKey == null || !savedKey.equals(code)) {
			return "error";
		} else {
			staffService.updateUserPassword(userId, DesUtil.encrypt(password));// 给password加密
		}
		return "toResetStep3";
	}

	public String updatePassWord() {
		try {
			String telephone1 = request.getParameter("telephone1");
			String password = request.getParameter("password");
			StaffVO staffVO = new StaffVO();
			staffVO.setTelephone(telephone1);
			staffVO.setPassword(password);
			String userID = staffService.updateStaffTelephone(staffVO);
			User user = identityService.createUserQuery().userId(userID).singleResult();
			ServletActionContext.getContext().getSession().put("user", user);
			List<String> permissions = permissionService.findPermissionsByUserID(user.getId());
			ServletActionContext.getContext().getSession().put("permissions", permissions);
			staffService.updateStaffTelephone(staffVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		return "updatePassWord";
	}

	public void getUserIdByKey() {
		String key = request.getParameter("key");
		String userId = null;
		Map<String, String> returnMap = new HashMap<String, String>();

		userId = getUserIdByUserName(key);
		if (StringUtils.isBlank(userId)) {
			userId = getUserIdByTelephone("'" + key + "'");
		}
		returnMap.put("userId", userId);
		if (StringUtils.isNotBlank(userId)) {
			StaffVO staffVo = staffService.getStaffByUserID(userId);
			returnMap.put("telephone", staffVo.getTelephone());
		}
		printByJson(returnMap);
	}

	public void getStaffById() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		StaffVO staffVo = staffService.getStaffByUserID(request.getParameter("id"));
		returnMap.put("staffVo", staffVo);
		printByJson(returnMap);

	}

	/**
	 * 根据 花名 获取UserId
	 */
	private String getUserIdByUserName(String userName) {
		List<User> users = identityService.createUserQuery().userLastName(userName).list();
		if (org.apache.commons.collections4.CollectionUtils.isEmpty(users)) {
			return null;
		} else {
			return users.get(0).getId();
		}
	}

	/**
	 * 根据电话号码获取userId
	 */
	private String getUserIdByTelephone(String telephone) {
		StaffVO staffVo = staffService.getStaffByTelephone(telephone);
		return staffVo == null ? null : staffVo.getUserID();
	}

	/**
	 * 获取验证码
	 */
	public void getValidateKey() {
		String userId = request.getParameter("userId");
		String telephone = request.getParameter("telephone");
		int randomNumber = (int) (Math.random() * 1000000);
		String validateKey = Strings.padStart(randomNumber + "", 6, '0');
		staffService.insertRestValidateKey(userId, validateKey);
		Map<String, Boolean> returnMap = new HashMap<String, Boolean>();
		try {
			ShortMsgSender.getInstance().send(telephone, "【智造链】重置密码验证码：" + validateKey);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		returnMap.put("success", true);
		printByJson(returnMap);
	}

	public void checkValidateKey() {
		String userId = request.getParameter("userId");
		String validateKey = request.getParameter("validateKey");
		String savedKey = staffService.getUsefulValidateKeyByUserId(userId, true);
		int result = 0;
		if (savedKey == null) {
			result = 1;
		} else if (!savedKey.equals(validateKey)) {
			result = 2;
		} else {
			result = 3;
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", result);
		printByJson(returnMap);
	}

	public String toDemoPage() {
		return "toDemoPage";
	}

	public int savePicFromURL(String picUrl, String savePath) throws Exception {
		// new一个URL对象
		URL url = new URL(picUrl);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		@Cleanup
		InputStream inStream = conn.getInputStream();
		File parent = new File(savePath);
		parent.mkdirs();
		String saveName = UUID.randomUUID().toString().replaceAll("-", "");
		@Cleanup
		OutputStream out = new FileOutputStream(new File(parent, saveName));
		byte[] buffer = new byte[10 * 1024 * 1024];
		int length = 0;
		while ((length = inStream.read(buffer, 0, buffer.length)) != -1) {
			out.write(buffer, 0, length);
			out.flush();
		}
		CommonAttachment commonAttachment = new CommonAttachment();
		commonAttachment.setAddTime(new Date());
		commonAttachment.setIsDeleted(0);
		commonAttachment.setSoftURL(savePath + "/" + saveName);
		commonAttachment.setSoftName(saveName);
		commonAttachment.setSuffix("jpg");
		Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
		return attachmentId;
	}

	// ------------------------------------------------
	@Getter
	private String selectedPanel;
	public String encryption() {
		try {
			List<Object> objects = staffService.findUserList();
			String userID = null;
			String userPWD = null;
			for (Object object : objects) {
				Object[] objs = (Object[]) object;
				userID = (String) objs[0];
				userPWD = (String) objs[1];
				if (userPWD != null) {
					staffService.updatePwdById(DesUtil.encrypt(userPWD), userID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectedPanel = "encryption";
		return "encryption";
	}
}
