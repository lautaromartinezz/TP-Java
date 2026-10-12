package util;

public class HtmlUtil {

	public static String escapeHtml(String value) {
		if (value == null) { return ""; }
		return value
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#39;");
	}

	public static String escapeJs(String value) {
		if (value == null) { return ""; }
		return value
			.replace("\\", "\\\\")
			.replace("\"", "\\\"")
			.replace("'", "\\'")
			.replace("\r", "\\r")
			.replace("\n", "\\n");
	}
}
