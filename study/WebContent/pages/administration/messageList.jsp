<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html>

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();

	});
	
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/notice/findMessageList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function getNotice(ntcID){
		
		$.ajax({
			url:'administration/notice/getNoticeByntcID',
	        type:'post',
	        data:{ntcID:ntcID},
	        dataType:'json',
	        success:function(data){
	        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
					layer.alert("操作失败", {offset:'100px'});
					return;
				}
	        	
	        	$("#exampleModalLabel").html(data.noticeVO.ntcTitle);
	        	$("#ntcContent").html(data.noticeVO.ntcContent);
	        	var html="";
	        	$.each(data.groups, function(i,group) {
	        		html+=group+"<br>";
	        	});
	        	$("#acceptor").html(html);
	        	$("#cheakModal").modal('show');
	        	
	        	var attachments = '';
	        	data.attachments.forEach(function(value, index){
	        		attachments += "<a href='administration/notice/download?attachmentPath="+value.softURL+"&attachmentName="+value.softName+"'>"+value.softName+"</a><br>";
	        	});
	        	$("#attachments").html(attachments);
	        }
			
		});

}
	
	function deleteNotice(ntcID){
			layer.confirm("确认删除？", {offset:'100px'}, function(index){
				layer.close(index);
				$.ajax({
					url:'administration/notice/deleteNotice',
			        type:'post',
			        data:{ntcID:ntcID},
			        dataType:'json',
			        success:function(data){
			        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
							layer.alert("操作失败", {offset:'100px'});
							return;
						}
			        	window.location.reload();
			        	Load.Base.LoadingPic.FullScreenShow(null);
			        }
					
				});
			});
	}
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
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
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="sendExpressForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">通知列表</h3>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="noticeVO.beginTime" value="${noticeVO.beginTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="noticeVO.endTime" value="${noticeVO.beginTime }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="结束时间">
			    </div>
			</div>
			
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
           <a class="btn btn-primary"  onclick="goPath('administration/notice/newMessage')" href="javascript:void(0)">新增通知</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">标题</th>
                  <th class="col-sm-2">创建人</th> 
                  <th class="col-sm-1">创建时间</th>
                  <th class="col-sm-1">操作</th>

                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty noticeList}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="notice" value="#request.noticeList" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#attendance_id+1"/></td>   
              			<td class="col-sm-1"><s:property value="#notice.ntcTitle"/></td>              			
              			<td class="col-sm-1"><s:property value="#notice.creatorName"/></td>              			
              			<td class="col-sm-1"><s:property value="#notice.noticeDate"/></td>              			
              			<td class="col-sm-1">
              			<a href="javascript:void(0)" onclick="getNotice(<s:property value='#notice.ntcID'/>)">
              			<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
						<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a href="javascript:void(0)" onclick="deleteNotice(<s:property value='#notice.ntcID'/>)">
						<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
						<use xlink:href="#icon-delete"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a href="javascript:void(0)" onclick="goPath('administration/notice/modifyNotice?notId=<s:property value='#notice.ntcID'/>')">
						<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
						<use xlink:href="#icon-modify"></use>
						</svg>
              			</a>
              			</td>              			
              		</tr>
              		<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
     <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	          <input type="hidden" id="groupDetailID" />
			  <p id="ntcContent"></p>
			   <div id="attachments"></div>
			  <hr>
			  <h4>通知部门：</h4>
			  <p id="acceptor"></p>
		  </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
  </body>
</html>

