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
    TextView textViewWeek1Frequency, textViewWeek1Duration,
            textViewWeek2Frequency, textViewWeek2Duration,
            textViewWeek3Frequency, textViewWeek3Duration,
            textViewWeek4Frequency, textViewWeek4Duration,
            textViewWeek5Frequency, textViewWeek5Duration,
            textViewWeek6Frequency, textViewWeek6Duration,
            textViewWeek7Frequency, textViewWeek7Duration,
            textViewWeek8Frequency, textViewWeek8Duration,

            textViewIndividualScorePerWeekCallDurations, textViewHMTotalCallDurations,
            textViewWeekDayBias,textViewWeekEndBias,
            textViewKnownBias,textViewUnknownBias,

            textViewSocialPercentage,
            textViewNumber,textViewName,textViewDistinctContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_score_statistics);
        textViewWeek1Frequency = findViewById(R.id.textViewWeek1Frequency);
        textViewWeek1Duration = findViewById(R.id.textViewWeek1Duration);
        textViewWeek2Frequency = findViewById(R.id.textViewWeek2Frequency);
        textViewWeek2Duration = findViewById(R.id.textViewWeek2Duration);
        textViewWeek3Frequency = findViewById(R.id.textViewWeek3Frequency);
        textViewWeek3Duration = findViewById(R.id.textViewWeek3Duration);
        textViewWeek4Frequency = findViewById(R.id.textViewWeek4Frequency);
        textViewWeek4Duration = findViewById(R.id.textViewWeek4Duration);
        textViewWeek5Frequency = findViewById(R.id.textViewWeek5Frequency);
        textViewWeek5Duration = findViewById(R.id.textViewWeek5Duration);
        textViewWeek6Frequency = findViewById(R.id.textViewWeek6Frequency);
        textViewWeek6Duration = findViewById(R.id.textViewWeek6Duration);
        textViewWeek7Frequency = findViewById(R.id.textViewWeek7Frequency);
        textViewWeek7Duration = findViewById(R.id.textViewWeek7Duration);
        textViewWeek8Frequency = findViewById(R.id.textViewWeek8Frequency);
        textViewWeek8Duration = findViewById(R.id.textViewWeek8Duration);
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

        long numberOfWeeks = callLogUtils.getTotalNumberOfWeeks(callLogInfo.getDate());
        float[] AllValues = new float[16];
        float[] WeekValues;
        int k=0;
        for (int j=1; j<=numberOfWeeks; j++){
            WeekValues = callLogUtils.getHMIndividualContactsPerWeek(callLogInfo.getNumber(), j, callLogInfo.getDate());
            AllValues[k] = WeekValues[0];
            AllValues[k+1] = WeekValues[1];
            k+=2;
        }
        textViewWeek1Frequency.setText(String.valueOf(AllValues[0]));
        textViewWeek1Duration.setText(String.valueOf(AllValues[1]));
        textViewWeek2Frequency.setText(String.valueOf(AllValues[2]));
        textViewWeek2Duration.setText(String.valueOf(AllValues[3]));
        textViewWeek3Frequency.setText(String.valueOf(AllValues[4]));
        textViewWeek3Duration.setText(String.valueOf(AllValues[5]));
        textViewWeek4Frequency.setText(String.valueOf(AllValues[6]));
        textViewWeek4Duration.setText(String.valueOf(AllValues[7]));
        textViewWeek5Frequency.setText(String.valueOf(AllValues[8]));
        textViewWeek5Duration.setText(String.valueOf(AllValues[9]));
        textViewWeek6Frequency.setText(String.valueOf(AllValues[10]));
        textViewWeek6Duration.setText(String.valueOf(AllValues[11]));
        textViewWeek7Frequency.setText(String.valueOf(AllValues[12]));
        textViewWeek7Duration.setText(String.valueOf(AllValues[13]));
        textViewWeek8Frequency.setText(String.valueOf(AllValues[14]));
        textViewWeek8Duration.setText(String.valueOf(AllValues[15]));


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
