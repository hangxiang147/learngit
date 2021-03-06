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
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="selectedPanel" value="'showRequirementCopy'"></s:set>
			<%@include file="/pages/performance/soft/subject/panel.jsp" %>		
				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">分配任务</h3>
				<div class="col-sm-2">
				</div>
				<div class="col-sm-10 control-label" style="text-align:left;color:red">
				总工时：<span id="totalWorkHour">0</span>时；工时分配：
				<span id="workHour">0时</span>
				</div>
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
	                  <th style="width:5%">ID</th>
	                  <th class="col-sm-1">编号</th>
	                  <th style="width:6%">P</th>
	                  <th class="col-sm-2">需求名称</th>
	                  <th class="col-sm-2">模块名</th>
	                  <th class="col-sm-1">创建</th>
	                  <th class="col-sm-1">状态</th>
	                  <th class="col-sm-1">阶段</th>
	                  <th class="col-sm-1">已分配任务</th>
	                  <th class="col-sm-1">总任务</th>
	                  <th class="col-sm-2">操作</th>
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
				var versionName = versionNames[i];
				var status = statuss[i];
				if(status=="已完成"){
					versionName += "(已完成)" 
				}
				html += '<li class="list-group-item">'+
						'<a class="hand" data-preifx="'+versionIds[i]+'" onclick="showRequirementLst(this)">'
						+versionName+'</a>'+
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
    function showRequirementLst(target){
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
    	Load.Base.LoadingPic.FullScreenShow(null);
    	$.ajax({
			type:'post',
			data:{'versionId':versionId},
			url:'/performance/soft/showRequirementLst',
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
						html += '<tr>'+
		              			'<td>'+(i+startIndex)+'</td>'+
		              			'<td>'+requirementLst[i].id+'</td>'+
		              			'<td>'+requirementLst[i].priority+'</td>'+
		              			'<td>'+requirementLst[i].name+'</td>'+
		              			'<td>'+requirementLst[i].module+'</td>'+
		              			'<td>'+requirementLst[i].creator+'</td>'+
		              			'<td>'+(requirementLst[i].state=="1"?"已分解":"未分解")+'</td>'+
		              			'<td>'+requirementLst[i].stage+'</td>'+
		              			'<td>'+requirementLst[i].taskNum+'</td>'+
		              			'<td>'+requirementLst[i].sumNum+'</td>'+
		              			'<td>'+
		              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'&fromPreparedRequire=2\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="查看需求" data-toggle="tooltip">'+
	             				'<use xlink:href="#icon-Detailedinquiry"></use>'+
	             				'</svg> '+
		              			'</a> '+
		              			(requirementLst[i].state=='1'?'<a href="javascript:confirmTask('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="任务分配" data-toggle="tooltip">'+
			             		'<use xlink:href="#icon-xiahurenwufenpei"></use>'+
			             		'</svg> '+
		              			'</a> ':'')
		              			+'<a onclick="goPath(\'performance/soft/goToTaskManage?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="流程详情" data-toggle="tooltip">'+
			             		'<use xlink:href="#icon-liucheng"></use>'+
			             		'</svg> '+ 
		              			'</a>'+
		              			'</td></tr>';	
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
					$("#totalWorkHour").text(data.usedWorkHour);
					var hoursByPerson = "";
					data.hoursByPerson.forEach(function(value, index){
						hoursByPerson += value[1]+"："+value[0]+"时 ";
					});
					if(hoursByPerson!=''){
						$("#workHour").text(hoursByPerson);
					}else{
						$("#workHour").text('0时');
					}
					$("[data-toggle='tooltip']").tooltip();
					$("#num").html(data.count);
				}
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
    	});
    }
    function changeLimit(target){
    	limit = $(target).text();
    	$(".dropdown button").html(limit+" <span class='caret'></span>");
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId},
			url:'/performance/soft/showRequirementLstForPage',
			success:function(data){
				if(data.error!=null && data.error.length>0){
					layer.alert("查询失败："+data.error);
					return;
				}else{
					var html='';
					var requirementLst = data.requirementLst;
					var totalPage = data.totalPage;
					var startIndex = data.startIndex;
					for(var i=0; i<requirementLst.length; i++){
						html += '<tr>'+
              			'<td>'+(i+startIndex)+'</td>'+
              			'<td>'+requirementLst[i].id+'</td>'+
              			'<td>'+requirementLst[i].priority+'</td>'+
              			'<td>'+requirementLst[i].name+'</td>'+
              			'<td>'+requirementLst[i].module+'</td>'+
              			'<td>'+requirementLst[i].creator+'</td>'+
              			'<td>'+(requirementLst[i].state=="1"?"已分解":"未分解")+'</td>'+
              			'<td>'+requirementLst[i].stage+'</td>'+
              			'<td>'+requirementLst[i].taskNum+'</td>'+
              			'<td>'+requirementLst[i].sumNum+'</td>'+
              			'<td>'+
              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'&fromPreparedRequire=2\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="查看需求" data-toggle="tooltip">'+
         				'<use xlink:href="#icon-Detailedinquiry"></use>'+
         				'</svg> '+
              			'</a> '+
              			(requirementLst[i].state=='1'?'<a href="javascript:confirmTask('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="任务分配" data-toggle="tooltip">'+
	             		'<use xlink:href="#icon-xiahurenwufenpei"></use>'+
	             		'</svg> '+
              			'</a> ':'')
              			+'<a onclick="goPath(\'performance/soft/goToTaskManage?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="流程详情" data-toggle="tooltip">'+
	             		'<use xlink:href="#icon-liucheng"></use>'+
	             		'</svg> '+ 
              			'</a>'+
              			'</td></tr>';	
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
			}
    	});
    }
    function changePage(target,index){
    	if($(target).parent().attr("class")=='disabled'){
    		return;
    	}
    	if('previous'==index){
    		page = page-1;
    	}else if('next'==index){
    		page = page+1;
    	}
    	$.ajax({
			type:'post',
			data:{'limit':limit,'page':page,'versionId':versionId},
			url:'/performance/soft/showRequirementLstForPage',
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
						html += '<tr>'+
              			'<td>'+(i+startIndex)+'</td>'+
              			'<td>'+requirementLst[i].id+'</td>'+
              			'<td>'+requirementLst[i].priority+'</td>'+
              			'<td>'+requirementLst[i].name+'</td>'+
              			'<td>'+requirementLst[i].module+'</td>'+
              			'<td>'+requirementLst[i].creator+'</td>'+
              			'<td>'+(requirementLst[i].state=="1"?"已分解":"未分解")+'</td>'+
              			'<td>'+requirementLst[i].stage+'</td>'+
              			'<td>'+requirementLst[i].taskNum+'</td>'+
              			'<td>'+requirementLst[i].sumNum+'</td>'+
              			'<td>'+
              			'<a onclick="goPath(\'/performance/soft/showRequirementDetail?requirementId='+requirementLst[i].id+'&fromPreparedRequire=2\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="查看需求" data-toggle="tooltip">'+
         				'<use xlink:href="#icon-Detailedinquiry"></use>'+
         				'</svg> '+
              			'</a> '+
              			(requirementLst[i].state=='1'?'<a href="javascript:confirmTask('+requirementLst[i].id+')"><svg class="icon" aria-hidden="true" title="任务分配" data-toggle="tooltip">'+
	             		'<use xlink:href="#icon-xiahurenwufenpei"></use>'+
	             		'</svg> '+
              			'</a> ':'')
              			+'<a onclick="goPath(\'performance/soft/goToTaskManage?requirementId='+requirementLst[i].id+'\')" href="javascript:void(0)"><svg class="icon" aria-hidden="true" title="流程详情" data-toggle="tooltip">'+
	             		'<use xlink:href="#icon-liucheng"></use>'+
	             		'</svg> '+ 
              			'</a>'+
              			'</td></tr>';	
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
    	window.location.href="/performance/soft/getSubRequirement?requirementId="+requirementId;
    	Load.Base.LoadingPic.FullScreenShow(null);
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
  						   '<span style="font-weight:bold">阶段：</span><span>'+requireInfo[2]+'</span>'+
  						   '</div>';
  				    html+= '<div>任务列表如下：</div>';
  				    html+= '<table class="table table-striped table-bordered">'+
	              			'<thead>'+
	                		'<tr>'+
		                  '<th class="col-sm-1">ID</th>'+
		                  '<th class="col-sm-1">P</th>'+
		                  '<th class="col-sm-2">任务名称</th>'+
		                  '<th class="col-sm-2">任务类型</th>'+
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
  		   					default:status='进行中';
  	   					}
  						html += '<tr><td class="left">'+(i+1)+'</td>'+
						'<td class="left">'+taskLst[i].priority+'</td>'+
						'<td class="left">'+taskLst[i].name+'</td>'+
						'<td class="left">'+taskLst[i].taskType+'</td>'+
						'<td class="left">'+taskLst[i].assigner+'</td>'+
						'<td class="left">'+status+'</td></tr>';
  					}
  					html+='</tbody></table>';
  				}
  			     $("#requireContent").html(html);
  				 $("#require").modal('show');
  			}	
	   });
    }
  	</script>
</body>
</html>