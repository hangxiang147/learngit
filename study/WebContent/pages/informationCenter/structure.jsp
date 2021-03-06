<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link href="assets/css/vipuser.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var storage=window.localStorage;
	var canAddDepartemnt=true;
	var deptId=null;
	var level=null;
	var node=null;
	$(function() {
		$('body').on('click','.leftTree h1',function () {
			if($(this).hasClass('add')){
				$(this).removeClass("add");
			}
			else{
				$(this).addClass("add");
			}
            $(this).next().toggle();
        });

		$('body').on('click','.leftTree a',function () {
			if($(this).hasClass('add')){
				$(this).removeClass("add");
				$(this).next().css("display","none");
			}
			else{
				$(this).addClass("add");
				$(this).next().css("display","block");
			} 
        });
		var deptId = storage.getItem("departmentId");
		//自动加载上次操作（新增，修改，删除）的节点，通过暂停实现，效率不高
 		if(deptId){
 			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'structure/getAllParentDepIds',
				type:'post',
				data:{'deptId':deptId},
				dataType:'json',
				success:function (data) {
					var deptIds = [];
					data.allParentDepIds.forEach(function(value, index){
						deptIds.push(value);
					});
					for(var i=deptIds.length-1; i>=0; i--){
						var _deptId = deptIds[i];
						if(i==(deptIds.length-1)){
							$("a[data-departmentid='"+_deptId+"']").click();
						}else{
							setTimeout(function(){
								$("a[data-departmentid='"+_deptId+"']").click();
						    },300);
						}
						
					}
					setTimeout(function(){
						$("a[data-departmentid='"+deptId+"']").click();
				    },1000);
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
					}
			});
			storage.setItem("departmentId","");
		} 
	});
	function showDepartmentsPositions(obj) {
			node = obj;
			canAddDepartemnt=true;
			var departmentID = $(obj).attr("data-departmentID");
			deptId=departmentID;
			level=1;
			var item=$(obj);
			item.parents().each(function (){
				if("UL"==$(this).prop('tagName'))
					level++;
			})
			$.ajax({
				url:'personal/showDepartmentsPositions',
				type:'post',
				data:{departmentID:departmentID},
				dataType:'json',
				success:function (data) {
					var a="";
					$.each(data.positionVOs, function(i, position) {	
						a+="<li><a href='javascript:void(0)' onclick='showStaff(this)' data-departmentID='"+departmentID+"' data-positionID='"+position.positionID+"'>"+position.positionName+"</a><ul id='showStaff"+departmentID+position.positionID+"'></ul></li>";

					});
					$.each(data.departmentVOs, function(i, department) {
						a+="<li><a href='javascript:void(0)' onclick='showDepartmentsPositions(this)' data-departmentID='"+department.departmentID+"'>"+department.departmentName+"</a><ul id='showDepartmentsPositions"+department.departmentID+"'></ul></li>";
					});
					$("#showDepartmentsPositions"+departmentID).html(a);
				

				}
			});
	}
	function showStaff(obj){
		node = obj;
		canAddDepartemnt=false;
		deptId=null;
		level=null;
		var departmentID = $(obj).attr("data-departmentID");
		var positionID=$(obj).attr("data-positionID");		
		$.ajax({
			url:'personal/showStaff',
			type:'post',
			data:{departmentID:departmentID,positionID:positionID},
			dataType:'json',
			success:function(data){
				var b="";
				$.each(data,function(i,staff){
					b+="<li>"+staff['lastName']+"</li>";
				});
 				$("#showStaff"+departmentID+positionID).html(b);
				
			}
		});
			
	}		
	

</script>
<style type="text/css">
ul {
	list-style: none;
}

