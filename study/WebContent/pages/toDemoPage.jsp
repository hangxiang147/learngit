<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>

<title >oa 功能说明demo页面</title>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<link href="/assets/css/dark.css" rel='stylesheet'/>
</head>
<body>
	<div id="trans_staffSelect">
	</div>
	<script type="text/html" id="js_staffSelect">## 1.关于人员选择Input

```
	# 注意search 属性
	<input id="staffSearch" class="form-control" type="search"/>
``` 
--------------------

```
/*
	引入 staffComplete 会自动引入它依赖的 jquery，textcomplete和样式文件
	最终数据绑定到这个input.data的三个属性上
*/

	require(['staffComplete'],function (staffComplete){
			new staffComplete().render($('#staffSearch'),function ($item){
				console.log($item.data("userId"));
				console.log($item.data("userName"));
				console.log($item.data("detail"))
			});
		});
```
-------
#### 示例
	</script>
	<input id="staffSearch" class="form-control" type="search"/>

<div id="trans_textareaSelect"  style="margin-top:100px" >
</div>
<script type="text/html" id="js_textareaSelect">## 2.关于人员选择textarea
```
#当前 虽然已经实现 单光标定位任然不是非常准确
<div class="form-group">
    <div class="col-sm-5 inputout">
    <div style="position:relative;">
    	<span class="input_text">
    	<textarea id="agent" class="form-control" rows="3" style="width:500px"></textarea>
    	</span>
    	<div id="namecy" class="namecy" style="width:500px"></div>
    </div>
    </div>
 </div>
```
----------

```
# <script src="/assets/js/myjs/textarea.js">
# 依赖于jquery和underscore
new staffInputBind().render('#agent',textAfterChoose,{textarea:$('#agent'),namecy:$('#namecy')});				
	//#namecy div 覆盖了 textarea 导致点击后不会得到焦点
	$('#namecy').click(function (){
	$('#agent').focus();
})
# textAfterChoose 是 textarea 中的方法
此外 需要样式文件
.namecy{position:absolute;top:10px;left:10px;}
.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;margin-top:4px}
.namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}

# 结果保存在目标
$('#agent').data("resultData");

# 详情见 /administration/process/startCommonSubject   通用流程 发起页面
```
</script>
<div id="trans_tableCreator"  style="margin-top:100px" >
</div>
<script type="text/html" id="js_tableCreator">## 3.自动生成的表格
```
# 先将数据 输出到页面上 保存在一个 script 中
    <c:forEach items="${formFields }" var="formField" >
		{{lineSplit}}${formField.fieldText}{{keyValueSplit}}${formField.fieldValue }
	</c:forEach> 
# 再将内容显示到指定div中,这里指定了两个会被markdown的字段 （不是markdown）字段也可以指定 ，会独占一行，会根据字段的长度自动合并
单元格，如果需要特定的样式需求，只能自己实现了。
	new tableCreator(getData(),['任务描述','最终得分详情']).create($('#tableContent'));

# 详情见 tablePerform.jsp 
```
</script>
<div id="trans_textareaDetail">

</div>
<script type="text/html"  id="js_textareaDetail" >## 4.textarea 扩展
```
# 修改了 textarea中的图片上传方式  改为 可以用剪切板 和 附件上传 （并不支持 文件的复制黏贴，如果想要支持那么只能考虑用flash实现）
# 这个是重写了插件图片上传点击的弹出dialog
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
	  <!-- textarea附件上传代码 -->
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
	# 这里是图片上传按钮及回掉
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
						# 依赖于 cursorPosition的js （是对textarea的帮助js）
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

#这个是对剪切板图片的支持
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
	<!-- textarea附件上传代码 -->

```

</script>
<div id="trans_others"></div>
<script id="js_others" type="text/html">## 系统编码过程中需要注意的地方
考勤
-  导入会删除 重复数据  （但是 假如 前一条和后一条公司不同 而且两条数据 都只有一个考勤时间 会进行合并）
-  考勤统计 会在统计才会对 工时进行统计 
-  部分考勤统计对一天多次请假 一天重复请假的判断存在错误（已经修改了1个，不继续修改的原因是因为这样判断效率比较低，且这种情况并不常见）
注意：假如请假时间是导入考勤数据之后请的假 那么导入时就判断的数据会有误差
---------------

工作汇报
- 工作汇报统计 会先把需要显示的天数和人员id筛选出来，再用指定数据拼凑sql 完成查询（这样快一点）

---------------

权限设置
- 系统将菜单和流程权限混合 用标志位区分是人员还是个人

---------------

人员所在岗位变更
- 系统有两张表维护 部门和 人员关系  
- oa_groupDetail 和 act_id_membership
- 假如直接改变 oa_department表或oa_position的从属关系  需要 在职位调整的地方 重新设置人员

---------------

任务的分隔
- 系统将 受理人是单人的 和受理人是多人进行分割 （首页只显示 单人受理的）

