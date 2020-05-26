package com.example.socialization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private ArrayList<CallLogInfo> callLogInfoArrayList = new ArrayList<>();
    private Context context;
    CallLogAdapter.OnCallLogItemClickListener onItemClickListener;

    interface OnCallLogItemClickListener {
        void onItemClicked(CallLogInfo callLogInfo);
    }

    public TimeLineAdapter(Context context) {
        callLogInfoArrayList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineAdapter.TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.socializing_score_timeline,parent,false);
        return new TimeLineAdapter.TimeLineViewHolder(view, onItemClickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TimeLineAdapter.TimeLineViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: ");

        holder.textViewTime.setText(Utils.getTime(callLogInfoArrayList.get(position).getDate()));

//        holder.textViewCallDuration.setText(Utils.formatSeconds(callLogInfoArrayList.get(position).getDuration()));

        holder.textViewName.setTextColor(R.color.blue);

        if(TextUtils.isEmpty(callLogInfoArrayList.get(position).getName())) {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getNumber());
            switch(Integer.parseInt(callLogInfoArrayList.get(position).getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    holder.textViewName.setTextColor(R.color.blue);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    holder.textViewName.setTextColor(Color.GREEN);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    holder.textViewName.setTextColor(Color.RED);
                    break;
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
        holder.textViewCallNumber.setText(callLogInfoArrayList.get(position).getNumber());

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

    class TimeLineViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTime;
        TextView textViewName;
        TextView textViewCallNumber;
        CallLogAdapter.OnCallLogItemClickListener onCallLogItemClickListener;

        public TimeLineViewHolder(@NonNull View view, CallLogAdapter.OnCallLogItemClickListener onCallLogItemClickListener){
            super(view.getRootView());
            textViewTime = view.findViewById(R.id.textViewTime);
            textViewCallNumber = view.findViewById(R.id.textViewCallNumber);
            textViewName = view.findViewById(R.id.textViewName);
            this.onCallLogItemClickListener = onCallLogItemClickListener;
//            view.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(context, SocialScoreStatistics.class);
//            context.startActivity(intent);
//        }

//        @Override
//        public void onClick(View view) {
////            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, SocialScoreStatistics.class);
//            intent.putExtra("number",callLogInfoArrayList.get(getAdapterPosition()).getNumber());
//            intent.putExtra("name",callLogInfoArrayList.get(getAdapterPosition()).getName());
//            intent.putExtra("date",callLogInfoArrayList.get(getAdapterPosition()).getDate());
//            intent.putExtra("duration",callLogInfoArrayList.get(getAdapterPosition()).getDuration());
//            context.startActivity(intent);
//        }
    }
}
