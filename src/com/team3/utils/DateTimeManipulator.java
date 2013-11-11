package com.team3.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint("SimpleDateFormat")
public class DateTimeManipulator {

	public static String getCurrentDate() {
		Calendar team3Calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(team3Calendar.getTime());
	}
	
	public static String getCurrentTime() {
		Calendar team3Calendar = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		return timeFormat.format(team3Calendar.getTime());
	}
	
	
}