---------------

消息推送
- 推送：JPushTaskCreateListener(开发时需要注释该类中的推送方法)
- 短信：ShortMsgSender.getInstance().send(手机号,内容);ShortMsgSender.getInstance().send(手机号,内容,发送时间);
- 邮件：EmailSender.getInstance().send(地址,标题,内容); 若使用 配置文件 email.properties 需要修改

---------------

上班时间
- 现在是方法java字典类中 假如变更 需要进行 修改 重新发布

------------------------

新公司的添加
- 关键流程的走通需要 几个主要职位 
- - 分公司经理
- - 主管 （需要名称为主管）
- - 人事（组要部门名称中带有人事）
---------------

require.js
-  实用require.js 可以使用绝大部分的js，已经设置好了依赖关系
-  用法 require([names],function (){

   })
-  	需要注意 系统默认父页面 会引入 jquery 和 bootstrap 等js ，有些js 会默认依赖jquery 会引入新的jquery 可能会导致一些问题，所以不要在父页面用$.data绑定数据。
-   用define 可以定义新的模块 可以查看 staffComplete2.js 查看实现方式
---------------

其他注意点
- 关键表
- -  人员部门关联关系表：act_id_user act_id_group act_id_membership  oa_staff oa_groupDetail oa_company oa_position oa_department
- -  运行流程查询关键表：act_ru_task act_ru_identiylink （如果节点 只有一个人 那么 通过instanceId 查询 act_ru_task即可 ，如果不是那么需要通过taskId关联查询act_ru_identiylink
- -  页面关键表：oa_reimbursement 报销  oa_advance 预付款 oa_vacation 请假表
-----------------
- 导出注意点
- -  需要扩大temp表 activiti 有些blob 字段 有些业务表 也会序列化字段到表中   
	set global max_allowed_packet=524288000
- -  oa_workreport oa_signin oa_advance oa_salaryDetail 如果不必要则不要到处 数据量较大
--------------------

 编码注意点
- -  可以参考报销流程来编写activiti流程代码
- -  写了一些辅助类 方便编码CopyUtil（用来vo和po的转换，简单的属性名相同就复制熟悉，但是不涉及到父类的属性）  ObjectByteArrTransformer （用来序列化字段）DateUtil（原有一些方法simpleDateFormat不是final的实现有一些问题） CheckDateUtil（用于一天存在在多次请假的判断类）
------------

input autoComplete实现
- 现在暂时没有封装  仅仅在招聘流程的表单中实现 通过formId 和InputId 将blur 后的数据缓存到库中 
- 为了性能优化 DicSimpleCache 会在查询时将数据缓存到内存中  在createContent方法中会将使用频率太低的数据从内存中清除
- 使用方法


```

	#其实我们应该用 textComplete插件 让这一过程在页码上更加美观，简洁
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
		
		//指定一个selector 就可以绑定
		new comAutoComplete().render('input[name="vitaeSignEntity.wysp"]')

```
------------------

userFilter
- 在 web.xml的excepUrlRegex中配置

-------------------

不需要oa目录头的页面配置
- 在 decorators.xml 中配置

-----------------------
oa目录头存在的问题
- 逻辑代码在 base.jsp 中  （现有方法会根据上次目录所在index确定下次页面显示选中位置,但这样存在一个问题 如果摁返回按钮 那么可能存在选中错误，可以考虑缓存所有请求过得页面和所选index来遍历选择）
  ```
$(function() {
			var url = location.href;
	        url = url.replace("http://", "");
	        var list = url.split("/");
	        var urljt = "";
	        for (var i=1; i<list.length; ++i) {
	        	urljt += list[i]+"/";
	        }
	        if (urljt !="") {
	        //修改 原有判断逻辑
	     	if(urljt=="attendance/attendanceDetail/"){
	     		$('#kqgl').addClass("dropdown-toggle-active");
	     		localStorage.lastMenuIndex=-1;
	     	}else if(urljt=="train/findTrainList"){
	     		$('#npgl').addClass("dropdown-toggle-active");
	     		localStorage.lastMenuIndex=-1;
	     	}
    		$('#navbar ul ul').each(function (index){
	        	$u=$(this);
	        	if ( localStorage.lastMenuIndex== index ) {
                	$u.parent().find(".dropdown-toggle").addClass("dropdown-toggle-active");
	            }
	        	$u.find('a').each(function (){
        			var $a=$(this);
        			if($a.attr("href")=="#"||!$a.attr("href"))return;
        			$a.attr("data-url",$a.attr("href"));
        			$a.removeAttr("href");
        			$a.click(function (){
        				localStorage.lastMenuIndex=index;
        				location.href=$(this).attr("data-url");
        			})
        		})
        	})
		});
  ```

