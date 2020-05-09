package com.example.socialization;

import android.content.Context;

import com.example.socialization.utils.CallLogUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SocialStatusScheduler{

    private Context context;
    private static SocialStatusScheduler instance;

    private SocialStatusScheduler(Context context){
        this.context = context;
    }

    public static SocialStatusScheduler getInstance(Context context){
        if(instance == null)
            instance = new SocialStatusScheduler(context);
        return instance;
    }

    public void TimerActivity() {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                //Toast.makeText(context, "Scheduler", Toast.LENGTH_SHORT).show();
                CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
                StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
                long LastDayToCount = callLogUtils.getLastDayToCount();
                ArrayList<CallLogInfo> allCalls = callLogUtils.readCallLogs();
                for(CallLogInfo callLogInfo:allCalls){
                    if(statisticsFragment.getSocial(callLogInfo.getNumber()) && callLogInfo.getDate() > LastDayToCount){
                        callLogInfo.setSocialStatus(Boolean.TRUE);
                    }
                }
            }
        };
        t.schedule(tt, 0, 10800000);
    }
}
