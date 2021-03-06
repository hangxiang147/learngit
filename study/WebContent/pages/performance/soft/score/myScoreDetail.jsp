<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
	});
</script>
<style>
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover,a:focus,a:visited{text-decoration:none}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<c:choose>
      	<c:when test="${isFromStatistic eq '1'}">
      	<s:set name="selectedPanel" value="'statisticScoreList'"></s:set>
        <%@include file="/pages/performance/soft/score/scorePanel.jsp" %>	
      	</c:when>
      	<c:otherwise>
      	<s:set name="selectedPanel" value="'myScoreList'"></s:set>
        <%@include file="/pages/performance/soft/score/panel.jsp" %>	
      	</c:otherwise>
      	</c:choose>
    			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/performance/soft/myScoreList">     	  
          	<h3 class="sub-header" style="margin-top:0px;">${year}年${month}月详情</h3> 
			  
	
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">类型</th>
                  <th class="col-sm-1">任务名称 </th>
                  <th class="col-sm-1">所属项目</th>
                  <th class="col-sm-1">任务角色</th>
                  <th class="col-sm-1">所属版本</th>
                  <th class="col-sm-1">所属模块</th>
                  <th class="col-sm-1">任务总分</th>
                  <th class="col-sm-1">得分</th>
                  <th class="col-sm-1">日期</th>
                  <th class="col-sm-1">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty list}">
              	<c:forEach items="${list}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[1] }</td>
              		<td>${item[2] }</td>
              		<td>${item[3] }</td>
              		<td>${item[5] }</td>
              		<td>${item[4]==''?'——':item[4]}</td>
              		<td>${item[0]eq'问题'?'——':item[7] }</td>
              		<td>${item[8] }</td>
              		<td>${item[9] }</td>
              		<td>
              		<c:if test="${ item[0] eq '问题'}">
              		    <a onclick="goPath('/performance/soft/showProblemOrderDetail?problemOrderId=${item[10]}&instanceId=${item[13]}')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看任务详情" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              		</a>
              		</c:if>
              		<c:if test="${ item[0] eq '需求'}">
	              		<a onclick="goPath('/performance/soft/showTaskDetail?taskId=${item[10] }&isFromScore=1&isFromStatistic=${isFromStatistic}&instanceId=${item[13]}')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看任务详情" data-toggle="tooltip">
	             				<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	              		</a>
              		</c:if>
              		<c:if test="${ item[0] eq '扣分'}">
	              		<a href="javascript:showReason('${item[11] }')">
		              		<svg class="icon" aria-hidden="true" title="查看任扣分原因" data-toggle="tooltip">
		             			<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	              		</a>
              		</c:if>
              		</td>
              	</c:forEach>
              	</c:if>
              </tbody>
            </table>
          </div>
          <div >
							<button onclick="javascript:history.go(-1)" class="btn btn-default">返回</button>

				</div>
        </div>
      </div>
    </div>
    <script src="/assets/icon/iconfont.js"></script>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/layer/layer.js"></script>
    <script>
    showReason=function (reason){
    	layer.open({
    		  offset:'100px',
    		  type: 1,
    		  title:'扣分原因',	
    		  skin: 'layui-layer-rim', 
    		  area: ['420px', '240px'],
    		  content: "&nbsp;&nbsp;&nbsp;&nbsp;"+reason
    		});
    }
    
    </script>
  </body>
</html>
