package com.example.socialization;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SocializingOnlineAppsActivity extends AppCompatActivity {

    private static final String TAG = "SocializingOnlineAppsActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socializing_apps);
        installedApps();
    }
    public void installedApps(){
        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
        for(int i=0 ; i<packageList.size();i++){
            PackageInfo packageInfo = packageList.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
                String app = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.d(TAG, "installedApps: " + i + " " + app);
            }
            Log.d(TAG, "installedApps: "+ packageInfo);

        }
    }
}
