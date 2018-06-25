package com.zhizaolian.staff.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * @author Zhouk
 * @date 2017年4月25日 上午9:23:31
 * @describtion 由于假如字典录入 先要判断 根据 formType 和 inputName 先要判断 menu 是否 存在 再根据
 *              menu的id 插入 content表 插入时又要根据 input的content 判读 diccontent 是否存在
 *              是新增一个 number为1的 还是 直接在原来的基础上++ 由于字典录入 只加不减 那么 这两个预先的判读 结果
 *              我可以存在一个map里 减少数据库的查询
 *
 *              然而content的内容表可能过多 那么 设置最大size为max_content_size 如果
 *              size>max_content_size 那么执行 清理工作 清理插入频率较小的(由此类维护 并不是
 *              数据库中content)的频率 如果清理后size 任然大于 max_content_size*3/4 那么 清理数值++
 *              继续清理 直到 满足要求 清理有 重置 fatcor 为/2+1
 **/
public class DicSimpleCache {
	private final static Map<DicSimpleCache.MenuKey, Integer> MENU_CACHE = new ConcurrentHashMap<>();
	private final static Map<DicSimpleCache.contentKey, Object[]> CONTENT_CACHE = new ConcurrentHashMap<>();
	private static int clearFactor = 1;
	private final static int CONTENT_MAX_SIZE = 1 << 10;
	private final static int CONTENT_CLEAR_MAXSIZE = CONTENT_MAX_SIZE * 3 / 4;
	public static void createMenu(String formType, String inputName,
			Integer menuId) {
		MenuKey menuKey = new MenuKey(formType, inputName);
		MENU_CACHE.put(menuKey, menuId);
	}

	public static void createContent(Integer menuId, Integer contentId,
			String content) {
		if (CONTENT_CACHE.size() > CONTENT_MAX_SIZE) {
			while (CONTENT_CACHE.size() > CONTENT_CLEAR_MAXSIZE) {
				clearContent();
				if (CONTENT_CACHE.size() > CONTENT_CLEAR_MAXSIZE) {
					clearFactor++;
				} else {
					break;
				}
			}
		}
		clearFactor = clearFactor / 2 + 1;
		contentKey cKey = new contentKey(menuId, content);
		CONTENT_CACHE.put(cKey, new Object[]{1, contentId});
	}

	private static void clearContent() {
		List<contentKey> removeList = new ArrayList<>();
		for (Entry<contentKey, Object[]> entry : CONTENT_CACHE.entrySet()) {
			int number = (int) entry.getValue()[0];
			if (number <= clearFactor) {
				removeList.add(entry.getKey());
			}
		}
		synchronized (CONTENT_CACHE) {
			for (contentKey contentKey : removeList) {
				CONTENT_CACHE.remove(contentKey);
			}
		}
	}

	public  static Integer getMenuId(String formType, String inputName) {
		MenuKey menuKey = new MenuKey(formType, inputName);
		return MENU_CACHE.get(menuKey);
	}

	/**
	 * @param menuId
	 * @param conetnt
	 * @return 让 调用端 自己++Index 减少 新增时的 map的get调用
	 */
	public static Object[] getContentId(Integer menuId, String conetnt) {
		contentKey cKey = new contentKey(menuId, conetnt);
		return CONTENT_CACHE.get(cKey);
	}
	private static class MenuKey {
		private final String formType;
		private final String inputname;
		public MenuKey(String formType, String inputname) {
			super();
			this.formType = formType;
			this.inputname = inputname;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((formType == null) ? 0 : formType.hashCode());
			result = prime * result
					+ ((inputname == null) ? 0 : inputname.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MenuKey other = (MenuKey) obj;
			if (formType == null) {
				if (other.formType != null)
					return false;
			} else if (!formType.equals(other.formType))
				return false;
			if (inputname == null) {
				if (other.inputname != null)
					return false;
			} else if (!inputname.equals(other.inputname))
				return false;
			return true;
		}
	}

	private static class contentKey {
		private final Integer dicMenuId;
		private final String content;
		public contentKey(Integer dicMenuId, String content) {
			super();
			this.dicMenuId = dicMenuId;
			this.content = content;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((content == null) ? 0 : content.hashCode());
			result = prime * result
					+ ((dicMenuId == null) ? 0 : dicMenuId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			contentKey other = (contentKey) obj;
			if (content == null) {
				if (other.content != null)
					return false;
			} else if (!content.equals(other.content))
				return false;
			if (dicMenuId == null) {
				if (other.dicMenuId != null)
					return false;
			} else if (!dicMenuId.equals(other.dicMenuId))
				return false;
			return true;
		}
		
	}
}
