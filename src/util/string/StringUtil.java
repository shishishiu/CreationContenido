package util.string;

public class StringUtil {
	/**
	 * Confirmar si "val" es numero
	 * @param val
	 * @return
	 */
	public static boolean isNumber(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (NumberFormatException nfex) {
			return false;
		}
	}
}
