<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>

<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
 <script src="/assets/js/layer/layer.js"></script>
 <script src="/assets/js/underscore-min.js"></script>
 <script src="/assets/js/dic.js"></script>
 <script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	var staffInputBind=(function ($,_){
	 	var staffInputBind=function(id){
	 		this.id=id||_.uniqueId("staffInput");
			this.$elem=null;
			this.selector=null;
			this.$wapper=null;
			this.lastQueryName=null;
	 	}
	 	staffInputBind.prototype.render=function(selector,afterChoose){
	 		this.$elem=$(selector);
	 		this.selector=selector;
	 		this.fn_afterChoose=afterChoose;
	 		this.$elem.attr("data-id",this.id);
	 		this.$elem.wrap("<span class='input_text1'></span>");
	 		this.$elem.parent().after("<div class='text_down1  inputout1'><ul></ul></div>");
	 		this.$elem.parent().after("<input name=\"name\" hidden></input><input name=\"id\" hidden></input><input name=\"detail\" hidden></input>")
	 		this.$wapper=this.$elem.closest("div");
	 		this.$elem.keyup(this.textChange.bind(this));
	 		return  this;
	 	}
	 	staffInputBind.prototype.hide=function(){
	 		this.$wapper.find("ul").parent().hide();
	 	}
	 	staffInputBind.prototype.textChange=function(){
	 		var value=this.$elem.val();
	 		if(!value)return;
	 		if(this.lastQueryName){
	 			if(this.lastQueryName===value){
	 				return;
	 			}else{
	 				this.lastQueryName=value;
	 				this.query.call(this,value);
	 			}
	 		}else{
	 			this.lastQueryName=value;
				this.query.call(this,value);
	 		}
	 	}
	 	staffInputBind.prototype.queryCallback=function(data){
	 		if(!_.isArray(data.staffVOs))return;
	 		data.staffVOs=[].slice.call(data.staffVOs,0,100);
	 		var resultHtml=_.chain(data.staffVOs).map(function(staff){
	    		var  groupDetail = staff.groupDetailVOs[0];
	 			return "<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>";
	 		}).join("").value();
	 		if(!resultHtml)return;
	 		var $ul=this.$wapper.find("ul");
	 		$ul.empty().append(resultHtml);
	 		$ul.parent().show();
	 		this.choose.call(this,$ul);
	 	};
	 	staffInputBind.prototype.choose=function($ul){
	 		var this_=this;
	 		$ul.find("li").bind("click",function(){
	 			var value_arr=$(this).find("input").val().split("@");
	 			this_.$wapper.find("input[name='id']").val(value_arr[0])
	 			this_.$wapper.find("input[name='name']").val(value_arr[1]);
	 			var index;
	 			if(~(index=$(this).html().indexOf("<input"))){
	 				var extraMsg=$(this).html().substring(0,index);
	 				this_.$wapper.find("input[name='detail']").val(extraMsg)		
	 			}
	 		 	this_.$elem.val(value_arr[1]);
	 		 	this_.lastQueryId=value_arr[0];
	 			this_.lastQueryName=value_arr[1];
	 			this_.$wapper.find("ul").parent().hide();
	 			if(this_.fn_afterChoose){
	 				this_.fn_afterChoose.call(this_);
	     		}
	 		})
	 		
	 	};
	 	staffInputBind.prototype.query=function(value){
	 		var this_=this;
	 		$.ajax({
	 			url:'personal/findStaffByName',
	 			type:'post',
	 			data:{name:value},
	 			dataType:'json',
	 			success:function(data){
	 				this_.queryCallback.call(this_,data);
	 			}
	 		});
	 	};
	 	return staffInputBind;
	 })(jQuery,_)
	$(function() {	
		
		$('#reternencePerson').change(function (){
			$('input[name="reimbursementVO.reternenceMobile"]').val("");
		})
			new staffInputBind().render("#reternencePerson",function (){
				var id=this.lastQueryId;
				if(id==$('#requestUserID').val()){
					layer.alert("申请人和证明人不能相同！",{offset:'100px'})
					$("#reternencePerson").val("");
					return;
				}
				$('input[name="reimbursementVO.reternenceId"]').val(this.lastQueryId);
				$('input[name="reimbursementVO.reternenceName"]').val(this.lastQueryName);
				$.ajax({
					url:'getStaffById',
					data:{id:id},
					success:function(data){
						var mobile;
						if(data&&(mobile=data.staffVo.telephone)){
							$('#div_mobile').find("input").val(mobile);
						}
					}
				})
			});

		$('select[name="reimbursementVO.isFixedAsset"]').change(function(){
			if($(this).val()=="1"){
				$('#div_fixedNo').show().find("input").attr("required","required");
			}else{
				$('#div_fixedNo').hide().find("input").removeAttr("required");
			}
		})
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			    event.stopPropagation();
			}
		}); 
		$('body').on('click','#lingkuan_div .text_down ul li',function () {
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
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#payee").val()!=$("#payeeFlag").val()) {$("#payee").val("");}if(!$('input[name="reimbursementVO.reternenceId"]').val()){$("#reternencePerson").val("")}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		
		$('body').on('click','.input_text1',function (event) { 
			if($(".text_down1 ul").html() != ""){
				$(".text_down1").show();
				event.stopPropagation();
			}
		}); 
		$('body').on('click','#baoxiao_div .text_down1 ul li',function () {
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
		
		new dicHelper(dicContent.moneyType,'').render($('#moneyTypeDiv'),'form-control',function (){
			this.$item.prop("name","reimbursementVO.moneyType");
			this.$item.change(function(){
				var html=$(this).find("option:selected").html();
				if(html=="人民币"){
					html="元"
				}
				html="合计金额（"+html+"）";
				$("#changeTitle").html(html);
			})
			this.$item.trigger("change");
		});
		new dicHelper(dicContent.invoiceTitle,'').render($('#invoiceTitleDiv'),'form-control',function (){
			this.$item.prop("name","reimbursementVO.invoiceTitle").prop("required","required").prop("id","title");
		});
		$("[data-toggle='tooltip']").tooltip();
	});

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	function check() {
		if(!$('input[name="reimbursementVO.reternenceId"]').val()){
			layer.alert("请填写证明人!",{offset:'100px'});
			return false;
		}
		if(!checkError()){
			layer.alert("银行卡号不符合规范!",{offset:'100px'});
			return false;
		}
		//填充 fileDetai
		var indexNumber=0;
		var fileCount=0;
		var result=_.reduce($('input[name="file"]'),function(arr,$input){
			//附件不能为空
			$.each($($input).prop("files"),function(k,v){
				var name=v['name'];
				var index=name.lastIndexOf("\\");
	            var fileName=name.substring(index+1,name.length)
				var suffix=fileName.replace(/.*\.(.*)/,"$1");
		        suffix=suffix.toLowerCase();
		        arr.push([fileName,suffix,indexNumber]);
		        fileCount++;
			});
			indexNumber++;
		    return arr;
		},[]);
		if(fileCount>8){
			layer.alert("最多上传8张图片!",{offset:'100px'});
		}
 	    $('input[name="fileDetail"]').val(JSON.stringify(result))
		if ($(".amount").length <= 0) {
			showAlert("用途不能为空！");
			return false;
		}
		$("#submitButton").addClass("disabled");
		Load.Base.LoadingPic.FullScreenShow(null);
		return true;
	}

	var fileTypes=['png','jpg','jpeg','pdf'];

	function checkFileType(this_){
		var name=$(this_).val();
		if(name!=null&&name!=''){
			var index=name.lastIndexOf("\\");
            var fileName=name.substring(index+1,name.length)
			var suffix=fileName.replace(/.*\.(.*)/,"$1");
	        suffix=suffix.toLowerCase();
	        if(!_.find(fileTypes,function(s){
	        	return s==suffix
	        })){
	        	layer.alert("附件类型只能是:png,jpg,jpeg,pdf!",{offset:'100px'});
	        	$(this_).val("");
	        }
		}
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
		var count = Number($("#usageCount").val());
		if (count >= 4) {
			showAlert("单个报销单最多填写4条用途！");
			return;
		}
		var html = "<tr>"
      		+"<td class=\"col-sm-6\"><div><input type=\"text\" class=\"form-control\" name=\"reimbursementVO.usage\" required=\"required\"/></div></td>"
      		+"<td class=\"col-sm-2\"><div><input onblur=\"blurfn(this)\" type=\"text\" class=\"form-control amount\" name=\"reimbursementVO.amount\" required=\"required\" oninput=\"calcTotalAmount(this)\"/></div></td>"
      		+"<td class=\"col-sm-2\"><div><input type=\"file\" name=\"file\"   multiple onchange=\"checkFileType(this)\" required=\"required\" /></div></td>"
      		+"<td class=\"col-sm-2\" style=\"text-align:center\"><a href=\"javascript:void(0);\" onclick=\"deleteUsage(this)\"><svg class=\"icon\" aria-hidden=\"true\" title=\"删除\" data-toggle=\"tooltip\">"
			+"<use xlink:href=\"#icon-delete\"></use>"
     		+"</svg></a></td>"
      		+"</tr>";
      	$("#usageDetail").append(html);
      	$("#usageCount").val(count+1);
      	$("[data-toggle='tooltip']").tooltip();
	}
	
	function deleteUsage(obj) {
		var trObj = $(obj).parent().parent();
		var total = Number($("#totalAmount").val()) - Number($(trObj).find(".amount").val());
		$(trObj).remove();
		$("#totalAmount").val(total);
		$("#totalAmountText").text(total);
		var count = Number($("#usageCount").val());
		$("#usageCount").val(count-1);
	}
	function blurfn(this_){
		$(this_).val(Number($(this_).val()).toFixed(2));
		calcTotalAmount(this_);
	}
	function calcTotalAmount(obj) {
		var number=$(obj).val();
		if(number&&isNaN(number)){
			number=+((number+"").substring(0,(number+"").length-1));
		}
		if(number>1000000000){
			number=number/10;
		}
		var index_;
		number+="";
		
		$(obj).val((number+"").replace(/[^0-9+\.]+/,''));
		var total = 0;
		$(".amount").each(function(i, obj) {
			total += Number($(obj).val());
		});
		$("#totalAmount").val(Number(total));
		$("#totalAmountText").text(Number(total));
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
  					+"<div class=\"col-sm-5\"><input type=\"text\" class=\"form-control\" id=\"cardNumber\" name=\"reimbursementVO.cardNumber\" onkeyup=\"(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)\" onblur=\"checkBankAccount(),this.v()\" required=\"required\"/></div>"
  					+"<div id=\"errorMsg\" class=\"col-sm-1\" style=\"display:none;margin-top:5px\"><span style=\"color:red\">不符合规范</span></div>"
  					+"</div></div>";
  		$(divObj).append(html);
  		$(divObj).find("a").text("撤销");
		$(divObj).find("a").attr("onclick", "resetAccount(this)");
	}
	
	function resetAccount(obj) {
		$(".change").remove();
		var html = "<input type=\"hidden\" name=\"reimbursementVO.cardName\" value=\""+$("#payeeCardName").val()+"\" />"
	    		   +"<input type=\"hidden\"  name=\"reimbursementVO.bank\" value=\""+$("#payeeBank").val()+"\" />"
	    		   +"<input type=\"hidden\" name=\"reimbursementVO.cardNumber\" value=\""+$("#payeeCardNumber").val()+"\" />";
	    var divObj = $(obj).parent().parent().parent();
	    $(divObj).append(html);
	    $(divObj).find("a").text("修改");
	    $(divObj).find("a").attr("onclick", "changeAccount(this)");
	}
  	function checkBankAccount(){
  		var accountNumber = $("#cardNumber").val();
  		var reg = new RegExp(/^[0-9]{10,30}$/);
  		var result = reg.test(accountNumber);
  		if(!result){
  			$("#errorMsg").css("display","block");
  		}else{
  			$("#errorMsg").css("display","none");
  		}
  	}
  	function checkError(){
  		var display = $("#errorMsg").css("display");
  		if(display=="block"){
  			return false;
  		}
  		return true;
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
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus,a:visited{text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'reimbursement'"></s:set>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="finance/process/save_startReimbursement" method="post" class="form-horizontal" onsubmit="return check()" enctype="multipart/form-data" >
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
			  	<label for="baoxiao" class="col-sm-1 control-label">报销人<span style="color:red"> *</span></label>
			  	<div class="col-sm-11"><span class="detail-control">${userName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser(this, 1)">修改</a></span></div>
			  </div>
			  <div class="form-group" id="lingkuan_div">
			  	<label for="lingkuan" class="col-sm-1 control-label">领款人<span style="color:red"> *</span></label>
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
			  		<label for="cardName" class="col-sm-1 control-label">户主姓名<span style="color:red"> *</span></label>
			  		<div class="col-sm-2"><input type="text" class="form-control" id="cardName" name="reimbursementVO.cardName" required="required"/></div>
			  	</div>
			  	<div class="form-group">
			  		<label for="bank" class="col-sm-1 control-label">开户行<span style="color:red"> *</span></label>
			  		<div class="col-sm-5"><input type="text" class="form-control" id="bank" name="reimbursementVO.bank" required="required"/></div>
			  	</div>
			  	<div class="form-group">
			  		<label for="cardNumber" class="col-sm-1 control-label">银行卡卡号<span style="color:red"> *</span></label>
			  		<div class="col-sm-5"><input type="text" class="form-control" id="cardNumber" name="reimbursementVO.cardNumber"
			  		onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="checkBankAccount(),this.v()" required="required"/></div>
			  		<div id="errorMsg" class="col-sm-1" style="display:none;margin-top:5px"><span style="color:red">不符合规范</span></div>
			  	</div>
			  </c:if>
			  </div>
			   <div class="form-group">
			  	<label for="detailNum" class="col-sm-1 control-label">固定资产或耗材</label>
			  	<div class="col-sm-2">
			  		<select name="reimbursementVO.isFixedAsset" class="form-control">
			  			<option value="0">否</option>
			  			<option value="1">是</option>
			  		</select>
			  	</div>
			  	<div  id="div_fixedNo" style="display:none">
			  		<label for="detailNum" class="col-sm-1 control-label">固定资产编号</label>
			  		<div class="col-sm-2"><input type="text" class="form-control"  name="reimbursementVO.fixedAssetNo" /></div>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label for="detailNum" class="col-sm-1 control-label">证明人<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input id="reternencePerson" class="form-control" required="required" autoComplete="off" ></input>
			  	</div>
			  	<input style="display:none" name="reimbursementVO.reternenceId"/>
			  	<input style="display:none" name="reimbursementVO.reternenceName"/>
			  	<div  id="div_fixedNo" >
			  		<label for="detailNum" class="col-sm-1 control-label">证明人手机号</label>
			  		<div class="col-sm-2" id="div_mobile"><input name="reimbursementVO.reternenceMobile" class="form-control" readonly="readonly"></input></div>
			  	</div>
			  	</div>
			  <div class="form-group">
			  	<label for="title" class="col-sm-1 control-label">发票抬头<span style="color:red"> *</span></label>
			  	<div class="col-sm-3" id="invoiceTitleDiv">
<%-- 			    	<select class="form-control" id="title" name="reimbursementVO.invoiceTitle" required="required"> --%>
<!-- 				      <option value="">请选择</option> -->
<!-- 					  <option value="1_南通智造链科技有限公司">南通智造链科技有限公司</option> -->
<!-- 					  <option value="2_南通江凌织造有限公司">南通江凌织造有限公司</option> -->
<!-- 					  <option value="3_南通好多衣纺织品有限公司">南通好多衣纺织品有限公司</option> -->
<!-- 					  <option value="4_南通智造链贸易有限公司">南通智造链贸易有限公司</option> -->
<!-- 					  <option value="5_南通迷丝茉服饰有限公司">南通迷丝茉服饰有限公司</option> -->
<!-- 					  <option value="6_广州亦酷亦雅电子商务有限公司">广州亦酷亦雅电子商务有限公司</option> -->
<!-- 					  <option value="7_南京智造链信息科技有限公司">南京智造链信息科技有限公司</option> -->
<!-- 					  <option value="8_南通亦酷亦雅电子商务有限公司">南通亦酷亦雅电子商务有限公司</option> -->
<%-- 					</select> --%>
			    </div>
			  	<label for="invoiceNum" class="col-sm-1 control-label">发票（张）<span style="color:red"> *</span></label>
			  	<div class="col-sm-2"><input type="number"   min="1"  class="form-control" id="invoiceNum" name="reimbursementVO.invoiceNum" required="required"/></div>
			  	<label for="detailNum" class="col-sm-1 control-label" style="width:10%">明细单（张）<span style="color:red"> *</span></label>
			  	<div class="col-sm-2"><input type="number"  min="1" class="form-control" id="detailNum" name="reimbursementVO.detailNum" required="required"/></div>
			  </div>
			  <div class="form-group">
			 	<label for="invoiceNum" class="col-sm-1 control-label">币种</label>
			  	<div class="col-sm-2" id="moneyTypeDiv"></div>
			 	<label for="totalAmount" class="col-sm-2 control-label"  id="changeTitle" >合计金额（元）</label>
			  	<div class="col-sm-2"><span class="detail-control" id="totalAmountText" style="padding:6px 0px"></span><input type="hidden" id="totalAmount" name="reimbursementVO.totalAmount"/></div>
			  </div>
			  
			  <div class="form-group">
<%-- 			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label> --%>
<!-- 			    <div class="col-sm-5"> -->
<!-- 			    	<input type="file" id="attachment" name="file"  multiple style="padding:6px 0px;" onchange="javascript:changeAttachemnt(this)"> -->
			    	<input style="display:none" name="fileDetail"   />
<!-- 			    </div> -->
			  
			  <input type="hidden" id="usageCount" value="1" />
			  <div class="btn btn-primary" onclick="addUsage()" style="float:right">增加</div>
			  <table id="usageDetail" class="table table-striped">
	              <thead>
	                <tr>
	                  <th style="text-align:center">用途<span style="color:red"> *</span></th>
	                  <th style="text-align:center">金额<span style="color:red"> *</span></th>
	                  <th style="text-align:center">附件<span style="color:red"> *</span></th>
	                  <th style="text-align:center">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<tr>
	              		<td class="col-sm-6"><div><input type="text" class="form-control" name="reimbursementVO.usage" required="required"/></div></td>
	              		<td class="col-sm-2"><div><input type="text"  onblur="blurfn(this)" class="form-control amount" name="reimbursementVO.amount" required="required" oninput="calcTotalAmount(this)" /></div></td>
	              		<td class="col-sm-2"><div><input type="file" name="file"  onchange="checkFileType(this)" required="required" multiple /></div></td>
	              		<td class="col-sm-2" style="text-align:center">
	              		<a href="javascript:void(0);" onclick="deleteUsage(this)">
	              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
	             				<use xlink:href="#icon-delete"></use>
	             			</svg>
	              		</a>
	              		</td>
	              	</tr>
	              </tbody>
              </table>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>