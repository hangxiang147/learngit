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
		<%@include file="/pages/attendance/salayPanel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<h3 class="sub-header" style="margin-top:0px;">工资单更改记录</h3>
		  <form action="HR/staffSalary/findChangeStaffSalaryList" class="form-horizontal">
		  	<div class="form-group">
		  		<label class="col-sm-1 control-label">姓名</label>
		  		<div class="col-sm-2">
		  			<input class="form-control" name="changeSalaryDetailVo.requestUserName" autoComplete="off" value="${changeSalaryDetailVo.requestUserName}">
		  		</div>
		  		<label class="col-sm-1 control-label">员工状态</label>
		  		<div class="col-sm-2">
		  			<select class="form-control" name="changeSalaryDetailVo.staffStatus">
		  				<option ${changeSalaryDetailVo.staffStatus=='5' ? 'selected':''} value="5">在职</option>
		  				<option ${changeSalaryDetailVo.staffStatus=='4' ? 'selected':''} value="4">离职</option>
		  			</select>
		  		</div>
		  		<label class="col-sm-1 control-label">流程状态</label>
		  		<div class="col-sm-2">
		  			<select class="form-control" name="changeSalaryDetailVo.status">
		  				<option value="">全部</option>
		  				<option ${changeSalaryDetailVo.status=='进行中' ? 'selected':''} value="进行中">进行中</option>
		  				<option ${changeSalaryDetailVo.status=='1' ? 'selected':''} value="1">已审批</option>
		  				<option ${changeSalaryDetailVo.status=='2' ? 'selected':''} value="2">未通过</option>
		  			</select>
		  		</div>
		  	</div>
		  	<div class="form-group">
		  		<label class="col-sm-1 control-label">年份</label>
		  		<div class="col-sm-2">
		  			<input class="form-control" name="changeSalaryDetailVo.year" autoComplete="off" value="${changeSalaryDetailVo.year}">
		  		</div>
		  		<label class="col-sm-1 control-label">月份</label>
		  		<div class="col-sm-2">
					<input class="form-control" name="changeSalaryDetailVo.month" autoComplete="off" value="${changeSalaryDetailVo.month}">
		  		</div>
		  		<div class="col-sm-1">
		  			<button type="submit" class="btn btn-primary">查询</button>
		  		</div>
		  	</div>
		  </form>
		  <div class="sub-header"></div>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                	<td style="width:7%">序号</td>
                	<td>姓名</td>
                	<td>部门</td>
                	<td>工资月份</td>
                	<td>发起人</td>
                	<td>发起时间</td>
                	<td>当前处理人</td>
                	<td>状态</td>
                	<td style="width:10%">操作</td>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${changeStaffSalaryList}" var="changeStaffSalary" varStatus="status">
              		<tr>
              			<td>${status.index+1+(page-1)*limit}</td>
              			<td>${changeStaffSalary.requestUserName}</td>
              			<td>${changeStaffSalary.department}</td>
              			<td>${changeStaffSalary.year}-${changeStaffSalary.month}</td>
              			<td>${changeStaffSalary.userName}</td>
              			<td><fmt:formatDate value="${changeStaffSalary.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
              			<td>${changeStaffSalary.assigneeUserName==null ? '——':changeStaffSalary.assigneeUserName}</td>
              			<td>${changeStaffSalary.status}</td>
              			<td>
		              		<a class="hand" onclick="goPath('HR/staffSalary/showSalaryChangeDetail?processInstanceId=${changeStaffSalary.processInstanceID}')">
								<svg class="icon" aria-hidden="true" title="更改明细" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>	
							</a>
							&nbsp;
							<a class="hand" onclick="goPath('HR/staffSalary/processHistory?processInstanceID=${changeStaffSalary.processInstanceID}&selectedPanel=changeStaffSalaryList')">
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
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