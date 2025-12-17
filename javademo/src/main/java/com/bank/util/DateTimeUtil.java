package com.bank.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date and time operations.
 */
public final class DateTimeUtil {
    private static final DateTimeFormatter DEFAULT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DEFAULT_FORMATTER);
    }

    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isWithinRange(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        return !target.isBefore(start) && !target.isAfter(end);
    }

    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS)
                .plusDays(1)
                .minusNanos(1);
    }
}


