/*activiti流程附件 验证及构造的js
 * 初始化时设置 是否检验文件类型 和 有效的文件类型 和需要过滤的文件类型 (如果设置 那么都会进行过滤)
 * 如果为否 那么只会 校验附件 不能大于5M
 * */
var FileChecker=function(config){
	this.opt={couldNull:true,isLimitSuffix:false,effectiveSuffix:[],filterSuffix:[],maxSize:5<<20};
	Object.assign(this.opt,config);
};
(function (){
	this.check=function(selector,typeIndex){
		var $fileItem=$(selector);
		if(!$fileItem)
			throw new Error("根据selector无法找到对应file input");
		var fileList=$fileItem.prop("files");
		if(fileList==null||fileList.length==0){
			if(this.opt.couldNull){
				return {isPass:true,fileDetail:""};
			}else{
				return {isPass:false};
			}
		}
		var returnResult={isPass:true};
		var fileArr=[];
		var isEffectiveLimit=this.opt.isLimitSuffix&&this.opt.effectiveSuffix&&this.opt.effectiveSuffix.length>0;
		var isFilterLimit=this.opt.isLimitSuffix&&this.opt.filterSuffix&&this.opt.filterSuffix.length>0;
		$.each(fileList,function (index,file){
			var fileName=file.name;
			var suffix;
			var suffixResult=fileName.match(/.*\.(.*)/);
			//对于没有文件后缀的判断
			if(!suffixResult||suffixResult.length<=1){
				suffix="";
			}else{
				suffix=suffixResult[1];
			}
			suffix=suffix.toLowerCase();
			//验证文件大小
			if(file.size>this.opt.maxSize){
				returnResult.isPass=false;
				this.alert("文件大小不能超过"+(this.opt.maxSize>>20)+"M");
				return false;
			}
			if(isEffectiveLimit){
				if(!this.opt.effectiveSuffix.includes(suffix)){
					returnResult.isPass=false;
					this.alert(suffix+"不是有效的文件类型");
					return false;
				}
			}
			if(isFilterLimit){
				if(this.opt.filterSuffix.includes(suffix)){
					returnResult.isPass=false;
					this.alert(suffix+"不是有效的文件类型");
					return false;
				}
			}
			fileArr.push([fileName,suffix,typeIndex?typeIndex:0]);
		}.bind(this));
		if(returnResult.isPass){
			returnResult.fileDetail=JSON.stringify(fileArr);
		}
		return returnResult;
	};
	this.alert=function (msg){
		if(layer){
			layer.alert(msg,{offset:'100px'});
		}else{
			alert(msg)
		}
	}
}).call(FileChecker.prototype)