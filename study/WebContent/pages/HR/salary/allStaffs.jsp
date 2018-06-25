<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
	function toAlterStaffSalary(userId){
		$.ajax({
			url:'HR/staffSalary/checkApplyAlterSalary',
			data:{'userId':userId},
			success:function(data){
				if(!data.maintainSalary){
					layer.alert("请先维护下员工的薪资标准", {offset:'100px'});
				}else if(data.hasApplyAlterSalary){
					layer.alert("调薪申请在审批中，不可再次申请", {offset:'100px'});
				}else if(!data.meetManageCondition){
					layer.alert("管理的人数达不到指标值，不予提薪", {offset:'100px'});
				}else{
					location.href="HR/staffSalary/toAlterStaffSalary?userId="+userId;
					Load.Base.LoadingPic.FullScreenShow(null);
				}
			}
		});
	}
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
          <form id="myForm" class="form-horizontal" action="HR/staff/findAllInJobStaffs">
          
          	<h3 class="sub-header" style="margin-top:0px;">薪资调整</h3>
          	<div class="form-group">
				<label for="staffName" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" name="staffName" value="${staffName}" >
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="submit" class="btn btn-primary">查询</button>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>性别</th>
                  <th>部门</th>
                  <th>联系电话</th>
                  <th>入职日期</th>
                  <th>目前薪资</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty allStaffs}">
              	<c:set var="staff_id" value="${(page-1)*limit}"></c:set>
              	<c:forEach items="${allStaffs}" var="staffVO">
              	    <tr>
              			<td>${staff_id+1}</td>
              			<td>${staffVO.staffName}</td>
              			<td>
              			   <svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
						   </svg>
              			</td>
              			<td>${staffVO.department}</td>
              			<td>${staffVO.telephone}</td>
              			<td><fmt:formatDate value="${staffVO.entryDate}" pattern="yyyy-MM-dd"/></td>
              			<td>${staffVO.standardSalary}</td>
              			<td>
              			<a onclick="goPath('HR/staffSalary/staffSalaryAlterations?userId=${staffVO.userID}')" href="javascript:void(0)">
              			   	<svg class="icon" aria-hidden="true" title="历史记录" data-toggle="tooltip">
								<use xlink:href="#icon-jilu"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a onclick="toAlterStaffSalary('${staffVO.userID}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="申请调薪" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
							</svg>
              			</a>
              			</td>
              		</tr>
              		<c:set var="staff_id" value="${staff_id+1}"></c:set>
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
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
  </body>
</html>
