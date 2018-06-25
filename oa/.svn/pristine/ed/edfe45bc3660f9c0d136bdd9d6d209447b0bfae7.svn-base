<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
$(function() {		
	
    $(document).click(function (event) {$(".text").hide();$(".text ul").empty();if ($("#agent").val()!=$("#agentFlag").val()) {$("#agent").val("");}});   
	$('.inputout').click(function (event) {$(".text_down").show();});
	
});
	var a=1;
	
	 var b=0;
	 var c=0;

	function add(index){
		switch (index)
		  {
			  case 1:
			    b=1;
			    c=1;
			    break;
			  case 2:
				b=2;
				c=1;
			    break;
			  case 3:
			    b=1;
			    c=2;
			    break;
			  case 4:
				b=2;
				c=2;
			    break;

		  }

		a++; 
		$("#demo"+index+"").append(" <tr id="+a+">"+
				"<td width=20%;>"+
                "<div class=' inputout'>"+
			    	"<span class='input_text"+a+"' onclick='f("+a+")'>"+
			    	"<input type='text' id='agent"+a+"' class='form-control staff_Name"+index+"' required='required' oninput='findStaffByName("+a+")'  style='width:150px; height:40px' onblur='clears("+a+")'/>"+
			    	"<input type='hidden' id='agentFlag"+a+"' value=''/>"+
			    	"<input type='hidden' id='agentID"+a+"' name='specialVO.userIDs' />"+		    	
			    	"</span>"+
			    	"<div class='text_down"+a+" text'>"+
					"<ul></ul>"+
					"</div>"+
			        "</div></td>"+
               	 "<td id='staff_company"+a+"' width=20%;></td>"+
               	 "<td id='staff_department"+a+"' width=20%;></td>"+
               	 "<td id='staff_position"+a+"' width=20%;></td>"+
               	 " <td>"+
                  "<div class='col-sm-2 inputout>"+
                  	"<input type='hidden' id='staff_type"+a+"' value="+b+">"+
                  	"<input type='hidden' id='special_type"+a+"' name='special_type' value="+c+">"+
                  "</div>"+
                  "</td>"+
                 "<td><a onclick='delRow("+a+")'>删除</a></td></tr>") ;
           			
	}
	 
	
	function findStaffByName(index){
		var name = $("#agent"+index).val();
		var positionCategory=$("#special_type"+index).val();
		if (name.length == 0) {
			return;
		}
		$(".text_down"+index+" ul").empty();
		$.ajax({
			url:'personal/findStaffByNameAndStatus',
			type:'post',
			data:{name:name,positionCategory:positionCategory},
			dataType:'json',
			success:function (data){
				
				$.each(data.staffVOs, function(i, staff) {
					var groupDetail = staff.groupDetailVOs[0];
					
					$(".text_down"+index+" ul").append("<li>"+staff.lastName+"（—"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"—）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
				});
				$(".text_down"+index).show();
			}
		});
		
	}
	
	function f(index){
		if($(".text_down"+index+" ul").html() != ""){
			$(".text_down"+index).show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down'+index+' ul li',function () {
			var shtml=$(this).html();
			$(".text_down"+index).hide();
			$("#agent"+index).val(shtml.split("（")[0]);
			$("#agentFlag"+index).val(shtml.split("（")[0]);
			$("#staff_company"+index).text(shtml.split("—")[1]);
			$("#staff_department"+index).text(shtml.split("—")[2]);
			$("#staff_position"+index).text(shtml.split("—")[3]);
			var agent = $(this).find("input").val();
			$("#agentID"+index).val(agent.split("@")[0]);
			$("#agentName"+index).val(agent.split("@")[1]);
			});
			
			$(".text_down ul").empty();
	}
	function delRow(index){
		
		$("tr[id='"+index+"']").remove();
	}
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
       
	function clears(index){
		 if ($("#agent"+index).val()!=$("#agentFlag"+index).val())
		{$("#agent"+index).val("");} 
		
	}
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	function submitstaff(index) {
		
		var flag = 0;
		 $(".staff_Name"+index+"").each(function(i, obj) {
			if ($(obj).val().trim() == '') {
				flag = 1;
			}
		}); 
		if(flag== 1){
			showAlert("姓名不能为空 ");
			return;
		}
		if(confirm("提交后将不能被修改，您是否确定提交！")){
			$("#submitButton"+index+"").attr("disabled", "disabled");
			var formData = new FormData($("#post_staff"+index+"")[0]);
			
			$.ajax({
				url:'HR/staff/saveSpecial',
				type:'post',
				data:formData,
				contentType:false,
				processData:false,
				success:function(data) {
					if (data.errorMessage=="errorMessage") {
						alert("提交失败");
						$("#submitButton").removeAttr("disabled");
						return;
					}
					if(data.status==1){
						alert("已存在!");
						window.location.reload();
						return;
					}
					
						if (confirm("保存成功 ")) {
							window.location.href = "HR/staff/findSpecialList";
						}
					
				}
			});
		}
	}
	
	function special_delete(index){
		
		var formData = new FormData($("#post_staff"+index+"")[0]);
		var documentStrIds="";
		var chkUserInfos=document.getElementsByName("specialVO.specialID");
		var flag=false;
		for(var i=0;i<chkUserInfos.length;i++){
            if(chkUserInfos[i].checked){
              documentStrIds+=chkUserInfos[i].value+";";
				flag=true;     //表示复选框有被选中的
			}
		}
		if($("input[name='specialVO.specialID']:checked").length>0){
			$("#special_delete"+index+"").addClass("disabled");
			
			
		}else{
			alert("请选择!");
			return false ;
		}

		if(confirm("删除后将不能被修改，您是否确定删除 ！")){
		if(flag){
	         window.location.href="HR/staff/deleteSpecial?documentStrIds="+documentStrIds;  //提交后台 别忘了传参
	}
		
	}
	}
	
	
		
		
	

