<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#agent").val(shtml.split("（")[0]);
			$("#agentFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#agentID").val(agent.split("@")[0]);
			$("#agentName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#agent").val()!=$("#agentFlag").val()) {$("#agent").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
	});
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/express/findSignExpressList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function signExprss(signExpressID){
		$.ajax({
			url:"administration/express/getSignExpressByID",
			type:"post",
	        data:{signExpressID:signExpressID},
		    dataType:"json",
		    success:function(data){
		    	if(data.errorMessage!=null && data.errorMessage.length!=0){
		    		window.location.href = "administration/express/error?panel=express&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	var html="";
	   	      $('#recipientName1').text(data.signExpress.recipientName);
			  $('#recipientDate').text(data.signExpress.receiptDate);
			  var companys=new Array("快递公司","申通" ,"EMS","顺丰","圆通 ","中通 ","韵达 ","汇通 ","全峰 ","德邦","邮政 ","跨越","百世");
			  $('#expressCompany').text(companys[data.signExpress.expressCompany]);
			  $('#expressNumber').text(data.signExpress.expressNumber);
		      $('#cheakModal').modal('show');
		      $('#signExpressID').val(data.signExpress.signExpressID);
		    	
		       } 
	        });
	}
	
	function findStaffByName() {
		var name = $("#agent").val();
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
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid1").html(html);
	}
	function update() {
	
		if ($("#agentID").val().trim() == '') {
			showAlert("领件人不能为空！");			
			return;
		}
		if ($("#signDate_1").val() == '') {
			showAlert("领件时间不能为空！");			
			return;
		}
		$('#cheakModal').modal('hide');
		var formData = new FormData($("#signForm")[0]);
		$.ajax({
			url:'administration/express/updateSignExpress',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
		    dateType:'json',
		    success:function(data) {
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "administration/express/error?panel=express&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	layer.alert("保存成功！", {offset:'100px'}, function(){
		    		window.location.reload();
		    	});
		    }
		});
	}
	
	

	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	.inputout{position:relative;}
	.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
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
      	<s:set name="panel" value="'express'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="sendExpressForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">快递签收明细</h3>
          	<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="recipientName" name="signExpressVO.recipientName" value="${signExpressVO.recipientName }">
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">物流公司</label>
			<div class="col-sm-2">						
			<select class="form-control"  name="signExpressVO.expressCompany" >
		      <option value="">请选择</option>
		      <option value="1" <s:if test="#request.signExpressVO.expressCompany == 1 ">selected="selected"</s:if> >申通</option>
		      <option value="2" <s:if test="#request.signExpressVO.expressCompany == 2 ">selected="selected"</s:if> >EMS</option>
		      <option value="3" <s:if test="#request.signExpressVO.expressCompany == 3 ">selected="selected"</s:if> >顺丰</option>
		      <option value="4" <s:if test="#request.signExpressVO.expressCompany == 4 ">selected="selected"</s:if> >圆通</option>
		      <option value="5" <s:if test="#request.signExpressVO.expressCompany == 5 ">selected="selected"</s:if> >中通</option>
		      <option value="6" <s:if test="#request.signExpressVO.expressCompany == 6 ">selected="selected"</s:if> >韵达</option>
		      <option value="7" <s:if test="#request.signExpressVO.expressCompany == 7 ">selected="selected"</s:if> >汇通</option>
		      <option value="8" <s:if test="#request.signExpressVO.expressCompany == 8 ">selected="selected"</s:if> >全峰</option>
		      <option value="9" <s:if test="#request.signExpressVO.expressCompany == 9 ">selected="selected"</s:if> >德邦</option>
		      <option value="10" <s:if test="#request.signExpressVO.expressCompany == 10 ">selected="selected"</s:if> >邮政</option>
		      <option value="11" <s:if test="#request.signExpressVO.expressCompany == 11 ">selected="selected"</s:if> >跨越</option>
		      <option value="12" <s:if test="#request.signExpressVO.expressCompany == 12 ">selected="selected"</s:if> >百世</option>
			</select>
			</div>
			<label for="beginDate" class="col-sm-1 control-label">状态</label>
				<div class="col-sm-2">						
			    <select class="form-control"  name="signExpressVO.status" >
				      <option value="">请选择</option>
				      <option value="1" <c:if test="${signExpressVO.status==1 }">selected="selected"</c:if>>未领取</option>
				      <option value="2" <c:if test="${signExpressVO.status==2 }">selected="selected"</c:if>>已领取</option>
				</select>
			     </div>
			    
			</div>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="signExpressVO.beginDate" value="${signExpressVO.beginDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="signExpressVO.endDate" value="${signExpressVO.endDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="结束时间">
			    </div>
			</div>
			
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
           <a class="btn btn-primary"  onclick="goPath('administration/express/addSignExpress')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>新增</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>收件人</th>
                  <th>收件日期</th> 
                  <th>物流公司</th>
                  <th>物流单号</th>
                  <th>领件人</th>
                  <th>领件时间</th>
                  <th>领取</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty signExpressVOs}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="signExpress" value="#request.signExpressVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#attendance_id+1"/></td>              			
              			<td class="col-sm-1"><s:property value="#signExpress.recipientName"/></td>              			
              			<td class="col-sm-1"><s:property value="#signExpress.receiptDate"/></td>              			      			
              			<td class="col-sm-1"><s:if test="#signExpress.expressCompany==1">申通</s:if><s:if test="#signExpress.expressCompany==2">EMS</s:if><s:if test="#signExpress.expressCompany==3">顺丰</s:if><s:if test="#signExpress.expressCompany==4">圆通</s:if><s:if test="#signExpress.expressCompany==5">中通</s:if><s:if test="#signExpress.expressCompany==6">韵达</s:if><s:if test="#signExpress.expressCompany==7">汇通</s:if><s:if test="#signExpress.expressCompany==8">全峰</s:if><s:if test="#signExpress.expressCompany==9">德邦</s:if><s:if test="#signExpress.expressCompany==10">邮政</s:if><s:if test="#signExpress.expressCompany==11">跨越</s:if>
              			
              			<s:if test="#signExpress.expressCompany==12">百世</s:if>
              			</td>              			
              			<td class="col-sm-1"><s:property value="#signExpress.expressNumber"/></td>              			
              			<td class="col-sm-1"><s:property value="#signExpress.claimName"/></td>
              			<td class="col-sm-1"><s:property value="#signExpress.claimDate"/></td>           			
              			<td class="col-sm-1">
              			<s:if test="#signExpress.status==1">
              			<a href="javascript:void(0)" onclick="signExprss(<s:property value='#signExpress.signExpressID'/>)">
              			<svg class="icon" aria-hidden="true" title="领取" data-toggle="tooltip">
							<use xlink:href="#icon-banli"></use>
						</svg>
              			</a>
              			</s:if><s:else>已领取</s:else></td>           			
              		</tr>
              		<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
	        <h4 class="modal-title" id="exampleModalLabel">领取</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	        <form id="assetForm1" class="form-horizontal">
	          <input type="hidden" id="groupDetailID" />
	          <div class="form-group">
	           <label for="reason" class="col-sm-3 control-label">收件人:</label>
			  
			    	 <span  id="recipientName1" class="col-sm-3 control-label" style="text-align:left"></span> 
			    
	           <label for="reason" class="col-sm-3 control-label">收件日期:</label>
			    
			    	<span id="recipientDate" class="col-sm-3 control-label" style="text-align:left"></span>

			    </div>
	          <div class="form-group">
	           <label for="reason" class="col-sm-3 control-label">物流单号:</label>
			    <div class="col-sm-3">
			    	<span id="expressNumber" class="col-sm-3 control-label" style="text-align:left"></span>
			    </div>
			     <label for="reason" class="col-sm-3 control-label">物流公司:</label>
			    	<span id="expressCompany" class="col-sm-3 control-label" style="text-align:left"></span>
	          </div>
	        </form>
	         <form id="signForm" class="form-horizontal">
	         <input type="hidden" class="form-control" id="signExpressID" name="signExpressVO.signExpressID">
	        
			   <div class="form-group">
			  	<label for="expressNumber" class="col-sm-3 control-label">领件人</label>
			   <div class="col-sm-6 inputout">
			    	<span class="input_text">
			    	<input type="text" id="agent" class="form-control"  oninput="findStaffByName()"  />
			    	<input type="hidden" id="agentFlag" value=""/>
			    	<input type="hidden" id="agentID" name="signExpressVO.claimID" />
			    	<input type="hidden" id="agentName" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
			  </div>
			   <div class="form-group">
			  	<label for="expressNumber" class="col-sm-3 control-label">领件时间</label>
			    <div class="col-sm-6">
			    	<input type="text" class="form-control" id="signDate_1" name="signExpressVO.claimDate"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
			    </div>
			  </div>
	        </form>
	        
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary"  onclick="update()" id="submitButton">提交</button>
	      <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
	      </div>
	    </div>
	  </div>
	</div>
  </body>
</html>
