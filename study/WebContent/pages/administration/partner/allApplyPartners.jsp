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
function showApplyContent(id){
	Load.Base.LoadingPic.FullScreenShow(null);
	$.ajax({
		url:'/administration/partner/showApplyContent',
		data:{'id':id},
		success:function(data){
			layer.alert(data.applyContent, {offset:'100px',title:'申请书'});
		},
		complete:function(){
			Load.Base.LoadingPic.FullScreenHide();
		}
	});
}
function auditApply(id){
	layer.confirm("<form class='form-horizontal'><div class='form-group'><label class='col-sm-3'>审批意见</label><div class='col-sm-8'><textarea class='form-control' rows='4' id='comment'></textarea></div></div><form>",{area:'400px',offset:'100px',title:'审批',btn:["同意","不同意"],btnAlign:'c'}, function(index){
		layer.close(index);
		var comment = $("#comment").val();
		location.href="/administration/partner/auditApply?result=1&applyId="+id+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	},function(index){
		var comment = $("#comment").val();
		if(!comment.trim()){
			layer.alert("意见不能为空",{type:1,offset:'150px',area:['200px','130px']});
			return false;
		}
		layer.close(index);
		location.href="/administration/partner/auditApply?result=2&applyId="+id+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	});
}
function showApplyComment(comment){
	layer.alert(comment, {offset:'100px',title:'审批意见'});
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
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">申请书列表</h3>
            <form action="administration/partner/findAllApplyPartners" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1">姓名</label>
        		<div class="col-sm-2">
        			<input class="form-control" autoComplete="off" name="applyer" value="${applyer}"/>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        	</form> 
        	<div class="sub-header"></div>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:20%">序号</td>
	              		<td style="width:20%">申请人</td>
	              		<td style="width:20%">申请时间</td>
	              		<td style="width:20%">状态</td>
	              		<td style="width:20%">操作</td>
	              	</tr>
	              </thead>
	              <tbody>
	              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					<c:forEach items="${partners}" var="partner" varStatus="status">
						<tr>
						<td>${status.index+startIndex+1}</td>
						<td>${partner[1]}</td>
						<td>${partner[2]}</td>
						<td>
							<c:if test="${partner[3]==0}">
							待审批
							</c:if>
							<c:if test="${partner[3]==1}">
							同意
							</c:if>
							<c:if test="${partner[3]==2}">
							拒绝
							</c:if>
						</td>
						<td>
							<a href="javascript:showApplyContent('${partner[0]}')">
		              			<svg class="icon" aria-hidden="true" title="查看申请书" data-toggle="tooltip">
            						<use xlink:href="#icon-Detailedinquiry"></use>
            					</svg>
		              		</a>
		              		<c:if test="${partner[3]==0}">
		              		&nbsp;
		              		<a href="javascript:auditApply('${partner[0]}')">
		              			<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
            						<use xlink:href="#icon-shenpi"></use>
            					</svg>
		              		</a>
		              		</c:if>
		              		<c:if test="${partner[4]!=null && partner[4]!=''}">
		              		&nbsp;
		              		<a href="javascript:showApplyComment('${partner[4]}')">
		              			<svg class="icon" aria-hidden="true" title="查看审批意见" data-toggle="tooltip">
            						<use xlink:href="#icon-shuoming-copy-copy"></use>
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