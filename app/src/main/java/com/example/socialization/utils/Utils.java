package com.example.socialization.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {
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

        long endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        result[1] = endOfLastWeekMilli;
        result[0] = startOfLastWeekMilli;
        return result;
    }

    public static long getLastDayToCount(long number_of_weeks,long start_day){
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
//        Log.d(TAG, "getLastDayToCount1: " + input);
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(number_of_weeks).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
//        Log.d(TAG, "getLastDayToCount2: " + endOfLastWeek);
        LocalDateTime startOfLastWeek = endOfLastWeek;
//        Log.d(TAG, "getLastDayToCount3: " + startOfLastWeek);
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return startOfLastWeekMilli;
    }

    public static long getStartOfDay(long start_day){
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        input = input.toLocalDate().atStartOfDay();
        LocalDateTime startOfDay = input;
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
