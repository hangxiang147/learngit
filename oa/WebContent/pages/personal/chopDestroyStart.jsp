<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startChopDestroy" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起印章缴销申请</h3> 			 
				  <input style="display:none" name="chopDestroyVo.chopId"  value="${chop.chop_Id}"/>
			  	  <input style="display:none" name="chopDestroyVo.chopName"  value="${chop.name}"/>
  			  	  <input style="display:none" name="chopDestroyVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="chopDestroyVo.userID"  value="${staff.userID}"/>  
  			  	  <input style="display:none" name="chopDestroyVo.description"  value="${chop.description}"/>  
  			  	  <input style="display:none" name="chopDestroyVo.type"  value="${chop.type}"/>  
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">印章名称</label>
			  	<div class="col-sm-2">${chop.name }</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">缴销原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  	<textarea id="destroyReason" rows="3" required class="form-control" name="chopDestroyVo.destroyReason" maxLength="200"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:5%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      </body>
</html>