package com.atomic.common.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NumberUtil {

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z' };

	final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>();

	static {
		for (int i = 0; i < digits.length; i++) {
			digitMap.put(digits[i], (int) i);
		}
	}

	/**
	 * 支持的最大进制数
	 */
	public static final int MAX_RADIX = digits.length;

	/**
	 * 支持的最小进制数
	 */
	public static final int MIN_RADIX = 2;

	/**
	 * 将长整型数值转换为指定的进制数（最大支持62进制，字母数字已经用尽）
	 * 
	 * @param i
	 * @param radix
	 * @return
	 */
	public static String toString(long i, int radix) {
		if (radix < MIN_RADIX || radix > MAX_RADIX)
			radix = 10;
		if (radix == 10)
			return Long.toString(i);

		final int size = 65;
		int charPos = 64;

		char[] buf = new char[size];
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (size - charPos));
	}

	static NumberFormatException forInputString(String s) {
		return new NumberFormatException("For input string: \"" + s + "\"");
	}

	/**
	 * 将字符串转换为长整型数字
	 * 
	 * @param s
	 *            数字字符串
	 * @param radix
	 *            进制数
	 * @return
	 */
	public static long toNumber(String s, int radix) {
		if (s == null) {
			throw new NumberFormatException("null");
		}

		if (radix < MIN_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " less than Numbers.MIN_RADIX");
		}
		if (radix > MAX_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " greater than Numbers.MAX_RADIX");
		}

		long result = 0;
		boolean negative = false;
		int i = 0, len = s.length();
		long limit = -Long.MAX_VALUE;
		long multmin;
		Integer digit;

		if (len > 0) {
			char firstChar = s.charAt(0);
			if (firstChar < '0') {
				if (firstChar == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
				} else if (firstChar != '+')
					throw forInputString(s);

				if (len == 1) {
					throw forInputString(s);
				}
				i++;
			}
			multmin = limit / radix;
			while (i < len) {
				digit = digitMap.get(s.charAt(i++));
				if (digit == null) {
					throw forInputString(s);
				}
				if (digit < 0) {
					throw forInputString(s);
				}
				if (result < multmin) {
					throw forInputString(s);
				}
				result *= radix;
				if (result < limit + digit) {
					throw forInputString(s);
				}
				result -= digit;
			}
		} else {
			throw forInputString(s);
		}
		return negative ? result : -result;
	}

	/**
	 * 传入16进制或者8进制的字符串，生成int类型
	 * 
	 * @param str
	 * @return
	 */
	public static int createInt(String str) {
		return NumberUtils.createInteger(str);
	}

	/**
	 * 格式化 string，返回 int 值
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt(String str) {
		return NumberUtils.toInt(str);
	}

	/**
	 * 格式化string，返回long值
	 * 
	 * @param str
	 * @return
	 */
	public static long toLong(String str) {
		return NumberUtils.toLong(str);
	}

	/**
	 * <p>
	 * Checks whether the String a valid Java number.
	 * </p>
	 * 
	 * <p>
	 * Valid numbers include hexadecimal marked with the <code>0x</code>
	 * qualifier, scientific notation and numbers marked with a type qualifier
	 * (e.g. 123L).
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumber(String str) {
		return NumberUtils.isNumber(str);
	}

	/**
	 * <p>
	 * Checks whether the <code>String</code> contains only digit characters.
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to check
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {
		return NumberUtils.isDigits(str);
	}

	/**
	 * Checks whether the <code>String</code> is a Integer.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		if (!isNumber(str)) {
			return false;
		}
		if (str.length() > Math.max(new Integer(Integer.MAX_VALUE).toString()
				.length(), new Integer(Integer.MIN_VALUE).toString().length())) {
			return false;
		}
		Long value = Long.parseLong(str);
		if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
			return true;
		}
		return false;
	}

	public static String format(String pattern, String str) throws Exception {
		if (isNumber(str)) {
			return format(pattern, Double.parseDouble(str));
		} else {
			throw new Exception("The input number is invalid.");
		}
	}

	public static String format(String pattern, double x) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(x);
	}

}
