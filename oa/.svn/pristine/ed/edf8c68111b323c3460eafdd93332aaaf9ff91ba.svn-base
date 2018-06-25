var dicContent={
	vitaeReson:{
		handleType:'select',
		data:[['人员离职','人员离职'],['岗位调用','岗位调动'],['人员缺失','人员缺失'],['其他','其他']]
	},
	//软件绩效 人员分类
	softPersonType:{
		handleType:'select',
		data:[['编码人员','编码人员'],['需求分析','需求分析'],['产品经理','产品经理'],['软件测试','软件测试'],['实施','实施']]
	},
	softPersonProject:{
		handleType:'select',
		data:[['MES','MES'],['OA','OA'],['COM','COM'],['IRISIE','IRISIE'],['APP','APP']]
	},
	//报销申请页币种
	moneyType:{
		handleType:'select',
		data:[['人民币:1','人民币'],['美元:6.8876','美元'],['欧元:7.7265','欧元'],['英镑:8.9367','英镑'],['日元:0.0617','日元'],['港币:0.8843','港币'],['韩元:0.006159','韩元']]
	},
	//报销抬头
	invoiceTitle:{
		handleType:'select',
		data:[
		      ['','请选择'],
		      ['1_南通智造链科技有限公司','南通智造链科技有限公司'],
		      ['2_南通江凌织造有限公司','南通江凌织造有限公司'],
		      ['3_南通好多衣纺织品有限公司','南通好多衣纺织品有限公司'],
		      ['4_南通智造链贸易有限公司','南通智造链贸易有限公司'],
		      ['5_南通迷丝茉服饰有限公司','南通迷丝茉服饰有限公司'],
		      ['6_广州亦酷亦雅电子商务有限公司','广州亦酷亦雅电子商务有限公司'],
		      ['7_南京智造链信息科技有限公司','南京智造链信息科技有限公司'],
		      ['8_南通亦酷亦雅电子商务有限公司','南通亦酷亦雅电子商务有限公司'],
		      ['9_南通互联智造能工业有限公司','南通互联智造能工业有限公司'],
		      ['10_佛山迷丝茉电子商务有限公司','佛山迷丝茉电子商务有限公司'],
		      ['11_广州市智风冠纺织品贸易有限公司','广州市智风冠纺织品贸易有限公司']]
	},
	flowType:{
		handleType:"select",
		data:[["","请选择"],
		      ["并行","并行"],
		      ["串行","串行"] 
		      ]
	},
	commonSubjectType:{
		handleType:'select',
		data:[["","请选择"],["告知","告知"],["审批","审批"]]
	}
}
//通过 dicContent 中定义的 handleType 进行统一的处理和初始化
var dicHelper=(function($,_){
	//传入字典是根据那个dicContent生成的值是多少
	var dicHelper=function(data,value){
		if(!data)return;
		switch (data.handleType) {
		case 'select':
			this.$item=$('<select></select>');
			this.$item.append(_.map(data.data,function(listData){
				return '<option value="'+listData[0]+'">'+listData[1]+'</option>';
			}));
			if(value)this.$item.find("option[value='"+value+"']").prop("selected","selected");
			break;
		default:
			break;
		}
	}
	
	dicHelper.prototype.render=function($parent,className,fnOthers){
		defaultRenderFn.call(this,className,fnOthers);
		$parent.append(this.$item);
	};
	dicHelper.prototype.renderByFn=function(fn,className,fnOthers){
		defaultRenderFn.call(this,className,fnOthers);
		fn(this.$elem);
	};
	
	var defaultRenderFn=function(className,fnOthers){
		if(className)
		this.$item.prop("class",className);
		if(fnOthers)fnOthers.call(this);
	}
	return dicHelper;
})($,_)