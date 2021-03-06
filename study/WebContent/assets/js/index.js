function remove_url_page_limit() {
	var url_sections = window.location.href.split('?');
	var params = url_sections[0]+'?';
	if (url_sections.length > 1) {
		params += url_sections[1].split('limit')[0].split('page')[0];
	}
	if (params[params.length-1] != '?' && params[params.length-1] != '&') {
		params += '&';
	}
	return params;
}

function set_href() {
	var params = remove_url_page_limit();
	//$('.dropdown-item-20').attr('href', params+'limit=20&page=1');
	$('.dropdown-item-20').attr('onclick', "goPath('"+params+"limit=20&page=1')");
	$('.dropdown-item-20').attr('href', 'javascript:void(0)');
	//$('.dropdown-item-50').attr('href', params+'limit=50&page=1');
	$('.dropdown-item-50').attr('onclick', "goPath('"+params+"limit=50&page=1')");
	$('.dropdown-item-50').attr('href', 'javascript:void(0)');
	//$('.dropdown-item-100').attr('href', params+'limit=100&page=1');
	$('.dropdown-item-100').attr('onclick', "goPath('"+params+"limit=100&page=1')");
	$('.dropdown-item-100').attr('href', 'javascript:void(0)');
	localStorage.listUrl=location.pathname+location.search;
	params += 'limit=' + $("#limit").val() + '&page=';
	var page = parseInt($("#page").val());
	if (page > 1) {
		//$('.previous-page').attr('href', params.concat(page-1));
		$('.previous-page').attr('onclick', "goPath('"+params.concat(page-1)+"')");
		$('.previous-page').attr('href', 'javascript:void(0)');
	} else {
		$('.previous-page').attr('href', 'javascript:void(0)');
	}
	if (page < parseInt($('#totalPage').val())) {
		//$('.next-page').attr('href', params.concat(page+1));
		$('.next-page').attr('onclick', "goPath('"+params.concat(page+1)+"')");
		$('.next-page').attr('href', 'javascript:void(0)');
	} else {
		$('.next-page').attr('href', 'javascript:void(0)');
	}
}

function toLastPagedList(){
	var listUrl=localStorage.listUrl;
	if(listUrl){
		location.href=listUrl;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	else
		history.go(-1);
}