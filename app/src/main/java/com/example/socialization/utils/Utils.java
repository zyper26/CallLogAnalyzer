package com.example.socialization.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final String TAG = "Utils";
    public static String formatSeconds(long timeInSeconds)
    {
        int hours = (int) (timeInSeconds / 3600);
        int secondsLeft = (int) (timeInSeconds - hours * 3600);
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
            /*if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";*/

        if(hours > 0)
            formattedTime += hours+"h ";

        formattedTime += minutes+"m ";
        formattedTime += seconds+"s";

            /*if (minutes < 10)
                formattedTime += "0";
            formattedTime += minutes + ":";

            if (seconds < 10)
                formattedTime += "0";
            formattedTime += seconds ;*/

        return formattedTime;
    }

    public static int getWeekend(long time){
        LocalDateTime input = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek dow = input.getDayOfWeek();
        if(dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY)
            return 1;
        else return 0;
    }

    public static long[] getPerWeekDatesRange(int WeekNumber, long start_day){
        long[] result = new long[2];
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime startOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        startOfLastWeek = startOfLastWeek.toLocalDate().atStartOfDay();
        long endOfLastWeekMilli;
        if(WeekNumber!=1)
            endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        else
            endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//        Log.d(TAG, "getPerWeekDatesRange: " + WeekNumber + " " + Instant.ofEpochMilli(endOfLastWeekMilli).atZone(ZoneId.systemDefault()).toLocalDateTime());;
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        result[1] = endOfLastWeekMilli;
        result[0] = startOfLastWeekMilli;
        return result;
    }

    public static long getLastDayToCount(long number_of_weeks,long start_day) {
        LocalDateTime LastDayLDT = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = LastDayLDT.getDayOfWeek();
        LocalDateTime FirstDay = LastDayLDT.minusWeeks(number_of_weeks).with(day);
        FirstDay = FirstDay.toLocalDate().atStartOfDay();
        LocalDateTime FirstDayLDT = FirstDay;
        long FirstDayMilli = FirstDayLDT.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return FirstDayMilli;
    }

    public static long getStartOfDay(long start_day){
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        input = input.toLocalDate().atStartOfDay();
        LocalDateTime startOfDay = input;
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String getTime(long start_day) {
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        int hour = input.getHour();
        int minute = input.getMinute();
        if(minute==0|minute==1|minute==2|minute==3|minute==4|minute==5) {
            if(hour<10){
                return "0"+hour + ":" + minute+"0";
            }
            else if(hour>9) {
                return hour + ":" + minute + "0";
            }
        }
        if(minute<9) {
            if(hour<10){
                return "0"+hour + ":0" + minute;
            }
            else if(hour>9) {
                return hour + ":0" + minute;
            }
        }
        if(hour<10){
            return "0"+hour + ":" + minute;
        }
        else if(hour>9) {
            return hour + ":" + minute;
        }
        return hour+":"+minute;
    }

    public static long getLocalDateTimeFromString(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