</script>
<style type="text/css">
    .inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}

	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.five{height:auto;}
	.five ul{margin:0px;padding:0px;*zoom:1;}
	.five ul:after{display:block;visibility:hidden;clear:both;overflow:hidden;height:0;content:"\0020";}
	
	.five ul li{width:20%;float:left;list-style-type:none;vertical-align:bottom;line-height:40px;}
</style>
</head>
 <body>

    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">蓝白岗位管理</h3>
       	  
       	  <form id="post_staff1">       	 			  
       	 <h4 class="sub-header" style="margin-top:0px;margin-bottom:0px;padding-top:10px;border-bottom: 0px solid #eee;">白色岗位不需要写日报的有：</h4>
       	 
       	 <div class="table-responsive">
            <table class="table table-striped">
              <tbody>
              	<c:if test="${not empty specialVOs}">
              	<s:set name="special_id1" value="(#request.page-1)*(#request.limit)"></s:set>
              			<td class="col-sm-1">
              			<div class="five">
              			<ul>
              			<s:iterator id="specialVO" value="#request.specialVOs" status="count"><li><input type='checkbox' name="specialVO.specialID" value="<s:property value='#specialVO.specialID'/>" /><s:property value="#specialVO.name"/></li></s:iterator>
              			</ul>
              			</div>
              			</td>             			             		
              		<s:set name="special_id1" value="#special_id1+1"></s:set>
              	
              	</c:if>
              </tbody>
            </table>
          </div>
          <button type="button"  id="special_delete1" class="btn btn-primary" onclick="special_delete(1)">删除</button> 
             &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
     		
       	 <button type="button" onclick="add(1)"  class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
              <tr>
              	
               	  <th width=20%;>姓名</th>
                  <th width=20%;>公司</th>
                  <th width=20%;>部门</th>
                  <th width=20%;>职务</th>
                                    
                  </tr>
              </thead>
              <tbody id="demo1">
               <tr id=1>
               	  <td width=20%;>
                  <div class=" inputout" >
			    	<span class="input_text1" onclick="f(1)">
					<input type="text" id="agent1" class="form-control staff_Name1" oninput="findStaffByName(1)"  style="width:150px; height:40px" onblur="clears(1)"/>			    	
					<input type="hidden"  id="agentFlag1" value=""/>
			    	<input type="hidden" id="agentID1" name="specialVO.userIDs" />		    	
			    	</span>
			    	<div class="text_down1 text">
						<ul></ul>
					</div>
			      </div>
			       <td id="staff_company1" width=20%;></td>
			      <td id="staff_department1" width=20%;></td>
                  <td id="staff_position1" width=20%;></td>
                  <td>
                  <div class="col-sm-2 inputout">
                  	<input type="hidden" id="staff_type1" name="specialVO.type" value="1">
                  	<input type="hidden" id="special_type1" name="special_type" value="1">
                  </div>
                  </td>                 
                  <td><a onclick="delRow(1)">删除</a></td>	
                </tr>
				
              </tbody>
            </table>
            
          <button type="button" style="float: right;" id="submitButton1" class="btn btn-primary" onclick="submitstaff(1)">提交</button>
      </form>  
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
       <form id="post_staff2" >       	 			  
       	 <h4 class="sub-header" style="margin-top:0px;margin-bottom:0px;border-bottom: 0px solid #eee;">白色岗位不需要签到的有：</h4>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <tbody>
              	<c:if test="${not empty specialVOs1}">
              	<s:set name="special_id2" value="(#request.page-1)*(#request.limit)"></s:set>
              	<td class="col-sm-1">
              			<div class="five">
              			<ul>
              			<s:iterator id="specialVO1" value="#request.specialVOs1" status="count"><li><input type='checkbox' name="specialVO.specialID" value="<s:property value='#specialVO1.specialID'/>" /><s:property value="#specialVO1.name"/></li></s:iterator>
              			</ul>
              			</div>
              			</td> 
              	</c:if>
              </tbody>
            </table>
          </div>
           <button type="button"  id="special_delete2" class="btn btn-primary" onclick="special_delete(2)">删除</button>
             &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
       	 <button type="button" onclick="add(2)"  class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
                <tr>
                  <th width=20%;>姓名</th>
                  <th width=20%;>公司</th>
                  <th width=20%;>部门</th>
                  <th width=20%;>职务</th>
                  
                </tr>
                </thead>
              <tbody id="demo2">
               <tr id=100>
               	  <td width=20%;>
                  <div class=" inputout" >
			    	<span class="input_text100" onclick="f(100)">
					<input type="text" id="agent100" class="form-control staff_Name2" oninput="findStaffByName(100)"  style="width:150px; height:40px" onblur="clears(100)"/>			    	
					<input type="hidden"  id="agentFlag100" value=""/>
			    	<input type="hidden" id="agentID100" name="specialVO.userIDs" />		    	
			    	</span>
			    	<div class="text_down100 text">
						<ul></ul>
					</div>
			      </div>
			      </td>
			      <td id="staff_company100" width=20%;></td>
			      <td id="staff_department100" width=20%;></td>
                  <td id="staff_position100" width=20%;></td>
                  <td>
                  <div class="col-sm-2 inputout">
                  	<input type="hidden" id="staff_type100" name="specialVO.type" value="2">
                  	<input type="hidden" id="special_type100" name="special_type" value="1">
                  </div>
                  </td>                 
                  <td><a onclick="delRow(100)">删除</a></td>	
                </tr>
				
              </tbody>
            </table>
           
          <button type="button" style="float: right;" id="submitButton2" class="btn btn-primary" onclick="submitstaff(2)">提交</button>
      </form>  
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      
       <form id="post_staff3" >       	 			  
       	 <h4 class="sub-header" style="margin-top:0px;margin-bottom:0px;border-bottom: 0px solid #eee;">蓝色岗位需要写日报的有：</h4>
       	 <div class="table-responsive">
            <table class="table table-striped">
              <tbody>
              	<c:if test="${not empty specialVOs2}">
              	<s:set name="special_id3" value="(#request.page-1)*(#request.limit)"></s:set>
              	<td class="col-sm-1">
              			<div class="five">
              			<ul>
              			<s:iterator id="specialVO2" value="#request.specialVOs2" status="count"><li><input type='checkbox' name="specialVO.specialID" value="<s:property value='#specialVO2.specialID'/>" /><s:property value="#specialVO2.name"/></li></s:iterator>
              			</ul>
              			</div>
              			</td> 
              	</c:if>
              </tbody>
            </table>
          </div>
          <button type="button"  id="special_delete3" class="btn btn-primary" onclick="special_delete(3)">删除</button>
             &nbsp;
     	 	 &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
       	 <button type="button" onclick="add(3)"  class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
                <tr>
                  <th width=20%;>姓名</th>
                  <th width=20%;>公司</th>
                  <th width=20%;>部门</th>
                  <th width=20%;>职务</th>
                  
                </tr>
                </thead>
              <tbody id="demo3">
               <tr id=200>
               	  <td width=20%;>
                  <div class=" inputout" >
			    	<span class="input_text200" onclick="f(200)">
					<input type="text" id="agent200" class="form-control staff_Name3" oninput="findStaffByName(200)"  style="width:150px; height:40px" onblur="clears(200)"/>			    	
					<input type="hidden"  id="agentFlag200" value=""/>
			    	<input type="hidden" id="agentID200" name="specialVO.userIDs" />		    	
			    	</span>
			    	<div class="text_down200 text">
						<ul></ul>
					</div>
			      </div>
			      </td>
			      <td id="staff_company200" width=20%;></td>
			      <td id="staff_department200" width=20%;></td>
                  <td id="staff_position200" width=20%;></td>
                  <td>
                  <div class="col-sm-2 inputout">
                  	<input type="hidden" id="staff_type200" name="specialVO.type" value="1">
                  	<input type="hidden" id="special_type200" name="special_type" value="2">
                  </div>
                  </td>                 
                  <td><a onclick="delRow(200)">删除</a></td>	
                </tr>
				
              </tbody>
            </table>
            
          <button type="button" style="float: right;" id="submitButton3" class="btn btn-primary" onclick="submitstaff(3)">提交</button>
      </form> 
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
      &nbsp;
       <form id="post_staff4" >       	 			  
       	 <h4 class="sub-header" style="margin-top:0px;margin-bottom:0px;border-bottom: 0px solid #eee;">蓝色岗位需要签到的有：</h4>
       	 <div class="table-responsive">
            <table class="table table-striped">
              <tbody>
              	<c:if test="${not empty specialVOs3}">
              	<s:set name="special_id4" value="(#request.page-1)*(#request.limit)"></s:set>
              	<td class="col-sm-1">
              			<div class="five">
              			<ul>
              			<s:iterator id="specialVO3" value="#request.specialVOs3" status="count"><li><input type='checkbox' name="specialVO.specialID" value="<s:property value='#specialVO3.specialID'/>" /><s:property value="#specialVO3.name"/></li></s:iterator>
              			</ul>
              			</div>
              			</td> 
              	</c:if>
              </tbody>
            </table>
          </div>
          <button type="button"  id="special_delete4" class="btn btn-primary" onclick="special_delete(4)">删除</button>
             &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
     		 &nbsp;
       	 <button type="button" onclick="add(4)"  class="btn btn-primary">增加</button>
            <table class="table table-striped" >
              <thead>
                <tr>
                  <th width=20%;>姓名</th>
                  <th width=20%;>公司</th>
                  <th width=20%;>部门</th>
                  <th width=20%;>职务</th>
                  
                </tr>
                </thead>
              <tbody id="demo4">
               <tr id=300>
               	  <td width=20%;>
                  <div class=" inputout" >
			    	<span class="input_text300" onclick="f(300)">
					<input type="text" id="agent300" class="form-control staff_Name4" oninput="findStaffByName(300)"  style="width:150px; height:40px" onblur="clears(300)"/>			    	
					<input type="hidden"  id="agentFlag300" value=""/>
			    	<input type="hidden" id="agentID300" name="specialVO.userIDs" />		    	
			    	</span>
			    	<div class="text_down300 text">
						<ul></ul>
					</div>
			      </div>
			      </td>
			      <td id="staff_company300" width=20%;></td>
			      <td id="staff_department300" width=20%;></td>
                  <td id="staff_position300" width=20%;></td>
                  <td>
                  <div class="col-sm-2 inputout">
                  	<input type="hidden" id="staff_type300" name="specialVO.type" value="2">
                  	<input type="hidden" id="special_type300" name="special_type" value="2">
                  </div>
                  </td>                 
                  <td><a onclick="delRow(300)">删除</a></td>	
                </tr>
				
              </tbody>
            </table>
            
          <button type="button" style="float: right;" id="submitButton4" class="btn btn-primary" onclick="submitstaff(4)">提交</button>
      </form> 
            
        </div>
      </div>
    </div>
  </body>
</html>
