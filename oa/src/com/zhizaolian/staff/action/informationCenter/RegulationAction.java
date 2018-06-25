package com.zhizaolian.staff.action.informationCenter;

import com.zhizaolian.staff.action.BaseAction;

import lombok.Getter;
import lombok.Setter;

public class RegulationAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
 
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	private String errorMessage;
	
	public String findProhibitions() {
		selectedPanel = "prohibitions";
		return "prohibitions";
	}
}
