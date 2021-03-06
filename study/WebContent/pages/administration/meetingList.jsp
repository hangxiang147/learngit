<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/notice/findNoticeList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function getMeetingID(meetingID){
		
		$.ajax({
			url:'administration/meeting/getMeetingByMeetingID',
	        type:'post',
	        data:{meetingID:meetingID},
	        dataType:'json',
	        success:function(data){
	        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
					alert("操作失败");
					return;
				}
	        		        	
	        	
	        	$("#exampleModalLabel").html(data.meetingVO.theme);
	        	$("#ntcContent").html(data.meetingVO.content);
	        	$("#minutes").html(data.meetingMinutesVO.content);
	        	var html="";
	        	$.each(data.meetingActorVO1, function(i,meetingActor) {
	        		html+=meetingActor.userName+"（";
	        		$.each(meetingActor.groupList,function(i,group){
	        			if(i==0){
	        				html+=group;
	        			}else{
	        				html+=","+group;
	        			}
	        			
	        		});
	        		html+=")<br>"
	        		
	        	});
	        	$.each(data.groups1, function(i,group) {
	        		html+=group+"<br>";
	        	});
	        	$("#pAcceptor").html(html);
	        	var chtml="";
	        	$.each(data.meetingActorVO2, function(i,meetingActor) {
	        		chtml+=meetingActor.userName+"（";
	        		$.each(meetingActor.groupList,function(i,group){
	        			if(i==0){
	        				chtml+=group;
	        			}else{
	        				chtml+=","+group;
	        			}
	        			
	        		});
	        		chtml+=")<br>"
	        		
	        	});
	        	$.each(data.groups, function(i,group) {
	        		chtml+=group+"<br>";
	        	});
	        	$("#cAcceptor").html(chtml);
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
        <s:set name="panel" value="'meeting'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="sendExpressForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">会议列表</h3>												          </form>          
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">会议开始时间</th>
                  <th class="col-sm-1">会议结束时间</th> 
                  <th class="col-sm-1">会议地点</th>
                  <th class="col-sm-1">会议主题</th>
                  <th class="col-sm-1">操作</th>

                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty meetingList}">
              	<s:set name="meeting_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="meeting" value="#request.meetingList" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#meeting_id+1"/></td>   
              			<td class="col-sm-1"><s:property value="#meeting.beginTime"/></td>              			
              			<td class="col-sm-1"><s:property value="#meeting.endTime"/></td>             			              			
              			<td class="col-sm-1"><s:property value="#meeting.place"/></td>              			
              			<td class="col-sm-1"><s:property value="#meeting.theme"/></td>              			
              			<td class="col-sm-1">
              			<a onclick="goPath('administration/meeting/getMeetingByMeetingID?meetingID=<s:property value="#meeting.meetingID"/>')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
							<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
              			</a>
              			&nbsp;
              			<s:if test='#meeting.contentMins==null'>
              			<a onclick="goPath('administration/meeting/newMeetingMinutes?meetingID=<s:property value="#meeting.meetingID"/>')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="会议纪要" data-toggle="tooltip">
							<use xlink:href="#icon-jilu"></use>
						</svg>
              			</a>
              			</s:if></td>              			
              		</tr>
              		<s:set name="meeting_id" value="#meeting_id+1"></s:set>
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
     <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="exampleModalLabel">会议内容</h3><div class="container-fluid1"></div>
	         </div>
	      	 <div class="modal-body">
	          <input type="hidden" id="groupDetailID" />
			  <p id="ntcContent"></p>
			  <hr>
			  <h4>会议纪要：</h4>
			  <p id="minutes"></p>
			  <h4>参加个人和部门：</h4>
			  <p id="pAcceptor"></p>
			  <h4>抄送个人和部门：</h4>
			  <p id="cAcceptor"></p>
		  </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
  </body>
</html>

