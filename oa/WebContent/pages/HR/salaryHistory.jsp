<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
	});
	function showAttachment(attachmentIds){
		$.ajax({
			url:'HR/staffInfoAlteration/showAttachment',
			data:{'attachmentIds':attachmentIds},
			success:function(data){
				if(!data.hasAtta){
					layer.alert("无",{offset:'100px',title:'附件'});
				}else{
					var html = '';
					var picHtml = '';
					var attaHtml = '';
					data.attaList.forEach(function(value, index){
						var suffix = value.suffix;
						if(suffix=='png'){
							picHtml += '<img onclick="showPic(\''+value.softName+'\',\''+value.softURL+'\')" style="cursor:hand;width:100px" src="HR/staffInfoAlteration/showImage?attachmentPath='+value.softURL+'">';
						}else{
							attaHtml += '<a href="HR/staffInfoAlteration/downloadAttach?attachmentPath='+value.softURL+'&attachmentName='+value.softName+'">'+value.softName+'</a><br>';
						}
					});
					picHtml = '<label>图片：</label><br>'+picHtml+'<br>';
					html += picHtml;
					if(attaHtml!=''){
						attaHtml = '<label>文件：</label><br>'+attaHtml;
						html += attaHtml;
					}
					layer.alert(html,{offset:'100px',title:'附件',maxWidth:'310px'});
				}
			}
		});
	}
	var showPic = function (name, path) {
		var picData = {
			start : 0,
			data : []
		}
		picData.data.push({
			alt : name,
			src : "HR/staffInfoAlteration/showImage?attachmentPath=" + path
		})
		layer.photos({
			offset : '50px',
			photos : picData,
			anim : 5
		});
	}
</script>
<style type="text/css">

	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.myTr td ,.myTr th {
		text-align:center;
	}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus{text-decoration:none}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header"><s:if test="#staffInfoAlterationVOList!=null"><s:property value="#staffInfoAlterationVOList[0].userName"/></s:if>薪资变动历史记录
          	<button style="float:right" type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'">返回</button>
          </h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr class="myTr">
                  <th>序号</th>
                  <th>变动前的薪资</th>
                  <th>变动后的薪资</th>
                  <th>生效时间</th>
                  <th>操作时间</th>
                  <th>操作人</th>
                  <th>查看附件</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffInfoAlterationVOList}">
              	<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="staffInfoAlterationVO" value="#request.staffInfoAlterationVOList" status="count">
              		<tr class="myTr">
              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.salaryBefore"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.salaryAfter"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.effectDate"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.operateTime"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.operatorName"/></td>
              			<td class="col-sm-1">
              			<a href="javascript:void(0)" onclick="showAttachment('<s:property value="#staffInfoAlterationVO.attachmentIds"/>')">
	              			<svg class="icon" aria-hidden="true">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              		</a>
              			</td>
              		</tr>
              		<s:set name="staff_id" value="#staff_id+1"></s:set>
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
  </body>
</html>
