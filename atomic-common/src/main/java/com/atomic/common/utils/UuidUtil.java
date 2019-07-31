package com.atomic.common.utils;

import java.util.Date;
import java.util.UUID;

public class UuidUtil {
	
	public static void main(String[] args) {
		Date date = new Date();
		 System.out.println(Long.toString(date.getTime())); 
		for(int i=0;i<10;i++){
		 String uuid =uuid();  
	      System.out.println(uuid); 
		}
	}
	
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return NumberUtil.toString(hi | (val & (hi - 1)), NumberUtil.MAX_RADIX)
				.substring(1);
	}

	/**
	 * 以62进制（字母加数字）生成19位UUID，最短的UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}
}
