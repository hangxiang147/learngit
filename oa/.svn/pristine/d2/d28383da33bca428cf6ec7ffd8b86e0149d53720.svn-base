<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function() {		
	$("[data-toggle='tooltip']").tooltip();
    $(document).click(function (event) {$(".text").hide();$(".text ul").empty();if ($("#agent").val()!=$("#agentFlag").val()) {$("#agent").val("");}});   
	$('.inputout').click(function (event) {$(".text_down").show();});
 
});
	var a=1;
	function add(){
		a++; 
		$("#demo").append(" <tr id="+a+">"+
                "<td><input type='text' class='form-control workContent' name='meetingMinutesVO.contents' style='width:700px; height:40px'/></td>"+ 
                   "<td>"+
                  "<div class='col-sm-2 inputout'>"+
			    	"<span class='input_text"+a+"' onclick='f("+a+")'>"+
			    	"<input type='text' id='agent"+a+"' class='form-control agentUser' required='required' oninput='findStaffByName("+a+")'  style='width:150px; height:40px' onblur='clears("+a+")'/>"+
			    	"<input type='hidden' id='agentFlag"+a+"' value=''/>"+
			    	"<input type='hidden' id='agentID"+a+"' name='meetingMinutesVO.ownerIDs' />"+		    	
			    	"</span>"+
			    	"<div class='text_down"+a+" text'>"+
					"<ul></ul>"+
					"</div>"+
			        "</div></td>"+
                 "<td><a onclick='delRow("+a+")' href='javascript:void(0)'><svg class='icon' aria-hidden='true' title='删除' data-toggle='tooltip'><use xlink:href='#icon-delete'></use></svg></a></td></tr>") ;
		$("[data-toggle='tooltip']").tooltip();			
	}
/* 	function check(obj){ 
		if (isNaN(obj.value)) 
		{alert("请输入数字！"); 
		obj.value="";} 
		}
	function check1(obj){ 
		if (isNaN(obj.value)) {
			alert("请输入数字！"); 
		    obj.value="";
		    }else{
		    	 if(obj.value!=""){ 
					if(parseInt(obj.value)!=parseFloat(obj.value)){
						alert("请输入整数！"); 
					    obj.value="";
						}
			   } 
			}
		    	 
		    } */
		
	function findStaffByName(index) {
		var name = $("#agent"+index).val();
		if (name.length == 0) {
			return;
		}
		$(".text_down"+index+" ul").empty();
		$.ajax({
			url:'personal/findStaffByName',
			type:'post',
			data:{name:name},
			dataType:'json',
			success:function (data){
				$.each(data.staffVOs, function(i, staff) {
					var groupDetail = staff.groupDetailVOs[0];
					$(".text_down"+index+" ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
				});
				$(".text_down"+index).show();
			}
		});
		
	}
	
	function f(index){
		if($(".text_down"+index+" ul").html() != ""){
			$(".text_down"+index).show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down'+index+' ul li',function () {
			var shtml=$(this).html();
			$(".text_down"+index).hide();
			$("#agent"+index).val(shtml.split("（")[0]);
			$("#agentFlag"+index).val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#agentID"+index).val(agent.split("@")[0]);
			$("#agentName"+index).val(agent.split("@")[1]);
			});
			
			$(".text_down ul").empty();
	}
	function delRow(index){
		
		$("tr[id='"+index+"']").remove();
	}
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
       
	function clears(index){
		 if ($("#agent"+index).val()!=$("#agentFlag"+index).val()){
			 $("#agent"+index).val("");
			 $("#agentID"+index).val("");
			 }   
		
	}
	
	
	
</script>
<style type="text/css">
    .inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}

	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
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
         	<form  action="administration/meeting/saveMeetingMinutes"  method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">会议纪要</h3>
        	  <input type="hidden" name="meetingMinutesVO.meetingID" value="${meetingVO.meetingID}">
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">会议主题</label>
			    	<label for="attachment" class="col-sm-3 control-label" style="text-align:left"><span id="model1" >${meetingVO.theme}</span></label>
			  </div>
       	 <br><br>
       	 <button type="button" onclick="add()" style="float: right;" class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
                <tr>
                  <th >会议纪要</th>
                  <th >负责人</th>
                  <th >操作</th>
                </tr>
                </thead>
              <tbody id="demo">
               <tr id=1>
                  <td>                 
                  <input type="text" class="form-control workContent" name="meetingMinutesVO.contents" required="required" style="width:700px; height:40px">
                  </td>   
                  <td>
                  <div class="col-sm-2 inputout">
			    	<span class="input_text1" onclick="f(1)">
					<input type="text" id="agent1" class="form-control " oninput="findStaffByName(1)" required="required" style="width:150px; height:40px" onblur="clears(1)"/>			    	
					<input type="hidden"  id="agentFlag1" value=""/>
			    	<input type="hidden" id="agentID1" name="meetingMinutesVO.ownerIDs" />		    	
			    	</span>
			    	<div class="text_down1 text">
						<ul></ul>
					</div>
			      </div></td>                  
                  <td><a onclick="delRow(1)" href="javascript:void(0)">
                  	<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip"><use xlink:href="#icon-delete"></use></svg>
                  </a></td>	
                </tr>

              </tbody>
            </table>
 
			   <div class="form-group">
			    <label for="uploadFile" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span>添加文件</label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="upload"  style="padding:6px 0px;" multiple="multiple">
			    </div>
			  </div>
 			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
      </form>     
        </div>
      </div>
    </div>
  </body>
</html>
