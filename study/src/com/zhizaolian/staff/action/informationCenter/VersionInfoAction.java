package com.zhizaolian.staff.action.informationCenter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.ProjectVersionEntity;
import com.zhizaolian.staff.entity.VersionFuncionInfo;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.service.VersionInfoService;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Getter;
import lombok.Setter;

public class VersionInfoAction extends BaseAction{
	
	private static final long serialVersionUID = 2669079947771654321L;
	@Getter
	private String selectedPanel;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private VersionFuncionInfo versionFuncionInfo;
	@Autowired
	private SoftPerformanceService softPerformanceService;
	@Autowired
	private VersionInfoService versionInfoService;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	
	public String findVersionInfoList(){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		try {
			ListResult<VersionFuncionInfo> versionFuncionInfos = versionInfoService.findVersionInfoList(limit, page, beginDate, endDate);
			request.setAttribute("versionFuncionInfos", versionFuncionInfos.getList());
			count = versionFuncionInfos.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if(totalPage==0){
				totalPage = 1;
			}
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "versionInfoList";
		return "findVersionInfoList";
	}
	public String saveVersionInfo(){
		User user = (User)request.getSession().getAttribute("user");
		try {
			versionInfoService.saveVersionInfo(versionFuncionInfo, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "保存失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "versionInfoList";
		return "render_findVersionInfoList";
	}
	public String addVersionInfo(){
		Integer projectId = softPerformanceService.getProjectIdByName(Constants.OA);
		List<ProjectVersionEntity> versions = softPerformanceService
				.getProjectVersionLst(String.valueOf(projectId));
		request.setAttribute("versions", versions);
		selectedPanel = "versionInfoList";
		return "addVersionInfo";
	}
	public String updateVersion(){
		String id = request.getParameter("id");
		try {
			VersionFuncionInfo versionFuncionInfo = versionInfoService.getVersionFuncionInfo(id);
			request.setAttribute("versionFuncionInfo", versionFuncionInfo);
			Integer projectId = softPerformanceService.getProjectIdByName(Constants.OA);
			List<ProjectVersionEntity> versions = softPerformanceService
					.getProjectVersionLst(String.valueOf(projectId));
			request.setAttribute("versions", versions);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "versionInfoList";
		return "updateVersion";
	}
	public String deleteVersion(){
		String id = request.getParameter("id");
		versionInfoService.deleteVersion(id);
		selectedPanel = "versionInfoList";
		return "render_findVersionInfoList";
	}
	public String addVersionNoticeActor(){
		User user = (User)request.getSession().getAttribute("user");
		versionInfoService.addVersionNoticeActor(user.getId());
		return "render_home";
	}
}
