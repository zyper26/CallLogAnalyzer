package com.example.socialization;

import android.content.Context;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.CallLogUtils;

import java.util.ArrayList;

public class SocialScore {
    private static String TAG = "StatisticsFragment";
    private int GlobalScore = 0;
    private int IndividualScore = 0;
    private Context context;
    private static SocialScore instance;

    private SocialScore(Context context){
        this.context = context;
    }

    public static SocialScore getInstance(Context context){
        if(instance == null)
            instance = new SocialScore(context);
        return instance;
    }

    public long[] getGlobalScore1(long start_day){                                                  // Number of times and duration of calls
        long result[] = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long LastDayToCount = callLogUtils.getLastDayToCount(start_day);
        start_day = callLogUtils.getStartOfDay(start_day);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        long totalCalls = 0,totalDuration = 0;
        for(CallLogInfo callLogInfo: incomingCalls) {
            if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
                totalCalls++;
                totalDuration += callLogInfo.getDuration();
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
                totalCalls++;
                totalDuration += callLogInfo.getDuration();
            }
        }
        result[0] = totalCalls;
        result[1] = totalDuration;
        return result;                                                                              //return Number of calls and total duration
    }

//    public float getGlobalScore2(long start_day){                                                                 //number of missed and unpicked calls
//        long[] result = new long[2];
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
//        long LastDayToCount = callLogUtils.getLastDayToCount(start_day);
//        ArrayList<CallLogInfo> missedCalls = callLogUtils.getMissedCalls();
//        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
//        int totalCalls = 0;
//        totalCalls = missedCalls.size();
//        for(CallLogInfo callLogInfo: outgoingCalls){
//            if(callLogInfo.getDuration() == 0 ) {
//                totalCalls++;
//            }
//        }
//        result[0] = totalCalls;
//        return (float)result[0];
//    }

    public float getGlobalScore3(long start_day){                                                                 //Per Week score of score1
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);
        long N = numberOfWeeks;
        long[] ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getNumberAndDuration(i,start_day);
            if(ND[0]!=0)
                score += (float)(10/N)*((float)ND[1]/(float)ND[0]);
            N-=1;
        }
        return score;
    }

//    public float getGlobalScore4(){                                                //Per Week score of score2
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
//        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
//        long N = numberOfWeeks;
//        long ND;
//        float score = 0;
//        for (int i=1; i<=numberOfWeeks; i++){
//            ND = callLogUtils.getNumberMissedPerWeek(i);
//            score += (float)(10/N)*((float)ND);
//            N-=1;
//        }
//        return score;
//    }

    public long[] getIndividualScore1(String number, long start_day){
        long[] result = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long LastDayToCount = callLogUtils.getLastDayToCount(start_day);
        start_day = callLogUtils.getStartOfDay(start_day);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        long totalCalls = 0;
        long totalDuration = 0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDate() <= start_day){
                    totalCalls++;
                    totalDuration += callLogInfo.getDuration();
                }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() <= start_day){
                    totalCalls++;
                    totalDuration += callLogInfo.getDuration();
                }
        }
        result[0] = totalCalls;
        result[1] = totalDuration;
//        System.out.println("ContactNumber: "+number);
//        System.out.println("getIndividualScore1: "+ result[1]*result[0]);
        return result;
    }

//    public float getIndividualScore2(String number){
//        long[] result = new long[2];
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
//        ArrayList<CallLogInfo> missedCalls = callLogUtils.getMissedCalls();
//        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
//        int totalCalls = 0;
//        for(CallLogInfo callLogInfo: missedCalls){
//            if(callLogInfo.getNumber().equals(number)) {
//                totalCalls++;
//            }
//        }
//        for(CallLogInfo callLogInfo: outgoingCalls){
//            if(callLogInfo.getNumber().equals(number)) {
//                if (callLogInfo.getDuration() == 0) {
//                    totalCalls++;
//                }
//            }
//        }
//        result[0] = totalCalls;
//        return (float)result[0];
//    }

    public float getIndividualScore3(String number, long start_day){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);
        long N = numberOfWeeks;
        long[] ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
                ND = callLogUtils.getNumberAndDurationOfNumber(number, i, start_day);
                if(ND[0]!=0)
                    score += (float)(10/N)*((float)ND[1]/(float)ND[0]);
                N-=1;
        }
        return score;
    }

    public float getHMGlobalPerWeek(long start_day){                                                                 //Per Week score of score1
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);
        long N = 1;
        float ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getHMGlobalContactsPerWeek(i,start_day);
            score += (float)(10/N)*(ND);
            N+=1;
        }
        return score;
    }

    public float getHMIndividualPerWeek(String number, long start_day){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);
        long N = 1;
