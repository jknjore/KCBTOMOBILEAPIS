package com.uon.KCBMobileApis.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {

    public String getCurrentDateTime(LocalDateTime localDateTime)
    {
        return getDateTime(localDateTime);
    }

    public String getCurrentDateTime() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        return  getDateTime(now);
    }

    public String getDateTime(LocalDateTime localDateTime)
    {
        // Define the formatter with English locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a", Locale.ENGLISH);

        return localDateTime.format(formatter).toUpperCase();
    }
}
