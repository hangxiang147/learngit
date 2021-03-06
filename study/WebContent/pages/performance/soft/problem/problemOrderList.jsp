<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus{text-decoration:none}
	.textcomplete-dropdown{
		z-index:1050 !important;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/performance/soft/manage/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">问题单列表</h3>
          	<br>
			<form class="form-horizontal" action="/performance/soft/problemOrderList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
				<div class="form-group">
				<label class="col-sm-1 control-label">项目</label>
				<div class="col-sm-2">
					<select class="form-control" name="projectId">
						<option <c:if test="${projectId==''}">selected="selected"</c:if> value="">全部</option>
						<c:forEach items="${projects}" var="project">
     					  	<option <c:if test="${projectId==project.id}">selected="selected"</c:if> value="${project.id}">${project.name}</option>
     					 	</c:forEach>
					</select>
				</div>
				<label class="col-sm-1 control-label">状态</label>
				<div class="col-sm-2">
					<select class="form-control" name="status">
						<option <c:if test="${status==''}">selected="selected"</c:if> value="">全部</option>
						<option <c:if test="${status=='1'}">selected="selected"</c:if> value="1">已解决</option>
						<option <c:if test="${status=='0'}">selected="selected"</c:if> value="0">处理中</option>
					</select>
				</div>
				<label class="col-sm-1 control-label">问题名称</label>
				<div class="col-sm-2">
					<input class="form-control" name="problemOrderName" autoComplete="off" value="${problemOrderName}">
				</div>
				<button type="submit" class="btn btn-primary" style="margin-left:5%">查询</button>
				</div>
			</form>
		  <a class="btn btn-primary" onclick="goPath('performance/soft/addProblemOrder')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>问题单</a>
          <h2 class="sub-header"></h2> 
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:10%">项目</th>
                  <th style="width:10%">版本</th>
                  <th style="width:20%">问题名称</th>
                  <th style="width:10%">状态</th>
                  <th style="width:10%">属主</th>
                  <th style="width:10%">创建人</th>
                  <th style="width:15%">创建时间</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${problemOrders}" var="problemOrder" varStatus="status">
              		<tr>
              		<td>${(page-1)*limit+status.index+1}</td>
              		<td>${problemOrder[0]}</td>
              		<td>${problemOrder[1]}</td>
              		<td>${problemOrder[2]}</td>
              		<%-- <td>${problemOrder[8]==null?'处理中':'已解决'}</td> --%>
              		<td>
              			<c:choose>
              				<c:when test="${problemOrder[8]==null }">
              					处理中
              				</c:when>
              				<c:when test="${problemOrder[8] eq '1' }">
              					已解决
              				</c:when>
              				<c:when test="${problemOrder[8] eq '31' }">
              					流程强制中断
              				</c:when>
              			</c:choose>
              		</td>
              		<td>${problemOrder[4]}</td>
              		<td>${problemOrder[3]}</td>
              		<td>${problemOrder[5]}</td>
              		<td>
              			<a onclick="goPath('/performance/soft/showProblemOrderDetail?problemOrderId=${problemOrder[6]}&instanceId=${problemOrder[7]}')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              		</a>
	              		&nbsp;
	              		<c:if test="${problemOrder[8]==null }">
		              		<a onclick="goPath('/performance/soft/forcedToEnd?id=${problemOrder[6]}&processInstanceID=${problemOrder[7]}')" href="javascript:void(0)">
								<svg class="icon" aria-hidden="true" title="强制终结" data-toggle="tooltip">
									<use xlink:href="#icon-zhongzhi"></use>
								</svg>
	          				</a>
          				</c:if>
              		</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
           <div class="dropdown" >
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
	<script src="/assets/icon/iconfont.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
	$(function(){
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	</script>
  </body>
</html>
