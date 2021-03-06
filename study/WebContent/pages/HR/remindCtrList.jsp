<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
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
      	<s:set name="panel" value="'contract'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">合同提醒列表</h3>
         <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">员工（乙方）</th> 
                  <th class="col-sm-2">公司名称（甲方）</th>
                  <th class="col-sm-1">开始时间</th>
                  <th class="col-sm-1">结束时间</th>
                  <th class="col-sm-1">操作</th>
                </tr>
              </thead> 
              <tbody>
              	<c:if test="${not empty contracts}">
              	<s:set name="ctract_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="ctract" value="#request.contracts" status="count">
              		<tr>
              			<td><s:property value="#ctract_id+1"/></td>   
              			<td><s:property value="#ctract.partyB"/></td>
              			<td><s:if test="#ctract.partyA==1">南通智造链科技有限公司</s:if>
              			    <s:if test="#ctract.partyA==2">南通迷丝茉服饰有限公司</s:if>
              			    <s:if test="#ctract.partyA==3">广州亦酷亦雅电子商务有限公司</s:if>
              			    <s:if test="#ctract.partyA==4">南通智造链电子商务有限公司</s:if>
              			</td>              			
              			<td><s:property value="#ctract.beginDate"/></td>              			
              			<td><s:property value="#ctract.endDate"/></td>              			
              			<td>
              			<a onclick="goPath('HR/contract/getContractByContractID?contractID=<s:property value="#ctract.contractID"/>&selectedPanel=remindCtrList')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
								<use xlink:href="#icon-Detailedinquiry"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a onclick="goPath('HR/contract/renewContract?contractID=<s:property value="#ctract.contractID"/>')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="续签" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
							</svg>
              			</a>
              			</td>              			
              		</tr>
              		<s:set name="ctract_id" value="#ctract_id+1"></s:set>
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
