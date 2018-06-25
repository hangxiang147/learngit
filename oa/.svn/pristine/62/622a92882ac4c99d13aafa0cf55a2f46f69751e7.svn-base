<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	$(function() {		
		
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			    event.stopPropagation();
			}
		}); 
		$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#payee").val(shtml.split("（")[0]);
			$("#payeeFlag").val(shtml.split("（")[0]);
			var payee = $(this).find("input").val();
			$("#payeeID").val(payee.split("@")[0]);
			$("#payeeName").val(payee.split("@")[1]);
			$.ajax({
				url:'personal/getBankAccountByUserID',
				type:'post',
				data:{userID: $("#payeeID").val()},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						showAlert(data.errorMessage);
						return;
					}
					if (data.bankAccountVO == null) {
						return;
					}
					$("#account_div").remove();
					var bankAccount = data.bankAccountVO;
					var cardName = bankAccount.cardName;
					var bank = bankAccount.bank;
					var cardNumber = bankAccount.cardNumber;
					var html = "<div id=\"account_div\">"
								+"<div class=\"form-group\">"
				  				+"<label for=\"account\" class=\"col-sm-1 control-label\">打款账号</label>"
				    			+"<div class=\"col-sm-11\"><span id=\"cardNameText\" class=\"detail-control\">"+cardName+"&nbsp;&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"changeAccount(this)\">修改</a></span></div>"
				    			+"<div class=\"col-sm-1\"></div>"
				    			+"<div class=\"col-sm-11\"><span id=\"bankText\" class=\"detail-control\">"+bank+"</span></div>"
				    			+"<div class=\"col-sm-1\"></div>"
				    			+"<div class=\"col-sm-11\"><span id=\"cardNumberText\" class=\"detail-control\">"+cardNumber+"</span></div>"
				    			+"<input type=\"hidden\" name=\"reimbursementVO.cardName\" value=\""+cardName+"\" />"
				    			+"<input type=\"hidden\" name=\"reimbursementVO.bank\" value=\""+bank+"\" />"
				    			+"<input type=\"hidden\" name=\"reimbursementVO.cardNumber\" value=\""+cardNumber+"\" />"
				    			+"</div></div>";
				    $("#lingkuan_div").after(html);
				    $("#payeeCardName").val(cardName);
				    $("#payeeBank").val(bank);
				    $("#payeeCardNumber").val(cardNumber);
				}
			});
		});
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#payee").val()!=$("#payeeFlag").val()) {$("#payee").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		
		$('body').on('click','.input_text1',function (event) { 
			if($(".text_down1 ul").html() != ""){
				$(".text_down1").show();
				event.stopPropagation();
			}
		}); 
		$('body').on('click','.text_down1 ul li',function () {
			var shtml=$(this).html();
			$(".text_down1").hide();
			$("#request").val(shtml.split("（")[0]);
			$("#requestFlag").val(shtml.split("（")[0]);
			var request = $(this).find("input").val();
			$("#requestUserID").val(request.split("@")[0]);
			$("#requestUserName").val(request.split("@")[1]);
		});
		$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#request").val()!=$("#requestFlag").val()) {$("#request").val("");}});  
		$('.inputout1').click(function (event) {$(".text_down1").show();});
	});

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function check() {
		$("#submitButton").addClass("disabled");
		return true;
	}
	
	
	function changeUser(obj, type) {
		var html = "<div class=\"col-sm-1\"></div>";
		if (type == 1) {
			html += "<div class=\"col-sm-2 inputout1\">"
   				+"<span class=\"input_text1\">"
   				+"<input type=\"text\" id=\"request\" class=\"form-control\" required=\"required\" oninput=\"findStaffByName1()\"  />"
   				+"<input type=\"hidden\" id=\"requestFlag\" value=\"\"/>"
   				+"</span>"
   				+"<div class=\"text_down1\">"
   				+"<ul></ul>"
   				+"</div>" 
   				+"</div>";
		} else {
			html += "<div class=\"col-sm-2 inputout\">"
   				+"<span class=\"input_text\">"
   				+"<input type=\"text\" id=\"payee\" class=\"form-control\" required=\"required\" oninput=\"findStaffByName()\"  />"
   				+"<input type=\"hidden\" id=\"payeeFlag\" value=\"\"/>"
   				+"</span>"
   				+"<div class=\"text_down\">"
   				+"<ul></ul>"
   				+"</div>"
   				+"</div>";
		}
   		var divObj = $(obj).parent().parent().parent();
		$(divObj).append(html);
		
		$(divObj).find("a").text("撤销");
		$(divObj).find("a").attr("onclick", "deleteUser(this, "+type+")");
	}
	
	function deleteUser(obj, type) {
		var divObj = $(obj).parent().parent().parent();
		$(divObj).find("div").each(function(i, obj) {
			if (i != 0) {
				$(obj).remove();
			}
		});
		$(divObj).find("a").text("修改");
		$(divObj).find("a").attr("onclick", "changeUser(this, "+type+")");
		
		if (type == 1) {
			$("#requestUserID").val($("#userID").val());
			$("#requestUserName").val($("#userName").val());
		} else {
			$("#payeeID").val($("#userID").val());
			$("#payeeName").val($("#userName").val());
			$("#account_div").remove();
			var cardName = $("#userCardName").val();
			var bank = $("#userBank").val();
			var cardNumber = $("#userCardNumber").val();
			$("#payeeCardName").val(cardName);
			$("#payeeBank").val(bank);
			$("#payeeCardNumber").val(cardNumber);
			var html = "<div id=\"account_div\">"
						+"<div class=\"form-group\">"
		  				+"<label for=\"account\" class=\"col-sm-1 control-label\">打款账号</label>"
		    			+"<div class=\"col-sm-11\"><span id=\"cardNameText\" class=\"detail-control\">"+cardName+"&nbsp;&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"changeAccount(this)\">修改</a></span></div>"
		    			+"<div class=\"col-sm-1\"></div>"
		    			+"<div class=\"col-sm-11\"><span id=\"bankText\" class=\"detail-control\">"+bank+"</span></div>"
		    			+"<div class=\"col-sm-1\"></div>"
		    			+"<div class=\"col-sm-11\"><span id=\"cardNumberText\" class=\"detail-control\">"+cardNumber+"</span></div>"
		    			+"<input type=\"hidden\" name=\"reimbursementVO.cardName\" value=\""+cardName+"\" />"
		    			+"<input type=\"hidden\" name=\"reimbursementVO.bank\" value=\""+bank+"\" />"
		    			+"<input type=\"hidden\" name=\"reimbursementVO.cardNumber\" value=\""+cardNumber+"\" />"
		    			+"</div></div>";
		    $("#lingkuan_div").after(html);
		}
	}
	
	function findStaffByName() {
		var name = $("#payee").val();
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
		var name = $("#request").val();
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
	
	function addUsage() {
		var html = "<tr>"
      		+"<td class=\"col-sm-8\"><div><input type=\"text\" class=\"form-control\" name=\"reimbursementVO.usage\" required=\"required\"/></div></td>"
      		+"<td class=\"col-sm-2\"><div><input type=\"text\" class=\"form-control amount\" name=\"reimbursementVO.amount\" required=\"required\" oninput=\"calcTotalAmount()\"/></div></td>"
      		+"<td class=\"col-sm-2\" style=\"text-align:center\"><a href=\"javascrip:void(0);\" onclick=\"addUsage()\">添加</a>|<a href=\"javascript:void(0);\" onclick=\"deleteUsage(this)\">删除</a></td>"
      		+"</tr>";
      	$("#usageDetail").append(html);
	}
	
	function deleteUsage(obj) {
		var trObj = $(obj).parent().parent();
		var total = Number($("#totalAmount").val()) - Number($(trObj).find(".amount").val());
		$(trObj).remove();
		$("#totalAmount").val(total);
		$("#totalAmountText").text(total);
	}
	
	function calcTotalAmount() {
		var total = 0;
		$(".amount").each(function(i, obj) {
			total += Number($(obj).val());
		});
		$("#totalAmount").val(total);
		$("#totalAmountText").text(total);
	}
	
	function changeAccount(obj) {
		var divObj = $(obj).parent().parent().parent();
		$(divObj).find("input").remove();
		var html = "<div class=\"col-sm-1 change\"></div>"
					+"<div class=\"col-sm-11 change\">"
					+"<div class=\"form-group\">"
  					+"<label for=\"cardName\" class=\"col-sm-1 control-label\">户主姓名</label>"
  					+"<div class=\"col-sm-2\"><input type=\"text\" class=\"form-control\" id=\"cardName\" name=\"reimbursementVO.cardName\" required=\"required\"/></div>"
  					+"</div>"
  					+"<div class=\"form-group\">"
  					+"<label for=\"bank\" class=\"col-sm-1 control-label\">开户行</label>"
  					+"<div class=\"col-sm-5\"><input type=\"text\" class=\"form-control\" id=\"bank\" name=\"reimbursementVO.bank\" required=\"required\"/></div>"
  					+"</div>"
  					+"<div class=\"form-group\">"
  					+"<label for=\"cardNumber\" class=\"col-sm-1 control-label\">银行卡卡号</label>"
  					+"<div class=\"col-sm-5\"><input type=\"text\" class=\"form-control\" id=\"cardNumber\" name=\"reimbursementVO.cardNumber\" required=\"required\"/></div>"
  					+"</div></div>";
  		$(divObj).append(html);
  		$(divObj).find("a").text("撤销");
		$(divObj).find("a").attr("onclick", "resetAccount(this)");
	}
	
	function resetAccount(obj) {
		$(".change").remove();
		var html = "<input type=\"hidden\" name=\"reimbursementVO.cardName\" value=\""+$("#payeeCardName").val()+"\" />"
	    		   +"<input type=\"hidden\" name=\"reimbursementVO.bank\" value=\""+$("#payeeBank").val()+"\" />"
	    		   +"<input type=\"hidden\" name=\"reimbursementVO.cardNumber\" value=\""+$("#payeeCardNumber").val()+"\" />";
	    var divObj = $(obj).parent().parent().parent();
	    $(divObj).append(html);
	    $(divObj).find("a").text("修改");
	    $(divObj).find("a").attr("onclick", "changeAccount(this)");
	}
</script>
<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 15px;
	    margin-top: 15px;
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startReimbursement" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">报销申请</h3> 
        	  <!-- 填写报销申请的操作人 -->
			  <input type="hidden" id="userID" name="reimbursementVO.userID" value="${user.id}"/>
			  <input type="hidden" id="userName" name="reimbursementVO.userName" value="${userName }" />
			  <!-- 报销人 -->
			  <input type="hidden" id="requestUserID" name="reimbursementVO.requestUserID" value="${user.id }"/>
   			  <input type="hidden" id="requestUserName" name="reimbursementVO.requestUserName" value="${userName }"/>
   			  <input type="hidden" id="payeeID" name="reimbursementVO.payeeID" value="${user.id }"/>
   			  <input type="hidden" id="payeeName" name="reimbursementVO.payeeName" value="${userName }" />
   			  <input type="hidden" id="payeeCardName" value="${reimbursementVO.cardName }" />
   			  <input type="hidden" id="payeeBank" value="${reimbursementVO.bank }" />
   			  <input type="hidden" id="payeeCardNumber" value="${reimbursementVO.cardNumber }" />
   			  <input type="hidden" id="userCardName" value="${reimbursementVO.cardName }" />
   			  <input type="hidden" id="userBank" value="${reimbursementVO.bank }" />
   			  <input type="hidden" id="userCardNumber" value="${reimbursementVO.cardNumber }" />
			  <div class="form-group" id="baoxiao_div">
			  	<label for="baoxiao" class="col-sm-1 control-label">报销人</label>
			  	<div class="col-sm-11"><span class="detail-control">${userName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser(this, 1)">修改</a></span></div>
			  </div>
			  <div class="form-group" id="lingkuan_div">
			  	<label for="lingkuan" class="col-sm-1 control-label">领款人</label>
			  	<div class="col-sm-11"><span class="detail-control">${userName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser(this, 2)">修改</a></span></div>
			  </div>
			  <div id="account_div">
			  <c:if test="${not empty reimbursementVO.cardName }">
			  <div class="form-group">
			  	<label for="account" class="col-sm-1 control-label">打款账号</label>
			    <div class="col-sm-11"><span id="cardNameText" class="detail-control">${reimbursementVO.cardName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeAccount(this)">修改</a></span></div>
			    <div class="col-sm-1"></div>
			    <div class="col-sm-11"><span id="bankText" class="detail-control">${reimbursementVO.bank }</span></div>
			    <div class="col-sm-1"></div>
			    <div class="col-sm-11"><span id="cardNumberText" class="detail-control">${reimbursementVO.cardNumber }</span></div>
			    <input type="hidden" name="reimbursementVO.cardName" value="${reimbursementVO.cardName }" />
			    <input type="hidden" name="reimbursementVO.bank" value="${reimbursementVO.bank }" />
			    <input type="hidden" name="reimbursementVO.cardNumber" value="${reimbursementVO.cardNumber }" />
			  </div>
			  </c:if>
			  <c:if test="${empty reimbursementVO.cardName }">
			  	<div class="form-group">
			  		<label for="cardName" class="col-sm-1 control-label">户主姓名</label>
			  		<div class="col-sm-2"><input type="text" class="form-control" id="cardName" name="reimbursementVO.cardName" required="required"/></div>
			  	</div>
			  	<div class="form-group">
			  		<label for="bank" class="col-sm-1 control-label">开户行</label>
			  		<div class="col-sm-5"><input type="text" class="form-control" id="bank" name="reimbursementVO.bank" required="required"/></div>
			  	</div>
			  	<div class="form-group">
			  		<label for="cardNumber" class="col-sm-1 control-label">银行卡卡号</label>
			  		<div class="col-sm-5"><input type="text" class="form-control" id="cardNumber" name="reimbursementVO.cardNumber" required="required"/></div>
			  	</div>
			  </c:if>
			  </div>
			  <div class="form-group">
			  	<label for="invoiceNum" class="col-sm-1 control-label">发票（张）</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="invoiceNum" name="reimbursementVO.invoiceNum" required="required"/></div>
			  	<label for="detailNum" class="col-sm-1 control-label">明细单（张）</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="detailNum" name="reimbursementVO.detailNum" required="required"/></div>
			 	<label for="totalAmount" class="col-sm-2 control-label">合计金额（元）</label>
			  	<div class="col-sm-2"><span class="detail-control" id="totalAmountText" style="padding:6px 0px"></span><input type="hidden" id="totalAmount" name="reimbursementVO.totalAmount"/></div>
			  </div>
			  <table id="usageDetail" class="table table-striped">
	              <thead>
	                <tr>
	                  <th style="text-align:center">用途</th>
	                  <th style="text-align:center">金额</th>
	                  <th style="text-align:center">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<tr>
	              		<td class="col-sm-8"><div><input type="text" class="form-control" name="reimbursementVO.usage" required="required"/></div></td>
	              		<td class="col-sm-2"><div><input type="text" class="form-control amount" name="reimbursementVO.amount" required="required" oninput="calcTotalAmount()" /></div></td>
	              		<td class="col-sm-2" style="text-align:center"><a href="javascrip:void(0);" onclick="addUsage()">添加</a>|<a href="javascript:void(0);" onclick="deleteUsage(this)">删除</a></td>
	              	</tr>
	              </tbody>
              </table>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>