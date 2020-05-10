package com.example.socialization;

import android.content.Context;

import com.example.socialization.utils.CallLogUtils;

import java.util.ArrayList;

public class StatisticsFragment {
    private int GlobalScore = 0;
    private int IndividualScore = 0;
    private Context context;
    private static StatisticsFragment instance;

    private StatisticsFragment(Context context){
        this.context = context;
    }

    public static StatisticsFragment getInstance(Context context){
        if(instance == null)
            instance = new StatisticsFragment(context);
        return instance;
    }

    public long[] getGlobalScore1(long start_day){                              // Number of times and duration of calls
        long result[] = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long LastDayToCount = callLogUtils.getLastDayToCount(start_day);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        long totalCalls = 0,totalDuration = 0;
        for(CallLogInfo callLogInfo: incomingCalls) {
            if (callLogInfo.getDate() > LastDayToCount){
                totalCalls++;
                totalDuration += callLogInfo.getDuration();
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>LastDayToCount) {
                totalCalls++;
                totalDuration += callLogInfo.getDuration();
            }
        }
        result[0] = totalCalls;
        result[1] = totalDuration;
        System.out.println("getGlobalScore1: "+ result[1]+ " " +result[0]);
        System.out.println("getGlobalScore1: "+ result[1]*result[0]);
        return result;
    }

    public float getGlobalScore2(){                                               //number of missed and unpicked calls
        long[] result = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> missedCalls = callLogUtils.getMissedCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        int totalCalls = 0;
        totalCalls = missedCalls.size();
        for(CallLogInfo callLogInfo: outgoingCalls){
            if(callLogInfo.getDuration() == 0) {
                totalCalls++;
            }
        }
        result[0] = totalCalls;
        return (float)result[0];
    }

    public float getGlobalScore3(){                                                //Per Week score of score1
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
        long N = numberOfWeeks;
        long[] ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getNumberAndDuration(i);
            score += (float)(10/N)*((float)ND[1]/(float)ND[0]);
            N-=1;
        }
        return score;
    }

    public float getGlobalScore4(){                                                //Per Week score of score2
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
        long N = numberOfWeeks;
        long ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getNumberMissedPerWeek(i);
            score += (float)(10/N)*((float)ND);
            N-=1;
        }
        return score;
    }

    public long[] getIndividualScore1(String number, long start_day){
        long[] result = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long LastDayToCount = callLogUtils.getLastDayToCount(start_day);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        long totalCalls = 0;
        long totalDuration = 0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if (callLogInfo.getDate() > LastDayToCount)
                if(callLogInfo.getNumber().equals(number)) {
                    totalCalls++;
                    totalDuration += callLogInfo.getDuration();
                }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDate() > LastDayToCount)
                if(callLogInfo.getNumber().equals(number)) {
                    if (callLogInfo.getDuration() > 0) {
                        totalCalls++;
                        totalDuration += callLogInfo.getDuration();
                    }
                }
        }
        result[0] = totalCalls;
        result[1] = totalDuration;
        System.out.println("ContactNumber: "+number);
        System.out.println("getIndividualScore1: "+ result[1]*result[0]);
        return result;
    }

    public float getIndividualScore2(String number){
        long[] result = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> missedCalls = callLogUtils.getMissedCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        int totalCalls = 0;
        for(CallLogInfo callLogInfo: missedCalls){
            if(callLogInfo.getNumber().equals(number)) {
                totalCalls++;
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if(callLogInfo.getNumber().equals(number)) {
                if (callLogInfo.getDuration() == 0) {
                    totalCalls++;
                }
            }
        }
        result[0] = totalCalls;
        return (float)result[0];
    }

    public float getIndividualScore3(String number){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
        long N = numberOfWeeks;
        long[] ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
                ND = callLogUtils.getNumberAndDurationOfNumber(number, i);
                score += (float)(10/N)*((float)ND[1]/(float)ND[0]);
                N-=1;
        }
        return score;
    }

    public float getIndividualScore4(String number){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
        long N = numberOfWeeks;
        long ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getNumberMissedPerWeekOfNumber(number,i);
            score += (float)(10/N)*((float)ND);
            N-=1;
        }
        return score;
    }

    public float getPercentageOfCalls(String number){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        int totalCalls = 0, Calls=0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if(callLogInfo.getNumber().equals(number)) {
                Calls++;
            }
            totalCalls++;
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDuration() > 0) {
                if(callLogInfo.getNumber().equals(number)) {
                    Calls++;
                }
                totalCalls++;
            }
        }
        System.out.println("getPercentageOfCalls: " + (float)Calls/(float)totalCalls);
        return (float)Calls/(float)totalCalls;
    }

    public long[][] getScore1(String number, long start_day){
        long[][] score = new long[2][2];
        long[] individual_result = new long[2];
        long[] global_result = new long[2];
        individual_result = getIndividualScore1(number, start_day);
        global_result = getGlobalScore1(start_day);
        score[0] = individual_result;
        score[1] = global_result;
        return score;
    }

    public float getScore2(String number){
        return getIndividualScore2(number)/getGlobalScore2();
    }

    public float getScore3(String number){
        return getIndividualScore3(number)/getGlobalScore3();
    }

    public float getScore4(String number){
        return getIndividualScore4(number)/getGlobalScore4();
    }

    public Boolean getSocial(String number, long start_day){
        long[][] result = getScore1(number, start_day);
        Boolean score1 = (result[0][0]*result[0][1])> (result[1][0]/(float)result[1][1]) *(result[1][0]*result[1][1]);
        Boolean score2;
        Boolean score3 = (result[0][0] > 0.3*result[1][0]);
        if( score1 || score3 )
            return true;
        else return false;
    }
}
