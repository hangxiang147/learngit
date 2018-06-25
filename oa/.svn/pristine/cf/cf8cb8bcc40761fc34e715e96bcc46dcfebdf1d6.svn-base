<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
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
      	<s:set name="panel" value="'PmManage'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">行政奖惩记录</h3> 
        	<form action="PM/process/findRewardAndPunishmentList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1">姓名</label>
        		<div class="col-sm-2">
        			<input class="form-control" autoComplete="off" name="rewardAndPunishmentVo.requestUserNames" 
        					value="${rewardAndPunishmentVo.requestUserNames}"/>
        		</div>
        		<label class="control-label col-sm-1">类型</label>
        		<div class="col-sm-2">
        		<select class="form-control" name="rewardAndPunishmentVo.status">
       	  	  		<option value="">全部</option>
       	  	  		<option ${rewardAndPunishmentVo.status=='进行中'?'selected':'' } value="进行中">进行中</option>
       	  	  		<option ${rewardAndPunishmentVo.status=='1'?'selected':'' } value="1">已审批</option>
       	  	  		<option ${rewardAndPunishmentVo.status=='2'?'selected':'' } value="2">未通过</option>
       	  	  	</select>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        	</form>
        	<button class="btn btn-primary" onclick="goPath('PM/process/showRewardAndPunishmentDiagram')">新增</button>
        	<div class="sub-header"></div>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:5%">序号</td>
	              		<td style="width:12%">姓名</td>
	              		<td style="width:7%">类型</td>
	              		<td style="width:7%">额度(元)</td>
	              		<td style="width:10%">生效月份</td>
	              		<td style="width:7%">发起人</td>
	              		<td style="width:15%">发起时间</td>
	              		<td style="width:10%">当前处理人</td>
	              		<td style="width:10%">流程状态</td>
	              		<td style="width:9%">操作</td>
	              	</tr>
	              </thead>
	              <tbody>
	              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					<c:forEach items="${rewardAndPunishmentVos}" var="rewardAndPunishmentVo" varStatus="status">
						<tr>
						<td>${status.index+startIndex+1}</td>
						<td>${rewardAndPunishmentVo.requestUserNames}</td>
						<td>${rewardAndPunishmentVo.type==0 ? '奖励':'惩罚'}</td>
						<td>${rewardAndPunishmentVo.money}</td>
						<td>${rewardAndPunishmentVo.effectiveDate}</td>
						<td>${rewardAndPunishmentVo.userName}</td>
						<td><fmt:formatDate value="${rewardAndPunishmentVo.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>${rewardAndPunishmentVo.assigneeUserName==null ? '——':rewardAndPunishmentVo.assigneeUserName}</td>
						<td>${rewardAndPunishmentVo.status}</td>
						<td>
							<a href="javascript:showReason('${rewardAndPunishmentVo.reason}')">
		              			<svg class="icon" aria-hidden="true" title="查看奖惩原因" data-toggle="tooltip">
            						<use xlink:href="#icon-Detailedinquiry"></use>
            					</svg>
		              		</a>
		              		&nbsp;
							<a class="hand" onclick="goPath('HR/staffSalary/processHistory?processInstanceID=${rewardAndPunishmentVo.processInstanceID}&selectedPanel=rewardAndPunishmentList')">
								<svg class="icon" aria-hidden="true" title="流程详情" data-toggle="tooltip">
									<use xlink:href="#icon-liucheng"></use>
								</svg>	
							</a>
							<c:if test="${null != rewardAndPunishmentVo.attachmentIds}">
								&nbsp;
								<span  onclick="showAttachments('${rewardAndPunishmentVo.attachmentIds}','${rewardAndPunishmentVo.attachmentNames }')" data-toggle="tooltip" title="查看附件" style="color:#3F78AD;font-size:18px" class="glyphicon glyphicon-paperclip hand"></span>
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
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        	</div>
		        <%@include file="/includes/pager.jsp" %>
      	</div>
      </div>
    </div>
    <script type="text/javascript">
		$(function(){
			set_href();
			$("[data-toggle='tooltip']").tooltip();
		});
		function showReason(reason){
			layer.alert(reason,{offset:'100px'});
		}
		function showAttachments(attachmentIdStr, attachmentNameStr){
			var html = "";
			var attachmentIds = attachmentIdStr.split(","); 
			var attachmentNames = attachmentNameStr.split(",");
			for(var i=0; i<attachmentIds.length; i++){
				html += '<a href="administration/notice/downloadAtta?id='+attachmentIds[i]+'">'+attachmentNames[i]+'</a><br>';
			}
			layer.alert(html, {offset:'100px',title:'附件'});
		}
	</script>
</body>
</html>