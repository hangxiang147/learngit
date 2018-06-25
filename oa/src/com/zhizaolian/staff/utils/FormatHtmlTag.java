package com.zhizaolian.staff.utils;

public class FormatHtmlTag {
	public static String format(String message) {
		if (message == null)
			return "";
		char content[] = new char[message.length()];

		message.getChars(0, message.length(), content, 0);

		StringBuilder result = new StringBuilder(content.length + 50);

		for (int i = 0; i < content.length; i++) {

			switch (content[i]) {

			case '<':

				result.append("&lt;");

				break;

			case '>':

				result.append("&gt;");

				break;

			case '&':

				result.append("&amp;");

				break;

			case '"':

				result.append("&quot;");

				break;

			default:

				result.append(content[i]);

			}

		}

		return (result.toString());
	}
}
