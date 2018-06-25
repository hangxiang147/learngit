<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/tree/jquery.ztree.core.js"></script>
<script src="assets/js/tree/jquery.ztree.excheck.js"></script>
<script src="assets/js/tree/jquery.ztree.exedit.js"></script>
<link href="assets/css/bootstrapztree.css" rel="stylesheet">
<script type="text/javascript">
var setting = {
        view: {
            selectedMulti: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        async:{
        	enable: true,
        	dataType: 'json',
        	url: "userCenter/showChildNodeShips",
        	type: "post",
    		autoParam: ["id"],
    		otherParam:["roleId",'${roleId}']
        },
        callback: {
    		onCheck: saveRoleRightShip
    	},
        check:{
        	enable:true
        }
    };
    var zNodes = JSON.parse('${firstNodeShips}');
    var treeObj;
    $(document).ready(function(){
        $.fn.zTree.init($("#rightTree"), setting, zNodes);
        treeObj = $.fn.zTree.getZTreeObj("rightTree");
    });
    function saveRoleRightShip(event, treeId, treeNode){
    	var roleId = $("input[name='roleId']").val();
		var ids = [];
		ids.push(treeNode.id);
    	var parent = false;
    	var checked = true;
    	//父节点
    	if(treeNode.isParent){
    		parent = true;
    		if(!treeNode.checked){
    			checked = false;
    		}
    	//子节点	
    	}else{
    		//取消
    		if(!treeNode.checked){
    			//获取所有勾选取消的父节点
    			var pNode = treeNode.getParentNode();
    			while(pNode){
    				if(!pNode.checked){
    					ids.push(pNode.id);
    				}
    				pNode = pNode.getParentNode();
    			}
    			checked = false;
    		//选中
    		}else{
    			var pNode = treeNode.getParentNode();
    			while(pNode){
    				ids.push(pNode.id);
    				pNode = pNode.getParentNode();
    			}
    		}
    	}
		$.ajax({
			url:'userCenter/saveRoleRightShips',
			type:'post',
			data:{'permissionIds':ids.join(','),'roleId':roleId,'checked':checked,'parent':parent,'appId':'${appId}'},
			success:function(data){
				if(data.success){
					if(treeNode.checked){
						layer.alert("设置成功",{offset:'100px'});
					}else{
						layer.alert("取消成功",{offset:'100px'});
					}
				}else{
					layer.alert("系统故障，请联系管理员",{offset:'100px'});
				}
			}
		});
    }
</script>
<style type="text/css">
.curSelectedNode{
	background:#82c0f5 !important;
}
#rightTree a:hover, #rightTree a:hover:focus{
	text-decoration:none;
}
#rightTree a:hover{
	background:#e5e5e5 !important;
}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'userCenter'"></s:set>
        <s:set name="selectedPanel" value="'roleRightShipManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">权限设置</h3>
       		<input type="hidden" name="roleId" value="${roleId}">
       		<input type="hidden" name="appId" value="${appId}">
			<ul id="rightTree" class="ztree"></ul>
        	<br>
        	<!-- <button type="submit" class="btn btn-primary">提交</button> -->
        	<button type="button" style="margin-left:1%" class="btn btn-default" onclick="javascript:location.href='userCenter/showRoleRightShipList?appId=${appId}'">返回</button>
        </div>
      </div>
    </div>
  </body>
</html>