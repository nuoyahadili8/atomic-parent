package com.atomic.hadoop.common.oozie.utils;

public class TimeConsumeUtils {
	public static String TimeConsume(Long timeConsume) {
		String timeConsumeStr = null;
		Long day = null;
		Long hour = null;
		Long minute = null;
		Long second = null;

		if (timeConsume == null) {
			timeConsumeStr = "";
		} else if (timeConsume >= 0 && timeConsume < 60) {
			timeConsumeStr = timeConsume + "秒";
		} else if (timeConsume >= 60 && timeConsume < 3600) {
			minute = timeConsume / 60;
			second = timeConsume % 60;
			timeConsumeStr = minute + "分" + unitFormat(second) + "秒";
		} else if (timeConsume >= 3600 && timeConsume < 86400) {
			hour = timeConsume / 3600;
			minute = timeConsume % 3600 / 60;
			second = timeConsume % 3600 % 60;
			timeConsumeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
		} else if (timeConsume >= 86400) {
			day = timeConsume / 86400;
			hour = timeConsume % 86400 / 3600;
			minute = timeConsume % 86400 % 3600/ 60;
			second = timeConsume % 86400 % 3600 % 60;
			timeConsumeStr = day + "天" + unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
		}
		return timeConsumeStr;
	}

	/*
	 * 格式化为2位数字
	 */
	public static String unitFormat(long timeConsume) {
		String retStr = null;
		if (timeConsume < 10)
			retStr = "0" + Long.toString(timeConsume);
		else
			retStr = "" + timeConsume;
		return retStr;
	}

	public static void main(String[] args) {
		System.out.println("  null:" + TimeConsume(null));
		System.out.println("     0:" + TimeConsume((long) 0));
		System.out.println("    60:" + TimeConsume((long) 60));
		System.out.println("  3620:" + TimeConsume((long) 3620));
		System.out.println(" 86400:" + TimeConsume((long) 86400));
		System.out.println("864000:" + TimeConsume((long) 864000));
	}
}
