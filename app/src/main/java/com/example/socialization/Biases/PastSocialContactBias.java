package com.example.socialization.Biases;

import android.content.Context;
import android.util.Log;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.SocializationOnline.SocialScore;
import com.example.socialization.utils.CallLogUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PastSocialContactBias {
    private static final String TAG = "PastSocialContactBias";
    public Context context;
    public static PastSocialContactBias instance;

    private PastSocialContactBias(Context context) {
        this.context = context;
    }

    public static PastSocialContactBias getInstance(Context context) {
        if (instance == null)
            instance = new PastSocialContactBias(context);
        return instance;
    }

    public float getDifference(String number, long start_day){
//            long LastDayToCount = getLastDayToCount(CallLogUtils.getInstance(context).getTotalNumberOfWeeks(start_day),start_day);

        CallLogUtils callLogUtils = CallLogUtils.getInstance(context);
        ArrayList<CallLogInfo> incomingOutgoingCallsList = callLogUtils.getIncomingOutgoingCalls();

        int cnt=0;
        for(CallLogInfo callLogInfo1:incomingOutgoingCallsList){
            if(callLogInfo1.getDate()<start_day) {
                if (callLogInfo1.getSocialStatus() == Boolean.TRUE) {
                    float global_score = callLogUtils.getHMGlobalContacts(start_day);
                    float ind_Score = SocialScore.getInstance(context).getHMIndividualPerWeek(number, start_day);
                    long distinct_contact = callLogUtils.getTotalDistinctContacts(start_day);
                    Log.d(TAG, "getDifference: "+weekNumber(start_day, callLogInfo1.getDate())*(ind_Score - (global_score/(float)distinct_contact)));
                    return weekNumber(start_day, callLogInfo1.getDate())*(ind_Score - (global_score/(float)distinct_contact));
                }
            }
            cnt++;
            if(cnt>500)
                return 0;
        }
        return 0;
    }

    public float weekNumber(long start_day, long date) {
        long numberOfWeeks;
        numberOfWeeks = TimeUnit.MILLISECONDS.toDays(start_day-date)/7;
        return 1/(float)numberOfWeeks;
    }
}
