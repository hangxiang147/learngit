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
		
		
	});
	function getNotice(ntcID){
		
		$.ajax({
			url:'administration/notice/getNoticeByntcID',
	        type:'post',
	        data:{ntcID:ntcID},
	        dataType:'json',
	        success:function(data){
	        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
					alert("操作失败");
					return;
				}
	        	
	        	$("#exampleModalLabel").html(data.noticeVO.ntcTitle);
	        	$("#ntcContent").html(data.noticeVO.ntcContent.replace(/\n/g,"<br/>").replace(/\s/g, "&nbsp;"));
	        	$("#cheakModal").modal('show');
	        	
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


  
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/informationCenter/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">大事记</h3>
       
         <c:if test="${not empty noticeList}">
         <div class="message">
         <ul>
         <s:iterator id="notice" value="#request.noticeList" status="count">
                 <li ><a class="ntcTit" href="javascript:void(0)" onclick="getNotice(<s:property value='#notice.ntcID'/>)"><s:property value="#notice.ntcTitle"/></a><span><s:property value="#notice.noticeDate"/></span></li>
         	</s:iterator>
         	</ul>
         	</div>
         </c:if>
             
          <div class="dropdown" style="padding-top:20px">
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
      <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	          <input type="hidden" id="groupDetailID" />
			  <p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>
