<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
      	<s:set name="selectedPanel" value="'newContractBorrow'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
       <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/personal/newContractBorrow" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">合同信息管理</h3> 
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">合同名称</label>
			  	<div class="col-sm-2" ><input type="text" autoComplete="off" class="form-control" name="name" value="${name}"></div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">编号</th>
                  <th class="col-sm-1">名称</th>
                  <th class="col-sm-2">描述</th>
                  <th class="col-sm-1">审核人</th>
                  <th class="col-sm-1">保管人</th> 
                  <th class="col-sm-2">操作</th> 
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty contracts}">
              	<c:forEach items="${contracts}" var="contract" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${contract.contractID}</td>
              		<td>${contract.name}</td>
              		<td>${contract.description}</td>
              		<td>${contract.subject_personName}</td>
              		<td>${contract.store_personName}</td>
              		<td>
            			<input class="btn btn-primary" value="申请借阅" onClick="new_task_for_ContractBorrow('${contract.id}')" style="width:100px"/>
            			<input class="btn btn-primary" value="查看附件" onClick="showAttachment('${contract.id}')" style="width:100px"/>
              		</td>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
          <script>
         var new_task_for_ContractBorrow = function (id){
        	 location.href="/personal/toBeginContractBorrowPage?id="+id;
        	 //location.href ="/pages/personal/certificateBorrowStart.jsp";
        	 Load.Base.LoadingPic.FullScreenShow(null);
         }
         function showAttachment(id){
         	location.href="administration/contractManage/showAttachment?contractId="+id;
         	Load.Base.LoadingPic.FullScreenShow(null);
         }
          </script>
        </div>
      </div>
    </div>
</body>
</html>