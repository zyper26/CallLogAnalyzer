package com.example.socialization;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        initComponents();
        createFileOfCallLogs();
    }

    private void initComponents(){
        if(getRuntimePermission())
            setUpViewPager();
    }

    public void createFileOfCallLogs() {
        String filename = "call_log.csv";
        CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
        ArrayList<CallLogInfo> mainList = callLogUtils.readCallLogs();
        File directoryDownload = getExternalFilesDir("CallLogCSV");
        File logDir = new File (String.valueOf(directoryDownload));
        if(!logDir.exists()) {
//            Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
            logDir.mkdirs();
        }

        File file = new File(logDir, filename);
        FileOutputStream outputStream;
        Log.d(TAG,  "createFileOfCallLogs: " + file);
        try {
            outputStream = new FileOutputStream(file,true);
//            Toast.makeText(this, "file creating", Toast.LENGTH_SHORT).show();
            for(CallLogInfo callLogInfo:mainList){
                outputStream.write((callLogInfo.getDate() + ",").getBytes());
                outputStream.write((callLogInfo.getName() + ",").getBytes());
                outputStream.write((callLogInfo.getNumber() + ",").getBytes());
                outputStream.write((callLogInfo.getCallType() + ",").getBytes());
                outputStream.write((callLogInfo.getDuration() + ",").getBytes());
                outputStream.write((callLogInfo.getSocialStatus() + ",").getBytes());
                outputStream.write((callLogInfo.getCallIncomingCount() +",").getBytes());
                outputStream.write((callLogInfo.getCallIncomingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getCallOutgoingCount() +",").getBytes());
                outputStream.write((callLogInfo.getCallOutgoingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getCallIncomingOutgoingCount() +",").getBytes());
                outputStream.write((callLogInfo.getCallIncomingOutgoingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getTotalIncomingCount() +",").getBytes());
                outputStream.write((callLogInfo.getTotalIncomingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getTotalOutgoingCount() +",").getBytes());
                outputStream.write((callLogInfo.getTotalOutgoingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getTotalIncomingOutgoingCount() +",").getBytes());
                outputStream.write((callLogInfo.getTotalIncomingOutgoingDuration() +",").getBytes());
                outputStream.write((callLogInfo.getTotalDistinctContacts() +",").getBytes());
                outputStream.write((callLogInfo.getIndividualScore() +",").getBytes());
                outputStream.write((callLogInfo.getGlobalScore() +",").getBytes());
                outputStream.write((callLogInfo.getKnownBias() +",").getBytes());
                outputStream.write((callLogInfo.getUnknownBias() +",").getBytes());
                outputStream.write((callLogInfo.getWeekDayBias() +",").getBytes());
                outputStream.write((callLogInfo.getWeekEndBias() +",").getBytes());
                outputStream.write((callLogInfo.getWeekDayDuration() +",").getBytes());
                outputStream.write((callLogInfo.getWeekEndDuration() +",").getBytes());
                outputStream.write((callLogInfo.getPastSocializingContactBias() +",").getBytes());
                outputStream.write((callLogInfo.getFinalIndividualScore() +",").getBytes());
                outputStream.write((callLogInfo.getFinalGlobalScore() +"\n").getBytes());
            }
            outputStream.close();
            Toast.makeText(this, "file created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "createFileOfCallLogs: ");
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
        adapter.addFragmentChart("TimeLine", fragment_chart);
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
