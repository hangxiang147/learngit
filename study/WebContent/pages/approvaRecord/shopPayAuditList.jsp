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
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#applyer'),function ($item){
			$("input[name='applyerId']").val($item.data("userId"));
		});
	});
	$(document).click(function (event) {
		if ($("input[name='applyerId']").val()=='')
		{
			$("#applyer").val("");
		}
	}); 
  	function checkEmpty(){
  		if($("#applyer").val()==''){
  			$("input[name='applyerId']").val('');
  		}
  	}
</script>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus{text-decoration:none}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'approvaRecord'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form class="form-horizontal" action="administration/process/findShopPayAuditList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
       	  <h3 class="sub-header" style="margin-top:0px;">店铺付费申请列表</h3>
       	  <div class="form-group">
				<label for="reimbursementNo" class="col-sm-1 control-label">发起人</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="applyer" name="applyer" value="${applyer}" onkeyup="checkEmpty()">
			    	<input type="hidden" name="applyerId" value="${applyerId}">
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="beginDate" name="beginDate" value="${beginDate }" 
			    	onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="endDate" name="endDate" value="${endDate }"
			    	 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d', minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间">
			    </div>
			    <div class="col-sm-2" style="padding-top:2px;">
			    	<button type="submit" class="btn btn-primary">查询</button>
			    </div>
		  </div>
		 </form>
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:10%">审批节点</th>
                  <th style="width:8%">发起人</th>
                  <th style="width:15%">流程开始时间</th>
                  <th style="width:15%">流程结束时间</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${shopPayApplyVos}" var="shopPayApplyVo" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${shopPayApplyVo[1]}</td>
              			<td>${shopPayApplyVo[4]}</td>
              			<td>${shopPayApplyVo[2]}</td>
              			<td>${shopPayApplyVo[3]}</td>
              			<td>
              				<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=${shopPayApplyVo[0]}&businessType=shopPayApply&from=approvaRecord&selectedPanel=shopPayList')" href="javascript:void(0)">
              					<svg class="icon" aria-hidden="true" title="查看申请详情" data-toggle="tooltip">
	             					<use xlink:href="#icon-Detailedinquiry"></use>
	             				</svg>
              				</a>
              				&nbsp;
              				<a onclick="goPath('administration/process/processHistoryForApprovaRecord?processInstanceID=${shopPayApplyVo[0]}&selectedPanel=shopPayList')" href="javascript:void(0)">
              					<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
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
