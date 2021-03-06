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
			<s:set name="selectedPanel" value="'showRequirement'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">版本管理</h3>
				<%-- <div>
				<a class="btn btn-primary"  href="/performance/soft/toEditRequirement"><span class="glyphicon glyphicon-plus"></span>需求</a>
				</div> --%>
				<form class="form-horizontal">
      				<div class="form-group">
      				<label for="user" class="col-sm-1 control-label">状态</label>
      					<div class="col-sm-2">
      					<select id="selectStatus" class="form-control">
      						<option selected value="0">全部</option>
      						<option value="2">未完成分解</option>
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
						<div class="col-sm-2" style="text-align:right">
						<a class="btn btn-primary hand"  onclick="exportVersionInfo()" ><span class="glyphicon glyphicon-download-alt"></span> 版本</a>
						</div>
      				</div>
      			</form>
				<br>
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
	                  <th style="width:4%">P</th>
	                  <th style="width:14%">需求名称</th>
	                  <th style="width:7%">模块名</th>
	                  <th style="width:6%">创建</th>
	                  <th style="width:5%">状态</th>
	                  <th style="width:6%">阶段</th>
	                  <th style="width:6%">分解</th>
	                  <th style="width:5%">已分配任务</th>
	                  <th style="width:5%">总任务</th>
	                  <th style="width:8%">操作</th>
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
	<script src="/assets/js/textarea/marked.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
	var projectAndRole = new Map();
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
			var role = versionArray[3];
			projectAndRole.set(project, role);
			for(var i=0; i<versionIds.length; i++){
				var versionName = versionNames[i];
				var status = statuss[i];
				if(status=="已完成"){
					versionName += "(已完成)"
					html += '<li class="list-group-item">'+
					'<a class="hand" data-preifx="'+versionIds[i]+'" onclick="showRequirementLst(this)">'
					+versionName+'</a>'+
					'</li>';
				}else{
					if(role=="产品经理"){
						html += '<li class="list-group-item">'+
						'<a class="hand" data-preifx="'+versionIds[i]+'" onclick="showRequirementLst(this)">'
						+versionName+'</a><a class="hand" onclick="comfirmRequire('+versionIds[i]+')" style="float:right">完成</a>'+
						'</li>';
					}else{
						html += '<li class="list-group-item">'+
						'<a class="hand" data-preifx="'+versionIds[i]+'" onclick="showRequirementLst(this)">'
						+versionName+'</a>'+
						'</li>';
					}
				}
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
   	 	var userSelectedNode = storage.getItem("userSelectedNode");
		 if(userSelectedNode!=null){
			 var userSelect = userSelectedNode.split(",");
			 var id = userSelect[0];
			 var version = userSelect[1];
			 $("#"+id).click();
			 var obj = $("a[data-preifx='"+version+"']");
			 showRequirementLst(obj);
		 }
    });
    var versionId='';
    var limit ='';
    var page='';
    var role='';
    function showRequirementLst(target){
    	var projectName = $(target).parent().parent().parent().prev().find("h4").text();
    	role = projectAndRole.get(projectName);
    	//记录下用户选择的节点
    	var version = $(target).attr('data-preifx');
    	var id = $(target).parent().parent().parent().prev().attr("id");
    	var userSelectedNode = [id, version];
    	storage.setItem("userSelectedNode",userSelectedNode);
    	//改变当前点击的版本的背景颜色
    	$("#projectVersions li").css("background-color","#fff");
    	$(target).parent().css("background-color","rgb(201, 221, 241)");
    	versionId = $(target).attr("data-preifx");
    	if(!versionId){
    		return;
    	}
    	var status = $("#selectStatus").find("option:selected").val();
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'versionId':versionId,'status':status},
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
						if((divide=='未完成'||(divide=='完成'&&requirementLst[i].stage=='待开发')) && requirementLst[i].status!='作废'){
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true"  title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</a> ';
			              	if(role=="产品经理" || role=="需求分析"){
			              		html += '<a href="/performance/soft/toEditRequirement?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-modify"></use>'+
	             				'</svg> '+
			              		'</a> '+
		              			'<a style="cursor:hand" class="delete" onclick="deleteRequirement(this)" data-preifx="'+requirementLst[i].id+'"><svg class="icon" aria-hidden="true"  title="删除" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-delete"></use>'+
	             				'</svg>'+
			              		'</a>';
			              	}
	              			
	              		html +=	'</td></tr>';	
						}
						else{
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
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
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
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
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId},
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
						if((divide=='未完成'||(divide=='完成'&&requirementLst[i].stage=='待开发')) && requirementLst[i].status!='作废'){
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</a> ';
			              	if(role=="产品经理" || role=="需求分析"){
			              		html += '<a href="/performance/soft/toEditRequirement?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-modify"></use>'+
	             				'</svg> '+
			              		'</a> '+
		              			'<a style="cursor:hand" class="delete" onclick="deleteRequirement(this)" data-preifx="'+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-delete"></use>'+
	             				'</svg>'+
			              		'</a>';
			              	}
	              			
	              		html +=	'</td></tr>';	
						}
						else{
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
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
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
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
				$("[data-toggle='tooltip']").tooltip();
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
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId},
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
						if((divide=='未完成'||(divide=='完成'&&requirementLst[i].stage=='待开发')) && requirementLst[i].status!='作废'){
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
	              			'<td>'+requirementLst[i].id+'</td>'+
	              			'<td>'+requirementLst[i].priority+'</td>'+
	              			'<td class="demand" title="'+requirementLst[i].name+'">'+requirementLst[i].name+'</td>'+
	              			'<td>'+requirementLst[i].module+'</td>'+
	              			'<td>'+requirementLst[i].creator+'</td>';
	              			if(requirementLst[i].status=='激活'){
	              				html += '<td class="green">'+requirementLst[i].status+'</td>';
	              			}else{
	              				html += '<td>'+requirementLst[i].status+'</td>';
	              			}
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
	              			'</a> ';
			              	if(role=="产品经理" || role=="需求分析"){
			              		html += '<a href="/performance/soft/toEditRequirement?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-modify"></use>'+
	             				'</svg> '+
			              		'</a> '+
		              			'<a style="cursor:hand" class="delete" onclick="deleteRequirement(this)" data-preifx="'+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-delete"></use>'+
	             				'</svg>'+
			              		'</a>';
			              	}
	              			
	              		html +=	'</td></tr>';	
						}
						else{
							html += '<tr>'+
	              			'<td>'+(i+startIndex)+'</td>'+
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
	              			html += '<td>'+requirementLst[i].stage+'</td>'+
	              			'<td>'+divide+'</td>'+
	              			'<td><a class="hand" onclick="confirmTask('+requirementLst[i].id+')">'+requirementLst[i].taskNum+'</a></td>'+
	              			'<td>'+requirementLst[i].sumNum+'</td>'+
	              			'<td>'+
	              			'<a href="/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'"><svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">'+
             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
             				'</svg>'+
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
				$("[data-toggle='tooltip']").tooltip();
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
			offset:'100px',
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
    var requireId='';
    function confirmTask(requirementId){
       requireId = requirementId;
 	   $.ajax({
 		   	url:'/performance/soft/showTaskByRequire',
   			type:'post',
   			data:{'requirementId':requirementId},
   			dataType:'json',
   			success:function (data){
   				var taskLst = data.taskLst;
   				var html = '';
   				for(var i=0; i<taskLst.length; i++){
   					var result = taskLst[i][4];
   					var status='';
   					switch(result){
	   					case 6:status='任务结束'; break;
	   					case 29:status='分值无效'; break;
	   					case 30:status='分值有效'; break;
	   					case 31:status='任务终止'; break;
	   					case 3:status='开发中';break;
	   					case 5:status='开发中';break;
	   					case 26:status='测试中';break;
	   					case 2:status='开发中';break;
	   					case 25:status='修改任务';break;
	   					case 49:status='实施中';break;
	   					case 28:status='确认分值中';break;
	   					default:status='开发中';
   					}
   					html += '<tr><td class="left">'+(i+1)+'</td>'+
								/* '<td class="left">'+taskLst[i][1]+'</td>'+ */
								'<td class="left">'+taskLst[i][0]+'</td>'+
								/* '<td class="left">'+taskLst[i][2]+'</td>'+ */
								'<td class="left">'+taskLst[i][3]+'</td>'+
								'<td class="left">'+status+'</td><tr>';
								
   				}
   				$("#content").html(html);
   			  	$("#task").modal('show');
   			}
 	   });
    }
    var versionId='';
    function comfirmRequire(versionId){
       this.versionId = versionId;
   	   $.ajax({
		   	url:'/performance/soft/showTaskByVersion',
  			type:'post',
  			data:{'versionId':versionId},
  			dataType:'json',
  			success:function (data){
  				var html='';
  				var requireAndTaskLstMap = JSON.parse(data.requireAndTaskLstMap);
  				for(var require in requireAndTaskLstMap){
  					var requireInfo = require.split("#&&#");
  					html+= '<div>'+
  						   '<span style="font-weight:bold">需求名称：</span><span>'+requireInfo[0]+'</span>&nbsp;&nbsp;'+
  						   '<span style="font-weight:bold">创建人：</span><span>'+requireInfo[1]+'</span>&nbsp;&nbsp;'+
  						   '<span style="font-weight:bold">阶段：</span><span class="requireStatus">'+requireInfo[2]+'</span>'+
  						   '</div>';
  				    html+= '<div>任务列表如下：</div>';
  				    html+= '<table class="table table-striped table-bordered">'+
	              			'<thead>'+
	                		'<tr>'+
		                  '<th class="col-sm-1">ID</th>'+
		                  '<th class="col-sm-2">任务名称</th>'+
		                  '<th class="col-sm-2">责任人</th>'+
		                  '<th class="col-sm-2">当前状态</th>'+
                		  '</tr></thead><tbody>';
  					var taskLst = requireAndTaskLstMap[require];
  					for(var i=0; i<taskLst.length; i++){
  						if(taskLst[i].name=='' || taskLst[i].name==null){
  							continue;
  						}
  						var result = taskLst[i].result;
  	   					var status='';
  	   					switch(result){
  		   					case '6':status='任务结束'; break;
  		   					case '29':status='分值无效'; break;
  		   					case '30':status='分值有效'; break;
  		   					case '31':status='任务终止'; break;
  		   					case '3':status='开发中';break;
  		   					case '5':status='开发中';break;
  		   					case '26':status='测试中';break;
  		   					case '2':status='开发中';break;
  		   					case '25':status='修改任务';break;
  		   					case '49':status='实施中';break;
  		   					case '28':status='确认分值中';break;
  		   					default:status='开发中';
  	   					}
  						html += '<tr><td class="left">'+(i+1)+'</td>'+
						'<td class="left">'+taskLst[i].name+'</td>'+
						'<td class="left">'+taskLst[i].assigner+'</td>'+
						'<td class="left taskStatus">'+status+'</td></tr>';
  					}
  					html+='</tbody></table>';
  				}
  			     $("#requireContent").html(html);
  				 $("#require").modal('show');
  			}
	   });
    }
	$("#submitButton").click(function(){
		var obj = $("a[data-preifx='"+versionId+"']");
		showRequirementLst(obj);
	});
	function exportVersionInfo(){
		/* var colorBlue="rgb(201, 221, 241)";
		var versionName;
		$('#projectVersions').find('a:even').each(function (){ 
			if($(this).parent().prop("style").backgroundColor==colorBlue){
				versionName=$(this).html();
			}
		}) */
		var versionName = $("a[data-preifx='"+versionId+"']").text();
		var projectName = $("a[data-preifx='"+versionId+"']").parent().parent().parent().prev().find("h4").text();
		if(!versionId||!projectName){
			layer.alert("请选择版本",{offset:'100px'})
			return;
		}else{
			layer.confirm("是否导出：项目："+projectName+" 版本："+versionName+"的excel数据？",{offset:'100px'},function (index){
				window.location.href="/performance/soft/exportVersionInfo?versionId="+versionId+"&projectName="+projectName;
				layer.close(index);
			})
		}
		
	}
  	</script>
</body>
</html>