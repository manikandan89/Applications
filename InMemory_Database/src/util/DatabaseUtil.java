package util;

public class DatabaseUtil {

public DatabaseUtil() {
		
	}
	// Checks whether the provided statement is a keyword
	public static boolean isKeyword(String statement) {
		if (statement.equalsIgnoreCase("SET") || statement.equalsIgnoreCase("GET")) {
			return true;
		} else {
			return false;
		}
	}
}
