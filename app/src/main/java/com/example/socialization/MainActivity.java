package com.example.socialization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    public static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button socializing_calls_button = findViewById(R.id.socializingOnline);
        socializing_calls_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, SocializingOnlineActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        final Button socializing_apps_button = findViewById(R.id.socializingApps);
        socializing_apps_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, SocializingOnlineAppsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

}
