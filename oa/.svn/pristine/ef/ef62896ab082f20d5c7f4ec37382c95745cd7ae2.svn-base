define(['textComplete','jquery','underscore'], function (textComplete,$,_) {
	var staffComplete=new Function();
	(function (){
		this.render=function ($item,callback){
			$item.textcomplete([
               {
                   match: /^([\u4E00-\u9FA5\uF900-\uFA2D]*)$/,
                   search: function (term, callback) {
                	   if(!term){
                		   callback([])
                		   return;
                	   }
                	   $.ajax({
		       	 			url:'personal/findStaffByName',
		       	 			type:'post',
		       	 			data:{name:term},
		       	 			dataType:'json',
		       	 			success:function(data){
		       	 				var resultData_=data.staffVOs;
		       	 				callback(resultData_.map(function (value){
		       	 					var groupDetail=value.groupDetailVOs[0];
		       	 					var detailGroup=JSON.stringify(groupDetail);
		       	 					var detail="("+groupDetail.companyName+"-"+groupDetail.departmentName+"-"+groupDetail.positionName+")";
		       	 					var img='<input data-detail="'+escape(value.userID)+"&"+escape(value.lastName)+'&'+escape(detailGroup)+'" hidden/><img src="personal/getUserPicture?userId='+value.userID+'" onerror="this.src=\'/assets/images/default.png\'" style="width:30px;height:35x;">'
		       	 					return img+value.lastName+detail;
		       	 				}))
		       	 			}
           	 			})
                       
                   },
                   index: 1,
                   replace: function (word) {
                	   var $item=$(word);
                	   var detail=$($item[0]).attr("data-detail");
                	   var detailArray=detail.split("&");
                	   detailArray=detailArray.map(function (value){
                		   return unescape(value);
                	   });
                	   $item.data("userId",detailArray[0]);
                	   $item.data("userName",detailArray[1]);
                	   $item.data("detail",detailArray[2]);
                	   if(callback)
                		   callback($item);
                       return detailArray[1];
                   }
               }
           ],{maxCount:10});
           
		}	
	}).call(staffComplete.prototype)
	return staffComplete;
});