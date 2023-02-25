package com.example.myapplicationjava;

import android.os.Build;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class Time {
    public static String formatForDb(int year, int month, int day)
    {
        month += 1;
        String date = "";
        date += year + "-";
        if (month<10)
            date += "0";
        date += month + "-";
        if (day<10)
            date += "0";
        date += day;
        return date;
    }
    public static int[] formatForPicker(String date)
    {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5,7)) - 1;
        int day = Integer.parseInt(date.substring(8));
        return new int[] {year, month, day};
    }

    public static String getTime(){
        Date date = new Date();
        date.setTime(date.getTime() - TimeZone.getDefault().getRawOffset());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String setUserTime(String time){
        ZoneId inputTimeZone = ZoneId.of("+0"); // create ZoneId for +3 timezone
        ZoneId outputTimeZone = ZoneId.systemDefault(); // use local timezone as output
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        ZonedDateTime inputZonedDateTime = ZonedDateTime.of(dateTime, inputTimeZone);
        ZonedDateTime outputZonedDateTime = inputZonedDateTime.withZoneSameInstant(outputTimeZone);
        return outputZonedDateTime.format(formatter);
    }
}


