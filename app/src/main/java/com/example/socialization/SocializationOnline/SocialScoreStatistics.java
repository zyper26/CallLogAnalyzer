package com.example.socialization.SocializationOnline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.socialization.Biases.KnownUnknownBiases;
import com.example.socialization.Biases.WeekDayBiases;
import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.R;
import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.socialization.utils.Utils.getStartOfDay;

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
//        long duration= getIntent().getLongExtra("duration",0);
        if(number == null){
            finish();
            return;
        }
        CallLogUtils callLogUtils = CallLogUtils.getInstance(this);
        start_day = getStartOfDay(start_day);

//        ------------ Individual Scores ----------------
        long LastDayToCount = Utils.getLastDayToCount(callLogUtils.getTotalNumberOfWeeks(start_day),start_day);
        start_day = getStartOfDay(start_day);
        ArrayList<CallLogInfo> incomingCalls = callLogUtils.getIncomingCalls();
        ArrayList<CallLogInfo> outgoingCalls = callLogUtils.getOutgoingCalls();
        long totalCallsIncoming = 0, totalCallsOutgoing = 0;
        long totalDurationIncoming = 0, totalDurationOutgoing=0;
        for(CallLogInfo callLogInfo: incomingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDate() <= start_day){
                totalCallsIncoming++;
                totalDurationIncoming += callLogInfo.getDuration();
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if (callLogInfo.getDate() >= LastDayToCount &&
                    callLogInfo.getNumber().equals(number) &&
                    callLogInfo.getDuration() > 0 &&
                    callLogInfo.getDate() <= start_day){
                totalCallsOutgoing++;
                totalDurationOutgoing += callLogInfo.getDuration();
            }
        }
        long totalCalls = totalCallsIncoming+totalCallsOutgoing;
        long totalDurations = totalDurationIncoming+totalDurationOutgoing;
//      ------------------------------------------------------

//        ---------------------Global Scores-----------------------
        long AllCallsIncoming = 0, AllCallsOutgoing = 0;
        long AllDurationIncoming = 0, AllDurationOutgoing=0;

        for(CallLogInfo callLogInfo: incomingCalls) {
            if (callLogInfo.getDate() >= LastDayToCount && callLogInfo.getDate() <= start_day){
                AllCallsIncoming++;
                AllDurationIncoming += callLogInfo.getDuration();
            }
        }
        for(CallLogInfo callLogInfo: outgoingCalls){
            if(callLogInfo.getDuration()>0 && callLogInfo.getDate()>=LastDayToCount && callLogInfo.getDate() <= start_day) {
                AllCallsOutgoing++;
                AllDurationOutgoing += callLogInfo.getDuration();
            }
        }

        long AllCalls = AllCallsIncoming + AllCallsOutgoing;
        long AllDurations = AllDurationIncoming + AllDurationOutgoing;

//        ------------------------------------------------------------

//       ------------------------- Assign Individual Values ------------------
        textViewIndividualCallCountTotal.setText(String.valueOf(totalCalls));
        textViewIndividualCallDurationsTotal.setText(Utils.formatSeconds(totalDurations));

        textViewIndividualCallCountIncoming.setText(String.valueOf(totalCallsIncoming));
        textViewIndividualCallDurationsIncoming.setText(Utils.formatSeconds(totalDurationIncoming));

        textViewIndividualCallCountOutgoing.setText(String.valueOf(totalCallsOutgoing));
        textViewIndividualCallDurationsOutgoing.setText(Utils.formatSeconds(totalDurationOutgoing));

//       ----------------------------- Assign Global Values ---------------------
        textViewGlobalCallCountTotal.setText(String.valueOf(AllCalls));
        textViewGlobalCallDurationsTotal.setText(Utils.formatSeconds(AllDurations));

        textViewGlobalCallCountIncoming.setText(String.valueOf(AllCallsIncoming));
        textViewGlobalCallDurationsIncoming.setText(Utils.formatSeconds(AllDurationIncoming));

        textViewGlobalCallCountOutgoing.setText(String.valueOf(AllCallsOutgoing));
        textViewGlobalCallDurationsOutgoing.setText(Utils.formatSeconds(AllDurationOutgoing));

//        --------------------------Distinct Contacts ---------------------

        long distinctContacts = callLogUtils.getTotalDistinctContacts(start_day);

//        ------------------------------Socializing Scores---------------------------

        SocialScore socialScore = SocialScore.getInstance(this);

        float HMIndividualUsersPerWeek =  socialScore.getHMIndividualPerWeek(number,start_day);
        float HMTotalCallDurations = callLogUtils.getHMGlobalContacts(start_day);

        textViewIndividualScorePerWeekCallDurations.setText(String.valueOf(HMIndividualUsersPerWeek));
        textViewHMTotalCallDurations.setText(String.valueOf(HMTotalCallDurations));


//        ----------------------------------Biases Scores-------------------------------------------

        float[] DayOfWeekBias = WeekDayBiases.getInstance(getApplicationContext()).getPercentageOfBiases(number,start_day);

        textViewWeekDayBias.setText(String.valueOf(DayOfWeekBias[0]));
        textViewWeekEndBias.setText(String.valueOf(DayOfWeekBias[1]));

        float knownBias = KnownUnknownBiases.getInstance(getApplicationContext()).getKnownBias(number, start_day);
        float unknownBias = KnownUnknownBiases.getInstance(getApplicationContext()).getUnknownBias(number, start_day);

        textViewKnownBias.setText(String.valueOf(knownBias));
        textViewUnknownBias.setText(String.valueOf(unknownBias));

//        ---------------------------------------Name and Number------------------------------------

        textViewNumber.setText(number);
        textViewName.setText(TextUtils.isEmpty(name) ? number : name);

//        textViewNumber.setText("Anonymous");
//        textViewName.setText("Anonymous");

        textViewDistinctContacts.setText(String.valueOf(distinctContacts+ " Last Date Taken: "+ Instant.ofEpochMilli(LastDayToCount).atZone(ZoneId.systemDefault()).toLocalDateTime()));
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
