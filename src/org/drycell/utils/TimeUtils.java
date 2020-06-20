package org.drycell.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

	public static String capitaliseFirstLetter(String s){
		String output = s.substring(0, 1).toUpperCase() + s.substring(1);
		return output.replaceAll("_", " ");
	}
	public static String parseTimeSeconds(long seconds) {
		long milliseconds = TimeUnit.SECONDS.toMillis(seconds);
		String days = TimeUnit.MILLISECONDS.toDays(milliseconds) + "d ";
		String hours = (TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds))) + "h ";
		String min = (TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
	              TimeUnit.MILLISECONDS.toHours(milliseconds))) + "min ";
		String s = (TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))) + "s";
	      return days + hours + min + s;
	   
	}
	public static String parseTimeMinutes(long minutes) {
		long milliseconds = TimeUnit.MINUTES.toMillis(minutes);
		String days = TimeUnit.MILLISECONDS.toDays(milliseconds) + "d ";
		String hours = (TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds))) + "h ";
		String min = (TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
	              TimeUnit.MILLISECONDS.toHours(milliseconds))) + "min ";
		String s = (TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))) + "s";
	      return days + hours + min + s;
	   
	}
	public static String parseTimeMillis(long milliseconds) {
		String days = TimeUnit.MILLISECONDS.toDays(milliseconds) + "d ";
		String hours = (TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds))) + "h ";
		String min = (TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
	              TimeUnit.MILLISECONDS.toHours(milliseconds))) + "min ";
		String s = (TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))) + "s";
	      return days + hours + min + s;
	   
	}
	
}
