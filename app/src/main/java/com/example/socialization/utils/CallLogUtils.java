package com.example.socialization.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.example.socialization.Biases.KnownUnknownBiases;
import com.example.socialization.Biases.PastSocialContactBias;
import com.example.socialization.Biases.WeekDayBiases;
import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.SocializationOnline.SocialScore;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.socialization.utils.Utils.getLastDayToCount;
import static com.example.socialization.utils.Utils.getPerWeekDatesRange;
import static com.example.socialization.utils.Utils.getStartOfDay;
import static java.lang.Math.min;


public class CallLogUtils {

    private static String TAG = "CallLogUtils";
    private static CallLogUtils instance;
    private Context context;
    private ArrayList<CallLogInfo> mainList = null;
    private ArrayList<CallLogInfo> missedCallList = null;
    private ArrayList<CallLogInfo> outgoingCallList = null;
    private ArrayList<CallLogInfo> incomingCallList = null;
    private ArrayList<CallLogInfo> socialCallList = null;
    private ArrayList<CallLogInfo> incomingOutgoingList = null;

    private CallLogUtils(Context context) {
        this.context = context;
    }

    public static CallLogUtils getInstance(Context context) {
        if (instance == null)
            instance = new CallLogUtils(context);
        return instance;
    }

    private void loadData() {
        mainList = new ArrayList<>();
        missedCallList = new ArrayList<>();
        outgoingCallList = new ArrayList<>();
        incomingCallList = new ArrayList<>();
        socialCallList = new ArrayList<>();
        incomingOutgoingList = new ArrayList<>();

        String projection[] = {"_id", CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME};
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        if(cursor == null){
            Log.d("CALLLOG","cursor is null");
            return;
        }

        if(cursor.getCount() == 0){
            Log.d("CALLLOG","cursor size is 0");
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CallLogInfo callLogInfo = new CallLogInfo();
            callLogInfo.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            callLogInfo.setNumber(cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER )));
            callLogInfo.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            callLogInfo.setSocialStatus(Boolean.FALSE);
            mainList.add(callLogInfo);
            switch(Integer.parseInt(callLogInfo.getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    outgoingCallList.add(callLogInfo);
                    if(callLogInfo.getDuration()>0){
                        incomingOutgoingList.add(callLogInfo);
                    }
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    incomingCallList.add(callLogInfo);
                    incomingOutgoingList.add(callLogInfo);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    missedCallList.add(callLogInfo);
                    break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<CallLogInfo> readCallLogs() {
        if (mainList == null) {
            loadData();
            mainList = updateSocialStatusList(mainList);
            mainList = updatePastSocializingContacts(mainList);
        }
        return mainList;
    }

    public ArrayList<CallLogInfo> getIncomingCalls(){
        if(mainList == null) {
            loadData();
            incomingCallList = updateSocialStatusList(incomingCallList);
            incomingCallList = updatePastSocializingContacts(incomingCallList);
        }
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null) {
            loadData();
            outgoingCallList = updateSocialStatusList(outgoingCallList);
            outgoingCallList = updatePastSocializingContacts(outgoingCallList);
        }
        return outgoingCallList;
    }

    public ArrayList<CallLogInfo> getIncomingOutgoingCalls(){
        if(mainList == null) {
            loadData();
            incomingOutgoingList = updateSocialStatusList(incomingOutgoingList);
            incomingOutgoingList = updatePastSocializingContacts(incomingOutgoingList);
        }
        return incomingOutgoingList;
    }

    public ArrayList<CallLogInfo> getSocialCallsOnDate(long start_day){
        ArrayList<CallLogInfo> result = new ArrayList<>();
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        input = input.toLocalDate().atStartOfDay();
        LocalDateTime end = input.plusDays(1).minusSeconds(1);
        long start_time_milli = input.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end_time_milli = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        for(CallLogInfo callLogInfo:mainList){
            if(callLogInfo.getDate()>=start_time_milli && callLogInfo.getDate()<=end_time_milli && callLogInfo.getSocialStatus()){
                result.add(callLogInfo);
            }
        }

        return result;
    }

    public ArrayList<CallLogInfo> getSocialCalls(){
        if(mainList == null) {
            loadData();
        }
//        Collections.reverse(mainList);
        for(CallLogInfo callLogInfo:mainList) {
            if(callLogInfo.getSocialStatus()) {
                socialCallList.add(callLogInfo);
            }
        }
        return socialCallList;
    }

    public long[] getIncomingCallsAndDuration(String number, long start_day){
        long LastDayToCount = Utils.getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
        long callIncomingCount = 0, callIncomingDuration = 0;
        for(CallLogInfo callLogInfo: incomingCallList){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDate() <= start_day){
                callIncomingCount++;
                callIncomingDuration += callLogInfo.getDuration();
            }
        }
        long[] result = new long[2];
        result[0] = callIncomingCount;
        result[1] = callIncomingDuration;
        return result;
    }

