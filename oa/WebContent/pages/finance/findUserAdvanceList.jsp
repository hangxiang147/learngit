<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<meta http-equiv="expires" content="0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
	$(function() {
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
        <s:set name="panel" value="'myHistoryProcess'"></s:set>
        <s:set name="selectedPanel" value="'AdvanceList'"></s:set>    
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       
        <form id="queryReimbursementForm" class="form-horizontal" action="/administration/process/findUserAdvanceList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
       	  <h3 class="sub-header" style="margin-top:0px;">预付列表</h3>
       	  <div class="form-group" style="margin-top:10px">
				<label for="beginDate" class="col-sm-1 control-label">预付单号</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" id="No_code" name="code" value="${code}" placeholder="预付编号">
			    </div>
			    <label class="col-sm-1 control-label">发起人</label>
				<div class="col-sm-2">
			    	<input type="text" autoComplete="off" id="applyer" class="form-control" name="applyer" onkeyup="checkEmpty()" value="${applyer}" >
			    	<input type="hidden" name="applyerId" value="${applyerId}">
			    </div>
		  </div>
		  <div class="form-group" style="margin-top:10px">
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-1"></div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-1">
			    <button type="submit" class="btn btn-primary">查询</button>
			    </div>
			</div>
		 </form>
			
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>  
                  <th>发起人</th>  
                  <th>预付编号</th>             
                  <th>预付金额</th>
                  <th>预付原因</th>
                  <th>流程开始时间</th>
                  <th>流程结束时间</th>
                  <th>任务开始时间</th>   
                  <th>任务结束时间</th>
                  <th>操作</th>             
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty reimbursementList}">
              	<s:set name="reimbursement_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="reimbursementVO" value="#request.reimbursementList" status="count">
              		<tr>
              			<td class="col-sm-1" style="width:5%"><s:property value="#reimbursement_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#reimbursementVO[10]"/></td>
              			<td class="col-sm-1"><s:property value="#reimbursementVO[6]"/></td>
              			<td class="col-sm-1"><s:property value="#reimbursementVO[7]"/></td>
              			<td class="col-sm-1"><s:property value="#reimbursementVO[8]"/></td>
              			<td class="col-sm-1"><s:date format="yyyy-MM-dd hh:mm" name="#reimbursementVO[2]"/></td>
              			<td class="col-sm-1"><s:date format="yyyy-MM-dd hh:mm" name="#reimbursementVO[3]" /></td>              			
              			<td class="col-sm-1"><s:date format="yyyy-MM-dd hh:mm" name="#reimbursementVO[4]"/></td>  
              			<td class="col-sm-1"><s:date format="yyyy-MM-dd hh:mm" name="#reimbursementVO[5]"/></td>  
              			<td class="col-sm-1" style="width:7%">
              			<a onclick="goPath('finance/reimbursement/getAdvanceDetail?type=2&processInstanceID=<s:property value='#reimbursementVO[0]'/>')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="看预付单详情" data-toggle="tooltip">
    						<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
						</a>
						&nbsp;
						<a onclick="goPath('finance/reimbursement/getReimbursementProcess?type=2&processInstanceID=<s:property value='#reimbursementVO[0]'/>')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="流程审批详情" data-toggle="tooltip">
    						<use xlink:href="#icon-liucheng"></use>
						</svg>
						</a>
						</td>           			
              		</tr>
              		<s:set name="reimbursement_id" value="#reimbursement_id+1"></s:set>
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
           	<ul class="dropdown-menu limit" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
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
    <script>
  	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#applyer'),function ($item){
			$("input[name='applyerId']").val($item.data("userId"));
		});
	});
  	function checkEmpty(){
  		if($("#applyer").val()==''){
  			$("input[name='applyerId']").val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='applyerId']").val()=='')
		{
			$("#applyer").val("");
		}
	}); 
      </script>
  </body>
</html>
