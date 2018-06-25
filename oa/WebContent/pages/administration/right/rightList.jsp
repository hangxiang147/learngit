<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
	.table td{word-break: break-all}
	</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'right'"></s:set>
        <%@include file="/pages/administration/right/panel.jsp" %>	
         <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
         <h3 class="sub-header" style="margin-top:0px;">权限信息管理</h3> 
<!--           <form  id="form_" action="/administration/right/saveRight" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label">名称</label>
			    <div class="col-sm-2">
			    	<input name="rightName"  class="form-control" required="required"/>
			    </div>
				<label for="name" class="col-sm-2 control-label">关键code</label>
			    <div class="col-sm-2">
			    	<input name="rightCode" class="form-control" required="required"/>
			    </div>
			    <div class="col-sm-2">
					<button type="submit"  class="btn btn-primary">新增</button>
			    </div>
			</div>
          </form> -->
          <button type="button"  class="btn btn-primary" onclick="newRight()">新增</button>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1" style="width:5%">序号</th>
                  <th class="col-sm-1">权限名称</th>
                  <th class="col-sm-1">权限code</th>
                  <th class="col-sm-1" style="width:7%">权限类型</th>
                  <th class="col-sm-1">流程key值</th>
                  <th class="col-sm-1">节点key值</th>
                  <th class="col-sm-1">配置类型</th>
                  <th class="col-sm-1">配置key值</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty result}">
              	<c:forEach items="${result}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item[0] }</td>
            		<td>${item[1] }</td>
            		<td>${item[3]==1?'流程权限':'页面权限'}</td>
            		<td>${item[4]==null||item[4]=='' ? '——':item[4] }</td>
            		<td>${item[5]==null||item[5]=='' ? '——':item[5] }</td>
            		<td>${item[6]==null||item[6]=='' ? '——':item[6]==1?'个人':'候选人' }</td>
            		<td>${item[7]==null||item[7]=='' ? '——':item[7] }</td>
              		</tr>
              	</c:forEach>
              	</c:if>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  <script src="/assets/js/layer/layer.js"></script>
  <script>
  $(function (){
	  $('#form_').submit(function(){
		  var name=$('input[name="rightName"]').val();
		  var code=$('input[name="rightCode"]').val();
		  var isPass=true;
		  $('table tbody').find("tr").each(function (){
			  var name_=$.trim($(this).find("td:eq(1)").html());
			  var code_=$.trim($(this).find("td:eq(2)").html());
			  if(name==name_){
				  isPass=false;
				  alert("名称已经存在，请重新输入")
				  $('input[name="rightName"]').val("").focus();
				  return false;
			  }
			  if(code==code_){
				  isPass=false;
				  alert("关键code已经存在，请重新输入");
				  $('input[name="rightCode"]').val("").focus();
				  return false;
			  }
		  })
		  return isPass;
		  
	  })
  })
  function newRight(){
	  $("#right").modal('show');
  }
  function selectRightType(){
	  var process = $("select[name='permission.process']").val();
	  if(!process){
		  return;
	  }
	  if(process=='0'){
		  $("#process").css("display","none");
		  $("textarea[name='permission.processKeys']").removeAttr("required");
		  $("input[name='permission.nodeKey']").removeAttr("required");
		  $("input[name='permission.mapKey']").removeAttr("required");
		  $("select[name='permission.type']").removeAttr("required");
	  }else{
		  $("#process").css("display","block");
		  $("textarea[name='permission.processKeys']").attr("required","required");
		  $("textarea[name='permission.nodeKey']").attr("required","required");
		  $("textarea[name='permission.mapKey']").attr("required","required");
		  $("select[name='permission.type']").attr("required","required");
	  }
  }
  function checkInfo(){
	  var isPass=true;
	  var name=$('input[name="permission.permissionName"]').val();
	  var code=$('input[name="permission.permissionCode"]').val();
	  $('table tbody').find("tr").each(function (){
		  var name_=$.trim($(this).find("td:eq(1)").html());
		  var code_=$.trim($(this).find("td:eq(2)").html());
		  if(name==name_){
			  isPass=false;
			  layer.alert("权限名称已经存在，请重新输入",{offset:'100px'});
			  $('input[name="permission.permissionName"]').val("").focus();
			  return;
		  }
		  if(code==code_){
			  isPass=false;
			  layer.alert("权限code已经存在，请重新输入",{offset:'100px'});
			  $('input[name="permission.permissionCode"]').val("").focus();
			  return;
		  }
	  })
	  if(!isPass){
		  return false;
	  }
	  $("#right").modal("hide");
	  Load.Base.LoadingPic.FullScreenShow(null);
  }
  </script>
  <div id="right" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form  class="form-horizontal" action="administration/right/saveRight" onsubmit="return checkInfo()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加权限</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-4 control-label">权限名称<span style="color:red"> *</span></label>
					<div class="col-sm-7">
						<input class="form-control" autocomplete="off" required="required"
						 name="permission.permissionName"/>
					</div>
				</div>
				<div class="form-group">
	    		<label class="col-sm-4 control-label">权限code<span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
	    			<input class="form-control" autocomplete="off" required="required"
					 name="permission.permissionCode"/>
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    		<label class="col-sm-4 control-label">权限类型<span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
	    			<select class="form-control" name="permission.process" required onchange="selectRightType()">
	    				<option value="">请选择</option>
	    				<option value="0">页面权限</option>
	    				<option value="1">流程权限</option>
	    			</select>
	    		</div>
	    	  </div> 
	    	  <div id="process" style="display:none">
	    	  <div class="form-group">
	    		<label class="col-sm-4 control-label">流程key值<span style="font-size:12px;font-weight:bold">（多个按英文逗号隔开）</span><span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
					 <textarea class="form-control" rows="3" name="permission.processKeys"></textarea>
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    		<label class="col-sm-4 control-label">节点key值<span style="font-size:12px;font-weight:bold">（多个按英文逗号隔开）</span><span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
					 <textarea class="form-control" rows="3" name="permission.nodeKey"></textarea>
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    		<label class="col-sm-4 control-label">配置key值<span style="font-size:12px;font-weight:bold">（多个按英文逗号隔开）</span><span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
					 <textarea class="form-control" rows="3" name="permission.mapKey"></textarea>
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    		<label class="col-sm-4 control-label">配置类型<span style="color:red"> *</span></label>
	    		<div class="col-sm-7">
	    			<select class="form-control" name="permission.type">
	    				<option value="">请选择</option>
	    				<option value="1">个人</option>
	    				<option value="2">候选人</option>
	    			</select>
	    		</div>
	    	  </div>
			  </div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</div>
		</form>
	</div>
	</div>
  </body>
</html>
