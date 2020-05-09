package com.example.socialization;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialization.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;


public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>{

    private ArrayList<CallLogInfo> callLogInfoArrayList;
    private Context context;
    private ImageView imageView;
    private TextView textViewCallDuration, textViewCallDate, textViewName, textViewCallNumber;


    public CallLogAdapter(Context context){
        callLogInfoArrayList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row,parent,false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        holder.bind(callLogInfoArrayList.get(position));
    }

    public void addCallLog(CallLogInfo callLogInfo){
        callLogInfoArrayList.add(callLogInfo);
    }

    public void addAllCallLog(ArrayList<CallLogInfo> list){
        callLogInfoArrayList.clear();
        callLogInfoArrayList.addAll(list);
//        Set<CallLogInfo> hash_Set = new HashSet<CallLogInfo>();
//        for(CallLogInfo callLogInfo: callLogInfoArrayList){
//            hash_Set.add(callLogInfo);
//        }
//        System.out.println("LoadData_main_list: "+hash_Set.size() + " " +callLogInfoArrayList.size());
    }

    @Override
    public int getItemCount() {
        return callLogInfoArrayList.size();
    }

    class CallLogViewHolder extends RecyclerView.ViewHolder{

        public CallLogViewHolder(View view) {
            super(view.getRootView());
            imageView = view.findViewById(R.id.imageViewProfile);
            textViewCallDate = view.findViewById(R.id.textViewCallDate);
            textViewCallDuration = view.findViewById(R.id.textViewCallDuration);
            textViewCallNumber = view.findViewById(R.id.textViewCallNumber);
            textViewName = view.findViewById(R.id.textViewName);
        }

        public void bind(final CallLogInfo callLog){
            switch(Integer.parseInt(callLog.getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    imageView.setImageResource(R.drawable.ic_outgoing);
                    DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.green));
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    imageView.setImageResource(R.drawable.ic_missed);
                    DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.blue));
                    textViewCallDuration.setText(Utils.formatSeconds(callLog.getDuration()));
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    imageView.setImageResource(R.drawable.ic_missed);
                    DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.red));
                    break;
            }

            if(TextUtils.isEmpty(callLog.getName())) {
                textViewName.setText(callLog.getNumber());
                StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
                if(callLog.getSocialStatus())
                    textViewName.setTextColor(Color.RED);

            }
            else {
                textViewName.setText(callLog.getName());
                StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
                if(callLog.getSocialStatus())
                    textViewName.setTextColor(Color.RED);

            }
            textViewCallDuration.setText(Utils.formatSeconds(callLog.getDuration()));
            Date dateObj = new Date(callLog.getDate());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   hh:mm a");
            textViewCallNumber.setText(callLog.getNumber());
            textViewCallDate.setText(formatter.format(dateObj));
        }
    }
}