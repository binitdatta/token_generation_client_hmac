package com.wmp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DateTimeUtil {
    private DateTimeUtil() {
        super();
    }

    public static void main(final String... args) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime utc = DateTimeUtil.toUtc(now);

        System.out.println("Now: " + now);
        System.out.println("UTC: " + utc);
    }

    public static LocalDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
        final ZonedDateTime zonedtime = time.atZone(fromZone);
        final ZonedDateTime converted = zonedtime.withZoneSameInstant(toZone);
        return converted.toLocalDateTime();
    }

    public static LocalDateTime toZone(final LocalDateTime time, final ZoneId toZone) {
        return DateTimeUtil.toZone(time, ZoneId.systemDefault(), toZone);
    }

    public static LocalDateTime toUtc(final LocalDateTime time, final ZoneId fromZone) {
        return DateTimeUtil.toZone(time, fromZone, ZoneOffset.UTC);
    }

    public static LocalDateTime toUtc(final LocalDateTime time) {
        return DateTimeUtil.toUtc(time, ZoneId.systemDefault());
    }
}