//        Log.d(TAG, "getIndividualScore3: "+numberOfWeeks);
        float ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getHMIndividualContactsPerWeek(number, i, start_day);
            score += (float)(10/N)*(ND);
            N+=1;
        }
        return score;
    }


//    public float getIndividualScore4(String number){
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
//        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks();
//        long N = numberOfWeeks;
//        long ND;
//        float score = 0;
//        for (int i=1; i<=numberOfWeeks; i++){
//            ND = callLogUtils.getNumberMissedPerWeekOfNumber(number,i);
//            score += (float)(10/N)*((float)ND);
//            N-=1;
//        }
//        return score;
//    }

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
//        System.out.println("getPercentageOfCalls: " + (float)Calls/(float)totalCalls);
        return (float)Calls/(float)totalCalls;
    }

    public long[][] getScore1(String number, long start_day){
        long[][] score = new long[2][2];
        score[0] = getIndividualScore1(number, start_day);                                          //Number of calls and total duration
        score[1] = getGlobalScore1(start_day);                                                      //Number of calls and total duration
        return score;
    }

    public float[] getScore3(String number, long start_day){
        float[] score = new float[2];
        float individualScorePerWeek = getIndividualScore3(number, start_day);                                          //Number of calls and total duration
        float globalScorePerWeek = getGlobalScore3(start_day);                                                      //Number of calls and total duration
        return score;
    }

//    public float getScore3(String number){
//        return getIndividualScore3(number)/getGlobalScore3();
//    }

//    public float getScore4(String number){
//        return getIndividualScore4(number)/getGlobalScore4();
//    }

    public Boolean getSocial(String number, long start_day){
        long[][] result1 = getScore1(number, start_day);
        float HMTotalUsers = CallLogUtils.getInstance(context).getHMGlobalContacts(start_day);
        float HMIndividualUsers = CallLogUtils.getInstance(context).getHMIndividualContacts(number,start_day);
        long distinctContacts = CallLogUtils.getInstance(context).getTotalDistinctContacts(start_day);

        float HMTotalUsersPerWeek = getHMGlobalPerWeek(start_day);
        float HMIndividualUsersPerWeek =  getHMIndividualPerWeek(number,start_day);
//        Boolean score1 = (result[0][0]*result[0][1])> (result[1][1]/(float)result[1][0]) *(result[1][0]*result[1][1]);
//        Boolean score1 = Boolean.FALSE;
//        if(result1[0][0]==0||result1[1][0]==0)
//            score1 = Boolean.FALSE;
//        else if((result1[0][1])/(result1[0][0]) > (result1[1][1]/result1[1][0]))
//                score1 = Boolean.TRUE;

//        Boolean score3 = result3[0] > 0.5 * result3[1];
//        Boolean score6 = (HMIndividualUsersPerWeek > HMTotalUsersPerWeek/distinctContacts);
//        Log.d(TAG, "getSocial: "+number+ " score: " +score1 + " " + score3 + " " + score5);
//        Boolean score7 = (HMIndividualUsers>HMTotalUsers/distinctContacts);

        float[] biases_value = Biases.getInstance(context).getPercentageOfBiases(number,start_day);


        Boolean score5 = (result1[0][0] > 0.3 * result1[1][0]);
        Boolean score8 = (HMIndividualUsersPerWeek>HMTotalUsers/distinctContacts);
        if( score5 || score8)
            return true;
        else return false;
    }



}
