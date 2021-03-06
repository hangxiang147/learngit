<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<script type="text/javascript">
	function taskComplete(result) {
		var businessType = "预约付款";
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var selectedPanel = $("#selectedPanel").val();
		<c:choose>
			<c:when test="${taskDefKey eq 'financialFirstAudit' }">
				window.location.href = "finance/process/reComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+"&businessType="+businessType; 
				Load.Base.LoadingPic.FullScreenShow(null);
				</c:when>
			<c:when test="${taskDefKey  eq 'remitMoney' }">
				window.location.href = "finance/process/reComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+"&businessType="+businessType+"&type_=2"; 
				Load.Base.LoadingPic.FullScreenShow(null);
				</c:when>
			<c:when test="${taskDefKey  eq 'auditInvoice' }">
				window.location.href = "finance/process/auditInvoice?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel;
				Load.Base.LoadingPic.FullScreenShow(null);
				</c:when>
			<c:otherwise>
				window.location.href = "finance/process/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+"&businessType="+businessType; 
				Load.Base.LoadingPic.FullScreenShow(null);
				</c:otherwise>
		</c:choose>
		

	}
	
	$(function (){
		var testNumber=$('#sumNumber').html();
		var number_arr_=["零","壹","贰","叁","肆","伍","陆","柒","捌","玖"];
		var unit_arr1=["拾","佰","仟"];
		var unit_arr2=["万","亿","兆"];
		var index;
		if(~(index=testNumber.indexOf("."))){
		    //只取到分
		    if(index+3<testNumber.length){
		        testNumber=testNumber.substring(0,index+3);
		    }else{
		        var num=2-(testNumber.length-1-index);
		        //需要填充的 num的数量
		        for(var i=0;i<num;i++){
		            testNumber+="0";
		        }
		    }
		}else{
		    testNumber=testNumber+".00";
		}
		var prev=testNumber.substring(0,testNumber.length-3);
		var tail=testNumber.substring(testNumber.length-2);
		var getTailHtml=function(){
			return "<span>"+number_arr_[tail[0]]+"</span>　角　" + "<span>"+number_arr_[tail[1]]+"</span>　分　";
		}
		var number_arr=[];
		var getPrevHtml=function(){
			for(var i=prev.length;i>=0;i-=4){
				var startIndex=(i-4>=0)?i-4:0;
				var endIndex=i;
				number_arr.push(prev.substring(startIndex,endIndex));
				if(startIndex==0)break;
			}
		}
		getPrevHtml();
		var resultHtml="";
		for(var i=0;i<number_arr.length;i++){
			if(i==0){
				var number_str=number_arr[i];
				for(var j=number_str.length-1,k=0;j>=0;j--,k++){
					var number_=number_str.charAt(j);
					if(k==0){
						resultHtml="<span>"+number_arr_[number_]+"</span>　元　"+resultHtml;				
					}else{
						resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr1[k-1]+"　"+resultHtml;				
					}
				}
			}else{
				var number_str=number_arr[i];
				for(var j=number_str.length-1,k=0;j>=0;j--,k++){
					var number_=number_str.charAt(j);
					if(k==0){
						resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr2[i-1]+"　"+resultHtml;						
					}else{
						resultHtml="<span>"+number_arr_[number_]+unit_arr1[k-1]+"</span>"+resultHtml;
						
					}
				}
			}
		}
		$('#fillInTd').empty().html("金额大写："+resultHtml+getTailHtml());
	})
	var print_=function (){
		var content=$('#printArea').html();
		var oldContent=$('body').html();
		$('body').empty().html(content);
		window.print();
		window.location.reload();
	}
	var search=location.search;
	//假如参数为空  这种情况 不可能发生 除了 从 print 页跳转回来
	if(!search){
		location.href=localStorage.lastUrl;
	}
	localStorage.lastUrl=location.href;
