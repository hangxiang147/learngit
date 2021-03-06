<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
$(function() {
		$('body').on('click','.input_text1',function (event) { 
			if($(".text_down1 ul").html() != ""){
			$(".text_down1").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down1 ul li',function () {
			var shtml=$(this).html();
			$(".text_down1").hide();
			$("#recipient").val(shtml.split("（")[0]);
			$("#recipientFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#recipientID").val(agent.split("@")[0]);
			$("#recipientName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#recipient").val()!=$("#recipientFlag").val()) {$("#recipient").val("");}});  
		$('.inputout1').click(function (event) {$(".text_down1").show();});
     });
	function findStaffByName1() {
		var name = $("#recipient").val();
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
	
	
	
</script>
<style type="text/css">
    #div1{
    right:200px;
    }
	 
	
    .col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	table{width:100%;border:0px solid #999;}
    table td{word-break: keep-all;white-space:nowrap;}
    table th{word-break: keep-all;white-space:nowrap;}
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
        <s:set name="panel" value="'asset'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="asset/updateUsage" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">退库</h3> 
        	   <div class="form-group">
	           <label for="serialNumber" class="col-sm-2 control-label">退库人&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2 inputout1">
			    	<span class="input_text1">
			    	<input type="text" id="recipient" class="form-control" required="required" oninput="findStaffByName1()"  />
			    	<input type="hidden" id="recipientFlag" value=""/>
			    	<input type="hidden" id="recipientID" name="assetUsageVO.returnOperatorID" />
			    	<input type="hidden" id="recipientName" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down1">
						<ul></ul>
					</div>
			    </div>
	           <label for="reason" class="col-sm-2 control-label">退库时间&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control reportDate"  id="reportDate" name="assetUsageVO.returnTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			    </div>
	         </div>
	         <div class="form-group">
	          <label for="reason" class="col-sm-2 control-label">地址</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" name="assetUsageVO.returnLocation"  />
			    </div>
	          <label for="reason" class="col-sm-2 control-label">备注</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" name="assetUsageVO.returnNote"  />
			    </div>
	         </div>
	        
	          
	          
	           <div class="form-group">
	           <div class="col-sm-9" style="text-align:right">    
	           <table class="table table-striped" id="test_table">
              <thead>
                <tr>
                  <th class="col-sm-1" >使用人</th>
                  <th class="col-sm-1" >资产条码</th>
                  <th class="col-sm-1" >资产名称</th>
                  <th class="col-sm-1" >资产类别</th>
                  <th class="col-sm-1" >所属公司</th>
                </tr>
              </thead>
              <tbody>
              <c:if test="${not empty assetUsageVOs}">
              	<s:iterator id="assetUsage" value="#request.assetUsageVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#assetUsage.recipientName"/><input type="hidden" name="assetUsageVO.usageIDs" value="<s:property value='#assetUsage.usageID'/>" /></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.assetVO.serialNumber"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.assetVO.assetName"/></td>
              			<td class="col-sm-1"><s:if test="#assetUsage.assetVO.type==1">行政办公设备</s:if><s:if test="#assetUsage.assetVO.type==2">电子产品</s:if><s:if test="#assetUsage.assetVO.type==3">通信设备</s:if><s:if test="#assetUsage.assetVO.type==4">交通工具</s:if></td>
              			<td class="col-sm-1"><s:if test="#assetUsage.assetVO.companyID==1">智造链骑岸总部</s:if><s:if test="#assetUsage.assetVO.companyID==2">智造链如东迷丝茉分部</s:if><s:if test="#assetUsage.assetVO.companyID==3">智造链南通分部</s:if><s:if test="#assetUsage.assetVO.companyID==4">智造链广州亦酷亦雅分部</s:if><s:if test="#assetUsage.assetVO.companyID==5">智造链南京分部</s:if><s:if test="#assetUsage.assetVO.companyID==6">智造链佛山迷丝茉分部</s:if></td>
              		</tr>
              	</s:iterator>
              	</c:if>
              </tbody>
            </table>
	          </div>
	          </div>
			  <div class="form-group" >
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>