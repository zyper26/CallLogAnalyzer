package com.example.socialization;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.CallLogUtils;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity  {

    public static String TAG = MainActivity.class.getSimpleName();
    CallLogViewPagerAdapter adapter;
    private ViewPager mViewPager;
    private static String MODEL_PATH = Environment.getExternalStorageDirectory().toString()+"/CONTACT_DATA/";
//    CallReceiver callReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout content_main_layout = (ConstraintLayout) findViewById(R.id.contentView);
        mViewPager = (ViewPager) content_main_layout.findViewById(R.id.viewpager);
        initComponents();
        //onStart(mViewPager);

        String filename = "call_log.csv";
        CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
        ArrayList<CallLogInfo> mainList = callLogUtils.readCallLogs();
        File directoryDownload = getDataDir();
        File logDir = new File (directoryDownload, "CallLogReader"); //Creates a new folder in DOWNLOAD directory
        if(!logDir.exists()) {
//            Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
            logDir.mkdirs();
        }

        File file = new File(logDir, filename);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file, true);
            for(CallLogInfo callLogInfo:mainList){
                outputStream.write((callLogInfo.getDate() + ",").getBytes());
                outputStream.write((callLogInfo.getName() + ",").getBytes());
                outputStream.write((callLogInfo.getNumber() + ",").getBytes());
                outputStream.write((callLogInfo.getCallType() + ",").getBytes());
                outputStream.write((callLogInfo.getDuration() + ",").getBytes());
                outputStream.write((callLogInfo.getSocialStatus() + "\n").getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    public
//    public void onStart(View v){
////        SocialStatusScheduler socialStatusScheduler = SocialStatusScheduler.getInstance(getApplicationContext());
////        socialStatusScheduler.TimerActivity();
//        CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
//        StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(getApplicationContext());
//        ArrayList<CallLogInfo> allCalls = callLogUtils.readCallLogs();
//        for(CallLogInfo callLogInfo:allCalls){
//            if(statisticsFragment.getSocial(callLogInfo.getNumber(),callLogInfo.getDate())
//                    && callLogInfo.getDate() > callLogUtils.getLastDayToCount(callLogInfo.getDate())){
//                callLogInfo.setSocialStatus(Boolean.TRUE);
//            }
//        }
//    }

    private void initComponents(){
        if(getRuntimePermission())
            setUpViewPager();
    }

    private void setUpViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        adapter = new CallLogViewPagerAdapter(getSupportFragmentManager());
        AllCallLogsFragment fragment1 = new AllCallLogsFragment();
        SocialContactsFragment fragment2 = new SocialContactsFragment();
        adapter.addFragment1("All Calls",fragment1);
        adapter.addFragment2("TimeLine", fragment2);
        mViewPager.setAdapter(adapter);
//        getDatesInWeek(1);
    }

    class CallLogViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CallLogViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment1(String title, AllCallLogsFragment fragment){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFragment2(String title, SocialContactsFragment fragment){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private boolean getRuntimePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    },100);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG,"Permission Granted");
                    setUpViewPager();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG,"Permission Denied");

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
