package com.example.socialization;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.Fragement.AllCallLogsFragment;
import com.example.socialization.Fragement.SocialContactsFragment;
import com.example.socialization.Fragement.SocializingOnlineChartFragment;
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

public class SocializingOnlineActivity extends AppCompatActivity {
    private static final String TAG = "SocializingOnlineCalls";
    CallLogViewPagerAdapter adapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socializing_online);
        ConstraintLayout content_main_layout = (ConstraintLayout) findViewById(R.id.contentView);
        mViewPager = (ViewPager) content_main_layout.findViewById(R.id.viewpager);

        if (getRuntimePermission()){
            setUpViewPager();
            createFileOfCallLogs(getDataDir());
        }
    }

    public void createFileOfCallLogs(File dataDirectory) {
        String filename = "call_log.csv";
        CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
        ArrayList<CallLogInfo> mainList = callLogUtils.readCallLogs();
        File directoryDownload = dataDirectory;
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

    protected void setUpViewPager() {
//        Toast.makeText(instance, , Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "setUpViewPager: " + mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        adapter = new CallLogViewPagerAdapter(getSupportFragmentManager());
        SocializingOnlineChartFragment fragment_chart = new SocializingOnlineChartFragment();
        AllCallLogsFragment fragment_all_calls = new AllCallLogsFragment();
        SocialContactsFragment fragment_social_calls = new SocialContactsFragment();
        adapter.addFragmentChart("Chart", fragment_chart);
        adapter.addFragmentAllCalls("All Calls",fragment_all_calls);
        adapter.addFragmentSocialCalls("Social Calls", fragment_social_calls);
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

        public void addFragmentAllCalls(String title, AllCallLogsFragment fragment){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFragmentChart(String title, SocializingOnlineChartFragment fragment) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFragmentSocialCalls(String title, SocialContactsFragment fragment){
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission Granted");
                    setUpViewPager();
                } else {
                    Log.d(TAG,"Permission Denied");

                }
                return;
            }
        }
    }
}
