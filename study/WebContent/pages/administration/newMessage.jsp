<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/editor/themes/default/default.css" />
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/editor/kindeditor-min.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">

	$(function() {
		//富文本
    	kedit('textarea[name="noticeVO.ntcContent"]');
		
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "noticeVO.departmentIDs");
		
    	$('#addVacationForm').submit(function(){
    		var fileList=document.getElementById('files').files;
    		var sumSize=0;
    		var result_arr=_.reduce(fileList,function(arr,input){
    			var name=input.name;
    			var fileName= name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
   	            var suffix=fileName.replace(/.*\.(.*)/,"$1");
   	            if(suffix)suffix=suffix.toLowerCase();
   	            arr.push([fileName,suffix]);
   	         	sumSize+=input.size;
   	            return arr;
    		},[]);
    		if(sumSize>1024*1024*20){
    			layer.alert("文件总大小不能超过20M", {offset:'100px'});
    			return false;
    		}
     	    $('input[name="fileDetail"]').val(JSON.stringify(result_arr));
     	    $('#submitButton').attr('disabled','disabled');
     	   	Load.Base.LoadingPic.FullScreenShow(null);
    	})
	});
	function kedit(kedit){
		var	editor =KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				items : [
					'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
					'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
					'insertunorderedlist', 'table', '|', 'emoticons', 'image', 'link', '|','preview']
			});
	}
	 function showDepartment(obj, level, ii) {
			level = level+1;
			$(".flag"+ii+" .department"+level).remove();
			$(".flag"+ii+" .department1 select").removeAttr("name");
			if ($(obj).val() == '') {
				if (level > 2) {
					$(".flag"+ii+" #department"+(level-2)).attr("name", "noticeVO.departmentIDs");
				}
				if(level==2){
					$(".flag"+ii+" #department"+(level-1)).attr("name", "noticeVO.departmentIDs");
				}
				return;
			}
			
			var parentID = 0;
			if (level != 1) {
				parentID = $(obj).val();
				$(obj).attr("name", "noticeVO.departmentIDs");
			}
			var companyID = $($(obj).parent().parent().find("select")[0]).val();
			$.ajax({
				url:'HR/staff/findDepartmentsByCompanyIDParentID',
				type:'post',
				data:{companyID: companyID,
					  parentID: parentID},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						if (level == 1) {
							window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						} else {
							return;
						}
					}
					
					var divObj = $(".flag"+ii+" #"+$(obj).attr('id')+"_div");
					if(level==1){
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
								+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" name=\"noticeVO.departmentIDs\">"
								+"<option value=\"\">--"+level+"级部门--</option></select>"
								+"</div>");
						
					}else{
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
									+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" >"
									+"<option value=\"\">--"+level+"级部门--</option></select>"
									+"</div>");
					}
					
					$.each(data.departmentVOs, function(i, department) {
						$(".flag"+ii+" #department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
					});
				}
			});
		}
	 var a=1;
	 function add(){
		 a++;
		 $("#demo").append(
				 '<div class="form-group flag'+a+'">'+
				 '<div class="col-sm-1 control-label"><a href="javasript:(0)" onclick="deleteTag(this)"><svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
					'<use xlink:href="#icon-delete"></use>'+
					'</svg></a></div>'+
				 '<div class="col-sm-2" id="company'+a+'_div">'+
			    	'<select class="form-control" id="company'+a+'" name="noticeVO.companyIDs" onchange="showDepartment(this, 0, '+a+')" required="required">'+
				      '<option value="">请选择</option>'+
				      	'<s:iterator id="company" value="#request.companyVOs" status="st">'+
				      		'<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>'+
				      	'</s:iterator>'+
					'</select>'+
			    '</div>'+
			    '</div>');
		 $("[data-toggle='tooltip']").tooltip();
	          }
	 function deleteTag(data){
		 $(data).parent().parent().remove();
	 }

</script>
<style type="text/css">
	.ke-container{
		width:100% !important;
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
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="addVacationForm"  action="administration/notice/saveMessage"  method="post" class="form-horizontal" enctype="multipart/form-data">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增通知</h3>
        	   <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label" style="width:10%">标题</label>
			   <div class="col-sm-4">
			    	<input type="text" class="form-control" id="expressNumber" name="noticeVO.ntcTitle" required="required"/>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label" style="width:10%">内容</label>
			     <div class="col-sm-6">
			   		<textarea class="form-control" rows="8" id="content" name="noticeVO.ntcContent"></textarea>
			    </div>
			     <button type="button" onclick="add()"  class="btn btn-primary">增加公司部门</button>
			  </div>
	    	  <div class="form-group flag1">
				<label for="company" class="col-sm-1 control-label" style="width:10%">公司部门</label>
				
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="noticeVO.companyIDs" onchange="showDepartment(this,0,1)" required="required">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			<div id="demo"></div>
	    	  <div class="form-group" >
				<label for="attachment" class="col-sm-1 control-label" style="width:10%"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
				<div class="col-sm-2">
	    			<input  multiple name="files"  id="files" type="file" />
	    			<input name="fileDetail" style="display:none"/>
	    		</div>
	    	  </div>
			  <div class="form-group">
			  	<div class="col-sm-1 control-label" style="width:10%"></div>
			  	<div class="col-sm-3">
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  	</div>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>