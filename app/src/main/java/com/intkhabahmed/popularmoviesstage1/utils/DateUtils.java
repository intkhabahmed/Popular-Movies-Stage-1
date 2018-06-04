package com.intkhabahmed.popularmoviesstage1.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getFormattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY", Locale.US);
        return formatter.format(date);
    }
}
