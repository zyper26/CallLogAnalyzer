package com.example.socialization.Fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.socialization.R;
import com.example.socialization.TimeLineAdapter;
import com.example.socialization.utils.CallLogUtils;
import com.example.socialization.utils.Utils;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.github.mikephil.charting.formatter.PercentFormatter;
//import com.github.mikephil.charting.utils.ColorTemplate;

public class SocializingOnlineChartFragment extends Fragment {

    private static final String TAG = "SocializingOnlineChartF";
    private View view;
//    private Date date;
    private Spinner spinnerDay,spinnerMonth,spinnerYear;
    private Button submitButton;
    RecyclerView recyclerView;
    TimeLineAdapter adapter;
    String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.timeline_log_fragment,container,false);

        spinnerDay = view.findViewById(R.id.spinnerDay);
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        spinnerAdapter();

        submitButton = view.findViewById(R.id.dateSelected);

        if(LocalDateTime.now().getMonthValue()<10) {
            if(LocalDateTime.now().getDayOfMonth()<10)
                date = LocalDateTime.now().getYear()+"-0" +LocalDateTime.now().getMonthValue()+"-0"+LocalDateTime.now().getDayOfMonth() +" 12:30";
            else if(LocalDateTime.now().getDayOfMonth()>9){
                date = LocalDateTime.now().getYear()+"-0" +LocalDateTime.now().getMonthValue()+"-"+LocalDateTime.now().getDayOfMonth() +" 12:30";
            }
        }
        else if(LocalDateTime.now().getMonthValue()>9){
            if(LocalDateTime.now().getDayOfMonth()<10)
                date = LocalDateTime.now().getYear()+"-" +LocalDateTime.now().getMonthValue()+"-0"+LocalDateTime.now().getDayOfMonth() +" 12:30";
            else if(LocalDateTime.now().getDayOfMonth()>9){
                date = LocalDateTime.now().getYear()+"-" +LocalDateTime.now().getMonthValue()+"-"+LocalDateTime.now().getDayOfMonth() +" 12:30";
            }
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        initComponents();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
                initComponents();
            }
        });

        return view;
    }

    public void updateDate(){
        date = spinnerYear.getSelectedItem().toString() + "-" + spinnerMonth.getSelectedItem().toString() + "-" + spinnerDay.getSelectedItem().toString() + " 12:30";
    }

    public void spinnerAdapter(){
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.day_array, R.layout.support_simple_spinner_dropdown_item);
        dayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        int spinnerDayPosition = dayAdapter.getPosition(String.valueOf(LocalDateTime.now().getDayOfMonth()));
        spinnerDay.setAdapter(dayAdapter);
        spinnerDay.setSelection(spinnerDayPosition);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.month_array, R.layout.support_simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        int spinnerMonthPosition = monthAdapter.getPosition(String.valueOf(LocalDateTime.now().getMonthValue()));
        spinnerMonth.setAdapter(monthAdapter);
        spinnerDay.setSelection(spinnerMonthPosition);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.year_array, R.layout.support_simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        int spinnerYearPosition = yearAdapter.getPosition(String.valueOf(LocalDateTime.now().getYear()));
        spinnerYear.setAdapter(yearAdapter);
        spinnerDay.setSelection(spinnerYearPosition);
    }


    public void initComponents(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TimeLineAdapter(getContext());
        recyclerView.setAdapter(adapter);
        loadData();
    }

    public void loadData(){
        CallLogUtils callLogUtils = CallLogUtils.getInstance(getContext());
        adapter.addAllCallLog(callLogUtils.getSocialCallsOnDate(Utils.getLocalDateTimeFromString(date)));
        adapter.notifyDataSetChanged();
    }
}
