<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
function showAlert(message) {
	var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
			+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
			+"<span id=\"danger-message\">"+message+"</span>"
			+"</div>";
	$(".container-fluid").before(html);
};
</script>
<style type="text/css">
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
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">工作汇报<span style="color:red;font-size:20px;">（工作任务请如实汇报，杜绝弄虚作假，违者重罚）</span></h3>
       	  
       	  <form id="postWorkReport" >
       	 <input type="hidden" id="requestUserID" name="workReportVO.userID" value="${staff.userID}"/>
       	 <input type="hidden"  id="reportDate" name="workReportVO.reportDate"  value="${date}"/>
       	 <input type="hidden"  id="reportDate" name="workReportVO.weekDay"  value="${weekDay}"/>
   	    
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">汇报人</label>
			  	<div class="col-sm-1"><span class="detail-control">${staff.lastName }</span></div>
			  </div><br><br>
       	<div class="form-group" id="beginDate_div">
	    <label for="beginDate" class="col-sm-1 control-label">汇报时间</label>
	    
	        <div class="col-sm-1" style="padding-right:0px;"><span class="detail-control">${date}</span></div>
	        <div class="col-sm-1"><span class="detail-control">${weekDay}</span></div>
    	 
	    </div> 
       	 <br><br>
       	 <button type="button" id="addBtn" style="float: right;" class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
                <tr>
                  <th >工作内容<span style="color:red;">（本页为提交页面，汇报工作无效，<span style="color:red; font-size:24px;">截屏无效</span>）</span></th>
                  <th >数量</th>
                  <th >下达任务人</th>
                  <th >完成情况</th>
                  <th >完成用时</th>
                  <th >操作</th>
                </tr>
                </thead>
              <tbody id="content">
             

              </tbody>
            </table>
          <button type="button" style="float:right;" onclick="javascript:submitWorkReport()" class="btn btn-primary" >提交</button>
           <span id="sumNumberSpan"  style="float:right;margin-right:50px"></span>
      </form>     
        </div>
      </div>
    </div>
	<script src="/assets/js/require/require2.js"></script>
	<script type="text/html" id="demoTr">
	  <tr >
                  <td width="500px">                 
                  <input type="text" class="form-control workContent" required name="workReportVO.workContent" style="width:500px">
                  </td>   
                  <td>                 
                  <input type="number" min="0" class="form-control quantities" required  name="workReportVO.quantities" style="width:100px">
                  </td>   
                  <td width="250px">
               
					<input type="text" name="chooseInput" class="form-control assignTaskUserID" style="width:150px" onkeyup="checkEmpty(this)"/>			    	
			    	<input type="hidden"  name="workReportVO.assignTaskUserID" />		    	
	
			     </td>                  
                  <td>
				  <input type="text" maxlength="4" class="form-control completeState" name="workReportVO.completeState" style="width:100px;" ></td>   
				  <td>              
                  <input type="number" min="0" class="form-control workHours" name="workReportVO.workHours" style="width:100px;"  ></td>
                  <td><a ><svg class="icon" aria-hidden="true">
					<use xlink:href="#icon-delete"></use>
					</svg></a></td>	
                </tr>

	</script>
	<script>
		require(['staffComplete','jquery'],function (staffComplete,$){
			var demoTr=$('#demoTr').html();
			$('#addBtn').click(function (){
				var $item=$(demoTr);
				$('#content').append($item)
				$item.find("a:last").click(function (){
					$item.remove();
				});
				new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
					$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
				});
				$item.find("input[name=\"workReportVO.workHours\"]").change(function (){
					var sumNumber=0;
					$('input[name="workReportVO.workHours"]').each(function (){
						if($(this).val())
							sumNumber+=Number($(this).val());
					});
					$('#sumNumberSpan').html("总用时:"+sumNumber+"小时")
				})
			});
			$('#addBtn').trigger("click")
		})
		$(document).click(function (event) {
			$("input[name='workReportVO.assignTaskUserID']").each(function(){
				if($(this).val()==''){
					$(this).prev().val("");
				}
			});
		}); 
	  	function checkEmpty(target){
	  		if($(target).val()==''){
	  			$(target).next().val('');
	  		}
	  	}
		function submitWorkReport() {
		
		var flag = 0;
		$(".workContent").each(function(i, obj) {
			if ($(obj).val().trim() == '') {
				flag = 1;
			}
		});
		if(flag== 1){
			showAlert("工作内容不能为空！");
			return;
		}
		$(".completeState").each(function(i, obj) {
			if ($(obj).val().trim() == '') {
				flag = 1;
			}
		});
		if(flag== 1){
			showAlert("完成情况不能为空！");
			return;
		}
		$(".workHours").each(function(i, obj) {
			if ($(obj).val().trim() == '') {
				flag = 1;
			}
		});
		if(flag== 1){
			showAlert("完成用时不能为空！");
			return;
		}
		$(".assignTaskUserID").each(function(i, obj) {
			if ($(obj).val().trim() == '') {
				flag = 1;
			}
		});
		if(flag== 1){
			showAlert("下达任务人不能为空！");
			return;
		}
		var reportDate = $("#reportDate").val();
		layer.confirm("提交后将不能被修改，您是否确定提交！", {offset:'100px'}, function(index){
			layer.close(index);
			$("#submitButton").attr("disabled", "disabled");
			var formData = new FormData($("#postWorkReport")[0]);
			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'personal/savaWorkReport',
				type:'post',
				data:formData,
				contentType:false,
				processData:false,
				success:function(data) {
					if (data=="errorMessage") {
						layer.alert("提交失败", {offset:'100px'});
						$("#submitButton").removeAttr("disabled");
						return;
					}
					if(data=="error"){
						layer.alert("您今天已经汇报过了不能重复汇报", {offset:'100px'});
					}else{
						layer.alert("录入成功！", {offset:'100px'}, function(index){
							layer.close(index);
							window.location.href = "personal/workReportOver?reportDate="+reportDate;
							Load.Base.LoadingPic.FullScreenShow(null);
						});
					}
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
				}
			})
		});
	}

	</script>
  </body>
</html>
