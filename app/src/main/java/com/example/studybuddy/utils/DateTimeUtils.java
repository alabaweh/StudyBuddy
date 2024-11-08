package com.example.studybuddy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_TIME_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static final long HOUR_IN_MILLIS = 3600000;
    private static final long DAY_IN_MILLIS = 86400000;

    // Date formatting
    public static String formatDate(Date date) {
        return date != null ? DATE_FORMAT.format(date) : "";
    }

    public static String formatTime(Date date) {
        return date != null ? TIME_FORMAT.format(date) : "";
    }

    public static String formatDateTime(Date date) {
        return date != null ? DATE_TIME_FORMAT.format(date) : "";
    }

    // Date parsing
    public static Date parseDate(String dateStr) {
        try {
            return dateStr != null ? DATE_FORMAT.parse(dateStr) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseTime(String timeStr) {
        try {
            return timeStr != null ? TIME_FORMAT.parse(timeStr) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateTime(String dateTimeStr) {
        try {
            return dateTimeStr != null ? DATE_TIME_FORMAT.parse(dateTimeStr) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    // Validation
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }

    public static boolean isValidTime(String timeStr) {
        return parseTime(timeStr) != null;
    }

    // Time calculations
    public static long getTimeDifferenceInHours(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / HOUR_IN_MILLIS;
    }

    public static long getTimeDifferenceInDays(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / DAY_IN_MILLIS;
    }

    public static String getTimeAgo(Date date) {
        if (date == null) return "";

        long diff = new Date().getTime() - date.getTime();
        long hours = diff / HOUR_IN_MILLIS;
        long days = diff / DAY_IN_MILLIS;

        if (hours < 1) return "Just now";
        if (hours < 24) return hours + " hours ago";
        if (days < 7) return days + " days ago";
        return formatDate(date);
    }
}