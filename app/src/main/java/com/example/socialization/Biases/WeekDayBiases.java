package com.example.socialization.Biases;

import android.content.Context;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import java.util.ArrayList;

public class WeekDayBiases {
    private static final String TAG = "Biases";
    public Context context;
    public static WeekDayBiases instance;

    private WeekDayBiases(Context context) {
        this.context = context;
    }

    public static WeekDayBiases getInstance(Context context) {
        if (instance == null)
            instance = new WeekDayBiases(context);
        return instance;
    }

    public long[] getCallsInWeekDay(String number, long start_day){             //Number of calls in weekend and weekday
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);

        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
//        incomingCalls = callLogUtils.updatePastSocializingContacts(incomingCalls);
//        outgoingCalls = callLogUtils.updatePastSocializingContacts(outgoingCalls);

        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);
        long N_wd = 0, N_we = 0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDate() < start_day){
                if(Utils.getWeekend(callLogInfo.getDate()) == 1){
                    N_we ++;
                }
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() < start_day){
                if(Utils.getWeekend(callLogInfo.getDate()) == 0 ){
                    N_wd ++;
                }
            }
        }
        long[] result = new long[2];
        result[0] = N_wd;
        result[1] = N_we;
        return result;
    }

    public long[] getDurationInWeekDay(String number, long start_day){             //Number of calls in weekend and weekday
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);

        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
//        incomingCalls = callLogUtils.updatePastSocializingContacts(incomingCalls);
//        outgoingCalls = callLogUtils.updatePastSocializingContacts(outgoingCalls);

        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);
        long D_wd = 0, D_we = 0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDate() < start_day){
                if(Utils.getWeekend(callLogInfo.getDate()) == 1){
                    D_we += callLogInfo.getDuration();
                }
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() < start_day){
                if(Utils.getWeekend(callLogInfo.getDate()) == 0 ){
                    D_wd += callLogInfo.getDuration();
                }
            }
        }
        long[] result = new long[2];
        result[0] = D_wd;
        result[1] = D_we;
        return result;
    }

    public float[] getPercentageOfBiases(String number, long start_day){
        long[] NumberOfCalls = getCallsInWeekDay(number,start_day);
        float[] percentage = new float[2];
        float[] normalizer = getBiasesMultiplierForNormalization();
        percentage[0] = NumberOfCalls[0]/(float)(NumberOfCalls[0]+NumberOfCalls[1]);
        percentage[1] = NumberOfCalls[1]/(float)(NumberOfCalls[0]+NumberOfCalls[1]);
        percentage[0] = percentage[0]*normalizer[0];
        percentage[1] = percentage[1]*normalizer[1];
        return percentage;
    }

    public float[] getBiasesMultiplierForNormalization(){
        float[] normalizer = new float[2];
        normalizer[0] = 5/(float)7;
        normalizer[1] = 2/(float)7;
        return normalizer;
    }

    public void rules(){
        int bias = 0;
        float diff = 0;
        float[] bias_values = new float[2];
        if(bias_values[1]>bias_values[0]){
            bias += 2;
            diff = bias_values[1] - bias_values[0];
        }
        else if(bias_values[1]<=bias_values[0] && bias_values[1]==0){
            bias += 1;
        }

    }

}
