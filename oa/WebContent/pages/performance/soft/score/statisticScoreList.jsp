
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>

<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
</style>
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
<script>
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
	});
	function showDetails(userId){
		var year = $("input[name='year']").val();
		var month = $("input[name='month']").val();
		$.ajax({
			url:'performance/soft/showBaseScoresDetail',
			type:'post',
			data:{'userId':userId,'year':year,'month':month},
			success:function(data){
				var html;
				data.performanceVos.forEach(function(value){
					var duty = value.duty;
					if(duty=='问题单'){
						html += "<tr><td>"+value.projectName+"</td><td>"+value.versionName+"</td>"+
						"<td>"+value.duty+"</td><td>——</td><td>——</td>"+
						"<td>——</td><td>"+value.getScores+"</td><td>"+value.baseScores+"</td></tr>";
					}else{
						html += "<tr><td>"+value.projectName+"</td><td>"+value.versionName+"</td>"+
						"<td>"+value.duty+"</td><td>"+value.staffNames+"</td><td>"+value.percent+"</td>"+
						"<td>"+value.versionScores+"</td><td>"+value.getScores+"</td><td>"+value.baseScores+"</td></tr>";
					}
				});
				
				$("#baseScores").html(html);
				$("#total").html("基数总计："+data.totalBaseScores);
				$("#detail").modal('show');
			}
		});
	}
</script>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'statisticScoreList'"></s:set>
        <%@include file="/pages/performance/soft/score/scorePanel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<h3 class="sub-header" style="margin-top:0px;">人员${year}年${month}月得分汇总</h3>
          	 <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">年份</label>
			  	<div class="col-sm-2" ><input type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="form-control" name="year" value="${year}"></div>
			  	<div class="col-sm-2" ><input type="text" onclick="WdatePicker({dateFmt:'MM'})" class="form-control" name="month" value="${month}"></div>
			  	<div class="col-sm-2" >
			  	<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			  	</div>
			  </div>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:15%">序号</th>
                  <th style="width:20%">姓名</th>
                  <th style="width:20%">总分</th>
                  <th style="width:25%">绩效系数<span style="color:red">（得分/基数）</span></th>
                  <th style="width:20%">查看详情</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty performanceVos}">
              	<c:forEach items="${performanceVos}" var="performanceVo" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${performanceVo.staffName}</td>
              		<td>${performanceVo.totalScores}</td>
              		<td><span onclick="showDetails('${performanceVo.userId}')" style='color:#286090;cursor:pointer' data-toggle='tooltip' title="点击查看具体的核算基数">${performanceVo.KValue}</span></td>
              		<td>
             			<a onclick="goPath('/performance/soft/myScoreDetail?month=${month}&year=${year}&userId=${performanceVo.userId}&isFromStatistic=1')" href="javascript:void(0)">
             			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
							<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
             			</a>
              		</td>
              		</tr>
              	</c:forEach>
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
    <div id="detail" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" >
	<div class="modal-dialog" style="width:1000px">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">绩效系数基数详情</h4>
			</div>
			<div class="modal-body">
			<div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:10%">项目</th>
                  <th style="width:10%">版本</th>
                  <th style="width:10%">职责</th>
                  <th>人员</th>
                  <th style="width:10%">比例</th>
                  <th style="width:10%">版本分数</th>
                  <th style="width:10%">得分</th>
                  <th style="width:10%">基数</th>
                </tr>
              </thead>
              <tbody id="baseScores">
              </tbody>
              </table>
              <div id="total" style="float:right"></div>
              </div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		<input type="hidden" name="projectVersion.id" value="${projectVersion.id}"/>
		<input type="hidden" name="projectVersion.projectId" value="${projectVersion.projectId}"/>
		<input type="hidden" name="projectId" value="${project.id}">
		</form>
	</div>
</div>
    </div>
    <script src="/assets/icon/iconfont.js"></script>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/layer/layer.js"></script>
  </body>
</html>

