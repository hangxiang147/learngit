<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
</script>
<style type="text/css">
	._title{background:#efefef;}
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;
	font-size:14px;height:auto;line-height:20px;padding:10px 10px;}
	.tab table tr .title {text-align:center;color:#000;width:15%;background:#efefef;}
	#spread>tbody>tr:nth-of-type(odd) {
    background-color: #fff;
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
      	<s:set name="panel" value="'audit'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">申请列表</h3>
       	  <div style="text-align:right"><button type="button" class="btn btn-primary" onclick="audit()">审批</button></div>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th><input type="checkbox" class="all"></th>
                  <th>序号</th>
                  <th>标题</th>
                  <th>发起人</th>
                  <th>发起时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty shopPayVos}">
              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="shopPayVo" value="#request.shopPayVos" status="count">
              		<input type="hidden" value="${shopPayVo.id}@@${shopPayVo.applyType}">
              		<tr>
              			<td style="width:2%"><input type="checkbox" class="check"></td>
              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
              			<td class="col-sm-3"><s:property value="#shopPayVo.userName"/>的<s:property value="#shopPayVo.applyType"/>申请</td>
              			<td class="col-sm-2"><s:property value="#shopPayVo.userName"/></td>
              			<td class="col-sm-3"><s:property value="#shopPayVo.applyDate"/></td>
              			<td class="col-sm-1">
              			<a onclick="showShopPayApplyDetail('${shopPayVo.id}', '${shopPayVo.applyType}')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
							<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
              			</a>
              			</td>
              		</tr>
              		<s:set name="task_id" value="#task_id+1"></s:set>
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
    <div id="applyDetail" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">申请详情</h4>
			</div>
			<div class="modal-body">
				<div id="content"></div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
		function showShopPayApplyDetail(id, applyType){
     		$.ajax({
    			url:'/administration/process/showShopPayApplyDetail',
    			type:'post',
    			data:{'id':id,'applyType':applyType},
    			dataType:'json',
    			success:function (data){
    				$("#content").html("");
    				var content = "";
    				if('付费推广充值'==applyType){
    					content = '<table id="spread" class="table table-striped"><tr><td class="_title">店铺</td>'+
        	  						  '<td class="_title">负责人</td>'+
        	  						  '<td class="_title">登陆账号</td>'+
        	  			              '<td class="_title">推广类型</td>'+
        	  			              '<td class="_title">平均每日花费</td>'+
        	  			              '<td class="_title">当前余额</td>'+
        	  			              '<td class="_title">预算充值金额</td>'+
        	  			              '<td class="_title">店铺每日总花费</td>'+
        	  		                  '</tr>';
    					data.spreadShops.forEach(function(value, index){
    						content +=  '<tr>'+
            	  			'<td>'+value.shopName+'</td>'+
            	  			'<td>'+(value.leader=="null"?"":value.leader)+'</td>'+
            	  			'<td>'+(value.loginAccount=="null"?"":value.loginAccount)+'</td>'+
            	  			'<td>'+value.spreadType+'</td>'+
            	  			'<td>'+value.costPerDay+'元'+'</td>'+
            	  			'<td>'+(value.currentBalance=="null"?"":value.currentBalance+'元')+'</td>'+
            	  			'<td>'+value.rechargeAmount+'元'+'</td>'+
            	  			'<td>'+value.totalCost+'元'+'</td></tr>';
    					});
    					content += "</table>";
    				}else if('付费服务/插件开通'==applyType){
    					
    					var shopPayPlugin = data.shopPayPlugin;
    					content = '<div class="tab"><table><tr>'+
    	  				'<td class="title">店铺名称</td>'+
    	  				'<td>'+shopPayPlugin.shopName+'</td>'+
    	  				'<td class="title">服务/应用名称</td>'+
    	  				'<td>'+shopPayPlugin.serviceName+'</td>'+
    	  				'<td class="title">插件/服务作用</td>'+
    	  				'<td>'+shopPayPlugin.serviceUse+'</td>'+
    	  			'</tr>'+
    	  			'<tr>'+
    	  				'<td class="title">开通时长</td>'+
    	  				'<td>'+shopPayPlugin.openTime+'个月'+'</td>'+
    	  				'<td class="title">付费金额</td>'+
    	  				'<td>'+shopPayPlugin.payMoney+'元'+'</td>'+
    	  				'<td class="title">付款账号</td>'+
    	  				'<td>'+shopPayPlugin.payAccount+'</td></tr></table></div>';
    				}else{
    					var shopOtherPay = data.shopOtherPay;
    					content = '<div class="tab"><table><tr>'+
    	  				'<td class="title">项目名称</td>'+
    	  				'<td>'+shopOtherPay.projectName+'</td>'+
    	  				'<td class="title">项目作用</td>'+
    	  				'<td>'+shopOtherPay.projectUse+'</td>'+
    	  			'</tr>'+
	    	  			'<tr>'+
		  				'<td class="title">项目明细</td>'+
		  				'<td>'+shopOtherPay.description+'</td>'+
		  				'<td class="title">项目花费</td>'+
		  				'<td>'+shopOtherPay.projectPay+'元'+'</td></tr></table></div>';
    				}
    				$("#content").append(content);
    				$("#applyDetail").modal('show');
    			}
    		});
		}
	//checkbox单击事件
     $(".check").on("click",function(){
         fullCkOrNot();
     });
     //全选checkbox响应其他checkbox的选中事件
     var fullCkOrNot = function(){
         var allCB = $(".all");
         if($(".check:checked").length == $(".check").length){
             allCB.prop("checked",true);
         }else{
        	 allCB.prop("checked",false);
     	}
     }
     //全选checkbox点击事件
     $(".all").on("click",function(){
    	 if($(".all").prop("checked")){
    		 $(".check").each(function(){
    			 $(this).prop("checked",true);
    		 });
    	 }else{
    		 $(".check").each(function(){
    			 $(this).prop("checked",false);
    		 });
    	 }
     });
     function audit(){
    	 var idAndApplyTypes = [];
    	 $(".check").each(function(){
    		 if($(this).prop("checked")){
    			 var idAndApplyType = $(this).parent().parent().prev().val();
    			 idAndApplyTypes.push(idAndApplyType);
    		 }
    	 });
    	 if(idAndApplyTypes.length<1){
    		 layer.alert("请勾选后再审批",{"offset":"100px"});
    		 return;
    	 }
 		layer.alert('',{ 
 			id: 'audit',
 			title:'审批',
			offset:'100px',
			content:'<form class="form-horizontal"><div class="form-group"><label class="col-sm-4 control-label">充值周期：</label><div class="col-sm-8"><input type="text" autocomplete="off" class="form-control" id="beginDate"'+
   			 		'placeholder="开始时间"/></div></div>'+
   			 		'<div class="form-group">'+
	    			'<label class="col-sm-4 control-label">至</label>'+
	    			'<div class="col-sm-8">'+
	    			'<input type="text" autocomplete="off" class="form-control" id="endDate" placeholder="结束时间"/></div></div></form>',
            btn: ['同意', '不同意'],  
            yes: function() {  
  	    		window.location.href="/administration/process/auditShopApply?idAndApplyTypes="+idAndApplyTypes+
  	    				"&auditResult=1&beginDate="+$("#beginDate").val()+"&endDate="+$("#endDate").val(); 
  	    		Load.Base.LoadingPic.FullScreenShow(null);
            },
            btn2: function() {  
	    		window.location.href="/administration/process/auditShopApply?idAndApplyTypes="+idAndApplyTypes+"&auditResult=2";
	    		Load.Base.LoadingPic.FullScreenShow(null);
        	}
        }); 
 		 $("#audit").next().next().css("text-align","center");
 		 $("#beginDate").attr('onclick',"WdatePicker({ dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\\'endDate\\')}'})");
 		 $("#endDate").attr('onclick',"WdatePicker({ dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\\'beginDate\\')}'})");
     }
	</script>
  </body>
</html>
