package com.example.socialization.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.SocialScore;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class CallLogUtils {

    private static String TAG = "CallLogUtils";
    private static CallLogUtils instance;
    private Context context;
    private ArrayList<CallLogInfo> mainList = null;
    private ArrayList<CallLogInfo> missedCallList = null;
    private ArrayList<CallLogInfo> outgoingCallList = null;
    private ArrayList<CallLogInfo> incomingCallList = null;
    private ArrayList<CallLogInfo> socialCallList = null;

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
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    incomingCallList.add(callLogInfo);
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
            int cnt = 0;
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:mainList) {
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
            mainList = temp;
        }
        return mainList;
    }

    public ArrayList<CallLogInfo> getMissedCalls(){
        if(mainList == null) {
            loadData();
            int cnt=0;
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:missedCallList) {
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
            missedCallList = temp;
        }
        return missedCallList;
    }

    public ArrayList<CallLogInfo> getIncomingCalls(){
        if(mainList == null) {
            loadData();
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            int cnt=0;
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:incomingCallList) {
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
            incomingCallList = temp;
        }
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null) {
            loadData();
            int cnt=0;
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:outgoingCallList) {
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
            outgoingCallList = temp;
        }
        return outgoingCallList;
    }

    public long[] getAllCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                if(Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE)
                    output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getIncomingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : incomingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getOutgoingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : outgoingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
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

    public long getTotalNumberOfWeeks(long start_day){
        long numberOfWeeks = 0;
        if(mainList == null)
            readCallLogs();
//        Log.d(TAG, "getTotalNumberOfWeeks2: "+totalLogs);
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

    public long[] getPerWeekDatesRange(int WeekNumber,long start_day){
        long[] result = new long[2];
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime startOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        startOfLastWeek = startOfLastWeek.toLocalDate().atStartOfDay();

        long endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        result[1] = endOfLastWeekMilli;
        result[0] = startOfLastWeekMilli;
        return result;
    }

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

    public long getNumberMissedPerWeekOfNumber(String number, int WeekNumber){
        long result=0;
        LocalDateTime input = LocalDateTime.now();;
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
        long endOfLastWeekMilli = endOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        LocalDateTime startOfLastWeek = endOfLastWeek.minusDays(6);
        long startOfLastWeekMilli = startOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getNumber().equals(number)){
                if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
                    if (Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.MISSED_TYPE || (callLogInfo.getDuration()==0 && Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.OUTGOING_TYPE)){
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public long getNumberMissedPerWeek(int WeekNumber){
        long result = 0;
        LocalDateTime input = LocalDateTime.now();;
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
        long endOfLastWeekMilli = endOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        LocalDateTime startOfLastWeek = endOfLastWeek.minusDays(6);
        long startOfLastWeekMilli = startOfLastWeek.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getDate()<=endOfLastWeekMilli && callLogInfo.getDate()>=startOfLastWeekMilli) {
                if (Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.MISSED_TYPE || (callLogInfo.getDuration()==0 && Integer.parseInt(callLogInfo.getCallType()) == CallLog.Calls.OUTGOING_TYPE))
                    result++;
            }
        }
        return result;
    }

    public long getLastDayToCount(long start_day){
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
//        Log.d(TAG, "getLastDayToCount1: " + input);
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(getTotalNumberOfWeeks(start_day)).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
//        Log.d(TAG, "getLastDayToCount2: " + endOfLastWeek);
        LocalDateTime startOfLastWeek = endOfLastWeek;
//        Log.d(TAG, "getLastDayToCount3: " + startOfLastWeek);
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return startOfLastWeekMilli;
    }

    public long getStartOfDay(long start_day){
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        input = input.toLocalDate().atStartOfDay();
        LocalDateTime startOfDay = input;
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public long getLastDayToCountTemp(long start_day) {
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(1).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
        LocalDateTime startOfLastWeek = endOfLastWeek;
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return startOfLastWeekMilli;
    }

    public float getHMGlobalContacts(long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
        long LastDayToCount = getLastDayToCount(start_day);
        start_day = getStartOfDay(start_day);
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
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
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
        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            String key = (String)mapElement.getKey();
            ContactDetails contactDetails = new ContactDetails();
            contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()*(float)contactDetails.getTimes();
        }
        return numerator;
    }

    public long getTotalDistinctContacts(long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();
        long LastDayToCount = getLastDayToCount(start_day);
        start_day = getStartOfDay(start_day);
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
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
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
        return distinctContactsMap.size();
    }

    public float getHMIndividualContacts(String number, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();

        long LastDayToCount = getLastDayToCount(start_day);
        start_day = getStartOfDay(start_day);

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
            if(callLogInfo.getNumber().equals(number) &&callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
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
        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            String key = (String)mapElement.getKey();
            ContactDetails contactDetails = new ContactDetails();
            contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()/(float)contactDetails.getTimes();
        }
        return numerator;
    }

    public float getHMGlobalContactsPerWeek(int WeekNumber, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();

        long LastDayToCount = getLastDayToCount(start_day);
        start_day = getStartOfDay(start_day);

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long endOfLastWeekMilli=temp[1],startOfLastWeekMilli=temp[0];

        for(CallLogInfo callLogInfo: incomingCallList) {
            if (callLogInfo.getDate() >= startOfLastWeekMilli && callLogInfo.getDate() <= endOfLastWeekMilli){
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
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>=startOfLastWeekMilli && callLogInfo.getDate() <= startOfLastWeekMilli) {
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
        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            String key = (String)mapElement.getKey();
            ContactDetails contactDetails = new ContactDetails();
            contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()/(float)contactDetails.getTimes();
        }
        return numerator;
    }


    public float getHMIndividualContactsPerWeek(String number, int WeekNumber, long start_day){
        HashMap<String, ContactDetails> distinctContactsMap = new HashMap<>();

        long[] temp = getPerWeekDatesRange(WeekNumber,start_day);
        long LastDayToCount=temp[0];
        start_day=temp[1];

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
            if(callLogInfo.getNumber().equals(number) &&callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
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
        float numerator = 0;
        for (HashMap.Entry mapElement : distinctContactsMap.entrySet()) {
            String key = (String)mapElement.getKey();
            ContactDetails contactDetails = new ContactDetails();
            contactDetails = (ContactDetails) mapElement.getValue();
            numerator += (float)contactDetails.getDuration()*(float)contactDetails.getTimes();
        }
        return numerator;
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
