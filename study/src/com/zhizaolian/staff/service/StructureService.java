package com.zhizaolian.staff.service;

import com.zhizaolian.staff.entity.StructureNodeEntity;
import com.zhizaolian.staff.vo.StructureNodeVo;

public interface StructureService {

	StructureNodeVo getStructureDatas();

	void editStructureNode(StructureNodeEntity structureNode);

	int getCurrentNodeDepth(StructureNodeEntity structureNode);

	void deleteStructureNode(String nodeId);

	StructureNodeEntity getStructureNode(Integer nodeId);

}
