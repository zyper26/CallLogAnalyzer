package com.example.socialization;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SocialScoreStatistics extends AppCompatActivity {

    private Context context;

    public SocialScoreStatistics(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_score_statistics);
    }
}
