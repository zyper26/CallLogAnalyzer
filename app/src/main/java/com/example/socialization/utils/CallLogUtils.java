package com.example.socialization.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

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
            SocialScore socialScore = SocialScore.getInstance(context);
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

//    public ArrayList<CallLogInfo> getMissedCalls(){
//        if(mainList == null) {
//            loadData();
//            missedCallList = updateSocialStatusList(missedCallList);
//        }
//        return missedCallList;
//    }

//    public long[] getAllCallState(String number){
//        long output[] = new long[2];
//        for(CallLogInfo callLogInfo : mainList){
//            if(callLogInfo.getNumber().equals(number)){
//                output[0]++;
//                if(Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE)
//                    output[1]+= callLogInfo.getDuration();
//            }
//        }
//        return output;
//    }
//
//    public long[] getIncomingCallState(String number){
//        long output[] = new long[2];
//        for(CallLogInfo callLogInfo : incomingCallList){
//            if(callLogInfo.getNumber().equals(number)){
//                output[0]++;
//                output[1]+= callLogInfo.getDuration();
//            }
//        }
//        return output;
//    }
//
//    public long[] getOutgoingCallState(String number){
//        long output[] = new long[2];
//        for(CallLogInfo callLogInfo : outgoingCallList){
//            if(callLogInfo.getNumber().equals(number)){
//                output[0]++;
//                output[1]+= callLogInfo.getDuration();
//            }
//        }
//        return output;
//    }

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

//    public long getNumberMissedPerWeekOfNumber(String number, int WeekNumber){
//        long result=0;
//        LocalDateTime input = LocalDateTime.now();;
//        DayOfWeek day = input.getDayOfWeek();
//        LocalDateTime endOfLastWeek = input.minusWeeks(WeekNumber).with(day);
//        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
//        long endOfLastWeekMilli = endOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
//        LocalDateTime startOfLastWeek = endOfLastWeek.minusDays(6);
//        long startOfLastWeekMilli = startOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
//        for(CallLogInfo callLogInfo : mainList){
//            if(callLogInfo.getNumber().equals(number)){
//                if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
//                    if (Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.MISSED_TYPE || (callLogInfo.getDuration()==0 && Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.OUTGOING_TYPE)){
//                        result++;
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    public long getNumberMissedPerWeek(int WeekNumber){
//        long result = 0;
//        LocalDateTime input = LocalDateTime.now();;
//        DayOfWeek day = input.getDayOfWeek();
//        LocalDateTime endOfLastWeek = input.minusWeeks(WeekNumber).with(day);
//        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
//        long endOfLastWeekMilli = endOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
//        LocalDateTime startOfLastWeek = endOfLastWeek.minusDays(6);
//        long startOfLastWeekMilli = startOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
//        for(CallLogInfo callLogInfo : mainList){
//            if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
//                if (Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.MISSED_TYPE || (callLogInfo.getDuration()==0 && Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.OUTGOING_TYPE))
//                    result++;
//            }
//        }
//        return result;
//    }


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