    public long[] getOutgoingCallsAndDuration(String number, long start_day){
        long LastDayToCount = Utils.getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
        long callOutgoingCount = 0, callOutgoingDuration = 0;
        for(CallLogInfo callLogInfo: outgoingCallList){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() <= start_day){
                callOutgoingCount++;
                callOutgoingDuration += callLogInfo.getDuration();
            }
        }
        long[] result = new long[2];
        result[0] = callOutgoingCount;
        result[1] = callOutgoingDuration;
        return result;
    }

    public long[] getTotalIncomingCallsAndDuration(long start_day){
        long LastDayToCount = Utils.getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
        long totalIncomingCount = 0, totalIncomingDuration = 0;
        for(CallLogInfo callLogInfo: incomingCallList){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getDate() <= start_day){
                totalIncomingCount++;
                totalIncomingDuration += callLogInfo.getDuration();
            }
        }
        long[] result = new long[2];
        result[0] = totalIncomingCount;
        result[1] = totalIncomingDuration;
        return result;
    }

    public long[] getTotalOutgoingCallsAndDuration(long start_day){
        long LastDayToCount = Utils.getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
        long totalOutgoingCount = 0, totalOutgoingDuration = 0;
        for(CallLogInfo callLogInfo: outgoingCallList){
            if (callLogInfo.getDate() >= LastDayToCount  &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() <= start_day){
                totalOutgoingCount++;
                totalOutgoingDuration += callLogInfo.getDuration();
            }
        }
        long[] result = new long[2];
        result[0] = totalOutgoingCount;
        result[1] = totalOutgoingDuration;
        return result;
    }

    public long getTotalNumberOfWeeks(long start_day){
        long numberOfWeeks = 0;
        if(mainList == null)
            readCallLogs();
        long minDate = start_day;
        for(CallLogInfo callLogInfo: incomingCallList) {
            minDate = min(callLogInfo.getDate(),minDate);
        }
        for(CallLogInfo callLogInfo: outgoingCallList){
            if(callLogInfo.getDuration()>0) {
                minDate = min(callLogInfo.getDate(),minDate);
            }
        }
        numberOfWeeks = TimeUnit.MILLISECONDS.toDays(start_day-minDate)/7;
        return min(numberOfWeeks,8);
    }

    public float getHMGlobalContacts(long start_day){
        HashMap<String, ContactDetails> distinctContactsMap;
        long LastDayToCount = getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);

        distinctContactsMap = getHashMap(LastDayToCount, start_day);

        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            ContactDetails contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()*(float)contactDetails.getTimes();
        }
        return numerator;
    }

    public long getTotalDistinctContacts(long start_day){
        HashMap<String, ContactDetails> distinctContactsMap;
        long LastDayToCount = getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        distinctContactsMap = getHashMap(LastDayToCount,start_day);
        return distinctContactsMap.size();
    }

    public float[] getHMIndividualContactsPerWeek(String number, int WeekNumber, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap;
        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long LastDayToCount=temp[0];
        start_day=temp[1];
        distinctContactsMap = getHashMap(LastDayToCount,start_day);
        float numerator = 0;
        float[] result = new float[2];
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
//            Log.d(TAG, "getHMIndividualContactsPerWeek: " + mapElement.getKey().getClass().getName() + " " + temp[0] + " " + temp[1]);
            if(String.valueOf(mapElement.getKey()).equals(number)) {
                ContactDetails contactDetails = (ContactDetails) mapElement.getValue();
                result[0] = contactDetails.getTimes();
                result[1] = contactDetails.getDuration();
//                Log.d(TAG, "getHMIndividualContactsPerWeekContacts: " + numerator);
            }
        }
        return result;
    }

    public ArrayList<CallLogInfo> updateSocialStatusList(ArrayList<CallLogInfo> List){

        SocialScore socialScore = SocialScore.getInstance(context);
        for(int i=0;i<List.size();i++) {
            CallLogInfo callLogInfo = List.get(i);
            CallLogInfo temp = new CallLogInfo();
            if(i<500) {
                temp.setDate(callLogInfo.getDate());
                temp.setName(callLogInfo.getName());
                temp.setNumber(callLogInfo.getNumber());
                temp.setCallType(callLogInfo.getCallType());
                temp.setDuration(callLogInfo.getDuration());
                long[] IncomingCallsAndDuration = getIncomingCallsAndDuration(callLogInfo.getNumber(), callLogInfo.getDate());
                temp.setCallIncomingCount(IncomingCallsAndDuration[0]);
                temp.setCallIncomingDuration(IncomingCallsAndDuration[1]);
                long[] OutgoingCallsAndDuration = getOutgoingCallsAndDuration(callLogInfo.getNumber(), callLogInfo.getDate());
                temp.setCallOutgoingCount(OutgoingCallsAndDuration[0]);
                temp.setCallOutgoingDuration(OutgoingCallsAndDuration[1]);
                temp.setCallIncomingOutgoingCount(IncomingCallsAndDuration[0]+OutgoingCallsAndDuration[0]);
                temp.setCallIncomingOutgoingDuration(IncomingCallsAndDuration[1]+OutgoingCallsAndDuration[1]);

                long[] TotalIncomingCallsAndDuration = getTotalIncomingCallsAndDuration(callLogInfo.getDate());
                temp.setTotalIncomingCount(TotalIncomingCallsAndDuration[0]);
                temp.setTotalIncomingDuration(TotalIncomingCallsAndDuration[1]);
                long[] TotalOutgoingCallsAndDuration = getTotalOutgoingCallsAndDuration(callLogInfo.getDate());
                temp.setTotalOutgoingCount(TotalOutgoingCallsAndDuration[0]);
                temp.setTotalOutgoingDuration(TotalOutgoingCallsAndDuration[1]);
                temp.setTotalIncomingOutgoingCount(TotalIncomingCallsAndDuration[0]+TotalOutgoingCallsAndDuration[0]);
                temp.setTotalIncomingOutgoingDuration(TotalIncomingCallsAndDuration[1]+TotalOutgoingCallsAndDuration[1]);

                temp.setTotalDistinctContacts(getTotalDistinctContacts(callLogInfo.getDate()));

                long numberOfWeeks = getTotalNumberOfWeeks(callLogInfo.getDate());
                float[] AllValues = new float[16];
                float[] WeekValues;
                int k=0;
                for (int j=1; j<=numberOfWeeks; j++){
                    WeekValues = getHMIndividualContactsPerWeek(callLogInfo.getNumber(), j, callLogInfo.getDate());
                    AllValues[k] = WeekValues[0];
                    AllValues[k+1] = WeekValues[1];
                    k+=2;
                }
                temp.setWeekFrequency1(AllValues[0]);
                temp.setWeekDuration1(AllValues[1]);
                temp.setWeekFrequency2(AllValues[2]);
                temp.setWeekDuration2(AllValues[3]);
                temp.setWeekFrequency3(AllValues[4]);
                temp.setWeekDuration3(AllValues[5]);
                temp.setWeekFrequency4(AllValues[6]);
                temp.setWeekDuration4(AllValues[7]);
                temp.setWeekFrequency5(AllValues[8]);
                temp.setWeekDuration5(AllValues[9]);
                temp.setWeekFrequency6(AllValues[10]);
                temp.setWeekDuration6(AllValues[11]);
                temp.setWeekFrequency7(AllValues[12]);
                temp.setWeekDuration7(AllValues[13]);
                temp.setWeekFrequency8(AllValues[14]);
                temp.setWeekDuration8(AllValues[15]);

                float HMIndividualUsersPerWeek = socialScore.getHMIndividualPerWeek(callLogInfo.getNumber(),callLogInfo.getDate());
                temp.setIndividualScore(HMIndividualUsersPerWeek);
                float HMTotalUsers = getHMGlobalContacts(callLogInfo.getDate())/(float)getTotalDistinctContacts(callLogInfo.getDate());
                temp.setGlobalScore(HMTotalUsers);

                KnownUnknownBiases knownUnknownBiases = KnownUnknownBiases.getInstance(context);

                temp.setUnknownBias(knownUnknownBiases.getUnknownBias(callLogInfo.getNumber(), callLogInfo.getDate()));
                temp.setKnownBias(knownUnknownBiases.getKnownBias(callLogInfo.getNumber(),callLogInfo.getDate()));

                WeekDayBiases weekDayBiases = WeekDayBiases.getInstance(context);
                float[] biases_value = weekDayBiases.getPercentageOfBiases(callLogInfo.getNumber(), callLogInfo.getDate());
                long[] duration1 = weekDayBiases.getDurationInWeekDay(callLogInfo.getNumber(), callLogInfo.getDate());
                temp.setWeekDayBias(biases_value[0]);
                temp.setWeekEndBias(biases_value[1]);
                temp.setWeekDayDuration(duration1[0]);
                temp.setWeekEndDuration(duration1[1]);

                float pastSocializingContactBias = PastSocialContactBias.getInstance(context).getDifference(callLogInfo.getNumber(), callLogInfo.getDate());
                temp.setPastSocializingContactBias(pastSocializingContactBias);

                float finalHMIndividualUsersPerWeek = HMIndividualUsersPerWeek +
                                                    knownUnknownBiases.getKnownBias(callLogInfo.getNumber(), callLogInfo.getDate()) +
                                                    biases_value[1]*(duration1[1]) + biases_value[0]*(duration1[0]);
                float finalHMTotalUsers = HMTotalUsers + knownUnknownBiases.getUnknownBias(callLogInfo.getNumber(), callLogInfo.getDate());
                temp.setFinalIndividualScore(finalHMIndividualUsersPerWeek);
                temp.setFinalGlobalScore(finalHMTotalUsers);

//                temp.setSocialStatus(finalHMIndividualUsersPerWeek*10>finalHMTotalUsers);
                temp.setSocialStatus(socialScore.getSocialScoreWithBiases(callLogInfo.getNumber(),callLogInfo.getDate()));
            }
            List.set(i,temp);
        }
        return List;
    }

    public ArrayList<CallLogInfo> updatePastSocializingContacts(ArrayList<CallLogInfo> List){
        for(int i=min(499,List.size()-1); i >=0; i--) {
            CallLogInfo callLogInfo = List.get(i);
            CallLogInfo temp = new CallLogInfo();

            temp.setDate(callLogInfo.getDate());
            temp.setName(callLogInfo.getName());
            temp.setNumber(callLogInfo.getNumber());
            temp.setCallType(callLogInfo.getCallType());
            temp.setDuration(callLogInfo.getDuration());

            temp.setCallIncomingCount(callLogInfo.getCallIncomingCount());
            temp.setCallIncomingDuration(callLogInfo.getCallIncomingDuration());
            temp.setCallOutgoingCount(callLogInfo.getCallOutgoingCount());
            temp.setCallOutgoingDuration(callLogInfo.getCallOutgoingDuration());
            temp.setCallIncomingOutgoingCount(callLogInfo.getTotalIncomingOutgoingCount());
            temp.setCallIncomingOutgoingDuration(callLogInfo.getTotalIncomingOutgoingDuration());

            temp.setTotalIncomingCount(callLogInfo.getTotalIncomingCount());
            temp.setTotalIncomingDuration(callLogInfo.getTotalIncomingDuration());
            temp.setTotalOutgoingCount(callLogInfo.getTotalOutgoingCount());
            temp.setTotalOutgoingDuration(callLogInfo.getTotalOutgoingDuration());
            temp.setTotalIncomingOutgoingCount(callLogInfo.getTotalIncomingOutgoingCount());
            temp.setTotalIncomingOutgoingDuration(callLogInfo.getTotalIncomingOutgoingDuration());

            temp.setTotalDistinctContacts(callLogInfo.getTotalDistinctContacts());

            temp.setIndividualScore(callLogInfo.getIndividualScore());
            temp.setGlobalScore(callLogInfo.getGlobalScore());

            temp.setWeekFrequency1(callLogInfo.getWeekFrequency1());
            temp.setWeekDuration1(callLogInfo.getWeekDuration1());
            temp.setWeekFrequency2(callLogInfo.getWeekFrequency2());
            temp.setWeekDuration2(callLogInfo.getWeekDuration2());
            temp.setWeekFrequency3(callLogInfo.getWeekFrequency3());
            temp.setWeekDuration3(callLogInfo.getWeekDuration3());
            temp.setWeekFrequency4(callLogInfo.getWeekFrequency4());
            temp.setWeekDuration4(callLogInfo.getWeekDuration4());
            temp.setWeekFrequency5(callLogInfo.getWeekFrequency5());
            temp.setWeekDuration5(callLogInfo.getWeekDuration5());
            temp.setWeekFrequency6(callLogInfo.getWeekFrequency6());
            temp.setWeekDuration6(callLogInfo.getWeekDuration6());
            temp.setWeekFrequency7(callLogInfo.getWeekFrequency7());
            temp.setWeekDuration7(callLogInfo.getWeekDuration7());
            temp.setWeekFrequency8(callLogInfo.getWeekFrequency8());
            temp.setWeekDuration8(callLogInfo.getWeekDuration8());

            temp.setUnknownBias(callLogInfo.getUnknownBias());
            temp.setKnownBias(callLogInfo.getKnownBias());

            temp.setWeekDayBias(callLogInfo.getWeekDayBias());
            temp.setWeekEndBias(callLogInfo.getWeekEndBias());
            temp.setWeekDayDuration(callLogInfo.getWeekDayDuration());
            temp.setWeekEndDuration(callLogInfo.getWeekEndDuration());

            float pastSocializingContactBias = PastSocialContactBias.getInstance(context).getDifference(callLogInfo.getNumber(), callLogInfo.getDate());
//            Log.d(TAG, "updatePastSocializingContacts: " + pastSocializingContactBias);
            temp.setPastSocializingContactBias(pastSocializingContactBias);

            float finalHMIndividualUsersPerWeek = callLogInfo.getFinalIndividualScore() + pastSocializingContactBias;
            float finalHMTotalUsers = callLogInfo.getFinalGlobalScore();
            temp.setFinalIndividualScore(finalHMIndividualUsersPerWeek);
            temp.setFinalGlobalScore(callLogInfo.getFinalGlobalScore());
            temp.setSocialStatus(finalHMIndividualUsersPerWeek*10>finalHMTotalUsers);
            List.set(i,temp);
        }
        return List;
    }

