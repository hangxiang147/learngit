/*oa流程表单中 通用表格的js*/
/*需要样式
 *   table{width:100%;border-collapse:collapse;}
	 table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	 table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	 table tr .black {background:#efefef;text-align:center;color:#000;}
	 table tbody tr td p{padding:4px 0px;}
	 table tbody tr td li{list-style:none;text-align: left;}
 * 
 * */
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
					}else if(value.length>15){
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
								innerContent+="<td class=\"black\" style=\"min-width:100px\">"+value[i].key+"</td><td colspan="+(value[i].size-1)+" style=\"text-align:left;max-width:1080px\">"+ marked(value[i].value)+"</td>";
								continue;
							}
						}
					}
					innerContent+="<td class=\"black\" style=\"min-width:100px\" >"+value[i].key+"</td><td colspan="+(value[i].size-1)+">"+value[i].value+"</td>";
				}
				return "<tr>"+innerContent+"</tr>";
			}).join("");
			if($parent)
				$parent.append("<table >"+resultHtml+"</table>");
			return "<table >"+resultHtml+"</table>";
		}
		return tableCreator;
	})($,_)
