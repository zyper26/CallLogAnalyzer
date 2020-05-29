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
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.socialization.utils.Utils.getLastDayToCount;
import static com.example.socialization.utils.Utils.getPerWeekDatesRange;
import static com.example.socialization.utils.Utils.getStartOfDay;


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
//            return incomingOutgoingList;
        }
        return mainList;
    }

    public ArrayList<CallLogInfo> getIncomingCalls(){
        if(mainList == null) {
            loadData();
            incomingCallList = updateSocialStatusList(incomingCallList);
        }
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null) {
            loadData();
            outgoingCallList = updateSocialStatusList(outgoingCallList);
        }
        return outgoingCallList;
    }

    public ArrayList<CallLogInfo> getIncomingOutgoingCalls(){
        if(mainList == null) {
            loadData();
            incomingOutgoingList = updateSocialStatusList(incomingOutgoingList);
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
            minDate = Math.min(callLogInfo.getDate(),minDate);
        }
        for(CallLogInfo callLogInfo: outgoingCallList){
            if(callLogInfo.getDuration()>0) {
                minDate = Math.min(callLogInfo.getDate(),minDate);
            }
        }
        numberOfWeeks = TimeUnit.MILLISECONDS.toDays(start_day-minDate)/7;
        return Math.min(numberOfWeeks,8);
    }

    //number.getClass().getSimpleName() //print type of object



    public long[] getNumberAndDuration(int WeekNumber, long start_day){
        long result[] = new long[2];

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long endOfLastWeekMilli=temp[1],startOfLastWeekMilli=temp[0];

        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
                if (Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE && callLogInfo.getDuration()>0) {
                    result[1] += callLogInfo.getDuration();
                    result[0]++;
                }
            }
        }
        return result;
    }

    public long[] getNumberAndDurationOfNumber(String number, int WeekNumber, long start_day){
        long result[] = new long[2];

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long endOfLastWeekMilli=temp[1],startOfLastWeekMilli=temp[0];

        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getNumber().equals(number)){
                if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
                    result[0]++;
                    if (Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE)
                        result[1] += callLogInfo.getDuration();
                }
            }
        }
        return result;
    }

    public float getHMGlobalContacts(long start_day){
        HashMap<String, ContactDetails> distinctContactsMap;
        long LastDayToCount = getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);

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
        start_day = getStartOfDay(start_day);

        distinctContactsMap = getHashMap(LastDayToCount,start_day);

        return distinctContactsMap.size();
    }

    public float getHMIndividualContacts(String number, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap;

        long LastDayToCount = getLastDayToCount(getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);

        distinctContactsMap = getHashMapOfNumber(number, LastDayToCount,start_day);

        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            ContactDetails contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()/(float)contactDetails.getTimes();
        }
        return numerator;
    }

    public float getHMGlobalContactsPerWeek(int WeekNumber, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();

        start_day = getStartOfDay(start_day);

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long LastDayToCount=temp[0];
        start_day=temp[1];

        distinctContactsMap = getHashMap(LastDayToCount,start_day);

        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            ContactDetails contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()/(float)contactDetails.getTimes();
        }
        return numerator;
    }


    public float getHMIndividualContactsPerWeek(String number, int WeekNumber, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();

        start_day = getStartOfDay(start_day);

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long LastDayToCount=temp[0];
        start_day=temp[1];

        distinctContactsMap = getHashMapOfNumber(number,LastDayToCount,start_day);

        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            ContactDetails contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()*(float)contactDetails.getTimes();
        }
        return numerator;
    }

    public ArrayList<CallLogInfo> updateSocialStatusList(ArrayList<CallLogInfo> List){
        int cnt = 0;
        ArrayList<CallLogInfo> temp = new ArrayList<>();
        SocialScore socialScore = SocialScore.getInstance(context);
        for(CallLogInfo callLogInfo:List) {
            if(cnt<500) {
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());

                long[] IncomingCallsAndDuration = getIncomingCallsAndDuration(callLogInfo.getNumber(), callLogInfo.getDate());
                callTemp.setCallIncomingCount(IncomingCallsAndDuration[0]);
                callTemp.setCallIncomingDuration(IncomingCallsAndDuration[1]);
                long[] OutgoingCallsAndDuration = getOutgoingCallsAndDuration(callLogInfo.getNumber(), callLogInfo.getDate());
                callTemp.setCallOutgoingCount(OutgoingCallsAndDuration[0]);
                callTemp.setCallOutgoingDuration(OutgoingCallsAndDuration[1]);
                callTemp.setCallIncomingOutgoingCount(IncomingCallsAndDuration[0]+OutgoingCallsAndDuration[0]);
                callTemp.setCallIncomingOutgoingDuration(IncomingCallsAndDuration[1]+OutgoingCallsAndDuration[1]);

                long[] TotalIncomingCallsAndDuration = getTotalIncomingCallsAndDuration(callLogInfo.getDate());
                callTemp.setTotalIncomingCount(TotalIncomingCallsAndDuration[0]);
                callTemp.setTotalIncomingDuration(TotalIncomingCallsAndDuration[1]);
                long[] TotalOutgoingCallsAndDuration = getTotalOutgoingCallsAndDuration(callLogInfo.getDate());
                callTemp.setTotalOutgoingCount(TotalOutgoingCallsAndDuration[0]);
                callTemp.setTotalOutgoingDuration(TotalOutgoingCallsAndDuration[1]);
                callTemp.setTotalIncomingOutgoingCount(TotalIncomingCallsAndDuration[0]+TotalOutgoingCallsAndDuration[0]);
                callTemp.setTotalIncomingOutgoingDuration(TotalIncomingCallsAndDuration[1]+TotalOutgoingCallsAndDuration[1]);

                callTemp.setTotalDistinctContacts(getTotalDistinctContacts(callLogInfo.getDate()));

                float HMIndividualUsersPerWeek = socialScore.getHMIndividualPerWeek(callLogInfo.getNumber(),callLogInfo.getDate());
                callTemp.setIndividualScore(HMIndividualUsersPerWeek);
                float HMTotalUsers = getHMGlobalContacts(callLogInfo.getDate())/(float)getTotalDistinctContacts(callLogInfo.getDate());
                callTemp.setGlobalScore(HMTotalUsers);

                KnownUnknownBiases knownUnknownBiases = KnownUnknownBiases.getInstance(context);
                callTemp.setUnknownBias(knownUnknownBiases.getUnknownBias(callLogInfo.getNumber(), callLogInfo.getDate()));
                callTemp.setKnownBias(knownUnknownBiases.getKnownBias(callLogInfo.getNumber(),callLogInfo.getDate()));

                WeekDayBiases weekDayBiases = WeekDayBiases.getInstance(context);
                float[] biases_value = weekDayBiases.getPercentageOfBiases(callLogInfo.getNumber(), callLogInfo.getDate());
                long[] duration1 = weekDayBiases.getDurationInWeekDay(callLogInfo.getNumber(), callLogInfo.getDate());
                callTemp.setWeekDayBias(biases_value[0]);
                callTemp.setWeekEndBias(biases_value[1]);
                callTemp.setWeekDayDuration(duration1[0]);
                callTemp.setWeekEndDuration(duration1[1]);

                float pastSocializingContactBias = PastSocialContactBias.getInstance(context).getDifference(callLogInfo.getNumber(), callLogInfo.getDate());
                callTemp.setPastSocializingContactBias(pastSocializingContactBias);

                float finalHMIndividualUsersPerWeek = HMIndividualUsersPerWeek +
                                                    knownUnknownBiases.getKnownBias(callLogInfo.getNumber(), callLogInfo.getDate()) +
                                                    biases_value[1]*(duration1[1]) + biases_value[1]*(duration1[0]) +
                                                    pastSocializingContactBias;
                float finalHMTotalUsers = knownUnknownBiases.getUnknownBias(callLogInfo.getNumber(), callLogInfo.getDate());
                callTemp.setFinalIndividualScore(finalHMIndividualUsersPerWeek);
                callTemp.setFinalGlobalScore(finalHMTotalUsers);

                callTemp.setSocialStatus(socialScore.getSocial(callLogInfo.getNumber(), callLogInfo.getDate()));
                temp.add(callTemp);
            }
            else{
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());
                callTemp.setSocialStatus(Boolean.FALSE);
                temp.add(callTemp);
            }
            cnt++;
        }
        return temp;
    }

    public HashMap<String, ContactDetails> getHashMapOfNumber(String number, long LastDayToCount,Long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
        for(CallLogInfo callLogInfo: incomingCallList) {
            if (callLogInfo.getNumber().equals(number) && callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
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
                if (callLogInfo.getNumber().equals(number) && callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
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

    public HashMap<String, ContactDetails> getHashMap(long LastDayToCount,Long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
        for(CallLogInfo callLogInfo: incomingCallList) {
            if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
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
                if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
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
