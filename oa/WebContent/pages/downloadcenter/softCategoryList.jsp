<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>

<script type="text/javascript">
	$(function() {
		set_href();
		
	});
</script>

<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	.image-small{
	width:30px;
	}
	.line-height-50{
	line-height:50px;
	}
</style>

</head>
  <body>	
    <div class="container-fluid">    
      <div class="row">
      	<s:set name="panel" value="'softCategory'"></s:set>
        <%@include file="/pages/downloadcenter/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header"></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr id="tableHeadLine">
                  <th width="80">序号</th>
                  <th width="60">&nbsp;</th>
                  <th width="20%">名称</th>
                  <th width="100">文件大小</th>
                  <th width="100">下载次数</th>
                  <th>说明</th>
                  <th width="100">下载</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty softCategoryList}">
              	<s:set name="softCategoryList_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="softUpAndDownloadVO" value="#request.softCategoryList" status="count">
              		<tr  id="tableTr">
              			<td><s:property value="#softCategoryList_id+1"/></td>
              			<td class="inputImage"><img class="image-small" alt="" src="http://www.zhizaolian.com:9000/downloadCenter/<s:property value="#softUpAndDownloadVO.softImage"/>"></td>
              			<td><s:property value="#softUpAndDownloadVO.softName"/></td>
              			<td><s:property value="#softUpAndDownloadVO.size"/>M</td>
              			<td><s:property value="#softUpAndDownloadVO.downloadTimes"/></td>
              			<td style="text-align:left;color:#999;"><s:property value="#softUpAndDownloadVO.softDetail"/></td>
              			<td><a href="<c:url value='/'/>downloadcenter/download?softUpAndDownloadVO.softID=<s:property value='#softUpAndDownloadVO.softID'/>" class="btn btn-primary" >下载</a></td>
              		</tr>
              		<s:set name="softCategoryList_id" value="#softCategoryList_id+1"></s:set>
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
