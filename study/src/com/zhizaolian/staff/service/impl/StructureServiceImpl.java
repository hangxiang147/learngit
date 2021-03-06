package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.StructureNodeEntity;
import com.zhizaolian.staff.service.StructureService;
import com.zhizaolian.staff.vo.StructureNodeVo;

@Service(value="structureService")
public class StructureServiceImpl implements StructureService {
	@Autowired
	private BaseDao baseDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public StructureNodeVo getStructureDatas() {
		//获取根节点
		String hql = "from StructureNodeEntity where isDeleted=0 order by id";
		List<StructureNodeEntity> rootNodes = (List<StructureNodeEntity>) baseDao.hqlPagedFind(hql, 1, 1);
		if(rootNodes.size()>0){
			StructureNodeEntity rootNode = rootNodes.get(0);
			return getAllChildren(rootNode.getId());
		}
		return null;
	}
	@Override
	public StructureNodeEntity getStructureNode(Integer nodeId){
		String hql = "from StructureNodeEntity where id="+nodeId;
		return (StructureNodeEntity) baseDao.hqlfindUniqueResult(hql);
	}
	public StructureNodeVo getAllChildren(Integer nodeId){
		//获取当前节点
		StructureNodeEntity structureNodeEntity = getStructureNode(nodeId);
		StructureNodeVo structureNodeVo = setValueToVo(structureNodeEntity);
		//获取当前节点的所有子节点
		List<StructureNodeEntity> children = getChildren(nodeId);
		for(StructureNodeEntity child: children){
			StructureNodeVo childNode = getAllChildren(child.getId());
			structureNodeVo.getChildren().add(childNode);
		}
		return structureNodeVo;
	}
	public StructureNodeVo setValueToVo(StructureNodeEntity structureNode){
		StructureNodeVo structureNodeVo = new StructureNodeVo();
		structureNodeVo.setId(String.valueOf(structureNode.getId()));
		String userName = structureNode.getUserName();
		if(StringUtils.isNotBlank(userName)){
			structureNodeVo.setName(structureNode.getUserName());
			structureNodeVo.setUserId(structureNode.getUserId());
			structureNodeVo.setTitle(structureNode.getDepartmentName());
		}else{
			structureNodeVo.setName(structureNode.getDepartmentName());
			structureNodeVo.setTitle("");
		}
		Map<String, Integer> relationshipMap = new HashMap<>();
		relationshipMap.put("children_num", structureNode.getChilerenNum());
		int parentId = structureNode.getParentId();
		if(parentId > 0){
			relationshipMap.put("parent_num", parentId);
		}
		int siblingNum = structureNode.getSiblingNum();
		if(siblingNum > 0){
			relationshipMap.put("sibling_num", siblingNum);
		}
		structureNodeVo.setRelationship(relationshipMap);
		return structureNodeVo;
	}
	@SuppressWarnings("unchecked")
	public List<StructureNodeEntity> getChildren(Integer parentId){
		String hql = "from StructureNodeEntity where parentId="+parentId+" and isDeleted=0 order by sort+0";
		return (List<StructureNodeEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void editStructureNode(StructureNodeEntity structureNode) {
		Integer id = structureNode.getId();
		if(null != id){
			StructureNodeEntity oldStructureNode = getStructureNode(id);
			oldStructureNode.setDepartmentName(structureNode.getDepartmentName());
			oldStructureNode.setUserId(structureNode.getUserId());
			oldStructureNode.setUserName(structureNode.getUserName());
			baseDao.hqlUpdate(oldStructureNode);
		}else{
			int parentId = structureNode.getParentId();
			List<StructureNodeEntity> children = getChildren(parentId);
			if(children.size()>0){
				structureNode.setSiblingNum(children.size());
				structureNode.setSort(String.valueOf(Integer.parseInt(children.get(children.size()-1).getSort())+1));
			}else{
				structureNode.setSort("0");
			}
			structureNode.setAddTime(new Date());
			baseDao.hqlSave(structureNode);
			//更新父节点的子节点数量
			int childrenNum = children.size()+1;
			updatePerentNodeChilerenNum(childrenNum, parentId);
		}
	}
	private void updatePerentNodeChilerenNum(int childrenNum, int parentId) {
		String hql = "update StructureNodeEntity set chilerenNum="+childrenNum+" where id="+parentId;
		baseDao.excuteHql(hql);
	}
	@Override
	public int getCurrentNodeDepth(StructureNodeEntity structureNode) {
		structureNode.setDepth(structureNode.getDepth()+1);
		StructureNodeEntity node = getStructureNode(structureNode.getParentId());
		int parentId = node.getParentId();
		if(parentId!=0){
			node.setDepth(structureNode.getDepth());
			structureNode.setDepth(getCurrentNodeDepth(node));
		}
		return structureNode.getDepth();
	}
	@Override
	public void deleteStructureNode(String nodeId) {
		String hql = "update StructureNodeEntity set isDeleted=1 where id="+nodeId;
		baseDao.excuteHql(hql);
		List<StructureNodeEntity> children = getChildren(Integer.parseInt(nodeId));
		//删除子节点
		for(StructureNodeEntity child: children){
			child.setIsDeleted(1);
			baseDao.hqlUpdate(child);
		}
		//更新父节点的子节点数量
		StructureNodeEntity node = getStructureNode(Integer.parseInt(nodeId));
		int childrenNum = node.getSiblingNum();
		updatePerentNodeChilerenNum(childrenNum, node.getParentId());
		//更新兄弟节点的兄弟数量
		List<StructureNodeEntity> siblingNodes = getSiblingNodes(node.getParentId(), Integer.parseInt(nodeId));
		for(StructureNodeEntity siblingNode: siblingNodes){
			siblingNode.setSiblingNum(siblingNode.getSiblingNum()-1);
			baseDao.hqlUpdate(siblingNode);
		}
	}
	@SuppressWarnings("unchecked")
	private List<StructureNodeEntity> getSiblingNodes(int parentId, int nodeId) {
		String hql = "from StructureNodeEntity where isDeleted=0 and parentId="+parentId+" and id!="+nodeId;
		return (List<StructureNodeEntity>) baseDao.hqlfind(hql);
	}
}
