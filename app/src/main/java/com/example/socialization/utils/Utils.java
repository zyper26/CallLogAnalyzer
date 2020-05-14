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
}
