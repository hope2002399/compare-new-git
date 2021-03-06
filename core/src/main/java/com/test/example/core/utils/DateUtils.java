package com.test.example.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public class DateUtils {

	private DateUtils() {

	}

	public static Date now() {
		return new GregorianCalendar().getTime();
	}

	public static Date now(DateFormator pattern) {
		Date date = now();
		String str = toString(date, pattern);
		return toDate(str);
	}

	public static Date toDate(String time, DateFormator pattern) {
		Assert.notNull(time);
		Assert.notNull(pattern);
		return CalendarUtils.toCalendar(time, pattern).getTime();
	}

	/**
	 * 提供从String类型到Date类型的类型转化，目前自动支持 "yyyy-MM-dd"、"yyyy-MM"、 "yyyy-MM-dd HH:mm:ss"、"MM-dd"等4种日期格式的自动转化
	 * 
	 * @param time
	 * @return
	 */
	public static Date toDate(String time) {
		Assert.notNull(time);
		for (String key : defaultDateFormatMap.keySet()) {
			if (isDateFormat(time, key)) {
				return toDate(time, defaultDateFormatMap.get(key));
			}
		}
		throw new RuntimeException("just support format : "
				+ testStringUtils.collectionToDelimitedString(defaultDateFormatMap.values(), ",") + " - " + time);
	}

	public static String toString(Date date, DateFormator pattern) {
		Assert.notNull(date);
		Assert.notNull(pattern);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern.toString());
		return sdf.format(date);
	}

	public static String toString(Date date) {
		Assert.notNull(date);
		return toString(date, DateFormator.YEAR_MONTH_DAY_HH_MM_SS);
	}

	/**
	 * 比较两个 Date 对象表示的时间值（从历元至现在的毫秒偏移量）。
	 * 
	 * @param d1
	 * @param d2
	 * @return 如果 d1 表示的时间等于 d2 表示的时间，则返回 0 值；如果此 d1 的时间在d2表示的时间之前，则返回小于 0 的值；如果 d1 的时间在 d2 表示的时间之后，则返回大于 0 的值。
	 * 
	 */
	public static int compare(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();
		c1.setTime(d1);
		c2.setTime(d2);

		return c1.compareTo(c2);
	}

	/**
	 * 比较两个 Date 对象表示的日期值（仅仅比较日期,忽略时分秒）。 -wuwm
	 * 
	 * @param d1
	 * @param d2
	 * @return 如果 d1 表示的日期等于 d2 表示的日期，则返回 0 值；如果此 d1 的日期在d2表示的日期之前，则返回小于 0 的值；如果 d1 的日期在 d2 表示的日期之后，则返回大于 0 的值。
	 * 
	 */
	public static int compareDate(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		d1 = toDate(toString(d1, DateFormator.YEAR_MONTH_DAY), DateFormator.YEAR_MONTH_DAY);
		d2 = toDate(toString(d2, DateFormator.YEAR_MONTH_DAY), DateFormator.YEAR_MONTH_DAY);
		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();
		c1.setTime(d1);
		c2.setTime(d2);

		return c1.compareTo(c2);
	}

	/**
	 * 根据年月获取一个月的开始时间（第一天的凌晨）
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date beginTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month - 1, 1, 0, 0, 0);
		return first.getTime();
	}

	/**
	 * 根据年月获取一个月的结束时间（最后一天的最后一毫秒）
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date endTimeOfMonth(int year, int month) {
		Calendar first = new GregorianCalendar(year, month, 1, 0, 0, 0);
		first.add(Calendar.MILLISECOND, -1);
		return first.getTime();
	}

	/**
	 * 获取前preDays天的Date对象
	 * 
	 * @param date
	 * @param preDays
	 * @return
	 */
	public static Date preDays(Date date, int preDays) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(date);
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.DATE, -preDays);
		return cloneCalendar.getTime();
	}

	/**
	 * 获取后nextDays天的Date对象
	 * 
	 * @param date
	 * @param nextDays
	 * @return
	 */
	public static Date nextDays(Date date, int nextDays) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(date);
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.DATE, nextDays);
		return cloneCalendar.getTime();
	}

	public static Date nextMonths(Date date, int nextMonth) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(date);
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.MONTH, nextMonth);
		return cloneCalendar.getTime();
	}

	public static Date preMonths(Date date, int preMonth) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(date);
		GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
		cloneCalendar.add(Calendar.MONTH, -preMonth);
		return cloneCalendar.getTime();
	}

	public static long getDiffMillis(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		long diff = d1.getTime() - d2.getTime();

		return diff;
	}

	/**
	 * 间隔天数
	 * 
	 * @param d1
	 * @param d2
	 * @return d1 - d2 实际天数,如果 d1 表示的时间等于 d2 表示的时间，则返回 0 值；如果此 d1 的时间在d2表示的时间之前，则返回小于 0 的值；如果 d1 的时间在 d2 表示的时间之后，则返回大于 0
	 *         的值。
	 */
	public static long dayDiff(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();

		c1.setTime(d1);
		c2.setTime(d2);

		long diffDays = CalendarUtils.getDiffDays(c1, c2);

		return diffDays;
	}

	/**
	 * 间隔毫秒数
	 * 
	 * @param d1
	 * @param d2
	 * @return d1 - d2 实际毫秒数,如果 d1 表示的时间等于 d2 表示的时间，则返回 0 值；如果此 d1 的时间在d2表示的时间之前，则返回小于 0 的值；如果 d1 的时间在 d2 表示的时间之后，则返回大于
	 *         0 的值。
	 */
	public static long diffMillis(Date d1, Date d2) {
		Assert.notNull(d1);
		Assert.notNull(d2);

		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();

		c1.setTime(d1);
		c2.setTime(d2);

		long diffDays = CalendarUtils.getDiffMillis(c1, c2);

		return diffDays;
	}

	/**
	 * 获取间隔时间
	 * 
	 * @param d1
	 * @param d2
	 * @return HH:MM:SS,返回时间间隔的绝对值，没有负数
	 */
	public static String getDiffs(Date d1, Date d2) {
		long diffMillis = getDiffMillis(d1, d2);
		long diffHours = diffMillis / (60L * 60L * 1000L);
		long diffMinutes = diffMillis / (60L * 1000L) % 60;
		long diffSeconds = diffMillis / 1000L % 60;
		diffHours = Math.abs(diffHours);
		diffMinutes = Math.abs(diffMinutes);
		diffSeconds = Math.abs(diffSeconds);
		StringBuffer temp = new StringBuffer();
		temp.append(diffHours < 10 ? "0" + diffHours : diffHours);
		temp.append(":");
		temp.append(diffMinutes < 10 ? "0" + diffMinutes : diffMinutes);
		temp.append(":");
		temp.append(diffSeconds < 10 ? "0" + diffSeconds : diffSeconds);
		return temp.toString();
	}

	public static boolean isDateFormat(String date) {
		Assert.notNull(date);
		for (String key : defaultDateFormatMap.keySet()) {
			if (isDateFormat(date, key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDateFormat(String date, String format) {
		Assert.notNull(date);
		return testStringUtils.isDefinedPattern(date, format);
	}

	public static Date getNowDate() {
		Date now = new Date();
		return toDate(toString(now, DateFormator.YEAR_MONTH_DAY), DateFormator.YEAR_MONTH_DAY);
	}

	/**
	 * 根据日期返回日期中的年. wuwm
	 * 
	 * @param d
	 * @return int
	 */
	public static int getYear(Date d) {
		Assert.notNull(d);
		String dateStr = toString(d, DateFormator.YEAR_MONTH); // yyyy-MM
		return Integer.parseInt(dateStr.split(DateFormator.SPLIT_CHAR.toString())[0]);
	}

	/**
	 * 根据日期返回日期中的年.
	 * 
	 * @param d
	 * @return int
	 */
	public static int getMonth(Date d) {
		Assert.notNull(d);
		String dateStr = toString(d, DateFormator.YEAR_MONTH); // yyyy-MM
		return Integer.parseInt(dateStr.split(DateFormator.SPLIT_CHAR.toString())[1]);
	}
	
	public static int getSeason(Date date) {
		int currentSeason = 0;
		int currentMonth = getMonth(date);
		if (currentMonth >= 1 && currentMonth <= 3)
			currentSeason = 1;
	    else if (currentMonth >= 4 && currentMonth <= 6)
	    	currentSeason = 2;
	    else if (currentMonth >= 7 && currentMonth <= 9)
	    	currentSeason = 3;
	    else if (currentMonth >= 10 && currentMonth <= 12)
	    	currentSeason = 4;
		return currentSeason;
	}

	/**
	 * 根据日期返回日期中的日.
	 * 
	 * @param d
	 * @return int
	 */
	public static int getDay(Date d) {
		Assert.notNull(d);
		String dateStr = toString(d, DateFormator.YEAR_MONTH_DAY); // yyyy-MM-dd
		return Integer.parseInt(dateStr.split(DateFormator.SPLIT_CHAR.toString())[2]);
	}

	public static boolean isEnDateFormat(String str) {
		String rex = "((((02|2)\\/29)\\/(19|20)(([02468][048])|([13579][26])))|((((0?[1-9])|(1[0-2]))\\/((0?[1-9])|(1\\d)|(2[0-8])))|((((0?[13578])|(1[02]))\\/31)|(((0?[1,3-9])|(1[0-2]))\\/(29|30))))\\/((20[0-9][0-9])|(19[0-9][0-9])|(18[0-9][0-9])))";
		Pattern regex = Pattern.compile(rex);
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	private static Map<String, DateFormator> defaultDateFormatMap = new HashMap<String, DateFormator>();
	static {
		defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}", DateFormator.YEAR_MONTH_DAY);
		defaultDateFormatMap.put("[0-9]{4}[0-9]{1,2}[0-9]{1,2}", DateFormator.YEARMONTHDAY);
		defaultDateFormatMap.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}", DateFormator.YEAR_MONTH_DAY_EU);
		defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
				DateFormator.YEAR_MONTH_DAY_HH_MM_SS);
		defaultDateFormatMap.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}",
				DateFormator.YEAR_MONTH_DAY_H_M_S_EU);
		defaultDateFormatMap.put("[0-9]{4}-[0-9]{1,2}", DateFormator.YEAR_MONTH);
		defaultDateFormatMap.put("[0-9]{4}/[0-9]{1,2}", DateFormator.YEAR_MONTH_EU);
		defaultDateFormatMap
				.put("((((02|2)/29)/(19|20)(([02468][048])|([13579][26])))|((((0?[1-9])|(1[0-2]))/((0?[1-9])|(1\\d)|(2[0-8])))|((((0?[13578])|(1[02]))/31)|(((0?[1,3-9])|(1[0-2]))/(29|30))))/((20[0-9][0-9])|(19[0-9][0-9])|(18[0-9][0-9])))",
						DateFormator.MONTH_DAY_YEAR_EU);
	}

	public static void main(String... strings) {
		// String datastr = DateFormator.toString(new Date(), DateFormator.YEAR_MONTH_DAY_HH_MM_SS);
		// String waitDes = "31237900000000000000030031" + "2011-08-04 12:00:57";
		// // System.out.println(waitDes.length());
		// String desStr = ServiceUtils.encodeToDes3(waitDes);
		// // System.out.println(desStr);
		//
		// String encodestr = ServiceUtils.decodeFromDes3(desStr);
		// String sid = org.apache.commons.lang.StringUtils.substring(encodestr, 5, 20);
		// // System.out.println("sid1="+sid);
		// BigDecimal dg = new BigDecimal(sid);
		// // System.out.println(dg.longValue());
		// String dateStr = org.apache.commons.lang.StringUtils.substring(encodestr, 20, 39);
		// // System.out.println(dateStr);
		// DateFormator.toDate(dateStr, DateFormator.YEAR_MONTH_DAY_HH_MM_SS);
		// String email = org.apache.commons.lang.StringUtils.substring(encodestr, 39);
		// // System.out.println(email);
		String time = "2007-01-04 00:00:00.0";
		
		// System.out.println(time.subSequence(0, time.length()-2));


	}

	
}
