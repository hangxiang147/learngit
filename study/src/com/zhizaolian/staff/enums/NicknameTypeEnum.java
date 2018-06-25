package com.zhizaolian.staff.enums;

/**
 * 花名类型
 * @author zpp
 *
 */
public enum NicknameTypeEnum {

	FEIHUWAIZHUAN(1, "飞狐外传"),
	
	XUESHANFEIHU(2, "雪山飞狐"),
	
	LIANCHENGJUE(3, "连诚诀"),
	
	TIANLONGBABU(4, "天龙八部"),
	
	SHEDIAOYINGXIONGZHUAN(5, "射雕英雄传"),
	
	BAIMAXIAOXIFENG(6, "白马啸西风"),
	
	LUDINGJI(7, "鹿鼎记"),
	
	XIAOAOJIANGHU(8, "笑傲江湖"),

	SHUJIANENCHOULU(9, "书剑恩仇录"),
	
	SHENDIAOXIALV(10, "神雕侠侣"),
	
	XIAKEXING(11, "侠客行"),
	
	YITIANTULONGJI(12, "倚天屠龙记"),
	
	BIXUEJIAN(13, "碧血剑"),
	
	YUANYANGDAO(14, "鸳鸯刀");
	
	private final int value;
	
	private final String name;
	
	NicknameTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static NicknameTypeEnum valueOf(int value) {
		for (NicknameTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
