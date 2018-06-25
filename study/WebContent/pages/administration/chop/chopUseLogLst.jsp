<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
	
</style>
<style>
.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
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
      	<s:set name="panel" value="'chopUseLog'"></s:set>
      	<s:set name="selectedPanel" value="'chopUseLog'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/chop/toChopUseLogLst" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">印章使用登记查询</h3> 
			   <div class="form-group" >
			  	<div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker()" placeholder="结束时间" >
			    </div>
			    <div class="col-sm-2">	
			    	<input class="form-control" id="user" autoComplete="off" name="userName" value="${userName}" placeholder="姓名">
			    	<input type="hidden" name="userId"  value="${userId}"/>
			    </div>
			    <div class="col-sm-2">
			    	<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
				</div>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:2%">序号</th>
                  <th class="col-sm-1">使用日期</th>
                  <th class="col-sm-1">部门</th>
                  <th class="col-sm-1">姓名</th>
                  <th class="col-sm-1">公章</th>
                  <th class="col-sm-1">法人章</th> 
                  <th class="col-sm-1">合同章</th> 
                  <th class="col-sm-1">财务专用章</th> 
                  <th class="col-sm-1">发票专用章</th> 
                  <th class="col-sm-1">营业执照正本</th>
                  <th class="col-sm-1">营业执照副本</th>
                  <th class="col-sm-1">开户许可证</th>
                  <th class="col-sm-1">法人身份证复印件</th>
                </tr>
              </thead>
              <tbody id="tbody_">
              	<c:if test="${not empty chopUstLogLst}">
              	<c:forEach items="${chopUstLogLst}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex}</td>
              		<td>${item.useDate }</td>
              		<td>${item.department }</td>
              		<td>${item.userName }</td>
              		<td>
              			<c:if test="${(item.chopType=='1' || item.chopType=='null') && item.chopName!='null'}">
              				${item.chopName}
              			</c:if>
              		</td>
              		<td>
              		    <c:if test="${item.chopType=='3'}">
              				${item.chopName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.chopType=='2'}">
              				${item.chopName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.chopType=='4'}">
              				${item.chopName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.chopType=='5'}">
              				${item.chopName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.certificateType=='营业执照正本'}">
              				${item.certificateName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.certificateType=='营业执照副本'}">
              				${item.certificateName}
              			</c:if>
              		</td>
              		<td>
              			<c:if test="${item.certificateType=='开户许可证'}">
              				${item.certificateName}
              			</c:if>
              		</td>
              		<td>${item.useIdReason}</td>
              		</tr>
              	</c:forEach>
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
    <script src="/assets/js/underscore-min.js"></script>
    <script>
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
    			this_.lastQueryName=value_arr[1];
    			this_.$wapper.find("ul").parent().hide();
    			if(this_.fn_afterChoose){
    				this_.fn_afterChoose();
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
    $(function (){
   	var init=+function (){
    		var afterSelect1=function (){
   			$('input[name="userId"]').val($('#user').closest("div").find("input[name='id']").val());
   		}
   		new staffInputBind().render("#user",afterSelect1);
   	}();
     $("#user").blur(function(){
    	if($("#user").val()==''){
    		$("input[name='userId']").val('');
    	}
    });
    })
    </script>
  </body>
</html>
