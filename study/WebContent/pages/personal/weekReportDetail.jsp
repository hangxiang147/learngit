<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		if('${disabled}'=='true'){
			//$("#addButton").attr("disabled","disabled");
		}
		$("[data-toggle='tooltip']").tooltip();
	});

	function search() {
		window.location.href = "personal/weekReportDetail?" + $("#workReportDetailForm").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}

</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	#weekReport tr td{vertical-align:middle}
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
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="workReportDetailForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">工作周报明细</h3>
          	
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="beginDate" value="${beginDate }"
			    	onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endDate" value="${endDate }"
			    	onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
          <button id="addButton" class="btn btn-primary" onclick="goPath('personal/newWeekReport')">新增工作周报</button>&nbsp;<span style="color:red">（周报汇报时间：周五下班至周一上班期间）</span>
          <div data-toggle='tooltip' onclick="location.href='pages/personal/video.jsp'" title="演示视频" style="font-size: 20px;cursor:pointer;vertical-align:middle;margin-top:-3px" class="glyphicon glyphicon-facetime-video"></div>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table id="weekReport" class="table table-striped">
              <thead>
                <tr>
                  <th style="width:8%">操作</th>
                  <th style="width:12%">提交时间</th>
                  <th style="width:20%">本周工作</th>
                  <th style="width:20%">风险点或问题</th>
                  <th style="width:20%">下周工作计划</th>
                  <th style="width:20%">本周工作总结</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${weekReportList}" var="weekReport">
              		<tr>
              		<td rowspan="${weekReport.maxRow}">
              		<a onclick="goPath('personal/weekReportOver?weekReportId=${weekReport.id}')" href="javascript:void(0)">
              				   <svg class="icon" aria-hidden="true" title="生成表格" data-toggle="tooltip">
	             					<use xlink:href="#icon-table"></use>
	             			   </svg>
              		</a></td>
              		<td rowspan="${weekReport.maxRow}">${weekReport.addTime}</td>
              		<c:forEach begin="0" end="${weekReport.maxRow-1}" varStatus="status">
              			<c:if test="${status.index!=0}"><tr></c:if>
              				<td>${weekReport.thisWeekWorkVo.content[status.index]}</td>
              				<td>${weekReport.riskVo.riskDescription[status.index]}</td>
              				<td>${weekReport.nextWeekWork.content[status.index]}</td>
              			<c:if test="${status.index==0}">
              			<td rowspan="${weekReport.maxRow}">${weekReport.weekWorkSummary}</td>
              			</c:if>
              		</c:forEach>
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
