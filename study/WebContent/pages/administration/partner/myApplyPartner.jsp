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
function showApplyComment(comment){
	layer.alert(comment, {offset:'100px',title:'审批意见'});
}
function showApprovalOpinion(obj){
	var approvalOpinion = $(obj).attr("data-approvalOpinion");
	layer.alert(approvalOpinion,{title:'审批意见',offset : ['100px']});
}
function showExitReason(obje){
	var exitReason = $(obje).attr("data-exitReason");
	layer.alert(exitReason,{title:'申请原因',offset : ['100px']});
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
        <h3 class="sub-header" style="margin-top:0px;">我的合伙人申请</h3> 
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:25%">序号</td>
	              		<td style="width:25%">申请时间</td>
	              		<td style="width:25%">状态</td>
	              		<td style="width:25%">操作</td>
	              	</tr>
	              </thead>
	              <tbody>
					<c:forEach items="${partners}" var="partner" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${partner.applyDate}</td>
							<td>
								<c:if test="${partner.status==null}">
								审批中
								</c:if>
								<c:if test="${partner.status==1}">
								同意
								</c:if>
								<c:if test="${partner.status==2}">
								拒绝
								</c:if>
							</td>
							<td>
								<a href="javascript:showApplyContent('${partner.id}')">
			              			<svg class="icon" aria-hidden="true" title="查看申请书" data-toggle="tooltip">
	            						<use xlink:href="#icon-Detailedinquiry"></use>
	            					</svg>
			              		</a>
			              		<c:if test="${partner.comment!=null}">
			              		&nbsp;
			              		<a href="javascript:showApplyComment('${partner.comment}')">
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
		     
				<h3 class="sub-header" style="margin-top:0px;">退出合伙人申请</h3> 
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<td style="width:25%">序号</td>
								<td style="width:25%">申请时间</td>
								<td style="width:25%">状态</td>
								<td style="width:25%">操作</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${exitParterApplyList }" var="item" varStatus="number">
							<tr>
								<td>${number.index+1 }</td>
								
								<td>
									<fmt:formatDate value="${item.applyAddTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td>
									<c:if test="${item.auditStatus==0}">
										审批中
									</c:if>
									<c:if test="${item.auditStatus==1}">
										不同意
									</c:if>
									<c:if test="${item.auditStatus==2}">
										同意
									</c:if>
								</td>
								<td>
									<a data-exitReason="${item.exitReason }" onclick="showExitReason(this)" href="javascript:void(0)">
										<svg class="icon" aria-hidden="true" title="查看申请原因" data-toggle="tooltip">
											<use xlink:href="#icon-Detailedinquiry"></use>
										</svg>
									</a>
									<c:choose>
										<c:when test="${item.approvalOpinion!=null && item.approvalOpinion!=''}">
											&nbsp;
											<a data-approvalOpinion="${item.approvalOpinion }" onclick="showApprovalOpinion(this)" href="javascript:void(0)">
												<svg class="icon" aria-hidden="true" title="查看审批意见" data-toggle="tooltip">
													<use xlink:href="#icon-shuoming-copy-copy"></use>
												</svg>
											</a>
										</c:when>
										<c:otherwise>
											
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
		     
      	</div>
      </div>
    </div>
</body>
</html>