package com.zhizaolian.staff.utils;

import com.google.common.base.Function;

/**
 * 在输入null值时直接返回null的Function
 * @author zpp
 * 
 */
public abstract class SafeFunction<F, T> implements Function<F, T> {
	
	@Override
	public final T apply(F input) {
		return input != null ? safeApply(input) : null;
	}
	 
	/**
	 * @param input 不会为null
	 * @return
	 */
	protected abstract T safeApply(F input);
	
}
