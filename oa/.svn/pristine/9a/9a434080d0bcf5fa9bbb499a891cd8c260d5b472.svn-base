`<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
.clear{clear:both;height:0px;font-size:0px;_line-height:0px;}
	.tableuser{border:1px solid #ddd;border-spacing:0;border-collapse:collapse;width:1080px;color:#555555;}
	.tableuser tr{display:table-row;vertical-align:inherit;border-color:inherit;}
	.tableuser tr td{border:1px solid #ddd;height:34px;line-height:34px;padding:10px 10px;}
	
	*{margin:auto;} 
	.logolm{background:#003e87;line-height:40px;width:450px;height:40px;text-align:center;color:#fff;font-size:18px;}
	.introduce{width:450px;color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.introduce table{width:100%;border-collapse:collapse;}
	.introduce table tr td{border:1px solid #ddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
	.introduce table tr td.lm{background:#f1f1f1;color:#000;}
	.introduce table tr td p{padding:4px 0px;}
	
	.vcard ul,.vcard form,.vcard p,.vcard h1,.vcard i,.vcard em{margin:0px;padding:0px;}
	.vcard i{list-style:none;font-style:normal;font-weight:normal;}
	.vcard li{list-style-type:none;vertical-align:bottom}
	.vcard{max-width:400px;color:#555555;box-shadow:0px 1px 5px #dddddd;position:relative;padding:20px 25px;margin-top:20px;}
	.vcard h1{font-size:20px;font-weight:normal;}
	.vcard h1 span{font-size:14px;color:#1f5ea6;}
	.vcard .comy{color:#1f5ea6;font-size:16px;margin-top:10px;}
	.vcard .information{margin-top:20px;}
	.vcard .information p{height:30px;line-height:30px;}
	.vcard .information p span{float:left;padding-left:5px;}
	.vcard .those{margin-top:10px;*zoom:1;}
	.vcard .those:after{display:block;visibility:hidden;clear:both;overflow:hidden;height:0;content:"";}
	.vcard .those ul li{float:left;height:12px;margin:5px 0px;width:25%;text-align:center;}
	.vcard .those ul li img{height:12px;}
	.ico{background:url(assets/images/ico.png) no-repeat;display:inline-block;width:13px;height:30px;float:left;}
	.ico_tell{background-position:-14px 7px;}
	.ico_mail{background-position:-56px 12px;}
	.ico_address{background-position:0px 6px;}
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
.text_down1{position:absolute;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;left:0px; top:33px;width:100%;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;4}
.text_down1 ul{padding:2px 10px;}
.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down1 ul li span{color:#cc0000;}
.table-responsive{color:#555555;box-shadow:0px 1px 5px #dddddd;}
table{border-collapse:collapse;}
 table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
 table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
 table tr td:nth-child(odd) {background:#efefef;text-align:center;color:#000;}
table tbody tr td p{padding:4px 0px;}
 table tbody tr td li{list-style:none;text-align: left;}

</style>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/></head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'job'"></s:set>
    	<s:set name="selectedPanel" value="'toSignList'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<div role="tabpanel" class="tab-pane" id="information" style="float:left;">
        		<form method="post" action="/HR/vitae/updateVitaeSign" id="form_">
	            <input class="form-control"  style="display:none" name="vitaeSignEntity.id" value="${vitaeSignEntity.id}">
	            <input class="form-control"  style="display:none" name="vitaeSignEntity.vitaeId" value="${vitaeSignEntity.vitaeId}">
	            <input class="form-control"  style="display:none" name="vitaeSignEntity.isDeleted" value="${vitaeSignEntity.isDeleted}">
	            <input class="form-control"  style="display:none" name="vitaeSignEntity.addTime" value="${vitaeSignEntity.addTime}">
	            <input class="form-control"  style="display:none" name="vitaeSignEntity.bh" value="${vitaeSignEntity.bh}">
	            <input class="form-control"  style="display:none" name="resultId" value="${resultId}">
	            <input class="form-control"  style="display:none" name="taskId" value="${taskID}">
        		<h3 class="sub-header" style="margin-top:0px;">基本信息
        		<span style="font-size:20px;color:#ccc;float:right;">编号：<span id="bhspan">&nbsp;&nbsp;&nbsp;001125</span></span>
        		
        		</h3>        		
				  	<div class="table-responsive" style="padding-top:0px;">
	            		<table class="tableuser">
						 	<tbody><tr>
						   	<td width="10%" align="center">姓名：</td>
						    <td width="15%"><input class="form-control" name="vitaeSignEntity.xm" value="${vitaeSignEntity.xm}" maxlength="20"></td>
						    <td width="10%" align="center">性别：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.xb"></select></td>
						    <td width="10%" align="center">民族：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.mz" ></select></td>
						    <td width="10%" align="center">年龄：</td>
						    <td width="15%"><input class="form-control" name="vitaeSignEntity.nl" type="number" min=1 value="${vitaeSignEntity.nl}"/></td>
						  </tr>
						  <tr>
						   <tbody><tr>
						   	<td width="10%" align="center">第一学历：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.zgxl"></select></td>
						   	<td width="10%" align="center">政治面貌：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.zzmm"></select></td>
						    <td width="10%" align="center">籍贯：</td>
						    <td width="15%"><input class="form-control" name="vitaeSignEntity.jg" value="${vitaeSignEntity.jg}" maxlength="20" ></input></td>
						    <td width="10%" align="center">健康状况：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.jkzk" ></select></td>
						  </tr>
						  <tr>
						   	<td width="10%" align="center">婚孕情况：</td>
						    <td width="15%"><input class="form-control" name="vitaeSignEntity.hyqk" value="${vitaeSignEntity.hyqk}"></input></td>
						   	<td width="10%" align="center">有无社保：</td>
						    <td width="15%"><select class="form-control" name="vitaeSignEntity.ywsb"></select></td>
						    <td width="10%" align="center">到岗时间：</td>
						    <td width="15%"><input type="text" class="form-control"  name="vitaeSignEntity.dgsj" value="<fmt:formatDate pattern="yyyy-MM-dd"  value="${vitaeSignEntity.dgsj}" />" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd '})"  placeholder="预计到岗时间"></td>
						    <td width="10%" align="center">期望月薪：</td>
						    <td width="15%"><input class="form-control" name="vitaeSignEntity.qwxz"  type="number" min=0 value="${vitaeSignEntity.qwxz}"></input></td>
						  </tr>
						  <tr>
						  	<td width="10%" align="center" >身份证：</td>
						    <td width="30%" colspan="3"><input class="form-control" name="vitaeSignEntity.sfz" value="${vitaeSignEntity.sfz}" maxlength="20"></input></td>
						   	<td width="10%" align="center">联系方式：</td>
						    <td width="30%" colspan="3"><input class="form-control" name="vitaeSignEntity.lxfs" value="${vitaeSignEntity.lxfs }" maxlength="20"></input></td>
						  </tr>
						  <tr>
						  	<td align="center">现居住地址</td>
						  	<td colspan="3" id="dz"></td>
						  	<td align="center">地址详情</td>
						  	<td colspan="3"><input class="form-control" name="vitaeSignEntity.dzDetail" value="${vitaeSignEntity.dzDetail }" maxlength="200"></td>
						  </tr>
						  <tr>
						  	<td align="center">外语水平</td>
						  	<td colspan="7"><input class="form-control"   name="vitaeSignEntity.wysp" value="${vitaeSignEntity.wysp}" maxlength="200"/></td>
						  </tr>
						  <tr>
						    <td align="center">精通软件：</td>
						    <td colspan="7">
						    <input  class="form-control"  name="vitaeSignEntity.tiSkill" value="${vitaeSignEntity.tiSkill}" maxlength="200"/>
						    </td>
						  </tr>
						  <tr>
						    <td colspan="2" align="center">是否受过刑事处罚：</td>
						    <td colspan="2"><select name="vitaeSignEntity.sfsgxscf" class="form-control"></select></td>
						    <td colspan="2" align="center">是否有不良嗜好：</td>
						    <td colspan="2"><select name="vitaeSignEntity.sfyblsh" class="form-control" ></select></td>
						  </tr>
						  <tr>
						    <td align="center">爱好特长：</td>
						    <td colspan="7"><input class="form-control" name="vitaeSignEntity.ah" value="${vitaeSignEntity.ah}" maxlength="200" ></input></td>
						  </tr>
						  <tr>
						    <td align="center">所受奖励：</td>
						    <td colspan="7"><input class="form-control" name="vitaeSignEntity.jl" value="${vitaeSignEntity.jl}" maxlength="200" ></input></td>
						  </tr>
						  </tbody>
						</table>
						<input name="jobDetail" style="display:none"/>
						<input name="eduDetail" style="display:none"/>
						<input name="familyDetail" style="display:none"/>
						<input name="vitaeSignEntity.dz0" style="display:none"/>
						<input name="vitaeSignEntity.dz1" style="display:none"/>
						<input name="vitaeSignEntity.dz2" style="display:none"/>
	          		</div> 
	          		</form>
			     <h3 class="sub-header" style="margin-top:10px;">家庭成员</h3>
			  	<div class="form-group" id="fimailyFirstLine" style="display:block">
			    <div >
					<button  class="btn btn-primary" >新增</button>  
					<table class="tableuser" id="fimailyFirstTable">
					
					</table>
			    </div>
			    </div>
			     <h3 class="sub-header" style="margin-top:50px;display:block" >教育经历</h3>
			  	<div class="form-group" id="eduFirstLine">
			    <div >
					<button  class="btn btn-primary" >新增</button>
					<table class="tableuser" id="eduTable">
					
					</table>
			    </div>
			    </div>
			   <h3 class="sub-header" style="margin-top:50px;display:block" >工作经历</h3>
			  	<div class="form-group" id="jobFirstLine">
			    <div >
					<button  class="btn btn-primary" >新增</button>
					<table class="tableuser" id="jobTable">
					
					</table>
			    </div>
			    </div>
<!-- 			   	<button  class="btn btn-primary" style="float:left" onclick="sub()">保存</button>   -->
			   	<button  class="btn " style="float: left;margin-left:10px" id="backBtn" onclick="Javascript:history.go(-1)">返回</button>  
			  
			</div>
			     
			  	</div>
    </div>
      </div>
		<script src="/assets/js/underscore-min.js"></script>
		<script src="/assets/js/data.js"></script>
		<script src="/assets/js/layer/layer.js"></script>
	    <script>	
	    
	 	//根据 当前 form的Name input的name在 blur 之后 自动记录value 到库里
	    //根据当前 form的Name input的name 和 已经输入的value 自动弹出 相似的值
	    //  content% 优先于 %content% 取前十个
	   	var comAutoComplete=(function ($,_){
		 	var comAutoComplete=function(id){
		 		this.id=id||_.uniqueId("autoCompleteInput");
				this.$elem=null;
				this.selector=null;
				this.$wapper=null;
				this.lastQueryName=null;
		 	}
		 	comAutoComplete.prototype.render=function(selector,afterChoose){
		 		this.$elem=$(selector);
		 		this.selector=selector;
		 		this.fn_afterChoose=afterChoose;
		 		this.$elem.attr("data-id",this.id);
		 		this.$elem.attr("autocomplete","off");
		 		this.$elem.wrap("<div class='input_text1' style=\"position:relative\"></div>");
		 		this.$wapper=this.$elem.closest("div");
		 		this.$showDiv=$("<div class='text_down1 inputout1'><ul></ul></div>")
		 		this.$showDiv.appendTo(this.$wapper);
		 		this.$elem.keyup(this.textChange.bind(this));
		 		this.$elem.blur(this.signResult.bind(this));
		 		this.$elem.keydown(this.enterEvent.bind(this))
		 		return  this;
		 	}
		 	comAutoComplete.prototype.renderByParent=function($parent,selector,afterChoose){
		 		this.$elem=$parent.find(selector);
		 		this.selector=selector;
		 		this.fn_afterChoose=afterChoose;
		 		this.$elem.attr("data-id",this.id);
		 		this.$elem.attr("autocomplete","off");
		 		this.$elem.wrap("<div class='input_text1' style=\"position:relative\"></div>");
		 		this.$wapper=this.$elem.closest("div");
		 		this.$showDiv=$("<div class='text_down1 inputout1'><ul></ul></div>")
		 		this.$showDiv.appendTo(this.$wapper);
		 		this.$elem.keyup(this.textChange.bind(this));
		 		this.$elem.blur(this.signResult.bind(this));
		 		this.$elem.keydown(this.enterEvent.bind(this))
		 		return  this;
		 	}
		 	comAutoComplete.prototype.enterEvent=function(event){
		 		if(event.keyCode ==13)
		 			this.$showDiv.css("display","none")
		 	}
		 	comAutoComplete.prototype.signResult=function(){
		 		var value=this.$elem.val();
		 		var name=this.$elem.attr("name");
		 		var formName='vitaeSignTable';
		 		if(!value)return;
		 		$.ajax({
		 			url:'/administration/dic/recordContent',
		 			data:{formKey:formName,inputKey:name,key:value}
		 		})
		 	}
		 	comAutoComplete.prototype.hide=function(){
		 		this.$wapper.find("ul").parent().hide();
		 	}
		 	comAutoComplete.prototype.textChange=function(){
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
		 	comAutoComplete.prototype.queryCallback=function(data){
		 		if(!_.isArray(data))return;
		 		if(data.length==0)return;
		 		var resultHtml=_.chain(data).map(function(elem){
		 			return "<li>"+elem+"</li>";
		 		}).join("").value();
		 		if(!resultHtml)return;
		 		var $ul=this.$wapper.find("ul").empty().append(resultHtml);
		 		this.$showDiv.show();
		 		this.choose.call(this,$ul);
		 	};
		 	comAutoComplete.prototype.choose=function($ul){
		 		var this_=this;
		 		$ul.find("li").bind("click",function(event){
		 			event.stopPropagation();
		 			var value_arr=$(this).html();
		 			this_.$elem.val(value_arr);
		 			this_.$showDiv.css("display","none")
		 		})
		 	};
		 	comAutoComplete.prototype.query=function(value){
		 		var this_=this;
		 		var formName='vitaeSignTable';
		 		var inputName=this.$elem.attr("name");
		 		$.ajax({
		 			url:'/administration/dic/getContentByKey',
		 			type:'post',
		 			data:{formKey:formName,inputKey:inputName,key:value},
		 			dataType:'json',
		 			success:function(data){
		 				this_.queryCallback.call(this_,data);
		 			}
		 		});
		 	};
		 	return comAutoComplete;
		 })(jQuery,_)
		 
	    var selectData={
	    		sex:[['1','男'],['2','女']],
	    		xl:[['1',"小学"],['2',"初中"],['3',"高中"],['4',"大专"],['5',"大学"],['6','硕士'],['7','博士及以上']],
	    		zzmm:[['1',"党员"],['2',"团员"],['3',"其他"]],
	    		jkzk:[['1',"健康"],['2','良好'],['3','差']],
	    		yw:[['0',"无"],['1',"有"]],
	    		sf:[['0',"是"],['1',"否"]]
	    }
	    $(function (){
	    	
	    	
	    	
	    	var init=(function (){
	    		selectInit();
	    		bhInsert();
	    	})();
	    	
	    	//bh 
			function bhInsert(){
	    		var bh='${vitaeSignEntity.id}';
	    		var length=7-bh.length;
	    		$('#bhspan').html("&nbsp;&nbsp;&nbsp;"+("0".repeat(length))+bh);
	    	}
	    	//select初始化
	    	function selectInit(){
	    		var commonSelectInit=function(selectedValue,dataArr,$item){
	    			var resultHtml=_.reduce(dataArr,function(html,value){
	    				return html+= "<option value='"+value[0]+"' "+(value[0]==selectedValue?"selected":"")+">"+value[1]+"</option>"
	    			},"")
	    			$item.empty().append(resultHtml);
	    		}
	    		commonSelectInit('${vitaeSignEntity.xb}',selectData.sex,$('select[name="vitaeSignEntity.xb"]'));
	    		commonSelectInit('${vitaeSignEntity.mz}',mzArray,$('select[name="vitaeSignEntity.mz"]'));
	    		commonSelectInit('${vitaeSignEntity.zgxl}',selectData.xl,$('select[name="vitaeSignEntity.zgxl"]'));
	    		commonSelectInit('${vitaeSignEntity.zzmm}',selectData.zzmm,$('select[name="vitaeSignEntity.zzmm"]'));
	    		commonSelectInit('${vitaeSignEntity.jkzk}',selectData.jkzk,$('select[name="vitaeSignEntity.jkzk"]'));
	    		commonSelectInit('${vitaeSignEntity.ywsb}',selectData.yw,$('select[name="vitaeSignEntity.ywsb"]'));
	    		commonSelectInit('${vitaeSignEntity.sfsgxscf}',selectData.sf,$('select[name="vitaeSignEntity.sfsgxscf"]'));
	    		commonSelectInit('${vitaeSignEntity.sfyblsh}',selectData.sf,$('select[name="vitaeSignEntity.sfyblsh"]'));
	    	}
	    	//县市区初始化
	    	var $contentArea=$('#dz');
	        var createSelect=function(jsonContent,isCreate){
	            var firstSelectContent=_.reduce(jsonContent,function(content,value){
	                var child_content=JSON.stringify(value.child);
	                return content+='<option value='+value.code+'  data-child='+child_content+'>'+value.name+'</option>';
	            },"")
	            if(!isCreate){
	                return firstSelectContent;
	            }
	            var $select=$("<select style=\"width:130px;float:left\">"+firstSelectContent+"</select>");
	            if($contentArea.find('select').length<2){
	                 $select.change(function (){
	                     var $child;
	                     if($child=$(this).data("childSelect")){
	                         $child.empty().append(createSelect(eval($(this).find("option:selected").attr("data-child")),false))
	                         $child.trigger("change");
	                     }else{
	                        $(this).data("childSelect", createSelect(eval($(this).find("option:selected").attr("data-child")),true));
	                     }
	                })
	            }
	            $contentArea.append($select);
	            return $select;
	        }
	        var initByConfig=["${vitaeSignEntity.dz0}","${vitaeSignEntity.dz1}","${vitaeSignEntity.dz2}"];
            createSelect(eval(chinaLocation),true);
            for(var i=0,n=initByConfig.length;i<n;i++){
            	if(initByConfig[i])
               	 $contentArea.find("select:eq("+i+")").val(initByConfig[i]).trigger("change");
            	else
            	 $contentArea.find("select:eq("+i+")").find('option:eq(0)').trigger("change");
            }
//             (function (id){
//             	var $item=$('input[name="vitaeSignEntity.'+id+'"]');
//             	$item.bind("click",function (){
//             		toSelectDetail(id,$item);
//             	}).bind("keydown",function (){
//             		return false;
//             	})
//             	return arguments.callee;
//             })('wysp')
			//部分页面一开始就存在的input框的autoComplete 初始化
			var comAutoCompleteInit=(function (){
				new comAutoComplete().render('input[name="vitaeSignEntity.wysp"]')
				new comAutoComplete().render('input[name="vitaeSignEntity.tiSkill"]')				
			})();

            //一些按钮的初始化
            $('#jobFirstLine').find("button").click(function (){
            	var insertHtml="<tr>"+
				'<td  width="13%">开始时间</td>'+
			    '<td width="20%">'+
			    	'<input name="jobstartTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd \'})" >'+
			    '</td>'+
				'<td  width="13%">结束时间</td>'+
			    '<td width="20%">'+
			    	'<input name="jobendTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd \'})" >'+
			    '</td>'+
			    '<td  width="13%">工作单位</label>'+
			    '<td width="20%" colspan=\'2\'> '+
			    	'<input name="jobcompanyName" class="form-control" required="required">'+
			    '</td>'+
				'</tr>'+
				"<tr>"+
				'<td  >公司规模(人)</td>'+
			    '<td >'+
			    	'<input type="number" min=1 name="jobguimo" class="form-control" required="required"  >'+
			    '</td>'+
			    '<td >薪水待遇</label>'+
			    '<td  > '+
			    	'<input  type="number" min=1  name="jobdaiyu" class="form-control" required="required">'+
			    '</td>'+
				'<td  >所在岗位</td>'+
			    '<td  colspan=\'2\' > '+
			    	'<input name="jobposition" class="form-control" required="required">'+
			    '</td>'+
				'</tr>'+
				"<tr>"+
				'<td  >是否工银行流水</td>'+
			    '<td >'+
			    	'<select name="jobsftgyhls" class="form-control" required="required" ><option value="0" >否</option><option value="1" > 是</option></select>'+
			    '</td>'+
			    '<td  >证明人及其电话</label>'+
			    '<td colspan="4"> '+
			    	'<input name="jobzmrdh" class="form-control" required="required">'+
			    '</td>'+
				'</tr>'+
				'<tr>'+
				'<td>离职原因</td>'+
				'<td colspan=\'2\' >'+
		    	'<input name="joblzyy" class="form-control" required="required">'+
				'</td>'+
				'<td>是否提供离职证明</td>'+
				'<td colspan=\'2\' >'+
		    	'<select name="jobsftglzzm" class="form-control" required="required" ><option value="0" >否</option><option value="1" > 是</option></select>'+
				'</td>'+
				'<td><button  class="btn btn-primary" >删除</button></td>'
				'</tr>'
				;
            	if($('#jobTable').find("tr").length>0){
            		//第二行开始 不需要 银行流水 和 离职证明
            		insertHtml="<tr>"+
    				'<td  width="13%">开始时间</td>'+
    			    '<td width="20%">'+
    			    	'<input name="jobstartTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" >'+
    			    '</td>'+
    				'<td  width="13%">结束时间</td>'+
    			    '<td width="20%">'+
    			    	'<input name="jobendTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" >'+
    			    '</td>'+
    			    '<td  width="13%">工作单位</label>'+
    			    '<td width="20%" colspan=\'2\'> '+
    			    	'<input name="jobcompanyName" class="form-control" required="required">'+
    			    '</td>'+
    				'</tr>'+
    				"<tr>"+
    				'<td  >公司规模(人)</td>'+
    			    '<td >'+
    			    	'<input type="number" min=1 name="jobguimo" class="form-control" required="required"  >'+
    			    '</td>'+
    			    '<td >薪水待遇(月)</label>'+
    			    '<td  > '+
    			    	'<input  type="number" min=1  name="jobdaiyu" class="form-control" required="required">'+
    			    '</td>'+
    				'<td  >所在岗位</td>'+
    			    '<td  colspan=\'2\' > '+
    			    	'<input name="jobposition" class="form-control" required="required">'+
    			    '</td>'+
    				'</tr>'+
    				"<tr>"+
    			    '<td  >证明人及其电话</label>'+
    			    '<td colspan="6"> '+
    			    	'<input name="jobzmrdh" class="form-control" required="required">'+
    			    '</td>'+
    				'</tr>'+
    				'<tr>'+
    				'<td>离职原因</td>'+
    				'<td colspan=\'5\' >'+
    		    	'<input name="joblzyy" class="form-control" required="required">'+
    				'</td>'+
    				'<td><button  class="btn btn-primary" >删除</button></td>'
    				'</tr>';
            	}
				var $insertItem=$(insertHtml);
				$insertItem.find('button').click(function (){
					var trObj = $(this).parent().parent();
					if($(trObj).next().length>0){
        				layer.alert("工作经历需要从最后一条记录开始删除!",{offset:'100px'});
						return;
					}

					$insertItem.remove();
				})
				var $zyInput=$('input[name="jobposition"]',$insertItem);
				//专业点击的选择事件
				$zyInput.bind("click",function (){
            		toSelectDetail(null,$(this));
            	}).bind("keydown",function (){
            		return false;
            	})
            	//职位autoComplete绑定
            	var autoCompleteBind=(function ($insertItem){
					new comAutoComplete().renderByParent($insertItem,'input[name="jobcompanyName"]')
				})($insertItem)
				$insertItem	.appendTo($('#jobTable'));
            });
            $('#eduFirstLine').find("button").click(function (){
            	var insertHtml="<tr>"+
				'<td  width="10%">开始时间</td>'+
			    '<td width="10%">'+
			    	'<input name="edustartTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" >'+
			    '</td>'+
				'<td  width="10%">结束时间</td>'+
			    '<td width="10%">'+
			    	'<input name="eduendTime" class="form-control" required="required" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\'})">'+
			    '</td>'+
			    '<td  width="10%">毕业院校</label>'+
			    '<td width="10%">'+
			    	'<input name="eduschoolName" class="form-control" required="required">'+
			    '</td>'+
		   	 '</tr>'+
		   	 '<tr>'+
		     '<td  width="10%">学历</label>'+
			    '<td width="10%">'+
			    	'<input name="eduxueli" class="form-control" >'+
			    '</td>'+
			     '<td  width="10%">专业</td>'+
			    '<td width="10%" colspan="2" >'+
			    	'<input name="eduzhuanye" class="form-control" >'+
			    '</td>'+
			    '<td width="10%" colspan="1">'+
		    	'<button  class="btn btn-primary"   >删除</button>'+
		   	 '</td>'+
				'</tr>';
				var $insertItem=$(insertHtml);
				$insertItem.find('button').click(function (){
					$insertItem.remove();
				})
				var autoCompleteBind=(function ($insertItem){
					new comAutoComplete().renderByParent($insertItem,'input[name="eduschoolName"]')
					new comAutoComplete().renderByParent($insertItem,'input[name="eduxueli"]')		
				})($insertItem)
				var $zyInput=$('input[name="eduzhuanye"]',$insertItem);
				//专业点击的选择事件
				$zyInput.bind("click",function (){
					//现在一共就2个 用弹出方式选择 一个是专业 一个是职业  
            		toSelectDetail('zy',$(this));
            	}).bind("keydown",function (){
            		return false;
            	})
				
				$insertItem	.appendTo($('#eduTable'));
            });
            
            $('#fimailyFirstLine').find("button").click(function (){
            	var insertHtml="<tr>"+
				'<td  width="10%">姓名</td>'+
			    '<td width="20%">'+
			    	'<input name="familyname" class="form-control" required="required">'+
			    '</td>'+
				'<td  width="10%">关系</td>'+
			    '<td width="20%">'+
			    	'<input name="familyrelationShip" class="form-control" required="required">'+
			    '</td>'+
			    '<td  width="10%">联系方式</label>'+
			    '<td width="20%">'+
			    	'<input name="familytelephone" class="form-control" required="required">'+
			    '</td>'+
			    '<td width="10%">'+
			    	'<button  class="btn btn-primary"  >删除</button>'+
			    '</td>'+
				'</tr>';
				var $insertItem=$(insertHtml);
				$insertItem.find('button').click(function (){
					$insertItem.remove();
				})
				var autoCompleteBind=(function ($insertItem){
					new comAutoComplete().renderByParent($insertItem,'input[name="familyrelationShip"]')
				})($insertItem)
				$insertItem	.appendTo($('#fimailyFirstTable'));
            });
            
            //全局点击事件    
            $('body').click(function (){
				$('.inputout1').css("display","none");
			})
			//三个按钮 第一次生成一条
// 			 $('#jobFirstLine').find("button").trigger("click");
// 			 $('#eduFirstLine').find("button").trigger("click");
// 			 $('#fimailyFirstLine').find("button").trigger("click");
			<c:forEach items="${vitaeSignEduEntities}" varStatus="index" var="item">
				 $('#eduFirstLine').find("button:eq(0)").trigger("click");
					var $eduDiv=$('#eduFirstLine');
					(function (value,name){
						$eduDiv.find("input[name='edu"+name+"']:last").val(value);
						return arguments.callee
					})('${item.startTime}','startTime')('${item.endTime}','endTime')('${item.schoolName}','schoolName')('${item.zhuanye}','zhuanye')('${item.xueli}','xueli')
			</c:forEach>
			<c:forEach items="${vitaeSignJobHistoryEntities}" varStatus="index" var="item">
				 $('#jobFirstLine').find("button:eq(0)").trigger("click");
					var $eduDiv=$('#jobFirstLine');
					(function (value,name){
						$eduDiv.find("input[name='job"+name+"']:last").val(value);
						return arguments.callee
					})('${item.startTime}','startTime')('${item.endTime}','endTime')('${item.companyName}','companyName')('${item.guimo}','guimo')('${item.daiyu}','daiyu')('${item.position}','position')('${item.zmrdh}','zmrdh')('${item.lzyy}','lzyy');
					(function (value,name){
						$eduDiv.find("select[name='job"+name+"']:last").val(value);
						return arguments.callee
					})('${item.sftgyhls}','sftgyhls')('${item.sftglzzm}','sftglzzm')
					</c:forEach>
			<c:forEach items="${vitaeSignFamilyEntities}" varStatus="index" var="item">
				$('#fimailyFirstLine').find("button:eq(0)").trigger("click");
				  var $eduDiv=$('#fimailyFirstLine');
				  (function (value,name){
						$eduDiv.find("input[name='family"+name+"']:last").val(value);
						return arguments.callee
					})('${item.name}','name')('${item.relationShip}','relationShip')('${item.telephone}','telephone')
			</c:forEach>
	    })
	    var toSelectDetail=function (type,$item){
	    	localStorage.selectDetailType=type;
	    	localStorage.selectDetailValue=$item.val();
	    	localStorage.returnValue=null;
	    	layer.open({
	  		  type: 2,
	  		  title: '请选择',
	  		  shadeClose: true,
	  		  shade: 0.8,
	  			offset: '100px',
	  		  area: ['842px', '82%'],
	  		  content: '/HR/vitae/chooseDetail?type='+type ,
	  		  end:function(){
	  				var value=localStorage.returnValue;
	  				if(value!=null)
	  					$item.val(value);
	  		  }
	  		}); 
	    }
	   	//下面三个大框数据转成json格式 方便后台处理保存
	 	var dataCollect=function (){
	   		var familyDataCollection=(function (){
	   			var result=[];
	   			$('#fimailyFirstLine').find("table tr").each(function (){
	   				var currentResult={};
		 			var $inputs=$(this).find('input');
		 			$inputs.each(function (){
		 				var name=$(this).attr("name");
		 				if(name&&(~name.indexOf("family"))){
		 					name=name.substring(6,name.length);
		 					currentResult[name]=$(this).val();
		 				}
		 			})
		 			result.push(currentResult);
		 		})
		 		return result;
	   		})();
	   		$('input[name="familyDetail"]').val(JSON.stringify(familyDataCollection))
	   		var jobDataCollection=(function (){
	   			var result=[];
	   			var currentResult={};
	   			$('#jobFirstLine').find("table tr").each(function (index){
		 			var $inputs=$(this).find('input');
		 			$inputs.each(function (){
		 				var name=$(this).attr("name");
		 				if(name&&(~name.indexOf("job"))){
		 					name=name.substring(3,name.length);
		 					currentResult[name]=$(this).val();
		 				}
		 			})
		 			var $selects=$(this).find('select');
		 			$selects.each(function (){
		 				var name=$(this).attr("name");
		 				if(name&&(~name.indexOf("job"))){
		 					name=name.substring(3,name.length);
		 					currentResult[name]=$(this).val();
		 				}
		 			})
		 			//每4个trpush 一次
		 			if(index%4==3){
			 			result.push(currentResult);
			 			currentResult={};
		 			}
		 		})
		 		return result;
	   		})();
	   		var eduDataCollection=(function (){
	   			var result=[];
	   			var currentResult={};
	   			$('#eduFirstLine').find("table tr").each(function (index){
		 			var $inputs=$(this).find('input');
		 			$inputs.each(function (){
		 				var name=$(this).attr("name");
		 				if(name&&(~name.indexOf("edu"))){
		 					name=name.substring(3,name.length);
		 					currentResult[name]=$(this).val();
		 				}
		 			})
		 			//每2个trpush 一次
		 			if(index%2==1){
			 			result.push(currentResult);
			 			currentResult={};
		 			}
		 		})
		 		return result;
	   		})();
	   		$('input[name="eduDetail"]').val(JSON.stringify(eduDataCollection))
	   		$('input[name="jobDetail"]').val(JSON.stringify(jobDataCollection))
	   		//省市区的保存
	   		var $dzDiv=$('#dz');
	   		$('input[name="vitaeSignEntity.dz0"]').val($dzDiv.find('select:eq(0)').val())
	   		$('input[name="vitaeSignEntity.dz1"]').val($dzDiv.find('select:eq(1)').val())
	   		$('input[name="vitaeSignEntity.dz2"]').val($dzDiv.find('select:eq(2)').val())
	   		return true;
	 	}
	   	var sub=function (){
	   		$('#form_').submit();
	   	}
	   	$(function(){
	   		$('button').attr("disabled","disabled");
	   		$('input').attr("disabled","disabled");
	   		$('select').attr("disabled","disabled");
	   		$('#backBtn').removeAttr("disabled");
	   	})
	    </script>
  </body>
</html>
