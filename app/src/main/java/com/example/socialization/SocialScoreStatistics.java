package com.example.socialization;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SocialScoreStatistics extends AppCompatActivity {

    TextView textViewCallCountTotal,textViewCallDurationsTotal,textViewCallCountIncoming,textViewCallDurationsIncoming,
    textViewCallDurationsOutgoing,textViewCallCountOutgoing,textViewNumber,textViewName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_score_statistics);
        textViewCallCountTotal = findViewById(R.id.textViewCallCountTotal);
        textViewCallDurationsTotal = findViewById(R.id.textViewCallDurationsTotal);
        textViewCallCountIncoming = findViewById(R.id.textViewCallCountIncoming);
        textViewCallDurationsIncoming = findViewById(R.id.textViewCallDurationsIncoming);
        textViewCallDurationsOutgoing = findViewById(R.id.textViewCallDurationsOutgoing);
        textViewCallCountOutgoing = findViewById(R.id.textViewCallCountOutgoing);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewName = findViewById(R.id.textViewName);
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        initValues();
    }

    private void initValues(){
        String number = getIntent().getStringExtra("number");
        String name = getIntent().getStringExtra("name");
        if(number == null){
            finish();
            return;
        }

        CallLogUtils callLogUtils = CallLogUtils.getInstance(this);
        long data[] = callLogUtils.getAllCallState(number);
        textViewCallCountTotal.setText(data[0]+" calls");
        textViewCallDurationsTotal.setText(Utils.formatSeconds(data[1]));

        data = callLogUtils.getIncomingCallState(number);
        textViewCallCountIncoming.setText(data[0]+" calls");
        textViewCallDurationsIncoming.setText(Utils.formatSeconds(data[1]));

        data = callLogUtils.getOutgoingCallState(number);
        textViewCallCountOutgoing.setText(data[0]+" calls");
        textViewCallDurationsOutgoing.setText(Utils.formatSeconds(data[1]));

        textViewNumber.setText(number);
        textViewName.setText(TextUtils.isEmpty(name) ? number : name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