</script>
<style type="text/css">
	.col_blue{color:#428bca;}
	.Cost{margin:0px 20px;}
	.Costlm{line-height:40px;height:40px;text-align:center;color:#428bca;font-size:30px;}
	.Costlm span{border-bottom:4px double #555555;line-height:50px;display:inline-block;padding:0px 20px;}
	.unit{height:40px;line-height:40px;margin-top:20px;}
	.unit span{display:inline-block;font-size:18px;color:#428bca;}
	.unit .fl{float:left;width:33.33%;}
	.unit .fr{float:right;width:33.33%;text-align:right;}
	.Cost_tab{color:#555555;}
	.Cost_tab table{width:100%;border-collapse:collapse;font-size:16px;}
	.Cost_tab table tr td{border:1px solid #555555;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
	.Cost_tab table tr td.p0{padding:0px;}
	.Cost_tab table tr td .Original_loan{width:50%;float:left;border-right:1px solid #555555;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;display:inline-block;height:50px;line-height:50px;padding-left:10px;color:#428bca;}
	.Cost_tab table tr td .Original_loan font{color:#111;}
	.Cost_tab table tr td .Back{width:50%;float:left;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;display:inline-block;line-height:50px;padding-left:10px;color:#428bca;}
	.Cost_tab table tr td .Back font{color:#111;}
	.Cost_tab table tr td.ok{font-size:20px;color:#111;}
	.Cost_tab table tr td.money span{color:#111;}
	.Cost_foot{height:40px;line-height:40px;}
	.Cost_foot span{float:left;width:20%;display:inline-block;font-size:16px;color:#428bca;}
	.Cost_foot span em, .unit span em{font-style:normal;color:#111;}
	
	.Cost_flm{color:#428bca;font-size:16px;margin-top:20px;height:auto;}
	.Cost_form{margin-top:10px;height:auto;}
	.Cost_form table{width:100%;border-collapse:collapse;font-size:14px;}
	.Cost_form table tr td{border-bottom:1px solid #dddddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;color:#888;}
	.Cost_form table tr td:nth-child(odd){color:#000;width:140px;text-align:center;}
	
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    height: 34px;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <c:if test="${isFromAll eq '1'}">
            	<s:set name="selectedPanel" value="'findAdvanceList'"></s:set>
      
      	<s:set name="panel" value="'all'"></s:set>
      	</c:if>
      	<c:if test="${isFromAll ne '1'}">
      	<s:set name="panel" value="'audit'"></s:set>
      	</c:if>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;"><c:if test="${taskDefKey == 'financialSecondAudit' }">财务二级审批</c:if>
        	  												 <c:if test="${taskDefKey == 'remitMoney' }">财务打款</c:if>
        	  </h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
			  <input type="hidden" id="selectedPanel" value="${selectedPanel }"/>   
			  <input type="hidden" id="businessKey" value="${businessKey }" /> 
			  
			    <div class="Cost">
			    <div  id="printArea">
				<div class="Costlm"><span>预 付 费 用 申 请 单</span></div>
				<div class="unit"><span class="fl">预付部门：<em>${departmentName }（${companyName }）</em></span><span class="fl">预付单号：<em>${reimbursementVO.reimbursementNo }</em></span><span class="fr">预付日期：<em>${reimbursementVO.requestDate.split(" ")[0] }</em></span></div>
				<div class="Cost_tab">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    <tr>
				      <td width="46%" align="center" class="col_blue">用途</td>
				       <td width="20%" align="center" class="col_blue">金额（<c:choose >
				      <c:when test="${reimbursementVO.moneyType eq null or reimbursementVO.moneyType eq '人民币:1' or  reimbursementVO.moneyType eq ''}">元</c:when>
				      <c:otherwise >
				      <c:set var="moneyType" value="${fn:split(reimbursementVO.moneyType, ':')}" />${moneyType[0]}</c:otherwise>
				      </c:choose>）</td>
					  <td rowspan="3" width="30" align="center" class="col_blue">部<br/>门<br/>领<br/>导<br/>签<br/>批</td>
				      <td rowspan="3" class="ok">${dept_leader_msg }</td>
				    </tr>
					<tr>
				      <td>${reimbursementVO.usage[0] }</td>
				      <td align="center">${reimbursementVO.amount[0] }</td>
				    </tr>
					<tr>
				      <td><s:if test="#request.reimbursementVO.usage.length > 1">${reimbursementVO.usage[1] }</s:if><s:else>&nbsp;</s:else></td>
				      <td align="center"><s:if test="#request.reimbursementVO.usage.length > 1">${reimbursementVO.amount[1] }</s:if><s:else>&nbsp;</s:else></td>
				    </tr>
					<tr>
				      <td><s:if test="#request.reimbursementVO.usage.length > 2">${reimbursementVO.usage[2] }</s:if><s:else>&nbsp;</s:else></td>
				      <td align="center"><s:if test="#request.reimbursementVO.usage.length > 2">${reimbursementVO.amount[2] }</s:if><s:else>&nbsp;</s:else></td>
					  <td rowspan="3" width="30" align="center" class="col_blue">公<br/>司<br/>领<br/>导<br/>审<br/>批</td>
				      <td rowspan="3" class="ok">${company_leader_msg }</td>
				    </tr>
					<tr>
				      <td><s:if test="#request.reimbursementVO.usage.length > 3">${reimbursementVO.usage[3] }</s:if><s:else>&nbsp;</s:else></td>
				      <td align="center"><s:if test="#request.reimbursementVO.usage.length > 3">${reimbursementVO.amount[3] }</s:if><s:else>&nbsp;</s:else></td>
				    </tr>
					<tr>
				      <td align="center" class="col_blue">合计</td>
				      <td align="center" id="sumNumber" >${reimbursementVO.totalAmount }</td>
				    </tr>
					<tr>
				      <td colspan="2" class="money col_blue" id="fillInTd" ></td>
				      <td colspan="2" class="p0"><span class="Original_loan">原借款：<font></font>元</span><span class="Back">退/补：<font></font>元</span></td>
				    </tr>
				</table>
				</div>
				<div class="Cost_foot"><span>会计主管：<c:if test="${taskDefKey ne 'supervisor_audit' and taskDefKey ne 'manager_audit' and taskDefKey ne 'financialFirstAudit'}"><em>${reimbursementVO.showPerson2}</em></c:if></span><span>会计：<em>${reimbursementVO.showPerson1}</em></span><span>出纳：<em>${reimbursementVO.showPerson3}</em></span><span>预付人：<em>${reimbursementVO.requestUserName }</em></span><span>领款人：<em>${reimbursementVO.payeeName }</em></span></div>				
				<div style="text-align:right;">
					<br>
					打款账号户名：${reimbursementVO.cardName }
					<br>
					打款账号开户行：${reimbursementVO.bank }
					<br>
					打款账号：${reimbursementVO.cardNumber }
				</div>
				<h3 class="sub-header" style="margin-top:0px;border-bottom:1px solid #dddddd;"></h3>
				</div>
				<c:if test="${reimbursementVO.isHaveInvoice  eq 1}">
				
				<div class="Cost_flm">单据及附件信息</div>
				<div class="Cost_form">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    <tr>
				      <td>发票抬头</td>
				      <td>${reimbursementVO.invoiceTitle }</td>
				      <td>发票张数</td>
				      <td width="15%">${reimbursementVO.invoiceNum }张</td>
					  <td>明细单张数</td>
				      <td width="15%">${reimbursementVO.detailNum }张</td>
				    </tr>
				</table>
				</div>
				</c:if>
				<div class="Cost_flm">打款账号</div>
				<div class="Cost_form">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
				      <td>户主姓名</td>
				      <td colspan="3" width="100">${reimbursementVO.cardName }</td>
				      <td>开户行</td>
				      <td>${reimbursementVO.bank }</td>
					  <td>银行卡卡号</td>
				      <td width="200">${reimbursementVO.cardNumber }</td>
				    </tr>
				</table>
				</div>
				<div class="Cost_flm">更多信息</div>
				<div class="Cost_form">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
				      <td>是否固定资产</td>
				      <td width="100">${reimbursementVO.isFixedAsset eq 1 ?"是":"否"}</td>
				      <c:if test="${reimbursementVO.isFixedAsset eq 1}">
				      <td>固定资产编号</td>
				      <td colspan="3" width="100">${reimbursementVO.fixedAssetNo }</td>
				     </c:if>
				      
				      <td>证明人</td>
				      <td>${reimbursementVO.reternenceName }</td>
					  <td>证明人手机号</td>
				      <td width="200">${reimbursementVO.reternenceMobile }</td>
				    </tr>
				</table>
				</div>
			      	<c:if test="${attachmentSize > 0 }">
			 		<div class="Cost_flm">附件</div>
				<div class="Cost_form">
				<table>
					<c:forEach items="${attas}" var="item" varStatus="index">
						 <c:if test="${item.type eq 'picture'}">
<a href="javascript:showPic(${index.index })" target="_self"><img style="height:200px;width:200px;margin-left:20px" src="personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}"/>
							</a>
						 </c:if>
						 <c:if test="${item.type ne 'picture' }">
							 <a	href="personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}" >${item.name}</a>
						</c:if>
					</c:forEach>
				</table>
				</div>
			  </c:if> 
			  <c:if test="${not empty invoiceAttas}">
			  	<div class="Cost_flm">发票</div>
				<div class="Cost_form">
				<table>
					<c:forEach items="${invoiceAttas}" var="item" varStatus="index">
						<a href="javascript:showInvoice(${index.index})" target="_self"><img style="height:200px;width:200px;margin-left:20px" src="personal/showImage?attaId=${item.id}"/>
						</a>						
					</c:forEach>
				</table>
				</div>
			  </c:if>  
			  	<div class="Cost_flm">审批意见</div>
				<div class="Cost_form">
				<table width="50%" border="0" cellspacing="0" cellpadding="0">
					<textarea id="comment" class="form-control"></textarea>
				</table> 
				</div>
			  <div class="form-group" id="btnsDiv">
			  	<c:if test="${taskDefKey == 'financialSecondAudit'|| taskDefKey=='financialFirstAudit'}">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:15px;">不同意</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">同意</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'auditInvoice'}">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:15px;">不通过</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">通过</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'remitMoney' }">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(12)" style="margin-left:15px;">打款失败</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(11)" style="margin-left:20px;">已打款</button>
			  		<button class="btn btn-primary" onclick="print_()" style="margin-left:20px;" >打印单据</button>
			  	</c:if>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  </div>
			  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  	<c:if test="${task.assigneeId ne null }">
				  <div class="form-group">
				  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
				  	</div>
				  	<c:if test="${not empty comments }">
	        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  	<c:if test="${comment.taskID == task.taskID and comment.content!=null and comment.content!='' }">
	        	  		<div class="col-sm-2"></div>
					  	<div class="col-sm-10">
					  		<span class="detail-control">${comment.content }</span>
					  	</div>
	        	  	</c:if>
	        	  	</c:forEach>
	        	  	</c:if>
				  </div>
			  </c:if>
			  </c:forEach>
			  </c:if>
			</form>
        </div>
      </div>
    </div>
      <script src="/assets/js/layer/layer.js"></script>
    	<script>
    	var picData={
    		title:"预约付款图片详情",
    		start:0,
    		data:[]
    	}
    	<c:forEach items="${attas}" var="item" varStatus="index">
    	//附件 仅仅有图片 
		 	picData.data.push({alt:"${item.name}",src:"/personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}"})
		</c:forEach>
    	var showPic=function (index){
    		picData.start=index;
    		layer.photos({
    			area: ['1000px'],
    			offset: '50px',
    		    photos: picData
    		    ,anim: 5 
    		  });
    	}
		var imageData={
				title:"发票详情",
	       		start:0,
	       		data:[]
   	    }
    	<c:forEach items="${invoiceAttas}" var="item" varStatus="index">
		 	imageData.data.push({alt:"${item.name}", src:"personal/showImage?attaId=${item.id}"})
		</c:forEach>
    	var showInvoice = function(index){
    		imageData.start=index;
       		layer.photos({
       			offset: '50px',
       		    photos: imageData
       		    ,anim: 5 
       		  });
    	}
    	 <c:if test="${isFromAll eq '1'}">
    	 $('#btnsDiv').find("button").each(function (){
    		 $(this).css("display","none");
    	 })
    	 $('#btnsDiv').find("button:last").css("display","block");
    	 </c:if>
    	</script>
</body>
</html>