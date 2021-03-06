<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
	});
	
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
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
        <s:set name="panel" value="'personalQuery'"></s:set>
        <s:set name="selectedPanel" value="'vitaeQuery'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
		<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'job'">style="display:none;"</s:if>>
			<ul class="nav nav-sidebar">
			  <li class="active"><a href="/administration/process/vitaeQuery">我的面试结果查询</a></li>
			</ul>
		</div> 	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/administration/process/vitaeQuery" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">我的面试结果查询</h3> 
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">应聘者姓名</label>
			  	<div class="col-sm-2" id="joinPerson" ><input type="text" class="form-control" name="name" value="${name}"></div>
			  </div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">姓名</th>
                  <th class="col-sm-1">我的打分</th>
                  <th class="col-sm-1">我的结论</th>
                  <th class="col-sm-1">最终结果</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty resultList}">
              	<c:forEach items="${resultList}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[1] }</td>
              		<td>${item[2] }</td>
              		<td>
              			<c:if test="${item[3] eq 0}">没有来应聘</c:if>
             			<c:if test="${item[3] eq 1}">前来应聘 </c:if>
             			<c:if test="${item[3] eq 2}">应聘通过 </c:if>
             			<c:if test="${item[3] eq 3}">应聘不通过</c:if>
             			<c:if test="${item[3] eq 4}">未入职</c:if>
             			<c:if test="${item[3] eq 5}">已入职</c:if>
             			<c:if test="${item[3] eq 6}">进入下轮复试</c:if>
              		</td>
              		<td>${item[4]}</td>
              		<td>${item[5] }</td>
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
    
    $(function(){
    	var myId='${userId}';
    	
    	$('table tr:gt(0)').each(function(){
    		try{
    			
    			var scores_arr=$(this).find("td:eq(2)").html().split(",");
        		var results_arr=$(this).find("td:eq(3)").html().split(",");
        		var ids=$(this).find("td:eq(5)").html()+","+$(this).find("td:eq(6)").html();
        		
        		$(this).find("td:eq(2)").html();
    			$(this).find("td:eq(3)").html();
    			$(this).find("td:eq(5)").remove();
    			$(this).find("td:eq(6)").remove();
        		
        		var ids_array=ids.split(",");
        		var  index=0;
        		for(var i=0;i<ids_array.length;i++){
        			if(myId==ids_array[i]){
        				$(this).find("td:eq(2)").html(scores_arr[i]);
        				$(this).find("td:eq(3)").html(results_arr[i]=="1"?"通过":"不通过");
        				$(this).find("td:eq(5)").remove();
            			$(this).find("td:eq(6)").remove();
        			}
        		}
        		
    		}catch(e){
    		}
    	})
    	
    })
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
    		data.staffVOs=[].slice.call(data.staffVOs,0,10);
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
    
    var fn_delete=function(id){
    	$.ajax({
    		url:'administration/chop/chopDelete',
    		type:'post',
    		data:{id:id},
    		success:function(data){
    			if(data&&(data=eval("("+data+")")))
	    			if(data.success){
	    				alert("删除成功！");
	    				location.reload();
	    			}else{
	    				alert("删除失败！");
	    			}
    		}
    	});
    }
    var fn_toUpdate =function(id){
    	location.href="administration/chop/toChopAddPage?id="+id;
    }
    var fn_log=function(id){ 
    	location.href="administration/chop/toChopUseLog?chopId="+id;
    }
    
    </script>
  </body>
</html>
