/*oa 系统中textarea选择一群人的封装*/
/*
 *  需要样式
 * .namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;margin-top:4px}
	.namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
	
	demo:
	 <div class="form-group">
	    <label for="agent" class="col-sm-1 control-label">参与人</label>
	    <div class="col-sm-5 inputout">
	    <div style="position:relative;">
	    	<span class="input_text">
	    	<textarea id="agent" class="form-control" rows="3" ></textarea>
	    	</span>
	    	<div id="namecy" class="namecy"></div>
	    </div>
	    </div>
	  </div>

	初始化方法:
	new staffInputBind().render('#agent',afterChoose,{textarea:$('#agent'),namecy:$('#namecy')});
    <c:forEach items="${trainVO.staffs}" var="staffVO" varStatus="count">
    	afterChoose.call({lastQueryId:'${staffVO.userID }',lastQueryName:'${staffVO.lastName}',textarea:$('#agent'),namecy:$('#namecy')});
	</c:forEach>
 * */
var fixedTextPlace=function (resultData){
	this.textarea.attr("rows",3*((Math.floor((resultData.length)/5)+1)));
	var text="";
	for( var i=0;i<resultData.length;i++)
	{
		text+="                       ";
		if((i+1)%5==0){
			text+="\n"+"                       ".repeat(5)+"\n";
		}
	}			
	this.textarea.val(text);
	this.textarea.focus();
}
function resetMouse(obj){
	var resultData=$(obj).data('resultData');
	if(!resultData){
		return;
	}
	var text='';
	for( var i=0;i<resultData.length;i++)
	{
		text+="                       ";
		if((i+1)%5==0){
			text+="\n"+"                       ".repeat(5)+"\n";
		}
	}
	$(obj).val(text);
}
var textAfterChoose=function (){
	this.textarea.val("");
	var resultData=this.textarea.data('resultData');
	var insertElem=[this.lastQueryId,this.lastQueryName]
	if(resultData||(resultData=[])){
		var isContaion=false;
		_.each(resultData,function (value){
			if(value[0]==insertElem[0]){
				isContaion=true;
				return false;
			}
		});
		if(isContaion){
			var this_=this;
			if(layer)
				layer.alert("该人员已经被选择!",{offset:'100px'},function (index){
					layer.close(index);
					fixedTextPlace.call(this_,resultData);
				});
			else
				alert("该人员已经被选择");
			return;
		}else{
			resultData.push(insertElem);
		}
	}
	fixedTextPlace.call(this,resultData)
	this.textarea.data('resultData',resultData);
	var span='<span style="width:73px;padding:6px 9px">'+this.lastQueryName+'<a>×</a></span>';
	var $span=$(span);
	$span.data("spanData",insertElem);
	var this_=this;
	$span.find('a').click(function(){
		var resultData=this_.textarea.data('resultData');
		var spanData=$(this).parent().data("spanData");
		var index=0;
		_.each(resultData,function(value){
			if(value[0]==spanData[0]){
				index=arguments[1];
				return false;
			}
		})
		var deletedData=resultData.slice(0,index).concat(resultData.slice(index+1));
		this_.textarea.data('resultData',deletedData);
		fixedTextPlace.call(this_,deletedData)
		$span.remove();
	});
	$span.data('id',this.lastQueryId);
	this.namecy.append($span);
}