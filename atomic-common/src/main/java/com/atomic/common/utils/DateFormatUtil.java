package com.atomic.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期格式化工具类
 *
 * @author Administrator
 */
public class DateFormatUtil {
	private static final int[] lock = new int[] {};
	private static final SimpleDateFormat sFormatTZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'+0800'");
	private static final SimpleDateFormat sFormatShort4 = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat sFormatShort6 = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat sFormatShort8 = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat sFormatShort10 = new SimpleDateFormat("yyyyMMddHH");
	private static final SimpleDateFormat sFormatLong10 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sFormatShort12 = new SimpleDateFormat("yyyyMMddHHmm");
	private static final SimpleDateFormat sFormatLong13 = new SimpleDateFormat("yyyy-MM-dd HH");
	private static final SimpleDateFormat sFormatShort14 = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat sFormatLong19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sFormatLong23 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat sFormatLong17 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final Pattern validDatePatten = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))-((((0?" + "[13578])|(1[02]))-((0?[1-9])|([1-2][0-9])|(3[01])))"
			+ "|(((0?[469])|(11))-((0?[1-9])|([1-2][0-9])|(30)))|" + "(0?2-((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12"
			+ "35679])|([13579][01345789]))-((((0?[13578])|(1[02]))" + "-((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" + "-((0?[1-9])|([1-2][0-9])|(30)))|(0?2-((0?["
			+ "1-9])|(1[0-9])|(2[0-8]))))))");

	/**
	 * 构造函数
	 */
	private DateFormatUtil() {

	}
	public static void main(String[] args){
		System.out.println(formatTz(new Date()));
	}
	public static String formatTz(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			String tzf = sFormatTZ.format(date);
			return tzf;
		}
	}
	public static Date parseDateTz(String strDate) {
		try {
			synchronized (lock) {
				return sFormatTZ.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}
	/**
	 * 日期格式化，返回的格式为"yyyy-MM-dd HH:mm:ss.SSS"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatLong23(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatLong23.format(date);
		}
	}
	/**
	 * 日期格式化，返回的格式为"yyyy-MM-dd HH:mm:ss"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatLong19(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatLong19.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyy-MM-dd HH"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatLong13(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatLong13.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyy-MM-dd"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatLong10(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatLong10.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为format型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @param format
	 * 
	 * @return String 返回的日期串
	 */
	public static String format(Date date, SimpleDateFormat format) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return format.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyyMMdd"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatShort8(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatShort8.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyyMM"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatShort6(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatShort6.format(date);
		}
	}

	public static String formatShort4(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatShort4.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyyMMddHH"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatShort10(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatShort10.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyyMMddHHmm"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatShort12(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatShort12.format(date);
		}
	}

	/**
	 * 日期格式化，返回的格式为"yyyyMMddHHmmssSSS"型的日期字符串。
	 * 
	 * @param date
	 *            原日期
	 * @return String 返回的日期串
	 */
	public static String formatShort17(Date date) {
		if (date == null) {
			return "";
		}
		synchronized (lock) {
			return sFormatLong17.format(date);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyyMMdd"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateShort8(String strDate) {
		try {
			synchronized (lock) {
				return sFormatShort8.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyyMM"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateShort6(String strDate) {
		try {
			synchronized (lock) {
				return sFormatShort6.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyyMMddHH"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateShort10(String strDate) {
		try {
			synchronized (lock) {
				return sFormatShort10.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyy-MM-dd"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateLong10(String strDate) {
		try {
			synchronized (lock) {
				return sFormatLong10.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyyMMddHHmmss"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateShort14(String strDate) {
		try {
			synchronized (lock) {
				return sFormatShort14.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyyMMddHHmmss"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static String formatDateShort14(Date date) {
		synchronized (lock) {
			return sFormatShort14.format(date);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为"yyyy-MM-dd HH:mm:ss"）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @return Date 转换后的日期
	 * @throws Exception
	 */
	public static Date parseDateLong19(String strDate) {
		try {
			synchronized (lock) {
				return sFormatLong19.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * 日期类型转换（String日期类型转换成Date类型，字符串日期格式为format）。 <br>
	 * 
	 * @param strDate
	 *            原日期字符串
	 * @param format
	 * 
	 * @return Date 转换后的日期
	 * @throws IllegalArgumentException
	 */
	public static Date parseDate(String strDate, SimpleDateFormat format) {
		try {
			synchronized (lock) {
				return format.parse(strDate);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse the strDate: " + strDate);
		}
	}

	/**
	 * @param strDate
	 * @return 是否符合字符串日期格式为"yyyy-MM-dd"且为有效的日期
	 */
	public static boolean isValidDate(String strDate) {
		Matcher m = validDatePatten.matcher(strDate);
		return m.matches();
	}

}
