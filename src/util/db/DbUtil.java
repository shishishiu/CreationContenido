package util.db;

public class DbUtil {
	/**
	 * Escape
	 * @param val
	 * @return
	 */
	public static String EscapeLike(String val){
		String str = "";
		str = val.replaceAll("'", "''");
		str = str.replaceAll("\"", "\"\"");
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("%", "\\\\%");

		return str;
	}
}
