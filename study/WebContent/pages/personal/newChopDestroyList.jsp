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
      
        <%@include file="/pages/administration/panel.jsp" %>	
       <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/personal/toChopListPage">     	  
          	<h3 class="sub-header" style="margin-top:0px;">印章信息管理</h3> 
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">印章名称</label>
			  	<div class="col-sm-2" id="joinPerson" ><input type="text" class="form-control" name="name" value="${name}"></div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">名称</th>
                  <th class="col-sm-1">类型</th>
                  <th class="col-sm-2">描述</th>
                  <th class="col-sm-1">审核人</th>
                  <th class="col-sm-1">保管人</th> 
                  <th class="col-sm-2">操作</th> 
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty chops}">
              	<c:forEach items="${chops}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item.name}</td>
              		<td><c:if test="${item.type=='1'}">
        	  				公章
        	  			</c:if>
        	  			<c:if test="${item.type=='2'}">
        	  				合同专用章
        	  			</c:if>
        	  			<c:if test="${item.type=='3'}">
        	  				法人章
        	  			</c:if>
        	  			<c:if test="${item.type=='4'}">
        	  				财务专用章
        	  			</c:if>
        	  			<c:if test="${item.type=='5'}">
        	  				发票专用章
        	  			</c:if></td>
              		<td>${item.description}</td>
              		<td>${item.subject_personName}</td>
              		<td>${item.store_personName}</td>
              		<td>
            			<input class="btn btn-primary" value="申请缴销" onclick="new_task_for_chopDestroy('${item.chop_Id}')" style="width:100px"/>
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
          $(function(){
        	  set_href();
          });
	         var  new_task_for_chopDestroy = function (id){
	        	 location.href="/personal/toBeginChopDestroyPage?id="+id;
	        	 Load.Base.LoadingPic.FullScreenShow(null);
	         }
          </script>
        </div>
      </div>
    </div>
</body>
</html>