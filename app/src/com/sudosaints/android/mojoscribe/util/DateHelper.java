package com.sudosaints.android.mojoscribe.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

public class DateHelper {

	public static final String DATE_FORMAT_DMY = "dd-MM-yyyy";
	public static final String DATE_FORMAT_MMMDDYYYY= "MMM dd yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String TIME_FORMAT_12_HOUR = "KK:mm";
	public static final String DATE_TIME_FORMAT_DMY = DATE_FORMAT_DMY + " " + TIME_FORMAT;

	public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT_YMD = DATE_FORMAT_YMD + " " + TIME_FORMAT;

	public static final String DATE_FORMAT_MDY = "MM/dd/yyyy";

	public static final String UTC_FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public static final String UTC_FORMAT_DATE_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final int MILLI_TO_HOUR = 1000 * 60 * 60;

	public static String get12HourFormattedTime(Date d) {
		return new SimpleDateFormat(TIME_FORMAT_12_HOUR).format(d);
	}

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("EEEE d'th' MMMM");
		Date date = new Date();
		return ((String) dateFormat.format(date));
	}
	
	public static String getMMMDDYYYYFormattedDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT_MMMDDYYYY).format(d);
	}

	public static String getDMYFormattedDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT_DMY).format(d);
	}

	public static String getYMDFormattedDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT_YMD).format(d);
	}

	public static String getDMYFormattedTimestamp(Date d) {
		return new SimpleDateFormat(DATE_TIME_FORMAT_DMY).format(d);
	}

	public static String getYMDFormattedTimestamp(Date d) {
		return new SimpleDateFormat(DATE_TIME_FORMAT_YMD).format(d);
	}

	public static Date parseDMYDateTime(String dateTime) {
		try {
			return new SimpleDateFormat(DATE_TIME_FORMAT_DMY).parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseYMDDateTime(String dateTime) {
		try {
			return new SimpleDateFormat(DATE_TIME_FORMAT_YMD).parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseDate(String date) {
		try {
			return new SimpleDateFormat(DATE_FORMAT_DMY).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getCurrDateWithTimeZone(String timeZone) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		cal.setTime(date);
		return cal.getTime();
	}

	public static void setToStartOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		date.setTime(cal.getTimeInMillis());
	}

	public static void setToEndOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		date.setTime(cal.getTimeInMillis());
	}

	public static Date getLeftTimeWindow(Date date, int window) {
		Calendar leftTimeWindow = Calendar.getInstance();
		leftTimeWindow.setTimeInMillis(date.getTime() - window);

		return leftTimeWindow.getTime();
	}

	public static Date getRightTimeWindow(Date date, int window) {
		Calendar rightTimeWindow = Calendar.getInstance();
		rightTimeWindow.setTimeInMillis(date.getTime() + window);

		return rightTimeWindow.getTime();
	}

	public static void setToFirstDayOfPreviousWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		date.setTime(cal.getTimeInMillis());
	}

	public static void setToLastDayOfPreviousWeek() {

	}

	public static boolean validateTime(String str) throws NumberFormatException, PatternSyntaxException {
		try {
			String[] hrsAndMinsStrings = str.split(":");
			if (hrsAndMinsStrings.length != 2) {
				return false;
			}
			if (Integer.parseInt(hrsAndMinsStrings[0]) > 23 || Integer.parseInt(hrsAndMinsStrings[1]) > 59) {
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new NumberFormatException("Invalid hrs./mins. values");
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			throw new PatternSyntaxException("Invalid time format", ":", -1);
		}
		return true;
	}

	/**
	 * Compares fromDateString with toDateString
	 * 
	 * @param fromDateString
	 *            fromDate which is supposed to be initial/earliest date
	 * @param toDateString
	 *            toDate which is supposed to be final/later date
	 * @return returns true if fromDate is less than toDate otherwise false
	 * @throws NullPointerException
	 *             if fromDateString or toDateString is not parsable to
	 *             {@link java.util.Date} object
	 * @author Vishal Pawale
	 */
	public static boolean compareDateStrings(String fromDateString, String toDateString) {
		Date fromDate = parseDate(fromDateString);
		Date toDate = parseDate(toDateString);
		if (toDate.compareTo(fromDate) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Compares fromDate with toDate
	 * 
	 * @param fromDate
	 *            fromDate which is supposed to be initial/earliest date
	 * @param toDate
	 *            toDate which is supposed to be final/later date
	 * @return returns true if fromDate is less than toDate otherwise false
	 * @throws NullPointerException
	 *             if any of the date is null
	 * @author Vishal Pawale
	 */
	public static boolean compareDates(Date fromDate, Date toDate) {
		if (toDate.compareTo(fromDate) >= 0) {
			return true;
		}
		return false;
	}

	public static int getMinsFromHHMM(String hhMMString) {

		String[] tokenArray = hhMMString.split(":");
		int hours = Integer.valueOf(tokenArray[0]);
		int mins = Integer.valueOf(tokenArray[1]);
		return (hours * 60) + mins;
	}

	public static String getHHMMFromMins(int mins) {

		int hours = mins / 60;
		int remainMinute = mins % 60;
		String result = String.format("%02d", hours) + ":" + String.format("%02d", remainMinute);
		return result;
	}

	public static int getDifferenceInHrs(Date date1, Date date2) {
		return Math.round((date1.getTime() - date2.getTime()) / MILLI_TO_HOUR);
	}

	public static String getMonthForInt(int num) {

		return new DateFormatSymbols().getMonths()[num];
	}

	public static Date convertUTCToDate(String date) {
		DateFormat utcFormat = new SimpleDateFormat(UTC_FORMAT_DATE);
		Date convertedDate = null;
		try {
			convertedDate = utcFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertedDate;
	}

	public static Date convertUTCToDateWithUTCZone(String date) {
		DateFormat utcFormat = new SimpleDateFormat(UTC_FORMAT_DATE);
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date convertedDate = null;
		try {
			convertedDate = utcFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * 
	 * @param date
	 * @param timeZone
	 * @return
	 * @author Bhushan
	 */
	public static Date convertUTCToDateWithZoneFormat(String date, String timeZone) {
		SimpleDateFormat utcFormat = new SimpleDateFormat(UTC_FORMAT_DATE_WITH_ZONE);
		Date utcDate = null, localDate = null;
		try {
			utcDate = utcFormat.parse(date);
			localDate = new Date(utcDate.getTime() + TimeZone.getTimeZone(timeZone).getRawOffset() + TimeZone.getTimeZone(timeZone).getDSTSavings());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return localDate;
	}

	public static Date getUTCDateWithZoneFormat(long millis, TimeZone timeZone) {
		SimpleDateFormat utcFormat = new SimpleDateFormat(UTC_FORMAT_DATE_WITH_ZONE);
		Date utcDate = null, localDate = null;
		try {
			utcDate = utcFormat.parse(utcFormat.format(new Date(millis)));
			localDate = new Date(utcDate.getTime() + timeZone.getRawOffset() + timeZone.getDSTSavings());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return localDate;
	}

	/**
	 * 
	 * @param timeZone
	 * @return
	 * @author Bhushan
	 */
	public static Date getCurrentDateOfTimezone(String timeZone) {
		Date currentDate = new Date();
		SimpleDateFormat utcFormat = new SimpleDateFormat(UTC_FORMAT_DATE);
		utcFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		return convertUTCToDate(utcFormat.format(currentDate));
	}

	public static String getMDYFormattedDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT_MDY).format(d);
	}

	public static String getFormattedTime(Date d) {
		return new SimpleDateFormat(TIME_FORMAT).format(d);
	}

	public static String getDifferenceInHHMM(long date1, long date2) {
		List<String> diffString = new ArrayList<String>();

		long diff = date1 - date2;

		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffDays > 0) {
			diffString.add(diffDays + " day");
		}
		if (diffHours > 0) {
			diffString.add(diffHours + " hour");
		}

		diffString.add(diffMinutes + " minutes");

		return StringUtils.join(diffString, " ");
	}

}