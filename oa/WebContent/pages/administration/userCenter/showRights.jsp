<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js?version=<%=Math.random()%>"></script>
<script src="assets/js/layer/layer.js"></script>
<script src="assets/js/tree/jquery.ztree.core.js"></script>
<script src="assets/js/tree/jquery.ztree.excheck.js"></script>
<script src="assets/js/tree/jquery.ztree.exedit.js"></script>
<link href="assets/css/bootstrapztree.css" rel="stylesheet">
<script type="text/javascript">
$(function(){
	$("[data-toggle='tooltip']").tooltip();
});
var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        edit: {
        	drag:{
        		isCopy: false,
        		isMove: false
        	},
            enable: true
        },
        async:{
        	enable: true,
        	dataType: 'json',
        	url: "userCenter/showChildNodes",
        	type: "post",
    		autoParam: ["id"]
        },
        callback:{
        	beforeRemove: beforeRemove,
        	beforeEditName: beforeEditName,
        	onAsyncSuccess:function(event, treeId, treeNode, msg){
                if('${mes}'=='true'){
                    $("#"+treeNode.tId+" ul .node_name").each(function(){
                    	var names = $(this).text().split("<span>");
                    	var name = names[0];
                    	var isUsed = names[1];
                    	var nameHtml = ""; 
                    	if("Y"==isUsed){
                    		nameHtml = name+"<span style='color:blue'>[启用]</span>";
                    	}else{
                    		nameHtml = name+"<span style='color:red'>[停止]</span>";
                    	}
                    	$(this).html(nameHtml);
                    	$(this).parent().attr("title",name);
                    });
                }
        	}
        }
    };
   var zNodes = JSON.parse('${firstNodes}');
    var treeObj;
    $(document).ready(function(){
        $.fn.zTree.init($("#rightTree"), setting, zNodes);
        treeObj = $.fn.zTree.getZTreeObj("rightTree");
        if('${mes}'=='true'){
            $(".node_name").each(function(){
            	var names = $(this).text().split("<span>");
            	var name = names[0];
            	var isUsed = names[1];
            	var nameHtml = ""; 
            	if("Y"==isUsed){
            		nameHtml = name+"<span style='color:blue'>[启用]</span>";
            	}else{
            		nameHtml = name+"<span style='color:red'>[停止]</span>";
            	}
            	$(this).html(nameHtml);
            	$(this).parent().attr("title",name);
            });
        }
    });

    function addHoverDom(treeId, treeNode) {
    	//根据图标判断是否是权限
    	if(treeNode.iconSkin)return;
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='新增节点' onfocus='this.blur();'></span>";
        sObj.after(addStr);
       // $("[data-toggle='tooltip']").tooltip();
        var btn = $("#addBtn_"+treeNode.tId);
        if(btn){
        	btn.bind("click",function(){
        		firstLevel = false;
    			$("#nodeType option").each(function(){
    				if($(this).val()=='目录'){
    					$(this).prop("selected","selected");
    				}else{
    					$(this).removeAttr("selected");
    				}
    			});
    			$("#rightCode").css("display","none");
    			$("input[name='permissionCode']").val('').removeAttr("disabled");
    			$("input[name='permissionName']").val('');
    			$("input[name='rightId']").val('');
    			$("input[name='sort']").val('');
    			$("input[name='pageUrl']").val('');
    			$("input[name='requestUrl']").val('');
    			$("select[name='isUsed'] option").removeAttr("selected");
    			$("#myModalLabel").text("新增节点");
				$("#addRight").modal("show");	
        	});
        }
    }
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    }
	function beforeRemove(treeId, treeNode) {
		treeObj.selectNode(treeNode);
		layer.confirm("确定删除当前被选中节点？",{offset:'100px'},function(){
			$.ajax({
				url:'/userCenter/deleteRightNode',
				data:{'id':treeNode.id,'appId':'${appId}'},
				type:'post',
				dataType:'json',
				success:function (result){
					if(result.hasChild){
						layer.alert("父节点不可删除",{offset:'100px'});
					}else if(result.isAllocated){
						layer.alert("权限已分配，不可删除",{offset:'100px'});
					}else if(result.success){
						layer.alert("删除成功",{offset:'100px'},function(index){
							layer.close(index);
							var node = treeNode.getParentNode();
							//只有父节点才可以异步加载
					        if(node && !node.isParent){
					        	node = node.getParentNode();
					        }
							if(node){
								treeObj.reAsyncChildNodes(node, "refresh");
				                if('${mes}'=='true'){
				                    $("#"+node.tId+" ul .node_name").each(function(){
				                    	var names = $(this).text().split("<span>");
				                    	var name = names[0];
				                    	var isUsed = names[1];
				                    	var nameHtml = ""; 
				                    	if("Y"==isUsed){
				                    		nameHtml = name+"<span style='color:blue'>[启用]</span>";
				                    	}else{
				                    		nameHtml = name+"<span style='color:red'>[停止]</span>";
				                    	}
				                    	$(this).html(nameHtml);
				                    	$(this).parent().attr("title",name);
				                    });
				                }
							}else{
					        	$.ajax({
					        		url:'userCenter/getFirstLevelNodes',
					        		data:{'appId':$("select[name='appId'] option:selected").val()},
					        		success:function(data){
					        			 $.fn.zTree.init($("#rightTree"), setting, data);
					                     if('${mes}'=='true'){
					                         $(".node_name").each(function(){
					                         	var names = $(this).text().split("<span>");
					                         	var name = names[0];
					                         	var isUsed = names[1];
					                         	var nameHtml = ""; 
					                         	if("Y"==isUsed){
					                         		nameHtml = name+"<span style='color:blue'>[启用]</span>";
					                         	}else{
					                         		nameHtml = name+"<span style='color:red'>[停止]</span>";
					                         	}
					                         	$(this).html(nameHtml);
					                         	$(this).parent().attr("title",name);
					                         });
					                     }
					        		}
					        	});
							}
						})
					}else{
						layer.alert("删除失败",{offset:'100px'});
					}
				}
			});
		});
		return false;
	}
	function beforeEditName(treeId, treeNode){
		firstLevel = false;
		treeObj.selectNode(treeNode);
		var nodeName = treeNode.name;
		//权限节点
		if(treeNode.iconSkin){
			$("#nodeType option").each(function(){
				if($(this).val()=='权限'){
					$(this).prop("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
			$("#rightCode").css("display","inline");
			$("input[name='permissionName']").val(treeNode.permissionName);
			$("input[name='permissionCode']").val(treeNode.permissionCode);
			$("input[name='pageUrl']").val(treeNode.pageUrl);
			$("input[name='requestUrl']").val(treeNode.requestUrl);
			var isUsed = treeNode.isUsed;
			$("select[name='isUsed'] option").each(function(){
			    var val = $(this).val();
			    if(val==isUsed){
			    	$(this).prop("selected","selected");
			    }else{
			    	$(this).removeAttr("selected");
			    }
			});
		}else{
			$("#nodeType option").each(function(){
				if($(this).val()=='目录'){
					$(this).prop("selected","selected");
				}else{
					$(this).removeAttr("selected");
				}
			});
			$("#rightCode").css("display","none");
			$("input[name='permissionName']").val(treeNode.permissionName);
			$("input[name='permissionCode']").val(treeNode.permissionCode);
			$("input[name='sort']").val(treeNode.sort);
			$("input[name='pageUrl']").val(treeNode.pageUrl);
			$("input[name='requestUrl']").val(treeNode.requestUrl);
			var isUsed = treeNode.isUsed;
			$("select[name='isUsed'] option").each(function(){
			    var val = $(this).val();
			    if(val==isUsed){
			    	$(this).prop("selected","selected");
			    }
			});
		}
		$("input[name='rightId']").val(treeNode.id);
		$("#myModalLabel").text("修改节点");
		$("#addRight").modal("show");
		return false;
	}
    var firstLevel = false;
    function addRightContent(){
    	firstLevel = true;
		$("#nodeType option").each(function(){
			if($(this).val()=='目录'){
				$(this).prop("selected","selected");
			}else{
				$(this).removeAttr("selected");
			}
		});
		$("#rightCode").css("display","none");
		$("input[name='permissionCode']").val('').removeAttr("disabled");
		$("input[name='permissionName']").val('');
		$("input[name='rightId']").val('');
		$("input[name='sort']").val('');
		$("input[name='pageUrl']").val('');
		$("input[name='requestUrl']").val('');
		$("select[name='isUsed'] option").removeAttr("selected");
		$("#myModalLabel").text("新增节点");
		$("#addRight").modal("show");	
    }
    function changeNodeType(){
    	var nodeType = $("#nodeType option:selected").val();
    	var rightId = $("input[name='rightId']").val();
    	//mes最多两级目录
    	if(treeObj.getSelectedNodes()[0].level==1 && nodeType=='目录' && !rightId){
    		layer.alert("最多两级目录",{offset:'100px'});
    		return;
    	}
    	if(nodeType=='目录'){
    		$("#rightCode").css("display","none");
    		$("input[name='permissionCode']").val('').removeAttr("disabled");
    		$("#sort").css("display","block");
    	}else{
    		$("input[name='permissionCode']").val('').removeAttr("disabled");
    		$("#rightCode").css("display","inline");
    		$("#sort").css("display","none");
	    	var id;
	    	//编辑模式
	    	if(rightId){
	    		var node = treeObj.getSelectedNodes()[0].getParentNode();
	    		if(node){
	    			id = node.id;
	    		}
	    	}else{
	    		var node = treeObj.getSelectedNodes()[0];
	    		if(node){
	    			id = node.id;
	    		}
	    	}
    		if(id && '${com}'=='true'){
    			var name = $("input[name='permissionName']").val();
    			if(!name){
    				return;
    			}
        		$.ajax({
        			url:'userCenter/generatePermissionCode',
        			data:{'id':id,'name':name},
        			success:function(data){
        				$("input[name='permissionCode']").val(data.permissionCode).attr("disabled", "disabled"); 	
        			}
        		});
    		}
    	}
    }
    function addRight_(){
    	var nodeType = $("#nodeType option:selected").val();
    	var selectedNode = treeObj.getSelectedNodes()[0];
    	var level = -1;
    	if(selectedNode){
    		level = selectedNode.level;
    	}
    	var rightId = $("input[name='rightId']").val();
    	//mes最多两级目录
    	if(level==1 && nodeType=='目录' && !rightId){
    		layer.alert("最多两级目录",{offset:'100px'});
    		return;
    	}
    	var rightName = $("input[name='permissionName']").val();
		if(!rightName.trim()){
			layer.alert("名称不能为空",{offset:'100px'});
			return;
		}
    	var nodeType = $("#nodeType option:selected").val();
    	var rightCode = $("input[name='permissionCode']").val();
    	var sort = $("input[name='sort']").val();
    	var isUsed = $("select[name='isUsed'] option:selected").val();
    	var pageUrl = $("input[name='pageUrl']").val();
    	var requestUrl = $("input[name='requestUrl']").val();
    	if('${mes}'=='true' && !rightCode){
    		if(!pageUrl){
    			layer.alert("程式页面地址不能为空",{offset:'100px'});
    			return;
    		}
    		if(!requestUrl){
    			layer.alert("程式请求地址不能为空",{offset:'100px'});
    			return;
    		}
    	}
    	if(nodeType=='权限'){
    		if(!rightCode.trim()){
    			layer.alert("权限编码不能为空",{offset:'100px'});
    			return;
    			//mes不做编码重复校验
    		}else if('${mes}'!='true' && !checkCode()){
    			return;
    		}
    	}
    	$("#addRight").modal("hide");
    	var appId = $("select[name='appId'] option:selected").val();
    	if(firstLevel){
			$.ajax({
				url:'/userCenter/addFirstRightContent',
				data:{'name':rightName,'appId':appId,'code':rightCode,'type':nodeType,
					'sort':sort,'isUsed':isUsed,'pageUrl':pageUrl,'requestUrl':requestUrl},
				type:'post',
				dataType:'json',
				success:function (result){
					if(result.success){
						layer.alert("添加成功",{offset:'100px'},function(index){
							layer.close(index);
							var newNode = treeObj.addNodes(null, result.nodeMap);
			                if('${mes}'=='true'){
			                    $("#"+newNode[0].tId+" .node_name").each(function(){
			                    	var names = $(this).text().split("<span>");
			                    	var name = names[0];
			                    	var isUsed = names[1];
			                    	var nameHtml = ""; 
			                    	if("Y"==isUsed){
			                    		nameHtml = name+"<span style='color:blue'>[启用]</span>";
			                    	}else{
			                    		nameHtml = name+"<span style='color:red'>[停止]</span>";
			                    	}
			                    	$(this).html(nameHtml);
			                    	$(this).parent().attr("title",name);
			                    });
			                }
						})
					}else{
						layer.alert("添加失败",{offset:'100px'});
					}
				}
			})
    	}else{
        	if(selectedNode){
        		var parentId = selectedNode.id;
        	}
        	var id = $("input[name='rightId']").val();
        	$.ajax({
        		url:'userCenter/addRight',
        		data:{'id':id,'appId':appId,'parentId':parentId,'rightName':rightName,
        			'rightCode':rightCode,'type':nodeType,'sort':sort,'isUsed':isUsed,'pageUrl':pageUrl,'requestUrl':requestUrl,'level':level},
        		type:'post',
        		success:function(result){
        			if(result.success){
    					layer.alert(id!=''?"修改成功":"添加成功",{offset:'100px'},function(index){
    						layer.close(index);
    						if(id!=''){
    							refreshParentNode();
    						}else{
    							refreshNode();
    						}
    					})
    				}else{
    					if(result.msg){
    						layer.alert(result.msg,{offset:'100px'});
    					}else{
    						layer.alert(id!=''?"修改失败":"添加失败",{offset:'100px'});
    					}
    				}
        		}
        	});
    	}
    }
    function addRight(){
    	var rightId = $("input[name='rightId']").val();
    	var id;
    	//编辑模式
    	if(rightId){
    		var node = treeObj.getSelectedNodes()[0].getParentNode();
    		if(node){
    			id = node.id;
    		}
    	}else{
    		var node = treeObj.getSelectedNodes()[0];
    		if(node){
    			id = node.id;
    		}
    	}
		if(id && '${com}'=='true'){
			var name = $("input[name='permissionName']").val();
			var type = $("#nodeType option:selected").val();
			if(type != '权限' || !name){
				addRight_();
				return;
			}
    		$.ajax({
    			url:'userCenter/generatePermissionCode',
    			data:{'id':id,'name':name},
    			success:function(data){
    				$("input[name='permissionCode']").val(data.permissionCode).attr("disabled", "disabled"); 
    				addRight_();
    			}
    		});
		}else{
			addRight_();
		}
    }
    function checkCode(){
    	var permissionCode = $("input[name='permissionCode']").val();
    	var appId = $("select[name='appId'] option:selected").val();
    	var id = $("input[name='rightId']").val();
    	var flag = true;
    	$.ajax({
    		url:'userCenter/checkRightCode',
    		data:{'permissionCode':permissionCode,'appId':appId,'id':id},
    		async:false,
    		success:function(data){
    			if(data.exist){
    				layer.alert("权限编码已存在",{offset:'100px'});
    				flag = false;
    			}
    		}
    	});
    	return flag;
    }
    /** 
     * 刷新当前节点 
     */  
    function refreshNode() {  
        var type = "refresh";
        var selectedNode = treeObj.getSelectedNodes()[0]; 
        if(!selectedNode.isParent){
        	selectedNode = selectedNode.getParentNode();
        }
        if(selectedNode){
        	treeObj.reAsyncChildNodes(selectedNode, type);
            if('${mes}'=='true'){
                $("#"+selectedNode.tId+" .node_name").each(function(){
                	var names = $(this).text().split("<span>");
                	var name = names[0];
                	var isUsed = names[1];
                	var nameHtml = ""; 
                	if("Y"==isUsed){
                		nameHtml = name+"<span style='color:blue'>[启用]</span>";
                	}else{
                		nameHtml = name+"<span style='color:red'>[停止]</span>";
                	}
                	$(this).html(nameHtml);
                	$(this).parent().attr("title",name);
                });
            }
        }else{
        	$.ajax({
        		url:'userCenter/getFirstLevelNodes',
        		data:{'appId':$("select[name='appId'] option:selected").val()},
        		success:function(data){
        			 $.fn.zTree.init($("#rightTree"), setting, data);
        		        if('${mes}'=='true'){
        		            $(".node_name").each(function(){
        		            	var names = $(this).text().split("<span>");
        		            	var name = names[0];
        		            	var isUsed = names[1];
        		            	var nameHtml = ""; 
        		            	if("Y"==isUsed){
        		            		nameHtml = name+"<span style='color:blue'>[启用]</span>";
        		            	}else{
        		            		nameHtml = name+"<span style='color:red'>[停止]</span>";
        		            	}
        		            	$(this).html(nameHtml);
        		            	$(this).parent().attr("title",name);
        		            });
        		        }
        		}
        	});
        }
    }  
    function refreshParentNode(){
        var type = "refresh";
        var selectedNode = treeObj.getSelectedNodes()[0]; 
        var parentNode = selectedNode.getParentNode();
        if(parentNode){
        	treeObj.reAsyncChildNodes(parentNode, type);
        }else{
        	$.ajax({
        		url:'userCenter/getFirstLevelNodes',
        		data:{'appId':$("select[name='appId'] option:selected").val()},
        		success:function(data){
        			 $.fn.zTree.init($("#rightTree"), setting, data);
        		        if('${mes}'=='true'){
        		            $(".node_name").each(function(){
        		            	var names = $(this).text().split("<span>");
        		            	var name = names[0];
        		            	var isUsed = names[1];
        		            	var nameHtml = ""; 
        		            	if("Y"==isUsed){
        		            		nameHtml = name+"<span style='color:blue'>[启用]</span>";
        		            	}else{
        		            		nameHtml = name+"<span style='color:red'>[停止]</span>";
        		            	}
        		            	$(this).html(nameHtml);
        		            	$(this).parent().attr("title",name);
        		            });
        		        }
        		}
        	});
        }
    }
    function autoCode(){
    	var rightId = $("input[name='rightId']").val();
    	var id;
    	//编辑模式
    	if(rightId){
    		var node = treeObj.getSelectedNodes()[0].getParentNode();
    		if(node){
    			id = node.id;
    		}
    	}else{
    		var node = treeObj.getSelectedNodes()[0];
    		if(node){
    			id = node.id;
    		}
    	}
		if(id && '${com}'=='true'){
			var name = $("input[name='permissionName']").val();
			var type = $("#nodeType option:selected").val();
			if(type != '权限' || !name){
				return;
			}
    		$.ajax({
    			url:'userCenter/generatePermissionCode',
    			data:{'id':id,'name':name},
    			success:function(data){
    				$("input[name='permissionCode']").val(data.permissionCode).attr("disabled", "disabled"); 	
    			}
    		});
		}
    }
</script>
<style type="text/css">
.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
}
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
        <s:set name="selectedPanel" value="'rightManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">权限管理</h3>
        <form action="userCenter/showRights" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="col-sm-1 control-label">应用</label>
        		<div class="col-sm-2">
        			<select name="appId" class="form-control">
        				<c:forEach items="${appInfos}" var="appInfo">
        					<option ${appId==appInfo.appId?'selected':''} value="${appInfo.appId}">${appInfo.appName}</option>
        				</c:forEach>
        			</select>
        		</div>
        		<button type="submit" class="btn btn-primary" style="margin-left:2%">查询</button>
        	</div>
        </form>
        <a class="btn btn-primary" href="javascript:addRightContent()"><span class="glyphicon glyphicon-plus"></span> 目录</a>
		<div class="sub-header"></div>
		<ul id="rightTree" class="ztree"></ul>
        </div>
      </div>
    </div>
    <div id="addRight" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">新增节点</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
	    	  		<label class="col-sm-4 control-label">类型<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
			    		<select id="nodeType" class="form-control" name="type" onchange="changeNodeType()">
			    			<option value="目录">目录</option>
			    			<option value="权限">权限</option>
	        			</select>
		    		</div>
	    	  	</div>
				<div class="form-group">
	    	  		<label class="col-sm-4 control-label">名称<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="permissionName" onblur="autoCode()">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-4 control-label">编码<span id="rightCode" style="color:red;display:none"> *</span></label>
		    		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="permissionCode">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group" id="sort">
	    	  		<label class="col-sm-4 control-label">顺序</label>
	    	  		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="sort" type="number">
		    		</div>
	    	  	</div>
	    	  	<c:if test="${mes}">
	    	  	<div class="form-group">
	    	  		<label class="col-sm-4 control-label">是否启用<span style="color:red"> *</span></label>
	    	  		<div class="col-sm-7">
		    		<select class="form-control" name="isUsed">
		    			<option value="Y">Y</option>
		    			<option value="N">N</option>
		    		</select>
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-4 control-label">程式页面地址<span style="color:red"> *</span></label>
	    	  		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="pageUrl">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-4 control-label">程式请求地址<span style="color:red"> *</span></label>
	    	  		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="requestUrl">
		    		</div>
	    	  	</div>
	    	  	</c:if>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="addRight()" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<input name="rightId" type="hidden">
		</div>
		</form>
	</div>
	</div>
  </body>
</html>