.margin-top-10 {
	margin-top: 10px;
}
#tree a:focus{
	background:#337ab7;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'structure'"></s:set>
			<%@include file="/pages/informationCenter/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			 <div class="containerLeft">
			<div class="form-group">
			    <div class="col-sm-2">
					<button class="btn btn-primary"  id="deptInsert">新增部门</button>
			    </div>
			    <div class="col-sm-2">
					<button class="btn btn-primary" id="positionInsert">新增职位</button>
			    </div>
			    <div class="col-sm-2">
					<button class="btn btn-primary"  id="modifyNode">修改节点</button>
			    </div>
			    <div class="col-sm-2">
					<button class="btn btn-primary" id="deleteNode">删除节点</button>
			    </div>
			</div>
	         <div id="tree" class="leftTree" style="margin-top:50px">

					<%-- <c:forEach items="${departmentFrameVOs}" var="departmentFrame">
					   
						<h1>${departmentFrame.departmentVO.departmentName }<font size="5px">(${departmentFrame.companyVO.companyName })</font></h1> --%>
                         
							<ul>
							<c:forEach items="${departments}" var="department">
								<li><a 
									href="javascript:void(0)" onclick="showDepartmentsPositions(this)" data-departmentID="${department.departmentID}">${department.departmentName }</a>
									<ul id="showDepartmentsPositions${department.departmentID}"></ul>							
								</li>
							 </c:forEach>
							<%-- <c:forEach items="${departmentFrame.positionVOs}" var="position">
                                <li><a href="javascript:void(0)" onclick="showStaff(this)" data-departmentID="${departmentFrame.departmentVO.departmentID}" data-positionID="${position.positionID}">${position.positionName}</a>
								     <ul id="showStaff${departmentFrame.departmentVO.departmentID}${position.positionID}"></ul>
								     
								</li>							 
							</c:forEach> --%>
							</ul>
						   
					<%-- </c:forEach> --%>
				</div>
				</div>

			</div>
		</div>
	</div>
	<div class="modal fade" id="addPosition" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog" role="document" style="width:25%">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">新增职位</h4>
	      </div>
	      <div class="modal-body">
	        <form class="form-horizontal">
	          <input type="hidden" id="departmentId">
	          <div class="form-group">
	            <label class="control-label col-sm-4">职位名称</label>
	            <div class="col-sm-7">
	            <input class="form-control" autoComplete="off" id="positionName">
	            </div>
	          </div>
	          <div class="form-group">
	            <label class="control-label col-sm-4">职位类型</label>
	            <div class="col-sm-7">
	            <select class="form-control" id="positionType">
	            	<option value="1">白领</option>
	            	<option value="2">蓝领</option>
	            </select>
	            </div>
	          </div>
	        </form>
	      </div> 
	      <div class="modal-footer" style="text-align:center">
	        <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="savePositon()">确认</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      </div>
	    </div>
	  </div>
	</div>
