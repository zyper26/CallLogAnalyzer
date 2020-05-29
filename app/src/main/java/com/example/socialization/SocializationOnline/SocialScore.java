package com.example.socialization.SocializationOnline;

import android.content.Context;
import android.util.Log;

import com.example.socialization.Biases.KnownUnknownBiases;
import com.example.socialization.Biases.PastSocialContactBias;
import com.example.socialization.Biases.WeekDayBiases;
import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import java.util.ArrayList;

import static com.example.socialization.utils.Utils.getStartOfDay;

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
        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
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

    public long[] getIndividualScore1(String number, long start_day){
        long[] result = new long[2];
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
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

    public long[][] getScore1(String number, long start_day){
        long[][] score = new long[2][2];
        score[0] = getIndividualScore1(number, start_day);                                          //Number of calls and total duration
        score[1] = getGlobalScore1(start_day);                                                      //Number of calls and total duration
        return score;
    }


    public Boolean getSocial(String number, long start_day){
//        long[][] result1 = getScore1(number, start_day);

//        Boolean score5 = (result1[0][0] > 0.3 * result1[1][0]);
        Boolean score8 = getSocialStatusUsingValues(number,start_day);

        if(score8)
            return true;
        else return false;
    }

    public Boolean getSocialStatusUsingValues(String number, long start_day){
        float[] result = getSocialScoreWithBiases(number,start_day);
        return result[0]>result[1];
    }

    public float[] getSocialScoreWithBiases(String number,long start_day){
        float HMTotalUsers = CallLogUtils.getInstance(context).getHMGlobalContacts(start_day);
        long distinctContacts = CallLogUtils.getInstance(context).getTotalDistinctContacts(start_day);

        float HMIndividualUsersPerWeek =  getHMIndividualPerWeek(number,start_day);
        HMTotalUsers/=distinctContacts;

        KnownUnknownBiases knownUnknownBiases = KnownUnknownBiases.getInstance(context);
        HMTotalUsers += knownUnknownBiases.getUnknownBias(number, start_day);
        HMIndividualUsersPerWeek += knownUnknownBiases.getKnownBias(number, start_day);

        WeekDayBiases weekDayBiases = WeekDayBiases.getInstance(context);
        float[] biases_value = weekDayBiases.getPercentageOfBiases(number,start_day);
        long[] duration1 = weekDayBiases.getDurationInWeekDay(number,start_day);

//        if(getWeekend(start_day)==1)
        HMIndividualUsersPerWeek = HMIndividualUsersPerWeek + biases_value[1]*(duration1[1]) + biases_value[1]*(duration1[0]);
//        if(getWeekend(start_day)==0)
//            HMIndividualUsersPerWeek = HMIndividualUsersPerWeek + biases_value[0]*(duration1[0]);

        if (!(HMIndividualUsersPerWeek > HMTotalUsers)){
            HMIndividualUsersPerWeek += PastSocialContactBias.getInstance(context).getDifference(number,start_day);
        }
        float[] result = new float[2];
        result[0] = HMIndividualUsersPerWeek;
        result[1] = HMTotalUsers;
        Log.d(TAG, "getSocialvaluespercentage: " + HMIndividualUsersPerWeek/HMTotalUsers );

        return result;
    }

}
