<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/structure/normalize.css" />
<link rel="stylesheet" href="assets/css/structure/font-awesome.min.css"/>
<link rel="stylesheet" href="assets/css/structure/jquery.orgchart.css"/>
<link rel="stylesheet" href="assets/css/structure/style.css"/>
<style type="text/css">

.inputout{position:relative;}
.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
.text_down ul{padding:2px 10px;}
.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down ul li span{color:#cc0000;}

.inputout1{position:relative;}
.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
.text_down1 ul{padding:2px 10px;}
.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down1 ul li span{color:#cc0000;}

.orgchart .second-menu-icon {
	transition: opacity .5s;
	opacity: 0;
	right: -5px;
	top: -5px;
	z-index: 2;
	color: rgba(68, 157, 68, 0.5);
	font-size: 18px;
	position: absolute;
}
.orgchart .second-menu-icon:hover {
  color: #449d44;
}
.orgchart .node:hover .second-menu-icon {
  opacity: 1;
}
.orgchart .node .second-menu {
  display: none;
  position: absolute;
  top: 0;
  right: -70px;
  border-radius: 35px;
  box-shadow: 0 0 10px 1px #999;
  background-color: #fff;
  z-index: 1;
}
.orgchart .node .second-menu .avatar {
  width: 60px;
  height: 60px;
  border-radius: 30px;
}
.orgchart{
  background-image:none;
}
.orgchart .node .title,.orgchart .down{
  background-color:#45A4DB;
}
.orgchart td.left,.orgchart td.right, .orgchart td.top{
	border-color:#45A4DB;
}
.orgchart .node .title .symbol{
	position:absolute;
	left:2px;
	top:14px;
}
.orgchart .node .title{
	height:50px;
	line-height:50px;
}
.personImg{
	width:35px;
	height:45px;
	border-radius:20px;
	position:absolute;
	right:2px;
	top:2px
}
.orgchart .node .content{
	height:25px;
	line-height:25px;
}
.icon {
   width: 2em; height: 2em;
   vertical-align: -0.15em;
   fill: currentColor;
   overflow: hidden;
}
.iconClass {
	position: fixed;
	top: 10%;
	left: 20%;
	z-index: 100;
}
.removeUnderline:hover, .removeUnderline:focus{
	text-decoration:none
}

</style>
</head>
<body>
<div class="container-fluid">
	<div class="row">
		<s:set name="panel" value="'structure'"></s:set>
		<%@include file="/pages/informationCenter/panel.jsp"%>
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<div id="chart-container" style="height:93%">
		<div class="iconClass">
			<auth:hasPermission name="structureManage">
			<a href="javascript:addNode()" class="removeUnderline">
       		<svg class="icon" aria-hidden="true" title="新增" data-toggle="tooltip" data-placement="bottom">
   				<use xlink:href="#icon-add"></use>
   			</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       		<a href="javascript:deleteNode()" class="removeUnderline">
       		<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip" data-placement="bottom">
   				<use xlink:href="#icon-delete"></use>
   			</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       		<a href="javascript:updateNode()" class="removeUnderline">
       		<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip" data-placement="bottom">
   				<use xlink:href="#icon-modify"></use>
   			</svg>
       		</a>
       		</auth:hasPermission>
       		&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:larger()" class="removeUnderline">
       		<svg class="icon" aria-hidden="true" title="放大" data-toggle="tooltip" data-placement="bottom">
   				<use xlink:href="#icon-fangda"></use>
   			</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       		<a href="javascript:smaller()" class="removeUnderline">
       			<svg class="icon" aria-hidden="true" title="缩小" data-toggle="tooltip" data-placement="bottom">
   					<use xlink:href="#icon-suoxiao"></use>
   				</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       		<a href="javascript:openAll()" class="removeUnderline">
       			<svg class="icon" aria-hidden="true" title="全部展开" data-toggle="tooltip" data-placement="bottom">
   					<use xlink:href="#icon-zhankai"></use>
   				</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       		<a href="javascript:exportImg()" class="removeUnderline">
       			<svg class="icon" aria-hidden="true" title="导出图片" data-toggle="tooltip" data-placement="bottom">
   					<use xlink:href="#icon-tupian"></use>
   				</svg>
       		</a>
       		&nbsp;&nbsp;&nbsp;&nbsp;
       	</div>
		</div>
		</div>
	</div>
</div>
<script src="/assets/js/layer/layer.js"></script>
<%-- <script type="text/javascript" src="assets/js/html2canvas.min.js"></script> --%>
<script type="text/javascript" src="assets/js/html2canvas.js"></script>
<script type="text/javascript" src="assets/js/canvas2image.js"></script>
<script type="text/javascript" src="assets/js/jquery.orgchart.js"></script>
<script src="/assets/icon/iconfont.js?version=<%=Math.random()%>"></script>
<script type="text/javascript">
var datascource;
(function($){

  $(function() {
	$("[data-toggle='tooltip']").tooltip();
	datascource = JSON.parse('${dataScource}');
	$('#chart-container').orgchart({
	  'data' : datascource,
	  'depth': parseInt('${depth}'),
	  'nodeTitle': 'name',
	  'nodeContent': 'title',
	  'nodeID': 'id',
	  'createNode': function($node, data) {
		/* var nodePrompt = $('<i>', {
		  'class': 'fa fa-info-circle second-menu-icon',
		  click: function() {
			$(this).siblings('.second-menu').toggle();
		  }
		}); */
		//var secondMenu = '<div class="second-menu"><img class="avatar" src="img/avatar/' + data.id + '.jpg"></div>';
		//$node.append(nodePrompt).append(secondMenu);
		$node.data("id",data.id);
		if(data.userId){
			$node.data("userId",data.userId);
			$node.find(".title").append("<img class='personImg' src='/HR/staff/getPictureForStructure?userID="+data.userId+"'/>");
		}else{
			$node.find(".title").css("border-radius","5px");
			$node.find(".content").remove();
		}
	  }
	});
	$('body').on('click','.input_text',function (event) { 
		if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
		}
		$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#executor").val(shtml.split("（")[0]);
			$("#executorFlag").val(shtml.split("（")[0]);
			var executor = $(this).find("input").val();
			$("input[name='structureNode.userId']").val(executor.split("@")[0]);
			$("input[name='structureNode.userName']").val(executor.split("@")[1]);
		});
	}); 
	$('body').on('click','.input_text1',function (event) { 
		if($(".text_down1 ul").html() != ""){
			$(".text_down1").show();
			event.stopPropagation();
		}
		$('body').on('click','.text_down1 ul li',function () {
			var shtml=$(this).html();
			$(".text_down1").hide();
			$("#executor1").val(shtml.split("（")[0]);
			$("#executorFlag1").val(shtml.split("（")[0]);
			var executor = $(this).find("input").val();
			$("input[name='structureNode.userId']").val(executor.split("@")[0]);
			$("input[name='structureNode.userName']").val(executor.split("@")[1]);
		});
	}); 
	$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#executor").val()!=$("#executorFlag").val()) {$("#executor").val("");}});  
	$('.inputout').click(function (event) {$(".text_down").show();});
	
	$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#executor1").val()!=$("#executorFlag1").val()) {$("#executor1").val("");}});  
	$('.inputout1').click(function (event) {$(".text_down1").show();});
  });
})(jQuery);
function findStaffByName() {
	var name = $("#executor").val();
	if (name.length == 0) {
		return;
	}
	$(".text_down ul").empty();
	$.ajax({
		url:'personal/findStaffByName',
		type:'post',
		data:{name:name},
		dataType:'json',
		success:function (data){
			$.each(data.staffVOs, function(i, staff) {
				var groupDetail = staff.groupDetailVOs[0];
				$(".text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
			});
			$(".text_down").show();
		}
	});
}
function findStaffByName1() {
	var name = $("#executor1").val();
	if (name.length == 0) {
		return;
	}
	$(".text_down1 ul").empty();
	$.ajax({
		url:'personal/findStaffByName',
		type:'post',
		data:{name:name},
		dataType:'json',
		success:function (data){
			$.each(data.staffVOs, function(i, staff) {
				var groupDetail = staff.groupDetailVOs[0];
				$(".text_down1 ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
			});
			$(".text_down1").show();
		}
	});
}
function addNode(){
	var selectedNode = $(".focused");
	var title = selectedNode.find(".title").text();
	var content = selectedNode.find(".content").text();
	if(!title){
		layer.alert("请选择父节点",{offset:'100px'});
		return;
	}
	$("input[name='structureNode.parentId']").val(selectedNode.data("id"));
	$("input[name='structureNode.departmentName']").val('');
	$("input[name='structureNode.userId']").val('');
	$("input[name='structureNode.userName']").val('');
	$("input[name='userName']").val('');
	$("input[name='userNameFlag']").val('');
	$("#addNode").modal("show");
}

function deleteNode(){
	var selectedNode = $(".focused");
	var id = selectedNode.data("id");
	if(!id){
		layer.alert("请选择需要删除的节点",{offset:'100px'});
		return;
	}
	var name = '';
	var title = selectedNode.find(".title").text();
	var content = selectedNode.find(".content").text();
	if(!content){
		name = title;
	}else{
		name = content;
	}
	layer.confirm("确认删除【"+name+"】？",{offset:'100px'},function(index){
		layer.close(index);
		Load.Base.LoadingPic.FullScreenShow(null);
		location.href="/structure/deleteStructureNode?id="+id;
	});
}
function updateNode(){
	var selectedNode = $(".focused");
	var title = selectedNode.find(".title").text();
	var content = selectedNode.find(".content").text();
	if(!title){
		layer.alert("请选择需要修改的节点",{offset:'100px'});
		return;
	}
	var userId = selectedNode.data("userId");
	if(userId){
		$("input[name='structureNode.userId']").val(userId);
		$("input[name='structureNode.userName']").val(title);
		$("input[name='userName']").val(title);
		$("input[name='userNameFlag']").val(title);
		$("input[name='structureNode.departmentName']").val(content);
	}else{
		$("input[name='structureNode.departmentName']").val(title);
		$("input[name='structureNode.userId']").val('');
		$("input[name='structureNode.userName']").val('');
		$("input[name='userName']").val('');
		$("input[name='userNameFlag']").val('');
	}
	$("input[name='structureNode.id']").val(selectedNode.data("id"));
	$("#updateNode").modal("show");
}
var scale = 1;
function smaller(){
	scale = scale-0.1;
	$(".orgchart").css("-webkit-transform", "scale("+scale+")").css("-webkit-transform-origin-x", "0");
	
	if(scale>1){
		$(".orgchart").css("top",55*scale+"px");
	}else if(scale==1){
		$(".orgchart").css("top","30px");
	}
}
function larger(){
	scale = scale+0.1;
	$(".orgchart").css("-webkit-transform", "scale("+scale+")").css("-webkit-transform-origin-x", "0");
	if(scale>1){
		$(".orgchart").css("top",55*scale+"px");
	}
}
function updateNodeInfo(){
	var departmentName = $("#updateNode input[name='structureNode.departmentName']").val();
	if(!departmentName.trim()){
		layer.alert("部门/职务不能为空",{offset:'100px'});
		return;
	}
	$("#updateNode").modal('hide');
	Load.Base.LoadingPic.FullScreenShow(null);
	$.ajax({
		type:'post',
		url:'/structure/updateStructureNode',
		data:$("#nodeInfoForm").serialize(),
		success:function(data){
			var structureNode = data.structureNode;
			if(data.success){
				var selectedNode = $(".focused");
				if(structureNode.userId){
					selectedNode.find(".title").text(structureNode.userName);
					var departmentName = selectedNode.find(".content").text();
					if(!departmentName){
						selectedNode.append("<div class='content'><div>");
					}
					selectedNode.find(".content").text(structureNode.departmentName);
					selectedNode.data("userId",structureNode.userId);
				}else{
					selectedNode.find(".title").text(structureNode.departmentName);
					selectedNode.find(".content").remove();
					selectedNode.data("userId","");
				}
			}
		},
		complete:function(){
			Load.Base.LoadingPic.FullScreenHide();
		}
	})
}
function addLoding(){
	$("#addNode").modal('hide');
	Load.Base.LoadingPic.FullScreenShow(null);
}
function checkEmpty(obj){
	var userName = $(obj).val();
	if(!userName){
		$("input[name='structureNode.userId']").val('');
		$("input[name='structureNode.userName']").val('');
		$("input[name='userNameFlag']").val('');
	}
}
function exportImg(){
	//不清晰
/* 	 var targetDom = $(".orgchart"); 
	var copyDom = targetDom.clone();
	copyDom.find(".symbol").remove();
	copyDom.css("background-color","#fff").css("-webkit-transform", "scale(1)");
	$("body").append(copyDom);
 	html2canvas(copyDom, { 
		allowTaint: true,    
        taintTest: false, 
		onrendered: function(canvas){
			 var imgUrl = canvas.toDataURL();
			 copyDom.remove();
			 var download = $("<a>").attr("href",imgUrl).attr("download","智造链科技2018年组织架构.png").appendTo("body");
			 download[0].click();  
			 download.remove();  
		}
    }); */
    var targetDom = document.getElementsByClassName("orgchart")[0];
	var copyDom = targetDom.cloneNode(true);
    $(copyDom).find(".symbol").remove();
	$(copyDom).css("background-color","#fff").css("-webkit-transform", "scale(1)").css("height","95%");
	$("body").html(copyDom);
	Load.Base.LoadingPic.FullScreenShow(null);
	var width = copyDom.offsetWidth;
	var height = copyDom.offsetHeight;
	var canvas = document.createElement("canvas"); //创建一个canvas节点
	var scale = 2; //定义任意放大倍数 支持小数
	canvas.width = width * scale; //定义canvas 宽度 * 缩放
	canvas.height = height * scale; //定义canvas高度 *缩放
	canvas.getContext("2d").scale(scale,scale); //获取context,设置scale 
	var opts = {
	    scale:scale, // 添加的scale 参数
	    canvas:canvas, //自定义 canvas
	    logging: true, //日志开关
	    width:width, //dom 原始宽度
	    height:height //dom 原始高度
	};
	
	html2canvas(copyDom, opts).then(function (canvas) {
	    //如果想要生成图片 引入canvas2Image.js 下载地址：
	    //https://github.com/hongru/canvas2image/blob/master/canvas2image.js
 	    var imgUrl = Canvas2Image.convertToImage(canvas, canvas.width, canvas.height).src;
		var download = $("<a>").attr("href",imgUrl).attr("download","智造链科技2018年组织架构.png").appendTo("body");
		download[0].click();  
		download.remove(); 
		location.reload();
	});
}
function openAll(){
	$(".orgchart").remove();
	$('#chart-container').orgchart({
		  'data' : datascource,
		  'depth': 10,
		  'nodeTitle': 'name',
		  'nodeContent': 'title',
		  'nodeID': 'id',
		  'createNode': function($node, data) {
			/* var nodePrompt = $('<i>', {
			  'class': 'fa fa-info-circle second-menu-icon',
			  click: function() {
				$(this).siblings('.second-menu').toggle();
			  }
			}); */
			//var secondMenu = '<div class="second-menu"><img class="avatar" src="img/avatar/' + data.id + '.jpg"></div>';
			//$node.append(nodePrompt).append(secondMenu);
			$node.data("id",data.id);
			if(data.userId){
				$node.data("userId",data.userId);
				$node.find(".title").append("<img class='personImg' src='/HR/staff/getPictureForStructure?userID="+data.userId+"'/>");
			}else{
				$node.find(".title").css("border-radius","5px");
				$node.find(".content").remove();
			}
		  }
		});
	$(".orgchart").css("-webkit-transform", "scale("+scale+")").css("-webkit-transform-origin-x", "0");
	if(scale>1){
		$(".orgchart").css("top",55*scale+"px");
	}else{
		$(".orgchart").css("top","30px");
	}
}
</script>
<div id="addNode" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal" method="post" action="/structure/addStructureNode"
				 onsubmit="addLoding()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">新增节点</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label class="col-sm-3 control-label">部门/职务<span style="color:red"> *</span></label>
					<div class="col-sm-6">
						<input class="form-control" autocomplete="off" required name="structureNode.departmentName"/>
					</div>
				</div>
				<div class="form-group">
				<label class="col-sm-3 control-label">负责人</label>
			    <div class="col-sm-6 inputout">
			    	<span class="input_text">
			    	<input autoComplete="off" type="text" id="executor" name="userName" class="form-control" oninput="findStaffByName()"  onblur="checkEmpty(this)"/>
			    	<input type="hidden" id="executorFlag" name="userNameFlag" value=""/>
			    	<input type="hidden" id="executorID" name="structureNode.userId" />
			    	<input type="hidden" id="executorName" name="structureNode.userName" />
			    	</span>
			    	<div class="text_down">
						<ul></ul>
					</div>
			  	</div>
	    	  </div> 
			</div>
			<input type="hidden" name="structureNode.parentId">
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
</div>
<div id="updateNode" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form id="nodeInfoForm" class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">修改节点</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">部门/职务<span style="color:red"> *</span></label>
					<div id="name" class="col-sm-6">
						<input class="form-control" autocomplete="off" required name="structureNode.departmentName"/>
					</div>
				</div>
				<div class="form-group">
				<label class="col-sm-3 control-label">负责人</label>
			    <div class="col-sm-6 inputout1">
			    	<span class="input_text1">
			    	<input autoComplete="off" type="text" id="executor1" name="userName" class="form-control" oninput="findStaffByName1()"  onblur="checkEmpty(this)"/>
			    	<input type="hidden" id="executorFlag1" name="userNameFlag" value=""/>
			    	<input type="hidden" id="executorID1" name="structureNode.userId" />
			    	<input type="hidden" id="executorName1" name="structureNode.userName" />
			    	</span>
			    	<div class="text_down1">
						<ul></ul>
					</div>
			  	</div>
	    	  </div> 
			</div>
			<input type="hidden" name="structureNode.id">
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="updateNodeInfo()" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
</div>
</body>
</html>