<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
	});
	
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
<style>
.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'certificateList'"></s:set>
      	<s:set name="selectedPanel" value="'certificateList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/certificate/toCertificateUseLog" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">证件使用信息查询</h3> 
			   <div class="form-group" >
			  <label class="col-sm-1 control-label">开始</label>
			  <div class="col-sm-2">
			  		<input style="display:none" name="certificateId" value="${certificateId}"/>
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="申请时间">
			    </div>
			    <label class="col-sm-1 control-label">结束</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker()" placeholder="申请时间" >
			    </div>
			  </div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1)'" style="margin-left:15px;">返回</button>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">申请人</th>
                  <th class="col-sm-1">申请时间</th>
                  <th class="col-sm-2">申请原因</th>
                  <th class="col-sm-1">是否外借</th>
                  <th class="col-sm-1">开始时间</th> 
                  <th class="col-sm-1">结束时间</th> 
                  <th class="col-sm-1">实际使用开始时间</th> 
                  <th class="col-sm-1">实际使用结束时间</th> 
                </tr>
              </thead>
              <tbody id="tbody_">
              	<c:if test="${not empty certificateBorrowLst}">
              	<c:forEach items="${certificateBorrowLst}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex}</td>
              		<td>${item.userName }</td>
              		<td>${item.addTime }</td>
              		<td>${item.reason }</td>
              		<td>${item.isBorrow=='0'?'否':'是'}</td>
              		<td>${item.startTime }</td>
              		<td>${item.endTime }</td>
              		<td>${item.realStartTime }</td>
              		<td>${item.realEndTime }</td>
              		</tr>
              	</c:forEach>
              	</c:if>
              </tbody>
            </table>
          </div>
          <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
  </body>
</html>
