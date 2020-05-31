package com.example.socialization.SocializationOnline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.R;
import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SocialScoreStatistics extends AppCompatActivity {

    private static final String TAG = "SocialScoreStatistics";
    TextView textViewIndividualCallCountTotal,textViewIndividualCallDurationsTotal,
            textViewIndividualCallCountIncoming,textViewIndividualCallDurationsIncoming,
            textViewIndividualCallDurationsOutgoing,textViewIndividualCallCountOutgoing,
            textViewGlobalCallCountTotal,textViewGlobalCallDurationsTotal,
            textViewGlobalCallCountIncoming,textViewGlobalCallDurationsIncoming,
            textViewGlobalCallDurationsOutgoing,textViewGlobalCallCountOutgoing,

            textViewIndividualScorePerWeekCallDurations, textViewHMTotalCallDurations,
            textViewWeekDayBias,textViewWeekEndBias,
            textViewKnownBias,textViewUnknownBias,

            textViewSocialPercentage,
            textViewNumber,textViewName,textViewDistinctContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_score_statistics);
        textViewIndividualCallCountTotal = findViewById(R.id.textViewIndividualCallCountTotal);
        textViewIndividualCallDurationsTotal = findViewById(R.id.textViewIndividualCallDurationsTotal);
        textViewIndividualCallCountIncoming = findViewById(R.id.textViewIndividualCallCountIncoming);
        textViewIndividualCallDurationsIncoming = findViewById(R.id.textViewIndividualCallDurationsIncoming);
        textViewIndividualCallDurationsOutgoing = findViewById(R.id.textViewIndividualCallDurationsOutgoing);
        textViewIndividualCallCountOutgoing = findViewById(R.id.textViewIndividualCallCountOutgoing);
        textViewGlobalCallCountTotal = findViewById(R.id.textViewGlobalCallCountTotal);
        textViewGlobalCallDurationsTotal = findViewById(R.id.textViewGlobalCallDurationsTotal);
        textViewGlobalCallCountIncoming = findViewById(R.id.textViewGlobalCallCountIncoming);
        textViewGlobalCallDurationsIncoming = findViewById(R.id.textViewGlobalCallDurationsIncoming);
        textViewGlobalCallDurationsOutgoing = findViewById(R.id.textViewGlobalCallDurationsOutgoing);
        textViewGlobalCallCountOutgoing = findViewById(R.id.textViewGlobalCallCountOutgoing);
        textViewIndividualScorePerWeekCallDurations = findViewById(R.id.textViewIndividualScorePerWeekCallDurations);
        textViewHMTotalCallDurations = findViewById(R.id.textViewHMTotalCallDurations);
        textViewWeekDayBias = findViewById(R.id.textViewWeekdayBias);
        textViewWeekEndBias = findViewById(R.id.textViewWeekendBias);
        textViewKnownBias = findViewById(R.id.textViewKnownBias);
        textViewUnknownBias = findViewById(R.id.textViewUnknownBias);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewName = findViewById(R.id.textViewName);
        textViewDistinctContacts = findViewById(R.id.textViewDistinctContacts);
//        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        initValues();
    }

    private void initValues(){
        String number = getIntent().getStringExtra("number");
        String name = getIntent().getStringExtra("name");
        long start_day = getIntent().getLongExtra("date",0);
//        ArrayList<Parcelable> callLogInfo1 = getIntent().getParcelableArrayListExtra("CallLogInfo");
//        long duration= getIntent().getLongExtra("duration",0);
        if(number == null){
            finish();
            return;
        }
        CallLogUtils callLogUtils = CallLogUtils.getInstance(this);

//        ------------ Individual Scores ----------------
        CallLogInfo callLogInfo = new CallLogInfo();
        ArrayList<CallLogInfo> allCallLogs = callLogUtils.readCallLogs();
//        allCallLogs = callLogUtils.updatePastSocializingContacts(allCallLogs);
        for(CallLogInfo callLogInfo1:allCallLogs){
            if(callLogInfo1.getNumber().equals(number) && callLogInfo1.getDate() ==start_day){
                callLogInfo=callLogInfo1;
                break;
            }
        }
        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);

//        ------------------------------------------------------------

//       ------------------------- Assign Individual Values ------------------
        textViewIndividualCallCountTotal.setText(String.valueOf(callLogInfo.getCallIncomingOutgoingCount()));
        textViewIndividualCallDurationsTotal.setText(Utils.formatSeconds(callLogInfo.getCallIncomingOutgoingDuration()));

        textViewIndividualCallCountIncoming.setText(String.valueOf(callLogInfo.getCallIncomingCount()));
        textViewIndividualCallDurationsIncoming.setText(Utils.formatSeconds(callLogInfo.getCallIncomingDuration()));

        textViewIndividualCallCountOutgoing.setText(String.valueOf(callLogInfo.getCallOutgoingCount()));
        textViewIndividualCallDurationsOutgoing.setText(Utils.formatSeconds(callLogInfo.getCallOutgoingDuration()));

//       ----------------------------- Assign Global Values ---------------------
        textViewGlobalCallCountTotal.setText(String.valueOf(callLogInfo.getTotalIncomingOutgoingCount()));
        textViewGlobalCallDurationsTotal.setText(Utils.formatSeconds(callLogInfo.getTotalIncomingOutgoingDuration()));

        textViewGlobalCallCountIncoming.setText(String.valueOf(callLogInfo.getTotalIncomingCount()));
        textViewGlobalCallDurationsIncoming.setText(Utils.formatSeconds(callLogInfo.getTotalIncomingDuration()));

        textViewGlobalCallCountOutgoing.setText(String.valueOf(callLogInfo.getTotalOutgoingCount()));
        textViewGlobalCallDurationsOutgoing.setText(Utils.formatSeconds(callLogInfo.getTotalOutgoingDuration()));


//        ------------------------------Socializing Scores---------------------------

        textViewIndividualScorePerWeekCallDurations.setText(String.valueOf(callLogInfo.getIndividualScore()));
        textViewHMTotalCallDurations.setText(String.valueOf(callLogInfo.getGlobalScore()));


//        ----------------------------------Biases Scores-------------------------------------------

        textViewWeekDayBias.setText(String.valueOf(callLogInfo.getWeekDayBias()));
        textViewWeekEndBias.setText(String.valueOf(callLogInfo.getWeekEndBias()));

        textViewKnownBias.setText(String.valueOf(callLogInfo.getKnownBias()));
        textViewUnknownBias.setText(String.valueOf(callLogInfo.getUnknownBias()));

//        ---------------------------------------Name and Number------------------------------------

        textViewNumber.setText(number);
        textViewName.setText(TextUtils.isEmpty(name) ? number : name);

//        textViewNumber.setText("Anonymous");
//        textViewName.setText("Anonymous");

        textViewDistinctContacts.setText(String.valueOf(callLogInfo.getTotalDistinctContacts()+ " Last Date Taken: "+ Instant.ofEpochMilli(LastDayToCount).atZone(ZoneId.systemDefault()).toLocalDateTime()));
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
