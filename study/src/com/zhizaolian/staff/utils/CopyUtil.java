package com.zhizaolian.staff.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

public class CopyUtil {

	/**
	 * 对于当前类 private 属性值的拷贝方法
	 * 
	 * @param itemObjectClass
	 *            生成Bean
	 * @param currentObject
	 *            被拷贝Bean
	 * @param type
	 *            拷贝方式 0:拷贝 attrs 中的属性 1:拷贝 itemObjectClass中的所有属性
	 *            2:拷贝currentObject中所有的属性
	 * @param attrs
	 *            需要拷贝的属性(type为0时生效)
	 * @return
	 * @throws Exception
	 */
	public final static Object DeclaredFieldCopy(Class<?> itemObjectClass, Object currentObject, int type,
			String[] attrs) throws Exception {
		if (itemObjectClass == null || currentObject == null)
			return null;
		if (type == 0 && (attrs == null || attrs.length == 0))
			return null;
		Object itemInstance = itemObjectClass.newInstance();
		if (type == 2) {
			Field[] fields = currentObject.getClass().getDeclaredFields();
			if (fields == null || fields.length == 0)
				return null;
			for (int i = 0, length = fields.length; i < length; i++) {
				String name = fields[i].getName();
				if (!"serialVersionUID".equals(name)) {
					fields[i].setAccessible(true);
					Object value = fields[i].get(currentObject);
					if (value == null)
						continue;
					Field field_new = itemObjectClass.getDeclaredField(name);
					if (field_new == null)
						throw new RuntimeException("bean属性缺失");
					field_new.setAccessible(true);
					field_new.set(itemInstance, value);
				}
			}
		} else if (type == 1) {
			Field[] fields = itemObjectClass.getDeclaredFields();
			if (fields == null || fields.length == 0)
				return null;
			for (int i = 0, length = fields.length; i < length; i++) {
				String name = fields[i].getName();
				if (!"serialVersionUID".equals(name)) {
					Field field_old = currentObject.getClass().getDeclaredField(name);
					if (field_old == null)
						throw new RuntimeException("bean属性缺失");
					field_old.setAccessible(true);
					Object value = field_old.get(currentObject);
					if (value == null)
						continue;
					fields[i].setAccessible(true);
					fields[i].set(itemInstance, value);
				}
			}
		} else {
			for (int i = 0, length = attrs.length; i < length; i++) {
				String fieldName = attrs[i];
				if (StringUtils.isEmpty(fieldName))
					continue;
				Field field_old = currentObject.getClass().getDeclaredField(fieldName);
				if (field_old == null)
					throw new RuntimeException("bean属性缺失");
				field_old.setAccessible(true);
				Object value = field_old.get(currentObject);
				if (value == null)
					continue;
				Field field_new = itemObjectClass.getDeclaredField(fieldName);
				if (field_new == null)
					throw new RuntimeException("bean属性缺失");
				field_new.setAccessible(true);
				field_new.set(itemInstance, value);
			}
		}

		return itemInstance;
	}


	public final static Object tryDeclaredFieldCopy(Class<?> itemObjectClass, Object currentObject, int type,
			String[] attrs) throws Exception {
		if (itemObjectClass == null || currentObject == null)
			return null;
		if (type == 0 && (attrs == null || attrs.length == 0))
			return null;
		Object itemInstance = itemObjectClass.newInstance();
		if (type == 2) {
			Field[] fields = currentObject.getClass().getDeclaredFields();
			if (fields == null || fields.length == 0)
				return null;
			for (int i = 0, length = fields.length; i < length; i++) {
				String name = fields[i].getName();
				try{
					if (!"serialVersionUID".equals(name)) {
						fields[i].setAccessible(true);
						Object value = fields[i].get(currentObject);
						if (value == null)
							continue;
						Field field_new = itemObjectClass.getDeclaredField(name);
						if (field_new == null)
							continue;
						field_new.setAccessible(true);
						field_new.set(itemInstance, value);
					}
				}catch(Exception ignore){};

			}
		} else if (type == 1) {
			Field[] fields = itemObjectClass.getDeclaredFields();
			if (fields == null || fields.length == 0)
				return null;
			for (int i = 0, length = fields.length; i < length; i++) {
				String name = fields[i].getName();
				try{
					if (!"serialVersionUID".equals(name)) {
						Field field_old = currentObject.getClass().getDeclaredField(name);
						if (field_old == null)
							continue;
						field_old.setAccessible(true);
						Object value = field_old.get(currentObject);
						if (value == null)
							continue;
						fields[i].setAccessible(true);
						fields[i].set(itemInstance, value);
					}
				}catch(Exception ignore){};
			}
		} else {
			for (int i = 0, length = attrs.length; i < length; i++) {
				String fieldName = attrs[i];
				try{
					if (StringUtils.isEmpty(fieldName))
						continue;
					Field field_old = currentObject.getClass().getDeclaredField(fieldName);
					if (field_old == null)
						continue;
					field_old.setAccessible(true);
					Object value = field_old.get(currentObject);
					if (value == null)
						continue;
					Field field_new = itemObjectClass.getDeclaredField(fieldName);
					if (field_new == null)
						continue;
					field_new.setAccessible(true);
					field_new.set(itemInstance, value);
				}catch(Exception ignore){};
			}
		}

		return itemInstance;
	}

	public static Object toEntity(Object vo,Class<?> entityClass){
		try {
			return DeclaredFieldCopy(entityClass, vo, 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("bean 属性拷贝异常");
		}
	}
	public static Object toVo(Object entity,Class<?> voClass){
		try {
			return DeclaredFieldCopy(voClass, entity, 2, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("bean 属性拷贝异常");
		}	
	}
	public static Object tryToEntity(Object vo,Class<?> entityClass){
		try { 
			return tryDeclaredFieldCopy(entityClass, vo, 1, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("bean 属性拷贝异常");
		}
	}
	public static Object tryToVo(Object entity,Class<?> voClass){
		try {
			return tryDeclaredFieldCopy(voClass, entity, 2, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("bean 属性拷贝异常");
		}	
	}
	public static Object clone(Object entity, Class<?> entityClass) throws Exception{
		Object itemInstance = entityClass.newInstance();
		Field[] fields = entity.getClass().getDeclaredFields();
		if (fields == null || fields.length == 0)
			return null;
		for (int i = 0, length = fields.length; i < length; i++) {
			String name = fields[i].getName();
			if (!"serialVersionUID".equals(name)) {
				fields[i].setAccessible(true);
				Object value = fields[i].get(entity);
				if (value == null)
					continue;
				Field field_new = entityClass.getDeclaredField(name);
				if (field_new == null)
					continue;
				field_new.setAccessible(true);
				field_new.set(itemInstance, value);
			}
		}
		return itemInstance;
	}
}
