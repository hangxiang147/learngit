
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
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
	a:hover{text-decoration:none}
	.delete:hover{color:red}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'softPersonTypeList'"></s:set>
        <%@include file="/pages/performance/soft/manage/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" action="performance/soft/softPersonTypeList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<h3 class="sub-header" style="margin-top:0px;">人员类型维护</h3>
          	<div class="form-group">
          		<label class="col-sm-1 control-label">姓名</label>
          		<div class="col-sm-2">
          			<input id="staff" class="form-control" autoComplete="off" type="text" name="staffName" value="${staffName}" onkeyup="checkEmpty()">
          			<input type="hidden" name="staffId" value="${staffId}">
          		</div>
          		<label class="col-sm-1 control-label">类型</label>
          		<div class="col-sm-2">
          			<select class="form-control" name="personType">
          				<option value="">请选择</option>
          				<option value="产品经理" ${personType=='产品经理'?"selected":""}>产品经理</option>
          				<option value="需求分析" ${personType=='需求分析'?"selected":""}>需求分析</option>
          				<option value="编码人员" ${personType=='编码人员'?"selected":""}>编码人员</option>
          				<option value="软件测试" ${personType=='软件测试'?"selected":""}>软件测试</option>
          				<option value="实施" ${personType=='实施'?"selected":""}>实施</option>
          			</select>
          		</div>
          		<label class="col-sm-1 control-label">项目</label>
          		<div class="col-sm-2">
          			<select class="form-control" name="projectName">
          				<option value="">请选择</option>
          				<c:forEach items="${projects}" var="project">
          					<option value="${project.name}" ${project.name==projectName?"selected":""}>${project.name}</option>
          				</c:forEach>
          			</select> 
          		</div>
          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          		<button type="submit" class="btn btn-primary">查询</button>
          	</div>
          </form>
		  <a onclick="goPath('/performance/soft/softPersonTypeAdd')" href="javascript:void(0)" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>人员新增</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>类型</th>
                  <th>所属项目</th>
                  <th>是否为组长</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty list}">
              	<c:forEach items="${list}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item.userName }</td>
              		<td>${item.type }</td> 
              		<td>${item.project }</td>
              		<td>${item.isGroupLeader=="1"?"是":"否" }</td>
              		<td>
             			<a class="delete" href="javascript:delete_('${item.id}')">
             			<svg class="icon" aria-hidden="true">
             				<use xlink:href="#icon-delete"></use>
             			</svg>
             			</a>
             			<a  onclick="goPath('/performance/soft/softPersonTypeAdd?id=${item.id}')" href="javascript:void(0)">
             			<svg class="icon" aria-hidden="true">
             				<use xlink:href="#icon-modify"></use>
             			</svg>
             			</a>
              		</td>
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
    <script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/require/require2.js"></script>
    <script>
    	
 	  var delete_=function(id){
 	    	layer.confirm('是否确认删除？', {
 	    		  btn: ['确认','取消'],offset:'100px'
 	    		},function(index){
 	    			layer.close(index);
 	    			$.get('/performance/soft/deleteGroup?id='+id,function(){
 	    	    		location.reload();
 	    	    		Load.Base.LoadingPic.FullScreenShow(null);
 	    	    	})
 	    		});
 	    	
 	    }
 		$(function() {
 			set_href();
 		});
 	  	require(['staffComplete'],function (staffComplete){
 			new staffComplete().render($('#staff'),function ($item){
 				$("input[name='staffId']").val($item.data("userId"));
 			});
 		});
 	  	function checkEmpty(){
 	  		if($("#staff").val()==''){
 	  			$("input[name='staffId']").val('');
 	  		}
 	  	}
 		$(document).click(function (event) {
 			if ($("input[name='staffId']").val()=='')
 			{
 				$("#staff").val("");
 			}
 		}); 
    </script>
  </body>
</html>

