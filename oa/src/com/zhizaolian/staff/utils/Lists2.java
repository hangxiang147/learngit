package com.zhizaolian.staff.utils;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * 提供一些常用的List相关的工具函数
 * @author zpp
 *
 */
public final class Lists2 {
	
	private Lists2() {}
	
	/**
	 * 同Lists.transform但把lazy transform的行为关闭
	 * @param fromList
	 * @param function
	 * @return
	 */
	public static<F, T> List<T> transform(Iterable<F> fromList, Function<? super F, ? extends T> function) {
		Iterable<? extends T> output = Iterables.transform(fromList, function);
		return Lists.newArrayList(output);
	}
}
