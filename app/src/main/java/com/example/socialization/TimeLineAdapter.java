package com.example.socialization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialization.CallFeatures.CallLogInfo;
import com.example.socialization.utils.Utils;
import com.github.vipulasri.timelineview.TimelineView;

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

//        holder.textViewCallDuration.setText(Utils.formatSeconds(callLogInfoArrayList.get(position).getDuration()));

        holder.textViewName.setTextColor(R.color.blue);

        if(TextUtils.isEmpty(callLogInfoArrayList.get(position).getName())) {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getNumber());
            holder.textViewNameEnd.setText(callLogInfoArrayList.get(position).getNumber());
            switch(Integer.parseInt(callLogInfoArrayList.get(position).getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    holder.textViewName.setTextColor(R.color.blue);
                    holder.textViewNameEnd.setTextColor(R.color.blue);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    holder.textViewName.setTextColor(R.color.green);
                    holder.textViewNameEnd.setTextColor(R.color.green);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    holder.textViewName.setTextColor(R.color.red);
                    holder.textViewNameEnd.setTextColor(R.color.red);
                    break;
            }
        }
        else {
            holder.textViewName.setText(callLogInfoArrayList.get(position).getName());
            holder.textViewNameEnd.setText(callLogInfoArrayList.get(position).getName());
        }
        holder.textViewCallNumber.setText(callLogInfoArrayList.get(position).getNumber());
        holder.textViewCallNumberEnd.setText(callLogInfoArrayList.get(position).getNumber());

        holder.textViewTime.setText(Utils.getTime(callLogInfoArrayList.get(position).getDate()));
        holder.textViewTimeEnd.setText(Utils.getTime(callLogInfoArrayList.get(position).getDate() + callLogInfoArrayList.get(position).getDuration()*1000));

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
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

        TextView textViewTime,textViewName,textViewCallNumber;
        TextView textViewTimeEnd,textViewNameEnd,textViewCallNumberEnd;
        TimelineView mTimelineView, mTimelineViewEnd;

        CallLogAdapter.OnCallLogItemClickListener onCallLogItemClickListener;

        public TimeLineViewHolder(@NonNull View view, CallLogAdapter.OnCallLogItemClickListener onCallLogItemClickListener){
            super(view.getRootView());
            mTimelineView = view.findViewById(R.id.timeline_view);
            mTimelineViewEnd = view.findViewById(R.id.timeline_view_end);

            textViewTime = view.findViewById(R.id.textViewTime);
            textViewCallNumber = view.findViewById(R.id.textViewCallNumber);
            textViewName = view.findViewById(R.id.textViewName);

            textViewTimeEnd = view.findViewById(R.id.textViewTimeEnd);
            textViewCallNumberEnd = view.findViewById(R.id.textViewCallNumberEnd);
            textViewNameEnd = view.findViewById(R.id.textViewNameEnd);
            this.onCallLogItemClickListener = onCallLogItemClickListener;
//            view.setOnClickListener(this);

        }

    }
}
