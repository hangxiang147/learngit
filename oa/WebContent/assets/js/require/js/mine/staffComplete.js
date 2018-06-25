define(['textComplete','jquery','underscore'], function (textComplete,$,_) {
	var staffInputBind=(function ($,_){
	 	var staffInputBind=function(id){
	 		this.id=id||_.uniqueId("staffInput");
			this.$elem=null;
			this.selector=null;
			this.$wapper=null;
			this.lastQueryName=null;
	 	}
	 	staffInputBind.prototype.render=function(selector,afterChoose,extraContent){
	 		if(extraContent)
	 			Object.assign(this,extraContent);
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
	 		value=$.trim(value);
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
	 		data.staffVOs=[].slice.call(data.staffVOs,0,20);
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
	 			this_.$elem.data("lastQueryId",this_.lastQueryId)
	 			this_.$elem.data("lastQueryName",this_.lastQueryName)
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
	return staffInputBind;
});