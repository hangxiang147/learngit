<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<link href="/assets/css/dark.css" rel='stylesheet'/>

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
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.clear{clear:both;height:0;font-size:0px;_line-height:0px;}
	.namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;margin-top:4px}
	.namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="/administration/commonSubject/startCommonSubject" method="post"  id="itemForm" 
        	class="form-horizontal" enctype="multipart/form-data" >  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起通用流程</h3> 			 
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">标题</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  autocomplete="off"  name="commonSubjectVo.title_	" required="required" maxlength=25></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">类别</label>
			  	<div class="col-sm-2" id="flowRoute"></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">流通方式</label>
			  	<div class="col-sm-2" id="typeDiv"></div>
			  </div>
			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">内容描述</label>
			  	<div class="col-sm-7"><textarea name="commonSubjectVo.content" data-provide="markdown" rows='7' maxlength=5000 >${jobEntity.jobDescription}</textarea></div>
			  </div>
			  <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">处理人员</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agent" class="form-control" rows="3" style="width:500px"></textarea>
			    	</span>
			    	<div id="namecy" class="namecy" style="width:500px"></div>
			    </div>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
			    <div class="col-sm-5">
			    	<input type="file" id="attachment" name="files" multiple="multiple" accept="jpeg/jpg/png/bmp" style="padding:6px 0px;">
			    	<input name="fileDetail" type="hidden"/>
			    </div>
			  </div>
			  <input style="display:none" name="commonSubjectVo.userIds"  />
			  <input style="display:none" name="commonSubjectVo.userNames" />
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      
      <!--textarea 图片上传代码 -->
      	<div class="modal fade bs-example-modal-lg" id="attachmentDiv" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-md" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">附件上传</h4><div class="container-fluid1"></div>
		      </div>
		      <div class="modal-body">
		      	<input type="file" name="fileAttachment" id="fileAttachment" />
		      </div>
		      <div class="modal-footer">
		      <button type="button" class="btn btn-primary" data-dismiss="modal" id="confirm">确定</button>
		      <button type="button" class="btn btn-primary" data-dismiss="modal" id="close">关闭</button>
		      </div>
		    </div>
		  </div>
		</div>
       <!--textarea 图片上传代码 -->
       
    <script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/textarea/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script src="/assets/js/textarea/marked.js"></script>
    <script>
     marked.setOptions({
         highlight: function (code) {
             return hljs.highlightAuto(code).value;
         }
     });
    </script>
    <script src="/assets/js/textarea/to-markdown.js"></script>
    <script src="/assets/js/textarea/bootstrap-markdown.js"></script>
    <script src="/assets/js/myjs/staffInput.js"></script>
    <script src="/assets/js/myjs/textarea.js"></script>
    <script src="/assets/js/myjs/fileChecker.js"></script>
    <script src="/assets/js/dic.js"></script>
    <script src="/assets/js/textareaHelper.js"></script>
    <script>
  	var initHandler=new Function();
	(function (){
		this.startInit=function (){
			Object.values(this.bindEvents).forEach(function (fn){
				fn.call(this);
			}.bind(this));
		};
		this.fileChecker=function (){
			//最大5m 不验证后缀类型
			return new FileChecker({maxSize:5<<20,isLimitSuffix:false});
		}
		this.bindEvents={
			'初始化2个select':function (){
				new dicHelper(dicContent.flowType).render($('#typeDiv'),'form-control',function (){
					this.$item.prop("name","commonSubjectVo.route").prop("required","required");
				});
				new dicHelper(dicContent.commonSubjectType).render($('#flowRoute'),'form-control',function (){
					this.$item.prop("name","commonSubjectVo.type").prop("required","required");
					var $routeSelect=$('select[name="commonSubjectVo.route"]');
					this.$item.change(function (){
						$routeSelect.find("option").removeAttr("disabled");
						if("告知"==this.$item.val()){
							$routeSelect.val("并行");
						}else if("审批"==this.$item.val()){
							//假如是审批的话  那么只能是串行
							$routeSelect.find("option").each(function (){
								if($(this).val()!="串行"){
									$(this).attr("disabled","disabled")
								}
							})
							$routeSelect.val("串行");
							
						}
					}.bind(this));
				});
			},
			'人员选择textarea初始化':function (){
				new staffInputBind().render('#agent',textAfterChoose,{textarea:$('#agent'),namecy:$('#namecy')});				
				//#namecy div 覆盖了 textarea 导致点击后不会得到焦点
				$('#namecy').click(function (){
					$('#agent').focus();
				})
			},
			'表单提交验证':function (){
				$('#itemForm').submit(function (){
					var data=$('#agent').data("resultData");
					if(!data||data.length==0){
						layer.alert("请填写流程处理人员",{offset:'100px'},function (index){
							layer.close(index);
						})
						return false;
					}else{
						var result=data.reduce(function (result,value){
							result[0].push(value[0]);
							result[1].push(value[1]);
							return result;
						},[[],[]]);
						$('input[name="commonSubjectVo.userIds"]').val(result[0].join(","));
						$('input[name="commonSubjectVo.userNames"]').val(result[1].join(","));
						var checkResult=this.fileChecker().check("#attachment");
						if(!checkResult.isPass){
							return false;
						}else{
							$('input[name="fileDetail"]').val(checkResult.fileDetail);
						}
						var data=new FormData($('#itemForm')[0]);
						$.ajax({
							url:'/administration/commonSubject/startCommonSubject',
							data:data,
							type:'post',
							dataType:'json',
							processData: false,
						    contentType: false,
							success:function (returnValue){
								returnValue=eval("("+returnValue+")");
								if(!returnValue.success){
									layer.alert("发起失败："+returnValue.extraMsg,{offset:'100px'});
								}else{
									layer.alert("发起成功！",{offset:'100px'},function (){
										location.href="administration/process/findMyProcessList?type=19";	
									})
								}
							}
						})
						return false;
					}
					Load.Base.LoadingPic.FullScreenShow(null);
				}.bind(this));
			}
		};
	}).call(initHandler.prototype)
	new initHandler().startInit();
    </script>
    <!-- textarea附件上传代码 -->
    <script>
    $(function (){
 	   _.delay(function (){
 		  var $items= $('button[title="Image"]');
 		  $items.each(function (){
 			  $(this).bind("click",function (event){
 				  event.stopPropagation();
 				  $('#fileAttachment').val("");
 				  $('#attachmentDiv').modal('show');
 				  $('#attachmentDiv').data("item",$(this).parent().parent().next());
 			  });
 		  })
 	   },500)
    });
    $('#close').click(function (){
		$('#attachmentDiv').modal('hide');
    })
    $('#confirm').click(function (){
		    var img = document.getElementById("fileAttachment");
		    var name=$(img).val();
		    var replace="";
			if(name!=null&&name!=''){
				var index=name.lastIndexOf("\\");
	            var fileName=name.substring(index+1,name.length)
				var suffix=fileName.replace(/.*\.(.*)/,"$1");
		        suffix=suffix.toLowerCase();
		        if(!_.find(['png','jpg','jpeg'],function(s){
		        	return s==suffix
		        })){
		        	layer.alert("附件类型只能是:png,jpg,jpeg!",{offset:'100px'})
		        	$(img).val("");
		        	return;
		        }else{
		        	replace=fileName;
		        }
			}else{
	        	return ;
			}
		    var fm = new FormData();
		    fm.append('imageName', fileName);
		    fm.append('file', img.files[0]);
		    $.ajax({
		            url: '/performance/soft/attachmentSave',
		            type: 'POST',
		            data: fm,
		            async:false,
		            contentType: false, 
		            processData: false, 
		            success: function (result) {
	   		            var id=result.id;
	   		            var url="/performance/soft/downloadPic?id="+id;
	   					$('#attachmentDiv').modal('hide');
	   					var $textarea=$('#attachmentDiv').data("item");
	   					var textarea=$textarea[0];
	   					pos = cursorPosition.get(textarea);  
	   					var addStr="!["+replace+"]"+"("+url+")";
	   					cursorPosition.add(textarea, pos, addStr);
	   					var pos =cursorPosition.get(textarea);
	   					var removeStr=addStr.length-1;
	   					pos.start=pos.start-removeStr+1;
	   					pos.end=replace.length+pos.start;
	   					cursorPosition.set(textarea, pos);
		            },fail:function (result){
		            	layer.alert("图片上传失败");
		            }
		        });
    })
 $("textarea").on("paste", function(event) {
 	var item_textarea=$(this);
	    var items = (event.clipboardData || event.originalEvent.clipboardData).items;
	    for (index in items) {
	        var item = items[index];
	        var type = item.type;
	        if(type && type.split('/')[0] == 'image') {
	            var suffix = type.split('/')[1];
	            var blob = item.getAsFile();
	            var size = blob.size;
	            if(size / (1024 * 1024) < 5) {
	                var reader = new FileReader();
	                reader.onload = function (event) {
	                    var form = document.createElement("form").setAttribute("enctype", "multipart/form-data");
	                    var formData = new FormData(form);
	                    formData.append("file", blob, "image." + suffix);
	                    formData.append("imageName","image")
	                    var xhr = new XMLHttpRequest();
	                    xhr.open('POST', '/performance/soft/attachmentSave', true);
	                    xhr.onload = function (event) {
	                        var responseText = event.currentTarget.responseText;
	                        var json = JSON.parse(responseText);
	   	   		            var url="/performance/soft/downloadPic?id="+json.id;
	                        var replace="image";
	                    	var textarea=item_textarea[0];
	   	   					pos = cursorPosition.get(textarea);  
	   	   					var addStr="!["+replace+"]"+"("+url+")";
	   	   					cursorPosition.add(textarea, pos, addStr);
	   	   					var pos =cursorPosition.get(textarea);
	   	   					var removeStr=addStr.length-1;
	   	   					pos.start=pos.start-removeStr+1;
	   	   					pos.end=replace.length+pos.start;
	   	   					cursorPosition.set(textarea, pos);
	                    };
	                    xhr.send(formData);
	                };
	                reader.readAsDataURL(blob);
	            } else {
	                layer.alert("上传图片大小不能超过5M",{offset:'100px'});
	            }
	        }}
		});
	</script>
	<!-- textarea附件上传代码 -->
</body>
</html>