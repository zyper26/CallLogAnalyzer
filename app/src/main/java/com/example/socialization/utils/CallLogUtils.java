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
import java.util.ArrayList;


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
//            Set<CallLogInfo> hash_Set = new HashSet<CallLogInfo>();
//            for(CallLogInfo callLogInfo:mainList){
//                hash_Set.add(callLogInfo);
//            }
//            System.out.println("LoadData_main_list: "+hash_Set.size());
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:mainList) {
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());
                callTemp.setSocialStatus(socialScore.getSocial(callLogInfo.getNumber(),callLogInfo.getDate()));
                temp.add(callTemp);
            }
            mainList = temp;
        }
        return mainList;
    }

    public ArrayList<CallLogInfo> getMissedCalls(){
        if(mainList == null) {
            loadData();
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:missedCallList) {
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());
                callTemp.setSocialStatus(socialScore.getSocial(callLogInfo.getNumber(),callLogInfo.getDate()));
                temp.add(callTemp);
            }
            missedCallList = temp;
        }
        return missedCallList;
    }

    public ArrayList<CallLogInfo> getIncomingCalls(){
        if(mainList == null) {
            loadData();
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:incomingCallList) {
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());
                callTemp.setSocialStatus(socialScore.getSocial(callLogInfo.getNumber(),callLogInfo.getDate()));
                temp.add(callTemp);
            }
            incomingCallList = temp;
        }
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null) {
            loadData();
            ArrayList<CallLogInfo> temp = new ArrayList<>();
            SocialScore socialScore = SocialScore.getInstance(context);
            for(CallLogInfo callLogInfo:outgoingCallList) {
                CallLogInfo callTemp = new CallLogInfo();
                callTemp.setCallType(callLogInfo.getCallType());
                callTemp.setDate(callLogInfo.getDate());
                callTemp.setDuration(callLogInfo.getDuration());
                callTemp.setName(callLogInfo.getName());
                callTemp.setNumber(callLogInfo.getNumber());
                callTemp.setSocialStatus(socialScore.getSocial(callLogInfo.getNumber(),callLogInfo.getDate()));
                temp.add(callTemp);
            }
            outgoingCallList = temp;
        }
        return outgoingCallList;
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
        long totalRemainingLogs = 0, numberOfWeeks = 0;
        if(mainList == null)
            readCallLogs();
        for(CallLogInfo callLogInfo:mainList) {
            if(callLogInfo.getDate()<start_day){
                totalRemainingLogs++;
            }
        }
//        System.out.println("TotalLogs: " + totalRemainingLogs);
        numberOfWeeks = totalRemainingLogs / 7;
        long remaining_days = totalRemainingLogs % 7;
        return Math.max(numberOfWeeks,8);
    }

    //number.getClass().getSimpleName() //print type of object

    public long[] getNumberAndDuration(int WeekNumber, long start_day){
        long result[] = new long[2];
        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime startOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        startOfLastWeek = startOfLastWeek.toLocalDate().atStartOfDay();

        long endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

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

        LocalDateTime input = Instant.ofEpochMilli(start_day).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime startOfLastWeek = input.minusWeeks(WeekNumber).with(day);
        startOfLastWeek = startOfLastWeek.toLocalDate().atStartOfDay();

        long endOfLastWeekMilli = input.minusWeeks(WeekNumber-1).with(day).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

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
//        Log.d(TAG, "getLastDayToCount1: " + input);;
        DayOfWeek day = input.getDayOfWeek();
        LocalDateTime endOfLastWeek = input.minusWeeks(8).with(day);
        endOfLastWeek = endOfLastWeek.toLocalDate().atStartOfDay();
//        Log.d(TAG, "getLastDayToCount2: " + endOfLastWeek);
        LocalDateTime startOfLastWeek = endOfLastWeek;
//        Log.d(TAG, "getLastDayToCount3: " + startOfLastWeek);
        long startOfLastWeekMilli = startOfLastWeek.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return startOfLastWeekMilli;
    }
}
