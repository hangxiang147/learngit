<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
</script>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<h3 class="sub-header" style="margin-top:0px;">薪资调整历史记录
			<button type="button" style="float:right" onclick="history.go(-1)" class="btn btn-default">返回</button>
		</h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                	<td style="width:7%">序号</td>
                	<td>调薪人员</td>
                	<td>发起人</td>
                	<td>发起时间</td>
                	<td>当前处理人</td>
                	<td>状态</td>
                	<td style="width:10%">操作</td>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${staffSalaryAlterations}" var="staffSalaryAlteration" varStatus="status">
              		<tr>
              			<td>${status.index+1}</td>
              			<td>${staffSalaryAlteration.staffName}</td>
              			<td>${staffSalaryAlteration.userName}</td>
              			<td><fmt:formatDate value="${staffSalaryAlteration.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
              			<td>${staffSalaryAlteration.assigneeUserName==null ? '——':staffSalaryAlteration.assigneeUserName}</td>
              			<td>${staffSalaryAlteration.status}</td>
              			<td>
		              		<a class="hand" onclick="goPath('HR/staffSalary/showSalaryAlterationDetail?id=${staffSalaryAlteration.id}')">
								<svg class="icon" aria-hidden="true" title="调薪明细" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>	
							</a>
							&nbsp;
							<a class="hand" onclick="goPath('HR/staffSalary/processHistory?processInstanceID=${staffSalaryAlteration.processInstanceID}&selectedPanel=salaryAlteration')">
								<svg class="icon" aria-hidden="true" title="流程详情" data-toggle="tooltip">
									<use xlink:href="#icon-liucheng"></use>
								</svg>	
							</a>
              			</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
</body>
</html>