package org.openjfx.hellofx;
import java.util.Date;
import java.util.TimeZone;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
public class YourDate {
	public static String getTimezone() {
		TimeZone timeZone = TimeZone.getDefault();
		System.out.println(timeZone.getDisplayName());
		String userTimezone = timeZone.getDisplayName();
		return userTimezone;
	}
	public static void main(String[] args) {
		// Assuming the input date is in IST
        String istDateString = "2023-12-08 18:53:34";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime istDateTime = LocalDateTime.parse(istDateString, formatter);

        // Convert to ZonedDateTime in IST
        ZoneId istZone = ZoneId.of("Asia/Kolkata");
        ZonedDateTime istZonedDateTime = ZonedDateTime.of(istDateTime, istZone);

        // Convert to ZonedDateTime in UTC
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcZonedDateTime = istZonedDateTime.withZoneSameInstant(utcZone);

        // Format the result
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String utcDateString = utcZonedDateTime.format(outputFormatter);

        System.out.println("IST Date and Time: " + istDateTime);
        System.out.println("UTC Date and Time: " + utcDateString);
	}
}
