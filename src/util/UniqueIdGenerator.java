package util;

import java.util.Random;

public class UniqueIdGenerator {
	
	public static String generateId (String className, int userId) {
		Random random = new Random();
		return String.valueOf(userId) + className + System.currentTimeMillis() + String.valueOf(random.nextLong());
	}
}

