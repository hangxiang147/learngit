<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
        <s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/personal/underlingSalaryDetail">     	  
          	<h3 class="sub-header" style="margin-top:0px;">工资单查看</h3> 
			   <div class="form-group" >
			  	<label  class="col-sm-1 control-label">年份</label>
			  	<div class="col-sm-2">
			    	<input name="year"  class="form-control"  onclick="WdatePicker({dateFmt:'yyyy'})" value="${year}"/>
			    </div>

			  	<label for="category" class="col-sm-1 control-label">月份</label>			    
			     <div class="col-sm-2">
			    	<input name="month"  class="form-control"  onclick="WdatePicker({dateFmt:'MM'})" value="${month}"/>
			    </div>
			    <div class="col-sm-2">
			    	<button type="submit" class="btn btn-primary">查看</button>
			    </div>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">姓名</th>
                  <th class="col-sm-1">部门</th>
 <!--                  <th class="col-sm-1">状态</th> -->
                  <th class="col-sm-1">是否已邮箱推送</th>
                  <th class="col-sm-1">是否已短信推送</th>
              <!--     <th class="col-sm-1">异议详情</th> -->
                  <th class="col-sm-1">工资单详情</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty lists}">
              	<c:forEach items="${lists}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item.name }</td>
            		<td>${item.departmentName}</td>
<%--               		<td>
              		<c:if test="${item.status eq null or  item.status eq '' }">
						待确认
					</c:if> 
					<c:if test="${item.status eq 2}">
						已确认
					</c:if> 
					<c:if test="${item.status eq 1}">
						<font color="red">待人事重新审核</font>
					</c:if>
              		</td> --%>
              		<td>${item.emailSend  eq '1'?"是":"否"}</td>
              		<td>${item.mobileSend eq '1'?"是":"否"}</td>
              	<%-- 	<td>${item.content }</td> --%>
				<td><a href="javascript:showSalary('${item.detailList}','${item.status}','${item.year}','${item.month}','${item.departmentName}','${item.name}')">查看详情</a></td> 
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
    <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      	<div>
	      	<hr>
	      	<h4>注：</h4>
	      	<p id="zhu">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;骑岸总部在门卫处领取；<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;南通分部在人事行政部领取；</p>
	      	</div>
	      
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
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
    
    
    var prevArray=["部门","姓名","基础工资","绩效奖金","绩效","加班补贴","满勤","应抵扣金额","其他扣除","迟到扣除","扣养老","扣医保","扣失业","大病","扣住房公积金","扣个税","实发工资"];
	function showSalary (detail,key,year,month,deptName,userName){
		var detail=detail.match(/\[(.*)\]\s*/)[1].split(",");
		$("#exampleModalLabel").html(year+"年"+month+"月 工资详情");
		var resultContent=[];
		prevArray.forEach(function (value,index){
			if(index>1){
				resultContent.push(prevArray[index]+":"+detail[index-2])
			}else{
				if(index==0){
					resultContent.push(prevArray[index]+":"+deptName);
				}else{
					resultContent.push(prevArray[index]+":"+userName);
				}
			}
		})
    	$("#ntcContent").html(resultContent.join("</br>"));
    	$("#zhu").parent().remove();
    	$("#cheakModal").modal('show');
	}
    </script>
  </body>
</html>
