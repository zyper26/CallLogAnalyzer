package com.example.socialization.Biases;

import android.content.Context;
import android.util.Log;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.CallLogUtils;

import java.util.ArrayList;

import static com.example.socialization.utils.Utils.getLastDayToCount;
import static com.example.socialization.utils.Utils.getStartOfDay;

public class KnownUnknownBiases {
    private static final String TAG = "KnownUnknownBiases";
    public Context context;
    public static KnownUnknownBiases instance;

    private KnownUnknownBiases(Context context) {
        this.context = context;
    }

    public static KnownUnknownBiases getInstance(Context context) {
        if (instance == null)
            instance = new KnownUnknownBiases(context);
        return instance;
    }

    public int[] getNumberOfKnownUnknownContactsCount(long start_day){
        int[] result = new int[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> incomingOutgoingCallsList = callLogUtils.getIncomingOutgoingCalls();
//        Log.d(TAG, "getNumberOfKnownUnknownContactsCount: " + incomingOutgoingCallsList.size());
        long LastDayToCount = getLastDayToCount(CallLogUtils.getInstance(context).getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);

        for(CallLogInfo callLogInfo:incomingOutgoingCallsList){
             if(callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate()<=start_day) {
                 if (callLogInfo.getName() == null) {
                     result[1]++;
                 } else if (callLogInfo.getName() != null) {
                     result[0]++;
                 }
             }
        }
        Log.d(TAG, "getNumberOfKnownUnknownContactsCount: " + incomingOutgoingCallsList.size() + " " + result[0] + " " + result[1]);
        return result;
    }

    public float getUnknownBias(String number,long start_day){
        int[] KUContactCounts = getNumberOfKnownUnknownContactsCount(start_day);
        float coef = KUContactCounts[1]/(float)(KUContactCounts[0]+KUContactCounts[1]);
//        Log.d(TAG, "getUnknownBias1: " + KUContactCounts[0] + " " + KUContactCounts[1]);
        long duration = 0;

        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> incomingOutgoingCallsList = callLogUtils.getIncomingOutgoingCalls();

        long LastDayToCount = getLastDayToCount(CallLogUtils.getInstance(context).getTotalNumberOfWeeks(start_day),start_day);
//        start_day = getStartOfDay(start_day);

//        Log.d(TAG, "getUnknownBias: " + incomingOutgoingCallsList);
        for(CallLogInfo callLogInfo:incomingOutgoingCallsList){
            if(callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate()<=start_day) {
                if (callLogInfo.getNumber().equals(number)) {
                    duration += callLogInfo.getDuration();
                }
            }
        }
//        Log.d(TAG, "getUnknownBias: duration "+duration);
        return coef*duration;
    }

    public float getKnownBias(String number, long start_day){
        int[] KUContactCounts = getNumberOfKnownUnknownContactsCount(start_day);
        float coef = KUContactCounts[0]/(float)(KUContactCounts[0]+KUContactCounts[1]);
//        Log.d(TAG, "getKnownBias1: " + KUContactCounts[0] + " " + KUContactCounts[1]);
        long duration = 0;

        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> incomingOutgoingCallsList = callLogUtils.getIncomingOutgoingCalls();

        long LastDayToCount = getLastDayToCount(CallLogUtils.getInstance(context).getTotalNumberOfWeeks(start_day),start_day);
//        start_day = getStartOfDay(start_day);

//        Log.d(TAG, "getKnownBias: " + incomingOutgoingCallsList);
        for(CallLogInfo callLogInfo:incomingOutgoingCallsList){
            if(callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate()<=start_day) {
                if (callLogInfo.getNumber().equals(number)) {
                    duration += callLogInfo.getDuration();
                }
            }
        }
//        Log.d(TAG, "getKnownBias: duration "+duration);
        return coef*duration;
    }
}


