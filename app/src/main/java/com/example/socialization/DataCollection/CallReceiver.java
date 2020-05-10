package com.example.socialization.DataCollection;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.socialization.DataBaseResolver.ContactFeatureProviderConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CallReceiver extends PhonecallReceiver {

    private static String TAG = "CallReceiver";
    int incomingCall = 1;
    int outgoingCall = 2;
    int missedCall = 3;
    private String callType;
    private long date;
    private int SocialStatus;
    private String contactName;
    private String contactNumber;
    long Duration; /* (millisecond/1000)/60 - min    (millisecond /1000)%60 - sec*/
//    long dur_min;
//    long dur_sec;
//    long DurInMin;
    int CallType;
    List<String> topPackage;
    Location location;
    double lati;
    double longi;
    public static DataInterface dataInterface = null;
    private static final int UPDATE_DB = 100;
    private static final int MAXAPP = 20;
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private static ContentResolver contentResolver;

    private  Context context;

    public CallReceiver(){
        if(dataInterface == null) {
            dataInterface = new DataInterface();
        }
    }

    public CallReceiver(Context ctx) {

        this.context = ctx;
        contentResolver = context.getContentResolver();
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            rwlock.writeLock().lock();
            rwlock.writeLock().unlock();
        }
    };

    private void addContactFeatures(long date , int callType,
                                    String contactName, String phoneNumber, long duration,
                                    Double lat, Double lon,
                                    int socialStatus){
                                    //String packageName){

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactFeatureProviderConstants.COL_1,date);
        contentValues.put(ContactFeatureProviderConstants.COL_2,callType);
        contentValues.put(ContactFeatureProviderConstants.COL_3,contactName);
        contentValues.put(ContactFeatureProviderConstants.COL_4,phoneNumber);
        contentValues.put(ContactFeatureProviderConstants.COL_5,duration);
        contentValues.put(ContactFeatureProviderConstants.COL_6,lat);
        contentValues.put(ContactFeatureProviderConstants.COL_7,lon);
        contentValues.put(ContactFeatureProviderConstants.COL_8,socialStatus);
//        contentValues.put(ContactFeatureProviderConstants.COL_9,packageName);

        if(contentResolver != null){
            contentResolver.insert(ContactFeatureProviderConstants.CONTENT_URI_1,contentValues);
        }else{
            Log.d(TAG,"Content Resolver is null");
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           /*Update Database*/
            rwlock.writeLock().lock();
            long date = dataInterface.getDate();
            int CallType = dataInterface.getGetCallType();
            String contactName = dataInterface.getName();
            String phoneNumber = dataInterface.getNumber();
            long duration = dataInterface.getDuration();
            double lat = dataInterface.getLat();
            double lon = dataInterface.getLon();
            int socialStatus = dataInterface.getSocialStatus();
//            List<String> packageName= dataInterface.getPackageName();

            addContactFeatures(date, CallType, contactName,  phoneNumber,  duration, lat, lon, socialStatus);
//            if(packageName != null){
//                for(int i = 0 ; i < packageName.size(); i++) {
//                    addContactFeatures(date, callType, contactName,  phoneNumber,  duration, lat, lon, socialStatus, packageName.get(i));
//                }
//            }else{
//                addContactFeatures(date, callType, contactName,  phoneNumber,  duration, lat, lon, socialStatus,"");
//            }

            rwlock.writeLock().unlock();
            new Thread(runnable1).start();

        }
    };



    public  final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_DB){
                new Thread(runnable).start();
            }
        }
    };



    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        rwlock.writeLock().lock();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   hh:mm a");
        CallType = CallLog.Calls.INCOMING_TYPE;
        date = start.toInstant().toEpochMilli();
        contactNumber = number;
        contactName = getContactDisplayNameByNumber(contactNumber,ctx);
        Duration = start.getTime();
        Toast.makeText(ctx, "onIncomingCallStarted"
                + "Time "+ formatter.format(start) + "Name:: "+contactName + "Num:: " + contactNumber, Toast.LENGTH_LONG).show();
        dataInterface.setDate(start.toInstant().toEpochMilli());
        dataInterface.setName(contactName);
        dataInterface.setCallType(CallType);
        dataInterface.setNumber(contactNumber);
        dataInterface.setDuration(Duration);
        this.context = ctx; /*Check the effect*/
        rwlock.writeLock().unlock();

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        rwlock.writeLock().lock();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   hh:mm a");
        date = start.toInstant().toEpochMilli();
        CallType = CallLog.Calls.OUTGOING_TYPE;
        contactNumber = number;
        contactName = getContactDisplayNameByNumber(contactNumber,ctx);
        Duration = start.getTime();
        Toast.makeText(ctx, "onOutgoingCallStarted"
                + "Time "+ formatter.format(start) + "Name:: "+contactName + "Num:: " + contactNumber, Toast.LENGTH_LONG).show();
        dataInterface.setDate(start.toInstant().toEpochMilli());
        dataInterface.setName(contactName);
        dataInterface.setCallType(CallType);
        dataInterface.setNumber(contactNumber);
        dataInterface.setDuration(Duration);
        this.context = ctx; /*Check the effect*/
        rwlock.writeLock().unlock();;
    }


    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        rwlock.writeLock().lock();
        Duration =  end.getTime() - start.getTime();
