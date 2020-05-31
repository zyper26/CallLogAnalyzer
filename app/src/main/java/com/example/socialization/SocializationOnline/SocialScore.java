package com.example.socialization.SocializationOnline;

import android.content.Context;
import android.util.Log;

import com.example.socialization.Biases.KnownUnknownBiases;
import com.example.socialization.Biases.PastSocialContactBias;
import com.example.socialization.Biases.WeekDayBiases;
import com.example.socialization.utils.CallLogUtils;

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

    public float getHMIndividualPerWeek(String number, long start_day){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(start_day);
        long N = 1;
        float[] ND;
        float score = 0;
        for (int i=1; i<=numberOfWeeks; i++){
            ND = callLogUtils.getHMIndividualContactsPerWeek(number, i, start_day);
            Log.d(TAG, "getHMIndividualPerWeek: " + ND[0]+" "+ND[1]);
            score += (1/(float)N)*(ND[0]*ND[1]);
            N+=1;
        }
        return score;
    }

    public Boolean getSocialScoreWithBiases(String number,long start_day){
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

        HMIndividualUsersPerWeek = HMIndividualUsersPerWeek + biases_value[1]*(duration1[1]) + biases_value[1]*(duration1[0]);

        HMIndividualUsersPerWeek += PastSocialContactBias.getInstance(context).getDifference(number,start_day);

        float[] result = new float[2];
        result[0] = HMIndividualUsersPerWeek;
        result[1] = HMTotalUsers;
        Log.d(TAG, "getSocialvaluespercentage: " + HMIndividualUsersPerWeek/HMTotalUsers );

        return result[0]*10>result[1];
    }

}
