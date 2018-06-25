package com.zhizaolian.staff.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class StructureNodeVo {
	
	private String id;
	
	private String name;
	
	private String title;
	
	private String userId;
	
	private Map<String, Integer> relationship;
	
	private List<StructureNodeVo> children = new ArrayList<>();
}
