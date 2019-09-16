package com.core.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SeriesUtils {

	/**
	 * 流水号
	 * 前缀 +  yyyyMMddHHmmssSSS + 6位随机数 + id　
	 */
	public static String generateSeries(String prefix) {


		StringBuilder stringBuilder = new StringBuilder();
		// 创建当前日期时间对象
		LocalDateTime localDateTime = LocalDateTime.now();
		SecureRandom RANDOM = new SecureRandom();
		//生成６位随机数
		String randCode = StringUtils.leftPad(Integer.toString(RANDOM.nextInt((int) Math
			.round(Math.pow(10, 3)))), 3, '0');

		stringBuilder.append(prefix);
		stringBuilder.append(localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
//		stringBuilder.append(sdf.format(new Date()));
		stringBuilder.append(randCode);
//		stringBuilder.append(Integer.toString(id));
				
		return stringBuilder.toString();
	}

	/**
	 * 生成公寓ID/集团ID方法
	 * 沿用历史(SmartLink)生成机制
	 * @return
	 */
	public static String newHotelId(){
		String[] str = new String[26];
		for (int i = 1; i <= 26; i++) {
			str[i - 1] = ((char) (96 + i) + "").toUpperCase();
		}
		String id = str[(int) (Math.random() * 25 + 1)] + str[(int) (Math.random() * 25 + 1)] + System.currentTimeMillis();
		return id;
	}
}