//        topPackage = getTopAppName(ctx);
        location = new Location(ctx);
        if(location != null && 1 == location.locationUpdate()){
            lati =  location.getLat();
            longi = location.getLon();
        }
        Toast.makeText(ctx, "onIncomingCallEnded" + "Duration::" + Duration + "Top Package:: "+ topPackage, Toast.LENGTH_SHORT).show();
        dataInterface.setDuration(Duration);
        dataInterface.setLat(lati);
        dataInterface.setLon(longi);
//        dataInterface.setPackage(topPackage);
        this.context = ctx; /*Check the effect*/

        rwlock.writeLock().unlock();;

        if(!dataInterface.getName().equals("Unknown number")){ /*Don't Update Training data having no contact DB entry*/
            Message message = new Message();
            message.what = UPDATE_DB;
            mHandler.sendMessage(message);
        }else{
            Toast.makeText(ctx, "onIncomingCallEnded" +"Name::->"+ dataInterface.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        rwlock.writeLock().lock();
        Duration =  end.getTime() - start.getTime();
//        topPackage = getTopAppName(ctx);
        location = new Location(ctx);
        if(location != null && 1 == location.locationUpdate()){
            lati =  location.getLat();
            longi = location.getLon();
        }
        Toast.makeText(ctx, "onOutgoingCallEnded"+ "Duration::" + Duration + "Top Package:: "+ topPackage , Toast.LENGTH_LONG).show();
        dataInterface.setDuration(Duration);
        dataInterface.setLat(lati);
        dataInterface.setLon(longi);
//        dataInterface.setPackage(topPackage);
        this.context = ctx; /*Check the effect*/

        rwlock.writeLock().unlock();

        if(!dataInterface.getName().equals("Unknown number")){ /*Don't Update Training data having no contact DB entry*/
            Message message = new Message();
            message.what = UPDATE_DB;
            mHandler.sendMessage(message);
        }else{
            Toast.makeText(ctx, "onOutgoingCallEnded" +"Name::->"+ dataInterface.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        rwlock.writeLock().lock();
        date = start.toInstant().toEpochMilli();
        CallType = missedCall;
        contactNumber = number;
        contactName = getContactDisplayNameByNumber(contactNumber,ctx);
        Duration = start.getTime();
        dataInterface.setDate(date);
        dataInterface.setName(contactName);
        dataInterface.setCallType(CallType);
        dataInterface.setNumber(contactNumber);
        dataInterface.setDuration(Duration);
        this.context = ctx; /*Check the effect*/

        rwlock.writeLock().unlock();
        Toast.makeText(ctx, "onMissedCall" , Toast.LENGTH_SHORT).show();
        if(!dataInterface.getName().equals("Unknown number")){ /*Don't Update DB having no contact DB entry*/
            Message message = new Message();
            message.what = UPDATE_DB;
            mHandler.sendMessage(message);
        }else{
            Toast.makeText(ctx, "onMissedCall" +"Name::->"+ dataInterface.getName(), Toast.LENGTH_SHORT).show();
        }

    }

    public String getContactDisplayNameByNumber(String number,Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        contactName = "Incoming call from";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, null, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                contactName = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }else{
                contactName = "Unknown number";
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return contactName;
    }

//    public static List<String> getTopAppName(Context context) {
//        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        String strName = null ;
//        List<String> packages = null;
//        final List<ActivityManager.RecentTaskInfo> runningTaskInfos;
//        try {
//              runningTaskInfos = mActivityManager.getRecentTasks(MAXAPP,0);
//              packages = new LinkedList<>();
//              for(int i = 0; i < runningTaskInfos.size() ; i++){
//                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                      strName = runningTaskInfos.get(i).topActivity.getPackageName();
//                  }
//                  packages.add(strName);
//              }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<String> packages = null;
//        ActivityManager mgr = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.AppTask>  tasks = mgr.getAppTasks();
//        String packagename;
//        String label = null;
//        for (ActivityManager.AppTask task: tasks){
//            packagename = task.getTaskInfo().baseIntent.getComponent().getPackageName();
//            packages.add(packagename);
//            try {
//                label = context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(packagename, PackageManager.GET_META_DATA)).toString();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            Log.v(TAG,packagename + ":" + label);
//        }

//        return packages;
//    }

    /*
    public String[] getForegroundPackageNameClassNameByUsageStats(Context ctx,long start, long end) {
        String packageNameByUsageStats = null;
        String classByUsageStats = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) ctx.getSystemService("usagestats");
            final UsageEvents usageEvents = mUsageStatsManager.queryEvents(start, end);
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    packageNameByUsageStats = event.getPackageName();
                    classByUsageStats = event.getClassName();
                    Log.d(TAG, "packageNameByUsageStats is" + packageNameByUsageStats + ", classByUsageStats is " + classByUsageStats);
                }
            }
        }
        return new String[]{packageNameByUsageStats,classByUsageStats};
    }

    public static String getForegroundPackage(UsageStatsManager usageStatsManager) {
        String packageName = null;

        final long INTERVAL = 1000 * 60;
        final long end = System.currentTimeMillis();
        final long begin = end - INTERVAL;
        final UsageEvents usageEvents = usageStatsManager.queryEvents(begin, end);
        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            switch (event.getEventType()) {
                case UsageEvents.Event.MOVE_TO_FOREGROUND:
                    packageName = event.getPackageName();
                    break;
                case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    if (event.getPackageName().equals(packageName)) {
                        packageName = null;
                    }
            }
        }

        return packageName;
    }

    public static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            } else {
                strName = getLollipopFGAppPackageName(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }


    private static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService("usagestats");
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    */


}