/**
 * 定制，为了解决冲突
 */
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
	var number=parseInt($(obj).attr('number'));
	if(!number){
		return;
	}
	var text='';
	for( var i=0;i<number;i++)
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
	this.textarea.attr('number',resultData.length);
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
		//定制
		var data = this_.textarea.data("resultData");
		var result=data.reduce(function (result,value){
			result.push(value[0]);
			return result;
		},[]);
		$('input[name="projectInfoVo.projectParticipants"]').val(result.join(","));
		$('input[name="partnerDetail.userIds"]').val(result.join(","));
	});
	$span.data('id',this.lastQueryId);
	this.namecy.append($span);
	//定制
	var data = this.textarea.data("resultData");
	if(data && data.length>0){
		var result=data.reduce(function (result,value){
			result.push(value[0]);
			return result;
		},[]);
		$('input[name="projectInfoVo.projectParticipants"]').val(result.join(","));
		$('input[name="partnerDetail.userIds"]').val(result.join(","));
	}
}