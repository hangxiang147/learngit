package com.zhizaolian.staff.action.administration;

import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.ShopInfoEntity;
import com.zhizaolian.staff.service.ShopManageService;
import com.zhizaolian.staff.utils.ListResult;

import lombok.Getter;
import lombok.Setter;

public class ShopManageAction extends BaseAction{
	
	private static final long serialVersionUID = 8605173916832463227L;
	@Getter
	private String errorMessage;
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Setter
	@Getter
	private ShopInfoEntity shopInfo;
	@Autowired
	private ShopManageService ShopManageService;
	
	public String showShops(){
		String reserveTelephone = request.getParameter("reserveTelephone");
		ListResult<ShopInfoEntity> shopInfoList = ShopManageService.showShops(limit, page, reserveTelephone);
		count = shopInfoList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("shopInfoList", shopInfoList.getList());
		request.setAttribute("reserveTelephone", reserveTelephone);
		selectedPanel = "shopInfoList";
		return "showShops";
	}
	public String addShopInfo(){
		selectedPanel = "shopInfoList";
		return "editShopInfo";
	}
	public String saveShopInfo(){
		ShopManageService.saveShopInfo(shopInfo);
		selectedPanel = "shopInfoList";
		return "render_showShops";
	}
	public String showShopInfoDetail(){
		String shopInfoId = request.getParameter("shopInfoId");
		ShopInfoEntity shopInfo = ShopManageService.showShopInfoDetail(shopInfoId);
		request.setAttribute("shopInfo", shopInfo);
		selectedPanel = "shopInfoList";
		return "showShopInfoDetail";
	}
	public String updateShopInfo(){
		String shopInfoId = request.getParameter("shopInfoId");
		ShopInfoEntity shopInfo = ShopManageService.showShopInfoDetail(shopInfoId);
		request.setAttribute("shopInfo", shopInfo);
		selectedPanel = "shopInfoList";
		return "editShopInfo";
	}
	public String deleteShop(){
		String id = request.getParameter("id");
		ShopManageService.deleteShop(id);
		return "render_showShops";
	}
	public String closeShop(){
		String id = request.getParameter("id");
		ShopManageService.closeShop(id);
		return "render_showShops";
	}
}
