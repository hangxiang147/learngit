package com.zhizaolian.staff.action.PM;

import lombok.Getter;
import lombok.Setter;

import com.zhizaolian.staff.action.BaseAction;

public class StructureAction extends BaseAction {
	
	@Getter
	@Setter
	private String selectedPanel;

	private static final long serialVersionUID = 1L;
	
	public String getStructureByCompanyID() {
		String companyID = request.getParameter("companyID");
		
		selectedPanel = companyID;
		return "getStructureByCompanyID";
	}
}
