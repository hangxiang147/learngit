
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
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
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	 table{width:100%;border-collapse:collapse;}
	 table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	 table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	 table tr .black {background:#efefef;text-align:center;color:#000;}
	 table tbody tr td p{padding:4px 0px;}
	 table tbody tr td li{list-style:none;text-align: left;}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" id="form_" action="/HR/vitae/save_startVitae" method="post" enctype="multipart/form-data">
            <s:token></s:token>
          	<input style="display:none" name="vitaeVo.userID" value="${staff.userID }"/>
          	<input style="display:none" name="vitaeVo.requestUserId"  value="${staff.userID }" />
          	<input style="display:none" name="vitaeVo.userName" value="${staff.lastName }"/>
          	<input style="display:none" name="vitaeVo.requestUserName" value="${staff.lastName }" />
          	<input style="display:none" name="vitaeVo.postCompanyId"/>
          	<input style="display:none" name="vitaeVo.postCompanyName"/>
          	<input style="display:none" name="vitaeVo.postDepartementId"/>
          	<input style="display:none" name="vitaeVo.postDepartmentName"/>
          	<input style="display:none" name="vitaeVo.postPositionId"/>
          	<input style="display:none" name="vitaeVo.postPositionName"/>
          	<h3 class="sub-header" style="margin-top:0px;">提出招聘需求</h3>
          	<div class="form-group">
			    <label for="company" class="col-sm-1 control-label">应聘者岗位&nbsp;</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffVO.companyID" onchange="showChild(this, 0)" required="required">
				      <option value="">--公司--</option>
				      		<option value="1" data-code="QA">智造链骑岸总部</option>
				    
				      		<option value="2" data-code="RD">智造链如东迷丝茉分部</option>
				      	
				      		<option value="3" data-code="NT">智造链南通分部</option>
				      	
				      		<option value="4" data-code="GZ">智造链广州亦酷亦雅分部</option>
				      	
				      		<option value="5" data-code="NJ">智造链南京分部</option>
				      		
				      						      		<option value="6" data-code="FS">智造链佛山迷丝茉分部</option>
				      		
					</select>
			    </div>
			  </div>
			<div class="form-group"  >
				<input style="display:none" name="type" value=1/>
				<label for="name" class="col-sm-1 control-label">应聘者职务</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="position" name="staffVO.positionID" required="required">
				      <option value="">--职务--</option>
					</select>
			    </div>
			</div>
			<div class="form-group"  >
			 <label for="name" class="col-sm-1 control-label">应聘岗位全称</label>
				<div class="col-sm-2">
			    	<input class="form-control" maxlength="50" required="required" name="vitaeVo.postName"  autocomplete="off"/>
			    </div>
			     <label for="name" class="col-sm-1 control-label">需求人员数</label>
				<div class="col-sm-2">
			    	<input class="form-control" type="number" min=1 required="required" name="vitaeVo.neddPersonNumber"  autocomplete="off"/>
			    </div>
			</div>
			<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label">岗位要求技能<span id="remainWordsNumber" style="color:red"></span></label>
			    <div class="col-sm-5">
			    	<textarea  maxlength="1000"  required="required" name="vitaeVo.needPersonDescription"  data-provide="markdown" rows='7' ></textarea>
			    </div>
			</div>
			<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label">申请原因</label>
			    <div class="col-sm-2" id="reasonDiv">
			    	<input class="form-control" maxlength="50" required="required" name="vitaeVo.reason"  autocomplete="off"/>
			    </div>
			</div>
			<div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
			    <div class="col-sm-5">
					<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />
					<input name="fileDetail" id="fileDetail" style="display:none"/>			    
				</div>
			  </div>
		
			<div class="form-group">
			    <button type="submit"  class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
          </form>

        </div>
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/util.js"></script>
    <script src="/assets/js/dic.js"></script>
    <script src="/assets/js/textarea/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script src="/assets/js/textarea/marked.js"></script>
    <script src="/assets/js/layer/layer.js"></script>
    <script>
     marked.setOptions({
         highlight: function (code) {
             return hljs.highlightAuto(code).value;
         }
     });
    </script>
    <script src="/assets/js/textarea/to-markdown.js"></script>
    <script src="/assets/js/textarea/bootstrap-markdown.js"></script>
    <script src="/assets/js/textarea/jquery.textcomplete.js"></script>
    <script>  
    var tableCreator=(function($,_){
		//根据data生成一个表格  MarkKey表示需要markdown的关键词  
		// 一般是根据 内容来生成表格  
		// 一般的 一行应该有三个 key value
		// 但是 如果 value 超过 10个字 那么 应该占2个位置
		// 如果超过 30个字 应该占 3个位置
		// 如果是 需要markdown的关键词  无论多少 独占一行
		var tableCreator=function(data,markKey){
			this.data=data;
			this.markKey=markKey;
			//为了方便 最后的处理  先将数据保存到一个数组里 
			//因为存在如下情况 假如 一个元素 占2行  下个元素 也占两行 那么 我需要 另起一行
			// 在生成内容 时 假如一个元素 只要占 2行 但是 当前line 不存在接下来的元素 那么 要将 标记为站行的字段 标记成3行
			this.resultTds=[];
		}
		tableCreator.prototype.create=function($parent){
			var currentLineIndex=0;
			midDataCreate.call(this);
			handleMidData.call(this);
			return htmlCreate.call(this,$parent);
		}
		//生成中间数组
		var midDataCreate=function(){
			this.resultTds.length=0;
			// 空余的量=6-currentLinkExsitNumber;
			var currentLinkExsitNumber=0;
			var currentArr=[];
			_.each(this.data,function(value,index){
				var key=value[0];
				var value=$.trim(value[1]);
				//小于 15个字 或者 为空 默认 占2个
				var currentItemNeedNumber=2;
				if(value){
					if(value.length>30){
						currentItemNeedNumber=6;
					}else if(value.length>10){
						currentItemNeedNumber=4;
					}
				}
				//如果是markdown字段 无论多少 独占一行
				if(this.markKey&&this.markKey.length>0){
					//如果是 markdown字段
					if(_.contains(this.markKey,key)){
						//设置为最大的 6  key 1 value 5
						currentItemNeedNumber=6;
					}
				}
				if(6-currentLinkExsitNumber>=currentItemNeedNumber){
					//可以容纳
					currentArr.push({size:currentItemNeedNumber,key:key,value:value});
					currentLinkExsitNumber+=currentItemNeedNumber;
				}else{
					//不可以容纳
					//push上个
					this.resultTds.push(currentArr);
					currentArr=[];
					currentArr.push({size:currentItemNeedNumber,key:key,value:value});
					currentLinkExsitNumber=currentItemNeedNumber;
				}
				//如果是最后 一次 那么 push最后的值
				if(index==this.data.length-1){
					this.resultTds.push(currentArr);
				}
			}.bind(this))
		}
		//处理中间数组 根据实际情况 修改 站行 关键词
		var handleMidData=function(){
			this.resultTds=_.map(this.resultTds,function(value){
				var sumSize=0;
				for(var i=0,n=value.length;i<n;i++){
					sumSize+=value[i].size;
				}
				if(sumSize<6){
					var lastItem=value[value.length-1];
					lastItem.size+=6-sumSize;
				}
				return value;
			});
		}
		//根据 处理好的  td数组 生成 最终的html
		var htmlCreate=function($parent){
			var this_=this;
			var resultHtml=_.map(this.resultTds,function(value){
				var innerContent="";
				for(var i=0,n=value.length;i<n;i++){
					var descriptionId="";
					if(value[i].size==6){
						//如果是markdown字段 无论多少 独占一行
						if(this_.markKey&&this_.markKey.length>0){
							//如果是 markdown字段
							if(_.contains(this_.markKey,value[i].key)){
								innerContent+="<td class=\"black\" style=\"width:100px\">"+value[i].key+"</td><td colspan="+(value[i].size-1)+" style=\"text-align:left;max-width:1080px\">"+ marked(value[i].value)+"</td>";
								continue;
							}
						}
					}
					innerContent+="<td class=\"black\" style=\"width:100px\" >"+value[i].key+"</td><td colspan="+(value[i].size-1)+">"+value[i].value+"</td>";
				}
				return "<tr>"+innerContent+"</tr>";
			}).join("");
			if($parent)
				$parent.append("<table >"+resultHtml+"</table>");
			return "<table >"+resultHtml+"</table>";
		}
		return tableCreator;
	})($,_)
    
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
	 			var index;
	 			if(~(index=$(this).html().indexOf("<input"))){
	 				var extraMsg=$(this).html().substring(0,index);
	 				this_.$wapper.find("input[name='detail']").val(extraMsg)		
	 			}
	 		 	this_.$elem.val(value_arr[1]);
	 		 	this_.lastQueryId=value_arr[0];
	 			this_.lastQueryName=value_arr[1];
	 			this_.$wapper.find("ul").parent().hide();
	 			if(this_.fn_afterChoose){
	 				this_.fn_afterChoose.call(this_);
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
    	var  staffInputInit=(function (){
    		new staffInputBind().render("#nameInput",function (){
    			$('input[name="keyId"]').val(this.lastQueryId);
    		});
    	})();
    	/* new dicHelper(dicContent.vitaeReson).render($('#reasonDiv'),'form-control',function (){
 			this.$item.prop("name","vitaeVo.reason").prop("required","required");
 		}); */
    	var $textareaItem=$('textarea[name="vitaeVo.needPersonDescription"]');
     	var textareaInit=(function ($item){
  		  $('#comment-area').textcomplete([
                 {
                     mentions: ['admin','Devops','ly','root_root','ly','gonglexin','ly','EricGuo','ly','steve','ly','liuxey','ly','axlrose','unix','ly','ly','newbee','ly','gaicitadie','ly','gazeldx','ly','jthmath','ly','ly','yugo','ly','lxy254069025','ly','Arata','hades3264331136','itfanr','itfanr','ly','ly'],
                     match: /\B@(\w*)$/,
                     search: function (term, callback) {
                         callback($.map(this.mentions, function (mention) {
                             return mention.indexOf(term) === 0 ? mention : null;
                         }));
                     },
                     index: 1,
                     replace: function (mention) {
                         return '@' + mention + ' ';
                     }
                 }
             ]);
  		})($textareaItem);
    	var $showNumberSpan=$('#remainWordsNumber');
    	$textareaItem.bind("keyup",function (){
    		var content=$textareaItem.val()+'';
    		if(content.length<50){
    			$showNumberSpan.empty().append("还需填写"+(50-content.length)+"字");
    		}else{
    			$showNumberSpan.empty();
    		}
    	});
    	var insertBtnBind=(function (){
    		$('#insertBtn').click(function (){
    			var companyId=$('select[name="staffVO.companyID"]').val();
    			var deptId=$('select[id^="department"]:last').val();
    			var position=$('#position').val()
    			if(!deptId){
    				alert("请选择部门！");
    				return false;
    			}
    			if(!position){
    				alert("请选择职位！");
    				return false;
    			}
				$.ajax({
					url:'/administration/right/getGroupIdByKeys',
					data:{c_id:companyId,d_id:deptId,p_id:position},
					success:function (data){
						console.log(data);
						if(!data||!data.id){
							alert("该职位下没有添加过人员！");
						}else{
							$.ajax({
			    				url:'/administration/right/createMemberShip',
			    				data:{rightId:$('#rightId').val(),type:2,keyId:data.id},
			    				success:function (){
			    					location.reload();
			    				}
			    			})	
						}
					}
				})
    		})
    	})();
    	set_href();
    })
    function showPosition(departmentID) {
		$.ajax({
			url: 'HR/staff/findPositionsByDepartmentID',
			type: 'post',
			data: {departmentID: departmentID},
			dataType: 'json',
			success: function(data) {
				$.each(data.positionVOs, function(i, position) {
					$("#position").append("<option value='"+position.positionID+"'>"+position.positionName+"</option>");
				});
			}
		});
	}
    function showChild(obj, level) {
		$("#position option").each(function(i, optionObj) {
			if (i != 0) {
				$(optionObj).remove();
			}
		});
		
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "staffVO.departmentID");
				showPosition($("#department"+(level-2)).val());
			}
			return;
		}

		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "staffVO.departmentID");
			showPosition(parentID);
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
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} 
					return;
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\" required=\"required\">"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
		
	}
    $('#form_').submit(function (){

    	var $textareaItem=$('textarea[name="vitaeVo.needPersonDescription"]');
    		if(($textareaItem.val()+'').length<50){
			layer.alert('岗位要求技能必须填写50字以上!',function (){
					$textareaItem.focus();
					layer.closeAll();
				});
				return false;
			}
			var checkFileResult_boolean=(function ($item,maxSize_u32,eachMaxSize){
			var isPass_boolean=true;
   			var files_arr=$item[0].files;
   			var sumSize_u32=0;
   			var result_arr=_.reduce(files_arr,function (result_arr,value_obj){
   				var fileName_string=value_obj.name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
   				var suffix_string=fileName_string.replace(/.*\.(.*)/,"$1");
   				suffix_string=(suffix_string+"").toLowerCase();
   				result_arr.push([fileName_string,suffix_string]);
   				sumSize_u32+=value_obj.size;
   				if(value_obj.size>eachMaxSize){
   					layer.alert(commonUtils.replace_fn("上传单个文件大小不能超过%sM",eachMaxSize>>>20),function (){
    					layer.closeAll();
    				});
   					return isPass_boolean=false;
   				}
   				if(sumSize_u32>maxSize_u32){
   					layer.alert(commonUtils.replace_fn("上传文件大小不能超过%sM",maxSize_u32>>>20),function (){
    					layer.closeAll();
    				});
   					return isPass_boolean=false;
   				}
   				return  result_arr;
   			},[]);
   			if(isPass_boolean)
   				$('#fileDetail').val(JSON.stringify(result_arr));
   			return isPass_boolean;
   		})($('#attachment'),20<<20,5<<20)
   		if(!checkFileResult_boolean)return checkFileResult_boolean;
   		var companyId=$('select[name="staffVO.companyID"]').val();
   		var companyName=$('select[name="staffVO.companyID"]').find("option:selected").html();
		var deptId=$('select[id^="department"]:last').val();
   		var departmentName=$('select[id^="department"]:last').find("option:selected").html();
		var position=$('#position').val()
		var positionName=$('#position').find("option:selected").html();
		$('input[name="vitaeVo.postCompanyId"]').val(companyId);
		$('input[name="vitaeVo.postCompanyName"]').val(companyName);
		$('input[name="vitaeVo.postDepartementId"]').val(deptId);
		$('input[name="vitaeVo.postDepartmentName"]').val(departmentName);
		$('input[name="vitaeVo.postPositionId"]').val(position);
		$('input[name="vitaeVo.postPositionName"]').val(positionName);
		var getData=function(){
			return [['公司名称',companyName],
			        ['部门名称',departmentName],
			        ['职位名称',positionName],
			        ['应聘岗位全称',$('input[name="vitaeVo.postName"]').val()],
			        ['需求人数',$('input[name="vitaeVo.neddPersonNumber"]').val()],
			        ['职位要求描述',$('textarea[name="vitaeVo.needPersonDescription"]').val()],
			        ['需求原因',$('select[name="vitaeVo.reason"]').val()],
			        ['附件',_.map($('#attachment')[0].files,function (value_obj){
			        		return value_obj.name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
			        }).join(",")]
			        ]
		}
		layer.open({
			  type: 1,
			  skin: 'layui-layer-demo',
			  closeBtn: 0, 
			  anim: 2,
			  title:'是否确认保存',
			  area: ['1180px'],
			  shadeClose: true,
			  content: new tableCreator(getData(),['职位要求描述']).create(),
			  btn:['确认','取消'],
			  yes:function(index){
				  layer.close(index);
				  $('#form_').unbind("submit");
				  $('#form_').submit();
				  Load.Base.LoadingPic.FullScreenShow(null);
			  }
			});
		return false;
    })
    </script>
  </body>
</html>

