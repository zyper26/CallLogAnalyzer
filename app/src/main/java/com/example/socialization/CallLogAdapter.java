package com.example.socialization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.SocializationOnline.SocialScoreStatistics;
import com.example.socialization.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;


public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>{

    private ArrayList<CallLogInfo> callLogInfoArrayList = new ArrayList<>();
    private Context context;
    OnCallLogItemClickListener onItemClickListener;

    interface OnCallLogItemClickListener {
        void onItemClicked(CallLogInfo callLogInfo);
    }

    public CallLogAdapter(Context context) {
        callLogInfoArrayList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row,parent,false);
        return new CallLogViewHolder(view, onItemClickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: ");
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
        holder.textViewName.setTextColor(R.color.blue);
        if(TextUtils.isEmpty(callLogInfoArrayList.get(position).getName())) {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getNumber());
//            StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
//            if(statisticsFragment.getSocial(callLogInfoArrayList.get(position).getNumber(), callLogInfoArrayList.get(position).getDate()))
            if(callLogInfoArrayList.get(position).getSocialStatus()) {
                holder.textViewName.setTextColor(Color.RED);
                //Log.d(TAG, "onBindViewHolder: "+ callLogInfoArrayList.get(position).getName() + " " + callLogInfoArrayList.get(position).getSocialStatus() + " " + position);
            }
        }
        else {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getName());
//            StatisticsFragment statisticsFragment = StatisticsFragment.getInstance(context);
//            if(statisticsFragment.getSocial(callLogInfoArrayList.get(position).getNumber(), callLogInfoArrayList.get(position).getDate()))
            if(callLogInfoArrayList.get(position).getSocialStatus()){
                holder.textViewName.setTextColor(Color.RED);
                //Log.d(TAG, "onBindViewHolder: "+ callLogInfoArrayList.get(position).getName() + " " + callLogInfoArrayList.get(position).getSocialStatus() + " " + position);
            }

        }
        //Log.d(TAG, "onBindViewHolder: "+ callLogInfoArrayList.get(position).getName() + " " + callLogInfoArrayList.get(position).getSocialStatus() + " " + position);
        holder.textViewCallDuration.setText(Utils.formatSeconds(callLogInfoArrayList.get(position).getDuration()));
        Date dateObj = new Date(callLogInfoArrayList.get(position).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy   hh:mm a");
        holder.textViewCallNumber.setText(callLogInfoArrayList.get(position).getNumber());
        holder.textViewCallDate.setText(formatter.format(dateObj));
        holder.textViewPosition.setText(String.valueOf(position));
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

    class CallLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewCallDuration;
        TextView textViewCallDate;
        TextView textViewName;
        TextView textViewCallNumber;
        TextView textViewPosition;
        OnCallLogItemClickListener onCallLogItemClickListener;

        public CallLogViewHolder(@NonNull View view, OnCallLogItemClickListener onCallLogItemClickListener){
            super(view.getRootView());
            imageView = view.findViewById(R.id.imageViewProfile);
            textViewCallDate = view.findViewById(R.id.textViewCallDate);
            textViewCallDuration = view.findViewById(R.id.textViewCallDuration);
            textViewCallNumber = view.findViewById(R.id.textViewCallNumber);
            textViewName = view.findViewById(R.id.textViewName);
            textViewPosition = view.findViewById(R.id.textViewPosition);
            this.onCallLogItemClickListener = onCallLogItemClickListener;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SocialScoreStatistics.class);
            intent.putExtra("number",callLogInfoArrayList.get(getAdapterPosition()).getNumber());
            intent.putExtra("name",callLogInfoArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("date",callLogInfoArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("duration",callLogInfoArrayList.get(getAdapterPosition()).getDuration());
            context.startActivity(intent);
        }
    }
}