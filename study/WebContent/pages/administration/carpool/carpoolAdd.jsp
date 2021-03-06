<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
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
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'carpoolList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/carpool/save_saveCarpool" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">拼车记录编辑</h3> 			 
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">所属部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffQueryVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.staffQueryVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			    <c:if test="${not empty departmentVOs}">
			    <c:if test="${not empty selectedDepartmentIDs}">
			    <s:set name="departmentClass" value="'col-sm-2'"></s:set>
			    <s:set name="parent" value="0"></s:set>
			    <s:iterator id="selectedDepartmentID" value="#request.selectedDepartmentIDs" status="st">
			    	<s:set name="level" value="#st.index+1"></s:set>
			    	<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
			    	<s:set name="departmentClass" value="#departmentClass+' department'+#level"></s:set>
			    	<s:set name="hasNextLevel" value="'false'"></s:set>
			    	<div class="<s:property value='#departmentClass'/>" id="department<s:property value='#level'/>_div" >
			    		<select class="form-control" id="department<s:property value='#level'/>" onchange="showDepartment(this, <s:property value='#level'/>)">
			    			<option value="">--<s:property value="#level"/>级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.parentID == #parent">
			    				<option value="<s:property value='#department.departmentID'/>" <s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			<s:if test="#department.parentID == #selectedDepartmentID"><s:set name="hasNextLevel" value="'true'"></s:set></s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    	<s:set name="parent" value="#selectedDepartmentID"></s:set>
			    </s:iterator>
			    <input type="hidden" id="departmentLevel" value="<s:property value='#level'/>"/>
			    <s:if test="#hasNextLevel == 'true'">
				    <s:set name="index" value="#level+1"></s:set>
				    <div class="<s:property value="#departmentClass+' department'+#index"/>" id="department<s:property value='#index'/>_div" >
			    		<select class="form-control" id="department<s:property value='#index'/>" onchange="showDepartment(this, <s:property value='#index'/>)">
			    			<option value="">--<s:property value="#index"/>级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.parentID == #selectDepartmentID">
			    				<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    </s:if>
			    </c:if>
			    <c:if test="${empty selectedDepartmentIDs}">
			    	<div class="col-sm-2 department1" id="department1_div" >
			    		<select class="form-control" id="department1" onchange="showDepartment(this, 1)">
			    			<option value="">--1级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.level == 1">
			    				<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    </c:if>
			    </c:if>
			</div>
			   <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">开始时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="startTime" value="${carpoolVo.startTime}"name="carpoolEntity.StartTime" required="required" onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label">结束时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="endTime" value="${carpoolVo.endTime}" name="carpoolEntity.EndTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">出发地点</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  value="${carpoolVo.startPlace }" name="carpoolEntity.StartPlace" required="required" ></div>
			 	<label for="reason" class="col-sm-1 control-label">结束地点</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" value="${carpoolVo.endPlace }" name="carpoolEntity.EndPlace" required="required" ></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">备注</label>
			  	<div class="col-sm-5"><input type="text" class="form-control"  value="${carpoolVo.remarks }"name="carpoolEntity.Remarks" ></div>

			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">拼车人员</label>
 			  	<div class="col-sm-2">
				    <input type="button" id="insertBtn" class="btn btn-primary" value="新增"></input>
				 </div>
			  </div>
			  <input style="display:none" name="carpoolEntity.PersonDetail" />
			   <input style="display:none" name="carpoolEntity.Dept_Id" />
			   <input style="display:none" name="carpoolEntity.Company_Id" />
			   <!--更新页面默认数据 -->
   			   <input style="display:none" name="carpoolEntity.Carpool_Id" value="${carpoolVo.carpool_Id}" />
   			   <input style="display:none" name="carpoolEntity.IsDeleted" value="${carpoolVo.isDeleted}" />
   			   <input style="display:none" name="carpoolEntity.AddTime" value="${carpoolVo.addTime}" />
   			   
   			   <input style="display:none" id="old_companyId" value="${carpoolVo.company_Id}" />
   			   <input style="display:none" id="old_departmentId" value="${carpoolVo.dept_Id}" />
			   
			  <h2 class="sub-header"></h2>
			  <div >
            <table class="table table-striped"  style="min-height:100px">
              <thead>
                <tr>
                  <th class="col-sm-2">姓名</th>
                  <th class="col-sm-1">公司</th>
                  <th class="col-sm-1">部门</th>
                  <th class="col-sm-1">职务</th>
                  <th class="col-sm-1">是否是司机</th>
                  <th class="col-sm-1">操作</th>            
                </tr>
              </thead>
               <tbody id="tbody_">
               
               </tbody>
            </table>
          </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script>
    /*
    	每次都要写太麻烦 封装成一段js    用法 写一个 input 外面包个div    
    	结果从这个div 下找 input[name="name"] 和 input[name="id"]  input[name="detail"]  的值
    */
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
  
    
    /**
    	部门选择的方法
    */
    function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "staffQueryVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "staffQueryVO.departmentID");
		}
		$.ajax({
			url:'HR/staff/findDepartmentsByCompanyIDParentID',
			type:'post',
			data:{companyID: $("#company").val(),
				  parentID: parentID},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (level == 1) {
						window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						return;
					}
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
    
    
    var init=+function(){
    	
    	$('#insertBtn').click(function (){
			var id=_.uniqueId("tr");
			var tr="<tr id=\""+id+"\" style=\"height:50px\"><td style=\"height:34px\"><div class=\"col-sm-5 inputout1\"><input  class=\"form-control\"/></div></td><td></td><td></td><td></td><td><select><option value=\"0\">否</option><option value=\"1\">是</option></select></td><td><input type=\"button\"  class=\"btn btn-primary\" value=\"删除\"/></td></tr>"
			var $tr=$(tr);
			$('#tbody_').append($tr);
			 		
			var bindNow =new staffInputBind();
			var afterChoose=function (){
				var $tr=$('#'+id);
				var extraMsg=$('input[name="detail"]',$tr).val();
				if(extraMsg&&(extraMsg=extraMsg.match(/\（(.*)\）/)[1].split("—")))
					for(var i=0;i<3;i++){
						$tr.find("td:eq("+(i+1)+")").html(extraMsg[i]);
					}
			}
			bindNow.render('#'+id+'>td:eq(0)>div>input',afterChoose);
			$('body').click(function(){
				bindNow.hide();
			})
			$('#'+id+'>td:eq(5)>input').click(function(){
				$tr.remove();
			});
    	})
    	
    	
    	//更新时候需要的一些初始化操作
		if('${carpoolVo}'){
			$('#form_').attr("action","administration/carpool/save_updateCarpool");
	    	$('#company').val('${carpoolVo.company_Id}');
			//初始化下面的拼车人员列表
			var data=[];
	  		<c:forEach items="${carpoolVo.personDetail_}" var= "item"  >
	  			data.push(['${item[0]}','${item[1]}','${item[2]}','${item[3]}','${item[4]}'])
			</c:forEach>
	  		for(var i=0,length=data.length;i<length;i++){
	  			
	  			$('#insertBtn').trigger("click");
	  			var $itemtr=$('#tbody_').find("tr:eq("+i+")");
	  			var $td0=$itemtr.find("td:eq(0)");
	  			$td0.find("input[name='id']").val(data[i][0]);
	  			$td0.find("input[name='name']").val(data[i][1]);
	  			$td0.find("input[class='form-control']").val(data[i][1]);
	  			$itemtr.find("td:eq(1)").html(data[i][2]);
	  			$itemtr.find("td:eq(2)").html(data[i][3]);
	  			$itemtr.find("td:eq(3)").html(data[i][4]);
				if(i==0){
					$itemtr.find("td:eq(4)").find("select").val(1);
	  			}
	  		}
		}
    	var getPersonData=function(){
    		//部门是否选择
    		var $depts=[$('#department1'),$('#department2'),$('#department3')];
   			var  deptId=_.chain($depts)
   			.filter(function($item){
   				return !!$item.length;
   			}).map(function($item){
   				return $item.val();
   			}).filter(function(value){
   				return !!value;
   			}).last().value();
   			var companyId=$('#company').val();
   			if('${carpoolVo}'){
   				companyId=companyId||$('#old_companyId').val();
   				deptId=deptId||$('#old_departmentId').val();
   			}
   			if(!companyId){
   				alert("请选择公司!");
    			return false;
   			}else if( !deptId){
   				alert("请选择部门!");
    			return false;
   			}
   			
   			else{
	       		$('input[name="carpoolEntity.Company_Id"]').val(companyId)
       			$('input[name="carpoolEntity.Dept_Id"]').val(deptId);
   			}
       			
    		
    		//人员数据是否有效
    		var data=[];
    		var resultMap={};
    		$('#tbody_ tr').each(function(){
    			var id=$(this).find("input[name='id']").val();
    			var isDriver=$(this).find("select").val();
    			if(id&&!resultMap[id]){
    				data.push([id,isDriver]);
    				resultMap[id]=true;    				
    			}
    		});
    		if(data.length==0){
    			alert("请填写拼车人员数据");
    			return false;
    		}
    		var detail=_.groupBy(data,function(d){
    			return d[1]+"_";
    		})
    		if(!detail['1_']){
    			alert("至少应该有一名驾驶人员");
    			return false;
    		}
    		if(detail['1_'].length>1){
    			alert("驾驶人员最多只有一名");
    			return false;
    		}
    		detail['0_']=detail['0_']||[];
    		detail['0_'].unshift(detail['1_'][0]);
    		return detail['0_'];
    	};
		$('#form_').submit(function(){
    		var personDetails=getPersonData();
    		if(!personDetails)return false;
    		$('input[name="carpoolEntity.PersonDetail"]').val(_.chain(personDetails).map(function(detail){
    			return detail[0];
    		}).join(",").value());
    		return true;
    	})
    	
    }();
    </script>
    
    	
  		
</body>
</html>