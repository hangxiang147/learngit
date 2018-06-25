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
		$("[data-toggle='tooltip']").tooltip();
	});

	function search() {
		window.location.href = "personal/workReportDetail?" + $("#workReportDetailForm").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}

</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
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
          	<h3 class="sub-header" style="margin-top:0px;">工作汇报明细</h3>
          	
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="workReportDetailVO.beginDate" value="${workReportDetailVO.beginDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="workReportDetailVO.endDate" value="${workReportDetailVO.endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
            <a class="btn btn-primary"  onclick="goPath('personal/newWorkReport')" href="javascript:void(0)">新增工作日报</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>操作</th>
                  <th>汇报日期</th>
                  <th>提交时间</th>
                  <th>工作内容</th>
                  <th>数量</th>
                  <th>下达任务人</th>
                  <th>完成情况</th>
                  <th>完成用时</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty workReportDetailVOs}">
              	<s:iterator id="workReportDetailVO" value="#request.workReportDetailVOs" status="count">
              		<tr>
              			<td class="col-sm-1" rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>">
              			<a onclick="goPath('personal/workReportOver?reportDate=<s:property value="#workReportDetailVO.reportDate"/>')" href="javascript:void(0)">
              				   <svg class="icon" aria-hidden="true" title="生成表格" data-toggle="tooltip">
	             					<use xlink:href="#icon-table"></use>
	             			   </svg>
              			</a>
              			</td>
              			<td class="col-sm-1" rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.reportDate"/><br>
              			<s:property value="#workReportDetailVO.weekDay"/></td>
              			<td class="col-sm-1" rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.addTime"/></td>
              			
              	         <s:iterator id="workContent" value="#workReportDetailVO.workContent" status="sta">
              				<s:if test="#sta.index!=0"><tr></s:if>
				             <td class="col-sm-4"><s:property value="#workContent" /></td> 
				             <td class="col-sm-1"><s:property value="#workReportDetailVO.quantities[#sta.index]" /></td> 
				             <td class="col-sm-1"><s:property value="#workReportDetailVO.assignTaskName[#sta.index]" /></td>
				             <td class="col-sm-1"><s:property value="#workReportDetailVO.completeState[#sta.index]" /></td>
				             <td class="col-sm-1"><s:property value="#workReportDetailVO.workHours[#sta.index]" /></td></tr>
				            </s:iterator>
 
              	</s:iterator>
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