--------------
有关附件处理的特殊判断
- 对于流程上的附件，因为像报销这种存在层级，我们在baseDao 中使用了一个特殊的实现  需要有 File[] files,String fileDetail,String instanceId 来绑定数据到
activiti流程上 ，注意其中fileDetail中的内容
	
	```
	/**
	 * @param files  
	 * ### 每个文件不能超过5M
	 * @param fileDetail
	 * 	> fileDetail是有关files数组的描述
	 * ##  fileDetail是一个json数组
	 * ### 数组里每个元素是这样的[fileName,fileSuffix,index]
	 * 1. fileName 文件名称
	 * 2. fileSuffix 文件后缀  （需要小写） 当是 /jpg|jpeg|png/ 时被判断为图片
	 * 3. index 假如附件存在层级 （例如 报销流程） 我们需要一个标识 字段 来确定是那个 类别下的附件，假如不存在分类都为0就行了
	 * ## 列入下面是一个有效的文件描述
	 * ### [['image0_0','jpg',0],['image0_1','png',0],['image1_0','png',1],['file1_1','zip',2]]
	 * ### 在这样的描述下 附件分为3个层级 0,1/2/3
	 * @param instanceId
	 * @throws Exception
	 */
	void saveActivitiAttchment(File[] files,String fileDetail,String instanceId) throws Exception;
	```
	
	为了更简单的实现，我们封装了 一个 fileChecker 如果成功则会返回这个fileDetail的json数据 方法使用
	使用时 引入fileChecker.js 
	通过 new 引入fileChecker(opt).render(selector,index)使用
	index是在页面中文件上传input所在的次序 selector 是这个input selector字符串
	关于opt有以下配置 
	this.opt={couldNull:true//是否可以为空
			,isLimitSuffix:false//是否验证后缀
			,effectiveSuffix:[]//有效的后缀
			,filterSuffix:[]//需要过滤的后缀,
			maxSize:5<<20//最大的文件大小 （默认这里为 5M,activiti 不能支持太大的附件 一般设置成5m就可以了）};

------------------
流程委托功能
- 流程委托是将一个人或者某个组可能处理的流程或者即将处理的 委托给另外一个人或组进行处理 （需要在oa_entrust 中配置 其在map 中的key值），这里需要仔细看下实现方式，以免配置错误

-------------------

## 剩余问题及可能存在的问题
- 转正 需要支持老数据的录入（不需要走流程） 一些老员工的数据需要录入
- 系统中的流程不断变多 ，需要解决目录太多导致的样式问题及性能问题
- 原有人员查询的功能效率底下 ，往往输入一个“周凯” 会查询 z ，zh，zho，zhou，zhou',zhou'k,zhou'ka,zhou'kai,周凯 才会进行预想的正确查询，可能需要对中文的正则判断
- 流程走到一半时离职人员 可能会导致流程错误 （是否在离职时需要限制？）
- 通用流程，流程人员需要能看到，点过的所有流程（通过act_hi_*表查询实现）可以看下报销有个审批过的报销查看实现方式（那里过滤了第一步和修改那步）
- 可以考虑取消签到功能 ，才用是否录入工作汇报判断
- 可以考虑在进行 请假和报销时 （假如人员存在多个职位）可以自由选择人员所在职位 再进行流程 ，以免多个职位导致的审批人员错误
- 考勤管理 考勤明细和工作汇报任然存在性能问题，虽然已经比原先好了很多，但任然需要3秒左右，可以考虑使用存储过程重写java逻辑代码
- 现有的权限分配有些只是隐藏了url 可以考虑换一种方式实现

## 更新的主要方式
 更新时需要将JPushTaskCreateListener中的推送注释取消掉，将hibernate配置的tablespace改变成服务器上的oa_
1. 登录ssh
2. cd..
3. cd usr/local/tomcat/webapps/
4. 备份ROOT.war到本地
5. 复制eclipse导出的ROOT.war到webapps目录下
6. cd..
7. cd bin
8. ./shutdown.sh
9. cd..
10. cd webapps
11. rm -rf ROOT
12. unzip ROOT.war -d ROOT
13. cd..
14. cd bin
15. ./startup.sh
16. ps -ef| grep tomcat
17. kill -9 [进程ID] 

</script>

	<script src="/assets/js/require/require.js">
	</script>
	<script>
		require(['marked','highlight','jquery','staffComplete'],function(marked,hlgt,$,staffInputBind){
			hljs.initHighlightingOnLoad();
			 marked.setOptions({
			        highlight: function (code) {
			            return hljs.highlightAuto(code).value;
			        }
			    });
	    	$('div[id^="trans"]').each(function (){
	    		var itemId="js_"+$(this).attr("id").split("_")[1];
// 	    		console.log($('#'+itemId).html())
				$(this).html(marked($('#'+itemId).html()));
			})
		})

		require(['staffComplete'],function (staffComplete){
			new staffComplete().render($('#staffSearch'),function ($item){
				console.log($item.data("userId"));
				console.log($item.data("userName"));
				console.log($item.data("detail"))
			});
		});
		
	</script>
</body>
</html>