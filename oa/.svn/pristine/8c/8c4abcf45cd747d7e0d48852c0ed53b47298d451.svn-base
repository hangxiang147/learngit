<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
	//checkbox单击事件
	$(".check").on("click",function(){
	    fullCkOrNot();
	});
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
});
require(['staffComplete'],function (staffComplete){
	new staffComplete().render($('#purchaserName'),function ($item){
		$("input[name='purchaserId']").val($item.data("userId"));
	});
});
function checkEmpty(){
	if($("#purchaserName").val()==''){
		$("input[name='purchaserId']").val('');
	}
}
$(document).click(function (event) {
	if ($("input[name='purchaserId']").val()=='')
	{
		$("#purchaserName").val("");
	}
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
function matchPartnerOption(){
	var matchPartnerIds = [];
	$('.check:checked').each(function(){
		matchPartnerIds.push($(this).val());
	});
	if(matchPartnerIds.length==0){
		layer.alert("请先勾选待配比的期权", {offset:'100px'});
		return;
	}else{
		layer.alert("<form class='form-horizontal'><div class='form-group'><label class='col-sm-5' style='width:35%'>期权比例</label><div class='col-sm-6'>"+
				"<select id='ratio' class='form-control' rows='4'><option value='2'>1 : 2</option><option value='4'>1 : 4</option><option value='8'>1 : 8</option></select></div></div><form>",{offset:'100px',title:'配比期权',btn:['提交']},function(index){
			var ratio = $("#ratio option:selected").val();
			var search = location.search;
			if(search){
				location.href="administration/partner/matchPartnerOption"+location.search+"&matchPartnerIds="+matchPartnerIds+"&ratio="+ratio;
			}else{
				location.href="administration/partner/matchPartnerOption?matchPartnerIds="+matchPartnerIds+"&ratio="+ratio;
			}
			layer.close(index);
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	}
}
</script>
<style type="text/css">
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
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">待配比期权列表</h3> 
        	<form action="administration/partner/partnerOptionList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1">认购时间</label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" id="beginDate" name="purchaseBeginDate" value="${purchaseBeginDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
        		</div>
        		<div class="col-sm-1 control-label" style="text-align:center">至</div>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" id="endDate" name="purchaseEndDate" value="${purchaseEndDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })"/>
        		</div>
        	</div>
        	<div class="form-group">
        		<label class="control-label col-sm-1">认购人员</label>
        		<div class="col-sm-2">
        			<input type="text" id="purchaserName" class="form-control" autoComplete="off" name="purchaserName" value="${purchaserName}" onblur="checkEmpty()"/>
        			<input type="hidden" value="${purchaserId}" name="purchaserId">
        		</div>
        		<label class="control-label col-sm-1">认购方式</label>
        		<div class="col-sm-2">
        		<select class="form-control" name="purchaseType">
       	  	  		<option value="">全部</option>
       	  	  		<option ${type=='薪资认购'?'selected':'' } value="薪资认购">薪资认购</option>
       	  	  		<option ${type=='公司奖励'?'selected':'' } value="公司奖励">公司奖励</option>
       	  	  	</select>
       	  	  	</div>
       	  	  	<label class="control-label col-sm-1">状态</label>
        		<div class="col-sm-2">
        		<select class="form-control" name="status">
       	  	  		<option value="">全部</option>
       	  	  		<option ${status=='0'?'selected':'' } value="0">待配比</option>
       	  	  		<option ${status=='1'?'selected':'' } value="1">已配比</option>
       	  	  	</select>
       	  	  	</div>
        	<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        	</form>
        	<button class="btn btn-primary" onclick="matchPartnerOption()">配比</button>
        	<div class="sub-header"></div>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:5%"><input class="all checkboxClass" type="checkbox" style="margin-top:0"></td>
	              		<td>认购时间</td>
	              		<td>认购人员</td>
	              		<td>认购金额（元）</td>
	              		<td>期权配比（元）</td>
	              		<td>认购方式</td>
	              		<td>状态</td>
	              	</tr>
	              </thead>
	              <tbody>
	              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					<c:forEach items="${partnerOptionList}" var="partnerOption" varStatus="status">
						<tr>
						<td>
						<c:if test="${partnerOption[6]=='0'}">
						<input name="partnerOptionId" value="${partnerOption[0]}" class="checkboxClass check" type="checkbox" style="margin-top:0">
						</c:if>
						</td>
						<td><fmt:formatDate value="${partnerOption[1]}" pattern="yyyy-MM-dd" /></td>
						<td>${partnerOption[2]}</td>
						<td>${partnerOption[3]==null?'——':partnerOption[3]}</td>
						<td>${partnerOption[4]==null?'——':partnerOption[4]}</td>
						<td>${partnerOption[5]}</td>
						<td>
							<c:if test="${partnerOption[6]=='0'}"><span style="color:#13d213">待配比</span></c:if>
							<c:if test="${partnerOption[6]=='1'}"><span style="color:red">已配比</span></c:if>
						</td>
						</tr>
					</c:forEach>
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
</body>
</html>