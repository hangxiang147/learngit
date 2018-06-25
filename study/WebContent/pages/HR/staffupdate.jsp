<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	$(function() {
	set_href();
	var skill;
	var skills=$("#staffVO_sk").val();
	if(skills!=null){
		var s=skills.substring(1,skills.length-1);
		skill=s.split(",");
	}
	var master;
	var masters=$("#staffVO_mas").val();
	if(masters!=null){
		var m=masters.substring(1,masters.length-1);
		master=m.split(",");
	}
	for(var i=0;i<skill.length;i++){
		for(var j=0;j<$(":checkbox").length;j++){
			if($($(":checkbox")[j]).val()==skill[i].trim()){
				$($(":checkbox")[j]).prop('checked',true);
				smallChange($(":checkbox")[j],j+1);
				$("#staff_skill"+(j+1)).find("select").val(master[i].trim());
			}
		}
		}			
	});			
	var checkSubmitFlg = false;
	function check() {
		for(var j=0;j<$(":checkbox").length;j++){
			if($($(":checkbox")[j]).is(':checked')){
				checkSubmitFlg = true;				
				if($("#staff_skill"+(j+1)).find("select").val()=="请选择"){
					layer.alert("请选择技能 ！",{offset:'100px'});
					return false;
				}			
			}
		}
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	 function smallChange(obj,index) {	       
	        var html="";
	        if (obj.checked == true){	        	
	           html+="<select name='staffVO.masters'>"
		     	 +"<option >请选择</option>"
			 	 +"<option value='了解'>了解</option>"
			 	 +"<option value='熟练'>熟练</option>"
			 	 +"<option value='精通'>精通</option>"
				 +"</select>";				 
				 $("#staff_skill"+index+"").html(html);
				 
	        }	 
	        else {	        	
	        	$("#staff_skill"+index+"").html("");
	        }
	    }
	window.history.forward();


</script>
<style type="text/css">
	a:link {
	 text-decoration: none;
	}
	a:visited {
	 text-decoration: none;
	}
	a:hover {
	 text-decoration: none;
	}
	a:active {
	 text-decoration: none;
	}
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
	
.clear{clear:both;height:0;font-size:0px;_line-height:0px;}
#namecy{position:absolute;top:10px;left:10px;}
#namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
#namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
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
        	<form action="HR/staff/updateSkill" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">技能修改</h3> 
        	  <input type="hidden" name="staffVO.userID" value="${skillVO.userID}"/>
        	   <input type="hidden" id="staffVO_sk"  value="${skillVO.skills}"/>
        	   <input type="hidden" id="staffVO_mas"  value="${skillVO.masters}"/>
        	   <div class="form-group">
			  	<label for="name" class="control-label">员工:</label>
			  	<span>${skillVO.userName}&nbsp;&nbsp;&nbsp;</span>
			  </div>       	   			          	
				<div>			 			  
			  	<div class="form-group">			  	
			  		<div class="five">
			  		<ul>
			  		<li><input type="checkbox" name="staffVO.skills" value="自动裁剪" onclick="smallChange(this,1)" id="checkbox_status1" />自动裁剪<span id="staff_skill1"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="手工裁剪" onclick="smallChange(this,2)" id="checkbox_status2"/>手工裁剪<span id="staff_skill2"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="整烫" onclick="smallChange(this,3)" id="checkbox_status3"/>整烫<span id="staff_skill3"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="制版" onclick="smallChange(this,4)" id="checkbox_status4"/>制版<span id="staff_skill4"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="排图" onclick="smallChange(this,5)" id="checkbox_status5"/>排图<span id="staff_skill5"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="工艺" onclick="smallChange(this,6)" id="checkbox_status6"/>工艺<span id="staff_skill6"></span></li>
			  		<li><input type="checkbox" name="staffVO.skills" value="直通车" onclick="smallChange(this,7)" id="checkbox_status7" />直通车<span id="staff_skill7"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="钻展" onclick="smallChange(this,8)" id="checkbox_status8"/>钻展<span id="staff_skill8"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="绣花" onclick="smallChange(this,9)" id="checkbox_status9"/>绣花<span id="staff_skill9"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="印花" onclick="smallChange(this,10)" id="checkbox_status10"/>印花<span id="staff_skill10"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="水电维修" onclick="smallChange(this,11)" id="checkbox_status11"/>水电维修<span id="staff_skill11"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="数码产品维修" onclick="smallChange(this,12)" id="checkbox_status12"/>数码产品维修<span id="staff_skill12"></span></li>
			  		<li><input type="checkbox" name="staffVO.skills" value="电脑维修" onclick="smallChange(this,13)" id="checkbox_status13" />电脑维修<span id="staff_skill13"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="网络维修" onclick="smallChange(this,14)" id="checkbox_status14"/>网络维修<span id="staff_skill14"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="电焊" onclick="smallChange(this,15)" id="checkbox_status15"/>电焊<span id="staff_skill15"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="钳工" onclick="smallChange(this,16)" id="checkbox_status16"/>钳工<span id="staff_skill16"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="缝制" onclick="smallChange(this,17)" id="checkbox_status17"/>缝制<span id="staff_skill17"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="锁钉" onclick="smallChange(this,18)" id="checkbox_status18"/>锁钉<span id="staff_skill18"></span></li>
			  		<li><input type="checkbox" name="staffVO.skills" value="语言翻译" onclick="smallChange(this,19)" id="checkbox_status19" />语言翻译<span id="staff_skill19"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="绘画" onclick="smallChange(this,20)" id="checkbox_status20"/>绘画<span id="staff_skill20"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="写作" onclick="smallChange(this,21)" id="checkbox_status21"/>写作<span id="staff_skill21"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="书法" onclick="smallChange(this,22)" id="checkbox_status22"/>书法<span id="staff_skill22"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="烹饪" onclick="smallChange(this,23)" id="checkbox_status23"/>烹饪<span id="staff_skill23"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="套口" onclick="smallChange(this,24)" id="checkbox_status24"/>套口<span id="staff_skill24"></span></li>
			  		<li><input type="checkbox" name="staffVO.skills" value="挡车" onclick="smallChange(this,25)" id="checkbox_status25" />挡车<span id="staff_skill25"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="套结" onclick="smallChange(this,26)" id="checkbox_status26"/>套结<span id="staff_skill26"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="报关" onclick="smallChange(this,27)" id="checkbox_status27"/>报关<span id="staff_skill27"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="质检" onclick="smallChange(this,28)" id="checkbox_status28"/>质检<span id="staff_skill28"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="办公软件" onclick="smallChange(this,29)" id="checkbox_status29"/>办公软件<span id="staff_skill29"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="A1驾照" onclick="smallChange(this,30)" id="checkbox_status30"/>A1驾照<span id="staff_skill30"></span></li>
			  		<li><input type="checkbox" name="staffVO.skills" value="A2驾照" onclick="smallChange(this,31)" id="checkbox_status31" />A2驾照<span id="staff_skill31"></span></li>			  					  					  		
			  		<li><input type="checkbox" name="staffVO.skills" value="A3驾照" onclick="smallChange(this,32)" id="checkbox_status32"/>A3驾照<span id="staff_skill32"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="B1驾照" onclick="smallChange(this,33)" id="checkbox_status33"/>B1驾照<span id="staff_skill33"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="B2驾照" onclick="smallChange(this,34)" id="checkbox_status34"/>B2驾照<span id="staff_skill34"></span></li>		  		
			  		<li><input type="checkbox" name="staffVO.skills" value="C1驾照" onclick="smallChange(this,35)" id="checkbox_status35"/>C1驾照<span id="staff_skill35"></span></li>			  		
			  		<li><input type="checkbox" name="staffVO.skills" value="C2驾照" onclick="smallChange(this,36)" id="checkbox_status36"/>C2驾照<span id="staff_skill36"></span></li>
			  		</ul>			  					  						    		
			    	</div>			    	
			  	</div>			  	
			  	</div>
			
			  <div class="form-group">
			  	<button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'" >返回</button>
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>			    
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>