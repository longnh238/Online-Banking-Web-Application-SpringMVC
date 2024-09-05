package com.example.oba.utils;

public class ConstantUtils {
	final public static double MIN_BALANCE_THRESHOLD = 1200;
	final public static double INITIAL_BALANCE = 0;

	final public static double DAILY_LIMIT = 1000;

	// 0: Second (0-59)
	// 0: Minute (0-59)
	// 0: Hour (0-23)
	// *: Day of month (1-31)
	// *: Month (1-12)
	// *: Day of week (0-7, where 0 and 7 represent Sunday)
	final public static String DAILY_LIMIT_RESET_TIME = "0 0 0 * * ?";

	// Get rate exchange
	final public static String base = "USD";
}
