<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
	.error {
		color: red;
	}
	</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startShopPayApply" method="post" id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起${applyType}申请</h3>
  			  	  <input style="display:none" name="applyType"  value="${applyType}"/> 
  			  <c:if test="${applyType=='付费服务/插件开通'}">
  			  <input style="display:none" name="shopPayPlugin.userName"  value="${staff.lastName}"/>
  			  <input style="display:none" name="shopPayPlugin.userID"  value="${staff.userID}"/>
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">店铺名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" required name="shopPayPlugin.shopName" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">服务/应用名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" required name="shopPayPlugin.serviceName" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">插件/服务作用<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" required name="shopPayPlugin.serviceUse" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">付款账号</label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" placeholder="店铺登录账号" class="form-control" name="shopPayPlugin.payAccount" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">开通时长<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="number" autoComplete="off" class="form-control" required name="shopPayPlugin.openTime" min="1" max="36" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">月</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">付费金额<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="text" autoComplete="off" class="form-control" onblur="checkNum(this)" required name="shopPayPlugin.payMoney" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">元</div>
			  </div>
			  </c:if>
			<c:if test="${applyType=='付费推广充值'}">
			<input style="display:none" name="spreadShopApply.userName"  value="${staff.lastName}"/>
  			<input style="display:none" name="spreadShopApply.userID"  value="${staff.userID}"/>
			<button type="button" onclick="add()" style="float: right;" class="btn btn-primary">增加</button>
      		<br>
      		<br>
	        <table class="table">
              <thead>
                <tr>
                  <th>店铺<span style="color:red"> *</span></th>
                  <th style="width:15%">负责人</th>
                  <th>登陆账号</th>
                  <th>推广类型<span style="color:red"> *</span></th>
                  <th style="width:15%">平均每日花费<span style="color:red"> *</span></th>
                  <th style="width:15%">预算充值金额<span style="color:red"> *</span></th>
                  <th style="width:15%">当前余额</th>
                  <th style="width:5%">操作</th>
                </tr>
                </thead>
	              <tbody id="spreadShop">
	               	  <tr id="spread1">
	                  	<td>
	                  		<input name="spreadShopVo.shopName" autocomplete="off" required class="form-control"/>
	                  	</td>
	                  	<td>
	                  		<input type="text" class="leader form-control" name="spreadShopVo.leader" autocomplete="off"/>
	                  	</td>
	                  	<td>
	                  		<input name="spreadShopVo.loginAccount" autocomplete="off" class="form-control"/>
	                  	</td>
	                  	<td>
	                  		<select class="form-control" required name="spreadShopVo.spreadType">
	                  		  <option value="">请选择</option>
	                  		  <option value="淘宝直通车">淘宝直通车</option>
	      					  <option value="钻石展位">钻石展位</option>
	      					  <option value="品销宝">品销宝</option>
	                  		</select>
	                  	</td>
	                  	<td>
                  			<input name="spreadShopVo.costPerDay" autocomplete="off" required onblur="checkNum(this)" class="form-control" style="width:80%;display:inline-block"
                  			onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"/> 元
	                  	</td>
	                  	<td>
                  			<input name="spreadShopVo.rechargeAmount" autocomplete="off" required onblur="checkNum(this)" class="form-control" style="width:80%;display:inline-block"
                  			onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"/> 元
	                  	</td>
	                  	<td>
	                  		<input name="spreadShopVo.currentBalance" autocomplete="off" class="form-control" style="width:80%;display:inline-block"
	                  	    onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"/> 元
	                  	</td>
	                  	<td></td>
	                  </tr>
	              </tbody>
	            </table>
			   </c:if>
			   <c:if test="${applyType=='其他服务费'}">
			    <input style="display:none" name="otherPay.userName"  value="${staff.lastName}"/>
  			  	<input style="display:none" name="otherPay.userID"  value="${staff.userID}"/>
			   	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">项目名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" required name="otherPay.projectName" maxLength="50">
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">项目作用<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea required maxLength="200" rows="3" class="form-control" name="otherPay.projectUse"></textarea>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">项目明细<span style="font-size:5px">（限200字）</span></label>
			  	<div class="col-sm-5">
			  		<textarea maxLength="200" rows="3" class="form-control" name="otherPay.description"></textarea>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">项目花费<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="text" autoComplete="off" class="form-control" onblur="checkNum(this)" required name="otherPay.projectPay" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">元</div>
			  	</div>
			   </c:if>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:5%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/layer/layer.js"></script>
      <script src="/assets/js/require/require2.js"></script>
      <script type="text/javascript">
    	require(['staffComplete'],function (staffComplete){
    		$(".leader").each(function(){
    			new staffComplete().render($(this),function ($item){
        		});
    		});
    	});
    	var index = 1;
    	function add(){
    		index++;
    		$("#spreadShop").append(
	               	  	'<tr id="spread'+index+'">'+
	                  	'<td>'+
	                  	'<input name="spreadShopVo.shopName" autocomplete="off" required class="form-control"/>'+
	                  	'</td>'+
	                  	'<td>'+
	                  		'<input type="text" class="leader form-control" name="spreadShopVo.leader" autocomplete="off"/>'+
	                  	'</td>'+
	                  	'<td>'+
	                  		'<input name="spreadShopVo.loginAccount" autocomplete="off" class="form-control"/>'+
	                  	'</td>'+
	                  	'<td>'+
	                  		'<select class="form-control" required name="spreadShopVo.spreadType">'+
	                  		  '<option value="">请选择</option>'+
	                  		  '<option value="淘宝直通车">淘宝直通车</option>'+
	      					  '<option value="钻石展位">钻石展位</option>'+
	      					  '<option value="品销宝">品销宝</option>'+
	                  		'</select>'+
	                  	'</td>'+
	                  	'<td>'+
                			'<input name="spreadShopVo.costPerDay" autocomplete="off" required class="form-control" style="width:80%;display:inline-block"'+
                			'onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,\'\');}).call(this)" onblur="this.v();"/> 元'+
	                  	'</td>'+
	                 	'<td>'+
            			'<input name="spreadShopVo.rechargeAmount" autocomplete="off" required class="form-control" style="width:80%;display:inline-block"'+
            			'onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,\'\');}).call(this)" onblur="this.v();"/> 元'+
                  		'</td>'+
	                  	'<td>'+
	                  		'<input name="spreadShopVo.currentBalance" autocomplete="off" class="form-control" style="width:80%;display:inline-block"'+
	                  	    'onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,\'\');}).call(this)" onblur="this.v();"/> 元'+
	                  	'</td>'+
	                  	'<td><a href="javascript:void(0)" onclick="del('+index+')">删除</a></td>'+
	                  '</tr>'
    				);
        	require(['staffComplete'],function (staffComplete){
        		$(".leader").each(function(){
        			new staffComplete().render($(this),function ($item){
            		});
        		});
        	});
    	}
    	function del(index){
    		$("#spread"+index).remove();
    	}
    	function checkNum(target){
    		if($(target).val()!='' && parseFloat($(target).val())<=0){
    			layer.alert("必须大于0",{"offset":"100px"});
    			$(target).val("");
    		}
    	}
      </script>
      </body>
</html>