<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	var showPic = function(name, path) {
		var picData = {
			start : 0,
			data : []
		}
		picData.data.push({
			alt : name,
			src : "performance/soft/showImage?attachmentPath=" + path
		})
		layer.photos({
			offset : '50px',
			photos : picData,
			anim : 5
		});
	}
	$(function(){
		$("#description img").each(function(){
			var url = $(this).attr("src");
			$(this).attr("onclick","showPic('','"+url+"')");
			$(this).attr("height","200px");
			$(this).attr("width","");
		})
	});
</script>
<style type="text/css">
.tab {
	color: #555555;
	box-shadow: 0px 1px 5px #dddddd;
}

.tab table {
	width: 100%;
	border-collapse: collapse;
	word-break: break-word;
}

.tab table tr td {
	border: 1px solid #ddd;
	word-wrap: break-word;
	font-size: 14px
}

.tab table tbody tr td {
	height: auto;
	line-height: 20px;
	padding: 10px 10px;
	text-align: center;
}

.tab table tr .black {
	background: #efefef;
	text-align: center;
	color: #000;
	width: 15%
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">
					问题单详情
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
				<div class="tab">
					<table>
						<tr>
							<td class="black">问题名称</td>
							<td style="width:25%">${problemOrder[2]}</td>
							<td class="black">创建时间</td>
							<td style="width:20%">${problemOrder[4]}</td>
							<td class="black">创建人</td>
							<td>${problemOrder[3]}</td>
						</tr>
						<tr>
							<td class="black">项目</td>
							<td>${problemOrder[0]}</td>
							<td class="black">版本</td>
							<td>${problemOrder[1]}</td>
							<td class="black">处理人</td>
							<td>${problemOrder[8]==null?'——':problemOrder[8]}</td>
						</tr>
						<tr>
							<td class="black">属主</td>
							<td>${problemOrder[9]}</td>
							<td class="black">责任人</td>
							<td>${problemOrder[10]==null?'——':problemOrder[10]}</td>
							<td class="black">分数</td>
							<td>${problemOrder[5]==null?'——':problemOrder[5]}</td>
						</tr>
						<tr>
							<td class="black">问题描述</td>
							<td colspan="5" id="description" style="text-align: left">${problemOrder[6]==null||problemOrder[6]==''?'——':problemOrder[6]}</td>
						</tr>
						<tr>
							<td class="black">附件</td>
							<c:if test="${!empty attaList}">
							<td colspan="5" style="text-align: left"><c:forEach
									items="${attaList}" var="atta">
									<c:if test="${atta.suffix=='png'}">
										<a
											href="javascript:showPic('${atta.softName}','${atta.softURL}')"
											style="text-decoration: none"> <img
											src="performance/soft/showImage?attachmentPath=${atta.softURL}"
											style="width: 200px; height: 200px">&nbsp;&nbsp;
										</a>
									</c:if>
									<c:if test="${atta.suffix!='png'}">
										<a
											href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
						</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
						</tr>
					</table>
				</div>
				<br>
					<c:if test="${showProcess}">
					<h3 class="sub-header" style="margin-top: 0px;">流程状态</h3>
					<form class="form-horizontal">
						<c:if test="${not empty finishedTaskVOs }">
							<c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
								<c:if test="${not empty task }">
									<div class="form-group">
										<label class="col-sm-2 control-label">【${task.taskName }】</label>
										<div class="col-sm-10">
											<div class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</div>
										</div>
										<c:if test="${not empty comments }">
											<c:forEach items="${comments }" var="comment" varStatus="st">
												<c:if test="${comment.taskID == task.taskID }">
													<div class="col-sm-2"></div>
													<div class="col-sm-10">
														<span class="detail-control">${comment.content }</span>
													</div>
												</c:if>
											</c:forEach>
										</c:if>
									</div>
								</c:if>
							</c:forEach>
						</c:if>
				</form>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