<script src="/assets/js/layer/layer.js"></script>
<script>
		$(function (){
			$('#deptInsert').click(function (){
				if(!canAddDepartemnt){
					layer.alert("请先选中需要添加部门的上级部门！",{offset:'100px'});
				}else{
					layer.prompt({title: '请填写部门名称', formType: 3,offset:'100px'},function (content,index){
						layer.close(index);
						if(!content){
							layer.alert("请填写部门名称!",{offset:'100px'})
							return;
						}
						Load.Base.LoadingPic.FullScreenShow(null);
						$.ajax({
							url:'/personal/addDept',
							data:{id:deptId?deptId:0,name:content,companyId:'${companyID}',level:(level?level:1)},
							type:'post',
							dataType:'json',
							success:function (result){
									layer.alert("添加成功",{offset:'100px'},function(index){
										layer.close(index);
										storage.setItem("departmentId",deptId);
										location.reload();
										Load.Base.LoadingPic.FullScreenShow(null);
									})
							
							},
							complete:function(){
								Load.Base.LoadingPic.FullScreenHide();
							}
						})
					});
				}
			});
			$('#positionInsert').click(function (){
				if(!canAddDepartemnt||!deptId){
					layer.alert("请先选中需要添加职位的上级部门！",{offset:'100px'});
				}else{
					$("#departmentId").val(deptId);
					$("#addPosition").modal("show");
/* 					
					layer.prompt({title: '请填写职位名称', formType: 3,offset:'100px'},function (content,index){
						layer.close(index);
						if(!content){
							layer.alert("请填写职位名称!",{offset:'100px'})
							return;
						}
						Load.Base.LoadingPic.FullScreenShow(null);
						$.ajax({
							url:'/personal/addPosition',
							data:{id:deptId,name:content,companyId:'${companyID}'},
							type:'post',
							dataType:'json',
							success:function (result){
									layer.alert("添加成功",{offset:'100px'},function(index){
										layer.close(index);
										storage.setItem("departmentId",deptId);
										location.reload();
										Load.Base.LoadingPic.FullScreenShow(null);
									})
								
							},
							complete:function(){
								Load.Base.LoadingPic.FullScreenHide();
								}
						})
					}); */
				}
			});
			$('#modifyNode').click(function (){
				if(!node){
					layer.alert("请先选中需要修改的节点！",{offset:'100px'});
				}else{
					var nodeName = $(node).text();
					var positionId = $(node).attr("data-positionid");
					var departmentId = $(node).attr("data-departmentid");
					var type;
					var id;
					if(empty(positionId)){
						type = "Department";
						id = departmentId;
					}else{
						type = "Position";
						id = positionId;
					}
					layer.prompt({title: '请填写修改内容', formType: 3,offset:'100px', value: nodeName},function (content,index){
						layer.close(index);
						if(!content){
							layer.alert("请填写修改内容!",{offset:'100px'})
							return;
						}
						Load.Base.LoadingPic.FullScreenShow(null);
						$.ajax({
							url:'/structure/modifyNode',
							data:{'id':id,'name':content,'type':type},
							type:'post',
							dataType:'json',
							success:function (result){
									layer.alert("修改成功",{offset:'100px'},function(index){
										layer.close(index);
										storage.setItem("departmentId",departmentId);
										location.reload();
										Load.Base.LoadingPic.FullScreenShow(null);
									})
							},
							complete:function(){
								Load.Base.LoadingPic.FullScreenHide();
								}
						})
					});
				}
			});
			$('#deleteNode').click(function (){
				if(!node){
					layer.alert("请先选中需要删除的节点！",{offset:'100px'});
				}else{
					var nodeName = $(node).text();
					var positionId = $(node).attr("data-positionid");
					var departmentId = $(node).attr("data-departmentid");
					var type;
					var id;
					if(empty(positionId)){
						type = "Department";
						id = departmentId;
					}else{
						type = "Position";
						id = positionId;
					}
					var msg;
					if(type=="Department"){
						msg = '确定删除部门【'+nodeName+'】';
					}else{
						msg = '确定删除职位【'+nodeName+'】';
					}
					layer.confirm(msg,{offset:'100px'},function(index){
						layer.close(index);
						Load.Base.LoadingPic.FullScreenShow(null);
						$.ajax({
							url:'/structure/deleteNode',
							data:{'id':id,'type':type,'companyId':'${companyID}'},
							type:'post',
							dataType:'json',
							success:function (result){
								var canDelete = result.canDelete;
								if(canDelete=="false"){
									layer.alert("节点下面有员工，无法删除",{offset:'100px'},function(index){
										layer.close(index);
										var parentNode = $(node).parent().parent().prev();
										departmentId = $(parentNode).attr("data-departmentid");
										storage.setItem("departmentId",departmentId);
										location.reload();
										Load.Base.LoadingPic.FullScreenShow(null);
									})
								}else{
									layer.alert("删除成功",{offset:'100px'},function(index){
										layer.close(index);
										var parentNode = $(node).parent().parent().prev();
										departmentId = $(parentNode).attr("data-departmentid");
										storage.setItem("departmentId",departmentId);
										location.reload();
										Load.Base.LoadingPic.FullScreenShow(null);
									})
								}
							},
							complete:function(){
								Load.Base.LoadingPic.FullScreenHide();
								}
						})
					});
				}
			});
		});
	function empty(obj){
		
		if(null==obj || ""==obj || undefined==obj){
			return true;
		}
		return false;
	}
	function savePositon(){
		var deptId = $("#departmentId").val();
		var name = $("#positionName").val();
		if(!name.trim()){
			layer.alert("职位名称不能为空",{offset:'100px'});
			return;
		}
		var positionType = $("#positionType option:selected").val();
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'/personal/addPosition',
			data:{id:deptId,name:name,companyId:'${companyID}','positionType':positionType},
			type:'post',
			dataType:'json',
			success:function (result){
					layer.alert("添加成功",{offset:'100px'},function(index){
						layer.close(index);
						storage.setItem("departmentId",deptId);
						location.reload();
						Load.Base.LoadingPic.FullScreenShow(null);
					})
				
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
				}
		})
	}
</script>
</body>
</html>