//    public HashMap<String, ContactDetails> getHashMapOfNumber(String number, long LastDayToCount,Long start_day){
//        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
//        for(CallLogInfo callLogInfo: incomingCallList) {
//            if (callLogInfo.getNumber().equals(number) && callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() < start_day){
//                String key = callLogInfo.getNumber();
//                long duration = callLogInfo.getDuration();
//                if(distinctContactsMap.containsKey(key)){
//                    ContactDetails contactDetails = new ContactDetails();
//                    contactDetails.setTimes(Objects.requireNonNull(distinctContactsMap.get(key)).getTimes()+1);
//                    contactDetails.setDuration(Objects.requireNonNull(distinctContactsMap.get(key)).getDuration()+duration);
//                    distinctContactsMap.put(key,contactDetails);
//                }
//                else{
//                    ContactDetails contactDetails = new ContactDetails();
//                    contactDetails.setTimes(1);
//                    contactDetails.setDuration(duration);
//                    distinctContactsMap.put(key,contactDetails);
//                }
//            }
//        }
//        for(CallLogInfo callLogInfo: outgoingCallList){
//            if(callLogInfo.getDuration()>0){
//                if (callLogInfo.getNumber().equals(number) && callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() < start_day){
//                    String key = callLogInfo.getNumber();
//                    long duration = callLogInfo.getDuration();
//                    if(distinctContactsMap.containsKey(key)){
//                        ContactDetails contactDetails = new ContactDetails();
//                        contactDetails.setTimes(Objects.requireNonNull(distinctContactsMap.get(key)).getTimes()+1);
//                        contactDetails.setDuration(Objects.requireNonNull(distinctContactsMap.get(key)).getDuration()+duration);
////                    ++distinctContactsMap.get(key);
//                        distinctContactsMap.put(key,contactDetails);
//                    }
//                    else{
//                        ContactDetails contactDetails = new ContactDetails();
//                        contactDetails.setTimes(1);
//                        contactDetails.setDuration(duration);
//                        distinctContactsMap.put(key,contactDetails);
//                    }
//                }
//            }
//        }
//
//        return distinctContactsMap;
//    }

    public HashMap<String, ContactDetails> getHashMap(long LastDayToCount,Long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
        for(CallLogInfo callLogInfo: incomingCallList) {
            if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() < start_day){
                String key = callLogInfo.getNumber();
                long duration = callLogInfo.getDuration();
                if(distinctContactsMap.containsKey(key)){
                    ContactDetails contactDetails = new ContactDetails();
                    contactDetails.setTimes(Objects.requireNonNull(distinctContactsMap.get(key)).getTimes()+1);
                    contactDetails.setDuration(Objects.requireNonNull(distinctContactsMap.get(key)).getDuration()+duration);
//                    ++distinctContactsMap.get(key);
                    distinctContactsMap.put(key,contactDetails);
                }
                else{
                    ContactDetails contactDetails = new ContactDetails();
                    contactDetails.setTimes(1);
                    contactDetails.setDuration(duration);
                    distinctContactsMap.put(key,contactDetails);
                }
            }
        }
        for(CallLogInfo callLogInfo: outgoingCallList){
            if(callLogInfo.getDuration()>0){
                if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() < start_day){
                    String key = callLogInfo.getNumber();
                    long duration = callLogInfo.getDuration();
                    if(distinctContactsMap.containsKey(key)){
                        ContactDetails contactDetails = new ContactDetails();
                        contactDetails.setTimes(Objects.requireNonNull(distinctContactsMap.get(key)).getTimes()+1);
                        contactDetails.setDuration(Objects.requireNonNull(distinctContactsMap.get(key)).getDuration()+duration);
//                    ++distinctContactsMap.get(key);
                        distinctContactsMap.put(key,contactDetails);
                    }
                    else{
                        ContactDetails contactDetails = new ContactDetails();
                        contactDetails.setTimes(1);
                        contactDetails.setDuration(duration);
                        distinctContactsMap.put(key,contactDetails);
                    }
                }
            }
        }
        return distinctContactsMap;
    }

    static class ContactDetails {
        private long times;
        private long duration;

        // constructor
        public ContactDetails() {
            this.times = times;
            this.duration = duration;
        }

        // getter
        public long getTimes() { return times; }
        public long getDuration() { return duration; }
        // setter

        public void setTimes(long times) { this.times = times; }
        public void setDuration(long duration) { this.duration = duration; }
    }

}
