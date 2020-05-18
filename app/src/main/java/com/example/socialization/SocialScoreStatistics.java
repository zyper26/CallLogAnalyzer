package com.example.socialization;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.socialization.CallFeatures.CallLogInfo;
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
            textViewGlobalScore1CallCount,textViewGlobalScore1CallDurations,
            textViewIndividualScore1CallCount,textViewIndividualScore1CallDurations,
            textViewGlobalScore3CallCount,
            textViewIndividualScore3CallCount,
            textViewWeekDayBias,textViewWeekEndBias,
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
        textViewGlobalScore1CallCount = findViewById(R.id.textViewGlobalScore1CallCount);
        textViewGlobalScore1CallDurations = findViewById(R.id.textViewGlobalScore1CallDurations);
        textViewIndividualScore1CallCount = findViewById(R.id.textViewIndividualScore1CallCount);
        textViewIndividualScore1CallDurations = findViewById(R.id.textViewIndividualScore1CallDurations);
        textViewGlobalScore3CallCount = findViewById(R.id.textViewGlobalScore3CallCount);
        textViewIndividualScore3CallCount = findViewById(R.id.textViewIndividualScore3CallCount);
        textViewWeekDayBias = findViewById(R.id.textViewWeekdayBias);
        textViewWeekEndBias = findViewById(R.id.textViewWeekendBias);
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

//        --------------------------Distinct Contacts ---------------------
        long distinctContacts = callLogUtils.getTotalDistinctContacts(start_day);
        float TU = callLogUtils.getHMGlobalContacts(start_day);
        float IU = callLogUtils.getHMIndividualContacts(number,start_day);

//        --------------------------------------------------------------

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

//        ------------------------------Assign Scores---------------------------

        SocialScore socialScore = SocialScore.getInstance(this);

        long globalScore1[] = socialScore.getGlobalScore1(start_day);
        long individualScore1[] = socialScore.getIndividualScore1(number,start_day);
//        float globalScore3 = socialScore.getGlobalScore3(start_day);
//        float individualScore3= socialScore.getIndividualScore3(number,start_day);

        float HMTotalUsersPerWeek = socialScore.getHMGlobalPerWeek(start_day);
        float HMIndividualUsersPerWeek =  socialScore.getHMIndividualPerWeek(number,start_day);

        textViewGlobalScore1CallCount.setText(String.valueOf(globalScore1[0]));
        textViewGlobalScore1CallDurations.setText(String.valueOf(HMTotalUsersPerWeek));

        textViewIndividualScore1CallCount.setText(String.valueOf(individualScore1[0]));
        textViewIndividualScore1CallDurations.setText(String.valueOf(HMIndividualUsersPerWeek));

        textViewGlobalScore3CallCount.setText(String.valueOf(TU));
        textViewIndividualScore3CallCount.setText(String.valueOf(IU));

        float[] bias = Biases.getInstance(getApplicationContext()).getPercentageOfBiases(number,start_day);

        textViewWeekDayBias.setText(String.valueOf(bias[0]));
        textViewWeekEndBias.setText(String.valueOf(bias[1]));


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
