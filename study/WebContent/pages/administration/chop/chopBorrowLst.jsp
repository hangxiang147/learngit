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
      	<s:set name="panel" value="'chopBorrowLst'"></s:set>
      	<s:set name="selectedPanel" value="'chopBorrowLst'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/chop/toChopBorrowLstPage">     	  
          	<h3 class="sub-header" style="margin-top:0px;">公章申请列表</h3> 
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">公章名称</label>
			  	<div class="col-sm-2" id="joinPerson" ><input type="text" class="form-control" name="name" value="${name}"></div>
			  	<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			  </div>
          </form>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-3">公章名称</th>
                  <th class="col-sm-2">申请人</th>
                  <th class="col-sm-2">申请时间</th>
                  <th class="col-sm-1">操作</th> 
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${chopBorrowLst}" var="chopBorrow" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex}</td>
              		<td>${chopBorrow.chop_Name }</td>
              		<td>${chopBorrow.userName }</td>
              		<td><fmt:formatDate value="${chopBorrow.addTime}"  type="both" /></td>
              		<td>
            			<a href="/administration/chop/showChopBorrowDetail?chopBorrowId=${chopBorrow.chopBorrow_Id}">查看</a>
              		</td>
              		</tr>
              	</c:forEach>
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
