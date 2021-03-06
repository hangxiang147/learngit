
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>

<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
</style>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'softPerformanceSubject'"></s:set>
        <%@include file="/pages/performance/soft/subject/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" >
          	<h3 class="sub-header" style="margin-top:0px;">测试人员功能审核</h3>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
		                   <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>所属项目</th>
		                  <th>版本</th>
		                  <th>模块</th>
		                  <th>分值</th>
		                  <th>截止时间</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty resultList}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.resultList" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.projectName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.versionName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.moduleName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.score"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.limitTime"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('/personal/tablePerform?taskID=<s:property value='#taskVO.taskID' />&isGroup=group')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
             						<use xlink:href="#icon-banli"></use>
             					</svg>
		              			</a></td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
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
     <script type="text/javascript">
	    $(function (){
	    	set_href();
	    	$("[data-toggle='tooltip']").tooltip();
	    });
 	</script>
  </body>
</html>

