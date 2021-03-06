<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
});
function closeRelation(id){
	layer.confirm("确定关闭？",{offset:'100px'},function(index){
		layer.close(index);
		location.href="/administration/public/closePublicRelation?publicRelationId="+id;
		Load.Base.LoadingPic.FullScreenShow(null);
	});
}
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
        <s:set name="panel" value="'public'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">社会公共关系列表</h3>
            <form action="administration/public/findPublicRelations" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        	    <label class="control-label col-sm-1">关系范畴</label>
        		<div class="col-sm-2">
        			<input class="form-control" autoComplete="off" name="category" value="${category}"/>
        		</div>
        		<label class="control-label col-sm-1" style="width:10%">对方联系人</label>
        		<div class="col-sm-2">
        			<input class="form-control" autoComplete="off" name="person" value="${person}"/>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        	</form> 
        	<button class="btn btn-primary" onclick="goPath('administration/public/addPublicRelation')">新增</button>
        	<div class="sub-header"></div>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:5%">序号</td>
	              		<td style="width:20%">社会公共关系范畴</td>
	              		<td style="width:17%">对方联系人</td>
	              		<td style="width:10%">职位</td>
	              		<td style="width:17%">我方关联人</td>
	              		<td style="width:10%">状态</td>
	              		<td style="width:10%">操作</td>
	              	</tr>
	              </thead>
	              <tbody>
	              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					<c:forEach items="${publicRelations}" var="publicRelation" varStatus="status">
						<tr>
						<td>${status.index+startIndex+1}</td>
						<td>${publicRelation.category}</td>
						<td>${publicRelation.otherName}【${publicRelation.otherPhone}】</td>
						<td>${publicRelation.otherJob}</td>
						<td><c:forEach items="${publicRelation.ourContacts}" var="ourContact">${ourContact}<br></c:forEach></td>
						<td>
							<c:if test="${publicRelation.status==0}"><span style="color:#13d213">开启</span></c:if>
							<c:if test="${publicRelation.status==1}"><span style="color:red">关闭</span></c:if>
						</td>
						<td>
							<a onclick="goPath('/administration/public/showHistoricalPublicEvents?publicRelationId=${publicRelation.id}')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看历史公关事件" data-toggle="tooltip">
            						<use xlink:href="#icon-Detailedinquiry"></use>
            					</svg>
		              		</a>
		              		<c:if test="${publicRelation.status==0}">
		              		&nbsp;
		              		<a href="javascript:goPath('administration/public/modifyPublicRelation?publicRelationId=${publicRelation.id}')">
		              			<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
            						<use xlink:href="#icon-modify"></use>
            					</svg>
		              		</a>
		              		&nbsp;
		              		<a href="javascript:closeRelation('${publicRelation.id}')">
		              			<svg class="icon" aria-hidden="true" title="关闭" data-toggle="tooltip">
            						<use xlink:href="#icon-delete"></use>
            					</svg>
		              		</a>
		              		</c:if>
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