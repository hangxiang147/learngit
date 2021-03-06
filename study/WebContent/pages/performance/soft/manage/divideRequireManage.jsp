<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	.hand {
		cursor: hand;
	}
	.left{
		text-align:left !important;
	}
	table{table-layout:fixed;word-break:break-all;}
	.red{color:red !important}
	.green{color:#4caf50 !important}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
	.delete:hover{color:red !important}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="selectedPanel" value="'divideRequire'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">任务分解</h3>
				      			<form class="form-horizontal">
				    <div class="form-group">
				    	<label for="number" class="col-sm-1 control-label">编号</label>
				    	<div class="col-sm-2">
				    		<input id="number" type="text" class="form-control">
				    	</div>
				    	
				    	<label for="chooseP" class="col-sm-1 control-label">P级</label>
				    	<div class="col-sm-2">
				    		<select id="chooseP" class="form-control">
				    			<option value="">请选择</option>
				    			<option value="加急">加急</option>
				    			<option value="高">高</option>
				    			<option value="中">中</option>
				    			<option value="低">低</option>
				    		</select>
				    	</div>
				    	
				    	<label for="requireName" class="col-sm-1 control-label">需求名称</label>
				    	<div class="col-sm-2">
				    		<input id="requireName" type="text" class="form-control"> 
				    	</div>
				    	
				    	<label for="versionNumber" class="col-sm-1 control-label">版本号</label>
				    	<div class="col-sm-2">
				    		<input id="versionNumber" type="text" class="form-control" readonly="readonly">
				    	</div>
				    </div>
				    
      				<div class="form-group">
      				<label for="user" class="col-sm-1 control-label">状态</label>
      					<div class="col-sm-2">
      					<select id="selectStatus" class="form-control">
      						<option value="0">全部</option>
      						<option selected value="2">未完成分解</option>
      						<option value="1">完成分解</option>
      						<option value="3">作废</option>
      					</select>
      					</div>
				    	<div class="col-sm-1">
						<button type="button" id="submitButton" class="btn btn-primary">查询</button>
						</div>
						<div class="col-sm-6 control-label" style="text-align:left">
							<span id="versionInfo" style="color:red"></span>
						</div>
      				</div>
      			</form>
				<div>
				<div style="width:15%;float:left">
					<div class="panel-group table-responsive">
						<div id="projectVersions" class="panel panel-primary leftMenu">
						</div>
					</div>
				</div>
				<div style="margin-left:17%">
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th style="width:4%">ID</th>
	                  <th style="width:5%">编号</th>
	                  <th style="width:5%">P</th>
	                  <th style="width:16%">需求名称</th>
	                  <th style="width:12%">模块名</th>
	                  <th style="width:6%">创建</th>
	                  <th style="width:6%">状态</th>
	                  <th style="width:6%">阶段</th>
	                  <th style="width:6%">分解</th>
	                  <th style="width:11%">操作</th>
	                </tr>
	              </thead>
	              <tbody id="_tab">
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
			    <li><a class="dropdown-item-20 hand" onclick="changeLimit(this)">20</a></li>
			    <li><a class="dropdown-item-50 hand" onclick="changeLimit(this)">50</a></li>
			    <li><a class="dropdown-item-100 hand" onclick="changeLimit(this)">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共<span id="num"></span>条记录</span>
           	<input type="hidden" id="limit"/>
         	</div>
          	<nav>
 			 <ul class="pager">
	    		<li class="disabled"><a class="previous-page hand" onclick="changePage(this,'previous')">上一页</a></li>
	    		<li class="disabled"><a class="next-page hand" onclick="changePage(this,'next')">下一页</a></li>
	    		<label>第1页，共1页</label>
  			</ul>
			</nav>
				</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/pages/performance/soft/manage/confirmTask.jsp" %>
	<%@include file="/pages/performance/soft/manage/confirmRequire.jsp" %>
	<%@include file="/pages/performance/soft/manage/changeVersion.jsp" %>
	<script src="/assets/js/textarea/marked.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
	var storage=window.localStorage;
    $(function(){
        //初始化项目版本列
        var projectVersions = '${projectVersions}';
        var projectVersionObj = JSON.parse(projectVersions);
        var html = '';
        var index = 0;
        for(var project in projectVersionObj){
        	html += '<div class="panel-heading hand" id="collapseListGroupHeading'+index+
					'" data-toggle="collapse" data-target="#collapseListGroup'+index+
					'" role="tab">'+
					'<h4 class="panel-title">'+
					'<span class="glyphicon glyphicon-chevron-up right"></span>'+
					project+'</h4></div>'+
					'<div id="collapseListGroup'+index+'" class="panel-collapse collapse"'+
					'role="tabpanel" aria-labelledby="collapseListGroupHeading'+index+'">'+
					'<ul class="list-group">';
			var versions = projectVersionObj[project];
			var versionArray = versions.split("#@@#");
			var versionNames = versionArray[0].split(",");
			var versionIds = versionArray[1].split(",");
			var statuss = versionArray[2].split(",");
			for(var i=0; i<versionIds.length; i++){
				html += '<li class="list-group-item">'+
						'<a class="hand" data-preifx="'+versionIds[i]+'" onclick="showRequirementLst(this)">'
						+versionNames[i]+'</a>'+
						'</li>';
			}
			html += '</ul></div>';
			index++;
        }
        $("#projectVersions").append(html);
        
        $(".panel-heading").click(function(e){
            /*切换折叠指示图标*/
            $(this).find("span").toggleClass("glyphicon-chevron-down");
            $(this).find("span").toggleClass("glyphicon-chevron-up");
        });
   	 	var userSelectedNode = storage.getItem("userSelectedNodeForDivide");
		 if(userSelectedNode!=null){
			 var userSelect = userSelectedNode.split(",");
			 var id = userSelect[0];
			 var version = userSelect[1];
			 versionId = version;
			 $("#"+id).click();
			 var obj = $("a[data-preifx='"+version+"']");
			 showRequirementLst(obj);
		 }
		 
		 
    });
    var versionId='';
    var limit ='';
    var page='';
    function showRequirementLst(target){
    	//记录下用户选择的节点
    	var version = $(target).attr('data-preifx');
    	var id = $(target).parent().parent().parent().prev().attr("id");
    	var userSelectedNode = [id, version];
    	var targetHtml = $(target).html();
    	$("#versionNumber").val(targetHtml);
    	storage.setItem("userSelectedNodeForDivide",userSelectedNode);
    	//改变当前点击的版本的背景颜色
    	$("#projectVersions li").css("background-color","#fff");
    	$(target).parent().css("background-color","rgb(201, 221, 241)");
    	versionId = $(target).attr("data-preifx");
    	if(!versionId){
    		return;
    	}
    	var status = $("#selectStatus").find("option:selected").val();
    	var number = $("#number").val();
    	var chooseP = $("#chooseP").val();
    	var requireName = $("#requireName").val();
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'versionId':versionId,'status':status,'number':number,'chooseP':chooseP,'requireName':requireName},
			url:'/performance/soft/getDivideRequirementLst',
			success:function(data){
				if(data.error!=null && data.error.length>0){
					layer.alert("查询失败："+data.error,{offset:'100px'});
					return;
				}else{
					var html='';
					var requirementLst = data.requirementLst;
					var totalPage = data.totalPage;
					var startIndex = data.startIndex;
					page = data.page;
					limit = data.limit;
					for(var i=0; i<requirementLst.length; i++){
						var divide = requirementLst[i].divide;
						if(divide==0){
							divide = "未完成";
						}else if(divide==1){
							divide = "完成";
						}
						var stage = requirementLst[i].stage;
						if(("完成"==divide && "已完成"==stage) || requirementLst[i].status=='作废'){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</td></tr>';	
						}
						else if("完成"==divide && "已完成"!=stage){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
             				'</a>'+
	              			'</td></tr>';	
						}
						else {
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showSubRequirement?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="任务分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-jihuafenjie"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="completeDivide('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="完成分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-wancheng"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand delete" onclick="deleteRequire('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-delete"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
	              			'</a>'+
	              			'</td></tr>';	
						}
					}
					$("#_tab").html(html);
					if(page<2 || totalPage<0){
						$(".pager li:eq(0)").addClass("disabled");
					}else{
						$(".pager li:eq(0)").removeClass("disabled");
					}
					if(page>=totalPage){
						$(".pager li:eq(1)").addClass("disabled");
					}else{
						$(".pager li:eq(1)").removeClass("disabled");
					}
					$(".pager label").text("第"+page+"页，共"+totalPage+"页");
					var hoursByPerson = "";
					data.hoursByPerson.forEach(function(value, index){
						hoursByPerson += value[1]+"："+value[0]+"时 ";
					});
					$("#versionInfo").html("此版本共计开发人员"+data.developerNum+"人&nbsp;&nbsp;&nbsp;合计工时："
					+data.totalWorkHour+"时&nbsp;&nbsp;&nbsp;剩余未分配工时："+data.remainHours+"时 <span style='color:#286090' "+
					"class='hand glyphicon glyphicon-eye-open' data-toggle='tooltip' title='"+hoursByPerson+"'></span>");
					$("[data-toggle='tooltip']").tooltip();
					$("#num").html(data.count);
				}
			},
			
			complete:function(){
				$(".demand").each(function(){
					if($(this).text().length>10){
						$(this).html($(this).text().replace(/\s+/g, "").substr(0, 10) + "...");
					}
				});
				Load.Base.LoadingPic.FullScreenHide();
			}
    	});
    }
    function changeLimit(target){
    	limit = $(target).text();
    	$(".dropdown button").html(limit+" <span class='caret'></span>");
    	var status = $("#selectStatus").find("option:selected").val();
    	var number = $("#number").val();
    	var chooseP = $("#chooseP").val();
    	var requireName = $("#requireName").val();
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId,'status':status,'number':number,'chooseP':chooseP,'requireName':requireName},
			url:'/performance/soft/showDivideRequirementLstForPage',
			success:function(data){
				if(data.error!=null && data.error.length>0){
					layer.alert("查询失败："+data.error,{offset:'100px'});
					return;
				}else{
					var html='';
					var requirementLst = data.requirementLst;
					var totalPage = data.totalPage;
					var startIndex = data.startIndex;
					for(var i=0; i<requirementLst.length; i++){
						var divide = requirementLst[i].divide;
						if(divide==0){
							divide = "未完成";
						}else if(divide==1){
							divide = "完成";
						}
						var stage = requirementLst[i].stage;
						if(("完成"==divide && "已完成"==stage) || requirementLst[i].status=='作废'){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</td></tr>';
						}
						else if("完成"==divide && "已完成"!=stage){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
             				'</a>'+
	              			'</td></tr>';
						}
						else {
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showSubRequirement?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="任务分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-jihuafenjie"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="completeDivide('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="完成分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-wancheng"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand delete" onclick="deleteRequire('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-delete"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
	              			'</a>'+
	              			'</td></tr>';		
						}
					}
					$("#_tab").html(html);
					if(page<2 || totalPage<0){
						$(".pager li:eq(0)").addClass("disabled");
					}else{
						$(".pager li:eq(0)").removeClass("disabled");
					}
					if(page>=totalPage){
						$(".pager li:eq(1)").addClass("disabled");
					}else{
						$(".pager li:eq(1)").removeClass("disabled");
					}
					$(".pager label").text("第"+page+"页，共"+totalPage+"页");
				}
			},
			complete:function(){
				$(".demand").each(function(){
					if($(this).text().length>10){
						$(this).html($(this).text().replace(/\s+/g, "").substr(0, 10) + "...");
					}
				});
				Load.Base.LoadingPic.FullScreenHide();
			}
    	});
    }
    function changePage(target, index){
    	if($(target).parent().attr("class")=='disabled'){
    		return;
    	}
    	if('previous'==index){
    		page = page-1;
    	}else if('next'==index){
    		page = page+1;
    	}
    	var status = $("#selectStatus").find("option:selected").val();
    	var number = $("#number").val();
    	var chooseP = $("#chooseP").val();
    	var requireName = $("#requireName").val();
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId,'status':status,'number':number,'chooseP':chooseP,'requireName':requireName},
			url:'/performance/soft/showDivideRequirementLstForPage',
			success:function(data){
				if(data.error!=null && data.error.length>0){
					layer.alert("查询失败："+data.error,{offset:'100px'});
					return;
				}else{
					var html='';
					var requirementLst = data.requirementLst;
					var totalPage = data.totalPage;
					var startIndex = data.startIndex;
					for(var i=0; i<requirementLst.length; i++){
						var divide = requirementLst[i].divide;
						if(divide==0){
							divide = "未完成";
						}else if(divide==1){
							divide = "完成";
						}
						var stage = requirementLst[i].stage;
						if(("完成"==divide && "已完成"==stage) || requirementLst[i].status=='作废'){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</td></tr>';	
						}
						else if("完成"==divide && "已完成"!=stage){
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
             				'</a>'+
	              			'</td></tr>';
						}
						else {
							html += '<tr>'+
	              			'<td >'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='作废'){
	              				html += '<td class="red">'+requirementLst[i].status+'</td>';
	              			}else if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td class="stage">'+requirementLst[i].stage+'</td>'+
	              			/* '<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+ */
	              			'<td>'+divide+'</td>'+
	              			'<td>'+
	              			'<a onclick="goPath(\'/performance/soft/showSubRequirement?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="任务分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-jihuafenjie"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="completeDivide('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="完成分解" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-wancheng"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand delete" onclick="deleteRequire('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true">'+
             				'<use xlink:href="#icon-delete"></use>'+
             				'</svg>'+
	              			'</a> '+
	              			'<a class="hand" onclick="changeVersion(this,'+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="版本转换" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-zhuanhuan"></use>'+
             				'</svg> '+
	              			'</a>'+
	              			'</td></tr>';	
						}
					}
					$("#_tab").html(html);
					if(page<2 || totalPage<0){
						$(".pager li:eq(0)").addClass("disabled");
					}else{
						$(".pager li:eq(0)").removeClass("disabled");
					}
					if(page>=totalPage){
						$(".pager li:eq(1)").addClass("disabled");
					}else{
						$(".pager li:eq(1)").removeClass("disabled");
					}
					$(".pager label").text("第"+page+"页，共"+totalPage+"页");
				}
			},
			complete:function(){
				$(".demand").each(function(){
					if($(this).text().length>10){
						$(this).html($(this).text().replace(/\s+/g, "").substr(0, 10) + "...");
					}
				});
				Load.Base.LoadingPic.FullScreenHide();
			}
    	});
    }
    function deleteRequirement(target){
		layer.open({  
            content: '确定删除吗？',  
            btn: ['确认', '取消'],  
            yes: function() {  
            	var requirementId = $(target).attr("data-preifx");
  	      		$.ajax({
  	    			url:'/performance/soft/checkIsCreateTask',
  	    			type:'post',
  	    			data:{'byType':'require','requireId':requirementId},
  	    			dataType:'json',
  	    			success:function (data){
  	    				if(data.exist=="true"){
  	    					layer.alert("该需求已分配任务，不可删除！",{offset:'100px'});
  	    				}else{
  	    					window.location.href='/performance/soft/deleteRequirement?requirementId='+requirementId; 
  	    				}
  	    			}
  	    		});
            }
        }); 
    }
	$("#submitButton").click(function(){
		var versionNumberValue = $("#versionNumber").val();
		if(versionNumberValue==""){
			layer.alert("必须选择版本号才能查询",{title:'提示',offset:['100px']});
		}else{
			var obj = $("a[data-preifx='"+versionId+"']");
			showRequirementLst(obj);
		}
		
		 
	});
	
	 function completeDivide(requireId){
			layer.open({  
	            content: '确定完成分解吗？',  
	            btn: ['确认', '取消'], 
	            offset:'100px',
	            yes: function() {  
	  	      		$.ajax({
	  	    			url:'/performance/soft/checkIsDivide',
	  	    			type:'post',
	  	    			data:{'requireId':requireId},
	  	    			dataType:'json',
	  	    			success:function (data){
	  	    				if(data.isDivide=="false"){
	  	    					layer.alert("该需求还未分解，无法完成分解！",{offset:'100px'});
	  	    				}else{
	  	    					window.location.href='/performance/soft/completeDivide?requireId='+requireId; 
	  	    				}
	  	    			}
	  	    		});
	            }
	        }); 
	  }
	 function deleteRequire(requireId){
			layer.open({  
	            content: '<div class="form-group">'+
	    				 '<label class="col-sm-4 control-label">作废原因<span style="color:red">*</span>：</label>'+
	    			     '<div class="col-sm-8">'+
	    				 '<textarea class="form-control" id="deleteReason"/></div></div>',
	            btn: ['确认', '取消'], 
	            offset:'100px',
	            yes: function() { 
	            	var deleteReason = $("#deleteReason").val();
	            	if(deleteReason==''){
	            		layer.alert("作废原因不能为空",{offset:'100px'});
	            		return;
	            	}
	            	window.location.href='/performance/soft/deleteRequire?requireId='+requireId+'&deleteReason='+deleteReason; 
	            }
	        }); 
	  }
	 function changeVersion(target, requireId){
		   // if(!checkRequireIsInDevelop(target)){
		   // 	layer.alert("需求已分配任务，无法切换版本",{offset:'100px'});
		   // 	return;
		   // }
		 	$("#changeVersionRequireId").val(requireId);
     		$.ajax({
	    			url:'/performance/soft/getVersionsByRequireId',
	    			type:'post',
	    			data:{'requireId':requireId},
	    			dataType:'json',
	    			success:function (data){
	    				var options = '';
	    				data.versions.forEach(function(value, index){
	    					if(versionId==value.id){
	    						options += "<option selected value='"+value.id+"'>"+value.version+"</option>";
	    					}else{
	    						options += "<option value='"+value.id+"'>"+value.version+"</option>";
	    					}
	    				});
	    				$("#selectVersion").html(options);
	    				$("#changeVersion").modal("show");
	    			}
	    		});
	  }
	 //检查需求是否已有任务分配
	 function checkRequireIsInDevelop(target){
		 var stage = $(target).parent().parent().find(".stage").text();
		 if(stage=="开发中"){
			 return false;
		 }
		 return true;
	 }
  	</script>
</body>
</html>