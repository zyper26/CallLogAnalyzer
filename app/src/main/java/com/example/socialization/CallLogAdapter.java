package com.example.socialization;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
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

import static com.example.socialization.MainActivity.TAG;


public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>{

    private ArrayList<CallLogInfo> callLogInfoArrayList = new ArrayList<>();
    private Context context;

    public CallLogAdapter(Context context) {
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
        Log.d(TAG, "onBindViewHolder: ");
        switch(Integer.parseInt(callLogInfoArrayList.get(position).getCallType()))
        {
            case CallLog.Calls.OUTGOING_TYPE:
                holder.imageView.setImageResource(R.drawable.ic_outgoing);
                DrawableCompat.setTint(holder.imageView.getDrawable(), ContextCompat.getColor(context, R.color.green));
                break;

            case CallLog.Calls.INCOMING_TYPE:
                holder.imageView.setImageResource(R.drawable.ic_missed);
                DrawableCompat.setTint(holder.imageView.getDrawable(), ContextCompat.getColor(context, R.color.blue));
                holder.textViewCallDuration.setText(Utils.formatSeconds(callLogInfoArrayList.get(position).getDuration()));
                break;

            case CallLog.Calls.MISSED_TYPE:
                holder.imageView.setImageResource(R.drawable.ic_missed);
                DrawableCompat.setTint(holder.imageView.getDrawable(), ContextCompat.getColor(context, R.color.red));
                break;
        }

        if(TextUtils.isEmpty(callLogInfoArrayList.get(position).getName())) {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getNumber());
            StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
            if(callLogInfoArrayList.get(position).getSocialStatus())
                holder.textViewName.setTextColor(Color.RED);

        }
        else {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getName());
            StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
            if(callLogInfoArrayList.get(position).getSocialStatus())
                holder.textViewName.setTextColor(Color.RED);

        }
        holder.textViewCallDuration.setText(Utils.formatSeconds(callLogInfoArrayList.get(position).getDuration()));
        Date dateObj = new Date(callLogInfoArrayList.get(position).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   hh:mm a");
        holder.textViewCallNumber.setText(callLogInfoArrayList.get(position).getNumber());
        holder.textViewCallDate.setText(formatter.format(dateObj));
    }

    public void addCallLog(CallLogInfo callLogInfo){
        callLogInfoArrayList.add(callLogInfo);
    }

    public void addAllCallLog(ArrayList<CallLogInfo> list){
        callLogInfoArrayList.clear();
        callLogInfoArrayList.addAll(list);
    }

    @Override
    public int getItemCount() {
        return callLogInfoArrayList.size();
    }

    class CallLogViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewCallDuration;
        TextView textViewCallDate;
        TextView textViewName;
        TextView textViewCallNumber;

        public CallLogViewHolder(View view) {
            super(view.getRootView());
            imageView = view.findViewById(R.id.imageViewProfile);
            textViewCallDate = view.findViewById(R.id.textViewCallDate);
            textViewCallDuration = view.findViewById(R.id.textViewCallDuration);
            textViewCallNumber = view.findViewById(R.id.textViewCallNumber);
            textViewName = view.findViewById(R.id.textViewName);
        }
    }
}