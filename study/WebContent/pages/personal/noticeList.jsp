<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="assets/css/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	$(function() {
		set_href();
		
		$('#cheakModal').on('hide.bs.modal', function () {
		  // 执行一些动作...
			window.location.reload();
		});
	});
	
	function updateNoticeActor(ntcID,userID){
	
			$.ajax({
				url:'personal/updateNoticeActor',
		        type:'post',
		        data:{ntcID:ntcID,userID:userID},
		        dataType:'json',
		        success:function(data){
		        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
						alert("操作失败");
						return;
					}
		        	
		        	$("#exampleModalLabel").html(data.noticeVO.ntcTitle);
		        	$("#ntcContent").html(data.noticeVO.ntcContent);
		        	if(data.noticeVO.ntcTitle!="快递领取通知"){
		        		$("#zhu").parent().remove();
		        	}
		        	$("#cheakModal").modal('show');
		        	var attachments = '';
		        	data.attachments.forEach(function(value, index){
		        		attachments += "<a href='administration/notice/download?attachmentPath="+value.softURL+"&attachmentName="+value.softName+"'>"+value.softName+"</a><br>";
		        	});
		        	$("#attachments").html(attachments);
		        }
				
			});
		
		
	}
	
	
</script>
<style type="text/css">
.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	table{width:100%;border:0px solid #999;}
    table td{word-break: keep-all;white-space:nowrap;}
    table th{word-break: keep-all;white-space:nowrap;}
    .message{height:auto;margin-top:20px;}
    .message ul{margin:0px;padding:0px;}
	.message ul li{list-style-type:none;vertical-align:bottom;line-height:40px;height:40px;border-bottom:1px dashed #cccccc;text-indent:16px;background:url(assets/images/r_dot_g.png) no-repeat left center;color:#888888}
	.message ul li a{float:Left;}
	.message ul li span{float:right;color:#999999;}
	.message ul li.read{background:url(assets/images/r_dot.png) no-repeat left center;color:#333333}
	.message ul li.read a{color:#f24451;}
	.modal-body p {margin:0 0 10;}
	.modal-body h4 {margin-top:10px; margin-bottom:10px;}
  
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'asset'"></s:set>
        <%@include file="/pages/personal/panel.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">我的消息</h3>
         <c:if test="${not empty noticeVOs}">
         <div class="message">
         <ul>
         <s:iterator id="noticeVO" value="#request.noticeVOs" status="count">
         <li <s:if test="#noticeVO.status==0">class="read"</s:if>><a class="ntcTit" href="javascript:void(0)" onclick="updateNoticeActor(<s:property value='#noticeVO.ntcID'/>,'<s:property value="#noticeVO.userID"/>')"><s:property value="#noticeVO.ntcTitle"/></a><span><s:property value="#noticeVO.noticeDate"/></span></li>
         	</s:iterator>
         	</ul>
         	</div>
         </c:if>
             
          <div class="dropdown" style="padding-top:20px;">
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
        </div>
      </div>
    </div>
      <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content" style="width:650px">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      	<div id="attachments"></div>
	      	<div>
	      	<hr>
	      	<h4>注：</h4>
	      	<p id="zhu">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;骑岸总部在门卫处领取；<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;南通分部在人事行政部领取；</p>
	      	</div>
	      
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>
